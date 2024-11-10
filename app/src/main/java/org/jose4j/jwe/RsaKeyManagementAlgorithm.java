package org.jose4j.jwe;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.MGF1ParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import javax.crypto.spec.SecretKeySpec;
import org.jose4j.jca.ProviderContext;
import org.jose4j.jwa.CryptoPrimitive;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwx.Headers;
import org.jose4j.jwx.KeyValidationSupport;
import org.jose4j.keys.AesKey;
import org.jose4j.keys.KeyPersuasion;
import org.jose4j.lang.ByteUtil;
import org.jose4j.lang.ExceptionHelp;
import org.jose4j.lang.HashUtil;
import org.jose4j.lang.InvalidKeyException;
import org.jose4j.lang.JoseException;

/* loaded from: classes5.dex */
public class RsaKeyManagementAlgorithm extends WrappingKeyManagementAlgorithm implements KeyManagementAlgorithm {
    public RsaKeyManagementAlgorithm(String javaAlg, String alg) {
        super(javaAlg, alg);
        setKeyType("RSA");
        setKeyPersuasion(KeyPersuasion.ASYMMETRIC);
    }

    @Override // org.jose4j.jwe.KeyManagementAlgorithm
    public void validateEncryptionKey(Key managementKey, ContentEncryptionAlgorithm contentEncryptionAlg) throws InvalidKeyException {
        PublicKey pk = (PublicKey) KeyValidationSupport.castKey(managementKey, PublicKey.class);
        KeyValidationSupport.checkRsaKeySize(pk);
    }

    @Override // org.jose4j.jwe.KeyManagementAlgorithm
    public void validateDecryptionKey(Key managementKey, ContentEncryptionAlgorithm contentEncryptionAlg) throws InvalidKeyException {
        PrivateKey pk = (PrivateKey) KeyValidationSupport.castKey(managementKey, PrivateKey.class);
        KeyValidationSupport.checkRsaKeySize(pk);
    }

    @Override // org.jose4j.jwa.Algorithm
    public boolean isAvailable() {
        try {
            return CipherUtil.getCipher(getJavaAlgorithm(), null) != null;
        } catch (JoseException e) {
            return false;
        }
    }

    /* loaded from: classes5.dex */
    public static class RsaOaep extends RsaKeyManagementAlgorithm implements KeyManagementAlgorithm {
        public RsaOaep() {
            super("RSA/ECB/OAEPWithSHA-1AndMGF1Padding", KeyManagementAlgorithmIdentifiers.RSA_OAEP);
        }
    }

    /* loaded from: classes5.dex */
    public static class RsaOaep256 extends RsaKeyManagementAlgorithm implements KeyManagementAlgorithm {
        public RsaOaep256() {
            super("RSA/ECB/OAEPWithSHA-256AndMGF1Padding", KeyManagementAlgorithmIdentifiers.RSA_OAEP_256);
            setAlgorithmParameterSpec(new OAEPParameterSpec(HashUtil.SHA_256, "MGF1", MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT));
        }

        @Override // org.jose4j.jwe.RsaKeyManagementAlgorithm, org.jose4j.jwa.Algorithm
        public boolean isAvailable() {
            try {
                JsonWebKey jwk = JsonWebKey.Factory.newJwk("{\"kty\":\"RSA\",\"n\":\"sXchDaQebHnPiGvyDOAT4saGEUetSyo9MKLOoWFsueri23bOdgWp4Dy1WlUzewbgBHod5pcM9H95GQRV3JDXboIRROSBigeC5yjU1hGzHHyXss8UDprecbAYxknTcQkhslANGRUZmdTOQ5qTRsLAt6BTYuyvVRdhS8exSZEy_c4gs_7svlJJQ4H9_NxsiIoLwAEk7-Q3UXERGYw_75IDrGA84-lA_-Ct4eTlXHBIY2EaV7t7LjJaynVJCpkv4LKjTTAumiGUIuQhrNhZLuF_RJLqHpM2kgWFLU7-VTdL1VbC2tejvcI2BlMkEpk1BzBZI0KQB0GaDWFLN-aEAw3vRw\",\"e\":\"AQAB\"}");
                ContentEncryptionKeyDescriptor cekDesc = new ContentEncryptionKeyDescriptor(16, AesKey.ALGORITHM);
                ContentEncryptionKeys contentEncryptionKeys = manageForEncrypt(jwk.getKey(), cekDesc, null, null, new ProviderContext());
                return contentEncryptionKeys != null;
            } catch (JoseException e) {
                this.log.debug(getAlgorithmIdentifier() + " is not available due to " + ExceptionHelp.toStringWithCauses(e));
                return false;
            }
        }
    }

    /* loaded from: classes5.dex */
    public static class Rsa1_5 extends RsaKeyManagementAlgorithm implements KeyManagementAlgorithm {
        public Rsa1_5() {
            super("RSA/ECB/PKCS1Padding", KeyManagementAlgorithmIdentifiers.RSA1_5);
        }

        @Override // org.jose4j.jwe.WrappingKeyManagementAlgorithm, org.jose4j.jwe.KeyManagementAlgorithm
        public Key manageForDecrypt(CryptoPrimitive cryptoPrimitive, byte[] encryptedKey, ContentEncryptionKeyDescriptor cekDesc, Headers headers, ProviderContext providerContext) throws JoseException {
            String cekAlg = cekDesc.getContentEncryptionKeyAlgorithm();
            int expectedKeySize = cekDesc.getContentEncryptionKeyByteLength();
            Key randomKey = new SecretKeySpec(ByteUtil.randomBytes(expectedKeySize), cekAlg);
            try {
                Key unwrappedKey = unwrap(cryptoPrimitive, encryptedKey, providerContext, cekDesc);
                if (unwrappedKey.getEncoded().length != expectedKeySize) {
                    return randomKey;
                }
                return unwrappedKey;
            } catch (Exception e) {
                if (this.log.isDebugEnabled()) {
                    String flatStack = ExceptionHelp.toStringWithCausesAndAbbreviatedStack(e, JsonWebEncryption.class);
                    this.log.debug("Key unwrap/decrypt failed. Substituting a randomly generated CEK and proceeding. {}", flatStack);
                }
                return randomKey;
            }
        }
    }
}
