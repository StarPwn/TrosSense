package io.netty.channel.kqueue;

import io.netty.channel.unix.FileDescriptor;
import io.netty.util.internal.SystemPropertyUtil;

/* loaded from: classes4.dex */
public final class KQueue {
    private static final Throwable UNAVAILABILITY_CAUSE;

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:17:0x0020 -> B:13:0x001e). Please report as a decompilation issue!!! */
    static {
        FileDescriptor kqueueFd;
        Throwable cause = null;
        if (SystemPropertyUtil.getBoolean("io.netty.transport.noNative", false)) {
            cause = new UnsupportedOperationException("Native transport was explicit disabled with -Dio.netty.transport.noNative=true");
        } else {
            FileDescriptor kqueueFd2 = null;
            try {
                try {
                    kqueueFd = Native.newKQueue();
                } catch (Throwable t) {
                    cause = t;
                    if (0 != 0) {
                        kqueueFd2.close();
                    }
                }
            } catch (Exception e) {
            }
            if (kqueueFd != null) {
                kqueueFd.close();
            }
        }
        UNAVAILABILITY_CAUSE = cause;
    }

    public static boolean isAvailable() {
        return UNAVAILABILITY_CAUSE == null;
    }

    public static void ensureAvailability() {
        if (UNAVAILABILITY_CAUSE != null) {
            throw ((Error) new UnsatisfiedLinkError("failed to load the required native library").initCause(UNAVAILABILITY_CAUSE));
        }
    }

    public static Throwable unavailabilityCause() {
        return UNAVAILABILITY_CAUSE;
    }

    public static boolean isTcpFastOpenClientSideAvailable() {
        return isAvailable() && Native.IS_SUPPORTING_TCP_FASTOPEN_CLIENT;
    }

    public static boolean isTcpFastOpenServerSideAvailable() {
        return isAvailable() && Native.IS_SUPPORTING_TCP_FASTOPEN_SERVER;
    }

    private KQueue() {
    }
}
