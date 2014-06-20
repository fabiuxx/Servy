/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servy.server.websockets;

public class WebSocketOpcode {
    public static final int CONTINUATION = 0x00;
    public static final int TEXT         = 0x01;
    public static final int BINNARY      = 0x02;
    public static final int CLOSE        = 0x08;
    public static final int PING         = 0x09;
    public static final int PONG         = 0x0A;
}
