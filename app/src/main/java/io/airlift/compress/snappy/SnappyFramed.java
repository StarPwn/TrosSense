package io.airlift.compress.snappy;

/* loaded from: classes.dex */
final class SnappyFramed {
    public static final int COMPRESSED_DATA_FLAG = 0;
    public static final byte[] HEADER_BYTES = {-1, 6, 0, 0, 115, 78, 97, 80, 112, 89};
    public static final int STREAM_IDENTIFIER_FLAG = 255;
    public static final int UNCOMPRESSED_DATA_FLAG = 1;

    private SnappyFramed() {
    }
}
