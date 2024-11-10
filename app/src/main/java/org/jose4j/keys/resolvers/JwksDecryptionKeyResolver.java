package org.jose4j.keys.resolvers;

import java.security.Key;
import java.util.List;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwk.DecryptionJwkSelector;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.PublicJsonWebKey;
import org.jose4j.jwx.JsonWebStructure;
import org.jose4j.lang.JoseException;
import org.jose4j.lang.UnresolvableKeyException;

/* loaded from: classes5.dex */
public class JwksDecryptionKeyResolver implements DecryptionKeyResolver {
    boolean disambiguateWithAttemptDecrypt;
    private final List<JsonWebKey> jsonWebKeys;
    private final DecryptionJwkSelector selector = new DecryptionJwkSelector();

    public JwksDecryptionKeyResolver(List<JsonWebKey> jsonWebKeys) {
        this.jsonWebKeys = jsonWebKeys;
    }

    @Override // org.jose4j.keys.resolvers.DecryptionKeyResolver
    public Key resolveKey(JsonWebEncryption jwe, List<JsonWebStructure> nestingContext) throws UnresolvableKeyException {
        JsonWebKey selected;
        try {
            List<JsonWebKey> selectedList = this.selector.selectList(jwe, this.jsonWebKeys);
            if (selectedList.isEmpty()) {
                selected = null;
            } else {
                if (selectedList.size() != 1 && this.disambiguateWithAttemptDecrypt) {
                    selected = this.selector.attemptDecryptDisambiguate(jwe, selectedList);
                    if (selected == null) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Unable to find a suitable key for JWE w/ header ").append(jwe.getHeaders().getFullHeaderAsJsonString());
                        sb.append(" using attempted decryption to disambiguate from filtered candidate JWKs ").append(this.jsonWebKeys);
                        throw new UnresolvableKeyException(sb.toString());
                    }
                }
                selected = selectedList.get(0);
            }
            if (selected != null) {
                return selected instanceof PublicJsonWebKey ? ((PublicJsonWebKey) selected).getPrivateKey() : selected.getKey();
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Unable to find a suitable key for JWE w/ header ").append(jwe.getHeaders().getFullHeaderAsJsonString());
            sb2.append(" from JWKs ").append(this.jsonWebKeys);
            throw new UnresolvableKeyException(sb2.toString());
        } catch (JoseException e) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Unable to find a suitable key for JWE w/ header ").append(jwe.getHeaders().getFullHeaderAsJsonString());
            sb3.append(" due to an unexpected exception (").append(e).append(") selecting from keys: ").append(this.jsonWebKeys);
            throw new UnresolvableKeyException(sb3.toString(), e);
        }
    }

    public void setDisambiguateWithAttemptDecrypt(boolean disambiguateWithAttemptDecrypt) {
        this.disambiguateWithAttemptDecrypt = disambiguateWithAttemptDecrypt;
    }
}
