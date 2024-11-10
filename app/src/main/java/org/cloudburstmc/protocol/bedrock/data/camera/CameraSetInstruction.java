package org.cloudburstmc.protocol.bedrock.data.camera;

import org.cloudburstmc.math.vector.Vector2f;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.common.NamedDefinition;
import org.cloudburstmc.protocol.common.util.OptionalBoolean;

/* loaded from: classes5.dex */
public class CameraSetInstruction {
    private OptionalBoolean defaultPreset;
    private EaseData ease;
    private Vector3f facing;
    private Vector3f pos;
    private NamedDefinition preset;
    private Vector2f rot;

    protected boolean canEqual(Object other) {
        return other instanceof CameraSetInstruction;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CameraSetInstruction)) {
            return false;
        }
        CameraSetInstruction other = (CameraSetInstruction) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$preset = getPreset();
        Object other$preset = other.getPreset();
        if (this$preset != null ? !this$preset.equals(other$preset) : other$preset != null) {
            return false;
        }
        Object this$ease = getEase();
        Object other$ease = other.getEase();
        if (this$ease != null ? !this$ease.equals(other$ease) : other$ease != null) {
            return false;
        }
        Object this$pos = getPos();
        Object other$pos = other.getPos();
        if (this$pos != null ? !this$pos.equals(other$pos) : other$pos != null) {
            return false;
        }
        Object this$rot = getRot();
        Object other$rot = other.getRot();
        if (this$rot != null ? !this$rot.equals(other$rot) : other$rot != null) {
            return false;
        }
        Object this$facing = getFacing();
        Object other$facing = other.getFacing();
        if (this$facing != null ? !this$facing.equals(other$facing) : other$facing != null) {
            return false;
        }
        Object this$defaultPreset = getDefaultPreset();
        Object other$defaultPreset = other.getDefaultPreset();
        if (this$defaultPreset == null) {
            if (other$defaultPreset == null) {
                return true;
            }
        } else if (this$defaultPreset.equals(other$defaultPreset)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        Object $preset = getPreset();
        int result = (1 * 59) + ($preset == null ? 43 : $preset.hashCode());
        Object $ease = getEase();
        int result2 = (result * 59) + ($ease == null ? 43 : $ease.hashCode());
        Object $pos = getPos();
        int result3 = (result2 * 59) + ($pos == null ? 43 : $pos.hashCode());
        Object $rot = getRot();
        int result4 = (result3 * 59) + ($rot == null ? 43 : $rot.hashCode());
        Object $facing = getFacing();
        int result5 = (result4 * 59) + ($facing == null ? 43 : $facing.hashCode());
        Object $defaultPreset = getDefaultPreset();
        return (result5 * 59) + ($defaultPreset != null ? $defaultPreset.hashCode() : 43);
    }

    public void setDefaultPreset(OptionalBoolean defaultPreset) {
        this.defaultPreset = defaultPreset;
    }

    public void setEase(EaseData ease) {
        this.ease = ease;
    }

    public void setFacing(Vector3f facing) {
        this.facing = facing;
    }

    public void setPos(Vector3f pos) {
        this.pos = pos;
    }

    public void setPreset(NamedDefinition preset) {
        this.preset = preset;
    }

    public void setRot(Vector2f rot) {
        this.rot = rot;
    }

    public String toString() {
        return "CameraSetInstruction(preset=" + getPreset() + ", ease=" + getEase() + ", pos=" + getPos() + ", rot=" + getRot() + ", facing=" + getFacing() + ", defaultPreset=" + getDefaultPreset() + ")";
    }

    public CameraSetInstruction(NamedDefinition preset, EaseData ease, Vector3f pos, Vector2f rot, Vector3f facing, OptionalBoolean defaultPreset) {
        this.defaultPreset = OptionalBoolean.empty();
        this.preset = preset;
        this.ease = ease;
        this.pos = pos;
        this.rot = rot;
        this.facing = facing;
        this.defaultPreset = defaultPreset;
    }

    public CameraSetInstruction() {
        this.defaultPreset = OptionalBoolean.empty();
    }

    public NamedDefinition getPreset() {
        return this.preset;
    }

    public EaseData getEase() {
        return this.ease;
    }

    public Vector3f getPos() {
        return this.pos;
    }

    public Vector2f getRot() {
        return this.rot;
    }

    public Vector3f getFacing() {
        return this.facing;
    }

    public OptionalBoolean getDefaultPreset() {
        return this.defaultPreset;
    }

    /* loaded from: classes5.dex */
    public static class EaseData {
        private final CameraEase easeType;
        private final float time;

        public EaseData(CameraEase easeType, float time) {
            this.easeType = easeType;
            this.time = time;
        }

        protected boolean canEqual(Object other) {
            return other instanceof EaseData;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof EaseData)) {
                return false;
            }
            EaseData other = (EaseData) o;
            if (!other.canEqual(this) || Float.compare(getTime(), other.getTime()) != 0) {
                return false;
            }
            Object this$easeType = getEaseType();
            Object other$easeType = other.getEaseType();
            return this$easeType != null ? this$easeType.equals(other$easeType) : other$easeType == null;
        }

        public int hashCode() {
            int result = (1 * 59) + Float.floatToIntBits(getTime());
            Object $easeType = getEaseType();
            return (result * 59) + ($easeType == null ? 43 : $easeType.hashCode());
        }

        public String toString() {
            return "CameraSetInstruction.EaseData(easeType=" + getEaseType() + ", time=" + getTime() + ")";
        }

        public CameraEase getEaseType() {
            return this.easeType;
        }

        public float getTime() {
            return this.time;
        }
    }
}
