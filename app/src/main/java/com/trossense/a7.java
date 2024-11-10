package com.trossense;

import com.trossense.sdk.entity.type.EntityLocalPlayer;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerType;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.ItemStackRequest;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.ItemStackRequestSlotData;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.DropAction;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestAction;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.PlaceAction;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.SwapAction;
import org.cloudburstmc.protocol.bedrock.data.inventory.transaction.InventoryActionData;
import org.cloudburstmc.protocol.bedrock.data.inventory.transaction.InventorySource;
import org.cloudburstmc.protocol.bedrock.data.inventory.transaction.InventoryTransactionType;
import org.cloudburstmc.protocol.bedrock.packet.ContainerClosePacket;
import org.cloudburstmc.protocol.bedrock.packet.InventoryContentPacket;
import org.cloudburstmc.protocol.bedrock.packet.InventorySlotPacket;
import org.cloudburstmc.protocol.bedrock.packet.InventoryTransactionPacket;
import org.cloudburstmc.protocol.bedrock.packet.ItemStackRequestPacket;

/* loaded from: classes3.dex */
public class a7 {
    private static cy a;
    private static c0 b;
    private static ItemStackRequestPacket c;
    private static final List<String> d;
    private static final long e = dj.a(-1543209327687409338L, -2173066384401333607L, MethodHandles.lookup().lookupClass()).a(4206491081394L);

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0046. Please report as an issue. */
    static {
        int i;
        int i2;
        String[] strArr = new String[10];
        String str = "\t2b<t1w\u0002/60d\u001ct\u000b4x*\u0010\t2b<t1w\u0002/60d\u001ct\u000b,\u0012\t2b<t1w\u0002/60d\u001ce\u00134~=\u0014\t2b<t1w\u0002/60d\u001cb\u00162h<y7\u0013\t2b<t1w\u0002/60d\u001c~\u00017a<c\u0010\t2b<t1w\u0002/60d\u001cw\u001c>\u0017\t2b<t1w\u0002/60d\u001cu\f>\u007f-g/w\u0010>\u0015\t2b<t1w\u0002/60d\u001cz\u0001<k0y$e";
        int length = "\t2b<t1w\u0002/60d\u001ct\u000b4x*\u0010\t2b<t1w\u0002/60d\u001ct\u000b,\u0012\t2b<t1w\u0002/60d\u001ce\u00134~=\u0014\t2b<t1w\u0002/60d\u001cb\u00162h<y7\u0013\t2b<t1w\u0002/60d\u001c~\u00017a<c\u0010\t2b<t1w\u0002/60d\u001cw\u001c>\u0017\t2b<t1w\u0002/60d\u001cu\f>\u007f-g/w\u0010>\u0015\t2b<t1w\u0002/60d\u001cz\u0001<k0y$e".length();
        char c2 = 18;
        int i3 = -1;
        int i4 = 0;
        while (true) {
            int i5 = 69;
            int i6 = i3 + 1;
            String substring = str.substring(i6, i6 + c2);
            boolean z = -1;
            while (true) {
                String a2 = a(i5, a(substring));
                i = i4 + 1;
                switch (z) {
                    case false:
                        break;
                    default:
                        strArr[i4] = a2;
                        i3 = i6 + c2;
                        if (i3 < length) {
                            break;
                        }
                        str = "\u0004?o1y<z\u000f\";=i\u0011h\u00019w1v\u0014\u0004?o1y<z\u000f\";=i\u0011k\u00005j5b+";
                        length = "\u0004?o1y<z\u000f\";=i\u0011h\u00019w1v\u0014\u0004?o1y<z\u000f\";=i\u0011k\u00005j5b+".length();
                        c2 = 19;
                        i2 = -1;
                        i4 = i;
                        i5 = 72;
                        i6 = i2 + 1;
                        substring = str.substring(i6, i6 + c2);
                        z = false;
                        break;
                }
                strArr[i4] = a2;
                i2 = i6 + c2;
                if (i2 >= length) {
                    c = null;
                    ArrayList arrayList = new ArrayList();
                    d = arrayList;
                    arrayList.add(strArr[4]);
                    arrayList.add(strArr[6]);
                    arrayList.add(strArr[7]);
                    arrayList.add(strArr[0]);
                    arrayList.add(strArr[2]);
                    arrayList.add(strArr[9]);
                    arrayList.add(strArr[5]);
                    arrayList.add(strArr[8]);
                    arrayList.add(strArr[3]);
                    arrayList.add(strArr[1]);
                    return;
                }
                c2 = str.charAt(i2);
                i4 = i;
                i5 = 72;
                i6 = i2 + 1;
                substring = str.substring(i6, i6 + c2);
                z = false;
            }
            c2 = str.charAt(i3);
            i4 = i;
        }
    }

