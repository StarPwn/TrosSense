package com.google.common.hash;

import com.google.common.hash.AbstractStreamingHashFunction;
import com.google.common.primitives.UnsignedBytes;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
final class Murmur3_128HashFunction extends AbstractStreamingHashFunction implements Serializable {
    private static final long serialVersionUID = 0;
    private final int seed;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Murmur3_128HashFunction(int seed) {
        this.seed = seed;
    }

    @Override // com.google.common.hash.HashFunction
    public int bits() {
        return 128;
    }

    @Override // com.google.common.hash.HashFunction
    public Hasher newHasher() {
        return new Murmur3_128Hasher(this.seed);
    }

    public String toString() {
        return "Hashing.murmur3_128(" + this.seed + ")";
    }

    public boolean equals(@Nullable Object object) {
        if (!(object instanceof Murmur3_128HashFunction)) {
            return false;
        }
        Murmur3_128HashFunction other = (Murmur3_128HashFunction) object;
        return this.seed == other.seed;
    }

    public int hashCode() {
        return getClass().hashCode() ^ this.seed;
    }

    /* loaded from: classes.dex */
    private static final class Murmur3_128Hasher extends AbstractStreamingHashFunction.AbstractStreamingHasher {
        private static final long C1 = -8663945395140668459L;
        private static final long C2 = 5545529020109919103L;
        private static final int CHUNK_SIZE = 16;
        private long h1;
        private long h2;
        private int length;

        Murmur3_128Hasher(int seed) {
            super(16);
            this.h1 = seed;
            this.h2 = seed;
            this.length = 0;
        }

        @Override // com.google.common.hash.AbstractStreamingHashFunction.AbstractStreamingHasher
        protected void process(ByteBuffer bb) {
            long k1 = bb.getLong();
            long k2 = bb.getLong();
            bmix64(k1, k2);
            this.length += 16;
        }

