package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class MobEffectPacket implements BedrockPacket {
    private int amplifier;
    private int duration;
    private int effectId;
    private Event event;
    private boolean particles;
    private long runtimeEntityId;
    private long tick;

    /* loaded from: classes5.dex */
    public enum Event {
        NONE,
        ADD,
        MODIFY,
        REMOVE
    }

    public void setAmplifier(int amplifier) {
        this.amplifier = amplifier;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setEffectId(int effectId) {
        this.effectId = effectId;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setParticles(boolean particles) {
        this.particles = particles;
    }

    public void setRuntimeEntityId(long runtimeEntityId) {
        this.runtimeEntityId = runtimeEntityId;
    }

    public void setTick(long tick) {
        this.tick = tick;
    }

    protected boolean canEqual(Object other) {
        return other instanceof MobEffectPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MobEffectPacket)) {
            return false;
        }
        MobEffectPacket other = (MobEffectPacket) o;
        if (!other.canEqual(this) || this.runtimeEntityId != other.runtimeEntityId || this.effectId != other.effectId || this.amplifier != other.amplifier || this.particles != other.particles || this.duration != other.duration || this.tick != other.tick) {
            return false;
        }
        Object this$event = this.event;
        Object other$event = other.event;
        return this$event != null ? this$event.equals(other$event) : other$event == null;
    }

    public int hashCode() {
        long $runtimeEntityId = this.runtimeEntityId;
        int result = (1 * 59) + ((int) (($runtimeEntityId >>> 32) ^ $runtimeEntityId));
        int result2 = (((((((result * 59) + this.effectId) * 59) + this.amplifier) * 59) + (this.particles ? 79 : 97)) * 59) + this.duration;
        long $tick = this.tick;
        int result3 = (result2 * 59) + ((int) (($tick >>> 32) ^ $tick));
        Object $event = this.event;
        return (result3 * 59) + ($event == null ? 43 : $event.hashCode());
    }

    public String toString() {
        return "MobEffectPacket(runtimeEntityId=" + this.runtimeEntityId + ", event=" + this.event + ", effectId=" + this.effectId + ", amplifier=" + this.amplifier + ", particles=" + this.particles + ", duration=" + this.duration + ", tick=" + this.tick + ")";
    }

    public long getRuntimeEntityId() {
        return this.runtimeEntityId;
    }

    public Event getEvent() {
        return this.event;
    }

    public int getEffectId() {
        return this.effectId;
    }

    public int getAmplifier() {
        return this.amplifier;
    }

    public boolean isParticles() {
        return this.particles;
    }

    public int getDuration() {
        return this.duration;
    }

    public long getTick() {
        return this.tick;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.MOB_EFFECT;
    }
}
