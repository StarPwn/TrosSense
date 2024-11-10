package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http2.Http2Connection;
import io.netty.handler.codec.http2.Http2Exception;
import io.netty.handler.codec.http2.Http2Stream;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;
import io.netty.util.concurrent.PromiseNotifier;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/* loaded from: classes4.dex */
public class DefaultHttp2Connection implements Http2Connection {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) DefaultHttp2Connection.class);
    final ActiveStreams activeStreams;
    Promise<Void> closePromise;
    final ConnectionStream connectionStream;
    final List<Http2Connection.Listener> listeners;
    final DefaultEndpoint<Http2LocalFlowController> localEndpoint;
    final PropertyKeyRegistry propertyKeyRegistry;
    final DefaultEndpoint<Http2RemoteFlowController> remoteEndpoint;
    final IntObjectMap<Http2Stream> streamMap;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public interface Event {
        void process();
    }

    public DefaultHttp2Connection(boolean server) {
        this(server, 100);
    }

    public DefaultHttp2Connection(boolean server, int maxReservedStreams) {
        this.streamMap = new IntObjectHashMap();
        this.propertyKeyRegistry = new PropertyKeyRegistry();
        this.connectionStream = new ConnectionStream();
        this.listeners = new ArrayList(4);
        this.activeStreams = new ActiveStreams(this.listeners);
        this.localEndpoint = new DefaultEndpoint<>(server, server ? Integer.MAX_VALUE : maxReservedStreams);
        this.remoteEndpoint = new DefaultEndpoint<>(!server, maxReservedStreams);
        this.streamMap.put(this.connectionStream.id(), (int) this.connectionStream);
    }

    final boolean isClosed() {
        return this.closePromise != null;
    }

    @Override // io.netty.handler.codec.http2.Http2Connection
    public Future<Void> close(Promise<Void> promise) {
        ObjectUtil.checkNotNull(promise, "promise");
        if (this.closePromise != null) {
            if (this.closePromise != promise) {
                if ((promise instanceof ChannelPromise) && ((ChannelFuture) this.closePromise).isVoid()) {
                    this.closePromise = promise;
                } else {
                    PromiseNotifier.cascade(this.closePromise, promise);
                }
            }
        } else {
            this.closePromise = promise;
        }
        if (isStreamMapEmpty()) {
            promise.trySuccess(null);
            return promise;
        }
        Iterator<IntObjectMap.PrimitiveEntry<Http2Stream>> itr = this.streamMap.entries().iterator();
        if (this.activeStreams.allowModifications()) {
            this.activeStreams.incrementPendingIterations();
            while (itr.hasNext()) {
                try {
                    DefaultStream stream = (DefaultStream) itr.next().value();
                    if (stream.id() != 0) {
                        stream.close(itr);
                    }
                } finally {
                    this.activeStreams.decrementPendingIterations();
                }
            }
        } else {
            while (itr.hasNext()) {
                Http2Stream stream2 = itr.next().value();
                if (stream2.id() != 0) {
                    stream2.close();
                }
            }
        }
        return this.closePromise;
    }

    @Override // io.netty.handler.codec.http2.Http2Connection
    public void addListener(Http2Connection.Listener listener) {
        this.listeners.add(listener);
    }

    @Override // io.netty.handler.codec.http2.Http2Connection
    public void removeListener(Http2Connection.Listener listener) {
        this.listeners.remove(listener);
    }

    @Override // io.netty.handler.codec.http2.Http2Connection
    public boolean isServer() {
        return this.localEndpoint.isServer();
    }

    @Override // io.netty.handler.codec.http2.Http2Connection
    public Http2Stream connectionStream() {
        return this.connectionStream;
    }

    @Override // io.netty.handler.codec.http2.Http2Connection
    public Http2Stream stream(int streamId) {
        return this.streamMap.get(streamId);
    }

    @Override // io.netty.handler.codec.http2.Http2Connection
    public boolean streamMayHaveExisted(int streamId) {
        return this.remoteEndpoint.mayHaveCreatedStream(streamId) || this.localEndpoint.mayHaveCreatedStream(streamId);
    }

    @Override // io.netty.handler.codec.http2.Http2Connection
    public int numActiveStreams() {
        return this.activeStreams.size();
    }

    @Override // io.netty.handler.codec.http2.Http2Connection
    public Http2Stream forEachActiveStream(Http2StreamVisitor visitor) throws Http2Exception {
        return this.activeStreams.forEachActiveStream(visitor);
    }

    @Override // io.netty.handler.codec.http2.Http2Connection
    public Http2Connection.Endpoint<Http2LocalFlowController> local() {
        return this.localEndpoint;
    }

    @Override // io.netty.handler.codec.http2.Http2Connection
    public Http2Connection.Endpoint<Http2RemoteFlowController> remote() {
        return this.remoteEndpoint;
    }

    @Override // io.netty.handler.codec.http2.Http2Connection
    public boolean goAwayReceived() {
        return ((DefaultEndpoint) this.localEndpoint).lastStreamKnownByPeer >= 0;
    }

    @Override // io.netty.handler.codec.http2.Http2Connection
    public void goAwayReceived(int lastKnownStream, long errorCode, ByteBuf debugData) throws Http2Exception {
        if (this.localEndpoint.lastStreamKnownByPeer() >= 0 && this.localEndpoint.lastStreamKnownByPeer() < lastKnownStream) {
            throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "lastStreamId MUST NOT increase. Current value: %d new value: %d", Integer.valueOf(this.localEndpoint.lastStreamKnownByPeer()), Integer.valueOf(lastKnownStream));
        }
        this.localEndpoint.lastStreamKnownByPeer(lastKnownStream);
        for (int i = 0; i < this.listeners.size(); i++) {
            try {
                this.listeners.get(i).onGoAwayReceived(lastKnownStream, errorCode, debugData);
            } catch (Throwable cause) {
                logger.error("Caught Throwable from listener onGoAwayReceived.", cause);
            }
        }
        closeStreamsGreaterThanLastKnownStreamId(lastKnownStream, this.localEndpoint);
    }

    @Override // io.netty.handler.codec.http2.Http2Connection
    public boolean goAwaySent() {
        return ((DefaultEndpoint) this.remoteEndpoint).lastStreamKnownByPeer >= 0;
    }

    @Override // io.netty.handler.codec.http2.Http2Connection
    public boolean goAwaySent(int lastKnownStream, long errorCode, ByteBuf debugData) throws Http2Exception {
        if (this.remoteEndpoint.lastStreamKnownByPeer() >= 0) {
            if (lastKnownStream == this.remoteEndpoint.lastStreamKnownByPeer()) {
                return false;
            }
            if (lastKnownStream > this.remoteEndpoint.lastStreamKnownByPeer()) {
                throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Last stream identifier must not increase between sending multiple GOAWAY frames (was '%d', is '%d').", Integer.valueOf(this.remoteEndpoint.lastStreamKnownByPeer()), Integer.valueOf(lastKnownStream));
            }
        }
        this.remoteEndpoint.lastStreamKnownByPeer(lastKnownStream);
        for (int i = 0; i < this.listeners.size(); i++) {
            try {
                this.listeners.get(i).onGoAwaySent(lastKnownStream, errorCode, debugData);
            } catch (Throwable cause) {
                logger.error("Caught Throwable from listener onGoAwaySent.", cause);
            }
        }
        closeStreamsGreaterThanLastKnownStreamId(lastKnownStream, this.remoteEndpoint);
        return true;
    }

    private void closeStreamsGreaterThanLastKnownStreamId(final int lastKnownStream, final DefaultEndpoint<?> endpoint) throws Http2Exception {
        forEachActiveStream(new Http2StreamVisitor() { // from class: io.netty.handler.codec.http2.DefaultHttp2Connection.1
            @Override // io.netty.handler.codec.http2.Http2StreamVisitor
            public boolean visit(Http2Stream stream) {
                if (stream.id() > lastKnownStream && endpoint.isValidStreamId(stream.id())) {
                    stream.close();
                    return true;
                }
                return true;
            }
        });
    }

    private boolean isStreamMapEmpty() {
        return this.streamMap.size() == 1;
    }

    void removeStream(DefaultStream stream, Iterator<?> itr) {
        boolean removed;
        if (itr == null) {
            removed = this.streamMap.remove(stream.id()) != null;
        } else {
            itr.remove();
            removed = true;
        }
        if (removed) {
            for (int i = 0; i < this.listeners.size(); i++) {
                try {
                    this.listeners.get(i).onStreamRemoved(stream);
                } catch (Throwable cause) {
                    logger.error("Caught Throwable from listener onStreamRemoved.", cause);
                }
            }
            if (this.closePromise != null && isStreamMapEmpty()) {
                this.closePromise.trySuccess(null);
            }
        }
    }

    static Http2Stream.State activeState(int streamId, Http2Stream.State initialState, boolean isLocal, boolean halfClosed) throws Http2Exception {
        switch (initialState) {
            case IDLE:
                return halfClosed ? isLocal ? Http2Stream.State.HALF_CLOSED_LOCAL : Http2Stream.State.HALF_CLOSED_REMOTE : Http2Stream.State.OPEN;
            case RESERVED_LOCAL:
                return Http2Stream.State.HALF_CLOSED_REMOTE;
            case RESERVED_REMOTE:
                return Http2Stream.State.HALF_CLOSED_LOCAL;
            default:
                throw Http2Exception.streamError(streamId, Http2Error.PROTOCOL_ERROR, "Attempting to open a stream in an invalid state: " + initialState, new Object[0]);
        }
    }

    void notifyHalfClosed(Http2Stream stream) {
        for (int i = 0; i < this.listeners.size(); i++) {
            try {
                this.listeners.get(i).onStreamHalfClosed(stream);
            } catch (Throwable cause) {
                logger.error("Caught Throwable from listener onStreamHalfClosed.", cause);
            }
        }
    }

    void notifyClosed(Http2Stream stream) {
        for (int i = 0; i < this.listeners.size(); i++) {
            try {
                this.listeners.get(i).onStreamClosed(stream);
            } catch (Throwable cause) {
                logger.error("Caught Throwable from listener onStreamClosed.", cause);
            }
        }
    }

    @Override // io.netty.handler.codec.http2.Http2Connection
    public Http2Connection.PropertyKey newKey() {
        return this.propertyKeyRegistry.newKey();
    }

    final DefaultPropertyKey verifyKey(Http2Connection.PropertyKey key) {
        return ((DefaultPropertyKey) ObjectUtil.checkNotNull((DefaultPropertyKey) key, "key")).verifyConnection(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class DefaultStream implements Http2Stream {
        private static final byte META_STATE_RECV_HEADERS = 16;
        private static final byte META_STATE_RECV_TRAILERS = 32;
        private static final byte META_STATE_SENT_HEADERS = 2;
        private static final byte META_STATE_SENT_PUSHPROMISE = 8;
        private static final byte META_STATE_SENT_RST = 1;
        private static final byte META_STATE_SENT_TRAILERS = 4;
        private final int id;
        private final long identity;
        private byte metaState;
        private final PropertyMap properties = new PropertyMap();
        private Http2Stream.State state;

        DefaultStream(long identity, int id, Http2Stream.State state) {
            this.identity = identity;
            this.id = id;
            this.state = state;
        }

        @Override // io.netty.handler.codec.http2.Http2Stream
        public final int id() {
            return this.id;
        }

        @Override // io.netty.handler.codec.http2.Http2Stream
        public final Http2Stream.State state() {
            return this.state;
        }

        @Override // io.netty.handler.codec.http2.Http2Stream
        public boolean isResetSent() {
            return (this.metaState & 1) != 0;
        }

        @Override // io.netty.handler.codec.http2.Http2Stream
        public Http2Stream resetSent() {
            this.metaState = (byte) (this.metaState | 1);
            return this;
        }

        @Override // io.netty.handler.codec.http2.Http2Stream
        public Http2Stream headersSent(boolean isInformational) {
            if (!isInformational) {
                this.metaState = (byte) (this.metaState | (isHeadersSent() ? (byte) 4 : (byte) 2));
            }
            return this;
        }

        @Override // io.netty.handler.codec.http2.Http2Stream
        public boolean isHeadersSent() {
            return (this.metaState & 2) != 0;
        }

        @Override // io.netty.handler.codec.http2.Http2Stream
        public boolean isTrailersSent() {
            return (this.metaState & 4) != 0;
        }

        @Override // io.netty.handler.codec.http2.Http2Stream
        public Http2Stream headersReceived(boolean isInformational) {
            if (!isInformational) {
                this.metaState = (byte) (this.metaState | (isHeadersReceived() ? (byte) 32 : (byte) 16));
            }
            return this;
        }

        @Override // io.netty.handler.codec.http2.Http2Stream
        public boolean isHeadersReceived() {
            return (this.metaState & 16) != 0;
        }

        @Override // io.netty.handler.codec.http2.Http2Stream
        public boolean isTrailersReceived() {
            return (this.metaState & 32) != 0;
        }

        @Override // io.netty.handler.codec.http2.Http2Stream
        public Http2Stream pushPromiseSent() {
            this.metaState = (byte) (this.metaState | 8);
            return this;
        }

        @Override // io.netty.handler.codec.http2.Http2Stream
        public boolean isPushPromiseSent() {
            return (this.metaState & 8) != 0;
        }

        @Override // io.netty.handler.codec.http2.Http2Stream
        public final <V> V setProperty(Http2Connection.PropertyKey propertyKey, V v) {
            return (V) this.properties.add(DefaultHttp2Connection.this.verifyKey(propertyKey), v);
        }

        @Override // io.netty.handler.codec.http2.Http2Stream
        public final <V> V getProperty(Http2Connection.PropertyKey propertyKey) {
            return (V) this.properties.get(DefaultHttp2Connection.this.verifyKey(propertyKey));
        }

        @Override // io.netty.handler.codec.http2.Http2Stream
        public final <V> V removeProperty(Http2Connection.PropertyKey propertyKey) {
            return (V) this.properties.remove(DefaultHttp2Connection.this.verifyKey(propertyKey));
        }

        @Override // io.netty.handler.codec.http2.Http2Stream
        public Http2Stream open(boolean halfClosed) throws Http2Exception {
            this.state = DefaultHttp2Connection.activeState(this.id, this.state, isLocal(), halfClosed);
            DefaultEndpoint<? extends Http2FlowController> endpoint = createdBy();
            if (!endpoint.canOpenStream()) {
                throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Maximum active streams violated for this endpoint: " + endpoint.maxActiveStreams(), new Object[0]);
            }
            activate();
            return this;
        }

        void activate() {
            if (this.state == Http2Stream.State.HALF_CLOSED_LOCAL) {
                headersSent(false);
            } else if (this.state == Http2Stream.State.HALF_CLOSED_REMOTE) {
                headersReceived(false);
            }
            DefaultHttp2Connection.this.activeStreams.activate(this);
        }

        Http2Stream close(Iterator<?> itr) {
            if (this.state == Http2Stream.State.CLOSED) {
                return this;
            }
            this.state = Http2Stream.State.CLOSED;
            DefaultEndpoint<? extends Http2FlowController> createdBy = createdBy();
            createdBy.numStreams--;
            DefaultHttp2Connection.this.activeStreams.deactivate(this, itr);
            return this;
        }

        @Override // io.netty.handler.codec.http2.Http2Stream
        public Http2Stream close() {
            return close(null);
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Code restructure failed: missing block: B:5:0x001c, code lost:            return r2;     */
        @Override // io.netty.handler.codec.http2.Http2Stream
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public io.netty.handler.codec.http2.Http2Stream closeLocalSide() {
            /*
                r2 = this;
                int[] r0 = io.netty.handler.codec.http2.DefaultHttp2Connection.AnonymousClass2.$SwitchMap$io$netty$handler$codec$http2$Http2Stream$State
                io.netty.handler.codec.http2.Http2Stream$State r1 = r2.state
                int r1 = r1.ordinal()
                r0 = r0[r1]
                switch(r0) {
                    case 4: goto L12;
                    case 5: goto L11;
                    default: goto Ld;
                }
            Ld:
                r2.close()
                goto L1c
            L11:
                goto L1c
            L12:
                io.netty.handler.codec.http2.Http2Stream$State r0 = io.netty.handler.codec.http2.Http2Stream.State.HALF_CLOSED_LOCAL
                r2.state = r0
                io.netty.handler.codec.http2.DefaultHttp2Connection r0 = io.netty.handler.codec.http2.DefaultHttp2Connection.this
                r0.notifyHalfClosed(r2)
            L1c:
                return r2
            */
            throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.codec.http2.DefaultHttp2Connection.DefaultStream.closeLocalSide():io.netty.handler.codec.http2.Http2Stream");
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Code restructure failed: missing block: B:5:0x001c, code lost:            return r2;     */
        @Override // io.netty.handler.codec.http2.Http2Stream
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public io.netty.handler.codec.http2.Http2Stream closeRemoteSide() {
            /*
                r2 = this;
                int[] r0 = io.netty.handler.codec.http2.DefaultHttp2Connection.AnonymousClass2.$SwitchMap$io$netty$handler$codec$http2$Http2Stream$State
                io.netty.handler.codec.http2.Http2Stream$State r1 = r2.state
                int r1 = r1.ordinal()
                r0 = r0[r1]
                switch(r0) {
                    case 4: goto L12;
                    case 5: goto Ld;
                    case 6: goto L11;
                    default: goto Ld;
                }
            Ld:
                r2.close()
                goto L1c
            L11:
                goto L1c
            L12:
                io.netty.handler.codec.http2.Http2Stream$State r0 = io.netty.handler.codec.http2.Http2Stream.State.HALF_CLOSED_REMOTE
                r2.state = r0
                io.netty.handler.codec.http2.DefaultHttp2Connection r0 = io.netty.handler.codec.http2.DefaultHttp2Connection.this
                r0.notifyHalfClosed(r2)
            L1c:
                return r2
            */
            throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.codec.http2.DefaultHttp2Connection.DefaultStream.closeRemoteSide():io.netty.handler.codec.http2.Http2Stream");
        }

        DefaultEndpoint<? extends Http2FlowController> createdBy() {
            return DefaultHttp2Connection.this.localEndpoint.isValidStreamId(this.id) ? DefaultHttp2Connection.this.localEndpoint : DefaultHttp2Connection.this.remoteEndpoint;
        }

        final boolean isLocal() {
            return DefaultHttp2Connection.this.localEndpoint.isValidStreamId(this.id);
        }

        /* loaded from: classes4.dex */
        private class PropertyMap {
            Object[] values;

            private PropertyMap() {
                this.values = EmptyArrays.EMPTY_OBJECTS;
            }

            <V> V add(DefaultPropertyKey defaultPropertyKey, V v) {
                resizeIfNecessary(defaultPropertyKey.index);
                V v2 = (V) this.values[defaultPropertyKey.index];
                this.values[defaultPropertyKey.index] = v;
                return v2;
            }

            <V> V get(DefaultPropertyKey defaultPropertyKey) {
                if (defaultPropertyKey.index >= this.values.length) {
                    return null;
                }
                return (V) this.values[defaultPropertyKey.index];
            }

            <V> V remove(DefaultPropertyKey defaultPropertyKey) {
                if (defaultPropertyKey.index >= this.values.length) {
                    return null;
                }
                V v = (V) this.values[defaultPropertyKey.index];
                this.values[defaultPropertyKey.index] = null;
                return v;
            }

            void resizeIfNecessary(int index) {
                if (index >= this.values.length) {
                    this.values = Arrays.copyOf(this.values, DefaultHttp2Connection.this.propertyKeyRegistry.size());
                }
            }
        }

        public boolean equals(Object obj) {
            return super.equals(obj);
        }

        public int hashCode() {
            long value = this.identity;
            if (value == 0) {
                return System.identityHashCode(this);
            }
            return (int) ((value >>> 32) ^ value);
        }
    }

    /* loaded from: classes4.dex */
    private final class ConnectionStream extends DefaultStream {
        ConnectionStream() {
            super(0L, 0, Http2Stream.State.IDLE);
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2Connection.DefaultStream, io.netty.handler.codec.http2.Http2Stream
        public boolean isResetSent() {
            return false;
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2Connection.DefaultStream
        DefaultEndpoint<? extends Http2FlowController> createdBy() {
            return null;
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2Connection.DefaultStream, io.netty.handler.codec.http2.Http2Stream
        public Http2Stream resetSent() {
            throw new UnsupportedOperationException();
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2Connection.DefaultStream, io.netty.handler.codec.http2.Http2Stream
        public Http2Stream open(boolean halfClosed) {
            throw new UnsupportedOperationException();
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2Connection.DefaultStream, io.netty.handler.codec.http2.Http2Stream
        public Http2Stream close() {
            throw new UnsupportedOperationException();
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2Connection.DefaultStream, io.netty.handler.codec.http2.Http2Stream
        public Http2Stream closeLocalSide() {
            throw new UnsupportedOperationException();
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2Connection.DefaultStream, io.netty.handler.codec.http2.Http2Stream
        public Http2Stream closeRemoteSide() {
            throw new UnsupportedOperationException();
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2Connection.DefaultStream, io.netty.handler.codec.http2.Http2Stream
        public Http2Stream headersSent(boolean isInformational) {
            throw new UnsupportedOperationException();
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2Connection.DefaultStream, io.netty.handler.codec.http2.Http2Stream
        public boolean isHeadersSent() {
            throw new UnsupportedOperationException();
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2Connection.DefaultStream, io.netty.handler.codec.http2.Http2Stream
        public Http2Stream pushPromiseSent() {
            throw new UnsupportedOperationException();
        }

        @Override // io.netty.handler.codec.http2.DefaultHttp2Connection.DefaultStream, io.netty.handler.codec.http2.Http2Stream
        public boolean isPushPromiseSent() {
            throw new UnsupportedOperationException();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class DefaultEndpoint<F extends Http2FlowController> implements Http2Connection.Endpoint<F> {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private F flowController;
        private int maxActiveStreams;
        private final int maxReservedStreams;
        private int maxStreams;
        private int nextReservationStreamId;
        private int nextStreamIdToCreate;
        int numActiveStreams;
        int numStreams;
        private boolean pushToAllowed;
        private final boolean server;
        private int lastStreamKnownByPeer = -1;
        private long lastCreatedStreamIdentity = 0;

        DefaultEndpoint(boolean server, int maxReservedStreams) {
            this.server = server;
            if (server) {
                this.nextStreamIdToCreate = 2;
                this.nextReservationStreamId = 0;
            } else {
                this.nextStreamIdToCreate = 1;
                this.nextReservationStreamId = 1;
            }
            this.pushToAllowed = !server;
            this.maxActiveStreams = Integer.MAX_VALUE;
            this.maxReservedStreams = ObjectUtil.checkPositiveOrZero(maxReservedStreams, "maxReservedStreams");
            updateMaxStreams();
        }

        @Override // io.netty.handler.codec.http2.Http2Connection.Endpoint
        public int incrementAndGetNextStreamId() {
            if (this.nextReservationStreamId < 0) {
                return this.nextReservationStreamId;
            }
            int i = this.nextReservationStreamId + 2;
            this.nextReservationStreamId = i;
            return i;
        }

        private void incrementExpectedStreamId(int streamId) {
            if (streamId > this.nextReservationStreamId && this.nextReservationStreamId >= 0) {
                this.nextReservationStreamId = streamId;
            }
            this.nextStreamIdToCreate = streamId + 2;
            this.numStreams++;
        }

        @Override // io.netty.handler.codec.http2.Http2Connection.Endpoint
        public boolean isValidStreamId(int streamId) {
            if (streamId > 0) {
                return this.server == ((streamId & 1) == 0);
            }
            return false;
        }

        @Override // io.netty.handler.codec.http2.Http2Connection.Endpoint
        public boolean mayHaveCreatedStream(int streamId) {
            return isValidStreamId(streamId) && streamId <= lastStreamCreated();
        }

        @Override // io.netty.handler.codec.http2.Http2Connection.Endpoint
        public boolean canOpenStream() {
            return this.numActiveStreams < this.maxActiveStreams;
        }

        @Override // io.netty.handler.codec.http2.Http2Connection.Endpoint
        public DefaultStream createStream(int streamId, boolean halfClosed) throws Http2Exception {
            Http2Stream.State state = DefaultHttp2Connection.activeState(streamId, Http2Stream.State.IDLE, isLocal(), halfClosed);
            checkNewStreamAllowed(streamId, state);
            this.lastCreatedStreamIdentity++;
            DefaultStream stream = new DefaultStream(this.lastCreatedStreamIdentity, streamId, state);
            incrementExpectedStreamId(streamId);
            addStream(stream);
            stream.activate();
            return stream;
        }

        @Override // io.netty.handler.codec.http2.Http2Connection.Endpoint
        public boolean created(Http2Stream stream) {
            return (stream instanceof DefaultStream) && ((DefaultStream) stream).createdBy() == this;
        }

        @Override // io.netty.handler.codec.http2.Http2Connection.Endpoint
        public boolean isServer() {
            return this.server;
        }

        @Override // io.netty.handler.codec.http2.Http2Connection.Endpoint
        public DefaultStream reservePushStream(int streamId, Http2Stream parent) throws Http2Exception {
            if (parent == null) {
                throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Parent stream missing", new Object[0]);
            }
            if (!isLocal() ? !parent.state().remoteSideOpen() : !parent.state().localSideOpen()) {
                throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Stream %d is not open for sending push promise", Integer.valueOf(parent.id()));
            }
            if (!opposite().allowPushTo()) {
                throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Server push not allowed to opposite endpoint", new Object[0]);
            }
            Http2Stream.State state = isLocal() ? Http2Stream.State.RESERVED_LOCAL : Http2Stream.State.RESERVED_REMOTE;
            checkNewStreamAllowed(streamId, state);
            this.lastCreatedStreamIdentity++;
            DefaultStream stream = new DefaultStream(this.lastCreatedStreamIdentity, streamId, state);
            incrementExpectedStreamId(streamId);
            addStream(stream);
            return stream;
        }

        private void addStream(DefaultStream stream) {
            DefaultHttp2Connection.this.streamMap.put(stream.id(), (int) stream);
            for (int i = 0; i < DefaultHttp2Connection.this.listeners.size(); i++) {
                try {
                    DefaultHttp2Connection.this.listeners.get(i).onStreamAdded(stream);
                } catch (Throwable cause) {
                    DefaultHttp2Connection.logger.error("Caught Throwable from listener onStreamAdded.", cause);
                }
            }
        }

        @Override // io.netty.handler.codec.http2.Http2Connection.Endpoint
        public void allowPushTo(boolean allow) {
            if (allow && this.server) {
                throw new IllegalArgumentException("Servers do not allow push");
            }
            this.pushToAllowed = allow;
        }

        @Override // io.netty.handler.codec.http2.Http2Connection.Endpoint
        public boolean allowPushTo() {
            return this.pushToAllowed;
        }

        @Override // io.netty.handler.codec.http2.Http2Connection.Endpoint
        public int numActiveStreams() {
            return this.numActiveStreams;
        }

        @Override // io.netty.handler.codec.http2.Http2Connection.Endpoint
        public int maxActiveStreams() {
            return this.maxActiveStreams;
        }

        @Override // io.netty.handler.codec.http2.Http2Connection.Endpoint
        public void maxActiveStreams(int maxActiveStreams) {
            this.maxActiveStreams = maxActiveStreams;
            updateMaxStreams();
        }

        @Override // io.netty.handler.codec.http2.Http2Connection.Endpoint
        public int lastStreamCreated() {
            return Math.max(0, this.nextStreamIdToCreate - 2);
        }

        @Override // io.netty.handler.codec.http2.Http2Connection.Endpoint
        public int lastStreamKnownByPeer() {
            return this.lastStreamKnownByPeer;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void lastStreamKnownByPeer(int lastKnownStream) {
            this.lastStreamKnownByPeer = lastKnownStream;
        }

        @Override // io.netty.handler.codec.http2.Http2Connection.Endpoint
        public F flowController() {
            return this.flowController;
        }

        @Override // io.netty.handler.codec.http2.Http2Connection.Endpoint
        public void flowController(F flowController) {
            this.flowController = (F) ObjectUtil.checkNotNull(flowController, "flowController");
        }

        @Override // io.netty.handler.codec.http2.Http2Connection.Endpoint
        public Http2Connection.Endpoint<? extends Http2FlowController> opposite() {
            return isLocal() ? DefaultHttp2Connection.this.remoteEndpoint : DefaultHttp2Connection.this.localEndpoint;
        }

        private void updateMaxStreams() {
            this.maxStreams = (int) Math.min(2147483647L, this.maxActiveStreams + this.maxReservedStreams);
        }

        private void checkNewStreamAllowed(int streamId, Http2Stream.State state) throws Http2Exception {
            if (state == Http2Stream.State.IDLE) {
                throw new AssertionError();
            }
            if (this.lastStreamKnownByPeer >= 0 && streamId > this.lastStreamKnownByPeer) {
                throw Http2Exception.streamError(streamId, Http2Error.REFUSED_STREAM, "Cannot create stream %d greater than Last-Stream-ID %d from GOAWAY.", Integer.valueOf(streamId), Integer.valueOf(this.lastStreamKnownByPeer));
            }
            boolean z = true;
            if (!isValidStreamId(streamId)) {
                if (streamId < 0) {
                    throw new Http2NoMoreStreamIdsException();
                }
                Http2Error http2Error = Http2Error.PROTOCOL_ERROR;
                Object[] objArr = new Object[2];
                objArr[0] = Integer.valueOf(streamId);
                objArr[1] = this.server ? "server" : "client";
                throw Http2Exception.connectionError(http2Error, "Request stream %d is not correct for %s connection", objArr);
            }
            if (streamId < this.nextStreamIdToCreate) {
                throw Http2Exception.closedStreamError(Http2Error.PROTOCOL_ERROR, "Request stream %d is behind the next expected stream %d", Integer.valueOf(streamId), Integer.valueOf(this.nextStreamIdToCreate));
            }
            if (this.nextStreamIdToCreate <= 0) {
                throw new Http2Exception(Http2Error.REFUSED_STREAM, "Stream IDs are exhausted for this endpoint.", Http2Exception.ShutdownHint.GRACEFUL_SHUTDOWN);
            }
            if (state != Http2Stream.State.RESERVED_LOCAL && state != Http2Stream.State.RESERVED_REMOTE) {
                z = false;
            }
            boolean isReserved = z;
            if ((!isReserved && !canOpenStream()) || (isReserved && this.numStreams >= this.maxStreams)) {
                throw Http2Exception.streamError(streamId, Http2Error.REFUSED_STREAM, "Maximum active streams violated for this endpoint: " + (isReserved ? this.maxStreams : this.maxActiveStreams), new Object[0]);
            }
            if (DefaultHttp2Connection.this.isClosed()) {
                throw Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, "Attempted to create stream id %d after connection was closed", Integer.valueOf(streamId));
            }
        }

        private boolean isLocal() {
            return this == DefaultHttp2Connection.this.localEndpoint;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class ActiveStreams {
        private final List<Http2Connection.Listener> listeners;
        private int pendingIterations;
        private final Queue<Event> pendingEvents = new ArrayDeque(4);
        private final Set<Http2Stream> streams = new LinkedHashSet();

        ActiveStreams(List<Http2Connection.Listener> listeners) {
            this.listeners = listeners;
        }

        public int size() {
            return this.streams.size();
        }

        public void activate(final DefaultStream stream) {
            if (allowModifications()) {
                addToActiveStreams(stream);
            } else {
                this.pendingEvents.add(new Event() { // from class: io.netty.handler.codec.http2.DefaultHttp2Connection.ActiveStreams.1
                    @Override // io.netty.handler.codec.http2.DefaultHttp2Connection.Event
                    public void process() {
                        ActiveStreams.this.addToActiveStreams(stream);
                    }
                });
            }
        }

        public void deactivate(final DefaultStream stream, final Iterator<?> itr) {
            if (allowModifications() || itr != null) {
                removeFromActiveStreams(stream, itr);
            } else {
                this.pendingEvents.add(new Event() { // from class: io.netty.handler.codec.http2.DefaultHttp2Connection.ActiveStreams.2
                    @Override // io.netty.handler.codec.http2.DefaultHttp2Connection.Event
                    public void process() {
                        ActiveStreams.this.removeFromActiveStreams(stream, itr);
                    }
                });
            }
        }

        public Http2Stream forEachActiveStream(Http2StreamVisitor visitor) throws Http2Exception {
            incrementPendingIterations();
            try {
                for (Http2Stream stream : this.streams) {
                    if (!visitor.visit(stream)) {
                        return stream;
                    }
                }
                decrementPendingIterations();
                return null;
            } finally {
                decrementPendingIterations();
            }
        }

        void addToActiveStreams(DefaultStream stream) {
            if (this.streams.add(stream)) {
                stream.createdBy().numActiveStreams++;
                for (int i = 0; i < this.listeners.size(); i++) {
                    try {
                        this.listeners.get(i).onStreamActive(stream);
                    } catch (Throwable cause) {
                        DefaultHttp2Connection.logger.error("Caught Throwable from listener onStreamActive.", cause);
                    }
                }
            }
        }

        void removeFromActiveStreams(DefaultStream stream, Iterator<?> itr) {
            if (this.streams.remove(stream)) {
                DefaultEndpoint<? extends Http2FlowController> createdBy = stream.createdBy();
                createdBy.numActiveStreams--;
                DefaultHttp2Connection.this.notifyClosed(stream);
            }
            DefaultHttp2Connection.this.removeStream(stream, itr);
        }

        boolean allowModifications() {
            return this.pendingIterations == 0;
        }

        void incrementPendingIterations() {
            this.pendingIterations++;
        }

        void decrementPendingIterations() {
            this.pendingIterations--;
            if (!allowModifications()) {
                return;
            }
            while (true) {
                Event event = this.pendingEvents.poll();
                if (event != null) {
                    try {
                        event.process();
                    } catch (Throwable cause) {
                        DefaultHttp2Connection.logger.error("Caught Throwable while processing pending ActiveStreams$Event.", cause);
                    }
                } else {
                    return;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public final class DefaultPropertyKey implements Http2Connection.PropertyKey {
        final int index;

        DefaultPropertyKey(int index) {
            this.index = index;
        }

        DefaultPropertyKey verifyConnection(Http2Connection connection) {
            if (connection != DefaultHttp2Connection.this) {
                throw new IllegalArgumentException("Using a key that was not created by this connection");
            }
            return this;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class PropertyKeyRegistry {
        final List<DefaultPropertyKey> keys;

        private PropertyKeyRegistry() {
            this.keys = new ArrayList(4);
        }

        DefaultPropertyKey newKey() {
            DefaultPropertyKey key = new DefaultPropertyKey(this.keys.size());
            this.keys.add(key);
            return key;
        }

        int size() {
            return this.keys.size();
        }
    }
}
