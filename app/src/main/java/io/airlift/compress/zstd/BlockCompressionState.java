package io.airlift.compress.zstd;

import java.util.Arrays;

/* loaded from: classes.dex */
class BlockCompressionState {
    private final long baseAddress;
    public final int[] chainTable;
    public final int[] hashTable;
    private int windowBaseOffset;

    public BlockCompressionState(CompressionParameters parameters, long baseAddress) {
        this.baseAddress = baseAddress;
        this.hashTable = new int[1 << parameters.getHashLog()];
        this.chainTable = new int[1 << parameters.getChainLog()];
    }

    public void slideWindow(int slideWindowSize) {
        for (int i = 0; i < this.hashTable.length; i++) {
            int newValue = this.hashTable[i] - slideWindowSize;
            this.hashTable[i] = newValue & (~(newValue >> 31));
        }
        for (int i2 = 0; i2 < this.chainTable.length; i2++) {
            int newValue2 = this.chainTable[i2] - slideWindowSize;
            this.chainTable[i2] = newValue2 & (~(newValue2 >> 31));
        }
    }

    public void reset() {
        Arrays.fill(this.hashTable, 0);
        Arrays.fill(this.chainTable, 0);
    }

    public void enforceMaxDistance(long inputLimit, int maxDistance) {
        int distance = (int) (inputLimit - this.baseAddress);
        int newOffset = distance - maxDistance;
        if (this.windowBaseOffset < newOffset) {
            this.windowBaseOffset = newOffset;
        }
    }

    public long getBaseAddress() {
        return this.baseAddress;
    }

    public int getWindowBaseOffset() {
        return this.windowBaseOffset;
    }
}
