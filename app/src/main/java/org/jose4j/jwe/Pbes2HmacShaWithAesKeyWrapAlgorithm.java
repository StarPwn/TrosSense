package org.jose4j.jwe;

import java.security.Key;
import javax.crypto.spec.SecretKeySpec;
import org.jose4j.base64url.Base64Url;
import org.jose4j.jca.ProviderContext;
import org.jose4j.jwa.AlgorithmInfo;
import org.jose4j.jwa.CryptoPrimitive;
import org.jose4j.jwe.AesKeyWrapManagementAlgorithm;
import org.jose4j.jwe.kdf.PasswordBasedKeyDerivationFunction2;
import org.jose4j.jwx.HeaderParameterNames;
import org.jose4j.jwx.Headers;
import org.jose4j.jwx.KeyValidationSupport;
import org.jose4j.keys.AesKey;
import org.jose4j.keys.KeyPersuasion;
import org.jose4j.keys.PbkdfKey;
import org.jose4j.lang.ByteUtil;
import org.jose4j.lang.InvalidKeyException;
import org.jose4j.lang.JoseException;
import org.jose4j.lang.StringUtil;
import org.jose4j.mac.MacUtil;

/* loaded from: classes5.dex */
public class Pbes2HmacShaWithAesKeyWrapAlgorithm extends AlgorithmInfo implements KeyManagementAlgorithm {
    private static final byte[] ZERO_BYTE = {0};
    private long defaultIterationCount = 65536;
    private int defaultSaltByteLength = 12;
    private AesKeyWrapManagementAlgorithm keyWrap;
    private ContentEncryptionKeyDescriptor keyWrapKeyDescriptor;
    private PasswordBasedKeyDerivationFunction2 pbkdf2;

    public Pbes2HmacShaWithAesKeyWrapAlgorithm(String alg, String hmacAlg, AesKeyWrapManagementAlgorithm keyWrapAlg) {
        setAlgorithmIdentifier(alg);
        setJavaAlgorithm("n/a");
        this.pbkdf2 = new PasswordBasedKeyDerivationFunction2(hmacAlg);
        setKeyPersuasion(KeyPersuasion.SYMMETRIC);
        setKeyType(PbkdfKey.ALGORITHM);
        this.keyWrap = keyWrapAlg;
        this.keyWrapKeyDescriptor = new ContentEncryptionKeyDescriptor(this.keyWrap.getKeyByteLength(), AesKey.ALGORITHM);
    }

    @Override // org.jose4j.jwe.KeyManagementAlgorithm
    public ContentEncryptionKeys manageForEncrypt(Key managementKey, ContentEncryptionKeyDescriptor cekDesc, Headers headers, byte[] cekOverride, ProviderContext providerContext) throws JoseException {
        Key derivedKey = deriveForEncrypt(managementKey, headers, providerContext);
        return this.keyWrap.manageForEncrypt(derivedKey, cekDesc, headers, cekOverride, providerContext);
    }

    protected Key deriveForEncrypt(Key managementKey, Headers headers, ProviderContext providerContext) throws JoseException {
        byte[] saltInput;
        Long iterationCount = headers.getLongHeaderValue(HeaderParameterNames.PBES2_ITERATION_COUNT);
        if (iterationCount == null) {
            iterationCount = Long.valueOf(this.defaultIterationCount);
            headers.setObjectHeaderValue(HeaderParameterNames.PBES2_ITERATION_COUNT, iterationCount);
        }
        if (iterationCount.longValue() < 1000) {
            throw new JoseException("iteration count (p2c=" + iterationCount + ") cannot be less than 1000 (and should probably be considerably more)");
        }
        String saltInputString = headers.getStringHeaderValue(HeaderParameterNames.PBES2_SALT_INPUT);
        Base64Url base64Url = new Base64Url();
        if (saltInputString == null) {
            saltInput = ByteUtil.randomBytes(this.defaultSaltByteLength, providerContext.getSecureRandom());
            headers.setStringHeaderValue(HeaderParameterNames.PBES2_SALT_INPUT, base64Url.base64UrlEncode(saltInput));
        } else {
            saltInput = base64Url.base64UrlDecode(saltInputString);
        }
        if (saltInput.length < 8) {
            throw new JoseException("A p2s salt input value containing 8 or more octets MUST be used.");
        }
        return deriveKey(managementKey, iterationCount, saltInput, providerContext);
    }

