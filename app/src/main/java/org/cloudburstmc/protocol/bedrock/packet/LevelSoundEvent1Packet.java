package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.bedrock.data.SoundEvent;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class LevelSoundEvent1Packet implements BedrockPacket {
    private boolean babySound;
    private int extraData;
    private int pitch;
    private Vector3f position;
    private boolean relativeVolumeDisabled;
    private SoundEvent sound;

    public void setBabySound(boolean babySound) {
        this.babySound = babySound;
    }

    public void setExtraData(int extraData) {
        this.extraData = extraData;
    }

    public void setPitch(int pitch) {
        this.pitch = pitch;
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
        return other instanceof LevelSoundEvent1Packet;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LevelSoundEvent1Packet)) {
            return false;
        }
        LevelSoundEvent1Packet other = (LevelSoundEvent1Packet) o;
        if (!other.canEqual(this) || this.extraData != other.extraData || this.pitch != other.pitch || this.babySound != other.babySound || this.relativeVolumeDisabled != other.relativeVolumeDisabled) {
            return false;
        }
        Object this$sound = this.sound;
        Object other$sound = other.sound;
        if (this$sound != null ? !this$sound.equals(other$sound) : other$sound != null) {
            return false;
        }
        Object this$position = this.position;
        Object other$position = other.position;
        return this$position != null ? this$position.equals(other$position) : other$position == null;
    }

    public int hashCode() {
        int result = (1 * 59) + this.extraData;
        int result2 = ((((result * 59) + this.pitch) * 59) + (this.babySound ? 79 : 97)) * 59;
        int i = this.relativeVolumeDisabled ? 79 : 97;
        Object $sound = this.sound;
        int result3 = ((result2 + i) * 59) + ($sound == null ? 43 : $sound.hashCode());
        Object $position = this.position;
        return (result3 * 59) + ($position != null ? $position.hashCode() : 43);
    }

    public String toString() {
        return "LevelSoundEvent1Packet(sound=" + this.sound + ", position=" + this.position + ", extraData=" + this.extraData + ", pitch=" + this.pitch + ", babySound=" + this.babySound + ", relativeVolumeDisabled=" + this.relativeVolumeDisabled + ")";
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

    public int getPitch() {
        return this.pitch;
    }

    public boolean isBabySound() {
        return this.babySound;
    }

    public boolean isRelativeVolumeDisabled() {
        return this.relativeVolumeDisabled;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.LEVEL_SOUND_EVENT_1;
    }
}
