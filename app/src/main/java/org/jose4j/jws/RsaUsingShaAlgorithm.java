package org.jose4j.jws;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import java.util.Set;
import org.jose4j.jwx.KeyValidationSupport;
import org.jose4j.lang.InvalidKeyException;

/* loaded from: classes5.dex */
public class RsaUsingShaAlgorithm extends BaseSignatureAlgorithm implements JsonWebSignatureAlgorithm {
    static final String MGF1 = "MGF1";
    public static final String RSASSA_PSS = "RSASSA-PSS";
    static final int TRAILER = 1;

    public RsaUsingShaAlgorithm(String id, String javaAlgo) {
        super(id, javaAlgo, "RSA");
    }

    @Override // org.jose4j.jws.BaseSignatureAlgorithm
    public void validatePublicKey(PublicKey key) throws InvalidKeyException {
        KeyValidationSupport.checkRsaKeySize(key);
    }

    @Override // org.jose4j.jws.BaseSignatureAlgorithm
    public void validatePrivateKey(PrivateKey privateKey) throws InvalidKeyException {
        KeyValidationSupport.checkRsaKeySize(privateKey);
    }

    /* loaded from: classes5.dex */
    public static class RsaPssSha256 extends RsaUsingShaAlgorithm {
        public RsaPssSha256() {
            super(AlgorithmIdentifiers.RSA_PSS_USING_SHA256, choosePssAlgorithmName("SHA256withRSAandMGF1"));
            if (getJavaAlgorithm().equals(RsaUsingShaAlgorithm.RSASSA_PSS)) {
                MGF1ParameterSpec mgf1pec = MGF1ParameterSpec.SHA256;
                PSSParameterSpec pssSpec = new PSSParameterSpec(mgf1pec.getDigestAlgorithm(), RsaUsingShaAlgorithm.MGF1, mgf1pec, 32, 1);
                setAlgorithmParameterSpec(pssSpec);
            }
        }
    }

    /* loaded from: classes5.dex */
    public static class RsaPssSha384 extends RsaUsingShaAlgorithm {
        public RsaPssSha384() {
            super(AlgorithmIdentifiers.RSA_PSS_USING_SHA384, choosePssAlgorithmName("SHA384withRSAandMGF1"));
            if (getJavaAlgorithm().equals(RsaUsingShaAlgorithm.RSASSA_PSS)) {
                MGF1ParameterSpec mgf1pec = MGF1ParameterSpec.SHA384;
                PSSParameterSpec pssSpec = new PSSParameterSpec(mgf1pec.getDigestAlgorithm(), RsaUsingShaAlgorithm.MGF1, mgf1pec, 48, 1);
                setAlgorithmParameterSpec(pssSpec);
            }
        }
    }

    /* loaded from: classes5.dex */
    public static class RsaPssSha512 extends RsaUsingShaAlgorithm {
        public RsaPssSha512() {
            super(AlgorithmIdentifiers.RSA_PSS_USING_SHA512, choosePssAlgorithmName("SHA512withRSAandMGF1"));
            if (getJavaAlgorithm().equals(RsaUsingShaAlgorithm.RSASSA_PSS)) {
                MGF1ParameterSpec mgf1pec = MGF1ParameterSpec.SHA512;
                PSSParameterSpec pssSpec = new PSSParameterSpec(mgf1pec.getDigestAlgorithm(), RsaUsingShaAlgorithm.MGF1, mgf1pec, 64, 1);
                setAlgorithmParameterSpec(pssSpec);
            }
        }
    }

    /* loaded from: classes5.dex */
    public static class RsaSha256 extends RsaUsingShaAlgorithm {
        public RsaSha256() {
            super(AlgorithmIdentifiers.RSA_USING_SHA256, "SHA256withRSA");
        }
    }

    /* loaded from: classes5.dex */
    public static class RsaSha384 extends RsaUsingShaAlgorithm {
        public RsaSha384() {
            super(AlgorithmIdentifiers.RSA_USING_SHA384, "SHA384withRSA");
        }
    }

    /* loaded from: classes5.dex */
    public static class RsaSha512 extends RsaUsingShaAlgorithm {
        public RsaSha512() {
            super(AlgorithmIdentifiers.RSA_USING_SHA512, "SHA512withRSA");
        }
    }

    static String choosePssAlgorithmName(String legacyName) {
        Set<String> signatureAlgorithms = Security.getAlgorithms("Signature");
        boolean useLegacyName = Boolean.getBoolean("org.jose4j.jws.use-legacy-rsapss-alg-names");
        return (!signatureAlgorithms.contains(RSASSA_PSS) || useLegacyName) ? legacyName : RSASSA_PSS;
    }
}
