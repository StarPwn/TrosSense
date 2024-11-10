package com.trossense;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class aa extends p {
    private static final String[] r;
    private float o;
    private ag p;
    final ag q;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0024. Please report as an issue. */
    static {
        int i;
        int i2;
        String[] strArr = new String[6];
        String str = "Wy\\\u000faV0ruYH\u0007z.;\u0003囡完艀\u0006K\u007fQ\u0003$g\u0004镤挟刵挊";
        int length = "Wy\\\u000faV0ruYH\u0007z.;\u0003囡完艀\u0006K\u007fQ\u0003$g\u0004镤挟刵挊".length();
        char c = 15;
        int i3 = 0;
        int i4 = -1;
        while (true) {
            int i5 = 116;
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
                        str = "Rl@\u001d8a0\u0002彩虴";
                        length = "Rl@\u001d8a0\u0002彩虴".length();
                        c = 7;
                        i2 = -1;
                        i3 = i;
                        i5 = 111;
                        i6 = i2 + 1;
                        substring = str.substring(i6, i6 + c);
                        z = false;
                        break;
                }
                strArr[i3] = a;
                i2 = i6 + c;
                if (i2 >= length) {
                    r = strArr;
                    return;
                }
                c = str.charAt(i2);
                i3 = i;
                i5 = 111;
                i6 = i2 + 1;
                substring = str.substring(i6, i6 + c);
                z = false;
            }
            c = str.charAt(i4);
            i3 = i;
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public aa(ag agVar, ag agVar2) {
        super(0.0f, 0.0f);
        this.q = agVar;
        this.p = agVar2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float a(aa aaVar, float f) {
        aaVar.o = f;
        return f;
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 111;
                    break;
                case 1:
                    i2 = 98;
                    break;
                case 2:
                    i2 = 70;
                    break;
                case 3:
                    i2 = 28;
                    break;
                case 4:
                    i2 = 53;
                    break;
                case 5:
                    i2 = 97;
                    break;
                default:
                    i2 = 40;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ '(');
        }
        return charArray;
    }

    @Override // com.trossense.p
    public void a(float f, float f2, long j, long j2) {
        String[] m = ae.m();
        if (ag.a(this.p) && j > 300) {
            ag.b(this.p).a(!ag.b(this.p).i());
            if (m != null) {
                return;
            }
        }
        ag.a(this.p, !ag.a(r1));
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0063  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x007c  */
    @Override // com.trossense.p
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void d(long r19) {
        /*
            r18 = this;
            r0 = r18
            r1 = 33376162975005(0x1e5afe568d1d, double:1.64900155159485E-310)
            long r1 = r19 ^ r1
            r3 = 111589528524585(0x657d76355729, double:5.51325524796193E-310)
            long r7 = r19 ^ r3
            r3 = 91228529291838(0x52f8cc14f23e, double:4.5072882243719E-310)
            long r3 = r19 ^ r3
            r5 = 121281231864540(0x6e4dfcbe42dc, double:5.9920890149576E-310)
            long r13 = r19 ^ r5
            float r5 = r0.a
            float r6 = r0.b
            float r9 = r0.d
            float r10 = r0.e
            float r11 = r0.o
            com.trossense.ag r12 = r0.p
            com.trossense.cq r12 = com.trossense.ag.b(r12)
            java.lang.Integer r12 = r12.m(r1)
            int r12 = r12.intValue()
            com.trossense.da.a(r5, r6, r7, r9, r10, r11, r12)
            com.trossense.clients.TrosSense r5 = com.trossense.clients.TrosSense.INSTANCE
            boolean r5 = r5.isEnglishLanguage
            if (r5 == 0) goto L45
            java.lang.String[] r5 = com.trossense.aa.r
            r6 = 0
            r6 = r5[r6]
            goto L4a
        L45:
            java.lang.String[] r5 = com.trossense.aa.r
            r6 = 3
            r6 = r5[r6]
        L4a:
            r7 = 0
            int r7 = (r19 > r7 ? 1 : (r19 == r7 ? 0 : -1))
            if (r7 <= 0) goto L5e
            com.trossense.clients.TrosSense r5 = com.trossense.clients.TrosSense.INSTANCE
            boolean r5 = r5.isEnglishLanguage
            if (r5 == 0) goto L5c
            java.lang.String[] r5 = com.trossense.aa.r
            r8 = 4
            r8 = r5[r8]
            goto L61
        L5c:
            java.lang.String[] r5 = com.trossense.aa.r
        L5e:
            r8 = 5
            r8 = r5[r8]
        L61:
            if (r7 <= 0) goto L72
            com.trossense.clients.TrosSense r5 = com.trossense.clients.TrosSense.INSTANCE
            boolean r5 = r5.isEnglishLanguage
            if (r5 == 0) goto L6c
            java.lang.String[] r5 = com.trossense.aa.r
            goto L72
        L6c:
            java.lang.String[] r5 = com.trossense.aa.r
            r9 = 1
            r5 = r5[r9]
            goto L75
        L72:
            r9 = 2
            r5 = r5[r9]
        L75:
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            if (r7 < 0) goto L8d
            java.lang.StringBuilder r9 = r9.append(r6)
            com.trossense.ag r6 = r0.q
            com.trossense.cq r6 = com.trossense.ag.b(r6)
            boolean r6 = r6.i()
            if (r6 == 0) goto L8e
            r6 = r5
        L8d:
            r8 = r6
        L8e:
            java.lang.StringBuilder r5 = r9.append(r8)
            java.lang.String r10 = r5.toString()
            com.trossense.ag r5 = r0.p
            com.trossense.cq r5 = com.trossense.ag.b(r5)
            java.lang.Integer r1 = r5.m(r1)
            int r1 = r1.intValue()
            int r1 = com.trossense.c9.a(r1)
            com.trossense.az r9 = com.trossense.ag.n()
            float r2 = r0.a
            float r5 = r0.d
            r6 = 1073741824(0x40000000, float:2.0)
            float r5 = r5 / r6
            float r2 = r2 + r5
            com.trossense.az r5 = com.trossense.ag.n()
            float r3 = r5.a(r3, r10)
            r4 = 1082130432(0x40800000, float:4.0)
            float r3 = r3 / r4
            float r2 = r2 - r3
            float r3 = r0.b
            float r15 = r0.e
            r16 = 1056964608(0x3f000000, float:0.5)
            r4 = 1132396544(0x437f0000, float:255.0)
            com.trossense.ag r5 = r0.p
            com.trossense.df r5 = r5.A
            float r5 = r5.b()
            float r5 = r5 * r4
            int r4 = (int) r5
            int r5 = android.graphics.Color.red(r1)
            int r6 = android.graphics.Color.green(r1)
            int r1 = android.graphics.Color.blue(r1)
            int r17 = android.graphics.Color.argb(r4, r5, r6, r1)
            r11 = r13
            r13 = r2
            r14 = r3
            r9.a(r10, r11, r13, r14, r15, r16, r17)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.aa.d(long):void");
    }
}
