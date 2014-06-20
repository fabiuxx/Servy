/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servy.server.http;

import java.nio.charset.Charset;

/**
 *
 * @author Acer
 */
public class HttpResponse extends HttpHeaderCollection {
    private int status;
    private HttpPayload payload;
    
    public HttpResponse() {
        this(200, new HttpPayload());
    }
    
    public HttpResponse(int status, HttpPayload payload) {
        this.status  = status;
        this.payload = payload;
    }
    
    public void SetStatusCode(int status) {
        this.status = status;
    }
    
    public int GetStatusCode() {
        return status;
    }
    
    public HttpResponse AppendPayload(String string) {
        byte[] bytes = string.getBytes(Charset.forName("UTF-8"));
        return AppendPayload(bytes);
    }
    
    public HttpResponse AppendPayload(byte[] bytes) {
        for(byte b : bytes) {
            payload.AddByte(b);
        }
        return this;
    }
    
    public String Pack() {
        StringBuilder builder = new StringBuilder();
        builder.append(HttpStatus.GetStatusLineForCode(status)).append("\r\n");
        for(String header : headers.keySet()) {
            builder.append(header).append(": ").append(GetHeader(header)).append("\r\n");
        }
        builder.append("\r\n");
        byte[] bytes = payload.GetBytes();
        for(byte b : bytes) {
            builder.append((char)b);
        }
        return builder.toString();
    }
}
