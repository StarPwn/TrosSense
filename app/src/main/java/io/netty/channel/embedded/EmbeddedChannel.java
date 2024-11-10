package io.netty.channel.embedded;

import io.netty.channel.AbstractChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.DefaultChannelPipeline;
import io.netty.channel.EventLoop;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.RecyclableArrayList;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.SocketAddress;
import java.nio.channels.ClosedChannelException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

/* loaded from: classes4.dex */
public class EmbeddedChannel extends AbstractChannel {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final ChannelConfig config;
    private Queue<Object> inboundMessages;
    private Throwable lastException;
    private final EmbeddedEventLoop loop;
    private final ChannelMetadata metadata;
    private Queue<Object> outboundMessages;
    private final ChannelFutureListener recordExceptionListener;
    private State state;
    private static final SocketAddress LOCAL_ADDRESS = new EmbeddedSocketAddress();
    private static final SocketAddress REMOTE_ADDRESS = new EmbeddedSocketAddress();
    private static final ChannelHandler[] EMPTY_HANDLERS = new ChannelHandler[0];
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) EmbeddedChannel.class);
    private static final ChannelMetadata METADATA_NO_DISCONNECT = new ChannelMetadata(false);
    private static final ChannelMetadata METADATA_DISCONNECT = new ChannelMetadata(true);

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public enum State {
        OPEN,
        ACTIVE,
        CLOSED
    }

    public EmbeddedChannel() {
        this(EMPTY_HANDLERS);
    }

    public EmbeddedChannel(ChannelId channelId) {
        this(channelId, EMPTY_HANDLERS);
    }

    public EmbeddedChannel(ChannelHandler... handlers) {
        this(EmbeddedChannelId.INSTANCE, handlers);
    }

    public EmbeddedChannel(boolean hasDisconnect, ChannelHandler... handlers) {
        this(EmbeddedChannelId.INSTANCE, hasDisconnect, handlers);
    }

    public EmbeddedChannel(boolean register, boolean hasDisconnect, ChannelHandler... handlers) {
        this(EmbeddedChannelId.INSTANCE, register, hasDisconnect, handlers);
    }

    public EmbeddedChannel(ChannelId channelId, ChannelHandler... handlers) {
        this(channelId, false, handlers);
    }

    public EmbeddedChannel(ChannelId channelId, boolean hasDisconnect, ChannelHandler... handlers) {
        this(channelId, true, hasDisconnect, handlers);
    }

    public EmbeddedChannel(ChannelId channelId, boolean register, boolean hasDisconnect, ChannelHandler... handlers) {
        this(null, channelId, register, hasDisconnect, handlers);
    }

    public EmbeddedChannel(Channel parent, ChannelId channelId, boolean register, boolean hasDisconnect, ChannelHandler... handlers) {
        super(parent, channelId);
        this.loop = new EmbeddedEventLoop();
        this.recordExceptionListener = new ChannelFutureListener() { // from class: io.netty.channel.embedded.EmbeddedChannel.1
            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(ChannelFuture future) throws Exception {
                EmbeddedChannel.this.recordException(future);
            }
        };
        this.metadata = metadata(hasDisconnect);
        this.config = new DefaultChannelConfig(this);
        setup(register, handlers);
    }

    public EmbeddedChannel(ChannelId channelId, boolean hasDisconnect, ChannelConfig config, ChannelHandler... handlers) {
        super(null, channelId);
        this.loop = new EmbeddedEventLoop();
        this.recordExceptionListener = new ChannelFutureListener() { // from class: io.netty.channel.embedded.EmbeddedChannel.1
            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(ChannelFuture future) throws Exception {
                EmbeddedChannel.this.recordException(future);
            }
        };
        this.metadata = metadata(hasDisconnect);
        this.config = (ChannelConfig) ObjectUtil.checkNotNull(config, "config");
        setup(true, handlers);
    }

    private static ChannelMetadata metadata(boolean hasDisconnect) {
        return hasDisconnect ? METADATA_DISCONNECT : METADATA_NO_DISCONNECT;
    }

    private void setup(boolean register, final ChannelHandler... handlers) {
        ObjectUtil.checkNotNull(handlers, "handlers");
        ChannelPipeline p = pipeline();
        p.addLast(new ChannelInitializer<Channel>() { // from class: io.netty.channel.embedded.EmbeddedChannel.2
            @Override // io.netty.channel.ChannelInitializer
            protected void initChannel(Channel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                for (ChannelHandler h : handlers) {
                    if (h != null) {
                        pipeline.addLast(h);
                    } else {
                        return;
                    }
                }
            }
        });
        if (register) {
            ChannelFuture future = this.loop.register(this);
            if (!future.isDone()) {
                throw new AssertionError();
            }
        }
    }

    public void register() throws Exception {
        ChannelFuture future = this.loop.register(this);
        if (!future.isDone()) {
            throw new AssertionError();
        }
        Throwable cause = future.cause();
        if (cause != null) {
            PlatformDependent.throwException(cause);
        }
    }

    @Override // io.netty.channel.AbstractChannel
    protected final DefaultChannelPipeline newChannelPipeline() {
        return new EmbeddedChannelPipeline(this);
    }

    @Override // io.netty.channel.Channel
    public ChannelMetadata metadata() {
        return this.metadata;
    }

    @Override // io.netty.channel.Channel
    public ChannelConfig config() {
        return this.config;
    }

    @Override // io.netty.channel.Channel
    public boolean isOpen() {
        return this.state != State.CLOSED;
    }

    @Override // io.netty.channel.Channel
    public boolean isActive() {
        return this.state == State.ACTIVE;
    }

    public Queue<Object> inboundMessages() {
        if (this.inboundMessages == null) {
            this.inboundMessages = new ArrayDeque();
        }
        return this.inboundMessages;
    }

    @Deprecated
    public Queue<Object> lastInboundBuffer() {
        return inboundMessages();
    }

    public Queue<Object> outboundMessages() {
        if (this.outboundMessages == null) {
            this.outboundMessages = new ArrayDeque();
        }
        return this.outboundMessages;
    }

    @Deprecated
    public Queue<Object> lastOutboundBuffer() {
        return outboundMessages();
    }

    public <T> T readInbound() {
        T t = (T) poll(this.inboundMessages);
        if (t != null) {
            ReferenceCountUtil.touch(t, "Caller of readInbound() will handle the message from this point");
        }
        return t;
    }

    public <T> T readOutbound() {
        T t = (T) poll(this.outboundMessages);
        if (t != null) {
            ReferenceCountUtil.touch(t, "Caller of readOutbound() will handle the message from this point.");
        }
        return t;
    }

    public boolean writeInbound(Object... msgs) {
        ensureOpen();
        if (msgs.length == 0) {
            return isNotEmpty(this.inboundMessages);
        }
        ChannelPipeline p = pipeline();
        for (Object m : msgs) {
            p.fireChannelRead(m);
        }
        flushInbound(false, voidPromise());
        return isNotEmpty(this.inboundMessages);
    }

    public ChannelFuture writeOneInbound(Object msg) {
        return writeOneInbound(msg, newPromise());
    }

    public ChannelFuture writeOneInbound(Object msg, ChannelPromise promise) {
        if (checkOpen(true)) {
            pipeline().fireChannelRead(msg);
        }
        return checkException(promise);
    }

    public EmbeddedChannel flushInbound() {
        flushInbound(true, voidPromise());
        return this;
    }

    private ChannelFuture flushInbound(boolean recordException, ChannelPromise promise) {
        if (checkOpen(recordException)) {
            pipeline().fireChannelReadComplete();
            runPendingTasks();
        }
        return checkException(promise);
    }

    public boolean writeOutbound(Object... msgs) {
        ensureOpen();
        if (msgs.length == 0) {
            return isNotEmpty(this.outboundMessages);
        }
        RecyclableArrayList futures = RecyclableArrayList.newInstance(msgs.length);
        try {
            for (Object m : msgs) {
                if (m == null) {
                    break;
                }
                futures.add(write(m));
            }
            flushOutbound0();
            int size = futures.size();
            for (int i = 0; i < size; i++) {
                ChannelFuture future = (ChannelFuture) futures.get(i);
                if (future.isDone()) {
                    recordException(future);
                } else {
                    future.addListener((GenericFutureListener<? extends Future<? super Void>>) this.recordExceptionListener);
                }
            }
            checkException();
            return isNotEmpty(this.outboundMessages);
        } finally {
            futures.recycle();
        }
    }

    public ChannelFuture writeOneOutbound(Object msg) {
        return writeOneOutbound(msg, newPromise());
    }

    public ChannelFuture writeOneOutbound(Object msg, ChannelPromise promise) {
        if (checkOpen(true)) {
            return write(msg, promise);
        }
        return checkException(promise);
    }

    public EmbeddedChannel flushOutbound() {
        if (checkOpen(true)) {
            flushOutbound0();
        }
        checkException(voidPromise());
        return this;
    }

    private void flushOutbound0() {
        runPendingTasks();
        flush();
    }

    public boolean finish() {
        return finish(false);
    }

    public boolean finishAndReleaseAll() {
        return finish(true);
    }

    /* JADX WARN: Removed duplicated region for block: B:9:0x001c A[DONT_GENERATE] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean finish(boolean r3) {
        /*
            r2 = this;
            r2.close()
            r2.checkException()     // Catch: java.lang.Throwable -> L27
            java.util.Queue<java.lang.Object> r0 = r2.inboundMessages     // Catch: java.lang.Throwable -> L27
            boolean r0 = isNotEmpty(r0)     // Catch: java.lang.Throwable -> L27
            if (r0 != 0) goto L19
            java.util.Queue<java.lang.Object> r0 = r2.outboundMessages     // Catch: java.lang.Throwable -> L27
            boolean r0 = isNotEmpty(r0)     // Catch: java.lang.Throwable -> L27
            if (r0 == 0) goto L17
            goto L19
        L17:
            r0 = 0
            goto L1a
        L19:
            r0 = 1
        L1a:
            if (r3 == 0) goto L26
            java.util.Queue<java.lang.Object> r1 = r2.inboundMessages
            releaseAll(r1)
            java.util.Queue<java.lang.Object> r1 = r2.outboundMessages
            releaseAll(r1)
        L26:
            return r0
        L27:
            r0 = move-exception
            if (r3 == 0) goto L34
            java.util.Queue<java.lang.Object> r1 = r2.inboundMessages
            releaseAll(r1)
            java.util.Queue<java.lang.Object> r1 = r2.outboundMessages
            releaseAll(r1)
        L34:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.channel.embedded.EmbeddedChannel.finish(boolean):boolean");
    }

    public boolean releaseInbound() {
        return releaseAll(this.inboundMessages);
    }

    public boolean releaseOutbound() {
        return releaseAll(this.outboundMessages);
    }

    private static boolean releaseAll(Queue<Object> queue) {
        if (!isNotEmpty(queue)) {
            return false;
        }
        while (true) {
            Object msg = queue.poll();
            if (msg != null) {
                ReferenceCountUtil.release(msg);
            } else {
                return true;
            }
        }
    }

    private void finishPendingTasks(boolean cancel) {
        runPendingTasks();
        if (cancel) {
            embeddedEventLoop().cancelScheduledTasks();
        }
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.ChannelOutboundInvoker
    public final ChannelFuture close() {
        return close(newPromise());
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.ChannelOutboundInvoker
    public final ChannelFuture disconnect() {
        return disconnect(newPromise());
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.ChannelOutboundInvoker
    public final ChannelFuture close(ChannelPromise promise) {
        runPendingTasks();
        ChannelFuture future = super.close(promise);
        finishPendingTasks(true);
        return future;
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.ChannelOutboundInvoker
    public final ChannelFuture disconnect(ChannelPromise promise) {
        ChannelFuture future = super.disconnect(promise);
        finishPendingTasks(!this.metadata.hasDisconnect());
        return future;
    }

    private static boolean isNotEmpty(Queue<Object> queue) {
        return (queue == null || queue.isEmpty()) ? false : true;
    }

    private static Object poll(Queue<Object> queue) {
        if (queue != null) {
            return queue.poll();
        }
        return null;
    }

    public void runPendingTasks() {
        try {
            embeddedEventLoop().runTasks();
        } catch (Exception e) {
            recordException(e);
        }
        try {
            embeddedEventLoop().runScheduledTasks();
        } catch (Exception e2) {
            recordException(e2);
        }
    }

    public boolean hasPendingTasks() {
        return embeddedEventLoop().hasPendingNormalTasks() || embeddedEventLoop().nextScheduledTask() == 0;
    }

    public long runScheduledPendingTasks() {
        try {
            return embeddedEventLoop().runScheduledTasks();
        } catch (Exception e) {
            recordException(e);
            return embeddedEventLoop().nextScheduledTask();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordException(ChannelFuture future) {
        if (!future.isSuccess()) {
            recordException(future.cause());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordException(Throwable cause) {
        if (this.lastException == null) {
            this.lastException = cause;
        } else {
            logger.warn("More than one exception was raised. Will report only the first one and log others.", cause);
        }
    }

    public void advanceTimeBy(long duration, TimeUnit unit) {
        embeddedEventLoop().advanceTimeBy(unit.toNanos(duration));
    }

    public void freezeTime() {
        embeddedEventLoop().freezeTime();
    }

    public void unfreezeTime() {
        embeddedEventLoop().unfreezeTime();
    }

    private ChannelFuture checkException(ChannelPromise promise) {
        Throwable t = this.lastException;
        if (t != null) {
            this.lastException = null;
            if (promise.isVoid()) {
                PlatformDependent.throwException(t);
            }
            return promise.setFailure(t);
        }
        return promise.setSuccess();
    }

    public void checkException() {
        checkException(voidPromise());
    }

    private boolean checkOpen(boolean recordException) {
        if (!isOpen()) {
            if (recordException) {
                recordException(new ClosedChannelException());
                return false;
            }
            return false;
        }
        return true;
    }

    private EmbeddedEventLoop embeddedEventLoop() {
        if (isRegistered()) {
            return (EmbeddedEventLoop) super.eventLoop();
        }
        return this.loop;
    }

    protected final void ensureOpen() {
        if (!checkOpen(true)) {
            checkException();
        }
    }

    @Override // io.netty.channel.AbstractChannel
    protected boolean isCompatible(EventLoop loop) {
        return loop instanceof EmbeddedEventLoop;
    }

    @Override // io.netty.channel.AbstractChannel
    protected SocketAddress localAddress0() {
        if (isActive()) {
            return LOCAL_ADDRESS;
        }
        return null;
    }

    @Override // io.netty.channel.AbstractChannel
    protected SocketAddress remoteAddress0() {
        if (isActive()) {
            return REMOTE_ADDRESS;
        }
        return null;
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doRegister() throws Exception {
        this.state = State.ACTIVE;
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doBind(SocketAddress localAddress) throws Exception {
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doDisconnect() throws Exception {
        if (!this.metadata.hasDisconnect()) {
            doClose();
        }
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doClose() throws Exception {
        this.state = State.CLOSED;
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doBeginRead() throws Exception {
    }

    @Override // io.netty.channel.AbstractChannel
    protected AbstractChannel.AbstractUnsafe newUnsafe() {
        return new EmbeddedUnsafe();
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public Channel.Unsafe unsafe() {
        return ((EmbeddedUnsafe) super.unsafe()).wrapped;
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doWrite(ChannelOutboundBuffer in) throws Exception {
        while (true) {
            Object msg = in.current();
            if (msg != null) {
                ReferenceCountUtil.retain(msg);
                handleOutboundMessage(msg);
                in.remove();
            } else {
                return;
            }
        }
    }

    protected void handleOutboundMessage(Object msg) {
        outboundMessages().add(msg);
    }

    protected void handleInboundMessage(Object msg) {
        inboundMessages().add(msg);
    }

    /* loaded from: classes4.dex */
    private final class EmbeddedUnsafe extends AbstractChannel.AbstractUnsafe {
        final Channel.Unsafe wrapped;

        private EmbeddedUnsafe() {
            super();
            this.wrapped = new Channel.Unsafe() { // from class: io.netty.channel.embedded.EmbeddedChannel.EmbeddedUnsafe.1
                @Override // io.netty.channel.Channel.Unsafe
                public RecvByteBufAllocator.Handle recvBufAllocHandle() {
                    return EmbeddedUnsafe.this.recvBufAllocHandle();
                }

                @Override // io.netty.channel.Channel.Unsafe
                public SocketAddress localAddress() {
                    return EmbeddedUnsafe.this.localAddress();
                }

                @Override // io.netty.channel.Channel.Unsafe
                public SocketAddress remoteAddress() {
                    return EmbeddedUnsafe.this.remoteAddress();
                }

                @Override // io.netty.channel.Channel.Unsafe
                public void register(EventLoop eventLoop, ChannelPromise promise) {
                    EmbeddedUnsafe.this.register(eventLoop, promise);
                    EmbeddedChannel.this.runPendingTasks();
                }

                @Override // io.netty.channel.Channel.Unsafe
                public void bind(SocketAddress localAddress, ChannelPromise promise) {
                    EmbeddedUnsafe.this.bind(localAddress, promise);
                    EmbeddedChannel.this.runPendingTasks();
                }

                @Override // io.netty.channel.Channel.Unsafe
                public void connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
                    EmbeddedUnsafe.this.connect(remoteAddress, localAddress, promise);
                    EmbeddedChannel.this.runPendingTasks();
                }

                @Override // io.netty.channel.Channel.Unsafe
                public void disconnect(ChannelPromise promise) {
                    EmbeddedUnsafe.this.disconnect(promise);
                    EmbeddedChannel.this.runPendingTasks();
                }

                @Override // io.netty.channel.Channel.Unsafe
                public void close(ChannelPromise promise) {
                    EmbeddedUnsafe.this.close(promise);
                    EmbeddedChannel.this.runPendingTasks();
                }

                @Override // io.netty.channel.Channel.Unsafe
                public void closeForcibly() {
                    EmbeddedUnsafe.this.closeForcibly();
                    EmbeddedChannel.this.runPendingTasks();
                }

                @Override // io.netty.channel.Channel.Unsafe
                public void deregister(ChannelPromise promise) {
                    EmbeddedUnsafe.this.deregister(promise);
                    EmbeddedChannel.this.runPendingTasks();
                }

                @Override // io.netty.channel.Channel.Unsafe
                public void beginRead() {
                    EmbeddedUnsafe.this.beginRead();
                    EmbeddedChannel.this.runPendingTasks();
                }

                @Override // io.netty.channel.Channel.Unsafe
                public void write(Object msg, ChannelPromise promise) {
                    EmbeddedUnsafe.this.write(msg, promise);
                    EmbeddedChannel.this.runPendingTasks();
                }

                @Override // io.netty.channel.Channel.Unsafe
                public void flush() {
                    EmbeddedUnsafe.this.flush();
                    EmbeddedChannel.this.runPendingTasks();
                }

                @Override // io.netty.channel.Channel.Unsafe
                public ChannelPromise voidPromise() {
                    return EmbeddedUnsafe.this.voidPromise();
                }

                @Override // io.netty.channel.Channel.Unsafe
                public ChannelOutboundBuffer outboundBuffer() {
                    return EmbeddedUnsafe.this.outboundBuffer();
                }
            };
        }

        @Override // io.netty.channel.Channel.Unsafe
        public void connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
            safeSetSuccess(promise);
        }
    }

    /* loaded from: classes4.dex */
    private final class EmbeddedChannelPipeline extends DefaultChannelPipeline {
        EmbeddedChannelPipeline(EmbeddedChannel channel) {
            super(channel);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // io.netty.channel.DefaultChannelPipeline
        public void onUnhandledInboundException(Throwable cause) {
            EmbeddedChannel.this.recordException(cause);
        }

        @Override // io.netty.channel.DefaultChannelPipeline
        protected void onUnhandledInboundMessage(ChannelHandlerContext ctx, Object msg) {
            EmbeddedChannel.this.handleInboundMessage(msg);
        }
    }
}
