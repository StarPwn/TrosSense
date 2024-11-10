package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.bedrock.data.Ability;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class RequestAbilityPacket implements BedrockPacket {
    private Ability ability;
    private boolean boolValue;
    private float floatValue;
    private Ability.Type type;

    public void setAbility(Ability ability) {
        this.ability = ability;
    }

    public void setBoolValue(boolean boolValue) {
        this.boolValue = boolValue;
    }

    public void setFloatValue(float floatValue) {
        this.floatValue = floatValue;
    }

    public void setType(Ability.Type type) {
        this.type = type;
    }

    protected boolean canEqual(Object other) {
        return other instanceof RequestAbilityPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RequestAbilityPacket)) {
            return false;
        }
        RequestAbilityPacket other = (RequestAbilityPacket) o;
        if (!other.canEqual(this) || this.boolValue != other.boolValue || Float.compare(this.floatValue, other.floatValue) != 0) {
            return false;
        }
        Object this$ability = this.ability;
        Object other$ability = other.ability;
        if (this$ability != null ? !this$ability.equals(other$ability) : other$ability != null) {
            return false;
        }
        Object this$type = this.type;
        Object other$type = other.type;
        return this$type != null ? this$type.equals(other$type) : other$type == null;
    }

    public int hashCode() {
        int result = (1 * 59) + (this.boolValue ? 79 : 97);
        int result2 = (result * 59) + Float.floatToIntBits(this.floatValue);
        Object $ability = this.ability;
        int result3 = (result2 * 59) + ($ability == null ? 43 : $ability.hashCode());
        Object $type = this.type;
        return (result3 * 59) + ($type != null ? $type.hashCode() : 43);
    }

    public String toString() {
        return "RequestAbilityPacket(ability=" + this.ability + ", type=" + this.type + ", boolValue=" + this.boolValue + ", floatValue=" + this.floatValue + ")";
    }

    public Ability getAbility() {
        return this.ability;
    }

    public Ability.Type getType() {
        return this.type;
    }

    public boolean isBoolValue() {
        return this.boolValue;
    }

    public float getFloatValue() {
        return this.floatValue;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.REQUEST_ABILITY;
    }
}
