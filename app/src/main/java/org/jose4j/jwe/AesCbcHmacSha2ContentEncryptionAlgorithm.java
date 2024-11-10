package org.jose4j.jwe;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import org.jose4j.base64url.Base64Url;
import org.jose4j.jca.ProviderContext;
import org.jose4j.jwa.AlgorithmInfo;
import org.jose4j.jwx.Headers;
import org.jose4j.keys.AesKey;
import org.jose4j.keys.HmacKey;
import org.jose4j.keys.KeyPersuasion;
import org.jose4j.lang.ByteUtil;
import org.jose4j.lang.IntegrityException;
import org.jose4j.lang.JoseException;
import org.jose4j.mac.MacUtil;

/* loaded from: classes5.dex */
public class AesCbcHmacSha2ContentEncryptionAlgorithm extends AlgorithmInfo implements ContentEncryptionAlgorithm {
    public static final int IV_BYTE_LENGTH = 16;
    private final ContentEncryptionKeyDescriptor contentEncryptionKeyDescriptor;
    private final String hmacJavaAlgorithm;
    private final int tagTruncationLength;

    public AesCbcHmacSha2ContentEncryptionAlgorithm(String alg, int cekByteLen, String javaHmacAlg, int tagTruncationLength) {
        setAlgorithmIdentifier(alg);
        this.contentEncryptionKeyDescriptor = new ContentEncryptionKeyDescriptor(cekByteLen, AesKey.ALGORITHM);
        this.hmacJavaAlgorithm = javaHmacAlg;
        this.tagTruncationLength = tagTruncationLength;
        setJavaAlgorithm("AES/CBC/PKCS5Padding");
        setKeyPersuasion(KeyPersuasion.SYMMETRIC);
        setKeyType(AesKey.ALGORITHM);
    }

    public String getHmacJavaAlgorithm() {
        return this.hmacJavaAlgorithm;
    }

    public int getTagTruncationLength() {
        return this.tagTruncationLength;
    }

    @Override // org.jose4j.jwe.ContentEncryptionAlgorithm
    public ContentEncryptionKeyDescriptor getContentEncryptionKeyDescriptor() {
        return this.contentEncryptionKeyDescriptor;
    }

    @Override // org.jose4j.jwe.ContentEncryptionAlgorithm
    public ContentEncryptionParts encrypt(byte[] plaintext, byte[] aad, byte[] contentEncryptionKey, Headers headers, byte[] ivOverride, ProviderContext providerContext) throws JoseException {
        byte[] iv = InitializationVectorHelp.iv(16, ivOverride, providerContext.getSecureRandom());
        return encrypt(plaintext, aad, contentEncryptionKey, iv, headers, providerContext);
    }

    ContentEncryptionParts encrypt(byte[] plaintext, byte[] aad, byte[] key, byte[] iv, Headers headers, ProviderContext providerContext) throws JoseException {
        Key hmacKey = new HmacKey(ByteUtil.leftHalf(key));
        Key encryptionKey = new AesKey(ByteUtil.rightHalf(key));
        String cipherProvider = ContentEncryptionHelp.getCipherProvider(headers, providerContext);
        Cipher cipher = CipherUtil.getCipher(getJavaAlgorithm(), cipherProvider);
        try {
            cipher.init(1, encryptionKey, new IvParameterSpec(iv));
            try {
                byte[] cipherText = cipher.doFinal(plaintext);
                String macProvider = ContentEncryptionHelp.getMacProvider(headers, providerContext);
                Mac mac = MacUtil.getInitializedMac(getHmacJavaAlgorithm(), hmacKey, macProvider);
                byte[] al = getAdditionalAuthenticatedDataLengthBytes(aad);
                byte[] authenticationTagInput = ByteUtil.concat(aad, iv, cipherText, al);
                byte[] authenticationTag = mac.doFinal(authenticationTagInput);
                return new ContentEncryptionParts(iv, cipherText, ByteUtil.subArray(authenticationTag, 0, getTagTruncationLength()));
            } catch (BadPaddingException | IllegalBlockSizeException e) {
                throw new JoseException(e.toString(), e);
            }
        } catch (InvalidAlgorithmParameterException e2) {
            throw new JoseException(e2.toString(), e2);
        } catch (InvalidKeyException e3) {
            throw new JoseException("Invalid key for " + getJavaAlgorithm(), e3);
        }
    }

