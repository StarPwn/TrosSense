package io.netty.handler.codec.compression;

import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.internal.ObjectUtil;
import java.util.zip.Checksum;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FastDecompressor;

/* loaded from: classes4.dex */
public class Lz4FrameDecoder extends ByteToMessageDecoder {
    private int blockType;
    private ByteBufChecksum checksum;
    private int compressedLength;
    private int currentChecksum;
    private State currentState;
    private int decompressedLength;
    private LZ4FastDecompressor decompressor;

    /* loaded from: classes4.dex */
    private enum State {
        INIT_BLOCK,
        DECOMPRESS_DATA,
        FINISHED,
        CORRUPTED
    }

    public Lz4FrameDecoder() {
        this(false);
    }

    public Lz4FrameDecoder(boolean validateChecksums) {
        this(LZ4Factory.fastestInstance(), validateChecksums);
    }

    public Lz4FrameDecoder(LZ4Factory factory, boolean validateChecksums) {
        this(factory, validateChecksums ? new Lz4XXHash32(-1756908916) : null);
    }

    public Lz4FrameDecoder(LZ4Factory factory, Checksum checksum) {
        this.currentState = State.INIT_BLOCK;
        this.decompressor = ((LZ4Factory) ObjectUtil.checkNotNull(factory, "factory")).fastDecompressor();
        this.checksum = checksum == null ? null : ByteBufChecksum.wrapChecksum(checksum);
    }

    /* JADX WARN: Removed duplicated region for block: B:79:0x0157 A[Catch: Exception -> 0x01a2, TRY_ENTER, TryCatch #4 {Exception -> 0x01a2, blocks: (B:8:0x0018, B:9:0x019e, B:10:0x01a1, B:44:0x015c, B:45:0x0177, B:46:0x0178, B:47:0x0193, B:48:0x0194, B:49:0x019d, B:79:0x0157, B:80:0x015b, B:70:0x011d), top: B:5:0x0013 }] */
    @Override // io.netty.handler.codec.ByteToMessageDecoder
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void decode(io.netty.channel.ChannelHandlerContext r18, io.netty.buffer.ByteBuf r19, java.util.List<java.lang.Object> r20) throws java.lang.Exception {
        /*
            Method dump skipped, instructions count: 450
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.codec.compression.Lz4FrameDecoder.decode(io.netty.channel.ChannelHandlerContext, io.netty.buffer.ByteBuf, java.util.List):void");
    }

    public boolean isClosed() {
        return this.currentState == State.FINISHED;
    }
}
