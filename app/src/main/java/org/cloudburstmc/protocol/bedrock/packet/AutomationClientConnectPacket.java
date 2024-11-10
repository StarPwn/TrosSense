package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class AutomationClientConnectPacket implements BedrockPacket {
    private String address;

    public void setAddress(String address) {
        this.address = address;
    }

    protected boolean canEqual(Object other) {
        return other instanceof AutomationClientConnectPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AutomationClientConnectPacket)) {
            return false;
        }
        AutomationClientConnectPacket other = (AutomationClientConnectPacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$address = this.address;
        Object other$address = other.address;
        return this$address != null ? this$address.equals(other$address) : other$address == null;
    }

    public int hashCode() {
        Object $address = this.address;
        int result = (1 * 59) + ($address == null ? 43 : $address.hashCode());
        return result;
    }

    public String toString() {
        return "AutomationClientConnectPacket(address=" + this.address + ")";
    }

    public String getAddress() {
        return this.address;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.AUTOMATION_CLIENT_CONNECT;
    }
}
