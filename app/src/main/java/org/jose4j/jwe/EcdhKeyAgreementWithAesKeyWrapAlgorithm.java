package org.jose4j.jwe;

import java.security.Key;
import javax.crypto.spec.SecretKeySpec;
import org.jose4j.jca.ProviderContext;
import org.jose4j.jwa.AlgorithmInfo;
import org.jose4j.jwa.CryptoPrimitive;
import org.jose4j.jwe.AesKeyWrapManagementAlgorithm;
import org.jose4j.jwx.Headers;
import org.jose4j.keys.AesKey;
import org.jose4j.keys.KeyPersuasion;
import org.jose4j.lang.ByteUtil;
import org.jose4j.lang.InvalidKeyException;
import org.jose4j.lang.JoseException;

/* loaded from: classes5.dex */
public class EcdhKeyAgreementWithAesKeyWrapAlgorithm extends AlgorithmInfo implements KeyManagementAlgorithm {
    private EcdhKeyAgreementAlgorithm ecdh;
    private AesKeyWrapManagementAlgorithm keyWrap;
    private ContentEncryptionKeyDescriptor keyWrapKeyDescriptor;

    public EcdhKeyAgreementWithAesKeyWrapAlgorithm(String alg, AesKeyWrapManagementAlgorithm keyWrapAlgorithm) {
        setAlgorithmIdentifier(alg);
        setJavaAlgorithm("N/A");
        setKeyType("EC");
        setKeyPersuasion(KeyPersuasion.ASYMMETRIC);
        this.keyWrap = keyWrapAlgorithm;
        this.ecdh = new EcdhKeyAgreementAlgorithm("alg");
        this.keyWrapKeyDescriptor = new ContentEncryptionKeyDescriptor(keyWrapAlgorithm.getKeyByteLength(), AesKey.ALGORITHM);
    }

    @Override // org.jose4j.jwe.KeyManagementAlgorithm
    public ContentEncryptionKeys manageForEncrypt(Key managementKey, ContentEncryptionKeyDescriptor cekDesc, Headers headers, byte[] cekOverride, ProviderContext providerContext) throws JoseException {
        ContentEncryptionKeys agreedKeys = this.ecdh.manageForEncrypt(managementKey, this.keyWrapKeyDescriptor, headers, (byte[]) null, providerContext);
        String contentEncryptionKeyAlgorithm = this.keyWrapKeyDescriptor.getContentEncryptionKeyAlgorithm();
        Key agreedKey = new SecretKeySpec(agreedKeys.getContentEncryptionKey(), contentEncryptionKeyAlgorithm);
        return this.keyWrap.manageForEncrypt(agreedKey, cekDesc, headers, cekOverride, providerContext);
    }

    @Override // org.jose4j.jwe.KeyManagementAlgorithm
    public CryptoPrimitive prepareForDecrypt(Key managementKey, Headers headers, ProviderContext providerContext) throws JoseException {
        return this.ecdh.prepareForDecrypt(managementKey, headers, providerContext);
    }

    @Override // org.jose4j.jwe.KeyManagementAlgorithm
    public Key manageForDecrypt(CryptoPrimitive cryptoPrimitive, byte[] encryptedKey, ContentEncryptionKeyDescriptor cekDesc, Headers headers, ProviderContext providerContext) throws JoseException {
        Key agreedKey = this.ecdh.manageForDecrypt(cryptoPrimitive, ByteUtil.EMPTY_BYTES, this.keyWrapKeyDescriptor, headers, providerContext);
        CryptoPrimitive wrapCryptoPrimitive = this.keyWrap.prepareForDecrypt(agreedKey, headers, providerContext);
        return this.keyWrap.manageForDecrypt(wrapCryptoPrimitive, encryptedKey, cekDesc, headers, providerContext);
    }

    @Override // org.jose4j.jwe.KeyManagementAlgorithm
    public void validateEncryptionKey(Key managementKey, ContentEncryptionAlgorithm contentEncryptionAlg) throws InvalidKeyException {
        this.ecdh.validateEncryptionKey(managementKey, contentEncryptionAlg);
    }

    @Override // org.jose4j.jwe.KeyManagementAlgorithm
    public void validateDecryptionKey(Key managementKey, ContentEncryptionAlgorithm contentEncryptionAlg) throws InvalidKeyException {
        this.ecdh.validateDecryptionKey(managementKey, contentEncryptionAlg);
    }

    @Override // org.jose4j.jwa.Algorithm
    public boolean isAvailable() {
        return this.ecdh.isAvailable() && this.keyWrap.isAvailable();
    }

    /* loaded from: classes5.dex */
    public static class EcdhKeyAgreementWithAes128KeyWrapAlgorithm extends EcdhKeyAgreementWithAesKeyWrapAlgorithm implements KeyManagementAlgorithm {
        public EcdhKeyAgreementWithAes128KeyWrapAlgorithm() {
            super(KeyManagementAlgorithmIdentifiers.ECDH_ES_A128KW, new AesKeyWrapManagementAlgorithm.Aes128().setUseGeneralProviderContext());
        }
    }

    /* loaded from: classes5.dex */
    public static class EcdhKeyAgreementWithAes192KeyWrapAlgorithm extends EcdhKeyAgreementWithAesKeyWrapAlgorithm implements KeyManagementAlgorithm {
        public EcdhKeyAgreementWithAes192KeyWrapAlgorithm() {
            super(KeyManagementAlgorithmIdentifiers.ECDH_ES_A192KW, new AesKeyWrapManagementAlgorithm.Aes192().setUseGeneralProviderContext());
        }
    }

    /* loaded from: classes5.dex */
    public static class EcdhKeyAgreementWithAes256KeyWrapAlgorithm extends EcdhKeyAgreementWithAesKeyWrapAlgorithm implements KeyManagementAlgorithm {
        public EcdhKeyAgreementWithAes256KeyWrapAlgorithm() {
            super(KeyManagementAlgorithmIdentifiers.ECDH_ES_A256KW, new AesKeyWrapManagementAlgorithm.Aes256().setUseGeneralProviderContext());
        }
    }
}
