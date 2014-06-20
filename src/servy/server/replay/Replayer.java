/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servy.server.replay;

import java.net.Socket;
import servy.server.http.HttpRequest;

/**
 *
 * @author Acer
 */
public abstract class Replayer implements Runnable {
    protected Socket client;
    protected Class<?> handler;
    protected HttpRequest request;
    
    public Replayer(Socket client, Class<?> handler, HttpRequest request) {
        this.client  = client;
        this.handler = handler;
        this.request = request;
    }
}
