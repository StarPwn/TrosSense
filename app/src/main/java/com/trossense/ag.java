package com.trossense;

import java.lang.invoke.MethodHandles;

/* loaded from: classes3.dex */
public class ag extends ae {
    private static az C = null;
    private static az D = null;
    private static final long E;
    private static final float r = 120.0f;
    private static final float s = 30.0f;
    public df A;
    public df B;
    private cq q;
    private aa t;
    private z u;
    private s v;
    private y w;
    private aj x;
    private aj y;
    private boolean z;

    static {
        long a = dj.a(2408818354104153696L, 4951317410546633369L, MethodHandles.lookup().lookupClass()).a(49769179675740L);
        E = a;
        long j = (a ^ 101399556253110L) ^ 108611095271689L;
        String[] strArr = new String[2];
        int length = "\u001fs^\u0005!Kx\u00128D\u0010$\f\u001fs^\u0005!Kx\u00128D\u0010$".length();
        char c = '\f';
        int i = -1;
        int i2 = 0;
        while (true) {
            int i3 = i + 1;
            int i4 = c + i3;
            int i5 = i2 + 1;
            strArr[i2] = a(6, a("\u001fs^\u0005!Kx\u00128D\u0010$\f\u001fs^\u0005!Kx\u00128D\u0010$".substring(i3, i4)));
            if (i4 >= length) {
                C = a1.a(strArr[1], 128, j);
                D = a1.a(strArr[0], 56, j);
                return;
            } else {
                i2 = i5;
                i = i4;
                c = "\u001fs^\u0005!Kx\u00128D\u0010$\f\u001fs^\u0005!Kx\u00128D\u0010$".charAt(i4);
            }
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ag(cq cqVar, long j) {
        super(0.0f, 0.0f, cqVar);
        long j2 = j ^ E;
        long j3 = 20585636585919L ^ j2;
        long j4 = 22777247758918L ^ j2;
        long j5 = j2 ^ 15120920236173L;
        this.A = new df(d.Decelerate, 250L, j5);
        this.B = new df(d.Decelerate, 300L, j5);
        this.q = cqVar;
        this.d = 360.0f;
        this.z = false;
        this.A.a(0L);
        aa aaVar = new aa(this, this);
        this.t = aaVar;
        a(aaVar);
        z zVar = new z(this, this);
        this.u = zVar;
        a(zVar);
        s sVar = new s(this, this, j4);
        this.v = sVar;
        a(sVar);
        y yVar = new y(this, this);
        this.w = yVar;
        a(yVar);
        aj ajVar = new aj(cqVar.j(), j3);
        this.x = ajVar;
        a(ajVar);
        aj ajVar2 = new aj(cqVar.k(), j3);
        this.y = ajVar2;
        a(ajVar2);
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 109;
                    break;
                case 1:
                    i2 = 16;
                    break;
                case 2:
                    i2 = 54;
                    break;
                case 3:
                    i2 = 98;
                    break;
                case 4:
                    i2 = 68;
                    break;
                case 5:
                    i2 = 36;
                    break;
                default:
                    i2 = 10;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean a(ag agVar) {
        return agVar.z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean a(ag agVar, boolean z) {
        agVar.z = z;
        return z;
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ '\n');
        }
        return charArray;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static cq b(ag agVar) {
        return agVar.q;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float c(ag agVar) {
        return agVar.d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float d(ag agVar) {
        return agVar.d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static az n() {
        return D;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static az o() {
        return C;
    }

    /* JADX WARN: Code restructure failed: missing block: B:38:0x015c, code lost:            if (r12 == false) goto L43;     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x0173, code lost:            r12 = false;     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x01d1, code lost:            if (r2 == false) goto L66;     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x01e8, code lost:            r2 = false;     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x023d, code lost:            if (r2 == false) goto L87;     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x0251, code lost:            if (r2 == false) goto L93;     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x01e5, code lost:            if (r2 == false) goto L74;     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x0170, code lost:            if (r12 == false) goto L51;     */
    /* JADX WARN: Removed duplicated region for block: B:48:0x01c7  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x01f2  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0233  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x025d  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x029e  */
    /* JADX WARN: Removed duplicated region for block: B:75:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0247  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x01db  */
    @Override // com.trossense.p, com.trossense.aq
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void a(long r18) {
        /*
            Method dump skipped, instructions count: 677
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.ag.a(long):void");
    }

    @Override // com.trossense.p
    public void d(long j) {
        long j2 = j ^ 0;
        C.a(this.q.c(j ^ 37087146222550L), j ^ 121281231864540L, this.a + 10.0f, this.b, 70.0f, 0.25f, -1);
        this.t.d(j2);
        if (this.u.i()) {
            this.u.d(j2);
        }
        if (this.v.i()) {
            this.v.d(j2);
        }
        if (this.w.i()) {
            this.w.d(j2);
        }
        if (this.x.i()) {
            this.x.d(j2);
        }
        if (this.y.i()) {
            this.y.d(j2);
        }
    }
}
