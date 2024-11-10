package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class EditorNetworkPacket implements BedrockPacket {
    private Object payload;

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    protected boolean canEqual(Object other) {
        return other instanceof EditorNetworkPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof EditorNetworkPacket)) {
            return false;
        }
        EditorNetworkPacket other = (EditorNetworkPacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$payload = this.payload;
        Object other$payload = other.payload;
        return this$payload != null ? this$payload.equals(other$payload) : other$payload == null;
    }

    public int hashCode() {
        Object $payload = this.payload;
        int result = (1 * 59) + ($payload == null ? 43 : $payload.hashCode());
        return result;
    }

    public String toString() {
        return "EditorNetworkPacket(payload=" + this.payload + ")";
    }

    public Object getPayload() {
        return this.payload;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.EDITOR_NETWORK;
    }
}
