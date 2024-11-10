package io.airlift.compress.zstd;

import androidx.core.view.PointerIconCompat;
import androidx.fragment.app.FragmentTransaction;
import com.google.common.base.Ascii;
import com.trossense.bl;
import io.airlift.compress.MalformedInputException;
import io.airlift.compress.zstd.BitInputStream;
import io.airlift.compress.zstd.FiniteStateEntropy;
import io.netty.handler.codec.http.HttpConstants;
import io.netty.handler.codec.memcache.binary.BinaryMemcacheOpcodes;
import java.util.Arrays;
import kotlin.UShort;
import okio.Utf8;
import sun.misc.Unsafe;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ZstdFrameDecompressor {
    static final int MAX_WINDOW_SIZE = 8388608;
    private static final int V07_MAGIC_NUMBER = -47205081;
    private FiniteStateEntropy.Table currentLiteralsLengthTable;
    private FiniteStateEntropy.Table currentMatchLengthTable;
    private FiniteStateEntropy.Table currentOffsetCodesTable;
    private long literalsAddress;
    private Object literalsBase;
    private long literalsLimit;
    private static final int[] DEC_32_TABLE = {4, 1, 2, 1, 4, 4, 4, 4};
    private static final int[] DEC_64_TABLE = {0, 0, 0, -1, 0, 1, 2, 3};
    private static final int[] LITERALS_LENGTH_BASE = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 18, 20, 22, 24, 28, 32, 40, 48, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536};
    private static final int[] MATCH_LENGTH_BASE = {3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 37, 39, 41, 43, 47, 51, 59, 67, 83, 99, bl.bs, bl.dv, 515, 1027, 2051, FragmentTransaction.TRANSIT_FRAGMENT_FADE, 8195, 16387, 32771, 65539};
    private static final int[] OFFSET_CODES_BASE = {0, 1, 1, 5, 13, 29, 61, bl.bm, bl.dp, 509, PointerIconCompat.TYPE_GRABBING, 2045, 4093, 8189, 16381, 32765, Utf8.REPLACEMENT_CODE_POINT, 131069, 262141, 524285, 1048573, 2097149, 4194301, 8388605, 16777213, 33554429, 67108861, 134217725, 268435453};
    private static final FiniteStateEntropy.Table DEFAULT_LITERALS_LENGTH_TABLE = new FiniteStateEntropy.Table(6, new int[]{0, 16, 32, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 32, 0, 0, 0, 0, 32, 0, 0, 32, 0, 32, 0, 32, 0, 0, 32, 0, 32, 0, 32, 0, 0, 16, 32, 0, 0, 48, 16, 32, 32, 32, 32, 32, 32, 32, 32, 0, 32, 32, 32, 32, 32, 32, 0, 0, 0, 0}, new byte[]{0, 0, 1, 3, 4, 6, 7, 9, 10, 12, 14, 16, 18, 19, 21, 22, 24, 25, 26, Ascii.ESC, 29, Ascii.US, 0, 1, 2, 4, 5, 7, 8, 10, 11, 13, 16, 17, 19, 20, 22, 23, 25, 25, 26, 28, 30, 0, 1, 2, 3, 5, 6, 8, 9, 11, 12, 15, 17, 18, 20, 21, 23, 24, BinaryMemcacheOpcodes.GATK, 34, BinaryMemcacheOpcodes.SASL_AUTH, 32}, new byte[]{4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 6, 5, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 4, 4, 5, 5, 5, 5, 5, 5, 5, 6, 5, 5, 5, 5, 5, 5, 4, 4, 5, 6, 6, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 6, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6});
    private static final FiniteStateEntropy.Table DEFAULT_OFFSET_CODES_TABLE = new FiniteStateEntropy.Table(5, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 16, 0, 0, 0, 0, 16, 0, 0, 0, 16, 0, 0, 0, 0, 0, 0, 0}, new byte[]{0, 6, 9, 15, 21, 3, 7, 12, 18, 23, 5, 8, 14, 20, 2, 7, 11, 17, 22, 4, 8, 13, 19, 1, 6, 10, 16, 28, Ascii.ESC, 26, 25, 24}, new byte[]{5, 4, 5, 5, 5, 5, 4, 5, 5, 5, 5, 4, 5, 5, 5, 4, 5, 5, 5, 5, 4, 5, 5, 5, 4, 5, 5, 5, 5, 5, 5, 5});
    private static final FiniteStateEntropy.Table DEFAULT_MATCH_LENGTH_TABLE = new FiniteStateEntropy.Table(6, new int[]{0, 0, 32, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 16, 0, 32, 0, 32, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 32, 48, 16, 32, 32, 32, 32, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new byte[]{0, 1, 2, 3, 5, 6, 8, 10, 13, 16, 19, 22, 25, 28, Ascii.US, BinaryMemcacheOpcodes.SASL_AUTH, BinaryMemcacheOpcodes.GATK, 37, 39, 41, 43, 45, 1, 2, 3, 4, 6, 7, 9, 12, 15, 18, 21, 24, Ascii.ESC, 30, 32, 34, BinaryMemcacheOpcodes.GATKQ, 38, 40, 42, HttpConstants.COMMA, 1, 1, 2, 4, 5, 7, 8, 11, 14, 17, 20, 23, 26, 29, 52, 51, 50, 49, 48, 47, 46}, new byte[]{6, 4, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 4, 4, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6});
    private final byte[] literals = new byte[131080];
    private final int[] previousOffsets = new int[3];
    private final FiniteStateEntropy.Table literalsLengthTable = new FiniteStateEntropy.Table(9);
    private final FiniteStateEntropy.Table offsetCodesTable = new FiniteStateEntropy.Table(8);
    private final FiniteStateEntropy.Table matchLengthTable = new FiniteStateEntropy.Table(9);
    private final Huffman huffman = new Huffman();
    private final FseTableReader fse = new FseTableReader();

    public int decompress(final Object inputBase, final long inputAddress, final long inputLimit, final Object outputBase, final long outputAddress, final long outputLimit) {
        FrameHeader frameHeader;
        int decodedSize;
        long input;
        Object obj;
        Object obj2 = inputBase;
        long j = inputLimit;
        if (outputAddress == outputLimit) {
            return 0;
        }
        long input2 = inputAddress;
        long output = outputAddress;
        while (input2 < j) {
            reset();
            long outputStart = output;
            long input3 = input2 + verifyMagic(obj2, input2, j);
            FrameHeader frameHeader2 = readFrameHeader(obj2, input3, j);
            long input4 = input3 + frameHeader2.headerSize;
            long output2 = output;
            while (true) {
                Util.verify(input4 + 3 <= j, input4, "Not enough input bytes");
                int header = Util.get24BitLittleEndian(obj2, input4);
                long input5 = input4 + 3;
                boolean lastBlock = (header & 1) != 0;
                int blockType = (header >>> 1) & 3;
                int blockSize = (header >>> 3) & 2097151;
                switch (blockType) {
                    case 0:
                        frameHeader = frameHeader2;
                        Util.verify(inputAddress + ((long) blockSize) <= inputLimit, input5, "Not enough input bytes");
                        decodedSize = decodeRawBlock(inputBase, input5, blockSize, outputBase, output2, outputLimit);
                        input = input5 + blockSize;
                        break;
                    case 1:
                        frameHeader = frameHeader2;
                        Util.verify(inputAddress + 1 <= inputLimit, input5, "Not enough input bytes");
                        decodedSize = decodeRleBlock(blockSize, inputBase, input5, outputBase, output2, outputLimit);
                        input = input5 + 1;
                        break;
                    case 2:
                        Util.verify(inputAddress + ((long) blockSize) <= j, input5, "Not enough input bytes");
                        frameHeader = frameHeader2;
                        decodedSize = decodeCompressedBlock(inputBase, input5, blockSize, outputBase, output2, outputLimit, frameHeader2.windowSize, outputAddress);
                        input = input5 + blockSize;
                        break;
                    default:
                        throw Util.fail(input5, "Invalid block type");
                }
                output2 += decodedSize;
                if (!lastBlock) {
                    j = inputLimit;
                    frameHeader2 = frameHeader;
                    input4 = input;
                    obj2 = inputBase;
                } else {
                    if (!frameHeader.hasChecksum) {
                        obj = inputBase;
                    } else {
                        int decodedFrameSize = (int) (output2 - outputStart);
                        long hash = XxHash64.hash(0L, outputBase, outputStart, decodedFrameSize);
                        obj = inputBase;
                        int checksum = UnsafeUtil.UNSAFE.getInt(obj, input);
                        if (checksum != ((int) hash)) {
                            throw new MalformedInputException(input, String.format("Bad checksum. Expected: %s, actual: %s", Integer.toHexString(checksum), Integer.toHexString((int) hash)));
                        }
                        input += 4;
                    }
                    input2 = input;
                    j = inputLimit;
                    obj2 = obj;
                    output = output2;
                }
            }
        }
        return (int) (output - outputAddress);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reset() {
        this.previousOffsets[0] = 1;
        this.previousOffsets[1] = 4;
        this.previousOffsets[2] = 8;
        this.currentLiteralsLengthTable = null;
        this.currentOffsetCodesTable = null;
        this.currentMatchLengthTable = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int decodeRawBlock(Object inputBase, long inputAddress, int blockSize, Object outputBase, long outputAddress, long outputLimit) {
        Util.verify(outputAddress + ((long) blockSize) <= outputLimit, inputAddress, "Output buffer too small");
        UnsafeUtil.UNSAFE.copyMemory(inputBase, inputAddress, outputBase, outputAddress, blockSize);
        return blockSize;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int decodeRleBlock(int size, Object inputBase, long inputAddress, Object outputBase, long outputAddress, long outputLimit) {
        Util.verify(outputAddress + ((long) size) <= outputLimit, inputAddress, "Output buffer too small");
        long output = outputAddress;
        long value = UnsafeUtil.UNSAFE.getByte(inputBase, inputAddress) & 255;
        int remaining = size;
        if (remaining >= 8) {
            long packed = (value << 8) | value | (value << 16) | (value << 24) | (value << 32) | (value << 40) | (value << 48) | (value << 56);
            do {
                UnsafeUtil.UNSAFE.putLong(outputBase, output, packed);
                output += 8;
                remaining -= 8;
            } while (remaining >= 8);
        }
        for (int i = 0; i < remaining; i++) {
            UnsafeUtil.UNSAFE.putByte(outputBase, output, (byte) value);
            output++;
        }
        return size;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Failed to find 'out' block for switch in B:8:0x002c. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:16:0x006f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int decodeCompressedBlock(java.lang.Object r24, final long r25, int r27, java.lang.Object r28, long r29, long r31, int r33, long r34) {
        /*
            r23 = this;
            r9 = r23
            r10 = r24
            r7 = r27
            long r0 = (long) r7
            long r18 = r25 + r0
            r11 = r25
            r0 = 131072(0x20000, float:1.83671E-40)
            r6 = 1
            r8 = 0
            if (r7 > r0) goto L13
            r0 = r6
            goto L14
        L13:
            r0 = r8
        L14:
            java.lang.String r1 = "Expected match length table to be present"
            io.airlift.compress.zstd.Util.verify(r0, r11, r1)
            r0 = 3
            if (r7 < r0) goto L1e
            r1 = r6
            goto L1f
        L1e:
            r1 = r8
        L1f:
            java.lang.String r2 = "Compressed block size too small"
            io.airlift.compress.zstd.Util.verify(r1, r11, r2)
            sun.misc.Unsafe r1 = io.airlift.compress.zstd.UnsafeUtil.UNSAFE
            byte r1 = r1.getByte(r10, r11)
            r20 = r1 & 3
            switch(r20) {
                case 0: goto L5a;
                case 1: goto L52;
                case 2: goto L41;
                case 3: goto L36;
                default: goto L2f;
            }
        L2f:
            java.lang.String r0 = "Invalid literals block encoding type"
            io.airlift.compress.MalformedInputException r0 = io.airlift.compress.zstd.Util.fail(r11, r0)
            throw r0
        L36:
            io.airlift.compress.zstd.Huffman r0 = r9.huffman
            boolean r0 = r0.isLoaded()
            java.lang.String r1 = "Dictionary is corrupted"
            io.airlift.compress.zstd.Util.verify(r0, r11, r1)
        L41:
            r0 = r23
            r1 = r24
            r2 = r11
            r4 = r27
            r5 = r20
            int r0 = r0.decodeCompressedLiterals(r1, r2, r4, r5)
            long r0 = (long) r0
            long r11 = r11 + r0
            r2 = r11
            goto L68
        L52:
            int r0 = r9.decodeRleLiterals(r10, r11, r7)
            long r0 = (long) r0
            long r11 = r11 + r0
            r2 = r11
            goto L68
        L5a:
            r0 = r23
            r1 = r24
            r2 = r11
            r4 = r18
            int r0 = r0.decodeRawLiterals(r1, r2, r4)
            long r0 = (long) r0
            long r11 = r11 + r0
            r2 = r11
        L68:
            r0 = 8388608(0x800000, float:1.1754944E-38)
            r1 = r33
            if (r1 > r0) goto L6f
            goto L70
        L6f:
            r6 = r8
        L70:
            java.lang.String r0 = "Window size too large (not yet supported)"
            io.airlift.compress.zstd.Util.verify(r6, r2, r0)
            long r4 = (long) r7
            long r4 = r25 + r4
            java.lang.Object r11 = r9.literalsBase
            long r12 = r9.literalsAddress
            long r14 = r9.literalsLimit
            r0 = r23
            r1 = r24
            r21 = r2
            r6 = r28
            r7 = r29
            r9 = r31
            r16 = r34
            int r0 = r0.decompressSequences(r1, r2, r4, r6, r7, r9, r11, r12, r14, r16)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.airlift.compress.zstd.ZstdFrameDecompressor.decodeCompressedBlock(java.lang.Object, long, int, java.lang.Object, long, long, int, long):int");
    }

    private int decompressSequences(final Object inputBase, final long inputAddress, final long inputLimit, final Object outputBase, final long outputAddress, final long outputLimit, final Object literalsBase, final long literalsAddress, final long literalsLimit, long outputAbsoluteBaseAddress) {
        int sequenceCount;
        byte[] matchLengthSymbols;
        int[] literalsLengthNewStates;
        FiniteStateEntropy.Table currentOffsetCodesTable;
        int offsetBits;
        int offsetBits2;
        int matchLength;
        int matchLengthCode;
        int offsetCode;
        int literalsLength;
        int literalsLength2;
        long input;
        int matchLengthCode2;
        long currentAddress;
        int matchLengthState;
        long bits;
        int literalsLengthState;
        byte[] matchLengthNumbersOfBits;
        int offsetCodesState;
        int size;
        int[] matchLengthNewStates;
        byte[] literalsLengthSymbols;
        int[] literalsLengthNewStates2;
        byte[] matchLengthSymbols2;
        FiniteStateEntropy.Table currentOffsetCodesTable2;
        int temp;
        long fastOutputLimit = outputLimit - 8;
        long fastMatchOutputLimit = fastOutputLimit - 8;
        long output = outputAddress;
        long literalsInput = literalsAddress;
        int size2 = (int) (inputLimit - inputAddress);
        Util.verify(size2 >= 1, inputAddress, "Not enough input bytes");
        long input2 = inputAddress + 1;
        int sequenceCount2 = UnsafeUtil.UNSAFE.getByte(inputBase, inputAddress) & 255;
        if (sequenceCount2 != 0) {
            if (sequenceCount2 == 255) {
                Util.verify(input2 + 2 <= inputLimit, input2, "Not enough input bytes");
                int sequenceCount3 = (UnsafeUtil.UNSAFE.getShort(inputBase, input2) & UShort.MAX_VALUE) + Constants.LONG_NUMBER_OF_SEQUENCES;
                input2 += 2;
                sequenceCount = sequenceCount3;
            } else if (sequenceCount2 > 127) {
                Util.verify(input2 < inputLimit, input2, "Not enough input bytes");
                sequenceCount = ((sequenceCount2 - 128) << 8) + (255 & UnsafeUtil.UNSAFE.getByte(inputBase, input2));
                input2++;
            } else {
                sequenceCount = sequenceCount2;
            }
            Util.verify(4 + input2 <= inputLimit, input2, "Not enough input bytes");
            byte type = UnsafeUtil.UNSAFE.getByte(inputBase, input2);
            int literalsLengthType = (type & 255) >>> 6;
            int offsetCodesType = (type >>> 4) & 3;
            int matchLengthType = (type >>> 2) & 3;
            long input3 = computeMatchLengthTable(matchLengthType, inputBase, computeOffsetsTable(offsetCodesType, inputBase, computeLiteralsTable(literalsLengthType, inputBase, input2 + 1, inputLimit), inputLimit), inputLimit);
            long input4 = input3;
            BitInputStream.Initializer initializer = new BitInputStream.Initializer(inputBase, input3, inputLimit);
            initializer.initialize();
            int bitsConsumed = initializer.getBitsConsumed();
            long bits2 = initializer.getBits();
            long currentAddress2 = initializer.getCurrentAddress();
            FiniteStateEntropy.Table currentLiteralsLengthTable = this.currentLiteralsLengthTable;
            FiniteStateEntropy.Table currentOffsetCodesTable3 = this.currentOffsetCodesTable;
            FiniteStateEntropy.Table currentMatchLengthTable = this.currentMatchLengthTable;
            int literalsLengthState2 = (int) BitInputStream.peekBits(bitsConsumed, bits2, currentLiteralsLengthTable.log2Size);
            int bitsConsumed2 = bitsConsumed + currentLiteralsLengthTable.log2Size;
            int offsetCodesState2 = (int) BitInputStream.peekBits(bitsConsumed2, bits2, currentOffsetCodesTable3.log2Size);
            int bitsConsumed3 = bitsConsumed2 + currentOffsetCodesTable3.log2Size;
            int matchLengthState2 = (int) BitInputStream.peekBits(bitsConsumed3, bits2, currentMatchLengthTable.log2Size);
            int bitsConsumed4 = bitsConsumed3 + currentMatchLengthTable.log2Size;
            int[] previousOffsets = this.previousOffsets;
            byte[] literalsLengthNumbersOfBits = currentLiteralsLengthTable.numberOfBits;
            int size3 = size2;
            int[] literalsLengthNewStates3 = currentLiteralsLengthTable.newState;
            byte[] literalsLengthSymbols2 = currentLiteralsLengthTable.symbol;
            byte[] matchLengthNumbersOfBits2 = currentMatchLengthTable.numberOfBits;
            int sequenceCount4 = sequenceCount;
            int[] matchLengthNewStates2 = currentMatchLengthTable.newState;
            int[] matchLengthNewStates3 = matchLengthNewStates2;
            byte[] matchLengthSymbols3 = currentMatchLengthTable.symbol;
            byte[] offsetCodesNumbersOfBits = currentOffsetCodesTable3.numberOfBits;
            int[] offsetCodesNewStates = currentOffsetCodesTable3.newState;
            byte[] offsetCodesSymbols = currentOffsetCodesTable3.symbol;
            long output2 = output;
            int literalsLengthState3 = literalsLengthState2;
            long literalsInput2 = literalsInput;
            long literalsInput3 = currentAddress2;
            int bitsConsumed5 = bitsConsumed4;
            long bits3 = bits2;
            int bitsConsumed6 = sequenceCount4;
            int sequenceCount5 = offsetCodesState2;
            int offsetCodesState3 = matchLengthState2;
            while (true) {
                if (bitsConsumed6 <= 0) {
                    output = output2;
                    literalsInput = literalsInput2;
                    break;
                }
                int sequenceCount6 = bitsConsumed6 - 1;
                int[] previousOffsets2 = previousOffsets;
                FiniteStateEntropy.Table currentLiteralsLengthTable2 = currentLiteralsLengthTable;
                BitInputStream.Loader loader = new BitInputStream.Loader(inputBase, input4, literalsInput3, bits3, bitsConsumed5);
                loader.load();
                int bitsConsumed7 = loader.getBitsConsumed();
                long bits4 = loader.getBits();
                long currentAddress3 = loader.getCurrentAddress();
                if (loader.isOverflow()) {
                    Util.verify(sequenceCount6 == 0, input4, "Not all sequences were consumed");
                    output = output2;
                    literalsInput = literalsInput2;
                } else {
                    long input5 = input4;
                    int literalsLengthCode = literalsLengthSymbols2[literalsLengthState3];
                    int matchLengthCode3 = matchLengthSymbols3[offsetCodesState3];
                    int offsetCode2 = offsetCodesSymbols[sequenceCount5];
                    int literalsLengthBits = Constants.LITERALS_LENGTH_BITS[literalsLengthCode];
                    byte[] offsetCodesSymbols2 = offsetCodesSymbols;
                    int matchLengthBits = Constants.MATCH_LENGTH_BITS[matchLengthCode3];
                    byte[] literalsLengthSymbols3 = literalsLengthSymbols2;
                    int offset = OFFSET_CODES_BASE[offsetCode2];
                    if (offsetCode2 <= 0) {
                        matchLengthSymbols = matchLengthSymbols3;
                        literalsLengthNewStates = literalsLengthNewStates3;
                        currentOffsetCodesTable = currentOffsetCodesTable3;
                        offsetBits = offsetCode2;
                    } else {
                        matchLengthSymbols = matchLengthSymbols3;
                        literalsLengthNewStates = literalsLengthNewStates3;
                        currentOffsetCodesTable = currentOffsetCodesTable3;
                        offsetBits = offsetCode2;
                        offset = (int) (offset + BitInputStream.peekBits(bitsConsumed7, bits4, offsetBits));
                        bitsConsumed7 += offsetBits;
                    }
                    if (offsetCode2 > 1) {
                        previousOffsets2[2] = previousOffsets2[1];
                        previousOffsets2[1] = previousOffsets2[0];
                        previousOffsets2[0] = offset;
                    } else {
                        if (literalsLengthCode == 0) {
                            offset++;
                        }
                        if (offset == 0) {
                            offset = previousOffsets2[0];
                        } else {
                            if (offset == 3) {
                                temp = previousOffsets2[0] - 1;
                            } else {
                                temp = previousOffsets2[offset];
                            }
                            if (temp == 0) {
                                temp = 1;
                            }
                            if (offset != 1) {
                                previousOffsets2[2] = previousOffsets2[1];
                            }
                            previousOffsets2[1] = previousOffsets2[0];
                            previousOffsets2[0] = temp;
                            offset = temp;
                        }
                    }
                    int matchLength2 = MATCH_LENGTH_BASE[matchLengthCode3];
                    if (matchLengthCode3 <= 31) {
                        offsetBits2 = offsetBits;
                        matchLength = matchLength2;
                    } else {
                        offsetBits2 = offsetBits;
                        int matchLength3 = (int) (matchLength2 + BitInputStream.peekBits(bitsConsumed7, bits4, matchLengthBits));
                        bitsConsumed7 += matchLengthBits;
                        matchLength = matchLength3;
                    }
                    int literalsLength3 = LITERALS_LENGTH_BASE[literalsLengthCode];
                    if (literalsLengthCode <= 15) {
                        matchLengthCode = matchLengthCode3;
                        offsetCode = offsetCode2;
                        literalsLength = literalsLength3;
                        literalsLength2 = bitsConsumed7;
                    } else {
                        matchLengthCode = matchLengthCode3;
                        offsetCode = offsetCode2;
                        literalsLength = (int) (literalsLength3 + BitInputStream.peekBits(bitsConsumed7, bits4, literalsLengthBits));
                        literalsLength2 = bitsConsumed7 + literalsLengthBits;
                    }
                    int totalBits = literalsLengthBits + matchLengthBits + offsetBits2;
                    if (totalBits > 31) {
                        matchLengthCode2 = literalsLength;
                        input = input5;
                        BitInputStream.Loader loader1 = new BitInputStream.Loader(inputBase, input5, currentAddress3, bits4, literalsLength2);
                        loader1.load();
                        literalsLength2 = loader1.getBitsConsumed();
                        bits4 = loader1.getBits();
                        currentAddress = loader1.getCurrentAddress();
                    } else {
                        input = input5;
                        matchLengthCode2 = literalsLength;
                        currentAddress = currentAddress3;
                    }
                    int numberOfBits = literalsLengthNumbersOfBits[literalsLengthState3];
                    int literalsLengthState4 = (int) (literalsLengthNewStates[literalsLengthState3] + BitInputStream.peekBits(literalsLength2, bits4, numberOfBits));
                    int bitsConsumed8 = literalsLength2 + numberOfBits;
                    int numberOfBits2 = matchLengthNumbersOfBits2[offsetCodesState3];
                    int matchLengthState3 = (int) (matchLengthNewStates3[offsetCodesState3] + BitInputStream.peekBits(bitsConsumed8, bits4, numberOfBits2));
                    int bitsConsumed9 = bitsConsumed8 + numberOfBits2;
                    int numberOfBits3 = offsetCodesNumbersOfBits[sequenceCount5];
                    int offsetCodesState4 = (int) (offsetCodesNewStates[sequenceCount5] + BitInputStream.peekBits(bitsConsumed9, bits4, numberOfBits3));
                    int bitsConsumed10 = bitsConsumed9 + numberOfBits3;
                    long literalOutputLimit = output2 + matchLengthCode2;
                    long matchOutputLimit = literalOutputLimit + matchLength;
                    byte[] literalsLengthNumbersOfBits2 = literalsLengthNumbersOfBits;
                    long input6 = input;
                    Util.verify(matchOutputLimit <= outputLimit, input6, "Output buffer too small");
                    long literalEnd = literalsInput2 + matchLengthCode2;
                    Util.verify(literalEnd <= literalsLimit, input6, "Input is corrupted");
                    long matchAddress = literalOutputLimit - offset;
                    Util.verify(matchAddress >= outputAbsoluteBaseAddress, input6, "Input is corrupted");
                    if (literalOutputLimit > fastOutputLimit) {
                        offsetCodesState = offsetCodesState4;
                        matchLengthState = matchLengthState3;
                        bits = bits4;
                        literalsLengthState = literalsLengthState4;
                        matchLengthNumbersOfBits = matchLengthNumbersOfBits2;
                        matchLengthNewStates = matchLengthNewStates3;
                        literalsLengthSymbols = literalsLengthSymbols3;
                        matchLengthSymbols2 = matchLengthSymbols;
                        size = size3;
                        literalsLengthNewStates2 = literalsLengthNewStates;
                        currentOffsetCodesTable2 = currentOffsetCodesTable;
                        executeLastSequence(outputBase, output2, literalOutputLimit, matchOutputLimit, fastOutputLimit, literalsInput2, matchAddress);
                    } else {
                        matchLengthState = matchLengthState3;
                        bits = bits4;
                        literalsLengthState = literalsLengthState4;
                        matchLengthNumbersOfBits = matchLengthNumbersOfBits2;
                        offsetCodesState = offsetCodesState4;
                        size = size3;
                        matchLengthNewStates = matchLengthNewStates3;
                        literalsLengthSymbols = literalsLengthSymbols3;
                        literalsLengthNewStates2 = literalsLengthNewStates;
                        matchLengthSymbols2 = matchLengthSymbols;
                        currentOffsetCodesTable2 = currentOffsetCodesTable;
                        copyMatch(outputBase, fastOutputLimit, copyLiterals(outputBase, literalsBase, output2, literalsInput2, literalOutputLimit), offset, matchOutputLimit, matchAddress, matchLength, fastMatchOutputLimit);
                    }
                    output2 = matchOutputLimit;
                    literalsInput2 = literalEnd;
                    bitsConsumed6 = sequenceCount6;
                    currentLiteralsLengthTable = currentLiteralsLengthTable2;
                    previousOffsets = previousOffsets2;
                    literalsInput3 = currentAddress;
                    bitsConsumed5 = bitsConsumed10;
                    sequenceCount5 = offsetCodesState;
                    offsetCodesState3 = matchLengthState;
                    bits3 = bits;
                    matchLengthNumbersOfBits2 = matchLengthNumbersOfBits;
                    literalsLengthState3 = literalsLengthState;
                    literalsLengthSymbols2 = literalsLengthSymbols;
                    matchLengthNewStates3 = matchLengthNewStates;
                    matchLengthSymbols3 = matchLengthSymbols2;
                    size3 = size;
                    currentOffsetCodesTable3 = currentOffsetCodesTable2;
                    literalsLengthNewStates3 = literalsLengthNewStates2;
                    offsetCodesSymbols = offsetCodesSymbols2;
                    input4 = input6;
                    literalsLengthNumbersOfBits = literalsLengthNumbersOfBits2;
                }
            }
        }
        return (int) (copyLastLiteral(outputBase, literalsBase, literalsLimit, output, literalsInput) - outputAddress);
    }

    private static long copyLastLiteral(Object outputBase, Object literalsBase, long literalsLimit, long output, long literalsInput) {
        long lastLiteralsSize = literalsLimit - literalsInput;
        UnsafeUtil.UNSAFE.copyMemory(literalsBase, literalsInput, outputBase, output, lastLiteralsSize);
        return output + lastLiteralsSize;
    }

    private static void copyMatch(Object outputBase, long fastOutputLimit, long output, int offset, long matchOutputLimit, long matchAddress, int matchLength, long fastMatchOutputLimit) {
        copyMatchTail(outputBase, fastOutputLimit, output + 8, matchOutputLimit, copyMatchHead(outputBase, output, offset, matchAddress), matchLength - 8, fastMatchOutputLimit);
    }

    private static void copyMatchTail(Object outputBase, long fastOutputLimit, long output, long matchOutputLimit, long matchAddress, int matchLength, long fastMatchOutputLimit) {
        if (matchOutputLimit < fastMatchOutputLimit) {
            long output2 = output;
            long matchAddress2 = matchAddress;
            int copied = 0;
            do {
                UnsafeUtil.UNSAFE.putLong(outputBase, output2, UnsafeUtil.UNSAFE.getLong(outputBase, matchAddress2));
                output2 += 8;
                matchAddress2 += 8;
                copied += 8;
            } while (copied < matchLength);
            return;
        }
        long output3 = output;
        long matchAddress3 = matchAddress;
        while (output3 < fastOutputLimit) {
            UnsafeUtil.UNSAFE.putLong(outputBase, output3, UnsafeUtil.UNSAFE.getLong(outputBase, matchAddress3));
            matchAddress3 += 8;
            output3 += 8;
        }
        while (output3 < matchOutputLimit) {
            UnsafeUtil.UNSAFE.putByte(outputBase, output3, UnsafeUtil.UNSAFE.getByte(outputBase, matchAddress3));
            matchAddress3 = 1 + matchAddress3;
            output3++;
        }
    }

    private static long copyMatchHead(Object outputBase, long output, int offset, long matchAddress) {
        if (offset < 8) {
            int increment32 = DEC_32_TABLE[offset];
            int decrement64 = DEC_64_TABLE[offset];
            UnsafeUtil.UNSAFE.putByte(outputBase, output, UnsafeUtil.UNSAFE.getByte(outputBase, matchAddress));
            UnsafeUtil.UNSAFE.putByte(outputBase, output + 1, UnsafeUtil.UNSAFE.getByte(outputBase, 1 + matchAddress));
            UnsafeUtil.UNSAFE.putByte(outputBase, output + 2, UnsafeUtil.UNSAFE.getByte(outputBase, 2 + matchAddress));
            UnsafeUtil.UNSAFE.putByte(outputBase, output + 3, UnsafeUtil.UNSAFE.getByte(outputBase, 3 + matchAddress));
            long matchAddress2 = matchAddress + increment32;
            UnsafeUtil.UNSAFE.putInt(outputBase, 4 + output, UnsafeUtil.UNSAFE.getInt(outputBase, matchAddress2));
            return matchAddress2 - decrement64;
        }
        UnsafeUtil.UNSAFE.putLong(outputBase, output, UnsafeUtil.UNSAFE.getLong(outputBase, matchAddress));
        return matchAddress + 8;
    }

    private static long copyLiterals(Object outputBase, Object literalsBase, long output, long literalsInput, long literalOutputLimit) {
        long literalInput = literalsInput;
        do {
            UnsafeUtil.UNSAFE.putLong(outputBase, output, UnsafeUtil.UNSAFE.getLong(literalsBase, literalInput));
            output += 8;
            literalInput += 8;
        } while (output < literalOutputLimit);
        return literalOutputLimit;
    }

    private long computeMatchLengthTable(int matchLengthType, Object inputBase, long input, long inputLimit) {
        switch (matchLengthType) {
            case 0:
                this.currentMatchLengthTable = DEFAULT_MATCH_LENGTH_TABLE;
                return input;
            case 1:
                Util.verify(input < inputLimit, input, "Not enough input bytes");
                long input2 = 1 + input;
                byte value = UnsafeUtil.UNSAFE.getByte(inputBase, input);
                Util.verify(value <= 52, input2, "Value exceeds expected maximum value");
                FseTableReader.initializeRleTable(this.matchLengthTable, value);
                this.currentMatchLengthTable = this.matchLengthTable;
                return input2;
            case 2:
                long input3 = this.fse.readFseTable(this.matchLengthTable, inputBase, input, inputLimit, 52, 9) + input;
                this.currentMatchLengthTable = this.matchLengthTable;
                return input3;
            case 3:
                Util.verify(this.currentMatchLengthTable != null, input, "Expected match length table to be present");
                return input;
            default:
                throw Util.fail(input, "Invalid match length encoding type");
        }
    }

    private long computeOffsetsTable(int offsetCodesType, Object inputBase, long input, long inputLimit) {
        switch (offsetCodesType) {
            case 0:
                this.currentOffsetCodesTable = DEFAULT_OFFSET_CODES_TABLE;
                return input;
            case 1:
                Util.verify(input < inputLimit, input, "Not enough input bytes");
                long input2 = 1 + input;
                byte value = UnsafeUtil.UNSAFE.getByte(inputBase, input);
                Util.verify(value <= 28, input2, "Value exceeds expected maximum value");
                FseTableReader.initializeRleTable(this.offsetCodesTable, value);
                this.currentOffsetCodesTable = this.offsetCodesTable;
                return input2;
            case 2:
                long input3 = this.fse.readFseTable(this.offsetCodesTable, inputBase, input, inputLimit, 28, 8) + input;
                this.currentOffsetCodesTable = this.offsetCodesTable;
                return input3;
            case 3:
                Util.verify(this.currentOffsetCodesTable != null, input, "Expected match length table to be present");
                return input;
            default:
                throw Util.fail(input, "Invalid offset code encoding type");
        }
    }

    private long computeLiteralsTable(int literalsLengthType, Object inputBase, long input, long inputLimit) {
        switch (literalsLengthType) {
            case 0:
                this.currentLiteralsLengthTable = DEFAULT_LITERALS_LENGTH_TABLE;
                return input;
            case 1:
                Util.verify(input < inputLimit, input, "Not enough input bytes");
                long input2 = 1 + input;
                byte value = UnsafeUtil.UNSAFE.getByte(inputBase, input);
                Util.verify(value <= 35, input2, "Value exceeds expected maximum value");
                FseTableReader.initializeRleTable(this.literalsLengthTable, value);
                this.currentLiteralsLengthTable = this.literalsLengthTable;
                return input2;
            case 2:
                long input3 = this.fse.readFseTable(this.literalsLengthTable, inputBase, input, inputLimit, 35, 9) + input;
                this.currentLiteralsLengthTable = this.literalsLengthTable;
                return input3;
            case 3:
                Util.verify(this.currentLiteralsLengthTable != null, input, "Expected match length table to be present");
                return input;
            default:
                throw Util.fail(input, "Invalid literals length encoding type");
        }
    }

    private void executeLastSequence(Object outputBase, long output, long literalOutputLimit, long matchOutputLimit, long fastOutputLimit, long literalInput, long matchAddress) {
        long output2;
        long literalInput2;
        if (output >= fastOutputLimit) {
            output2 = output;
            literalInput2 = literalInput;
        } else {
            long output3 = output;
            long literalInput3 = literalInput;
            do {
                UnsafeUtil.UNSAFE.putLong(outputBase, output3, UnsafeUtil.UNSAFE.getLong(this.literalsBase, literalInput3));
                output3 += 8;
                literalInput3 += 8;
            } while (output3 < fastOutputLimit);
            literalInput2 = literalInput3 - (output3 - fastOutputLimit);
            output2 = fastOutputLimit;
        }
        while (output2 < literalOutputLimit) {
            UnsafeUtil.UNSAFE.putByte(outputBase, output2, UnsafeUtil.UNSAFE.getByte(this.literalsBase, literalInput2));
            output2++;
            literalInput2++;
        }
        long output4 = output2;
        long output5 = matchAddress;
        while (output4 < matchOutputLimit) {
            UnsafeUtil.UNSAFE.putByte(outputBase, output4, UnsafeUtil.UNSAFE.getByte(outputBase, output5));
            output4++;
            output5++;
        }
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:5:0x001f. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:12:0x00a3  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x00af  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x00c0  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x00de  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x00fe  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x00cb  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x00b1  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x00a5  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private int decodeCompressedLiterals(java.lang.Object r26, final long r27, int r29, int r30) {
        /*
            Method dump skipped, instructions count: 292
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.airlift.compress.zstd.ZstdFrameDecompressor.decodeCompressedLiterals(java.lang.Object, long, int, int):int");
    }

    private int decodeRleLiterals(Object inputBase, final long inputAddress, int blockSize) {
        int outputSize;
        long input;
        int type = (UnsafeUtil.UNSAFE.getByte(inputBase, inputAddress) >> 2) & 3;
        switch (type) {
            case 0:
            case 2:
                outputSize = (UnsafeUtil.UNSAFE.getByte(inputBase, inputAddress) & 255) >>> 3;
                input = inputAddress + 1;
                break;
            case 1:
                outputSize = (UnsafeUtil.UNSAFE.getShort(inputBase, inputAddress) & UShort.MAX_VALUE) >>> 4;
                input = inputAddress + 2;
                break;
            case 3:
                Util.verify(blockSize >= 4, inputAddress, "Not enough input bytes");
                outputSize = (UnsafeUtil.UNSAFE.getInt(inputBase, inputAddress) & 16777215) >>> 4;
                input = inputAddress + 3;
                break;
            default:
                throw Util.fail(inputAddress, "Invalid RLE literals header encoding type");
        }
        Util.verify(outputSize <= 131072, input, "Output exceeds maximum block size");
        long input2 = 1 + input;
        byte value = UnsafeUtil.UNSAFE.getByte(inputBase, input);
        Arrays.fill(this.literals, 0, outputSize + 8, value);
        this.literalsBase = this.literals;
        this.literalsAddress = Unsafe.ARRAY_BYTE_BASE_OFFSET;
        this.literalsLimit = Unsafe.ARRAY_BYTE_BASE_OFFSET + outputSize;
        return (int) (input2 - inputAddress);
    }

    private int decodeRawLiterals(Object inputBase, final long inputAddress, long inputLimit) {
        long input;
        int literalSize;
        int type = (UnsafeUtil.UNSAFE.getByte(inputBase, inputAddress) >> 2) & 3;
        switch (type) {
            case 0:
            case 2:
                int literalSize2 = (UnsafeUtil.UNSAFE.getByte(inputBase, inputAddress) & 255) >>> 3;
                long input2 = inputAddress + 1;
                input = input2;
                literalSize = literalSize2;
                break;
            case 1:
                int literalSize3 = (65535 & UnsafeUtil.UNSAFE.getShort(inputBase, inputAddress)) >>> 4;
                long input3 = inputAddress + 2;
                input = input3;
                literalSize = literalSize3;
                break;
            case 3:
                int header = ((65535 & UnsafeUtil.UNSAFE.getShort(inputBase, 1 + inputAddress)) << 8) | (UnsafeUtil.UNSAFE.getByte(inputBase, inputAddress) & 255);
                int literalSize4 = header >>> 4;
                long input4 = inputAddress + 3;
                input = input4;
                literalSize = literalSize4;
                break;
            default:
                throw Util.fail(inputAddress, "Invalid raw literals header encoding type");
        }
        long input5 = literalSize;
        Util.verify(input5 + input <= inputLimit, input, "Not enough input bytes");
        if (literalSize > (inputLimit - input) - 8) {
            this.literalsBase = this.literals;
            this.literalsAddress = Unsafe.ARRAY_BYTE_BASE_OFFSET;
            this.literalsLimit = Unsafe.ARRAY_BYTE_BASE_OFFSET + literalSize;
            UnsafeUtil.UNSAFE.copyMemory(inputBase, input, this.literals, this.literalsAddress, literalSize);
            Arrays.fill(this.literals, literalSize, literalSize + 8, (byte) 0);
        } else {
            this.literalsBase = inputBase;
            this.literalsAddress = input;
            this.literalsLimit = this.literalsAddress + literalSize;
        }
        return (int) ((input + literalSize) - inputAddress);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static FrameHeader readFrameHeader(final Object inputBase, final long inputAddress, final long inputLimit) {
        int windowSize;
        Util.verify(inputAddress < inputLimit, inputAddress, "Not enough input bytes");
        long input = inputAddress + 1;
        int frameHeaderDescriptor = UnsafeUtil.UNSAFE.getByte(inputBase, inputAddress) & 255;
        boolean singleSegment = (frameHeaderDescriptor & 32) != 0;
        int dictionaryDescriptor = frameHeaderDescriptor & 3;
        int contentSizeDescriptor = frameHeaderDescriptor >>> 6;
        int headerSize = (singleSegment ? 0 : 1) + 1 + (dictionaryDescriptor == 0 ? 0 : 1 << (dictionaryDescriptor - 1)) + (contentSizeDescriptor == 0 ? singleSegment ? 1 : 0 : 1 << contentSizeDescriptor);
        Util.verify(((long) headerSize) <= inputLimit - inputAddress, input, "Not enough input bytes");
        int windowSize2 = -1;
        if (!singleSegment) {
            long input2 = input + 1;
            int windowDescriptor = UnsafeUtil.UNSAFE.getByte(inputBase, input) & 255;
            int exponent = windowDescriptor >>> 3;
            int mantissa = windowDescriptor & 7;
            int base = 1 << (exponent + 10);
            windowSize2 = base + ((base / 8) * mantissa);
            input = input2;
        }
        long dictionaryId = -1;
        switch (dictionaryDescriptor) {
            case 1:
                windowSize = windowSize2;
                dictionaryId = UnsafeUtil.UNSAFE.getByte(inputBase, input) & 255;
                input++;
                break;
            case 2:
                windowSize = windowSize2;
                dictionaryId = UnsafeUtil.UNSAFE.getShort(inputBase, input) & UShort.MAX_VALUE;
                input += 2;
                break;
            case 3:
                windowSize = windowSize2;
                dictionaryId = UnsafeUtil.UNSAFE.getInt(inputBase, input) & 4294967295L;
                input += 4;
                break;
            default:
                windowSize = windowSize2;
                break;
        }
        Util.verify(dictionaryId == -1, input, "Custom dictionaries not supported");
        long contentSize = -1;
        switch (contentSizeDescriptor) {
            case 0:
                if (singleSegment) {
                    contentSize = UnsafeUtil.UNSAFE.getByte(inputBase, input) & 255;
                    input++;
                    break;
                }
                break;
            case 1:
                contentSize = (UnsafeUtil.UNSAFE.getShort(inputBase, input) & UShort.MAX_VALUE) + 256;
                input += 2;
                break;
            case 2:
                contentSize = UnsafeUtil.UNSAFE.getInt(inputBase, input) & 4294967295L;
                input += 4;
                break;
            case 3:
                contentSize = UnsafeUtil.UNSAFE.getLong(inputBase, input);
                input += 8;
                break;
        }
        boolean hasChecksum = (frameHeaderDescriptor & 4) != 0;
        return new FrameHeader(input - inputAddress, windowSize, contentSize, dictionaryId, hasChecksum);
    }

    public static long getDecompressedSize(final Object inputBase, final long inputAddress, final long inputLimit) {
        long input = inputAddress + verifyMagic(inputBase, inputAddress, inputLimit);
        return readFrameHeader(inputBase, input, inputLimit).contentSize;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int verifyMagic(Object inputBase, long inputAddress, long inputLimit) {
        Util.verify(inputLimit - inputAddress >= 4, inputAddress, "Not enough input bytes");
        int magic = UnsafeUtil.UNSAFE.getInt(inputBase, inputAddress);
        if (magic != -47205080) {
            if (magic == V07_MAGIC_NUMBER) {
                throw new MalformedInputException(inputAddress, "Data encoded in unsupported ZSTD v0.7 format");
            }
            throw new MalformedInputException(inputAddress, "Invalid magic prefix: " + Integer.toHexString(magic));
        }
        return 4;
    }
}
