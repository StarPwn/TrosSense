package org.jose4j.keys.resolvers;

import java.io.IOException;
import java.security.Key;
import java.util.List;
import org.jose4j.jwk.HttpsJwks;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.VerificationJwkSelector;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwx.JsonWebStructure;
import org.jose4j.lang.JoseException;
import org.jose4j.lang.UnresolvableKeyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: classes5.dex */
public class HttpsJwksVerificationKeyResolver implements VerificationKeyResolver {
    private static final Logger log = LoggerFactory.getLogger((Class<?>) HttpsJwksVerificationKeyResolver.class);
    private boolean disambiguateWithVerifySignature;
    private HttpsJwks httpsJkws;
    private VerificationJwkSelector verificationJwkSelector = new VerificationJwkSelector();

    public HttpsJwksVerificationKeyResolver(HttpsJwks httpsJkws) {
        this.httpsJkws = httpsJkws;
    }

    @Override // org.jose4j.keys.resolvers.VerificationKeyResolver
    public Key resolveKey(JsonWebSignature jws, List<JsonWebStructure> nestingContext) throws UnresolvableKeyException {
        try {
            List<JsonWebKey> jsonWebKeys = this.httpsJkws.getJsonWebKeys();
            JsonWebKey theChosenOne = select(jws, jsonWebKeys);
            if (theChosenOne == null) {
                log.debug("Refreshing JWKs from {} as no suitable verification key for JWS w/ header {} was found in {}", this.httpsJkws.getLocation(), jws.getHeaders().getFullHeaderAsJsonString(), jsonWebKeys);
                this.httpsJkws.refresh();
                jsonWebKeys = this.httpsJkws.getJsonWebKeys();
                theChosenOne = select(jws, jsonWebKeys);
            }
            if (theChosenOne == null) {
                StringBuilder sb = new StringBuilder();
                sb.append("Unable to find a suitable verification key for JWS w/ header ").append(jws.getHeaders().getFullHeaderAsJsonString());
                sb.append(" from JWKs ").append(jsonWebKeys).append(" obtained from ").append(this.httpsJkws.getLocation());
                throw new UnresolvableKeyException(sb.toString());
            }
            return theChosenOne.getKey();
        } catch (IOException | JoseException e) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Unable to find a suitable verification key for JWS w/ header ").append(jws.getHeaders().getFullHeaderAsJsonString());
            sb2.append(" due to an unexpected exception (").append(e).append(") while obtaining or using keys from JWKS endpoint at ").append(this.httpsJkws.getLocation());
            throw new UnresolvableKeyException(sb2.toString(), e);
        }
    }

    protected JsonWebKey select(JsonWebSignature jws, List<JsonWebKey> jsonWebKeys) throws JoseException {
        if (this.disambiguateWithVerifySignature) {
            return this.verificationJwkSelector.selectWithVerifySignatureDisambiguate(jws, jsonWebKeys);
        }
        return this.verificationJwkSelector.select(jws, jsonWebKeys);
    }

    public void setDisambiguateWithVerifySignature(boolean disambiguateWithVerifySignature) {
        this.disambiguateWithVerifySignature = disambiguateWithVerifySignature;
    }
}
