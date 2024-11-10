package org.cloudburstmc.protocol.bedrock.data.map;

/* loaded from: classes5.dex */
public final class MapPixel {
    private final int index;
    private final int pixel;

    public MapPixel(int pixel, int index) {
        this.pixel = pixel;
        this.index = index;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MapPixel)) {
            return false;
        }
        MapPixel other = (MapPixel) o;
        return getPixel() == other.getPixel() && getIndex() == other.getIndex();
    }

    public int hashCode() {
        int result = (1 * 59) + getPixel();
        return (result * 59) + getIndex();
    }

    public String toString() {
        return "MapPixel(pixel=" + getPixel() + ", index=" + getIndex() + ")";
    }

    public int getPixel() {
        return this.pixel;
    }

    public int getIndex() {
        return this.index;
    }
}
