package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http2.Http2Connection;
import io.netty.handler.codec.http2.Http2Exception;
import io.netty.handler.codec.http2.Http2Stream;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;

/* loaded from: classes4.dex */
public class DefaultHttp2LocalFlowController implements Http2LocalFlowController {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final float DEFAULT_WINDOW_UPDATE_RATIO = 0.5f;
    private static final FlowState REDUCED_FLOW_STATE = new FlowState() { // from class: io.netty.handler.codec.http2.DefaultHttp2LocalFlowController.2
        @Override // io.netty.handler.codec.http2.DefaultHttp2LocalFlowController.FlowState
        public int windowSize() {
            return 0;
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2LocalFlowController.FlowState
        public int initialWindowSize() {
            return 0;
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2LocalFlowController.FlowState
        public void window(int initialWindowSize) {
            throw new UnsupportedOperationException();
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2LocalFlowController.FlowState
        public void incrementInitialStreamWindow(int delta) {
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2LocalFlowController.FlowState
        public boolean writeWindowUpdateIfNeeded() throws Http2Exception {
            throw new UnsupportedOperationException();
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2LocalFlowController.FlowState
        public boolean consumeBytes(int numBytes) throws Http2Exception {
            return false;
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2LocalFlowController.FlowState
        public int unconsumedBytes() {
            return 0;
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2LocalFlowController.FlowState
        public float windowUpdateRatio() {
            throw new UnsupportedOperationException();
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2LocalFlowController.FlowState
        public void windowUpdateRatio(float ratio) {
            throw new UnsupportedOperationException();
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2LocalFlowController.FlowState
        public void receiveFlowControlledFrame(int dataLength) throws Http2Exception {
            throw new UnsupportedOperationException();
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2LocalFlowController.FlowState
        public void incrementFlowControlWindows(int delta) throws Http2Exception {
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2LocalFlowController.FlowState
        public void endOfStream(boolean endOfStream) {
            throw new UnsupportedOperationException();
        }
    };
    private final Http2Connection connection;
    private ChannelHandlerContext ctx;
    private Http2FrameWriter frameWriter;
    private int initialWindowSize;
    private final Http2Connection.PropertyKey stateKey;
    private float windowUpdateRatio;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public interface FlowState {
        boolean consumeBytes(int i) throws Http2Exception;

        void endOfStream(boolean z);

        void incrementFlowControlWindows(int i) throws Http2Exception;

        void incrementInitialStreamWindow(int i);

        int initialWindowSize();

        void receiveFlowControlledFrame(int i) throws Http2Exception;

        int unconsumedBytes();

        void window(int i);

        int windowSize();

        float windowUpdateRatio();

        void windowUpdateRatio(float f);

        boolean writeWindowUpdateIfNeeded() throws Http2Exception;
    }

    public DefaultHttp2LocalFlowController(Http2Connection connection) {
        this(connection, 0.5f, false);
    }

    public DefaultHttp2LocalFlowController(Http2Connection connection, float windowUpdateRatio, boolean autoRefillConnectionWindow) {
        FlowState connectionState;
        this.initialWindowSize = 65535;
        this.connection = (Http2Connection) ObjectUtil.checkNotNull(connection, "connection");
        windowUpdateRatio(windowUpdateRatio);
        this.stateKey = connection.newKey();
        if (autoRefillConnectionWindow) {
            connectionState = new AutoRefillState(connection.connectionStream(), this.initialWindowSize);
        } else {
            connectionState = new DefaultState(connection.connectionStream(), this.initialWindowSize);
        }
        connection.connectionStream().setProperty(this.stateKey, connectionState);
        connection.addListener(new Http2ConnectionAdapter() { // from class: io.netty.handler.codec.http2.DefaultHttp2LocalFlowController.1
            @Override // io.netty.handler.codec.http2.Http2ConnectionAdapter, io.netty.handler.codec.http2.Http2Connection.Listener
            public void onStreamAdded(Http2Stream stream) {
                stream.setProperty(DefaultHttp2LocalFlowController.this.stateKey, DefaultHttp2LocalFlowController.REDUCED_FLOW_STATE);
            }

            @Override // io.netty.handler.codec.http2.Http2ConnectionAdapter, io.netty.handler.codec.http2.Http2Connection.Listener
            public void onStreamActive(Http2Stream stream) {
                stream.setProperty(DefaultHttp2LocalFlowController.this.stateKey, new DefaultState(stream, DefaultHttp2LocalFlowController.this.initialWindowSize));
            }

            @Override // io.netty.handler.codec.http2.Http2ConnectionAdapter, io.netty.handler.codec.http2.Http2Connection.Listener
            public void onStreamClosed(Http2Stream stream) {
                try {
                    try {
                        FlowState state = DefaultHttp2LocalFlowController.this.state(stream);
                        int unconsumedBytes = state.unconsumedBytes();
                        if (DefaultHttp2LocalFlowController.this.ctx != null && unconsumedBytes > 0 && DefaultHttp2LocalFlowController.this.consumeAllBytes(state, unconsumedBytes)) {
                            DefaultHttp2LocalFlowController.this.ctx.flush();
                        }
                    } catch (Http2Exception e) {
                        PlatformDependent.throwException(e);
                    }
                } finally {
                    stream.setProperty(DefaultHttp2LocalFlowController.this.stateKey, DefaultHttp2LocalFlowController.REDUCED_FLOW_STATE);
                }
            }
        });
    }

    @Override // io.netty.handler.codec.http2.Http2LocalFlowController
    public DefaultHttp2LocalFlowController frameWriter(Http2FrameWriter frameWriter) {
        this.frameWriter = (Http2FrameWriter) ObjectUtil.checkNotNull(frameWriter, "frameWriter");
        return this;
    }

    @Override // io.netty.handler.codec.http2.Http2FlowController
    public void channelHandlerContext(ChannelHandlerContext ctx) {
        this.ctx = (ChannelHandlerContext) ObjectUtil.checkNotNull(ctx, "ctx");
    }

    @Override // io.netty.handler.codec.http2.Http2FlowController
    public void initialWindowSize(int newWindowSize) throws Http2Exception {
        if (this.ctx != null && !this.ctx.executor().inEventLoop()) {
            throw new AssertionError();
        }
        int delta = newWindowSize - this.initialWindowSize;
        this.initialWindowSize = newWindowSize;
        WindowUpdateVisitor visitor = new WindowUpdateVisitor(delta);
        this.connection.forEachActiveStream(visitor);
        visitor.throwIfError();
    }

    @Override // io.netty.handler.codec.http2.Http2FlowController
    public int initialWindowSize() {
        return this.initialWindowSize;
    }

    @Override // io.netty.handler.codec.http2.Http2FlowController
    public int windowSize(Http2Stream stream) {
        return state(stream).windowSize();
    }

    @Override // io.netty.handler.codec.http2.Http2LocalFlowController
    public int initialWindowSize(Http2Stream stream) {
        return state(stream).initialWindowSize();
    }

    @Override // io.netty.handler.codec.http2.Http2FlowController
    public void incrementWindowSize(Http2Stream stream, int delta) throws Http2Exception {
        if (this.ctx == null || !this.ctx.executor().inEventLoop()) {
            throw new AssertionError();
        }
        FlowState state = state(stream);
        state.incrementInitialStreamWindow(delta);
        state.writeWindowUpdateIfNeeded();
    }

    @Override // io.netty.handler.codec.http2.Http2LocalFlowController
    public boolean consumeBytes(Http2Stream stream, int numBytes) throws Http2Exception {
        if (this.ctx == null || !this.ctx.executor().inEventLoop()) {
            throw new AssertionError();
        }
        ObjectUtil.checkPositiveOrZero(numBytes, "numBytes");
        if (numBytes == 0 || stream == null || isClosed(stream)) {
            return false;
        }
        if (stream.id() == 0) {
            throw new UnsupportedOperationException("Returning bytes for the connection window is not supported");
        }
        return consumeAllBytes(state(stream), numBytes);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean consumeAllBytes(FlowState state, int numBytes) throws Http2Exception {
        return connectionState().consumeBytes(numBytes) | state.consumeBytes(numBytes);
    }

    @Override // io.netty.handler.codec.http2.Http2LocalFlowController
    public int unconsumedBytes(Http2Stream stream) {
        return state(stream).unconsumedBytes();
    }

    private static void checkValidRatio(float ratio) {
        if (Double.compare(ratio, 0.0d) <= 0 || Double.compare(ratio, 1.0d) >= 0) {
            throw new IllegalArgumentException("Invalid ratio: " + ratio);
        }
    }

    public void windowUpdateRatio(float ratio) {
        if (this.ctx != null && !this.ctx.executor().inEventLoop()) {
            throw new AssertionError();
        }
        checkValidRatio(ratio);
        this.windowUpdateRatio = ratio;
    }

    public float windowUpdateRatio() {
        return this.windowUpdateRatio;
    }

    public void windowUpdateRatio(Http2Stream stream, float ratio) throws Http2Exception {
        if (this.ctx == null || !this.ctx.executor().inEventLoop()) {
            throw new AssertionError();
        }
        checkValidRatio(ratio);
        FlowState state = state(stream);
        state.windowUpdateRatio(ratio);
        state.writeWindowUpdateIfNeeded();
    }

    public float windowUpdateRatio(Http2Stream stream) throws Http2Exception {
        return state(stream).windowUpdateRatio();
    }

    @Override // io.netty.handler.codec.http2.Http2LocalFlowController
    public void receiveFlowControlledFrame(Http2Stream stream, ByteBuf data, int padding, boolean endOfStream) throws Http2Exception {
        if (this.ctx == null || !this.ctx.executor().inEventLoop()) {
            throw new AssertionError();
        }
        int dataLength = data.readableBytes() + padding;
        FlowState connectionState = connectionState();
        connectionState.receiveFlowControlledFrame(dataLength);
        if (stream != null && !isClosed(stream)) {
            FlowState state = state(stream);
            state.endOfStream(endOfStream);
            state.receiveFlowControlledFrame(dataLength);
        } else if (dataLength > 0) {
            connectionState.consumeBytes(dataLength);
        }
    }

    private FlowState connectionState() {
        return (FlowState) this.connection.connectionStream().getProperty(this.stateKey);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public FlowState state(Http2Stream stream) {
        return (FlowState) stream.getProperty(this.stateKey);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isClosed(Http2Stream stream) {
        return stream.state() == Http2Stream.State.CLOSED;
    }

    /* loaded from: classes4.dex */
    private final class AutoRefillState extends DefaultState {
        AutoRefillState(Http2Stream stream, int initialWindowSize) {
            super(stream, initialWindowSize);
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2LocalFlowController.DefaultState, io.netty.handler.codec.http2.DefaultHttp2LocalFlowController.FlowState
        public void receiveFlowControlledFrame(int dataLength) throws Http2Exception {
            super.receiveFlowControlledFrame(dataLength);
            super.consumeBytes(dataLength);
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2LocalFlowController.DefaultState, io.netty.handler.codec.http2.DefaultHttp2LocalFlowController.FlowState
        public boolean consumeBytes(int numBytes) throws Http2Exception {
            return false;
        }
    }

    /* loaded from: classes4.dex */
    private class DefaultState implements FlowState {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private boolean endOfStream;
        private int initialStreamWindowSize;
        private int lowerBound;
        private int processedWindow;
        private final Http2Stream stream;
        private float streamWindowUpdateRatio;
        private int window;

        DefaultState(Http2Stream stream, int initialWindowSize) {
            this.stream = stream;
            window(initialWindowSize);
            this.streamWindowUpdateRatio = DefaultHttp2LocalFlowController.this.windowUpdateRatio;
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2LocalFlowController.FlowState
        public void window(int initialWindowSize) {
            if (DefaultHttp2LocalFlowController.this.ctx != null && !DefaultHttp2LocalFlowController.this.ctx.executor().inEventLoop()) {
                throw new AssertionError();
            }
            this.initialStreamWindowSize = initialWindowSize;
            this.processedWindow = initialWindowSize;
            this.window = initialWindowSize;
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2LocalFlowController.FlowState
        public int windowSize() {
            return this.window;
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2LocalFlowController.FlowState
        public int initialWindowSize() {
            return this.initialStreamWindowSize;
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2LocalFlowController.FlowState
        public void endOfStream(boolean endOfStream) {
            this.endOfStream = endOfStream;
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2LocalFlowController.FlowState
        public float windowUpdateRatio() {
            return this.streamWindowUpdateRatio;
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2LocalFlowController.FlowState
        public void windowUpdateRatio(float ratio) {
            if (DefaultHttp2LocalFlowController.this.ctx != null && !DefaultHttp2LocalFlowController.this.ctx.executor().inEventLoop()) {
                throw new AssertionError();
            }
            this.streamWindowUpdateRatio = ratio;
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2LocalFlowController.FlowState
        public void incrementInitialStreamWindow(int delta) {
            int newValue = (int) Math.min(2147483647L, Math.max(0L, this.initialStreamWindowSize + delta));
            int delta2 = newValue - this.initialStreamWindowSize;
            this.initialStreamWindowSize += delta2;
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2LocalFlowController.FlowState
        public void incrementFlowControlWindows(int delta) throws Http2Exception {
            if (delta > 0 && this.window > Integer.MAX_VALUE - delta) {
                throw Http2Exception.streamError(this.stream.id(), Http2Error.FLOW_CONTROL_ERROR, "Flow control window overflowed for stream: %d", Integer.valueOf(this.stream.id()));
            }
            this.window += delta;
            this.processedWindow += delta;
            this.lowerBound = Math.min(delta, 0);
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2LocalFlowController.FlowState
        public void receiveFlowControlledFrame(int dataLength) throws Http2Exception {
            if (dataLength < 0) {
                throw new AssertionError();
            }
            this.window -= dataLength;
            if (this.window < this.lowerBound) {
                throw Http2Exception.streamError(this.stream.id(), Http2Error.FLOW_CONTROL_ERROR, "Flow control window exceeded for stream: %d", Integer.valueOf(this.stream.id()));
            }
        }

        private void returnProcessedBytes(int delta) throws Http2Exception {
            if (this.processedWindow - delta < this.window) {
                throw Http2Exception.streamError(this.stream.id(), Http2Error.INTERNAL_ERROR, "Attempting to return too many bytes for stream %d", Integer.valueOf(this.stream.id()));
            }
            this.processedWindow -= delta;
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2LocalFlowController.FlowState
        public boolean consumeBytes(int numBytes) throws Http2Exception {
            returnProcessedBytes(numBytes);
            return writeWindowUpdateIfNeeded();
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2LocalFlowController.FlowState
        public int unconsumedBytes() {
            return this.processedWindow - this.window;
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2LocalFlowController.FlowState
        public boolean writeWindowUpdateIfNeeded() throws Http2Exception {
            if (this.endOfStream || this.initialStreamWindowSize <= 0 || DefaultHttp2LocalFlowController.isClosed(this.stream)) {
                return false;
            }
            int threshold = (int) (this.initialStreamWindowSize * this.streamWindowUpdateRatio);
            if (this.processedWindow > threshold) {
                return false;
            }
            writeWindowUpdate();
            return true;
        }

        private void writeWindowUpdate() throws Http2Exception {
            int deltaWindowSize = this.initialStreamWindowSize - this.processedWindow;
            try {
                incrementFlowControlWindows(deltaWindowSize);
                DefaultHttp2LocalFlowController.this.frameWriter.writeWindowUpdate(DefaultHttp2LocalFlowController.this.ctx, this.stream.id(), deltaWindowSize, DefaultHttp2LocalFlowController.this.ctx.newPromise());
            } catch (Throwable t) {
                throw Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, t, "Attempting to return too many bytes for stream %d", Integer.valueOf(this.stream.id()));
            }
        }
    }

    /* loaded from: classes4.dex */
    private final class WindowUpdateVisitor implements Http2StreamVisitor {
        private Http2Exception.CompositeStreamException compositeException;
        private final int delta;

        WindowUpdateVisitor(int delta) {
            this.delta = delta;
        }

        @Override // io.netty.handler.codec.http2.Http2StreamVisitor
        public boolean visit(Http2Stream stream) throws Http2Exception {
            try {
                FlowState state = DefaultHttp2LocalFlowController.this.state(stream);
                state.incrementFlowControlWindows(this.delta);
                state.incrementInitialStreamWindow(this.delta);
                return true;
            } catch (Http2Exception.StreamException e) {
                if (this.compositeException == null) {
                    this.compositeException = new Http2Exception.CompositeStreamException(e.error(), 4);
                }
                this.compositeException.add(e);
                return true;
            }
        }

        public void throwIfError() throws Http2Exception.CompositeStreamException {
            if (this.compositeException != null) {
                throw this.compositeException;
            }
        }
    }
}
