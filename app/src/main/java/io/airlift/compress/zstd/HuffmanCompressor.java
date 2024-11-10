package io.airlift.compress.zstd;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class HuffmanCompressor {
    private HuffmanCompressor() {
    }

    public static int compress4streams(Object outputBase, long outputAddress, int outputSize, Object inputBase, long inputAddress, int inputSize, HuffmanCompressionTable table) {
        long inputLimit = inputAddress + inputSize;
        long outputLimit = outputAddress + outputSize;
        int segmentSize = (inputSize + 3) / 4;
        if (outputSize < 17 || inputSize <= 9) {
            return 0;
        }
        long output = outputAddress + 6;
        int compressedSize = compressSingleStream(outputBase, output, (int) (outputLimit - output), inputBase, inputAddress, segmentSize, table);
        if (compressedSize == 0) {
            return 0;
        }
        UnsafeUtil.UNSAFE.putShort(outputBase, outputAddress, (short) compressedSize);
        long output2 = output + compressedSize;
        long input = inputAddress + segmentSize;
        int compressedSize2 = compressSingleStream(outputBase, output2, (int) (outputLimit - output2), inputBase, input, segmentSize, table);
        if (compressedSize2 == 0) {
            return 0;
        }
        UnsafeUtil.UNSAFE.putShort(outputBase, outputAddress + 2, (short) compressedSize2);
        long output3 = output2 + compressedSize2;
        long input2 = input + segmentSize;
        int compressedSize3 = compressSingleStream(outputBase, output3, (int) (outputLimit - output3), inputBase, input2, segmentSize, table);
        if (compressedSize3 == 0) {
            return 0;
        }
        UnsafeUtil.UNSAFE.putShort(outputBase, outputAddress + 2 + 2, (short) compressedSize3);
        long output4 = output3 + compressedSize3;
        long input3 = input2 + segmentSize;
        int compressedSize4 = compressSingleStream(outputBase, output4, (int) (outputLimit - output4), inputBase, input3, (int) (inputLimit - input3), table);
        if (compressedSize4 == 0) {
            return 0;
        }
        return (int) ((output4 + compressedSize4) - outputAddress);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static int compressSingleStream(Object outputBase, long outputAddress, int outputSize, Object inputBase, long inputAddress, int inputSize, HuffmanCompressionTable table) {
        if (outputSize >= 8) {
            BitOutputStream bitstream = new BitOutputStream(outputBase, outputAddress, outputSize);
            int n = inputSize & (-4);
            switch (inputSize & 3) {
                case 1:
                    table.encodeSymbol(bitstream, UnsafeUtil.UNSAFE.getByte(inputBase, n + inputAddress + 0) & 255);
                    bitstream.flush();
                    break;
                case 2:
                    table.encodeSymbol(bitstream, UnsafeUtil.UNSAFE.getByte(inputBase, n + inputAddress + 1) & 255);
                    table.encodeSymbol(bitstream, UnsafeUtil.UNSAFE.getByte(inputBase, n + inputAddress + 0) & 255);
                    bitstream.flush();
                    break;
                case 3:
                    table.encodeSymbol(bitstream, UnsafeUtil.UNSAFE.getByte(inputBase, n + inputAddress + 2) & 255);
                    table.encodeSymbol(bitstream, UnsafeUtil.UNSAFE.getByte(inputBase, n + inputAddress + 1) & 255);
                    table.encodeSymbol(bitstream, UnsafeUtil.UNSAFE.getByte(inputBase, n + inputAddress + 0) & 255);
                    bitstream.flush();
                    break;
            }
            while (n > 0) {
                table.encodeSymbol(bitstream, UnsafeUtil.UNSAFE.getByte(inputBase, (n + inputAddress) - 1) & 255);
                table.encodeSymbol(bitstream, UnsafeUtil.UNSAFE.getByte(inputBase, (n + inputAddress) - 2) & 255);
                table.encodeSymbol(bitstream, UnsafeUtil.UNSAFE.getByte(inputBase, (n + inputAddress) - 3) & 255);
                table.encodeSymbol(bitstream, UnsafeUtil.UNSAFE.getByte(inputBase, (n + inputAddress) - 4) & 255);
                bitstream.flush();
                n -= 4;
            }
            return bitstream.close();
        }
        return 0;
    }
}
