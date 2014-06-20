package servy.test;

import servy.server.HttpServer;
import servy.utils.Logger;

public class Servy {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        Logger.Filter(Logger.LogLevel.DEBUG);
        HttpServer server = new HttpServer("7070");
        server.AddHandler("/asd/fua", HandlerForAnyHTTP.class);
        server.AddHandler("/{lol}/fua/{hi}", HandlerForAnyHTTP.class);
        server.AddHandler("/index", HandlerForAnyWS.class);
        server.Serve();
    }
}
