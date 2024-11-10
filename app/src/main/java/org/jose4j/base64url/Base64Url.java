package org.jose4j.base64url;

import org.jose4j.lang.StringUtil;

/* loaded from: classes5.dex */
public class Base64Url {
    private org.jose4j.base64url.internal.apache.commons.codec.binary.Base64 base64urlCodec = new org.jose4j.base64url.internal.apache.commons.codec.binary.Base64(-1, null, true);

    public String base64UrlDecodeToUtf8String(String encodedValue) {
        return base64UrlDecodeToString(encodedValue, StringUtil.UTF_8);
    }

    public String base64UrlDecodeToString(String encodedValue, String charsetName) {
        byte[] bytes = base64UrlDecode(encodedValue);
        return StringUtil.newString(bytes, charsetName);
    }

    public byte[] base64UrlDecode(String encodedValue) {
        return this.base64urlCodec.decode(encodedValue);
    }

    public String base64UrlEncodeUtf8ByteRepresentation(String value) {
        return base64UrlEncode(value, StringUtil.UTF_8);
    }

    public String base64UrlEncode(String value, String charsetName) {
        byte[] bytes = StringUtil.getBytesUnchecked(value, charsetName);
        return base64UrlEncode(bytes);
    }

    public String base64UrlEncode(byte[] bytes) {
        return this.base64urlCodec.encodeToString(bytes);
    }

    private static Base64Url getOne() {
        return new Base64Url();
    }

    public static String decodeToUtf8String(String encodedValue) {
        return getOne().base64UrlDecodeToString(encodedValue, StringUtil.UTF_8);
    }

    public static String decodeToString(String encodedValue, String charsetName) {
        return getOne().base64UrlDecodeToString(encodedValue, charsetName);
    }

    public static byte[] decode(String encodedValue) {
        return getOne().base64UrlDecode(encodedValue);
    }

    public static String encodeUtf8ByteRepresentation(String value) {
        return getOne().base64UrlEncodeUtf8ByteRepresentation(value);
    }

    public static String encode(String value, String charsetName) {
        return getOne().base64UrlEncode(value, charsetName);
    }

    public static String encode(byte[] bytes) {
        return getOne().base64UrlEncode(bytes);
    }
}
