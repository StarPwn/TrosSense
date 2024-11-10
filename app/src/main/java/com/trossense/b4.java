package com.trossense;

import com.trossense.clients.TrosSense;
import com.trossense.sdk.PointerHolder;
import com.trossense.sdk.block.Block;
import com.trossense.sdk.entity.type.EntityLocalPlayer;
import com.trossense.sdk.inventory.PlayerInventory;
import com.trossense.sdk.math.Vec3i;
import java.lang.invoke.MethodHandles;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.bedrock.data.PlayerActionType;
import org.cloudburstmc.protocol.bedrock.packet.InventoryTransactionPacket;
import org.cloudburstmc.protocol.bedrock.packet.MobEquipmentPacket;
import org.cloudburstmc.protocol.bedrock.packet.PlayerActionPacket;

@cg(a = "AutoTool", b = b.Player, c = "自动工具")
/* loaded from: classes3.dex */
public class b4 extends bm {
    private static final long m = dj.a(-3496753340026670844L, 4771803476521518011L, MethodHandles.lookup().lookupClass()).a(95066845178038L);
    private static final String[] n;
    private final cp j;
    private cj k;
    private Vec3i l;

    static {
        String[] strArr = new String[2];
        int length = "c\u001f*KB\u0005權髸寧戓立".length();
        int i = 0;
        char c = 5;
        int i2 = -1;
        while (true) {
            int i3 = i2 + 1;
            int i4 = c + i3;
            int i5 = i + 1;
            strArr[i] = b(64, b("c\u001f*KB\u0005權髸寧戓立".substring(i3, i4)));
            if (i4 >= length) {
                n = strArr;
                return;
            } else {
                i2 = i4;
                c = "c\u001f*KB\u0005權髸寧戓立".charAt(i4);
                i = i5;
            }
        }
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public b4(long r12) {
        /*
            r11 = this;
            long r0 = com.trossense.b4.m
            long r12 = r12 ^ r0
            r0 = 116566567075139(0x6a0444d2bd43, double:5.75915362454755E-310)
            long r0 = r0 ^ r12
            r2 = 48388324671217(0x2c0248e73ef1, double:2.39070088798608E-310)
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
            java.lang.String[] r12 = com.trossense.b4.n
            r0 = 0
            r7 = r12[r0]
            r0 = 1
            r8 = r12[r0]
            java.lang.Boolean r10 = java.lang.Boolean.valueOf(r0)
            r3 = r13
            r9 = r11
            r3.<init>(r4, r6, r7, r8, r9, r10)
            r11.j = r13
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.b4.<init>(long):void");
    }

    private cj a(EntityLocalPlayer entityLocalPlayer, Block block, long j) {
        long j2 = (m ^ j) ^ 54185340553812L;
        int[] n2 = b5.n();
        int d = entityLocalPlayer.ak().d();
        PlayerInventory ak = entityLocalPlayer.ak();
        int i = 0;
        cj cjVar = new cj(this, -1, entityLocalPlayer.a(block), j2);
        while (i < 9) {
            if (n2 == null) {
                break;
            }
            if (i != d) {
                ak.a(i);
                float a = entityLocalPlayer.a(block);
                if (cjVar.b() < a) {
                    cjVar = new cj(this, i, a, j2);
                }
            }
            i++;
            if (n2 == null) {
                break;
            }
        }
        ak.a(d);
        return cjVar;
    }

    private static String b(int i, char[] cArr) {
        int length = cArr.length;
        for (int i2 = 0; length > i2; i2++) {
            char c = cArr[i2];
            int i3 = 100;
            switch (i2 % 7) {
                case 0:
                    i3 = 112;
                    break;
                case 1:
                    i3 = 47;
                    break;
                case 2:
                    i3 = 5;
                    break;
                case 3:
                case 4:
                    break;
                case 5:
                    i3 = 54;
                    break;
                default:
                    i3 = 102;
                    break;
            }
            cArr[i2] = (char) (c ^ (i ^ i3));
        }
        return new String(cArr).intern();
    }

    private static char[] b(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 'f');
        }
        return charArray;
    }

    @bk
    public void a(bb bbVar) {
        EntityLocalPlayer localPlayer;
        EntityLocalPlayer localPlayer2;
        long j = m ^ 12237719697982L;
        long j2 = 72724965511501L ^ j;
        long j3 = j ^ 84017163080952L;
        if (bbVar.e() instanceof PlayerActionPacket) {
            PlayerActionPacket playerActionPacket = (PlayerActionPacket) bbVar.e();
            if ((playerActionPacket.getAction() == PlayerActionType.START_BREAK || playerActionPacket.getAction() == PlayerActionType.CONTINUE_BREAK) && this.k != null && this.l != null) {
                Vector3i blockPosition = playerActionPacket.getBlockPosition();
                if (blockPosition.getX() == this.l.x && blockPosition.getY() == this.l.y && blockPosition.getZ() == this.l.z && (localPlayer2 = TrosSense.INSTANCE.getLocalPlayer()) != null && this.k.a() != -1) {
                    bbVar.c();
                    int d = localPlayer2.ak().d();
                    MobEquipmentPacket mobEquipmentPacket = new MobEquipmentPacket();
                    mobEquipmentPacket.setContainerId(0);
                    mobEquipmentPacket.setHotbarSlot(cj.a(this.k));
                    mobEquipmentPacket.setInventorySlot(cj.a(this.k));
                    mobEquipmentPacket.setRuntimeEntityId(localPlayer2.C());
                    mobEquipmentPacket.setItem(localPlayer2.ak().c().a(cj.a(this.k)).q());
                    localPlayer2.a(j2, mobEquipmentPacket);
                    localPlayer2.d(playerActionPacket, j3);
                    MobEquipmentPacket mobEquipmentPacket2 = new MobEquipmentPacket();
                    mobEquipmentPacket2.setContainerId(0);
                    mobEquipmentPacket2.setHotbarSlot(d);
                    mobEquipmentPacket2.setInventorySlot(d);
                    mobEquipmentPacket2.setRuntimeEntityId(localPlayer2.C());
                    mobEquipmentPacket2.setItem(localPlayer2.ak().c().a(d).q());
                    localPlayer2.a(j2, mobEquipmentPacket2);
                }
            }
        }
        if (bbVar.e() instanceof InventoryTransactionPacket) {
            InventoryTransactionPacket inventoryTransactionPacket = (InventoryTransactionPacket) bbVar.e();
            if (inventoryTransactionPacket.getActionType() != 2 || this.k == null || this.l == null) {
                return;
            }
            Vector3i blockPosition2 = inventoryTransactionPacket.getBlockPosition();
            if (blockPosition2.getX() == this.l.x && blockPosition2.getY() == this.l.y && blockPosition2.getZ() == this.l.z && (localPlayer = TrosSense.INSTANCE.getLocalPlayer()) != null && this.k.a() != -1) {
                bbVar.c();
                int d2 = localPlayer.ak().d();
                MobEquipmentPacket mobEquipmentPacket3 = new MobEquipmentPacket();
                mobEquipmentPacket3.setContainerId(0);
                mobEquipmentPacket3.setHotbarSlot(cj.a(this.k));
                mobEquipmentPacket3.setInventorySlot(cj.a(this.k));
                mobEquipmentPacket3.setRuntimeEntityId(localPlayer.C());
                mobEquipmentPacket3.setItem(localPlayer.ak().c().a(cj.a(this.k)).q());
                localPlayer.a(j2, mobEquipmentPacket3);
                inventoryTransactionPacket.setHotbarSlot(cj.a(this.k));
                inventoryTransactionPacket.setItemInHand(localPlayer.ak().c().a(cj.a(this.k)).q());
                localPlayer.d(inventoryTransactionPacket, j3);
                MobEquipmentPacket mobEquipmentPacket4 = new MobEquipmentPacket();
                mobEquipmentPacket4.setContainerId(0);
                mobEquipmentPacket4.setHotbarSlot(d2);
                mobEquipmentPacket4.setInventorySlot(d2);
                mobEquipmentPacket4.setRuntimeEntityId(localPlayer.C());
                mobEquipmentPacket4.setItem(localPlayer.ak().c().a(d2).q());
                localPlayer.a(j2, mobEquipmentPacket4);
            }
        }
    }

    @bk
    public void a(bc bcVar) {
        long j = (m ^ 114542129033142L) ^ 78369925428665L;
        int[] n2 = b5.n();
        Block a = bcVar.a();
        EntityLocalPlayer c = bcVar.c();
        if (!this.j.e().booleanValue()) {
            cj a2 = a(c, a, j);
            if (a2.a() != -1) {
                c.ak().a(a2.a());
            }
        }
        if (this.j.e().booleanValue()) {
            cj a3 = a(c, a, j);
            if (a3.a() != -1) {
                this.k = a3;
                this.l = a.f();
                bcVar.a(this.k.b());
            }
        }
        if (n2 == null) {
            PointerHolder.b(new int[2]);
        }
    }
}
