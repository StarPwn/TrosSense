package org.jose4j.jwt.consumer;

import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.ErrorCodeValidator;

/* loaded from: classes5.dex */
public class SubValidator implements ErrorCodeValidator {
    private static final ErrorCodeValidator.Error MISSING_SUB = new ErrorCodeValidator.Error(14, "No Subject (sub) claim is present.");
    private String expectedSubject;
    private boolean requireSubject;

    public SubValidator(boolean requireSubject) {
        this.requireSubject = requireSubject;
    }

    public SubValidator(String expectedSubject) {
        this(true);
        this.expectedSubject = expectedSubject;
    }

    @Override // org.jose4j.jwt.consumer.ErrorCodeValidator
    public ErrorCodeValidator.Error validate(JwtContext jwtContext) throws MalformedClaimException {
        JwtClaims jwtClaims = jwtContext.getJwtClaims();
        String subject = jwtClaims.getSubject();
        if (subject == null && this.requireSubject) {
            return MISSING_SUB;
        }
        if (this.expectedSubject != null && !this.expectedSubject.equals(subject)) {
            String msg = "Subject (sub) claim value (" + subject + ") doesn't match expected value of " + this.expectedSubject;
            return new ErrorCodeValidator.Error(15, msg);
        }
        return null;
    }
}
