package io.netty.handler.codec.spdy;

import io.netty.channel.ChannelPromise;
import io.netty.util.internal.PlatformDependent;
import java.util.Comparator;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class SpdySession {
    private final AtomicInteger receiveWindowSize;
    private final AtomicInteger sendWindowSize;
    private final AtomicInteger activeLocalStreams = new AtomicInteger();
    private final AtomicInteger activeRemoteStreams = new AtomicInteger();
    private final Map<Integer, StreamState> activeStreams = PlatformDependent.newConcurrentHashMap();
    private final StreamComparator streamComparator = new StreamComparator();

    /* JADX INFO: Access modifiers changed from: package-private */
    public SpdySession(int sendWindowSize, int receiveWindowSize) {
        this.sendWindowSize = new AtomicInteger(sendWindowSize);
        this.receiveWindowSize = new AtomicInteger(receiveWindowSize);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int numActiveStreams(boolean remote) {
        if (remote) {
            return this.activeRemoteStreams.get();
        }
        return this.activeLocalStreams.get();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean noActiveStreams() {
        return this.activeStreams.isEmpty();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isActiveStream(int streamId) {
        return this.activeStreams.containsKey(Integer.valueOf(streamId));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Map<Integer, StreamState> activeStreams() {
        Map<Integer, StreamState> streams = new TreeMap<>(this.streamComparator);
        streams.putAll(this.activeStreams);
        return streams;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void acceptStream(int streamId, byte priority, boolean remoteSideClosed, boolean localSideClosed, int sendWindowSize, int receiveWindowSize, boolean remote) {
        if (!remoteSideClosed || !localSideClosed) {
            StreamState state = this.activeStreams.put(Integer.valueOf(streamId), new StreamState(priority, remoteSideClosed, localSideClosed, sendWindowSize, receiveWindowSize));
            if (state == null) {
                if (remote) {
                    this.activeRemoteStreams.incrementAndGet();
                } else {
                    this.activeLocalStreams.incrementAndGet();
                }
            }
        }
    }

    private StreamState removeActiveStream(int streamId, boolean remote) {
        StreamState state = this.activeStreams.remove(Integer.valueOf(streamId));
        if (state != null) {
            if (remote) {
                this.activeRemoteStreams.decrementAndGet();
            } else {
                this.activeLocalStreams.decrementAndGet();
            }
        }
        return state;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeStream(int streamId, Throwable cause, boolean remote) {
        StreamState state = removeActiveStream(streamId, remote);
        if (state != null) {
            state.clearPendingWrites(cause);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isRemoteSideClosed(int streamId) {
        StreamState state = this.activeStreams.get(Integer.valueOf(streamId));
        return state == null || state.isRemoteSideClosed();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void closeRemoteSide(int streamId, boolean remote) {
        StreamState state = this.activeStreams.get(Integer.valueOf(streamId));
        if (state != null) {
            state.closeRemoteSide();
            if (state.isLocalSideClosed()) {
                removeActiveStream(streamId, remote);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isLocalSideClosed(int streamId) {
        StreamState state = this.activeStreams.get(Integer.valueOf(streamId));
        return state == null || state.isLocalSideClosed();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void closeLocalSide(int streamId, boolean remote) {
        StreamState state = this.activeStreams.get(Integer.valueOf(streamId));
        if (state != null) {
            state.closeLocalSide();
            if (state.isRemoteSideClosed()) {
                removeActiveStream(streamId, remote);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasReceivedReply(int streamId) {
        StreamState state = this.activeStreams.get(Integer.valueOf(streamId));
        return state != null && state.hasReceivedReply();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void receivedReply(int streamId) {
        StreamState state = this.activeStreams.get(Integer.valueOf(streamId));
        if (state != null) {
            state.receivedReply();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getSendWindowSize(int streamId) {
        if (streamId == 0) {
            return this.sendWindowSize.get();
        }
        StreamState state = this.activeStreams.get(Integer.valueOf(streamId));
        if (state != null) {
            return state.getSendWindowSize();
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int updateSendWindowSize(int streamId, int deltaWindowSize) {
        if (streamId == 0) {
            return this.sendWindowSize.addAndGet(deltaWindowSize);
        }
        StreamState state = this.activeStreams.get(Integer.valueOf(streamId));
        if (state != null) {
            return state.updateSendWindowSize(deltaWindowSize);
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int updateReceiveWindowSize(int streamId, int deltaWindowSize) {
        if (streamId == 0) {
            return this.receiveWindowSize.addAndGet(deltaWindowSize);
        }
        StreamState state = this.activeStreams.get(Integer.valueOf(streamId));
        if (state == null) {
            return -1;
        }
        if (deltaWindowSize > 0) {
            state.setReceiveWindowSizeLowerBound(0);
        }
        return state.updateReceiveWindowSize(deltaWindowSize);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getReceiveWindowSizeLowerBound(int streamId) {
        StreamState state;
        if (streamId == 0 || (state = this.activeStreams.get(Integer.valueOf(streamId))) == null) {
            return 0;
        }
        return state.getReceiveWindowSizeLowerBound();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateAllSendWindowSizes(int deltaWindowSize) {
        for (StreamState state : this.activeStreams.values()) {
            state.updateSendWindowSize(deltaWindowSize);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateAllReceiveWindowSizes(int deltaWindowSize) {
        for (StreamState state : this.activeStreams.values()) {
            state.updateReceiveWindowSize(deltaWindowSize);
            if (deltaWindowSize < 0) {
                state.setReceiveWindowSizeLowerBound(deltaWindowSize);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean putPendingWrite(int streamId, PendingWrite pendingWrite) {
        StreamState state = this.activeStreams.get(Integer.valueOf(streamId));
        return state != null && state.putPendingWrite(pendingWrite);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PendingWrite getPendingWrite(int streamId) {
        PendingWrite pendingWrite;
        if (streamId == 0) {
            for (Map.Entry<Integer, StreamState> e : activeStreams().entrySet()) {
                StreamState state = e.getValue();
                if (state.getSendWindowSize() > 0 && (pendingWrite = state.getPendingWrite()) != null) {
                    return pendingWrite;
                }
            }
            return null;
        }
        StreamState state2 = this.activeStreams.get(Integer.valueOf(streamId));
        if (state2 != null) {
            return state2.getPendingWrite();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PendingWrite removePendingWrite(int streamId) {
        StreamState state = this.activeStreams.get(Integer.valueOf(streamId));
        if (state != null) {
            return state.removePendingWrite();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class StreamState {
        private boolean localSideClosed;
        private final Queue<PendingWrite> pendingWriteQueue = new ConcurrentLinkedQueue();
        private final byte priority;
        private final AtomicInteger receiveWindowSize;
        private int receiveWindowSizeLowerBound;
        private boolean receivedReply;
        private boolean remoteSideClosed;
        private final AtomicInteger sendWindowSize;

        StreamState(byte priority, boolean remoteSideClosed, boolean localSideClosed, int sendWindowSize, int receiveWindowSize) {
            this.priority = priority;
            this.remoteSideClosed = remoteSideClosed;
            this.localSideClosed = localSideClosed;
            this.sendWindowSize = new AtomicInteger(sendWindowSize);
            this.receiveWindowSize = new AtomicInteger(receiveWindowSize);
        }

        byte getPriority() {
            return this.priority;
        }

        boolean isRemoteSideClosed() {
            return this.remoteSideClosed;
        }

        void closeRemoteSide() {
            this.remoteSideClosed = true;
        }

        boolean isLocalSideClosed() {
            return this.localSideClosed;
        }

        void closeLocalSide() {
            this.localSideClosed = true;
        }

        boolean hasReceivedReply() {
            return this.receivedReply;
        }

        void receivedReply() {
            this.receivedReply = true;
        }

        int getSendWindowSize() {
            return this.sendWindowSize.get();
        }

        int updateSendWindowSize(int deltaWindowSize) {
            return this.sendWindowSize.addAndGet(deltaWindowSize);
        }

        int updateReceiveWindowSize(int deltaWindowSize) {
            return this.receiveWindowSize.addAndGet(deltaWindowSize);
        }

        int getReceiveWindowSizeLowerBound() {
            return this.receiveWindowSizeLowerBound;
        }

        void setReceiveWindowSizeLowerBound(int receiveWindowSizeLowerBound) {
            this.receiveWindowSizeLowerBound = receiveWindowSizeLowerBound;
        }

        boolean putPendingWrite(PendingWrite msg) {
            return this.pendingWriteQueue.offer(msg);
        }

        PendingWrite getPendingWrite() {
            return this.pendingWriteQueue.peek();
        }

        PendingWrite removePendingWrite() {
            return this.pendingWriteQueue.poll();
        }

        void clearPendingWrites(Throwable cause) {
            while (true) {
                PendingWrite pendingWrite = this.pendingWriteQueue.poll();
                if (pendingWrite != null) {
                    pendingWrite.fail(cause);
                } else {
                    return;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class StreamComparator implements Comparator<Integer> {
        StreamComparator() {
        }

        @Override // java.util.Comparator
        public int compare(Integer id1, Integer id2) {
            StreamState state1 = (StreamState) SpdySession.this.activeStreams.get(id1);
            StreamState state2 = (StreamState) SpdySession.this.activeStreams.get(id2);
            int result = state1.getPriority() - state2.getPriority();
            if (result != 0) {
                return result;
            }
            return id1.intValue() - id2.intValue();
        }
    }

    /* loaded from: classes4.dex */
    public static final class PendingWrite {
        final ChannelPromise promise;
        final SpdyDataFrame spdyDataFrame;

        /* JADX INFO: Access modifiers changed from: package-private */
        public PendingWrite(SpdyDataFrame spdyDataFrame, ChannelPromise promise) {
            this.spdyDataFrame = spdyDataFrame;
            this.promise = promise;
        }

        void fail(Throwable cause) {
            this.spdyDataFrame.release();
            this.promise.setFailure(cause);
        }
    }
}
