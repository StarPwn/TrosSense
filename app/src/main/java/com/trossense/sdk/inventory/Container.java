package com.trossense.sdk.inventory;

import com.trossense.sdk.PointerHolder;
import com.trossense.sdk.item.ItemStack;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerType;

/* loaded from: classes3.dex */
public class Container extends PointerHolder {
    public Container(long j) {
        super(j);
    }

    public native int a();

    public ItemStack a(int i) {
        return f(i);
    }

    public native int b();

    public native boolean c();

    public native String d();

    public native int e();

    public native ItemStack f(int i);

    public ContainerType f() {
        try {
            return ContainerType.from(a());
        } catch (Exception e) {
            e.printStackTrace();
            return ContainerType.NONE;
        }
    }

    public int g() {
        return b();
    }

    public boolean h() {
        return c();
    }

    public String i() {
        return d();
    }

    public int j() {
        return e();
    }
}
