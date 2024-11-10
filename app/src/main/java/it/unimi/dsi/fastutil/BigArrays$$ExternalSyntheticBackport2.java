package it.unimi.dsi.fastutil;

/* compiled from: D8$$SyntheticClass */
/* loaded from: classes4.dex */
public final /* synthetic */ class BigArrays$$ExternalSyntheticBackport2 {
    public static /* synthetic */ String m(long j, int i) {
        if (j == 0) {
            return "0";
        }
        if (j > 0) {
            return Long.toString(j, i);
        }
        if (i < 2 || i > 36) {
            i = 10;
        }
        int i2 = 64;
        char[] cArr = new char[64];
        int i3 = i - 1;
        if ((i & i3) == 0) {
            int numberOfTrailingZeros = Integer.numberOfTrailingZeros(i);
            do {
                i2--;
                cArr[i2] = Character.forDigit(((int) j) & i3, i);
                j >>>= numberOfTrailingZeros;
            } while (j != 0);
        } else {
            long m = (i & 1) == 0 ? (j >>> 1) / (i >>> 1) : BigArrays$$ExternalSyntheticBackport0.m(j, i);
            long j2 = i;
            cArr[63] = Character.forDigit((int) (j - (m * j2)), i);
            i2 = 63;
            while (m > 0) {
                i2--;
                cArr[i2] = Character.forDigit((int) (m % j2), i);
                m /= j2;
            }
        }
        return new String(cArr, i2, 64 - i2);
    }
}
