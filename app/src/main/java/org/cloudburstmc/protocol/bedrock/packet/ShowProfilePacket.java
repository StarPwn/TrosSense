package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class ShowProfilePacket implements BedrockPacket {
    private String xuid;

    public void setXuid(String xuid) {
        this.xuid = xuid;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ShowProfilePacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ShowProfilePacket)) {
            return false;
        }
        ShowProfilePacket other = (ShowProfilePacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$xuid = this.xuid;
        Object other$xuid = other.xuid;
        return this$xuid != null ? this$xuid.equals(other$xuid) : other$xuid == null;
    }

    public int hashCode() {
        Object $xuid = this.xuid;
        int result = (1 * 59) + ($xuid == null ? 43 : $xuid.hashCode());
        return result;
    }

    public String toString() {
        return "ShowProfilePacket(xuid=" + this.xuid + ")";
    }

    public String getXuid() {
        return this.xuid;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.SHOW_PROFILE;
    }
}
