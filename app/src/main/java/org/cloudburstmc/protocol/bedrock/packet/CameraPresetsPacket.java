package org.cloudburstmc.protocol.bedrock.packet;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import org.cloudburstmc.protocol.bedrock.data.camera.CameraPreset;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class CameraPresetsPacket implements BedrockPacket {
    private final List<CameraPreset> presets = new ObjectArrayList();

    protected boolean canEqual(Object other) {
        return other instanceof CameraPresetsPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CameraPresetsPacket)) {
            return false;
        }
        CameraPresetsPacket other = (CameraPresetsPacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$presets = this.presets;
        Object other$presets = other.presets;
        return this$presets != null ? this$presets.equals(other$presets) : other$presets == null;
    }

    public int hashCode() {
        Object $presets = this.presets;
        int result = (1 * 59) + ($presets == null ? 43 : $presets.hashCode());
        return result;
    }

    public String toString() {
        return "CameraPresetsPacket(presets=" + this.presets + ")";
    }

    public List<CameraPreset> getPresets() {
        return this.presets;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.CAMERA_PRESETS;
    }
}
