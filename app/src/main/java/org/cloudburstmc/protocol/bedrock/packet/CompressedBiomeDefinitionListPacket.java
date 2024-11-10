package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class CompressedBiomeDefinitionListPacket implements BedrockPacket {
    private NbtMap definitions;

    public void setDefinitions(NbtMap definitions) {
        this.definitions = definitions;
    }

    protected boolean canEqual(Object other) {
        return other instanceof CompressedBiomeDefinitionListPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CompressedBiomeDefinitionListPacket)) {
            return false;
        }
        CompressedBiomeDefinitionListPacket other = (CompressedBiomeDefinitionListPacket) o;
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
        return "CompressedBiomeDefinitionListPacket()";
    }

    public NbtMap getDefinitions() {
        return this.definitions;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.COMPRESSED_BIOME_DEFINITIONS_LIST;
    }
}
