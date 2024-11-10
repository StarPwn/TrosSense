package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DecoderResult;

/* loaded from: classes4.dex */
final class ComposedLastHttpContent implements LastHttpContent {
    private DecoderResult result;
    private final HttpHeaders trailingHeaders;

    ComposedLastHttpContent(HttpHeaders trailingHeaders) {
        this.trailingHeaders = trailingHeaders;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ComposedLastHttpContent(HttpHeaders trailingHeaders, DecoderResult result) {
        this(trailingHeaders);
        this.result = result;
    }

    @Override // io.netty.handler.codec.http.LastHttpContent
    public HttpHeaders trailingHeaders() {
        return this.trailingHeaders;
    }

    @Override // io.netty.handler.codec.http.HttpContent, io.netty.buffer.ByteBufHolder
    public LastHttpContent copy() {
        LastHttpContent content = new DefaultLastHttpContent(Unpooled.EMPTY_BUFFER);
        content.trailingHeaders().set(trailingHeaders());
        return content;
    }

    @Override // io.netty.handler.codec.http.HttpContent, io.netty.buffer.ByteBufHolder
    public LastHttpContent duplicate() {
        return copy();
    }

    @Override // io.netty.handler.codec.http.HttpContent, io.netty.buffer.ByteBufHolder
    public LastHttpContent retainedDuplicate() {
        return copy();
    }

    @Override // io.netty.handler.codec.http.HttpContent, io.netty.buffer.ByteBufHolder
    public LastHttpContent replace(ByteBuf content) {
        LastHttpContent dup = new DefaultLastHttpContent(content);
        dup.trailingHeaders().setAll(trailingHeaders());
        return dup;
    }

    @Override // io.netty.util.ReferenceCounted
    public LastHttpContent retain(int increment) {
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public LastHttpContent retain() {
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public LastHttpContent touch() {
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public LastHttpContent touch(Object hint) {
        return this;
    }

    @Override // io.netty.buffer.ByteBufHolder
    public ByteBuf content() {
        return Unpooled.EMPTY_BUFFER;
    }

    @Override // io.netty.handler.codec.DecoderResultProvider
    public DecoderResult decoderResult() {
        return this.result;
    }

    @Override // io.netty.handler.codec.http.HttpObject
    public DecoderResult getDecoderResult() {
        return decoderResult();
    }

    @Override // io.netty.handler.codec.DecoderResultProvider
    public void setDecoderResult(DecoderResult result) {
        this.result = result;
    }

    @Override // io.netty.util.ReferenceCounted
    public int refCnt() {
        return 1;
    }

    @Override // io.netty.util.ReferenceCounted
    public boolean release() {
        return false;
    }

    @Override // io.netty.util.ReferenceCounted
    public boolean release(int decrement) {
        return false;
    }
}
