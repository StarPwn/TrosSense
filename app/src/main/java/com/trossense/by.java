package com.trossense;

import com.trossense.sdk.component.MoveInputComponent;
import com.trossense.sdk.entity.type.EntityLocalPlayer;
import java.lang.invoke.MethodHandles;

@cg(a = "KeepSprint", b = b.Movement, c = "自动疾跑")
/* loaded from: classes3.dex */
public class by extends bm {
    private static final long k = dj.a(3438077452609508340L, 5113967580783132450L, MethodHandles.lookup().lookupClass()).a(191446809415827L);
    private static final String[] l;
    private final cp j;

    static {
        String[] strArr = new String[2];
        int length = "]tw\u001b\u0003党斠佔".length();
        int i = 0;
        char c = 4;
        int i2 = -1;
        while (true) {
            int i3 = i2 + 1;
            int i4 = c + i3;
            int i5 = i + 1;
            strArr[i] = b(28, b("]tw\u001b\u0003党斠佔".substring(i3, i4)));
            if (i4 >= length) {
                l = strArr;
                return;
            } else {
                i2 = i4;
                c = "]tw\u001b\u0003党斠佔".charAt(i4);
                i = i5;
            }
        }
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public by(long r12) {
        /*
            r11 = this;
            long r0 = com.trossense.by.k
            long r12 = r12 ^ r0
            r0 = 53362631048615(0x308874aafda7, double:2.6364642772822E-310)
            long r0 = r0 ^ r12
            r2 = 130354281152021(0x768e789f7e15, double:6.44035721055447E-310)
            long r12 = r12 ^ r2
            r2 = 16
            long r4 = r12 >>> r2
            r2 = 48
            long r12 = r12 << r2
            long r12 = r12 >>> r2
            int r12 = (int) r12
            r11.<init>(r0)
            com.trossense.cp r13 = new com.trossense.cp
            short r6 = (short) r12
            java.lang.String[] r12 = com.trossense.by.l
            r0 = 0
            r7 = r12[r0]
            r1 = 1
            r8 = r12[r1]
            java.lang.Boolean r10 = java.lang.Boolean.valueOf(r0)
            r3 = r13
            r9 = r11
            r3.<init>(r4, r6, r7, r8, r9, r10)
            r11.j = r13
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.by.<init>(long):void");
    }

    private static String b(int i, char[] cArr) {
        int length = cArr.length;
        for (int i2 = 0; length > i2; i2++) {
            char c = cArr[i2];
            int i3 = 5;
            switch (i2 % 7) {
                case 0:
                    i3 = 46;
                    break;
                case 1:
                case 2:
                    break;
                case 3:
                    i3 = 110;
                    break;
                case 4:
                    i3 = 100;
                    break;
                case 5:
                    i3 = 29;
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
        EntityLocalPlayer a = bgVar.a();
        MoveInputComponent ad = a.ad();
        if (a.S()) {
            return;
        }
        if (ad.moveForward == 0.0f && ad.moveSide == 0.0f) {
            return;
        }
        if (this.j.e().booleanValue() || ad.moveForward > 0.8d) {
            a.a(true);
        }
    }
}
