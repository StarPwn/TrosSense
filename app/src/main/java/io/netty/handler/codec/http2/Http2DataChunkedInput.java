package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.stream.ChunkedInput;
import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public final class Http2DataChunkedInput implements ChunkedInput<Http2DataFrame> {
    private boolean endStreamSent;
    private final ChunkedInput<ByteBuf> input;
    private final Http2FrameStream stream;

    public Http2DataChunkedInput(ChunkedInput<ByteBuf> input, Http2FrameStream stream) {
        this.input = (ChunkedInput) ObjectUtil.checkNotNull(input, "input");
        this.stream = (Http2FrameStream) ObjectUtil.checkNotNull(stream, "stream");
    }

    @Override // io.netty.handler.stream.ChunkedInput
    public boolean isEndOfInput() throws Exception {
        if (this.input.isEndOfInput()) {
            return this.endStreamSent;
        }
        return false;
    }

    @Override // io.netty.handler.stream.ChunkedInput
    public void close() throws Exception {
        this.input.close();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.handler.stream.ChunkedInput
    @Deprecated
    public Http2DataFrame readChunk(ChannelHandlerContext ctx) throws Exception {
        return readChunk(ctx.alloc());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.handler.stream.ChunkedInput
    public Http2DataFrame readChunk(ByteBufAllocator allocator) throws Exception {
        if (this.endStreamSent) {
            return null;
        }
        if (this.input.isEndOfInput()) {
            this.endStreamSent = true;
            return new DefaultHttp2DataFrame(true).stream(this.stream);
        }
        ByteBuf buf = this.input.readChunk(allocator);
        if (buf == null) {
            return null;
        }
        Http2DataFrame dataFrame = new DefaultHttp2DataFrame(buf, this.input.isEndOfInput()).stream(this.stream);
        if (dataFrame.isEndStream()) {
            this.endStreamSent = true;
        }
        return dataFrame;
    }

    @Override // io.netty.handler.stream.ChunkedInput
    public long length() {
        return this.input.length();
    }

    @Override // io.netty.handler.stream.ChunkedInput
    public long progress() {
        return this.input.progress();
    }
}
