package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.bedrock.annotation.Incompressible;
import org.cloudburstmc.protocol.bedrock.data.PacketCompressionAlgorithm;
import org.cloudburstmc.protocol.common.PacketSignal;

@Incompressible
/* loaded from: classes5.dex */
public class NetworkSettingsPacket implements BedrockPacket {
    private boolean clientThrottleEnabled;
    private float clientThrottleScalar;
    private int clientThrottleThreshold;
    private PacketCompressionAlgorithm compressionAlgorithm;
    private int compressionThreshold;

    public void setClientThrottleEnabled(boolean clientThrottleEnabled) {
        this.clientThrottleEnabled = clientThrottleEnabled;
    }

    public void setClientThrottleScalar(float clientThrottleScalar) {
        this.clientThrottleScalar = clientThrottleScalar;
    }

    public void setClientThrottleThreshold(int clientThrottleThreshold) {
        this.clientThrottleThreshold = clientThrottleThreshold;
    }

    public void setCompressionAlgorithm(PacketCompressionAlgorithm compressionAlgorithm) {
        this.compressionAlgorithm = compressionAlgorithm;
    }

    public void setCompressionThreshold(int compressionThreshold) {
        this.compressionThreshold = compressionThreshold;
    }

    protected boolean canEqual(Object other) {
        return other instanceof NetworkSettingsPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof NetworkSettingsPacket)) {
            return false;
        }
        NetworkSettingsPacket other = (NetworkSettingsPacket) o;
        if (!other.canEqual(this) || this.compressionThreshold != other.compressionThreshold || this.clientThrottleEnabled != other.clientThrottleEnabled || this.clientThrottleThreshold != other.clientThrottleThreshold || Float.compare(this.clientThrottleScalar, other.clientThrottleScalar) != 0) {
            return false;
        }
        Object this$compressionAlgorithm = this.compressionAlgorithm;
        Object other$compressionAlgorithm = other.compressionAlgorithm;
        return this$compressionAlgorithm != null ? this$compressionAlgorithm.equals(other$compressionAlgorithm) : other$compressionAlgorithm == null;
    }

    public int hashCode() {
        int result = (1 * 59) + this.compressionThreshold;
        int result2 = (((((result * 59) + (this.clientThrottleEnabled ? 79 : 97)) * 59) + this.clientThrottleThreshold) * 59) + Float.floatToIntBits(this.clientThrottleScalar);
        Object $compressionAlgorithm = this.compressionAlgorithm;
        return (result2 * 59) + ($compressionAlgorithm == null ? 43 : $compressionAlgorithm.hashCode());
    }

    public String toString() {
        return "NetworkSettingsPacket(compressionThreshold=" + this.compressionThreshold + ", compressionAlgorithm=" + this.compressionAlgorithm + ", clientThrottleEnabled=" + this.clientThrottleEnabled + ", clientThrottleThreshold=" + this.clientThrottleThreshold + ", clientThrottleScalar=" + this.clientThrottleScalar + ")";
    }

    public int getCompressionThreshold() {
        return this.compressionThreshold;
    }

    public PacketCompressionAlgorithm getCompressionAlgorithm() {
        return this.compressionAlgorithm;
    }

    public boolean isClientThrottleEnabled() {
        return this.clientThrottleEnabled;
    }

    public int getClientThrottleThreshold() {
        return this.clientThrottleThreshold;
    }

    public float getClientThrottleScalar() {
        return this.clientThrottleScalar;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.NETWORK_SETTINGS;
    }
}
