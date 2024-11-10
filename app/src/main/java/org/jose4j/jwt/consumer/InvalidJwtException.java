package org.jose4j.jwt.consumer;

import java.util.Collections;
import java.util.List;
import org.jose4j.jwt.consumer.ErrorCodeValidator;

/* loaded from: classes5.dex */
public class InvalidJwtException extends Exception {
    private List<ErrorCodeValidator.Error> details;
    private JwtContext jwtContext;

    public InvalidJwtException(String message, List<ErrorCodeValidator.Error> details, JwtContext jwtContext) {
        super(message);
        this.details = Collections.emptyList();
        this.details = details;
        this.jwtContext = jwtContext;
    }

    public InvalidJwtException(String message, ErrorCodeValidator.Error detail, Throwable cause, JwtContext jwtContext) {
        super(message, cause);
        this.details = Collections.emptyList();
        this.jwtContext = jwtContext;
        this.details = Collections.singletonList(detail);
    }

    public boolean hasErrorCode(int code) {
        for (ErrorCodeValidator.Error error : this.details) {
            if (code == error.getErrorCode()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasExpired() {
        return hasErrorCode(1);
    }

    public List<ErrorCodeValidator.Error> getErrorDetails() {
        return this.details;
    }

    public JwtContext getJwtContext() {
        return this.jwtContext;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.getMessage());
        if (!this.details.isEmpty()) {
            sb.append(" Additional details: ");
            sb.append(this.details);
        }
        return sb.toString();
    }

    public String getOriginalMessage() {
        return super.getMessage();
    }
}
