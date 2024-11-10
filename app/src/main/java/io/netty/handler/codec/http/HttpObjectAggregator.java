package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.MessageAggregator;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/* loaded from: classes4.dex */
public class HttpObjectAggregator extends MessageAggregator<HttpObject, HttpMessage, HttpContent, FullHttpMessage> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final boolean closeOnExpectationFailed;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) HttpObjectAggregator.class);
    private static final FullHttpResponse CONTINUE = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE, Unpooled.EMPTY_BUFFER);
    private static final FullHttpResponse EXPECTATION_FAILED = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.EXPECTATION_FAILED, Unpooled.EMPTY_BUFFER);
    private static final FullHttpResponse TOO_LARGE_CLOSE = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE, Unpooled.EMPTY_BUFFER);
    private static final FullHttpResponse TOO_LARGE = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE, Unpooled.EMPTY_BUFFER);

    static {
        EXPECTATION_FAILED.headers().set((CharSequence) HttpHeaderNames.CONTENT_LENGTH, (Object) 0);
        TOO_LARGE.headers().set((CharSequence) HttpHeaderNames.CONTENT_LENGTH, (Object) 0);
        TOO_LARGE_CLOSE.headers().set((CharSequence) HttpHeaderNames.CONTENT_LENGTH, (Object) 0);
        TOO_LARGE_CLOSE.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
    }

    public HttpObjectAggregator(int maxContentLength) {
        this(maxContentLength, false);
    }

    public HttpObjectAggregator(int maxContentLength, boolean closeOnExpectationFailed) {
        super(maxContentLength);
        this.closeOnExpectationFailed = closeOnExpectationFailed;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageAggregator
    public boolean isStartMessage(HttpObject msg) throws Exception {
        return msg instanceof HttpMessage;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageAggregator
    public boolean isContentMessage(HttpObject msg) throws Exception {
        return msg instanceof HttpContent;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageAggregator
    public boolean isLastContentMessage(HttpContent msg) throws Exception {
        return msg instanceof LastHttpContent;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageAggregator
    public boolean isAggregated(HttpObject msg) throws Exception {
        return msg instanceof FullHttpMessage;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageAggregator
    public boolean isContentLengthInvalid(HttpMessage start, int maxContentLength) {
        try {
            return HttpUtil.getContentLength(start, -1L) > ((long) maxContentLength);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static Object continueResponse(HttpMessage start, int maxContentLength, ChannelPipeline pipeline) {
        if (HttpUtil.isUnsupportedExpectation(start)) {
            pipeline.fireUserEventTriggered((Object) HttpExpectationFailedEvent.INSTANCE);
            return EXPECTATION_FAILED.retainedDuplicate();
        }
        if (!HttpUtil.is100ContinueExpected(start)) {
            return null;
        }
        if (HttpUtil.getContentLength(start, -1L) <= maxContentLength) {
            return CONTINUE.retainedDuplicate();
        }
        pipeline.fireUserEventTriggered((Object) HttpExpectationFailedEvent.INSTANCE);
        return TOO_LARGE.retainedDuplicate();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageAggregator
    public Object newContinueResponse(HttpMessage start, int maxContentLength, ChannelPipeline pipeline) {
        Object response = continueResponse(start, maxContentLength, pipeline);
        if (response != null) {
            start.headers().remove(HttpHeaderNames.EXPECT);
        }
        return response;
    }

    @Override // io.netty.handler.codec.MessageAggregator
    protected boolean closeAfterContinueResponse(Object msg) {
        return this.closeOnExpectationFailed && ignoreContentAfterContinueResponse(msg);
    }

    @Override // io.netty.handler.codec.MessageAggregator
    protected boolean ignoreContentAfterContinueResponse(Object msg) {
        if (msg instanceof HttpResponse) {
            HttpResponse httpResponse = (HttpResponse) msg;
            return httpResponse.status().codeClass().equals(HttpStatusClass.CLIENT_ERROR);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageAggregator
    public FullHttpMessage beginAggregation(HttpMessage start, ByteBuf content) throws Exception {
        if (start instanceof FullHttpMessage) {
            throw new AssertionError();
        }
        HttpUtil.setTransferEncodingChunked(start, false);
        if (start instanceof HttpRequest) {
            AggregatedFullHttpMessage ret = new AggregatedFullHttpRequest((HttpRequest) start, content, null);
            return ret;
        }
        if (start instanceof HttpResponse) {
            AggregatedFullHttpMessage ret2 = new AggregatedFullHttpResponse((HttpResponse) start, content, null);
            return ret2;
        }
        throw new Error();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageAggregator
    public void aggregate(FullHttpMessage aggregated, HttpContent content) throws Exception {
        if (content instanceof LastHttpContent) {
            ((AggregatedFullHttpMessage) aggregated).setTrailingHeaders(((LastHttpContent) content).trailingHeaders());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageAggregator
    public void finishAggregation(FullHttpMessage aggregated) throws Exception {
        if (!HttpUtil.isContentLengthSet(aggregated)) {
            aggregated.headers().set(HttpHeaderNames.CONTENT_LENGTH, String.valueOf(aggregated.content().readableBytes()));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageAggregator
    public void handleOversizedMessage(final ChannelHandlerContext ctx, HttpMessage oversized) throws Exception {
        if (oversized instanceof HttpRequest) {
            if ((oversized instanceof FullHttpMessage) || (!HttpUtil.is100ContinueExpected(oversized) && !HttpUtil.isKeepAlive(oversized))) {
                ChannelFuture future = ctx.writeAndFlush(TOO_LARGE_CLOSE.retainedDuplicate());
                future.addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.handler.codec.http.HttpObjectAggregator.1
                    @Override // io.netty.util.concurrent.GenericFutureListener
                    public void operationComplete(ChannelFuture future2) throws Exception {
                        if (!future2.isSuccess()) {
                            HttpObjectAggregator.logger.debug("Failed to send a 413 Request Entity Too Large.", future2.cause());
                        }
                        ctx.close();
                    }
                });
                return;
            } else {
                ctx.writeAndFlush(TOO_LARGE.retainedDuplicate()).addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.handler.codec.http.HttpObjectAggregator.2
                    @Override // io.netty.util.concurrent.GenericFutureListener
                    public void operationComplete(ChannelFuture future2) throws Exception {
                        if (!future2.isSuccess()) {
                            HttpObjectAggregator.logger.debug("Failed to send a 413 Request Entity Too Large.", future2.cause());
                            ctx.close();
                        }
                    }
                });
                return;
            }
        }
        if (oversized instanceof HttpResponse) {
            ctx.close();
            throw new TooLongHttpContentException("Response entity too large: " + oversized);
        }
        throw new IllegalStateException();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static abstract class AggregatedFullHttpMessage implements FullHttpMessage {
        private final ByteBuf content;
        protected final HttpMessage message;
        private HttpHeaders trailingHeaders;

        @Override // io.netty.handler.codec.http.LastHttpContent, io.netty.handler.codec.http.HttpContent, io.netty.buffer.ByteBufHolder
        public abstract FullHttpMessage copy();

        @Override // io.netty.handler.codec.http.LastHttpContent, io.netty.handler.codec.http.HttpContent, io.netty.buffer.ByteBufHolder
        public abstract FullHttpMessage duplicate();

        @Override // io.netty.handler.codec.http.LastHttpContent, io.netty.handler.codec.http.HttpContent, io.netty.buffer.ByteBufHolder
        public abstract FullHttpMessage retainedDuplicate();

        AggregatedFullHttpMessage(HttpMessage message, ByteBuf content, HttpHeaders trailingHeaders) {
            this.message = message;
            this.content = content;
            this.trailingHeaders = trailingHeaders;
        }

        @Override // io.netty.handler.codec.http.LastHttpContent
        public HttpHeaders trailingHeaders() {
            HttpHeaders trailingHeaders = this.trailingHeaders;
            if (trailingHeaders == null) {
                return EmptyHttpHeaders.INSTANCE;
            }
            return trailingHeaders;
        }

        void setTrailingHeaders(HttpHeaders trailingHeaders) {
            this.trailingHeaders = trailingHeaders;
        }

        @Override // io.netty.handler.codec.http.HttpMessage
        public HttpVersion getProtocolVersion() {
            return this.message.protocolVersion();
        }

        @Override // io.netty.handler.codec.http.HttpMessage
        public HttpVersion protocolVersion() {
            return this.message.protocolVersion();
        }

        @Override // io.netty.handler.codec.http.HttpMessage, io.netty.handler.codec.http.HttpRequest, io.netty.handler.codec.http.FullHttpRequest
        public FullHttpMessage setProtocolVersion(HttpVersion version) {
            this.message.setProtocolVersion(version);
            return this;
        }

        @Override // io.netty.handler.codec.http.HttpMessage
        public HttpHeaders headers() {
            return this.message.headers();
        }

        @Override // io.netty.handler.codec.DecoderResultProvider
        public DecoderResult decoderResult() {
            return this.message.decoderResult();
        }

        @Override // io.netty.handler.codec.http.HttpObject
        public DecoderResult getDecoderResult() {
            return this.message.decoderResult();
        }

        @Override // io.netty.handler.codec.DecoderResultProvider
        public void setDecoderResult(DecoderResult result) {
            this.message.setDecoderResult(result);
        }

        @Override // io.netty.buffer.ByteBufHolder
        public ByteBuf content() {
            return this.content;
        }

        @Override // io.netty.util.ReferenceCounted
        public int refCnt() {
            return this.content.refCnt();
        }

        @Override // io.netty.util.ReferenceCounted
        public FullHttpMessage retain() {
            this.content.retain();
            return this;
        }

        @Override // io.netty.util.ReferenceCounted
        public FullHttpMessage retain(int increment) {
            this.content.retain(increment);
            return this;
        }

        @Override // io.netty.util.ReferenceCounted
        public FullHttpMessage touch(Object hint) {
            this.content.touch(hint);
            return this;
        }

        @Override // io.netty.util.ReferenceCounted
        public FullHttpMessage touch() {
            this.content.touch();
            return this;
        }

        @Override // io.netty.util.ReferenceCounted
        public boolean release() {
            return this.content.release();
        }

        @Override // io.netty.util.ReferenceCounted
        public boolean release(int decrement) {
            return this.content.release(decrement);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class AggregatedFullHttpRequest extends AggregatedFullHttpMessage implements FullHttpRequest {
        AggregatedFullHttpRequest(HttpRequest request, ByteBuf content, HttpHeaders trailingHeaders) {
            super(request, content, trailingHeaders);
        }

        @Override // io.netty.handler.codec.http.HttpObjectAggregator.AggregatedFullHttpMessage, io.netty.handler.codec.http.LastHttpContent, io.netty.handler.codec.http.HttpContent, io.netty.buffer.ByteBufHolder
        public FullHttpRequest copy() {
            return replace(content().copy());
        }

        @Override // io.netty.handler.codec.http.HttpObjectAggregator.AggregatedFullHttpMessage, io.netty.handler.codec.http.LastHttpContent, io.netty.handler.codec.http.HttpContent, io.netty.buffer.ByteBufHolder
        public FullHttpRequest duplicate() {
            return replace(content().duplicate());
        }

        @Override // io.netty.handler.codec.http.HttpObjectAggregator.AggregatedFullHttpMessage, io.netty.handler.codec.http.LastHttpContent, io.netty.handler.codec.http.HttpContent, io.netty.buffer.ByteBufHolder
        public FullHttpRequest retainedDuplicate() {
            return replace(content().retainedDuplicate());
        }

        @Override // io.netty.handler.codec.http.LastHttpContent, io.netty.handler.codec.http.HttpContent, io.netty.buffer.ByteBufHolder
        public FullHttpRequest replace(ByteBuf content) {
            DefaultFullHttpRequest dup = new DefaultFullHttpRequest(protocolVersion(), method(), uri(), content, headers().copy(), trailingHeaders().copy());
            dup.setDecoderResult(decoderResult());
            return dup;
        }

        @Override // io.netty.handler.codec.http.HttpObjectAggregator.AggregatedFullHttpMessage, io.netty.util.ReferenceCounted
        public FullHttpRequest retain(int increment) {
            super.retain(increment);
            return this;
        }

        @Override // io.netty.handler.codec.http.HttpObjectAggregator.AggregatedFullHttpMessage, io.netty.util.ReferenceCounted
        public FullHttpRequest retain() {
            super.retain();
            return this;
        }

        @Override // io.netty.handler.codec.http.HttpObjectAggregator.AggregatedFullHttpMessage, io.netty.util.ReferenceCounted
        public FullHttpRequest touch() {
            super.touch();
            return this;
        }

        @Override // io.netty.handler.codec.http.HttpObjectAggregator.AggregatedFullHttpMessage, io.netty.util.ReferenceCounted
        public FullHttpRequest touch(Object hint) {
            super.touch(hint);
            return this;
        }

        @Override // io.netty.handler.codec.http.HttpRequest, io.netty.handler.codec.http.FullHttpRequest
        public FullHttpRequest setMethod(HttpMethod method) {
            ((HttpRequest) this.message).setMethod(method);
            return this;
        }

        @Override // io.netty.handler.codec.http.HttpRequest, io.netty.handler.codec.http.FullHttpRequest
        public FullHttpRequest setUri(String uri) {
            ((HttpRequest) this.message).setUri(uri);
            return this;
        }

        @Override // io.netty.handler.codec.http.HttpRequest
        public HttpMethod getMethod() {
            return ((HttpRequest) this.message).method();
        }

        @Override // io.netty.handler.codec.http.HttpRequest
        public String getUri() {
            return ((HttpRequest) this.message).uri();
        }

        @Override // io.netty.handler.codec.http.HttpRequest
        public HttpMethod method() {
            return getMethod();
        }

        @Override // io.netty.handler.codec.http.HttpRequest
        public String uri() {
            return getUri();
        }

        @Override // io.netty.handler.codec.http.HttpObjectAggregator.AggregatedFullHttpMessage, io.netty.handler.codec.http.HttpMessage, io.netty.handler.codec.http.HttpRequest, io.netty.handler.codec.http.FullHttpRequest
        public FullHttpRequest setProtocolVersion(HttpVersion version) {
            super.setProtocolVersion(version);
            return this;
        }

        public String toString() {
            return HttpMessageUtil.appendFullRequest(new StringBuilder(256), this).toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class AggregatedFullHttpResponse extends AggregatedFullHttpMessage implements FullHttpResponse {
        AggregatedFullHttpResponse(HttpResponse message, ByteBuf content, HttpHeaders trailingHeaders) {
            super(message, content, trailingHeaders);
        }

        @Override // io.netty.handler.codec.http.HttpObjectAggregator.AggregatedFullHttpMessage, io.netty.handler.codec.http.LastHttpContent, io.netty.handler.codec.http.HttpContent, io.netty.buffer.ByteBufHolder
        public FullHttpResponse copy() {
            return replace(content().copy());
        }

        @Override // io.netty.handler.codec.http.HttpObjectAggregator.AggregatedFullHttpMessage, io.netty.handler.codec.http.LastHttpContent, io.netty.handler.codec.http.HttpContent, io.netty.buffer.ByteBufHolder
        public FullHttpResponse duplicate() {
            return replace(content().duplicate());
        }

        @Override // io.netty.handler.codec.http.HttpObjectAggregator.AggregatedFullHttpMessage, io.netty.handler.codec.http.LastHttpContent, io.netty.handler.codec.http.HttpContent, io.netty.buffer.ByteBufHolder
        public FullHttpResponse retainedDuplicate() {
            return replace(content().retainedDuplicate());
        }

        @Override // io.netty.handler.codec.http.LastHttpContent, io.netty.handler.codec.http.HttpContent, io.netty.buffer.ByteBufHolder
        public FullHttpResponse replace(ByteBuf content) {
            DefaultFullHttpResponse dup = new DefaultFullHttpResponse(getProtocolVersion(), getStatus(), content, headers().copy(), trailingHeaders().copy());
            dup.setDecoderResult(decoderResult());
            return dup;
        }

        @Override // io.netty.handler.codec.http.HttpResponse, io.netty.handler.codec.http.FullHttpResponse
        public FullHttpResponse setStatus(HttpResponseStatus status) {
            ((HttpResponse) this.message).setStatus(status);
            return this;
        }

        @Override // io.netty.handler.codec.http.HttpResponse
        public HttpResponseStatus getStatus() {
            return ((HttpResponse) this.message).status();
        }

        @Override // io.netty.handler.codec.http.HttpResponse
        public HttpResponseStatus status() {
            return getStatus();
        }

        @Override // io.netty.handler.codec.http.HttpObjectAggregator.AggregatedFullHttpMessage, io.netty.handler.codec.http.HttpMessage, io.netty.handler.codec.http.HttpRequest, io.netty.handler.codec.http.FullHttpRequest
        public FullHttpResponse setProtocolVersion(HttpVersion version) {
            super.setProtocolVersion(version);
            return this;
        }

        @Override // io.netty.handler.codec.http.HttpObjectAggregator.AggregatedFullHttpMessage, io.netty.util.ReferenceCounted
        public FullHttpResponse retain(int increment) {
            super.retain(increment);
            return this;
        }

        @Override // io.netty.handler.codec.http.HttpObjectAggregator.AggregatedFullHttpMessage, io.netty.util.ReferenceCounted
        public FullHttpResponse retain() {
            super.retain();
            return this;
        }

        @Override // io.netty.handler.codec.http.HttpObjectAggregator.AggregatedFullHttpMessage, io.netty.util.ReferenceCounted
        public FullHttpResponse touch(Object hint) {
            super.touch(hint);
            return this;
        }

        @Override // io.netty.handler.codec.http.HttpObjectAggregator.AggregatedFullHttpMessage, io.netty.util.ReferenceCounted
        public FullHttpResponse touch() {
            super.touch();
            return this;
        }

        public String toString() {
            return HttpMessageUtil.appendFullResponse(new StringBuilder(256), this).toString();
        }
    }
}
