package io.netty.channel;

import java.nio.channels.ClosedChannelException;

/* loaded from: classes4.dex */
final class ExtendedClosedChannelException extends ClosedChannelException {
    ExtendedClosedChannelException(Throwable cause) {
        if (cause != null) {
            initCause(cause);
        }
    }

    @Override // java.lang.Throwable
    public Throwable fillInStackTrace() {
        return this;
    }
}
