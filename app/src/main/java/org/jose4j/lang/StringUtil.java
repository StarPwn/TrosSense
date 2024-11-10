package org.jose4j.lang;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/* loaded from: classes5.dex */
public class StringUtil {
    public static final String US_ASCII = "US-ASCII";
    public static final String UTF_8 = "UTF-8";

    public static String newStringUtf8(byte[] bytes) {
        return newString(bytes, UTF_8);
    }

    public static String newStringUsAscii(byte[] bytes) {
        return newString(bytes, US_ASCII);
    }

    public static String newString(byte[] bytes, String charsetName) {
        if (bytes == null) {
            return null;
        }
        try {
            return new String(bytes, charsetName);
        } catch (UnsupportedEncodingException e) {
            throw newISE(charsetName);
        }
    }

    public static String newString(byte[] bytes, Charset charset) {
        if (bytes == null) {
            return null;
        }
        return new String(bytes, charset);
    }

    public static byte[] getBytesUtf8(String string) {
        return getBytesUnchecked(string, UTF_8);
    }

    public static byte[] getBytesAscii(String string) {
        return getBytesUnchecked(string, US_ASCII);
    }

    public static byte[] getBytes(String string, Charset charset) {
        if (string == null) {
            return null;
        }
        return string.getBytes(charset);
    }

    public static byte[] getBytesUnchecked(String string, String charsetName) {
        if (string == null) {
            return null;
        }
        try {
            return string.getBytes(charsetName);
        } catch (UnsupportedEncodingException e) {
            throw newISE(charsetName);
        }
    }

    private static IllegalStateException newISE(String charsetName) {
        return new IllegalStateException("Unknown or unsupported character set name: " + charsetName);
    }
}
