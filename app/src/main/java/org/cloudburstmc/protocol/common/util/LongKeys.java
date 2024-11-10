package org.cloudburstmc.protocol.common.util;

/* loaded from: classes5.dex */
public final class LongKeys {
    private LongKeys() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static int high(long key) {
        return (int) (key >> 32);
    }

    public static int low(long key) {
        return (int) key;
    }

    public static long key(int high, int low) {
        return (high << 32) | (low & 4294967295L);
    }
}
