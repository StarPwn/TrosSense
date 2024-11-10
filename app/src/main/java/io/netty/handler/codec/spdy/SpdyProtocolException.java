package io.netty.handler.codec.spdy;

import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.ThrowableUtil;

/* loaded from: classes4.dex */
public class SpdyProtocolException extends Exception {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final long serialVersionUID = 7870000537743847264L;

    public SpdyProtocolException() {
    }

    public SpdyProtocolException(String message, Throwable cause) {
        super(message, cause);
    }

    public SpdyProtocolException(String message) {
        super(message);
    }

    public SpdyProtocolException(Throwable cause) {
        super(cause);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SpdyProtocolException newStatic(String message, Class<?> clazz, String method) {
        SpdyProtocolException exception;
        if (PlatformDependent.javaVersion() >= 7) {
            exception = new StacklessSpdyProtocolException(message, true);
        } else {
            exception = new StacklessSpdyProtocolException(message);
        }
        return (SpdyProtocolException) ThrowableUtil.unknownStackTrace(exception, clazz, method);
    }

    private SpdyProtocolException(String message, boolean shared) {
        super(message, null, false, true);
        if (!shared) {
            throw new AssertionError();
        }
    }

    /* loaded from: classes4.dex */
    private static final class StacklessSpdyProtocolException extends SpdyProtocolException {
        private static final long serialVersionUID = -6302754207557485099L;

        StacklessSpdyProtocolException(String message) {
            super(message);
        }

        StacklessSpdyProtocolException(String message, boolean shared) {
            super(message, shared);
        }

        @Override // java.lang.Throwable
        public Throwable fillInStackTrace() {
            return this;
        }
    }
}
