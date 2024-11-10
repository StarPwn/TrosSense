package io.airlift.compress.snappy;

import io.airlift.compress.MalformedInputException;
import kotlin.UShort;
import sun.misc.Unsafe;

/* loaded from: classes.dex */
public final class SnappyRawDecompressor {
    private static final int[] DEC_32_TABLE = {4, 1, 2, 1, 4, 4, 4, 4};
    private static final int[] DEC_64_TABLE = {0, 0, 0, -1, 0, 1, 2, 3};
    private static final int[] wordmask = {0, 255, 65535, 16777215, -1};
    private static final short[] opLookupTable = {1, 2052, 4097, 8193, 2, 2053, 4098, 8194, 3, 2054, 4099, 8195, 4, 2055, 4100, 8196, 5, 2056, 4101, 8197, 6, 2057, 4102, 8198, 7, 2058, 4103, 8199, 8, 2059, 4104, 8200, 9, 2308, 4105, 8201, 10, 2309, 4106, 8202, 11, 2310, 4107, 8203, 12, 2311, 4108, 8204, 13, 2312, 4109, 8205, 14, 2313, 4110, 8206, 15, 2314, 4111, 8207, 16, 2315, 4112, 8208, 17, 2564, 4113, 8209, 18, 2565, 4114, 8210, 19, 2566, 4115, 8211, 20, 2567, 4116, 8212, 21, 2568, 4117, 8213, 22, 2569, 4118, 8214, 23, 2570, 4119, 8215, 24, 2571, 4120, 8216, 25, 2820, 4121, 8217, 26, 2821, 4122, 8218, 27, 2822, 4123, 8219, 28, 2823, 4124, 8220, 29, 2824, 4125, 8221, 30, 2825, 4126, 8222, 31, 2826, 4127, 8223, 32, 2827, 4128, 8224, 33, 3076, 4129, 8225, 34, 3077, 4130, 8226, 35, 3078, 4131, 8227, 36, 3079, 4132, 8228, 37, 3080, 4133, 8229, 38, 3081, 4134, 8230, 39, 3082, 4135, 8231, 40, 3083, 4136, 8232, 41, 3332, 4137, 8233, 42, 3333, 4138, 8234, 43, 3334, 4139, 8235, 44, 3335, 4140, 8236, 45, 3336, 4141, 8237, 46, 3337, 4142, 8238, 47, 3338, 4143, 8239, 48, 3339, 4144, 8240, 49, 3588, 4145, 8241, 50, 3589, 4146, 8242, 51, 3590, 4147, 8243, 52, 3591, 4148, 8244, 53, 3592, 4149, 8245, 54, 3593, 4150, 8246, 55, 3594, 4151, 8247, 56, 3595, 4152, 8248, 57, 3844, 4153, 8249, 58, 3845, 4154, 8250, 59, 3846, 4155, 8251, 60, 3847, 4156, 8252, 2049, 3848, 4157, 8253, 4097, 3849, 4158, 8254, 6145, 3850, 4159, 8255, 8193, 3851, 4160, 8256};

    private SnappyRawDecompressor() {
    }

    public static int getUncompressedLength(Object compressed, long compressedAddress, long compressedLimit) {
        return readUncompressedLength(compressed, compressedAddress, compressedLimit)[0];
    }

