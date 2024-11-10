package org.jose4j.jwt.consumer;

import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.ErrorCodeValidator;

/* loaded from: classes5.dex */
public class ErrorCodeValidatorAdapter implements ErrorCodeValidator {
    private Validator validator;

    public ErrorCodeValidatorAdapter(Validator validator) {
        this.validator = validator;
    }

    @Override // org.jose4j.jwt.consumer.ErrorCodeValidator
    public ErrorCodeValidator.Error validate(JwtContext jwtContext) throws MalformedClaimException {
        String result = this.validator.validate(jwtContext);
        if (result == null) {
            return null;
        }
        return new ErrorCodeValidator.Error(17, result);
    }
}
