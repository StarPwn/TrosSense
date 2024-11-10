package org.cloudburstmc.protocol.bedrock.data.structure;

/* loaded from: classes5.dex */
public enum StructureBlockType {
    DATA,
    SAVE,
    LOAD,
    CORNER,
    INVALID,
    EXPORT;

    private static final StructureBlockType[] VALUES = values();

    public static StructureBlockType from(int id) {
        return VALUES[id];
    }
}
