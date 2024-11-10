package org.jose4j.keys.resolvers;

import java.security.Key;
import java.util.List;
import org.jose4j.jwk.PublicJsonWebKey;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwx.JsonWebStructure;
import org.jose4j.lang.JoseException;
import org.jose4j.lang.UnresolvableKeyException;

/* loaded from: classes5.dex */
public class EmbeddedJwkVerificationKeyResolver implements VerificationKeyResolver {
    private PublicJsonWebKey jwk;

    @Override // org.jose4j.keys.resolvers.VerificationKeyResolver
    public Key resolveKey(JsonWebSignature jws, List<JsonWebStructure> nestingContext) throws UnresolvableKeyException {
        try {
            this.jwk = jws.getJwkHeader();
            if (this.jwk == null) {
                throw new UnresolvableKeyException("No jwk in JWS header");
            }
            return this.jwk.getKey();
        } catch (JoseException e) {
            throw new UnresolvableKeyException("Problem processing jwk from JWS header (" + e.getMessage() + ")", e);
        }
    }

    public PublicJsonWebKey getJwk() {
        return this.jwk;
    }
}
