package io.netty.resolver.dns;

import io.netty.handler.codec.dns.DnsResponseCode;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.ThrowableUtil;

/* loaded from: classes4.dex */
public final class DnsErrorCauseException extends RuntimeException {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final long serialVersionUID = 7485145036717494533L;
    private final DnsResponseCode code;

    private DnsErrorCauseException(String message, DnsResponseCode code) {
        super(message);
        this.code = code;
    }

    private DnsErrorCauseException(String message, DnsResponseCode code, boolean shared) {
        super(message, null, false, true);
        this.code = code;
        if (!shared) {
            throw new AssertionError();
        }
    }

    @Override // java.lang.Throwable
    public Throwable fillInStackTrace() {
        return this;
    }

    public DnsResponseCode getCode() {
        return this.code;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static DnsErrorCauseException newStatic(String message, DnsResponseCode code, Class<?> clazz, String method) {
        DnsErrorCauseException exception;
        if (PlatformDependent.javaVersion() >= 7) {
            exception = new DnsErrorCauseException(message, code, true);
        } else {
            exception = new DnsErrorCauseException(message, code);
        }
        return (DnsErrorCauseException) ThrowableUtil.unknownStackTrace(exception, clazz, method);
    }
}
