/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servy.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Acer
 */
public class Logger {
    private static final String DEFAULT_LOGFILE = "server-log";
    private static final Object lock = new Object();
    private static final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
    private static Logger instance = null;
    private static String filename = DEFAULT_LOGFILE;
    private static int filter = 2;
     
    /*
     * Retorna la unica instancia del Logger.
     */
    public static Logger GetLogger() {
        if(instance == null) {
            instance = new Logger();
        }
        return instance;
    }
    
    /*
     * Abre el archivo de log para escritura.
     */
    private static PrintWriter OpenLogFile() throws IOException {
        FileWriter fw = new FileWriter(filename, true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw);
        return pw;
    }
    
    /*
     * Obtiene una cadena que representa la hora-fecha local.
     */
    private static String GetTimeString() {
        Date d = new Date();
        return formatter.format(d);
    }
    
    /*
     * Indica que niveles de log pueden escribirse.
     */
    public static void Filter(LogLevel...levels) {
        if(levels.length >= 1) {
            for(LogLevel level : levels) {
                Logger.filter |= level.flag;
            }
        }
    }
    
    /*
     * Metodo principal que escribe en el archivo de log.
     */
    private static void Write(LogLevel level, String message) {
        // El nivel debe estar habilitado.
        if((filter & level.flag) > 0) {
            synchronized(lock) {
                try {
                    PrintWriter writer = OpenLogFile();
                    writer.println("" + level + " [" + GetTimeString() + "] -- " + message);
                    writer.close();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            };
        }
    }
    
    /*
     * Mensaje de nivel DEBUG.
     */
    public void d(String message) {
        Write(LogLevel.DEBUG, message);
    }
    
    /*
     * Mensaje de nivel INFO.
     */
    public void i(String message) {
        Write(LogLevel.INFO, message);
    }
    
    /*
     * Mensaje de nivel WARN.
     */
    public void w(String message) {
        Write(LogLevel.WARN, message);
    }
    
    /*
     * Mensaje de nivel ERROR.
     */
    public void e(String message) {
        Write(LogLevel.ERROR, message);
    }
    
    /*
     * Mensaje de nivel FATAL.
     */
    public void f(String message) {
        Write(LogLevel.FATAL, message);
    }
    
    /*
     * Enumeracion para los niveles de log.
     */
    public enum LogLevel {
        DEBUG("DEBUG",1),
        INFO ("INFO ",2),
        WARN ("WARN ",4),
        ERROR("ERROR",8),
        FATAL("FATAL",16);
        
        private final String value;
        private final int flag;
        
        private LogLevel(String s, int f) {
            value = s;
            flag  = f;
        }

        @Override
        public String toString(){
           return value;
        }
    };
}
