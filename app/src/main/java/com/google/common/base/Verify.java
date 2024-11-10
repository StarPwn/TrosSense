package com.google.common.base;

import javax.annotation.Nullable;

/* loaded from: classes.dex */
public final class Verify {
    public static void verify(boolean expression) {
        if (!expression) {
            throw new VerifyException();
        }
    }

    public static void verify(boolean expression, @Nullable String errorMessageTemplate, @Nullable Object... errorMessageArgs) {
        if (!expression) {
            throw new VerifyException(Preconditions.format(errorMessageTemplate, errorMessageArgs));
        }
    }

    public static <T> T verifyNotNull(@Nullable T t) {
        return (T) verifyNotNull(t, "expected a non-null reference", new Object[0]);
    }

    public static <T> T verifyNotNull(@Nullable T reference, @Nullable String errorMessageTemplate, @Nullable Object... errorMessageArgs) {
        verify(reference != null, errorMessageTemplate, errorMessageArgs);
        return reference;
    }

    private Verify() {
    }
}
