package io.netty.handler.codec.http2;

import io.netty.handler.codec.http2.Http2Connection;
import io.netty.handler.codec.http2.StreamByteDistributor;
import io.netty.util.internal.ObjectUtil;
import java.util.ArrayDeque;
import java.util.Deque;

/* loaded from: classes4.dex */
public final class UniformStreamByteDistributor implements StreamByteDistributor {
    private final Http2Connection.PropertyKey stateKey;
    private long totalStreamableBytes;
    private final Deque<State> queue = new ArrayDeque(4);
    private int minAllocationChunk = 1024;

    public UniformStreamByteDistributor(Http2Connection connection) {
        this.stateKey = connection.newKey();
        Http2Stream connectionStream = connection.connectionStream();
        connectionStream.setProperty(this.stateKey, new State(connectionStream));
        connection.addListener(new Http2ConnectionAdapter() { // from class: io.netty.handler.codec.http2.UniformStreamByteDistributor.1
            @Override // io.netty.handler.codec.http2.Http2ConnectionAdapter, io.netty.handler.codec.http2.Http2Connection.Listener
            public void onStreamAdded(Http2Stream stream) {
                stream.setProperty(UniformStreamByteDistributor.this.stateKey, new State(stream));
            }

            @Override // io.netty.handler.codec.http2.Http2ConnectionAdapter, io.netty.handler.codec.http2.Http2Connection.Listener
            public void onStreamClosed(Http2Stream stream) {
                UniformStreamByteDistributor.this.state(stream).close();
            }
        });
    }

    public void minAllocationChunk(int minAllocationChunk) {
        ObjectUtil.checkPositive(minAllocationChunk, "minAllocationChunk");
        this.minAllocationChunk = minAllocationChunk;
    }

    @Override // io.netty.handler.codec.http2.StreamByteDistributor
    public void updateStreamableBytes(StreamByteDistributor.StreamState streamState) {
        state(streamState.stream()).updateStreamableBytes(Http2CodecUtil.streamableBytes(streamState), streamState.hasFrame(), streamState.windowSize());
    }

    @Override // io.netty.handler.codec.http2.StreamByteDistributor
    public void updateDependencyTree(int childStreamId, int parentStreamId, short weight, boolean exclusive) {
    }

    @Override // io.netty.handler.codec.http2.StreamByteDistributor
    public boolean distribute(int maxBytes, StreamByteDistributor.Writer writer) throws Http2Exception {
        int size = this.queue.size();
        if (size == 0) {
            return this.totalStreamableBytes > 0;
        }
        int chunkSize = Math.max(this.minAllocationChunk, maxBytes / size);
        State state = this.queue.pollFirst();
        while (true) {
            state.enqueued = false;
            if (!state.windowNegative) {
                if (maxBytes == 0 && state.streamableBytes > 0) {
                    this.queue.addFirst(state);
                    state.enqueued = true;
                    break;
                }
                int chunk = Math.min(chunkSize, Math.min(maxBytes, state.streamableBytes));
                maxBytes -= chunk;
                state.write(chunk, writer);
            }
            State pollFirst = this.queue.pollFirst();
            state = pollFirst;
            if (pollFirst == null) {
                break;
            }
        }
        return this.totalStreamableBytes > 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public State state(Http2Stream stream) {
        return (State) ((Http2Stream) ObjectUtil.checkNotNull(stream, "stream")).getProperty(this.stateKey);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class State {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        boolean enqueued;
        final Http2Stream stream;
        int streamableBytes;
        boolean windowNegative;
        boolean writing;

        State(Http2Stream stream) {
            this.stream = stream;
        }

        void updateStreamableBytes(int newStreamableBytes, boolean hasFrame, int windowSize) {
            if (!hasFrame && newStreamableBytes != 0) {
                throw new AssertionError("hasFrame: " + hasFrame + " newStreamableBytes: " + newStreamableBytes);
            }
            int delta = newStreamableBytes - this.streamableBytes;
            if (delta != 0) {
                this.streamableBytes = newStreamableBytes;
                UniformStreamByteDistributor.this.totalStreamableBytes += delta;
            }
            this.windowNegative = windowSize < 0;
            if (hasFrame) {
                if (windowSize > 0 || (windowSize == 0 && !this.writing)) {
                    addToQueue();
                }
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        void write(int numBytes, StreamByteDistributor.Writer writer) throws Http2Exception {
            this.writing = true;
            try {
                writer.write(this.stream, numBytes);
            } finally {
            }
        }

        void addToQueue() {
            if (!this.enqueued) {
                this.enqueued = true;
                UniformStreamByteDistributor.this.queue.addLast(this);
            }
        }

        void removeFromQueue() {
            if (this.enqueued) {
                this.enqueued = false;
                UniformStreamByteDistributor.this.queue.remove(this);
            }
        }

        void close() {
            removeFromQueue();
            updateStreamableBytes(0, false, 0);
        }
    }
}
