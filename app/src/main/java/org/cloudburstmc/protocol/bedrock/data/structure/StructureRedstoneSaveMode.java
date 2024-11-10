package org.cloudburstmc.protocol.bedrock.data.structure;

/* loaded from: classes5.dex */
public enum StructureRedstoneSaveMode {
    SAVES_TO_MEMORY,
    SAVES_TO_DISK;

    private static final StructureRedstoneSaveMode[] VALUES = values();

    public static StructureRedstoneSaveMode from(int id) {
        return VALUES[id];
    }
}
