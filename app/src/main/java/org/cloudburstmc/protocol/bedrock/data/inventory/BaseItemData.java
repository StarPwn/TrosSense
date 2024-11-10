package org.cloudburstmc.protocol.bedrock.data.inventory;

import java.util.Arrays;
import java.util.Objects;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.protocol.bedrock.data.definitions.BlockDefinition;
import org.cloudburstmc.protocol.bedrock.data.definitions.ItemDefinition;

/* loaded from: classes5.dex */
final class BaseItemData implements ItemData {
    static final String[] EMPTY_ARRAY = new String[0];
    private final BlockDefinition blockDefinition;
    private final long blockingTicks;
    private final String[] canBreak;
    private final String[] canPlace;
    private final int count;
    private final int damage;
    private ItemDefinition definition;
    private int netId;
    private final NbtMap tag;
    private boolean usingNetId;

    public void setDefinition(ItemDefinition definition) {
        this.definition = definition;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.ItemData
    public void setNetId(int netId) {
        this.netId = netId;
    }

    public void setUsingNetId(boolean usingNetId) {
        this.usingNetId = usingNetId;
    }

    public String toString() {
        return "BaseItemData(definition=" + getDefinition() + ", damage=" + getDamage() + ", count=" + getCount() + ", tag=" + getTag() + ", canPlace=" + Arrays.deepToString(getCanPlace()) + ", canBreak=" + Arrays.deepToString(getCanBreak()) + ", blockingTicks=" + getBlockingTicks() + ", blockDefinition=" + getBlockDefinition() + ", usingNetId=" + isUsingNetId() + ", netId=" + getNetId() + ")";
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.ItemData
    public ItemDefinition getDefinition() {
        return this.definition;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.ItemData
    public int getDamage() {
        return this.damage;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.ItemData
    public int getCount() {
        return this.count;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.ItemData
    public NbtMap getTag() {
        return this.tag;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.ItemData
    public String[] getCanPlace() {
        return this.canPlace;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.ItemData
    public String[] getCanBreak() {
        return this.canBreak;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.ItemData
    public long getBlockingTicks() {
        return this.blockingTicks;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.ItemData
    public BlockDefinition getBlockDefinition() {
        return this.blockDefinition;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.ItemData
    public boolean isUsingNetId() {
        return this.usingNetId;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.ItemData
    public int getNetId() {
        return this.netId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BaseItemData(ItemDefinition definition, int damage, int count, NbtMap tag, String[] canPlace, String[] canBreak, long blockingTicks, BlockDefinition blockDefinition, boolean hasNetId, int netId) {
        this.definition = definition;
        this.damage = damage;
        this.count = count;
        this.tag = tag;
        this.canPlace = canPlace == null ? EMPTY_ARRAY : canPlace;
        this.canBreak = canBreak == null ? EMPTY_ARRAY : canBreak;
        this.blockingTicks = blockingTicks;
        this.blockDefinition = blockDefinition;
        this.netId = netId;
        this.usingNetId = hasNetId;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.ItemData
    public boolean isValid() {
        return (isNull() || this.definition == null || this.definition == ItemDefinition.AIR) ? false : true;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.ItemData
    public boolean isNull() {
        return this.count <= 0;
    }

    public int hashCode() {
        return Objects.hash(this.definition, Integer.valueOf(this.damage), Integer.valueOf(this.count), this.tag, Integer.valueOf(Arrays.hashCode(this.canPlace)), Integer.valueOf(Arrays.hashCode(this.canBreak)), Long.valueOf(this.blockingTicks), this.blockDefinition);
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.ItemData
    public boolean equals(ItemData other, boolean checkAmount, boolean checkMetadata, boolean checkUserdata) {
        return this.definition == other.getDefinition() && (!checkAmount || this.count == other.getCount()) && ((!checkMetadata || (this.damage == other.getDamage() && this.blockingTicks == other.getBlockingTicks())) && (!checkUserdata || (Objects.equals(this.tag, other.getTag()) && Arrays.equals(this.canPlace, other.getCanPlace()) && Arrays.equals(this.canBreak, other.getCanBreak()))));
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof ItemData) {
            return equals((ItemData) obj, true, true, true);
        }
        return false;
    }
}
