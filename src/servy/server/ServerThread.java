/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servy.server;

import servy.server.dispatch.HttpDispatcher;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import servy.server.http.HttpPayload;
import servy.server.http.HttpRequest;
import servy.server.http.HttpParser;
import servy.server.replay.ReplayHandlerForHttp;
import servy.server.replay.ReplayHandlerForWebsockets;
import servy.server.replay.ReplayerForHttp;
import servy.server.replay.Replayer;
import servy.server.replay.ReplayerForWebsockets;
import servy.utils.Logger;

/**
 *
 * @author Acer
 */
public class ServerThread implements Runnable {
    private static final Logger log = Logger.GetLogger();
    private Socket client;
    private BufferedReader reader;
    private PrintWriter writer;
    private final HttpDispatcher dispatcher;
    
    /*
     * Constructor.
     */
    public ServerThread(Socket client, HttpDispatcher serverDispatcher) throws IOException {
        this.client = client;
        this.reader = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
        this.writer = new PrintWriter(client.getOutputStream());
        this.dispatcher = serverDispatcher;
    }
    
    /*
     * Logica principal del hilo.
     */
    @Override
    public void run() {
        try {            
            // Leemos el cuerpo de la peticion HTTP.
            String body = ReadRequestBody();
            HttpRequest request = HttpParser.ParseRequestBody(body);            
            // Si existen las siguientes cabeceras, significa que hay datos adjuntos en la peticion.
            if(request.ContainsHeaders("Content-Length", "Content-Type")) {
                // La cantidad de datos a leer se extrae de la cabecera Content-Length.
                int total_bytes = Integer.parseInt(request.GetHeader("Content-Length"));
                // Se leen los bytes adjuntos en la peticion.
                HttpPayload payload = ReadRequestPayload(total_bytes);
                request.SetPayload(payload);
            }
            // Recurso solicitado.
            String resource = request.GetResource();
            // Las partes resueltas de la URL se agregan como parametros.
            request.AddParamaters(dispatcher.GetUrlMatches(resource));
            // Se busca un handler para el recurso.
            Class<?> handler_class = dispatcher.GetUrlHandler(resource);
            if(handler_class != null) {
                // Se identifica el tipo de handler asciado.
                Replayer replayer = null;
                if(handler_class.getSuperclass().equals(ReplayHandlerForHttp.class)) {
                    // Se tratara la conexion como una peticion normal HTTP.
                    replayer = new ReplayerForHttp(client, handler_class, request);
                } else if(handler_class.getSuperclass().equals(ReplayHandlerForWebsockets.class)) {
                    // Se tratara la conexion bajo el protocolo de websockets.
                    replayer = new ReplayerForWebsockets(client, handler_class, request);
                } else {
                    // El handler no esta soportado.
                    throw new Exception("Unsupported Replayer type: " + handler_class.getName());
                }
                // Se genera la respuesta en segundo plano.
                HttpServerThreadManager.Run(replayer);
            } else {
                // El recurso solicitado no existe para el servidor.
                String E404 = ServerDefaultResponses.ERROR404(resource);
                WriteAndCloseConnection(E404);
            }
        } catch (Exception e) {
            // Error interno en el servidor.
            String E500 = ServerDefaultResponses.ERROR500(e.getLocalizedMessage());
            WriteAndCloseConnection(E500);
        }
    }
    
    /*
     * Se encarga de leer la primera parte de la peticion. (Comando + Encabezados)
     */
    private String ReadRequestBody() throws IOException {
        StringBuilder builder = new StringBuilder();
        int c, new_lines = 0;
        // Leemos los bytes del cliente.
        while(true) {
            c = reader.read();
            if(c < 0) {
                break;
            }
            builder.append((char)c);
            if(c == HttpServer.CR) {
                c = reader.read();
                if(c < 0) {
                    break;
                }
                builder.append((char)c);
                if(c == HttpServer.LF) {
                    new_lines ++;
                } else {
                    new_lines = 0;
                }
            } else {
                new_lines = 0;
            }
            // El cuerpo de la peticion termina con la secuencia \r\n\r\n.
            if(new_lines == 2) {
                break;
            }
        }
        return builder.toString();
    }
    
    /*
     * Se encarga de leer los bytes del contenido enviado por el cliente.
     */
    private HttpPayload ReadRequestPayload(int length) throws IOException {
        HttpPayload payload = new HttpPayload();
        int read;
        while(length != 0 && (read = reader.read()) >= 0) {
            char c = (char)read;
            payload.AddByte((byte)read);
            length --;
        }
        return payload;
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
