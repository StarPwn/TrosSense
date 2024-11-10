package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.EmptyHttpHeaders;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpScheme;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http2.Http2CodecUtil;
import io.netty.handler.codec.http2.HttpConversionUtil;
import io.netty.util.ReferenceCountUtil;

/* loaded from: classes4.dex */
public class HttpToHttp2ConnectionHandler extends Http2ConnectionHandler {
    private int currentStreamId;
    private HttpScheme httpScheme;
    private final boolean validateHeaders;

    protected HttpToHttp2ConnectionHandler(Http2ConnectionDecoder decoder, Http2ConnectionEncoder encoder, Http2Settings initialSettings, boolean validateHeaders) {
        super(decoder, encoder, initialSettings);
        this.validateHeaders = validateHeaders;
    }

    protected HttpToHttp2ConnectionHandler(Http2ConnectionDecoder decoder, Http2ConnectionEncoder encoder, Http2Settings initialSettings, boolean validateHeaders, boolean decoupleCloseAndGoAway) {
        this(decoder, encoder, initialSettings, validateHeaders, decoupleCloseAndGoAway, null);
    }

    protected HttpToHttp2ConnectionHandler(Http2ConnectionDecoder decoder, Http2ConnectionEncoder encoder, Http2Settings initialSettings, boolean validateHeaders, boolean decoupleCloseAndGoAway, HttpScheme httpScheme) {
        super(decoder, encoder, initialSettings, decoupleCloseAndGoAway);
        this.validateHeaders = validateHeaders;
        this.httpScheme = httpScheme;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public HttpToHttp2ConnectionHandler(Http2ConnectionDecoder decoder, Http2ConnectionEncoder encoder, Http2Settings initialSettings, boolean validateHeaders, boolean decoupleCloseAndGoAway, boolean flushPreface, HttpScheme httpScheme) {
        super(decoder, encoder, initialSettings, decoupleCloseAndGoAway, flushPreface);
        this.validateHeaders = validateHeaders;
        this.httpScheme = httpScheme;
    }

    private int getStreamId(HttpHeaders httpHeaders) throws Exception {
        return httpHeaders.getInt(HttpConversionUtil.ExtensionHeaderNames.STREAM_ID.text(), connection().local().incrementAndGetNextStreamId());
    }

    @Override // io.netty.handler.codec.http2.Http2ConnectionHandler, io.netty.channel.ChannelOutboundHandler
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        HttpHeaders trailers;
        Http2Headers http2Trailers;
        if (!(msg instanceof HttpMessage) && !(msg instanceof HttpContent)) {
            ctx.write(msg, promise);
            return;
        }
        boolean release = true;
        Http2CodecUtil.SimpleChannelPromiseAggregator promiseAggregator = new Http2CodecUtil.SimpleChannelPromiseAggregator(promise, ctx.channel(), ctx.executor());
        try {
            Http2ConnectionEncoder encoder = encoder();
            boolean endStream = false;
            if (msg instanceof HttpMessage) {
                HttpMessage httpMsg = (HttpMessage) msg;
                this.currentStreamId = getStreamId(httpMsg.headers());
                if (this.httpScheme != null && !httpMsg.headers().contains(HttpConversionUtil.ExtensionHeaderNames.SCHEME.text())) {
                    httpMsg.headers().set(HttpConversionUtil.ExtensionHeaderNames.SCHEME.text(), this.httpScheme.name());
                }
                Http2Headers http2Headers = HttpConversionUtil.toHttp2Headers(httpMsg, this.validateHeaders);
                boolean endStream2 = (msg instanceof FullHttpMessage) && !((FullHttpMessage) msg).content().isReadable();
                writeHeaders(ctx, encoder, this.currentStreamId, httpMsg.headers(), http2Headers, endStream2, promiseAggregator);
                endStream = endStream2;
            }
            if (!endStream && (msg instanceof HttpContent)) {
                boolean isLastContent = false;
                HttpHeaders trailers2 = EmptyHttpHeaders.INSTANCE;
                Http2Headers http2Trailers2 = EmptyHttp2Headers.INSTANCE;
                if (!(msg instanceof LastHttpContent)) {
                    trailers = trailers2;
                    http2Trailers = http2Trailers2;
                } else {
                    isLastContent = true;
                    LastHttpContent lastContent = (LastHttpContent) msg;
                    HttpHeaders trailers3 = lastContent.trailingHeaders();
                    Http2Headers http2Trailers3 = HttpConversionUtil.toHttp2Headers(trailers3, this.validateHeaders);
                    trailers = trailers3;
                    http2Trailers = http2Trailers3;
                }
                ByteBuf content = ((HttpContent) msg).content();
                boolean endStream3 = isLastContent && trailers.isEmpty();
                encoder.writeData(ctx, this.currentStreamId, content, 0, endStream3, promiseAggregator.newPromise());
                release = false;
                if (!trailers.isEmpty()) {
                    writeHeaders(ctx, encoder, this.currentStreamId, trailers, http2Trailers, true, promiseAggregator);
                }
            }
            if (release) {
                ReferenceCountUtil.release(msg);
            }
            promiseAggregator.doneAllocatingPromises();
        } catch (Throwable t) {
            try {
                onError(ctx, true, t);
                promiseAggregator.setFailure(t);
            } finally {
                if (release) {
                    ReferenceCountUtil.release(msg);
                }
                promiseAggregator.doneAllocatingPromises();
            }
        }
    }

    private static void writeHeaders(ChannelHandlerContext ctx, Http2ConnectionEncoder encoder, int streamId, HttpHeaders headers, Http2Headers http2Headers, boolean endStream, Http2CodecUtil.SimpleChannelPromiseAggregator promiseAggregator) {
        int dependencyId = headers.getInt(HttpConversionUtil.ExtensionHeaderNames.STREAM_DEPENDENCY_ID.text(), 0);
        short weight = headers.getShort(HttpConversionUtil.ExtensionHeaderNames.STREAM_WEIGHT.text(), (short) 16);
        encoder.writeHeaders(ctx, streamId, http2Headers, dependencyId, weight, false, 0, endStream, promiseAggregator.newPromise());
    }
}
