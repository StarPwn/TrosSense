package io.netty.handler.codec.compression;

import com.google.common.base.Ascii;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.PromiseNotifier;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.zip.CRC32;
import java.util.zip.Deflater;

/* loaded from: classes4.dex */
public class JdkZlibEncoder extends ZlibEncoder {
    private final CRC32 crc;
    private volatile ChannelHandlerContext ctx;
    private final Deflater deflater;
    private volatile boolean finished;
    private final ZlibWrapper wrapper;
    private boolean writeHeader;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) JdkZlibEncoder.class);
    private static final byte[] gzipHeader = {Ascii.US, -117, 8, 0, 0, 0, 0, 0, 0, 0};
    private static final int MAX_INITIAL_OUTPUT_BUFFER_SIZE = SystemPropertyUtil.getInt("io.netty.jdkzlib.encoder.maxInitialOutputBufferSize", 65536);
    private static final int MAX_INPUT_BUFFER_SIZE = SystemPropertyUtil.getInt("io.netty.jdkzlib.encoder.maxInputBufferSize", 65536);

    static {
        if (logger.isDebugEnabled()) {
            logger.debug("-Dio.netty.jdkzlib.encoder.maxInitialOutputBufferSize={}", Integer.valueOf(MAX_INITIAL_OUTPUT_BUFFER_SIZE));
            logger.debug("-Dio.netty.jdkzlib.encoder.maxInputBufferSize={}", Integer.valueOf(MAX_INPUT_BUFFER_SIZE));
        }
    }

    public JdkZlibEncoder() {
        this(6);
    }

    public JdkZlibEncoder(int compressionLevel) {
        this(ZlibWrapper.ZLIB, compressionLevel);
    }

    public JdkZlibEncoder(ZlibWrapper wrapper) {
        this(wrapper, 6);
    }

    public JdkZlibEncoder(ZlibWrapper wrapper, int compressionLevel) {
        this.crc = new CRC32();
        this.writeHeader = true;
        ObjectUtil.checkInRange(compressionLevel, 0, 9, "compressionLevel");
        ObjectUtil.checkNotNull(wrapper, "wrapper");
        if (wrapper == ZlibWrapper.ZLIB_OR_NONE) {
            throw new IllegalArgumentException("wrapper '" + ZlibWrapper.ZLIB_OR_NONE + "' is not allowed for compression.");
        }
        this.wrapper = wrapper;
        this.deflater = new Deflater(compressionLevel, wrapper != ZlibWrapper.ZLIB);
    }

    public JdkZlibEncoder(byte[] dictionary) {
        this(6, dictionary);
    }

    public JdkZlibEncoder(int compressionLevel, byte[] dictionary) {
        this.crc = new CRC32();
        this.writeHeader = true;
        ObjectUtil.checkInRange(compressionLevel, 0, 9, "compressionLevel");
        ObjectUtil.checkNotNull(dictionary, "dictionary");
        this.wrapper = ZlibWrapper.ZLIB;
        this.deflater = new Deflater(compressionLevel);
        this.deflater.setDictionary(dictionary);
    }

    @Override // io.netty.handler.codec.compression.ZlibEncoder
    public ChannelFuture close() {
        return close(ctx().newPromise());
    }

    @Override // io.netty.handler.codec.compression.ZlibEncoder
    public ChannelFuture close(final ChannelPromise promise) {
        ChannelHandlerContext ctx = ctx();
        EventExecutor executor = ctx.executor();
        if (executor.inEventLoop()) {
            return finishEncode(ctx, promise);
        }
        final ChannelPromise p = ctx.newPromise();
        executor.execute(new Runnable() { // from class: io.netty.handler.codec.compression.JdkZlibEncoder.1
            @Override // java.lang.Runnable
            public void run() {
                ChannelFuture f = JdkZlibEncoder.this.finishEncode(JdkZlibEncoder.this.ctx(), p);
                PromiseNotifier.cascade(f, promise);
            }
        });
        return p;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ChannelHandlerContext ctx() {
        ChannelHandlerContext ctx = this.ctx;
        if (ctx == null) {
            throw new IllegalStateException("not added to a pipeline");
        }
        return ctx;
    }

    @Override // io.netty.handler.codec.compression.ZlibEncoder
    public boolean isClosed() {
        return this.finished;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageToByteEncoder
    public void encode(ChannelHandlerContext ctx, ByteBuf uncompressed, ByteBuf out) throws Exception {
        if (this.finished) {
            out.writeBytes(uncompressed);
            return;
        }
        int len = uncompressed.readableBytes();
        if (len == 0) {
            return;
        }
        if (uncompressed.hasArray()) {
            encodeSome(uncompressed, out);
        } else {
            int heapBufferSize = Math.min(len, MAX_INPUT_BUFFER_SIZE);
            ByteBuf heapBuf = ctx.alloc().heapBuffer(heapBufferSize, heapBufferSize);
            while (uncompressed.isReadable()) {
                try {
                    uncompressed.readBytes(heapBuf, Math.min(heapBuf.writableBytes(), uncompressed.readableBytes()));
                    encodeSome(heapBuf, out);
                    heapBuf.clear();
                } finally {
                    heapBuf.release();
                }
            }
        }
        this.deflater.setInput(EmptyArrays.EMPTY_BYTES);
    }

    private void encodeSome(ByteBuf in, ByteBuf out) {
        byte[] inAry = in.array();
        int offset = in.arrayOffset() + in.readerIndex();
        if (this.writeHeader) {
            this.writeHeader = false;
            if (this.wrapper == ZlibWrapper.GZIP) {
                out.writeBytes(gzipHeader);
            }
        }
        int len = in.readableBytes();
        if (this.wrapper == ZlibWrapper.GZIP) {
            this.crc.update(inAry, offset, len);
        }
        this.deflater.setInput(inAry, offset, len);
        while (true) {
            deflate(out);
            if (!out.isWritable()) {
                out.ensureWritable(out.writerIndex());
            } else if (this.deflater.needsInput()) {
                in.skipBytes(len);
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageToByteEncoder
    public final ByteBuf allocateBuffer(ChannelHandlerContext ctx, ByteBuf msg, boolean preferDirect) throws Exception {
        int sizeEstimate = ((int) Math.ceil(msg.readableBytes() * 1.001d)) + 12;
        if (this.writeHeader) {
            switch (this.wrapper) {
                case GZIP:
                    sizeEstimate += gzipHeader.length;
                    break;
                case ZLIB:
                    sizeEstimate += 2;
                    break;
            }
        }
        if (sizeEstimate < 0 || sizeEstimate > MAX_INITIAL_OUTPUT_BUFFER_SIZE) {
            return ctx.alloc().heapBuffer(MAX_INITIAL_OUTPUT_BUFFER_SIZE);
        }
        return ctx.alloc().heapBuffer(sizeEstimate);
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
        ByteBuf footer = ctx.alloc().heapBuffer();
        if (this.writeHeader && this.wrapper == ZlibWrapper.GZIP) {
            this.writeHeader = false;
            footer.writeBytes(gzipHeader);
        }
        this.deflater.finish();
        while (!this.deflater.finished()) {
            deflate(footer);
            if (!footer.isWritable()) {
                ctx.write(footer);
                footer = ctx.alloc().heapBuffer();
            }
        }
        if (this.wrapper == ZlibWrapper.GZIP) {
            int crcValue = (int) this.crc.getValue();
            int uncBytes = this.deflater.getTotalIn();
            footer.writeByte(crcValue);
            footer.writeByte(crcValue >>> 8);
            footer.writeByte(crcValue >>> 16);
            footer.writeByte(crcValue >>> 24);
            footer.writeByte(uncBytes);
            footer.writeByte(uncBytes >>> 8);
            footer.writeByte(uncBytes >>> 16);
            footer.writeByte(uncBytes >>> 24);
        }
        this.deflater.end();
        return ctx.writeAndFlush(footer, promise);
    }

    private void deflate(ByteBuf out) {
        int numBytes;
        if (PlatformDependent.javaVersion() < 7) {
            deflateJdk6(out);
        }
        do {
            int writerIndex = out.writerIndex();
            numBytes = this.deflater.deflate(out.array(), out.arrayOffset() + writerIndex, out.writableBytes(), 2);
            out.writerIndex(writerIndex + numBytes);
        } while (numBytes > 0);
    }

    private void deflateJdk6(ByteBuf out) {
        int numBytes;
        do {
            int writerIndex = out.writerIndex();
            numBytes = this.deflater.deflate(out.array(), out.arrayOffset() + writerIndex, out.writableBytes());
            out.writerIndex(writerIndex + numBytes);
        } while (numBytes > 0);
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
    }
}
