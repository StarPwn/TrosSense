package io.netty.handler.ssl;

import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public abstract class SslCompletionEvent {
    private final Throwable cause;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SslCompletionEvent() {
        this.cause = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SslCompletionEvent(Throwable cause) {
        this.cause = (Throwable) ObjectUtil.checkNotNull(cause, "cause");
    }

    public final boolean isSuccess() {
        return this.cause == null;
    }

    public final Throwable cause() {
        return this.cause;
    }

    public String toString() {
        Throwable cause = cause();
        if (cause == null) {
            return getClass().getSimpleName() + "(SUCCESS)";
        }
        return getClass().getSimpleName() + '(' + cause + ')';
    }
}
