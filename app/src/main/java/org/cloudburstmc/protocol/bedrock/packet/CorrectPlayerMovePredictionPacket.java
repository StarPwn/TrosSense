package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.bedrock.data.PredictionType;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class CorrectPlayerMovePredictionPacket implements BedrockPacket {
    private Vector3f delta;
    private boolean onGround;
    private Vector3f position;
    private PredictionType predictionType = PredictionType.PLAYER;
    private long tick;

    public void setDelta(Vector3f delta) {
        this.delta = delta;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setPredictionType(PredictionType predictionType) {
        this.predictionType = predictionType;
    }

    public void setTick(long tick) {
        this.tick = tick;
    }

    protected boolean canEqual(Object other) {
        return other instanceof CorrectPlayerMovePredictionPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CorrectPlayerMovePredictionPacket)) {
            return false;
        }
        CorrectPlayerMovePredictionPacket other = (CorrectPlayerMovePredictionPacket) o;
        if (!other.canEqual(this) || this.onGround != other.onGround || this.tick != other.tick) {
            return false;
        }
        Object this$position = this.position;
        Object other$position = other.position;
        if (this$position != null ? !this$position.equals(other$position) : other$position != null) {
            return false;
        }
        Object this$delta = this.delta;
        Object other$delta = other.delta;
        if (this$delta != null ? !this$delta.equals(other$delta) : other$delta != null) {
            return false;
        }
        Object this$predictionType = this.predictionType;
        Object other$predictionType = other.predictionType;
        return this$predictionType != null ? this$predictionType.equals(other$predictionType) : other$predictionType == null;
    }

    public int hashCode() {
        int result = (1 * 59) + (this.onGround ? 79 : 97);
        long $tick = this.tick;
        int result2 = (result * 59) + ((int) (($tick >>> 32) ^ $tick));
        Object $position = this.position;
        int result3 = (result2 * 59) + ($position == null ? 43 : $position.hashCode());
        Object $delta = this.delta;
        int result4 = (result3 * 59) + ($delta == null ? 43 : $delta.hashCode());
        Object $predictionType = this.predictionType;
        return (result4 * 59) + ($predictionType != null ? $predictionType.hashCode() : 43);
    }

    public String toString() {
        return "CorrectPlayerMovePredictionPacket(position=" + this.position + ", delta=" + this.delta + ", onGround=" + this.onGround + ", tick=" + this.tick + ", predictionType=" + this.predictionType + ")";
    }

    public Vector3f getPosition() {
        return this.position;
    }

    public Vector3f getDelta() {
        return this.delta;
    }

    public boolean isOnGround() {
        return this.onGround;
    }

    public long getTick() {
        return this.tick;
    }

    public PredictionType getPredictionType() {
        return this.predictionType;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.CORRECT_PLAYER_MOVE_PREDICTION;
    }
}
