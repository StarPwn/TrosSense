package org.jose4j.jwe;

import java.security.Key;
import org.jose4j.jca.ProviderContext;
import org.jose4j.jwa.AlgorithmInfo;
import org.jose4j.jwa.CryptoPrimitive;
import org.jose4j.jwk.OctetSequenceJsonWebKey;
import org.jose4j.jwx.Headers;
import org.jose4j.jwx.KeyValidationSupport;
import org.jose4j.keys.KeyPersuasion;
import org.jose4j.lang.ByteUtil;
import org.jose4j.lang.InvalidKeyException;
import org.jose4j.lang.JoseException;

/* loaded from: classes5.dex */
public class DirectKeyManagementAlgorithm extends AlgorithmInfo implements KeyManagementAlgorithm {
    public DirectKeyManagementAlgorithm() {
        setAlgorithmIdentifier(KeyManagementAlgorithmIdentifiers.DIRECT);
        setKeyPersuasion(KeyPersuasion.SYMMETRIC);
        setKeyType(OctetSequenceJsonWebKey.KEY_TYPE);
    }

    @Override // org.jose4j.jwe.KeyManagementAlgorithm
    public ContentEncryptionKeys manageForEncrypt(Key managementKey, ContentEncryptionKeyDescriptor cekDesc, Headers headers, byte[] cekOverride, ProviderContext providerContext) throws JoseException {
        KeyValidationSupport.cekNotAllowed(cekOverride, getAlgorithmIdentifier());
        byte[] cekBytes = managementKey.getEncoded();
        return new ContentEncryptionKeys(cekBytes, ByteUtil.EMPTY_BYTES);
    }

    @Override // org.jose4j.jwe.KeyManagementAlgorithm
    public CryptoPrimitive prepareForDecrypt(Key managementKey, Headers headers, ProviderContext providerContext) {
        return new CryptoPrimitive(managementKey);
    }

    @Override // org.jose4j.jwe.KeyManagementAlgorithm
    public Key manageForDecrypt(CryptoPrimitive cryptoPrimitive, byte[] encryptedKey, ContentEncryptionKeyDescriptor cekDesc, Headers headers, ProviderContext providerContext) throws JoseException {
        Key managementKey = cryptoPrimitive.getKey();
        if (encryptedKey.length != 0) {
            throw new InvalidKeyException("An empty octet sequence is to be used as the JWE Encrypted Key value when utilizing direct encryption but this JWE has " + encryptedKey.length + " octets in the encrypted key part.");
        }
        return managementKey;
    }

    @Override // org.jose4j.jwe.KeyManagementAlgorithm
    public void validateEncryptionKey(Key managementKey, ContentEncryptionAlgorithm contentEncryptionAlg) throws InvalidKeyException {
        validateKey(managementKey, contentEncryptionAlg);
    }

    private void validateKey(Key managementKey, ContentEncryptionAlgorithm contentEncryptionAlg) throws InvalidKeyException {
        int managementKeyByteLength;
        int expectedByteLength;
        KeyValidationSupport.notNull(managementKey);
        if (managementKey.getEncoded() != null && (expectedByteLength = contentEncryptionAlg.getContentEncryptionKeyDescriptor().getContentEncryptionKeyByteLength()) != (managementKeyByteLength = managementKey.getEncoded().length)) {
            throw new InvalidKeyException("Invalid key for " + getAlgorithmIdentifier() + " with " + contentEncryptionAlg.getAlgorithmIdentifier() + ", expected a " + ByteUtil.bitLength(expectedByteLength) + " bit key but a " + ByteUtil.bitLength(managementKeyByteLength) + " bit key was provided.");
        }
    }

    @Override // org.jose4j.jwe.KeyManagementAlgorithm
    public void validateDecryptionKey(Key managementKey, ContentEncryptionAlgorithm contentEncryptionAlg) throws InvalidKeyException {
        validateKey(managementKey, contentEncryptionAlg);
    }

    @Override // org.jose4j.jwa.Algorithm
    public boolean isAvailable() {
        return true;
    }
}
