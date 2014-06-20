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
public class HttpStatus {
    private static Map<Integer, String> status;
    
    static {
        HttpStatus.status = new HashMap<>();
        HttpStatus.status.put(100, "Continue");
        HttpStatus.status.put(101, "Switching Protocols");
        HttpStatus.status.put(102, "Processing");
        HttpStatus.status.put(200, "OK");
        HttpStatus.status.put(201, "Created");
        HttpStatus.status.put(202, "Accepted");
        HttpStatus.status.put(203, "Non-Authoritative Information");
        HttpStatus.status.put(204, "No Content");
        HttpStatus.status.put(205, "Reset Content");
        HttpStatus.status.put(206, "Partial Content");
        HttpStatus.status.put(207, "Multi-Status");
        HttpStatus.status.put(208, "Already Reported");
        HttpStatus.status.put(226, "IM Used");
        HttpStatus.status.put(300, "Multiple Choices");
        HttpStatus.status.put(301, "Moved Permanently");
        HttpStatus.status.put(302, "Found");
        HttpStatus.status.put(303, "See Other");
        HttpStatus.status.put(304, "Not Modified");
        HttpStatus.status.put(305, "Use Proxy");
        HttpStatus.status.put(307, "Temporary Redirect");
        HttpStatus.status.put(308, "Permanent Redirect");
        HttpStatus.status.put(400, "Bad Request");
        HttpStatus.status.put(401, "Unauthorized");
        HttpStatus.status.put(402, "Payment Required");
        HttpStatus.status.put(403, "Forbidden");
        HttpStatus.status.put(404, "Not Found");
        HttpStatus.status.put(405, "Method Not Allowed");
        HttpStatus.status.put(406, "Not Acceptable");
        HttpStatus.status.put(407, "Proxy Authentication Required");
        HttpStatus.status.put(408, "Request Timeout");
        HttpStatus.status.put(409, "Conflict");
        HttpStatus.status.put(410, "Gone");
        HttpStatus.status.put(411, "Length Required");
        HttpStatus.status.put(412, "Precondition Failed");
        HttpStatus.status.put(413, "Payload Too Large");
        HttpStatus.status.put(414, "URI Too Long");
        HttpStatus.status.put(415, "Unsupported Media Type");
        HttpStatus.status.put(416, "Requested Range Not Satisfiable");
        HttpStatus.status.put(417, "Expectation Failed");
        HttpStatus.status.put(422, "Unprocessable Entity");
        HttpStatus.status.put(423, "Locked");
        HttpStatus.status.put(424, "Failed Dependency");
        HttpStatus.status.put(426, "Upgrade Required");
        HttpStatus.status.put(428, "Precondition Required");
        HttpStatus.status.put(429, "Too Many Requests");
        HttpStatus.status.put(431, "Request Header Fields Too Large");
        HttpStatus.status.put(500, "Internal Server Error");
        HttpStatus.status.put(501, "Not Implemented");
        HttpStatus.status.put(502, "Bad Gateway");
        HttpStatus.status.put(503, "Service Unavailable");
        HttpStatus.status.put(504, "Gateway Timeout");
        HttpStatus.status.put(505, "HTTP Version Not Supported");
        HttpStatus.status.put(506, "Variant Also Negotiates (Experimental)");
        HttpStatus.status.put(507, "Insufficient Storage");
        HttpStatus.status.put(508, "Loop Detected");
        HttpStatus.status.put(510, "Not Extended");
        HttpStatus.status.put(511, "Network Authentication Required");
    }
    
    public static String GetStatusLineForCode(int code) {
        if(status.containsKey(code)) {
            return "HTTP/1.1 " + code + " " + HttpStatus.status.get(code);
        }
        return "HTTP/1.1 500 Internal Server Error";
    }
    
    public static Integer GetCodeForString(String status) {
        for(Entry<Integer, String> entry : HttpStatus.status.entrySet()) {
            if(entry.getValue().equals(status)) {
                return entry.getKey();
            }
        }
        return 404;
    }
    
    public static String GetStringForCode(Integer code) {
        if(status.containsKey(code)) {
            return status.get(code);
        }
        return null;
    }
}
