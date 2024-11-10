package com.trossense;

import com.trossense.sdk.PointerHolder;
import java.lang.invoke.MethodHandles;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/* loaded from: classes3.dex */
public class c3 {
    private static int[] b;
    private static final long c = dj.a(-2757493021541485987L, 2652281952921625791L, MethodHandles.lookup().lookupClass()).a(120716603874073L);
    private static final Random a = new Random();

    static {
        b(null);
    }

    public static double a(double d, long j, double d2) {
        long j2 = j ^ c;
        if (d == d2 || j2 < 0) {
            return d;
        }
        if (d > d2) {
            d = d2;
            d2 = d;
        }
        return ThreadLocalRandom.current().nextDouble(d, d2);
    }

    public static int a(int i, long j, int i2) {
        b();
        int max = Math.max(i, i2);
        int min = Math.min(i, i2);
        int nextInt = a.nextInt((max - min) + 1) + min;
        if (PointerHolder.s() == null) {
            b(new int[1]);
        }
        return nextInt;
    }

    public static void b(int[] iArr) {
        b = iArr;
    }

    public static int[] b() {
        return b;
    }
}