    @Override // org.jose4j.jwe.ContentEncryptionAlgorithm
    public byte[] decrypt(ContentEncryptionParts contentEncryptionParts, byte[] aad, byte[] contentEncryptionKey, Headers headers, ProviderContext providerContext) throws JoseException {
        String cipherProvider = ContentEncryptionHelp.getCipherProvider(headers, providerContext);
        String macProvider = ContentEncryptionHelp.getMacProvider(headers, providerContext);
        byte[] iv = contentEncryptionParts.getIv();
        byte[] ciphertext = contentEncryptionParts.getCiphertext();
        byte[] authenticationTag = contentEncryptionParts.getAuthenticationTag();
        byte[] al = getAdditionalAuthenticatedDataLengthBytes(aad);
        byte[] authenticationTagInput = ByteUtil.concat(aad, iv, ciphertext, al);
        Key hmacKey = new HmacKey(ByteUtil.leftHalf(contentEncryptionKey));
        Mac mac = MacUtil.getInitializedMac(getHmacJavaAlgorithm(), hmacKey, macProvider);
        byte[] calculatedAuthenticationTag = mac.doFinal(authenticationTagInput);
        boolean tagMatch = ByteUtil.secureEquals(authenticationTag, ByteUtil.subArray(calculatedAuthenticationTag, 0, getTagTruncationLength()));
        if (!tagMatch) {
            Base64Url base64Url = new Base64Url();
            String encTag = base64Url.base64UrlEncode(authenticationTag);
            throw new IntegrityException("Authentication tag check failed. Message=" + encTag);
        }
        Key encryptionKey = new AesKey(ByteUtil.rightHalf(contentEncryptionKey));
        Cipher cipher = CipherUtil.getCipher(getJavaAlgorithm(), cipherProvider);
        try {
            cipher.init(2, encryptionKey, new IvParameterSpec(iv));
            try {
                return cipher.doFinal(ciphertext);
            } catch (BadPaddingException | IllegalBlockSizeException e) {
                String cipherProvider2 = e.toString();
                throw new JoseException(cipherProvider2, e);
            }
        } catch (InvalidAlgorithmParameterException e2) {
            throw new JoseException(e2.toString(), e2);
        } catch (InvalidKeyException e3) {
            throw new JoseException("Invalid key for " + getJavaAlgorithm(), e3);
        }
    }

    private byte[] getAdditionalAuthenticatedDataLengthBytes(byte[] additionalAuthenticatedData) {
        long aadLength = ByteUtil.bitLength(additionalAuthenticatedData);
        return ByteUtil.getBytes(aadLength);
    }

    @Override // org.jose4j.jwa.Algorithm
    public boolean isAvailable() {
        int contentEncryptionKeyByteLength = getContentEncryptionKeyDescriptor().getContentEncryptionKeyByteLength();
        int aesByteKeyLength = contentEncryptionKeyByteLength / 2;
        return CipherStrengthSupport.isAvailable(getJavaAlgorithm(), aesByteKeyLength);
    }

    /* loaded from: classes5.dex */
    public static class Aes128CbcHmacSha256 extends AesCbcHmacSha2ContentEncryptionAlgorithm implements ContentEncryptionAlgorithm {
        public Aes128CbcHmacSha256() {
            super(ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256, 32, MacUtil.HMAC_SHA256, 16);
        }
    }

    /* loaded from: classes5.dex */
    public static class Aes192CbcHmacSha384 extends AesCbcHmacSha2ContentEncryptionAlgorithm implements ContentEncryptionAlgorithm {
        public Aes192CbcHmacSha384() {
            super(ContentEncryptionAlgorithmIdentifiers.AES_192_CBC_HMAC_SHA_384, 48, MacUtil.HMAC_SHA384, 24);
        }
    }

    /* loaded from: classes5.dex */
    public static class Aes256CbcHmacSha512 extends AesCbcHmacSha2ContentEncryptionAlgorithm implements ContentEncryptionAlgorithm {
        public Aes256CbcHmacSha512() {
            super(ContentEncryptionAlgorithmIdentifiers.AES_256_CBC_HMAC_SHA_512, 64, MacUtil.HMAC_SHA512, 32);
        }
    }
}
