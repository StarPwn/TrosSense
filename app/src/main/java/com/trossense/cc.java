package com.trossense;

import com.trossense.clients.TrosSense;
import com.trossense.sdk.entity.type.EntityLocalPlayer;
import java.lang.invoke.MethodHandles;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerType;
import org.cloudburstmc.protocol.bedrock.packet.ContainerClosePacket;
import org.cloudburstmc.protocol.bedrock.packet.InteractPacket;

@cg(a = "InvManager", b = b.World, c = "物品整理", d = 30)
/* loaded from: classes3.dex */
public class cc extends bm {
    private static final long B = dj.a(-3401505200302487278L, -6189593206375564573L, MethodHandles.lookup().lookupClass()).a(175515554362758L);
    private static final String[] D;
    private boolean A;
    private final ct j;
    private final ct k;
    private final ct l;
    private final ct m;
    private final ct n;
    private final ct o;
    private final ct p;
    private final ct q;
    private final cr r;
    private final cr s;
    private final ct t;
    private final ct u;
    private boolean v;
    private boolean w;
    private final c7 x;
    private int y;
    private c7 z;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0043. Please report as an issue. */
    static {
        int i;
        int i2;
        String[] strArr = new String[53];
        String str = "qIpNm\u0017lzT$Xf\fhpD\bOIs^b\u0004yy\u0015qIpNm\u0017lzT$B}:ayGyB`\u0002~\tSNrR.*}yN\tQAf\u000bJ\u0000a}Y\b前槝6\u001b七撨佑5\u0004ROpN\u0002樽拿\u000fYNzN|5h}Rr\u000b]\tbh\u0003飃牉槣\u0016qIpNm\u0017lzT$La\tiyNAJ~\u0015ay\u0011qIpNm\u0017lzT$B}:ksOz\u0013qIpNm\u0017lzT$B}:~tOhNb\u0004剳扫牷哪\u0004丑歛因腕\u0005式\b強\u0002槳\u0002斻槝\u0013qIpNm\u0017lzT$B}:eyLsNz\u001aqIpNm\u0017lzT$_a\u0011hq\u007fqMQ\u0010cxYwEi\n^LqHeE^pOj\u0002铮槝\u0006朜尯拡叽旸閑\u0005HOjNc\b]X{\u000b]\tbh\u000bOHq]k\t-OLq_\tSNrR.*}yN\b^Oi\u000b]\tbh\tSP{E.(bxE\nOWqYjE^pOj\u0012qIpNm\u0017lzT$B}:osOjX\fSFxCo\u000bi<ijNc\u0005HOjNc\u0003斥坷槣\u0004叶圈才弫\u0012qIpNm\u0017lzT$B}:~kOlO\u0015qIpNm\u0017lzT$N`\u0001hn\u007fnNo\u0017a\tZOqO.6asT\u0004ROpN\u0004敨琦樿弤\u0002镌槝\u0003珑珀槣\u0006OHwNb\u0001\bOIs^b\u0004yy qIpNm\u0017lzT$N`\u0006e}NjNj:jsLzN`:llPrN\u0010qIpNm\u0017lzT$B}:osW\u0002盢牬\u0017qIpNm\u0017lzT$B}:ntEm_~\tlhE\u0010qIpNm\u0017lzT$B}:ldE\fLI}@o\u001dh<srDz\u0006朜备拡叽旸閑\tQIp\u000bJ\u0000a}Y";
        int length = "qIpNm\u0017lzT$Xf\fhpD\bOIs^b\u0004yy\u0015qIpNm\u0017lzT$B}:ayGyB`\u0002~\tSNrR.*}yN\tQAf\u000bJ\u0000a}Y\b前槝6\u001b七撨佑5\u0004ROpN\u0002樽拿\u000fYNzN|5h}Rr\u000b]\tbh\u0003飃牉槣\u0016qIpNm\u0017lzT$La\tiyNAJ~\u0015ay\u0011qIpNm\u0017lzT$B}:ksOz\u0013qIpNm\u0017lzT$B}:~tOhNb\u0004剳扫牷哪\u0004丑歛因腕\u0005式\b強\u0002槳\u0002斻槝\u0013qIpNm\u0017lzT$B}:eyLsNz\u001aqIpNm\u0017lzT$_a\u0011hq\u007fqMQ\u0010cxYwEi\n^LqHeE^pOj\u0002铮槝\u0006朜尯拡叽旸閑\u0005HOjNc\b]X{\u000b]\tbh\u000bOHq]k\t-OLq_\tSNrR.*}yN\b^Oi\u000b]\tbh\tSP{E.(bxE\nOWqYjE^pOj\u0012qIpNm\u0017lzT$B}:osOjX\fSFxCo\u000bi<ijNc\u0005HOjNc\u0003斥坷槣\u0004叶圈才弫\u0012qIpNm\u0017lzT$B}:~kOlO\u0015qIpNm\u0017lzT$N`\u0001hn\u007fnNo\u0017a\tZOqO.6asT\u0004ROpN\u0004敨琦樿弤\u0002镌槝\u0003珑珀槣\u0006OHwNb\u0001\bOIs^b\u0004yy qIpNm\u0017lzT$N`\u0006e}NjNj:jsLzN`:llPrN\u0010qIpNm\u0017lzT$B}:osW\u0002盢牬\u0017qIpNm\u0017lzT$B}:ntEm_~\tlhE\u0010qIpNm\u0017lzT$B}:ldE\fLI}@o\u001dh<srDz\u0006朜备拡叽旸閑\tQIp\u000bJ\u0000a}Y".length();
        char c = 16;
        int i3 = 0;
        int i4 = -1;
        while (true) {
            int i5 = i4 + 1;
            String substring = str.substring(i5, i5 + c);
            boolean z = -1;
            int i6 = 1;
            while (true) {
                String b = b(i6, b(substring));
                switch (z) {
                    case false:
                        break;
                    default:
                        i = i3 + 1;
                        strArr[i3] = b;
                        i4 = i5 + c;
                        if (i4 < length) {
                            break;
                        }
                        str = "\u0003;\u0002<\u001fe\u001e\b&V0\u000fH\u000f\u00071\u00078\u0004r\u0006=:\u0005<\u0010s";
                        length = "\u0003;\u0002<\u001fe\u001e\b&V0\u000fH\u000f\u00071\u00078\u0004r\u0006=:\u0005<\u0010s".length();
                        c = 20;
                        i2 = -1;
                        i3 = i;
                        i6 = 115;
                        i5 = i2 + 1;
                        substring = str.substring(i5, i5 + c);
                        z = false;
                        break;
                }
                i = i3 + 1;
                strArr[i3] = b;
                i2 = i5 + c;
                if (i2 >= length) {
                    D = strArr;
                    return;
                }
                c = str.charAt(i2);
                i3 = i;
                i6 = 115;
                i5 = i2 + 1;
                substring = str.substring(i5, i5 + c);
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
    public cc(long r32) {
        /*
            Method dump skipped, instructions count: 418
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.cc.<init>(long):void");
    }

    private void a(long j, EntityLocalPlayer entityLocalPlayer, cy cyVar, int i) {
        long j2 = B ^ j;
        long j3 = 34010994190636L ^ j2;
        long j4 = 9245701530870L ^ j2;
        int i2 = (int) (j4 >>> 32);
        long j5 = (j4 << 32) >>> 32;
        if (!this.s.j(j2 ^ 90343496117552L).a.equals(D[1]) || (a7.a() instanceof c0)) {
            a7.a(entityLocalPlayer, i2, j5, cyVar, i);
            return;
        }
        if (this.v) {
            return;
        }
        this.v = true;
        InteractPacket interactPacket = new InteractPacket();
        interactPacket.setRuntimeEntityId(entityLocalPlayer.C());
        interactPacket.setAction(InteractPacket.Action.OPEN_INVENTORY);
        interactPacket.setMousePosition(Vector3f.ZERO);
        entityLocalPlayer.a(j3, interactPacket);
        this.z.a();
    }

    private void a(EntityLocalPlayer entityLocalPlayer, long j, cy cyVar, short s, int i, cy cyVar2, int i2) {
        long j2 = ((j << 16) | ((s << 48) >>> 48)) ^ B;
        long j3 = 48997696158857L ^ j2;
        long j4 = j2 ^ 126421628621814L;
        if (!this.s.j(j2 ^ 105915445573781L).a.equals(D[1]) || (a7.a() instanceof c0)) {
            a7.a(entityLocalPlayer, j4, cyVar, i, cyVar2, i2);
            return;
        }
        if (this.v) {
            return;
        }
        this.v = true;
        InteractPacket interactPacket = new InteractPacket();
        interactPacket.setRuntimeEntityId(entityLocalPlayer.C());
        interactPacket.setAction(InteractPacket.Action.OPEN_INVENTORY);
        interactPacket.setMousePosition(Vector3f.ZERO);
        entityLocalPlayer.a(j3, interactPacket);
        this.z.a();
    }

    private static String b(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 29;
                    break;
                case 1:
                    i2 = 33;
                    break;
                case 2:
                    i2 = 31;
                    break;
                case 3:
                    i2 = 42;
                    break;
                case 4:
                    i2 = 15;
                    break;
                case 5:
                    i2 = 100;
                    break;
                default:
                    i2 = 12;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] b(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ '\f');
        }
        return charArray;
    }

    @bk
    public void a(ba baVar) {
        if (baVar.e() instanceof com.trossense.sdk.p) {
            com.trossense.sdk.p pVar = (com.trossense.sdk.p) baVar.e();
            if (this.v && pVar.a() == 0) {
                this.v = false;
                baVar.c();
            }
        }
        if ((baVar.e() instanceof ContainerClosePacket) && ((ContainerClosePacket) baVar.e()).getId() == 0 && this.w) {
            this.w = false;
        }
    }

    @bk
    public void a(bb bbVar) {
        long j = B ^ 31149112937111L;
        long j2 = 1054849071258L ^ j;
        long j3 = j ^ 131172286924005L;
        EntityLocalPlayer localPlayer = TrosSense.INSTANCE.getLocalPlayer();
        if (!this.v && localPlayer != null && (bbVar.e() instanceof InteractPacket) && this.s.j(j3).a.equals(D[1]) && ((InteractPacket) bbVar.e()).getAction() == InteractPacket.Action.OPEN_INVENTORY) {
            this.w = true;
            if (a7.a() instanceof c0) {
                com.trossense.sdk.p pVar = new com.trossense.sdk.p();
                pVar.a((byte) 0);
                pVar.a(localPlayer.d());
                pVar.a(ContainerType.INVENTORY);
                pVar.a(Vector3i.ZERO);
                localPlayer.b(pVar, j2);
                bbVar.c();
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:188:0x0414  */
    /* JADX WARN: Removed duplicated region for block: B:197:0x044b  */
    /* JADX WARN: Removed duplicated region for block: B:204:0x0470  */
    /* JADX WARN: Removed duplicated region for block: B:214:0x04a4  */
    /* JADX WARN: Removed duplicated region for block: B:221:0x04ca  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0105 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:231:0x0522  */
    /* JADX WARN: Removed duplicated region for block: B:238:0x055a  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0106  */
    /* JADX WARN: Removed duplicated region for block: B:242:0x0511  */
    /* JADX WARN: Removed duplicated region for block: B:245:0x043d  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0568 A[LOOP:0: B:24:0x0111->B:37:0x0568, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:38:0x05a1 A[EDGE_INSN: B:38:0x05a1->B:39:0x05a1 BREAK  A[LOOP:0: B:24:0x0111->B:37:0x0568], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0695  */
    @com.trossense.bk
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void a(com.trossense.bg r54) {
        /*
            Method dump skipped, instructions count: 1794
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.cc.a(com.trossense.bg):void");
    }

    @Override // com.trossense.bm
    public void j(long j) {
        this.w = false;
        this.y = 0;
        this.x.a();
        this.v = false;
        this.z.a();
    }
}
