package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.bedrock.data.event.EventData;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class EventPacket implements BedrockPacket {
    private EventData eventData;
    private long uniqueEntityId;
    private byte usePlayerId;

    /* loaded from: classes5.dex */
    public enum Event {
        ACHIEVEMENT_AWARDED,
        ENTITY_INTERACT,
        PORTAL_BUILT,
        PORTAL_USED,
        MOB_KILLED,
        CAULDRON_USED,
        PLAYER_DEATH,
        BOSS_KILLED,
        AGENT_COMMAND,
        AGENT_CREATED,
        PATTERN_REMOVED,
        SLASH_COMMAND_EXECUTED,
        FISH_BUCKETED,
        MOB_BORN,
        PET_DIED,
        CAULDRON_BLOCK_USED,
        COMPOSTER_BLOCK_USED,
        BELL_BLOCK_USED
    }

    public void setEventData(EventData eventData) {
        this.eventData = eventData;
    }

    public void setUniqueEntityId(long uniqueEntityId) {
        this.uniqueEntityId = uniqueEntityId;
    }

    public void setUsePlayerId(byte usePlayerId) {
        this.usePlayerId = usePlayerId;
    }

    protected boolean canEqual(Object other) {
        return other instanceof EventPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof EventPacket)) {
            return false;
        }
        EventPacket other = (EventPacket) o;
        if (!other.canEqual(this) || this.uniqueEntityId != other.uniqueEntityId || this.usePlayerId != other.usePlayerId) {
            return false;
        }
        Object this$eventData = this.eventData;
        Object other$eventData = other.eventData;
        return this$eventData != null ? this$eventData.equals(other$eventData) : other$eventData == null;
    }

    public int hashCode() {
        long $uniqueEntityId = this.uniqueEntityId;
        int result = (1 * 59) + ((int) (($uniqueEntityId >>> 32) ^ $uniqueEntityId));
        int result2 = (result * 59) + this.usePlayerId;
        Object $eventData = this.eventData;
        return (result2 * 59) + ($eventData == null ? 43 : $eventData.hashCode());
    }

    public String toString() {
        return "EventPacket(uniqueEntityId=" + this.uniqueEntityId + ", usePlayerId=" + ((int) this.usePlayerId) + ", eventData=" + this.eventData + ")";
    }

    public long getUniqueEntityId() {
        return this.uniqueEntityId;
    }

    public byte getUsePlayerId() {
        return this.usePlayerId;
    }

    public EventData getEventData() {
        return this.eventData;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.EVENT;
    }
}
