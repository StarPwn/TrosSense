package com.trossense.sdk.entity.type;

import com.trossense.sdk.PointerHolder;
import com.trossense.sdk.item.ItemStack;
import com.trossense.sdk.level.Level;
import com.trossense.sdk.math.AxisAlignedBB;
import com.trossense.sdk.math.Vec2f;
import com.trossense.sdk.math.Vec3f;

/* loaded from: classes3.dex */
public class EntityActor extends PointerHolder {
    private static boolean a;

    static {
        if (Z()) {
            return;
        }
        e(true);
    }

    public EntityActor(long j) {
        super(j);
    }

    public static boolean Z() {
        return a;
    }

    public static boolean aa() {
        return !Z();
    }

    public static void e(boolean z) {
        a = z;
    }

    public boolean A() {
        return j();
    }

    public float B() {
        return l();
    }

    public long C() {
        return n();
    }

    public int D() {
        return p();
    }

    public Vec3f E() {
        return r();
    }

    public float F() {
        return t();
    }

    public long G() {
        return v();
    }

    public float H() {
        return b1();
    }

    public float I() {
        return c1();
    }

    public float J() {
        return f1();
    }

    public float K() {
        return g1();
    }

    public Vec2f L() {
        return j1();
    }

    public Vec2f M() {
        return k1();
    }

    public Vec3f N() {
        return n1();
    }

    public Vec3f O() {
        return p1();
    }

    public Vec3f P() {
        return r1();
    }

    public AxisAlignedBB Q() {
        return u1();
    }

    public Vec2f R() {
        return w1();
    }

    public boolean S() {
        return f2();
    }

    public boolean T() {
        return g2();
    }

    public boolean U() {
        return i2();
    }

    public EntityActor V() {
        return k2();
    }

    public Level W() {
        return l2();
    }

    public ItemStack X() {
        return m2();
    }

    public ItemStack Y() {
        return n2();
    }

    public native int a();

    public void a(float f) {
        m(f);
    }

    public void a(int i) {
        q(i);
    }

    public void a(int i, float f) {
        a1(i, f);
    }

    public void a(int i, boolean z) {
        z(i, z);
    }

    public void a(long j) {
        o(j);
    }

    public void a(AxisAlignedBB axisAlignedBB) {
        t1(axisAlignedBB);
    }

    public void a(Vec2f vec2f) {
        l1(vec2f);
    }

    public void a(Vec3f vec3f) {
        s(vec3f);
    }

    public void a(boolean z) {
        d(z);
    }

    public native void a1(int i, float f);

    public native float a2(int i);

    public void b(float f) {
        u(f);
    }

    public void b(int i, float f) {
        c2(i, f);
    }

    public void b(int i, boolean z) {
        y1(i, z);
    }

    public void b(long j) {
        w(j);
    }

    public void b(Vec2f vec2f) {
        m1(vec2f);
    }

    public void b(Vec3f vec3f) {
        o1(vec3f);
    }

    public void b(boolean z) {
        k(z);
    }

    public native boolean b();

    public boolean b(int i) {
        return x(i);
    }

    public boolean b(EntityActor entityActor) {
        return g(entityActor);
    }

    public native float b1();

    public native float b2(int i);

    public float c(int i) {
        return y(i);
    }

    public native void c();

    public void c(float f) {
        d1(f);
    }

    public void c(int i, float f) {
        d2(i, f);
    }

    public void c(Vec2f vec2f) {
        v1(vec2f);
    }

    public void c(Vec3f vec3f) {
        q1(vec3f);
    }

    public void c(boolean z) {
        h2(z);
    }

    public native float c1();

    public native void c2(int i, float f);

    public int d() {
        return a();
    }

    public void d(float f) {
        e1(f);
    }

    public void d(int i, float f) {
        e2(i, f);
    }

    public void d(Vec3f vec3f) {
        s1(vec3f);
    }

    public native void d(boolean z);

    public boolean d(int i) {
        return x1(i);
    }

    public native void d1(float f);

    public native void d2(int i, float f);

    public float e(int i) {
        return z1(i);
    }

    public void e(float f) {
        h1(f);
    }

    public native boolean e();

    public native void e1(float f);

    public native void e2(int i, float f);

    public float f(int i) {
        return a2(i);
    }

    public void f(float f) {
        i1(f);
    }

    public native boolean f();

    public native float f1();

    public native boolean f2();

    public float g(int i) {
        return b2(i);
    }

    public boolean g() {
        return b();
    }

    public native boolean g(EntityActor entityActor);

    public native float g1();

    public native boolean g2();

    public ItemStack h(int i) {
        return o2(i);
    }

    public native String h();

    public native void h1(float f);

    public native void h2(boolean z);

    public native String i();

    public native void i1(float f);

    public native boolean i2();

    public native boolean j();

    public native Vec2f j1();

    public native void k(boolean z);

    public native Vec2f k1();

    public native EntityActor k2();

    public native float l();

    public native void l1(Vec2f vec2f);

    public native Level l2();

    public native void m(float f);

    public native void m1(Vec2f vec2f);

    public native ItemStack m2();

    public native long n();

    public native Vec3f n1();

    public native ItemStack n2();

    public native void o(long j);

    public native void o1(Vec3f vec3f);

    public native ItemStack o2(int i);

    public native int p();

    public native Vec3f p1();

    public native void q(int i);

    public native void q1(Vec3f vec3f);

    public native Vec3f r();

    public native Vec3f r1();

    public native void s(Vec3f vec3f);

    public native void s1(Vec3f vec3f);

    public native float t();

    public native void t1(AxisAlignedBB axisAlignedBB);

    public void u() {
        c();
    }

    public native void u(float f);

    public native AxisAlignedBB u1();

    public native long v();

    public native void v1(Vec2f vec2f);

    public native void w(long j);

    public boolean w() {
        return e();
    }

    public native Vec2f w1();

    public boolean x() {
        return f();
    }

    public native boolean x(int i);

    public native boolean x1(int i);

    public native float y(int i);

    public String y() {
        return h();
    }

    public native void y1(int i, boolean z);

    public String z() {
        return i();
    }

    public native void z(int i, boolean z);

    public native float z1(int i);
}
