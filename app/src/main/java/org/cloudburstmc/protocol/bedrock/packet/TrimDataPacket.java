package org.cloudburstmc.protocol.bedrock.packet;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import org.cloudburstmc.protocol.bedrock.data.TrimMaterial;
import org.cloudburstmc.protocol.bedrock.data.TrimPattern;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class TrimDataPacket implements BedrockPacket {
    private final List<TrimPattern> patterns = new ObjectArrayList();
    private final List<TrimMaterial> materials = new ObjectArrayList();

    protected boolean canEqual(Object other) {
        return other instanceof TrimDataPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TrimDataPacket)) {
            return false;
        }
        TrimDataPacket other = (TrimDataPacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$patterns = this.patterns;
        Object other$patterns = other.patterns;
        if (this$patterns != null ? !this$patterns.equals(other$patterns) : other$patterns != null) {
            return false;
        }
        Object this$materials = this.materials;
        Object other$materials = other.materials;
        return this$materials != null ? this$materials.equals(other$materials) : other$materials == null;
    }

    public int hashCode() {
        Object $patterns = this.patterns;
        int result = (1 * 59) + ($patterns == null ? 43 : $patterns.hashCode());
        Object $materials = this.materials;
        return (result * 59) + ($materials != null ? $materials.hashCode() : 43);
    }

    public String toString() {
        return "TrimDataPacket(patterns=" + this.patterns + ", materials=" + this.materials + ")";
    }

    public List<TrimPattern> getPatterns() {
        return this.patterns;
    }

    public List<TrimMaterial> getMaterials() {
        return this.materials;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.TRIM_DATA;
    }
}