    public a7(long j) {
        c0 c0Var = new c0((int) (((j ^ e) ^ 116307944416126L) >>> 32), (short) ((r5 << 32) >>> 48), (char) ((r5 << 48) >>> 48));
        b = c0Var;
        c0Var.a = new ArrayList();
    }

    public static int a(short s, cy cyVar, long j) {
        long j2 = ((((j << 16) >>> 16) | (s << 48)) ^ e) ^ 85106386758673L;
        for (int i = 0; i < cyVar.b(); i++) {
            if (cyVar.a(i, j2) == ItemData.AIR) {
                return i;
            }
        }
        return -1;
    }

    public static cy a() {
        return a;
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c2 = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 33;
                    break;
                case 1:
                    i2 = 30;
                    break;
                case 2:
                    i2 = 73;
                    break;
                case 3:
                    i2 = 28;
                    break;
                case 4:
                    i2 = 82;
                    break;
                case 5:
                    i2 = 6;
                    break;
                default:
                    i2 = 83;
                    break;
            }
            cArr[i3] = (char) (c2 ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    public static List a(ItemData itemData, com.trossense.sdk.f fVar, cy cyVar, long j) {
        long j2 = (j ^ e) ^ 128135059093368L;
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < cyVar.b(); i++) {
            ItemData a2 = cyVar.a(i, j2);
            if (a2.getDefinition().getIdentifier().equals(itemData.getDefinition().getIdentifier()) && itemData.getDamage() == a2.getDamage() && ((itemData.getTag() == null || itemData.getTag().equals(a2.getTag())) && (a2.getTag() == null || a2.getTag().equals(itemData.getTag())))) {
                arrayList.add(Integer.valueOf(i));
            }
        }
        return arrayList;
    }

    public static void a(long j, EntityLocalPlayer entityLocalPlayer) {
        long j2 = (j ^ e) ^ 103968160777028L;
        ItemStackRequestPacket itemStackRequestPacket = c;
        if (itemStackRequestPacket != null) {
            entityLocalPlayer.d(itemStackRequestPacket, j2);
            c = null;
        }
    }

    public static void a(EntityLocalPlayer entityLocalPlayer, int i, long j, cy cyVar, int i2) {
        long j2;
        long j3;
        EntityLocalPlayer entityLocalPlayer2 = entityLocalPlayer;
        long j4 = ((i << 32) | ((j << 32) >>> 32)) ^ e;
        long j5 = 123530861868969L ^ j4;
        long j6 = 74030569832301L ^ j4;
        long j7 = 69372696208267L ^ j4;
        long j8 = 11168136388189L ^ j4;
        long j9 = 59111789294875L ^ j4;
        long j10 = j4 ^ 131600491002872L;
        ItemData a2 = cyVar.a(i2, j5);
        if (a5.d) {
            ItemStackRequestPacket itemStackRequestPacket = new ItemStackRequestPacket();
            j2 = j10;
            j3 = j7;
            itemStackRequestPacket.getRequests().add(new ItemStackRequest(0, new ItemStackRequestAction[]{new DropAction(a2.getCount(), new ItemStackRequestSlotData(cyVar.b(j6, i2), cyVar.c(j9, i2), a2.getNetId()), false)}, new String[0], null));
            entityLocalPlayer2 = entityLocalPlayer;
            entityLocalPlayer2.d(itemStackRequestPacket, j8);
        } else {
            j2 = j10;
            j3 = j7;
            InventoryTransactionPacket inventoryTransactionPacket = new InventoryTransactionPacket();
            inventoryTransactionPacket.setTransactionType(InventoryTransactionType.NORMAL);
            ItemData build = a2.toBuilder().usingNetId(false).netId(0).build();
            inventoryTransactionPacket.getActions().add(new InventoryActionData(InventorySource.fromWorldInteraction(InventorySource.Flag.DROP_ITEM), 0, ItemData.AIR, build));
            inventoryTransactionPacket.getActions().add(new InventoryActionData(InventorySource.fromContainerWindowId(cyVar.a()), i2, build, ItemData.AIR));
            entityLocalPlayer2.d(inventoryTransactionPacket, j8);
        }
        InventorySlotPacket inventorySlotPacket = new InventorySlotPacket();
        inventorySlotPacket.setSlot(cyVar.c(j9, i2));
        inventorySlotPacket.setItem(ItemData.AIR);
        inventorySlotPacket.setContainerId(cyVar.a());
        entityLocalPlayer2.b(inventorySlotPacket, j3);
        cyVar.a(i2, ItemData.AIR, j2);
        entityLocalPlayer.u();
    }

