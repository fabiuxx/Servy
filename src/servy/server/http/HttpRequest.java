/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servy.server.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Acer
 */
public class HttpRequest extends HttpHeaderCollection {
    private String method;
    private String resource;
    private Map<String, String> params;
    private HttpPayload payload;
    
    public HttpRequest() {
        this("", "", new HashMap<String, String>(), new HttpPayload());
    }
    
    public HttpRequest(String method, String resource, Map<String, String> params, HttpPayload payload) {
        this.method   = method;
        this.resource = resource;
        this.params   = params;
        this.payload  = payload;
    }
    
    public void AddParamater(String name, String value) {
        params.put(name, value);
    }
    
    public void AddParamaters(Map<String, String> parameters) {
        if(parameters != null) {
            for(Entry<String, String> entry : parameters.entrySet()) {
                AddParamater(entry.getKey(), entry.getValue());
            }
        }
    }
    
    public String GetParamaterValue(String name) {
        if(ContainsParamater(name)) {
            return params.get(name);
        }
        return null;
    }
    
    public Map<String, String> GetParamaters() {
        return params;
    }
    
    public boolean ContainsParamater(String name) {
        return params.containsKey(name);
    }
    
    public void SetMethod(String method) {
        this.method = method;
    }
    
    public String GetMethod() {
        return method;
    }
    
    public boolean IsMethod(String method) {
        return this.method.equals(method);
    }
    
    public void SetResource(String resource) {
        this.resource = resource;
    }
    
    public String GetResource() {
        return resource;
    }
    
    public void SetPayload(HttpPayload payload) {
        this.payload = payload;
    }
    
    public HttpPayload GetPayload() {
        return payload;
    }
}
