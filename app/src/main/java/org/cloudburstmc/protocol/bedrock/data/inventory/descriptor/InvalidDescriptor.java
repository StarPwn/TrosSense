package org.cloudburstmc.protocol.bedrock.data.inventory.descriptor;

import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;

/* loaded from: classes5.dex */
public class InvalidDescriptor implements ItemDescriptor {
    public static final InvalidDescriptor INSTANCE = new InvalidDescriptor();

    public String toString() {
        return "InvalidDescriptor()";
    }

    private InvalidDescriptor() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.ItemDescriptor
    public ItemDescriptorType getType() {
        return ItemDescriptorType.INVALID;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.ItemDescriptor
    public ItemData.Builder toItem() {
        throw new UnsupportedOperationException();
    }
}
