package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.bedrock.data.CameraShakeAction;
import org.cloudburstmc.protocol.bedrock.data.CameraShakeType;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class CameraShakePacket implements BedrockPacket {
    private float duration;
    private float intensity;
    private CameraShakeAction shakeAction;
    private CameraShakeType shakeType;

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }

    public void setShakeAction(CameraShakeAction shakeAction) {
        this.shakeAction = shakeAction;
    }

    public void setShakeType(CameraShakeType shakeType) {
        this.shakeType = shakeType;
    }

    protected boolean canEqual(Object other) {
        return other instanceof CameraShakePacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CameraShakePacket)) {
            return false;
        }
        CameraShakePacket other = (CameraShakePacket) o;
        if (!other.canEqual(this) || Float.compare(this.intensity, other.intensity) != 0 || Float.compare(this.duration, other.duration) != 0) {
            return false;
        }
        Object this$shakeType = this.shakeType;
        Object other$shakeType = other.shakeType;
        if (this$shakeType != null ? !this$shakeType.equals(other$shakeType) : other$shakeType != null) {
            return false;
        }
        Object this$shakeAction = this.shakeAction;
        Object other$shakeAction = other.shakeAction;
        return this$shakeAction != null ? this$shakeAction.equals(other$shakeAction) : other$shakeAction == null;
    }

    public int hashCode() {
        int result = (1 * 59) + Float.floatToIntBits(this.intensity);
        int result2 = (result * 59) + Float.floatToIntBits(this.duration);
        Object $shakeType = this.shakeType;
        int result3 = (result2 * 59) + ($shakeType == null ? 43 : $shakeType.hashCode());
        Object $shakeAction = this.shakeAction;
        return (result3 * 59) + ($shakeAction != null ? $shakeAction.hashCode() : 43);
    }

    public String toString() {
        return "CameraShakePacket(intensity=" + this.intensity + ", duration=" + this.duration + ", shakeType=" + this.shakeType + ", shakeAction=" + this.shakeAction + ")";
    }

    public float getIntensity() {
        return this.intensity;
    }

    public float getDuration() {
        return this.duration;
    }

    public CameraShakeType getShakeType() {
        return this.shakeType;
    }

    public CameraShakeAction getShakeAction() {
        return this.shakeAction;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.CAMERA_SHAKE;
    }
}
