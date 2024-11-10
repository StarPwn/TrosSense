package com.trossense;

import com.trossense.sdk.entity.type.EntityLocalPlayer;
import com.trossense.sdk.math.Vec3f;
import com.trossense.sdk.math.Vec3i;
import java.lang.invoke.MethodHandles;
import org.cloudburstmc.protocol.bedrock.packet.MobEquipmentPacket;

@cg(a = "AntiVoid", b = b.Player, c = "反虚空", d = 52)
/* loaded from: classes3.dex */
public class b3 extends bm {
    private static int r;
    private static final long s = dj.a(-4430292508336982034L, 3442276019445576420L, MethodHandles.lookup().lookupClass()).a(171982097070301L);
    private static final String[] t;
    private final ct j;
    private final cr k;
    private final ct l;
    private c7 m;
    private Vec3f n;
    private Vec3f o;
    private boolean p;
    private boolean q;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0045. Please report as an issue. */
    static {
        int i;
        int i2;
        String[] strArr = new String[15];
        String str = "矽禀\u0004\u001c\u0014qt\u0002橰彴\u0005\u0013\u0017|\u007fX\u0005\u0002\u000bz~U\u0002佱遺\b\u0015\u0012feR\u000b*4\u0002趌秀\f\u0013\u0017|\u007fXE\u001a4\u0018z\u007fW\u0004矽禀秇敡\b\u0010\u0012gA_\u0004*4\u0004稫汯攫罿\u0005\u0002\u000bz~U";
        int length = "矽禀\u0004\u001c\u0014qt\u0002橰彴\u0005\u0013\u0017|\u007fX\u0005\u0002\u000bz~U\u0002佱遺\b\u0015\u0012feR\u000b*4\u0002趌秀\f\u0013\u0017|\u007fXE\u001a4\u0018z\u007fW\u0004矽禀秇敡\b\u0010\u0012gA_\u0004*4\u0004稫汯攫罿\u0005\u0002\u000bz~U".length();
        char c = 2;
        int i3 = -1;
        int i4 = 0;
        while (true) {
            int i5 = 23;
            int i6 = i3 + 1;
            String substring = str.substring(i6, i6 + c);
            boolean z = -1;
            while (true) {
                String b = b(i5, b(substring));
                i = i4 + 1;
                switch (z) {
                    case false:
                        break;
                    default:
                        strArr[i4] = b;
                        i3 = i6 + c;
                        if (i3 < length) {
                            break;
                        }
                        str = "IK>\u0018\u0006]sm\u0005JN%&\u0001";
                        length = "IK>\u0018\u0006]sm\u0005JN%&\u0001".length();
                        c = '\b';
                        i2 = -1;
                        i4 = i;
                        i5 = 78;
                        i6 = i2 + 1;
                        substring = str.substring(i6, i6 + c);
                        z = false;
                        break;
                }
                strArr[i4] = b;
                i2 = i6 + c;
                if (i2 >= length) {
                    t = strArr;
                    r = 0;
                    return;
                }
                c = str.charAt(i2);
                i4 = i;
                i5 = 78;
                i6 = i2 + 1;
                substring = str.substring(i6, i6 + c);
                z = false;
            }
            c = str.charAt(i3);
            i4 = i;
        }
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public b3(long r26) {
        /*
            Method dump skipped, instructions count: 217
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.b3.<init>(long):void");
    }

    private int a(long j, EntityLocalPlayer entityLocalPlayer) {
        if (entityLocalPlayer.X().o()) {
            return entityLocalPlayer.ak().d();
        }
        for (int i = 0; i < 9; i++) {
            if (entityLocalPlayer.ak().c().a(i).o()) {
                return i;
            }
        }
        return -1;
    }

    private static String b(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 70;
                    break;
                case 1:
                    i2 = 108;
                    break;
                case 2:
                    i2 = 2;
                    break;
                case 3:
                    i2 = 6;
                    break;
                case 4:
                    i2 = 36;
                    break;
                case 5:
                    i2 = 114;
                    break;
                default:
                    i2 = 94;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private boolean b(long j, EntityLocalPlayer entityLocalPlayer) {
        long j2 = j ^ s;
        long j3 = 104329857076490L ^ j2;
        int a = a(j2 ^ 23592331635034L, entityLocalPlayer);
        if (a == -1) {
            return false;
        }
        Vec3f N = entityLocalPlayer.N();
        Vec3i vec3i = new Vec3i((int) Math.floor(N.x), (int) (Math.floor(N.y) - 3.0d), (int) Math.floor(N.z));
        if (!entityLocalPlayer.W().d(new Vec3i((int) Math.floor(N.x), (int) (Math.floor(N.y) - 2.0d), (int) Math.floor(N.z))).t()) {
            return true;
        }
        int d = entityLocalPlayer.ak().d();
        boolean z = d != a;
        if (z) {
            entityLocalPlayer.ak().a(a);
            MobEquipmentPacket mobEquipmentPacket = new MobEquipmentPacket();
            mobEquipmentPacket.setContainerId(0);
            mobEquipmentPacket.setHotbarSlot(a);
            mobEquipmentPacket.setInventorySlot(a);
            mobEquipmentPacket.setRuntimeEntityId(entityLocalPlayer.C());
            mobEquipmentPacket.setItem(entityLocalPlayer.ak().c().a(a).q());
            entityLocalPlayer.a(j3, mobEquipmentPacket);
        }
        entityLocalPlayer.a(vec3i, com.trossense.sdk.block.a.UP.index, false);
        if (z) {
            entityLocalPlayer.ak().a(d);
            MobEquipmentPacket mobEquipmentPacket2 = new MobEquipmentPacket();
            mobEquipmentPacket2.setContainerId(0);
            mobEquipmentPacket2.setHotbarSlot(d);
            mobEquipmentPacket2.setInventorySlot(d);
            mobEquipmentPacket2.setRuntimeEntityId(entityLocalPlayer.C());
            mobEquipmentPacket2.setItem(entityLocalPlayer.ak().c().a(d).q());
            entityLocalPlayer.a(j3, mobEquipmentPacket2);
        }
        return true;
    }

    private static char[] b(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ '^');
        }
        return charArray;
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x00c9, code lost:            if (r14 == null) goto L22;     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x00d6, code lost:            if (r14 == null) goto L26;     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x011c, code lost:            if (r14 != null) goto L59;     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x0157, code lost:            if (r14 == null) goto L61;     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x00f4, code lost:            if (r14 == null) goto L36;     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x015c, code lost:            r5 = false;     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x00f2, code lost:            if (r14 == null) goto L35;     */
    /* JADX WARN: Failed to find 'out' block for switch in B:27:0x00e1. Please report as an issue. */
    @com.trossense.bk
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void a(com.trossense.bg r35) {
        /*
            Method dump skipped, instructions count: 434
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.b3.a(com.trossense.bg):void");
    }

    @Override // com.trossense.bm
    public void k(long j) {
        if (this.p) {
            a4.b = false;
        }
        this.p = false;
        this.q = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-trossense-b3, reason: not valid java name */
    public boolean m169lambda$new$0$comtrossenseb3() {
        return this.k.j((s ^ 1908960498473L) ^ 122903273443799L).a.equals(t[3]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$1$com-trossense-b3, reason: not valid java name */
    public boolean m170lambda$new$1$comtrossenseb3() {
        return !this.k.j((s ^ 108882940710445L) ^ 14828812750035L).a.equals(t[14]);
    }
}
