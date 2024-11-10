package io.netty.handler.codec.compression;

import com.aayushatharva.brotli4j.decoder.DecoderJNI;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.internal.ObjectUtil;
import java.nio.ByteBuffer;
import java.util.List;

/* loaded from: classes4.dex */
public final class BrotliDecoder extends ByteToMessageDecoder {
    private DecoderJNI.Wrapper decoder;
    private boolean destroyed;
    private final int inputBufferSize;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public enum State {
        DONE,
        NEEDS_MORE_INPUT,
        ERROR
    }

    static {
        try {
            Brotli.ensureAvailability();
        } catch (Throwable throwable) {
            throw new ExceptionInInitializerError(throwable);
        }
    }

    public BrotliDecoder() {
        this(8192);
    }

    public BrotliDecoder(int inputBufferSize) {
        this.inputBufferSize = ObjectUtil.checkPositive(inputBufferSize, "inputBufferSize");
    }

    private ByteBuf pull(ByteBufAllocator alloc) {
        ByteBuffer nativeBuffer = this.decoder.pull();
        ByteBuf copy = alloc.buffer(nativeBuffer.remaining());
        copy.writeBytes(nativeBuffer);
        return copy;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: io.netty.handler.codec.compression.BrotliDecoder$1, reason: invalid class name */
    /* loaded from: classes4.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$aayushatharva$brotli4j$decoder$DecoderJNI$Status = new int[DecoderJNI.Status.values().length];

        static {
            try {
                $SwitchMap$com$aayushatharva$brotli4j$decoder$DecoderJNI$Status[DecoderJNI.Status.DONE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$aayushatharva$brotli4j$decoder$DecoderJNI$Status[DecoderJNI.Status.OK.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$aayushatharva$brotli4j$decoder$DecoderJNI$Status[DecoderJNI.Status.NEEDS_MORE_INPUT.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$aayushatharva$brotli4j$decoder$DecoderJNI$Status[DecoderJNI.Status.NEEDS_MORE_OUTPUT.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    private State decompress(ByteBuf input, List<Object> output, ByteBufAllocator alloc) {
        while (true) {
            switch (AnonymousClass1.$SwitchMap$com$aayushatharva$brotli4j$decoder$DecoderJNI$Status[this.decoder.getStatus().ordinal()]) {
                case 1:
                    return State.DONE;
                case 2:
                    this.decoder.push(0);
                    break;
                case 3:
                    if (this.decoder.hasOutput()) {
                        output.add(pull(alloc));
                    }
                    if (!input.isReadable()) {
                        return State.NEEDS_MORE_INPUT;
                    }
                    ByteBuffer decoderInputBuffer = this.decoder.getInputBuffer();
                    decoderInputBuffer.clear();
                    int readBytes = readBytes(input, decoderInputBuffer);
                    this.decoder.push(readBytes);
                    break;
                case 4:
                    output.add(pull(alloc));
                    break;
                default:
                    return State.ERROR;
            }
        }
    }

    private static int readBytes(ByteBuf in, ByteBuffer dest) {
        int limit = Math.min(in.readableBytes(), dest.remaining());
        ByteBuffer slice = dest.slice();
        slice.limit(limit);
        in.readBytes(slice);
        dest.position(dest.position() + limit);
        return limit;
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        this.decoder = new DecoderJNI.Wrapper(this.inputBufferSize);
    }

    @Override // io.netty.handler.codec.ByteToMessageDecoder
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (this.destroyed) {
            in.skipBytes(in.readableBytes());
            return;
        }
        if (!in.isReadable()) {
            return;
        }
        try {
            State state = decompress(in, out, ctx.alloc());
            if (state == State.DONE) {
                destroy();
            } else if (state == State.ERROR) {
                throw new DecompressionException("Brotli stream corrupted");
            }
        } catch (Exception e) {
            destroy();
            throw e;
        }
    }

    private void destroy() {
        if (!this.destroyed) {
            this.destroyed = true;
            this.decoder.destroy();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.ByteToMessageDecoder
    public void handlerRemoved0(ChannelHandlerContext ctx) throws Exception {
        try {
            destroy();
        } finally {
            super.handlerRemoved0(ctx);
        }
    }

    @Override // io.netty.handler.codec.ByteToMessageDecoder, io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        try {
            destroy();
        } finally {
            super.channelInactive(ctx);
        }
    }
}
