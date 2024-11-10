package org.cloudburstmc.protocol.bedrock.packet;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class TextPacket implements BedrockPacket {
    private String message;
    private boolean needsTranslation;
    private List<String> parameters = new ObjectArrayList();
    private String platformChatId = "";
    private String sourceName;
    private Type type;
    private String xuid;

    /* loaded from: classes5.dex */
    public enum Type {
        RAW,
        CHAT,
        TRANSLATION,
        POPUP,
        JUKEBOX_POPUP,
        TIP,
        SYSTEM,
        WHISPER,
        ANNOUNCEMENT,
        WHISPER_JSON,
        JSON,
        ANNOUNCEMENT_JSON
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setNeedsTranslation(boolean needsTranslation) {
        this.needsTranslation = needsTranslation;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }

    public void setPlatformChatId(String platformChatId) {
        this.platformChatId = platformChatId;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setXuid(String xuid) {
        this.xuid = xuid;
    }

    protected boolean canEqual(Object other) {
        return other instanceof TextPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TextPacket)) {
            return false;
        }
        TextPacket other = (TextPacket) o;
        if (!other.canEqual(this) || this.needsTranslation != other.needsTranslation) {
            return false;
        }
        Object this$type = this.type;
        Object other$type = other.type;
        if (this$type != null ? !this$type.equals(other$type) : other$type != null) {
            return false;
        }
        Object this$sourceName = this.sourceName;
        Object other$sourceName = other.sourceName;
        if (this$sourceName != null ? !this$sourceName.equals(other$sourceName) : other$sourceName != null) {
            return false;
        }
        Object this$message = this.message;
        Object other$message = other.message;
        if (this$message != null ? !this$message.equals(other$message) : other$message != null) {
            return false;
        }
        Object this$parameters = this.parameters;
        Object other$parameters = other.parameters;
        if (this$parameters != null ? !this$parameters.equals(other$parameters) : other$parameters != null) {
            return false;
        }
        Object this$xuid = this.xuid;
        Object other$xuid = other.xuid;
        if (this$xuid != null ? !this$xuid.equals(other$xuid) : other$xuid != null) {
            return false;
        }
        Object this$platformChatId = this.platformChatId;
        Object other$platformChatId = other.platformChatId;
        if (this$platformChatId == null) {
            if (other$platformChatId == null) {
                return true;
            }
        } else if (this$platformChatId.equals(other$platformChatId)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = (1 * 59) + (this.needsTranslation ? 79 : 97);
        Object $type = this.type;
        int result2 = (result * 59) + ($type == null ? 43 : $type.hashCode());
        Object $sourceName = this.sourceName;
        int result3 = (result2 * 59) + ($sourceName == null ? 43 : $sourceName.hashCode());
        Object $message = this.message;
        int result4 = (result3 * 59) + ($message == null ? 43 : $message.hashCode());
        Object $parameters = this.parameters;
        int result5 = (result4 * 59) + ($parameters == null ? 43 : $parameters.hashCode());
        Object $xuid = this.xuid;
        int result6 = (result5 * 59) + ($xuid == null ? 43 : $xuid.hashCode());
        Object $platformChatId = this.platformChatId;
        return (result6 * 59) + ($platformChatId != null ? $platformChatId.hashCode() : 43);
    }

    public String toString() {
        return "TextPacket(type=" + this.type + ", needsTranslation=" + this.needsTranslation + ", sourceName=" + this.sourceName + ", message=" + this.message + ", parameters=" + this.parameters + ", xuid=" + this.xuid + ", platformChatId=" + this.platformChatId + ")";
    }

    public Type getType() {
        return this.type;
    }

    public boolean isNeedsTranslation() {
        return this.needsTranslation;
    }

    public String getSourceName() {
        return this.sourceName;
    }

    public String getMessage() {
        return this.message;
    }

    public List<String> getParameters() {
        return this.parameters;
    }

    public String getXuid() {
        return this.xuid;
    }

    public String getPlatformChatId() {
        return this.platformChatId;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.TEXT;
    }
}
