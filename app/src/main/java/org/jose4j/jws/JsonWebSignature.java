package org.jose4j.jws;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Key;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwa.AlgorithmFactory;
import org.jose4j.jwa.AlgorithmFactoryFactory;
import org.jose4j.jwa.CryptoPrimitive;
import org.jose4j.jwx.CompactSerializer;
import org.jose4j.jwx.HeaderParameterNames;
import org.jose4j.jwx.JsonWebStructure;
import org.jose4j.keys.KeyPersuasion;
import org.jose4j.lang.IntegrityException;
import org.jose4j.lang.InvalidAlgorithmException;
import org.jose4j.lang.JoseException;
import org.jose4j.lang.StringUtil;

/* loaded from: classes5.dex */
public class JsonWebSignature extends JsonWebStructure {
    public static final short COMPACT_SERIALIZATION_PARTS = 3;
    private String encodedPayload;
    private byte[] payloadBytes;
    private String payloadCharEncoding = StringUtil.UTF_8;
    private CryptoPrimitive signingPrimitive;
    private Boolean validSignature;

    public JsonWebSignature() {
        if (!Boolean.getBoolean("org.jose4j.jws.default-allow-none")) {
            setAlgorithmConstraints(AlgorithmConstraints.DISALLOW_NONE);
        }
    }

    @Override // org.jose4j.jwx.JsonWebStructure
    public void setPayload(String payload) {
        this.payloadBytes = StringUtil.getBytesUnchecked(payload, this.payloadCharEncoding);
        this.encodedPayload = null;
    }

    public byte[] getPayloadBytes() throws JoseException {
        if (!verifySignature()) {
            throw new IntegrityException("JWS signature is invalid.");
        }
        return this.payloadBytes;
    }

    public byte[] getUnverifiedPayloadBytes() {
        return this.payloadBytes;
    }

    public void setPayloadBytes(byte[] payloadBytes) {
        this.payloadBytes = payloadBytes;
    }

    @Override // org.jose4j.jwx.JsonWebStructure
    protected void setCompactSerializationParts(String[] parts) throws JoseException {
        if (parts.length != 3) {
            throw new JoseException("A JWS Compact Serialization must have exactly 3 parts separated by period ('.') characters");
        }
        setEncodedHeader(parts[0]);
        if (isRfc7797UnencodedPayload()) {
            setPayload(parts[1]);
        } else {
            setEncodedPayload(parts[1]);
        }
        setSignature(this.base64url.base64UrlDecode(parts[2]));
    }

    @Override // org.jose4j.jwx.JsonWebStructure
    public String getCompactSerialization() throws JoseException {
        String payload;
        sign();
        if (isRfc7797UnencodedPayload()) {
            payload = getStringPayload();
            if (payload.contains(".")) {
                throw new JoseException("per https://tools.ietf.org/html/rfc7797#section-5.2 when using the JWS Compact Serialization, unencoded non-detached payloads using period ('.') characters would cause parsing errors; such payloads MUST NOT be used with the JWS Compact Serialization.");
            }
        } else {
            payload = getEncodedPayload();
        }
        return CompactSerializer.serialize(getEncodedHeader(), payload, getEncodedSignature());
    }

    public String getDetachedContentCompactSerialization() throws JoseException {
        sign();
        return CompactSerializer.serialize(getEncodedHeader(), "", getEncodedSignature());
    }

    public CryptoPrimitive prepareSigningPrimitive() throws JoseException {
        this.signingPrimitive = createSigningPrimitive();
        return this.signingPrimitive;
    }

    private CryptoPrimitive createSigningPrimitive() throws JoseException {
        JsonWebSignatureAlgorithm algorithm = getAlgorithm();
        Key signingKey = getKey();
        if (isDoKeyValidation()) {
            algorithm.validateSigningKey(signingKey);
        }
        return algorithm.prepareForSign(signingKey, getProviderCtx());
    }

    public void sign() throws JoseException {
        CryptoPrimitive cryptoPrimitive = this.signingPrimitive == null ? createSigningPrimitive() : this.signingPrimitive;
        byte[] inputBytes = getSigningInputBytes();
        byte[] signatureBytes = getAlgorithm().sign(cryptoPrimitive, inputBytes);
        setSignature(signatureBytes);
    }

    @Override // org.jose4j.jwx.JsonWebStructure
    protected void onNewKey() {
        this.validSignature = null;
    }

