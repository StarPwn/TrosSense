package org.cloudburstmc.protocol.bedrock.packet;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import org.cloudburstmc.protocol.bedrock.data.inventory.ComponentItemData;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class ItemComponentPacket implements BedrockPacket {
    private final List<ComponentItemData> items = new ObjectArrayList();

    protected boolean canEqual(Object other) {
        return other instanceof ItemComponentPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ItemComponentPacket)) {
            return false;
        }
        ItemComponentPacket other = (ItemComponentPacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$items = this.items;
        Object other$items = other.items;
        return this$items != null ? this$items.equals(other$items) : other$items == null;
    }

    public int hashCode() {
        Object $items = this.items;
        int result = (1 * 59) + ($items == null ? 43 : $items.hashCode());
        return result;
    }

    public String toString() {
        return "ItemComponentPacket(items=" + this.items + ")";
    }

    public List<ComponentItemData> getItems() {
        return this.items;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.ITEM_COMPONENT;
    }
}
