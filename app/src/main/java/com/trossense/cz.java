package com.trossense;

import java.util.List;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerSlotType;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;

/* loaded from: classes3.dex */
public class cz implements cy {
    public int a;
    public List<ItemData> b;

    @Override // com.trossense.cy
    public int a() {
        return this.a;
    }

    @Override // com.trossense.cy
    public ItemData a(int i, long j) {
        List<ItemData> list = this.b;
        if (j <= 0 || list != null) {
            int size = list.size();
            if (j > 0) {
                if (size > i) {
                    size = i;
                }
            }
            if (size >= 0) {
                return this.b.get(i);
            }
        }
        return ItemData.AIR;
    }

    @Override // com.trossense.cy
    public void a(int i, ItemData itemData, long j) {
        int size = this.b.size();
        if (j > 0) {
            if (size <= i) {
                return;
            } else {
                size = i;
            }
        }
        if (size < 0) {
            return;
        }
        this.b.set(i, itemData);
    }

    @Override // com.trossense.cy
    public int b() {
        return this.b.size();
    }

    @Override // com.trossense.cy
    public ContainerSlotType b(long j, int i) {
        return ContainerSlotType.LEVEL_ENTITY;
    }

    @Override // com.trossense.cy
    public int c(long j, int i) {
        return i;
    }
}
