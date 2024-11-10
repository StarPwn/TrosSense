package org.jose4j.jwe;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;
import org.jose4j.jca.ProviderContext;
import org.jose4j.jwa.AlgorithmInfo;
import org.jose4j.jwa.CryptoPrimitive;
import org.jose4j.jwx.Headers;
import org.jose4j.lang.ByteUtil;
import org.jose4j.lang.IntegrityException;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: classes5.dex */
public abstract class WrappingKeyManagementAlgorithm extends AlgorithmInfo implements KeyManagementAlgorithm {
    private AlgorithmParameterSpec algorithmParameterSpec;
    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected boolean useSuppliedKeyProviderContext = true;

    public WrappingKeyManagementAlgorithm(String javaAlg, String alg) {
        setJavaAlgorithm(javaAlg);
        setAlgorithmIdentifier(alg);
    }

    public void setAlgorithmParameterSpec(AlgorithmParameterSpec algorithmParameterSpec) {
        this.algorithmParameterSpec = algorithmParameterSpec;
    }

    @Override // org.jose4j.jwe.KeyManagementAlgorithm
    public ContentEncryptionKeys manageForEncrypt(Key managementKey, ContentEncryptionKeyDescriptor cekDesc, Headers headers, byte[] cekOverride, ProviderContext providerContext) throws JoseException {
        byte[] contentEncryptionKey = cekOverride == null ? ByteUtil.randomBytes(cekDesc.getContentEncryptionKeyByteLength()) : cekOverride;
        return manageForEnc(managementKey, cekDesc, contentEncryptionKey, providerContext);
    }

    protected ContentEncryptionKeys manageForEnc(Key managementKey, ContentEncryptionKeyDescriptor cekDesc, byte[] contentEncryptionKey, ProviderContext providerContext) throws JoseException {
        ProviderContext.Context ctx = chooseContext(providerContext);
        String provider = ctx.getCipherProvider();
        Cipher cipher = CipherUtil.getCipher(getJavaAlgorithm(), provider);
        try {
            initCipher(cipher, 3, managementKey);
            String contentEncryptionKeyAlgorithm = cekDesc.getContentEncryptionKeyAlgorithm();
            byte[] encryptedKey = cipher.wrap(new SecretKeySpec(contentEncryptionKey, contentEncryptionKeyAlgorithm));
            return new ContentEncryptionKeys(contentEncryptionKey, encryptedKey);
        } catch (InvalidAlgorithmParameterException e) {
            e = e;
            throw new JoseException("Unable to encrypt (" + cipher.getAlgorithm() + ") the Content Encryption Key: " + e, e);
        } catch (InvalidKeyException e2) {
            throw new org.jose4j.lang.InvalidKeyException("Unable to encrypt (" + cipher.getAlgorithm() + ") the Content Encryption Key: " + e2, e2);
        } catch (IllegalBlockSizeException e3) {
            e = e3;
            throw new JoseException("Unable to encrypt (" + cipher.getAlgorithm() + ") the Content Encryption Key: " + e, e);
        }
    }

    void initCipher(Cipher cipher, int mode, Key key) throws InvalidAlgorithmParameterException, InvalidKeyException {
        if (this.algorithmParameterSpec == null) {
            cipher.init(mode, key);
        } else {
            cipher.init(mode, key, this.algorithmParameterSpec);
        }
    }

    @Override // org.jose4j.jwe.KeyManagementAlgorithm
    public CryptoPrimitive prepareForDecrypt(Key managementKey, Headers headers, ProviderContext providerContext) throws JoseException {
        ProviderContext.Context ctx = chooseContext(providerContext);
        String provider = ctx.getCipherProvider();
        Cipher cipher = CipherUtil.getCipher(getJavaAlgorithm(), provider);
        int mode = ctx.getKeyDecipherModeOverride() == ProviderContext.KeyDecipherMode.DECRYPT ? 2 : 4;
        try {
            initCipher(cipher, mode, managementKey);
            return new CryptoPrimitive(cipher);
        } catch (InvalidAlgorithmParameterException e) {
            throw new JoseException("Unable to initialize cipher (" + cipher.getAlgorithm() + ") for key unwrap/decrypt - " + e, e);
        } catch (InvalidKeyException e2) {
            throw new org.jose4j.lang.InvalidKeyException("Unable to initialize cipher (" + cipher.getAlgorithm() + ") for key unwrap/decrypt - " + e2, e2);
        }
    }

    private ProviderContext.Context chooseContext(ProviderContext providerContext) {
        return this.useSuppliedKeyProviderContext ? providerContext.getSuppliedKeyProviderContext() : providerContext.getGeneralProviderContext();
    }

    public Key manageForDecrypt(CryptoPrimitive cryptoPrimitive, byte[] encryptedKey, ContentEncryptionKeyDescriptor cekDesc, Headers headers, ProviderContext providerContext) throws JoseException {
        try {
            return unwrap(cryptoPrimitive, encryptedKey, providerContext, cekDesc);
        } catch (Exception e) {
            throw new IntegrityException(getAlgorithmIdentifier() + " key unwrap/decrypt failed.", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Key unwrap(CryptoPrimitive cryptoPrimitive, byte[] encryptedKey, ProviderContext providerContext, ContentEncryptionKeyDescriptor cekDesc) throws NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        ProviderContext.Context ctx = chooseContext(providerContext);
        Cipher cipher = cryptoPrimitive.getCipher();
        String cekAlg = cekDesc.getContentEncryptionKeyAlgorithm();
        if (ctx.getKeyDecipherModeOverride() == ProviderContext.KeyDecipherMode.DECRYPT) {
            byte[] clear = cipher.doFinal(encryptedKey);
            return new SecretKeySpec(clear, cekAlg);
        }
        return cipher.unwrap(encryptedKey, cekAlg, 3);
    }
}
