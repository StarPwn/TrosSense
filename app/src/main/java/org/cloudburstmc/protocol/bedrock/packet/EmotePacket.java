package org.cloudburstmc.protocol.bedrock.packet;

import java.util.EnumSet;
import java.util.Set;
import org.cloudburstmc.protocol.bedrock.data.EmoteFlag;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class EmotePacket implements BedrockPacket {
    private String emoteId;
    private final Set<EmoteFlag> flags = EnumSet.noneOf(EmoteFlag.class);
    private String platformId;
    private long runtimeEntityId;
    private String xuid;

    public void setEmoteId(String emoteId) {
        this.emoteId = emoteId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public void setRuntimeEntityId(long runtimeEntityId) {
        this.runtimeEntityId = runtimeEntityId;
    }

    public void setXuid(String xuid) {
        this.xuid = xuid;
    }

    protected boolean canEqual(Object other) {
        return other instanceof EmotePacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof EmotePacket)) {
            return false;
        }
        EmotePacket other = (EmotePacket) o;
        if (!other.canEqual(this) || this.runtimeEntityId != other.runtimeEntityId) {
            return false;
        }
        Object this$xuid = this.xuid;
        Object other$xuid = other.xuid;
        if (this$xuid != null ? !this$xuid.equals(other$xuid) : other$xuid != null) {
            return false;
        }
        Object this$platformId = this.platformId;
        Object other$platformId = other.platformId;
        if (this$platformId != null ? !this$platformId.equals(other$platformId) : other$platformId != null) {
            return false;
        }
        Object this$emoteId = this.emoteId;
        Object other$emoteId = other.emoteId;
        if (this$emoteId != null ? !this$emoteId.equals(other$emoteId) : other$emoteId != null) {
            return false;
        }
        Object this$flags = this.flags;
        Object other$flags = other.flags;
        return this$flags != null ? this$flags.equals(other$flags) : other$flags == null;
    }

    public int hashCode() {
        long $runtimeEntityId = this.runtimeEntityId;
        int result = (1 * 59) + ((int) (($runtimeEntityId >>> 32) ^ $runtimeEntityId));
        Object $xuid = this.xuid;
        int result2 = (result * 59) + ($xuid == null ? 43 : $xuid.hashCode());
        Object $platformId = this.platformId;
        int result3 = (result2 * 59) + ($platformId == null ? 43 : $platformId.hashCode());
        Object $emoteId = this.emoteId;
        int result4 = (result3 * 59) + ($emoteId == null ? 43 : $emoteId.hashCode());
        Object $flags = this.flags;
        return (result4 * 59) + ($flags != null ? $flags.hashCode() : 43);
    }

    public String toString() {
        return "EmotePacket(runtimeEntityId=" + this.runtimeEntityId + ", xuid=" + this.xuid + ", platformId=" + this.platformId + ", emoteId=" + this.emoteId + ", flags=" + this.flags + ")";
    }

    public long getRuntimeEntityId() {
        return this.runtimeEntityId;
    }

    public String getXuid() {
        return this.xuid;
    }

    public String getPlatformId() {
        return this.platformId;
    }

    public String getEmoteId() {
        return this.emoteId;
    }

    public Set<EmoteFlag> getFlags() {
        return this.flags;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.EMOTE;
    }
}
