package com.trossense.sdk.entity.type;

import com.trossense.dj;
import com.trossense.sdk.InstanceGenerator;
import com.trossense.sdk.block.Block;
import com.trossense.sdk.component.MoveInputComponent;
import com.trossense.sdk.inventory.PlayerInventory;
import com.trossense.sdk.math.Vec2f;
import com.trossense.sdk.math.Vec3f;
import com.trossense.sdk.math.Vec3i;
import java.lang.invoke.MethodHandles;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;

/* loaded from: classes3.dex */
public class EntityLocalPlayer extends b {
    private static String[] c;
    private static final long f = dj.a(-4959296102529583370L, 2709134230738703132L, MethodHandles.lookup().lookupClass()).a(52963660502192L);

    static {
        if (al() != null) {
            b(new String[3]);
        }
    }

    public EntityLocalPlayer(long j) {
        super(j);
    }

    private native void a1(byte[] bArr);

    public static String[] al() {
        return c;
    }

    public static void b(String[] strArr) {
        c = strArr;
    }

    public static native void c1(boolean z);

    public static native void d1(boolean z);

    public static void f(boolean z) {
        c1(z);
    }

    public static native void f1(float f2);

    public static void g(float f2) {
        f1(f2);
    }

    public static void g(boolean z) {
        d1(z);
    }

    private native void x(byte[] bArr);

    private native void y(byte[] bArr);

    public float a(Block block) {
        return e1(block);
    }

    public void a(byte b) {
        u(b);
    }

    public void a(long j, BedrockPacket bedrockPacket) {
        x(InstanceGenerator.a((j ^ f) ^ 41832601353913L, bedrockPacket));
    }

    public native void a(EntityActor entityActor);

    public void a(Vec3i vec3i) {
        e2(vec3i);
    }

    public void a(Vec3i vec3i, int i) {
        b(vec3i, i);
    }

    public void a(Vec3i vec3i, int i, boolean z) {
        g(vec3i, i, z);
    }

    public void a(String str) {
        b1(str);
    }

    public void ac() {
        i1();
    }

    public MoveInputComponent ad() {
        return j2();
    }

    public Vec3f ae() {
        return l3();
    }

    public Vec3f af() {
        return m();
    }

    public int ag() {
        return n3();
    }

    public byte ah() {
        return o();
    }

    public Vec3i ai() {
        return p3();
    }

    public Vec3i aj() {
        return q();
    }

    public PlayerInventory ak() {
        return g3();
    }

    public void b(Vec3i vec3i) {
        v(vec3i);
    }

    public native void b(Vec3i vec3i, int i);

    public void b(BedrockPacket bedrockPacket, long j) {
        y(InstanceGenerator.a((j ^ f) ^ 32396601050842L, bedrockPacket));
    }

    public native void b1(String str);

    public void c(EntityActor entityActor) {
        a(entityActor);
    }

    public void c(BedrockPacket bedrockPacket, long j) {
        z(InstanceGenerator.a((j ^ f) ^ 87716462070638L, bedrockPacket));
    }

    public native boolean c(Vec3i vec3i, int i);

    public void d(Vec2f vec2f) {
        k(vec2f);
    }

    public void d(BedrockPacket bedrockPacket, long j) {
        a1(InstanceGenerator.a((j ^ f) ^ 44284162259724L, bedrockPacket));
    }

    public native boolean d(Vec3i vec3i, int i);

    public void e(Vec3f vec3f) {
        r(vec3f);
    }

    public boolean e(Vec3i vec3i, int i) {
        return c(vec3i, i);
    }

    public native float e1(Block block);

    public native void e2(Vec3i vec3i);

    public void f(Vec3f vec3f) {
        s(vec3f);
    }

    public native void f(Vec3i vec3i, int i);

    public void g(Vec3f vec3f) {
        w(vec3f);
    }

    public native void g(Vec3i vec3i, int i, boolean z);

    public boolean g(Vec3i vec3i, int i) {
        return d(vec3i, i);
    }

    public native PlayerInventory g3();

    public native void h(Vec3i vec3i, int i);

    public void i(int i) {
        t(i);
    }

    public void i(Vec3i vec3i, int i) {
        f(vec3i, i);
    }

    public native void i1();

    public void j(Vec3i vec3i, int i) {
        h(vec3i, i);
    }

    public native MoveInputComponent j2();

    public native void k(Vec2f vec2f);

    public native Vec3f l3();

    public native Vec3f m();

    public native int n3();

    public native byte o();

    public native Vec3i p3();

    public native Vec3i q();

    public native void r(Vec3f vec3f);

    @Override // com.trossense.sdk.entity.type.EntityActor
    public native void s(Vec3f vec3f);

    public native void t(int i);

    public native void u(byte b);

    public native void v(Vec3i vec3i);

    public native void w(Vec3f vec3f);

    public native void z(byte[] bArr);
}
