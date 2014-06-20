/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servy.utils;

/**
 *
 * @author Acer
 */
public class Base64 {
    private final static String CHARSET  = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    private final static String PAD_CHAR = "=";
    
    public static String Encode(String input) {
        String pad = "";
        // Si la entrada no es multiplo de 3, se agregan caracteres de relleno.
        int c = input.length() % 3;
        if(c > 0) {
            for (;c < 3; c++) {
		pad   += PAD_CHAR;
		input += "\0";
	    }
        }
        // Se itera sobre la entrada, 3 caracteres a la vez.
        StringBuilder builder = new StringBuilder();
	for (c = 0; c < input.length(); c += 3) {
	    // Se agregan un delimitador de linea cada 76 caracteres.
	    if (c > 0 && (c / 3 * 4) % 76 == 0) {
		builder.append("\r\n");
            }
            // Los 3 caracteres se convierten en 4.
	    int n  = (input.charAt(c) << 16) + (input.charAt(c + 1) << 8) + (input.charAt(c + 2));
	    int n1 = (n >> 18) & 63;
            int n2 = (n >> 12) & 63;
            int n3 = (n >> 6) & 63;
            int n4 = (n) & 63;
            builder.append(CHARSET.charAt(n1));
            builder.append(CHARSET.charAt(n2));
            builder.append(CHARSET.charAt(n3));
            builder.append(CHARSET.charAt(n4));
	}
        // Se retorna la cadena final.
        return builder.substring(0, builder.length() - pad.length()) + pad;
    }
    
    public static String Decode(String input) {
        // Control de seguridad. Se eliminan los caracteres no validos.
        input = input.replaceAll("[^" + CHARSET + "=]", "");
        if(input.length() <= 0) {
            return "";
        }
        // Se reemplaza cualquier caracter final de relleno con el caracter A.
        String pad = (input.charAt(input.length() - 1) == '=' ? (input.charAt(input.length() - 2) == '=' ? "AA" : "A") : "");
        input = input.substring(0, input.length() - pad.length()) + pad;
        StringBuilder builder = new StringBuilder();
        // Se itera sobre la entrada, 4 caracteres a la vez.
	for (int c = 0; c < input.length(); c += 4) {
            // Los 4 caracteres se convierten en 3.
	    int n = (CHARSET.indexOf(input.charAt(c)) << 18) + (CHARSET.indexOf(input.charAt(c + 1)) << 12) + (CHARSET.indexOf(input.charAt(c + 2)) << 6) + CHARSET.indexOf(input.charAt(c + 3));
            builder.append((char)((n >>> 16) & 0xFF));
            builder.append((char)((n >>> 8) & 0xFF));
	    builder.append((char) (n & 0xFF));
	}
	// Se retorna la cadena final.
	return builder.substring(0, builder.length() - pad.length());
    }
}
