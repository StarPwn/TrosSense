package org.jose4j.jwt.consumer;

import java.util.List;
import java.util.Set;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.ErrorCodeValidator;

/* loaded from: classes5.dex */
public class AudValidator implements ErrorCodeValidator {
    private static final ErrorCodeValidator.Error MISSING_AUD = new ErrorCodeValidator.Error(7, "No Audience (aud) claim present.");
    private Set<String> acceptableAudiences;
    private boolean requireAudience;

    public AudValidator(Set<String> acceptableAudiences, boolean requireAudience) {
        this.acceptableAudiences = acceptableAudiences;
        this.requireAudience = requireAudience;
    }

    @Override // org.jose4j.jwt.consumer.ErrorCodeValidator
    public ErrorCodeValidator.Error validate(JwtContext jwtContext) throws MalformedClaimException {
        JwtClaims jwtClaims = jwtContext.getJwtClaims();
        if (!jwtClaims.hasAudience()) {
            if (this.requireAudience) {
                return MISSING_AUD;
            }
            return null;
        }
        List<String> audiences = jwtClaims.getAudience();
        boolean ok = false;
        for (String audience : audiences) {
            if (this.acceptableAudiences.contains(audience)) {
                ok = true;
            }
        }
        if (ok) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Audience (aud) claim ").append(audiences);
        if (this.acceptableAudiences.isEmpty()) {
            sb.append(" present in the JWT but no expected audience value(s) were provided to the JWT Consumer.");
        } else {
            sb.append(" doesn't contain an acceptable identifier.");
        }
        sb.append(" Expected ");
        if (this.acceptableAudiences.size() == 1) {
            sb.append(this.acceptableAudiences.iterator().next());
        } else {
            sb.append("one of ").append(this.acceptableAudiences);
        }
        sb.append(" as an aud value.");
        return new ErrorCodeValidator.Error(8, sb.toString());
    }
}
