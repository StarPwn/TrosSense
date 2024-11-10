package io.netty.handler.codec.compression;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.PromiseNotifier;

/* loaded from: classes4.dex */
public class Bzip2Encoder extends MessageToByteEncoder<ByteBuf> {
    private Bzip2BlockCompressor blockCompressor;
    private volatile ChannelHandlerContext ctx;
    private State currentState;
    private volatile boolean finished;
    private final int streamBlockSize;
    private int streamCRC;
    private final Bzip2BitWriter writer;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public enum State {
        INIT,
        INIT_BLOCK,
        WRITE_DATA,
        CLOSE_BLOCK
    }

    public Bzip2Encoder() {
        this(9);
    }

    public Bzip2Encoder(int blockSizeMultiplier) {
        this.currentState = State.INIT;
        this.writer = new Bzip2BitWriter();
        if (blockSizeMultiplier < 1 || blockSizeMultiplier > 9) {
            throw new IllegalArgumentException("blockSizeMultiplier: " + blockSizeMultiplier + " (expected: 1-9)");
        }
        this.streamBlockSize = 100000 * blockSizeMultiplier;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Failed to find 'out' block for switch in B:7:0x0012. Please report as an issue. */
    @Override // io.netty.handler.codec.MessageToByteEncoder
    public void encode(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) throws Exception {
        if (this.finished) {
            out.writeBytes(in);
            return;
        }
        while (true) {
            switch (this.currentState) {
                case INIT:
                    out.ensureWritable(4);
                    out.writeMedium(4348520);
                    out.writeByte((this.streamBlockSize / 100000) + 48);
                    this.currentState = State.INIT_BLOCK;
                case INIT_BLOCK:
                    this.blockCompressor = new Bzip2BlockCompressor(this.writer, this.streamBlockSize);
                    this.currentState = State.WRITE_DATA;
                case WRITE_DATA:
                    if (!in.isReadable()) {
                        return;
                    }
                    Bzip2BlockCompressor blockCompressor = this.blockCompressor;
                    int length = Math.min(in.readableBytes(), blockCompressor.availableSize());
                    int bytesWritten = blockCompressor.write(in, in.readerIndex(), length);
                    in.skipBytes(bytesWritten);
                    if (!blockCompressor.isFull()) {
                        if (!in.isReadable()) {
                            return;
                        }
                    } else {
                        this.currentState = State.CLOSE_BLOCK;
                        closeBlock(out);
                        this.currentState = State.INIT_BLOCK;
                    }
                case CLOSE_BLOCK:
                    closeBlock(out);
                    this.currentState = State.INIT_BLOCK;
                default:
                    throw new IllegalStateException();
            }
        }
    }

    private void closeBlock(ByteBuf out) {
        Bzip2BlockCompressor blockCompressor = this.blockCompressor;
        if (!blockCompressor.isEmpty()) {
            blockCompressor.close(out);
            int blockCRC = blockCompressor.crc();
            this.streamCRC = ((this.streamCRC << 1) | (this.streamCRC >>> 31)) ^ blockCRC;
        }
    }

    public boolean isClosed() {
        return this.finished;
    }

    public ChannelFuture close() {
        return close(ctx().newPromise());
    }

    public ChannelFuture close(final ChannelPromise promise) {
        ChannelHandlerContext ctx = ctx();
        EventExecutor executor = ctx.executor();
        if (executor.inEventLoop()) {
            return finishEncode(ctx, promise);
        }
        executor.execute(new Runnable() { // from class: io.netty.handler.codec.compression.Bzip2Encoder.1
            @Override // java.lang.Runnable
            public void run() {
                ChannelFuture f = Bzip2Encoder.this.finishEncode(Bzip2Encoder.this.ctx(), promise);
                PromiseNotifier.cascade(f, promise);
            }
        });
        return promise;
    }

    @Override // io.netty.channel.ChannelOutboundHandlerAdapter, io.netty.channel.ChannelOutboundHandler
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ChannelFuture f = finishEncode(ctx, ctx.newPromise());
        EncoderUtil.closeAfterFinishEncode(ctx, f, promise);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ChannelFuture finishEncode(ChannelHandlerContext ctx, ChannelPromise promise) {
        if (this.finished) {
            promise.setSuccess();
            return promise;
        }
        this.finished = true;
        ByteBuf footer = ctx.alloc().buffer();
        closeBlock(footer);
        int streamCRC = this.streamCRC;
        Bzip2BitWriter writer = this.writer;
        try {
            writer.writeBits(footer, 24, 1536581L);
            writer.writeBits(footer, 24, 3690640L);
            writer.writeInt(footer, streamCRC);
            writer.flush(footer);
            this.blockCompressor = null;
            return ctx.writeAndFlush(footer, promise);
        } catch (Throwable th) {
            this.blockCompressor = null;
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ChannelHandlerContext ctx() {
        ChannelHandlerContext ctx = this.ctx;
        if (ctx == null) {
            throw new IllegalStateException("not added to a pipeline");
        }
        return ctx;
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
    }
}
