package com.trossense;

import com.trossense.clients.TrosSense;
import com.trossense.sdk.entity.type.EntityActor;
import com.trossense.sdk.entity.type.EntityLocalPlayer;
import com.trossense.sdk.math.Vec2f;
import java.lang.invoke.MethodHandles;
import java.util.function.Consumer;

@cg(a = "HitBox", b = b.Combat, c = "碰撞箱")
/* loaded from: classes3.dex */
public class bo extends bm {
    private static final long m = dj.a(376504656879844099L, 4145829564662249600L, MethodHandles.lookup().lookupClass()).a(22603051681526L);
    private static final String[] n;
    private final ct j;
    private final ct k;
    private final ct l;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0044. Please report as an issue. */
    static {
        int i;
        int i2;
        String[] strArr = new String[6];
        String str = "x\u00017h#\u0005y\t#j\u001e\u0004所控趐禶\u0004r輜屷寵";
        int length = "x\u00017h#\u0005y\t#j\u001e\u0004所控趐禶\u0004r輜屷寵".length();
        char c = 5;
        int i3 = 0;
        int i4 = -1;
        while (true) {
            int i5 = 80;
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
                        str = "-轂尩宫\u0005&_i6|";
                        length = "-轂尩宫\u0005&_i6|".length();
                        c = 4;
                        i2 = -1;
                        i3 = i;
                        i5 = 14;
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
                i5 = 14;
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
    public bo(long r23) {
        /*
            r22 = this;
            r10 = r22
            long r0 = com.trossense.bo.m
            long r0 = r0 ^ r23
            r2 = 119677217091064(0x6cd886032df8, double:5.91284015545796E-310)
            long r11 = r0 ^ r2
            r2 = 29314811557020(0x1aa962edf49c, double:1.44834413046335E-310)
            long r0 = r0 ^ r2
            r10.<init>(r0)
            com.trossense.ct r13 = new com.trossense.ct
            java.lang.String[] r14 = com.trossense.bo.n
            r0 = 0
            r1 = r14[r0]
            r0 = 4
            r2 = r14[r0]
            r15 = 3
            java.lang.Integer r16 = java.lang.Integer.valueOf(r15)
            r17 = 1
            java.lang.Integer r18 = java.lang.Integer.valueOf(r17)
            r0 = 6
            java.lang.Integer r19 = java.lang.Integer.valueOf(r0)
            r3 = 4591870180066957722(0x3fb999999999999a, double:0.1)
            java.lang.Double r20 = java.lang.Double.valueOf(r3)
            r0 = r13
            r3 = r22
            r4 = r11
            r6 = r16
            r7 = r18
            r8 = r19
            r9 = r20
            r0.<init>(r1, r2, r3, r4, r6, r7, r8, r9)
            r10.j = r13
            com.trossense.ct r13 = new com.trossense.ct
            r21 = 5
            r1 = r14[r21]
            r2 = r14[r15]
            r0 = r13
            r0.<init>(r1, r2, r3, r4, r6, r7, r8, r9)
            r10.k = r13
            com.trossense.ct r13 = new com.trossense.ct
            r1 = r14[r17]
            r0 = 2
            r2 = r14[r0]
            java.lang.Integer r6 = java.lang.Integer.valueOf(r21)
            r0 = 10
            java.lang.Integer r8 = java.lang.Integer.valueOf(r0)
            r0 = r13
            r7 = r16
            r0.<init>(r1, r2, r3, r4, r6, r7, r8, r9)
            r10.l = r13
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.bo.<init>(long):void");
    }

    private static String b(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 123;
                    break;
                case 1:
                    i2 = 56;
                    break;
                case 2:
                    i2 = 29;
                    break;
                case 3:
                    i2 = 93;
                    break;
                case 4:
                    i2 = 43;
                    break;
                case 5:
                    i2 = 81;
                    break;
                default:
                    i2 = 26;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] b(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 26);
        }
        return charArray;
    }

    @bk
    public void a(bg bgVar) {
        long j = (m ^ 79834242392816L) ^ 82542036563300L;
        EntityLocalPlayer a = bgVar.a();
        final float floatValue = this.j.e().floatValue();
        final float floatValue2 = this.k.e().floatValue();
        c8.a(j, a, this.l.e().floatValue()).forEach(new Consumer() { // from class: com.trossense.bo$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((EntityActor) obj).c(new Vec2f(floatValue, floatValue2));
            }
        });
    }

    @Override // com.trossense.bm
    public void k(long j) {
        long j2 = j ^ 105071338891096L;
        EntityLocalPlayer localPlayer = TrosSense.INSTANCE.getLocalPlayer();
        if (localPlayer != null) {
            c8.a(j2, localPlayer, 2.1474836E9f).forEach(new Consumer() { // from class: com.trossense.bo$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ((EntityActor) obj).c(new Vec2f(0.6f, 1.8f));
                }
            });
        }
    }
}
