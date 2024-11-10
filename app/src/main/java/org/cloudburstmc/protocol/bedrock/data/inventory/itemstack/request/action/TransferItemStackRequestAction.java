package org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action;

import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.ItemStackRequestSlotData;

/* loaded from: classes5.dex */
public interface TransferItemStackRequestAction extends ItemStackRequestAction {
    int getCount();

    ItemStackRequestSlotData getDestination();

    ItemStackRequestSlotData getSource();
}
