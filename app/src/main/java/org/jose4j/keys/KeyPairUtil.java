package org.jose4j.keys;

import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Set;
import org.jose4j.base64url.SimplePEMEncoder;
import org.jose4j.lang.JoseException;

/* loaded from: classes5.dex */
abstract class KeyPairUtil {
    private static final String BEGIN_PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----";
    private static final String END_PUBLIC_KEY = "-----END PUBLIC KEY-----";
    protected String provider;
    protected SecureRandom secureRandom;

    abstract String getAlgorithm();

    /* JADX INFO: Access modifiers changed from: protected */
    public KeyPairUtil(String provider, SecureRandom secureRandom) {
        this.provider = provider;
        this.secureRandom = secureRandom;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public KeyFactory getKeyFactory() throws JoseException {
        String agl = getAlgorithm();
        try {
            return this.provider == null ? KeyFactory.getInstance(agl) : KeyFactory.getInstance(agl, this.provider);
        } catch (NoSuchAlgorithmException e) {
            throw new JoseException("Couldn't find " + agl + " KeyFactory! " + e, e);
        } catch (NoSuchProviderException e2) {
            throw new JoseException("Cannot get KeyFactory instance with provider " + this.provider, e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public KeyPairGenerator getKeyPairGenerator() throws JoseException {
        String alg = getAlgorithm();
        try {
            return this.provider == null ? KeyPairGenerator.getInstance(alg) : KeyPairGenerator.getInstance(alg, this.provider);
        } catch (NoSuchAlgorithmException e) {
            throw new JoseException("Couldn't find " + alg + " KeyPairGenerator! " + e, e);
        } catch (NoSuchProviderException e2) {
            throw new JoseException("Cannot get KeyPairGenerator instance with provider " + this.provider, e2);
        }
    }

    public PublicKey fromPemEncoded(String pem) throws JoseException, InvalidKeySpecException {
        int beginIndex = pem.indexOf(BEGIN_PUBLIC_KEY) + BEGIN_PUBLIC_KEY.length();
        int endIndex = pem.indexOf(END_PUBLIC_KEY);
        String base64 = pem.substring(beginIndex, endIndex).trim();
        byte[] decode = SimplePEMEncoder.decode(base64);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decode);
        KeyFactory kf = getKeyFactory();
        return kf.generatePublic(spec);
    }

    public static String pemEncode(PublicKey publicKey) {
        byte[] encoded = publicKey.getEncoded();
        return "-----BEGIN PUBLIC KEY-----\r\n" + SimplePEMEncoder.encode(encoded) + END_PUBLIC_KEY;
    }

    public boolean isAvailable() {
        Set<String> keyFactories = Security.getAlgorithms("KeyFactory");
        Set<String> keyPairGenerators = Security.getAlgorithms("KeyPairGenerator");
        String algorithm = getAlgorithm();
        return keyPairGenerators.contains(algorithm) && keyFactories.contains(algorithm);
    }
}
