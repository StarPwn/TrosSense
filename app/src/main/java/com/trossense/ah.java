package com.trossense;

import android.graphics.Color;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public class ah extends ae {
    private static az s;
    private static az t;
    private static az u;
    private static float v;
    private static final long x;
    private t q;
    private cr r;
    private List<u> w;

    static {
        long a = dj.a(8200121645992178425L, -6250570531645085500L, MethodHandles.lookup().lookupClass()).a(165030751960074L);
        x = a;
        long j = (a ^ 27953459777959L) ^ 94771971265466L;
        String[] strArr = new String[3];
        int length = "`By\nD\u0011\u0004m\tc\u001fA\b}Dx\u0005\t\f\u0004r\f`By\nD\u0011\u0004m\tc\u001fA".length();
        int i = -1;
        char c = '\f';
        int i2 = 0;
        while (true) {
            int i3 = i + 1;
            int i4 = c + i3;
            int i5 = i2 + 1;
            strArr[i2] = a(12, a("`By\nD\u0011\u0004m\tc\u001fA\b}Dx\u0005\t\f\u0004r\f`By\nD\u0011\u0004m\tc\u001fA".substring(i3, i4)));
            if (i4 >= length) {
                s = a1.a(strArr[1], 40, j);
                t = a1.a(strArr[2], 128, j);
                u = a1.a(strArr[0], 56, j);
                v = 100.0f;
                return;
            }
            i2 = i5;
            c = "`By\nD\u0011\u0004m\tc\u001fA\b}Dx\u0005\t\f\u0004r\f`By\nD\u0011\u0004m\tc\u001fA".charAt(i4);
            i = i4;
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ah(cr crVar, long j) {
        super(0.0f, 0.0f, crVar);
        long j2 = j ^ x;
        this.r = crVar;
        this.d = 360.0f;
        t tVar = new t(j2 ^ 84332271505991L, this, this.d - 20.0f, 50.0f, crVar, this);
        this.q = tVar;
        a(tVar);
        t.a(this.q).a(0L);
        n(j2 ^ 14359945027032L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static t a(ah ahVar) {
        return ahVar.q;
    }

    private static String a(int i, char[] cArr) {
        int length = cArr.length;
        for (int i2 = 0; length > i2; i2++) {
            char c = cArr[i2];
            int i3 = 43;
            switch (i2 % 7) {
                case 0:
                    i3 = 24;
                    break;
                case 1:
                case 4:
                    break;
                case 2:
                    i3 = 27;
                    break;
                case 3:
                    i3 = 103;
                    break;
                case 5:
                    i3 = 116;
                    break;
                default:
                    i3 = 124;
                    break;
            }
            cArr[i2] = (char) (c ^ (i ^ i3));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ '|');
        }
        return charArray;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static cr b(ah ahVar) {
        return ahVar.r;
    }

    private void n(long j) {
        int i;
        long j2 = x ^ j;
        long j3 = j2 ^ 72533762098170L;
        long j4 = j2 ^ 37595493440251L;
        int i2 = (int) (j4 >>> 32);
        int i3 = (int) ((j4 << 32) >>> 48);
        int i4 = (int) ((j4 << 48) >>> 48);
        this.w = new ArrayList();
        List<cu> l = this.r.l();
        int i5 = 0;
        while (i5 < l.size()) {
            if (i5 != this.r.k()) {
                i = i5;
                u uVar = new u(j3, this, this.d - 20.0f, 40.0f, l.get(i5).a(i2, (short) i3, i4), this);
                uVar.a(false);
                a(uVar);
                this.w.add(uVar);
            } else {
                i = i5;
            }
            i5 = i + 1;
        }
    }

    private void o(long j) {
        long j2 = j ^ x;
        long j3 = 51386224458137L ^ j2;
        int i = (int) (j3 >>> 32);
        int i2 = (int) ((j3 << 32) >>> 48);
        int i3 = (int) ((j3 << 48) >>> 48);
        long j4 = j2 ^ 122381738387871L;
        if (this.w.size() != this.r.l().size() - 1) {
            ArrayList arrayList = new ArrayList();
            for (p pVar : this.l) {
                if (pVar instanceof u) {
                    arrayList.add(pVar);
                }
                this.l.removeAll(arrayList);
            }
            n(j4);
            return;
        }
        List<cu> l = this.r.l();
        int i4 = 0;
        for (int i5 = 0; i5 < l.size(); i5++) {
            if (i5 != this.r.k()) {
                this.w.get(i4).a(l.get(i5).a(i, (short) i2, i3));
                i4++;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static az p() {
        return t;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static az q() {
        return s;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:20:0x00a5 A[EDGE_INSN: B:20:0x00a5->B:21:0x00a5 BREAK  A[LOOP:0: B:5:0x0057->B:19:?], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0126 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:47:? A[LOOP:2: B:36:0x00f4->B:47:?, LOOP_END, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:9:0x0081  */
    /* JADX WARN: Type inference failed for: r16v4, types: [com.trossense.p] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:41:0x006a -> B:8:0x007f). Please report as a decompilation issue!!! */
    @Override // com.trossense.p, com.trossense.aq
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void a(long r22) {
        /*
            Method dump skipped, instructions count: 295
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.ah.a(long):void");
    }

    @Override // com.trossense.p
    public void d(long j) {
        long j2 = j ^ 0;
        u.a(this.r.c(j ^ 37087146222550L), j ^ 121281231864540L, this.a + 20.0f, this.b + 15.0f, 5.0f, 0.5f, -1);
        da.a(this.a + 10.0f, this.b + v, j ^ 111589528524585L, this.d - 20.0f, this.e - v, 15.0f, Color.rgb(45, 45, 45));
        this.q.d(j2);
        for (u uVar : this.w) {
            if (uVar.i()) {
                uVar.d(j2);
            }
        }
    }
}
