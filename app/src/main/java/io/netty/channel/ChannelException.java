package io.netty.channel;

import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.ThrowableUtil;

/* loaded from: classes4.dex */
public class ChannelException extends RuntimeException {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final long serialVersionUID = 2908618315971075004L;

    public ChannelException() {
    }

    public ChannelException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChannelException(String message) {
        super(message);
    }

    public ChannelException(Throwable cause) {
        super(cause);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ChannelException(String message, Throwable cause, boolean shared) {
        super(message, cause, false, true);
        if (!shared) {
            throw new AssertionError();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ChannelException newStatic(String message, Class<?> clazz, String method) {
        ChannelException exception;
        if (PlatformDependent.javaVersion() >= 7) {
            exception = new StacklessChannelException(message, null, true);
        } else {
            exception = new StacklessChannelException(message, null);
        }
        return (ChannelException) ThrowableUtil.unknownStackTrace(exception, clazz, method);
    }

    /* loaded from: classes4.dex */
    private static final class StacklessChannelException extends ChannelException {
        private static final long serialVersionUID = -6384642137753538579L;

        StacklessChannelException(String message, Throwable cause) {
            super(message, cause);
        }

        StacklessChannelException(String message, Throwable cause, boolean shared) {
            super(message, cause, shared);
        }

        @Override // java.lang.Throwable
        public Throwable fillInStackTrace() {
            return this;
        }
    }
}
