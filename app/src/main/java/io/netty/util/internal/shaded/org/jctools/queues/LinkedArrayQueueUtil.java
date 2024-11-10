package io.netty.util.internal.shaded.org.jctools.queues;

import io.netty.util.internal.shaded.org.jctools.util.UnsafeRefArrayAccess;

/* loaded from: classes4.dex */
final class LinkedArrayQueueUtil {
    LinkedArrayQueueUtil() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int length(Object[] buf) {
        return buf.length;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long modifiedCalcCircularRefElementOffset(long index, long mask) {
        return UnsafeRefArrayAccess.REF_ARRAY_BASE + ((index & mask) << (UnsafeRefArrayAccess.REF_ELEMENT_SHIFT - 1));
    }

    static long nextArrayOffset(Object[] curr) {
        return UnsafeRefArrayAccess.REF_ARRAY_BASE + ((length(curr) - 1) << UnsafeRefArrayAccess.REF_ELEMENT_SHIFT);
    }
}
