package io.netty.channel;

import io.netty.util.internal.ThrowableUtil;
import java.nio.channels.ClosedChannelException;

/* loaded from: classes4.dex */
final class StacklessClosedChannelException extends ClosedChannelException {
    private static final long serialVersionUID = -2214806025529435136L;

    private StacklessClosedChannelException() {
    }

    @Override // java.lang.Throwable
    public Throwable fillInStackTrace() {
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static StacklessClosedChannelException newInstance(Class<?> clazz, String method) {
        return (StacklessClosedChannelException) ThrowableUtil.unknownStackTrace(new StacklessClosedChannelException(), clazz, method);
    }
}
