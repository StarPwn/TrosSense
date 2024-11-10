package org.cloudburstmc.protocol.bedrock.data.structure;

/* loaded from: classes5.dex */
public enum StructureTemplateRequestOperation {
    NONE,
    EXPORT_FROM_SAVED_MODE,
    EXPORT_FROM_LOAD_MODE,
    QUERY_SAVED_STRUCTURE,
    IMPORT;

    private static final StructureTemplateRequestOperation[] VALUES = values();

    public static StructureTemplateRequestOperation from(int id) {
        return VALUES[id];
    }
}
