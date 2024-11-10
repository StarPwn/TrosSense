package com.google.common.hash;

import com.google.common.hash.AbstractStreamingHashFunction;
import com.google.common.primitives.UnsignedBytes;
import java.io.Serializable;
import java.nio.ByteBuffer;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
final class Murmur3_32HashFunction extends AbstractStreamingHashFunction implements Serializable {
    private static final int C1 = -862048943;
    private static final int C2 = 461845907;
    private static final long serialVersionUID = 0;
    private final int seed;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Murmur3_32HashFunction(int seed) {
        this.seed = seed;
    }

    @Override // com.google.common.hash.HashFunction
    public int bits() {
        return 32;
    }

    @Override // com.google.common.hash.HashFunction
    public Hasher newHasher() {
        return new Murmur3_32Hasher(this.seed);
    }

    public String toString() {
        return "Hashing.murmur3_32(" + this.seed + ")";
    }

    public boolean equals(@Nullable Object object) {
        if (!(object instanceof Murmur3_32HashFunction)) {
            return false;
        }
        Murmur3_32HashFunction other = (Murmur3_32HashFunction) object;
        return this.seed == other.seed;
    }

    public int hashCode() {
        return getClass().hashCode() ^ this.seed;
    }

    @Override // com.google.common.hash.AbstractStreamingHashFunction, com.google.common.hash.HashFunction
    public HashCode hashInt(int input) {
        int k1 = mixK1(input);
        int h1 = mixH1(this.seed, k1);
        return fmix(h1, 4);
    }

    @Override // com.google.common.hash.AbstractStreamingHashFunction, com.google.common.hash.HashFunction
    public HashCode hashLong(long input) {
        int low = (int) input;
        int high = (int) (input >>> 32);
        int k1 = mixK1(low);
        int h1 = mixH1(this.seed, k1);
        int k12 = mixK1(high);
        return fmix(mixH1(h1, k12), 8);
    }

    @Override // com.google.common.hash.AbstractStreamingHashFunction, com.google.common.hash.HashFunction
    public HashCode hashUnencodedChars(CharSequence input) {
        int h1 = this.seed;
        for (int i = 1; i < input.length(); i += 2) {
            int k1 = input.charAt(i - 1) | (input.charAt(i) << 16);
            h1 = mixH1(h1, mixK1(k1));
        }
        int i2 = input.length();
        if ((i2 & 1) == 1) {
            int k12 = input.charAt(input.length() - 1);
            h1 ^= mixK1(k12);
        }
        int k13 = input.length();
        return fmix(h1, k13 * 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int mixK1(int k1) {
        return Integer.rotateLeft(k1 * C1, 15) * C2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int mixH1(int h1, int k1) {
        return (Integer.rotateLeft(h1 ^ k1, 13) * 5) - 430675100;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static HashCode fmix(int h1, int length) {
        int h12 = h1 ^ length;
        int h13 = (h12 ^ (h12 >>> 16)) * (-2048144789);
        int h14 = (h13 ^ (h13 >>> 13)) * (-1028477387);
        return HashCode.fromInt(h14 ^ (h14 >>> 16));
    }

    /* loaded from: classes.dex */
    private static final class Murmur3_32Hasher extends AbstractStreamingHashFunction.AbstractStreamingHasher {
        private static final int CHUNK_SIZE = 4;
        private int h1;
        private int length;

        Murmur3_32Hasher(int seed) {
            super(4);
            this.h1 = seed;
            this.length = 0;
        }

        @Override // com.google.common.hash.AbstractStreamingHashFunction.AbstractStreamingHasher
        protected void process(ByteBuffer bb) {
            int k1 = Murmur3_32HashFunction.mixK1(bb.getInt());
            this.h1 = Murmur3_32HashFunction.mixH1(this.h1, k1);
            this.length += 4;
        }

        @Override // com.google.common.hash.AbstractStreamingHashFunction.AbstractStreamingHasher
        protected void processRemaining(ByteBuffer bb) {
            this.length += bb.remaining();
            int k1 = 0;
            int i = 0;
            while (bb.hasRemaining()) {
                k1 ^= UnsignedBytes.toInt(bb.get()) << i;
                i += 8;
            }
            int i2 = this.h1;
            this.h1 = i2 ^ Murmur3_32HashFunction.mixK1(k1);
        }

        @Override // com.google.common.hash.AbstractStreamingHashFunction.AbstractStreamingHasher
        public HashCode makeHash() {
            return Murmur3_32HashFunction.fmix(this.h1, this.length);
        }
    }
}
