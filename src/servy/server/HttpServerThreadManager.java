/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servy.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Acer
 */
public class HttpServerThreadManager {
    private static ExecutorService executor = Executors.newCachedThreadPool();
    
    /*
     * Inicia la ejecucion del objeto en el pool de hilos.
     */
    public static void Run(Runnable runnable) {
        executor.execute(runnable);
    }
}
