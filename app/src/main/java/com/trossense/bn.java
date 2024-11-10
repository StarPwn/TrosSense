package com.trossense;

import com.trossense.sdk.entity.type.EntityActor;
import com.trossense.sdk.entity.type.EntityLocalPlayer;
import com.trossense.sdk.math.Vec2f;
import java.lang.invoke.MethodHandles;

@cg(a = "AimBot", b = b.Combat, c = "自瞄")
/* loaded from: classes3.dex */
public class bn extends bm {
    private static final long m = dj.a(5346286922825109911L, -3413428560100501376L, MethodHandles.lookup().lookupClass()).a(32062007804000L);
    private static final String[] n;
    private final ct j;
    private final ct k;
    private final ct l;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0044. Please report as an issue. */
    static {
        int i;
        int i2;
        String[] strArr = new String[6];
        String str = "扦掜趀禡\u0005_23}\u007f\u0012@2%:H\u001eWl'4utQp}68~\u0012@:3:H\u001eWl'4utQp}68~";
        int length = "扦掜趀禡\u0005_23}\u007f\u0012@2%:H\u001eWl'4utQp}68~\u0012@:3:H\u001eWl'4utQp}68~".length();
        char c = 4;
        int i3 = 0;
        int i4 = -1;
        while (true) {
            int i5 = 10;
            int i6 = i4 + 1;
            String substring = str.substring(i6, i6 + c);
            boolean z = -1;
            while (true) {
                String b = b(i5, b(substring));
                i = i3 + 1;
                switch (z) {
                    case false:
                        break;
                    default:
                        strArr[i3] = b;
                        i4 = i6 + c;
                        if (i4 < length) {
                            break;
                        }
                        str = "朂屓輾夡逊廘\u0006朂奻輾夡逊廘";
                        length = "朂屓輾夡逊廘\u0006朂奻輾夡逊廘".length();
                        c = 6;
                        i2 = -1;
                        i3 = i;
                        i5 = 5;
                        i6 = i2 + 1;
                        substring = str.substring(i6, i6 + c);
                        z = false;
                        break;
                }
                strArr[i3] = b;
                i2 = i6 + c;
                if (i2 >= length) {
                    n = strArr;
                    return;
                }
                c = str.charAt(i2);
                i3 = i;
                i5 = 5;
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
    public bn(long r18) {
        /*
            r17 = this;
            r10 = r17
            long r0 = com.trossense.bn.m
            long r0 = r0 ^ r18
            r2 = 26494378571549(0x1818b405f71d, double:1.3089962260115E-310)
            long r11 = r0 ^ r2
            r2 = 121398608211577(0x6e6950eb2e79, double:5.9978881770281E-310)
            long r0 = r0 ^ r2
            com.trossense.br.o()
            r10.<init>(r0)
            com.trossense.ct r13 = new com.trossense.ct
            java.lang.String[] r14 = com.trossense.bn.n
            r0 = 1
            java.lang.Integer r15 = java.lang.Integer.valueOf(r0)
            r1 = r14[r0]
            r0 = 0
            r2 = r14[r0]
            r9 = 3
            java.lang.Integer r7 = java.lang.Integer.valueOf(r9)
            r0 = 10
            java.lang.Integer r8 = java.lang.Integer.valueOf(r0)
            r3 = 4591870180066957722(0x3fb999999999999a, double:0.1)
            java.lang.Double r16 = java.lang.Double.valueOf(r3)
            r0 = r13
            r3 = r17
            r4 = r11
            r6 = r7
            r9 = r16
            r0.<init>(r1, r2, r3, r4, r6, r7, r8, r9)
            r10.j = r13
            com.trossense.ct r13 = new com.trossense.ct
            r0 = 2
            r1 = r14[r0]
            r0 = 5
            r2 = r14[r0]
            r0 = 180(0xb4, float:2.52E-43)
            java.lang.Integer r16 = java.lang.Integer.valueOf(r0)
            r0 = r13
            r6 = r16
            r7 = r15
            r8 = r16
            r9 = r15
            r0.<init>(r1, r2, r3, r4, r6, r7, r8, r9)
            r10.k = r13
            com.trossense.ct r9 = new com.trossense.ct
            r8 = 3
            r1 = r14[r8]
            r0 = 4
            r2 = r14[r0]
            r0 = r9
            r11 = r8
            r8 = r16
            r12 = r9
            r9 = r15
            r0.<init>(r1, r2, r3, r4, r6, r7, r8, r9)
            r10.l = r12
            com.trossense.bn$$ExternalSyntheticLambda0 r0 = new com.trossense.bn$$ExternalSyntheticLambda0
            r0.<init>()
            r12.a(r0)
            com.trossense.bn$$ExternalSyntheticLambda1 r0 = new com.trossense.bn$$ExternalSyntheticLambda1
            r0.<init>()
            r13.a(r0)
            int[] r0 = com.trossense.sdk.PointerHolder.s()
            if (r0 != 0) goto L8f
            int[] r0 = new int[r11]
            com.trossense.br.b(r0)
        L8f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.bn.<init>(long):void");
    }

    private static String b(int i, char[] cArr) {
        int length = cArr.length;
        for (int i2 = 0; length > i2; i2++) {
            char c = cArr[i2];
            int i3 = 16;
            switch (i2 % 7) {
                case 0:
                    i3 = 7;
                    break;
                case 1:
                    i3 = 89;
                    break;
                case 2:
                    i3 = 87;
                    break;
                case 3:
                case 4:
                    break;
                case 5:
                    i3 = 123;
                    break;
                default:
                    i3 = 41;
                    break;
            }
            cArr[i2] = (char) (c ^ (i ^ i3));
        }
        return new String(cArr).intern();
    }

    private static char[] b(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ ')');
        }
        return charArray;
    }

    @bk
    public void a(bg bgVar) {
        long j = m ^ 85069713339370L;
        long j2 = 82642244104537L ^ j;
        long j3 = j ^ 4603403626192L;
        long j4 = 80297466963184L ^ j;
        EntityLocalPlayer a = bgVar.a();
        EntityActor b = c8.b(a, this.j.e().floatValue(), j ^ 62337427996782L);
        if (b != null) {
            float a2 = (float) c3.a(this.l.e().doubleValue(), j3, this.k.e().doubleValue());
            Vec2f a3 = c6.a(a, j2, b.N());
            float a4 = c6.a(j4, a.L().y, a3.y, a2);
            float a5 = c6.a(j4, a.L().x, a3.x, a2);
            Vec2f L = a.L();
            a.d(new Vec2f(L.x - a5, a4 - L.y));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-trossense-bn, reason: not valid java name */
    public Number m179lambda$new$0$comtrossensebn(Number number) {
        return Double.valueOf(Math.min(number.doubleValue(), this.k.e().doubleValue()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$1$com-trossense-bn, reason: not valid java name */
    public Number m180lambda$new$1$comtrossensebn(Number number) {
        return Double.valueOf(Math.max(number.doubleValue(), this.l.e().doubleValue()));
    }
}
