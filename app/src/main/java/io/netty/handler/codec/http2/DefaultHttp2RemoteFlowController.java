package io.netty.handler.codec.http2;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http2.Http2Connection;
import io.netty.handler.codec.http2.Http2RemoteFlowController;
import io.netty.handler.codec.http2.Http2Stream;
import io.netty.handler.codec.http2.StreamByteDistributor;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.ArrayDeque;
import java.util.Deque;

/* loaded from: classes4.dex */
public class DefaultHttp2RemoteFlowController implements Http2RemoteFlowController {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int MIN_WRITABLE_CHUNK = 32768;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) DefaultHttp2RemoteFlowController.class);
    private final Http2Connection connection;
    private final FlowState connectionState;
    private ChannelHandlerContext ctx;
    private int initialWindowSize;
    private WritabilityMonitor monitor;
    private final Http2Connection.PropertyKey stateKey;
    private final StreamByteDistributor streamByteDistributor;

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public DefaultHttp2RemoteFlowController(Http2Connection connection) {
        this(connection, (Http2RemoteFlowController.Listener) null);
    }

    public DefaultHttp2RemoteFlowController(Http2Connection connection, StreamByteDistributor streamByteDistributor) {
        this(connection, streamByteDistributor, null);
    }

    public DefaultHttp2RemoteFlowController(Http2Connection connection, Http2RemoteFlowController.Listener listener) {
        this(connection, new WeightedFairQueueByteDistributor(connection), listener);
    }

    public DefaultHttp2RemoteFlowController(Http2Connection connection, StreamByteDistributor streamByteDistributor, Http2RemoteFlowController.Listener listener) {
        this.initialWindowSize = 65535;
        this.connection = (Http2Connection) ObjectUtil.checkNotNull(connection, "connection");
        this.streamByteDistributor = (StreamByteDistributor) ObjectUtil.checkNotNull(streamByteDistributor, "streamWriteDistributor");
        this.stateKey = connection.newKey();
        this.connectionState = new FlowState(connection.connectionStream());
        connection.connectionStream().setProperty(this.stateKey, this.connectionState);
        listener(listener);
        this.monitor.windowSize(this.connectionState, this.initialWindowSize);
        connection.addListener(new Http2ConnectionAdapter() { // from class: io.netty.handler.codec.http2.DefaultHttp2RemoteFlowController.1
            @Override // io.netty.handler.codec.http2.Http2ConnectionAdapter, io.netty.handler.codec.http2.Http2Connection.Listener
            public void onStreamAdded(Http2Stream stream) {
                stream.setProperty(DefaultHttp2RemoteFlowController.this.stateKey, new FlowState(stream));
            }

            @Override // io.netty.handler.codec.http2.Http2ConnectionAdapter, io.netty.handler.codec.http2.Http2Connection.Listener
            public void onStreamActive(Http2Stream stream) {
                DefaultHttp2RemoteFlowController.this.monitor.windowSize(DefaultHttp2RemoteFlowController.this.state(stream), DefaultHttp2RemoteFlowController.this.initialWindowSize);
            }

            @Override // io.netty.handler.codec.http2.Http2ConnectionAdapter, io.netty.handler.codec.http2.Http2Connection.Listener
            public void onStreamClosed(Http2Stream stream) {
                DefaultHttp2RemoteFlowController.this.state(stream).cancel(Http2Error.STREAM_CLOSED, null);
            }

            @Override // io.netty.handler.codec.http2.Http2ConnectionAdapter, io.netty.handler.codec.http2.Http2Connection.Listener
            public void onStreamHalfClosed(Http2Stream stream) {
                if (Http2Stream.State.HALF_CLOSED_LOCAL == stream.state()) {
                    DefaultHttp2RemoteFlowController.this.state(stream).cancel(Http2Error.STREAM_CLOSED, null);
                }
            }
        });
    }

    @Override // io.netty.handler.codec.http2.Http2FlowController
    public void channelHandlerContext(ChannelHandlerContext ctx) throws Http2Exception {
        this.ctx = (ChannelHandlerContext) ObjectUtil.checkNotNull(ctx, "ctx");
        channelWritabilityChanged();
        if (isChannelWritable()) {
            writePendingBytes();
        }
    }

    @Override // io.netty.handler.codec.http2.Http2RemoteFlowController
    public ChannelHandlerContext channelHandlerContext() {
        return this.ctx;
    }

    @Override // io.netty.handler.codec.http2.Http2FlowController
    public void initialWindowSize(int newWindowSize) throws Http2Exception {
        if (this.ctx != null && !this.ctx.executor().inEventLoop()) {
            throw new AssertionError();
        }
        this.monitor.initialWindowSize(newWindowSize);
    }

    @Override // io.netty.handler.codec.http2.Http2FlowController
    public int initialWindowSize() {
        return this.initialWindowSize;
    }

    @Override // io.netty.handler.codec.http2.Http2FlowController
    public int windowSize(Http2Stream stream) {
        return state(stream).windowSize();
    }

    @Override // io.netty.handler.codec.http2.Http2RemoteFlowController
    public boolean isWritable(Http2Stream stream) {
        return this.monitor.isWritable(state(stream));
    }

    @Override // io.netty.handler.codec.http2.Http2RemoteFlowController
    public void channelWritabilityChanged() throws Http2Exception {
        this.monitor.channelWritabilityChange();
    }

    @Override // io.netty.handler.codec.http2.Http2RemoteFlowController
    public void updateDependencyTree(int childStreamId, int parentStreamId, short weight, boolean exclusive) {
        if (weight < 1 || weight > 256) {
            throw new AssertionError("Invalid weight");
        }
        if (childStreamId == parentStreamId) {
            throw new AssertionError("A stream cannot depend on itself");
        }
        if (childStreamId <= 0 || parentStreamId < 0) {
            throw new AssertionError("childStreamId must be > 0. parentStreamId must be >= 0.");
        }
        this.streamByteDistributor.updateDependencyTree(childStreamId, parentStreamId, weight, exclusive);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isChannelWritable() {
        return this.ctx != null && isChannelWritable0();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isChannelWritable0() {
        return this.ctx.channel().isWritable();
    }

    @Override // io.netty.handler.codec.http2.Http2RemoteFlowController
    public void listener(Http2RemoteFlowController.Listener listener) {
        this.monitor = listener == null ? new WritabilityMonitor() : new ListenerWritabilityMonitor(listener);
    }

    @Override // io.netty.handler.codec.http2.Http2FlowController
    public void incrementWindowSize(Http2Stream stream, int delta) throws Http2Exception {
        if (this.ctx != null && !this.ctx.executor().inEventLoop()) {
            throw new AssertionError();
        }
        this.monitor.incrementWindowSize(state(stream), delta);
    }

    @Override // io.netty.handler.codec.http2.Http2RemoteFlowController
    public void addFlowControlled(Http2Stream stream, Http2RemoteFlowController.FlowControlled frame) {
        if (this.ctx != null && !this.ctx.executor().inEventLoop()) {
            throw new AssertionError();
        }
        ObjectUtil.checkNotNull(frame, "frame");
        try {
            this.monitor.enqueueFrame(state(stream), frame);
        } catch (Throwable t) {
            frame.error(this.ctx, t);
        }
    }

    @Override // io.netty.handler.codec.http2.Http2RemoteFlowController
    public boolean hasFlowControlled(Http2Stream stream) {
        return state(stream).hasFrame();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public FlowState state(Http2Stream stream) {
        return (FlowState) stream.getProperty(this.stateKey);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int connectionWindowSize() {
        return this.connectionState.windowSize();
    }

    private int minUsableChannelBytes() {
        return Math.max(this.ctx.channel().config().getWriteBufferLowWaterMark(), 32768);
    }

    private int maxUsableChannelBytes() {
        int channelWritableBytes = (int) Math.min(2147483647L, this.ctx.channel().bytesBeforeUnwritable());
        int usableBytes = channelWritableBytes > 0 ? Math.max(channelWritableBytes, minUsableChannelBytes()) : 0;
        return Math.min(this.connectionState.windowSize(), usableBytes);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int writableBytes() {
        return Math.min(connectionWindowSize(), maxUsableChannelBytes());
    }

    @Override // io.netty.handler.codec.http2.Http2RemoteFlowController
    public void writePendingBytes() throws Http2Exception {
        this.monitor.writePendingBytes();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class FlowState implements StreamByteDistributor.StreamState {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private boolean cancelled;
        private boolean markedWritable;
        private long pendingBytes;
        private final Deque<Http2RemoteFlowController.FlowControlled> pendingWriteQueue = new ArrayDeque(2);
        private final Http2Stream stream;
        private int window;
        private boolean writing;

        FlowState(Http2Stream stream) {
            this.stream = stream;
        }

        boolean isWritable() {
            return ((long) windowSize()) > pendingBytes() && !this.cancelled;
        }

        @Override // io.netty.handler.codec.http2.StreamByteDistributor.StreamState
        public Http2Stream stream() {
            return this.stream;
        }

        boolean markedWritability() {
            return this.markedWritable;
        }

        void markedWritability(boolean isWritable) {
            this.markedWritable = isWritable;
        }

        @Override // io.netty.handler.codec.http2.StreamByteDistributor.StreamState
        public int windowSize() {
            return this.window;
        }

        void windowSize(int initialWindowSize) {
            this.window = initialWindowSize;
        }

        /* JADX WARN: Code restructure failed: missing block: B:20:0x0070, code lost:            return -1;     */
        /* JADX WARN: Code restructure failed: missing block: B:22:?, code lost:            return -1;     */
        /* JADX WARN: Code restructure failed: missing block: B:24:0x007e, code lost:            if (r10.cancelled != false) goto L31;     */
        /* JADX WARN: Code restructure failed: missing block: B:25:0x0080, code lost:            cancel(io.netty.handler.codec.http2.Http2Error.INTERNAL_ERROR, r1);     */
        /* JADX WARN: Code restructure failed: missing block: B:26:0x00a0, code lost:            return r2;     */
        /* JADX WARN: Code restructure failed: missing block: B:47:0x009d, code lost:            if (r10.cancelled == false) goto L40;     */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        int writeAllocatedBytes(int r11) {
            /*
                r10 = this;
                r0 = r11
                r1 = 0
                r2 = 1
                r3 = 0
                boolean r4 = r10.writing     // Catch: java.lang.Throwable -> L86
                if (r4 != 0) goto L88
                r10.writing = r2     // Catch: java.lang.Throwable -> L86
                r4 = 0
            Lb:
                boolean r5 = r10.cancelled     // Catch: java.lang.Throwable -> L86
                if (r5 != 0) goto L5a
                io.netty.handler.codec.http2.Http2RemoteFlowController$FlowControlled r5 = r10.peek()     // Catch: java.lang.Throwable -> L86
                r6 = r5
                if (r5 == 0) goto L5a
                int r5 = r10.writableWindow()     // Catch: java.lang.Throwable -> L86
                int r5 = java.lang.Math.min(r11, r5)     // Catch: java.lang.Throwable -> L86
                if (r5 > 0) goto L27
                int r7 = r6.size()     // Catch: java.lang.Throwable -> L86
                if (r7 <= 0) goto L27
                goto L5a
            L27:
                r4 = 1
                int r7 = r6.size()     // Catch: java.lang.Throwable -> L86
                io.netty.handler.codec.http2.DefaultHttp2RemoteFlowController r8 = io.netty.handler.codec.http2.DefaultHttp2RemoteFlowController.this     // Catch: java.lang.Throwable -> L50
                io.netty.channel.ChannelHandlerContext r8 = io.netty.handler.codec.http2.DefaultHttp2RemoteFlowController.access$500(r8)     // Catch: java.lang.Throwable -> L50
                int r9 = java.lang.Math.max(r3, r5)     // Catch: java.lang.Throwable -> L50
                r6.write(r8, r9)     // Catch: java.lang.Throwable -> L50
                int r8 = r6.size()     // Catch: java.lang.Throwable -> L50
                if (r8 != 0) goto L47
                java.util.Deque<io.netty.handler.codec.http2.Http2RemoteFlowController$FlowControlled> r8 = r10.pendingWriteQueue     // Catch: java.lang.Throwable -> L50
                r8.remove()     // Catch: java.lang.Throwable -> L50
                r6.writeComplete()     // Catch: java.lang.Throwable -> L50
            L47:
                int r8 = r6.size()     // Catch: java.lang.Throwable -> L86
                int r8 = r7 - r8
                int r11 = r11 - r8
                goto Lb
            L50:
                r8 = move-exception
                int r9 = r6.size()     // Catch: java.lang.Throwable -> L86
                int r9 = r7 - r9
                int r11 = r11 - r9
                throw r8     // Catch: java.lang.Throwable -> L86
            L5a:
                if (r4 != 0) goto L72
            L5d:
                r10.writing = r3
                int r2 = r0 - r11
                r10.decrementPendingBytes(r2, r3)
                r10.decrementFlowControlWindow(r2)
                boolean r3 = r10.cancelled
                if (r3 == 0) goto L70
                io.netty.handler.codec.http2.Http2Error r3 = io.netty.handler.codec.http2.Http2Error.INTERNAL_ERROR
                r10.cancel(r3, r1)
            L70:
                r3 = -1
                return r3
            L72:
                r10.writing = r3
                int r2 = r0 - r11
                r10.decrementPendingBytes(r2, r3)
                r10.decrementFlowControlWindow(r2)
                boolean r3 = r10.cancelled
                if (r3 == 0) goto La0
            L80:
                io.netty.handler.codec.http2.Http2Error r3 = io.netty.handler.codec.http2.Http2Error.INTERNAL_ERROR
                r10.cancel(r3, r1)
                goto La0
            L86:
                r4 = move-exception
                goto L8e
            L88:
                java.lang.AssertionError r4 = new java.lang.AssertionError     // Catch: java.lang.Throwable -> L86
                r4.<init>()     // Catch: java.lang.Throwable -> L86
                throw r4     // Catch: java.lang.Throwable -> L86
            L8e:
                r10.cancelled = r2     // Catch: java.lang.Throwable -> La1
                r1 = r4
                r10.writing = r3
                int r2 = r0 - r11
                r10.decrementPendingBytes(r2, r3)
                r10.decrementFlowControlWindow(r2)
                boolean r3 = r10.cancelled
                if (r3 == 0) goto La0
                goto L80
            La0:
                return r2
            La1:
                r2 = move-exception
                r10.writing = r3
                int r4 = r0 - r11
                r10.decrementPendingBytes(r4, r3)
                r10.decrementFlowControlWindow(r4)
                boolean r3 = r10.cancelled
                if (r3 == 0) goto Lb5
                io.netty.handler.codec.http2.Http2Error r3 = io.netty.handler.codec.http2.Http2Error.INTERNAL_ERROR
                r10.cancel(r3, r1)
            Lb5:
                throw r2
            */
            throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.codec.http2.DefaultHttp2RemoteFlowController.FlowState.writeAllocatedBytes(int):int");
        }

        int incrementStreamWindow(int delta) throws Http2Exception {
            if (delta > 0 && Integer.MAX_VALUE - delta < this.window) {
                throw Http2Exception.streamError(this.stream.id(), Http2Error.FLOW_CONTROL_ERROR, "Window size overflow for stream: %d", Integer.valueOf(this.stream.id()));
            }
            this.window += delta;
            DefaultHttp2RemoteFlowController.this.streamByteDistributor.updateStreamableBytes(this);
            return this.window;
        }

        private int writableWindow() {
            return Math.min(this.window, DefaultHttp2RemoteFlowController.this.connectionWindowSize());
        }

        @Override // io.netty.handler.codec.http2.StreamByteDistributor.StreamState
        public long pendingBytes() {
            return this.pendingBytes;
        }

        void enqueueFrame(Http2RemoteFlowController.FlowControlled frame) {
            Http2RemoteFlowController.FlowControlled last = this.pendingWriteQueue.peekLast();
            if (last == null) {
                enqueueFrameWithoutMerge(frame);
                return;
            }
            int lastSize = last.size();
            if (last.merge(DefaultHttp2RemoteFlowController.this.ctx, frame)) {
                incrementPendingBytes(last.size() - lastSize, true);
            } else {
                enqueueFrameWithoutMerge(frame);
            }
        }

        private void enqueueFrameWithoutMerge(Http2RemoteFlowController.FlowControlled frame) {
            this.pendingWriteQueue.offer(frame);
            incrementPendingBytes(frame.size(), true);
        }

        @Override // io.netty.handler.codec.http2.StreamByteDistributor.StreamState
        public boolean hasFrame() {
            return !this.pendingWriteQueue.isEmpty();
        }

        private Http2RemoteFlowController.FlowControlled peek() {
            return this.pendingWriteQueue.peek();
        }

        void cancel(Http2Error error, Throwable cause) {
            this.cancelled = true;
            if (this.writing) {
                return;
            }
            Http2RemoteFlowController.FlowControlled frame = this.pendingWriteQueue.poll();
            if (frame != null) {
                Http2Exception exception = Http2Exception.streamError(this.stream.id(), error, cause, "Stream closed before write could take place", new Object[0]);
                do {
                    writeError(frame, exception);
                    frame = this.pendingWriteQueue.poll();
                } while (frame != null);
            }
            DefaultHttp2RemoteFlowController.this.streamByteDistributor.updateStreamableBytes(this);
            DefaultHttp2RemoteFlowController.this.monitor.stateCancelled(this);
        }

        private void incrementPendingBytes(int numBytes, boolean updateStreamableBytes) {
            this.pendingBytes += numBytes;
            DefaultHttp2RemoteFlowController.this.monitor.incrementPendingBytes(numBytes);
            if (updateStreamableBytes) {
                DefaultHttp2RemoteFlowController.this.streamByteDistributor.updateStreamableBytes(this);
            }
        }

        private void decrementPendingBytes(int bytes, boolean updateStreamableBytes) {
            incrementPendingBytes(-bytes, updateStreamableBytes);
        }

        private void decrementFlowControlWindow(int bytes) {
            int negativeBytes = -bytes;
            try {
                DefaultHttp2RemoteFlowController.this.connectionState.incrementStreamWindow(negativeBytes);
                incrementStreamWindow(negativeBytes);
            } catch (Http2Exception e) {
                throw new IllegalStateException("Invalid window state when writing frame: " + e.getMessage(), e);
            }
        }

        private void writeError(Http2RemoteFlowController.FlowControlled frame, Http2Exception cause) {
            if (DefaultHttp2RemoteFlowController.this.ctx == null) {
                throw new AssertionError();
            }
            decrementPendingBytes(frame.size(), true);
            frame.error(DefaultHttp2RemoteFlowController.this.ctx, cause);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class WritabilityMonitor implements StreamByteDistributor.Writer {
        private boolean inWritePendingBytes;
        private long totalPendingBytes;

        private WritabilityMonitor() {
        }

        @Override // io.netty.handler.codec.http2.StreamByteDistributor.Writer
        public final void write(Http2Stream stream, int numBytes) {
            DefaultHttp2RemoteFlowController.this.state(stream).writeAllocatedBytes(numBytes);
        }

        void channelWritabilityChange() throws Http2Exception {
        }

        void stateCancelled(FlowState state) {
        }

        void windowSize(FlowState state, int initialWindowSize) {
            state.windowSize(initialWindowSize);
        }

        void incrementWindowSize(FlowState state, int delta) throws Http2Exception {
            state.incrementStreamWindow(delta);
        }

        void enqueueFrame(FlowState state, Http2RemoteFlowController.FlowControlled frame) throws Http2Exception {
            state.enqueueFrame(frame);
        }

        final void incrementPendingBytes(int delta) {
            this.totalPendingBytes += delta;
        }

        final boolean isWritable(FlowState state) {
            return isWritableConnection() && state.isWritable();
        }

        final void writePendingBytes() throws Http2Exception {
            if (this.inWritePendingBytes) {
                return;
            }
            this.inWritePendingBytes = true;
            try {
                int bytesToWrite = DefaultHttp2RemoteFlowController.this.writableBytes();
                while (DefaultHttp2RemoteFlowController.this.streamByteDistributor.distribute(bytesToWrite, this)) {
                    int writableBytes = DefaultHttp2RemoteFlowController.this.writableBytes();
                    bytesToWrite = writableBytes;
                    if (writableBytes <= 0 || !DefaultHttp2RemoteFlowController.this.isChannelWritable0()) {
                        break;
                    }
                }
            } finally {
                this.inWritePendingBytes = false;
            }
        }

        void initialWindowSize(int newWindowSize) throws Http2Exception {
            ObjectUtil.checkPositiveOrZero(newWindowSize, "newWindowSize");
            final int delta = newWindowSize - DefaultHttp2RemoteFlowController.this.initialWindowSize;
            DefaultHttp2RemoteFlowController.this.initialWindowSize = newWindowSize;
            DefaultHttp2RemoteFlowController.this.connection.forEachActiveStream(new Http2StreamVisitor() { // from class: io.netty.handler.codec.http2.DefaultHttp2RemoteFlowController.WritabilityMonitor.1
                @Override // io.netty.handler.codec.http2.Http2StreamVisitor
                public boolean visit(Http2Stream stream) throws Http2Exception {
                    DefaultHttp2RemoteFlowController.this.state(stream).incrementStreamWindow(delta);
                    return true;
                }
            });
            if (delta > 0 && DefaultHttp2RemoteFlowController.this.isChannelWritable()) {
                writePendingBytes();
            }
        }

        final boolean isWritableConnection() {
            return ((long) DefaultHttp2RemoteFlowController.this.connectionState.windowSize()) - this.totalPendingBytes > 0 && DefaultHttp2RemoteFlowController.this.isChannelWritable();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class ListenerWritabilityMonitor extends WritabilityMonitor implements Http2StreamVisitor {
        private final Http2RemoteFlowController.Listener listener;

        ListenerWritabilityMonitor(Http2RemoteFlowController.Listener listener) {
            super();
            this.listener = listener;
        }

        @Override // io.netty.handler.codec.http2.Http2StreamVisitor
        public boolean visit(Http2Stream stream) throws Http2Exception {
            FlowState state = DefaultHttp2RemoteFlowController.this.state(stream);
            if (isWritable(state) != state.markedWritability()) {
                notifyWritabilityChanged(state);
                return true;
            }
            return true;
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2RemoteFlowController.WritabilityMonitor
        void windowSize(FlowState state, int initialWindowSize) {
            super.windowSize(state, initialWindowSize);
            try {
                checkStateWritability(state);
            } catch (Http2Exception e) {
                throw new RuntimeException("Caught unexpected exception from window", e);
            }
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2RemoteFlowController.WritabilityMonitor
        void incrementWindowSize(FlowState state, int delta) throws Http2Exception {
            super.incrementWindowSize(state, delta);
            checkStateWritability(state);
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2RemoteFlowController.WritabilityMonitor
        void initialWindowSize(int newWindowSize) throws Http2Exception {
            super.initialWindowSize(newWindowSize);
            if (isWritableConnection()) {
                checkAllWritabilityChanged();
            }
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2RemoteFlowController.WritabilityMonitor
        void enqueueFrame(FlowState state, Http2RemoteFlowController.FlowControlled frame) throws Http2Exception {
            super.enqueueFrame(state, frame);
            checkConnectionThenStreamWritabilityChanged(state);
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2RemoteFlowController.WritabilityMonitor
        void stateCancelled(FlowState state) {
            try {
                checkConnectionThenStreamWritabilityChanged(state);
            } catch (Http2Exception e) {
                throw new RuntimeException("Caught unexpected exception from checkAllWritabilityChanged", e);
            }
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2RemoteFlowController.WritabilityMonitor
        void channelWritabilityChange() throws Http2Exception {
            if (DefaultHttp2RemoteFlowController.this.connectionState.markedWritability() != DefaultHttp2RemoteFlowController.this.isChannelWritable()) {
                checkAllWritabilityChanged();
            }
        }

        private void checkStateWritability(FlowState state) throws Http2Exception {
            if (isWritable(state) != state.markedWritability()) {
                if (state == DefaultHttp2RemoteFlowController.this.connectionState) {
                    checkAllWritabilityChanged();
                } else {
                    notifyWritabilityChanged(state);
                }
            }
        }

        private void notifyWritabilityChanged(FlowState state) {
            state.markedWritability(!state.markedWritability());
            try {
                this.listener.writabilityChanged(state.stream);
            } catch (Throwable cause) {
                DefaultHttp2RemoteFlowController.logger.error("Caught Throwable from listener.writabilityChanged", cause);
            }
        }

        private void checkConnectionThenStreamWritabilityChanged(FlowState state) throws Http2Exception {
            if (isWritableConnection() != DefaultHttp2RemoteFlowController.this.connectionState.markedWritability()) {
                checkAllWritabilityChanged();
            } else if (isWritable(state) != state.markedWritability()) {
                notifyWritabilityChanged(state);
            }
        }

        private void checkAllWritabilityChanged() throws Http2Exception {
            DefaultHttp2RemoteFlowController.this.connectionState.markedWritability(isWritableConnection());
            DefaultHttp2RemoteFlowController.this.connection.forEachActiveStream(this);
        }
    }
}
