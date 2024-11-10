package com.trossense;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.opengl.GLSurfaceView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import com.trossense.sdk.PointerHolder;
import java.lang.invoke.MethodHandles;

/* loaded from: classes3.dex */
public class a extends PopupWindow {
    private static final long e = dj.a(3779576124016089675L, -1403388197453744153L, MethodHandles.lookup().lookupClass()).a(97872059429140L);
    private GLSurfaceView a;
    private Activity b;
    private int c;
    private int d;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public a(char c, int i, int i2, Activity activity) {
        super(activity);
        long j = ((((i2 << 48) >>> 48) | ((c << 48) | ((i << 32) >>> 16))) ^ e) ^ 45930074570211L;
        boolean c2 = g.c();
        this.b = activity;
        this.c = a();
        int b = b();
        this.d = b;
        if (this.c < b) {
            this.c = b();
            this.d = a();
        }
        GLSurfaceView gLSurfaceView = new GLSurfaceView(activity);
        this.a = gLSurfaceView;
        gLSurfaceView.setEGLContextClientVersion(3);
        this.a.setEGLConfigChooser(8, 8, 8, 8, 16, 8);
        this.a.getHolder().setFormat(-3);
        this.a.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        this.a.setZOrderOnTop(false);
        this.a.setRenderer(new f(j, this.c, this.d));
        setContentView(this.a);
        setBackgroundDrawable(new ColorDrawable(0));
        setOutsideTouchable(false);
        setClippingEnabled(false);
        setFocusable(false);
        setTouchable(false);
        setWidth(-1);
        setHeight(-1);
        showAtLocation(this.b.getWindow().getDecorView(), 17, 0, 0);
        int i3 = c2;
        if (i2 >= 0) {
            if (c2 != 0) {
                return;
            } else {
                i3 = 4;
            }
        }
        PointerHolder.b(new int[i3]);
    }

    private int a() {
        return this.b.getResources().getDisplayMetrics().widthPixels;
    }

    private int b() {
        return this.b.getResources().getDisplayMetrics().heightPixels;
    }

    @Override // android.widget.PopupWindow
    public boolean isTouchable() {
        return super.isTouchable();
    }
}
