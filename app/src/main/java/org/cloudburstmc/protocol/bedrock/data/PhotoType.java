package org.cloudburstmc.protocol.bedrock.data;

/* loaded from: classes5.dex */
public enum PhotoType {
    PORTFOLIO,
    PHOTO_ITEM,
    BOOK;

    private static final PhotoType[] VALUES = values();

    public static PhotoType from(int id) {
        if (id < 0 || id >= VALUES.length) {
            return null;
        }
        return VALUES[id];
    }
}
