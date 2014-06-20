/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servy.server.dispatch;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Acer
 */
public class HttpDispatcher {
    private Map<String, DispachableUrl> urls;
    private Map<String, Class<?>> handlers;
    
    /*
     * Constructor.
     */
    public HttpDispatcher() {
        this.urls = new LinkedHashMap<String, DispachableUrl>();
        this.handlers = new HashMap<String, Class<?>>();
    }
    
    /*
     * Asocia una URL con un controlador.
     */
    public void AddUrlHandler(String url, Class<?> klass) {
        handlers.put(url, klass);
        urls.put(url, new DispachableUrl(url));
    }
    
    /*
     * Se encarga de buscar el controlador adecuado para una url, si hubiere.
     */
    public Class GetUrlHandler(String url) throws Exception {
        DispachableUrl A = null;
        DispachableUrl B = new DispachableUrl(url);
        for(String key : urls.keySet()) {
            A = urls.get(key);
            if(A.Accepts(B)) {
                return handlers.get(key);
            }
        }
        return null;
    }
    
    /*
     * Se encarga de obtener una coleccion de elementos dinamicos {...} con sus
     * respetivos valores resueltos.
     */
    public Map<String, String> GetUrlMatches(String url) {
        DispachableUrl A = null;
        DispachableUrl B = new DispachableUrl(url);
        for(String key : urls.keySet()) {
            A = urls.get(key);
            if(A.Accepts(B)) {
                return A.GetMatches(B);
            }
        }
        return null;
    }
}
