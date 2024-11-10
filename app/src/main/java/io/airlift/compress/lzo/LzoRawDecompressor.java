package io.airlift.compress.lzo;

/* loaded from: classes.dex */
public final class LzoRawDecompressor {
    private static final int[] DEC_32_TABLE = {4, 1, 2, 1, 4, 4, 4, 4};
    private static final int[] DEC_64_TABLE = {0, 0, 0, -1, 0, 1, 2, 3};

    private LzoRawDecompressor() {
    }

    /* JADX WARN: Code restructure failed: missing block: B:130:0x0106, code lost:            r2 = r6;        r0 = r11;     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int decompress(final java.lang.Object r33, final long r34, final long r36, final java.lang.Object r38, final long r39, final long r41) throws io.airlift.compress.MalformedInputException {
        /*
            Method dump skipped, instructions count: 881
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.airlift.compress.lzo.LzoRawDecompressor.decompress(java.lang.Object, long, long, java.lang.Object, long, long):int");
    }

    private static String toBinary(int command) {
        String binaryString = String.format("%8s", Integer.toBinaryString(command)).replace(' ', '0');
        return "0b" + binaryString.substring(0, 4) + "_" + binaryString.substring(4);
    }
}
