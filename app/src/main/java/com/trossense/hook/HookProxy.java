package com.trossense.hook;

import com.trossense.dj;
import java.lang.invoke.MethodHandles;

/* loaded from: classes3.dex */
public class HookProxy {
    private static final long a = dj.a(2923858272143776939L, -4125992381120662783L, MethodHandles.lookup().lookupClass()).a(38217760283609L);

    public static void a(long j) {
        JavaProxy.b(j, (a ^ 110578610617140L) ^ 69651935958684L);
    }

    public static void b(int i, int i2) {
        JavaProxy.a(i, i2, (a ^ 32416030059595L) ^ 688792419133L);
    }

    public static float c(long j, long j2, int i, int i2, int i3, float f) {
        return JavaProxy.a(j, j2, i, (a ^ 89059209635623L) ^ 35380190550093L, i2, i3, f);
    }

    public static void d(long j) {
        JavaProxy.c(j);
    }

    public static void e(long j) {
        JavaProxy.d(j);
    }

    public static void f(long j, long j2) {
        JavaProxy.a(j, j2);
    }

    public static byte[] g(byte[] bArr) {
        return JavaProxy.a(bArr, (a ^ 22531078357636L) ^ 41880768752454L);
    }

    public static void h() {
        JavaProxy.a();
    }

    public static byte[] i(byte[] bArr) {
        return JavaProxy.b(bArr, (a ^ 88612375068565L) ^ 104032933866948L);
    }

    public static void j() {
        JavaProxy.b();
    }

    public static boolean k(int i, float f, float f2, int i2) {
        return JavaProxy.a(i, (a ^ 139291380561575L) ^ 50115796367032L, f, f2, i2);
    }
}
