package io.netty.handler.ssl;

import io.netty.handler.ssl.OpenSslSessionCache;
import io.netty.internal.tcnative.SSL;
import io.netty.util.AsciiString;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes4.dex */
final class OpenSslClientSessionCache extends OpenSslSessionCache {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Map<HostPort, OpenSslSessionCache.NativeSslSession> sessions;

    /* JADX INFO: Access modifiers changed from: package-private */
    public OpenSslClientSessionCache(OpenSslEngineMap engineMap) {
        super(engineMap);
        this.sessions = new HashMap();
    }

    @Override // io.netty.handler.ssl.OpenSslSessionCache
    protected boolean sessionCreated(OpenSslSessionCache.NativeSslSession session) {
        if (!Thread.holdsLock(this)) {
            throw new AssertionError();
        }
        HostPort hostPort = keyFor(session.getPeerHost(), session.getPeerPort());
        if (hostPort == null || this.sessions.containsKey(hostPort)) {
            return false;
        }
        this.sessions.put(hostPort, session);
        return true;
    }

    @Override // io.netty.handler.ssl.OpenSslSessionCache
    protected void sessionRemoved(OpenSslSessionCache.NativeSslSession session) {
        if (!Thread.holdsLock(this)) {
            throw new AssertionError();
        }
        HostPort hostPort = keyFor(session.getPeerHost(), session.getPeerPort());
        if (hostPort == null) {
            return;
        }
        this.sessions.remove(hostPort);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // io.netty.handler.ssl.OpenSslSessionCache
    public boolean setSession(long ssl, OpenSslSession session, String host, int port) {
        HostPort hostPort = keyFor(host, port);
        if (hostPort == null) {
            return false;
        }
        boolean singleUsed = false;
        synchronized (this) {
            try {
                try {
                    OpenSslSessionCache.NativeSslSession nativeSslSession = this.sessions.get(hostPort);
                    if (nativeSslSession == null) {
                        return false;
                    }
                    if (!nativeSslSession.isValid()) {
                        removeSessionWithId(nativeSslSession.sessionId());
                        return false;
                    }
                    boolean reused = SSL.setSession(ssl, nativeSslSession.session());
                    if (reused) {
                        singleUsed = nativeSslSession.shouldBeSingleUse();
                    }
                    if (reused) {
                        if (singleUsed) {
                            nativeSslSession.invalidate();
                            session.invalidate();
                        }
                        nativeSslSession.setLastAccessedTime(System.currentTimeMillis());
                        session.setSessionDetails(nativeSslSession.getCreationTime(), nativeSslSession.getLastAccessedTime(), nativeSslSession.sessionId(), nativeSslSession.keyValueStorage);
                    }
                    return reused;
                } catch (Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                throw th;
            }
        }
    }

    private static HostPort keyFor(String host, int port) {
        if (host == null && port < 1) {
            return null;
        }
        return new HostPort(host, port);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // io.netty.handler.ssl.OpenSslSessionCache
    public synchronized void clear() {
        super.clear();
        this.sessions.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class HostPort {
        private final int hash;
        private final String host;
        private final int port;

        HostPort(String host, int port) {
            this.host = host;
            this.port = port;
            this.hash = (AsciiString.hashCode(host) * 31) + port;
        }

        public int hashCode() {
            return this.hash;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof HostPort)) {
                return false;
            }
            HostPort other = (HostPort) obj;
            return this.port == other.port && this.host.equalsIgnoreCase(other.host);
        }

        public String toString() {
            return "HostPort{host='" + this.host + "', port=" + this.port + '}';
        }
    }
}
