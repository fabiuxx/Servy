/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servy.server.replay;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import servy.server.ServerDefaultResponses;
import servy.server.http.HttpRequest;
import servy.server.websockets.ServerWebSocket;
import servy.server.websockets.WebSocketOpcode;

/**
 *
 * @author Acer
 */
public class ReplayerForWebsockets extends Replayer {
    private InputStream reader;
    private PrintWriter writer;
    
    public ReplayerForWebsockets(Socket client, Class<?> handler, HttpRequest request) throws Exception {
        super(client, handler, request);
        this.reader = client.getInputStream();
        this.writer = new PrintWriter(client.getOutputStream());
    }
    
    @Override
    public void run() {
        try {
            // La peticion debe ser de cambio al protocolo websockets.
            if(CheckIfWebSockets(request)) {
                // Se crea un objeto websocket para esta conexion.
                String key = request.GetHeader("Sec-WebSocket-Key");
                ServerWebSocket wsocket = new ServerWebSocket(client, key);
                // Se debe aprobar el handshake.
                if(wsocket.Handshake()) {
                    // Se crea una instancia para el handler.
                    ReplayHandlerForWebsockets handler_instance = (ReplayHandlerForWebsockets)handler.newInstance();
                    handler_instance.OnOpen(wsocket.WebSocket());
                    // Bucle para interactuar con el cliente.
                    while(wsocket.IsOpen()) {
                        // Se espera algun mensaje desde el cliente.
                        Object o = wsocket.ReadFrame();
                        if(o instanceof String) {
                            // Ocurrio un evento de entrada de texto. De momento
                            // solo se puede recibir texto desde los clientes.
                            handler_instance.OnMessage((String)o);
                        } else if(o instanceof Integer) {
                            // Ocurrio un evento de control.
                            int opcode = (int)o;
                            switch(opcode) {
                                case WebSocketOpcode.CLOSE:
                                    // El handler debe responder al evento de fin de conexion.
                                    handler_instance.OnClose();
                                    break;
                                default:
                                    // De momento no se responde a otros opcodes.
                                    ;
                            }
                        } else {
                            break;
                        }
                    }
                } else {
                    throw new Exception("WebSocket handshake failed.");
                }
            } else {
                throw new Exception("WebSocket handler can not replay the request.");
            }
        } catch (Exception e) {
            String E500 = ServerDefaultResponses.ERROR500(e.getLocalizedMessage());
            WriteAndCloseConnection(E500);
        }
    }
    
    /*
     * Verifica si la peticion es efectivamente una solicitud de
     * cambio al protocolo websockets.
     */
    private boolean CheckIfWebSockets(HttpRequest request) {
        if(request.ContainsHeaders("Upgrade", "Connection")) {
            String ws = request.GetHeader("Upgrade").toLowerCase();
            String up = request.GetHeader("Connection").toLowerCase();
            return (ws.equals("websocket") && up.equals("upgrade"));
        }
        return false;
    }
    
    /*
     * Escribe en el flujo de entrada del cliente y cierra la conexion.
     */
    private void WriteAndCloseConnection(String message) {
        writer.print(message);
        writer.flush();
        writer.close();
    }
}