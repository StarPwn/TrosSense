package io.netty.util;

import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/* loaded from: classes4.dex */
public final class ReferenceCountUtil {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) ReferenceCountUtil.class);

    static {
        ResourceLeakDetector.addExclusions(ReferenceCountUtil.class, "touch");
    }

    public static <T> T retain(T t) {
        if (t instanceof ReferenceCounted) {
            return (T) ((ReferenceCounted) t).retain();
        }
        return t;
    }

    public static <T> T retain(T t, int i) {
        ObjectUtil.checkPositive(i, "increment");
        if (t instanceof ReferenceCounted) {
            return (T) ((ReferenceCounted) t).retain(i);
        }
        return t;
    }

    public static <T> T touch(T t) {
        if (t instanceof ReferenceCounted) {
            return (T) ((ReferenceCounted) t).touch();
        }
        return t;
    }

    public static <T> T touch(T t, Object obj) {
        if (t instanceof ReferenceCounted) {
            return (T) ((ReferenceCounted) t).touch(obj);
        }
        return t;
    }

    public static boolean release(Object msg) {
        if (msg instanceof ReferenceCounted) {
            return ((ReferenceCounted) msg).release();
        }
        return false;
    }

    public static boolean release(Object msg, int decrement) {
        ObjectUtil.checkPositive(decrement, "decrement");
        if (msg instanceof ReferenceCounted) {
            return ((ReferenceCounted) msg).release(decrement);
        }
        return false;
    }

    public static void safeRelease(Object msg) {
        try {
            release(msg);
        } catch (Throwable t) {
            logger.warn("Failed to release a message: {}", msg, t);
        }
    }

    public static void safeRelease(Object msg, int decrement) {
        try {
            ObjectUtil.checkPositive(decrement, "decrement");
            release(msg, decrement);
        } catch (Throwable t) {
            if (logger.isWarnEnabled()) {
                logger.warn("Failed to release a message: {} (decrement: {})", msg, Integer.valueOf(decrement), t);
            }
        }
    }

    @Deprecated
    public static <T> T releaseLater(T t) {
        return (T) releaseLater(t, 1);
    }

    @Deprecated
    public static <T> T releaseLater(T msg, int decrement) {
        ObjectUtil.checkPositive(decrement, "decrement");
        if (msg instanceof ReferenceCounted) {
            ThreadDeathWatcher.watch(Thread.currentThread(), new ReleasingTask((ReferenceCounted) msg, decrement));
        }
        return msg;
    }

    public static int refCnt(Object msg) {
        if (msg instanceof ReferenceCounted) {
            return ((ReferenceCounted) msg).refCnt();
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class ReleasingTask implements Runnable {
        private final int decrement;
        private final ReferenceCounted obj;

        ReleasingTask(ReferenceCounted obj, int decrement) {
            this.obj = obj;
            this.decrement = decrement;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                if (!this.obj.release(this.decrement)) {
                    ReferenceCountUtil.logger.warn("Non-zero refCnt: {}", this);
                } else {
                    ReferenceCountUtil.logger.debug("Released: {}", this);
                }
            } catch (Exception ex) {
                ReferenceCountUtil.logger.warn("Failed to release an object: {}", this.obj, ex);
            }
        }

        public String toString() {
            return StringUtil.simpleClassName(this.obj) + ".release(" + this.decrement + ") refCnt: " + this.obj.refCnt();
        }
    }

    private ReferenceCountUtil() {
    }
}
