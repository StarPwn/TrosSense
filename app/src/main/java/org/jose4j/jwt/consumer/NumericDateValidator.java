package org.jose4j.jwt.consumer;

import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwt.consumer.ErrorCodeValidator;
import org.jose4j.lang.Maths;

/* loaded from: classes5.dex */
public class NumericDateValidator implements ErrorCodeValidator {
    private static final ErrorCodeValidator.Error MISSING_EXP = new ErrorCodeValidator.Error(2, "No Expiration Time (exp) claim present.");
    private static final ErrorCodeValidator.Error MISSING_IAT = new ErrorCodeValidator.Error(3, "No Issued At (iat) claim present.");
    private static final ErrorCodeValidator.Error MISSING_NBF = new ErrorCodeValidator.Error(4, "No Not Before (nbf) claim present.");
    private Integer iatAllowedSecondsInTheFuture;
    private Integer iatAllowedSecondsInThePast;
    private boolean requireExp;
    private boolean requireIat;
    private boolean requireNbf;
    private NumericDate staticEvaluationTime;
    private int allowedClockSkewSeconds = 0;
    private int maxFutureValidityInMinutes = 0;

    public void setRequireExp(boolean requireExp) {
        this.requireExp = requireExp;
    }

    public void setRequireIat(boolean requireIat) {
        this.requireIat = requireIat;
    }

    public void setRequireNbf(boolean requireNbf) {
        this.requireNbf = requireNbf;
    }

    public void setEvaluationTime(NumericDate evaluationTime) {
        this.staticEvaluationTime = evaluationTime;
    }

    public void setAllowedClockSkewSeconds(int allowedClockSkewSeconds) {
        this.allowedClockSkewSeconds = allowedClockSkewSeconds;
    }

    public void setMaxFutureValidityInMinutes(int maxFutureValidityInMinutes) {
        this.maxFutureValidityInMinutes = maxFutureValidityInMinutes;
    }

    public void setIatAllowedSecondsInTheFuture(int iatAllowedSecondsInTheFuture) {
        this.iatAllowedSecondsInTheFuture = Integer.valueOf(iatAllowedSecondsInTheFuture);
    }

    public void setIatAllowedSecondsInThePast(int iatAllowedSecondsInThePast) {
        this.iatAllowedSecondsInThePast = Integer.valueOf(iatAllowedSecondsInThePast);
    }

    @Override // org.jose4j.jwt.consumer.ErrorCodeValidator
    public ErrorCodeValidator.Error validate(JwtContext jwtContext) throws MalformedClaimException {
        JwtClaims jwtClaims = jwtContext.getJwtClaims();
        NumericDate expirationTime = jwtClaims.getExpirationTime();
        NumericDate issuedAt = jwtClaims.getIssuedAt();
        NumericDate notBefore = jwtClaims.getNotBefore();
        if (this.requireExp && expirationTime == null) {
            return MISSING_EXP;
        }
        if (this.requireIat && issuedAt == null) {
            return MISSING_IAT;
        }
        if (this.requireNbf && notBefore == null) {
            return MISSING_NBF;
        }
        NumericDate evaluationTime = this.staticEvaluationTime == null ? NumericDate.now() : this.staticEvaluationTime;
        if (expirationTime != null) {
            if (Maths.subtract(evaluationTime.getValue(), this.allowedClockSkewSeconds) >= expirationTime.getValue()) {
                String msg = "The JWT is no longer valid - the evaluation time " + evaluationTime + " is on or after the Expiration Time (exp=" + expirationTime + ") claim value" + skewMessage();
                return new ErrorCodeValidator.Error(1, msg);
            }
            if (issuedAt != null && expirationTime.isBefore(issuedAt)) {
                return new ErrorCodeValidator.Error(17, "The Expiration Time (exp=" + expirationTime + ") claim value cannot be before the Issued At (iat=" + issuedAt + ") claim value.");
            }
            if (notBefore != null && expirationTime.isBefore(notBefore)) {
                return new ErrorCodeValidator.Error(17, "The Expiration Time (exp=" + expirationTime + ") claim value cannot be before the Not Before (nbf=" + notBefore + ") claim value.");
            }
            if (this.maxFutureValidityInMinutes > 0) {
                long deltaInSeconds = Maths.subtract(Maths.subtract(expirationTime.getValue(), this.allowedClockSkewSeconds), evaluationTime.getValue());
                if (deltaInSeconds > this.maxFutureValidityInMinutes * 60) {
                    String msg2 = "The Expiration Time (exp=" + expirationTime + ") claim value cannot be more than " + this.maxFutureValidityInMinutes + " minutes in the future relative to the evaluation time " + evaluationTime + skewMessage();
                    return new ErrorCodeValidator.Error(5, msg2);
                }
            }
        }
        if (notBefore != null && Maths.add(evaluationTime.getValue(), this.allowedClockSkewSeconds) < notBefore.getValue()) {
            String msg3 = "The JWT is not yet valid as the evaluation time " + evaluationTime + " is before the Not Before (nbf=" + notBefore + ") claim time" + skewMessage();
            return new ErrorCodeValidator.Error(6, msg3);
        }
        if (issuedAt != null) {
            if (this.iatAllowedSecondsInTheFuture != null && Maths.subtract(Maths.subtract(issuedAt.getValue(), evaluationTime.getValue()), this.allowedClockSkewSeconds) > this.iatAllowedSecondsInTheFuture.intValue()) {
                String msg4 = "iat " + issuedAt + " is more than " + this.iatAllowedSecondsInTheFuture + " second(s) ahead of now " + evaluationTime + skewMessage();
                return new ErrorCodeValidator.Error(23, msg4);
            }
            if (this.iatAllowedSecondsInThePast != null && Maths.subtract(Maths.subtract(evaluationTime.getValue(), issuedAt.getValue()), this.allowedClockSkewSeconds) > this.iatAllowedSecondsInThePast.intValue()) {
                String msg5 = "As of now " + evaluationTime + " iat " + issuedAt + " is more than " + this.iatAllowedSecondsInThePast + " second(s) in the past" + skewMessage();
                return new ErrorCodeValidator.Error(24, msg5);
            }
            return null;
        }
        return null;
    }

    private String skewMessage() {
        if (this.allowedClockSkewSeconds > 0) {
            return " (even when providing " + this.allowedClockSkewSeconds + " seconds of leeway to account for clock skew).";
        }
        return ".";
    }
}
