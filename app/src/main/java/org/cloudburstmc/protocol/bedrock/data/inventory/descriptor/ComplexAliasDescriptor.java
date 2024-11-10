package org.cloudburstmc.protocol.bedrock.data.inventory.descriptor;

import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;

/* loaded from: classes5.dex */
public final class ComplexAliasDescriptor implements ItemDescriptor {
    private final String name;

    public ComplexAliasDescriptor(String name) {
        this.name = name;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ComplexAliasDescriptor)) {
            return false;
        }
        ComplexAliasDescriptor other = (ComplexAliasDescriptor) o;
        Object this$name = getName();
        Object other$name = other.getName();
        return this$name != null ? this$name.equals(other$name) : other$name == null;
    }

    public int hashCode() {
        Object $name = getName();
        int result = (1 * 59) + ($name == null ? 43 : $name.hashCode());
        return result;
    }

    public String toString() {
        return "ComplexAliasDescriptor(name=" + getName() + ")";
    }

    public String getName() {
        return this.name;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.ItemDescriptor
    public ItemDescriptorType getType() {
        return ItemDescriptorType.COMPLEX_ALIAS;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.ItemDescriptor
    public ItemData.Builder toItem() {
        throw new UnsupportedOperationException();
    }
}
