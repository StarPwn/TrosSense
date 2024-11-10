package io.airlift.compress.zstd;

import androidx.core.view.InputDeviceCompat;
import io.airlift.compress.zstd.CompressionParameters;
import sun.misc.Unsafe;

/* loaded from: classes.dex */
class ZstdFrameCompressor {
    private static final int CHECKSUM_FLAG = 4;
    static final int MAX_FRAME_HEADER_SIZE = 14;
    private static final int MAX_HUFFMAN_TABLE_LOG = 11;
    private static final int MINIMUM_LITERALS_SIZE = 63;
    private static final int SINGLE_SEGMENT_FLAG = 32;

    private ZstdFrameCompressor() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int writeMagic(final Object outputBase, final long outputAddress, final long outputLimit) {
        Util.checkArgument(outputLimit - outputAddress >= 4, "Output buffer too small");
        UnsafeUtil.UNSAFE.putInt(outputBase, outputAddress, Constants.MAGIC_NUMBER);
        return 4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int writeFrameHeader(final Object outputBase, final long outputAddress, final long outputLimit, int inputSize, int windowSize) {
        boolean singleSegment = false;
        Util.checkArgument(outputLimit - outputAddress >= 14, "Output buffer too small");
        int contentSizeDescriptor = 0;
        if (inputSize != -1) {
            contentSizeDescriptor = (inputSize >= 256 ? 1 : 0) + (inputSize >= 65792 ? 1 : 0);
        }
        int frameHeaderDescriptor = (contentSizeDescriptor << 6) | 4;
        if (inputSize != -1 && windowSize >= inputSize) {
            singleSegment = true;
        }
        if (singleSegment) {
            frameHeaderDescriptor |= 32;
        }
        UnsafeUtil.UNSAFE.putByte(outputBase, outputAddress, (byte) frameHeaderDescriptor);
        long output = outputAddress + 1;
        if (!singleSegment) {
            int base = Integer.highestOneBit(windowSize);
            int exponent = (32 - Integer.numberOfLeadingZeros(base)) - 1;
            if (exponent < 10) {
                throw new IllegalArgumentException("Minimum window size is 1024");
            }
            int remainder = windowSize - base;
            if (remainder % (base / 8) != 0) {
                throw new IllegalArgumentException("Window size of magnitude 2^" + exponent + " must be multiple of " + (base / 8));
            }
            int mantissa = remainder / (base / 8);
            int encoded = ((exponent - 10) << 3) | mantissa;
            UnsafeUtil.UNSAFE.putByte(outputBase, output, (byte) encoded);
            output++;
        }
        switch (contentSizeDescriptor) {
            case 0:
                if (singleSegment) {
                    UnsafeUtil.UNSAFE.putByte(outputBase, output, (byte) inputSize);
                    output = 1 + output;
                    break;
                }
                break;
            case 1:
                UnsafeUtil.UNSAFE.putShort(outputBase, output, (short) (inputSize + InputDeviceCompat.SOURCE_ANY));
                output += 2;
                break;
            case 2:
                UnsafeUtil.UNSAFE.putInt(outputBase, output, inputSize);
                output += 4;
                break;
            default:
                throw new AssertionError();
        }
        return (int) (output - outputAddress);
    }

    static int writeChecksum(Object outputBase, long outputAddress, long outputLimit, Object inputBase, long inputAddress, long inputLimit) {
        Util.checkArgument(outputLimit - outputAddress >= 4, "Output buffer too small");
        int inputSize = (int) (inputLimit - inputAddress);
        long hash = XxHash64.hash(0L, inputBase, inputAddress, inputSize);
        UnsafeUtil.UNSAFE.putInt(outputBase, outputAddress, (int) hash);
        return 4;
    }

    public static int compress(Object inputBase, long inputAddress, long inputLimit, Object outputBase, long outputAddress, long outputLimit, int compressionLevel) {
        int inputSize = (int) (inputLimit - inputAddress);
        CompressionParameters parameters = CompressionParameters.compute(compressionLevel, inputSize);
        long output = outputAddress + writeMagic(outputBase, outputAddress, outputLimit);
        return (int) ((((output + writeFrameHeader(outputBase, output, outputLimit, inputSize, parameters.getWindowSize())) + compressFrame(inputBase, inputAddress, inputLimit, outputBase, r15, outputLimit, parameters)) + writeChecksum(outputBase, r15, outputLimit, inputBase, inputAddress, inputLimit)) - outputAddress);
    }

    private static int compressFrame(Object inputBase, long inputAddress, long inputLimit, Object outputBase, long outputAddress, long outputLimit, CompressionParameters parameters) {
        int blockSize = parameters.getBlockSize();
        int outputSize = (int) (outputLimit - outputAddress);
        int remaining = (int) (inputLimit - inputAddress);
        long output = outputAddress;
        long input = inputAddress;
        CompressionContext context = new CompressionContext(parameters, inputAddress, remaining);
        do {
            Util.checkArgument(outputSize >= 6, "Output buffer too small");
            boolean lastBlock = blockSize >= remaining;
            blockSize = Math.min(blockSize, remaining);
            int compressedSize = writeCompressedBlock(inputBase, input, blockSize, outputBase, output, outputSize, context, lastBlock);
            input += blockSize;
            remaining -= blockSize;
            output += compressedSize;
            outputSize -= compressedSize;
        } while (remaining > 0);
        return (int) (output - outputAddress);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:10:0x003b  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0060  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0027  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int writeCompressedBlock(java.lang.Object r19, long r20, int r22, java.lang.Object r23, long r24, int r26, io.airlift.compress.zstd.CompressionContext r27, boolean r28) {
        /*
            r9 = r22
            r10 = r23
            r11 = r24
            r13 = r26
            r14 = 1
            r15 = 0
            if (r28 != 0) goto L19
            r8 = r27
            io.airlift.compress.zstd.CompressionParameters r0 = r8.parameters
            int r0 = r0.getBlockSize()
            if (r9 != r0) goto L17
            goto L1b
        L17:
            r0 = r15
            goto L1c
        L19:
            r8 = r27
        L1b:
            r0 = r14
        L1c:
            java.lang.String r1 = "Only last block can be smaller than block size"
            io.airlift.compress.zstd.Util.checkArgument(r0, r1)
            r16 = 0
            r17 = 3
            if (r9 <= 0) goto L39
            long r5 = r11 + r17
            int r7 = r13 + (-3)
            r0 = r19
            r1 = r20
            r3 = r22
            r4 = r23
            r8 = r27
            int r16 = compressBlock(r0, r1, r3, r4, r5, r7, r8)
        L39:
            if (r16 != 0) goto L60
            int r0 = r9 + 3
            if (r0 > r13) goto L40
            goto L41
        L40:
            r14 = r15
        L41:
            java.lang.String r0 = "Output size too small"
            io.airlift.compress.zstd.Util.checkArgument(r14, r0)
            r0 = r28 | 0
            int r1 = r9 << 3
            r14 = r0 | r1
            io.airlift.compress.zstd.Util.put24BitLittleEndian(r10, r11, r14)
            sun.misc.Unsafe r0 = io.airlift.compress.zstd.UnsafeUtil.UNSAFE
            long r5 = r11 + r17
            long r7 = (long) r9
            r1 = r19
            r2 = r20
            r4 = r23
            r0.copyMemory(r1, r2, r4, r5, r7)
            int r0 = r9 + 3
            goto L6b
        L60:
            r0 = r28 | 4
            int r1 = r16 << 3
            r0 = r0 | r1
            io.airlift.compress.zstd.Util.put24BitLittleEndian(r10, r11, r0)
            int r1 = r16 + 3
            r0 = r1
        L6b:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.airlift.compress.zstd.ZstdFrameCompressor.writeCompressedBlock(java.lang.Object, long, int, java.lang.Object, long, int, io.airlift.compress.zstd.CompressionContext, boolean):int");
    }

    private static int compressBlock(Object inputBase, long inputAddress, int inputSize, Object outputBase, long outputAddress, int outputSize, CompressionContext context) {
        if (inputSize < 7) {
            return 0;
        }
        CompressionParameters parameters = context.parameters;
        context.blockCompressionState.enforceMaxDistance(inputAddress + inputSize, parameters.getWindowSize());
        context.sequenceStore.reset();
        int lastLiteralsSize = parameters.getStrategy().getCompressor().compressBlock(inputBase, inputAddress, inputSize, context.sequenceStore, context.blockCompressionState, context.offsets, parameters);
        long lastLiteralsAddress = (inputAddress + inputSize) - lastLiteralsSize;
        context.sequenceStore.appendLiterals(inputBase, lastLiteralsAddress, lastLiteralsSize);
        context.sequenceStore.generateCodes();
        long outputLimit = outputAddress + outputSize;
        int compressedLiteralsSize = encodeLiterals(context.huffmanContext, parameters, outputBase, outputAddress, (int) (outputLimit - outputAddress), context.sequenceStore.literalsBuffer, context.sequenceStore.literalsLength);
        long output = outputAddress + compressedLiteralsSize;
        int compressedSequencesSize = SequenceEncoder.compressSequences(outputBase, output, (int) (outputLimit - output), context.sequenceStore, parameters.getStrategy(), context.sequenceEncodingContext);
        int compressedSize = compressedLiteralsSize + compressedSequencesSize;
        if (compressedSize != 0) {
            int maxCompressedSize = inputSize - calculateMinimumGain(inputSize, parameters.getStrategy());
            if (compressedSize > maxCompressedSize) {
                return 0;
            }
            context.commit();
            return compressedSize;
        }
        return compressedSize;
    }

    private static int encodeLiterals(HuffmanCompressionContext context, CompressionParameters parameters, Object outputBase, long outputAddress, int outputSize, byte[] literals, int literalsSize) {
        HuffmanCompressionTable newTable;
        boolean reuseTable;
        int serializedTableSize;
        int maxSymbol;
        int maxSymbol2;
        int largestCount;
        int compressedSize;
        boolean bypassCompression = parameters.getStrategy() == CompressionParameters.Strategy.FAST && parameters.getTargetLength() > 0;
        if (bypassCompression || literalsSize <= 63) {
            return rawLiterals(outputBase, outputAddress, outputSize, literals, Unsafe.ARRAY_BYTE_BASE_OFFSET, literalsSize);
        }
        int headerSize = (literalsSize >= 1024 ? 1 : 0) + 3 + (literalsSize >= 16384 ? 1 : 0);
        Util.checkArgument(headerSize + 1 <= outputSize, "Output buffer too small");
        int[] counts = new int[256];
        Histogram.count(literals, literalsSize, counts);
        int maxSymbol3 = Histogram.findMaxSymbol(counts, 255);
        int largestCount2 = Histogram.findLargestCount(counts, maxSymbol3);
        long literalsAddress = Unsafe.ARRAY_BYTE_BASE_OFFSET;
        if (largestCount2 == literalsSize) {
            return rleLiterals(outputBase, outputAddress, outputSize, literals, Unsafe.ARRAY_BYTE_BASE_OFFSET, literalsSize);
        }
        if (largestCount2 <= (literalsSize >>> 7) + 4) {
            return rawLiterals(outputBase, outputAddress, outputSize, literals, Unsafe.ARRAY_BYTE_BASE_OFFSET, literalsSize);
        }
        HuffmanCompressionTable previousTable = context.getPreviousTable();
        boolean canReuse = previousTable.isValid(counts, maxSymbol3);
        boolean preferReuse = parameters.getStrategy().ordinal() < CompressionParameters.Strategy.LAZY.ordinal() && literalsSize <= 1024;
        if (preferReuse && canReuse) {
            newTable = previousTable;
            reuseTable = true;
            serializedTableSize = 0;
        } else {
            HuffmanCompressionTable newTable2 = context.borrowTemporaryTable();
            newTable2.initialize(counts, maxSymbol3, HuffmanCompressionTable.optimalNumberOfBits(11, literalsSize, maxSymbol3), context.getCompressionTableWorkspace());
            newTable = newTable2;
            int serializedTableSize2 = newTable2.write(outputBase, outputAddress + headerSize, outputSize - headerSize, context.getTableWriterWorkspace());
            if (canReuse && previousTable.estimateCompressedSize(counts, maxSymbol3) <= newTable.estimateCompressedSize(counts, maxSymbol3) + serializedTableSize2) {
                context.discardTemporaryTable();
                newTable = previousTable;
                reuseTable = true;
                serializedTableSize = 0;
            } else {
                reuseTable = false;
                serializedTableSize = serializedTableSize2;
            }
        }
        boolean singleStream = literalsSize < 256;
        if (singleStream) {
            maxSymbol = maxSymbol3;
            maxSymbol2 = serializedTableSize;
            largestCount = headerSize;
            compressedSize = HuffmanCompressor.compressSingleStream(outputBase, headerSize + outputAddress + serializedTableSize, (outputSize - headerSize) - serializedTableSize, literals, literalsAddress, literalsSize, newTable);
        } else {
            maxSymbol = maxSymbol3;
            maxSymbol2 = serializedTableSize;
            largestCount = headerSize;
            compressedSize = HuffmanCompressor.compress4streams(outputBase, largestCount + outputAddress + maxSymbol2, (outputSize - largestCount) - maxSymbol2, literals, literalsAddress, literalsSize, newTable);
        }
        int totalSize = maxSymbol2 + compressedSize;
        int minimumGain = calculateMinimumGain(literalsSize, parameters.getStrategy());
        if (compressedSize != 0 && totalSize < literalsSize - minimumGain) {
            int encodingType = reuseTable ? 3 : 2;
            switch (largestCount) {
                case 3:
                    int header = ((singleStream ? 0 : 1) << 2) | encodingType | (literalsSize << 4) | (totalSize << 14);
                    Util.put24BitLittleEndian(outputBase, outputAddress, header);
                    break;
                case 4:
                    int header2 = encodingType | 8 | (literalsSize << 4) | (totalSize << 18);
                    UnsafeUtil.UNSAFE.putInt(outputBase, outputAddress, header2);
                    break;
                case 5:
                    int header3 = encodingType | 12 | (literalsSize << 4) | (totalSize << 22);
                    UnsafeUtil.UNSAFE.putInt(outputBase, outputAddress, header3);
                    UnsafeUtil.UNSAFE.putByte(outputBase, 4 + outputAddress, (byte) (totalSize >>> 10));
                    break;
                default:
                    throw new IllegalStateException();
            }
            int header4 = largestCount + totalSize;
            return header4;
        }
        context.discardTemporaryTable();
        return rawLiterals(outputBase, outputAddress, outputSize, literals, Unsafe.ARRAY_BYTE_BASE_OFFSET, literalsSize);
    }

    private static int rleLiterals(Object outputBase, long outputAddress, int outputSize, Object inputBase, long inputAddress, int inputSize) {
        int headerSize = (inputSize > 31 ? 1 : 0) + 1 + (inputSize > 4095 ? 1 : 0);
        switch (headerSize) {
            case 1:
                UnsafeUtil.UNSAFE.putByte(outputBase, outputAddress, (byte) (1 | (inputSize << 3)));
                break;
            case 2:
                UnsafeUtil.UNSAFE.putShort(outputBase, outputAddress, (short) ((inputSize << 4) | 5));
                break;
            case 3:
                UnsafeUtil.UNSAFE.putInt(outputBase, outputAddress, (inputSize << 4) | 13);
                break;
            default:
                throw new IllegalStateException();
        }
        UnsafeUtil.UNSAFE.putByte(outputBase, headerSize + outputAddress, UnsafeUtil.UNSAFE.getByte(inputBase, inputAddress));
        return headerSize + 1;
    }

    private static int calculateMinimumGain(int inputSize, CompressionParameters.Strategy strategy) {
        int minLog = strategy == CompressionParameters.Strategy.BTULTRA ? 7 : 6;
        return (inputSize >>> minLog) + 2;
    }

    private static int rawLiterals(Object outputBase, long outputAddress, int outputSize, Object inputBase, long inputAddress, int inputSize) {
        int headerSize;
        int headerSize2 = inputSize >= 32 ? 1 + 1 : 1;
        if (inputSize < 4096) {
            headerSize = headerSize2;
        } else {
            headerSize = headerSize2 + 1;
        }
        Util.checkArgument(inputSize + headerSize <= outputSize, "Output buffer too small");
        switch (headerSize) {
            case 1:
                UnsafeUtil.UNSAFE.putByte(outputBase, outputAddress, (byte) ((inputSize << 3) | 0));
                break;
            case 2:
                UnsafeUtil.UNSAFE.putShort(outputBase, outputAddress, (short) ((inputSize << 4) | 4));
                break;
            case 3:
                Util.put24BitLittleEndian(outputBase, outputAddress, (inputSize << 4) | 12);
                break;
            default:
                throw new AssertionError();
        }
        Util.checkArgument(inputSize + 1 <= outputSize, "Output buffer too small");
        UnsafeUtil.UNSAFE.copyMemory(inputBase, inputAddress, outputBase, outputAddress + headerSize, inputSize);
        return headerSize + inputSize;
    }
}
