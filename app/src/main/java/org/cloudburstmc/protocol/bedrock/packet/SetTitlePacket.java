package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class SetTitlePacket implements BedrockPacket {
    private int fadeInTime;
    private int fadeOutTime;
    private String platformOnlineId;
    private int stayTime;
    private String text;
    private Type type;
    private String xuid;

    /* loaded from: classes5.dex */
    public enum Type {
        CLEAR,
        RESET,
        TITLE,
        SUBTITLE,
        ACTIONBAR,
        TIMES,
        TITLE_JSON,
        SUBTITLE_JSON,
        ACTIONBAR_JSON
    }

    public void setFadeInTime(int fadeInTime) {
        this.fadeInTime = fadeInTime;
    }

    public void setFadeOutTime(int fadeOutTime) {
        this.fadeOutTime = fadeOutTime;
    }

    public void setPlatformOnlineId(String platformOnlineId) {
        this.platformOnlineId = platformOnlineId;
    }

    public void setStayTime(int stayTime) {
        this.stayTime = stayTime;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setXuid(String xuid) {
        this.xuid = xuid;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SetTitlePacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SetTitlePacket)) {
            return false;
        }
        SetTitlePacket other = (SetTitlePacket) o;
        if (!other.canEqual(this) || this.fadeInTime != other.fadeInTime || this.stayTime != other.stayTime || this.fadeOutTime != other.fadeOutTime) {
            return false;
        }
        Object this$type = this.type;
        Object other$type = other.type;
        if (this$type != null ? !this$type.equals(other$type) : other$type != null) {
            return false;
        }
        Object this$text = this.text;
        Object other$text = other.text;
        if (this$text != null ? !this$text.equals(other$text) : other$text != null) {
            return false;
        }
        Object this$xuid = this.xuid;
        Object other$xuid = other.xuid;
        if (this$xuid != null ? !this$xuid.equals(other$xuid) : other$xuid != null) {
            return false;
        }
        Object this$platformOnlineId = this.platformOnlineId;
        Object other$platformOnlineId = other.platformOnlineId;
        return this$platformOnlineId != null ? this$platformOnlineId.equals(other$platformOnlineId) : other$platformOnlineId == null;
    }

    public int hashCode() {
        int result = (1 * 59) + this.fadeInTime;
        int result2 = (((result * 59) + this.stayTime) * 59) + this.fadeOutTime;
        Object $type = this.type;
        int result3 = (result2 * 59) + ($type == null ? 43 : $type.hashCode());
        Object $text = this.text;
        int result4 = (result3 * 59) + ($text == null ? 43 : $text.hashCode());
        Object $xuid = this.xuid;
        int result5 = (result4 * 59) + ($xuid == null ? 43 : $xuid.hashCode());
        Object $platformOnlineId = this.platformOnlineId;
        return (result5 * 59) + ($platformOnlineId != null ? $platformOnlineId.hashCode() : 43);
    }

    public String toString() {
        return "SetTitlePacket(type=" + this.type + ", text=" + this.text + ", fadeInTime=" + this.fadeInTime + ", stayTime=" + this.stayTime + ", fadeOutTime=" + this.fadeOutTime + ", xuid=" + this.xuid + ", platformOnlineId=" + this.platformOnlineId + ")";
    }

    public Type getType() {
        return this.type;
    }

    public String getText() {
        return this.text;
    }

    public int getFadeInTime() {
        return this.fadeInTime;
    }

    public int getStayTime() {
        return this.stayTime;
    }

    public int getFadeOutTime() {
        return this.fadeOutTime;
    }

    public String getXuid() {
        return this.xuid;
    }

    public String getPlatformOnlineId() {
        return this.platformOnlineId;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.SET_TITLE;
    }
}
