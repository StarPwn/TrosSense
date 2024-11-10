package io.netty.handler.codec.base64;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.codec.http2.Http2CodecUtil;
import io.netty.util.ByteProcessor;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import java.nio.ByteOrder;
import okio.Utf8;

/* loaded from: classes4.dex */
public final class Base64 {
    private static final byte EQUALS_SIGN = 61;
    private static final byte EQUALS_SIGN_ENC = -1;
    private static final int MAX_LINE_LENGTH = 76;
    private static final byte NEW_LINE = 10;
    private static final byte WHITE_SPACE_ENC = -5;

    private static byte[] alphabet(Base64Dialect dialect) {
        return ((Base64Dialect) ObjectUtil.checkNotNull(dialect, "dialect")).alphabet;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static byte[] decodabet(Base64Dialect dialect) {
        return ((Base64Dialect) ObjectUtil.checkNotNull(dialect, "dialect")).decodabet;
    }

    private static boolean breakLines(Base64Dialect dialect) {
        return ((Base64Dialect) ObjectUtil.checkNotNull(dialect, "dialect")).breakLinesByDefault;
    }

    public static ByteBuf encode(ByteBuf src) {
        return encode(src, Base64Dialect.STANDARD);
    }

    public static ByteBuf encode(ByteBuf src, Base64Dialect dialect) {
        return encode(src, breakLines(dialect), dialect);
    }

    public static ByteBuf encode(ByteBuf src, boolean breakLines) {
        return encode(src, breakLines, Base64Dialect.STANDARD);
    }

    public static ByteBuf encode(ByteBuf src, boolean breakLines, Base64Dialect dialect) {
        ObjectUtil.checkNotNull(src, "src");
        ByteBuf dest = encode(src, src.readerIndex(), src.readableBytes(), breakLines, dialect);
        src.readerIndex(src.writerIndex());
        return dest;
    }

    public static ByteBuf encode(ByteBuf src, int off, int len) {
        return encode(src, off, len, Base64Dialect.STANDARD);
    }

    public static ByteBuf encode(ByteBuf src, int off, int len, Base64Dialect dialect) {
        return encode(src, off, len, breakLines(dialect), dialect);
    }

    public static ByteBuf encode(ByteBuf src, int off, int len, boolean breakLines) {
        return encode(src, off, len, breakLines, Base64Dialect.STANDARD);
    }

    public static ByteBuf encode(ByteBuf src, int off, int len, boolean breakLines, Base64Dialect dialect) {
        return encode(src, off, len, breakLines, dialect, src.alloc());
    }

    public static ByteBuf encode(ByteBuf src, int off, int len, boolean breakLines, Base64Dialect dialect, ByteBufAllocator allocator) {
        ObjectUtil.checkNotNull(src, "src");
        ObjectUtil.checkNotNull(dialect, "dialect");
        ByteBuf dest = allocator.buffer(encodedBufferSize(len, breakLines)).order(src.order());
        byte[] alphabet = alphabet(dialect);
        int len2 = len - 2;
        int d = 0;
        int e = 0;
        int lineLength = 0;
        while (d < len2) {
            encode3to4(src, d + off, 3, dest, e, alphabet);
            lineLength += 4;
            if (breakLines && lineLength == 76) {
                dest.setByte(e + 4, 10);
                e++;
                lineLength = 0;
            }
            d += 3;
            e += 4;
        }
        if (d < len) {
            encode3to4(src, d + off, len - d, dest, e, alphabet);
            e += 4;
        }
        if (e > 1 && dest.getByte(e - 1) == 10) {
            e--;
        }
        return dest.slice(0, e);
    }

    private static void encode3to4(ByteBuf src, int srcOffset, int numSigBytes, ByteBuf dest, int destOffset, byte[] alphabet) {
        int inBuff;
        int inBuff2;
        int i = 0;
        if (src.order() == ByteOrder.BIG_ENDIAN) {
            switch (numSigBytes) {
                case 1:
                    inBuff2 = toInt(src.getByte(srcOffset));
                    break;
                case 2:
                    inBuff2 = toIntBE(src.getShort(srcOffset));
                    break;
                default:
                    if (numSigBytes > 0) {
                        int inBuff3 = src.getMedium(srcOffset);
                        i = toIntBE(inBuff3);
                    }
                    inBuff2 = i;
                    break;
            }
            encode3to4BigEndian(inBuff2, numSigBytes, dest, destOffset, alphabet);
            return;
        }
        switch (numSigBytes) {
            case 1:
                inBuff = toInt(src.getByte(srcOffset));
                break;
            case 2:
                inBuff = toIntLE(src.getShort(srcOffset));
                break;
            default:
                if (numSigBytes > 0) {
                    int inBuff4 = src.getMedium(srcOffset);
                    i = toIntLE(inBuff4);
                }
                inBuff = i;
                break;
        }
        encode3to4LittleEndian(inBuff, numSigBytes, dest, destOffset, alphabet);
    }

    static int encodedBufferSize(int len, boolean breakLines) {
        long len43 = (len << 2) / 3;
        long ret = (3 + len43) & (-4);
        if (breakLines) {
            ret += len43 / 76;
        }
        if (ret < 2147483647L) {
            return (int) ret;
        }
        return Integer.MAX_VALUE;
    }

    private static int toInt(byte value) {
        return (value & 255) << 16;
    }

    private static int toIntBE(short value) {
        return ((65280 & value) << 8) | ((value & Http2CodecUtil.MAX_UNSIGNED_BYTE) << 8);
    }

    private static int toIntLE(short value) {
        return ((value & Http2CodecUtil.MAX_UNSIGNED_BYTE) << 16) | (65280 & value);
    }

    private static int toIntBE(int mediumValue) {
        return (16711680 & mediumValue) | (65280 & mediumValue) | (mediumValue & 255);
    }

    private static int toIntLE(int mediumValue) {
        return ((mediumValue & 255) << 16) | (65280 & mediumValue) | ((16711680 & mediumValue) >>> 16);
    }

    private static void encode3to4BigEndian(int inBuff, int numSigBytes, ByteBuf dest, int destOffset, byte[] alphabet) {
        switch (numSigBytes) {
            case 1:
                dest.setInt(destOffset, (alphabet[inBuff >>> 18] << 24) | (alphabet[(inBuff >>> 12) & 63] << 16) | 15616 | 61);
                return;
            case 2:
                dest.setInt(destOffset, (alphabet[inBuff >>> 18] << 24) | (alphabet[(inBuff >>> 12) & 63] << 16) | (alphabet[(inBuff >>> 6) & 63] << 8) | 61);
                return;
            case 3:
                dest.setInt(destOffset, (alphabet[inBuff >>> 18] << 24) | (alphabet[(inBuff >>> 12) & 63] << 16) | (alphabet[(inBuff >>> 6) & 63] << 8) | alphabet[inBuff & 63]);
                return;
            default:
                return;
        }
    }

    private static void encode3to4LittleEndian(int inBuff, int numSigBytes, ByteBuf dest, int destOffset, byte[] alphabet) {
        switch (numSigBytes) {
            case 1:
                dest.setInt(destOffset, 1023410176 | alphabet[inBuff >>> 18] | (alphabet[(inBuff >>> 12) & 63] << 8) | 3997696);
                return;
            case 2:
                dest.setInt(destOffset, 1023410176 | alphabet[inBuff >>> 18] | (alphabet[(inBuff >>> 12) & 63] << 8) | (alphabet[(inBuff >>> 6) & 63] << 16));
                return;
            case 3:
                dest.setInt(destOffset, alphabet[inBuff >>> 18] | (alphabet[(inBuff >>> 12) & 63] << 8) | (alphabet[(inBuff >>> 6) & 63] << 16) | (alphabet[inBuff & 63] << 24));
                return;
            default:
                return;
        }
    }

    public static ByteBuf decode(ByteBuf src) {
        return decode(src, Base64Dialect.STANDARD);
    }

    public static ByteBuf decode(ByteBuf src, Base64Dialect dialect) {
        ObjectUtil.checkNotNull(src, "src");
        ByteBuf dest = decode(src, src.readerIndex(), src.readableBytes(), dialect);
        src.readerIndex(src.writerIndex());
        return dest;
    }

    public static ByteBuf decode(ByteBuf src, int off, int len) {
        return decode(src, off, len, Base64Dialect.STANDARD);
    }

    public static ByteBuf decode(ByteBuf src, int off, int len, Base64Dialect dialect) {
        return decode(src, off, len, dialect, src.alloc());
    }

    public static ByteBuf decode(ByteBuf src, int off, int len, Base64Dialect dialect, ByteBufAllocator allocator) {
        ObjectUtil.checkNotNull(src, "src");
        ObjectUtil.checkNotNull(dialect, "dialect");
        return new Decoder().decode(src, off, len, allocator, dialect);
    }

    static int decodedBufferSize(int len) {
        return len - (len >>> 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class Decoder implements ByteProcessor {
        private final byte[] b4;
        private int b4Posn;
        private byte[] decodabet;
        private ByteBuf dest;
        private int outBuffPosn;

        private Decoder() {
            this.b4 = new byte[4];
        }

        ByteBuf decode(ByteBuf src, int off, int len, ByteBufAllocator allocator, Base64Dialect dialect) {
            this.dest = allocator.buffer(Base64.decodedBufferSize(len)).order(src.order());
            this.decodabet = Base64.decodabet(dialect);
            try {
                src.forEachByte(off, len, this);
                return this.dest.slice(0, this.outBuffPosn);
            } catch (Throwable cause) {
                this.dest.release();
                PlatformDependent.throwException(cause);
                return null;
            }
        }

        @Override // io.netty.util.ByteProcessor
        public boolean process(byte value) throws Exception {
            byte sbiDecode;
            if (value > 0 && (sbiDecode = this.decodabet[value]) >= -5) {
                if (sbiDecode >= -1) {
                    byte[] bArr = this.b4;
                    int i = this.b4Posn;
                    this.b4Posn = i + 1;
                    bArr[i] = value;
                    if (this.b4Posn > 3) {
                        this.outBuffPosn += decode4to3(this.b4, this.dest, this.outBuffPosn, this.decodabet);
                        this.b4Posn = 0;
                        return value != 61;
                    }
                }
                return true;
            }
            throw new IllegalArgumentException("invalid Base64 input character: " + ((int) ((short) (value & 255))) + " (decimal)");
        }

        private static int decode4to3(byte[] src, ByteBuf dest, int destOffset, byte[] decodabet) {
            int decodedValue;
            int decodedValue2;
            byte src0 = src[0];
            byte src1 = src[1];
            byte src2 = src[2];
            if (src2 == 61) {
                try {
                    int decodedValue3 = ((decodabet[src0] & 255) << 2) | ((decodabet[src1] & 255) >>> 4);
                    dest.setByte(destOffset, decodedValue3);
                    return 1;
                } catch (IndexOutOfBoundsException e) {
                    throw new IllegalArgumentException("not encoded in Base64");
                }
            }
            byte src3 = src[3];
            if (src3 == 61) {
                byte b1 = decodabet[src1];
                try {
                    if (dest.order() == ByteOrder.BIG_ENDIAN) {
                        decodedValue2 = ((decodabet[src2] & 252) >>> 2) | ((((decodabet[src0] & Utf8.REPLACEMENT_BYTE) << 2) | ((b1 & 240) >> 4)) << 8) | ((b1 & 15) << 4);
                    } else {
                        decodedValue2 = ((((decodabet[src2] & 252) >>> 2) | ((b1 & 15) << 4)) << 8) | ((decodabet[src0] & Utf8.REPLACEMENT_BYTE) << 2) | ((b1 & 240) >> 4);
                    }
                    dest.setShort(destOffset, decodedValue2);
                    return 2;
                } catch (IndexOutOfBoundsException e2) {
                    throw new IllegalArgumentException("not encoded in Base64");
                }
            }
            try {
                if (dest.order() == ByteOrder.BIG_ENDIAN) {
                    decodedValue = ((decodabet[src0] & Utf8.REPLACEMENT_BYTE) << 18) | ((decodabet[src1] & 255) << 12) | ((decodabet[src2] & 255) << 6) | (decodabet[src3] & 255);
                } else {
                    byte b12 = decodabet[src1];
                    byte b2 = decodabet[src2];
                    decodedValue = ((decodabet[src0] & Utf8.REPLACEMENT_BYTE) << 2) | ((b12 & 15) << 12) | ((b12 & 240) >>> 4) | ((b2 & 3) << 22) | ((b2 & 252) << 6) | ((decodabet[src3] & 255) << 16);
                }
                dest.setMedium(destOffset, decodedValue);
                return 3;
            } catch (IndexOutOfBoundsException e3) {
                throw new IllegalArgumentException("not encoded in Base64");
            }
        }
    }

    private Base64() {
    }
}
