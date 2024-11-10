package org.cloudburstmc.protocol.bedrock.data.definitions;

/* loaded from: classes5.dex */
public final class DimensionDefinition {
    private final int generatorType;
    private final String id;
    private final int maximumHeight;
    private final int minimumHeight;

    public DimensionDefinition(String id, int maximumHeight, int minimumHeight, int generatorType) {
        this.id = id;
        this.maximumHeight = maximumHeight;
        this.minimumHeight = minimumHeight;
        this.generatorType = generatorType;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DimensionDefinition)) {
            return false;
        }
        DimensionDefinition other = (DimensionDefinition) o;
        if (getMaximumHeight() != other.getMaximumHeight() || getMinimumHeight() != other.getMinimumHeight() || getGeneratorType() != other.getGeneratorType()) {
            return false;
        }
        Object this$id = getId();
        Object other$id = other.getId();
        return this$id != null ? this$id.equals(other$id) : other$id == null;
    }

    public int hashCode() {
        int result = (1 * 59) + getMaximumHeight();
        int result2 = (((result * 59) + getMinimumHeight()) * 59) + getGeneratorType();
        Object $id = getId();
        return (result2 * 59) + ($id == null ? 43 : $id.hashCode());
    }

    public String toString() {
        return "DimensionDefinition(id=" + getId() + ", maximumHeight=" + getMaximumHeight() + ", minimumHeight=" + getMinimumHeight() + ", generatorType=" + getGeneratorType() + ")";
    }

    public String getId() {
        return this.id;
    }

    public int getMaximumHeight() {
        return this.maximumHeight;
    }

    public int getMinimumHeight() {
        return this.minimumHeight;
    }

    public int getGeneratorType() {
        return this.generatorType;
    }
}
