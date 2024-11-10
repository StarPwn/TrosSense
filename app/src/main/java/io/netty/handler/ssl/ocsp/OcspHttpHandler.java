package io.netty.handler.ssl.ocsp;

import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.concurrent.Promise;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import org.bouncycastle.cert.ocsp.OCSPException;
import org.bouncycastle.cert.ocsp.OCSPResp;

/* loaded from: classes4.dex */
final class OcspHttpHandler extends SimpleChannelInboundHandler<FullHttpResponse> {
    private static final InternalLogger LOGGER = InternalLoggerFactory.getInstance((Class<?>) OcspHttpHandler.class);
    public static final String OCSP_REQUEST_TYPE = "application/ocsp-request";
    public static final String OCSP_RESPONSE_TYPE = "application/ocsp-response";
    private final Promise<OCSPResp> responseFuture;

    /* JADX INFO: Access modifiers changed from: package-private */
    public OcspHttpHandler(Promise<OCSPResp> responsePromise) {
        this.responseFuture = (Promise) ObjectUtil.checkNotNull(responsePromise, "ResponsePromise");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.SimpleChannelInboundHandler
    public void channelRead0(ChannelHandlerContext ctx, FullHttpResponse response) throws Exception {
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Received OCSP HTTP Response: {}", response);
            }
            String contentType = response.headers().get(HttpHeaderNames.CONTENT_TYPE);
            if (contentType == null) {
                throw new OCSPException("HTTP Response does not contain 'CONTENT-TYPE' header");
            }
            if (!contentType.equalsIgnoreCase(OCSP_RESPONSE_TYPE)) {
                throw new OCSPException("Response Content-Type was: " + contentType + "; Expected: " + OCSP_RESPONSE_TYPE);
            }
            if (response.status() != HttpResponseStatus.OK) {
                throw new IllegalArgumentException("HTTP Response Code was: " + response.status().code() + "; Expected: 200");
            }
            this.responseFuture.trySuccess(new OCSPResp(ByteBufUtil.getBytes(response.content())));
        } finally {
            ctx.channel().close();
        }
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler, io.netty.channel.ChannelInboundHandler
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        this.responseFuture.tryFailure(cause);
    }
}
