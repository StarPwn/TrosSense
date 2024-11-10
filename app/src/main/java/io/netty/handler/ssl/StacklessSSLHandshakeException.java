package io.netty.handler.ssl;

import io.netty.util.internal.ThrowableUtil;
import javax.net.ssl.SSLHandshakeException;

/* loaded from: classes4.dex */
final class StacklessSSLHandshakeException extends SSLHandshakeException {
    private static final long serialVersionUID = -1244781947804415549L;

    private StacklessSSLHandshakeException(String reason) {
        super(reason);
    }

    @Override // java.lang.Throwable
    public Throwable fillInStackTrace() {
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static StacklessSSLHandshakeException newInstance(String reason, Class<?> clazz, String method) {
        return (StacklessSSLHandshakeException) ThrowableUtil.unknownStackTrace(new StacklessSSLHandshakeException(reason), clazz, method);
    }
}
