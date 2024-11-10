package io.netty.handler.codec.http;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/* loaded from: classes4.dex */
public class HttpServerKeepAliveHandler extends ChannelDuplexHandler {
    private static final String MULTIPART_PREFIX = "multipart";
    private int pendingResponses;
    private boolean persistentConnection = true;

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            if (this.persistentConnection) {
                this.pendingResponses++;
                this.persistentConnection = HttpUtil.isKeepAlive(request);
            }
        }
        super.channelRead(ctx, msg);
    }

    @Override // io.netty.channel.ChannelDuplexHandler, io.netty.channel.ChannelOutboundHandler
    public void write(ChannelHandlerContext channelHandlerContext, Object obj, ChannelPromise channelPromise) throws Exception {
        if (obj instanceof HttpResponse) {
            HttpResponse httpResponse = (HttpResponse) obj;
            trackResponse(httpResponse);
            if (!HttpUtil.isKeepAlive(httpResponse) || !isSelfDefinedMessageLength(httpResponse)) {
                this.pendingResponses = 0;
                this.persistentConnection = false;
            }
            if (!shouldKeepAlive()) {
                HttpUtil.setKeepAlive(httpResponse, false);
            }
        }
        ChannelPromise channelPromise2 = channelPromise;
        if (obj instanceof LastHttpContent) {
            channelPromise2 = channelPromise;
            if (!shouldKeepAlive()) {
                channelPromise2 = channelPromise.unvoid().addListener((GenericFutureListener<? extends Future<? super Void>>) ChannelFutureListener.CLOSE);
            }
        }
        super.write(channelHandlerContext, obj, channelPromise2);
    }

    private void trackResponse(HttpResponse response) {
        if (!isInformational(response)) {
            this.pendingResponses--;
        }
    }

    private boolean shouldKeepAlive() {
        return this.pendingResponses != 0 || this.persistentConnection;
    }

    private static boolean isSelfDefinedMessageLength(HttpResponse response) {
        return HttpUtil.isContentLengthSet(response) || HttpUtil.isTransferEncodingChunked(response) || isMultipart(response) || isInformational(response) || response.status().code() == HttpResponseStatus.NO_CONTENT.code();
    }

    private static boolean isInformational(HttpResponse response) {
        return response.status().codeClass() == HttpStatusClass.INFORMATIONAL;
    }

    private static boolean isMultipart(HttpResponse response) {
        String contentType = response.headers().get(HttpHeaderNames.CONTENT_TYPE);
        return contentType != null && contentType.regionMatches(true, 0, MULTIPART_PREFIX, 0, MULTIPART_PREFIX.length());
    }
}
