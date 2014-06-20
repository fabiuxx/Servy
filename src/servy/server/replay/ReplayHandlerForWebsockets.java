/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servy.server.replay;

import servy.server.websockets.WebSocket;

/**
 *
 * @author Acer
 */
public abstract class ReplayHandlerForWebsockets {
    public abstract void   OnOpen(WebSocket client);
    public abstract void   OnClose();
    public abstract String OnMessage(String message);
}
