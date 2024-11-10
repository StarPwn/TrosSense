package io.netty.handler.ssl;

import io.netty.internal.tcnative.SSLSession;
import io.netty.internal.tcnative.SSLSessionCache;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.ResourceLeakDetectorFactory;
import io.netty.util.ResourceLeakTracker;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.SystemPropertyUtil;
import java.security.Principal;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javax.security.cert.X509Certificate;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public class OpenSslSessionCache implements SSLSessionCache {
    private static final int DEFAULT_CACHE_SIZE;
    private static final OpenSslSession[] EMPTY_SESSIONS = new OpenSslSession[0];
    private final OpenSslEngineMap engineMap;
    private int sessionCounter;
    private final Map<OpenSslSessionId, NativeSslSession> sessions = new LinkedHashMap<OpenSslSessionId, NativeSslSession>() { // from class: io.netty.handler.ssl.OpenSslSessionCache.1
        private static final long serialVersionUID = -7773696788135734448L;

        @Override // java.util.LinkedHashMap
        protected boolean removeEldestEntry(Map.Entry<OpenSslSessionId, NativeSslSession> eldest) {
            int maxSize = OpenSslSessionCache.this.maximumCacheSize.get();
            if (maxSize >= 0 && size() > maxSize) {
                OpenSslSessionCache.this.removeSessionWithId(eldest.getKey());
                return false;
            }
            return false;
        }
    };
    private final AtomicInteger maximumCacheSize = new AtomicInteger(DEFAULT_CACHE_SIZE);
    private final AtomicInteger sessionTimeout = new AtomicInteger(300);

    static {
        int cacheSize = SystemPropertyUtil.getInt("javax.net.ssl.sessionCacheSize", 20480);
        if (cacheSize >= 0) {
            DEFAULT_CACHE_SIZE = cacheSize;
        } else {
            DEFAULT_CACHE_SIZE = 20480;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public OpenSslSessionCache(OpenSslEngineMap engineMap) {
        this.engineMap = engineMap;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void setSessionTimeout(int seconds) {
        int oldTimeout = this.sessionTimeout.getAndSet(seconds);
        if (oldTimeout > seconds) {
            clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int getSessionTimeout() {
        return this.sessionTimeout.get();
    }

    protected boolean sessionCreated(NativeSslSession session) {
        return true;
    }

    protected void sessionRemoved(NativeSslSession session) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void setSessionCacheSize(int size) {
        long oldSize = this.maximumCacheSize.getAndSet(size);
        if (oldSize > size || size == 0) {
            clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int getSessionCacheSize() {
        return this.maximumCacheSize.get();
    }

    private void expungeInvalidSessions() {
        if (this.sessions.isEmpty()) {
            return;
        }
        long now = System.currentTimeMillis();
        Iterator<Map.Entry<OpenSslSessionId, NativeSslSession>> iterator = this.sessions.entrySet().iterator();
        while (iterator.hasNext()) {
            NativeSslSession session = iterator.next().getValue();
            if (!session.isValid(now)) {
                iterator.remove();
                notifyRemovalAndFree(session);
            } else {
                return;
            }
        }
    }

    public boolean sessionCreated(long ssl, long sslSession) {
        ReferenceCountedOpenSslEngine engine = this.engineMap.get(ssl);
        if (engine == null) {
            return false;
        }
        OpenSslSession openSslSession = (OpenSslSession) engine.getSession();
        NativeSslSession session = new NativeSslSession(sslSession, engine.getPeerHost(), engine.getPeerPort(), 1000 * getSessionTimeout(), openSslSession.keyValueStorage());
        openSslSession.setSessionDetails(session.creationTime, session.lastAccessedTime, session.sessionId(), session.keyValueStorage);
        synchronized (this) {
            int i = this.sessionCounter + 1;
            this.sessionCounter = i;
            if (i == 255) {
                this.sessionCounter = 0;
                expungeInvalidSessions();
            }
            if (!sessionCreated(session)) {
                session.close();
                return false;
            }
            NativeSslSession old = this.sessions.put(session.sessionId(), session);
            if (old != null) {
                notifyRemovalAndFree(old);
            }
            return true;
        }
    }

    public final long getSession(long ssl, byte[] sessionId) {
        OpenSslSessionId id = new OpenSslSessionId(sessionId);
        synchronized (this) {
            NativeSslSession session = this.sessions.get(id);
            if (session == null) {
                return -1L;
            }
            if (session.isValid() && session.upRef()) {
                if (session.shouldBeSingleUse()) {
                    removeSessionWithId(session.sessionId());
                }
                session.setLastAccessedTime(System.currentTimeMillis());
                ReferenceCountedOpenSslEngine engine = this.engineMap.get(ssl);
                if (engine != null) {
                    OpenSslSession sslSession = (OpenSslSession) engine.getSession();
                    sslSession.setSessionDetails(session.getCreationTime(), session.getLastAccessedTime(), session.sessionId(), session.keyValueStorage);
                }
                return session.session();
            }
            removeSessionWithId(session.sessionId());
            return -1L;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setSession(long ssl, OpenSslSession session, String host, int port) {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final synchronized void removeSessionWithId(OpenSslSessionId id) {
        NativeSslSession sslSession = this.sessions.remove(id);
        if (sslSession != null) {
            notifyRemovalAndFree(sslSession);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final synchronized boolean containsSessionWithId(OpenSslSessionId id) {
        return this.sessions.containsKey(id);
    }

    private void notifyRemovalAndFree(NativeSslSession session) {
        sessionRemoved(session);
        session.free();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final synchronized OpenSslSession getSession(OpenSslSessionId id) {
        NativeSslSession session = this.sessions.get(id);
        if (session == null || session.isValid()) {
            return session;
        }
        removeSessionWithId(session.sessionId());
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final List<OpenSslSessionId> getIds() {
        OpenSslSession[] sessionsArray;
        synchronized (this) {
            sessionsArray = (OpenSslSession[]) this.sessions.values().toArray(EMPTY_SESSIONS);
        }
        List<OpenSslSessionId> ids = new ArrayList<>(sessionsArray.length);
        for (OpenSslSession session : sessionsArray) {
            if (session.isValid()) {
                ids.add(session.sessionId());
            }
        }
        return ids;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void clear() {
        Iterator<Map.Entry<OpenSslSessionId, NativeSslSession>> iterator = this.sessions.entrySet().iterator();
        while (iterator.hasNext()) {
            NativeSslSession session = iterator.next().getValue();
            iterator.remove();
            notifyRemovalAndFree(session);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static final class NativeSslSession implements OpenSslSession {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        static final ResourceLeakDetector<NativeSslSession> LEAK_DETECTOR = ResourceLeakDetectorFactory.instance().newResourceLeakDetector(NativeSslSession.class);
        private boolean freed;
        private final OpenSslSessionId id;
        final Map<String, Object> keyValueStorage;
        private final String peerHost;
        private final int peerPort;
        private final long session;
        private final long timeout;
        private final long creationTime = System.currentTimeMillis();
        private volatile long lastAccessedTime = this.creationTime;
        private volatile boolean valid = true;
        private final ResourceLeakTracker<NativeSslSession> leakTracker = LEAK_DETECTOR.track(this);

        NativeSslSession(long session, String peerHost, int peerPort, long timeout, Map<String, Object> keyValueStorage) {
            this.session = session;
            this.peerHost = peerHost;
            this.peerPort = peerPort;
            this.timeout = timeout;
            this.id = new OpenSslSessionId(SSLSession.getSessionId(session));
            this.keyValueStorage = keyValueStorage;
        }

        @Override // io.netty.handler.ssl.OpenSslSession
        public Map<String, Object> keyValueStorage() {
            return this.keyValueStorage;
        }

        @Override // io.netty.handler.ssl.OpenSslSession
        public void prepareHandshake() {
            throw new UnsupportedOperationException();
        }

        @Override // io.netty.handler.ssl.OpenSslSession
        public void setSessionDetails(long creationTime, long lastAccessedTime, OpenSslSessionId id, Map<String, Object> keyValueStorage) {
            throw new UnsupportedOperationException();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean shouldBeSingleUse() {
            if (this.freed) {
                throw new AssertionError();
            }
            return SSLSession.shouldBeSingleUse(this.session);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public long session() {
            if (this.freed) {
                throw new AssertionError();
            }
            return this.session;
        }

        boolean upRef() {
            if (this.freed) {
                throw new AssertionError();
            }
            return SSLSession.upRef(this.session);
        }

        synchronized void free() {
            close();
            SSLSession.free(this.session);
        }

        void close() {
            if (this.freed) {
                throw new AssertionError();
            }
            this.freed = true;
            invalidate();
            if (this.leakTracker != null) {
                this.leakTracker.close(this);
            }
        }

        @Override // io.netty.handler.ssl.OpenSslSession
        public OpenSslSessionId sessionId() {
            return this.id;
        }

        boolean isValid(long now) {
            return this.creationTime + this.timeout >= now && this.valid;
        }

        @Override // io.netty.handler.ssl.OpenSslSession
        public void setLocalCertificate(Certificate[] localCertificate) {
            throw new UnsupportedOperationException();
        }

        @Override // javax.net.ssl.SSLSession
        public OpenSslSessionContext getSessionContext() {
            return null;
        }

        @Override // io.netty.handler.ssl.OpenSslSession
        public void tryExpandApplicationBufferSize(int packetLengthDataOnly) {
            throw new UnsupportedOperationException();
        }

        @Override // io.netty.handler.ssl.OpenSslSession
        public void handshakeFinished(byte[] id, String cipher, String protocol, byte[] peerCertificate, byte[][] peerCertificateChain, long creationTime, long timeout) {
            throw new UnsupportedOperationException();
        }

        @Override // javax.net.ssl.SSLSession
        public byte[] getId() {
            return this.id.cloneBytes();
        }

        @Override // javax.net.ssl.SSLSession
        public long getCreationTime() {
            return this.creationTime;
        }

        @Override // io.netty.handler.ssl.OpenSslSession
        public void setLastAccessedTime(long time) {
            this.lastAccessedTime = time;
        }

        @Override // javax.net.ssl.SSLSession
        public long getLastAccessedTime() {
            return this.lastAccessedTime;
        }

        @Override // javax.net.ssl.SSLSession
        public void invalidate() {
            this.valid = false;
        }

        @Override // javax.net.ssl.SSLSession
        public boolean isValid() {
            return isValid(System.currentTimeMillis());
        }

        @Override // javax.net.ssl.SSLSession
        public void putValue(String name, Object value) {
            throw new UnsupportedOperationException();
        }

        @Override // javax.net.ssl.SSLSession
        public Object getValue(String name) {
            return null;
        }

        @Override // javax.net.ssl.SSLSession
        public void removeValue(String name) {
        }

        @Override // javax.net.ssl.SSLSession
        public String[] getValueNames() {
            return EmptyArrays.EMPTY_STRINGS;
        }

        @Override // javax.net.ssl.SSLSession
        public Certificate[] getPeerCertificates() {
            throw new UnsupportedOperationException();
        }

        @Override // javax.net.ssl.SSLSession
        public Certificate[] getLocalCertificates() {
            throw new UnsupportedOperationException();
        }

        @Override // javax.net.ssl.SSLSession
        public X509Certificate[] getPeerCertificateChain() {
            throw new UnsupportedOperationException();
        }

        @Override // javax.net.ssl.SSLSession
        public Principal getPeerPrincipal() {
            throw new UnsupportedOperationException();
        }

        @Override // javax.net.ssl.SSLSession
        public Principal getLocalPrincipal() {
            throw new UnsupportedOperationException();
        }

        @Override // javax.net.ssl.SSLSession
        public String getCipherSuite() {
            return null;
        }

        @Override // javax.net.ssl.SSLSession
        public String getProtocol() {
            return null;
        }

        @Override // javax.net.ssl.SSLSession
        public String getPeerHost() {
            return this.peerHost;
        }

        @Override // javax.net.ssl.SSLSession
        public int getPeerPort() {
            return this.peerPort;
        }

        @Override // javax.net.ssl.SSLSession
        public int getPacketBufferSize() {
            return ReferenceCountedOpenSslEngine.MAX_RECORD_SIZE;
        }

        @Override // javax.net.ssl.SSLSession
        public int getApplicationBufferSize() {
            return ReferenceCountedOpenSslEngine.MAX_PLAINTEXT_LENGTH;
        }

        public int hashCode() {
            return this.id.hashCode();
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof OpenSslSession)) {
                return false;
            }
            OpenSslSession session1 = (OpenSslSession) o;
            return this.id.equals(session1.sessionId());
        }
    }
}
