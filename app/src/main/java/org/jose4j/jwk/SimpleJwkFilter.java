package org.jose4j.jwk;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/* loaded from: classes5.dex */
public class SimpleJwkFilter {
    private Criteria alg;
    private boolean allowThumbsFallbackDeriveFromX5c;
    private MultiValueCriteria crv;
    private MultiValueCriteria keyOps;
    private Criteria kid;
    private Criteria kty;
    private Criteria use;
    private Criteria x5t;
    private Criteria x5tS256;
    public static boolean OMITTED_OKAY = true;
    public static boolean VALUE_REQUIRED = false;
    private static final String[] EMPTY = new String[2];

    public void setKid(String expectedKid, boolean omittedValueAcceptable) {
        this.kid = new Criteria(expectedKid, omittedValueAcceptable);
    }

    public void setKty(String expectedKty) {
        this.kty = new Criteria(expectedKty, false);
    }

    public void setUse(String expectedUse, boolean omittedValueAcceptable) {
        this.use = new Criteria(expectedUse, omittedValueAcceptable);
    }

    public void setKeyOperations(String[] expectedKeyOps, boolean omittedValueAcceptable) {
        this.keyOps = new MultiValueCriteria(expectedKeyOps, omittedValueAcceptable);
    }

    public void setAlg(String expectedAlg, boolean omittedValueAcceptable) {
        this.alg = new Criteria(expectedAlg, omittedValueAcceptable);
    }

    public void setX5t(String expectedThumb, boolean omittedValueAcceptable) {
        this.x5t = new Criteria(expectedThumb, omittedValueAcceptable);
    }

    public void setX5tS256(String expectedThumb, boolean omittedValueAcceptable) {
        this.x5tS256 = new Criteria(expectedThumb, omittedValueAcceptable);
    }

    public void setAllowFallbackDeriveFromX5cForX5Thumbs(boolean allow) {
        this.allowThumbsFallbackDeriveFromX5c = allow;
    }

    public void setCrv(String expectedCrv, boolean omittedValueAcceptable) {
        this.crv = new MultiValueCriteria(new String[]{expectedCrv}, omittedValueAcceptable);
    }

    public void setCrvs(String[] expectedCrvs, boolean omittedValueAcceptable) {
        this.crv = new MultiValueCriteria(expectedCrvs, omittedValueAcceptable);
    }

    public List<JsonWebKey> filter(Collection<JsonWebKey> jsonWebKeys) {
        List<JsonWebKey> filtered = new ArrayList<>();
        for (JsonWebKey jwk : jsonWebKeys) {
            boolean match = isMatch(this.kid, jwk.getKeyId());
            boolean match2 = match & isMatch(this.kty, jwk.getKeyType()) & isMatch(this.use, jwk.getUse()) & isMatch(this.alg, jwk.getAlgorithm());
            String[] thumbs = getThumbs(jwk, this.allowThumbsFallbackDeriveFromX5c);
            if (match2 & isMatch(this.x5t, thumbs[0]) & isMatch(this.x5tS256, thumbs[1]) & (this.crv == null || this.crv.meetsCriteria(getCrv(jwk))) & (this.keyOps == null || this.keyOps.meetsCriteria(jwk.getKeyOps()))) {
                filtered.add(jwk);
            }
        }
        return filtered;
    }

    boolean isMatch(Criteria criteria, String value) {
        return criteria == null || criteria.meetsCriteria(value);
    }

    String getCrv(JsonWebKey jwk) {
        if (jwk instanceof EllipticCurveJsonWebKey) {
            return ((EllipticCurveJsonWebKey) jwk).getCurveName();
        }
        if (jwk instanceof OctetKeyPairJsonWebKey) {
            return ((OctetKeyPairJsonWebKey) jwk).getSubtype();
        }
        return null;
    }

    String[] getThumbs(JsonWebKey jwk, boolean allowFallbackDeriveFromX5c) {
        if (this.x5t == null && this.x5tS256 == null) {
            return EMPTY;
        }
        if (jwk instanceof PublicJsonWebKey) {
            PublicJsonWebKey publicJwk = (PublicJsonWebKey) jwk;
            String x5t = publicJwk.getX509CertificateSha1Thumbprint(allowFallbackDeriveFromX5c);
            String x5tS256 = publicJwk.getX509CertificateSha256Thumbprint(allowFallbackDeriveFromX5c);
            return new String[]{x5t, x5tS256};
        }
        return EMPTY;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static class Criteria {
        boolean noValueOk;
        String value;

        private Criteria(String value, boolean noValueOk) {
            this.value = value;
            this.noValueOk = noValueOk;
        }

        public boolean meetsCriteria(String value) {
            if (value == null) {
                return this.noValueOk;
            }
            return value.equals(this.value);
        }
    }

    /* loaded from: classes5.dex */
    private static class MultiValueCriteria {
        boolean noValueOk;
        String[] values;

        private MultiValueCriteria(String[] values, boolean noValueOk) {
            this.values = values;
            this.noValueOk = noValueOk;
        }

        public boolean meetsCriteria(String value) {
            return meetsCriteria(Collections.singletonList(value));
        }

        public boolean meetsCriteria(List<String> values) {
            if (values == null) {
                return this.noValueOk;
            }
            for (String value : this.values) {
                if (values.contains(value)) {
                    return true;
                }
            }
            return false;
        }
    }
}
