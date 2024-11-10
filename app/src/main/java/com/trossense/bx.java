package com.trossense;

import com.trossense.clients.TrosSense;
import com.trossense.sdk.PointerHolder;
import com.trossense.sdk.component.MoveInputComponent;
import com.trossense.sdk.entity.type.EntityLocalPlayer;
import com.trossense.sdk.math.Vec3f;
import java.lang.invoke.MethodHandles;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.bedrock.packet.MovePlayerPacket;

@cg(a = "Fly", b = b.Movement, c = "飞行")
/* loaded from: classes3.dex */
public class bx extends bm {
    public static bx m;
    private static final long q = dj.a(1040031588054364570L, -7179294668722953662L, MethodHandles.lookup().lookupClass()).a(226969256436142L);
    private static final String[] r;
    private final ct j;
    private final ct k;
    private final cr l;
    private final cp n;
    private boolean o;
    private boolean p;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0045. Please report as an issue. */
    static {
        int i;
        int i2;
        String[] strArr = new String[17];
        String str = "苟雘庅\u000f&_ZPtk4\u001aQDj~a?\n\r8UZMgg;\u0002cX\\k`\u0004歔骧飶衵\u0005,\\AWe\u0005,\\AWe\u00078QFPbh;\u0003&I\\\u0002橏弿\u0004埬盄逷废\u0005=@GVh\u0003&I\\\u0004#_L\\\u00078QFPbh;\u0002矂秋";
        int length = "苟雘庅\u000f&_ZPtk4\u001aQDj~a?\n\r8UZMgg;\u0002cX\\k`\u0004歔骧飶衵\u0005,\\AWe\u0005,\\AWe\u00078QFPbh;\u0003&I\\\u0002橏弿\u0004埬盄逷废\u0005=@GVh\u0003&I\\\u0004#_L\\\u00078QFPbh;\u0002矂秋".length();
        char c = 3;
        int i3 = 0;
        int i4 = -1;
        while (true) {
            int i5 = 97;
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
                        str = "气帍遹廑\u0002馹茷";
                        length = "气帍遹廑\u0002馹茷".length();
                        c = 4;
                        i2 = -1;
                        i3 = i;
                        i5 = 47;
                        i6 = i2 + 1;
                        substring = str.substring(i6, i6 + c);
                        z = false;
                        break;
                }
                strArr[i3] = b;
                i2 = i6 + c;
                if (i2 >= length) {
                    r = strArr;
                    return;
                }
                c = str.charAt(i2);
                i3 = i;
                i5 = 47;
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
    public bx(long r29) {
        /*
            Method dump skipped, instructions count: 236
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.bx.<init>(long):void");
    }

    private static String b(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 15;
                    break;
                case 1:
                    i2 = 81;
                    break;
                case 2:
                    i2 = 73;
                    break;
                case 3:
                    i2 = 88;
                    break;
                case 4:
                    i2 = 111;
                    break;
                case 5:
                    i2 = 101;
                    break;
                default:
                    i2 = 59;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] b(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ ';');
        }
        return charArray;
    }

    @bk
    public void a(bb bbVar) {
        long j = q ^ 80676720343849L;
        long j2 = 121950968868066L ^ j;
        long j3 = 64663849229192L ^ j;
        long j4 = j ^ 71687086062240L;
        if (bbVar.e() instanceof com.trossense.sdk.ad) {
            com.trossense.sdk.ad adVar = (com.trossense.sdk.ad) bbVar.e();
            EntityLocalPlayer localPlayer = TrosSense.INSTANCE.getLocalPlayer();
            if (localPlayer == null) {
                return;
            }
            if (this.o) {
                a6.c(j2, localPlayer);
                a6.a(j3, localPlayer, 9999.0f);
                this.o = false;
            }
            if (!adVar.getDelta().equals(Vector3f.ZERO)) {
                a6.a(j4, localPlayer, adVar.getPosition());
            }
            Vector3f position = adVar.getPosition();
            adVar.setPosition(Vector3f.from(position.getX(), a6.a() - 0.25f, position.getZ()));
        }
    }

    @bk
    public void a(bg bgVar) {
        String o = b2.o();
        EntityLocalPlayer a = bgVar.a();
        if (this.n.e().booleanValue()) {
            a.a(9, true);
        }
        MoveInputComponent ad = a.ad();
        double d = (a.L().y + 90.0f) * 0.017453292f;
        float sin = (float) Math.sin(d);
        float cos = (float) Math.cos(d);
        float f = ad.moveForward;
        float f2 = ad.moveSide;
        float floatValue = this.k.e().floatValue();
        float f3 = ((f * cos) + (f2 * sin)) * floatValue;
        float f4 = ((f * sin) - (f2 * cos)) * floatValue;
        float floatValue2 = this.j.e().floatValue();
        float f5 = (ad.isJumpDown || ad.isFlyingAscendDown) ? 0.0f + floatValue2 : 0.0f;
        if (ad.isSneakDown || ad.isFlyingDescendDown) {
            f5 -= floatValue2;
        }
        a.d(new Vec3f(f3, f5, f4));
        if (o == null) {
            PointerHolder.b(new int[3]);
        }
    }

    @Override // com.trossense.bm
    public void j(long j) {
        String o = b2.o();
        this.o = true;
        if (this.l.j(j ^ 29096378907956L).a.equals(r[5])) {
            this.p = true;
            a4.b = true;
            a4.a((Class<?>[]) new Class[]{MovePlayerPacket.class, com.trossense.sdk.ad.class});
            if (o != null) {
                return;
            }
        }
        this.p = false;
    }

    @Override // com.trossense.bm
    public void k(long j) {
        EntityLocalPlayer localPlayer;
        long j2 = j ^ 69173392464839L;
        if (this.p) {
            a4.b = false;
            a4.a();
        }
        if (!this.l.j(j2).a.equals(r[7]) || (localPlayer = TrosSense.INSTANCE.getLocalPlayer()) == null) {
            return;
        }
        localPlayer.d(new Vec3f(0.0f, 0.0f, 0.0f));
    }
}
