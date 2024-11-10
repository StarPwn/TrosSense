package org.jose4j.jwe;

import com.trossense.bl;
import org.jose4j.jca.ProviderContext;
import org.jose4j.jwa.AlgorithmInfo;
import org.jose4j.jwe.SimpleAeadCipher;
import org.jose4j.jwx.Headers;
import org.jose4j.keys.AesKey;
import org.jose4j.keys.KeyPersuasion;
import org.jose4j.lang.ByteUtil;
import org.jose4j.lang.JoseException;

/* loaded from: classes5.dex */
public class AesGcmContentEncryptionAlgorithm extends AlgorithmInfo implements ContentEncryptionAlgorithm {
    private static final int IV_BYTE_LENGTH = 12;
    private static final int TAG_BYTE_LENGTH = 16;
    private ContentEncryptionKeyDescriptor contentEncryptionKeyDescriptor;
    private SimpleAeadCipher simpleAeadCipher;

    public AesGcmContentEncryptionAlgorithm(String alg, int keyBitLength) {
        setAlgorithmIdentifier(alg);
        setJavaAlgorithm(SimpleAeadCipher.GCM_TRANSFORMATION_NAME);
        setKeyPersuasion(KeyPersuasion.SYMMETRIC);
        setKeyType(AesKey.ALGORITHM);
        this.contentEncryptionKeyDescriptor = new ContentEncryptionKeyDescriptor(ByteUtil.byteLength(keyBitLength), AesKey.ALGORITHM);
        this.simpleAeadCipher = new SimpleAeadCipher(getJavaAlgorithm(), 16);
    }

    @Override // org.jose4j.jwe.ContentEncryptionAlgorithm
    public ContentEncryptionKeyDescriptor getContentEncryptionKeyDescriptor() {
        return this.contentEncryptionKeyDescriptor;
    }

    @Override // org.jose4j.jwe.ContentEncryptionAlgorithm
    public ContentEncryptionParts encrypt(byte[] plaintext, byte[] aad, byte[] contentEncryptionKey, Headers headers, byte[] ivOverride, ProviderContext providerContext) throws JoseException {
        byte[] iv = InitializationVectorHelp.iv(12, ivOverride, providerContext.getSecureRandom());
        String cipherProvider = ContentEncryptionHelp.getCipherProvider(headers, providerContext);
        return encrypt(plaintext, aad, contentEncryptionKey, iv, cipherProvider);
    }

    public ContentEncryptionParts encrypt(byte[] plaintext, byte[] aad, byte[] contentEncryptionKey, byte[] iv, String provider) throws JoseException {
        AesKey cek = new AesKey(contentEncryptionKey);
        SimpleAeadCipher.CipherOutput encrypted = this.simpleAeadCipher.encrypt(cek, iv, plaintext, aad, provider);
        return new ContentEncryptionParts(iv, encrypted.getCiphertext(), encrypted.getTag());
    }

    @Override // org.jose4j.jwe.ContentEncryptionAlgorithm
    public byte[] decrypt(ContentEncryptionParts contentEncParts, byte[] aad, byte[] contentEncryptionKey, Headers headers, ProviderContext providerContext) throws JoseException {
        byte[] iv = contentEncParts.getIv();
        AesKey cek = new AesKey(contentEncryptionKey);
        byte[] ciphertext = contentEncParts.getCiphertext();
        byte[] tag = contentEncParts.getAuthenticationTag();
        String cipherProvider = ContentEncryptionHelp.getCipherProvider(headers, providerContext);
        return this.simpleAeadCipher.decrypt(cek, iv, ciphertext, tag, aad, cipherProvider);
    }

    @Override // org.jose4j.jwa.Algorithm
    public boolean isAvailable() {
        int keyByteLength = getContentEncryptionKeyDescriptor().getContentEncryptionKeyByteLength();
        return this.simpleAeadCipher.isAvailable(this.log, keyByteLength, 12, getAlgorithmIdentifier());
    }

    /* loaded from: classes5.dex */
    public static class Aes256Gcm extends AesGcmContentEncryptionAlgorithm {
        public Aes256Gcm() {
            super(ContentEncryptionAlgorithmIdentifiers.AES_256_GCM, 256);
        }
    }

    /* loaded from: classes5.dex */
    public static class Aes192Gcm extends AesGcmContentEncryptionAlgorithm {
        public Aes192Gcm() {
            super(ContentEncryptionAlgorithmIdentifiers.AES_192_GCM, bl.cq);
        }
    }

    /* loaded from: classes5.dex */
    public static class Aes128Gcm extends AesGcmContentEncryptionAlgorithm {
        public Aes128Gcm() {
            super(ContentEncryptionAlgorithmIdentifiers.AES_128_GCM, 128);
        }
    }
}
