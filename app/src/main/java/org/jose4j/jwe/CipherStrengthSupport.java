package org.jose4j.jwe;

import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import org.jose4j.lang.ByteUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: classes5.dex */
public class CipherStrengthSupport {
    private static final Logger log = LoggerFactory.getLogger((Class<?>) CipherStrengthSupport.class);

    public static boolean isAvailable(String algorithm, int keyByteLength) {
        int bitKeyLength = ByteUtil.bitLength(keyByteLength);
        try {
            int maxAllowedKeyLength = Cipher.getMaxAllowedKeyLength(algorithm);
            boolean isAvailable = bitKeyLength <= maxAllowedKeyLength;
            if (!isAvailable) {
                log.debug("max allowed key length for {} is {}", algorithm, Integer.valueOf(maxAllowedKeyLength));
                return isAvailable;
            }
            return isAvailable;
        } catch (NoSuchAlgorithmException e) {
            log.debug("Unknown/unsupported algorithm, {} {}", algorithm, e);
            return false;
        }
    }
}
