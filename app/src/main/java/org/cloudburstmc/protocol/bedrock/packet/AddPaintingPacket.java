package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class AddPaintingPacket extends AddHangingEntityPacket {
    private String motive;

    public void setMotive(String motive) {
        this.motive = motive;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.AddHangingEntityPacket
    protected boolean canEqual(Object other) {
        return other instanceof AddPaintingPacket;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.AddHangingEntityPacket
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AddPaintingPacket)) {
            return false;
        }
        AddPaintingPacket other = (AddPaintingPacket) o;
        if (!other.canEqual(this) || !super.equals(o)) {
            return false;
        }
        Object this$motive = this.motive;
        Object other$motive = other.motive;
        return this$motive != null ? this$motive.equals(other$motive) : other$motive == null;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.AddHangingEntityPacket
    public int hashCode() {
        int result = super.hashCode();
        Object $motive = this.motive;
        return (result * 59) + ($motive == null ? 43 : $motive.hashCode());
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.AddHangingEntityPacket
    public String toString() {
        return "AddPaintingPacket(motive=" + this.motive + ")";
    }

    public String getMotive() {
        return this.motive;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.AddHangingEntityPacket, org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.AddHangingEntityPacket, org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.ADD_PAINTING;
    }
}
