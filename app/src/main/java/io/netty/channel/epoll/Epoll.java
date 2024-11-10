package io.netty.channel.epoll;

import io.netty.channel.unix.FileDescriptor;
import io.netty.util.internal.SystemPropertyUtil;

/* loaded from: classes4.dex */
public final class Epoll {
    private static final Throwable UNAVAILABILITY_CAUSE;

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:21:0x002c -> B:14:0x003d). Please report as a decompilation issue!!! */
    static {
        Throwable cause = null;
        if (SystemPropertyUtil.getBoolean("io.netty.transport.noNative", false)) {
            cause = new UnsupportedOperationException("Native transport was explicit disabled with -Dio.netty.transport.noNative=true");
        } else {
            FileDescriptor epollFd = null;
            FileDescriptor eventFd = null;
            try {
                try {
                    epollFd = Native.newEpollCreate();
                    FileDescriptor eventFd2 = Native.newEventFd();
                    if (epollFd != null) {
                        try {
                            epollFd.close();
                        } catch (Exception e) {
                        }
                    }
                    if (eventFd2 != null) {
                        eventFd2.close();
                    }
                } catch (Throwable t) {
                    cause = t;
                    if (epollFd != null) {
                        try {
                            epollFd.close();
                        } catch (Exception e2) {
                        }
                    }
                    if (0 != 0) {
                        eventFd.close();
                    }
                }
            } catch (Exception e3) {
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

    private Epoll() {
    }
}
