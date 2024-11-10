package org.cloudburstmc.protocol.bedrock.data.inventory;

import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.protocol.bedrock.data.definitions.BlockDefinition;
import org.cloudburstmc.protocol.bedrock.data.definitions.ItemDefinition;

/* loaded from: classes5.dex */
public interface ItemData {
    public static final ItemData AIR = new BaseItemData(ItemDefinition.AIR, 0, 0, null, BaseItemData.EMPTY_ARRAY, BaseItemData.EMPTY_ARRAY, 0, null, false, 0);

    boolean equals(ItemData itemData, boolean z, boolean z2, boolean z3);

    BlockDefinition getBlockDefinition();

    long getBlockingTicks();

    String[] getCanBreak();

    String[] getCanPlace();

    int getCount();

    int getDamage();

    ItemDefinition getDefinition();

    int getNetId();

    NbtMap getTag();

    boolean isNull();

    boolean isUsingNetId();

    boolean isValid();

    void setNetId(int i);

    default Builder toBuilder() {
        return new Builder();
    }

    static Builder builder() {
        return new Builder();
    }

    /* loaded from: classes5.dex */
    public static class Builder {
        private BlockDefinition blockDefinition;
        private long blockingTicks;
        private String[] canBreak;
        private String[] canPlace;
        private int count;
        private int damage;
        private ItemDefinition definition;
        private int netId;
        private NbtMap tag;
        private boolean usingNetId;

        private Builder() {
        }

        private Builder(ItemData data) {
            this.definition = data.getDefinition();
            this.damage = data.getDamage();
            this.count = data.getCount();
            this.tag = data.getTag();
            this.canPlace = data.getCanPlace();
            this.canBreak = data.getCanBreak();
            this.blockingTicks = data.getBlockingTicks();
            this.blockDefinition = data.getBlockDefinition();
            this.usingNetId = data.isUsingNetId();
            this.netId = data.getNetId();
        }

        public Builder definition(ItemDefinition definition) {
            this.definition = definition;
            return this;
        }

        public Builder damage(int damage) {
            this.damage = damage;
            return this;
        }

        public Builder count(int count) {
            this.count = count;
            return this;
        }

        public Builder tag(NbtMap tag) {
            this.tag = tag;
            return this;
        }

        public Builder canPlace(String... canPlace) {
            this.canPlace = canPlace;
            return this;
        }

        public Builder canBreak(String... canBreak) {
            this.canBreak = canBreak;
            return this;
        }

        public Builder blockingTicks(long blockingTicks) {
            this.blockingTicks = blockingTicks;
            return this;
        }

        public Builder blockDefinition(BlockDefinition blockDefinition) {
            this.blockDefinition = blockDefinition;
            return this;
        }

        public Builder usingNetId(boolean usingNetId) {
            this.usingNetId = usingNetId;
            return this;
        }

        public Builder netId(int netId) {
            this.netId = netId;
            return this;
        }

        public ItemData build() {
            return new BaseItemData(this.definition, this.damage, this.count, this.tag, this.canPlace, this.canBreak, this.blockingTicks, this.blockDefinition, this.usingNetId, this.netId);
        }
    }
}
