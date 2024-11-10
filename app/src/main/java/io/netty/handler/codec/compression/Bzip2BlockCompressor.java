package io.netty.handler.codec.compression;

import io.netty.buffer.ByteBuf;
import io.netty.util.ByteProcessor;

/* loaded from: classes4.dex */
final class Bzip2BlockCompressor {
    private final byte[] block;
    private int blockLength;
    private final int blockLengthLimit;
    private final int[] bwtBlock;
    private int rleLength;
    private final Bzip2BitWriter writer;
    private final ByteProcessor writeProcessor = new ByteProcessor() { // from class: io.netty.handler.codec.compression.Bzip2BlockCompressor.1
        @Override // io.netty.util.ByteProcessor
        public boolean process(byte value) throws Exception {
            return Bzip2BlockCompressor.this.write(value);
        }
    };
    private final Crc32 crc = new Crc32();
    private final boolean[] blockValuesPresent = new boolean[256];
    private int rleCurrentValue = -1;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Bzip2BlockCompressor(Bzip2BitWriter writer, int blockSize) {
        this.writer = writer;
        this.block = new byte[blockSize + 1];
        this.bwtBlock = new int[blockSize + 1];
        this.blockLengthLimit = blockSize - 6;
    }

    private void writeSymbolMap(ByteBuf out) {
        Bzip2BitWriter writer = this.writer;
        boolean[] blockValuesPresent = this.blockValuesPresent;
        boolean[] condensedInUse = new boolean[16];
        for (int i = 0; i < condensedInUse.length; i++) {
            int j = 0;
            int k = i << 4;
            while (true) {
                if (j >= 16) {
                    break;
                }
                if (!blockValuesPresent[k]) {
                    j++;
                    k++;
                } else {
                    condensedInUse[i] = true;
                    break;
                }
            }
        }
        for (boolean isCondensedInUse : condensedInUse) {
            writer.writeBoolean(out, isCondensedInUse);
        }
        for (int i2 = 0; i2 < condensedInUse.length; i2++) {
            if (condensedInUse[i2]) {
                int j2 = 0;
                int k2 = i2 << 4;
                while (j2 < 16) {
                    writer.writeBoolean(out, blockValuesPresent[k2]);
                    j2++;
                    k2++;
                }
            }
        }
    }

    private void writeRun(int value, int runLength) {
        int blockLength = this.blockLength;
        byte[] block = this.block;
        this.blockValuesPresent[value] = true;
        this.crc.updateCRC(value, runLength);
        byte byteValue = (byte) value;
        switch (runLength) {
            case 1:
                block[blockLength] = byteValue;
                this.blockLength = blockLength + 1;
                return;
            case 2:
                block[blockLength] = byteValue;
                block[blockLength + 1] = byteValue;
                this.blockLength = blockLength + 2;
                return;
            case 3:
                block[blockLength] = byteValue;
                block[blockLength + 1] = byteValue;
                block[blockLength + 2] = byteValue;
                this.blockLength = blockLength + 3;
                return;
            default:
                int runLength2 = runLength - 4;
                this.blockValuesPresent[runLength2] = true;
                block[blockLength] = byteValue;
                block[blockLength + 1] = byteValue;
                block[blockLength + 2] = byteValue;
                block[blockLength + 3] = byteValue;
                block[blockLength + 4] = (byte) runLength2;
                this.blockLength = blockLength + 5;
                return;
        }
    }

    boolean write(int value) {
        if (this.blockLength > this.blockLengthLimit) {
            return false;
        }
        int rleCurrentValue = this.rleCurrentValue;
        int rleLength = this.rleLength;
        if (rleLength == 0) {
            this.rleCurrentValue = value;
            this.rleLength = 1;
        } else if (rleCurrentValue != value) {
            writeRun(rleCurrentValue & 255, rleLength);
            this.rleCurrentValue = value;
            this.rleLength = 1;
        } else if (rleLength == 254) {
            writeRun(rleCurrentValue & 255, 255);
            this.rleLength = 0;
        } else {
            this.rleLength = rleLength + 1;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int write(ByteBuf buffer, int offset, int length) {
        int index = buffer.forEachByte(offset, length, this.writeProcessor);
        return index == -1 ? length : index - offset;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void close(ByteBuf out) {
        if (this.rleLength > 0) {
            writeRun(this.rleCurrentValue & 255, this.rleLength);
        }
        this.block[this.blockLength] = this.block[0];
        Bzip2DivSufSort divSufSort = new Bzip2DivSufSort(this.block, this.bwtBlock, this.blockLength);
        int bwtStartPointer = divSufSort.bwt();
        Bzip2BitWriter writer = this.writer;
        writer.writeBits(out, 24, 3227993L);
        writer.writeBits(out, 24, 2511705L);
        writer.writeInt(out, this.crc.getCRC());
        writer.writeBoolean(out, false);
        writer.writeBits(out, 24, bwtStartPointer);
        writeSymbolMap(out);
        Bzip2MTFAndRLE2StageEncoder mtfEncoder = new Bzip2MTFAndRLE2StageEncoder(this.bwtBlock, this.blockLength, this.blockValuesPresent);
        mtfEncoder.encode();
        Bzip2HuffmanStageEncoder huffmanEncoder = new Bzip2HuffmanStageEncoder(writer, mtfEncoder.mtfBlock(), mtfEncoder.mtfLength(), mtfEncoder.mtfAlphabetSize(), mtfEncoder.mtfSymbolFrequencies());
        huffmanEncoder.encode(out);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int availableSize() {
        if (this.blockLength == 0) {
            return this.blockLengthLimit + 2;
        }
        return (this.blockLengthLimit - this.blockLength) + 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isFull() {
        return this.blockLength > this.blockLengthLimit;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isEmpty() {
        return this.blockLength == 0 && this.rleLength == 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int crc() {
        return this.crc.getCRC();
    }
}
