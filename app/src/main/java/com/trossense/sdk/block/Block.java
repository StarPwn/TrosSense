package com.trossense.sdk.block;

import com.trossense.dj;
import com.trossense.sdk.PointerHolder;
import com.trossense.sdk.math.AxisAlignedBB;
import com.trossense.sdk.math.Vec3i;
import java.lang.invoke.MethodHandles;

/* loaded from: classes3.dex */
public class Block extends PointerHolder {
    private static PointerHolder[] a;
    private static final long c = dj.a(1227825681645463785L, -5339440427011660882L, MethodHandles.lookup().lookupClass()).a(151519060961339L);
    private Vec3i pos;

    static {
        if (h() != null) {
            b(new PointerHolder[4]);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public Block(long j, Vec3i vec3i) {
        super(j);
        PointerHolder[] h = h();
        this.pos = vec3i;
        if (h != null) {
            PointerHolder.b(new int[1]);
        }
    }

    public static void b(PointerHolder[] pointerHolderArr) {
        a = pointerHolderArr;
    }

    public static PointerHolder[] h() {
        return a;
    }

    public native String a();

    public native AxisAlignedBB b();

    public native int c();

    public String d() {
        return a();
    }

    public AxisAlignedBB e() {
        return b();
    }

    public Vec3i f() {
        return this.pos;
    }

    public int g() {
        return c();
    }
}
