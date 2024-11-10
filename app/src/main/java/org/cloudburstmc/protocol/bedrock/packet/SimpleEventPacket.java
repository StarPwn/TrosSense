package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.bedrock.data.SimpleEventType;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class SimpleEventPacket implements BedrockPacket {
    private SimpleEventType event;

    public void setEvent(SimpleEventType event) {
        this.event = event;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SimpleEventPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SimpleEventPacket)) {
            return false;
        }
        SimpleEventPacket other = (SimpleEventPacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$event = this.event;
        Object other$event = other.event;
        return this$event != null ? this$event.equals(other$event) : other$event == null;
    }

    public int hashCode() {
        Object $event = this.event;
        int result = (1 * 59) + ($event == null ? 43 : $event.hashCode());
        return result;
    }

    public String toString() {
        return "SimpleEventPacket(event=" + this.event + ")";
    }

    public SimpleEventType getEvent() {
        return this.event;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.SIMPLE_EVENT;
    }
}
