package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.bedrock.data.LevelEventType;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class LevelEventPacket implements BedrockPacket {
    private int data;
    private Vector3f position;
    private LevelEventType type;

    public void setData(int data) {
        this.data = data;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setType(LevelEventType type) {
        this.type = type;
    }

    protected boolean canEqual(Object other) {
        return other instanceof LevelEventPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LevelEventPacket)) {
            return false;
        }
        LevelEventPacket other = (LevelEventPacket) o;
        if (!other.canEqual(this) || this.data != other.data) {
            return false;
        }
        Object this$type = this.type;
        Object other$type = other.type;
        if (this$type != null ? !this$type.equals(other$type) : other$type != null) {
            return false;
        }
        Object this$position = this.position;
        Object other$position = other.position;
        return this$position != null ? this$position.equals(other$position) : other$position == null;
    }

    public int hashCode() {
        int result = (1 * 59) + this.data;
        Object $type = this.type;
        int result2 = (result * 59) + ($type == null ? 43 : $type.hashCode());
        Object $position = this.position;
        return (result2 * 59) + ($position != null ? $position.hashCode() : 43);
    }

    public String toString() {
        return "LevelEventPacket(type=" + this.type + ", position=" + this.position + ", data=" + this.data + ")";
    }

    public LevelEventType getType() {
        return this.type;
    }

    public Vector3f getPosition() {
        return this.position;
    }

    public int getData() {
        return this.data;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.LEVEL_EVENT;
    }
}
