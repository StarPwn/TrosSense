package com.google.common.hash;

import com.google.common.base.Preconditions;
import com.google.common.hash.BloomFilter;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import java.math.RoundingMode;
import java.util.Arrays;
import javax.annotation.Nullable;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public enum BloomFilterStrategies implements BloomFilter.Strategy {
    MURMUR128_MITZ_32 { // from class: com.google.common.hash.BloomFilterStrategies.1
        @Override // com.google.common.hash.BloomFilter.Strategy
        public <T> boolean put(T object, Funnel<? super T> funnel, int numHashFunctions, BitArray bits) {
            long bitSize = bits.bitSize();
            long hash64 = Hashing.murmur3_128().hashObject(object, funnel).asLong();
            int hash1 = (int) hash64;
            int hash2 = (int) (hash64 >>> 32);
            boolean bitsChanged = false;
            for (int i = 1; i <= numHashFunctions; i++) {
                int combinedHash = (i * hash2) + hash1;
                if (combinedHash < 0) {
                    combinedHash = ~combinedHash;
                }
                bitsChanged |= bits.set(combinedHash % bitSize);
            }
            return bitsChanged;
        }

        @Override // com.google.common.hash.BloomFilter.Strategy
        public <T> boolean mightContain(T object, Funnel<? super T> funnel, int numHashFunctions, BitArray bits) {
            long bitSize = bits.bitSize();
            long hash64 = Hashing.murmur3_128().hashObject(object, funnel).asLong();
            int hash1 = (int) hash64;
            int hash2 = (int) (hash64 >>> 32);
            for (int i = 1; i <= numHashFunctions; i++) {
                int combinedHash = (i * hash2) + hash1;
                if (combinedHash < 0) {
                    combinedHash = ~combinedHash;
                }
                if (!bits.get(combinedHash % bitSize)) {
                    return false;
                }
            }
            return true;
        }
    },
    MURMUR128_MITZ_64 { // from class: com.google.common.hash.BloomFilterStrategies.2
        @Override // com.google.common.hash.BloomFilter.Strategy
        public <T> boolean put(T object, Funnel<? super T> funnel, int numHashFunctions, BitArray bits) {
            long bitSize = bits.bitSize();
            byte[] bytes = Hashing.murmur3_128().hashObject(object, funnel).getBytesInternal();
            long hash1 = lowerEight(bytes);
            long hash2 = upperEight(bytes);
            boolean bitsChanged = false;
            long combinedHash = hash1;
            int i = 0;
            while (i < numHashFunctions) {
                bitsChanged |= bits.set((Long.MAX_VALUE & combinedHash) % bitSize);
                combinedHash += hash2;
                i++;
                bytes = bytes;
            }
            return bitsChanged;
        }

        @Override // com.google.common.hash.BloomFilter.Strategy
        public <T> boolean mightContain(T object, Funnel<? super T> funnel, int numHashFunctions, BitArray bits) {
            long bitSize = bits.bitSize();
            byte[] bytes = Hashing.murmur3_128().hashObject(object, funnel).getBytesInternal();
            long hash1 = lowerEight(bytes);
            long hash2 = upperEight(bytes);
            long combinedHash = hash1;
            for (int i = 0; i < numHashFunctions; i++) {
                if (!bits.get((Long.MAX_VALUE & combinedHash) % bitSize)) {
                    return false;
                }
                combinedHash += hash2;
            }
            return true;
        }

        private long lowerEight(byte[] bytes) {
            return Longs.fromBytes(bytes[7], bytes[6], bytes[5], bytes[4], bytes[3], bytes[2], bytes[1], bytes[0]);
        }

        private long upperEight(byte[] bytes) {
            return Longs.fromBytes(bytes[15], bytes[14], bytes[13], bytes[12], bytes[11], bytes[10], bytes[9], bytes[8]);
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class BitArray {
        long bitCount;
        final long[] data;

        /* JADX INFO: Access modifiers changed from: package-private */
        public BitArray(long bits) {
            this(new long[Ints.checkedCast(LongMath.divide(bits, 64L, RoundingMode.CEILING))]);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public BitArray(long[] data) {
            Preconditions.checkArgument(data.length > 0, "data length is zero!");
            this.data = data;
            long bitCount = 0;
            for (long value : data) {
                bitCount += Long.bitCount(value);
            }
            this.bitCount = bitCount;
        }

        boolean set(long index) {
            if (!get(index)) {
                long[] jArr = this.data;
                int i = (int) (index >>> 6);
                jArr[i] = jArr[i] | (1 << ((int) index));
                this.bitCount++;
                return true;
            }
            return false;
        }

        boolean get(long index) {
            return (this.data[(int) (index >>> 6)] & (1 << ((int) index))) != 0;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public long bitSize() {
            return this.data.length * 64;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public long bitCount() {
            return this.bitCount;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public BitArray copy() {
            return new BitArray((long[]) this.data.clone());
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void putAll(BitArray array) {
            Preconditions.checkArgument(this.data.length == array.data.length, "BitArrays must be of equal length (%s != %s)", this.data.length, array.data.length);
            this.bitCount = 0L;
            for (int i = 0; i < this.data.length; i++) {
                long[] jArr = this.data;
                jArr[i] = jArr[i] | array.data[i];
                this.bitCount += Long.bitCount(this.data[i]);
            }
        }

        public boolean equals(@Nullable Object o) {
            if (o instanceof BitArray) {
                BitArray bitArray = (BitArray) o;
                return Arrays.equals(this.data, bitArray.data);
            }
            return false;
        }

        public int hashCode() {
            return Arrays.hashCode(this.data);
        }
    }
}
