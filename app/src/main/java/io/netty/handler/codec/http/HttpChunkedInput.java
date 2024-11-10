package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.stream.ChunkedInput;

/* loaded from: classes4.dex */
public class HttpChunkedInput implements ChunkedInput<HttpContent> {
    private final ChunkedInput<ByteBuf> input;
    private final LastHttpContent lastHttpContent;
    private boolean sentLastChunk;

    public HttpChunkedInput(ChunkedInput<ByteBuf> input) {
        this.input = input;
        this.lastHttpContent = LastHttpContent.EMPTY_LAST_CONTENT;
    }

    public HttpChunkedInput(ChunkedInput<ByteBuf> input, LastHttpContent lastHttpContent) {
        this.input = input;
        this.lastHttpContent = lastHttpContent;
    }

    @Override // io.netty.handler.stream.ChunkedInput
    public boolean isEndOfInput() throws Exception {
        if (this.input.isEndOfInput()) {
            return this.sentLastChunk;
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
    public HttpContent readChunk(ChannelHandlerContext ctx) throws Exception {
        return readChunk(ctx.alloc());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.handler.stream.ChunkedInput
    public HttpContent readChunk(ByteBufAllocator allocator) throws Exception {
        if (this.input.isEndOfInput()) {
            if (this.sentLastChunk) {
                return null;
            }
            this.sentLastChunk = true;
            return this.lastHttpContent;
        }
        ByteBuf buf = this.input.readChunk(allocator);
        if (buf == null) {
            return null;
        }
        return new DefaultHttpContent(buf);
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
