/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servy.test;

import servy.server.websockets.WebSocket;
import servy.server.replay.ReplayHandlerForWebsockets;

/**
 *
 * @author Acer
 */
public class HandlerForAnyWS extends ReplayHandlerForWebsockets {
    public static WebSocket A = null;
    public static WebSocket B = null;
    public WebSocket me = null;
    public int who = -1;
    
    @Override
    public void OnOpen(WebSocket client) {
        if(A == null) {
            A   = client;
            me  = A;
            who = 1;
        } else {
            B   = client;
            me  = B;
            who = 2;
        }
        System.out.println("I am ws " + who);
    }

    @Override
    public void OnClose() {
        System.out.println(who + " got closed?" + me.IsOpen());
    }

    @Override
    public String OnMessage(String message) {
        try {
            System.out.println(who + " << " + message);
            String echo = "from " + who + " {" + ((String)message).toUpperCase() + "}";
            WebSocket other = (who == 1) ? B : A;
            if(other != null) {
                other.Send(echo);
            }
            System.out.println(who + " >> " + echo);
        } catch(Exception e)
        {
        }
        return "";
    }
    
}
