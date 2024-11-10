package com.trossense;

import com.trossense.sdk.PointerHolder;
import com.trossense.sdk.block.Block;
import com.trossense.sdk.entity.type.EntityLocalPlayer;
import java.lang.invoke.MethodHandles;

/* loaded from: classes3.dex */
public class bc extends a9 {
    private static final long e = dj.a(-6451656712263500195L, 4067502884624805000L, MethodHandles.lookup().lookupClass()).a(117660377914429L);
    private Block a;
    private EntityLocalPlayer c;
    private float d;

    public bc(Block block, EntityLocalPlayer entityLocalPlayer, float f, long j) {
        int d = bg.d();
        this.a = block;
        this.c = entityLocalPlayer;
        this.d = f;
        if (d != 0) {
            PointerHolder.b(new int[1]);
        }
    }

    public Block a() {
        return this.a;
    }

    public void a(float f) {
        this.d = f;
    }

    public void a(Block block) {
        this.a = block;
    }

    public void a(EntityLocalPlayer entityLocalPlayer) {
        this.c = entityLocalPlayer;
    }

    public EntityLocalPlayer c() {
        return this.c;
    }

    public float d() {
        return this.d;
    }
}
