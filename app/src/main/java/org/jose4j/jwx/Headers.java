package org.jose4j.jwx;

import java.util.LinkedHashMap;
import java.util.Map;
import org.jose4j.base64url.Base64Url;
import org.jose4j.json.JsonUtil;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.PublicJsonWebKey;
import org.jose4j.lang.JoseException;
import org.jose4j.lang.JsonHelp;

/* loaded from: classes5.dex */
public class Headers {
    private String encodedHeader;
    private String header;
    protected Base64Url base64url = new Base64Url();
    private Map<String, Object> headerMap = new LinkedHashMap();

    public String getFullHeaderAsJsonString() {
        if (this.header == null) {
            this.header = JsonUtil.toJson(this.headerMap);
        }
        return this.header;
    }

    public String getEncodedHeader() {
        if (this.encodedHeader == null) {
            String headerAsString = getFullHeaderAsJsonString();
            this.encodedHeader = this.base64url.base64UrlEncodeUtf8ByteRepresentation(headerAsString);
        }
        String headerAsString2 = this.encodedHeader;
        return headerAsString2;
    }

    public void setStringHeaderValue(String name, String value) {
        setObjectHeaderValue(name, value);
    }

    public void setObjectHeaderValue(String name, Object value) {
        this.headerMap.put(name, value);
        this.header = null;
        this.encodedHeader = null;
    }

    public void setJwkHeaderValue(String name, JsonWebKey jwk) {
        Map<String, Object> jwkParams = jwk.toParams(JsonWebKey.OutputControlLevel.PUBLIC_ONLY);
        setObjectHeaderValue(name, jwkParams);
    }

    public String getStringHeaderValue(String headerName) {
        return JsonHelp.getString(this.headerMap, headerName);
    }

    public Long getLongHeaderValue(String headerName) {
        return JsonHelp.getLong(this.headerMap, headerName);
    }

    public Object getObjectHeaderValue(String name) {
        return this.headerMap.get(name);
    }

    @Deprecated
    public JsonWebKey getJwkHeaderValue(String name) throws JoseException {
        return getPublicJwkHeaderValue(name, null);
    }

    public PublicJsonWebKey getPublicJwkHeaderValue(String name, String jcaProvider) throws JoseException {
        Object objectHeaderValue = getObjectHeaderValue(name);
        Map<String, Object> jwkParams = (Map) objectHeaderValue;
        if (jwkParams != null) {
            PublicJsonWebKey publicJsonWebKey = PublicJsonWebKey.Factory.newPublicJwk(jwkParams, jcaProvider);
            if (publicJsonWebKey.getPrivateKey() != null) {
                throw new JoseException(name + " header contains a private key, which it most definitely should not.");
            }
            return publicJsonWebKey;
        }
        return null;
    }

    public void setFullHeaderAsJsonString(String header) throws JoseException {
        this.encodedHeader = null;
        this.header = header;
        this.headerMap = JsonUtil.parseJson(header);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setEncodedHeader(String encodedHeader) throws JoseException {
        this.encodedHeader = encodedHeader;
        this.header = this.base64url.base64UrlDecodeToUtf8String(this.encodedHeader);
        this.headerMap = JsonUtil.parseJson(this.header);
    }
}