    public static int decompress(final Object inputBase, final long inputAddress, final long inputLimit, final Object outputBase, final long outputAddress, final long outputLimit) {
        int[] varInt = readUncompressedLength(inputBase, inputAddress, inputLimit);
        boolean z = false;
        int expectedLength = varInt[0];
        long input = inputAddress + varInt[1];
        long input2 = expectedLength;
        if (input2 <= outputLimit - outputAddress) {
            z = true;
        }
        SnappyInternalUtils.checkArgument(z, "Uncompressed length %s must be less than %s", Integer.valueOf(expectedLength), Long.valueOf(outputLimit - outputAddress));
        int uncompressedSize = uncompressAll(inputBase, input, inputLimit, outputBase, outputAddress, outputLimit);
        if (expectedLength != uncompressedSize) {
            throw new MalformedInputException(0L, String.format("Recorded length is %s bytes but actual length after decompression is %s bytes ", Integer.valueOf(expectedLength), Integer.valueOf(uncompressedSize)));
        }
        return expectedLength;
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:88:0x0049. Please report as an issue. */
    private static int uncompressAll(final Object inputBase, final long inputAddress, final long inputLimit, final Object outputBase, final long outputAddress, final long outputLimit) {
        int trailer;
        int entry;
        long fastOutputLimit;
        long matchAddress;
        long output;
        long output2;
        long input;
        long output3;
        Object obj = inputBase;
        long fastOutputLimit2 = outputLimit - 8;
        long input2 = inputAddress;
        long output4 = outputAddress;
        while (input2 < inputLimit) {
            long input3 = input2 + 1;
            int opCode = UnsafeUtil.UNSAFE.getByte(obj, input2) & 255;
            int entry2 = opLookupTable[opCode] & UShort.MAX_VALUE;
            int trailerBytes = entry2 >>> 11;
            int trailer2 = 0;
            if (input3 + 4 < inputLimit) {
                trailer = UnsafeUtil.UNSAFE.getInt(obj, input3) & wordmask[trailerBytes];
            } else {
                if (trailerBytes + input3 > inputLimit) {
                    throw new MalformedInputException(input3 - inputAddress);
                }
                switch (trailerBytes) {
                    case 2:
                        trailer2 |= (UnsafeUtil.UNSAFE.getByte(obj, input3 + 1) & 255) << 8;
                    case 1:
                        trailer = trailer2 | (UnsafeUtil.UNSAFE.getByte(obj, input3) & 255);
                        break;
                    case 3:
                        entry = entry2;
                        entry2 = entry;
                        trailer2 |= (UnsafeUtil.UNSAFE.getByte(obj, input3 + 2) & 255) << 16;
                        trailer2 |= (UnsafeUtil.UNSAFE.getByte(obj, input3 + 1) & 255) << 8;
                        trailer = trailer2 | (UnsafeUtil.UNSAFE.getByte(obj, input3) & 255);
                        break;
                    case 4:
                        entry = entry2;
                        trailer2 = (UnsafeUtil.UNSAFE.getByte(obj, input3 + 3) & 255) << 24;
                        entry2 = entry;
                        trailer2 |= (UnsafeUtil.UNSAFE.getByte(obj, input3 + 2) & 255) << 16;
                        trailer2 |= (UnsafeUtil.UNSAFE.getByte(obj, input3 + 1) & 255) << 8;
                        trailer = trailer2 | (UnsafeUtil.UNSAFE.getByte(obj, input3) & 255);
                        break;
                    default:
                        trailer = 0;
                        break;
                }
            }
            if (trailer < 0) {
                throw new MalformedInputException(input3 - inputAddress);
            }
            long input4 = input3 + trailerBytes;
            int entry3 = entry2;
            int entry4 = entry3 & 255;
            if (entry4 == 0) {
                input2 = input4;
            } else {
                if ((opCode & 3) == 0) {
                    int literalLength = entry4 + trailer;
                    long literalOutputLimit = output4 + literalLength;
                    if (literalOutputLimit <= fastOutputLimit2 && input4 + literalLength <= inputLimit - 8) {
                        long input5 = input4;
                        while (true) {
                            Unsafe unsafe = UnsafeUtil.UNSAFE;
                            long j = UnsafeUtil.UNSAFE.getLong(obj, input5);
                            int entry5 = entry3;
                            int trailerBytes2 = trailerBytes;
                            unsafe.putLong(outputBase, output4, j);
                            input5 += 8;
                            output4 += 8;
                            if (output4 < literalOutputLimit) {
                                entry3 = entry5;
                                trailerBytes = trailerBytes2;
                                obj = inputBase;
                            } else {
                                input = input5 - (output4 - literalOutputLimit);
                                output3 = literalOutputLimit;
                                fastOutputLimit = fastOutputLimit2;
                                output4 = output3;
                                input2 = input;
                                output2 = 8;
                            }
                        }
                    }
                    if (literalOutputLimit > outputLimit) {
                        throw new MalformedInputException(input4 - inputAddress);
                    }
                    fastOutputLimit = fastOutputLimit2;
                    long fastOutputLimit3 = output4;
                    UnsafeUtil.UNSAFE.copyMemory(inputBase, input4, outputBase, output4, literalLength);
                    input = input4 + literalLength;
                    output3 = literalLength + fastOutputLimit3;
                    output4 = output3;
                    input2 = input;
                    output2 = 8;
                } else {
                    fastOutputLimit = fastOutputLimit2;
                    long fastOutputLimit4 = output4;
                    int matchOffset = (entry3 & 1792) + trailer;
                    long matchAddress2 = fastOutputLimit4 - matchOffset;
                    if (matchAddress2 < outputAddress || entry4 + fastOutputLimit4 > outputLimit) {
                        throw new MalformedInputException(input4 - inputAddress);
                    }
                    long matchOutputLimit = fastOutputLimit4 + entry4;
                    if (fastOutputLimit4 > fastOutputLimit) {
                        long matchAddress3 = matchAddress2;
                        long matchAddress4 = fastOutputLimit4;
                        while (matchAddress4 < matchOutputLimit) {
                            long output5 = matchAddress4 + 1;
                            UnsafeUtil.UNSAFE.putByte(outputBase, matchAddress4, UnsafeUtil.UNSAFE.getByte(outputBase, matchAddress3));
                            matchAddress4 = output5;
                            matchAddress3++;
                        }
                        output2 = 8;
                    } else {
                        if (matchOffset < 8) {
                            int increment32 = DEC_32_TABLE[matchOffset];
                            int decrement64 = DEC_64_TABLE[matchOffset];
                            UnsafeUtil.UNSAFE.putByte(outputBase, fastOutputLimit4, UnsafeUtil.UNSAFE.getByte(outputBase, matchAddress2));
                            UnsafeUtil.UNSAFE.putByte(outputBase, fastOutputLimit4 + 1, UnsafeUtil.UNSAFE.getByte(outputBase, matchAddress2 + 1));
                            UnsafeUtil.UNSAFE.putByte(outputBase, fastOutputLimit4 + 2, UnsafeUtil.UNSAFE.getByte(outputBase, matchAddress2 + 2));
                            UnsafeUtil.UNSAFE.putByte(outputBase, fastOutputLimit4 + 3, UnsafeUtil.UNSAFE.getByte(outputBase, matchAddress2 + 3));
                            long output6 = fastOutputLimit4 + 4;
                            long matchAddress5 = matchAddress2 + increment32;
                            UnsafeUtil.UNSAFE.putInt(outputBase, output6, UnsafeUtil.UNSAFE.getInt(outputBase, matchAddress5));
                            output = output6 + 4;
                            matchAddress = matchAddress5 - decrement64;
                        } else {
                            UnsafeUtil.UNSAFE.putLong(outputBase, fastOutputLimit4, UnsafeUtil.UNSAFE.getLong(outputBase, matchAddress2));
                            matchAddress = matchAddress2 + 8;
                            output = fastOutputLimit4 + 8;
                        }
                        if (matchOutputLimit <= fastOutputLimit) {
                            for (long output7 = output; output7 < matchOutputLimit; output7 += 8) {
                                UnsafeUtil.UNSAFE.putLong(outputBase, output7, UnsafeUtil.UNSAFE.getLong(outputBase, matchAddress));
                                matchAddress += 8;
                            }
                            output2 = 8;
                        } else {
                            if (matchOutputLimit > outputLimit) {
                                throw new MalformedInputException(input4 - inputAddress);
                            }
                            long matchAddress6 = matchAddress;
                            long matchAddress7 = output;
                            while (matchAddress7 < fastOutputLimit) {
                                UnsafeUtil.UNSAFE.putLong(outputBase, matchAddress7, UnsafeUtil.UNSAFE.getLong(outputBase, matchAddress6));
                                matchAddress6 += 8;
                                matchAddress7 += 8;
                            }
                            while (true) {
                                long matchAddress8 = matchAddress6;
                                if (matchAddress7 < matchOutputLimit) {
                                    long output8 = matchAddress7 + 1;
                                    matchAddress6 = matchAddress8 + 1;
                                    UnsafeUtil.UNSAFE.putByte(outputBase, matchAddress7, UnsafeUtil.UNSAFE.getByte(outputBase, matchAddress8));
                                    matchAddress7 = output8;
                                } else {
                                    output2 = 8;
                                }
                            }
                        }
                    }
                    output4 = matchOutputLimit;
                    input2 = input4;
                }
                obj = inputBase;
                fastOutputLimit2 = fastOutputLimit;
            }
        }
        long fastOutputLimit5 = output4;
        long output9 = fastOutputLimit5 - outputAddress;
        return (int) output9;
    }

    static int[] readUncompressedLength(Object compressed, long compressedAddress, long compressedLimit) {
        int b = getUnsignedByteSafe(compressed, 0 + compressedAddress, compressedLimit);
        int bytesRead = 0 + 1;
        int result = b & 127;
        if ((b & 128) != 0) {
            int b2 = getUnsignedByteSafe(compressed, bytesRead + compressedAddress, compressedLimit);
            bytesRead++;
            result |= (b2 & 127) << 7;
            if ((b2 & 128) != 0) {
                int b3 = getUnsignedByteSafe(compressed, bytesRead + compressedAddress, compressedLimit);
                bytesRead++;
                result |= (b3 & 127) << 14;
                if ((b3 & 128) != 0) {
                    int b4 = getUnsignedByteSafe(compressed, bytesRead + compressedAddress, compressedLimit);
                    bytesRead++;
                    result |= (b4 & 127) << 21;
                    if ((b4 & 128) != 0) {
                        int b5 = getUnsignedByteSafe(compressed, bytesRead + compressedAddress, compressedLimit);
                        bytesRead++;
                        result |= (b5 & 127) << 28;
                        if ((b5 & 128) != 0) {
                            throw new MalformedInputException(bytesRead + compressedAddress, "last byte of compressed length int has high bit set");
                        }
                    }
                }
            }
        }
        return new int[]{result, bytesRead};
    }

    private static int getUnsignedByteSafe(Object base, long address, long limit) {
        if (address >= limit) {
            throw new MalformedInputException(limit - address, "Input is truncated");
        }
        return UnsafeUtil.UNSAFE.getByte(base, address) & 255;
    }
}
