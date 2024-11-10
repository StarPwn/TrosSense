package com.trossense;

/* loaded from: classes3.dex */
public class a_ extends a9 {
    private static int[] c;
    private boolean a = false;

    static {
        if (d() == null) {
            b(new int[2]);
        }
    }

    public static void b(int[] iArr) {
        c = iArr;
    }

    public static int[] d() {
        return c;
    }

    public void a(boolean z) {
        this.a = z;
    }

    public boolean a() {
        return this.a;
    }

    public void c() {
        this.a = true;
    }
}
