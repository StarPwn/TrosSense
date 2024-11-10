package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class ContainerSetDataPacket implements BedrockPacket {
    public static final int BREWING_STAND_BREW_TIME = 0;
    public static final int BREWING_STAND_FUEL_AMOUNT = 1;
    public static final int BREWING_STAND_FUEL_TOTAL = 2;
    public static final int FURNACE_FUEL_AUX = 4;
    public static final int FURNACE_LIT_DURATION = 2;
    public static final int FURNACE_LIT_TIME = 1;
    public static final int FURNACE_STORED_XP = 3;
    public static final int FURNACE_TICK_COUNT = 0;
    private int property;
    private int value;
    private byte windowId;

    public void setProperty(int property) {
        this.property = property;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setWindowId(byte windowId) {
        this.windowId = windowId;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ContainerSetDataPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ContainerSetDataPacket)) {
            return false;
        }
        ContainerSetDataPacket other = (ContainerSetDataPacket) o;
        return other.canEqual(this) && this.windowId == other.windowId && this.property == other.property && this.value == other.value;
    }

    public int hashCode() {
        int result = (1 * 59) + this.windowId;
        return (((result * 59) + this.property) * 59) + this.value;
    }

    public String toString() {
        return "ContainerSetDataPacket(windowId=" + ((int) this.windowId) + ", property=" + this.property + ", value=" + this.value + ")";
    }

    public byte getWindowId() {
        return this.windowId;
    }

    public int getProperty() {
        return this.property;
    }

    public int getValue() {
        return this.value;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.CONTAINER_SET_DATA;
    }
}
