package org.jose4j.jwe;

import com.trossense.bl;
import java.security.Key;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.jose4j.base64url.Base64Url;
import org.jose4j.jca.ProviderContext;
import org.jose4j.jwa.AlgorithmInfo;
import org.jose4j.jwa.CryptoPrimitive;
import org.jose4j.jwe.SimpleAeadCipher;
import org.jose4j.jwk.OctetSequenceJsonWebKey;
import org.jose4j.jwx.HeaderParameterNames;
import org.jose4j.jwx.Headers;
import org.jose4j.jwx.KeyValidationSupport;
import org.jose4j.keys.KeyPersuasion;
import org.jose4j.lang.ByteUtil;
import org.jose4j.lang.InvalidKeyException;
import org.jose4j.lang.JoseException;

/* loaded from: classes5.dex */
public class AesGcmKeyEncryptionAlgorithm extends AlgorithmInfo implements KeyManagementAlgorithm {
    private static final int IV_BYTE_LENGTH = 12;
    private static final int TAG_BYTE_LENGTH = 16;
    private int keyByteLength;
    private SimpleAeadCipher simpleAeadCipher;

    public AesGcmKeyEncryptionAlgorithm(String alg, int keyByteLength) {
        setAlgorithmIdentifier(alg);
        setJavaAlgorithm(SimpleAeadCipher.GCM_TRANSFORMATION_NAME);
        setKeyPersuasion(KeyPersuasion.SYMMETRIC);
        setKeyType(OctetSequenceJsonWebKey.KEY_TYPE);
        this.simpleAeadCipher = new SimpleAeadCipher(getJavaAlgorithm(), 16);
        this.keyByteLength = keyByteLength;
    }

    @Override // org.jose4j.jwe.KeyManagementAlgorithm
    public ContentEncryptionKeys manageForEncrypt(Key managementKey, ContentEncryptionKeyDescriptor cekDesc, Headers headers, byte[] cekOverride, ProviderContext providerContext) throws JoseException {
        byte[] iv;
        SecureRandom secureRandom = providerContext.getSecureRandom();
        byte[] cek = cekOverride == null ? ByteUtil.randomBytes(cekDesc.getContentEncryptionKeyByteLength(), secureRandom) : cekOverride;
        Base64Url base64Url = new Base64Url();
        String encodedIv = headers.getStringHeaderValue(HeaderParameterNames.INITIALIZATION_VECTOR);
        if (encodedIv == null) {
            byte[] iv2 = ByteUtil.randomBytes(12, secureRandom);
            headers.setStringHeaderValue(HeaderParameterNames.INITIALIZATION_VECTOR, base64Url.base64UrlEncode(iv2));
            iv = iv2;
        } else {
            byte[] iv3 = base64Url.base64UrlDecode(encodedIv);
            iv = iv3;
        }
        String cipherProvider = providerContext.getSuppliedKeyProviderContext().getCipherProvider();
        SimpleAeadCipher.CipherOutput encrypted = this.simpleAeadCipher.encrypt(managementKey, iv, cek, null, cipherProvider);
        byte[] encryptedKey = encrypted.getCiphertext();
        byte[] tag = encrypted.getTag();
        String encodedTag = base64Url.base64UrlEncode(tag);
        headers.setStringHeaderValue(HeaderParameterNames.AUTHENTICATION_TAG, encodedTag);
        return new ContentEncryptionKeys(cek, encryptedKey);
    }

    @Override // org.jose4j.jwe.KeyManagementAlgorithm
    public CryptoPrimitive prepareForDecrypt(Key managementKey, Headers headers, ProviderContext providerContext) throws JoseException {
        Base64Url base64Url = new Base64Url();
        String encodedIv = headers.getStringHeaderValue(HeaderParameterNames.INITIALIZATION_VECTOR);
        byte[] iv = base64Url.base64UrlDecode(encodedIv);
        String cipherProvider = providerContext.getSuppliedKeyProviderContext().getCipherProvider();
        Cipher cipher = this.simpleAeadCipher.getInitialisedCipher(managementKey, iv, 2, cipherProvider);
        return new CryptoPrimitive(cipher);
    }

    @Override // org.jose4j.jwe.KeyManagementAlgorithm
    public Key manageForDecrypt(CryptoPrimitive cryptoPrimitive, byte[] encryptedKey, ContentEncryptionKeyDescriptor cekDesc, Headers headers, ProviderContext providerContext) throws JoseException {
        Base64Url base64Url = new Base64Url();
        String encodedTag = headers.getStringHeaderValue(HeaderParameterNames.AUTHENTICATION_TAG);
        byte[] tag = base64Url.base64UrlDecode(encodedTag);
        Cipher cipher = cryptoPrimitive.getCipher();
        byte[] cek = this.simpleAeadCipher.decrypt(encryptedKey, tag, null, cipher);
        return new SecretKeySpec(cek, cekDesc.getContentEncryptionKeyAlgorithm());
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
        KeyValidationSupport.validateAesWrappingKey(managementKey, getAlgorithmIdentifier(), this.keyByteLength);
    }

    @Override // org.jose4j.jwa.Algorithm
    public boolean isAvailable() {
        return this.simpleAeadCipher.isAvailable(this.log, this.keyByteLength, 12, getAlgorithmIdentifier());
    }

    /* loaded from: classes5.dex */
    public static class Aes128Gcm extends AesGcmKeyEncryptionAlgorithm {
        public Aes128Gcm() {
            super(KeyManagementAlgorithmIdentifiers.A128GCMKW, ByteUtil.byteLength(128));
        }
    }

    /* loaded from: classes5.dex */
    public static class Aes192Gcm extends AesGcmKeyEncryptionAlgorithm {
        public Aes192Gcm() {
            super(KeyManagementAlgorithmIdentifiers.A192GCMKW, ByteUtil.byteLength(bl.cq));
        }
    }

    /* loaded from: classes5.dex */
    public static class Aes256Gcm extends AesGcmKeyEncryptionAlgorithm {
        public Aes256Gcm() {
            super(KeyManagementAlgorithmIdentifiers.A256GCMKW, ByteUtil.byteLength(256));
        }
    }
}
