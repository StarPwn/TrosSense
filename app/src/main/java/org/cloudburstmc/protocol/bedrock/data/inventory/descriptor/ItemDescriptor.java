package org.cloudburstmc.protocol.bedrock.data.inventory.descriptor;

import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;

/* loaded from: classes5.dex */
public interface ItemDescriptor {
    ItemDescriptorType getType();

    ItemData.Builder toItem();
}
