package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.CoalescingBufferQueue;
import io.netty.handler.codec.http.HttpStatusClass;
import io.netty.handler.codec.http2.Http2CodecUtil;
import io.netty.handler.codec.http2.Http2FrameWriter;
import io.netty.handler.codec.http2.Http2HeadersEncoder;
import io.netty.handler.codec.http2.Http2RemoteFlowController;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.ObjectUtil;
import java.util.ArrayDeque;
import java.util.Queue;

/* loaded from: classes4.dex */
public class DefaultHttp2ConnectionEncoder implements Http2ConnectionEncoder, Http2SettingsReceivedConsumer {
    private final Http2Connection connection;
    private final Http2FrameWriter frameWriter;
    private Http2LifecycleManager lifecycleManager;
    private final Queue<Http2Settings> outstandingLocalSettingsQueue = new ArrayDeque(4);
    private Queue<Http2Settings> outstandingRemoteSettingsQueue;

    public DefaultHttp2ConnectionEncoder(Http2Connection connection, Http2FrameWriter frameWriter) {
        this.connection = (Http2Connection) ObjectUtil.checkNotNull(connection, "connection");
        this.frameWriter = (Http2FrameWriter) ObjectUtil.checkNotNull(frameWriter, "frameWriter");
        if (connection.remote().flowController() == null) {
            connection.remote().flowController(new DefaultHttp2RemoteFlowController(connection));
        }
    }

    @Override // io.netty.handler.codec.http2.Http2ConnectionEncoder
    public void lifecycleManager(Http2LifecycleManager lifecycleManager) {
        this.lifecycleManager = (Http2LifecycleManager) ObjectUtil.checkNotNull(lifecycleManager, "lifecycleManager");
    }

    @Override // io.netty.handler.codec.http2.Http2ConnectionEncoder
    public Http2FrameWriter frameWriter() {
        return this.frameWriter;
    }

    @Override // io.netty.handler.codec.http2.Http2ConnectionEncoder
    public Http2Connection connection() {
        return this.connection;
    }

    @Override // io.netty.handler.codec.http2.Http2ConnectionEncoder
    public final Http2RemoteFlowController flowController() {
        return connection().remote().flowController();
    }

    @Override // io.netty.handler.codec.http2.Http2ConnectionEncoder
    public void remoteSettings(Http2Settings settings) throws Http2Exception {
        Boolean pushEnabled = settings.pushEnabled();
        Http2FrameWriter.Configuration config = configuration();
        Http2HeadersEncoder.Configuration outboundHeaderConfig = config.headersConfiguration();
        Http2FrameSizePolicy outboundFrameSizePolicy = config.frameSizePolicy();
        if (pushEnabled != null) {
            if (!this.connection.isServer() && pushEnabled.booleanValue()) {
                throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Client received a value of ENABLE_PUSH specified to other than 0", new Object[0]);
            }
            this.connection.remote().allowPushTo(pushEnabled.booleanValue());
        }
        Long maxConcurrentStreams = settings.maxConcurrentStreams();
        if (maxConcurrentStreams != null) {
            this.connection.local().maxActiveStreams((int) Math.min(maxConcurrentStreams.longValue(), 2147483647L));
        }
        Long headerTableSize = settings.headerTableSize();
        if (headerTableSize != null) {
            outboundHeaderConfig.maxHeaderTableSize(headerTableSize.longValue());
        }
        Long maxHeaderListSize = settings.maxHeaderListSize();
        if (maxHeaderListSize != null) {
            outboundHeaderConfig.maxHeaderListSize(maxHeaderListSize.longValue());
        }
        Integer maxFrameSize = settings.maxFrameSize();
        if (maxFrameSize != null) {
            outboundFrameSizePolicy.maxFrameSize(maxFrameSize.intValue());
        }
        Integer initialWindowSize = settings.initialWindowSize();
        if (initialWindowSize != null) {
            flowController().initialWindowSize(initialWindowSize.intValue());
        }
    }

