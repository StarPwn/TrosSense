package org.jose4j.jwk;

import java.security.KeyPair;
import java.security.SecureRandom;
import org.jose4j.jwk.PublicJsonWebKey;
import org.jose4j.keys.RsaKeyUtil;
import org.jose4j.lang.JoseException;

/* loaded from: classes5.dex */
public class RsaJwkGenerator {
    public static RsaJsonWebKey generateJwk(int bits) throws JoseException {
        return generateJwk(bits, null, null);
    }

    public static RsaJsonWebKey generateJwk(int bits, String provider, SecureRandom secureRandom) throws JoseException {
        RsaKeyUtil keyUtil = new RsaKeyUtil(provider, secureRandom);
        KeyPair keyPair = keyUtil.generateKeyPair(bits);
        RsaJsonWebKey rsaJwk = (RsaJsonWebKey) PublicJsonWebKey.Factory.newPublicJwk(keyPair.getPublic());
        rsaJwk.setPrivateKey(keyPair.getPrivate());
        return rsaJwk;
    }
}