    public boolean verifySignature() throws JoseException {
        JsonWebSignatureAlgorithm algorithm = getAlgorithm();
        Key verificationKey = getKey();
        if (isDoKeyValidation()) {
            algorithm.validateVerificationKey(verificationKey);
        }
        if (this.validSignature == null) {
            checkCrit();
            byte[] signatureBytes = getSignature();
            byte[] inputBytes = getSigningInputBytes();
            this.validSignature = Boolean.valueOf(algorithm.verifySignature(signatureBytes, verificationKey, inputBytes, getProviderCtx()));
        }
        return this.validSignature.booleanValue();
    }

    @Override // org.jose4j.jwx.JsonWebStructure
    protected boolean isSupportedCriticalHeader(String headerName) {
        return HeaderParameterNames.BASE64URL_ENCODE_PAYLOAD.equals(headerName);
    }

    @Override // org.jose4j.jwx.JsonWebStructure
    public JsonWebSignatureAlgorithm getAlgorithm() throws InvalidAlgorithmException {
        return getAlgorithm(true);
    }

    @Override // org.jose4j.jwx.JsonWebStructure
    public JsonWebSignatureAlgorithm getAlgorithmNoConstraintCheck() throws InvalidAlgorithmException {
        return getAlgorithm(false);
    }

    private JsonWebSignatureAlgorithm getAlgorithm(boolean checkConstraints) throws InvalidAlgorithmException {
        String algo = getAlgorithmHeaderValue();
        if (algo == null) {
            throw new InvalidAlgorithmException("Signature algorithm header (alg) not set.");
        }
        if (checkConstraints) {
            getAlgorithmConstraints().checkConstraint(algo);
        }
        AlgorithmFactoryFactory factoryFactory = AlgorithmFactoryFactory.getInstance();
        AlgorithmFactory<JsonWebSignatureAlgorithm> jwsAlgorithmFactory = factoryFactory.getJwsAlgorithmFactory();
        return jwsAlgorithmFactory.getAlgorithm(algo);
    }

    private byte[] getSigningInputBytes() throws JoseException {
        if (!isRfc7797UnencodedPayload()) {
            String signingInputString = CompactSerializer.serialize(getEncodedHeader(), getEncodedPayload());
            return StringUtil.getBytesAscii(signingInputString);
        }
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            os.write(StringUtil.getBytesAscii(getEncodedHeader()));
            os.write(46);
            os.write(this.payloadBytes);
            return os.toByteArray();
        } catch (IOException e) {
            throw new JoseException("This should never happen from a ByteArrayOutputStream", e);
        }
    }

    protected boolean isRfc7797UnencodedPayload() {
        Object b64 = this.headers.getObjectHeaderValue(HeaderParameterNames.BASE64URL_ENCODE_PAYLOAD);
        return (b64 == null || !(b64 instanceof Boolean) || ((Boolean) b64).booleanValue()) ? false : true;
    }

    @Override // org.jose4j.jwx.JsonWebStructure
    public String getPayload() throws JoseException {
        if (!Boolean.getBoolean("org.jose4j.jws.getPayload-skip-verify") && !verifySignature()) {
            throw new IntegrityException("JWS signature is invalid.");
        }
        return getStringPayload();
    }

    public String getUnverifiedPayload() {
        return getStringPayload();
    }

    private String getStringPayload() {
        return StringUtil.newString(this.payloadBytes, this.payloadCharEncoding);
    }

    public String getPayloadCharEncoding() {
        return this.payloadCharEncoding;
    }

    public void setPayloadCharEncoding(String payloadCharEncoding) {
        this.payloadCharEncoding = payloadCharEncoding;
    }

    public String getKeyType() throws InvalidAlgorithmException {
        return getAlgorithmNoConstraintCheck().getKeyType();
    }

    public KeyPersuasion getKeyPersuasion() throws InvalidAlgorithmException {
        return getAlgorithmNoConstraintCheck().getKeyPersuasion();
    }

    public void setEncodedPayload(String encodedPayload) {
        this.encodedPayload = encodedPayload;
        this.payloadBytes = this.base64url.base64UrlDecode(encodedPayload);
    }

    public String getEncodedPayload() {
        return this.encodedPayload != null ? this.encodedPayload : this.base64url.base64UrlEncode(this.payloadBytes);
    }

    public String getEncodedSignature() {
        return this.base64url.base64UrlEncode(getSignature());
    }

    protected byte[] getSignature() {
        return getIntegrity();
    }

    protected void setSignature(byte[] signature) {
        setIntegrity(signature);
    }
}
