package io.airlift.compress.zstd;

import java.io.IOException;
import java.io.InputStream;
import sun.misc.Unsafe;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class XxHash64 {
    private static final long BUFFER_ADDRESS = Unsafe.ARRAY_BYTE_BASE_OFFSET;
    private static final long DEFAULT_SEED = 0;
    private static final long PRIME64_1 = -7046029288634856825L;
    private static final long PRIME64_2 = -4417276706812531889L;
    private static final long PRIME64_3 = 1609587929392839161L;
    private static final long PRIME64_4 = -8796714831421723037L;
    private static final long PRIME64_5 = 2870177450012600261L;
    private long bodyLength;
    private final byte[] buffer;
    private int bufferSize;
    private final long seed;
    private long v1;
    private long v2;
    private long v3;
    private long v4;

    public XxHash64() {
        this(0L);
    }

    private XxHash64(long seed) {
        this.buffer = new byte[32];
        this.seed = seed;
        this.v1 = seed + PRIME64_1 + PRIME64_2;
        this.v2 = PRIME64_2 + seed;
        this.v3 = seed;
        this.v4 = seed - PRIME64_1;
    }

    public XxHash64 update(byte[] data) {
        return update(data, 0, data.length);
    }

    public XxHash64 update(byte[] data, int offset, int length) {
        Util.checkPositionIndexes(offset, offset + length, data.length);
        updateHash(data, Unsafe.ARRAY_BYTE_BASE_OFFSET + offset, length);
        return this;
    }

    public long hash() {
        long hash;
        if (this.bodyLength > 0) {
            hash = computeBody();
        } else {
            long hash2 = this.seed;
            hash = hash2 + PRIME64_5;
        }
        return updateTail(hash + this.bodyLength + this.bufferSize, this.buffer, BUFFER_ADDRESS, 0, this.bufferSize);
    }

    private long computeBody() {
        long hash = Long.rotateLeft(this.v1, 1) + Long.rotateLeft(this.v2, 7) + Long.rotateLeft(this.v3, 12) + Long.rotateLeft(this.v4, 18);
        return update(update(update(update(hash, this.v1), this.v2), this.v3), this.v4);
    }

    private void updateHash(Object base, long address, int length) {
        if (this.bufferSize > 0) {
            int available = Math.min(32 - this.bufferSize, length);
            UnsafeUtil.UNSAFE.copyMemory(base, address, this.buffer, this.bufferSize + BUFFER_ADDRESS, available);
            this.bufferSize += available;
            address += available;
            length -= available;
            if (this.bufferSize == 32) {
                updateBody(this.buffer, BUFFER_ADDRESS, this.bufferSize);
                this.bufferSize = 0;
            }
        }
        if (length >= 32) {
            int index = updateBody(base, address, length);
            address += index;
            length -= index;
        }
        if (length > 0) {
            UnsafeUtil.UNSAFE.copyMemory(base, address, this.buffer, BUFFER_ADDRESS, length);
            this.bufferSize = length;
        }
    }

    private int updateBody(Object base, long address, int length) {
        int remaining = length;
        while (remaining >= 32) {
            this.v1 = mix(this.v1, UnsafeUtil.UNSAFE.getLong(base, address));
            this.v2 = mix(this.v2, UnsafeUtil.UNSAFE.getLong(base, 8 + address));
            this.v3 = mix(this.v3, UnsafeUtil.UNSAFE.getLong(base, 16 + address));
            this.v4 = mix(this.v4, UnsafeUtil.UNSAFE.getLong(base, 24 + address));
            address += 32;
            remaining -= 32;
        }
        int index = length - remaining;
        this.bodyLength += index;
        return index;
    }

    public static long hash(long value) {
        long hash = updateTail(2870177450012600269L, value);
        return finalShuffle(hash);
    }

    public static long hash(InputStream in) throws IOException {
        return hash(0L, in);
    }

    public static long hash(long seed, InputStream in) throws IOException {
        XxHash64 hash = new XxHash64(seed);
        byte[] buffer = new byte[8192];
        while (true) {
            int length = in.read(buffer);
            if (length != -1) {
                hash.update(buffer, 0, length);
            } else {
                return hash.hash();
            }
        }
    }

    public static long hash(long seed, Object base, long address, int length) {
        long hash;
        if (length >= 32) {
            hash = updateBody(seed, base, address, length);
        } else {
            hash = PRIME64_5 + seed;
        }
        int index = length & (-32);
        return updateTail(hash + length, base, address, index, length);
    }

    private static long updateTail(long hash, Object base, long address, int index, int length) {
        while (index <= length - 8) {
            hash = updateTail(hash, UnsafeUtil.UNSAFE.getLong(base, index + address));
            index += 8;
        }
        if (index <= length - 4) {
            hash = updateTail(hash, UnsafeUtil.UNSAFE.getInt(base, index + address));
            index += 4;
        }
        while (index < length) {
            hash = updateTail(hash, UnsafeUtil.UNSAFE.getByte(base, index + address));
            index++;
        }
        return finalShuffle(hash);
    }

    private static long updateBody(long seed, Object base, long address, int length) {
        long v1 = seed + PRIME64_1 + PRIME64_2;
        long v2 = seed + PRIME64_2;
        long v4 = seed - PRIME64_1;
        long v3 = seed;
        long v22 = v2;
        long v12 = v1;
        long v42 = v4;
        long v43 = address;
        for (int remaining = length; remaining >= 32; remaining -= 32) {
            v12 = mix(v12, UnsafeUtil.UNSAFE.getLong(base, v43));
            v22 = mix(v22, UnsafeUtil.UNSAFE.getLong(base, 8 + v43));
            v3 = mix(v3, UnsafeUtil.UNSAFE.getLong(base, 16 + v43));
            v42 = mix(v42, UnsafeUtil.UNSAFE.getLong(base, 24 + v43));
            v43 += 32;
        }
        long hash = Long.rotateLeft(v12, 1) + Long.rotateLeft(v22, 7) + Long.rotateLeft(v3, 12) + Long.rotateLeft(v42, 18);
        return update(update(update(update(hash, v12), v22), v3), v42);
    }

    private static long mix(long current, long value) {
        return Long.rotateLeft((PRIME64_2 * value) + current, 31) * PRIME64_1;
    }

    private static long update(long hash, long value) {
        long temp = mix(0L, value) ^ hash;
        return (PRIME64_1 * temp) + PRIME64_4;
    }

    private static long updateTail(long hash, long value) {
        long temp = mix(0L, value) ^ hash;
        return (Long.rotateLeft(temp, 27) * PRIME64_1) + PRIME64_4;
    }

    private static long updateTail(long hash, int value) {
        long unsigned = value & 4294967295L;
        long temp = (PRIME64_1 * unsigned) ^ hash;
        return (Long.rotateLeft(temp, 23) * PRIME64_2) + PRIME64_3;
    }

    private static long updateTail(long hash, byte value) {
        int unsigned = value & 255;
        long temp = (unsigned * PRIME64_5) ^ hash;
        return Long.rotateLeft(temp, 11) * PRIME64_1;
    }

    private static long finalShuffle(long hash) {
        long hash2 = (hash ^ (hash >>> 33)) * PRIME64_2;
        long hash3 = (hash2 ^ (hash2 >>> 29)) * PRIME64_3;
        return hash3 ^ (hash3 >>> 32);
    }
}
