package io.netty.handler.ssl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.SocketAddress;
import java.util.List;

/* loaded from: classes4.dex */
public abstract class SslClientHelloHandler<T> extends ByteToMessageDecoder implements ChannelOutboundHandler {
    public static final int MAX_CLIENT_HELLO_LENGTH = 16777215;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) SslClientHelloHandler.class);
    private ByteBuf handshakeBuffer;
    private boolean handshakeFailed;
    private final int maxClientHelloLength;
    private boolean readPending;
    private boolean suppressRead;

    protected abstract Future<T> lookup(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception;

    protected abstract void onLookupComplete(ChannelHandlerContext channelHandlerContext, Future<T> future) throws Exception;

    public SslClientHelloHandler() {
        this(16777215);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public SslClientHelloHandler(int maxClientHelloLength) {
        this.maxClientHelloLength = ObjectUtil.checkInRange(maxClientHelloLength, 0, 16777215, "maxClientHelloLength");
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:11:0x0021. Please report as an issue. */
    @Override // io.netty.handler.codec.ByteToMessageDecoder
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (!this.suppressRead && !this.handshakeFailed) {
            try {
                int readerIndex = in.readerIndex();
                int readableBytes = in.readableBytes();
                int handshakeLength = -1;
                while (readableBytes >= 5) {
                    int contentType = in.getUnsignedByte(readerIndex);
                    switch (contentType) {
                        case 20:
                        case 21:
                            int len = SslUtils.getEncryptedPacketLength(in, readerIndex);
                            if (len == -2) {
                                this.handshakeFailed = true;
                                NotSslRecordException e = new NotSslRecordException("not an SSL/TLS record: " + ByteBufUtil.hexDump(in));
                                in.skipBytes(in.readableBytes());
                                ctx.fireUserEventTriggered((Object) new SniCompletionEvent(e));
                                SslUtils.handleHandshakeFailure(ctx, e, true);
                                throw e;
                            }
                            if (len == -1) {
                                return;
                            }
                            select(ctx, null);
                            return;
                        case 22:
                            int majorVersion = in.getUnsignedByte(readerIndex + 1);
                            if (majorVersion == 3) {
                                int packetLength = in.getUnsignedShort(readerIndex + 3) + 5;
                                if (readableBytes < packetLength) {
                                    return;
                                }
                                if (packetLength == 5) {
                                    select(ctx, null);
                                    return;
                                }
                                int endOffset = readerIndex + packetLength;
                                if (handshakeLength == -1) {
                                    if (readerIndex + 4 > endOffset) {
                                        return;
                                    }
                                    int handshakeType = in.getUnsignedByte(readerIndex + 5);
                                    if (handshakeType != 1) {
                                        select(ctx, null);
                                        return;
                                    }
                                    handshakeLength = in.getUnsignedMedium(readerIndex + 5 + 1);
                                    if (handshakeLength > this.maxClientHelloLength && this.maxClientHelloLength != 0) {
                                        TooLongFrameException e2 = new TooLongFrameException("ClientHello length exceeds " + this.maxClientHelloLength + ": " + handshakeLength);
                                        in.skipBytes(in.readableBytes());
                                        ctx.fireUserEventTriggered((Object) new SniCompletionEvent(e2));
                                        SslUtils.handleHandshakeFailure(ctx, e2, true);
                                        throw e2;
                                    }
                                    readerIndex += 4;
                                    packetLength -= 4;
                                    if (handshakeLength + 4 + 5 <= packetLength) {
                                        select(ctx, in.retainedSlice(readerIndex + 5, handshakeLength));
                                        return;
                                    } else if (this.handshakeBuffer == null) {
                                        this.handshakeBuffer = ctx.alloc().buffer(handshakeLength);
                                    } else {
                                        this.handshakeBuffer.clear();
                                    }
                                }
                                this.handshakeBuffer.writeBytes(in, readerIndex + 5, packetLength - 5);
                                readerIndex += packetLength;
                                readableBytes -= packetLength;
                                if (handshakeLength <= this.handshakeBuffer.readableBytes()) {
                                    ByteBuf clientHello = this.handshakeBuffer.setIndex(0, handshakeLength);
                                    this.handshakeBuffer = null;
                                    select(ctx, clientHello);
                                    return;
                                }
                            } else {
                                select(ctx, null);
                                return;
                            }
                            break;
                        default:
                            select(ctx, null);
                            return;
                    }
                }
            } catch (TooLongFrameException e3) {
                throw e3;
            } catch (NotSslRecordException e4) {
                throw e4;
            } catch (Exception e5) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Unexpected client hello packet: " + ByteBufUtil.hexDump(in), (Throwable) e5);
                }
                select(ctx, null);
            }
        }
    }

    private void releaseHandshakeBuffer() {
        releaseIfNotNull(this.handshakeBuffer);
        this.handshakeBuffer = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void releaseIfNotNull(ByteBuf buffer) {
        if (buffer != null) {
            buffer.release();
        }
    }

    private void select(final ChannelHandlerContext ctx, final ByteBuf clientHello) throws Exception {
        try {
            Future<T> future = lookup(ctx, clientHello);
            if (future.isDone()) {
                onLookupComplete(ctx, future);
            } else {
                this.suppressRead = true;
                future.addListener(new FutureListener<T>() { // from class: io.netty.handler.ssl.SslClientHelloHandler.1
                    @Override // io.netty.util.concurrent.GenericFutureListener
                    public void operationComplete(Future<T> future2) {
                        SslClientHelloHandler.releaseIfNotNull(clientHello);
                        try {
                            SslClientHelloHandler.this.suppressRead = false;
                            try {
                                try {
                                    try {
                                        SslClientHelloHandler.this.onLookupComplete(ctx, future2);
                                    } catch (DecoderException err) {
                                        ctx.fireExceptionCaught((Throwable) err);
                                    }
                                } catch (Exception cause) {
                                    ctx.fireExceptionCaught((Throwable) new DecoderException(cause));
                                }
                            } catch (Throwable cause2) {
                                ctx.fireExceptionCaught(cause2);
                            }
                        } finally {
                            if (SslClientHelloHandler.this.readPending) {
                                SslClientHelloHandler.this.readPending = false;
                                ctx.read();
                            }
                        }
                    }
                });
                clientHello = null;
            }
        } catch (Throwable cause) {
            try {
                PlatformDependent.throwException(cause);
            } finally {
                releaseIfNotNull(clientHello);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.ByteToMessageDecoder
    public void handlerRemoved0(ChannelHandlerContext ctx) throws Exception {
        releaseHandshakeBuffer();
        super.handlerRemoved0(ctx);
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    public void read(ChannelHandlerContext ctx) throws Exception {
        if (this.suppressRead) {
            this.readPending = true;
        } else {
            ctx.read();
        }
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        ctx.bind(localAddress, promise);
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        ctx.connect(remoteAddress, localAddress, promise);
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ctx.disconnect(promise);
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ctx.close(promise);
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ctx.deregister(promise);
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ctx.write(msg, promise);
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    public void flush(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
