package com.trossense;

import android.opengl.Matrix;
import java.lang.invoke.MethodHandles;
import java.util.Stack;

/* loaded from: classes3.dex */
public class c_ {
    public static final int a = 0;
    public static final int b = 1;
    public static final int c = 2;
    private static final long i = dj.a(-1680345081908791995L, 4926904594342505488L, MethodHandles.lookup().lookupClass()).a(260867895916097L);
    static final /* synthetic */ boolean h = true;
    private static Stack<float[]> d = new Stack<>();
    private static Stack<float[]> e = new Stack<>();
    private static Stack<float[]> f = new Stack<>();
    private static int g = 0;

    static {
        float[] fArr = new float[16];
        Matrix.setIdentityM(fArr, 0);
        float[] fArr2 = new float[16];
        Matrix.setIdentityM(fArr2, 0);
        float[] fArr3 = new float[16];
        Matrix.setIdentityM(fArr3, 0);
        d.add(fArr);
        e.add(fArr2);
        f.add(fArr3);
    }

    public static void a(float f2, float f3, short s, long j, float f4, float f5, float f6, float f7) {
        Matrix.orthoM((float[]) b((((s << 48) | ((j << 16) >>> 16)) ^ i) ^ 112025621009300L).peek(), 0, f2, f3, f4, f5, f6, f7);
    }

    public static void a(float f2, long j, float f3, float f4, float f5) {
        Matrix.rotateM((float[]) b((j ^ i) ^ 125839243454185L).peek(), 0, f2, f3, f4, f5);
    }

    public static void a(int i2) {
        g = i2;
    }

    public static void a(long j) {
        long j2 = j ^ i;
        Stack b2 = b(113206694448542L ^ j2);
        if (!h) {
            if (j2 <= 0) {
                return;
            }
            if (b2 == null) {
                throw new AssertionError();
            }
        }
        b2.push((float[]) ((float[]) b2.peek()).clone());
    }

    public static void a(long j, float f2, float f3, float f4) {
        Matrix.translateM((float[]) b((j ^ i) ^ 13602516487019L).peek(), 0, f2, f3, f4);
    }

    private static Stack b(long j) {
        switch (g) {
            case 0:
                return f;
            case 1:
                return e;
            case 2:
                return d;
            default:
                return null;
        }
    }

    public static void b(float f2, long j, float f3, float f4) {
        Matrix.scaleM((float[]) b((j ^ i) ^ 89601009349909L).peek(), 0, f2, f3, f4);
    }

    public static void c(long j) {
        b((j ^ i) ^ 44151293726822L).pop();
    }

    public static void d(long j) {
        Matrix.setIdentityM((float[]) b((j ^ i) ^ 23997566782546L).peek(), 0);
    }

    public static float[] e() {
        float[] fArr = new float[16];
        float[] fArr2 = new float[16];
        Matrix.multiplyMM(fArr2, 0, f.peek(), 0, d.peek(), 0);
        Matrix.multiplyMM(fArr, 0, fArr2, 0, e.peek(), 0);
        return fArr;
    }
}
