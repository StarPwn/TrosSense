package io.netty.handler.codec.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/* loaded from: classes4.dex */
public class HttpServerExpectContinueHandler extends ChannelInboundHandlerAdapter {
    private static final FullHttpResponse EXPECTATION_FAILED = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.EXPECTATION_FAILED, Unpooled.EMPTY_BUFFER);
    private static final FullHttpResponse ACCEPT = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE, Unpooled.EMPTY_BUFFER);

    static {
        EXPECTATION_FAILED.headers().set((CharSequence) HttpHeaderNames.CONTENT_LENGTH, (Object) 0);
        ACCEPT.headers().set((CharSequence) HttpHeaderNames.CONTENT_LENGTH, (Object) 0);
    }

    protected HttpResponse acceptMessage(HttpRequest request) {
        return ACCEPT.retainedDuplicate();
    }

    protected HttpResponse rejectResponse(HttpRequest request) {
        return EXPECTATION_FAILED.retainedDuplicate();
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest req = (HttpRequest) msg;
            if (HttpUtil.is100ContinueExpected(req)) {
                HttpResponse accept = acceptMessage(req);
                if (accept == null) {
                    HttpResponse rejection = rejectResponse(req);
                    ReferenceCountUtil.release(msg);
                    ctx.writeAndFlush(rejection).addListener((GenericFutureListener<? extends Future<? super Void>>) ChannelFutureListener.CLOSE_ON_FAILURE);
                    return;
                }
                ctx.writeAndFlush(accept).addListener((GenericFutureListener<? extends Future<? super Void>>) ChannelFutureListener.CLOSE_ON_FAILURE);
                req.headers().remove(HttpHeaderNames.EXPECT);
            }
        }
        super.channelRead(ctx, msg);
    }
}
