package org.jose4j.jwk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.jose4j.json.JsonUtil;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.lang.ExceptionHelp;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: classes5.dex */
public class JsonWebKeySet {
    public static final String JWK_SET_MEMBER_NAME = "keys";
    private static final Logger log = LoggerFactory.getLogger((Class<?>) JsonWebKeySet.class);
    private List<JsonWebKey> keys;

    public JsonWebKeySet(String json) throws JoseException {
        Map<String, Object> parsed = JsonUtil.parseJson(json);
        List<Map<String, Object>> jwkParamMapList = (List) parsed.get(JWK_SET_MEMBER_NAME);
        if (jwkParamMapList == null) {
            throw new JoseException("The JSON JWKS content does not include the keys member.");
        }
        this.keys = new ArrayList(jwkParamMapList.size());
        for (Map<String, Object> jwkParamsMap : jwkParamMapList) {
            try {
                JsonWebKey jwk = JsonWebKey.Factory.newJwk(jwkParamsMap);
                this.keys.add(jwk);
            } catch (Exception e) {
                log.debug("Ignoring an individual JWK in a JWKS due to a problem processing it ({}). JWK params: {} and the full JWKS content: {}.", ExceptionHelp.toStringWithCauses(e), jwkParamsMap, json);
            }
        }
    }

    public JsonWebKeySet(JsonWebKey... keys) {
        this((List<? extends JsonWebKey>) Arrays.asList(keys));
    }

    public JsonWebKeySet(List<? extends JsonWebKey> keys) {
        this.keys = new ArrayList(keys.size());
        for (JsonWebKey jwk : keys) {
            this.keys.add(jwk);
        }
    }

    public void addJsonWebKey(JsonWebKey jsonWebKey) {
        this.keys.add(jsonWebKey);
    }

    public List<JsonWebKey> getJsonWebKeys() {
        return this.keys;
    }

    public JsonWebKey findJsonWebKey(String keyId, String keyType, String use, String algorithm) {
        List<JsonWebKey> found = findJsonWebKeys(keyId, keyType, use, algorithm);
        if (found.isEmpty()) {
            return null;
        }
        return found.iterator().next();
    }

    public List<JsonWebKey> findJsonWebKeys(String keyId, String keyType, String use, String algorithm) {
        List<JsonWebKey> found = new ArrayList<>();
        for (JsonWebKey jwk : this.keys) {
            boolean isMeetsCriteria = true;
            if (keyId != null) {
                isMeetsCriteria = keyId.equals(jwk.getKeyId());
            }
            if (use != null) {
                isMeetsCriteria &= use.equals(jwk.getUse());
            }
            if (keyType != null) {
                isMeetsCriteria &= keyType.equals(jwk.getKeyType());
            }
            if (algorithm != null) {
                isMeetsCriteria &= algorithm.equals(jwk.getAlgorithm());
            }
            if (isMeetsCriteria) {
                found.add(jwk);
            }
        }
        return found;
    }

    public String toJson() {
        return toJson(JsonWebKey.OutputControlLevel.INCLUDE_SYMMETRIC);
    }

    public String toJson(JsonWebKey.OutputControlLevel outputControlLevel) {
        ArrayList arrayList = new ArrayList(this.keys.size());
        for (JsonWebKey key : this.keys) {
            Map<String, Object> params = key.toParams(outputControlLevel);
            arrayList.add(params);
        }
        Map<String, Object> jwks = new LinkedHashMap<>();
        jwks.put(JWK_SET_MEMBER_NAME, arrayList);
        return JsonUtil.toJson(jwks);
    }
}
