package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import java.util.zip.Deflater;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public class SpdyHeaderBlockZlibEncoder extends SpdyHeaderBlockRawEncoder {
    private final Deflater compressor;
    private boolean finished;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SpdyHeaderBlockZlibEncoder(SpdyVersion spdyVersion, int compressionLevel) {
        super(spdyVersion);
        if (compressionLevel < 0 || compressionLevel > 9) {
            throw new IllegalArgumentException("compressionLevel: " + compressionLevel + " (expected: 0-9)");
        }
        this.compressor = new Deflater(compressionLevel);
        this.compressor.setDictionary(SpdyCodecUtil.SPDY_DICT);
    }

    private int setInput(ByteBuf decompressed) {
        int len = decompressed.readableBytes();
        if (decompressed.hasArray()) {
            this.compressor.setInput(decompressed.array(), decompressed.arrayOffset() + decompressed.readerIndex(), len);
        } else {
            byte[] in = new byte[len];
            decompressed.getBytes(decompressed.readerIndex(), in);
            this.compressor.setInput(in, 0, in.length);
        }
        return len;
    }

    private ByteBuf encode(ByteBufAllocator alloc, int len) {
        ByteBuf compressed = alloc.heapBuffer(len);
        boolean release = true;
        while (compressInto(compressed)) {
            try {
                compressed.ensureWritable(compressed.capacity() << 1);
            } finally {
                if (release) {
                    compressed.release();
                }
            }
        }
        release = false;
        return compressed;
    }

    private boolean compressInto(ByteBuf compressed) {
        int numBytes;
        byte[] out = compressed.array();
        int off = compressed.arrayOffset() + compressed.writerIndex();
        int toWrite = compressed.writableBytes();
        if (PlatformDependent.javaVersion() >= 7) {
            numBytes = this.compressor.deflate(out, off, toWrite, 2);
        } else {
            numBytes = this.compressor.deflate(out, off, toWrite);
        }
        compressed.writerIndex(compressed.writerIndex() + numBytes);
        return numBytes == toWrite;
    }

    @Override // io.netty.handler.codec.spdy.SpdyHeaderBlockRawEncoder, io.netty.handler.codec.spdy.SpdyHeaderBlockEncoder
    public ByteBuf encode(ByteBufAllocator alloc, SpdyHeadersFrame frame) throws Exception {
        ObjectUtil.checkNotNullWithIAE(alloc, "alloc");
        ObjectUtil.checkNotNullWithIAE(frame, "frame");
        if (this.finished) {
            return Unpooled.EMPTY_BUFFER;
        }
        ByteBuf decompressed = super.encode(alloc, frame);
        try {
            if (!decompressed.isReadable()) {
                return Unpooled.EMPTY_BUFFER;
            }
            int len = setInput(decompressed);
            return encode(alloc, len);
        } finally {
            decompressed.release();
        }
    }

    @Override // io.netty.handler.codec.spdy.SpdyHeaderBlockRawEncoder, io.netty.handler.codec.spdy.SpdyHeaderBlockEncoder
    public void end() {
        if (this.finished) {
            return;
        }
        this.finished = true;
        this.compressor.end();
        super.end();
    }
}
