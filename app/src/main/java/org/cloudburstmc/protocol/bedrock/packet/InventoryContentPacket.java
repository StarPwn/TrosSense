package org.cloudburstmc.protocol.bedrock.packet;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class InventoryContentPacket implements BedrockPacket {
    private int containerId;
    private List<ItemData> contents = new ObjectArrayList();

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

    public void setContents(List<ItemData> contents) {
        this.contents = contents;
    }

    protected boolean canEqual(Object other) {
        return other instanceof InventoryContentPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof InventoryContentPacket)) {
            return false;
        }
        InventoryContentPacket other = (InventoryContentPacket) o;
        if (!other.canEqual(this) || this.containerId != other.containerId) {
            return false;
        }
        Object this$contents = this.contents;
        Object other$contents = other.contents;
        return this$contents != null ? this$contents.equals(other$contents) : other$contents == null;
    }

    public int hashCode() {
        int result = (1 * 59) + this.containerId;
        Object $contents = this.contents;
        return (result * 59) + ($contents == null ? 43 : $contents.hashCode());
    }

    public String toString() {
        return "InventoryContentPacket(contents=" + this.contents + ", containerId=" + this.containerId + ")";
    }

    public List<ItemData> getContents() {
        return this.contents;
    }

    public int getContainerId() {
        return this.containerId;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.INVENTORY_CONTENT;
    }
}
