package com.google.common.util.concurrent;

import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class ExecutionError extends Error {
    private static final long serialVersionUID = 0;

    protected ExecutionError() {
    }

    protected ExecutionError(@Nullable String message) {
        super(message);
    }

    public ExecutionError(@Nullable String message, @Nullable Error cause) {
        super(message, cause);
    }

    public ExecutionError(@Nullable Error cause) {
        super(cause);
    }
}
