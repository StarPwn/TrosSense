package com.trossense.sdk.render;

import com.trossense.sdk.PointerHolder;

/* loaded from: classes3.dex */
public class ScreenView extends PointerHolder {
    private static boolean a;

    static {
        if (e()) {
            return;
        }
        b(true);
    }

    public ScreenView(long j) {
        super(j);
    }

    public static void b(boolean z) {
        a = z;
    }

    public static boolean e() {
        return a;
    }

    public static boolean f() {
        return !e();
    }

    public native void a();

    public native String b();

    public void c() {
        a();
    }

    public String d() {
        return b();
    }
}
