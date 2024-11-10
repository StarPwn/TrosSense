package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.internal.PlatformDependent;

/* loaded from: classes4.dex */
public abstract class SpdyHeaderBlockEncoder {
    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract ByteBuf encode(ByteBufAllocator byteBufAllocator, SpdyHeadersFrame spdyHeadersFrame) throws Exception;

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void end();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SpdyHeaderBlockEncoder newInstance(SpdyVersion version, int compressionLevel, int windowBits, int memLevel) {
        if (PlatformDependent.javaVersion() >= 7) {
            return new SpdyHeaderBlockZlibEncoder(version, compressionLevel);
        }
        return new SpdyHeaderBlockJZlibEncoder(version, compressionLevel, windowBits, memLevel);
    }
}
