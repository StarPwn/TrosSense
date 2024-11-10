package org.cloudburstmc.protocol.bedrock.packet;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import org.cloudburstmc.protocol.bedrock.data.definitions.DimensionDefinition;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class DimensionDataPacket implements BedrockPacket {
    private final List<DimensionDefinition> definitions = new ObjectArrayList();

    protected boolean canEqual(Object other) {
        return other instanceof DimensionDataPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DimensionDataPacket)) {
            return false;
        }
        DimensionDataPacket other = (DimensionDataPacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$definitions = this.definitions;
        Object other$definitions = other.definitions;
        return this$definitions != null ? this$definitions.equals(other$definitions) : other$definitions == null;
    }

    public int hashCode() {
        Object $definitions = this.definitions;
        int result = (1 * 59) + ($definitions == null ? 43 : $definitions.hashCode());
        return result;
    }

    public String toString() {
        return "DimensionDataPacket(definitions=" + this.definitions + ")";
    }

    public List<DimensionDefinition> getDefinitions() {
        return this.definitions;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.DIMENSION_DATA;
    }
}
