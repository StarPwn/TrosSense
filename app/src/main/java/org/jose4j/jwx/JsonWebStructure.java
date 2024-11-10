package org.jose4j.jwx;

import java.security.Key;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jose4j.base64url.Base64Url;
import org.jose4j.jca.ProviderContext;
import org.jose4j.jwa.Algorithm;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwk.PublicJsonWebKey;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.keys.X509Util;
import org.jose4j.lang.InvalidAlgorithmException;
import org.jose4j.lang.JoseException;

/* loaded from: classes5.dex */
public abstract class JsonWebStructure {
    private static final ProviderContext DEFAULT_PROVIDER_CONTEXT = new ProviderContext();
    private byte[] integrity;
    private Key key;
    protected String rawCompactSerialization;
    protected Base64Url base64url = new Base64Url();
    protected Headers headers = new Headers();
    protected boolean doKeyValidation = true;
    private AlgorithmConstraints algorithmConstraints = AlgorithmConstraints.NO_CONSTRAINTS;
    private Set<String> knownCriticalHeaders = Collections.emptySet();
    private ProviderContext providerCtx = DEFAULT_PROVIDER_CONTEXT;

    public abstract Algorithm getAlgorithm() throws InvalidAlgorithmException;

    public abstract Algorithm getAlgorithmNoConstraintCheck() throws InvalidAlgorithmException;

    public abstract String getCompactSerialization() throws JoseException;

    public abstract String getPayload() throws JoseException;

    protected abstract void setCompactSerializationParts(String[] strArr) throws JoseException;

    public abstract void setPayload(String str);

    public static JsonWebStructure fromCompactSerialization(String cs) throws JoseException {
        JsonWebStructure jsonWebObject;
        String[] parts = CompactSerializer.deserialize(cs);
        if (parts.length == 5) {
            jsonWebObject = new JsonWebEncryption();
        } else if (parts.length == 3) {
            jsonWebObject = new JsonWebSignature();
        } else {
            throw new JoseException("Invalid JOSE Compact Serialization. Expecting either 3 or 5 parts for JWS or JWE respectively but was " + parts.length + ".");
        }
        jsonWebObject.setCompactSerializationParts(parts);
        jsonWebObject.rawCompactSerialization = cs;
        return jsonWebObject;
    }

    public void setCompactSerialization(String compactSerialization) throws JoseException {
        String[] parts = CompactSerializer.deserialize(compactSerialization);
        setCompactSerializationParts(parts);
        this.rawCompactSerialization = compactSerialization;
    }

