package com.trossense;

import android.graphics.Color;
import java.lang.invoke.MethodHandles;

/* loaded from: classes3.dex */
public class c9 {
    private static final long a = dj.a(-9079894514692682318L, 7529795932188079895L, MethodHandles.lookup().lookupClass()).a(6261079179645L);

    public static float a(int i, int i2) {
        return ((int) (((System.currentTimeMillis() / i) + i2) % 360)) / 360.0f;
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x0058, code lost:            if (r0 == 0) goto L17;     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0068, code lost:            if (r0 == 0) goto L20;     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0078, code lost:            if (r0 == 0) goto L23;     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0088, code lost:            if (r0 == 0) goto L26;     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0097, code lost:            if (r0 == 0) goto L29;     */
    /* JADX WARN: Failed to find 'out' block for switch in B:9:0x0044. Please report as an issue. */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int a(float r10, char r11, int r12, float r13, float r14, short r15) {
        /*
            Method dump skipped, instructions count: 192
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.c9.a(float, char, int, float, float, short):int");
    }

    public static int a(int i) {
        return (255 - b(i, 0)) + ((255 - b(i, 8)) << 8) + ((255 - b(i, 16)) << 16) + (b(i, 24) << 24);
    }

    public static int a(int i, char c, char c2, int i2, int i3, float f, float f2, float f3) {
        int a2 = a(((int) (((System.currentTimeMillis() / i2) + i3) % 360)) / 360.0f, (char) (r0 >>> 48), (int) (((((((i << 32) | ((c << 48) >>> 32)) | ((c2 << 48) >>> 48)) ^ a) ^ 84413050941333L) << 16) >>> 32), f, f2, (short) ((r0 << 48) >>> 48));
        return Color.argb(Math.max(0, Math.min(255, (int) (255.0f * f3))), Color.red(a2), Color.green(a2), Color.blue(a2));
    }

    public static int a(int i, float f) {
        return Color.argb((int) (f * 255.0f), Color.red(i), Color.green(i), Color.blue(i));
    }

    public static int a(int i, int i2, float f) {
        double min = Math.min(1.0f, Math.max(0.0f, f));
        return Color.argb(c2.a(Color.alpha(i), Color.alpha(i2), min), c2.a(Color.red(i), Color.red(i2), min), c2.a(Color.green(i), Color.green(i2), min), c2.a(Color.blue(i), Color.blue(i2), min));
    }

    public static int a(int i, int i2, int i3, int i4, boolean z, long j) {
        long j2 = (j ^ a) ^ 59014396576231L;
        int i5 = (int) (j2 >>> 56);
        long j3 = (j2 << 8) >>> 8;
        int currentTimeMillis = (int) (((System.currentTimeMillis() / i) + i2) % 360);
        if (currentTimeMillis >= 180) {
            currentTimeMillis = 360 - currentTimeMillis;
        }
        float f = currentTimeMillis * 2;
        return z ? b(i3, i4, f / 360.0f, (byte) i5, j3) : a(i3, i4, f / 360.0f);
    }

    public static float[] a(int i, int i2, int i3, long j, float[] fArr) {
        if (fArr == null) {
            fArr = new float[3];
        }
        int max = Math.max(i, i2);
        if (i3 > max) {
            max = i3;
        }
        int min = Math.min(i, i2);
        if (i3 < min) {
            min = i3;
        }
        float f = max;
        float f2 = f / 255.0f;
        float f3 = 0.0f;
        float f4 = max != 0 ? (max - min) / f : 0.0f;
        if (f4 != 0.0f) {
            float f5 = max - min;
            float f6 = (max - i) / f5;
            float f7 = (max - i2) / f5;
            float f8 = (max - i3) / f5;
            float f9 = (i == max ? f8 - f7 : i2 == max ? (f6 + 2.0f) - f8 : (f7 + 4.0f) - f6) / 6.0f;
            f3 = f9 < 0.0f ? f9 + 1.0f : f9;
        }
        fArr[0] = f3;
        fArr[1] = f4;
        fArr[2] = f2;
        return fArr;
    }

    private static int b(int i, int i2) {
        return (i >> i2) & 255;
    }

    public static int b(int i, int i2, float f, byte b, long j) {
        long j2 = ((b << 56) | ((j << 8) >>> 8)) ^ a;
        int i3 = (int) (((27373943065730L ^ j2) << 16) >>> 32);
        long j3 = j2 ^ 13082284801662L;
        float min = Math.min(1.0f, Math.max(0.0f, f));
        float[] a2 = a(Color.red(i), Color.green(i), Color.blue(i), j3, null);
        float[] a3 = a(Color.red(i2), Color.green(i2), Color.blue(i2), j3, null);
        double d = min;
        return a(a(c2.a(a2[0], a3[0], d), (char) (r2 >>> 48), i3, c2.a(a2[1], a3[1], d), c2.a(a2[2], a3[2], d), (short) ((r2 << 48) >>> 48)), c2.a(Color.alpha(i), Color.alpha(i2), d) / 255.0f);
    }

    public static int b(int i, long j, float f) {
        int red = Color.red(i);
        int green = Color.green(i);
        int blue = Color.blue(i);
        int alpha = Color.alpha(i);
        int i2 = (int) (1.0d / (1.0d - f));
        if (red == 0 && green == 0 && blue == 0) {
            return Color.argb(alpha, i2, i2, i2);
        }
        if (red > 0 && red < i2) {
            red = i2;
        }
        if (green > 0 && green < i2) {
            green = i2;
        }
        if (blue > 0 && blue < i2) {
            blue = i2;
        }
        return Color.argb(alpha, Math.min((int) (red / f), 255), Math.min((int) (green / f), 255), Math.min((int) (blue / f), 255));
    }
}
