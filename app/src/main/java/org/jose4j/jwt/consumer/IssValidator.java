package org.jose4j.jwt.consumer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.ErrorCodeValidator;

/* loaded from: classes5.dex */
public class IssValidator implements ErrorCodeValidator {
    private Set<String> expectedIssuers;
    private boolean requireIssuer;

    public IssValidator(String expectedIssuer, boolean requireIssuer) {
        if (expectedIssuer != null) {
            this.expectedIssuers = Collections.singleton(expectedIssuer);
        }
        this.requireIssuer = requireIssuer;
    }

    public IssValidator(boolean requireIssuer, String... expectedIssuers) {
        this.requireIssuer = requireIssuer;
        if (expectedIssuers != null && expectedIssuers.length > 0) {
            this.expectedIssuers = new HashSet();
            Collections.addAll(this.expectedIssuers, expectedIssuers);
        }
    }

    @Override // org.jose4j.jwt.consumer.ErrorCodeValidator
    public ErrorCodeValidator.Error validate(JwtContext jwtContext) throws MalformedClaimException {
        String issuer = jwtContext.getJwtClaims().getIssuer();
        if (issuer == null) {
            if (this.requireIssuer) {
                return new ErrorCodeValidator.Error(11, "No Issuer (iss) claim present.");
            }
            return null;
        }
        if (this.expectedIssuers == null || this.expectedIssuers.contains(issuer)) {
            return null;
        }
        return new ErrorCodeValidator.Error(12, "Issuer (iss) claim value (" + issuer + ") doesn't match expected value of " + expectedValue());
    }

    private String expectedValue() {
        return this.expectedIssuers.size() == 1 ? this.expectedIssuers.iterator().next() : "one of " + this.expectedIssuers;
    }
}