    @Override // org.jose4j.jwe.KeyManagementAlgorithm
    public CryptoPrimitive prepareForDecrypt(Key managementKey, Headers headers, ProviderContext providerContext) throws JoseException {
        Long iterationCount = headers.getLongHeaderValue(HeaderParameterNames.PBES2_ITERATION_COUNT);
        String saltInputString = headers.getStringHeaderValue(HeaderParameterNames.PBES2_SALT_INPUT);
        Base64Url base64Url = new Base64Url();
        byte[] saltInput = base64Url.base64UrlDecode(saltInputString);
        Key derivedKey = deriveKey(managementKey, iterationCount, saltInput, providerContext);
        return new CryptoPrimitive(derivedKey);
    }

    @Override // org.jose4j.jwe.KeyManagementAlgorithm
    public Key manageForDecrypt(CryptoPrimitive cryptoPrimitive, byte[] encryptedKey, ContentEncryptionKeyDescriptor cekDesc, Headers headers, ProviderContext providerContext) throws JoseException {
        Key derivedKey = cryptoPrimitive.getKey();
        CryptoPrimitive wrapCryptoPrimitive = this.keyWrap.prepareForDecrypt(derivedKey, headers, providerContext);
        return this.keyWrap.manageForDecrypt(wrapCryptoPrimitive, encryptedKey, cekDesc, headers, providerContext);
    }

    private Key deriveKey(Key managementKey, Long iterationCount, byte[] saltInput, ProviderContext providerContext) throws JoseException {
        byte[] salt = ByteUtil.concat(StringUtil.getBytesUtf8(getAlgorithmIdentifier()), ZERO_BYTE, saltInput);
        int dkLen = this.keyWrapKeyDescriptor.getContentEncryptionKeyByteLength();
        String macProvider = providerContext.getSuppliedKeyProviderContext().getMacProvider();
        byte[] derivedKeyBytes = this.pbkdf2.derive(managementKey.getEncoded(), salt, iterationCount.intValue(), dkLen, macProvider);
        return new SecretKeySpec(derivedKeyBytes, this.keyWrapKeyDescriptor.getContentEncryptionKeyAlgorithm());
    }

    @Override // org.jose4j.jwe.KeyManagementAlgorithm
    public void validateEncryptionKey(Key managementKey, ContentEncryptionAlgorithm contentEncryptionAlg) throws InvalidKeyException {
        validateKey(managementKey);
    }

    @Override // org.jose4j.jwe.KeyManagementAlgorithm
    public void validateDecryptionKey(Key managementKey, ContentEncryptionAlgorithm contentEncryptionAlg) throws InvalidKeyException {
        validateKey(managementKey);
    }

    public void validateKey(Key managementKey) throws InvalidKeyException {
        KeyValidationSupport.notNull(managementKey);
    }

    @Override // org.jose4j.jwa.Algorithm
    public boolean isAvailable() {
        return this.keyWrap.isAvailable();
    }

    public long getDefaultIterationCount() {
        return this.defaultIterationCount;
    }

    public void setDefaultIterationCount(long defaultIterationCount) {
        this.defaultIterationCount = defaultIterationCount;
    }

    public int getDefaultSaltByteLength() {
        return this.defaultSaltByteLength;
    }

    public void setDefaultSaltByteLength(int defaultSaltByteLength) {
        this.defaultSaltByteLength = defaultSaltByteLength;
    }

    /* loaded from: classes5.dex */
    public static class HmacSha256Aes128 extends Pbes2HmacShaWithAesKeyWrapAlgorithm {
        public HmacSha256Aes128() {
            super(KeyManagementAlgorithmIdentifiers.PBES2_HS256_A128KW, MacUtil.HMAC_SHA256, new AesKeyWrapManagementAlgorithm.Aes128().setUseGeneralProviderContext());
        }
    }

    /* loaded from: classes5.dex */
    public static class HmacSha384Aes192 extends Pbes2HmacShaWithAesKeyWrapAlgorithm {
        public HmacSha384Aes192() {
            super(KeyManagementAlgorithmIdentifiers.PBES2_HS384_A192KW, MacUtil.HMAC_SHA384, new AesKeyWrapManagementAlgorithm.Aes192().setUseGeneralProviderContext());
        }
    }

    /* loaded from: classes5.dex */
    public static class HmacSha512Aes256 extends Pbes2HmacShaWithAesKeyWrapAlgorithm {
        public HmacSha512Aes256() {
            super(KeyManagementAlgorithmIdentifiers.PBES2_HS512_A256KW, MacUtil.HMAC_SHA512, new AesKeyWrapManagementAlgorithm.Aes256().setUseGeneralProviderContext());
        }
    }
}
