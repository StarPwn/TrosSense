package org.jose4j.jwk;

import java.util.Collection;
import java.util.List;
import org.jose4j.jws.EcdsaUsingShaAlgorithm;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jws.JsonWebSignatureAlgorithm;
import org.jose4j.lang.JoseException;

/* loaded from: classes5.dex */
public class VerificationJwkSelector {
    private static final String[] EDDSA_CRVS = {"Ed25519", "Ed448"};

    public JsonWebKey select(JsonWebSignature jws, Collection<JsonWebKey> keys) throws JoseException {
        List<JsonWebKey> jsonWebKeys = selectList(jws, keys);
        if (jsonWebKeys.isEmpty()) {
            return null;
        }
        return jsonWebKeys.get(0);
    }

    public List<JsonWebKey> selectList(JsonWebSignature jws, Collection<JsonWebKey> keys) throws JoseException {
        SimpleJwkFilter filter = SelectorSupport.filterForInboundSigned(jws);
        List<JsonWebKey> filtered = filter.filter(keys);
        if (hasMoreThanOne(filtered)) {
            filter.setAlg(jws.getAlgorithmHeaderValue(), SimpleJwkFilter.OMITTED_OKAY);
            filtered = filter.filter(filtered);
        }
        if (hasMoreThanOne(filtered)) {
            String keyType = jws.getKeyType();
            if ("EC".equals(keyType)) {
                JsonWebSignatureAlgorithm algorithm = jws.getAlgorithmNoConstraintCheck();
                EcdsaUsingShaAlgorithm ecdsaAlgorithm = (EcdsaUsingShaAlgorithm) algorithm;
                filter.setCrv(ecdsaAlgorithm.getCurveName(), SimpleJwkFilter.OMITTED_OKAY);
                return filter.filter(filtered);
            }
            if (OctetKeyPairJsonWebKey.KEY_TYPE.equals(keyType)) {
                filter.setCrvs(EDDSA_CRVS, SimpleJwkFilter.OMITTED_OKAY);
                return filter.filter(filtered);
            }
            return filtered;
        }
        return filtered;
    }

    public JsonWebKey selectWithVerifySignatureDisambiguate(JsonWebSignature jws, Collection<JsonWebKey> keys) throws JoseException {
        List<JsonWebKey> jsonWebKeys = selectList(jws, keys);
        if (jsonWebKeys.isEmpty()) {
            return null;
        }
        if (jsonWebKeys.size() == 1) {
            return jsonWebKeys.get(0);
        }
        for (JsonWebKey jwk : jsonWebKeys) {
            jws.setKey(jwk.getKey());
            if (jws.verifySignature()) {
                return jwk;
            }
        }
        return null;
    }

    private boolean hasMoreThanOne(List<JsonWebKey> filtered) {
        return filtered.size() > 1;
    }
}
