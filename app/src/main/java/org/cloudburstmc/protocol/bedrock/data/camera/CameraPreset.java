package org.cloudburstmc.protocol.bedrock.data.camera;

import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.common.util.OptionalBoolean;

/* loaded from: classes5.dex */
public class CameraPreset {
    private String identifier;
    private CameraAudioListener listener;
    private String parentPreset;
    private Float pitch;
    private OptionalBoolean playEffect;
    private Vector3f pos;
    private Float yaw;

    protected boolean canEqual(Object other) {
        return other instanceof CameraPreset;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CameraPreset)) {
            return false;
        }
        CameraPreset other = (CameraPreset) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$yaw = getYaw();
        Object other$yaw = other.getYaw();
        if (this$yaw != null ? !this$yaw.equals(other$yaw) : other$yaw != null) {
            return false;
        }
        Object this$pitch = getPitch();
        Object other$pitch = other.getPitch();
        if (this$pitch != null ? !this$pitch.equals(other$pitch) : other$pitch != null) {
            return false;
        }
        Object this$identifier = getIdentifier();
        Object other$identifier = other.getIdentifier();
        if (this$identifier != null ? !this$identifier.equals(other$identifier) : other$identifier != null) {
            return false;
        }
        Object this$parentPreset = getParentPreset();
        Object other$parentPreset = other.getParentPreset();
        if (this$parentPreset != null ? !this$parentPreset.equals(other$parentPreset) : other$parentPreset != null) {
            return false;
        }
        Object this$pos = getPos();
        Object other$pos = other.getPos();
        if (this$pos != null ? !this$pos.equals(other$pos) : other$pos != null) {
            return false;
        }
        Object this$listener = getListener();
        Object other$listener = other.getListener();
        if (this$listener != null ? !this$listener.equals(other$listener) : other$listener != null) {
            return false;
        }
        Object this$playEffect = getPlayEffect();
        Object other$playEffect = other.getPlayEffect();
        return this$playEffect == null ? other$playEffect == null : this$playEffect.equals(other$playEffect);
    }

    public int hashCode() {
        Object $yaw = getYaw();
        int result = (1 * 59) + ($yaw == null ? 43 : $yaw.hashCode());
        Object $pitch = getPitch();
        int result2 = (result * 59) + ($pitch == null ? 43 : $pitch.hashCode());
        Object $identifier = getIdentifier();
        int result3 = (result2 * 59) + ($identifier == null ? 43 : $identifier.hashCode());
        Object $parentPreset = getParentPreset();
        int result4 = (result3 * 59) + ($parentPreset == null ? 43 : $parentPreset.hashCode());
        Object $pos = getPos();
        int result5 = (result4 * 59) + ($pos == null ? 43 : $pos.hashCode());
        Object $listener = getListener();
        int result6 = (result5 * 59) + ($listener == null ? 43 : $listener.hashCode());
        Object $playEffect = getPlayEffect();
        return (result6 * 59) + ($playEffect != null ? $playEffect.hashCode() : 43);
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setListener(CameraAudioListener listener) {
        this.listener = listener;
    }

    public void setParentPreset(String parentPreset) {
        this.parentPreset = parentPreset;
    }

    public void setPitch(Float pitch) {
        this.pitch = pitch;
    }

    public void setPlayEffect(OptionalBoolean playEffect) {
        this.playEffect = playEffect;
    }

    public void setPos(Vector3f pos) {
        this.pos = pos;
    }

    public void setYaw(Float yaw) {
        this.yaw = yaw;
    }

    public String toString() {
        return "CameraPreset(identifier=" + getIdentifier() + ", parentPreset=" + getParentPreset() + ", pos=" + getPos() + ", yaw=" + getYaw() + ", pitch=" + getPitch() + ", listener=" + getListener() + ", playEffect=" + getPlayEffect() + ")";
    }

    public CameraPreset(String identifier, String parentPreset, Vector3f pos, Float yaw, Float pitch, CameraAudioListener listener, OptionalBoolean playEffect) {
        this.parentPreset = "";
        this.identifier = identifier;
        this.parentPreset = parentPreset;
        this.pos = pos;
        this.yaw = yaw;
        this.pitch = pitch;
        this.listener = listener;
        this.playEffect = playEffect;
    }

    public CameraPreset() {
        this.parentPreset = "";
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public String getParentPreset() {
        return this.parentPreset;
    }

    public Vector3f getPos() {
        return this.pos;
    }

    public Float getYaw() {
        return this.yaw;
    }

    public Float getPitch() {
        return this.pitch;
    }

    public CameraAudioListener getListener() {
        return this.listener;
    }

    public OptionalBoolean getPlayEffect() {
        return this.playEffect;
    }
}
