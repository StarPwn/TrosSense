package org.jose4j.keys;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import org.jose4j.lang.JoseException;

/* loaded from: classes5.dex */
public class RsaKeyUtil extends KeyPairUtil {
    public static final String RSA = "RSA";

    @Override // org.jose4j.keys.KeyPairUtil
    public /* bridge */ /* synthetic */ PublicKey fromPemEncoded(String str) throws JoseException, InvalidKeySpecException {
        return super.fromPemEncoded(str);
    }

    @Override // org.jose4j.keys.KeyPairUtil
    public /* bridge */ /* synthetic */ boolean isAvailable() {
        return super.isAvailable();
    }

    public RsaKeyUtil() {
        this(null, null);
    }

    public RsaKeyUtil(String provider, SecureRandom secureRandom) {
        super(provider, secureRandom);
    }

    @Override // org.jose4j.keys.KeyPairUtil
    String getAlgorithm() {
        return "RSA";
    }

    public RSAPublicKey publicKey(BigInteger modulus, BigInteger publicExponent) throws JoseException {
        RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(modulus, publicExponent);
        try {
            PublicKey publicKey = getKeyFactory().generatePublic(rsaPublicKeySpec);
            return (RSAPublicKey) publicKey;
        } catch (InvalidKeySpecException e) {
            throw new JoseException("Invalid key spec: " + e, e);
        }
    }

    public RSAPrivateKey privateKey(BigInteger modulus, BigInteger privateExponent) throws JoseException {
        RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(modulus, privateExponent);
        return getRsaPrivateKey(keySpec);
    }

    public RSAPrivateKey privateKey(BigInteger modulus, BigInteger publicExponent, BigInteger privateExponent, BigInteger primeP, BigInteger primeQ, BigInteger primeExponentP, BigInteger primeExponentQ, BigInteger crtCoefficient) throws JoseException {
        RSAPrivateCrtKeySpec keySpec = new RSAPrivateCrtKeySpec(modulus, publicExponent, privateExponent, primeP, primeQ, primeExponentP, primeExponentQ, crtCoefficient);
        return getRsaPrivateKey(keySpec);
    }

    public RSAPrivateKey getRsaPrivateKey(RSAPrivateKeySpec keySpec) throws JoseException {
        try {
            PrivateKey privateKey = getKeyFactory().generatePrivate(keySpec);
            return (RSAPrivateKey) privateKey;
        } catch (InvalidKeySpecException e) {
            throw new JoseException("Invalid key spec: " + e, e);
        }
    }

    public KeyPair generateKeyPair(int bits) throws JoseException {
        KeyPairGenerator keyGenerator = getKeyPairGenerator();
        if (this.secureRandom == null) {
            keyGenerator.initialize(bits);
        } else {
            keyGenerator.initialize(bits, this.secureRandom);
        }
        return keyGenerator.generateKeyPair();
    }
}
