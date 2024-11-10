package org.jose4j.mac;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import javax.crypto.Mac;
import org.jose4j.lang.InvalidKeyException;
import org.jose4j.lang.JoseException;

/* loaded from: classes5.dex */
public class MacUtil {
    public static final String HMAC_SHA256 = "HmacSHA256";
    public static final String HMAC_SHA384 = "HmacSHA384";
    public static final String HMAC_SHA512 = "HmacSHA512";

    public static Mac getInitializedMac(String algorithm, Key key) throws JoseException {
        return getInitializedMac(algorithm, key, null);
    }

    public static Mac getInitializedMac(String algorithm, Key key, String provider) throws JoseException {
        Mac mac = getMac(algorithm, provider);
        initMacWithKey(mac, key);
        return mac;
    }

    public static Mac getMac(String algorithm) throws JoseException {
        return getMac(algorithm, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r0v12, types: [javax.crypto.Mac] */
    /* JADX WARN: Type inference failed for: r0v14 */
    /* JADX WARN: Type inference failed for: r0v15 */
    public static Mac getMac(String str, String str2) throws JoseException {
        String str3 = "Unable to get a MAC implementation of algorithm name: ";
        try {
            str3 = str2 == null ? Mac.getInstance(str) : Mac.getInstance(str, str2);
            return str3;
        } catch (NoSuchAlgorithmException e) {
            throw new JoseException(str3 + str, e);
        } catch (NoSuchProviderException e2) {
            throw new JoseException(str3 + str + " using provider " + str2, e2);
        }
    }

    public static void initMacWithKey(Mac mac, Key key) throws InvalidKeyException {
        try {
            mac.init(key);
        } catch (java.security.InvalidKeyException e) {
            throw new InvalidKeyException("Key is not valid for " + mac.getAlgorithm() + " - " + e, e);
        }
    }
}
