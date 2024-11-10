package org.cloudburstmc.protocol.bedrock.data.structure;

/* loaded from: classes5.dex */
public enum StructureRotation {
    NONE,
    ROTATE_90,
    ROTATE_180,
    ROTATE_270;

    private static final StructureRotation[] VALUES = values();

    public static StructureRotation from(int id) {
        return VALUES[id];
    }
}
