package com.trossense;

import com.trossense.clients.TrosSense;
import com.trossense.sdk.block.Material;
import com.trossense.sdk.component.MoveInputComponent;
import com.trossense.sdk.entity.type.EntityLocalPlayer;
import com.trossense.sdk.level.Level;
import com.trossense.sdk.math.Vec3f;
import com.trossense.sdk.math.Vec3i;
import java.lang.invoke.MethodHandles;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.bedrock.data.inventory.transaction.InventoryTransactionType;
import org.cloudburstmc.protocol.bedrock.packet.InventoryTransactionPacket;
import org.cloudburstmc.protocol.bedrock.packet.MobEquipmentPacket;
import org.cloudburstmc.protocol.bedrock.packet.MovePlayerPacket;

@cg(a = "Scaffold", b = b.World, c = "自动铺路", d = 35)
/* loaded from: classes3.dex */
public class ce extends bm {
    private static final long w = dj.a(2325054875365179374L, -2528289717462790577L, MethodHandles.lookup().lookupClass()).a(47711189899063L);
    private static final String[] x;
    private final cr j;
    private final ct k;
    private final cp l;
    private final ct m;
    private final ct n;
    private final cp o;
    private final cr p;
    private int q;
    private boolean r;
    private float s;
    private Vec3i t;
    private com.trossense.sdk.block.a u;
    private int v;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0045. Please report as an issue. */
    static {
        int i;
        int i2;
        String[] strArr = new String[33];
        String str = "乏造拘\u0002庴休\u0005\u0011Y^th\u0006杂夎轝夯逑庛\u0002歸骾\b\u0010FEzzT\u0001,\u0005\u0016FF~|\u0006杂尦轝夯逑庛\u0005\u0011Y^th\u0004\fF_~\u0006\u0007QE~`Y\u0005\u0016L]ww\u0004\u0003\\Et\u0004\fF_~\b\u0003@CKb\\\r'\b\u0000df;H\\\u001d6\b\u0000df;H\\\u001d6\u0002輮夝\u0005\u0016FF~|\u0004\u000fFU~\u0005幁吠岪连逑\u0004钸跆樐弔\u0006\fFCvoQ\u0012\u000f@_;\\R\u001a#]Xt`\u001d=2LT\u007f\u0002么塽\u0006運拀斈坌樯弲\u0005\u0016L]ww\u0002膨劁\u0002次帑\u0003稸氽铋\u000b\u0011L]~mIN\u000fFU~";
        int length = "乏造拘\u0002庴休\u0005\u0011Y^th\u0006杂夎轝夯逑庛\u0002歸骾\b\u0010FEzzT\u0001,\u0005\u0016FF~|\u0006杂尦轝夯逑庛\u0005\u0011Y^th\u0004\fF_~\u0006\u0007QE~`Y\u0005\u0016L]ww\u0004\u0003\\Et\u0004\fF_~\b\u0003@CKb\\\r'\b\u0000df;H\\\u001d6\b\u0000df;H\\\u001d6\u0002輮夝\u0005\u0016FF~|\u0004\u000fFU~\u0005幁吠岪连逑\u0004钸跆樐弔\u0006\fFCvoQ\u0012\u000f@_;\\R\u001a#]Xt`\u001d=2LT\u007f\u0002么塽\u0006運拀斈坌樯弲\u0005\u0016L]ww\u0002膨劁\u0002次帑\u0003稸氽铋\u000b\u0011L]~mIN\u000fFU~".length();
        char c = 3;
        int i3 = 0;
        int i4 = -1;
        while (true) {
            int i5 = 24;
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
                        str = "p/6\u0007\u0012|;:H/!iP.+\u0007\u0013nNA?'\f";
                        length = "p/6\u0007\u0012|;:H/!iP.+\u0007\u0013nNA?'\f".length();
                        c = 4;
                        i2 = -1;
                        i3 = i;
                        i5 = 107;
                        i6 = i2 + 1;
                        substring = str.substring(i6, i6 + c);
                        z = false;
                        break;
                }
                strArr[i3] = b;
                i2 = i6 + c;
                if (i2 >= length) {
                    x = strArr;
                    return;
                }
                c = str.charAt(i2);
                i3 = i;
                i5 = 107;
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
    public ce(long r30) {
        /*
            Method dump skipped, instructions count: 399
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.ce.<init>(long):void");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x003f, code lost:            if (r6.equals(com.trossense.ce.x[9]) != false) goto L15;     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean a(long r6, com.trossense.sdk.entity.type.EntityLocalPlayer r8) {
        /*
            r5 = this;
            long r0 = com.trossense.ce.w
            long r6 = r6 ^ r0
            r0 = 108415297718125(0x629a674f076d, double:5.35642740861765E-310)
            long r0 = r0 ^ r6
            r2 = 48162724028998(0x2bcdc20ea246, double:2.37955473528606E-310)
            long r6 = r6 ^ r2
            int r0 = r5.b(r0, r8)
            r5.v = r0
            com.trossense.cr r0 = r5.j
            com.trossense.cu r6 = r0.j(r6)
            java.lang.String r6 = r6.a
            int r7 = r6.hashCode()
            r0 = 2
            r1 = 1
            r2 = 0
            r3 = -1
            switch(r7) {
                case 2052559: goto L42;
                case 2433880: goto L35;
                case 80099049: goto L29;
                default: goto L28;
            }
        L28:
            goto L50
        L29:
            java.lang.String[] r7 = com.trossense.ce.x
            r7 = r7[r0]
            boolean r6 = r6.equals(r7)
            if (r6 == 0) goto L50
            r0 = r1
            goto L51
        L35:
            java.lang.String[] r7 = com.trossense.ce.x
            r4 = 9
            r7 = r7[r4]
            boolean r6 = r6.equals(r7)
            if (r6 == 0) goto L50
            goto L51
        L42:
            java.lang.String[] r7 = com.trossense.ce.x
            r0 = 31
            r7 = r7[r0]
            boolean r6 = r6.equals(r7)
            if (r6 == 0) goto L50
            r0 = r2
            goto L51
        L50:
            r0 = r3
        L51:
            switch(r0) {
                case 0: goto L64;
                case 1: goto L5d;
                default: goto L54;
            }
        L54:
            com.trossense.sdk.item.ItemStack r6 = r8.X()
            boolean r6 = r6.o()
            return r6
        L5d:
            int r6 = r5.v
            if (r6 == r3) goto L62
            goto L63
        L62:
            r1 = r2
        L63:
            return r1
        L64:
            int r6 = r5.v
            if (r6 != r3) goto L69
            return r2
        L69:
            com.trossense.sdk.inventory.PlayerInventory r6 = r8.ak()
            int r7 = r5.v
            r6.a(r7)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.ce.a(long, com.trossense.sdk.entity.type.EntityLocalPlayer):boolean");
    }

    private boolean a(short s, char c, EntityLocalPlayer entityLocalPlayer, int i, Vec3i vec3i) {
        Level W = entityLocalPlayer.W();
        if (!W.d(vec3i).t()) {
            return false;
        }
        int i2 = 0;
        while (true) {
            if (i2 >= 4) {
                return false;
            }
            int i3 = 0;
            for (int i4 = 4; i3 < i4; i4 = 4) {
                for (int i5 = 1; i5 > -3; i5 -= 2) {
                    Vec3i vec3i2 = new Vec3i(vec3i.x + (i2 * i5), vec3i.y, vec3i.z + (i3 * i5));
                    if (W.d(vec3i2).t()) {
                        for (com.trossense.sdk.block.a aVar : com.trossense.sdk.block.a.values()) {
                            Vec3i b = aVar.b();
                            Vec3i vec3i3 = new Vec3i(vec3i2.x + b.x, vec3i2.y + b.y, vec3i2.z + b.z);
                            Material d = W.d(vec3i3);
                            if (d.j() && !d.t()) {
                                this.t = vec3i3;
                                this.u = aVar;
                                return true;
                            }
                        }
                    }
                }
                i3++;
            }
            i2++;
        }
    }

    private int b(long j, EntityLocalPlayer entityLocalPlayer) {
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
                    i2 = 90;
                    break;
                case 1:
                    i2 = 49;
                    break;
                case 2:
                    i2 = 41;
                    break;
                case 3:
                    i2 = 3;
                    break;
                case 4:
                    i2 = 22;
                    break;
                case 5:
                    i2 = 37;
                    break;
                default:
                    i2 = 118;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] b(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 'v');
        }
        return charArray;
    }

    private Vec3i c(EntityLocalPlayer entityLocalPlayer) {
        Vec3f sub = entityLocalPlayer.N().sub(0.0f, 2.0f, 0.0f);
        return new Vec3i((int) Math.floor(sub.x), (int) Math.floor(sub.y), (int) Math.floor(sub.z));
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x00b8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean d(int r17, int r18, com.trossense.sdk.entity.type.EntityLocalPlayer r19, char r20) {
        /*
            Method dump skipped, instructions count: 240
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.ce.d(int, int, com.trossense.sdk.entity.type.EntityLocalPlayer, char):boolean");
    }

    private void e(int i, short s, char c, EntityLocalPlayer entityLocalPlayer) {
        long j = (((c << 48) >>> 48) | ((i << 32) | ((s << 48) >>> 32))) ^ w;
        long j2 = 94329400706675L ^ j;
        long j3 = j ^ 27550884528751L;
        int d = entityLocalPlayer.ak().d();
        boolean z = d != this.v;
        String str = this.j.j(j3).a;
        String[] strArr = x;
        if (str.equals(strArr[8]) && z) {
            entityLocalPlayer.ak().a(this.v);
            MobEquipmentPacket mobEquipmentPacket = new MobEquipmentPacket();
            mobEquipmentPacket.setContainerId(0);
            mobEquipmentPacket.setHotbarSlot(this.v);
            mobEquipmentPacket.setInventorySlot(this.v);
            mobEquipmentPacket.setRuntimeEntityId(entityLocalPlayer.C());
            mobEquipmentPacket.setItem(entityLocalPlayer.ak().c().a(this.v).q());
            entityLocalPlayer.a(j2, mobEquipmentPacket);
        }
        entityLocalPlayer.a(this.t, this.u.index, false);
        if (this.j.j(j3).a.equals(strArr[2]) && z) {
            entityLocalPlayer.ak().a(d);
            MobEquipmentPacket mobEquipmentPacket2 = new MobEquipmentPacket();
            mobEquipmentPacket2.setContainerId(0);
            mobEquipmentPacket2.setHotbarSlot(d);
            mobEquipmentPacket2.setInventorySlot(d);
            mobEquipmentPacket2.setRuntimeEntityId(entityLocalPlayer.C());
            mobEquipmentPacket2.setItem(entityLocalPlayer.ak().c().a(d).q());
            entityLocalPlayer.a(j2, mobEquipmentPacket2);
        }
    }

    private void g(long j, EntityLocalPlayer entityLocalPlayer) {
        long j2 = (w ^ j) ^ 79667270037792L;
        MoveInputComponent ad = entityLocalPlayer.ad();
        if ((ad.moveForward == 0.0f && ad.moveSide == 0.0f) || ad.isJumpDown || !entityLocalPlayer.A() || entityLocalPlayer.T() || !c5.a(entityLocalPlayer, j2, entityLocalPlayer.Q().offset(0.0d, -0.5d, 0.0d).expand(-0.3d, 0.0d, -0.3d)).isEmpty()) {
            return;
        }
        entityLocalPlayer.d(entityLocalPlayer.P().add(0.0f, 0.42f, 0.0f));
    }

    private void h(EntityLocalPlayer entityLocalPlayer, long j) {
        long j2 = j ^ w;
        long j3 = 32347369139356L ^ j2;
        long j4 = j2 ^ 90628118870805L;
        if (this.l.e().booleanValue()) {
            a8.a(c6.a(entityLocalPlayer, j3, new Vec3f(this.t.x - this.u.b().x, this.t.y - this.u.b().y, this.t.z - this.u.b().z)), (int) c3.a(this.n.e().doubleValue(), j4, this.m.e().doubleValue()));
        }
    }

    @bk
    public void a(ba baVar) {
        if (baVar.e() instanceof MovePlayerPacket) {
            MovePlayerPacket movePlayerPacket = (MovePlayerPacket) baVar.e();
            EntityLocalPlayer localPlayer = TrosSense.INSTANCE.getLocalPlayer();
            if (localPlayer == null || movePlayerPacket.getRuntimeEntityId() != localPlayer.C()) {
                return;
            }
            this.r = true;
            this.s = 0.0f;
        }
    }

    @bk
    public void a(bb bbVar) {
        EntityLocalPlayer localPlayer;
        if (bbVar.e() instanceof InventoryTransactionPacket) {
            InventoryTransactionPacket inventoryTransactionPacket = (InventoryTransactionPacket) bbVar.e();
            if (inventoryTransactionPacket.getTransactionType() == InventoryTransactionType.ITEM_USE) {
                if (inventoryTransactionPacket.getActionType() == 0 && (localPlayer = TrosSense.INSTANCE.getLocalPlayer()) != null) {
                    Vec3f N = localPlayer.N();
                    Vector3i blockPosition = inventoryTransactionPacket.getBlockPosition();
                    Vec3f P = localPlayer.P();
                    if (Math.abs(blockPosition.getX() - N.x) < 2.0f && Math.abs(blockPosition.getZ() - N.z) < 2.0f && blockPosition.getY() == ((int) Math.floor(N.y)) - 3 && N.y - blockPosition.getY() > 3.62f && P.y > -0.0784000015258789d) {
                        localPlayer.d(new Vec3f(P.x, -0.0784f, P.z));
                    }
                }
                inventoryTransactionPacket.setClickPosition(Vector3f.ZERO);
            }
        }
    }

    @bk
    public void a(bg bgVar) {
        long j = w ^ 82858685433741L;
        long j2 = 74583011951262L ^ j;
        long j3 = 97652876017674L ^ j;
        long j4 = 33807281691466L ^ j;
        long j5 = 124340499522231L ^ j;
        int i = (int) (j5 >>> 32);
        int i2 = (int) ((j5 << 32) >>> 48);
        int i3 = (int) ((j5 << 48) >>> 48);
        EntityLocalPlayer a = bgVar.a();
        f(j ^ 61886760651694L, a);
        if (d((int) (j4 >>> 32), (int) ((j4 << 32) >>> 48), a, (char) ((j4 << 48) >>> 48)) && a(j2, a)) {
            h(a, j3);
            e(i, (short) i2, (char) i3, a);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:66:0x0052, code lost:            if (r4 == null) goto L17;     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x003d, code lost:            if (r4 == null) goto L11;     */
    /* JADX WARN: Failed to find 'out' block for switch in B:11:0x006a. Please report as an issue. */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0094  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00a0  */
    /* JADX WARN: Removed duplicated region for block: B:25:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:28:0x00d7  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00df  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00ef  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x00e3  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00da  */
    /* JADX WARN: Type inference failed for: r8v1 */
    /* JADX WARN: Type inference failed for: r8v11 */
    /* JADX WARN: Type inference failed for: r8v13 */
    /* JADX WARN: Type inference failed for: r8v14 */
    /* JADX WARN: Type inference failed for: r8v15 */
    /* JADX WARN: Type inference failed for: r8v3 */
    /* JADX WARN: Type inference failed for: r8v5 */
    /* JADX WARN: Type inference failed for: r8v6 */
    /* JADX WARN: Type inference failed for: r8v8 */
    /* JADX WARN: Type inference failed for: r8v9 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void f(long r11, com.trossense.sdk.entity.type.EntityLocalPlayer r13) {
        /*
            Method dump skipped, instructions count: 320
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.ce.f(long, com.trossense.sdk.entity.type.EntityLocalPlayer):void");
    }

    @Override // com.trossense.bm
    public void j(long j) {
        EntityLocalPlayer localPlayer = TrosSense.INSTANCE.getLocalPlayer();
        if (localPlayer != null) {
            this.q = c(localPlayer).y;
        }
        this.r = false;
        this.s = 0.0f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-trossense-ce, reason: not valid java name */
    public boolean m198lambda$new$0$comtrossensece() {
        return !this.l.e().booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$1$com-trossense-ce, reason: not valid java name */
    public boolean m199lambda$new$1$comtrossensece() {
        return !this.l.e().booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$2$com-trossense-ce, reason: not valid java name */
    public Number m200lambda$new$2$comtrossensece(Number number) {
        return Double.valueOf(Math.min(number.doubleValue(), this.m.e().doubleValue()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$3$com-trossense-ce, reason: not valid java name */
    public Number m201lambda$new$3$comtrossensece(Number number) {
        return Double.valueOf(Math.max(number.doubleValue(), this.n.e().doubleValue()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$4$com-trossense-ce, reason: not valid java name */
    public boolean m202lambda$new$4$comtrossensece() {
        return this.p.j((w ^ 25376400327669L) ^ 95239720169584L).a.equals(x[11]);
    }
}
