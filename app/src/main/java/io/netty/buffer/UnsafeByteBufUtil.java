package io.netty.buffer;

import io.netty.handler.codec.http2.Http2CodecUtil;
import io.netty.util.internal.MathUtil;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;
import kotlin.UShort;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class UnsafeByteBufUtil {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int MAX_HAND_ROLLED_SET_ZERO_BYTES = 64;
    private static final boolean UNALIGNED = PlatformDependent.isUnaligned();
    private static final byte ZERO = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte getByte(long address) {
        return PlatformDependent.getByte(address);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static short getShort(long address) {
        if (UNALIGNED) {
            short v = PlatformDependent.getShort(address);
            return PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? v : Short.reverseBytes(v);
        }
        return (short) ((PlatformDependent.getByte(address) << 8) | (PlatformDependent.getByte(1 + address) & 255));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static short getShortLE(long address) {
        if (UNALIGNED) {
            short v = PlatformDependent.getShort(address);
            return PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? Short.reverseBytes(v) : v;
        }
        return (short) ((PlatformDependent.getByte(address) & Http2CodecUtil.MAX_UNSIGNED_BYTE) | (PlatformDependent.getByte(1 + address) << 8));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getUnsignedMedium(long address) {
        short reverseBytes;
        if (UNALIGNED) {
            int i = (PlatformDependent.getByte(address) & 255) << 16;
            if (PlatformDependent.BIG_ENDIAN_NATIVE_ORDER) {
                reverseBytes = PlatformDependent.getShort(1 + address);
            } else {
                reverseBytes = Short.reverseBytes(PlatformDependent.getShort(1 + address));
            }
            return i | (reverseBytes & UShort.MAX_VALUE);
        }
        return ((PlatformDependent.getByte(address) & 255) << 16) | ((PlatformDependent.getByte(1 + address) & 255) << 8) | (PlatformDependent.getByte(2 + address) & 255);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getUnsignedMediumLE(long address) {
        short s;
        if (UNALIGNED) {
            int i = PlatformDependent.getByte(address) & 255;
            if (PlatformDependent.BIG_ENDIAN_NATIVE_ORDER) {
                s = Short.reverseBytes(PlatformDependent.getShort(1 + address));
            } else {
                s = PlatformDependent.getShort(1 + address);
            }
            return i | ((s & UShort.MAX_VALUE) << 8);
        }
        return (PlatformDependent.getByte(address) & 255) | ((PlatformDependent.getByte(1 + address) & 255) << 8) | ((PlatformDependent.getByte(2 + address) & 255) << 16);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getInt(long address) {
        if (UNALIGNED) {
            int v = PlatformDependent.getInt(address);
            return PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? v : Integer.reverseBytes(v);
        }
        return (PlatformDependent.getByte(address) << 24) | ((PlatformDependent.getByte(1 + address) & 255) << 16) | ((PlatformDependent.getByte(2 + address) & 255) << 8) | (PlatformDependent.getByte(3 + address) & 255);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getIntLE(long address) {
        if (UNALIGNED) {
            int v = PlatformDependent.getInt(address);
            return PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? Integer.reverseBytes(v) : v;
        }
        return (PlatformDependent.getByte(address) & 255) | ((PlatformDependent.getByte(1 + address) & 255) << 8) | ((PlatformDependent.getByte(2 + address) & 255) << 16) | (PlatformDependent.getByte(3 + address) << 24);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long getLong(long address) {
        if (UNALIGNED) {
            long v = PlatformDependent.getLong(address);
            return PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? v : Long.reverseBytes(v);
        }
        return (PlatformDependent.getByte(address) << 56) | ((PlatformDependent.getByte(1 + address) & 255) << 48) | ((PlatformDependent.getByte(2 + address) & 255) << 40) | ((PlatformDependent.getByte(3 + address) & 255) << 32) | ((PlatformDependent.getByte(4 + address) & 255) << 24) | ((PlatformDependent.getByte(5 + address) & 255) << 16) | ((PlatformDependent.getByte(6 + address) & 255) << 8) | (PlatformDependent.getByte(7 + address) & 255);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long getLongLE(long address) {
        if (UNALIGNED) {
            long v = PlatformDependent.getLong(address);
            return PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? Long.reverseBytes(v) : v;
        }
        return (PlatformDependent.getByte(address) & 255) | ((PlatformDependent.getByte(1 + address) & 255) << 8) | ((PlatformDependent.getByte(2 + address) & 255) << 16) | ((PlatformDependent.getByte(3 + address) & 255) << 24) | ((PlatformDependent.getByte(4 + address) & 255) << 32) | ((PlatformDependent.getByte(5 + address) & 255) << 40) | ((255 & PlatformDependent.getByte(6 + address)) << 48) | (PlatformDependent.getByte(7 + address) << 56);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setByte(long address, int value) {
        PlatformDependent.putByte(address, (byte) value);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setShort(long address, int value) {
        if (UNALIGNED) {
            PlatformDependent.putShort(address, PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? (short) value : Short.reverseBytes((short) value));
        } else {
            PlatformDependent.putByte(address, (byte) (value >>> 8));
            PlatformDependent.putByte(1 + address, (byte) value);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setShortLE(long address, int value) {
        if (UNALIGNED) {
            PlatformDependent.putShort(address, PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? Short.reverseBytes((short) value) : (short) value);
        } else {
            PlatformDependent.putByte(address, (byte) value);
            PlatformDependent.putByte(1 + address, (byte) (value >>> 8));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setMedium(long address, int value) {
        PlatformDependent.putByte(address, (byte) (value >>> 16));
        if (UNALIGNED) {
            PlatformDependent.putShort(1 + address, PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? (short) value : Short.reverseBytes((short) value));
        } else {
            PlatformDependent.putByte(1 + address, (byte) (value >>> 8));
            PlatformDependent.putByte(2 + address, (byte) value);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setMediumLE(long address, int value) {
        PlatformDependent.putByte(address, (byte) value);
        if (UNALIGNED) {
            PlatformDependent.putShort(1 + address, PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? Short.reverseBytes((short) (value >>> 8)) : (short) (value >>> 8));
        } else {
            PlatformDependent.putByte(1 + address, (byte) (value >>> 8));
            PlatformDependent.putByte(2 + address, (byte) (value >>> 16));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setInt(long address, int value) {
        if (UNALIGNED) {
            PlatformDependent.putInt(address, PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? value : Integer.reverseBytes(value));
            return;
        }
        PlatformDependent.putByte(address, (byte) (value >>> 24));
        PlatformDependent.putByte(1 + address, (byte) (value >>> 16));
        PlatformDependent.putByte(2 + address, (byte) (value >>> 8));
        PlatformDependent.putByte(3 + address, (byte) value);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setIntLE(long address, int value) {
        if (UNALIGNED) {
            PlatformDependent.putInt(address, PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? Integer.reverseBytes(value) : value);
            return;
        }
        PlatformDependent.putByte(address, (byte) value);
        PlatformDependent.putByte(1 + address, (byte) (value >>> 8));
        PlatformDependent.putByte(2 + address, (byte) (value >>> 16));
        PlatformDependent.putByte(3 + address, (byte) (value >>> 24));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setLong(long address, long value) {
        if (UNALIGNED) {
            PlatformDependent.putLong(address, PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? value : Long.reverseBytes(value));
            return;
        }
        PlatformDependent.putByte(address, (byte) (value >>> 56));
        PlatformDependent.putByte(1 + address, (byte) (value >>> 48));
        PlatformDependent.putByte(2 + address, (byte) (value >>> 40));
        PlatformDependent.putByte(3 + address, (byte) (value >>> 32));
        PlatformDependent.putByte(4 + address, (byte) (value >>> 24));
        PlatformDependent.putByte(5 + address, (byte) (value >>> 16));
        PlatformDependent.putByte(6 + address, (byte) (value >>> 8));
        PlatformDependent.putByte(7 + address, (byte) value);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setLongLE(long address, long value) {
        if (UNALIGNED) {
            PlatformDependent.putLong(address, PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? Long.reverseBytes(value) : value);
            return;
        }
        PlatformDependent.putByte(address, (byte) value);
        PlatformDependent.putByte(1 + address, (byte) (value >>> 8));
        PlatformDependent.putByte(2 + address, (byte) (value >>> 16));
        PlatformDependent.putByte(3 + address, (byte) (value >>> 24));
        PlatformDependent.putByte(4 + address, (byte) (value >>> 32));
        PlatformDependent.putByte(5 + address, (byte) (value >>> 40));
        PlatformDependent.putByte(6 + address, (byte) (value >>> 48));
        PlatformDependent.putByte(7 + address, (byte) (value >>> 56));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte getByte(byte[] array, int index) {
        return PlatformDependent.getByte(array, index);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static short getShort(byte[] array, int index) {
        if (UNALIGNED) {
            short v = PlatformDependent.getShort(array, index);
            return PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? v : Short.reverseBytes(v);
        }
        return (short) ((PlatformDependent.getByte(array, index) << 8) | (PlatformDependent.getByte(array, index + 1) & 255));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static short getShortLE(byte[] array, int index) {
        if (UNALIGNED) {
            short v = PlatformDependent.getShort(array, index);
            return PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? Short.reverseBytes(v) : v;
        }
        return (short) ((PlatformDependent.getByte(array, index) & Http2CodecUtil.MAX_UNSIGNED_BYTE) | (PlatformDependent.getByte(array, index + 1) << 8));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getUnsignedMedium(byte[] array, int index) {
        short reverseBytes;
        if (UNALIGNED) {
            int i = (PlatformDependent.getByte(array, index) & 255) << 16;
            if (PlatformDependent.BIG_ENDIAN_NATIVE_ORDER) {
                reverseBytes = PlatformDependent.getShort(array, index + 1);
            } else {
                reverseBytes = Short.reverseBytes(PlatformDependent.getShort(array, index + 1));
            }
            return i | (reverseBytes & UShort.MAX_VALUE);
        }
        return ((PlatformDependent.getByte(array, index) & 255) << 16) | ((PlatformDependent.getByte(array, index + 1) & 255) << 8) | (PlatformDependent.getByte(array, index + 2) & 255);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getUnsignedMediumLE(byte[] array, int index) {
        short s;
        if (UNALIGNED) {
            int i = PlatformDependent.getByte(array, index) & 255;
            if (PlatformDependent.BIG_ENDIAN_NATIVE_ORDER) {
                s = Short.reverseBytes(PlatformDependent.getShort(array, index + 1));
            } else {
                s = PlatformDependent.getShort(array, index + 1);
            }
            return i | ((s & UShort.MAX_VALUE) << 8);
        }
        return (PlatformDependent.getByte(array, index) & 255) | ((PlatformDependent.getByte(array, index + 1) & 255) << 8) | ((PlatformDependent.getByte(array, index + 2) & 255) << 16);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getInt(byte[] array, int index) {
        if (UNALIGNED) {
            int v = PlatformDependent.getInt(array, index);
            return PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? v : Integer.reverseBytes(v);
        }
        return (PlatformDependent.getByte(array, index) << 24) | ((PlatformDependent.getByte(array, index + 1) & 255) << 16) | ((PlatformDependent.getByte(array, index + 2) & 255) << 8) | (PlatformDependent.getByte(array, index + 3) & 255);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getIntLE(byte[] array, int index) {
        if (UNALIGNED) {
            int v = PlatformDependent.getInt(array, index);
            return PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? Integer.reverseBytes(v) : v;
        }
        return (PlatformDependent.getByte(array, index) & 255) | ((PlatformDependent.getByte(array, index + 1) & 255) << 8) | ((PlatformDependent.getByte(array, index + 2) & 255) << 16) | (PlatformDependent.getByte(array, index + 3) << 24);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long getLong(byte[] array, int index) {
        if (UNALIGNED) {
            long v = PlatformDependent.getLong(array, index);
            return PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? v : Long.reverseBytes(v);
        }
        return (PlatformDependent.getByte(array, index) << 56) | ((PlatformDependent.getByte(array, index + 1) & 255) << 48) | ((PlatformDependent.getByte(array, index + 2) & 255) << 40) | ((PlatformDependent.getByte(array, index + 3) & 255) << 32) | ((PlatformDependent.getByte(array, index + 4) & 255) << 24) | ((PlatformDependent.getByte(array, index + 5) & 255) << 16) | ((PlatformDependent.getByte(array, index + 6) & 255) << 8) | (PlatformDependent.getByte(array, index + 7) & 255);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long getLongLE(byte[] array, int index) {
        if (UNALIGNED) {
            long v = PlatformDependent.getLong(array, index);
            return PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? Long.reverseBytes(v) : v;
        }
        return (PlatformDependent.getByte(array, index) & 255) | ((PlatformDependent.getByte(array, index + 1) & 255) << 8) | ((PlatformDependent.getByte(array, index + 2) & 255) << 16) | ((PlatformDependent.getByte(array, index + 3) & 255) << 24) | ((PlatformDependent.getByte(array, index + 4) & 255) << 32) | ((PlatformDependent.getByte(array, index + 5) & 255) << 40) | ((255 & PlatformDependent.getByte(array, index + 6)) << 48) | (PlatformDependent.getByte(array, index + 7) << 56);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setByte(byte[] array, int index, int value) {
        PlatformDependent.putByte(array, index, (byte) value);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setShort(byte[] array, int index, int value) {
        if (UNALIGNED) {
            PlatformDependent.putShort(array, index, PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? (short) value : Short.reverseBytes((short) value));
        } else {
            PlatformDependent.putByte(array, index, (byte) (value >>> 8));
            PlatformDependent.putByte(array, index + 1, (byte) value);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setShortLE(byte[] array, int index, int value) {
        if (UNALIGNED) {
            PlatformDependent.putShort(array, index, PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? Short.reverseBytes((short) value) : (short) value);
        } else {
            PlatformDependent.putByte(array, index, (byte) value);
            PlatformDependent.putByte(array, index + 1, (byte) (value >>> 8));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setMedium(byte[] array, int index, int value) {
        PlatformDependent.putByte(array, index, (byte) (value >>> 16));
        if (UNALIGNED) {
            PlatformDependent.putShort(array, index + 1, PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? (short) value : Short.reverseBytes((short) value));
        } else {
            PlatformDependent.putByte(array, index + 1, (byte) (value >>> 8));
            PlatformDependent.putByte(array, index + 2, (byte) value);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setMediumLE(byte[] array, int index, int value) {
        PlatformDependent.putByte(array, index, (byte) value);
        if (UNALIGNED) {
            PlatformDependent.putShort(array, index + 1, PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? Short.reverseBytes((short) (value >>> 8)) : (short) (value >>> 8));
        } else {
            PlatformDependent.putByte(array, index + 1, (byte) (value >>> 8));
            PlatformDependent.putByte(array, index + 2, (byte) (value >>> 16));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setInt(byte[] array, int index, int value) {
        if (UNALIGNED) {
            PlatformDependent.putInt(array, index, PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? value : Integer.reverseBytes(value));
            return;
        }
        PlatformDependent.putByte(array, index, (byte) (value >>> 24));
        PlatformDependent.putByte(array, index + 1, (byte) (value >>> 16));
        PlatformDependent.putByte(array, index + 2, (byte) (value >>> 8));
        PlatformDependent.putByte(array, index + 3, (byte) value);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setIntLE(byte[] array, int index, int value) {
        if (UNALIGNED) {
            PlatformDependent.putInt(array, index, PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? Integer.reverseBytes(value) : value);
            return;
        }
        PlatformDependent.putByte(array, index, (byte) value);
        PlatformDependent.putByte(array, index + 1, (byte) (value >>> 8));
        PlatformDependent.putByte(array, index + 2, (byte) (value >>> 16));
        PlatformDependent.putByte(array, index + 3, (byte) (value >>> 24));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setLong(byte[] array, int index, long value) {
        if (UNALIGNED) {
            PlatformDependent.putLong(array, index, PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? value : Long.reverseBytes(value));
            return;
        }
        PlatformDependent.putByte(array, index, (byte) (value >>> 56));
        PlatformDependent.putByte(array, index + 1, (byte) (value >>> 48));
        PlatformDependent.putByte(array, index + 2, (byte) (value >>> 40));
        PlatformDependent.putByte(array, index + 3, (byte) (value >>> 32));
        PlatformDependent.putByte(array, index + 4, (byte) (value >>> 24));
        PlatformDependent.putByte(array, index + 5, (byte) (value >>> 16));
        PlatformDependent.putByte(array, index + 6, (byte) (value >>> 8));
        PlatformDependent.putByte(array, index + 7, (byte) value);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setLongLE(byte[] array, int index, long value) {
        if (UNALIGNED) {
            PlatformDependent.putLong(array, index, PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? Long.reverseBytes(value) : value);
            return;
        }
        PlatformDependent.putByte(array, index, (byte) value);
        PlatformDependent.putByte(array, index + 1, (byte) (value >>> 8));
        PlatformDependent.putByte(array, index + 2, (byte) (value >>> 16));
        PlatformDependent.putByte(array, index + 3, (byte) (value >>> 24));
        PlatformDependent.putByte(array, index + 4, (byte) (value >>> 32));
        PlatformDependent.putByte(array, index + 5, (byte) (value >>> 40));
        PlatformDependent.putByte(array, index + 6, (byte) (value >>> 48));
        PlatformDependent.putByte(array, index + 7, (byte) (value >>> 56));
    }

    private static void batchSetZero(byte[] data, int index, int length) {
        int longBatches = length / 8;
        for (int i = 0; i < longBatches; i++) {
            PlatformDependent.putLong(data, index, 0L);
            index += 8;
        }
        int i2 = length % 8;
        for (int i3 = 0; i3 < i2; i3++) {
            PlatformDependent.putByte(data, index + i3, (byte) 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setZero(byte[] array, int index, int length) {
        if (length == 0) {
            return;
        }
        if (UNALIGNED && length <= 64) {
            batchSetZero(array, index, length);
        } else {
            PlatformDependent.setMemory(array, index, length, (byte) 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ByteBuf copy(AbstractByteBuf buf, long addr, int index, int length) {
        buf.checkIndex(index, length);
        ByteBuf copy = buf.alloc().directBuffer(length, buf.maxCapacity());
        if (length != 0) {
            if (copy.hasMemoryAddress()) {
                PlatformDependent.copyMemory(addr, copy.memoryAddress(), length);
                copy.setIndex(0, length);
            } else {
                copy.writeBytes(buf, index, length);
            }
        }
        return copy;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int setBytes(AbstractByteBuf buf, long addr, int index, InputStream in, int length) throws IOException {
        buf.checkIndex(index, length);
        ByteBuf tmpBuf = buf.alloc().heapBuffer(length);
        try {
            byte[] tmp = tmpBuf.array();
            int offset = tmpBuf.arrayOffset();
            int readBytes = in.read(tmp, offset, length);
            if (readBytes > 0) {
                PlatformDependent.copyMemory(tmp, offset, addr, readBytes);
            }
            return readBytes;
        } finally {
            tmpBuf.release();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void getBytes(AbstractByteBuf buf, long addr, int index, ByteBuf dst, int dstIndex, int length) {
        buf.checkIndex(index, length);
        ObjectUtil.checkNotNull(dst, "dst");
        if (MathUtil.isOutOfBounds(dstIndex, length, dst.capacity())) {
            throw new IndexOutOfBoundsException("dstIndex: " + dstIndex);
        }
        if (dst.hasMemoryAddress()) {
            PlatformDependent.copyMemory(addr, dst.memoryAddress() + dstIndex, length);
        } else if (dst.hasArray()) {
            PlatformDependent.copyMemory(addr, dst.array(), dst.arrayOffset() + dstIndex, length);
        } else {
            dst.setBytes(dstIndex, buf, index, length);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void getBytes(AbstractByteBuf buf, long addr, int index, byte[] dst, int dstIndex, int length) {
        buf.checkIndex(index, length);
        ObjectUtil.checkNotNull(dst, "dst");
        if (MathUtil.isOutOfBounds(dstIndex, length, dst.length)) {
            throw new IndexOutOfBoundsException("dstIndex: " + dstIndex);
        }
        if (length != 0) {
            PlatformDependent.copyMemory(addr, dst, dstIndex, length);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void getBytes(AbstractByteBuf buf, long addr, int index, ByteBuffer dst) {
        buf.checkIndex(index, dst.remaining());
        if (dst.remaining() == 0) {
            return;
        }
        if (dst.isDirect()) {
            if (dst.isReadOnly()) {
                throw new ReadOnlyBufferException();
            }
            long dstAddress = PlatformDependent.directBufferAddress(dst);
            PlatformDependent.copyMemory(addr, dstAddress + dst.position(), dst.remaining());
            dst.position(dst.position() + dst.remaining());
            return;
        }
        if (dst.hasArray()) {
            PlatformDependent.copyMemory(addr, dst.array(), dst.arrayOffset() + dst.position(), dst.remaining());
            dst.position(dst.position() + dst.remaining());
        } else {
            dst.put(buf.nioBuffer());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setBytes(AbstractByteBuf buf, long addr, int index, ByteBuf src, int srcIndex, int length) {
        buf.checkIndex(index, length);
        ObjectUtil.checkNotNull(src, "src");
        if (MathUtil.isOutOfBounds(srcIndex, length, src.capacity())) {
            throw new IndexOutOfBoundsException("srcIndex: " + srcIndex);
        }
        if (length != 0) {
            if (src.hasMemoryAddress()) {
                PlatformDependent.copyMemory(src.memoryAddress() + srcIndex, addr, length);
            } else if (src.hasArray()) {
                PlatformDependent.copyMemory(src.array(), src.arrayOffset() + srcIndex, addr, length);
            } else {
                src.getBytes(srcIndex, buf, index, length);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setBytes(AbstractByteBuf buf, long addr, int index, byte[] src, int srcIndex, int length) {
        buf.checkIndex(index, length);
        ObjectUtil.checkNotNull(src, "src");
        if (MathUtil.isOutOfBounds(srcIndex, length, src.length)) {
            throw new IndexOutOfBoundsException("srcIndex: " + srcIndex);
        }
        if (length != 0) {
            PlatformDependent.copyMemory(src, srcIndex, addr, length);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setBytes(AbstractByteBuf buf, long addr, int index, ByteBuffer src) {
        int length = src.remaining();
        if (length == 0) {
            return;
        }
        if (src.isDirect()) {
            buf.checkIndex(index, length);
            long srcAddress = PlatformDependent.directBufferAddress(src);
            PlatformDependent.copyMemory(srcAddress + src.position(), addr, length);
            src.position(src.position() + length);
            return;
        }
        if (src.hasArray()) {
            buf.checkIndex(index, length);
            PlatformDependent.copyMemory(src.array(), src.arrayOffset() + src.position(), addr, length);
            src.position(src.position() + length);
        } else if (length < 8) {
            setSingleBytes(buf, addr, index, src, length);
        } else {
            if (buf.nioBufferCount() != 1) {
                throw new AssertionError();
            }
            ByteBuffer internalBuffer = buf.internalNioBuffer(index, length);
            internalBuffer.put(src);
        }
    }

    private static void setSingleBytes(AbstractByteBuf buf, long addr, int index, ByteBuffer src, int length) {
        buf.checkIndex(index, length);
        int srcPosition = src.position();
        int srcLimit = src.limit();
        long dstAddr = addr;
        for (int srcIndex = srcPosition; srcIndex < srcLimit; srcIndex++) {
            byte value = src.get(srcIndex);
            PlatformDependent.putByte(dstAddr, value);
            dstAddr++;
        }
        src.position(srcLimit);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void getBytes(AbstractByteBuf buf, long addr, int index, OutputStream out, int length) throws IOException {
        buf.checkIndex(index, length);
        if (length != 0) {
            int len = Math.min(length, 8192);
            if (len <= 1024 || !buf.alloc().isDirectBufferPooled()) {
                getBytes(addr, ByteBufUtil.threadLocalTempArray(len), 0, len, out, length);
                return;
            }
            ByteBuf tmpBuf = buf.alloc().heapBuffer(len);
            try {
                byte[] tmp = tmpBuf.array();
                int offset = tmpBuf.arrayOffset();
                getBytes(addr, tmp, offset, len, out, length);
            } finally {
                tmpBuf.release();
            }
        }
    }

    private static void getBytes(long inAddr, byte[] in, int inOffset, int inLen, OutputStream out, int outLen) throws IOException {
        do {
            int len = Math.min(inLen, outLen);
            PlatformDependent.copyMemory(inAddr, in, inOffset, len);
            out.write(in, inOffset, len);
            outLen -= len;
            inAddr += len;
        } while (outLen > 0);
    }

    private static void batchSetZero(long addr, int length) {
        int longBatches = length / 8;
        for (int i = 0; i < longBatches; i++) {
            PlatformDependent.putLong(addr, 0L);
            addr += 8;
        }
        int i2 = length % 8;
        for (int i3 = 0; i3 < i2; i3++) {
            PlatformDependent.putByte(i3 + addr, (byte) 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setZero(long addr, int length) {
        if (length == 0) {
            return;
        }
        if (length <= 64) {
            if (!UNALIGNED) {
                int bytesToGetAligned = zeroTillAligned(addr, length);
                addr += bytesToGetAligned;
                length -= bytesToGetAligned;
                if (length == 0) {
                    return;
                }
                if (addr % 8 != 0) {
                    throw new AssertionError();
                }
            }
            batchSetZero(addr, length);
            return;
        }
        PlatformDependent.setMemory(addr, length, (byte) 0);
    }

    private static int zeroTillAligned(long addr, int length) {
        int bytesToGetAligned = Math.min((int) (addr % 8), length);
        for (int i = 0; i < bytesToGetAligned; i++) {
            PlatformDependent.putByte(i + addr, (byte) 0);
        }
        return bytesToGetAligned;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static UnpooledUnsafeDirectByteBuf newUnsafeDirectByteBuf(ByteBufAllocator alloc, int initialCapacity, int maxCapacity) {
        if (PlatformDependent.useDirectBufferNoCleaner()) {
            return new UnpooledUnsafeNoCleanerDirectByteBuf(alloc, initialCapacity, maxCapacity);
        }
        return new UnpooledUnsafeDirectByteBuf(alloc, initialCapacity, maxCapacity);
    }

    private UnsafeByteBufUtil() {
    }
}
