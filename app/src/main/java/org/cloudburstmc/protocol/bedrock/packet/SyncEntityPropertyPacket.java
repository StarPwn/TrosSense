package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class SyncEntityPropertyPacket implements BedrockPacket {
    private NbtMap data;

    public void setData(NbtMap data) {
        this.data = data;
    }

    public String toString() {
        return "SyncEntityPropertyPacket(data=" + getData() + ")";
    }

    protected boolean canEqual(Object other) {
        return other instanceof SyncEntityPropertyPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SyncEntityPropertyPacket)) {
            return false;
        }
        SyncEntityPropertyPacket other = (SyncEntityPropertyPacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$data = this.data;
        Object other$data = other.data;
        return this$data != null ? this$data.equals(other$data) : other$data == null;
    }

    public int hashCode() {
        Object $data = this.data;
        int result = (1 * 59) + ($data == null ? 43 : $data.hashCode());
        return result;
    }

    public NbtMap getData() {
        return this.data;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.SYNC_ENTITY_PROPERTY;
    }
}
