package org.jose4j.keys.resolvers;

import java.security.Key;
import java.util.List;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.VerificationJwkSelector;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwx.JsonWebStructure;
import org.jose4j.lang.JoseException;
import org.jose4j.lang.UnresolvableKeyException;

/* loaded from: classes5.dex */
public class JwksVerificationKeyResolver implements VerificationKeyResolver {
    private boolean disambiguateWithVerifySignature;
    private List<JsonWebKey> jsonWebKeys;
    private VerificationJwkSelector selector = new VerificationJwkSelector();

    public JwksVerificationKeyResolver(List<JsonWebKey> jsonWebKeys) {
        this.jsonWebKeys = jsonWebKeys;
    }

    @Override // org.jose4j.keys.resolvers.VerificationKeyResolver
    public Key resolveKey(JsonWebSignature jws, List<JsonWebStructure> nestingContext) throws UnresolvableKeyException {
        JsonWebKey selected;
        try {
            if (this.disambiguateWithVerifySignature) {
                selected = this.selector.selectWithVerifySignatureDisambiguate(jws, this.jsonWebKeys);
            } else {
                selected = this.selector.select(jws, this.jsonWebKeys);
            }
            if (selected == null) {
                StringBuilder sb = new StringBuilder();
                sb.append("Unable to find a suitable verification key for JWS w/ header ").append(jws.getHeaders().getFullHeaderAsJsonString());
                sb.append(" from JWKs ").append(this.jsonWebKeys);
                throw new UnresolvableKeyException(sb.toString());
            }
            return selected.getKey();
        } catch (JoseException e) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Unable to find a suitable verification key for JWS w/ header ").append(jws.getHeaders().getFullHeaderAsJsonString());
            sb2.append(" due to an unexpected exception (").append(e).append(") selecting from keys: ").append(this.jsonWebKeys);
            throw new UnresolvableKeyException(sb2.toString(), e);
        }
    }

    public void setDisambiguateWithVerifySignature(boolean disambiguateWithVerifySignature) {
        this.disambiguateWithVerifySignature = disambiguateWithVerifySignature;
    }
}
