package org.cloudburstmc.protocol.bedrock.data.inventory.descriptor;

import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;

/* loaded from: classes5.dex */
public final class DeferredDescriptor implements ItemDescriptor {
    private final int auxValue;
    private final String fullName;

    public DeferredDescriptor(String fullName, int auxValue) {
        this.fullName = fullName;
        this.auxValue = auxValue;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DeferredDescriptor)) {
            return false;
        }
        DeferredDescriptor other = (DeferredDescriptor) o;
        if (getAuxValue() != other.getAuxValue()) {
            return false;
        }
        Object this$fullName = getFullName();
        Object other$fullName = other.getFullName();
        return this$fullName != null ? this$fullName.equals(other$fullName) : other$fullName == null;
    }

    public int hashCode() {
        int result = (1 * 59) + getAuxValue();
        Object $fullName = getFullName();
        return (result * 59) + ($fullName == null ? 43 : $fullName.hashCode());
    }

    public String toString() {
        return "DeferredDescriptor(fullName=" + getFullName() + ", auxValue=" + getAuxValue() + ")";
    }

    public String getFullName() {
        return this.fullName;
    }

    public int getAuxValue() {
        return this.auxValue;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.ItemDescriptor
    public ItemDescriptorType getType() {
        return ItemDescriptorType.DEFERRED;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.ItemDescriptor
    public ItemData.Builder toItem() {
        throw new UnsupportedOperationException();
    }
}
