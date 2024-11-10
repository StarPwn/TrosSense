package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class ChangeMobPropertyPacket implements BedrockPacket {
    private boolean boolValue;
    private float floatValue;
    private int intValue;
    private String property;
    private String stringValue;
    private long uniqueEntityId;

    public void setBoolValue(boolean boolValue) {
        this.boolValue = boolValue;
    }

    public void setFloatValue(float floatValue) {
        this.floatValue = floatValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public void setUniqueEntityId(long uniqueEntityId) {
        this.uniqueEntityId = uniqueEntityId;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ChangeMobPropertyPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ChangeMobPropertyPacket)) {
            return false;
        }
        ChangeMobPropertyPacket other = (ChangeMobPropertyPacket) o;
        if (!other.canEqual(this) || this.uniqueEntityId != other.uniqueEntityId || this.boolValue != other.boolValue || this.intValue != other.intValue || Float.compare(this.floatValue, other.floatValue) != 0) {
            return false;
        }
        Object this$property = this.property;
        Object other$property = other.property;
        if (this$property != null ? !this$property.equals(other$property) : other$property != null) {
            return false;
        }
        Object this$stringValue = this.stringValue;
        Object other$stringValue = other.stringValue;
        return this$stringValue != null ? this$stringValue.equals(other$stringValue) : other$stringValue == null;
    }

    public int hashCode() {
        long $uniqueEntityId = this.uniqueEntityId;
        int result = (1 * 59) + ((int) (($uniqueEntityId >>> 32) ^ $uniqueEntityId));
        int result2 = (((((result * 59) + (this.boolValue ? 79 : 97)) * 59) + this.intValue) * 59) + Float.floatToIntBits(this.floatValue);
        Object $property = this.property;
        int result3 = (result2 * 59) + ($property == null ? 43 : $property.hashCode());
        Object $stringValue = this.stringValue;
        return (result3 * 59) + ($stringValue != null ? $stringValue.hashCode() : 43);
    }

    public String toString() {
        return "ChangeMobPropertyPacket(uniqueEntityId=" + this.uniqueEntityId + ", property=" + this.property + ", boolValue=" + this.boolValue + ", stringValue=" + this.stringValue + ", intValue=" + this.intValue + ", floatValue=" + this.floatValue + ")";
    }

    public long getUniqueEntityId() {
        return this.uniqueEntityId;
    }

    public String getProperty() {
        return this.property;
    }

    public boolean isBoolValue() {
        return this.boolValue;
    }

    public String getStringValue() {
        return this.stringValue;
    }

    public int getIntValue() {
        return this.intValue;
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
        return BedrockPacketType.CHANGE_MOB_PROPERTY;
    }
}
