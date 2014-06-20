/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servy.server.replay;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import servy.server.ServerDefaultResponses;
import servy.server.http.HttpRequest;
import servy.server.http.HttpResponse;

/**
 *
 * @author Acer
 */
public class ReplayerForHttp extends Replayer {
    private BufferedReader reader;
    private PrintWriter writer;
    
    public ReplayerForHttp(Socket client, Class<?> handler, HttpRequest request) throws Exception {
        super(client, handler, request);
        this.reader  = new BufferedReader(new InputStreamReader(client.getInputStream()));
        this.writer  = new PrintWriter(client.getOutputStream());
    }

    /*
     * Logica principal.
     */
    @Override
    public void run() {
        try {
            ReplayHandlerForHttp handler_instance = (ReplayHandlerForHttp)(this.handler.newInstance());
            if(handler_instance != null) {
                // Se crea un nuevo objeto de respuesta.
                HttpResponse response = new HttpResponse();
                if(request.IsMethod("GET")) {
                    handler_instance.OnGET(request, response);
                } else if(request.IsMethod("POST")) {
                    handler_instance.OnPOST(request, response);
                }
                // Se envia la respuesta al cliente.
                response.SetHeader("Connection", "close");
                String replay = response.Pack();
                WriteAndCloseConnection(replay);
            }
        } catch (Exception e) {
            String E500 = ServerDefaultResponses.ERROR500(e.getLocalizedMessage());
            WriteAndCloseConnection(E500);
        }
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
