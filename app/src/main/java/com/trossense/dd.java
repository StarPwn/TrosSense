package com.trossense;

import android.opengl.GLES20;

/* loaded from: classes3.dex */
public class dd {
    public static void a() {
        GLES20.glClear(1024);
        GLES20.glEnable(2960);
    }

    public static void b() {
        GLES20.glStencilFunc(519, 1, 1);
        GLES20.glStencilOp(7681, 7681, 7681);
        GLES20.glColorMask(false, false, false, false);
    }

    public static void c() {
        GLES20.glColorMask(true, true, true, true);
        GLES20.glStencilFunc(514, 1, 1);
        GLES20.glStencilOp(7680, 7680, 7680);
    }

    public static void d() {
        GLES20.glDisable(2960);
    }
}
