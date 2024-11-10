package io.netty.handler.codec.compression;

import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/* loaded from: classes4.dex */
public final class Zstd {
    private static final Throwable cause;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) Zstd.class);

    static {
        Throwable t = null;
        try {
            Class.forName("com.github.luben.zstd.Zstd", false, PlatformDependent.getClassLoader(Zstd.class));
        } catch (ClassNotFoundException e) {
            t = e;
            logger.debug("zstd-jni not in the classpath; Zstd support will be unavailable.");
        } catch (Throwable e2) {
            t = e2;
            logger.debug("Failed to load zstd-jni; Zstd support will be unavailable.", t);
        }
        cause = t;
    }

    public static boolean isAvailable() {
        return cause == null;
    }

    public static void ensureAvailability() throws Throwable {
        if (cause != null) {
            throw cause;
        }
    }

    public static Throwable cause() {
        return cause;
    }

    private Zstd() {
    }
}
