/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servy.utils;

import java.security.MessageDigest;

/**
 *
 * @author Acer
 */
public class SHA1 {
    private static final String algorithm = "SHA-1";
    
    public static byte[] digest(String message) throws Exception {
        byte[] buffer = message.getBytes();
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        messageDigest.reset();
        messageDigest.update(buffer);
        byte[] digest = messageDigest.digest();
        return digest;
    }
}
