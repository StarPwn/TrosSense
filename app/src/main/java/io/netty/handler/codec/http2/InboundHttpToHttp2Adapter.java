package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpScheme;
import io.netty.handler.codec.http2.HttpConversionUtil;

/* loaded from: classes4.dex */
public class InboundHttpToHttp2Adapter extends ChannelInboundHandlerAdapter {
    private final Http2Connection connection;
    private final Http2FrameListener listener;

    public InboundHttpToHttp2Adapter(Http2Connection connection, Http2FrameListener listener) {
        this.connection = connection;
        this.listener = listener;
    }

    private static int getStreamId(Http2Connection connection, HttpHeaders httpHeaders) {
        return httpHeaders.getInt(HttpConversionUtil.ExtensionHeaderNames.STREAM_ID.text(), connection.remote().incrementAndGetNextStreamId());
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpMessage) {
            handle(ctx, this.connection, this.listener, (FullHttpMessage) msg);
        } else {
            super.channelRead(ctx, msg);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void handle(ChannelHandlerContext ctx, Http2Connection connection, Http2FrameListener listener, FullHttpMessage message) throws Http2Exception {
        int streamId;
        Http2Stream stream;
        boolean z;
        boolean z2;
        try {
            streamId = getStreamId(connection, message.headers());
            Http2Stream stream2 = connection.stream(streamId);
            stream = stream2 == null ? connection.remote().createStream(streamId, false) : stream2;
            message.headers().set(HttpConversionUtil.ExtensionHeaderNames.SCHEME.text(), HttpScheme.HTTP.name());
        } catch (Throwable th) {
            th = th;
        }
        try {
            Http2Headers messageHeaders = HttpConversionUtil.toHttp2Headers((HttpMessage) message, true);
            boolean hasContent = message.content().isReadable();
            boolean hasTrailers = !message.trailingHeaders().isEmpty();
            if (hasContent || hasTrailers) {
                z = false;
            } else {
                z = true;
            }
            listener.onHeadersRead(ctx, streamId, messageHeaders, 0, z);
            if (hasContent) {
                ByteBuf content = message.content();
                if (hasTrailers) {
                    z2 = false;
                } else {
                    z2 = true;
                }
                listener.onDataRead(ctx, streamId, content, 0, z2);
            }
            if (hasTrailers) {
                Http2Headers headers = HttpConversionUtil.toHttp2Headers(message.trailingHeaders(), true);
                listener.onHeadersRead(ctx, streamId, headers, 0, true);
            }
            stream.closeRemoteSide();
            message.release();
        } catch (Throwable th2) {
            th = th2;
            message.release();
            throw th;
        }
    }
}
