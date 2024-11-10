package com.trossense.sdk.entity;

/* loaded from: classes3.dex */
public class b {
    public static final int a = 1;
    public static final int b = 2;
    public static final int c = 3;
    public static final int d = 4;
    public static final int e = 5;
    public static final int f = 6;
    public static final int g = 7;
    public static final int h = 9;
    public static final int i = 13;
    public static final int j = 14;
    private static boolean k;

    static {
        if (b()) {
            return;
        }
        b(true);
    }

    public static void b(boolean z) {
        k = z;
    }

    public static boolean b() {
        return k;
    }

    public static boolean c() {
        return !b();
    }
}
