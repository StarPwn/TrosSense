package org.cloudburstmc.protocol.bedrock.data.inventory.descriptor;

import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;

/* loaded from: classes5.dex */
public final class MolangDescriptor implements ItemDescriptor {
    private final int molangVersion;
    private final String tagExpression;

    public MolangDescriptor(String tagExpression, int molangVersion) {
        this.tagExpression = tagExpression;
        this.molangVersion = molangVersion;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MolangDescriptor)) {
            return false;
        }
        MolangDescriptor other = (MolangDescriptor) o;
        if (getMolangVersion() != other.getMolangVersion()) {
            return false;
        }
        Object this$tagExpression = getTagExpression();
        Object other$tagExpression = other.getTagExpression();
        return this$tagExpression != null ? this$tagExpression.equals(other$tagExpression) : other$tagExpression == null;
    }

    public int hashCode() {
        int result = (1 * 59) + getMolangVersion();
        Object $tagExpression = getTagExpression();
        return (result * 59) + ($tagExpression == null ? 43 : $tagExpression.hashCode());
    }

    public String toString() {
        return "MolangDescriptor(tagExpression=" + getTagExpression() + ", molangVersion=" + getMolangVersion() + ")";
    }

    public String getTagExpression() {
        return this.tagExpression;
    }

    public int getMolangVersion() {
        return this.molangVersion;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.ItemDescriptor
    public ItemDescriptorType getType() {
        return ItemDescriptorType.MOLANG;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.ItemDescriptor
    public ItemData.Builder toItem() {
        throw new UnsupportedOperationException();
    }
}
