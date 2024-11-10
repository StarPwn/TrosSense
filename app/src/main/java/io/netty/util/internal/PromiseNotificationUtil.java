package io.netty.util.internal;

import io.netty.util.concurrent.Promise;
import io.netty.util.internal.logging.InternalLogger;

/* loaded from: classes4.dex */
public final class PromiseNotificationUtil {
    private PromiseNotificationUtil() {
    }

    public static void tryCancel(Promise<?> p, InternalLogger logger) {
        if (!p.cancel(false) && logger != null) {
            Throwable err = p.cause();
            if (err == null) {
                logger.warn("Failed to cancel promise because it has succeeded already: {}", p);
            } else {
                logger.warn("Failed to cancel promise because it has failed already: {}, unnotified cause:", p, err);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <V> void trySuccess(Promise<? super V> p, V v, InternalLogger logger) {
        if (!p.trySuccess(v) && logger != null) {
            Throwable err = p.cause();
            if (err == null) {
                logger.warn("Failed to mark a promise as success because it has succeeded already: {}", p);
            } else {
                logger.warn("Failed to mark a promise as success because it has failed already: {}, unnotified cause:", p, err);
            }
        }
    }

    public static void tryFailure(Promise<?> p, Throwable cause, InternalLogger logger) {
        if (!p.tryFailure(cause) && logger != null) {
            Throwable err = p.cause();
            if (err == null) {
                logger.warn("Failed to mark a promise as failure because it has succeeded already: {}", p, cause);
            } else if (logger.isWarnEnabled()) {
                logger.warn("Failed to mark a promise as failure because it has failed already: {}, unnotified cause: {}", p, ThrowableUtil.stackTraceToString(err), cause);
            }
        }
    }
}