    public static void a(EntityLocalPlayer entityLocalPlayer, long j, cy cyVar, int i, int i2, cy cyVar2, int i3) {
        ItemData itemData;
        ItemData itemData2;
        EntityLocalPlayer entityLocalPlayer2 = entityLocalPlayer;
        long j2 = e ^ j;
        long j3 = 53704863388296L ^ j2;
        long j4 = 4215304673868L ^ j2;
        long j5 = 140299142153898L ^ j2;
        long j6 = 82092639697788L ^ j2;
        long j7 = j2 ^ 128930344210490L;
        long j8 = j2 ^ 60684782624473L;
        ItemData a2 = cyVar.a(i, j3);
        ItemData a3 = cyVar2.a(i3, j3);
        ItemData build = a2.getCount() - i2 <= 0 ? ItemData.AIR : a2.toBuilder().count(a2.getCount() - i2).build();
        ItemData build2 = a3.toBuilder().count(a3.getCount() + i2).build();
        if (a5.d) {
            ItemStackRequestPacket itemStackRequestPacket = new ItemStackRequestPacket();
            itemStackRequestPacket.getRequests().add(new ItemStackRequest(0, new ItemStackRequestAction[]{new PlaceAction(i2, new ItemStackRequestSlotData(cyVar.b(j4, i), cyVar.c(j7, i), a2.getNetId()), new ItemStackRequestSlotData(cyVar2.b(j4, i3), cyVar2.c(j7, i3), a3.getNetId()))}, new String[0], null));
            entityLocalPlayer2 = entityLocalPlayer;
            entityLocalPlayer2.d(itemStackRequestPacket, j6);
            itemData2 = build2;
            itemData = build;
        } else {
            InventoryTransactionPacket inventoryTransactionPacket = new InventoryTransactionPacket();
            inventoryTransactionPacket.setTransactionType(InventoryTransactionType.NORMAL);
            itemData = build;
            inventoryTransactionPacket.getActions().add(new InventoryActionData(InventorySource.fromContainerWindowId(cyVar.a()), i, a2, itemData));
            itemData2 = build2;
            inventoryTransactionPacket.getActions().add(new InventoryActionData(InventorySource.fromContainerWindowId(cyVar2.a()), i3, a3, itemData2));
            entityLocalPlayer2.d(inventoryTransactionPacket, j6);
        }
        InventorySlotPacket inventorySlotPacket = new InventorySlotPacket();
        inventorySlotPacket.setSlot(cyVar.c(j7, i));
        inventorySlotPacket.setItem(itemData);
        inventorySlotPacket.setContainerId(cyVar.a());
        entityLocalPlayer2.b(inventorySlotPacket, j5);
        InventorySlotPacket inventorySlotPacket2 = new InventorySlotPacket();
        inventorySlotPacket2.setSlot(cyVar2.c(j7, i3));
        inventorySlotPacket2.setItem(itemData2);
        inventorySlotPacket2.setContainerId(cyVar2.a());
        entityLocalPlayer2.b(inventorySlotPacket2, j5);
        cyVar.a(i, itemData, j8);
        cyVar2.a(i3, itemData2, j8);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static void a(EntityLocalPlayer entityLocalPlayer, long j, cy cyVar, int i, cy cyVar2, int i2) {
        long j2;
        long j3;
        InventoryTransactionPacket inventoryTransactionPacket;
        long j4;
        InventoryTransactionPacket inventoryTransactionPacket2;
        EntityLocalPlayer entityLocalPlayer2 = entityLocalPlayer;
        long j5 = e ^ j;
        long j6 = 62351530956044L ^ j5;
        long j7 = 12885038751176L ^ j5;
        long j8 = 131892189020462L ^ j5;
        long j9 = 73413157993720L ^ j5;
        long j10 = j5 ^ 137643313239998L;
        long j11 = j5 ^ 69665401198941L;
        ItemData a2 = cyVar.a(i, j6);
        ItemData a3 = cyVar2.a(i2, j6);
        if (a5.d) {
            ItemStackRequestPacket itemStackRequestPacket = new ItemStackRequestPacket();
            j2 = j11;
            if (a3 == ItemData.AIR) {
                j4 = j9;
                inventoryTransactionPacket2 = itemStackRequestPacket;
                itemStackRequestPacket.getRequests().add(new ItemStackRequest(0, new ItemStackRequestAction[]{new PlaceAction(a2.getCount(), new ItemStackRequestSlotData(cyVar.b(j7, i), cyVar.c(j10, i), a2.getNetId()), new ItemStackRequestSlotData(cyVar2.b(j7, i2), cyVar2.c(j10, i2), a3.getNetId()))}, new String[0], null));
            } else {
                ItemStackRequestPacket itemStackRequestPacket2 = itemStackRequestPacket;
                j4 = j9;
                itemStackRequestPacket2.getRequests().add(new ItemStackRequest(0, new ItemStackRequestAction[]{new SwapAction(new ItemStackRequestSlotData(cyVar.b(j7, i), cyVar.c(j10, i), a2.getNetId()), new ItemStackRequestSlotData(cyVar2.b(j7, i2), cyVar2.c(j10, i2), a3.getNetId()))}, new String[0], null));
                inventoryTransactionPacket2 = itemStackRequestPacket2;
            }
            entityLocalPlayer2 = entityLocalPlayer;
            j3 = j4;
            inventoryTransactionPacket = inventoryTransactionPacket2;
        } else {
            j2 = j11;
            j3 = j9;
            inventoryTransactionPacket = new InventoryTransactionPacket();
            inventoryTransactionPacket.setTransactionType(InventoryTransactionType.NORMAL);
            inventoryTransactionPacket.getActions().add(new InventoryActionData(InventorySource.fromContainerWindowId(cyVar.a()), i, a2, a3));
            inventoryTransactionPacket.getActions().add(new InventoryActionData(InventorySource.fromContainerWindowId(cyVar2.a()), i2, a3, a2));
        }
        entityLocalPlayer2.d(inventoryTransactionPacket, j3);
        InventorySlotPacket inventorySlotPacket = new InventorySlotPacket();
        inventorySlotPacket.setSlot(cyVar.c(j10, i));
        inventorySlotPacket.setItem(a3);
        inventorySlotPacket.setContainerId(cyVar.a());
        entityLocalPlayer2.b(inventorySlotPacket, j8);
        InventorySlotPacket inventorySlotPacket2 = new InventorySlotPacket();
        inventorySlotPacket2.setSlot(cyVar2.c(j10, i2));
        inventorySlotPacket2.setItem(a2);
        inventorySlotPacket2.setContainerId(cyVar2.a());
        entityLocalPlayer2.b(inventorySlotPacket2, j8);
        long j12 = j2;
        cyVar.a(i, a3, j12);
        cyVar2.a(i2, a2, j12);
    }

    public static boolean a(ItemData itemData, com.trossense.sdk.f fVar, int i, cy cyVar, long j) {
        ItemData a2;
        long j2 = j ^ e;
        long j3 = 75330577296598L ^ j2;
        long j4 = j2 ^ 81730008308200L;
        if (itemData == ItemData.AIR) {
            return false;
        }
        for (String str : d) {
            if (fVar.c.contains(str)) {
                float a3 = c4.a(j4, itemData, fVar);
                for (int i2 = 0; i2 < cyVar.b(); i2++) {
                    if (i2 != i && (a2 = cyVar.a(i2, j3)) != ItemData.AIR && (a2.getDefinition() instanceof com.trossense.sdk.f)) {
                        com.trossense.sdk.f fVar2 = (com.trossense.sdk.f) a2.getDefinition();
                        if (fVar2.c.contains(str) && a3 <= c4.a(j4, a2, fVar2)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 'S');
        }
        return charArray;
    }

    public static c0 b() {
        return b;
    }

    @bk(5)
    public void a(ba baVar) {
        c0 c0Var;
        int slot;
        long j = (e ^ 100820748267414L) ^ 93477282773757L;
        if (baVar.e() instanceof com.trossense.sdk.p) {
            com.trossense.sdk.p pVar = (com.trossense.sdk.p) baVar.e();
            if (pVar.b() == ContainerType.CONTAINER && pVar.a() != 0) {
                cz czVar = new cz();
                a = czVar;
                czVar.a = pVar.a();
            }
            if (pVar.a() == 0) {
                a = b;
            }
        }
        if (baVar.e() instanceof ContainerClosePacket) {
            ContainerClosePacket containerClosePacket = (ContainerClosePacket) baVar.e();
            if (a != null && containerClosePacket.getId() == a.a()) {
                a = null;
            }
        }
        if (baVar.e() instanceof InventoryContentPacket) {
            InventoryContentPacket inventoryContentPacket = (InventoryContentPacket) baVar.e();
            if (inventoryContentPacket.getContainerId() == 0) {
                b.a = inventoryContentPacket.getContents();
            } else if (inventoryContentPacket.getContainerId() == 119) {
                b.c = inventoryContentPacket.getContents().get(0);
            } else if (inventoryContentPacket.getContainerId() == 120) {
                b.b = inventoryContentPacket.getContents();
            } else {
                cy cyVar = a;
                if (cyVar != null && cyVar.a() == inventoryContentPacket.getContainerId()) {
                    cy cyVar2 = a;
                    if (cyVar2 instanceof cz) {
                        ((cz) cyVar2).b = inventoryContentPacket.getContents();
                    }
                }
            }
        }
        if (baVar.e() instanceof InventorySlotPacket) {
            InventorySlotPacket inventorySlotPacket = (InventorySlotPacket) baVar.e();
            if (inventorySlotPacket.getContainerId() == 0) {
                int slot2 = inventorySlotPacket.getSlot();
                if (b.a == null || slot2 < 0 || slot2 > b.a.size()) {
                    return;
                }
                b.a.set(slot2, inventorySlotPacket.getItem());
                return;
            }
            if (inventorySlotPacket.getContainerId() == 119) {
                c0Var = b;
                slot = 40;
            } else {
                if (inventorySlotPacket.getContainerId() != 120) {
                    cy cyVar3 = a;
                    if (cyVar3 == null || cyVar3.a() != inventorySlotPacket.getContainerId()) {
                        return;
                    }
                    a.a(inventorySlotPacket.getSlot(), inventorySlotPacket.getItem(), j);
                    return;
                }
                c0Var = b;
                slot = inventorySlotPacket.getSlot() + 36;
            }
            c0Var.a(slot, inventorySlotPacket.getItem(), j);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:165:0x02e8, code lost:            r9 = r13;     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:105:0x0206  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x0267  */
    /* JADX WARN: Removed duplicated region for block: B:134:0x02a2  */
    /* JADX WARN: Removed duplicated region for block: B:148:0x02dd A[LOOP:3: B:73:0x010f->B:148:0x02dd, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:149:0x02ef A[EDGE_INSN: B:149:0x02ef->B:150:0x02ef BREAK  A[LOOP:3: B:73:0x010f->B:148:0x02dd], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:158:0x0183  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0301  */
    /* JADX WARN: Removed duplicated region for block: B:65:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:87:0x0165  */
    /* JADX WARN: Type inference failed for: r7v35, types: [com.trossense.cy] */
    @com.trossense.bk(5)
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void a(com.trossense.bb r24) {
        /*
            Method dump skipped, instructions count: 775
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.a7.a(com.trossense.bb):void");
    }
}
