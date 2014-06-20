/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servy.server;

import servy.server.dispatch.HttpDispatcher;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import servy.utils.Logger;

/**
 *
 * @author Acer
 */
public class HttpServer {
    private static final Logger log = Logger.GetLogger();
    public static final int PROTOCOL_HTTP_1_1  = 0x10;
    public static final int PROTOCOL_WEBSOCKET = 0x11;
    public static final int CR = 0x0D;
    public static final int LF = 0x0A;
    public static final String LINE_SEPARATOR = "" + CR + LF;
    private HttpDispatcher dispatcher;
    private ServerSocket socket;
    private String port;
    
    public HttpServer(String port) throws IOException {
        this.port       = port;
        this.socket     = new ServerSocket(Integer.parseInt(port));
        this.dispatcher = new HttpDispatcher();
    }
    
    public void AddHandler(String url, Class<?> klass) {
        dispatcher.AddUrlHandler(url, klass);
    }
    
    public void Serve() throws Exception {
        log.d("server is up ...");
        while(true) {
            Socket client = socket.accept();
            ServerThread worker = new ServerThread(client, dispatcher);
            HttpServerThreadManager.Run(worker);
        }
    }
}
