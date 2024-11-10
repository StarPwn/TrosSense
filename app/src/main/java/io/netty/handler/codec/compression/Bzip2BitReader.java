package io.netty.handler.codec.compression;

import io.netty.buffer.ByteBuf;

/* loaded from: classes4.dex */
class Bzip2BitReader {
    private static final int MAX_COUNT_OF_READABLE_BYTES = 268435455;
    private long bitBuffer;
    private int bitCount;
    private ByteBuf in;

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setByteBuf(ByteBuf in) {
        this.in = in;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int readBits(int count) {
        long readData;
        int offset;
        if (count < 0 || count > 32) {
            throw new IllegalArgumentException("count: " + count + " (expected: 0-32 )");
        }
        int bitCount = this.bitCount;
        long bitBuffer = this.bitBuffer;
        if (bitCount < count) {
            switch (this.in.readableBytes()) {
                case 1:
                    readData = this.in.readUnsignedByte();
                    offset = 8;
                    break;
                case 2:
                    readData = this.in.readUnsignedShort();
                    offset = 16;
                    break;
                case 3:
                    readData = this.in.readUnsignedMedium();
                    offset = 24;
                    break;
                default:
                    readData = this.in.readUnsignedInt();
                    offset = 32;
                    break;
            }
            bitBuffer = (bitBuffer << offset) | readData;
            bitCount += offset;
            this.bitBuffer = bitBuffer;
        }
        int bitCount2 = bitCount - count;
        this.bitCount = bitCount2;
        return (int) ((bitBuffer >>> bitCount2) & (count != 32 ? (1 << count) - 1 : 4294967295L));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean readBoolean() {
        return readBits(1) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int readInt() {
        return readBits(32);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void refill() {
        int readData = this.in.readUnsignedByte();
        this.bitBuffer = (this.bitBuffer << 8) | readData;
        this.bitCount += 8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isReadable() {
        return this.bitCount > 0 || this.in.isReadable();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasReadableBits(int count) {
        if (count >= 0) {
            return this.bitCount >= count || ((this.in.readableBytes() << 3) & Integer.MAX_VALUE) >= count - this.bitCount;
        }
        throw new IllegalArgumentException("count: " + count + " (expected value greater than 0)");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasReadableBytes(int count) {
        if (count < 0 || count > MAX_COUNT_OF_READABLE_BYTES) {
            throw new IllegalArgumentException("count: " + count + " (expected: 0-" + MAX_COUNT_OF_READABLE_BYTES + ')');
        }
        return hasReadableBits(count << 3);
    }
}
