package org.cloudburstmc.protocol.bedrock.data;

/* loaded from: classes5.dex */
public final class MapDecoration {
    private final int color;
    private final int image;
    private final String label;
    private final int rotation;
    private final int xOffset;
    private final int yOffset;

    public MapDecoration(int image, int rotation, int xOffset, int yOffset, String label, int color) {
        this.image = image;
        this.rotation = rotation;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.label = label;
        this.color = color;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MapDecoration)) {
            return false;
        }
        MapDecoration other = (MapDecoration) o;
        if (getImage() != other.getImage() || getRotation() != other.getRotation() || getXOffset() != other.getXOffset() || getYOffset() != other.getYOffset() || getColor() != other.getColor()) {
            return false;
        }
        Object this$label = getLabel();
        Object other$label = other.getLabel();
        return this$label != null ? this$label.equals(other$label) : other$label == null;
    }

    public int hashCode() {
        int result = (1 * 59) + getImage();
        int result2 = (((((((result * 59) + getRotation()) * 59) + getXOffset()) * 59) + getYOffset()) * 59) + getColor();
        Object $label = getLabel();
        return (result2 * 59) + ($label == null ? 43 : $label.hashCode());
    }

    public String toString() {
        return "MapDecoration(image=" + getImage() + ", rotation=" + getRotation() + ", xOffset=" + getXOffset() + ", yOffset=" + getYOffset() + ", label=" + getLabel() + ", color=" + getColor() + ")";
    }

    public int getImage() {
        return this.image;
    }

    public int getRotation() {
        return this.rotation;
    }

    public int getXOffset() {
        return this.xOffset;
    }

    public int getYOffset() {
        return this.yOffset;
    }

    public String getLabel() {
        return this.label;
    }

    public int getColor() {
        return this.color;
    }
}
