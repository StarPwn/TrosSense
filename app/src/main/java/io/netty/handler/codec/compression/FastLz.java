package io.netty.handler.codec.compression;

import com.google.common.base.Ascii;
import io.netty.buffer.ByteBuf;

/* loaded from: classes4.dex */
final class FastLz {
    static final byte BLOCK_TYPE_COMPRESSED = 1;
    static final byte BLOCK_TYPE_NON_COMPRESSED = 0;
    static final byte BLOCK_WITHOUT_CHECKSUM = 0;
    static final byte BLOCK_WITH_CHECKSUM = 16;
    static final int CHECKSUM_OFFSET = 4;
    private static final int HASH_LOG = 13;
    private static final int HASH_MASK = 8191;
    private static final int HASH_SIZE = 8192;
    static final int LEVEL_1 = 1;
    static final int LEVEL_2 = 2;
    static final int LEVEL_AUTO = 0;
    static final int MAGIC_NUMBER = 4607066;
    static final int MAX_CHUNK_LENGTH = 65535;
    private static final int MAX_COPY = 32;
    private static final int MAX_DISTANCE = 8191;
    private static final int MAX_FARDISTANCE = 73725;
    private static final int MAX_LEN = 264;
    static final int MIN_LENGTH_TO_COMPRESSION = 32;
    private static final int MIN_RECOMENDED_LENGTH_FOR_LEVEL_2 = 65536;
    static final int OPTIONS_OFFSET = 3;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int calculateOutputBufferLength(int inputLength) {
        int outputLength = (int) (inputLength * 1.06d);
        return Math.max(outputLength, 66);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:144:0x018a A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00ec  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int compress(io.netty.buffer.ByteBuf r28, int r29, int r30, io.netty.buffer.ByteBuf r31, int r32, int r33) {
        /*
            Method dump skipped, instructions count: 1020
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.codec.compression.FastLz.compress(io.netty.buffer.ByteBuf, int, int, io.netty.buffer.ByteBuf, int, int):int");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int decompress(ByteBuf input, int inOffset, int inLength, ByteBuf output, int outOffset, int outLength) {
        int level;
        int i;
        int loop;
        long ctrl;
        int ip;
        int ref;
        char c = 5;
        int i2 = 1;
        int level2 = (input.getByte(inOffset) >> 5) + 1;
        if (level2 != 1 && level2 != 2) {
            throw new DecompressionException(String.format("invalid level: %d (expected: %d or %d)", Integer.valueOf(level2), 1, 2));
        }
        int op = 0;
        int ip2 = 0 + 1;
        int ip3 = inOffset + 0;
        long ctrl2 = input.getByte(ip3) & Ascii.US;
        int loop2 = 1;
        while (true) {
            int ref2 = op;
            long len = ctrl2 >> c;
            long ofs = (31 & ctrl2) << 8;
            if (ctrl2 >= 32) {
                long len2 = len - 1;
                int ref3 = (int) (ref2 - ofs);
                if (len2 != 6) {
                    loop = loop2;
                    ctrl = ctrl2;
                } else if (level2 == 1) {
                    loop = loop2;
                    len2 += input.getUnsignedByte(inOffset + ip2);
                    ip2++;
                    ctrl = ctrl2;
                } else {
                    loop = loop2;
                    while (true) {
                        int code = input.getUnsignedByte(inOffset + ip2);
                        ctrl = ctrl2;
                        len2 += code;
                        ip2++;
                        if (code != 255) {
                            break;
                        }
                        ctrl2 = ctrl;
                    }
                }
                if (level2 == 1) {
                    ip = ip2 + 1;
                    ref = ref3 - input.getUnsignedByte(inOffset + ip2);
                    level = level2;
                } else {
                    ip = ip2 + 1;
                    int code2 = input.getUnsignedByte(inOffset + ip2);
                    int ref4 = ref3 - code2;
                    if (code2 != 255 || ofs != 7936) {
                        level = level2;
                        ref = ref4;
                    } else {
                        long ofs2 = input.getUnsignedByte(inOffset + ip) << 8;
                        ip = ip + 1 + 1;
                        level = level2;
                        ref = (int) ((op - (ofs2 + input.getUnsignedByte(inOffset + r9))) - 8191);
                    }
                }
                if (op + len2 + 3 > outLength || ref - 1 < 0) {
                    return 0;
                }
                if (ip < inLength) {
                    ctrl2 = input.getUnsignedByte(inOffset + ip);
                    ip++;
                    loop2 = loop;
                } else {
                    loop2 = 0;
                    ctrl2 = ctrl;
                }
                if (ref == op) {
                    i = 1;
                    byte b = output.getByte((outOffset + ref) - 1);
                    int op2 = op + 1;
                    output.setByte(outOffset + op, b);
                    int op3 = op2 + 1;
                    output.setByte(outOffset + op2, b);
                    int op4 = op3 + 1;
                    output.setByte(outOffset + op3, b);
                    while (len2 != 0) {
                        output.setByte(outOffset + op4, b);
                        len2--;
                        op4++;
                    }
                    op = op4;
                    ip2 = ip;
                } else {
                    i = 1;
                    int ref5 = ref - 1;
                    int op5 = op + 1;
                    int ref6 = ref5 + 1;
                    output.setByte(outOffset + op, output.getByte(outOffset + ref5));
                    int op6 = op5 + 1;
                    int ref7 = ref6 + 1;
                    output.setByte(outOffset + op5, output.getByte(outOffset + ref6));
                    int op7 = op6 + 1;
                    int ref8 = ref7 + 1;
                    output.setByte(outOffset + op6, output.getByte(outOffset + ref7));
                    while (len2 != 0) {
                        output.setByte(outOffset + op7, output.getByte(outOffset + ref8));
                        len2--;
                        op7++;
                        ref8++;
                    }
                    op = op7;
                    ip2 = ip;
                }
            } else {
                level = level2;
                i = i2;
                long ctrl3 = ctrl2 + 1;
                if (op + ctrl3 > outLength || ip2 + ctrl3 > inLength) {
                    return 0;
                }
                int op8 = op + 1;
                int ip4 = ip2 + 1;
                output.setByte(outOffset + op, input.getByte(inOffset + ip2));
                ctrl2 = ctrl3 - 1;
                while (ctrl2 != 0) {
                    output.setByte(outOffset + op8, input.getByte(inOffset + ip4));
                    ctrl2--;
                    op8++;
                    ip4++;
                }
                int loop3 = ip4 < inLength ? i : 0;
                if (loop3 != 0) {
                    ctrl2 = input.getUnsignedByte(inOffset + ip4);
                    op = op8;
                    ip2 = ip4 + 1;
                    loop2 = loop3;
                } else {
                    op = op8;
                    loop2 = loop3;
                    ip2 = ip4;
                }
            }
            if (loop2 != 0) {
                level2 = level;
                i2 = i;
                c = 5;
            } else {
                return op;
            }
        }
    }

    private static int hashFunction(ByteBuf p, int offset) {
        int v = readU16(p, offset);
        return (v ^ (readU16(p, offset + 1) ^ (v >> 3))) & 8191;
    }

    private static int readU16(ByteBuf data, int offset) {
        if (offset + 1 >= data.readableBytes()) {
            return data.getUnsignedByte(offset);
        }
        return (data.getUnsignedByte(offset + 1) << 8) | data.getUnsignedByte(offset);
    }

    private FastLz() {
    }
}
