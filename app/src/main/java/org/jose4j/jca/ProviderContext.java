package org.jose4j.jca;

import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

/* loaded from: classes5.dex */
public class ProviderContext {
    private SecureRandom secureRandom;
    private Context suppliedKeyProviderContext = new Context();
    private Context generalProviderContext = new Context();

    /* loaded from: classes5.dex */
    public enum KeyDecipherMode {
        UNWRAP,
        DECRYPT
    }

    public Context getSuppliedKeyProviderContext() {
        return this.suppliedKeyProviderContext;
    }

    public Context getGeneralProviderContext() {
        return this.generalProviderContext;
    }

    public SecureRandom getSecureRandom() {
        return this.secureRandom;
    }

    public void setSecureRandom(SecureRandom secureRandom) {
        this.secureRandom = secureRandom;
    }

    /* loaded from: classes5.dex */
    public class Context {
        private String cipherProvider;
        private String generalProvider;
        private String keyAgreementProvider;
        private KeyDecipherMode keyDecipherModeOverride;
        private String keyFactoryProvider;
        private String keyPairGeneratorProvider;
        private String macProvider;
        private String messageDigestProvider;
        private SignatureAlgorithmOverride signatureAlgorithmOverride;
        private String signatureProvider;

        public Context() {
        }

        public String getGeneralProvider() {
            return this.generalProvider;
        }

        public void setGeneralProvider(String generalProvider) {
            this.generalProvider = generalProvider;
        }

        public String getKeyPairGeneratorProvider() {
            return select(this.keyPairGeneratorProvider);
        }

        public void setKeyPairGeneratorProvider(String keyPairGeneratorProvider) {
            this.keyPairGeneratorProvider = keyPairGeneratorProvider;
        }

        public String getKeyAgreementProvider() {
            return select(this.keyAgreementProvider);
        }

        public void setKeyAgreementProvider(String keyAgreementProvider) {
            this.keyAgreementProvider = keyAgreementProvider;
        }

        public String getCipherProvider() {
            return select(this.cipherProvider);
        }

        public void setCipherProvider(String cipherProvider) {
            this.cipherProvider = cipherProvider;
        }

        public KeyDecipherMode getKeyDecipherModeOverride() {
            return this.keyDecipherModeOverride;
        }

        public void setKeyDecipherModeOverride(KeyDecipherMode keyDecipherModeOverride) {
            this.keyDecipherModeOverride = keyDecipherModeOverride;
        }

        public String getSignatureProvider() {
            return select(this.signatureProvider);
        }

        public void setSignatureProvider(String signatureProvider) {
            this.signatureProvider = signatureProvider;
        }

        public SignatureAlgorithmOverride getSignatureAlgorithmOverride() {
            return this.signatureAlgorithmOverride;
        }

        public void setSignatureAlgorithmOverride(SignatureAlgorithmOverride signatureAlgorithmOverride) {
            this.signatureAlgorithmOverride = signatureAlgorithmOverride;
        }

        public String getMacProvider() {
            return select(this.macProvider);
        }

        public void setMacProvider(String macProvider) {
            this.macProvider = macProvider;
        }

        public String getMessageDigestProvider() {
            return select(this.messageDigestProvider);
        }

        public void setMessageDigestProvider(String messageDigestProvider) {
            this.messageDigestProvider = messageDigestProvider;
        }

        public String getKeyFactoryProvider() {
            return select(this.keyFactoryProvider);
        }

        public void setKeyFactoryProvider(String keyFactoryProvider) {
            this.keyFactoryProvider = keyFactoryProvider;
        }

        private String select(String specificValue) {
            return specificValue == null ? this.generalProvider : specificValue;
        }
    }

    /* loaded from: classes5.dex */
    public static class SignatureAlgorithmOverride {
        private AlgorithmParameterSpec AlgorithmParameterSpec;
        private String algorithmName;

        public SignatureAlgorithmOverride(String algorithmName, AlgorithmParameterSpec aps) {
            this.algorithmName = algorithmName;
            this.AlgorithmParameterSpec = aps;
        }

        public String getAlgorithmName() {
            return this.algorithmName;
        }

        public AlgorithmParameterSpec getAlgorithmParameterSpec() {
            return this.AlgorithmParameterSpec;
        }
    }
}
