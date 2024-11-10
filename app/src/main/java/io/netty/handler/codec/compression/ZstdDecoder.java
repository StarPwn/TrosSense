package io.netty.handler.codec.compression;

import com.github.luben.zstd.BaseZstdBufferDecompressingStreamNoFinalizer;
import com.github.luben.zstd.ZstdBufferDecompressingStreamNoFinalizer;
import com.github.luben.zstd.ZstdDirectBufferDecompressingStreamNoFinalizer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

/* loaded from: classes4.dex */
public final class ZstdDecoder extends ByteToMessageDecoder {
    private State currentState;
    private final int outCapacity;
    private ZstdStream stream;

    /* loaded from: classes4.dex */
    private enum State {
        DECOMPRESS_DATA,
        CORRUPTED
    }

    public ZstdDecoder() {
        try {
            Zstd.ensureAvailability();
            this.outCapacity = ZstdBufferDecompressingStreamNoFinalizer.recommendedTargetBufferSize();
            this.currentState = State.DECOMPRESS_DATA;
        } catch (Throwable throwable) {
            throw new ExceptionInInitializerError(throwable);
        }
    }

    @Override // io.netty.handler.codec.ByteToMessageDecoder
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        try {
            if (this.currentState == State.CORRUPTED) {
                in.skipBytes(in.readableBytes());
                return;
            }
            int compressedLength = in.readableBytes();
            if (compressedLength == 0) {
                return;
            }
            if (this.stream == null) {
                this.stream = new ZstdStream(in.isDirect(), this.outCapacity);
            }
            do {
                ByteBuf decompressed = this.stream.decompress(ctx.alloc(), in);
                if (decompressed == null) {
                    return;
                } else {
                    out.add(decompressed);
                }
            } while (in.isReadable());
        } catch (DecompressionException e) {
            this.currentState = State.CORRUPTED;
            throw e;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.ByteToMessageDecoder
    public void handlerRemoved0(ChannelHandlerContext ctx) throws Exception {
        try {
            if (this.stream != null) {
                this.stream.close();
                this.stream = null;
            }
        } finally {
            super.handlerRemoved0(ctx);
        }
    }

    /* loaded from: classes4.dex */
    private static final class ZstdStream {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private ByteBuffer current;
        private final BaseZstdBufferDecompressingStreamNoFinalizer decompressingStream;
        private final boolean direct;
        private final int outCapacity;
        private static final ByteBuffer EMPTY_HEAP_BUFFER = ByteBuffer.allocate(0);
        private static final ByteBuffer EMPTY_DIRECT_BUFFER = ByteBuffer.allocateDirect(0);

        ZstdStream(boolean direct, int outCapacity) {
            this.direct = direct;
            this.outCapacity = outCapacity;
            if (direct) {
                this.decompressingStream = new ZstdDirectBufferDecompressingStreamNoFinalizer(EMPTY_DIRECT_BUFFER) { // from class: io.netty.handler.codec.compression.ZstdDecoder.ZstdStream.1
                    protected ByteBuffer refill(ByteBuffer toRefill) {
                        return ZstdStream.this.refill(toRefill);
                    }
                };
            } else {
                this.decompressingStream = new ZstdBufferDecompressingStreamNoFinalizer(EMPTY_HEAP_BUFFER) { // from class: io.netty.handler.codec.compression.ZstdDecoder.ZstdStream.2
                    protected ByteBuffer refill(ByteBuffer toRefill) {
                        return ZstdStream.this.refill(toRefill);
                    }
                };
            }
        }

        ByteBuf decompress(ByteBufAllocator alloc, ByteBuf in) throws DecompressionException {
            ByteBuf source;
            int read;
            int read2;
            if (this.direct && !in.isDirect()) {
                source = alloc.directBuffer(in.readableBytes());
                source.writeBytes(in, in.readerIndex(), in.readableBytes());
            } else if (this.direct || in.hasArray()) {
                source = in;
            } else {
                source = alloc.heapBuffer(in.readableBytes());
                source.writeBytes(in, in.readerIndex(), in.readableBytes());
            }
            int inPosition = -1;
            ByteBuf outBuffer = null;
            try {
                try {
                    ByteBuffer inNioBuffer = CompressionUtil.safeNioBuffer(source, source.readerIndex(), source.readableBytes());
                    inPosition = inNioBuffer.position();
                    if (!inNioBuffer.hasRemaining()) {
                        throw new AssertionError();
                    }
                    this.current = inNioBuffer;
                    ByteBuf outBuffer2 = this.direct ? alloc.directBuffer(this.outCapacity) : alloc.heapBuffer(this.outCapacity);
                    ByteBuffer target = outBuffer2.internalNioBuffer(outBuffer2.writerIndex(), outBuffer2.writableBytes());
                    int position = target.position();
                    while (true) {
                        if (this.decompressingStream.read(target) != 0 && this.decompressingStream.hasRemaining() && target.hasRemaining() && this.current.hasRemaining()) {
                        }
                        int written = target.position() - position;
                        if (written > 0) {
                            outBuffer2.writerIndex(outBuffer2.writerIndex() + written);
                            ByteBuf out = outBuffer2;
                            outBuffer = null;
                            return out;
                        }
                        if (!this.decompressingStream.hasRemaining() || !this.current.hasRemaining()) {
                            break;
                        }
                    }
                    if (outBuffer2 != null) {
                        outBuffer2.release();
                    }
                    if (source != in) {
                        source.release();
                    }
                    ByteBuffer buffer = this.current;
                    this.current = null;
                    if (inPosition != -1 && (read2 = buffer.position() - inPosition) > 0) {
                        in.skipBytes(read2);
                    }
                    return null;
                } catch (IOException e) {
                    throw new DecompressionException(e);
                }
            } finally {
                if (outBuffer != null) {
                    outBuffer.release();
                }
                if (source != in) {
                    source.release();
                }
                ByteBuffer buffer2 = this.current;
                this.current = null;
                if (inPosition != -1 && (read = buffer2.position() - inPosition) > 0) {
                    in.skipBytes(read);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public ByteBuffer refill(ByteBuffer toRefill) {
            return this.current;
        }

        void close() {
            this.decompressingStream.close();
        }
    }
}
