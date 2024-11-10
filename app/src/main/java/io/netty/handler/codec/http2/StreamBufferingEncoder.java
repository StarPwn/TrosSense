package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

/* loaded from: classes4.dex */
public class StreamBufferingEncoder extends DecoratingHttp2ConnectionEncoder {
    private boolean closed;
    private GoAwayDetail goAwayDetail;
    private int maxConcurrentStreams;
    private final TreeMap<Integer, PendingStream> pendingStreams;

    /* loaded from: classes4.dex */
    public static final class Http2ChannelClosedException extends Http2Exception {
        private static final long serialVersionUID = 4768543442094476971L;

        public Http2ChannelClosedException() {
            super(Http2Error.REFUSED_STREAM, "Connection closed");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class GoAwayDetail {
        private final byte[] debugData;
        private final long errorCode;
        private final int lastStreamId;

        GoAwayDetail(int lastStreamId, long errorCode, byte[] debugData) {
            this.lastStreamId = lastStreamId;
            this.errorCode = errorCode;
            this.debugData = (byte[]) debugData.clone();
        }
    }

    /* loaded from: classes4.dex */
    public static final class Http2GoAwayException extends Http2Exception {
        private static final long serialVersionUID = 1326785622777291198L;
        private final GoAwayDetail goAwayDetail;

        public Http2GoAwayException(int lastStreamId, long errorCode, byte[] debugData) {
            this(new GoAwayDetail(lastStreamId, errorCode, debugData));
        }

        Http2GoAwayException(GoAwayDetail goAwayDetail) {
            super(Http2Error.STREAM_CLOSED);
            this.goAwayDetail = goAwayDetail;
        }

        public int lastStreamId() {
            return this.goAwayDetail.lastStreamId;
        }

        public long errorCode() {
            return this.goAwayDetail.errorCode;
        }

        public byte[] debugData() {
            return (byte[]) this.goAwayDetail.debugData.clone();
        }
    }

    public StreamBufferingEncoder(Http2ConnectionEncoder delegate) {
        this(delegate, 100);
    }

    public StreamBufferingEncoder(Http2ConnectionEncoder delegate, int initialMaxConcurrentStreams) {
        super(delegate);
        this.pendingStreams = new TreeMap<>();
        this.maxConcurrentStreams = initialMaxConcurrentStreams;
        connection().addListener(new Http2ConnectionAdapter() { // from class: io.netty.handler.codec.http2.StreamBufferingEncoder.1
            @Override // io.netty.handler.codec.http2.Http2ConnectionAdapter, io.netty.handler.codec.http2.Http2Connection.Listener
            public void onGoAwayReceived(int lastStreamId, long errorCode, ByteBuf debugData) {
                StreamBufferingEncoder.this.goAwayDetail = new GoAwayDetail(lastStreamId, errorCode, ByteBufUtil.getBytes(debugData, debugData.readerIndex(), debugData.readableBytes(), false));
                StreamBufferingEncoder.this.cancelGoAwayStreams(StreamBufferingEncoder.this.goAwayDetail);
            }

            @Override // io.netty.handler.codec.http2.Http2ConnectionAdapter, io.netty.handler.codec.http2.Http2Connection.Listener
            public void onStreamClosed(Http2Stream stream) {
                StreamBufferingEncoder.this.tryCreatePendingStreams();
            }
        });
    }

    public int numBufferedStreams() {
        return this.pendingStreams.size();
    }

    @Override // io.netty.handler.codec.http2.DecoratingHttp2FrameWriter, io.netty.handler.codec.http2.Http2FrameWriter
    public ChannelFuture writeHeaders(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding, boolean endStream, ChannelPromise promise) {
        return writeHeaders(ctx, streamId, headers, 0, (short) 16, false, padding, endStream, promise);
    }

    @Override // io.netty.handler.codec.http2.DecoratingHttp2FrameWriter, io.netty.handler.codec.http2.Http2FrameWriter
    public ChannelFuture writeHeaders(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endOfStream, ChannelPromise promise) {
        PendingStream pendingStream;
        if (this.closed) {
            return promise.setFailure((Throwable) new Http2ChannelClosedException());
        }
        if (!isExistingStream(streamId) && !canCreateStream()) {
            if (this.goAwayDetail != null) {
                return promise.setFailure((Throwable) new Http2GoAwayException(this.goAwayDetail));
            }
            PendingStream pendingStream2 = this.pendingStreams.get(Integer.valueOf(streamId));
            if (pendingStream2 == null) {
                PendingStream pendingStream3 = new PendingStream(ctx, streamId);
                this.pendingStreams.put(Integer.valueOf(streamId), pendingStream3);
                pendingStream = pendingStream3;
            } else {
                pendingStream = pendingStream2;
            }
            pendingStream.frames.add(new HeadersFrame(headers, streamDependency, weight, exclusive, padding, endOfStream, promise));
            return promise;
        }
        return super.writeHeaders(ctx, streamId, headers, streamDependency, weight, exclusive, padding, endOfStream, promise);
    }

    @Override // io.netty.handler.codec.http2.DecoratingHttp2FrameWriter, io.netty.handler.codec.http2.Http2FrameWriter
    public ChannelFuture writeRstStream(ChannelHandlerContext ctx, int streamId, long errorCode, ChannelPromise promise) {
        if (isExistingStream(streamId)) {
            return super.writeRstStream(ctx, streamId, errorCode, promise);
        }
        PendingStream stream = this.pendingStreams.remove(Integer.valueOf(streamId));
        if (stream != null) {
            stream.close(null);
            promise.setSuccess();
        } else {
            promise.setFailure((Throwable) Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Stream does not exist %d", Integer.valueOf(streamId)));
        }
        return promise;
    }

    @Override // io.netty.handler.codec.http2.DecoratingHttp2FrameWriter, io.netty.handler.codec.http2.Http2DataWriter
    public ChannelFuture writeData(ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding, boolean endOfStream, ChannelPromise promise) {
        if (isExistingStream(streamId)) {
            return super.writeData(ctx, streamId, data, padding, endOfStream, promise);
        }
        PendingStream pendingStream = this.pendingStreams.get(Integer.valueOf(streamId));
        if (pendingStream != null) {
            pendingStream.frames.add(new DataFrame(data, padding, endOfStream, promise));
        } else {
            ReferenceCountUtil.safeRelease(data);
            promise.setFailure((Throwable) Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Stream does not exist %d", Integer.valueOf(streamId)));
        }
        return promise;
    }

    @Override // io.netty.handler.codec.http2.DecoratingHttp2ConnectionEncoder, io.netty.handler.codec.http2.Http2ConnectionEncoder
    public void remoteSettings(Http2Settings settings) throws Http2Exception {
        super.remoteSettings(settings);
        this.maxConcurrentStreams = connection().local().maxActiveStreams();
        tryCreatePendingStreams();
    }

    @Override // io.netty.handler.codec.http2.DecoratingHttp2FrameWriter, io.netty.handler.codec.http2.Http2FrameWriter, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        try {
            if (!this.closed) {
                this.closed = true;
                Http2ChannelClosedException e = new Http2ChannelClosedException();
                while (!this.pendingStreams.isEmpty()) {
                    PendingStream stream = this.pendingStreams.pollFirstEntry().getValue();
                    stream.close(e);
                }
            }
        } finally {
            super.close();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tryCreatePendingStreams() {
        while (!this.pendingStreams.isEmpty() && canCreateStream()) {
            Map.Entry<Integer, PendingStream> entry = this.pendingStreams.pollFirstEntry();
            PendingStream pendingStream = entry.getValue();
            try {
                pendingStream.sendFrames();
            } catch (Throwable t) {
                pendingStream.close(t);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelGoAwayStreams(GoAwayDetail goAwayDetail) {
        Iterator<PendingStream> iter = this.pendingStreams.values().iterator();
        Exception e = new Http2GoAwayException(goAwayDetail);
        while (iter.hasNext()) {
            PendingStream stream = iter.next();
            if (stream.streamId > goAwayDetail.lastStreamId) {
                iter.remove();
                stream.close(e);
            }
        }
    }

    private boolean canCreateStream() {
        return connection().local().numActiveStreams() < this.maxConcurrentStreams;
    }

    private boolean isExistingStream(int streamId) {
        return streamId <= connection().local().lastStreamCreated();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class PendingStream {
        final ChannelHandlerContext ctx;
        final Queue<Frame> frames = new ArrayDeque(2);
        final int streamId;

        PendingStream(ChannelHandlerContext ctx, int streamId) {
            this.ctx = ctx;
            this.streamId = streamId;
        }

        void sendFrames() {
            for (Frame frame : this.frames) {
                frame.send(this.ctx, this.streamId);
            }
        }

        void close(Throwable t) {
            for (Frame frame : this.frames) {
                frame.release(t);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static abstract class Frame {
        final ChannelPromise promise;

        abstract void send(ChannelHandlerContext channelHandlerContext, int i);

        Frame(ChannelPromise promise) {
            this.promise = promise;
        }

        void release(Throwable t) {
            if (t == null) {
                this.promise.setSuccess();
            } else {
                this.promise.setFailure(t);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class HeadersFrame extends Frame {
        final boolean endOfStream;
        final boolean exclusive;
        final Http2Headers headers;
        final int padding;
        final int streamDependency;
        final short weight;

        HeadersFrame(Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endOfStream, ChannelPromise promise) {
            super(promise);
            this.headers = headers;
            this.streamDependency = streamDependency;
            this.weight = weight;
            this.exclusive = exclusive;
            this.padding = padding;
            this.endOfStream = endOfStream;
        }

        @Override // io.netty.handler.codec.http2.StreamBufferingEncoder.Frame
        void send(ChannelHandlerContext ctx, int streamId) {
            StreamBufferingEncoder.this.writeHeaders(ctx, streamId, this.headers, this.streamDependency, this.weight, this.exclusive, this.padding, this.endOfStream, this.promise);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class DataFrame extends Frame {
        final ByteBuf data;
        final boolean endOfStream;
        final int padding;

        DataFrame(ByteBuf data, int padding, boolean endOfStream, ChannelPromise promise) {
            super(promise);
            this.data = data;
            this.padding = padding;
            this.endOfStream = endOfStream;
        }

        @Override // io.netty.handler.codec.http2.StreamBufferingEncoder.Frame
        void release(Throwable t) {
            super.release(t);
            ReferenceCountUtil.safeRelease(this.data);
        }

        @Override // io.netty.handler.codec.http2.StreamBufferingEncoder.Frame
        void send(ChannelHandlerContext ctx, int streamId) {
            StreamBufferingEncoder.this.writeData(ctx, streamId, this.data, this.padding, this.endOfStream, this.promise);
        }
    }
}
