package io.netty.handler.codec.compression;

/* loaded from: classes4.dex */
public final class GzipOptions extends DeflateOptions {
    static final GzipOptions DEFAULT = new GzipOptions(6, 15, 8);

    /* JADX INFO: Access modifiers changed from: package-private */
    public GzipOptions(int compressionLevel, int windowBits, int memLevel) {
        super(compressionLevel, windowBits, memLevel);
    }
}
