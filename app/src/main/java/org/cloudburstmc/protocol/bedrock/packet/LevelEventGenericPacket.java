package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.bedrock.data.LevelEventType;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class LevelEventGenericPacket implements BedrockPacket {
    private Object tag;
    private LevelEventType type;

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public void setType(LevelEventType type) {
        this.type = type;
    }

    protected boolean canEqual(Object other) {
        return other instanceof LevelEventGenericPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LevelEventGenericPacket)) {
            return false;
        }
        LevelEventGenericPacket other = (LevelEventGenericPacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$type = this.type;
        Object other$type = other.type;
        if (this$type != null ? !this$type.equals(other$type) : other$type != null) {
            return false;
        }
        Object this$tag = this.tag;
        Object other$tag = other.tag;
        return this$tag != null ? this$tag.equals(other$tag) : other$tag == null;
    }

    public int hashCode() {
        Object $type = this.type;
        int result = (1 * 59) + ($type == null ? 43 : $type.hashCode());
        Object $tag = this.tag;
        return (result * 59) + ($tag != null ? $tag.hashCode() : 43);
    }

    public String toString() {
        return "LevelEventGenericPacket(type=" + this.type + ", tag=" + this.tag + ")";
    }

    public LevelEventType getType() {
        return this.type;
    }

    public Object getTag() {
        return this.tag;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.LEVEL_EVENT_GENERIC;
    }
}
