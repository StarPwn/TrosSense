package org.cloudburstmc.protocol.bedrock.data.structure;

/* loaded from: classes5.dex */
public enum StructureMirror {
    NONE,
    X,
    Z,
    XZ;

    private static final StructureMirror[] VALUES = values();

    public static StructureMirror from(int id) {
        return VALUES[id];
    }
}
