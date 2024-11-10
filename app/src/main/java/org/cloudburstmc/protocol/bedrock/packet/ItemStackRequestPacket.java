package org.cloudburstmc.protocol.bedrock.packet;

import java.util.ArrayList;
import java.util.List;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.ItemStackRequest;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class ItemStackRequestPacket implements BedrockPacket {
    private final List<ItemStackRequest> requests = new ArrayList();

    protected boolean canEqual(Object other) {
        return other instanceof ItemStackRequestPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ItemStackRequestPacket)) {
            return false;
        }
        ItemStackRequestPacket other = (ItemStackRequestPacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$requests = getRequests();
        Object other$requests = other.getRequests();
        return this$requests != null ? this$requests.equals(other$requests) : other$requests == null;
    }

    public int hashCode() {
        Object $requests = getRequests();
        int result = (1 * 59) + ($requests == null ? 43 : $requests.hashCode());
        return result;
    }

    public String toString() {
        return "ItemStackRequestPacket(requests=" + this.requests + ")";
    }

    public List<ItemStackRequest> getRequests() {
        return this.requests;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.ITEM_STACK_REQUEST;
    }
}
