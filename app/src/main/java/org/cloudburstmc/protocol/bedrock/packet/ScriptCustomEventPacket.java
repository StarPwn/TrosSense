package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

@Deprecated
/* loaded from: classes5.dex */
public class ScriptCustomEventPacket implements BedrockPacket {
    private String data;
    private String eventName;

    public void setData(String data) {
        this.data = data;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ScriptCustomEventPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ScriptCustomEventPacket)) {
            return false;
        }
        ScriptCustomEventPacket other = (ScriptCustomEventPacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$eventName = this.eventName;
        Object other$eventName = other.eventName;
        if (this$eventName != null ? !this$eventName.equals(other$eventName) : other$eventName != null) {
            return false;
        }
        Object this$data = this.data;
        Object other$data = other.data;
        return this$data != null ? this$data.equals(other$data) : other$data == null;
    }

    public int hashCode() {
        Object $eventName = this.eventName;
        int result = (1 * 59) + ($eventName == null ? 43 : $eventName.hashCode());
        Object $data = this.data;
        return (result * 59) + ($data != null ? $data.hashCode() : 43);
    }

    public String toString() {
        return "ScriptCustomEventPacket(eventName=" + this.eventName + ", data=" + this.data + ")";
    }

    public String getEventName() {
        return this.eventName;
    }

    public String getData() {
        return this.data;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.SCRIPT_CUSTOM_EVENT;
    }
}
