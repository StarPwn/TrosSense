package org.jose4j.jwk;

import java.security.Key;
import java.util.Map;
import javax.crypto.spec.SecretKeySpec;
import org.jose4j.base64url.Base64Url;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.keys.AesKey;
import org.jose4j.lang.JoseException;

/* loaded from: classes5.dex */
public class OctetSequenceJsonWebKey extends JsonWebKey {
    public static final String KEY_TYPE = "oct";
    public static final String KEY_VALUE_MEMBER_NAME = "k";
    private byte[] octetSequence;

    public OctetSequenceJsonWebKey(Key key) {
        super(key);
        this.octetSequence = key.getEncoded();
    }

    public OctetSequenceJsonWebKey(Map<String, Object> params) throws JoseException {
        super(params);
        Base64Url base64Url = new Base64Url();
        String b64KeyBytes = getStringRequired(params, KEY_VALUE_MEMBER_NAME);
        this.octetSequence = base64Url.base64UrlDecode(b64KeyBytes);
        this.key = new SecretKeySpec(this.octetSequence, AesKey.ALGORITHM);
        removeFromOtherParams(KEY_VALUE_MEMBER_NAME);
    }

    @Override // org.jose4j.jwk.JsonWebKey
    public String getKeyType() {
        return KEY_TYPE;
    }

    public byte[] getOctetSequence() {
        return this.octetSequence;
    }

    private String getEncoded() {
        return Base64Url.encode(this.octetSequence);
    }

    @Override // org.jose4j.jwk.JsonWebKey
    protected void fillTypeSpecificParams(Map<String, Object> params, JsonWebKey.OutputControlLevel outputLevel) {
        if (JsonWebKey.OutputControlLevel.INCLUDE_SYMMETRIC.compareTo(outputLevel) >= 0) {
            params.put(KEY_VALUE_MEMBER_NAME, getEncoded());
        }
    }

    @Override // org.jose4j.jwk.JsonWebKey
    protected String produceThumbprintHashInput() {
        String k = getEncoded();
        return String.format("{\"k\":\"%s\",\"kty\":\"oct\"}", k);
    }
}
