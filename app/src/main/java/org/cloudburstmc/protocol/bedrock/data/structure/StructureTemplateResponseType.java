package org.cloudburstmc.protocol.bedrock.data.structure;

/* loaded from: classes5.dex */
public enum StructureTemplateResponseType {
    NONE,
    EXPORT,
    QUERY,
    IMPORT;

    private static final StructureTemplateResponseType[] VALUES = values();

    public static StructureTemplateResponseType from(int id) {
        return VALUES[id];
    }
}
