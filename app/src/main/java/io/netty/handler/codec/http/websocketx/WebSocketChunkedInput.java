package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.stream.ChunkedInput;
import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public final class WebSocketChunkedInput implements ChunkedInput<WebSocketFrame> {
    private final ChunkedInput<ByteBuf> input;
    private final int rsv;

    public WebSocketChunkedInput(ChunkedInput<ByteBuf> input) {
        this(input, 0);
    }

    public WebSocketChunkedInput(ChunkedInput<ByteBuf> input, int rsv) {
        this.input = (ChunkedInput) ObjectUtil.checkNotNull(input, "input");
        this.rsv = rsv;
    }

    @Override // io.netty.handler.stream.ChunkedInput
    public boolean isEndOfInput() throws Exception {
        return this.input.isEndOfInput();
    }

    @Override // io.netty.handler.stream.ChunkedInput
    public void close() throws Exception {
        this.input.close();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.handler.stream.ChunkedInput
    @Deprecated
    public WebSocketFrame readChunk(ChannelHandlerContext ctx) throws Exception {
        return readChunk(ctx.alloc());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.handler.stream.ChunkedInput
    public WebSocketFrame readChunk(ByteBufAllocator allocator) throws Exception {
        ByteBuf buf = this.input.readChunk(allocator);
        if (buf == null) {
            return null;
        }
        return new ContinuationWebSocketFrame(this.input.isEndOfInput(), this.rsv, buf);
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
