package org.cloudburstmc.protocol.bedrock.data.structure;

/* loaded from: classes5.dex */
public enum StructureAnimationMode {
    NONE,
    LAYER,
    BLOCKS;

    private static final StructureAnimationMode[] VALUES = values();

    public static StructureAnimationMode from(int id) {
        return VALUES[id];
    }
}
