package io.netty.handler.codec.http2;

import androidx.core.app.NotificationCompat;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelProgressivePromise;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.DefaultChannelPipeline;
import io.netty.channel.EventLoop;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.VoidChannelPromise;
import io.netty.channel.socket.ChannelInputShutdownReadComplete;
import io.netty.channel.socket.ChannelOutputShutdownEvent;
import io.netty.handler.codec.http2.Http2FrameCodec;
import io.netty.handler.ssl.SslCloseCompletionEvent;
import io.netty.util.DefaultAttributeMap;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.ClosedChannelException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public abstract class AbstractHttp2StreamChannel extends DefaultAttributeMap implements Http2StreamChannel {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int MIN_HTTP2_FRAME_SIZE = 9;
    private final ChannelId channelId;
    private final ChannelPromise closePromise;
    private Runnable fireChannelWritabilityChangedTask;
    private boolean firstFrameWritten;
    private int flowControlledBytes;
    private Queue<Object> inboundBuffer;
    private boolean outboundClosed;
    private final ChannelPipeline pipeline;
    private boolean readCompletePending;
    private volatile boolean registered;
    private final Http2FrameCodec.DefaultHttp2FrameStream stream;
    private volatile long totalPendingSize;
    private volatile int unwritable;
    static final Http2FrameStreamVisitor WRITABLE_VISITOR = new Http2FrameStreamVisitor() { // from class: io.netty.handler.codec.http2.AbstractHttp2StreamChannel.1
        @Override // io.netty.handler.codec.http2.Http2FrameStreamVisitor
        public boolean visit(Http2FrameStream stream) {
            AbstractHttp2StreamChannel childChannel = (AbstractHttp2StreamChannel) ((Http2FrameCodec.DefaultHttp2FrameStream) stream).attachment;
            childChannel.trySetWritable();
            return true;
        }
    };
    static final Http2FrameStreamVisitor CHANNEL_INPUT_SHUTDOWN_READ_COMPLETE_VISITOR = new UserEventStreamVisitor(ChannelInputShutdownReadComplete.INSTANCE);
    static final Http2FrameStreamVisitor CHANNEL_OUTPUT_SHUTDOWN_EVENT_VISITOR = new UserEventStreamVisitor(ChannelOutputShutdownEvent.INSTANCE);
    static final Http2FrameStreamVisitor SSL_CLOSE_COMPLETION_EVENT_VISITOR = new UserEventStreamVisitor(SslCloseCompletionEvent.SUCCESS);
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) AbstractHttp2StreamChannel.class);
    private static final ChannelMetadata METADATA = new ChannelMetadata(false, 16);
    private static final AtomicLongFieldUpdater<AbstractHttp2StreamChannel> TOTAL_PENDING_SIZE_UPDATER = AtomicLongFieldUpdater.newUpdater(AbstractHttp2StreamChannel.class, "totalPendingSize");
    private static final AtomicIntegerFieldUpdater<AbstractHttp2StreamChannel> UNWRITABLE_UPDATER = AtomicIntegerFieldUpdater.newUpdater(AbstractHttp2StreamChannel.class, "unwritable");
    private final ChannelFutureListener windowUpdateFrameWriteListener = new ChannelFutureListener() { // from class: io.netty.handler.codec.http2.AbstractHttp2StreamChannel.2
        @Override // io.netty.util.concurrent.GenericFutureListener
        public void operationComplete(ChannelFuture future) {
            AbstractHttp2StreamChannel.windowUpdateFrameWriteComplete(future, AbstractHttp2StreamChannel.this);
        }
    };
    private final Http2StreamChannelConfig config = new Http2StreamChannelConfig(this);
    private final Http2ChannelUnsafe unsafe = new Http2ChannelUnsafe();
    private ReadStatus readStatus = ReadStatus.IDLE;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public enum ReadStatus {
        IDLE,
        IN_PROGRESS,
        REQUESTED
    }

    protected abstract void addChannelToReadCompletePendingQueue();

    protected abstract boolean isParentReadInProgress();

    protected abstract ChannelHandlerContext parentContext();

    /* loaded from: classes4.dex */
    private static final class UserEventStreamVisitor implements Http2FrameStreamVisitor {
        private final Object event;

        UserEventStreamVisitor(Object event) {
            this.event = ObjectUtil.checkNotNull(event, NotificationCompat.CATEGORY_EVENT);
        }

        @Override // io.netty.handler.codec.http2.Http2FrameStreamVisitor
        public boolean visit(Http2FrameStream stream) {
            AbstractHttp2StreamChannel childChannel = (AbstractHttp2StreamChannel) ((Http2FrameCodec.DefaultHttp2FrameStream) stream).attachment;
            childChannel.pipeline().fireUserEventTriggered(this.event);
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class FlowControlledFrameSizeEstimator implements MessageSizeEstimator {
        static final FlowControlledFrameSizeEstimator INSTANCE = new FlowControlledFrameSizeEstimator();
        private static final MessageSizeEstimator.Handle HANDLE_INSTANCE = new MessageSizeEstimator.Handle() { // from class: io.netty.handler.codec.http2.AbstractHttp2StreamChannel.FlowControlledFrameSizeEstimator.1
            @Override // io.netty.channel.MessageSizeEstimator.Handle
            public int size(Object msg) {
                if (msg instanceof Http2DataFrame) {
                    return (int) Math.min(2147483647L, ((Http2DataFrame) msg).initialFlowControlledBytes() + 9);
                }
                return 9;
            }
        };

        private FlowControlledFrameSizeEstimator() {
        }

        @Override // io.netty.channel.MessageSizeEstimator
        public MessageSizeEstimator.Handle newHandle() {
            return HANDLE_INSTANCE;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void windowUpdateFrameWriteComplete(ChannelFuture future, Channel streamChannel) {
        Throwable unwrappedCause;
        Throwable cause = future.cause();
        if (cause != null) {
            if ((cause instanceof Http2FrameStreamException) && (unwrappedCause = cause.getCause()) != null) {
                cause = unwrappedCause;
            }
            streamChannel.pipeline().fireExceptionCaught(cause);
            streamChannel.unsafe().close(streamChannel.unsafe().voidPromise());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AbstractHttp2StreamChannel(Http2FrameCodec.DefaultHttp2FrameStream stream, int id, ChannelHandler inboundHandler) {
        this.stream = stream;
        stream.attachment = this;
        this.pipeline = new DefaultChannelPipeline(this) { // from class: io.netty.handler.codec.http2.AbstractHttp2StreamChannel.3
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // io.netty.channel.DefaultChannelPipeline
            public void incrementPendingOutboundBytes(long size) {
                AbstractHttp2StreamChannel.this.incrementPendingOutboundBytes(size, true);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // io.netty.channel.DefaultChannelPipeline
            public void decrementPendingOutboundBytes(long size) {
                AbstractHttp2StreamChannel.this.decrementPendingOutboundBytes(size, true);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // io.netty.channel.DefaultChannelPipeline
            public void onUnhandledInboundException(Throwable cause) {
                if (cause instanceof Http2FrameStreamException) {
                    AbstractHttp2StreamChannel.this.closeWithError(((Http2FrameStreamException) cause).error());
                    return;
                }
                Http2Exception exception = Http2CodecUtil.getEmbeddedHttp2Exception(cause);
                if (exception != null) {
                    AbstractHttp2StreamChannel.this.closeWithError(exception.error());
                } else {
                    super.onUnhandledInboundException(cause);
                }
            }
        };
        this.closePromise = this.pipeline.newPromise();
        this.channelId = new Http2StreamChannelId(parent().id(), id);
        if (inboundHandler != null) {
            this.pipeline.addLast(inboundHandler);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void incrementPendingOutboundBytes(long size, boolean invokeLater) {
        if (size == 0) {
            return;
        }
        long newWriteBufferSize = TOTAL_PENDING_SIZE_UPDATER.addAndGet(this, size);
        if (newWriteBufferSize > config().getWriteBufferHighWaterMark()) {
            setUnwritable(invokeLater);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void decrementPendingOutboundBytes(long size, boolean invokeLater) {
        if (size == 0) {
            return;
        }
        long newWriteBufferSize = TOTAL_PENDING_SIZE_UPDATER.addAndGet(this, -size);
        if (newWriteBufferSize < config().getWriteBufferLowWaterMark() && parent().isWritable()) {
            setWritable(invokeLater);
        }
    }

    final void trySetWritable() {
        if (this.totalPendingSize < config().getWriteBufferLowWaterMark()) {
            setWritable(false);
        }
    }

    private void setWritable(boolean invokeLater) {
        int oldValue;
        int newValue;
        do {
            oldValue = this.unwritable;
            newValue = oldValue & (-2);
        } while (!UNWRITABLE_UPDATER.compareAndSet(this, oldValue, newValue));
        if (oldValue != 0 && newValue == 0) {
            fireChannelWritabilityChanged(invokeLater);
        }
    }

    private void setUnwritable(boolean invokeLater) {
        int oldValue;
        int newValue;
        do {
            oldValue = this.unwritable;
            newValue = oldValue | 1;
        } while (!UNWRITABLE_UPDATER.compareAndSet(this, oldValue, newValue));
        if (oldValue == 0) {
            fireChannelWritabilityChanged(invokeLater);
        }
    }

    private void fireChannelWritabilityChanged(boolean invokeLater) {
        final ChannelPipeline pipeline = pipeline();
        if (invokeLater) {
            Runnable task = this.fireChannelWritabilityChangedTask;
            if (task == null) {
                Runnable runnable = new Runnable() { // from class: io.netty.handler.codec.http2.AbstractHttp2StreamChannel.4
                    @Override // java.lang.Runnable
                    public void run() {
                        pipeline.fireChannelWritabilityChanged();
                    }
                };
                task = runnable;
                this.fireChannelWritabilityChangedTask = runnable;
            }
            eventLoop().execute(task);
            return;
        }
        pipeline.fireChannelWritabilityChanged();
    }

    @Override // io.netty.handler.codec.http2.Http2StreamChannel
    public Http2FrameStream stream() {
        return this.stream;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void closeOutbound() {
        this.outboundClosed = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void streamClosed() {
        this.unsafe.readEOS();
        this.unsafe.doBeginRead();
    }

    @Override // io.netty.channel.Channel
    public ChannelMetadata metadata() {
        return METADATA;
    }

    @Override // io.netty.channel.Channel
    public ChannelConfig config() {
        return this.config;
    }

    @Override // io.netty.channel.Channel
    public boolean isOpen() {
        return !this.closePromise.isDone();
    }

    @Override // io.netty.channel.Channel
    public boolean isActive() {
        return isOpen();
    }

    @Override // io.netty.channel.Channel
    public boolean isWritable() {
        return this.unwritable == 0;
    }

    @Override // io.netty.channel.Channel
    public ChannelId id() {
        return this.channelId;
    }

    @Override // io.netty.channel.Channel
    public EventLoop eventLoop() {
        return parent().eventLoop();
    }

    @Override // io.netty.channel.Channel
    public Channel parent() {
        return parentContext().channel();
    }

    @Override // io.netty.channel.Channel
    public boolean isRegistered() {
        return this.registered;
    }

    @Override // io.netty.channel.Channel
    public SocketAddress localAddress() {
        return parent().localAddress();
    }

    @Override // io.netty.channel.Channel
    public SocketAddress remoteAddress() {
        return parent().remoteAddress();
    }

    @Override // io.netty.channel.Channel
    public ChannelFuture closeFuture() {
        return this.closePromise;
    }

    @Override // io.netty.channel.Channel
    public long bytesBeforeUnwritable() {
        long bytes = (config().getWriteBufferHighWaterMark() - this.totalPendingSize) + 1;
        if (bytes <= 0 || !isWritable()) {
            return 0L;
        }
        return bytes;
    }

    @Override // io.netty.channel.Channel
    public long bytesBeforeWritable() {
        long bytes = (this.totalPendingSize - config().getWriteBufferLowWaterMark()) + 1;
        if (bytes <= 0 || isWritable()) {
            return 0L;
        }
        return bytes;
    }

    @Override // io.netty.channel.Channel
    public Channel.Unsafe unsafe() {
        return this.unsafe;
    }

    @Override // io.netty.channel.Channel
    public ChannelPipeline pipeline() {
        return this.pipeline;
    }

    @Override // io.netty.channel.Channel
    public ByteBufAllocator alloc() {
        return config().getAllocator();
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public Channel read() {
        pipeline().read();
        return this;
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public Channel flush() {
        pipeline().flush();
        return this;
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture bind(SocketAddress localAddress) {
        return pipeline().bind(localAddress);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture connect(SocketAddress remoteAddress) {
        return pipeline().connect(remoteAddress);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress) {
        return pipeline().connect(remoteAddress, localAddress);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture disconnect() {
        return pipeline().disconnect();
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture close() {
        return pipeline().close();
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture deregister() {
        return pipeline().deregister();
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture bind(SocketAddress localAddress, ChannelPromise promise) {
        return pipeline().bind(localAddress, promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture connect(SocketAddress remoteAddress, ChannelPromise promise) {
        return pipeline().connect(remoteAddress, promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
        return pipeline().connect(remoteAddress, localAddress, promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture disconnect(ChannelPromise promise) {
        return pipeline().disconnect(promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture close(ChannelPromise promise) {
        return pipeline().close(promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture deregister(ChannelPromise promise) {
        return pipeline().deregister(promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture write(Object msg) {
        return pipeline().write(msg);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture write(Object msg, ChannelPromise promise) {
        return pipeline().write(msg, promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture writeAndFlush(Object msg, ChannelPromise promise) {
        return pipeline().writeAndFlush(msg, promise);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture writeAndFlush(Object msg) {
        return pipeline().writeAndFlush(msg);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelPromise newPromise() {
        return pipeline().newPromise();
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelProgressivePromise newProgressivePromise() {
        return pipeline().newProgressivePromise();
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture newSucceededFuture() {
        return pipeline().newSucceededFuture();
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelFuture newFailedFuture(Throwable cause) {
        return pipeline().newFailedFuture(cause);
    }

    @Override // io.netty.channel.ChannelOutboundInvoker
    public ChannelPromise voidPromise() {
        return pipeline().voidPromise();
    }

    public int hashCode() {
        return id().hashCode();
    }

    public boolean equals(Object o) {
        return this == o;
    }

    @Override // java.lang.Comparable
    public int compareTo(Channel o) {
        if (this == o) {
            return 0;
        }
        return id().compareTo(o.id());
    }

    public String toString() {
        return parent().toString() + "(H2 - " + this.stream + ')';
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void fireChildRead(Http2Frame frame) {
        if (!eventLoop().inEventLoop()) {
            throw new AssertionError();
        }
        if (!isActive()) {
            ReferenceCountUtil.release(frame);
            return;
        }
        if (this.readStatus != ReadStatus.IDLE) {
            if (this.inboundBuffer != null && !this.inboundBuffer.isEmpty()) {
                throw new AssertionError();
            }
            RecvByteBufAllocator.Handle allocHandle = this.unsafe.recvBufAllocHandle();
            this.unsafe.doRead0(frame, allocHandle);
            if (allocHandle.continueReading()) {
                maybeAddChannelToReadCompletePendingQueue();
                return;
            } else {
                this.unsafe.notifyReadComplete(allocHandle, true, false);
                return;
            }
        }
        if (this.inboundBuffer == null) {
            this.inboundBuffer = new ArrayDeque(4);
        }
        this.inboundBuffer.add(frame);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void fireChildReadComplete() {
        if (!eventLoop().inEventLoop()) {
            throw new AssertionError();
        }
        if (this.readStatus == ReadStatus.IDLE && this.readCompletePending) {
            throw new AssertionError();
        }
        this.unsafe.notifyReadComplete(this.unsafe.recvBufAllocHandle(), false, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void closeWithError(Http2Error error) {
        if (!eventLoop().inEventLoop()) {
            throw new AssertionError();
        }
        this.unsafe.close(this.unsafe.voidPromise(), error);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class Http2ChannelUnsafe implements Channel.Unsafe {
        private boolean closeInitiated;
        private boolean readEOS;
        private boolean receivedEndOfStream;
        private RecvByteBufAllocator.Handle recvHandle;
        private boolean sentEndOfStream;
        private final VoidChannelPromise unsafeVoidPromise;
        private boolean writeDoneAndNoFlush;

        private Http2ChannelUnsafe() {
            this.unsafeVoidPromise = new VoidChannelPromise(AbstractHttp2StreamChannel.this, false);
        }

        @Override // io.netty.channel.Channel.Unsafe
        public void connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
            if (!promise.setUncancellable()) {
                return;
            }
            promise.setFailure((Throwable) new UnsupportedOperationException());
        }

        @Override // io.netty.channel.Channel.Unsafe
        public RecvByteBufAllocator.Handle recvBufAllocHandle() {
            if (this.recvHandle == null) {
                this.recvHandle = AbstractHttp2StreamChannel.this.config().getRecvByteBufAllocator().newHandle();
                this.recvHandle.reset(AbstractHttp2StreamChannel.this.config());
            }
            return this.recvHandle;
        }

        @Override // io.netty.channel.Channel.Unsafe
        public SocketAddress localAddress() {
            return AbstractHttp2StreamChannel.this.parent().unsafe().localAddress();
        }

        @Override // io.netty.channel.Channel.Unsafe
        public SocketAddress remoteAddress() {
            return AbstractHttp2StreamChannel.this.parent().unsafe().remoteAddress();
        }

        @Override // io.netty.channel.Channel.Unsafe
        public void register(EventLoop eventLoop, ChannelPromise promise) {
            if (promise.setUncancellable()) {
                if (!AbstractHttp2StreamChannel.this.registered) {
                    AbstractHttp2StreamChannel.this.registered = true;
                    promise.setSuccess();
                    AbstractHttp2StreamChannel.this.pipeline().fireChannelRegistered();
                    if (AbstractHttp2StreamChannel.this.isActive()) {
                        AbstractHttp2StreamChannel.this.pipeline().fireChannelActive();
                        return;
                    }
                    return;
                }
                promise.setFailure((Throwable) new UnsupportedOperationException("Re-register is not supported"));
            }
        }

        @Override // io.netty.channel.Channel.Unsafe
        public void bind(SocketAddress localAddress, ChannelPromise promise) {
            if (!promise.setUncancellable()) {
                return;
            }
            promise.setFailure((Throwable) new UnsupportedOperationException());
        }

        @Override // io.netty.channel.Channel.Unsafe
        public void disconnect(ChannelPromise promise) {
            close(promise);
        }

        @Override // io.netty.channel.Channel.Unsafe
        public void close(ChannelPromise promise) {
            close(promise, Http2Error.CANCEL);
        }

        void close(final ChannelPromise promise, Http2Error error) {
            if (!promise.setUncancellable()) {
                return;
            }
            if (this.closeInitiated) {
                if (AbstractHttp2StreamChannel.this.closePromise.isDone()) {
                    promise.setSuccess();
                    return;
                } else {
                    if (!(promise instanceof VoidChannelPromise)) {
                        AbstractHttp2StreamChannel.this.closePromise.addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.handler.codec.http2.AbstractHttp2StreamChannel.Http2ChannelUnsafe.1
                            @Override // io.netty.util.concurrent.GenericFutureListener
                            public void operationComplete(ChannelFuture future) {
                                promise.setSuccess();
                            }
                        });
                        return;
                    }
                    return;
                }
            }
            this.closeInitiated = true;
            AbstractHttp2StreamChannel.this.readCompletePending = false;
            boolean wasActive = AbstractHttp2StreamChannel.this.isActive();
            if (AbstractHttp2StreamChannel.this.parent().isActive() && Http2CodecUtil.isStreamIdValid(AbstractHttp2StreamChannel.this.stream.id()) && !this.readEOS && (!this.receivedEndOfStream || !this.sentEndOfStream)) {
                Http2StreamFrame resetFrame = new DefaultHttp2ResetFrame(error).stream(AbstractHttp2StreamChannel.this.stream());
                write(resetFrame, AbstractHttp2StreamChannel.this.unsafe().voidPromise());
                flush();
            }
            if (AbstractHttp2StreamChannel.this.inboundBuffer != null) {
                while (true) {
                    Object msg = AbstractHttp2StreamChannel.this.inboundBuffer.poll();
                    if (msg == null) {
                        break;
                    } else {
                        ReferenceCountUtil.release(msg);
                    }
                }
                AbstractHttp2StreamChannel.this.inboundBuffer = null;
            }
            AbstractHttp2StreamChannel.this.outboundClosed = true;
            AbstractHttp2StreamChannel.this.closePromise.setSuccess();
            promise.setSuccess();
            fireChannelInactiveAndDeregister(voidPromise(), wasActive);
        }

        @Override // io.netty.channel.Channel.Unsafe
        public void closeForcibly() {
            close(AbstractHttp2StreamChannel.this.unsafe().voidPromise());
        }

        @Override // io.netty.channel.Channel.Unsafe
        public void deregister(ChannelPromise promise) {
            fireChannelInactiveAndDeregister(promise, false);
        }

        private void fireChannelInactiveAndDeregister(final ChannelPromise promise, final boolean fireChannelInactive) {
            if (promise.setUncancellable()) {
                if (!AbstractHttp2StreamChannel.this.registered) {
                    promise.setSuccess();
                } else {
                    invokeLater(new Runnable() { // from class: io.netty.handler.codec.http2.AbstractHttp2StreamChannel.Http2ChannelUnsafe.2
                        @Override // java.lang.Runnable
                        public void run() {
                            if (fireChannelInactive) {
                                AbstractHttp2StreamChannel.this.pipeline.fireChannelInactive();
                            }
                            if (AbstractHttp2StreamChannel.this.registered) {
                                AbstractHttp2StreamChannel.this.registered = false;
                                AbstractHttp2StreamChannel.this.pipeline.fireChannelUnregistered();
                            }
                            Http2ChannelUnsafe.this.safeSetSuccess(promise);
                        }
                    });
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void safeSetSuccess(ChannelPromise promise) {
            if (!(promise instanceof VoidChannelPromise) && !promise.trySuccess()) {
                AbstractHttp2StreamChannel.logger.warn("Failed to mark a promise as success because it is done already: {}", promise);
            }
        }

        private void invokeLater(Runnable task) {
            try {
                AbstractHttp2StreamChannel.this.eventLoop().execute(task);
            } catch (RejectedExecutionException e) {
                AbstractHttp2StreamChannel.logger.warn("Can't invoke task later as EventLoop rejected it", (Throwable) e);
            }
        }

        @Override // io.netty.channel.Channel.Unsafe
        public void beginRead() {
            if (!AbstractHttp2StreamChannel.this.isActive()) {
                return;
            }
            updateLocalWindowIfNeeded();
            switch (AbstractHttp2StreamChannel.this.readStatus) {
                case IDLE:
                    AbstractHttp2StreamChannel.this.readStatus = ReadStatus.IN_PROGRESS;
                    doBeginRead();
                    return;
                case IN_PROGRESS:
                    AbstractHttp2StreamChannel.this.readStatus = ReadStatus.REQUESTED;
                    return;
                default:
                    return;
            }
        }

        private Object pollQueuedMessage() {
            if (AbstractHttp2StreamChannel.this.inboundBuffer == null) {
                return null;
            }
            return AbstractHttp2StreamChannel.this.inboundBuffer.poll();
        }

        void doBeginRead() {
            Object pollQueuedMessage;
            if (AbstractHttp2StreamChannel.this.readStatus == ReadStatus.IDLE) {
                if (this.readEOS) {
                    if (AbstractHttp2StreamChannel.this.inboundBuffer == null || AbstractHttp2StreamChannel.this.inboundBuffer.isEmpty()) {
                        flush();
                        AbstractHttp2StreamChannel.this.unsafe.closeForcibly();
                        return;
                    }
                    return;
                }
                return;
            }
            do {
                Object message = pollQueuedMessage();
                if (message == null) {
                    flush();
                    if (this.readEOS) {
                        AbstractHttp2StreamChannel.this.unsafe.closeForcibly();
                        return;
                    }
                    return;
                }
                RecvByteBufAllocator.Handle allocHandle = recvBufAllocHandle();
                allocHandle.reset(AbstractHttp2StreamChannel.this.config());
                boolean continueReading = false;
                do {
                    doRead0((Http2Frame) message, allocHandle);
                    if (!this.readEOS) {
                        boolean continueReading2 = allocHandle.continueReading();
                        continueReading = continueReading2;
                        if (!continueReading2) {
                            break;
                        }
                    }
                    pollQueuedMessage = pollQueuedMessage();
                    message = pollQueuedMessage;
                } while (pollQueuedMessage != null);
                if (continueReading && AbstractHttp2StreamChannel.this.isParentReadInProgress() && !this.readEOS) {
                    AbstractHttp2StreamChannel.this.maybeAddChannelToReadCompletePendingQueue();
                } else {
                    notifyReadComplete(allocHandle, true, true);
                    resetReadStatus();
                }
            } while (AbstractHttp2StreamChannel.this.readStatus != ReadStatus.IDLE);
        }

        void readEOS() {
            this.readEOS = true;
        }

        private void updateLocalWindowIfNeeded() {
            if (AbstractHttp2StreamChannel.this.flowControlledBytes != 0 && !AbstractHttp2StreamChannel.this.parentContext().isRemoved()) {
                int bytes = AbstractHttp2StreamChannel.this.flowControlledBytes;
                AbstractHttp2StreamChannel.this.flowControlledBytes = 0;
                ChannelFuture future = AbstractHttp2StreamChannel.this.write0(AbstractHttp2StreamChannel.this.parentContext(), new DefaultHttp2WindowUpdateFrame(bytes).stream((Http2FrameStream) AbstractHttp2StreamChannel.this.stream));
                this.writeDoneAndNoFlush = true;
                if (future.isDone()) {
                    AbstractHttp2StreamChannel.windowUpdateFrameWriteComplete(future, AbstractHttp2StreamChannel.this);
                } else {
                    future.addListener((GenericFutureListener<? extends Future<? super Void>>) AbstractHttp2StreamChannel.this.windowUpdateFrameWriteListener);
                }
            }
        }

        private void resetReadStatus() {
            AbstractHttp2StreamChannel.this.readStatus = AbstractHttp2StreamChannel.this.readStatus == ReadStatus.REQUESTED ? ReadStatus.IN_PROGRESS : ReadStatus.IDLE;
        }

        void notifyReadComplete(RecvByteBufAllocator.Handle allocHandle, boolean forceReadComplete, boolean inReadLoop) {
            if (AbstractHttp2StreamChannel.this.readCompletePending || forceReadComplete) {
                AbstractHttp2StreamChannel.this.readCompletePending = false;
                if (!inReadLoop) {
                    resetReadStatus();
                }
                allocHandle.readComplete();
                AbstractHttp2StreamChannel.this.pipeline().fireChannelReadComplete();
                flush();
                if (this.readEOS) {
                    AbstractHttp2StreamChannel.this.unsafe.closeForcibly();
                }
            }
        }

        void doRead0(Http2Frame frame, RecvByteBufAllocator.Handle allocHandle) {
            int bytes;
            if (frame instanceof Http2DataFrame) {
                bytes = ((Http2DataFrame) frame).initialFlowControlledBytes();
                AbstractHttp2StreamChannel.this.flowControlledBytes += bytes;
            } else {
                bytes = 9;
            }
            this.receivedEndOfStream |= isEndOfStream(frame);
            allocHandle.attemptedBytesRead(bytes);
            allocHandle.lastBytesRead(bytes);
            allocHandle.incMessagesRead(1);
            AbstractHttp2StreamChannel.this.pipeline().fireChannelRead((Object) frame);
        }

        @Override // io.netty.channel.Channel.Unsafe
        public void write(Object msg, ChannelPromise promise) {
            if (!promise.setUncancellable()) {
                ReferenceCountUtil.release(msg);
                return;
            }
            if (!AbstractHttp2StreamChannel.this.isActive() || (AbstractHttp2StreamChannel.this.outboundClosed && ((msg instanceof Http2HeadersFrame) || (msg instanceof Http2DataFrame)))) {
                ReferenceCountUtil.release(msg);
                promise.setFailure((Throwable) new ClosedChannelException());
                return;
            }
            try {
                if (msg instanceof Http2StreamFrame) {
                    Http2StreamFrame frame = validateStreamFrame((Http2StreamFrame) msg).stream(AbstractHttp2StreamChannel.this.stream());
                    writeHttp2StreamFrame(frame, promise);
                } else {
                    String msgStr = msg.toString();
                    ReferenceCountUtil.release(msg);
                    promise.setFailure((Throwable) new IllegalArgumentException("Message must be an " + StringUtil.simpleClassName((Class<?>) Http2StreamFrame.class) + ": " + msgStr));
                }
            } catch (Throwable t) {
                promise.tryFailure(t);
            }
        }

        private boolean isEndOfStream(Http2Frame frame) {
            if (frame instanceof Http2HeadersFrame) {
                return ((Http2HeadersFrame) frame).isEndStream();
            }
            if (frame instanceof Http2DataFrame) {
                return ((Http2DataFrame) frame).isEndStream();
            }
            return false;
        }

        private void writeHttp2StreamFrame(Http2StreamFrame frame, final ChannelPromise promise) {
            boolean firstWrite;
            if (AbstractHttp2StreamChannel.this.firstFrameWritten || Http2CodecUtil.isStreamIdValid(AbstractHttp2StreamChannel.this.stream().id()) || (frame instanceof Http2HeadersFrame)) {
                if (!AbstractHttp2StreamChannel.this.firstFrameWritten) {
                    firstWrite = AbstractHttp2StreamChannel.this.firstFrameWritten = true;
                } else {
                    firstWrite = false;
                }
                this.sentEndOfStream |= isEndOfStream(frame);
                ChannelFuture f = AbstractHttp2StreamChannel.this.write0(AbstractHttp2StreamChannel.this.parentContext(), frame);
                if (!f.isDone()) {
                    final long bytes = FlowControlledFrameSizeEstimator.HANDLE_INSTANCE.size(frame);
                    AbstractHttp2StreamChannel.this.incrementPendingOutboundBytes(bytes, false);
                    final boolean z = firstWrite;
                    f.addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.handler.codec.http2.AbstractHttp2StreamChannel.Http2ChannelUnsafe.3
                        @Override // io.netty.util.concurrent.GenericFutureListener
                        public void operationComplete(ChannelFuture future) {
                            if (z) {
                                Http2ChannelUnsafe.this.firstWriteComplete(future, promise);
                            } else {
                                Http2ChannelUnsafe.this.writeComplete(future, promise);
                            }
                            AbstractHttp2StreamChannel.this.decrementPendingOutboundBytes(bytes, false);
                        }
                    });
                    this.writeDoneAndNoFlush = true;
                    return;
                }
                if (firstWrite) {
                    firstWriteComplete(f, promise);
                    return;
                } else {
                    writeComplete(f, promise);
                    return;
                }
            }
            ReferenceCountUtil.release(frame);
            promise.setFailure((Throwable) new IllegalArgumentException("The first frame must be a headers frame. Was: " + frame.name()));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void firstWriteComplete(ChannelFuture future, ChannelPromise promise) {
            Throwable cause = future.cause();
            if (cause == null) {
                promise.setSuccess();
            } else {
                closeForcibly();
                promise.setFailure(wrapStreamClosedError(cause));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void writeComplete(ChannelFuture future, ChannelPromise promise) {
            Throwable cause = future.cause();
            if (cause == null) {
                promise.setSuccess();
                return;
            }
            Throwable error = wrapStreamClosedError(cause);
            if (error instanceof IOException) {
                if (!AbstractHttp2StreamChannel.this.config.isAutoClose()) {
                    AbstractHttp2StreamChannel.this.outboundClosed = true;
                } else {
                    closeForcibly();
                }
            }
            promise.setFailure(error);
        }

        private Throwable wrapStreamClosedError(Throwable cause) {
            if ((cause instanceof Http2Exception) && ((Http2Exception) cause).error() == Http2Error.STREAM_CLOSED) {
                return new ClosedChannelException().initCause(cause);
            }
            return cause;
        }

        private Http2StreamFrame validateStreamFrame(Http2StreamFrame frame) {
            if (frame.stream() != null && frame.stream() != AbstractHttp2StreamChannel.this.stream) {
                String msgString = frame.toString();
                ReferenceCountUtil.release(frame);
                throw new IllegalArgumentException("Stream " + frame.stream() + " must not be set on the frame: " + msgString);
            }
            return frame;
        }

        @Override // io.netty.channel.Channel.Unsafe
        public void flush() {
            if (!this.writeDoneAndNoFlush || AbstractHttp2StreamChannel.this.isParentReadInProgress()) {
                return;
            }
            this.writeDoneAndNoFlush = false;
            AbstractHttp2StreamChannel.this.flush0(AbstractHttp2StreamChannel.this.parentContext());
        }

        @Override // io.netty.channel.Channel.Unsafe
        public ChannelPromise voidPromise() {
            return this.unsafeVoidPromise;
        }

        @Override // io.netty.channel.Channel.Unsafe
        public ChannelOutboundBuffer outboundBuffer() {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class Http2StreamChannelConfig extends DefaultChannelConfig {
        Http2StreamChannelConfig(Channel channel) {
            super(channel);
        }

        @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
        public MessageSizeEstimator getMessageSizeEstimator() {
            return FlowControlledFrameSizeEstimator.INSTANCE;
        }

        @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
        public ChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator) {
            throw new UnsupportedOperationException();
        }

        @Override // io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
        public ChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator) {
            if (!(allocator.newHandle() instanceof RecvByteBufAllocator.ExtendedHandle)) {
                throw new IllegalArgumentException("allocator.newHandle() must return an object of type: " + RecvByteBufAllocator.ExtendedHandle.class);
            }
            super.setRecvByteBufAllocator(allocator);
            return this;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void maybeAddChannelToReadCompletePendingQueue() {
        if (!this.readCompletePending) {
            this.readCompletePending = true;
            addChannelToReadCompletePendingQueue();
        }
    }

    protected void flush0(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    protected ChannelFuture write0(ChannelHandlerContext ctx, Object msg) {
        ChannelPromise promise = ctx.newPromise();
        ctx.write(msg, promise);
        return promise;
    }
}
