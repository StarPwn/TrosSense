package org.cloudburstmc.protocol.bedrock.packet;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import org.cloudburstmc.protocol.bedrock.data.definitions.FeatureDefinition;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class FeatureRegistryPacket implements BedrockPacket {
    private final List<FeatureDefinition> features = new ObjectArrayList();

    protected boolean canEqual(Object other) {
        return other instanceof FeatureRegistryPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FeatureRegistryPacket)) {
            return false;
        }
        FeatureRegistryPacket other = (FeatureRegistryPacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$features = this.features;
        Object other$features = other.features;
        return this$features != null ? this$features.equals(other$features) : other$features == null;
    }

    public int hashCode() {
        Object $features = this.features;
        int result = (1 * 59) + ($features == null ? 43 : $features.hashCode());
        return result;
    }

    public String toString() {
        return "FeatureRegistryPacket(features=" + this.features + ")";
    }

    public List<FeatureDefinition> getFeatures() {
        return this.features;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.FEATURE_REGISTRY;
    }
}
