package org.jose4j.jwe;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import org.jose4j.lang.JoseException;

/* loaded from: classes5.dex */
public class CipherUtil {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static Cipher getCipher(String algorithm, String provider) throws JoseException {
        try {
            return provider == null ? Cipher.getInstance(algorithm) : Cipher.getInstance(algorithm, provider);
        } catch (NoSuchAlgorithmException e) {
            e = e;
            throw new JoseException(e.toString(), e);
        } catch (NoSuchProviderException e2) {
            throw new JoseException("Unable to get a Cipher implementation of " + algorithm + " using provider " + provider, e2);
        } catch (NoSuchPaddingException e3) {
            e = e3;
            throw new JoseException(e.toString(), e);
        }
    }
}
