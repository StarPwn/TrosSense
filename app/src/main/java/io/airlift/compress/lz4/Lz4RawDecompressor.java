package io.airlift.compress.lz4;

import io.airlift.compress.MalformedInputException;
import kotlin.UShort;

/* loaded from: classes.dex */
public final class Lz4RawDecompressor {
    private static final int[] DEC_32_TABLE = {4, 1, 2, 1, 4, 4, 4, 4};
    private static final int[] DEC_64_TABLE = {0, 0, 0, -1, 0, 1, 2, 3};
    private static final int OFFSET_SIZE = 2;
    private static final int TOKEN_SIZE = 1;

    private Lz4RawDecompressor() {
    }

    public static int decompress(final Object inputBase, final long inputAddress, final long inputLimit, final Object outputBase, final long outputAddress, final long outputLimit) {
        long input;
        int literalLength;
        int literalLength2;
        int token;
        long input2;
        int value;
        long input3;
        long input4;
        long matchAddress;
        long output;
        long j;
        long j2;
        Object obj = inputBase;
        long j3 = 8;
        long fastOutputLimit = outputLimit - 8;
        if (inputAddress == inputLimit) {
            throw new MalformedInputException(0L, "input is empty");
        }
        long j4 = 1;
        if (outputAddress == outputLimit) {
            if (inputLimit - inputAddress == 1 && UnsafeUtil.UNSAFE.getByte(obj, inputAddress) == 0) {
                return 0;
            }
            return -1;
        }
        long output2 = outputAddress;
        for (long input5 = inputAddress; input5 < inputLimit; input5 = input4) {
            long input6 = input5 + j4;
            int token2 = UnsafeUtil.UNSAFE.getByte(obj, input5) & 255;
            int literalLength3 = token2 >>> 4;
            if (literalLength3 != 15) {
                input = input6;
                literalLength = literalLength3;
            } else {
                while (true) {
                    input = input6 + j4;
                    int value2 = UnsafeUtil.UNSAFE.getByte(obj, input6) & 255;
                    literalLength3 += value2;
                    if (value2 != 255 || input >= inputLimit - 15) {
                        break;
                    }
                    input6 = input;
                }
                literalLength = literalLength3;
            }
            long literalEnd = input + literalLength;
            long literalOutputLimit = output2 + literalLength;
            if (literalOutputLimit > fastOutputLimit - 4) {
                literalLength2 = literalLength;
            } else if (literalEnd > inputLimit - j3) {
                literalLength2 = literalLength;
            } else {
                long j5 = output2;
                int index = 0;
                long input7 = input;
                long output3 = j5;
                while (true) {
                    int literalLength4 = literalLength;
                    token = token2;
                    UnsafeUtil.UNSAFE.putLong(outputBase, output3, UnsafeUtil.UNSAFE.getLong(obj, input7));
                    output3 += j3;
                    input7 += j3;
                    int index2 = index + 8;
                    if (index2 >= literalLength4) {
                        break;
                    }
                    index = index2;
                    obj = inputBase;
                    token2 = token;
                    literalLength = literalLength4;
                }
                int offset = UnsafeUtil.UNSAFE.getShort(obj, literalEnd) & UShort.MAX_VALUE;
                long input8 = literalEnd + 2;
                long matchAddress2 = literalOutputLimit - offset;
                if (matchAddress2 < outputAddress) {
                    throw new MalformedInputException(input8 - inputAddress, "offset outside destination buffer");
                }
                int matchLength = token & 15;
                if (matchLength != 15) {
                    input2 = fastOutputLimit;
                    value = matchLength;
                    input3 = input8;
                } else {
                    while (input8 <= inputLimit - 5) {
                        long input9 = input8 + 1;
                        int value3 = UnsafeUtil.UNSAFE.getByte(obj, input8) & 255;
                        matchLength += value3;
                        if (value3 != 255) {
                            value = matchLength;
                            input3 = input9;
                            input2 = fastOutputLimit;
                        } else {
                            input8 = input9;
                        }
                    }
                    long fastOutputLimit2 = input8 - inputAddress;
                    throw new MalformedInputException(fastOutputLimit2);
                }
                int matchLength2 = value + 4;
                long matchOutputLimit = literalOutputLimit + matchLength2;
                if (offset >= 8) {
                    input4 = input3;
                    UnsafeUtil.UNSAFE.putLong(outputBase, literalOutputLimit, UnsafeUtil.UNSAFE.getLong(outputBase, matchAddress2));
                    matchAddress = matchAddress2 + 8;
                    output = literalOutputLimit + 8;
                } else {
                    int increment32 = DEC_32_TABLE[offset];
                    int decrement64 = DEC_64_TABLE[offset];
                    UnsafeUtil.UNSAFE.putByte(outputBase, literalOutputLimit, UnsafeUtil.UNSAFE.getByte(outputBase, matchAddress2));
                    input4 = input3;
                    long input10 = matchAddress2 + 1;
                    UnsafeUtil.UNSAFE.putByte(outputBase, literalOutputLimit + 1, UnsafeUtil.UNSAFE.getByte(outputBase, input10));
                    UnsafeUtil.UNSAFE.putByte(outputBase, literalOutputLimit + 2, UnsafeUtil.UNSAFE.getByte(outputBase, matchAddress2 + 2));
                    UnsafeUtil.UNSAFE.putByte(outputBase, literalOutputLimit + 3, UnsafeUtil.UNSAFE.getByte(outputBase, 3 + matchAddress2));
                    long output4 = literalOutputLimit + 4;
                    long matchAddress3 = matchAddress2 + increment32;
                    UnsafeUtil.UNSAFE.putInt(outputBase, output4, UnsafeUtil.UNSAFE.getInt(outputBase, matchAddress3));
                    output = output4 + 4;
                    matchAddress = matchAddress3 - decrement64;
                }
                if (matchOutputLimit > input2 - 4) {
                    if (matchOutputLimit > outputLimit - 5) {
                        throw new MalformedInputException(input4 - inputAddress, String.format("last %s bytes must be literals", 5));
                    }
                    long output5 = output;
                    long matchAddress4 = matchAddress;
                    while (output5 < input2) {
                        UnsafeUtil.UNSAFE.putLong(outputBase, output5, UnsafeUtil.UNSAFE.getLong(outputBase, matchAddress4));
                        matchAddress4 += 8;
                        output5 += 8;
                    }
                    while (output5 < matchOutputLimit) {
                        UnsafeUtil.UNSAFE.putByte(outputBase, output5, UnsafeUtil.UNSAFE.getByte(outputBase, matchAddress4));
                        output5++;
                        matchAddress4++;
                    }
                    j = 1;
                    j2 = 8;
                } else {
                    j = 1;
                    int i = 0;
                    long output6 = output;
                    long matchAddress5 = matchAddress;
                    do {
                        UnsafeUtil.UNSAFE.putLong(outputBase, output6, UnsafeUtil.UNSAFE.getLong(outputBase, matchAddress5));
                        j2 = 8;
                        output6 += 8;
                        matchAddress5 += 8;
                        i += 8;
                    } while (i < matchLength2 - 8);
                }
                output2 = matchOutputLimit;
                obj = inputBase;
                j3 = j2;
                j4 = j;
                fastOutputLimit = input2;
            }
            if (literalOutputLimit > outputLimit) {
                throw new MalformedInputException(input - inputAddress, "attempt to write last literal outside of destination buffer");
            }
            if (literalEnd != inputLimit) {
                throw new MalformedInputException(input - inputAddress, "all input must be consumed");
            }
            int literalLength5 = literalLength2;
            UnsafeUtil.UNSAFE.copyMemory(inputBase, input, outputBase, output2, literalLength5);
            output2 += literalLength5;
            return (int) (output2 - outputAddress);
        }
        return (int) (output2 - outputAddress);
    }
}
