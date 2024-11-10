package io.netty.channel;

import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public final class WriteBufferWaterMark {
    public static final WriteBufferWaterMark DEFAULT = new WriteBufferWaterMark(32768, 65536, false);
    private static final int DEFAULT_HIGH_WATER_MARK = 65536;
    private static final int DEFAULT_LOW_WATER_MARK = 32768;
    private final int high;
    private final int low;

    public WriteBufferWaterMark(int low, int high) {
        this(low, high, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WriteBufferWaterMark(int low, int high, boolean validate) {
        if (validate) {
            ObjectUtil.checkPositiveOrZero(low, "low");
            if (high < low) {
                throw new IllegalArgumentException("write buffer's high water mark cannot be less than  low water mark (" + low + "): " + high);
            }
        }
        this.low = low;
        this.high = high;
    }

    public int low() {
        return this.low;
    }

    public int high() {
        return this.high;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder(55).append("WriteBufferWaterMark(low: ").append(this.low).append(", high: ").append(this.high).append(")");
        return builder.toString();
    }
}
