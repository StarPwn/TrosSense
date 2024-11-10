package org.cloudburstmc.protocol.common.util;

import io.netty.buffer.ByteBuf;

/* loaded from: classes5.dex */
public final class VarInts {
    private VarInts() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void writeInt(ByteBuf buffer, int value) {
        encode(buffer, ((value << 1) ^ (value >> 31)) & 4294967295L);
    }

    public static int readInt(ByteBuf buffer) {
        int n = (int) decode(buffer, 32);
        return (n >>> 1) ^ (-(n & 1));
    }

    public static void writeUnsignedInt(ByteBuf buffer, int value) {
        encode(buffer, value & 4294967295L);
    }

    public static int readUnsignedInt(ByteBuf buffer) {
        return (int) decode(buffer, 32);
    }

    public static void writeLong(ByteBuf buffer, long value) {
        encode(buffer, (value << 1) ^ (value >> 63));
    }

    public static long readLong(ByteBuf buffer) {
        long n = decode(buffer, 64);
        return (n >>> 1) ^ (-(1 & n));
    }

    public static void writeUnsignedLong(ByteBuf buffer, long value) {
        encode(buffer, value);
    }

    public static long readUnsignedLong(ByteBuf buffer) {
        return decode(buffer, 64);
    }

    private static void encode(ByteBuf buf, long value) {
        if (((-128) & value) == 0) {
            buf.writeByte((byte) value);
        } else if (((-16384) & value) == 0) {
            int w = (int) ((((127 & value) | 128) << 8) | (value >>> 7));
            buf.writeShort(w);
        } else {
            encodeFull(buf, value);
        }
    }

    private static void encodeFull(ByteBuf buf, long value) {
        if (((-128) & value) == 0) {
            buf.writeByte((byte) value);
            return;
        }
        if (((-16384) & value) == 0) {
            int w = (int) ((value >>> 7) | (((value & 127) | 128) << 8));
            buf.writeShort(w);
            return;
        }
        if (((-2097152) & value) == 0) {
            int w2 = (int) (((((value >>> 7) & 127) | 128) << 8) | (((value & 127) | 128) << 16) | (value >>> 14));
            buf.writeMedium(w2);
            return;
        }
        if (((-268435456) & value) == 0) {
            int w3 = (int) (((((value >>> 7) & 127) | 128) << 16) | (((value & 127) | 128) << 24) | ((((value >>> 14) & 127) | 128) << 8) | (value >>> 21));
            buf.writeInt(w3);
            return;
        }
        if ((value & (-34359738368L)) == 0) {
            int w4 = (int) (((((value >>> 7) & 127) | 128) << 16) | (((value & 127) | 128) << 24) | ((((value >>> 14) & 127) | 128) << 8) | ((value >>> 21) & 127) | 128);
            buf.writeInt(w4);
            buf.writeByte((int) (value >>> 28));
            return;
        }
        if ((value & (-4398046511104L)) == 0) {
            int w5 = (int) (((((value >>> 7) & 127) | 128) << 16) | (((value & 127) | 128) << 24) | ((((value >>> 14) & 127) | 128) << 8) | ((value >>> 21) & 127) | 128);
            int w22 = (int) (((((value >>> 28) & 127) | 128) << 8) | (value >>> 35));
            buf.writeInt(w5);
            buf.writeShort(w22);
            return;
        }
        if ((value & (-562949953421312L)) == 0) {
            int w6 = (int) (((((value >>> 7) & 127) | 128) << 16) | (((value & 127) | 128) << 24) | ((((value >>> 14) & 127) | 128) << 8) | ((value >>> 21) & 127) | 128);
            int w23 = (int) (((((value >>> 28) & 127) | 128) << 16) | ((128 | (127 & (value >>> 35))) << 8) | (value >>> 42));
            buf.writeInt(w6);
            buf.writeMedium(w23);
            return;
        }
        if ((value & (-72057594037927936L)) == 0) {
            long w7 = ((((value >>> 7) & 127) | 128) << 48) | (((value & 127) | 128) << 56) | ((((value >>> 14) & 127) | 128) << 40) | ((((value >>> 21) & 127) | 128) << 32) | ((((value >>> 28) & 127) | 128) << 24) | ((((value >>> 35) & 127) | 128) << 16) | ((((value >>> 42) & 127) | 128) << 8) | (value >>> 49);
            buf.writeLong(w7);
        } else {
            if ((value & Long.MIN_VALUE) == 0) {
                long w8 = ((((value >>> 7) & 127) | 128) << 48) | (((value & 127) | 128) << 56) | ((((value >>> 14) & 127) | 128) << 40) | ((((value >>> 21) & 127) | 128) << 32) | ((((value >>> 28) & 127) | 128) << 24) | ((((value >>> 35) & 127) | 128) << 16) | ((((value >>> 42) & 127) | 128) << 8) | ((value >>> 49) & 127) | 128;
                buf.writeLong(w8);
                buf.writeByte((byte) (value >>> 56));
                return;
            }
            long w9 = ((((value >>> 7) & 127) | 128) << 48) | (((value & 127) | 128) << 56) | ((((value >>> 14) & 127) | 128) << 40) | ((((value >>> 21) & 127) | 128) << 32) | ((((value >>> 28) & 127) | 128) << 24) | ((((value >>> 35) & 127) | 128) << 16) | ((((value >>> 42) & 127) | 128) << 8) | ((value >>> 49) & 127) | 128;
            long w24 = ((((value >>> 56) & 127) | 128) << 8) | (value >>> 63);
            buf.writeLong(w9);
            buf.writeShort((int) w24);
        }
    }

    private static long decode(ByteBuf buf, int maxBits) {
        long result = 0;
        for (int shift = 0; shift < maxBits; shift += 7) {
            byte b = buf.readByte();
            result |= (b & 127) << shift;
            if ((b & 128) == 0) {
                return result;
            }
        }
        throw new ArithmeticException("VarInt was too large");
    }
}
