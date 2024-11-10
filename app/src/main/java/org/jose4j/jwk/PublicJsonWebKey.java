package org.jose4j.jwk;

import java.math.BigInteger;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.jose4j.json.JsonUtil;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.keys.BigEndianBigInteger;
import org.jose4j.keys.X509Util;
import org.jose4j.lang.JoseException;
import org.jose4j.lang.JsonHelp;

/* loaded from: classes5.dex */
public abstract class PublicJsonWebKey extends JsonWebKey {
    public static final String X509_CERTIFICATE_CHAIN_PARAMETER = "x5c";
    public static final String X509_SHA256_THUMBPRINT_PARAMETER = "x5t#S256";
    public static final String X509_THUMBPRINT_PARAMETER = "x5t";
    public static final String X509_URL_PARAMETER = "x5u";
    private List<X509Certificate> certificateChain;
    protected String jcaProvider;
    protected PrivateKey privateKey;
    protected boolean writeOutPrivateKeyToJson;
    private String x5t;
    private String x5tS256;
    private String x5u;

    protected abstract void fillPrivateTypeSpecificParams(Map<String, Object> map);

    protected abstract void fillPublicTypeSpecificParams(Map<String, Object> map);

    /* JADX INFO: Access modifiers changed from: protected */
    public PublicJsonWebKey(PublicKey publicKey) {
        super(publicKey);
    }

