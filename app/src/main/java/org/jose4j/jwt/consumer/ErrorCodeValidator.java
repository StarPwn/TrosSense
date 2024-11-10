package org.jose4j.jwt.consumer;

import org.jose4j.jwt.MalformedClaimException;

/* loaded from: classes5.dex */
public interface ErrorCodeValidator {
    Error validate(JwtContext jwtContext) throws MalformedClaimException;

    /* loaded from: classes5.dex */
    public static class Error {
        private int errorCode;
        private String errorMessage;

        public Error(int errorCode, String errorMessage) {
            this.errorCode = errorCode;
            this.errorMessage = errorMessage;
        }

        public int getErrorCode() {
            return this.errorCode;
        }

        public String getErrorMessage() {
            return this.errorMessage;
        }

        public String toString() {
            return "[" + this.errorCode + "] " + this.errorMessage;
        }
    }
}
