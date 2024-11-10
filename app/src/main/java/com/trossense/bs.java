package com.trossense;

import com.trossense.clients.TrosSense;
import com.trossense.sdk.entity.type.EntityLocalPlayer;
import java.lang.invoke.MethodHandles;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.bedrock.packet.SetEntityMotionPacket;

@cg(a = "Velocity", b = b.Combat, c = "反击退")
/* loaded from: classes3.dex */
public class bs extends bm {
    private static final long m = dj.a(8865481197073531606L, 6170005006994973069L, MethodHandles.lookup().lookupClass()).a(140534291064708L);
    private static final String[] n;
    private final ct j;
    private final ct k;
    private final cp l;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0044. Please report as an issue. */
    static {
        int i;
        int i2;
        String[] strArr = new String[6];
        String str = "垓盳\u0002氥年\n^a\u001bm{R&di\u0019\u0004叻圯稇习";
        int length = "垓盳\u0002氥年\n^a\u001bm{R&di\u0019\u0004叻圯稇习".length();
        char c = 2;
        int i3 = 0;
        int i4 = -1;
        while (true) {
            int i5 = 82;
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
                        str = "\u00067P{\u0019\u0010x:9N\b\u0018=Pf\n\u001cw\"";
                        length = "\u00067P{\u0019\u0010x:9N\b\u0018=Pf\n\u001cw\"".length();
                        c = '\n';
                        i2 = -1;
                        i3 = i;
                        i5 = 13;
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
                i5 = 13;
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
    public bs(long r23) {
        /*
            r22 = this;
            r10 = r22
            long r0 = com.trossense.bs.m
            long r0 = r0 ^ r23
            r2 = 29914901772729(0x1b351b17f9b9, double:1.4779925264621E-310)
            long r11 = r0 ^ r2
            r2 = 120143119720669(0x6d44fff920dd, double:5.93585880381745E-310)
            long r2 = r2 ^ r0
            r4 = 47566558110575(0x2b42f3cca36f, double:2.35010022533464E-310)
            long r0 = r0 ^ r4
            r4 = 16
            long r13 = r0 >>> r4
            r4 = 48
            long r0 = r0 << r4
            long r0 = r0 >>> r4
            int r15 = (int) r0
            r10.<init>(r2)
            com.trossense.ct r9 = new com.trossense.ct
            java.lang.String[] r16 = com.trossense.bs.n
            r0 = 5
            r1 = r16[r0]
            r17 = 0
            java.lang.Integer r18 = java.lang.Integer.valueOf(r17)
            r2 = r16[r17]
            r19 = 1
            java.lang.Integer r20 = java.lang.Integer.valueOf(r19)
            r3 = 4576918229304087675(0x3f847ae147ae147b, double:0.01)
            java.lang.Double r21 = java.lang.Double.valueOf(r3)
            r0 = r9
            r3 = r22
            r4 = r11
            r6 = r18
            r7 = r18
            r8 = r20
            r23 = r13
            r13 = r9
            r9 = r21
            r0.<init>(r1, r2, r3, r4, r6, r7, r8, r9)
            r10.j = r13
            com.trossense.ct r13 = new com.trossense.ct
            r0 = 4
            r1 = r16[r0]
            r2 = r16[r19]
            r0 = r13
            r0.<init>(r1, r2, r3, r4, r6, r7, r8, r9)
            r10.k = r13
            com.trossense.cp r8 = new com.trossense.cp
            short r3 = (short) r15
            r0 = 2
            r4 = r16[r0]
            r0 = 3
            r5 = r16[r0]
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r17)
            r0 = r8
            r1 = r23
            r6 = r22
            r0.<init>(r1, r3, r4, r5, r6, r7)
            r10.l = r8
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.bs.<init>(long):void");
    }

    private static String b(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 67;
                    break;
                case 1:
                    i2 = 85;
                    break;
                case 2:
                    i2 = 47;
                    break;
                case 3:
                    i2 = 31;
                    break;
                case 4:
                    i2 = 110;
                    break;
                case 5:
                    i2 = 114;
                    break;
                default:
                    i2 = 27;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] b(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 27);
        }
        return charArray;
    }

    @bk
    public void a(ba baVar) {
        if (baVar.e() instanceof SetEntityMotionPacket) {
            SetEntityMotionPacket setEntityMotionPacket = (SetEntityMotionPacket) baVar.e();
            EntityLocalPlayer localPlayer = TrosSense.INSTANCE.getLocalPlayer();
            if (localPlayer == null || setEntityMotionPacket.getRuntimeEntityId() != localPlayer.C()) {
                return;
            }
            if (this.l.e().booleanValue() && localPlayer.A()) {
                return;
            }
            Vector3f motion = setEntityMotionPacket.getMotion();
            setEntityMotionPacket.setMotion(Vector3f.from(this.k.e().floatValue() * motion.getX(), this.j.e().floatValue() * motion.getY(), this.k.e().floatValue() * motion.getZ()));
        }
    }
}
