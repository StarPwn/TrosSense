package org.cloudburstmc.protocol.bedrock.packet;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class ResourcePackClientResponsePacket implements BedrockPacket {
    private final List<String> packIds = new ObjectArrayList();
    private Status status;

    /* loaded from: classes5.dex */
    public enum Status {
        NONE,
        REFUSED,
        SEND_PACKS,
        HAVE_ALL_PACKS,
        COMPLETED
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ResourcePackClientResponsePacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ResourcePackClientResponsePacket)) {
            return false;
        }
        ResourcePackClientResponsePacket other = (ResourcePackClientResponsePacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$packIds = this.packIds;
        Object other$packIds = other.packIds;
        if (this$packIds != null ? !this$packIds.equals(other$packIds) : other$packIds != null) {
            return false;
        }
        Object this$status = this.status;
        Object other$status = other.status;
        return this$status != null ? this$status.equals(other$status) : other$status == null;
    }

    public int hashCode() {
        Object $packIds = this.packIds;
        int result = (1 * 59) + ($packIds == null ? 43 : $packIds.hashCode());
        Object $status = this.status;
        return (result * 59) + ($status != null ? $status.hashCode() : 43);
    }

    public String toString() {
        return "ResourcePackClientResponsePacket(packIds=" + this.packIds + ", status=" + this.status + ")";
    }

    public List<String> getPackIds() {
        return this.packIds;
    }

    public Status getStatus() {
        return this.status;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.RESOURCE_PACK_CLIENT_RESPONSE;
    }
}
