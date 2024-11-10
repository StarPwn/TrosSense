package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.bedrock.data.SoundEvent;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class LevelSoundEvent2Packet implements BedrockPacket {
    private boolean babySound;
    private int extraData;
    private String identifier;
    private Vector3f position;
    private boolean relativeVolumeDisabled;
    private SoundEvent sound;

    public void setBabySound(boolean babySound) {
        this.babySound = babySound;
    }

    public void setExtraData(int extraData) {
        this.extraData = extraData;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setRelativeVolumeDisabled(boolean relativeVolumeDisabled) {
        this.relativeVolumeDisabled = relativeVolumeDisabled;
    }

    public void setSound(SoundEvent sound) {
        this.sound = sound;
    }

    protected boolean canEqual(Object other) {
        return other instanceof LevelSoundEvent2Packet;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LevelSoundEvent2Packet)) {
            return false;
        }
        LevelSoundEvent2Packet other = (LevelSoundEvent2Packet) o;
        if (!other.canEqual(this) || this.extraData != other.extraData || this.babySound != other.babySound || this.relativeVolumeDisabled != other.relativeVolumeDisabled) {
            return false;
        }
        Object this$sound = this.sound;
        Object other$sound = other.sound;
        if (this$sound != null ? !this$sound.equals(other$sound) : other$sound != null) {
            return false;
        }
        Object this$position = this.position;
        Object other$position = other.position;
        if (this$position != null ? !this$position.equals(other$position) : other$position != null) {
            return false;
        }
        Object this$identifier = this.identifier;
        Object other$identifier = other.identifier;
        return this$identifier != null ? this$identifier.equals(other$identifier) : other$identifier == null;
    }

    public int hashCode() {
        int result = (1 * 59) + this.extraData;
        int result2 = ((result * 59) + (this.babySound ? 79 : 97)) * 59;
        int i = this.relativeVolumeDisabled ? 79 : 97;
        Object $sound = this.sound;
        int result3 = ((result2 + i) * 59) + ($sound == null ? 43 : $sound.hashCode());
        Object $position = this.position;
        int result4 = (result3 * 59) + ($position == null ? 43 : $position.hashCode());
        Object $identifier = this.identifier;
        return (result4 * 59) + ($identifier != null ? $identifier.hashCode() : 43);
    }

    public String toString() {
        return "LevelSoundEvent2Packet(sound=" + this.sound + ", position=" + this.position + ", extraData=" + this.extraData + ", identifier=" + this.identifier + ", babySound=" + this.babySound + ", relativeVolumeDisabled=" + this.relativeVolumeDisabled + ")";
    }

    public SoundEvent getSound() {
        return this.sound;
    }

    public Vector3f getPosition() {
        return this.position;
    }

    public int getExtraData() {
        return this.extraData;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public boolean isBabySound() {
        return this.babySound;
    }

    public boolean isRelativeVolumeDisabled() {
        return this.relativeVolumeDisabled;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.LEVEL_SOUND_EVENT_2;
    }
}
