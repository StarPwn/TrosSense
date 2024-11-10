package com.google.common.base;

import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class VerifyException extends RuntimeException {
    public VerifyException() {
    }

    public VerifyException(@Nullable String message) {
        super(message);
    }

    public VerifyException(@Nullable Throwable cause) {
        super(cause);
    }

    public VerifyException(@Nullable String message, @Nullable Throwable cause) {
        super(message, cause);
    }
}
