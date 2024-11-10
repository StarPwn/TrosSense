package org.cloudburstmc.protocol.bedrock.packet;

import java.util.Arrays;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class CreativeContentPacket implements BedrockPacket {
    private ItemData[] contents;

    public void setContents(ItemData[] contents) {
        this.contents = contents;
    }

    protected boolean canEqual(Object other) {
        return other instanceof CreativeContentPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CreativeContentPacket)) {
            return false;
        }
        CreativeContentPacket other = (CreativeContentPacket) o;
        return other.canEqual(this) && Arrays.deepEquals(this.contents, other.contents);
    }

    public int hashCode() {
        int result = (1 * 59) + Arrays.deepHashCode(this.contents);
        return result;
    }

    public String toString() {
        return "CreativeContentPacket(contents=" + Arrays.deepToString(this.contents) + ")";
    }

    public ItemData[] getContents() {
        return this.contents;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.CREATIVE_CONTENT;
    }
}