        private void bmix64(long k1, long k2) {
            this.h1 ^= mixK1(k1);
            this.h1 = Long.rotateLeft(this.h1, 27);
            this.h1 += this.h2;
            this.h1 = (this.h1 * 5) + 1390208809;
            this.h2 ^= mixK2(k2);
            this.h2 = Long.rotateLeft(this.h2, 31);
            this.h2 += this.h1;
            this.h2 = (this.h2 * 5) + 944331445;
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Failed to find 'out' block for switch in B:2:0x001d. Please report as an issue. */
        @Override // com.google.common.hash.AbstractStreamingHashFunction.AbstractStreamingHasher
        protected void processRemaining(ByteBuffer bb) {
            long k1;
            long k12 = 0;
            long k2 = 0;
            this.length += bb.remaining();
            switch (bb.remaining()) {
                case 1:
                    k1 = k12 ^ UnsignedBytes.toInt(bb.get(0));
                    this.h1 ^= mixK1(k1);
                    this.h2 ^= mixK2(k2);
                    return;
                case 2:
                    k12 ^= UnsignedBytes.toInt(bb.get(1)) << 8;
                    k1 = k12 ^ UnsignedBytes.toInt(bb.get(0));
                    this.h1 ^= mixK1(k1);
                    this.h2 ^= mixK2(k2);
                    return;
                case 3:
                    k12 ^= UnsignedBytes.toInt(bb.get(2)) << 16;
                    k12 ^= UnsignedBytes.toInt(bb.get(1)) << 8;
                    k1 = k12 ^ UnsignedBytes.toInt(bb.get(0));
                    this.h1 ^= mixK1(k1);
                    this.h2 ^= mixK2(k2);
                    return;
                case 4:
                    k12 ^= UnsignedBytes.toInt(bb.get(3)) << 24;
                    k12 ^= UnsignedBytes.toInt(bb.get(2)) << 16;
                    k12 ^= UnsignedBytes.toInt(bb.get(1)) << 8;
                    k1 = k12 ^ UnsignedBytes.toInt(bb.get(0));
                    this.h1 ^= mixK1(k1);
                    this.h2 ^= mixK2(k2);
                    return;
                case 5:
                    k12 ^= UnsignedBytes.toInt(bb.get(4)) << 32;
                    k12 ^= UnsignedBytes.toInt(bb.get(3)) << 24;
                    k12 ^= UnsignedBytes.toInt(bb.get(2)) << 16;
                    k12 ^= UnsignedBytes.toInt(bb.get(1)) << 8;
                    k1 = k12 ^ UnsignedBytes.toInt(bb.get(0));
                    this.h1 ^= mixK1(k1);
                    this.h2 ^= mixK2(k2);
                    return;
                case 6:
                    k12 ^= UnsignedBytes.toInt(bb.get(5)) << 40;
                    k12 ^= UnsignedBytes.toInt(bb.get(4)) << 32;
                    k12 ^= UnsignedBytes.toInt(bb.get(3)) << 24;
                    k12 ^= UnsignedBytes.toInt(bb.get(2)) << 16;
                    k12 ^= UnsignedBytes.toInt(bb.get(1)) << 8;
                    k1 = k12 ^ UnsignedBytes.toInt(bb.get(0));
                    this.h1 ^= mixK1(k1);
                    this.h2 ^= mixK2(k2);
                    return;
                case 7:
                    k12 = 0 ^ (UnsignedBytes.toInt(bb.get(6)) << 48);
                    k12 ^= UnsignedBytes.toInt(bb.get(5)) << 40;
                    k12 ^= UnsignedBytes.toInt(bb.get(4)) << 32;
                    k12 ^= UnsignedBytes.toInt(bb.get(3)) << 24;
                    k12 ^= UnsignedBytes.toInt(bb.get(2)) << 16;
                    k12 ^= UnsignedBytes.toInt(bb.get(1)) << 8;
                    k1 = k12 ^ UnsignedBytes.toInt(bb.get(0));
                    this.h1 ^= mixK1(k1);
                    this.h2 ^= mixK2(k2);
                    return;
                case 8:
                    k1 = 0 ^ bb.getLong();
                    this.h1 ^= mixK1(k1);
                    this.h2 ^= mixK2(k2);
                    return;
                case 9:
                    k2 ^= UnsignedBytes.toInt(bb.get(8));
                    k1 = 0 ^ bb.getLong();
                    this.h1 ^= mixK1(k1);
                    this.h2 ^= mixK2(k2);
                    return;
                case 10:
                    k2 ^= UnsignedBytes.toInt(bb.get(9)) << 8;
                    k2 ^= UnsignedBytes.toInt(bb.get(8));
                    k1 = 0 ^ bb.getLong();
                    this.h1 ^= mixK1(k1);
                    this.h2 ^= mixK2(k2);
                    return;
                case 11:
                    k2 ^= UnsignedBytes.toInt(bb.get(10)) << 16;
                    k2 ^= UnsignedBytes.toInt(bb.get(9)) << 8;
                    k2 ^= UnsignedBytes.toInt(bb.get(8));
                    k1 = 0 ^ bb.getLong();
                    this.h1 ^= mixK1(k1);
                    this.h2 ^= mixK2(k2);
                    return;
                case 12:
                    k2 ^= UnsignedBytes.toInt(bb.get(11)) << 24;
                    k2 ^= UnsignedBytes.toInt(bb.get(10)) << 16;
                    k2 ^= UnsignedBytes.toInt(bb.get(9)) << 8;
                    k2 ^= UnsignedBytes.toInt(bb.get(8));
                    k1 = 0 ^ bb.getLong();
                    this.h1 ^= mixK1(k1);
                    this.h2 ^= mixK2(k2);
                    return;
                case 13:
                    k2 ^= UnsignedBytes.toInt(bb.get(12)) << 32;
                    k2 ^= UnsignedBytes.toInt(bb.get(11)) << 24;
                    k2 ^= UnsignedBytes.toInt(bb.get(10)) << 16;
                    k2 ^= UnsignedBytes.toInt(bb.get(9)) << 8;
                    k2 ^= UnsignedBytes.toInt(bb.get(8));
                    k1 = 0 ^ bb.getLong();
                    this.h1 ^= mixK1(k1);
                    this.h2 ^= mixK2(k2);
                    return;
                case 14:
                    k2 ^= UnsignedBytes.toInt(bb.get(13)) << 40;
                    k2 ^= UnsignedBytes.toInt(bb.get(12)) << 32;
                    k2 ^= UnsignedBytes.toInt(bb.get(11)) << 24;
                    k2 ^= UnsignedBytes.toInt(bb.get(10)) << 16;
                    k2 ^= UnsignedBytes.toInt(bb.get(9)) << 8;
                    k2 ^= UnsignedBytes.toInt(bb.get(8));
                    k1 = 0 ^ bb.getLong();
                    this.h1 ^= mixK1(k1);
                    this.h2 ^= mixK2(k2);
                    return;
                case 15:
                    k2 = 0 ^ (UnsignedBytes.toInt(bb.get(14)) << 48);
                    k2 ^= UnsignedBytes.toInt(bb.get(13)) << 40;
                    k2 ^= UnsignedBytes.toInt(bb.get(12)) << 32;
                    k2 ^= UnsignedBytes.toInt(bb.get(11)) << 24;
                    k2 ^= UnsignedBytes.toInt(bb.get(10)) << 16;
                    k2 ^= UnsignedBytes.toInt(bb.get(9)) << 8;
                    k2 ^= UnsignedBytes.toInt(bb.get(8));
                    k1 = 0 ^ bb.getLong();
                    this.h1 ^= mixK1(k1);
                    this.h2 ^= mixK2(k2);
                    return;
                default:
                    throw new AssertionError("Should never get here.");
            }
        }

        @Override // com.google.common.hash.AbstractStreamingHashFunction.AbstractStreamingHasher
        public HashCode makeHash() {
            this.h1 ^= this.length;
            this.h2 ^= this.length;
            this.h1 += this.h2;
            this.h2 += this.h1;
            this.h1 = fmix64(this.h1);
            this.h2 = fmix64(this.h2);
            this.h1 += this.h2;
            this.h2 += this.h1;
            return HashCode.fromBytesNoCopy(ByteBuffer.wrap(new byte[16]).order(ByteOrder.LITTLE_ENDIAN).putLong(this.h1).putLong(this.h2).array());
        }

        private static long fmix64(long k) {
            long k2 = (k ^ (k >>> 33)) * (-49064778989728563L);
            long k3 = (k2 ^ (k2 >>> 33)) * (-4265267296055464877L);
            return k3 ^ (k3 >>> 33);
        }

        private static long mixK1(long k1) {
            return Long.rotateLeft(k1 * C1, 31) * C2;
        }

        private static long mixK2(long k2) {
            return Long.rotateLeft(k2 * C2, 33) * C1;
        }
    }
}
