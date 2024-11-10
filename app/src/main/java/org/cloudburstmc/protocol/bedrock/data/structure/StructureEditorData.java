package org.cloudburstmc.protocol.bedrock.data.structure;

/* loaded from: classes5.dex */
public final class StructureEditorData {
    private final boolean boundingBoxVisible;
    private final String dataField;
    private final boolean includingPlayers;
    private final String name;
    private final StructureRedstoneSaveMode redstoneSaveMode;
    private final StructureSettings settings;
    private final StructureBlockType type;

    public StructureEditorData(String name, String dataField, boolean includingPlayers, boolean boundingBoxVisible, StructureBlockType type, StructureSettings settings, StructureRedstoneSaveMode redstoneSaveMode) {
        this.name = name;
        this.dataField = dataField;
        this.includingPlayers = includingPlayers;
        this.boundingBoxVisible = boundingBoxVisible;
        this.type = type;
        this.settings = settings;
        this.redstoneSaveMode = redstoneSaveMode;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof StructureEditorData)) {
            return false;
        }
        StructureEditorData other = (StructureEditorData) o;
        if (isIncludingPlayers() != other.isIncludingPlayers() || isBoundingBoxVisible() != other.isBoundingBoxVisible()) {
            return false;
        }
        Object this$name = getName();
        Object other$name = other.getName();
        if (this$name != null ? !this$name.equals(other$name) : other$name != null) {
            return false;
        }
        Object this$dataField = getDataField();
        Object other$dataField = other.getDataField();
        if (this$dataField != null ? !this$dataField.equals(other$dataField) : other$dataField != null) {
            return false;
        }
        Object this$type = getType();
        Object other$type = other.getType();
        if (this$type != null ? !this$type.equals(other$type) : other$type != null) {
            return false;
        }
        Object this$settings = getSettings();
        Object other$settings = other.getSettings();
        if (this$settings != null ? !this$settings.equals(other$settings) : other$settings != null) {
            return false;
        }
        Object this$redstoneSaveMode = getRedstoneSaveMode();
        Object other$redstoneSaveMode = other.getRedstoneSaveMode();
        return this$redstoneSaveMode != null ? this$redstoneSaveMode.equals(other$redstoneSaveMode) : other$redstoneSaveMode == null;
    }

    public int hashCode() {
        int result = (1 * 59) + (isIncludingPlayers() ? 79 : 97);
        int result2 = result * 59;
        int i = isBoundingBoxVisible() ? 79 : 97;
        Object $name = getName();
        int result3 = ((result2 + i) * 59) + ($name == null ? 43 : $name.hashCode());
        Object $dataField = getDataField();
        int result4 = (result3 * 59) + ($dataField == null ? 43 : $dataField.hashCode());
        Object $type = getType();
        int result5 = (result4 * 59) + ($type == null ? 43 : $type.hashCode());
        Object $settings = getSettings();
        int result6 = (result5 * 59) + ($settings == null ? 43 : $settings.hashCode());
        Object $redstoneSaveMode = getRedstoneSaveMode();
        return (result6 * 59) + ($redstoneSaveMode != null ? $redstoneSaveMode.hashCode() : 43);
    }

    public String toString() {
        return "StructureEditorData(name=" + getName() + ", dataField=" + getDataField() + ", includingPlayers=" + isIncludingPlayers() + ", boundingBoxVisible=" + isBoundingBoxVisible() + ", type=" + getType() + ", settings=" + getSettings() + ", redstoneSaveMode=" + getRedstoneSaveMode() + ")";
    }

    public String getName() {
        return this.name;
    }

    public String getDataField() {
        return this.dataField;
    }

    public boolean isIncludingPlayers() {
        return this.includingPlayers;
    }

    public boolean isBoundingBoxVisible() {
        return this.boundingBoxVisible;
    }

    public StructureBlockType getType() {
        return this.type;
    }

    public StructureSettings getSettings() {
        return this.settings;
    }

    public StructureRedstoneSaveMode getRedstoneSaveMode() {
        return this.redstoneSaveMode;
    }
}