    protected PublicJsonWebKey(Map<String, Object> params) throws JoseException {
        this(params, null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public PublicJsonWebKey(Map<String, Object> params, String jcaProvider) throws JoseException {
        super(params);
        this.jcaProvider = jcaProvider;
        if (params.containsKey("x5c")) {
            List<String> x5cStrings = JsonHelp.getStringArray(params, "x5c");
            this.certificateChain = new ArrayList(x5cStrings.size());
            X509Util x509Util = X509Util.getX509Util(jcaProvider);
            for (String b64EncodedDer : x5cStrings) {
                X509Certificate x509Certificate = x509Util.fromBase64Der(b64EncodedDer);
                this.certificateChain.add(x509Certificate);
            }
        }
        this.x5t = getString(params, "x5t");
        this.x5tS256 = getString(params, "x5t#S256");
        this.x5u = getString(params, "x5u");
        removeFromOtherParams("x5c", "x5t#S256", "x5t", "x5u");
    }

    @Override // org.jose4j.jwk.JsonWebKey
    protected void fillTypeSpecificParams(Map<String, Object> params, JsonWebKey.OutputControlLevel outputLevel) {
        fillPublicTypeSpecificParams(params);
        if (this.certificateChain != null) {
            X509Util x509Util = new X509Util();
            List<String> x5cStrings = new ArrayList<>(this.certificateChain.size());
            for (X509Certificate cert : this.certificateChain) {
                String b64EncodedDer = x509Util.toBase64(cert);
                x5cStrings.add(b64EncodedDer);
            }
            params.put("x5c", x5cStrings);
        }
        putIfNotNull("x5t", this.x5t, params);
        putIfNotNull("x5t#S256", this.x5tS256, params);
        putIfNotNull("x5u", this.x5u, params);
        if (this.writeOutPrivateKeyToJson || outputLevel == JsonWebKey.OutputControlLevel.INCLUDE_PRIVATE) {
            fillPrivateTypeSpecificParams(params);
        }
    }

    @Override // org.jose4j.jwk.JsonWebKey
    public PublicKey getPublicKey() {
        return (PublicKey) this.key;
    }

    public void setWriteOutPrivateKeyToJson(boolean writeOutPrivateKeyToJson) {
        this.writeOutPrivateKeyToJson = writeOutPrivateKeyToJson;
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public List<X509Certificate> getCertificateChain() {
        return this.certificateChain;
    }

    public X509Certificate getLeafCertificate() {
        if (this.certificateChain == null || this.certificateChain.isEmpty()) {
            return null;
        }
        return this.certificateChain.get(0);
    }

    public String getX509CertificateSha1Thumbprint() {
        return getX509CertificateSha1Thumbprint(false);
    }

    public String getX509CertificateSha1Thumbprint(boolean allowFallbackDeriveFromX5c) {
        X509Certificate leafCertificate;
        String result = this.x5t;
        if (result == null && allowFallbackDeriveFromX5c && (leafCertificate = getLeafCertificate()) != null) {
            return X509Util.x5t(leafCertificate);
        }
        return result;
    }

    public String getX509CertificateSha256Thumbprint() {
        return getX509CertificateSha256Thumbprint(false);
    }

    public String getX509CertificateSha256Thumbprint(boolean allowFallbackDeriveFromX5c) {
        X509Certificate leafCertificate;
        String result = this.x5tS256;
        if (result == null && allowFallbackDeriveFromX5c && (leafCertificate = getLeafCertificate()) != null) {
            return X509Util.x5tS256(leafCertificate);
        }
        return result;
    }

    public String getX509Url() {
        return this.x5u;
    }

    public void setCertificateChain(List<X509Certificate> certificateChain) {
        checkForBareKeyCertMismatch();
        this.certificateChain = certificateChain;
    }

    public void setX509CertificateSha1Thumbprint(String x5t) {
        this.x5t = x5t;
    }

    public void setX509CertificateSha256Thumbprint(String x5tS2) {
        this.x5tS256 = x5tS2;
    }

    public void setX509Url(String x5u) {
        this.x5u = x5u;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void checkForBareKeyCertMismatch() {
        X509Certificate leafCertificate = getLeafCertificate();
        boolean certAndBareKeyMismatch = (leafCertificate == null || leafCertificate.getPublicKey().equals(getPublicKey())) ? false : true;
        if (certAndBareKeyMismatch) {
            throw new IllegalArgumentException("The key in the first certificate MUST match the bare public key represented by other members of the JWK. Public key = " + getPublicKey() + " cert = " + leafCertificate);
        }
    }

    public void setCertificateChain(X509Certificate... certificates) {
        setCertificateChain(Arrays.asList(certificates));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BigInteger getBigIntFromBase64UrlEncodedParam(Map<String, Object> params, String parameterName, boolean required) throws JoseException {
        String base64UrlValue = getString(params, parameterName, required);
        return BigEndianBigInteger.fromBase64Url(base64UrlValue);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void putBigIntAsBase64UrlEncodedParam(Map<String, Object> params, String parameterName, BigInteger value) {
        String base64UrlValue = BigEndianBigInteger.toBase64Url(value);
        params.put(parameterName, base64UrlValue);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void putBigIntAsBase64UrlEncodedParam(Map<String, Object> params, String parameterName, BigInteger value, int minLength) {
        String base64UrlValue = BigEndianBigInteger.toBase64Url(value, minLength);
        params.put(parameterName, base64UrlValue);
    }

    /* loaded from: classes5.dex */
    public static class Factory {
        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        public static PublicJsonWebKey newPublicJwk(Map<String, Object> params, String jcaProvider) throws JoseException {
            char c;
            String kty = JsonWebKey.getStringRequired(params, JsonWebKey.KEY_TYPE_PARAMETER);
            switch (kty.hashCode()) {
                case 2206:
                    if (kty.equals("EC")) {
                        c = 1;
                        break;
                    }
                    c = 65535;
                    break;
                case 78324:
                    if (kty.equals(OctetKeyPairJsonWebKey.KEY_TYPE)) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                case 81440:
                    if (kty.equals("RSA")) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                default:
                    c = 65535;
                    break;
            }
            switch (c) {
                case 0:
                    return new RsaJsonWebKey(params, jcaProvider);
                case 1:
                    return new EllipticCurveJsonWebKey(params, jcaProvider);
                case 2:
                    return new OctetKeyPairJsonWebKey(params, jcaProvider);
                default:
                    throw new JoseException("Unknown key type (for public keys): '" + kty + "'");
            }
        }

        public static PublicJsonWebKey newPublicJwk(Map<String, Object> params) throws JoseException {
            return newPublicJwk(params, (String) null);
        }

        public static PublicJsonWebKey newPublicJwk(Key publicKey) throws JoseException {
            JsonWebKey jsonWebKey = JsonWebKey.Factory.newJwk(publicKey);
            return (PublicJsonWebKey) jsonWebKey;
        }

        public static PublicJsonWebKey newPublicJwk(String json) throws JoseException {
            return newPublicJwk(json, (String) null);
        }

        public static PublicJsonWebKey newPublicJwk(String json, String jcaProvider) throws JoseException {
            Map<String, Object> parsed = JsonUtil.parseJson(json);
            return newPublicJwk(parsed, jcaProvider);
        }
    }
}
