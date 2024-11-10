package org.jose4j.base64url;

/* loaded from: classes5.dex */
public class SimplePEMEncoder {
    public static String encode(byte[] bytes) {
        return getCodec().encodeToString(bytes);
    }

    public static byte[] decode(String encoded) {
        return getCodec().decode(encoded);
    }

    static org.jose4j.base64url.internal.apache.commons.codec.binary.Base64 getCodec() {
        return new org.jose4j.base64url.internal.apache.commons.codec.binary.Base64(64);
    }
}
