package org.cloudburstmc.protocol.bedrock.data.inventory.descriptor;

import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;

/* loaded from: classes5.dex */
public class ItemDescriptorWithCount {
    public static final ItemDescriptorWithCount EMPTY = new ItemDescriptorWithCount(InvalidDescriptor.INSTANCE, 0);
    private final int count;
    private final ItemDescriptor descriptor;

    public String toString() {
        return "ItemDescriptorWithCount(descriptor=" + getDescriptor() + ", count=" + getCount() + ")";
    }

    protected boolean canEqual(Object other) {
        return other instanceof ItemDescriptorWithCount;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ItemDescriptorWithCount)) {
            return false;
        }
        ItemDescriptorWithCount other = (ItemDescriptorWithCount) o;
        if (!other.canEqual(this) || getCount() != other.getCount()) {
            return false;
        }
        Object this$descriptor = getDescriptor();
        Object other$descriptor = other.getDescriptor();
        return this$descriptor != null ? this$descriptor.equals(other$descriptor) : other$descriptor == null;
    }

    public int hashCode() {
        int result = (1 * 59) + getCount();
        Object $descriptor = getDescriptor();
        return (result * 59) + ($descriptor == null ? 43 : $descriptor.hashCode());
    }

    public ItemDescriptorWithCount(ItemDescriptor descriptor, int count) {
        this.descriptor = descriptor;
        this.count = count;
    }

    public ItemDescriptor getDescriptor() {
        return this.descriptor;
    }

    public int getCount() {
        return this.count;
    }

    public ItemData toItem() {
        if (this.descriptor == InvalidDescriptor.INSTANCE) {
            return ItemData.AIR;
        }
        return this.descriptor.toItem().count(this.count).build();
    }

    public static ItemDescriptorWithCount fromItem(ItemData item) {
        if (item == ItemData.AIR) {
            return EMPTY;
        }
        return new ItemDescriptorWithCount(new DefaultDescriptor(item.getDefinition(), item.getDamage()), item.getCount());
    }
}
