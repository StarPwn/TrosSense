package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class StopSoundPacket implements BedrockPacket {
    private String soundName;
    private boolean stoppingAllSound;

    public void setSoundName(String soundName) {
        this.soundName = soundName;
    }

    public void setStoppingAllSound(boolean stoppingAllSound) {
        this.stoppingAllSound = stoppingAllSound;
    }

    protected boolean canEqual(Object other) {
        return other instanceof StopSoundPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof StopSoundPacket)) {
            return false;
        }
        StopSoundPacket other = (StopSoundPacket) o;
        if (!other.canEqual(this) || this.stoppingAllSound != other.stoppingAllSound) {
            return false;
        }
        Object this$soundName = this.soundName;
        Object other$soundName = other.soundName;
        return this$soundName != null ? this$soundName.equals(other$soundName) : other$soundName == null;
    }

    public int hashCode() {
        int result = (1 * 59) + (this.stoppingAllSound ? 79 : 97);
        Object $soundName = this.soundName;
        return (result * 59) + ($soundName == null ? 43 : $soundName.hashCode());
    }

    public String toString() {
        return "StopSoundPacket(soundName=" + this.soundName + ", stoppingAllSound=" + this.stoppingAllSound + ")";
    }

    public String getSoundName() {
        return this.soundName;
    }

    public boolean isStoppingAllSound() {
        return this.stoppingAllSound;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.STOP_SOUND;
    }
}
