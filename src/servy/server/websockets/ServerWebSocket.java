/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servy.server.websockets;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import servy.utils.SHA1;

/**
 *
 * @author Acer
 */
public class ServerWebSocket {
    private static final int OPCODE_MASK      = 0x0F;
    private static final int MORE_FRAMES_MASK = 0x80;
    private static final int HAS_DATA_MASK    = 0x80;
    private Socket client;
    private String key;
    private boolean open;
    private InputStream reader;
    private OutputStream writer;
    
    /*
     * Cnstructor.
     */
    public ServerWebSocket(Socket client, String key) throws Exception {
        this.client = client;
        this.key    = key;
        this.open   = true;
        this.reader = client.getInputStream();
        this.writer = client.getOutputStream();
    }
    
    /*
     * Lee datos provenientes del cliente. Retorna una cadena en caso de que se
     * haya identificador un frame de texto, caso contrario retorna un entero.
     */
    public Object ReadFrame() throws Exception {
        // Leemos el byte de cabecera del frame.
        Object ret = null;
        int read   = reader.read();
        int opcode = read & OPCODE_MASK;
        // Realizamos una accion de acuerdo al opcode.
        switch(opcode) {
            case WebSocketOpcode.TEXT:
                ret = OpcodeTextOperation();
                break;
            case WebSocketOpcode.CLOSE:
                ret = OpcodeCloseOperation();
                break;
            default:
                System.out.println("dafuq? " + opcode);
        }
        return ret;
    }
    
    /*
     * Envia datos de texto al cliente.
     */
    public void SendText(String message) throws Exception {
        final ByteArrayOutputStream data = new ByteArrayOutputStream();
        final DataOutputStream stream = new DataOutputStream(data);
        final int len = message.length();
        stream.writeByte(129);
        if(len <= 125) {
            stream.writeByte(len);
        } else if(len >= 126 && len <= 65535) {
            stream.writeByte(126);
            stream.writeShort(len);
        } else {
            stream.writeByte(127);
            stream.writeLong(len);
        }
        stream.write(message.getBytes("UTF8"));
        byte[] reply = data.toByteArray();
        SendBytes(reply);
    }
    
    /*
     * Realiza el proceso de handshake con el cliente.
     */
    public boolean Handshake() {
        try {
            // Se genera la clave para la aceptacion del handshake.
            String accept_key = ServerWebSocket.GetHandshakeKey(this.key);
            // Envio del handshake.
            StringBuilder sb = new StringBuilder();
            sb.append("HTTP/1.1 101 Switching Protocols").append("\r\n");
            sb.append("Upgrade: WebSocket").append("\r\n");
            sb.append("Connection: Upgrade").append("\r\n");
            sb.append("Sec-WebSocket-Accept: ").append(accept_key).append("\r\n\r\n");
            writer.write(sb.toString().getBytes("UTF8"));
            writer.flush();
            return true;
        } catch(Exception e) {
            return false;
        }
    }
    
    /*
     * Indica si el websocket esta abierto.
     */
    public boolean IsOpen() {
        return open;
    }
    
    /*
     * Genera una clave para la aceptacion del handshake.
     */
    public static String GetHandshakeKey(String key) throws Exception {
        // Construccion de la clave de respuesta.
        key = key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
        key = new String(SHA1.digest(key), "ISO-8859-1");
        key = servy.utils.Base64.Encode(key);
        return key;
    }
    
    /*
     * Calcula la cantidad de bytes de datos esperados en el frame.
     */
    private int GetFramePayloadLength(int len, boolean encoded) throws Exception {
        if(encoded) {
            len -= 128;
        }	
        if(len == 127) {
            len = (reader.read() << 16) | (reader.read() << 8) | reader.read();
	}
        else if(len == 126) {
            len = (reader.read() << 8) | reader.read();
        }
        return len;
    }
    
    /*
     * Lee la clave utilizada para enmascarar el frame.
     */
    private byte[] GetFrameKey(boolean encoded) throws Exception {
        if(encoded) {
            return ReadBytes(4);
        }
        return null;
    }
    
    /*
     * Lee los datos del frame del cliente.
     */
    private byte[] GetFramePayload(int len, byte[] key) throws Exception {
        byte[] data = ReadBytes(len);
        if(key != null) {
            for(int i=0; i<len; i++) {
                data[i] = (byte)(data[i] ^ key[i % 4]);
            }
        }
        return data;
    }
    
    /*
     * Lee una cantidad definida de bytes desde el cliente.
     */
    private byte[] ReadBytes(int len) throws Exception {
        byte[] bytes = new byte[len];
        int i = 0;
        while(i < len) {
            int c = reader.read();
            if(c < 0) {
                break;
            }
            bytes[i] = (byte)c;
            i ++;
        }
        return bytes;
    }
    
    /*
     * Envia una cantidad de bytes al cliente.
     */
    private void SendBytes(byte[] bytes) throws Exception {
        writer.write(bytes);
        writer.flush();
    }
    
    /*
     * Realiza una operacion de lectura de texto.
     */
    private String OpcodeTextOperation() throws Exception {
        int len = reader.read();
        boolean encoded = (len >= 128);
        len = GetFramePayloadLength(len, encoded);
        byte[] fkey = GetFrameKey(encoded);
        byte[] data = GetFramePayload(len, fkey);
        String msg = new String(data, "UTF8");
        return msg;
    }
    
    /*
     * Realiza una operacion de finalizacion de conexion.
     */
    private int OpcodeCloseOperation() throws Exception {
        writer.flush();
        writer.close();
        open = false;
        return WebSocketOpcode.CLOSE;
    }
    
    /*
     * Crea un objeto que representa a este mismo objeto. Pero
     * con capacidades mas limitadas.
     */
    public WebSocket WebSocket() {
        return new WebSocket(this);
    }
}
