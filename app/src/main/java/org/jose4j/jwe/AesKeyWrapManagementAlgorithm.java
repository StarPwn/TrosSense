package org.jose4j.jwe;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import org.jose4j.jwk.OctetSequenceJsonWebKey;
import org.jose4j.jwx.KeyValidationSupport;
import org.jose4j.keys.KeyPersuasion;
import org.jose4j.lang.ExceptionHelp;
import org.jose4j.lang.InvalidKeyException;

/* loaded from: classes5.dex */
public class AesKeyWrapManagementAlgorithm extends WrappingKeyManagementAlgorithm {
    int keyByteLength;

    public AesKeyWrapManagementAlgorithm(String alg, int keyByteLength) {
        super("AESWrap", alg);
        setKeyType(OctetSequenceJsonWebKey.KEY_TYPE);
        setKeyPersuasion(KeyPersuasion.SYMMETRIC);
        this.keyByteLength = keyByteLength;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getKeyByteLength() {
        return this.keyByteLength;
    }

    @Override // org.jose4j.jwe.KeyManagementAlgorithm
    public void validateEncryptionKey(Key managementKey, ContentEncryptionAlgorithm contentEncryptionAlg) throws InvalidKeyException {
        validateKey(managementKey);
    }

    @Override // org.jose4j.jwe.KeyManagementAlgorithm
    public void validateDecryptionKey(Key managementKey, ContentEncryptionAlgorithm contentEncryptionAlg) throws InvalidKeyException {
        validateKey(managementKey);
    }

    void validateKey(Key managementKey) throws InvalidKeyException {
        KeyValidationSupport.validateAesWrappingKey(managementKey, getAlgorithmIdentifier(), getKeyByteLength());
    }

    @Override // org.jose4j.jwa.Algorithm
    public boolean isAvailable() {
        int aesByteKeyLength = getKeyByteLength();
        String alg = getJavaAlgorithm();
        try {
            Cipher.getInstance(alg);
            return CipherStrengthSupport.isAvailable(alg, aesByteKeyLength);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            this.log.debug("{} for {} is not available ({}).", alg, getAlgorithmIdentifier(), ExceptionHelp.toStringWithCauses(e));
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AesKeyWrapManagementAlgorithm setUseGeneralProviderContext() {
        this.useSuppliedKeyProviderContext = false;
        return this;
    }

    /* loaded from: classes5.dex */
    public static class Aes128 extends AesKeyWrapManagementAlgorithm {
        public Aes128() {
            super(KeyManagementAlgorithmIdentifiers.A128KW, 16);
        }
    }

    /* loaded from: classes5.dex */
    public static class Aes192 extends AesKeyWrapManagementAlgorithm {
        public Aes192() {
            super(KeyManagementAlgorithmIdentifiers.A192KW, 24);
        }
    }

    /* loaded from: classes5.dex */
    public static class Aes256 extends AesKeyWrapManagementAlgorithm {
        public Aes256() {
            super(KeyManagementAlgorithmIdentifiers.A256KW, 32);
        }
    }
}
