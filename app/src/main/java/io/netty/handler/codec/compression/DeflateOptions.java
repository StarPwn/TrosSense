package io.netty.handler.codec.compression;

import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public class DeflateOptions implements CompressionOptions {
    static final DeflateOptions DEFAULT = new DeflateOptions(6, 15, 8);
    private final int compressionLevel;
    private final int memLevel;
    private final int windowBits;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DeflateOptions(int compressionLevel, int windowBits, int memLevel) {
        this.compressionLevel = ObjectUtil.checkInRange(compressionLevel, 0, 9, "compressionLevel");
        this.windowBits = ObjectUtil.checkInRange(windowBits, 9, 15, "windowBits");
        this.memLevel = ObjectUtil.checkInRange(memLevel, 1, 9, "memLevel");
    }

    public int compressionLevel() {
        return this.compressionLevel;
    }

    public int windowBits() {
        return this.windowBits;
    }

    public int memLevel() {
        return this.memLevel;
    }
}