    @Override // io.netty.handler.codec.http2.Http2DataWriter
    public ChannelFuture writeData(ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding, boolean endOfStream, ChannelPromise promise) {
        ChannelPromise promise2 = promise.unvoid();
        try {
            Http2Stream stream = requireStream(streamId);
            switch (stream.state()) {
                case OPEN:
                case HALF_CLOSED_REMOTE:
                    flowController().addFlowControlled(stream, new FlowControlledData(stream, data, padding, endOfStream, promise2));
                    return promise2;
                default:
                    throw new IllegalStateException("Stream " + stream.id() + " in unexpected state " + stream.state());
            }
        } catch (Throwable e) {
            data.release();
            return promise2.setFailure(e);
        }
        data.release();
        return promise2.setFailure(e);
    }

    @Override // io.netty.handler.codec.http2.Http2FrameWriter
    public ChannelFuture writeHeaders(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding, boolean endStream, ChannelPromise promise) {
        return writeHeaders0(ctx, streamId, headers, false, 0, (short) 0, false, padding, endStream, promise);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean validateHeadersSentState(Http2Stream stream, Http2Headers headers, boolean isServer, boolean endOfStream) {
        boolean isInformational = isServer && HttpStatusClass.valueOf(headers.status()) == HttpStatusClass.INFORMATIONAL;
        if (((isInformational || !endOfStream) && stream.isHeadersSent()) || stream.isTrailersSent()) {
            throw new IllegalStateException("Stream " + stream.id() + " sent too many headers EOS: " + endOfStream);
        }
        return isInformational;
    }

    @Override // io.netty.handler.codec.http2.Http2FrameWriter
    public ChannelFuture writeHeaders(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endOfStream, ChannelPromise promise) {
        return writeHeaders0(ctx, streamId, headers, true, streamDependency, weight, exclusive, padding, endOfStream, promise);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static ChannelFuture sendHeaders(Http2FrameWriter frameWriter, ChannelHandlerContext ctx, int streamId, Http2Headers headers, boolean hasPriority, int streamDependency, short weight, boolean exclusive, int padding, boolean endOfStream, ChannelPromise promise) {
        if (hasPriority) {
            return frameWriter.writeHeaders(ctx, streamId, headers, streamDependency, weight, exclusive, padding, endOfStream, promise);
        }
        return frameWriter.writeHeaders(ctx, streamId, headers, padding, endOfStream, promise);
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:8:0x005d. Please report as an issue. */
    private ChannelFuture writeHeaders0(ChannelHandlerContext ctx, int streamId, Http2Headers headers, boolean hasPriority, int streamDependency, short weight, boolean exclusive, int padding, boolean endOfStream, ChannelPromise promise) {
        boolean z;
        Http2Stream stream;
        Http2Stream stream2;
        Http2RemoteFlowController flowController;
        ChannelPromise promise2 = promise;
        try {
            stream = this.connection.stream(streamId);
        } catch (Throwable th) {
            t = th;
            z = true;
        }
        try {
            if (stream == null) {
                try {
                    stream2 = this.connection.local().createStream(streamId, false);
                } catch (Http2Exception cause) {
                    if (!this.connection.remote().mayHaveCreatedStream(streamId)) {
                        throw cause;
                    }
                    promise2.tryFailure(new IllegalStateException("Stream no longer exists: " + streamId, cause));
                    return promise2;
                }
            } else {
                switch (stream.state()) {
                    case RESERVED_LOCAL:
                        stream.open(endOfStream);
                    case OPEN:
                    case HALF_CLOSED_REMOTE:
                        stream2 = stream;
                        break;
                    default:
                        z = true;
                        try {
                            throw new IllegalStateException("Stream " + stream.id() + " in unexpected state " + stream.state());
                        } catch (Throwable th2) {
                            t = th2;
                            promise2 = promise;
                            this.lifecycleManager.onError(ctx, z, t);
                            promise2.tryFailure(t);
                            return promise2;
                        }
                }
            }
            Http2RemoteFlowController flowController2 = flowController();
            if (endOfStream) {
                try {
                    if (flowController2.hasFlowControlled(stream2)) {
                        flowController2.addFlowControlled(stream2, new FlowControlledHeaders(stream2, headers, hasPriority, streamDependency, weight, exclusive, padding, true, promise));
                        return promise;
                    }
                    flowController = flowController2;
                } catch (Throwable th3) {
                    t = th3;
                    promise2 = promise;
                    z = true;
                    this.lifecycleManager.onError(ctx, z, t);
                    promise2.tryFailure(t);
                    return promise2;
                }
            } else {
                flowController = flowController2;
            }
            try {
                promise2 = promise.unvoid();
                try {
                    boolean isInformational = validateHeadersSentState(stream2, headers, this.connection.isServer(), endOfStream);
                    ChannelFuture future = sendHeaders(this.frameWriter, ctx, streamId, headers, hasPriority, streamDependency, weight, exclusive, padding, endOfStream, promise2);
                    Throwable failureCause = future.cause();
                    if (failureCause == null) {
                        stream2.headersSent(isInformational);
                        if (future.isSuccess()) {
                            z = true;
                        } else {
                            notifyLifecycleManagerOnError(future, ctx);
                            z = true;
                        }
                    } else {
                        z = true;
                        try {
                            this.lifecycleManager.onError(ctx, true, failureCause);
                        } catch (Throwable th4) {
                            t = th4;
                            this.lifecycleManager.onError(ctx, z, t);
                            promise2.tryFailure(t);
                            return promise2;
                        }
                    }
                    if (endOfStream) {
                        this.lifecycleManager.closeStreamLocal(stream2, future);
                    }
                    return future;
                } catch (Throwable th5) {
                    t = th5;
                    z = true;
                }
            } catch (Throwable th6) {
                t = th6;
                z = true;
                promise2 = promise;
                this.lifecycleManager.onError(ctx, z, t);
                promise2.tryFailure(t);
                return promise2;
            }
        } catch (Throwable th7) {
            t = th7;
            z = true;
            this.lifecycleManager.onError(ctx, z, t);
            promise2.tryFailure(t);
            return promise2;
        }
    }

    @Override // io.netty.handler.codec.http2.Http2FrameWriter
    public ChannelFuture writePriority(ChannelHandlerContext ctx, int streamId, int streamDependency, short weight, boolean exclusive, ChannelPromise promise) {
        return this.frameWriter.writePriority(ctx, streamId, streamDependency, weight, exclusive, promise);
    }

    @Override // io.netty.handler.codec.http2.Http2FrameWriter
    public ChannelFuture writeRstStream(ChannelHandlerContext ctx, int streamId, long errorCode, ChannelPromise promise) {
        return this.lifecycleManager.resetStream(ctx, streamId, errorCode, promise);
    }

    @Override // io.netty.handler.codec.http2.Http2FrameWriter
    public ChannelFuture writeSettings(ChannelHandlerContext ctx, Http2Settings settings, ChannelPromise promise) {
        this.outstandingLocalSettingsQueue.add(settings);
        try {
            Boolean pushEnabled = settings.pushEnabled();
            if (pushEnabled != null && this.connection.isServer()) {
                throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Server sending SETTINGS frame with ENABLE_PUSH specified", new Object[0]);
            }
            return this.frameWriter.writeSettings(ctx, settings, promise);
        } catch (Throwable e) {
            return promise.setFailure(e);
        }
    }

    @Override // io.netty.handler.codec.http2.Http2FrameWriter
    public ChannelFuture writeSettingsAck(ChannelHandlerContext ctx, ChannelPromise promise) {
        if (this.outstandingRemoteSettingsQueue == null) {
            return this.frameWriter.writeSettingsAck(ctx, promise);
        }
        Http2Settings settings = this.outstandingRemoteSettingsQueue.poll();
        if (settings == null) {
            return promise.setFailure((Throwable) new Http2Exception(Http2Error.INTERNAL_ERROR, "attempted to write a SETTINGS ACK with no  pending SETTINGS"));
        }
        Http2CodecUtil.SimpleChannelPromiseAggregator aggregator = new Http2CodecUtil.SimpleChannelPromiseAggregator(promise, ctx.channel(), ctx.executor());
        this.frameWriter.writeSettingsAck(ctx, aggregator.newPromise());
        ChannelPromise applySettingsPromise = aggregator.newPromise();
        try {
            remoteSettings(settings);
            applySettingsPromise.setSuccess();
        } catch (Throwable e) {
            applySettingsPromise.setFailure(e);
            this.lifecycleManager.onError(ctx, true, e);
        }
        return aggregator.doneAllocatingPromises();
    }

    @Override // io.netty.handler.codec.http2.Http2FrameWriter
    public ChannelFuture writePing(ChannelHandlerContext ctx, boolean ack, long data, ChannelPromise promise) {
        return this.frameWriter.writePing(ctx, ack, data, promise);
    }

    @Override // io.netty.handler.codec.http2.Http2FrameWriter
    public ChannelFuture writePushPromise(ChannelHandlerContext ctx, int streamId, int promisedStreamId, Http2Headers headers, int padding, ChannelPromise promise) {
        ChannelPromise promise2;
        Http2Stream stream;
        try {
        } catch (Throwable th) {
            t = th;
        }
        try {
            if (this.connection.goAwayReceived()) {
                throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Sending PUSH_PROMISE after GO_AWAY received.", new Object[0]);
            }
            try {
                stream = requireStream(streamId);
                this.connection.local().reservePushStream(promisedStreamId, stream);
                promise2 = promise.unvoid();
            } catch (Throwable th2) {
                t = th2;
                promise2 = promise;
                this.lifecycleManager.onError(ctx, true, t);
                promise2.tryFailure(t);
                return promise2;
            }
            try {
                ChannelFuture future = this.frameWriter.writePushPromise(ctx, streamId, promisedStreamId, headers, padding, promise2);
                Throwable failureCause = future.cause();
                if (failureCause == null) {
                    stream.pushPromiseSent();
                    if (!future.isSuccess()) {
                        notifyLifecycleManagerOnError(future, ctx);
                    }
                } else {
                    this.lifecycleManager.onError(ctx, true, failureCause);
                }
                return future;
            } catch (Throwable th3) {
                t = th3;
                this.lifecycleManager.onError(ctx, true, t);
                promise2.tryFailure(t);
                return promise2;
            }
        } catch (Throwable th4) {
            t = th4;
            promise2 = promise;
            this.lifecycleManager.onError(ctx, true, t);
            promise2.tryFailure(t);
            return promise2;
        }
    }

    @Override // io.netty.handler.codec.http2.Http2FrameWriter
    public ChannelFuture writeGoAway(ChannelHandlerContext ctx, int lastStreamId, long errorCode, ByteBuf debugData, ChannelPromise promise) {
        return this.lifecycleManager.goAway(ctx, lastStreamId, errorCode, debugData, promise);
    }

    @Override // io.netty.handler.codec.http2.Http2FrameWriter
    public ChannelFuture writeWindowUpdate(ChannelHandlerContext ctx, int streamId, int windowSizeIncrement, ChannelPromise promise) {
        return promise.setFailure((Throwable) new UnsupportedOperationException("Use the Http2[Inbound|Outbound]FlowController objects to control window sizes"));
    }

    @Override // io.netty.handler.codec.http2.Http2ConnectionEncoder, io.netty.handler.codec.http2.Http2FrameWriter
    public ChannelFuture writeFrame(ChannelHandlerContext ctx, byte frameType, int streamId, Http2Flags flags, ByteBuf payload, ChannelPromise promise) {
        return this.frameWriter.writeFrame(ctx, frameType, streamId, flags, payload, promise);
    }

    @Override // io.netty.handler.codec.http2.Http2FrameWriter, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.frameWriter.close();
    }

    @Override // io.netty.handler.codec.http2.Http2ConnectionEncoder
    public Http2Settings pollSentSettings() {
        return this.outstandingLocalSettingsQueue.poll();
    }

    @Override // io.netty.handler.codec.http2.Http2FrameWriter
    public Http2FrameWriter.Configuration configuration() {
        return this.frameWriter.configuration();
    }

    private Http2Stream requireStream(int streamId) {
        String message;
        Http2Stream stream = this.connection.stream(streamId);
        if (stream == null) {
            if (this.connection.streamMayHaveExisted(streamId)) {
                message = "Stream no longer exists: " + streamId;
            } else {
                message = "Stream does not exist: " + streamId;
            }
            throw new IllegalArgumentException(message);
        }
        return stream;
    }

    @Override // io.netty.handler.codec.http2.Http2SettingsReceivedConsumer
    public void consumeReceivedSettings(Http2Settings settings) {
        if (this.outstandingRemoteSettingsQueue == null) {
            this.outstandingRemoteSettingsQueue = new ArrayDeque(2);
        }
        this.outstandingRemoteSettingsQueue.add(settings);
    }

    /* loaded from: classes4.dex */
    private final class FlowControlledData extends FlowControlledBase {
        private int dataSize;
        private final CoalescingBufferQueue queue;

        FlowControlledData(Http2Stream stream, ByteBuf buf, int padding, boolean endOfStream, ChannelPromise promise) {
            super(stream, padding, endOfStream, promise);
            this.queue = new CoalescingBufferQueue(promise.channel());
            this.queue.add(buf, promise);
            this.dataSize = this.queue.readableBytes();
        }

        @Override // io.netty.handler.codec.http2.Http2RemoteFlowController.FlowControlled
        public int size() {
            return this.dataSize + this.padding;
        }

        @Override // io.netty.handler.codec.http2.Http2RemoteFlowController.FlowControlled
        public void error(ChannelHandlerContext ctx, Throwable cause) {
            this.queue.releaseAndFailAll(cause);
            DefaultHttp2ConnectionEncoder.this.lifecycleManager.onError(ctx, true, cause);
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r1v5, types: [io.netty.channel.ChannelPromise] */
        /* JADX WARN: Type inference failed for: r3v1, types: [io.netty.channel.ChannelPromise] */
        @Override // io.netty.handler.codec.http2.Http2RemoteFlowController.FlowControlled
        public void write(ChannelHandlerContext channelHandlerContext, int allowedBytes) {
            int queuedData = this.queue.readableBytes();
            boolean z = false;
            if (!this.endOfStream) {
                if (queuedData == 0) {
                    if (this.queue.isEmpty()) {
                        this.dataSize = 0;
                        this.padding = 0;
                        return;
                    } else {
                        ?? addListener = channelHandlerContext.newPromise().addListener((GenericFutureListener<? extends Future<? super Void>>) this);
                        channelHandlerContext.write(this.queue.remove(0, addListener), addListener);
                        return;
                    }
                }
                if (allowedBytes == 0) {
                    return;
                }
            }
            int writableData = Math.min(queuedData, allowedBytes);
            ?? addListener2 = channelHandlerContext.newPromise().addListener((GenericFutureListener<? extends Future<? super Void>>) this);
            ByteBuf toWrite = this.queue.remove(writableData, addListener2);
            this.dataSize = this.queue.readableBytes();
            int writablePadding = Math.min(allowedBytes - writableData, this.padding);
            this.padding -= writablePadding;
            Http2FrameWriter frameWriter = DefaultHttp2ConnectionEncoder.this.frameWriter();
            int id = this.stream.id();
            if (this.endOfStream && size() == 0) {
                z = true;
            }
            frameWriter.writeData(channelHandlerContext, id, toWrite, writablePadding, z, addListener2);
        }

        @Override // io.netty.handler.codec.http2.Http2RemoteFlowController.FlowControlled
        public boolean merge(ChannelHandlerContext ctx, Http2RemoteFlowController.FlowControlled next) {
            if (FlowControlledData.class == next.getClass()) {
                FlowControlledData nextData = (FlowControlledData) next;
                if (Integer.MAX_VALUE - nextData.size() < size()) {
                    return false;
                }
                nextData.queue.copyTo(this.queue);
                this.dataSize = this.queue.readableBytes();
                this.padding = Math.max(this.padding, nextData.padding);
                this.endOfStream = nextData.endOfStream;
                return true;
            }
            return false;
        }
    }

    private void notifyLifecycleManagerOnError(ChannelFuture future, final ChannelHandlerContext ctx) {
        future.addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.handler.codec.http2.DefaultHttp2ConnectionEncoder.1
            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(ChannelFuture future2) throws Exception {
                Throwable cause = future2.cause();
                if (cause != null) {
                    DefaultHttp2ConnectionEncoder.this.lifecycleManager.onError(ctx, true, cause);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class FlowControlledHeaders extends FlowControlledBase {
        private final boolean exclusive;
        private final boolean hasPriority;
        private final Http2Headers headers;
        private final int streamDependency;
        private final short weight;

        FlowControlledHeaders(Http2Stream stream, Http2Headers headers, boolean hasPriority, int streamDependency, short weight, boolean exclusive, int padding, boolean endOfStream, ChannelPromise promise) {
            super(stream, padding, endOfStream, promise.unvoid());
            this.headers = headers;
            this.hasPriority = hasPriority;
            this.streamDependency = streamDependency;
            this.weight = weight;
            this.exclusive = exclusive;
        }

        @Override // io.netty.handler.codec.http2.Http2RemoteFlowController.FlowControlled
        public int size() {
            return 0;
        }

        @Override // io.netty.handler.codec.http2.Http2RemoteFlowController.FlowControlled
        public void error(ChannelHandlerContext ctx, Throwable cause) {
            if (ctx != null) {
                DefaultHttp2ConnectionEncoder.this.lifecycleManager.onError(ctx, true, cause);
            }
            this.promise.tryFailure(cause);
        }

        @Override // io.netty.handler.codec.http2.Http2RemoteFlowController.FlowControlled
        public void write(ChannelHandlerContext ctx, int allowedBytes) {
            boolean isInformational = DefaultHttp2ConnectionEncoder.validateHeadersSentState(this.stream, this.headers, DefaultHttp2ConnectionEncoder.this.connection.isServer(), this.endOfStream);
            this.promise.addListener((GenericFutureListener<? extends Future<? super Void>>) this);
            ChannelFuture f = DefaultHttp2ConnectionEncoder.sendHeaders(DefaultHttp2ConnectionEncoder.this.frameWriter, ctx, this.stream.id(), this.headers, this.hasPriority, this.streamDependency, this.weight, this.exclusive, this.padding, this.endOfStream, this.promise);
            Throwable failureCause = f.cause();
            if (failureCause == null) {
                this.stream.headersSent(isInformational);
            }
        }

        @Override // io.netty.handler.codec.http2.Http2RemoteFlowController.FlowControlled
        public boolean merge(ChannelHandlerContext ctx, Http2RemoteFlowController.FlowControlled next) {
            return false;
        }
    }

    /* loaded from: classes4.dex */
    public abstract class FlowControlledBase implements Http2RemoteFlowController.FlowControlled, ChannelFutureListener {
        protected boolean endOfStream;
        protected int padding;
        protected ChannelPromise promise;
        protected final Http2Stream stream;

        FlowControlledBase(Http2Stream stream, int padding, boolean endOfStream, ChannelPromise promise) {
            ObjectUtil.checkPositiveOrZero(padding, "padding");
            this.padding = padding;
            this.endOfStream = endOfStream;
            this.stream = stream;
            this.promise = promise;
        }

        @Override // io.netty.handler.codec.http2.Http2RemoteFlowController.FlowControlled
        public void writeComplete() {
            if (this.endOfStream) {
                DefaultHttp2ConnectionEncoder.this.lifecycleManager.closeStreamLocal(this.stream, this.promise);
            }
        }

        @Override // io.netty.util.concurrent.GenericFutureListener
        public void operationComplete(ChannelFuture future) throws Exception {
            if (!future.isSuccess()) {
                error(DefaultHttp2ConnectionEncoder.this.flowController().channelHandlerContext(), future.cause());
            }
        }
    }
}
