package io.netty.handler.timeout;

import io.netty.util.internal.PlatformDependent;

/* loaded from: classes4.dex */
public final class ReadTimeoutException extends TimeoutException {
    public static final ReadTimeoutException INSTANCE;
    private static final long serialVersionUID = 169287984113283421L;

    static {
        INSTANCE = PlatformDependent.javaVersion() >= 7 ? new ReadTimeoutException(true) : new ReadTimeoutException();
    }

    public ReadTimeoutException() {
    }

    public ReadTimeoutException(String message) {
        super(message, false);
    }

    private ReadTimeoutException(boolean shared) {
        super(null, shared);
    }
}
