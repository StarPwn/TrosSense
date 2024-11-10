package com.trossense;

import android.graphics.Color;
import com.trossense.sdk.PointerHolder;
import java.lang.invoke.MethodHandles;

/* loaded from: classes3.dex */
public class cq extends co<Integer> {
    private static final long p = dj.a(-846105124697481902L, 8474439181367322806L, MethodHandles.lookup().lookupClass()).a(234279464277487L);
    private static final String[] q;
    public ct i;
    public ct j;
    private float k;
    private float l;
    private float m;
    private boolean n;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0043. Please report as an issue. */
    static {
        int i;
        int i2;
        char c = 5;
        String[] strArr = new String[5];
        String str = "sPJ|!\f\u0005\u0010\u001da`\nSX\u0005\u001f+=\nsA[l7[\u0015IOA";
        int length = "sPJ|!\f\u0005\u0010\u001da`\nSX\u0005\u001f+=\nsA[l7[\u0015IOA".length();
        int i3 = 0;
        int i4 = -1;
        while (true) {
            int i5 = 16;
            int i6 = i4 + 1;
            String substring = str.substring(i6, i6 + c);
            boolean z = -1;
            while (true) {
                String a = a(i5, a(substring));
                i = i3 + 1;
                switch (z) {
                    case false:
                        break;
                    default:
                        strArr[i3] = a;
                        i4 = i6 + c;
                        if (i4 < length) {
                            break;
                        }
                        str = "渉叁选庆\u0005渉叁饧咬廚";
                        length = "渉叁选庆\u0005渉叁饧咬廚".length();
                        c = 4;
                        i2 = -1;
                        i3 = i;
                        i5 = 41;
                        i6 = i2 + 1;
                        substring = str.substring(i6, i6 + c);
                        z = false;
                        break;
                }
                strArr[i3] = a;
                i2 = i6 + c;
                if (i2 >= length) {
                    q = strArr;
                    return;
                }
                c = str.charAt(i2);
                i3 = i;
                i5 = 41;
                i6 = i2 + 1;
                substring = str.substring(i6, i6 + c);
                z = false;
            }
            c = str.charAt(i4);
            i3 = i;
        }
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public cq(short r22, char r23, int r24, java.lang.String r25, java.lang.String r26, com.trossense.bm r27, java.lang.Integer r28) {
        /*
            r21 = this;
            r7 = r21
            r0 = r22
            long r0 = (long) r0
            r2 = 48
            long r0 = r0 << r2
            r3 = r23
            long r3 = (long) r3
            long r2 = r3 << r2
            r4 = 16
            long r2 = r2 >>> r4
            long r0 = r0 | r2
            r2 = r24
            long r2 = (long) r2
            r4 = 32
            long r2 = r2 << r4
            long r2 = r2 >>> r4
            long r0 = r0 | r2
            long r2 = com.trossense.cq.p
            long r0 = r0 ^ r2
            r2 = 86382384565180(0x4e907758cbbc, double:4.26785686195026E-310)
            long r18 = r0 ^ r2
            r2 = 11751323724393(0xab01179ea69, double:5.805925345382E-311)
            long r3 = r0 ^ r2
            boolean r20 = com.trossense.co.g()
            r0 = r21
            r1 = r25
            r2 = r26
            r5 = r27
            r6 = r28
            r0.<init>(r1, r2, r3, r5, r6)
            com.trossense.ct r0 = new com.trossense.ct
            java.lang.String[] r1 = com.trossense.cq.q
            r2 = 0
            r9 = r1[r2]
            r3 = 3
            r10 = r1[r3]
            r3 = 15
            java.lang.Integer r14 = java.lang.Integer.valueOf(r3)
            r3 = 1
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)
            r4 = 40
            java.lang.Integer r16 = java.lang.Integer.valueOf(r4)
            r11 = 0
            r8 = r0
            r12 = r18
            r15 = r3
            r17 = r3
            r8.<init>(r9, r10, r11, r12, r14, r15, r16, r17)
            r7.i = r0
            com.trossense.ct r0 = new com.trossense.ct
            r4 = 2
            r9 = r1[r4]
            r4 = 4
            r10 = r1[r4]
            java.lang.Integer r15 = java.lang.Integer.valueOf(r2)
            r1 = 4591870180066957722(0x3fb999999999999a, double:0.1)
            java.lang.Double r17 = java.lang.Double.valueOf(r1)
            r8 = r0
            r14 = r3
            r16 = r3
            r8.<init>(r9, r10, r11, r12, r14, r15, r16, r17)
            r7.j = r0
            if (r20 == 0) goto L88
            r0 = 5
            int[] r0 = new int[r0]
            com.trossense.sdk.PointerHolder.b(r0)
        L88:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.cq.<init>(short, char, int, java.lang.String, java.lang.String, com.trossense.bm, java.lang.Integer):void");
    }

    private static String a(int i, char[] cArr) {
        int length = cArr.length;
        for (int i2 = 0; length > i2; i2++) {
            char c = cArr[i2];
            int i3 = 48;
            switch (i2 % 7) {
                case 0:
                case 1:
                    break;
                case 2:
                    i3 = 63;
                    break;
                case 3:
                    i3 = 9;
                    break;
                case 4:
                    i3 = 85;
                    break;
                case 5:
                    i3 = 42;
                    break;
                default:
                    i3 = 113;
                    break;
            }
            cArr[i2] = (char) (c ^ (i ^ i3));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 'q');
        }
        return charArray;
    }

    public void a(float f) {
        this.k = f;
    }

    public void a(int i, long j) {
        this.i.a(Integer.valueOf(i), (j ^ p) ^ 101978684262971L);
    }

    public void a(long j, Integer num) {
        long j2 = j ^ p;
        boolean g = co.g();
        float[] a = c9.a(Color.red(num.intValue()), Color.green(num.intValue()), Color.blue(num.intValue()), j2 ^ 13482676174861L, null);
        this.k = a[0];
        boolean z = true;
        this.l = a[1];
        this.m = a[2];
        super.a(num, (char) (r0 >>> 48), ((28504318838184L ^ j2) << 16) >>> 16);
        if (PointerHolder.s() == null) {
            if (j2 >= 0) {
                if (g) {
                    g = false;
                }
                co.b(z);
            }
            z = g;
            co.b(z);
        }
    }

    @Override // com.trossense.co
    public /* bridge */ /* synthetic */ void a(Object obj, char c, long j) {
        a((((j << 16) >>> 16) | (c << 48)) ^ 65370567752952L, (Integer) obj);
    }

    public void a(boolean z) {
        this.n = z;
    }

    public void b(float f, int i, long j) {
        this.j.a(Float.valueOf(f), ((((j << 32) >>> 32) | (i << 32)) ^ p) ^ 61468139885591L);
    }

    public void c(float f) {
        this.m = f;
    }

    public void d(float f) {
        this.l = f;
    }

    @Override // com.trossense.co
    public /* bridge */ /* synthetic */ Integer e() {
        return m((p ^ 39725034845859L) ^ 129058603112344L);
    }

    public boolean i() {
        return this.n;
    }

    public ct j() {
        return this.i;
    }

    public ct k() {
        return this.j;
    }

    public void l(long j) {
        if (this.n) {
            this.k = c9.a(this.i.e().intValue(), 1);
            this.l = this.j.e().floatValue();
            this.m = 1.0f;
        }
    }

    public Integer m(long j) {
        long j2 = j ^ p;
        long j3 = 95536132452080L ^ j2;
        l(j2 ^ 8276951682898L);
        return Integer.valueOf(c9.a(this.k, (char) (j3 >>> 48), (int) ((j3 << 16) >>> 32), this.l, this.m, (short) ((j3 << 48) >>> 48)));
    }

    public String n(long j) {
        int intValue = m((j ^ p) ^ 134125264646382L).intValue();
        return String.format(q[1], Integer.valueOf(Color.red(intValue)), Integer.valueOf(Color.green(intValue)), Integer.valueOf(Color.blue(intValue)));
    }

    public float o() {
        return this.m;
    }

    public float p() {
        return this.k;
    }

    public float q() {
        return this.l;
    }
}
