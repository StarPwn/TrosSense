package com.trossense;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import java.lang.invoke.MethodHandles;

/* loaded from: classes3.dex */
public class az {
    private static int[] k;
    private final a0[] a;
    private final int c;
    private final Canvas d;
    private final Paint e;
    private final Bitmap f;
    private final ay g;
    private final boolean h;
    private final int i;
    private final float j;
    private static final long l = dj.a(-5057621306038421861L, 7447965044686472234L, MethodHandles.lookup().lookupClass()).a(147646312001671L);
    private static final int b = Color.argb(bl.ce, 0, 0, 0);

    static {
        b(new int[5]);
    }

    public az(long j, ay ayVar) {
        this(ayVar, (j ^ l) ^ 50357385300825L, true);
    }

    public az(ay ayVar, long j, boolean z) {
        this.a = new a0[65536];
        this.h = z;
        int b2 = (int) (ayVar.b() * 1.25d);
        this.c = b2;
        this.g = ayVar;
        int b3 = ayVar.b();
        this.i = b3;
        Bitmap createBitmap = Bitmap.createBitmap(b2, b2, Bitmap.Config.ARGB_8888);
        this.f = createBitmap;
        this.d = new Canvas(createBitmap);
        createBitmap.eraseColor(0);
        Paint paint = new Paint();
        this.e = paint;
        paint.setColor(-1);
        paint.setAntiAlias(z);
        paint.setDither(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setFilterBitmap(true);
        paint.setTextSize(b3);
        if (ayVar.a() != null) {
            paint.setTypeface(ayVar.a());
        }
        this.j = paint.getFontMetrics().bottom - paint.getFontMetrics().top;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int a(az azVar) {
        return azVar.c;
    }

    private a0 a(int i, short s, int i2, char c) {
        a0[] a0VarArr = this.a;
        a0 a0Var = a0VarArr[c];
        if (a0Var != null) {
            return a0Var;
        }
        a0 b2 = b(c);
        a0VarArr[c] = b2;
        return b2;
    }

    private void a(long j, a0 a0Var, float f, float f2, int i) {
        a0Var.a(f, f2, i, (j ^ l) ^ 11604384779911L);
    }

    private a0 b(char c) {
        String valueOf = String.valueOf(c);
        this.f.eraseColor(0);
        this.d.drawText(valueOf, 0.0f, -this.e.getFontMetrics().top, this.e);
        return new a0(this, this.f, this.e.measureText(valueOf));
    }

    public static void b(int[] iArr) {
        k = iArr;
    }

    public static int[] b() {
        return k;
    }

    public float a() {
        return this.j;
    }

    public float a(float f) {
        return (f - a()) / 2.0f;
    }

    public float a(long j, String str) {
        long j2 = (j ^ l) ^ 128187937227206L;
        int i = (int) (j2 >>> 32);
        int i2 = (int) ((j2 << 32) >>> 48);
        int i3 = (int) ((j2 << 48) >>> 48);
        if (str == null || str.isEmpty()) {
            return 0.0f;
        }
        int i4 = 0;
        for (int i5 = 0; i5 < str.length(); i5++) {
            i4 = (int) (i4 + a(i, (short) i2, i3, str.charAt(i5)).b);
        }
        return i4;
    }

    public float a(String str, float f, float f2, int i, long j) {
        long j2 = l ^ j;
        return b(str, j2 ^ 75395200626587L, f - (a(65742797800150L ^ j2, str) / 2.0f), f2, i);
    }

    public float a(String str, float f, float f2, long j, int i, boolean z) {
        long j2 = l ^ j;
        return b(j2 ^ 69199601341970L, str, f - (a(46071518062514L ^ j2, str) / 2.0f), f2, i, z);
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x0135 A[LOOP:0: B:11:0x0065->B:31:0x0135, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x013e A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void a(long r28, java.lang.String r30, float r31, float r32, int r33, int r34, boolean r35, boolean r36) {
        /*
            Method dump skipped, instructions count: 319
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.az.a(long, java.lang.String, float, float, int, int, boolean, boolean):void");
    }

    public void a(String str, float f, float f2, float f3, long j, float f4, int i, int i2, boolean z, boolean z2) {
        long j2 = l ^ j;
        da.a(f, f2 + (f3 / 2.0f), j2 ^ 61777650860765L, f4);
        a(j2 ^ 107527289157813L, str, f, f2 + a(f3), i, i2, z, z2);
        da.c((96929707852658L ^ j2) >>> 16, (short) ((r3 << 48) >>> 48));
    }

    public void a(String str, long j, float f, float f2, float f3, float f4, int i) {
        long j2 = l ^ j;
        da.a(f, f2 + (f3 / 2.0f), j2 ^ 32467109361653L, f4);
        b(str, j2 ^ 121070947718879L, f, f2 + a(f3), i);
        da.c((137992343942746L ^ j2) >>> 16, (short) ((r3 << 48) >>> 48));
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x008d, code lost:            if (r14 == null) goto L20;     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public float b(long r19, java.lang.String r21, float r22, float r23, int r24, boolean r25) {
        /*
            r18 = this;
            r7 = r18
            r8 = r21
            long r0 = com.trossense.az.l
            long r0 = r0 ^ r19
            r2 = 109534578831462(0x639f01a97066, double:5.4117272432315E-310)
            long r2 = r2 ^ r0
            r4 = 32
            long r5 = r2 >>> r4
            int r9 = (int) r5
            long r4 = r2 << r4
            r6 = 48
            long r4 = r4 >>> r6
            int r10 = (int) r4
            long r2 = r2 << r6
            long r2 = r2 >>> r6
            int r11 = (int) r2
            r2 = 81744459067972(0x4a589d71f244, double:4.0387128963361E-310)
            long r12 = r0 ^ r2
            int[] r14 = b()
            if (r8 == 0) goto Lab
            boolean r0 = r21.isEmpty()
            if (r0 == 0) goto L31
            goto Lab
        L31:
            r0 = 0
            r15 = r22
            r6 = r0
        L35:
            int r0 = r21.length()
            if (r6 >= r0) goto Laa
            char r0 = r8.charAt(r6)
            short r1 = (short) r10
            com.trossense.a0 r5 = r7.a(r9, r1, r11, r0)
            if (r25 == 0) goto L90
            int r0 = android.graphics.Color.alpha(r24)
            r1 = 200(0xc8, float:2.8E-43)
            if (r0 >= r1) goto L4f
            goto L55
        L4f:
            int r0 = com.trossense.az.b
            int r0 = android.graphics.Color.alpha(r0)
        L55:
            int r1 = com.trossense.az.b
            int r2 = android.graphics.Color.red(r1)
            int r3 = android.graphics.Color.green(r1)
            int r1 = android.graphics.Color.blue(r1)
            int r16 = android.graphics.Color.argb(r0, r2, r3, r1)
            int r0 = r7.c
            float r1 = (float) r0
            r2 = 1018712556(0x3cb851ec, float:0.0225)
            float r1 = r1 * r2
            float r4 = r15 + r1
            float r0 = (float) r0
            float r0 = r0 * r2
            float r17 = r23 + r0
            r0 = r18
            r1 = r12
            r3 = r5
            r19 = r5
            r5 = r17
            r17 = r6
            r6 = r16
            r0.a(r1, r3, r4, r5, r6)
            r3 = r19
            r4 = r15
            r5 = r23
            r6 = r24
            r0.a(r1, r3, r4, r5, r6)
            if (r14 != 0) goto La1
            goto L94
        L90:
            r19 = r5
            r17 = r6
        L94:
            r0 = r18
            r1 = r12
            r3 = r19
            r4 = r15
            r5 = r23
            r6 = r24
            r0.a(r1, r3, r4, r5, r6)
        La1:
            r0 = r19
            float r0 = r0.b
            float r15 = r15 + r0
            int r6 = r17 + 1
            if (r14 != 0) goto L35
        Laa:
            return r15
        Lab:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.az.b(long, java.lang.String, float, float, int, boolean):float");
    }

    public float b(String str, long j, float f, float f2, int i) {
        return b((l ^ j) ^ 76558389843869L, str, f, f2, i, false);
    }
}
