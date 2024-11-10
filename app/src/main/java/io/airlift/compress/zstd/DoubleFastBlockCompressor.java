package io.airlift.compress.zstd;

/* loaded from: classes.dex */
class DoubleFastBlockCompressor implements BlockCompressor {
    private static final int MIN_MATCH = 3;
    private static final int PRIME_4_BYTES = -1640531535;
    private static final long PRIME_5_BYTES = 889523592379L;
    private static final long PRIME_6_BYTES = 227718039650203L;
    private static final long PRIME_7_BYTES = 58295818150454627L;
    private static final long PRIME_8_BYTES = -3523014627327384477L;
    private static final int REP_MOVE = 2;
    private static final int SEARCH_STRENGTH = 8;

    /* JADX WARN: Removed duplicated region for block: B:35:0x026d  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x034e  */
    @Override // io.airlift.compress.zstd.BlockCompressor
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int compressBlock(java.lang.Object r54, final long r55, int r57, io.airlift.compress.zstd.SequenceStore r58, io.airlift.compress.zstd.BlockCompressionState r59, io.airlift.compress.zstd.RepeatedOffsets r60, io.airlift.compress.zstd.CompressionParameters r61) {
        /*
            Method dump skipped, instructions count: 971
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.airlift.compress.zstd.DoubleFastBlockCompressor.compressBlock(java.lang.Object, long, int, io.airlift.compress.zstd.SequenceStore, io.airlift.compress.zstd.BlockCompressionState, io.airlift.compress.zstd.RepeatedOffsets, io.airlift.compress.zstd.CompressionParameters):int");
    }

    public static int count(Object inputBase, final long inputAddress, final long inputLimit, final long matchAddress) {
        long input = inputAddress;
        long match = matchAddress;
        int remaining = (int) (inputLimit - inputAddress);
        int count = 0;
        while (count < remaining - 7) {
            long diff = UnsafeUtil.UNSAFE.getLong(inputBase, match) ^ UnsafeUtil.UNSAFE.getLong(inputBase, input);
            if (diff != 0) {
                return (Long.numberOfTrailingZeros(diff) >> 3) + count;
            }
            count += 8;
            input += 8;
            match += 8;
        }
        while (count < remaining && UnsafeUtil.UNSAFE.getByte(inputBase, match) == UnsafeUtil.UNSAFE.getByte(inputBase, input)) {
            count++;
            input++;
            match++;
        }
        return count;
    }

    private static int hash(Object inputBase, long inputAddress, int bits, int matchSearchLength) {
        switch (matchSearchLength) {
            case 5:
                return hash5(UnsafeUtil.UNSAFE.getLong(inputBase, inputAddress), bits);
            case 6:
                return hash6(UnsafeUtil.UNSAFE.getLong(inputBase, inputAddress), bits);
            case 7:
                return hash7(UnsafeUtil.UNSAFE.getLong(inputBase, inputAddress), bits);
            case 8:
                return hash8(UnsafeUtil.UNSAFE.getLong(inputBase, inputAddress), bits);
            default:
                return hash4(UnsafeUtil.UNSAFE.getInt(inputBase, inputAddress), bits);
        }
    }

    private static int hash4(int value, int bits) {
        return (PRIME_4_BYTES * value) >>> (32 - bits);
    }

    private static int hash5(long value, int bits) {
        return (int) (((value << 24) * PRIME_5_BYTES) >>> (64 - bits));
    }

    private static int hash6(long value, int bits) {
        return (int) (((value << 16) * PRIME_6_BYTES) >>> (64 - bits));
    }

    private static int hash7(long value, int bits) {
        return (int) (((value << 8) * PRIME_7_BYTES) >>> (64 - bits));
    }

    private static int hash8(long value, int bits) {
        return (int) ((PRIME_8_BYTES * value) >>> (64 - bits));
    }
}
