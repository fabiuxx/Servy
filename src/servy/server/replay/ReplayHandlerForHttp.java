/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servy.server.replay;

import servy.server.http.HttpRequest;
import servy.server.http.HttpResponse;

/**
 *
 * @author Acer
 */
public abstract class ReplayHandlerForHttp {
    public abstract void OnGET(HttpRequest request, HttpResponse response);
    public abstract void OnPOST(HttpRequest request, HttpResponse response);
}
