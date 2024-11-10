package com.trossense.sdk.level;

import com.trossense.sdk.PointerHolder;
import com.trossense.sdk.block.Block;
import com.trossense.sdk.block.Material;
import com.trossense.sdk.entity.type.EntityActor;
import com.trossense.sdk.math.Vec3i;

/* loaded from: classes3.dex */
public class Level extends PointerHolder {
    private static PointerHolder[] a;

    static {
        if (g() != null) {
            b(new PointerHolder[4]);
        }
    }

    public Level(long j) {
        super(j);
    }

    public static void b(PointerHolder[] pointerHolderArr) {
        a = pointerHolderArr;
    }

    public static PointerHolder[] g() {
        return a;
    }

    public native Block a(Vec3i vec3i);

    public void a(long j) {
        e(j);
    }

    public EntityActor[] a() {
        return c();
    }

    public long b() {
        return d();
    }

    public native Material b(Vec3i vec3i);

    public Block c(Vec3i vec3i) {
        return a(vec3i);
    }

    public native EntityActor[] c();

    public native long d();

    public Material d(Vec3i vec3i) {
        return b(vec3i);
    }

    public native void e(long j);

    public EntityActor[] e() {
        return f();
    }

    public native EntityActor[] f();
}
