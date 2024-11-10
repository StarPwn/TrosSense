package com.trossense.sdk.item;

import com.trossense.sdk.PointerHolder;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;

/* loaded from: classes3.dex */
public class ItemStack extends PointerHolder {
    private static PointerHolder[] a;

    static {
        if (r() != null) {
            b(new PointerHolder[3]);
        }
    }

    public ItemStack(long j) {
        super(j);
    }

    public static void b(PointerHolder[] pointerHolderArr) {
        a = pointerHolderArr;
    }

    public static PointerHolder[] r() {
        return a;
    }

    public native short a();

    public void a(int i) {
        e(i);
    }

    public native boolean b();

    public native int c();

    public native int d();

    public short e() {
        return a();
    }

    public native void e(int i);

    public native String f();

    public native boolean g();

    public native int h();

    public native ItemData i();

    public boolean j() {
        return b();
    }

    public int l() {
        return c();
    }

    public int m() {
        return d();
    }

    public String n() {
        return f();
    }

    public boolean o() {
        return g();
    }

    public int p() {
        return h();
    }

    public ItemData q() {
        return i();
    }
}
