package org.cloudburstmc.protocol.bedrock.data;

/* loaded from: classes5.dex */
public class TrimMaterial {
    private final String color;
    private final String itemName;
    private final String materialId;

    public TrimMaterial(String materialId, String color, String itemName) {
        this.materialId = materialId;
        this.color = color;
        this.itemName = itemName;
    }

    protected boolean canEqual(Object other) {
        return other instanceof TrimMaterial;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TrimMaterial)) {
            return false;
        }
        TrimMaterial other = (TrimMaterial) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$materialId = getMaterialId();
        Object other$materialId = other.getMaterialId();
        if (this$materialId != null ? !this$materialId.equals(other$materialId) : other$materialId != null) {
            return false;
        }
        Object this$color = getColor();
        Object other$color = other.getColor();
        if (this$color != null ? !this$color.equals(other$color) : other$color != null) {
            return false;
        }
        Object this$itemName = getItemName();
        Object other$itemName = other.getItemName();
        return this$itemName != null ? this$itemName.equals(other$itemName) : other$itemName == null;
    }

    public int hashCode() {
        Object $materialId = getMaterialId();
        int result = (1 * 59) + ($materialId == null ? 43 : $materialId.hashCode());
        Object $color = getColor();
        int result2 = (result * 59) + ($color == null ? 43 : $color.hashCode());
        Object $itemName = getItemName();
        return (result2 * 59) + ($itemName != null ? $itemName.hashCode() : 43);
    }

    public String toString() {
        return "TrimMaterial(materialId=" + getMaterialId() + ", color=" + getColor() + ", itemName=" + getItemName() + ")";
    }

    public String getMaterialId() {
        return this.materialId;
    }

    public String getColor() {
        return this.color;
    }

    public String getItemName() {
        return this.itemName;
    }
}
