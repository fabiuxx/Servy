/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servy.server.http;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Acer
 */
public class HttpPayload {
    private List<Byte> bytes;
    
    public HttpPayload() {
        this(new ArrayList<Byte>());
    }
    
    public HttpPayload(List<Byte> bytes) {
        this.bytes = bytes;
    }
    
    public void AddByte(byte value) {
        bytes.add(value);
    }
    
    public byte[] GetBytes() {
        byte[] _bytes = new byte[bytes.size()];
        int i = 0;
        for(Byte b : bytes) {
            _bytes[i ++] = b;
        }
        return _bytes;
    }
    
    public int GetTotalBytes() {
        return bytes.size();
    }
}
