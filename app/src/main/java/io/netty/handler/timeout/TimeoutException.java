package io.netty.handler.timeout;

import io.netty.channel.ChannelException;

/* loaded from: classes4.dex */
public class TimeoutException extends ChannelException {
    private static final long serialVersionUID = 4673641882869672533L;

    /* JADX INFO: Access modifiers changed from: package-private */
    public TimeoutException() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TimeoutException(String message, boolean shared) {
        super(message, null, shared);
    }

    @Override // java.lang.Throwable
    public Throwable fillInStackTrace() {
        return this;
    }
}
