/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servy.server.websockets;

/**
 *
 * @author Acer
 */
public class WebSocket {
    private final ServerWebSocket wsocket;
    
    /*
     * Constructor.
     */
    public WebSocket(ServerWebSocket wsocket) {
        this.wsocket = wsocket;
    }
    
    /*
     * Indica si el websocket esta abierto.
     */
    public boolean IsOpen() {
        return wsocket.IsOpen();
    }
    
    /*
     * Envia texto por el canal de salida.
     */
    public void Send(String message) throws Exception {
        wsocket.SendText(message);
    }
}