    public String getHeader() {
        return getHeaders().getFullHeaderAsJsonString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getEncodedHeader() {
        return this.headers.getEncodedHeader();
    }

    public void setHeader(String name, String value) {
        this.headers.setStringHeaderValue(name, value);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setEncodedHeader(String encodedHeader) throws JoseException {
        checkNotEmptyPart(encodedHeader, "Encoded Header");
        this.headers.setEncodedHeader(encodedHeader);
    }

    public Headers getHeaders() {
        return this.headers;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void checkNotEmptyPart(String encodedPart, String partName) throws JoseException {
        if (encodedPart == null || encodedPart.length() == 0) {
            throw new JoseException("The " + partName + " cannot be empty.");
        }
    }

    public String getHeader(String name) {
        return this.headers.getStringHeaderValue(name);
    }

    public void setHeader(String name, Object value) {
        this.headers.setObjectHeaderValue(name, value);
    }

    public Object getObjectHeader(String name) {
        return this.headers.getObjectHeaderValue(name);
    }

    public void setAlgorithmHeaderValue(String alg) {
        setHeader("alg", alg);
    }

    public String getAlgorithmHeaderValue() {
        return getHeader("alg");
    }

    public void setContentTypeHeaderValue(String cty) {
        setHeader(HeaderParameterNames.CONTENT_TYPE, cty);
    }

    public String getContentTypeHeaderValue() {
        return getHeader(HeaderParameterNames.CONTENT_TYPE);
    }

    public void setKeyIdHeaderValue(String kid) {
        setHeader("kid", kid);
    }

    public String getKeyIdHeaderValue() {
        return getHeader("kid");
    }

    public PublicJsonWebKey getJwkHeader() throws JoseException {
        return this.headers.getPublicJwkHeaderValue(HeaderParameterNames.JWK, null);
    }

    public void setJwkHeader(PublicJsonWebKey jwk) {
        this.headers.setJwkHeaderValue(HeaderParameterNames.JWK, jwk);
    }

    public X509Certificate getLeafCertificateHeaderValue() throws JoseException {
        List<X509Certificate> certificateChain = getCertificateChainHeaderValue();
        if (certificateChain == null || certificateChain.isEmpty()) {
            return null;
        }
        return certificateChain.get(0);
    }

    public List<X509Certificate> getCertificateChainHeaderValue() throws JoseException {
        Object x5c = this.headers.getObjectHeaderValue("x5c");
        if (x5c instanceof List) {
            List x5cList = (List) x5c;
            List<X509Certificate> certificateChain = new ArrayList<>(x5cList.size());
            X509Util x509Util = new X509Util();
            for (Object certificate : x5cList) {
                certificateChain.add(x509Util.fromBase64Der((String) certificate));
            }
            return certificateChain;
        }
        return null;
    }

    public void setCertificateChainHeaderValue(X509Certificate... chain) {
        List<String> chainStrings = new ArrayList<>();
        X509Util x509Util = new X509Util();
        for (X509Certificate certificate : chain) {
            chainStrings.add(x509Util.toBase64(certificate));
        }
        this.headers.setObjectHeaderValue("x5c", chainStrings);
    }

    public String getX509CertSha1ThumbprintHeaderValue() {
        return getHeader("x5t");
    }

    public void setX509CertSha1ThumbprintHeaderValue(String x5t) {
        setHeader("x5t", x5t);
    }

    public void setX509CertSha1ThumbprintHeaderValue(X509Certificate certificate) {
        String x5t = X509Util.x5t(certificate);
        setX509CertSha1ThumbprintHeaderValue(x5t);
    }

    public String getX509CertSha256ThumbprintHeaderValue() {
        return getHeader("x5t#S256");
    }

    public void setX509CertSha256ThumbprintHeaderValue(String x5tS256) {
        setHeader("x5t#S256", x5tS256);
    }

    public void setX509CertSha256ThumbprintHeaderValue(X509Certificate certificate) {
        String x5tS256 = X509Util.x5tS256(certificate);
        setX509CertSha256ThumbprintHeaderValue(x5tS256);
    }

    public Key getKey() {
        return this.key;
    }

    public void setKey(Key key) {
        boolean same = true;
        Key key2 = this.key;
        if (key != null ? key2 == null || !key.equals(this.key) : key2 != null) {
            same = false;
        }
        if (!same) {
            onNewKey();
        }
        this.key = key;
    }

    protected void onNewKey() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public byte[] getIntegrity() {
        return this.integrity;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setIntegrity(byte[] integrity) {
        this.integrity = integrity;
    }

    public boolean isDoKeyValidation() {
        return this.doKeyValidation;
    }

    public void setDoKeyValidation(boolean doKeyValidation) {
        this.doKeyValidation = doKeyValidation;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AlgorithmConstraints getAlgorithmConstraints() {
        return this.algorithmConstraints;
    }

    public void setAlgorithmConstraints(AlgorithmConstraints algorithmConstraints) {
        this.algorithmConstraints = algorithmConstraints;
    }

    public void setCriticalHeaderNames(String... headerNames) {
        this.headers.setObjectHeaderValue(HeaderParameterNames.CRITICAL, headerNames);
    }

    public void setKnownCriticalHeaders(String... knownCriticalHeaders) {
        this.knownCriticalHeaders = new HashSet(Arrays.asList(knownCriticalHeaders));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void checkCrit() throws JoseException {
        List<String> criticalHeaders;
        Object criticalHeaderObjectValue = this.headers.getObjectHeaderValue(HeaderParameterNames.CRITICAL);
        if (criticalHeaderObjectValue != null) {
            if (criticalHeaderObjectValue instanceof List) {
                criticalHeaders = (List) criticalHeaderObjectValue;
            } else if (criticalHeaderObjectValue instanceof String[]) {
                criticalHeaders = Arrays.asList((String[]) criticalHeaderObjectValue);
            } else {
                throw new JoseException("crit header value not an array (" + criticalHeaderObjectValue.getClass() + ").");
            }
            for (String criticalHeader : criticalHeaders) {
                if (!this.knownCriticalHeaders.contains(criticalHeader) && !isSupportedCriticalHeader(criticalHeader)) {
                    throw new JoseException("Unrecognized header '" + criticalHeader + "' marked as critical.");
                }
            }
        }
    }

    protected boolean isSupportedCriticalHeader(String headerName) {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ProviderContext getProviderCtx() {
        return this.providerCtx;
    }

    public void setProviderContext(ProviderContext providerCtx) {
        this.providerCtx = providerCtx;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName()).append(getHeaders().getFullHeaderAsJsonString());
        if (this.rawCompactSerialization != null) {
            sb.append("->").append(this.rawCompactSerialization);
        }
        return sb.toString();
    }
}
