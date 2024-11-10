package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class PlaySoundPacket implements BedrockPacket {
    private float pitch;
    private Vector3f position;
    private String sound;
    private float volume;

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    protected boolean canEqual(Object other) {
        return other instanceof PlaySoundPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PlaySoundPacket)) {
            return false;
        }
        PlaySoundPacket other = (PlaySoundPacket) o;
        if (!other.canEqual(this) || Float.compare(this.volume, other.volume) != 0 || Float.compare(this.pitch, other.pitch) != 0) {
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
        int result = (1 * 59) + Float.floatToIntBits(this.volume);
        int result2 = (result * 59) + Float.floatToIntBits(this.pitch);
        Object $sound = this.sound;
        int result3 = (result2 * 59) + ($sound == null ? 43 : $sound.hashCode());
        Object $position = this.position;
        return (result3 * 59) + ($position != null ? $position.hashCode() : 43);
    }

    public String toString() {
        return "PlaySoundPacket(sound=" + this.sound + ", position=" + this.position + ", volume=" + this.volume + ", pitch=" + this.pitch + ")";
    }

    public String getSound() {
        return this.sound;
    }

    public Vector3f getPosition() {
        return this.position;
    }

    public float getVolume() {
        return this.volume;
    }

    public float getPitch() {
        return this.pitch;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.PLAY_SOUND;
    }
}
