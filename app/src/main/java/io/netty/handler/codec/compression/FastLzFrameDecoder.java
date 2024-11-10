package io.netty.handler.codec.compression;

import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.zip.Adler32;
import java.util.zip.Checksum;

/* loaded from: classes4.dex */
public class FastLzFrameDecoder extends ByteToMessageDecoder {
    private final ByteBufChecksum checksum;
    private int chunkLength;
    private int currentChecksum;
    private State currentState;
    private boolean hasChecksum;
    private boolean isCompressed;
    private int originalLength;

    /* loaded from: classes4.dex */
    private enum State {
        INIT_BLOCK,
        INIT_BLOCK_PARAMS,
        DECOMPRESS_DATA,
        CORRUPTED
    }

    public FastLzFrameDecoder() {
        this(false);
    }

    public FastLzFrameDecoder(boolean validateChecksums) {
        this(validateChecksums ? new Adler32() : null);
    }

    public FastLzFrameDecoder(Checksum checksum) {
        this.currentState = State.INIT_BLOCK;
        this.checksum = checksum == null ? null : ByteBufChecksum.wrapChecksum(checksum);
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:6:0x0012. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:90:0x0156 A[Catch: Exception -> 0x0169, TryCatch #2 {Exception -> 0x0169, blocks: (B:8:0x0017, B:9:0x0165, B:10:0x0168, B:29:0x015b, B:30:0x0164, B:82:0x0149, B:90:0x0156, B:91:0x015a), top: B:5:0x0012 }] */
    @Override // io.netty.handler.codec.ByteToMessageDecoder
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void decode(io.netty.channel.ChannelHandlerContext r17, io.netty.buffer.ByteBuf r18, java.util.List<java.lang.Object> r19) throws java.lang.Exception {
        /*
            Method dump skipped, instructions count: 384
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.codec.compression.FastLzFrameDecoder.decode(io.netty.channel.ChannelHandlerContext, io.netty.buffer.ByteBuf, java.util.List):void");
    }
}
