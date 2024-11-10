package io.netty.buffer.search;

import io.netty.util.internal.PlatformDependent;

/* loaded from: classes4.dex */
public class KmpSearchProcessorFactory extends AbstractSearchProcessorFactory {
    private final int[] jumpTable;
    private final byte[] needle;

    /* loaded from: classes4.dex */
    public static class Processor implements SearchProcessor {
        private long currentPosition;
        private final int[] jumpTable;
        private final byte[] needle;

        Processor(byte[] needle, int[] jumpTable) {
            this.needle = needle;
            this.jumpTable = jumpTable;
        }

        @Override // io.netty.util.ByteProcessor
        public boolean process(byte value) {
            while (this.currentPosition > 0 && PlatformDependent.getByte(this.needle, this.currentPosition) != value) {
                this.currentPosition = PlatformDependent.getInt(this.jumpTable, this.currentPosition);
            }
            if (PlatformDependent.getByte(this.needle, this.currentPosition) == value) {
                this.currentPosition++;
            }
            if (this.currentPosition == this.needle.length) {
                this.currentPosition = PlatformDependent.getInt(this.jumpTable, this.currentPosition);
                return false;
            }
            return true;
        }

        @Override // io.netty.buffer.search.SearchProcessor
        public void reset() {
            this.currentPosition = 0L;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public KmpSearchProcessorFactory(byte[] needle) {
        this.needle = (byte[]) needle.clone();
        this.jumpTable = new int[needle.length + 1];
        int j = 0;
        for (int i = 1; i < needle.length; i++) {
            while (j > 0 && needle[j] != needle[i]) {
                j = this.jumpTable[j];
            }
            if (needle[j] == needle[i]) {
                j++;
            }
            this.jumpTable[i + 1] = j;
        }
    }

    @Override // io.netty.buffer.search.SearchProcessorFactory
    public Processor newSearchProcessor() {
        return new Processor(this.needle, this.jumpTable);
    }
}
