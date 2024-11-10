package org.jose4j.jwk;

import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwx.JsonWebStructure;
import org.jose4j.lang.JoseException;

/* loaded from: classes5.dex */
class SelectorSupport {
    private static final String[] VERIFY_OPS = {KeyOperations.VERIFY};
    private static final String[] DECRYPT_OPS = {KeyOperations.DECRYPT, KeyOperations.DERIVE_KEY, KeyOperations.UNWRAP_KEY};

    SelectorSupport() {
    }

    public static SimpleJwkFilter filterForInboundSigned(JsonWebSignature jws) throws JoseException {
        SimpleJwkFilter filter = commonFilterForInbound(jws);
        filter.setUse(Use.SIGNATURE, SimpleJwkFilter.OMITTED_OKAY);
        filter.setKeyOperations(VERIFY_OPS, SimpleJwkFilter.OMITTED_OKAY);
        return filter;
    }

    public static SimpleJwkFilter filterForInboundEncrypted(JsonWebEncryption jwe) throws JoseException {
        SimpleJwkFilter filter = commonFilterForInbound(jwe);
        filter.setUse("enc", SimpleJwkFilter.OMITTED_OKAY);
        filter.setKeyOperations(DECRYPT_OPS, SimpleJwkFilter.OMITTED_OKAY);
        return filter;
    }

    private static SimpleJwkFilter commonFilterForInbound(JsonWebStructure jwx) throws JoseException {
        SimpleJwkFilter filter = new SimpleJwkFilter();
        String kid = jwx.getKeyIdHeaderValue();
        if (kid != null) {
            filter.setKid(kid, SimpleJwkFilter.VALUE_REQUIRED);
        }
        String x5t = jwx.getX509CertSha1ThumbprintHeaderValue();
        String x5tS256 = jwx.getX509CertSha256ThumbprintHeaderValue();
        filter.setAllowFallbackDeriveFromX5cForX5Thumbs(true);
        if (x5t != null) {
            filter.setX5t(x5t, SimpleJwkFilter.OMITTED_OKAY);
        }
        if (x5tS256 != null) {
            filter.setX5tS256(x5tS256, SimpleJwkFilter.OMITTED_OKAY);
        }
        String keyType = jwx.getAlgorithmNoConstraintCheck().getKeyType();
        filter.setKty(keyType);
        return filter;
    }
}
