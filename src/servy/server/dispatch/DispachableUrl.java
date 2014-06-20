/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servy.server.dispatch;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Acer
 */
public class DispachableUrl {
    private List<DispachableURLElement> elements;
    
    /*
     * Constructor.
     */
    public DispachableUrl(String url) {
        this.elements = new LinkedList<DispachableURLElement>();
        String[] elements = url.split("/");
        for(String element : elements) {
            if(! element.equals("")) {
                this.elements.add(new DispachableURLElement(element));
            }
        }
    }
    
    /*
     * Indica si este objeto reconoce a otro como equivalente.
     */
    public boolean Accepts(DispachableUrl other) {
        if(this.elements.size() == other.elements.size()) {
            // Se itera sobre los elementos que forman ambas urls.
            for(int i=0; i<this.elements.size(); i++){
                DispachableURLElement a = this.elements.get(i);
                DispachableURLElement b = other.elements.get(i);
                // Si el elemento a es fijo, el elemento b debe tener el mismo nombre.
                if(a.fixed && !a.name.equals(b.name)) {
                    return false;
                }
            }
            // Si todos son iguales o la URL tiene elementos variables, las URLs son equivalentes.
            return true;
        }
        return false;
    }
    
    /*
     * Retorna una coleccion de elementos {...} con sus respectivos valores, si hubiere.
     * Se asume que ambos objetos deben ser equivalentes, es decir this.Accepts(other) == true.
     */
    public Map<String, String> GetMatches(DispachableUrl other) {
        Map<String, String> matches = new HashMap<String, String>();
        // Se itera sobre los elementos que forman ambas urls.
        for(int i=0; i<this.elements.size(); i++){
            DispachableURLElement a = this.elements.get(i);
            DispachableURLElement b = other.elements.get(i);
            // Si el elemento a es fijo, el elemento b debe tener el mismo nombre.
            if(! a.fixed) {
                matches.put(a.name, b.name);
            }
        }
        return matches;
    }
    
    /*
     * Clase privada que representa un elemento de una URL.
     */
    private class DispachableURLElement {
        public String name;
        public boolean fixed;
        
        /*
         * Constructor.
         */
        public DispachableURLElement(String name) {
            // Si es un elemento variable, debe estar en el formato {nombre}.
            int p0 = name.indexOf("{");
            if(p0 >= 0) {
                int p1 = name.indexOf("}");
                this.name  = name.substring(p0, p1 + 1);
                this.fixed = false;
            } else {
                // Es un elemento fijo.
                this.name  = name;
                this.fixed = true;
            }
        }
    }
}
