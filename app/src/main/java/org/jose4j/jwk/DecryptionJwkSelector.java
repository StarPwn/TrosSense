package org.jose4j.jwk;

import java.security.Key;
import java.util.Collection;
import java.util.List;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.lang.ExceptionHelp;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: classes5.dex */
public class DecryptionJwkSelector {
    private static final Logger log = LoggerFactory.getLogger((Class<?>) DecryptionJwkSelector.class);

    public JsonWebKey select(JsonWebEncryption jwe, Collection<JsonWebKey> keys) throws JoseException {
        List<JsonWebKey> jsonWebKeys = selectList(jwe, keys);
        if (jsonWebKeys.isEmpty()) {
            return null;
        }
        return jsonWebKeys.get(0);
    }

    public List<JsonWebKey> selectList(JsonWebEncryption jwe, Collection<JsonWebKey> keys) throws JoseException {
        SimpleJwkFilter filter = SelectorSupport.filterForInboundEncrypted(jwe);
        return filter.filter(keys);
    }

    public JsonWebKey attemptDecryptDisambiguate(JsonWebEncryption jwe, List<JsonWebKey> jsonWebKeys) {
        Key key;
        for (JsonWebKey jwk : jsonWebKeys) {
            if (jwk instanceof PublicJsonWebKey) {
                PublicJsonWebKey publicJwk = (PublicJsonWebKey) jwk;
                key = publicJwk.getPrivateKey();
            } else {
                key = jwk.getKey();
            }
            if (key != null) {
                jwe.setKey(key);
                try {
                    byte[] plaintextBytes = jwe.getPlaintextBytes();
                    if (plaintextBytes != null) {
                        return jwk;
                    }
                } catch (JoseException e) {
                    log.debug("Not using key (kid={}) b/c attempt to decrypt failed trying to disambiguate ({}).", jwk.getKeyId(), ExceptionHelp.toStringWithCauses(e));
                }
            }
        }
        return null;
    }
}
