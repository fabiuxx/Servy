/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servy.server.http;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Acer
 */
public class HttpHeaderCollection {
    protected Map<String, String> headers;
    
    public HttpHeaderCollection() {
        headers = new HashMap<String, String>();
    }
    
    public void SetHeader(String name, String value) {
        headers.put(name, value);
    }
    
    public String GetHeader(String name) {
        if(ContainsHeader(name)) {
            return headers.get(name);
        }
        return null;
    }
    
    public Map<String, String> GetHeaders() {
        return headers;
    }
    
    public boolean ContainsHeader(String name) {
        return headers.containsKey(name);
    }
    
    public boolean ContainsHeaders(String...names) {
        for(String name : names) {
            if(! ContainsHeader(name)) {
                return false;
            }
        }
        return true;
    }
}
