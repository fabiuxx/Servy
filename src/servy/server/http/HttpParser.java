/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servy.server.http;

import java.net.URLDecoder;

/**
 *
 * @author Acer
 */
public class HttpParser {
    /*
     * Analiza la primera parte de una peticion HTTP.
     */
    public static HttpRequest ParseRequestBody(String request) throws HttpParserException {
        // Objeto que encapasula una peticion HTTP.
        HttpRequest request_obj = new HttpRequest();
        // Separamos la peticion en lineas.
        String[] lines = request.split("\r\n");
        // Control de seguridad.
        if(Character.isWhitespace(lines[0].charAt(0))) {
            throw new HttpParserException("Bad request.");
        }
        // Primera linea de la peticion.
        String[] command = lines[0].split("\\s");
        if(command.length != 3) {
            throw new HttpParserException("Bad request.");
        }
        // Control de la version del protocolo HTTP.
        int major, minor;
        if (command[2].indexOf("HTTP/") == 0 && command[2].indexOf('.') > 5) {
            String[] tmp = command[2].substring(5).split("\\.");
            try {
                major = Integer.parseInt(tmp[0]);
                minor = Integer.parseInt(tmp[1]);
                if(! ValidateHttpVersion(major, minor)) {
                    throw new HttpParserException("Not supported HTTP v" + major + "." + minor);
                }
            } catch (NumberFormatException e) {
                throw new HttpParserException("Bad request.");
            }
        } else {
            throw new HttpParserException("Bad request.");
        }
        // Analisis del recurso solicitado.
        ParseResourceURL(command[1], request_obj);
        // Inspeccion del metodo HTTP.
        String method = command[0].toUpperCase();
        if(method.equals("GET") || method.equals("POST")) {
            request_obj.SetMethod(method);
        } else {
            throw new HttpParserException("Not implemented method: " + method);
        }
        // Parseado de los encabezados.
        for(int i=1; i<lines.length; i++) {
            String line = lines[i];
            if(line.equals("")) {
                continue;
            }
            int delimiter_pos = line.indexOf(":");
            String key = line.substring(0, delimiter_pos);
            String val = line.substring(delimiter_pos + 1).trim();
            request_obj.SetHeader(key, val);
        }
        // Retornamos el objetito.
        return request_obj;
    }
    
    private static void ParseResourceURL(String url, HttpRequest request_obj) throws HttpParserException {        
        // Verificamos la presencia de argumentos en la peticion.
        int args_division = url.indexOf("?");
        if(args_division < 0) {
            // No hay parametros presentes.
            request_obj.SetResource(url);
        } else {
            try {
                // Hay parametros presentes.
                String resource = URLDecoder.decode(url.substring(0, args_division), "ISO-8859-1");
                request_obj.SetResource(resource);
                String[] params = url.substring(args_division + 1).split("&");
                for(String param : params) {
                    String[] parts = param.split("=");
                    String key, val;
                    if(parts.length == 2) {
                        // Par clave valor.
                        key = URLDecoder.decode(parts[0], "ISO-8859-1");
                        val = URLDecoder.decode(parts[1], "ISO-8859-1");
                    } else {
                        key = URLDecoder.decode(parts[0], "ISO-8859-1");
                        val = "true";
                    }
                    request_obj.AddParamater(key, val);
                }
            } catch(Exception e) {
                    throw new HttpParserException("Bad request.");
            }
        }
    }
    
    private static boolean ValidateHttpVersion(int major, int minor) {
        // Versiones soportadas: 1.0 y 1.1
        if(major == 1) {
            return (minor == 0 || minor == 1);
        } else {
            return false;
        }
    }
}

class HttpParserException extends Exception {
    public HttpParserException(String message) {
        super(message);
    }
}