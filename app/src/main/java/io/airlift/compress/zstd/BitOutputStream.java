package io.airlift.compress.zstd;

import kotlinx.coroutines.internal.LockFreeTaskQueueCore;
import okhttp3.internal.ws.WebSocketProtocol;

/* loaded from: classes.dex */
class BitOutputStream {
    private static final long[] BIT_MASK = {0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383, 32767, WebSocketProtocol.PAYLOAD_SHORT_MAX, 131071, 262143, 524287, 1048575, 2097151, 4194303, 8388607, 16777215, 33554431, 67108863, 134217727, 268435455, 536870911, LockFreeTaskQueueCore.HEAD_MASK, 2147483647L};
    private int bitCount;
    private long container;
    private long currentAddress;
    private final long outputAddress;
    private final Object outputBase;
    private final long outputLimit;

    public BitOutputStream(Object outputBase, long outputAddress, int outputSize) {
        Util.checkArgument(outputSize >= 8, "Output buffer too small");
        this.outputBase = outputBase;
        this.outputAddress = outputAddress;
        this.outputLimit = (this.outputAddress + outputSize) - 8;
        this.currentAddress = this.outputAddress;
    }

    public void addBits(int value, int bits) {
        this.container |= (value & BIT_MASK[bits]) << this.bitCount;
        this.bitCount += bits;
    }

    public void addBitsFast(int value, int bits) {
        this.container |= value << this.bitCount;
        this.bitCount += bits;
    }

    public void flush() {
        int bytes = this.bitCount >>> 3;
        UnsafeUtil.UNSAFE.putLong(this.outputBase, this.currentAddress, this.container);
        this.currentAddress += bytes;
        if (this.currentAddress > this.outputLimit) {
            this.currentAddress = this.outputLimit;
        }
        this.bitCount &= 7;
        this.container >>>= bytes * 8;
    }

    public int close() {
        addBitsFast(1, 1);
        flush();
        if (this.currentAddress >= this.outputLimit) {
            return 0;
        }
        return (int) ((this.currentAddress - this.outputAddress) + (this.bitCount <= 0 ? 0 : 1));
    }
}
