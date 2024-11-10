package io.netty.handler.ssl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.AbstractCoalescingBufferQueue;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPromise;
import io.netty.channel.unix.UnixChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.UnsupportedMessageTypeException;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.ImmediateExecutor;
import io.netty.util.concurrent.Promise;
import io.netty.util.concurrent.PromiseNotifier;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.ThrowableUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSession;

/* loaded from: classes4.dex */
public class SslHandler extends ByteToMessageDecoder implements ChannelOutboundHandler {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int MAX_PLAINTEXT_LENGTH = 16384;
    private static final int STATE_CLOSE_NOTIFY = 64;
    private static final int STATE_FIRE_CHANNEL_READ = 256;
    private static final int STATE_FLUSHED_BEFORE_HANDSHAKE = 2;
    private static final int STATE_HANDSHAKE_STARTED = 8;
    private static final int STATE_NEEDS_FLUSH = 16;
    private static final int STATE_OUTBOUND_CLOSED = 32;
    private static final int STATE_PROCESS_TASK = 128;
    private static final int STATE_READ_DURING_HANDSHAKE = 4;
    private static final int STATE_SENT_FIRST_MESSAGE = 1;
    private static final int STATE_UNWRAP_REENTRY = 512;
    private volatile long closeNotifyFlushTimeoutMillis;
    private volatile long closeNotifyReadTimeoutMillis;
    private volatile ChannelHandlerContext ctx;
    private final Executor delegatedTaskExecutor;
    private final SSLEngine engine;
    private final SslEngineType engineType;
    private Promise<Channel> handshakePromise;
    private volatile long handshakeTimeoutMillis;
    private final boolean jdkCompatibilityMode;
    private int packetLength;
    private SslHandlerCoalescingBufferQueue pendingUnencryptedWrites;
    private final ByteBuffer[] singleBuffer;
    private final LazyChannelPromise sslClosePromise;
    private final SslTasksRunner sslTaskRunner;
    private final SslTasksRunner sslTaskRunnerForUnwrap;
    private final boolean startTls;
    private short state;
    volatile int wrapDataSize;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) SslHandler.class);
    private static final Pattern IGNORABLE_CLASS_IN_STACK = Pattern.compile("^.*(?:Socket|Datagram|Sctp|Udt)Channel.*$");
    private static final Pattern IGNORABLE_ERROR_MESSAGE = Pattern.compile("^.*(?:connection.*(?:reset|closed|abort|broken)|broken.*pipe).*$", 2);

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public enum SslEngineType {
        TCNATIVE(true, ByteToMessageDecoder.COMPOSITE_CUMULATOR) { // from class: io.netty.handler.ssl.SslHandler.SslEngineType.1
            @Override // io.netty.handler.ssl.SslHandler.SslEngineType
            SSLEngineResult unwrap(SslHandler handler, ByteBuf in, int len, ByteBuf out) throws SSLException {
                SSLEngineResult result;
                int nioBufferCount = in.nioBufferCount();
                int writerIndex = out.writerIndex();
                if (nioBufferCount > 1) {
                    ReferenceCountedOpenSslEngine opensslEngine = (ReferenceCountedOpenSslEngine) handler.engine;
                    try {
                        handler.singleBuffer[0] = SslHandler.toByteBuffer(out, writerIndex, out.writableBytes());
                        result = opensslEngine.unwrap(in.nioBuffers(in.readerIndex(), len), handler.singleBuffer);
                    } finally {
                        handler.singleBuffer[0] = null;
                    }
                } else {
                    result = handler.engine.unwrap(SslHandler.toByteBuffer(in, in.readerIndex(), len), SslHandler.toByteBuffer(out, writerIndex, out.writableBytes()));
                }
                out.writerIndex(result.bytesProduced() + writerIndex);
                return result;
            }

            @Override // io.netty.handler.ssl.SslHandler.SslEngineType
            ByteBuf allocateWrapBuffer(SslHandler handler, ByteBufAllocator allocator, int pendingBytes, int numComponents) {
                return allocator.directBuffer(((ReferenceCountedOpenSslEngine) handler.engine).calculateOutNetBufSize(pendingBytes, numComponents));
            }

            @Override // io.netty.handler.ssl.SslHandler.SslEngineType
            int calculateRequiredOutBufSpace(SslHandler handler, int pendingBytes, int numComponents) {
                return ((ReferenceCountedOpenSslEngine) handler.engine).calculateMaxLengthForWrap(pendingBytes, numComponents);
            }

            @Override // io.netty.handler.ssl.SslHandler.SslEngineType
            int calculatePendingData(SslHandler handler, int guess) {
                int sslPending = ((ReferenceCountedOpenSslEngine) handler.engine).sslPending();
                return sslPending > 0 ? sslPending : guess;
            }

            @Override // io.netty.handler.ssl.SslHandler.SslEngineType
            boolean jdkCompatibilityMode(SSLEngine engine) {
                return ((ReferenceCountedOpenSslEngine) engine).jdkCompatibilityMode;
            }
        },
        CONSCRYPT(true ? 1 : 0, ByteToMessageDecoder.COMPOSITE_CUMULATOR) { // from class: io.netty.handler.ssl.SslHandler.SslEngineType.2
            @Override // io.netty.handler.ssl.SslHandler.SslEngineType
            SSLEngineResult unwrap(SslHandler handler, ByteBuf in, int len, ByteBuf out) throws SSLException {
                SSLEngineResult result;
                int nioBufferCount = in.nioBufferCount();
                int writerIndex = out.writerIndex();
                if (nioBufferCount > 1) {
                    try {
                        handler.singleBuffer[0] = SslHandler.toByteBuffer(out, writerIndex, out.writableBytes());
                        result = ((ConscryptAlpnSslEngine) handler.engine).unwrap(in.nioBuffers(in.readerIndex(), len), handler.singleBuffer);
                    } finally {
                        handler.singleBuffer[0] = null;
                    }
                } else {
                    result = handler.engine.unwrap(SslHandler.toByteBuffer(in, in.readerIndex(), len), SslHandler.toByteBuffer(out, writerIndex, out.writableBytes()));
                }
                out.writerIndex(result.bytesProduced() + writerIndex);
                return result;
            }

            @Override // io.netty.handler.ssl.SslHandler.SslEngineType
            ByteBuf allocateWrapBuffer(SslHandler handler, ByteBufAllocator allocator, int pendingBytes, int numComponents) {
                return allocator.directBuffer(((ConscryptAlpnSslEngine) handler.engine).calculateOutNetBufSize(pendingBytes, numComponents));
            }

            @Override // io.netty.handler.ssl.SslHandler.SslEngineType
            int calculateRequiredOutBufSpace(SslHandler handler, int pendingBytes, int numComponents) {
                return ((ConscryptAlpnSslEngine) handler.engine).calculateRequiredOutBufSpace(pendingBytes, numComponents);
            }

            @Override // io.netty.handler.ssl.SslHandler.SslEngineType
            int calculatePendingData(SslHandler handler, int guess) {
                return guess;
            }

            @Override // io.netty.handler.ssl.SslHandler.SslEngineType
            boolean jdkCompatibilityMode(SSLEngine engine) {
                return true;
            }
        },
        JDK(0 == true ? 1 : 0, ByteToMessageDecoder.MERGE_CUMULATOR) { // from class: io.netty.handler.ssl.SslHandler.SslEngineType.3
            @Override // io.netty.handler.ssl.SslHandler.SslEngineType
            SSLEngineResult unwrap(SslHandler handler, ByteBuf in, int len, ByteBuf out) throws SSLException {
                int consumed;
                int writerIndex = out.writerIndex();
                ByteBuffer inNioBuffer = SslHandler.toByteBuffer(in, in.readerIndex(), len);
                int position = inNioBuffer.position();
                SSLEngineResult result = handler.engine.unwrap(inNioBuffer, SslHandler.toByteBuffer(out, writerIndex, out.writableBytes()));
                out.writerIndex(result.bytesProduced() + writerIndex);
                if (result.bytesConsumed() == 0 && (consumed = inNioBuffer.position() - position) != result.bytesConsumed()) {
                    return new SSLEngineResult(result.getStatus(), result.getHandshakeStatus(), consumed, result.bytesProduced());
                }
                return result;
            }

            @Override // io.netty.handler.ssl.SslHandler.SslEngineType
            ByteBuf allocateWrapBuffer(SslHandler handler, ByteBufAllocator allocator, int pendingBytes, int numComponents) {
                return allocator.heapBuffer(Math.max(pendingBytes, handler.engine.getSession().getPacketBufferSize()));
            }

            @Override // io.netty.handler.ssl.SslHandler.SslEngineType
            int calculateRequiredOutBufSpace(SslHandler handler, int pendingBytes, int numComponents) {
                return handler.engine.getSession().getPacketBufferSize();
            }

            @Override // io.netty.handler.ssl.SslHandler.SslEngineType
            int calculatePendingData(SslHandler handler, int guess) {
                return guess;
            }

            @Override // io.netty.handler.ssl.SslHandler.SslEngineType
            boolean jdkCompatibilityMode(SSLEngine engine) {
                return true;
            }
        };

        final ByteToMessageDecoder.Cumulator cumulator;
        final boolean wantsDirectBuffer;

        abstract ByteBuf allocateWrapBuffer(SslHandler sslHandler, ByteBufAllocator byteBufAllocator, int i, int i2);

        abstract int calculatePendingData(SslHandler sslHandler, int i);

        abstract int calculateRequiredOutBufSpace(SslHandler sslHandler, int i, int i2);

        abstract boolean jdkCompatibilityMode(SSLEngine sSLEngine);

        abstract SSLEngineResult unwrap(SslHandler sslHandler, ByteBuf byteBuf, int i, ByteBuf byteBuf2) throws SSLException;

        static SslEngineType forEngine(SSLEngine engine) {
            return engine instanceof ReferenceCountedOpenSslEngine ? TCNATIVE : engine instanceof ConscryptAlpnSslEngine ? CONSCRYPT : JDK;
        }

        SslEngineType(boolean wantsDirectBuffer, ByteToMessageDecoder.Cumulator cumulator) {
            this.wantsDirectBuffer = wantsDirectBuffer;
            this.cumulator = cumulator;
        }
    }

    public SslHandler(SSLEngine engine) {
        this(engine, false);
    }

    public SslHandler(SSLEngine engine, boolean startTls) {
        this(engine, startTls, ImmediateExecutor.INSTANCE);
    }

    public SslHandler(SSLEngine engine, Executor delegatedTaskExecutor) {
        this(engine, false, delegatedTaskExecutor);
    }

    public SslHandler(SSLEngine engine, boolean startTls, Executor delegatedTaskExecutor) {
        this.singleBuffer = new ByteBuffer[1];
        this.sslTaskRunnerForUnwrap = new SslTasksRunner(true);
        this.sslTaskRunner = new SslTasksRunner(false);
        this.handshakePromise = new LazyChannelPromise();
        this.sslClosePromise = new LazyChannelPromise();
        this.handshakeTimeoutMillis = 10000L;
        this.closeNotifyFlushTimeoutMillis = 3000L;
        this.wrapDataSize = 16384;
        this.engine = (SSLEngine) ObjectUtil.checkNotNull(engine, "engine");
        this.delegatedTaskExecutor = (Executor) ObjectUtil.checkNotNull(delegatedTaskExecutor, "delegatedTaskExecutor");
        this.engineType = SslEngineType.forEngine(engine);
        this.startTls = startTls;
        this.jdkCompatibilityMode = this.engineType.jdkCompatibilityMode(engine);
        setCumulator(this.engineType.cumulator);
    }

    public long getHandshakeTimeoutMillis() {
        return this.handshakeTimeoutMillis;
    }

    public void setHandshakeTimeout(long handshakeTimeout, TimeUnit unit) {
        ObjectUtil.checkNotNull(unit, "unit");
        setHandshakeTimeoutMillis(unit.toMillis(handshakeTimeout));
    }

    public void setHandshakeTimeoutMillis(long handshakeTimeoutMillis) {
        this.handshakeTimeoutMillis = ObjectUtil.checkPositiveOrZero(handshakeTimeoutMillis, "handshakeTimeoutMillis");
    }

    public final void setWrapDataSize(int wrapDataSize) {
        this.wrapDataSize = wrapDataSize;
    }

    @Deprecated
    public long getCloseNotifyTimeoutMillis() {
        return getCloseNotifyFlushTimeoutMillis();
    }

    @Deprecated
    public void setCloseNotifyTimeout(long closeNotifyTimeout, TimeUnit unit) {
        setCloseNotifyFlushTimeout(closeNotifyTimeout, unit);
    }

    @Deprecated
    public void setCloseNotifyTimeoutMillis(long closeNotifyFlushTimeoutMillis) {
        setCloseNotifyFlushTimeoutMillis(closeNotifyFlushTimeoutMillis);
    }

    public final long getCloseNotifyFlushTimeoutMillis() {
        return this.closeNotifyFlushTimeoutMillis;
    }

    public final void setCloseNotifyFlushTimeout(long closeNotifyFlushTimeout, TimeUnit unit) {
        setCloseNotifyFlushTimeoutMillis(unit.toMillis(closeNotifyFlushTimeout));
    }

    public final void setCloseNotifyFlushTimeoutMillis(long closeNotifyFlushTimeoutMillis) {
        this.closeNotifyFlushTimeoutMillis = ObjectUtil.checkPositiveOrZero(closeNotifyFlushTimeoutMillis, "closeNotifyFlushTimeoutMillis");
    }

    public final long getCloseNotifyReadTimeoutMillis() {
        return this.closeNotifyReadTimeoutMillis;
    }

    public final void setCloseNotifyReadTimeout(long closeNotifyReadTimeout, TimeUnit unit) {
        setCloseNotifyReadTimeoutMillis(unit.toMillis(closeNotifyReadTimeout));
    }

    public final void setCloseNotifyReadTimeoutMillis(long closeNotifyReadTimeoutMillis) {
        this.closeNotifyReadTimeoutMillis = ObjectUtil.checkPositiveOrZero(closeNotifyReadTimeoutMillis, "closeNotifyReadTimeoutMillis");
    }

    public SSLEngine engine() {
        return this.engine;
    }

    public String applicationProtocol() {
        Object engine = engine();
        if (!(engine instanceof ApplicationProtocolAccessor)) {
            return null;
        }
        return ((ApplicationProtocolAccessor) engine).getNegotiatedApplicationProtocol();
    }

    public Future<Channel> handshakeFuture() {
        return this.handshakePromise;
    }

    @Deprecated
    public ChannelFuture close() {
        return closeOutbound();
    }

    @Deprecated
    public ChannelFuture close(ChannelPromise promise) {
        return closeOutbound(promise);
    }

    public ChannelFuture closeOutbound() {
        return closeOutbound(this.ctx.newPromise());
    }

    public ChannelFuture closeOutbound(final ChannelPromise promise) {
        ChannelHandlerContext ctx = this.ctx;
        if (ctx.executor().inEventLoop()) {
            closeOutbound0(promise);
        } else {
            ctx.executor().execute(new Runnable() { // from class: io.netty.handler.ssl.SslHandler.1
                @Override // java.lang.Runnable
                public void run() {
                    SslHandler.this.closeOutbound0(promise);
                }
            });
        }
        return promise;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void closeOutbound0(ChannelPromise promise) {
        setState(32);
        this.engine.closeOutbound();
        try {
            flush(this.ctx, promise);
        } catch (Exception e) {
            if (!promise.tryFailure(e)) {
                logger.warn("{} flush() raised a masked exception.", this.ctx.channel(), e);
            }
        }
    }

    public Future<Channel> sslCloseFuture() {
        return this.sslClosePromise;
    }

    @Override // io.netty.handler.codec.ByteToMessageDecoder
    public void handlerRemoved0(ChannelHandlerContext ctx) throws Exception {
        try {
            if (this.pendingUnencryptedWrites != null && !this.pendingUnencryptedWrites.isEmpty()) {
                this.pendingUnencryptedWrites.releaseAndFailAll(ctx, new ChannelException("Pending write on removal of SslHandler"));
            }
            this.pendingUnencryptedWrites = null;
            SSLException cause = null;
            if (!this.handshakePromise.isDone()) {
                cause = new SSLHandshakeException("SslHandler removed before handshake completed");
                if (this.handshakePromise.tryFailure(cause)) {
                    ctx.fireUserEventTriggered((Object) new SslHandshakeCompletionEvent(cause));
                }
            }
            if (!this.sslClosePromise.isDone()) {
                if (cause == null) {
                    cause = new SSLException("SslHandler removed before SSLEngine was closed");
                }
                notifyClosePromise(cause);
            }
        } finally {
            ReferenceCountUtil.release(this.engine);
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
    public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ctx.deregister(promise);
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        closeOutboundAndChannel(ctx, promise, true);
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        closeOutboundAndChannel(ctx, promise, false);
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    public void read(ChannelHandlerContext ctx) throws Exception {
        if (!this.handshakePromise.isDone()) {
            setState(4);
        }
        ctx.read();
    }

    private static IllegalStateException newPendingWritesNullException() {
        return new IllegalStateException("pendingUnencryptedWrites is null, handlerRemoved0 called?");
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (!(msg instanceof ByteBuf)) {
            UnsupportedMessageTypeException exception = new UnsupportedMessageTypeException(msg, (Class<?>[]) new Class[]{ByteBuf.class});
            ReferenceCountUtil.safeRelease(msg);
            promise.setFailure((Throwable) exception);
        } else if (this.pendingUnencryptedWrites == null) {
            ReferenceCountUtil.safeRelease(msg);
            promise.setFailure((Throwable) newPendingWritesNullException());
        } else {
            this.pendingUnencryptedWrites.add((ByteBuf) msg, promise);
        }
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    public void flush(ChannelHandlerContext ctx) throws Exception {
        if (this.startTls && !isStateSet(1)) {
            setState(1);
            this.pendingUnencryptedWrites.writeAndRemoveAll(ctx);
            forceFlush(ctx);
            startHandshakeProcessing(true);
            return;
        }
        if (isStateSet(128)) {
            return;
        }
        try {
            wrapAndFlush(ctx);
        } catch (Throwable cause) {
            setHandshakeFailure(ctx, cause);
            PlatformDependent.throwException(cause);
        }
    }

    private void wrapAndFlush(ChannelHandlerContext ctx) throws SSLException {
        if (this.pendingUnencryptedWrites.isEmpty()) {
            this.pendingUnencryptedWrites.add(Unpooled.EMPTY_BUFFER, ctx.newPromise());
        }
        if (!this.handshakePromise.isDone()) {
            setState(2);
        }
        try {
            wrap(ctx, false);
        } finally {
            forceFlush(ctx);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Failed to find 'out' block for switch in B:30:0x00d2. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00c6 A[Catch: all -> 0x012f, TRY_ENTER, TryCatch #0 {all -> 0x012f, blocks: (B:3:0x0007, B:4:0x0009, B:6:0x000f, B:8:0x0015, B:10:0x0027, B:12:0x002f, B:14:0x0039, B:16:0x003d, B:17:0x0047, B:18:0x0064, B:20:0x006a, B:21:0x0074, B:25:0x007e, B:27:0x008d, B:67:0x0095, B:69:0x009d, B:71:0x00a5, B:73:0x00ae, B:74:0x00b6, B:29:0x00c6, B:30:0x00d2, B:55:0x00d5, B:56:0x0109, B:57:0x0123, B:59:0x00d8, B:31:0x00e6, B:33:0x00ec, B:35:0x00f4, B:40:0x00fc, B:42:0x0100, B:81:0x0082, B:83:0x0088, B:84:0x0071, B:86:0x0051, B:87:0x005e, B:90:0x001c), top: B:2:0x0007 }] */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0126 A[DONT_GENERATE] */
    /* JADX WARN: Removed duplicated region for block: B:49:0x012b A[DONT_GENERATE] */
    /* JADX WARN: Removed duplicated region for block: B:52:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0095 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void wrap(io.netty.channel.ChannelHandlerContext r11, boolean r12) throws javax.net.ssl.SSLException {
        /*
            Method dump skipped, instructions count: 330
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.ssl.SslHandler.wrap(io.netty.channel.ChannelHandlerContext, boolean):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: io.netty.handler.ssl.SslHandler$11, reason: invalid class name */
    /* loaded from: classes4.dex */
    public static /* synthetic */ class AnonymousClass11 {
        static final /* synthetic */ int[] $SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus = new int[SSLEngineResult.HandshakeStatus.values().length];

        static {
            try {
                $SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus[SSLEngineResult.HandshakeStatus.NEED_TASK.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus[SSLEngineResult.HandshakeStatus.FINISHED.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus[SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus[SSLEngineResult.HandshakeStatus.NEED_WRAP.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus[SSLEngineResult.HandshakeStatus.NEED_UNWRAP.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to find 'out' block for switch in B:15:0x0044. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00d4  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00a1 A[Catch: all -> 0x00d8, TryCatch #0 {all -> 0x00d8, blocks: (B:3:0x0005, B:7:0x000f, B:8:0x0016, B:10:0x0024, B:12:0x0032, B:14:0x0038, B:15:0x0044, B:48:0x0047, B:49:0x00b7, B:50:0x00d1, B:17:0x004d, B:52:0x005b, B:55:0x0063, B:57:0x006b, B:59:0x0070, B:64:0x007a, B:67:0x0082, B:69:0x008a, B:25:0x0094, B:32:0x009b, B:34:0x00a1, B:38:0x00a6, B:40:0x00ac), top: B:2:0x0005 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean wrapNonAppData(final io.netty.channel.ChannelHandlerContext r9, boolean r10) throws javax.net.ssl.SSLException {
        /*
            r8 = this;
            r0 = 0
            io.netty.buffer.ByteBufAllocator r1 = r9.alloc()
        L5:
            boolean r2 = r9.isRemoved()     // Catch: java.lang.Throwable -> Ld8
            r3 = 0
            if (r2 != 0) goto Ld2
            r2 = 1
            if (r0 != 0) goto L16
            r4 = 2048(0x800, float:2.87E-42)
            io.netty.buffer.ByteBuf r4 = r8.allocateOutNetBuf(r9, r4, r2)     // Catch: java.lang.Throwable -> Ld8
            r0 = r4
        L16:
            javax.net.ssl.SSLEngine r4 = r8.engine     // Catch: java.lang.Throwable -> Ld8
            io.netty.buffer.ByteBuf r5 = io.netty.buffer.Unpooled.EMPTY_BUFFER     // Catch: java.lang.Throwable -> Ld8
            javax.net.ssl.SSLEngineResult r4 = r8.wrap(r1, r4, r5, r0)     // Catch: java.lang.Throwable -> Ld8
            int r5 = r4.bytesProduced()     // Catch: java.lang.Throwable -> Ld8
            if (r5 <= 0) goto L38
            io.netty.channel.ChannelFuture r5 = r9.write(r0)     // Catch: java.lang.Throwable -> Ld8
            io.netty.handler.ssl.SslHandler$2 r6 = new io.netty.handler.ssl.SslHandler$2     // Catch: java.lang.Throwable -> Ld8
            r6.<init>()     // Catch: java.lang.Throwable -> Ld8
            r5.addListener(r6)     // Catch: java.lang.Throwable -> Ld8
            if (r10 == 0) goto L37
            r5 = 16
            r8.setState(r5)     // Catch: java.lang.Throwable -> Ld8
        L37:
            r0 = 0
        L38:
            javax.net.ssl.SSLEngineResult$HandshakeStatus r5 = r4.getHandshakeStatus()     // Catch: java.lang.Throwable -> Ld8
            int[] r6 = io.netty.handler.ssl.SslHandler.AnonymousClass11.$SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus     // Catch: java.lang.Throwable -> Ld8
            int r7 = r5.ordinal()     // Catch: java.lang.Throwable -> Ld8
            r6 = r6[r7]     // Catch: java.lang.Throwable -> Ld8
            switch(r6) {
                case 1: goto L94;
                case 2: goto L7a;
                case 3: goto L5b;
                case 4: goto L5a;
                case 5: goto L4b;
                default: goto L47;
            }     // Catch: java.lang.Throwable -> Ld8
        L47:
            java.lang.IllegalStateException r2 = new java.lang.IllegalStateException     // Catch: java.lang.Throwable -> Ld8
            goto Lb7
        L4b:
            if (r10 != 0) goto L53
            int r2 = r8.unwrapNonAppData(r9)     // Catch: java.lang.Throwable -> Ld8
            if (r2 > 0) goto L9b
        L53:
        L54:
            if (r0 == 0) goto L59
            r0.release()
        L59:
            return r3
        L5a:
            goto L9b
        L5b:
            boolean r3 = r8.setHandshakeSuccess()     // Catch: java.lang.Throwable -> Ld8
            if (r3 == 0) goto L6e
            if (r10 == 0) goto L6e
            io.netty.handler.ssl.SslHandler$SslHandlerCoalescingBufferQueue r3 = r8.pendingUnencryptedWrites     // Catch: java.lang.Throwable -> Ld8
            boolean r3 = r3.isEmpty()     // Catch: java.lang.Throwable -> Ld8
            if (r3 != 0) goto L6e
            r8.wrap(r9, r2)     // Catch: java.lang.Throwable -> Ld8
        L6e:
            if (r10 != 0) goto L73
            r8.unwrapNonAppData(r9)     // Catch: java.lang.Throwable -> Ld8
        L73:
            if (r0 == 0) goto L79
            r0.release()
        L79:
            return r2
        L7a:
            boolean r6 = r8.setHandshakeSuccess()     // Catch: java.lang.Throwable -> Ld8
            if (r6 == 0) goto L8d
            if (r10 == 0) goto L8d
            io.netty.handler.ssl.SslHandler$SslHandlerCoalescingBufferQueue r6 = r8.pendingUnencryptedWrites     // Catch: java.lang.Throwable -> Ld8
            boolean r6 = r6.isEmpty()     // Catch: java.lang.Throwable -> Ld8
            if (r6 != 0) goto L8d
            r8.wrap(r9, r2)     // Catch: java.lang.Throwable -> Ld8
        L8d:
            if (r0 == 0) goto L93
            r0.release()
        L93:
            return r3
        L94:
            boolean r2 = r8.runDelegatedTasks(r10)     // Catch: java.lang.Throwable -> Ld8
            if (r2 != 0) goto L9b
            goto Ld2
        L9b:
            int r2 = r4.bytesProduced()     // Catch: java.lang.Throwable -> Ld8
            if (r2 != 0) goto La6
            javax.net.ssl.SSLEngineResult$HandshakeStatus r2 = javax.net.ssl.SSLEngineResult.HandshakeStatus.NEED_TASK     // Catch: java.lang.Throwable -> Ld8
            if (r5 == r2) goto La6
            goto Ld2
        La6:
            int r2 = r4.bytesConsumed()     // Catch: java.lang.Throwable -> Ld8
            if (r2 != 0) goto Lb5
            javax.net.ssl.SSLEngineResult$HandshakeStatus r2 = r4.getHandshakeStatus()     // Catch: java.lang.Throwable -> Ld8
            javax.net.ssl.SSLEngineResult$HandshakeStatus r6 = javax.net.ssl.SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING     // Catch: java.lang.Throwable -> Ld8
            if (r2 != r6) goto Lb5
            goto Ld2
        Lb5:
            goto L5
        Lb7:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Ld8
            r3.<init>()     // Catch: java.lang.Throwable -> Ld8
            java.lang.String r6 = "Unknown handshake status: "
            java.lang.StringBuilder r3 = r3.append(r6)     // Catch: java.lang.Throwable -> Ld8
            javax.net.ssl.SSLEngineResult$HandshakeStatus r6 = r4.getHandshakeStatus()     // Catch: java.lang.Throwable -> Ld8
            java.lang.StringBuilder r3 = r3.append(r6)     // Catch: java.lang.Throwable -> Ld8
            java.lang.String r3 = r3.toString()     // Catch: java.lang.Throwable -> Ld8
            r2.<init>(r3)     // Catch: java.lang.Throwable -> Ld8
            throw r2     // Catch: java.lang.Throwable -> Ld8
        Ld2:
            if (r0 == 0) goto Ld7
            r0.release()
        Ld7:
            return r3
        Ld8:
            r2 = move-exception
            if (r0 == 0) goto Lde
            r0.release()
        Lde:
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.ssl.SslHandler.wrapNonAppData(io.netty.channel.ChannelHandlerContext, boolean):boolean");
    }

    private SSLEngineResult wrapMultiple(ByteBufAllocator alloc, SSLEngine engine, ByteBuf in, ByteBuf out) throws SSLException {
        SSLEngineResult result = null;
        do {
            int nextSliceSize = Math.min(16384, in.readableBytes());
            int nextOutSize = this.engineType.calculateRequiredOutBufSpace(this, nextSliceSize, in.nioBufferCount());
            if (!out.isWritable(nextOutSize)) {
                if (result != null) {
                    break;
                }
                out.ensureWritable(nextOutSize);
            }
            ByteBuf wrapBuf = in.readSlice(nextSliceSize);
            result = wrap(alloc, engine, wrapBuf, out);
            if (result.getStatus() == SSLEngineResult.Status.CLOSED) {
                break;
            }
            if (wrapBuf.isReadable()) {
                in.readerIndex(in.readerIndex() - wrapBuf.readableBytes());
            }
        } while (in.readableBytes() > 0);
        return result;
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0070 A[Catch: all -> 0x0087, LOOP:0: B:10:0x0045->B:12:0x0070, LOOP_END, TRY_LEAVE, TryCatch #0 {all -> 0x0087, blocks: (B:3:0x0003, B:5:0x0011, B:8:0x0018, B:10:0x0045, B:12:0x0070, B:20:0x002d, B:22:0x0031, B:24:0x0038, B:25:0x0041), top: B:2:0x0003 }] */
    /* JADX WARN: Removed duplicated region for block: B:13:0x007c A[EDGE_INSN: B:13:0x007c->B:14:0x007c BREAK  A[LOOP:0: B:10:0x0045->B:12:0x0070], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0083 A[DONT_GENERATE] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private javax.net.ssl.SSLEngineResult wrap(io.netty.buffer.ByteBufAllocator r11, javax.net.ssl.SSLEngine r12, io.netty.buffer.ByteBuf r13, io.netty.buffer.ByteBuf r14) throws javax.net.ssl.SSLException {
        /*
            r10 = this;
            r0 = 0
            r1 = 0
            r2 = 0
            int r3 = r13.readerIndex()     // Catch: java.lang.Throwable -> L87
            int r4 = r13.readableBytes()     // Catch: java.lang.Throwable -> L87
            boolean r5 = r13.isDirect()     // Catch: java.lang.Throwable -> L87
            if (r5 != 0) goto L2d
            io.netty.handler.ssl.SslHandler$SslEngineType r5 = r10.engineType     // Catch: java.lang.Throwable -> L87
            boolean r5 = r5.wantsDirectBuffer     // Catch: java.lang.Throwable -> L87
            if (r5 != 0) goto L18
            goto L2d
        L18:
            io.netty.buffer.ByteBuf r5 = r11.directBuffer(r4)     // Catch: java.lang.Throwable -> L87
            r0 = r5
            r0.writeBytes(r13, r3, r4)     // Catch: java.lang.Throwable -> L87
            java.nio.ByteBuffer[] r5 = r10.singleBuffer     // Catch: java.lang.Throwable -> L87
            int r6 = r0.readerIndex()     // Catch: java.lang.Throwable -> L87
            java.nio.ByteBuffer r6 = r0.internalNioBuffer(r6, r4)     // Catch: java.lang.Throwable -> L87
            r5[r2] = r6     // Catch: java.lang.Throwable -> L87
            goto L45
        L2d:
            boolean r5 = r13 instanceof io.netty.buffer.CompositeByteBuf     // Catch: java.lang.Throwable -> L87
            if (r5 != 0) goto L41
            int r5 = r13.nioBufferCount()     // Catch: java.lang.Throwable -> L87
            r6 = 1
            if (r5 != r6) goto L41
            java.nio.ByteBuffer[] r5 = r10.singleBuffer     // Catch: java.lang.Throwable -> L87
            java.nio.ByteBuffer r6 = r13.internalNioBuffer(r3, r4)     // Catch: java.lang.Throwable -> L87
            r5[r2] = r6     // Catch: java.lang.Throwable -> L87
            goto L45
        L41:
            java.nio.ByteBuffer[] r5 = r13.nioBuffers()     // Catch: java.lang.Throwable -> L87
        L45:
            int r6 = r14.writerIndex()     // Catch: java.lang.Throwable -> L87
            int r7 = r14.writableBytes()     // Catch: java.lang.Throwable -> L87
            java.nio.ByteBuffer r6 = toByteBuffer(r14, r6, r7)     // Catch: java.lang.Throwable -> L87
            javax.net.ssl.SSLEngineResult r7 = r12.wrap(r5, r6)     // Catch: java.lang.Throwable -> L87
            int r8 = r7.bytesConsumed()     // Catch: java.lang.Throwable -> L87
            r13.skipBytes(r8)     // Catch: java.lang.Throwable -> L87
            int r8 = r14.writerIndex()     // Catch: java.lang.Throwable -> L87
            int r9 = r7.bytesProduced()     // Catch: java.lang.Throwable -> L87
            int r8 = r8 + r9
            r14.writerIndex(r8)     // Catch: java.lang.Throwable -> L87
            javax.net.ssl.SSLEngineResult$Status r8 = r7.getStatus()     // Catch: java.lang.Throwable -> L87
            javax.net.ssl.SSLEngineResult$Status r9 = javax.net.ssl.SSLEngineResult.Status.BUFFER_OVERFLOW     // Catch: java.lang.Throwable -> L87
            if (r8 != r9) goto L7c
            javax.net.ssl.SSLSession r8 = r12.getSession()     // Catch: java.lang.Throwable -> L87
            int r8 = r8.getPacketBufferSize()     // Catch: java.lang.Throwable -> L87
            r14.ensureWritable(r8)     // Catch: java.lang.Throwable -> L87
            goto L45
        L7c:
            java.nio.ByteBuffer[] r8 = r10.singleBuffer
            r8[r2] = r1
            if (r0 == 0) goto L86
            r0.release()
        L86:
            return r7
        L87:
            r3 = move-exception
            java.nio.ByteBuffer[] r4 = r10.singleBuffer
            r4[r2] = r1
            if (r0 == 0) goto L91
            r0.release()
        L91:
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.ssl.SslHandler.wrap(io.netty.buffer.ByteBufAllocator, javax.net.ssl.SSLEngine, io.netty.buffer.ByteBuf, io.netty.buffer.ByteBuf):javax.net.ssl.SSLEngineResult");
    }

    @Override // io.netty.handler.codec.ByteToMessageDecoder, io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        boolean handshakeFailed = this.handshakePromise.cause() != null;
        ClosedChannelException exception = new ClosedChannelException();
        if (isStateSet(8) && !this.handshakePromise.isDone()) {
            ThrowableUtil.addSuppressed(exception, StacklessSSLHandshakeException.newInstance("Connection closed while SSL/TLS handshake was in progress", SslHandler.class, "channelInactive"));
        }
        setHandshakeFailure(ctx, exception, !isStateSet(32), isStateSet(8), false);
        notifyClosePromise(exception);
        try {
            super.channelInactive(ctx);
        } catch (DecoderException e) {
            if (!handshakeFailed || !(e.getCause() instanceof SSLException)) {
                throw e;
            }
        }
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler, io.netty.channel.ChannelInboundHandler
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (ignoreException(cause)) {
            if (logger.isDebugEnabled()) {
                logger.debug("{} Swallowing a harmless 'connection reset by peer / broken pipe' error that occurred while writing close_notify in response to the peer's close_notify", ctx.channel(), cause);
            }
            if (ctx.channel().isActive()) {
                ctx.close();
                return;
            }
            return;
        }
        ctx.fireExceptionCaught(cause);
    }

    private boolean ignoreException(Throwable t) {
        if (!(t instanceof SSLException) && (t instanceof IOException) && this.sslClosePromise.isDone()) {
            String message = t.getMessage();
            if (message != null && IGNORABLE_ERROR_MESSAGE.matcher(message).matches()) {
                return true;
            }
            StackTraceElement[] elements = t.getStackTrace();
            for (StackTraceElement element : elements) {
                String classname = element.getClassName();
                String methodname = element.getMethodName();
                if (!classname.startsWith("io.netty.") && "read".equals(methodname)) {
                    if (IGNORABLE_CLASS_IN_STACK.matcher(classname).matches()) {
                        return true;
                    }
                    try {
                        Class<?> clazz = PlatformDependent.getClassLoader(getClass()).loadClass(classname);
                        if (!SocketChannel.class.isAssignableFrom(clazz) && !DatagramChannel.class.isAssignableFrom(clazz)) {
                            if (PlatformDependent.javaVersion() >= 7 && "com.sun.nio.sctp.SctpChannel".equals(clazz.getSuperclass().getName())) {
                                return true;
                            }
                        }
                        return true;
                    } catch (Throwable cause) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Unexpected exception while loading class {} classname {}", getClass(), classname, cause);
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean isEncrypted(ByteBuf buffer) {
        if (buffer.readableBytes() >= 5) {
            return SslUtils.getEncryptedPacketLength(buffer, buffer.readerIndex()) != -2;
        }
        throw new IllegalArgumentException("buffer must have at least 5 readable bytes");
    }

    private void decodeJdkCompatible(ChannelHandlerContext ctx, ByteBuf in) throws NotSslRecordException {
        int packetLength = this.packetLength;
        if (packetLength > 0) {
            if (in.readableBytes() < packetLength) {
                return;
            }
        } else {
            int readableBytes = in.readableBytes();
            if (readableBytes < 5) {
                return;
            }
            packetLength = SslUtils.getEncryptedPacketLength(in, in.readerIndex());
            if (packetLength == -2) {
                NotSslRecordException e = new NotSslRecordException("not an SSL/TLS record: " + ByteBufUtil.hexDump(in));
                in.skipBytes(in.readableBytes());
                setHandshakeFailure(ctx, e);
                throw e;
            }
            if (packetLength == -1) {
                return;
            }
            if (packetLength <= 0) {
                throw new AssertionError();
            }
            if (packetLength > readableBytes) {
                this.packetLength = packetLength;
                return;
            }
        }
        this.packetLength = 0;
        try {
            int bytesConsumed = unwrap(ctx, in, packetLength);
            if (bytesConsumed != packetLength && !this.engine.isInboundDone()) {
                throw new AssertionError("we feed the SSLEngine a packets worth of data: " + packetLength + " but it only consumed: " + bytesConsumed);
            }
        } catch (Throwable cause) {
            handleUnwrapThrowable(ctx, cause);
        }
    }

    private void decodeNonJdkCompatible(ChannelHandlerContext ctx, ByteBuf in) {
        try {
            unwrap(ctx, in, in.readableBytes());
        } catch (Throwable cause) {
            handleUnwrapThrowable(ctx, cause);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleUnwrapThrowable(ChannelHandlerContext ctx, Throwable cause) {
        try {
            if (this.handshakePromise.tryFailure(cause)) {
                ctx.fireUserEventTriggered((Object) new SslHandshakeCompletionEvent(cause));
            }
            if (this.pendingUnencryptedWrites != null) {
                wrapAndFlush(ctx);
            }
        } catch (SSLException ex) {
            logger.debug("SSLException during trying to call SSLEngine.wrap(...) because of an previous SSLException, ignoring...", (Throwable) ex);
        } finally {
            setHandshakeFailure(ctx, cause, true, false, true);
        }
        PlatformDependent.throwException(cause);
    }

    @Override // io.netty.handler.codec.ByteToMessageDecoder
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws SSLException {
        if (isStateSet(128)) {
            return;
        }
        if (this.jdkCompatibilityMode) {
            decodeJdkCompatible(ctx, in);
        } else {
            decodeNonJdkCompatible(ctx, in);
        }
    }

    @Override // io.netty.handler.codec.ByteToMessageDecoder, io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        channelReadComplete0(ctx);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void channelReadComplete0(ChannelHandlerContext ctx) {
        discardSomeReadBytes();
        flushIfNeeded(ctx);
        readIfNeeded(ctx);
        clearState(256);
        ctx.fireChannelReadComplete();
    }

    private void readIfNeeded(ChannelHandlerContext ctx) {
        if (!ctx.channel().config().isAutoRead()) {
            if (!isStateSet(256) || !this.handshakePromise.isDone()) {
                ctx.read();
            }
        }
    }

    private void flushIfNeeded(ChannelHandlerContext ctx) {
        if (isStateSet(16)) {
            forceFlush(ctx);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int unwrapNonAppData(ChannelHandlerContext ctx) throws SSLException {
        return unwrap(ctx, Unpooled.EMPTY_BUFFER, 0);
    }

    /* JADX WARN: Code restructure failed: missing block: B:30:0x00e5, code lost:            if (r12 != javax.net.ssl.SSLEngineResult.HandshakeStatus.NEED_UNWRAP) goto L71;     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x00e7, code lost:            readIfNeeded(r19);     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x0048, code lost:            if (setHandshakeSuccess() == false) goto L14;     */
    /* JADX WARN: Removed duplicated region for block: B:55:0x00d7 A[Catch: all -> 0x0118, TryCatch #0 {all -> 0x0118, blocks: (B:3:0x0015, B:5:0x0033, B:7:0x0053, B:9:0x0059, B:11:0x0066, B:13:0x006b, B:14:0x006f, B:17:0x00a4, B:19:0x00a8, B:32:0x00ea, B:34:0x00f1, B:36:0x00f9, B:38:0x0100, B:22:0x00c2, B:24:0x00c6, B:50:0x00d0, B:55:0x00d7, B:56:0x00dc, B:29:0x00e3, B:31:0x00e7, B:64:0x00b3, B:66:0x00b7, B:71:0x0077, B:74:0x007d, B:75:0x0080, B:78:0x0098, B:79:0x0092, B:81:0x0037, B:83:0x003d, B:87:0x0052, B:88:0x004a, B:92:0x0044), top: B:2:0x0015 }] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x00e2 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:60:? A[LOOP:0: B:2:0x0015->B:60:?, LOOP_END, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private int unwrap(io.netty.channel.ChannelHandlerContext r19, io.netty.buffer.ByteBuf r20, int r21) throws javax.net.ssl.SSLException {
        /*
            Method dump skipped, instructions count: 299
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.ssl.SslHandler.unwrap(io.netty.channel.ChannelHandlerContext, io.netty.buffer.ByteBuf, int):int");
    }

    private boolean setHandshakeSuccessUnwrapMarkReentry() {
        boolean setReentryState = !isStateSet(512);
        if (setReentryState) {
            setState(512);
        }
        try {
            return setHandshakeSuccess();
        } finally {
            if (setReentryState) {
                clearState(512);
            }
        }
    }

    private void executeNotifyClosePromise(ChannelHandlerContext ctx) {
        try {
            ctx.executor().execute(new Runnable() { // from class: io.netty.handler.ssl.SslHandler.3
                @Override // java.lang.Runnable
                public void run() {
                    SslHandler.this.notifyClosePromise(null);
                }
            });
        } catch (RejectedExecutionException e) {
            notifyClosePromise(e);
        }
    }

    private void executeChannelRead(final ChannelHandlerContext ctx, final ByteBuf decodedOut) {
        try {
            ctx.executor().execute(new Runnable() { // from class: io.netty.handler.ssl.SslHandler.4
                @Override // java.lang.Runnable
                public void run() {
                    ctx.fireChannelRead((Object) decodedOut);
                }
            });
        } catch (RejectedExecutionException e) {
            decodedOut.release();
            throw e;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static ByteBuffer toByteBuffer(ByteBuf out, int index, int len) {
        return out.nioBufferCount() == 1 ? out.internalNioBuffer(index, len) : out.nioBuffer(index, len);
    }

    private static boolean inEventLoop(Executor executor) {
        return (executor instanceof EventExecutor) && ((EventExecutor) executor).inEventLoop();
    }

    private boolean runDelegatedTasks(boolean inUnwrap) {
        if (this.delegatedTaskExecutor != ImmediateExecutor.INSTANCE && !inEventLoop(this.delegatedTaskExecutor)) {
            executeDelegatedTask(inUnwrap);
            return false;
        }
        while (true) {
            Runnable task = this.engine.getDelegatedTask();
            if (task == null) {
                return true;
            }
            setState(128);
            if (task instanceof AsyncRunnable) {
                boolean pending = false;
                try {
                    AsyncRunnable asyncTask = (AsyncRunnable) task;
                    AsyncTaskCompletionHandler completionHandler = new AsyncTaskCompletionHandler(inUnwrap);
                    asyncTask.run(completionHandler);
                    pending = completionHandler.resumeLater();
                    if (pending) {
                        return false;
                    }
                    if (!pending) {
                    }
                } finally {
                    if (!pending) {
                    }
                }
            } else {
                try {
                    task.run();
                } finally {
                    clearState(128);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public SslTasksRunner getTaskRunner(boolean inUnwrap) {
        return inUnwrap ? this.sslTaskRunnerForUnwrap : this.sslTaskRunner;
    }

    private void executeDelegatedTask(boolean inUnwrap) {
        executeDelegatedTask(getTaskRunner(inUnwrap));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void executeDelegatedTask(SslTasksRunner task) {
        setState(128);
        try {
            this.delegatedTaskExecutor.execute(task);
        } catch (RejectedExecutionException e) {
            clearState(128);
            throw e;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class AsyncTaskCompletionHandler implements Runnable {
        boolean didRun;
        private final boolean inUnwrap;
        boolean resumeLater;

        AsyncTaskCompletionHandler(boolean inUnwrap) {
            this.inUnwrap = inUnwrap;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.didRun = true;
            if (this.resumeLater) {
                SslHandler.this.getTaskRunner(this.inUnwrap).runComplete();
            }
        }

        boolean resumeLater() {
            if (!this.didRun) {
                this.resumeLater = true;
                return true;
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class SslTasksRunner implements Runnable {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private final boolean inUnwrap;
        private final Runnable runCompleteTask = new Runnable() { // from class: io.netty.handler.ssl.SslHandler.SslTasksRunner.1
            @Override // java.lang.Runnable
            public void run() {
                SslTasksRunner.this.runComplete();
            }
        };

        SslTasksRunner(boolean inUnwrap) {
            this.inUnwrap = inUnwrap;
        }

        private void taskError(Throwable e) {
            if (this.inUnwrap) {
                try {
                    SslHandler.this.handleUnwrapThrowable(SslHandler.this.ctx, e);
                    return;
                } catch (Throwable cause) {
                    safeExceptionCaught(cause);
                    return;
                }
            }
            SslHandler.this.setHandshakeFailure(SslHandler.this.ctx, e);
            SslHandler.this.forceFlush(SslHandler.this.ctx);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void safeExceptionCaught(Throwable cause) {
            try {
                SslHandler.this.exceptionCaught(SslHandler.this.ctx, wrapIfNeeded(cause));
            } catch (Throwable error) {
                SslHandler.this.ctx.fireExceptionCaught(error);
            }
        }

        private Throwable wrapIfNeeded(Throwable cause) {
            if (this.inUnwrap) {
                return cause instanceof DecoderException ? cause : new DecoderException(cause);
            }
            return cause;
        }

        private void tryDecodeAgain() {
            try {
                SslHandler.this.channelRead(SslHandler.this.ctx, Unpooled.EMPTY_BUFFER);
            } finally {
                try {
                } finally {
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Failed to find 'out' block for switch in B:6:0x0029. Please report as an issue. */
        public void resumeOnEventExecutor() {
            if (SslHandler.this.ctx.executor().inEventLoop()) {
                SslHandler.this.clearState(128);
                try {
                    SSLEngineResult.HandshakeStatus status = SslHandler.this.engine.getHandshakeStatus();
                    switch (AnonymousClass11.$SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus[status.ordinal()]) {
                        case 1:
                            SslHandler.this.executeDelegatedTask(this);
                            return;
                        case 2:
                        case 3:
                            SslHandler.this.setHandshakeSuccess();
                            try {
                                SslHandler.this.wrap(SslHandler.this.ctx, this.inUnwrap);
                                if (this.inUnwrap) {
                                    SslHandler.this.unwrapNonAppData(SslHandler.this.ctx);
                                }
                                SslHandler.this.forceFlush(SslHandler.this.ctx);
                                tryDecodeAgain();
                                return;
                            } catch (Throwable e) {
                                taskError(e);
                                return;
                            }
                        case 4:
                            try {
                                if (!SslHandler.this.wrapNonAppData(SslHandler.this.ctx, false) && this.inUnwrap) {
                                    SslHandler.this.unwrapNonAppData(SslHandler.this.ctx);
                                }
                                SslHandler.this.forceFlush(SslHandler.this.ctx);
                                tryDecodeAgain();
                                return;
                            } catch (Throwable e2) {
                                taskError(e2);
                                return;
                            }
                        case 5:
                            try {
                                SslHandler.this.unwrapNonAppData(SslHandler.this.ctx);
                                tryDecodeAgain();
                                return;
                            } catch (SSLException e3) {
                                SslHandler.this.handleUnwrapThrowable(SslHandler.this.ctx, e3);
                                return;
                            }
                        default:
                            throw new AssertionError();
                    }
                } catch (Throwable cause) {
                    safeExceptionCaught(cause);
                    return;
                }
            }
            throw new AssertionError();
        }

        void runComplete() {
            EventExecutor executor = SslHandler.this.ctx.executor();
            executor.execute(new Runnable() { // from class: io.netty.handler.ssl.SslHandler.SslTasksRunner.2
                @Override // java.lang.Runnable
                public void run() {
                    SslTasksRunner.this.resumeOnEventExecutor();
                }
            });
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                Runnable task = SslHandler.this.engine.getDelegatedTask();
                if (task == null) {
                    return;
                }
                if (task instanceof AsyncRunnable) {
                    AsyncRunnable asyncTask = (AsyncRunnable) task;
                    asyncTask.run(this.runCompleteTask);
                } else {
                    task.run();
                    runComplete();
                }
            } catch (Throwable cause) {
                handleException(cause);
            }
        }

        private void handleException(final Throwable cause) {
            EventExecutor executor = SslHandler.this.ctx.executor();
            if (executor.inEventLoop()) {
                SslHandler.this.clearState(128);
                safeExceptionCaught(cause);
            } else {
                try {
                    executor.execute(new Runnable() { // from class: io.netty.handler.ssl.SslHandler.SslTasksRunner.3
                        @Override // java.lang.Runnable
                        public void run() {
                            SslHandler.this.clearState(128);
                            SslTasksRunner.this.safeExceptionCaught(cause);
                        }
                    });
                } catch (RejectedExecutionException e) {
                    SslHandler.this.clearState(128);
                    SslHandler.this.ctx.fireExceptionCaught(cause);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean setHandshakeSuccess() {
        boolean z = !this.handshakePromise.isDone() && this.handshakePromise.trySuccess(this.ctx.channel());
        boolean notified = z;
        if (z) {
            if (logger.isDebugEnabled()) {
                SSLSession session = this.engine.getSession();
                logger.debug("{} HANDSHAKEN: protocol:{} cipher suite:{}", this.ctx.channel(), session.getProtocol(), session.getCipherSuite());
            }
            this.ctx.fireUserEventTriggered((Object) SslHandshakeCompletionEvent.SUCCESS);
        }
        if (isStateSet(4)) {
            clearState(4);
            if (!this.ctx.channel().config().isAutoRead()) {
                this.ctx.read();
            }
        }
        return notified;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setHandshakeFailure(ChannelHandlerContext ctx, Throwable cause) {
        setHandshakeFailure(ctx, cause, true, true, false);
    }

    private void setHandshakeFailure(ChannelHandlerContext ctx, Throwable cause, boolean closeInbound, boolean notify, boolean alwaysFlushAndClose) {
        String msg;
        try {
            setState(32);
            this.engine.closeOutbound();
            if (closeInbound) {
                try {
                    this.engine.closeInbound();
                } catch (SSLException e) {
                    if (logger.isDebugEnabled() && ((msg = e.getMessage()) == null || (!msg.contains("possible truncation attack") && !msg.contains("closing inbound before receiving peer's close_notify")))) {
                        logger.debug("{} SSLEngine.closeInbound() raised an exception.", ctx.channel(), e);
                    }
                }
            }
            if (this.handshakePromise.tryFailure(cause) || alwaysFlushAndClose) {
                SslUtils.handleHandshakeFailure(ctx, cause, notify);
            }
        } finally {
            releaseAndFailAll(ctx, cause);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setHandshakeFailureTransportFailure(ChannelHandlerContext ctx, Throwable cause) {
        try {
            SSLException transportFailure = new SSLException("failure when writing TLS control frames", cause);
            releaseAndFailAll(ctx, transportFailure);
            if (this.handshakePromise.tryFailure(transportFailure)) {
                ctx.fireUserEventTriggered((Object) new SslHandshakeCompletionEvent(transportFailure));
            }
        } finally {
            ctx.close();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void releaseAndFailAll(ChannelHandlerContext ctx, Throwable cause) {
        if (this.pendingUnencryptedWrites != null) {
            this.pendingUnencryptedWrites.releaseAndFailAll(ctx, cause);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyClosePromise(Throwable cause) {
        if (cause == null) {
            if (this.sslClosePromise.trySuccess(this.ctx.channel())) {
                this.ctx.fireUserEventTriggered((Object) SslCloseCompletionEvent.SUCCESS);
            }
        } else if (this.sslClosePromise.tryFailure(cause)) {
            this.ctx.fireUserEventTriggered((Object) new SslCloseCompletionEvent(cause));
        }
    }

    private void closeOutboundAndChannel(ChannelHandlerContext ctx, final ChannelPromise promise, boolean disconnect) throws Exception {
        setState(32);
        this.engine.closeOutbound();
        if (!ctx.channel().isActive()) {
            if (disconnect) {
                ctx.disconnect(promise);
                return;
            } else {
                ctx.close(promise);
                return;
            }
        }
        ChannelPromise closeNotifyPromise = ctx.newPromise();
        try {
            flush(ctx, closeNotifyPromise);
            if (!isStateSet(64)) {
                setState(64);
                safeClose(ctx, closeNotifyPromise, (ChannelPromise) PromiseNotifier.cascade(false, ctx.newPromise(), promise));
            } else {
                this.sslClosePromise.addListener((GenericFutureListener) new FutureListener<Channel>() { // from class: io.netty.handler.ssl.SslHandler.5
                    @Override // io.netty.util.concurrent.GenericFutureListener
                    public void operationComplete(Future<Channel> future) {
                        promise.setSuccess();
                    }
                });
            }
        } catch (Throwable th) {
            if (!isStateSet(64)) {
                setState(64);
                safeClose(ctx, closeNotifyPromise, (ChannelPromise) PromiseNotifier.cascade(false, ctx.newPromise(), promise));
            } else {
                this.sslClosePromise.addListener((GenericFutureListener) new FutureListener<Channel>() { // from class: io.netty.handler.ssl.SslHandler.5
                    @Override // io.netty.util.concurrent.GenericFutureListener
                    public void operationComplete(Future<Channel> future) {
                        promise.setSuccess();
                    }
                });
            }
            throw th;
        }
    }

    private void flush(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        if (this.pendingUnencryptedWrites != null) {
            this.pendingUnencryptedWrites.add(Unpooled.EMPTY_BUFFER, promise);
        } else {
            promise.setFailure((Throwable) newPendingWritesNullException());
        }
        flush(ctx);
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
        Channel channel = ctx.channel();
        this.pendingUnencryptedWrites = new SslHandlerCoalescingBufferQueue(channel, 16);
        setOpensslEngineSocketFd(channel);
        boolean fastOpen = Boolean.TRUE.equals(channel.config().getOption(ChannelOption.TCP_FASTOPEN_CONNECT));
        boolean active = channel.isActive();
        if (active || fastOpen) {
            startHandshakeProcessing(active);
            if (fastOpen) {
                ChannelOutboundBuffer outboundBuffer = channel.unsafe().outboundBuffer();
                if (outboundBuffer == null || outboundBuffer.totalPendingWriteBytes() > 0) {
                    setState(16);
                }
            }
        }
    }

    private void startHandshakeProcessing(boolean flushAtEnd) {
        if (!isStateSet(8)) {
            setState(8);
            if (this.engine.getUseClientMode()) {
                handshake(flushAtEnd);
            }
            applyHandshakeTimeout();
            return;
        }
        if (isStateSet(16)) {
            forceFlush(this.ctx);
        }
    }

    public Future<Channel> renegotiate() {
        ChannelHandlerContext ctx = this.ctx;
        if (ctx == null) {
            throw new IllegalStateException();
        }
        return renegotiate(ctx.executor().newPromise());
    }

    public Future<Channel> renegotiate(final Promise<Channel> promise) {
        ObjectUtil.checkNotNull(promise, "promise");
        ChannelHandlerContext ctx = this.ctx;
        if (ctx == null) {
            throw new IllegalStateException();
        }
        EventExecutor executor = ctx.executor();
        if (!executor.inEventLoop()) {
            executor.execute(new Runnable() { // from class: io.netty.handler.ssl.SslHandler.6
                @Override // java.lang.Runnable
                public void run() {
                    SslHandler.this.renegotiateOnEventLoop(promise);
                }
            });
            return promise;
        }
        renegotiateOnEventLoop(promise);
        return promise;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void renegotiateOnEventLoop(Promise<Channel> newHandshakePromise) {
        Promise<Channel> oldHandshakePromise = this.handshakePromise;
        if (!oldHandshakePromise.isDone()) {
            PromiseNotifier.cascade(oldHandshakePromise, newHandshakePromise);
            return;
        }
        this.handshakePromise = newHandshakePromise;
        handshake(true);
        applyHandshakeTimeout();
    }

    private void handshake(boolean flushAtEnd) {
        if (this.engine.getHandshakeStatus() != SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING || this.handshakePromise.isDone()) {
            return;
        }
        ChannelHandlerContext ctx = this.ctx;
        try {
            this.engine.beginHandshake();
            wrapNonAppData(ctx, false);
            if (!flushAtEnd) {
            }
        } catch (Throwable e) {
            try {
                setHandshakeFailure(ctx, e);
            } finally {
                if (flushAtEnd) {
                    forceFlush(ctx);
                }
            }
        }
    }

    private void applyHandshakeTimeout() {
        final Promise<Channel> localHandshakePromise = this.handshakePromise;
        final long handshakeTimeoutMillis = this.handshakeTimeoutMillis;
        if (handshakeTimeoutMillis <= 0 || localHandshakePromise.isDone()) {
            return;
        }
        final Future<?> timeoutFuture = this.ctx.executor().schedule(new Runnable() { // from class: io.netty.handler.ssl.SslHandler.7
            @Override // java.lang.Runnable
            public void run() {
                if (localHandshakePromise.isDone()) {
                    return;
                }
                SSLException exception = new SslHandshakeTimeoutException("handshake timed out after " + handshakeTimeoutMillis + "ms");
                try {
                    if (localHandshakePromise.tryFailure(exception)) {
                        SslUtils.handleHandshakeFailure(SslHandler.this.ctx, exception, true);
                    }
                } finally {
                    SslHandler.this.releaseAndFailAll(SslHandler.this.ctx, exception);
                }
            }
        }, handshakeTimeoutMillis, TimeUnit.MILLISECONDS);
        localHandshakePromise.addListener((GenericFutureListener<? extends Future<? super Channel>>) new FutureListener<Channel>() { // from class: io.netty.handler.ssl.SslHandler.8
            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(Future<Channel> f) throws Exception {
                timeoutFuture.cancel(false);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void forceFlush(ChannelHandlerContext ctx) {
        clearState(16);
        ctx.flush();
    }

    private void setOpensslEngineSocketFd(Channel c) {
        if ((c instanceof UnixChannel) && (this.engine instanceof ReferenceCountedOpenSslEngine)) {
            ((ReferenceCountedOpenSslEngine) this.engine).bioSetFd(((UnixChannel) c).fd().intValue());
        }
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        setOpensslEngineSocketFd(ctx.channel());
        if (!this.startTls) {
            startHandshakeProcessing(true);
        }
        ctx.fireChannelActive();
    }

    private void safeClose(final ChannelHandlerContext ctx, final ChannelFuture flushFuture, final ChannelPromise promise) {
        final Future<?> timeoutFuture;
        if (!ctx.channel().isActive()) {
            ctx.close(promise);
            return;
        }
        if (!flushFuture.isDone()) {
            long closeNotifyTimeout = this.closeNotifyFlushTimeoutMillis;
            if (closeNotifyTimeout > 0) {
                timeoutFuture = ctx.executor().schedule(new Runnable() { // from class: io.netty.handler.ssl.SslHandler.9
                    @Override // java.lang.Runnable
                    public void run() {
                        if (!flushFuture.isDone()) {
                            SslHandler.logger.warn("{} Last write attempt timed out; force-closing the connection.", ctx.channel());
                            SslHandler.addCloseListener(ctx.close(ctx.newPromise()), promise);
                        }
                    }
                }, closeNotifyTimeout, TimeUnit.MILLISECONDS);
            } else {
                timeoutFuture = null;
            }
        } else {
            timeoutFuture = null;
        }
        flushFuture.addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.handler.ssl.SslHandler.10
            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(ChannelFuture f) {
                final Future<?> closeNotifyReadTimeoutFuture;
                if (timeoutFuture != null) {
                    timeoutFuture.cancel(false);
                }
                final long closeNotifyReadTimeout = SslHandler.this.closeNotifyReadTimeoutMillis;
                if (closeNotifyReadTimeout <= 0) {
                    SslHandler.addCloseListener(ctx.close(ctx.newPromise()), promise);
                    return;
                }
                if (!SslHandler.this.sslClosePromise.isDone()) {
                    closeNotifyReadTimeoutFuture = ctx.executor().schedule(new Runnable() { // from class: io.netty.handler.ssl.SslHandler.10.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (!SslHandler.this.sslClosePromise.isDone()) {
                                SslHandler.logger.debug("{} did not receive close_notify in {}ms; force-closing the connection.", ctx.channel(), Long.valueOf(closeNotifyReadTimeout));
                                SslHandler.addCloseListener(ctx.close(ctx.newPromise()), promise);
                            }
                        }
                    }, closeNotifyReadTimeout, TimeUnit.MILLISECONDS);
                } else {
                    closeNotifyReadTimeoutFuture = null;
                }
                SslHandler.this.sslClosePromise.addListener((GenericFutureListener) new FutureListener<Channel>() { // from class: io.netty.handler.ssl.SslHandler.10.2
                    @Override // io.netty.util.concurrent.GenericFutureListener
                    public void operationComplete(Future<Channel> future) throws Exception {
                        if (closeNotifyReadTimeoutFuture != null) {
                            closeNotifyReadTimeoutFuture.cancel(false);
                        }
                        SslHandler.addCloseListener(ctx.close(ctx.newPromise()), promise);
                    }
                });
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void addCloseListener(ChannelFuture future, ChannelPromise promise) {
        PromiseNotifier.cascade(false, future, promise);
    }

    private ByteBuf allocate(ChannelHandlerContext ctx, int capacity) {
        ByteBufAllocator alloc = ctx.alloc();
        if (this.engineType.wantsDirectBuffer) {
            return alloc.directBuffer(capacity);
        }
        return alloc.buffer(capacity);
    }

    private ByteBuf allocateOutNetBuf(ChannelHandlerContext ctx, int pendingBytes, int numComponents) {
        return this.engineType.allocateWrapBuffer(this, ctx.alloc(), pendingBytes, numComponents);
    }

    private boolean isStateSet(int bit) {
        return (this.state & bit) == bit;
    }

    private void setState(int bit) {
        this.state = (short) (this.state | bit);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearState(int bit) {
        this.state = (short) (this.state & (~bit));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class SslHandlerCoalescingBufferQueue extends AbstractCoalescingBufferQueue {
        SslHandlerCoalescingBufferQueue(Channel channel, int initSize) {
            super(channel, initSize);
        }

        @Override // io.netty.channel.AbstractCoalescingBufferQueue
        protected ByteBuf compose(ByteBufAllocator alloc, ByteBuf cumulation, ByteBuf next) {
            int wrapDataSize = SslHandler.this.wrapDataSize;
            if (!(cumulation instanceof CompositeByteBuf)) {
                return SslHandler.attemptCopyToCumulation(cumulation, next, wrapDataSize) ? cumulation : copyAndCompose(alloc, cumulation, next);
            }
            CompositeByteBuf composite = (CompositeByteBuf) cumulation;
            int numComponents = composite.numComponents();
            if (numComponents == 0 || !SslHandler.attemptCopyToCumulation(composite.internalComponent(numComponents - 1), next, wrapDataSize)) {
                composite.addComponent(true, next);
            }
            return composite;
        }

        @Override // io.netty.channel.AbstractCoalescingBufferQueue
        protected ByteBuf composeFirst(ByteBufAllocator allocator, ByteBuf first) {
            if (first instanceof CompositeByteBuf) {
                CompositeByteBuf composite = (CompositeByteBuf) first;
                if (SslHandler.this.engineType.wantsDirectBuffer) {
                    first = allocator.directBuffer(composite.readableBytes());
                } else {
                    first = allocator.heapBuffer(composite.readableBytes());
                }
                try {
                    first.writeBytes(composite);
                } catch (Throwable cause) {
                    first.release();
                    PlatformDependent.throwException(cause);
                }
                composite.release();
            }
            return first;
        }

        @Override // io.netty.channel.AbstractCoalescingBufferQueue
        protected ByteBuf removeEmptyValue() {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean attemptCopyToCumulation(ByteBuf cumulation, ByteBuf next, int wrapDataSize) {
        int inReadableBytes = next.readableBytes();
        int cumulationCapacity = cumulation.capacity();
        if (wrapDataSize - cumulation.readableBytes() < inReadableBytes || ((!cumulation.isWritable(inReadableBytes) || cumulationCapacity < wrapDataSize) && (cumulationCapacity >= wrapDataSize || !ByteBufUtil.ensureWritableSuccess(cumulation.ensureWritable(inReadableBytes, false))))) {
            return false;
        }
        cumulation.writeBytes(next);
        next.release();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class LazyChannelPromise extends DefaultPromise<Channel> {
        private LazyChannelPromise() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // io.netty.util.concurrent.DefaultPromise
        public EventExecutor executor() {
            if (SslHandler.this.ctx != null) {
                return SslHandler.this.ctx.executor();
            }
            throw new IllegalStateException();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // io.netty.util.concurrent.DefaultPromise
        public void checkDeadLock() {
            if (SslHandler.this.ctx == null) {
                return;
            }
            super.checkDeadLock();
        }
    }
}
