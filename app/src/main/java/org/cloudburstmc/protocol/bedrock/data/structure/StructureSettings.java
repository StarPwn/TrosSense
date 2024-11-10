package org.cloudburstmc.protocol.bedrock.data.structure;

import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector3i;

/* loaded from: classes5.dex */
public final class StructureSettings {
    private final StructureAnimationMode animationMode;
    private final float animationSeconds;
    private final boolean ignoringBlocks;
    private final boolean ignoringEntities;
    private final int integritySeed;
    private final float integrityValue;
    private final long lastEditedByEntityId;
    private final StructureMirror mirror;
    private final boolean nonTickingPlayersAndTickingAreasEnabled;
    private final Vector3i offset;
    private final String paletteName;
    private final Vector3f pivot;
    private final StructureRotation rotation;
    private final Vector3i size;

    public StructureSettings(String paletteName, boolean ignoringEntities, boolean ignoringBlocks, boolean nonTickingPlayersAndTickingAreasEnabled, Vector3i size, Vector3i offset, long lastEditedByEntityId, StructureRotation rotation, StructureMirror mirror, StructureAnimationMode animationMode, float animationSeconds, float integrityValue, int integritySeed, Vector3f pivot) {
        this.paletteName = paletteName;
        this.ignoringEntities = ignoringEntities;
        this.ignoringBlocks = ignoringBlocks;
        this.nonTickingPlayersAndTickingAreasEnabled = nonTickingPlayersAndTickingAreasEnabled;
        this.size = size;
        this.offset = offset;
        this.lastEditedByEntityId = lastEditedByEntityId;
        this.rotation = rotation;
        this.mirror = mirror;
        this.animationMode = animationMode;
        this.animationSeconds = animationSeconds;
        this.integrityValue = integrityValue;
        this.integritySeed = integritySeed;
        this.pivot = pivot;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof StructureSettings)) {
            return false;
        }
        StructureSettings other = (StructureSettings) o;
        if (isIgnoringEntities() != other.isIgnoringEntities() || isIgnoringBlocks() != other.isIgnoringBlocks() || isNonTickingPlayersAndTickingAreasEnabled() != other.isNonTickingPlayersAndTickingAreasEnabled() || getLastEditedByEntityId() != other.getLastEditedByEntityId() || Float.compare(getAnimationSeconds(), other.getAnimationSeconds()) != 0 || Float.compare(getIntegrityValue(), other.getIntegrityValue()) != 0 || getIntegritySeed() != other.getIntegritySeed()) {
            return false;
        }
        Object this$paletteName = getPaletteName();
        Object other$paletteName = other.getPaletteName();
        if (this$paletteName != null ? !this$paletteName.equals(other$paletteName) : other$paletteName != null) {
            return false;
        }
        Object this$size = getSize();
        Object other$size = other.getSize();
        if (this$size != null ? !this$size.equals(other$size) : other$size != null) {
            return false;
        }
        Object this$offset = getOffset();
        Object other$offset = other.getOffset();
        if (this$offset != null ? !this$offset.equals(other$offset) : other$offset != null) {
            return false;
        }
        Object this$rotation = getRotation();
        Object other$rotation = other.getRotation();
        if (this$rotation != null ? !this$rotation.equals(other$rotation) : other$rotation != null) {
            return false;
        }
        Object this$mirror = getMirror();
        Object other$mirror = other.getMirror();
        if (this$mirror != null ? !this$mirror.equals(other$mirror) : other$mirror != null) {
            return false;
        }
        Object this$animationMode = getAnimationMode();
        Object other$animationMode = other.getAnimationMode();
        if (this$animationMode != null ? !this$animationMode.equals(other$animationMode) : other$animationMode != null) {
            return false;
        }
        Object this$pivot = getPivot();
        Object other$pivot = other.getPivot();
        return this$pivot == null ? other$pivot == null : this$pivot.equals(other$pivot);
    }

    public int hashCode() {
        int result = (1 * 59) + (isIgnoringEntities() ? 79 : 97);
        int result2 = ((result * 59) + (isIgnoringBlocks() ? 79 : 97)) * 59;
        int i = isNonTickingPlayersAndTickingAreasEnabled() ? 79 : 97;
        long $lastEditedByEntityId = getLastEditedByEntityId();
        int result3 = ((((((((result2 + i) * 59) + ((int) (($lastEditedByEntityId >>> 32) ^ $lastEditedByEntityId))) * 59) + Float.floatToIntBits(getAnimationSeconds())) * 59) + Float.floatToIntBits(getIntegrityValue())) * 59) + getIntegritySeed();
        Object $paletteName = getPaletteName();
        int result4 = (result3 * 59) + ($paletteName == null ? 43 : $paletteName.hashCode());
        Object $size = getSize();
        int result5 = (result4 * 59) + ($size == null ? 43 : $size.hashCode());
        Object $offset = getOffset();
        int result6 = (result5 * 59) + ($offset == null ? 43 : $offset.hashCode());
        Object $rotation = getRotation();
        int result7 = (result6 * 59) + ($rotation == null ? 43 : $rotation.hashCode());
        Object $mirror = getMirror();
        int result8 = (result7 * 59) + ($mirror == null ? 43 : $mirror.hashCode());
        Object $animationMode = getAnimationMode();
        int result9 = (result8 * 59) + ($animationMode == null ? 43 : $animationMode.hashCode());
        Object $pivot = getPivot();
        return (result9 * 59) + ($pivot != null ? $pivot.hashCode() : 43);
    }

    public String toString() {
        return "StructureSettings(paletteName=" + getPaletteName() + ", ignoringEntities=" + isIgnoringEntities() + ", ignoringBlocks=" + isIgnoringBlocks() + ", nonTickingPlayersAndTickingAreasEnabled=" + isNonTickingPlayersAndTickingAreasEnabled() + ", size=" + getSize() + ", offset=" + getOffset() + ", lastEditedByEntityId=" + getLastEditedByEntityId() + ", rotation=" + getRotation() + ", mirror=" + getMirror() + ", animationMode=" + getAnimationMode() + ", animationSeconds=" + getAnimationSeconds() + ", integrityValue=" + getIntegrityValue() + ", integritySeed=" + getIntegritySeed() + ", pivot=" + getPivot() + ")";
    }

    public String getPaletteName() {
        return this.paletteName;
    }

    public boolean isIgnoringEntities() {
        return this.ignoringEntities;
    }

    public boolean isIgnoringBlocks() {
        return this.ignoringBlocks;
    }

    public boolean isNonTickingPlayersAndTickingAreasEnabled() {
        return this.nonTickingPlayersAndTickingAreasEnabled;
    }

    public Vector3i getSize() {
        return this.size;
    }

    public Vector3i getOffset() {
        return this.offset;
    }

    public long getLastEditedByEntityId() {
        return this.lastEditedByEntityId;
    }

    public StructureRotation getRotation() {
        return this.rotation;
    }

    public StructureMirror getMirror() {
        return this.mirror;
    }

    public StructureAnimationMode getAnimationMode() {
        return this.animationMode;
    }

    public float getAnimationSeconds() {
        return this.animationSeconds;
    }

    public float getIntegrityValue() {
        return this.integrityValue;
    }

    public int getIntegritySeed() {
        return this.integritySeed;
    }

    public Vector3f getPivot() {
        return this.pivot;
    }
}
