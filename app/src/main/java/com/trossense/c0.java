package com.trossense;

import com.trossense.sdk.PointerHolder;
import java.lang.invoke.MethodHandles;
import java.util.List;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerSlotType;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;

/* loaded from: classes3.dex */
public class c0 implements cy {
    private static int[] d;
    private static final long e = dj.a(-6720574976794254218L, 5857854592048311383L, MethodHandles.lookup().lookupClass()).a(89041207988950L);
    public List<ItemData> a;
    public List<ItemData> b;
    public ItemData c;

    static {
        if (c() != null) {
            b(new int[3]);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x002b, code lost:            r3.c = org.cloudburstmc.protocol.bedrock.data.inventory.ItemData.AIR;     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x002f, code lost:            return;     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public c0(int r4, short r5, char r6) {
        /*
            r3 = this;
            int[] r4 = c()
            r3.<init>()
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r3.b = r0
            r0 = 0
        Lf:
            r1 = 4
            if (r0 >= r1) goto L2b
            java.util.List<org.cloudburstmc.protocol.bedrock.data.inventory.ItemData> r1 = r3.b
            org.cloudburstmc.protocol.bedrock.data.inventory.ItemData r2 = org.cloudburstmc.protocol.bedrock.data.inventory.ItemData.AIR
            r1.add(r2)
            int r0 = r0 + 1
            r1 = r4
        L1c:
            if (r6 <= 0) goto L21
            if (r1 != 0) goto L2f
            r1 = r4
        L21:
            if (r1 == 0) goto Lf
            r1 = 1
            int[] r1 = new int[r1]
            if (r5 <= 0) goto L1c
            com.trossense.sdk.PointerHolder.b(r1)
        L2b:
            org.cloudburstmc.protocol.bedrock.data.inventory.ItemData r4 = org.cloudburstmc.protocol.bedrock.data.inventory.ItemData.AIR
            r3.c = r4
        L2f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.c0.<init>(int, short, char):void");
    }

    public static void b(int[] iArr) {
        d = iArr;
    }

    public static int[] c() {
        return d;
    }

    @Override // com.trossense.cy
    public int a() {
        return 0;
    }

    @Override // com.trossense.cy
    public ItemData a(int i, long j) {
        int i2 = 36;
        if (j >= 0) {
            if (i >= 36) {
                if (j < 0) {
                    i2 = 39;
                } else if (i <= 39) {
                    return this.b.get(i - 36);
                }
            }
            i2 = 40;
        }
        if (i == i2) {
            return this.c;
        }
        List<ItemData> list = this.a;
        if (j <= 0 || list != null) {
            int size = list.size();
            if (j >= 0) {
                if (size > i) {
                    size = i;
                }
            }
            if (size >= 0) {
                return this.a.get(i);
            }
        }
        return ItemData.AIR;
    }

    @Override // com.trossense.cy
    public void a(int i, ItemData itemData, long j) {
        c();
        if (i >= 36 && i <= 39) {
            this.b.set(i - 36, itemData);
            return;
        }
        if (i == 40) {
            this.c = itemData;
            return;
        }
        if (this.a.size() <= i || i < 0) {
            return;
        }
        this.a.set(i, itemData);
        if (PointerHolder.s() == null) {
            b(new int[4]);
        }
    }

    @Override // com.trossense.cy
    public int b() {
        return this.a.size() + 5;
    }

    @Override // com.trossense.cy
    public ContainerSlotType b(long j, int i) {
        return (i < 36 || i > 39) ? i == 40 ? ContainerSlotType.OFFHAND : ContainerSlotType.HOTBAR_AND_INVENTORY : ContainerSlotType.ARMOR;
    }

    @Override // com.trossense.cy
    public int c(long j, int i) {
        if (i >= 36 && i <= 39) {
            return i - 36;
        }
        if (i == 40) {
            return 0;
        }
        return i;
    }
}
