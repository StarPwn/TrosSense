package org.cloudburstmc.netty.util;

/* loaded from: classes5.dex */
public class IntRange {
    public int end;
    public int start;

    public IntRange(int num) {
        this(num, num);
    }

    public IntRange(int start, int end) {
        this.start = start;
        this.end = end;
    }
}
