package com.trossense.sdk.inventory;

import com.trossense.sdk.PointerHolder;

/* loaded from: classes3.dex */
public class PlayerInventory extends PointerHolder {
    private static PointerHolder[] a;

    static {
        if (e() == null) {
            b(new PointerHolder[1]);
        }
    }

    public PlayerInventory(long j) {
        super(j);
    }

    public static void b(PointerHolder[] pointerHolderArr) {
        a = pointerHolderArr;
    }

    public static PointerHolder[] e() {
        return a;
    }

    public native Container a();

    public void a(int i) {
        c(i);
    }

    public native int b();

    public Container c() {
        return a();
    }

    public native void c(int i);

    public int d() {
        return b();
    }
}
