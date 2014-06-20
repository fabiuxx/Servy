/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servy.test;

import java.util.Map.Entry;
import servy.server.replay.ReplayHandlerForHttp;
import servy.server.http.HttpRequest;
import servy.server.http.HttpResponse;

/**
 *
 * @author Acer
 */
public class HandlerForAnyHTTP extends ReplayHandlerForHttp {

    @Override
    public void OnGET(HttpRequest request, HttpResponse response) {        
        String C = RequestToHTML(request);
        response.SetStatusCode(200);
        response.SetHeader("Content-Type", "text/html");
        response.SetHeader("Content-Length", "" + C.length());
        response.AppendPayload(C);
    }

    @Override
    public void OnPOST(HttpRequest request, HttpResponse response) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    private String RequestToHTML(HttpRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append("<html>");
        builder.append("<body>");
        builder.append("<h1>").append(request.GetMethod()).append("&nbsp;&nbsp;").append(request.GetResource()).append("</h1>");
        builder.append("<h3>Headers</h3>");
        for(Entry<String, String> e : request.GetHeaders().entrySet()) {
            builder.append(e.getKey()).append("&nbsp;:&nbsp;").append(e.getValue()).append("</br>");
        }
        builder.append("<h3>Paramaters</h3>");
        for(Entry<String, String> e : request.GetParamaters().entrySet()) {
            builder.append(e.getKey()).append("&nbsp;=>&nbsp;").append(e.getValue()).append("</br>");
        }
        if(request.GetPayload().GetTotalBytes() > 0) {
            builder.append("<h3>Data</h3>");
            int i = 0;
            for(byte _b : request.GetPayload().GetBytes()) {
                builder.append((char)_b);
                i ++;
                if(i == 8) {
                    builder.append("</br>");
                    i = 0;
                }
            }
        }
        builder.append("</body>");
        builder.append("</html>");
        return builder.toString();
    }
}
