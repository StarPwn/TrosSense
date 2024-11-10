package org.jose4j.jws;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import org.jose4j.jca.ProviderContext;
import org.jose4j.jwa.AlgorithmInfo;
import org.jose4j.jwa.CryptoPrimitive;
import org.jose4j.jwk.OctetSequenceJsonWebKey;
import org.jose4j.keys.KeyPersuasion;
import org.jose4j.lang.ByteUtil;
import org.jose4j.lang.InvalidKeyException;
import org.jose4j.lang.JoseException;
import org.jose4j.mac.MacUtil;

/* loaded from: classes5.dex */
public class HmacUsingShaAlgorithm extends AlgorithmInfo implements JsonWebSignatureAlgorithm {
    private int minimumKeyLength;

    public HmacUsingShaAlgorithm(String id, String javaAlgo, int minimumKeyLength) {
        setAlgorithmIdentifier(id);
        setJavaAlgorithm(javaAlgo);
        setKeyPersuasion(KeyPersuasion.SYMMETRIC);
        setKeyType(OctetSequenceJsonWebKey.KEY_TYPE);
        this.minimumKeyLength = minimumKeyLength;
    }

    @Override // org.jose4j.jws.JsonWebSignatureAlgorithm
    public boolean verifySignature(byte[] signatureBytes, Key key, byte[] securedInputBytes, ProviderContext providerContext) throws JoseException {
        if (!(key instanceof SecretKey)) {
            throw new InvalidKeyException(key.getClass() + " cannot be used for HMAC verification.");
        }
        Mac mac = getMacInstance(key, providerContext);
        byte[] calculatedSigature = mac.doFinal(securedInputBytes);
        return ByteUtil.secureEquals(signatureBytes, calculatedSigature);
    }

    @Override // org.jose4j.jws.JsonWebSignatureAlgorithm
    public CryptoPrimitive prepareForSign(Key key, ProviderContext providerContext) throws JoseException {
        Mac mac = getMacInstance(key, providerContext);
        return new CryptoPrimitive(mac);
    }

    @Override // org.jose4j.jws.JsonWebSignatureAlgorithm
    public byte[] sign(CryptoPrimitive cryptoPrimitive, byte[] securedInputBytes) throws JoseException {
        Mac mac = cryptoPrimitive.getMac();
        return mac.doFinal(securedInputBytes);
    }

    private Mac getMacInstance(Key key, ProviderContext providerContext) throws JoseException {
        String macProvider = providerContext.getSuppliedKeyProviderContext().getMacProvider();
        return MacUtil.getInitializedMac(getJavaAlgorithm(), key, macProvider);
    }

    void validateKey(Key key) throws InvalidKeyException {
        int length;
        if (key == null) {
            throw new InvalidKeyException("key is null");
        }
        if (key.getEncoded() != null && (length = ByteUtil.bitLength(key.getEncoded())) < this.minimumKeyLength) {
            throw new InvalidKeyException("A key of the same size as the hash output (i.e. " + this.minimumKeyLength + " bits for " + getAlgorithmIdentifier() + ") or larger MUST be used with the HMAC SHA algorithms but this key is only " + length + " bits");
        }
    }

    @Override // org.jose4j.jws.JsonWebSignatureAlgorithm
    public void validateSigningKey(Key key) throws InvalidKeyException {
        validateKey(key);
    }

    @Override // org.jose4j.jws.JsonWebSignatureAlgorithm
    public void validateVerificationKey(Key key) throws InvalidKeyException {
        validateKey(key);
    }

    @Override // org.jose4j.jwa.Algorithm
    public boolean isAvailable() {
        try {
            Mac.getInstance(getJavaAlgorithm());
            return true;
        } catch (NoSuchAlgorithmException e) {
            return false;
        }
    }

    /* loaded from: classes5.dex */
    public static class HmacSha256 extends HmacUsingShaAlgorithm {
        public HmacSha256() {
            super(AlgorithmIdentifiers.HMAC_SHA256, MacUtil.HMAC_SHA256, 256);
        }
    }

    /* loaded from: classes5.dex */
    public static class HmacSha384 extends HmacUsingShaAlgorithm {
        public HmacSha384() {
            super(AlgorithmIdentifiers.HMAC_SHA384, MacUtil.HMAC_SHA384, 384);
        }
    }

    /* loaded from: classes5.dex */
    public static class HmacSha512 extends HmacUsingShaAlgorithm {
        public HmacSha512() {
            super(AlgorithmIdentifiers.HMAC_SHA512, MacUtil.HMAC_SHA512, 512);
        }
    }
}
