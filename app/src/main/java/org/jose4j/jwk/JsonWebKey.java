package org.jose4j.jwk;

import java.io.Serializable;
import java.security.Key;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.jose4j.base64url.Base64Url;
import org.jose4j.json.JsonUtil;
import org.jose4j.lang.HashUtil;
import org.jose4j.lang.JoseException;
import org.jose4j.lang.JsonHelp;
import org.jose4j.lang.StringUtil;

/* loaded from: classes5.dex */
public abstract class JsonWebKey implements Serializable {
    public static final String ALGORITHM_PARAMETER = "alg";
    public static final String KEY_ID_PARAMETER = "kid";
    public static final String KEY_OPERATIONS = "key_ops";
    public static final String KEY_TYPE_PARAMETER = "kty";
    public static final String USE_PARAMETER = "use";
    private String algorithm;
    protected Key key;
    private String keyId;
    private List<String> keyOps;
    protected Map<String, Object> otherParameters = new LinkedHashMap();
    private String use;

    /* loaded from: classes5.dex */
    public enum OutputControlLevel {
        INCLUDE_PRIVATE,
        INCLUDE_SYMMETRIC,
        PUBLIC_ONLY
    }

    protected abstract void fillTypeSpecificParams(Map<String, Object> map, OutputControlLevel outputControlLevel);

    public abstract String getKeyType();

    protected abstract String produceThumbprintHashInput();

    /* JADX INFO: Access modifiers changed from: protected */
    public JsonWebKey(Key key) {
        this.key = key;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public JsonWebKey(Map<String, Object> params) throws JoseException {
        this.otherParameters.putAll(params);
        removeFromOtherParams(KEY_TYPE_PARAMETER, USE_PARAMETER, "kid", "alg", KEY_OPERATIONS);
        setUse(getString(params, USE_PARAMETER));
        setKeyId(getString(params, "kid"));
        setAlgorithm(getString(params, "alg"));
        if (params.containsKey(KEY_OPERATIONS)) {
            this.keyOps = JsonHelp.getStringArray(params, KEY_OPERATIONS);
        }
    }

    public PublicKey getPublicKey() {
        try {
            return (PublicKey) this.key;
        } catch (Exception e) {
            return null;
        }
    }

    public Key getKey() {
        return this.key;
    }

    public String getUse() {
        return this.use;
    }

    public void setUse(String use) {
        this.use = use;
    }

    public String getKeyId() {
        return this.keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getAlgorithm() {
        return this.algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public List<String> getKeyOps() {
        return this.keyOps;
    }

    public void setKeyOps(List<String> keyOps) {
        this.keyOps = keyOps;
    }

    public void setOtherParameter(String name, Object value) {
        this.otherParameters.put(name, value);
    }

    public <T> T getOtherParameterValue(String name, Class<T> type) {
        Object o = this.otherParameters.get(name);
        return type.cast(o);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void removeFromOtherParams(String... names) {
        for (String name : names) {
            this.otherParameters.remove(name);
        }
    }

    public Map<String, Object> toParams(OutputControlLevel outputLevel) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(KEY_TYPE_PARAMETER, getKeyType());
        putIfNotNull("kid", getKeyId(), params);
        putIfNotNull(USE_PARAMETER, getUse(), params);
        putIfNotNull(KEY_OPERATIONS, this.keyOps, params);
        putIfNotNull("alg", getAlgorithm(), params);
        fillTypeSpecificParams(params, outputLevel);
        params.putAll(this.otherParameters);
        return params;
    }

    public String toJson() {
        return toJson(OutputControlLevel.INCLUDE_SYMMETRIC);
    }

    public String toJson(OutputControlLevel outputLevel) {
        Map<String, Object> params = toParams(outputLevel);
        return JsonUtil.toJson(params);
    }

    public String toString() {
        return getClass().getName() + toParams(OutputControlLevel.PUBLIC_ONLY);
    }

    public String calculateBase64urlEncodedThumbprint(String hashAlgorithm) {
        byte[] thumbprint = calculateThumbprint(hashAlgorithm);
        return Base64Url.encode(thumbprint);
    }

    public byte[] calculateThumbprint(String hashAlgorithm) {
        MessageDigest digest = HashUtil.getMessageDigest(hashAlgorithm);
        String hashInputString = produceThumbprintHashInput();
        byte[] hashInputBytes = StringUtil.getBytesUtf8(hashInputString);
        return digest.digest(hashInputBytes);
    }

    public String calculateThumbprintUri(String hashAlgorithm) {
        if (!hashAlgorithm.equals(HashUtil.SHA_256)) {
            throw new UnsupportedOperationException("Only SHA-256 algorithm supported at this time for Thumbprint URIs");
        }
        return "urn:ietf:params:oauth:jwk-thumbprint:sha-256:" + calculateBase64urlEncodedThumbprint(hashAlgorithm);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void putIfNotNull(String name, Object value, Map<String, Object> params) {
        if (value != null) {
            params.put(name, value);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static String getString(Map<String, Object> params, String name) throws JoseException {
        return JsonHelp.getStringChecked(params, name);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static String getStringRequired(Map<String, Object> params, String name) throws JoseException {
        return getString(params, name, true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static String getString(Map<String, Object> params, String name, boolean required) throws JoseException {
        String value = getString(params, name);
        if (value == null && required) {
            throw new JoseException("Missing required '" + name + "' parameter.");
        }
        return value;
    }

    /* loaded from: classes5.dex */
    public static class Factory {
        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        public static JsonWebKey newJwk(Map<String, Object> params) throws JoseException {
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
                case 109856:
                    if (kty.equals(OctetSequenceJsonWebKey.KEY_TYPE)) {
                        c = 3;
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
                    return new RsaJsonWebKey(params);
                case 1:
                    return new EllipticCurveJsonWebKey(params);
                case 2:
                    return new OctetKeyPairJsonWebKey(params);
                case 3:
                    return new OctetSequenceJsonWebKey(params);
                default:
                    throw new JoseException("Unknown key type algorithm: '" + kty + "'");
            }
        }

        public static JsonWebKey newJwk(Key key) throws JoseException {
            if (RSAPublicKey.class.isInstance(key)) {
                return new RsaJsonWebKey((RSAPublicKey) key);
            }
            if (ECPublicKey.class.isInstance(key)) {
                return new EllipticCurveJsonWebKey((ECPublicKey) key);
            }
            if (PublicKey.class.isInstance(key)) {
                if (OctetKeyPairJsonWebKey.isApplicable(key)) {
                    return new OctetKeyPairJsonWebKey((PublicKey) key);
                }
                throw new JoseException("Unsupported or unknown public key (alg=" + key.getAlgorithm() + ") " + key);
            }
            return new OctetSequenceJsonWebKey(key);
        }

        public static JsonWebKey newJwk(String json) throws JoseException {
            Map<String, Object> parsed = JsonUtil.parseJson(json);
            return newJwk(parsed);
        }
    }
}
