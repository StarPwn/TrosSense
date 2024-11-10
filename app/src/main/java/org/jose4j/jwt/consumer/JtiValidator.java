package org.jose4j.jwt.consumer;

import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.ErrorCodeValidator;

/* loaded from: classes5.dex */
public class JtiValidator implements ErrorCodeValidator {
    private static final ErrorCodeValidator.Error MISSING_JTI = new ErrorCodeValidator.Error(13, "The JWT ID (jti) claim is not present.");
    private boolean requireJti;

    public JtiValidator(boolean requireJti) {
        this.requireJti = requireJti;
    }

    @Override // org.jose4j.jwt.consumer.ErrorCodeValidator
    public ErrorCodeValidator.Error validate(JwtContext jwtContext) throws MalformedClaimException {
        String subject = jwtContext.getJwtClaims().getJwtId();
        if (subject == null && this.requireJti) {
            return MISSING_JTI;
        }
        return null;
    }
}
