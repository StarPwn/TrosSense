package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpStatusClass;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http2.Http2Connection;
import io.netty.handler.codec.http2.HttpConversionUtil;
import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public class InboundHttp2ToHttpAdapter extends Http2EventAdapter {
    private static final ImmediateSendDetector DEFAULT_SEND_DETECTOR = new ImmediateSendDetector() { // from class: io.netty.handler.codec.http2.InboundHttp2ToHttpAdapter.1
        @Override // io.netty.handler.codec.http2.InboundHttp2ToHttpAdapter.ImmediateSendDetector
        public boolean mustSendImmediately(FullHttpMessage msg) {
            if (msg instanceof FullHttpResponse) {
                return ((FullHttpResponse) msg).status().codeClass() == HttpStatusClass.INFORMATIONAL;
            }
            if (msg instanceof FullHttpRequest) {
                return msg.headers().contains(HttpHeaderNames.EXPECT);
            }
            return false;
        }

        @Override // io.netty.handler.codec.http2.InboundHttp2ToHttpAdapter.ImmediateSendDetector
        public FullHttpMessage copyIfNeeded(ByteBufAllocator allocator, FullHttpMessage msg) {
            if (msg instanceof FullHttpRequest) {
                FullHttpRequest copy = ((FullHttpRequest) msg).replace(allocator.buffer(0));
                copy.headers().remove(HttpHeaderNames.EXPECT);
                return copy;
            }
            return null;
        }
    };
    protected final Http2Connection connection;
    private final int maxContentLength;
    private final Http2Connection.PropertyKey messageKey;
    private final boolean propagateSettings;
    private final ImmediateSendDetector sendDetector = DEFAULT_SEND_DETECTOR;
    protected final boolean validateHttpHeaders;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public interface ImmediateSendDetector {
        FullHttpMessage copyIfNeeded(ByteBufAllocator byteBufAllocator, FullHttpMessage fullHttpMessage);

        boolean mustSendImmediately(FullHttpMessage fullHttpMessage);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public InboundHttp2ToHttpAdapter(Http2Connection connection, int maxContentLength, boolean validateHttpHeaders, boolean propagateSettings) {
        this.connection = (Http2Connection) ObjectUtil.checkNotNull(connection, "connection");
        this.maxContentLength = ObjectUtil.checkPositive(maxContentLength, "maxContentLength");
        this.validateHttpHeaders = validateHttpHeaders;
        this.propagateSettings = propagateSettings;
        this.messageKey = connection.newKey();
    }

    protected final void removeMessage(Http2Stream stream, boolean release) {
        FullHttpMessage msg = (FullHttpMessage) stream.removeProperty(this.messageKey);
        if (release && msg != null) {
            msg.release();
        }
    }

    protected final FullHttpMessage getMessage(Http2Stream stream) {
        return (FullHttpMessage) stream.getProperty(this.messageKey);
    }

    protected final void putMessage(Http2Stream stream, FullHttpMessage message) {
        FullHttpMessage previous = (FullHttpMessage) stream.setProperty(this.messageKey, message);
        if (previous != message && previous != null) {
            previous.release();
        }
    }

    @Override // io.netty.handler.codec.http2.Http2EventAdapter, io.netty.handler.codec.http2.Http2Connection.Listener
    public void onStreamRemoved(Http2Stream stream) {
        removeMessage(stream, true);
    }

    protected void fireChannelRead(ChannelHandlerContext ctx, FullHttpMessage msg, boolean release, Http2Stream stream) {
        removeMessage(stream, release);
        HttpUtil.setContentLength(msg, msg.content().readableBytes());
        ctx.fireChannelRead((Object) msg);
    }

    protected FullHttpMessage newMessage(Http2Stream stream, Http2Headers headers, boolean validateHttpHeaders, ByteBufAllocator alloc) throws Http2Exception {
        return this.connection.isServer() ? HttpConversionUtil.toFullHttpRequest(stream.id(), headers, alloc, validateHttpHeaders) : HttpConversionUtil.toFullHttpResponse(stream.id(), headers, alloc, validateHttpHeaders);
    }

    protected FullHttpMessage processHeadersBegin(ChannelHandlerContext ctx, Http2Stream stream, Http2Headers headers, boolean endOfStream, boolean allowAppend, boolean appendToTrailer) throws Http2Exception {
        FullHttpMessage msg = getMessage(stream);
        boolean release = true;
        if (msg == null) {
            msg = newMessage(stream, headers, this.validateHttpHeaders, ctx.alloc());
        } else if (allowAppend) {
            release = false;
            HttpConversionUtil.addHttp2ToHttpHeaders(stream.id(), headers, msg, appendToTrailer);
        } else {
            release = false;
            msg = null;
        }
        if (this.sendDetector.mustSendImmediately(msg)) {
            FullHttpMessage copy = endOfStream ? null : this.sendDetector.copyIfNeeded(ctx.alloc(), msg);
            fireChannelRead(ctx, msg, release, stream);
            return copy;
        }
        return msg;
    }

    private void processHeadersEnd(ChannelHandlerContext ctx, Http2Stream stream, FullHttpMessage msg, boolean endOfStream) {
        if (endOfStream) {
            fireChannelRead(ctx, msg, getMessage(stream) != msg, stream);
        } else {
            putMessage(stream, msg);
        }
    }

    @Override // io.netty.handler.codec.http2.Http2EventAdapter, io.netty.handler.codec.http2.Http2FrameListener
    public int onDataRead(ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding, boolean endOfStream) throws Http2Exception {
        Http2Stream stream = this.connection.stream(streamId);
        FullHttpMessage msg = getMessage(stream);
        if (msg == null) {
            throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Data Frame received for unknown stream id %d", Integer.valueOf(streamId));
        }
        ByteBuf content = msg.content();
        int dataReadableBytes = data.readableBytes();
        if (content.readableBytes() > this.maxContentLength - dataReadableBytes) {
            throw Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, "Content length exceeded max of %d for stream id %d", Integer.valueOf(this.maxContentLength), Integer.valueOf(streamId));
        }
        content.writeBytes(data, data.readerIndex(), dataReadableBytes);
        if (endOfStream) {
            fireChannelRead(ctx, msg, false, stream);
        }
        return dataReadableBytes + padding;
    }

    @Override // io.netty.handler.codec.http2.Http2EventAdapter, io.netty.handler.codec.http2.Http2FrameListener
    public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding, boolean endOfStream) throws Http2Exception {
        Http2Stream stream = this.connection.stream(streamId);
        FullHttpMessage msg = processHeadersBegin(ctx, stream, headers, endOfStream, true, true);
        if (msg != null) {
            processHeadersEnd(ctx, stream, msg, endOfStream);
        }
    }

    @Override // io.netty.handler.codec.http2.Http2EventAdapter, io.netty.handler.codec.http2.Http2FrameListener
    public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endOfStream) throws Http2Exception {
        Http2Stream stream = this.connection.stream(streamId);
        FullHttpMessage msg = processHeadersBegin(ctx, stream, headers, endOfStream, true, true);
        if (msg != null) {
            if (streamDependency != 0) {
                msg.headers().setInt(HttpConversionUtil.ExtensionHeaderNames.STREAM_DEPENDENCY_ID.text(), streamDependency);
            }
            msg.headers().setShort(HttpConversionUtil.ExtensionHeaderNames.STREAM_WEIGHT.text(), weight);
            processHeadersEnd(ctx, stream, msg, endOfStream);
        }
    }

    @Override // io.netty.handler.codec.http2.Http2EventAdapter, io.netty.handler.codec.http2.Http2FrameListener
    public void onRstStreamRead(ChannelHandlerContext ctx, int streamId, long errorCode) throws Http2Exception {
        Http2Stream stream = this.connection.stream(streamId);
        FullHttpMessage msg = getMessage(stream);
        if (msg != null) {
            onRstStreamRead(stream, msg);
        }
        ctx.fireExceptionCaught((Throwable) Http2Exception.streamError(streamId, Http2Error.valueOf(errorCode), "HTTP/2 to HTTP layer caught stream reset", new Object[0]));
    }

    @Override // io.netty.handler.codec.http2.Http2EventAdapter, io.netty.handler.codec.http2.Http2FrameListener
    public void onPushPromiseRead(ChannelHandlerContext ctx, int streamId, int promisedStreamId, Http2Headers headers, int padding) throws Http2Exception {
        Http2Stream promisedStream = this.connection.stream(promisedStreamId);
        if (headers.status() == null) {
            headers.status(HttpResponseStatus.OK.codeAsText());
        }
        FullHttpMessage msg = processHeadersBegin(ctx, promisedStream, headers, false, false, false);
        if (msg == null) {
            throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Push Promise Frame received for pre-existing stream id %d", Integer.valueOf(promisedStreamId));
        }
        msg.headers().setInt(HttpConversionUtil.ExtensionHeaderNames.STREAM_PROMISE_ID.text(), streamId);
        msg.headers().setShort(HttpConversionUtil.ExtensionHeaderNames.STREAM_WEIGHT.text(), (short) 16);
        processHeadersEnd(ctx, promisedStream, msg, false);
    }

    @Override // io.netty.handler.codec.http2.Http2EventAdapter, io.netty.handler.codec.http2.Http2FrameListener
    public void onSettingsRead(ChannelHandlerContext ctx, Http2Settings settings) throws Http2Exception {
        if (this.propagateSettings) {
            ctx.fireChannelRead((Object) settings);
        }
    }

    protected void onRstStreamRead(Http2Stream stream, FullHttpMessage msg) {
        removeMessage(stream, true);
    }
}
