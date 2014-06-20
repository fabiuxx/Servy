/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servy.server;

import servy.server.http.HttpStatus;

/**
 *
 * @author Acer
 */
public class ServerDefaultResponses {
    
    private static String HtmlTemplate(String content) {
        StringBuilder builder = new StringBuilder();
        builder.append("<html><body>");
        builder.append(content);
        builder.append("</body></html>");
        return builder.toString();
    }
    
    public static String ResponseFor(int code, String content) {
        StringBuilder builder = new StringBuilder();
        String html = HtmlTemplate(content);
        builder.append(HttpStatus.GetStatusLineForCode(code)).append("\r\n");
        builder.append("Server: Servy").append("\r\n");
        builder.append("Content-Type: text/html").append("\r\n");
        builder.append("Content-Length: ").append(html.length()).append("\r\n");
        builder.append("\r\n");
        builder.append(html);
        return builder.toString();
    }
    
    public static String ERROR404(String resource) {
        String C = HtmlTemplate("<h1>ERROR 404</h1>" + resource);
        return ResponseFor(404, C);
    }
    
    public static String ERROR500(String cause) {
        String C = HtmlTemplate("<h1>ERROR 500</h1>" + cause);
        return ResponseFor(500, C);
    }
}
