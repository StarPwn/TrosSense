package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class TransferPacket implements BedrockPacket {
    private String address;
    private int port;

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPort(int port) {
        this.port = port;
    }

    protected boolean canEqual(Object other) {
        return other instanceof TransferPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TransferPacket)) {
            return false;
        }
        TransferPacket other = (TransferPacket) o;
        if (!other.canEqual(this) || this.port != other.port) {
            return false;
        }
        Object this$address = this.address;
        Object other$address = other.address;
        return this$address != null ? this$address.equals(other$address) : other$address == null;
    }

    public int hashCode() {
        int result = (1 * 59) + this.port;
        Object $address = this.address;
        return (result * 59) + ($address == null ? 43 : $address.hashCode());
    }

    public String toString() {
        return "TransferPacket(address=" + this.address + ", port=" + this.port + ")";
    }

    public String getAddress() {
        return this.address;
    }

    public int getPort() {
        return this.port;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.TRANSFER;
    }
}
