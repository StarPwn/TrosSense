package io.netty.handler.ssl;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.ScheduledFuture;
import io.netty.util.internal.ObjectUtil;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/* loaded from: classes4.dex */
public abstract class AbstractSniHandler<T> extends SslClientHelloHandler<T> {
    protected final long handshakeTimeoutMillis;
    private String hostname;
    private ScheduledFuture<?> timeoutFuture;

    protected abstract Future<T> lookup(ChannelHandlerContext channelHandlerContext, String str) throws Exception;

    protected abstract void onLookupComplete(ChannelHandlerContext channelHandlerContext, String str, Future<T> future) throws Exception;

    private static String extractSniHostname(ByteBuf in) {
        int offset = in.readerIndex();
        int endOffset = in.writerIndex();
        int offset2 = offset + 34;
        if (endOffset - offset2 >= 6) {
            int sessionIdLength = in.getUnsignedByte(offset2);
            int offset3 = offset2 + sessionIdLength + 1;
            int cipherSuitesLength = in.getUnsignedShort(offset3);
            int offset4 = offset3 + cipherSuitesLength + 2;
            int compressionMethodLength = in.getUnsignedByte(offset4);
            int offset5 = offset4 + compressionMethodLength + 1;
            int extensionsLength = in.getUnsignedShort(offset5);
            int offset6 = offset5 + 2;
            int extensionsLimit = offset6 + extensionsLength;
            if (extensionsLimit <= endOffset) {
                while (extensionsLimit - offset6 >= 4) {
                    int extensionType = in.getUnsignedShort(offset6);
                    int offset7 = offset6 + 2;
                    int extensionLength = in.getUnsignedShort(offset7);
                    int offset8 = offset7 + 2;
                    if (extensionsLimit - offset8 >= extensionLength) {
                        if (extensionType == 0) {
                            int offset9 = offset8 + 2;
                            if (extensionsLimit - offset9 >= 3) {
                                int serverNameType = in.getUnsignedByte(offset9);
                                int offset10 = offset9 + 1;
                                if (serverNameType == 0) {
                                    int serverNameLength = in.getUnsignedShort(offset10);
                                    int offset11 = offset10 + 2;
                                    if (extensionsLimit - offset11 >= serverNameLength) {
                                        String hostname = in.toString(offset11, serverNameLength, CharsetUtil.US_ASCII);
                                        return hostname.toLowerCase(Locale.US);
                                    }
                                    return null;
                                }
                                return null;
                            }
                            return null;
                        }
                        offset6 = offset8 + extensionLength;
                    } else {
                        return null;
                    }
                }
                return null;
            }
            return null;
        }
        return null;
    }

    protected AbstractSniHandler(long handshakeTimeoutMillis) {
        this(0, handshakeTimeoutMillis);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractSniHandler(int maxClientHelloLength, long handshakeTimeoutMillis) {
        super(maxClientHelloLength);
        this.handshakeTimeoutMillis = ObjectUtil.checkPositiveOrZero(handshakeTimeoutMillis, "handshakeTimeoutMillis");
    }

    public AbstractSniHandler() {
        this(0, 0L);
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        if (ctx.channel().isActive()) {
            checkStartTimeout(ctx);
        }
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelActive();
        checkStartTimeout(ctx);
    }

    private void checkStartTimeout(final ChannelHandlerContext ctx) {
        if (this.handshakeTimeoutMillis <= 0 || this.timeoutFuture != null) {
            return;
        }
        this.timeoutFuture = ctx.executor().schedule(new Runnable() { // from class: io.netty.handler.ssl.AbstractSniHandler.1
            @Override // java.lang.Runnable
            public void run() {
                if (ctx.channel().isActive()) {
                    SslHandshakeTimeoutException exception = new SslHandshakeTimeoutException("handshake timed out after " + AbstractSniHandler.this.handshakeTimeoutMillis + "ms");
                    ctx.fireUserEventTriggered((Object) new SniCompletionEvent(exception));
                    ctx.close();
                }
            }
        }, this.handshakeTimeoutMillis, TimeUnit.MILLISECONDS);
    }

    @Override // io.netty.handler.ssl.SslClientHelloHandler
    protected Future<T> lookup(ChannelHandlerContext ctx, ByteBuf clientHello) throws Exception {
        this.hostname = clientHello == null ? null : extractSniHostname(clientHello);
        return lookup(ctx, this.hostname);
    }

    @Override // io.netty.handler.ssl.SslClientHelloHandler
    protected void onLookupComplete(ChannelHandlerContext ctx, Future<T> future) throws Exception {
        if (this.timeoutFuture != null) {
            this.timeoutFuture.cancel(false);
        }
        try {
            onLookupComplete(ctx, this.hostname, future);
        } finally {
            fireSniCompletionEvent(ctx, this.hostname, future);
        }
    }

    private static void fireSniCompletionEvent(ChannelHandlerContext ctx, String hostname, Future<?> future) {
        Throwable cause = future.cause();
        if (cause == null) {
            ctx.fireUserEventTriggered((Object) new SniCompletionEvent(hostname));
        } else {
            ctx.fireUserEventTriggered((Object) new SniCompletionEvent(hostname, cause));
        }
    }
}
