package org.cloudburstmc.protocol.bedrock.packet;

import java.util.ArrayList;
import java.util.List;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.response.ItemStackResponse;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class ItemStackResponsePacket implements BedrockPacket {
    private final List<ItemStackResponse> entries = new ArrayList();

    protected boolean canEqual(Object other) {
        return other instanceof ItemStackResponsePacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ItemStackResponsePacket)) {
            return false;
        }
        ItemStackResponsePacket other = (ItemStackResponsePacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$entries = getEntries();
        Object other$entries = other.getEntries();
        return this$entries != null ? this$entries.equals(other$entries) : other$entries == null;
    }

    public int hashCode() {
        Object $entries = getEntries();
        int result = (1 * 59) + ($entries == null ? 43 : $entries.hashCode());
        return result;
    }

    public String toString() {
        return "ItemStackResponsePacket(entries=" + this.entries + ")";
    }

    public List<ItemStackResponse> getEntries() {
        return this.entries;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.ITEM_STACK_RESPONSE;
    }
}
