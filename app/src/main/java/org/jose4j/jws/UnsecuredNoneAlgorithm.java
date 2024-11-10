package org.jose4j.jws;

import java.security.Key;
import org.jose4j.jca.ProviderContext;
import org.jose4j.jwa.AlgorithmInfo;
import org.jose4j.jwa.CryptoPrimitive;
import org.jose4j.keys.KeyPersuasion;
import org.jose4j.lang.ByteUtil;
import org.jose4j.lang.InvalidKeyException;
import org.jose4j.lang.JoseException;

/* loaded from: classes5.dex */
public class UnsecuredNoneAlgorithm extends AlgorithmInfo implements JsonWebSignatureAlgorithm {
    private static final String CANNOT_HAVE_KEY_MESSAGE = "JWS Plaintext (alg=none) must not use a key.";

    public UnsecuredNoneAlgorithm() {
        setAlgorithmIdentifier("none");
        setKeyPersuasion(KeyPersuasion.NONE);
    }

    @Override // org.jose4j.jws.JsonWebSignatureAlgorithm
    public boolean verifySignature(byte[] signatureBytes, Key key, byte[] securedInputBytes, ProviderContext providerContext) throws JoseException {
        validateKey(key);
        return signatureBytes.length == 0;
    }

    @Override // org.jose4j.jws.JsonWebSignatureAlgorithm
    public CryptoPrimitive prepareForSign(Key key, ProviderContext providerContext) throws JoseException {
        validateKey(key);
        return null;
    }

    @Override // org.jose4j.jws.JsonWebSignatureAlgorithm
    public byte[] sign(CryptoPrimitive cryptoPrimitive, byte[] securedInputBytes) {
        return ByteUtil.EMPTY_BYTES;
    }

    @Override // org.jose4j.jws.JsonWebSignatureAlgorithm
    public void validateSigningKey(Key key) throws InvalidKeyException {
        validateKey(key);
    }

    @Override // org.jose4j.jws.JsonWebSignatureAlgorithm
    public void validateVerificationKey(Key key) throws InvalidKeyException {
        validateKey(key);
    }

    private void validateKey(Key key) throws InvalidKeyException {
        if (key != null) {
            throw new InvalidKeyException(CANNOT_HAVE_KEY_MESSAGE);
        }
    }

    @Override // org.jose4j.jwa.Algorithm
    public boolean isAvailable() {
        return true;
    }
}
