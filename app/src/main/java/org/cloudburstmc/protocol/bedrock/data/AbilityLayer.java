package org.cloudburstmc.protocol.bedrock.data;

import java.util.EnumSet;
import java.util.Set;

/* loaded from: classes5.dex */
public class AbilityLayer {
    private final Set<Ability> abilitiesSet = EnumSet.noneOf(Ability.class);
    private final Set<Ability> abilityValues = EnumSet.noneOf(Ability.class);
    private float flySpeed;
    private Type layerType;
    private float walkSpeed;

    /* loaded from: classes5.dex */
    public enum Type {
        CACHE,
        BASE,
        SPECTATOR,
        COMMANDS,
        EDITOR
    }

    protected boolean canEqual(Object other) {
        return other instanceof AbilityLayer;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AbilityLayer)) {
            return false;
        }
        AbilityLayer other = (AbilityLayer) o;
        if (!other.canEqual(this) || Float.compare(getFlySpeed(), other.getFlySpeed()) != 0 || Float.compare(getWalkSpeed(), other.getWalkSpeed()) != 0) {
            return false;
        }
        Object this$layerType = getLayerType();
        Object other$layerType = other.getLayerType();
        if (this$layerType != null ? !this$layerType.equals(other$layerType) : other$layerType != null) {
            return false;
        }
        Object this$abilitiesSet = getAbilitiesSet();
        Object other$abilitiesSet = other.getAbilitiesSet();
        if (this$abilitiesSet != null ? !this$abilitiesSet.equals(other$abilitiesSet) : other$abilitiesSet != null) {
            return false;
        }
        Object this$abilityValues = getAbilityValues();
        Object other$abilityValues = other.getAbilityValues();
        return this$abilityValues != null ? this$abilityValues.equals(other$abilityValues) : other$abilityValues == null;
    }

    public int hashCode() {
        int result = (1 * 59) + Float.floatToIntBits(getFlySpeed());
        int result2 = (result * 59) + Float.floatToIntBits(getWalkSpeed());
        Object $layerType = getLayerType();
        int result3 = (result2 * 59) + ($layerType == null ? 43 : $layerType.hashCode());
        Object $abilitiesSet = getAbilitiesSet();
        int result4 = (result3 * 59) + ($abilitiesSet == null ? 43 : $abilitiesSet.hashCode());
        Object $abilityValues = getAbilityValues();
        return (result4 * 59) + ($abilityValues != null ? $abilityValues.hashCode() : 43);
    }

    public void setFlySpeed(float flySpeed) {
        this.flySpeed = flySpeed;
    }

    public void setLayerType(Type layerType) {
        this.layerType = layerType;
    }

    public void setWalkSpeed(float walkSpeed) {
        this.walkSpeed = walkSpeed;
    }

    public String toString() {
        return "AbilityLayer(layerType=" + getLayerType() + ", abilitiesSet=" + getAbilitiesSet() + ", abilityValues=" + getAbilityValues() + ", flySpeed=" + getFlySpeed() + ", walkSpeed=" + getWalkSpeed() + ")";
    }

    public Type getLayerType() {
        return this.layerType;
    }

    public Set<Ability> getAbilitiesSet() {
        return this.abilitiesSet;
    }

    public Set<Ability> getAbilityValues() {
        return this.abilityValues;
    }

    public float getFlySpeed() {
        return this.flySpeed;
    }

    public float getWalkSpeed() {
        return this.walkSpeed;
    }
}
