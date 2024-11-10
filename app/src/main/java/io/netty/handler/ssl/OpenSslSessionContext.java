package io.netty.handler.ssl;

import io.netty.internal.tcnative.SSL;
import io.netty.internal.tcnative.SSLContext;
import io.netty.internal.tcnative.SessionTicketKey;
import io.netty.util.internal.ObjectUtil;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.concurrent.locks.Lock;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSessionContext;
import org.jose4j.jwk.JsonWebKeySet;

/* loaded from: classes4.dex */
public abstract class OpenSslSessionContext implements SSLSessionContext {
    final ReferenceCountedOpenSslContext context;
    private final long mask;
    private final OpenSslKeyMaterialProvider provider;
    private final OpenSslSessionCache sessionCache;
    private final OpenSslSessionStats stats;

    /* JADX INFO: Access modifiers changed from: package-private */
    public OpenSslSessionContext(ReferenceCountedOpenSslContext context, OpenSslKeyMaterialProvider provider, long mask, OpenSslSessionCache cache) {
        this.context = context;
        this.provider = provider;
        this.mask = mask;
        this.stats = new OpenSslSessionStats(context);
        this.sessionCache = cache;
        SSLContext.setSSLSessionCache(context.ctx, cache);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean useKeyManager() {
        return this.provider != null;
    }

    @Override // javax.net.ssl.SSLSessionContext
    public void setSessionCacheSize(int size) {
        ObjectUtil.checkPositiveOrZero(size, "size");
        this.sessionCache.setSessionCacheSize(size);
    }

    @Override // javax.net.ssl.SSLSessionContext
    public int getSessionCacheSize() {
        return this.sessionCache.getSessionCacheSize();
    }

    @Override // javax.net.ssl.SSLSessionContext
    public void setSessionTimeout(int seconds) {
        ObjectUtil.checkPositiveOrZero(seconds, "seconds");
        Lock writerLock = this.context.ctxLock.writeLock();
        writerLock.lock();
        try {
            SSLContext.setSessionCacheTimeout(this.context.ctx, seconds);
            this.sessionCache.setSessionTimeout(seconds);
        } finally {
            writerLock.unlock();
        }
    }

    @Override // javax.net.ssl.SSLSessionContext
    public int getSessionTimeout() {
        return this.sessionCache.getSessionTimeout();
    }

    @Override // javax.net.ssl.SSLSessionContext
    public SSLSession getSession(byte[] bytes) {
        return this.sessionCache.getSession(new OpenSslSessionId(bytes));
    }

    @Override // javax.net.ssl.SSLSessionContext
    public Enumeration<byte[]> getIds() {
        return new Enumeration<byte[]>() { // from class: io.netty.handler.ssl.OpenSslSessionContext.1
            private final Iterator<OpenSslSessionId> ids;

            {
                this.ids = OpenSslSessionContext.this.sessionCache.getIds().iterator();
            }

            @Override // java.util.Enumeration
            public boolean hasMoreElements() {
                return this.ids.hasNext();
            }

            @Override // java.util.Enumeration
            public byte[] nextElement() {
                return this.ids.next().cloneBytes();
            }
        };
    }

    @Deprecated
    public void setTicketKeys(byte[] keys) {
        if (keys.length % 48 != 0) {
            throw new IllegalArgumentException("keys.length % 48 != 0");
        }
        SessionTicketKey[] tickets = new SessionTicketKey[keys.length / 48];
        int i = 0;
        int a = 0;
        while (i < tickets.length) {
            byte[] name = Arrays.copyOfRange(keys, a, 16);
            int a2 = a + 16;
            byte[] hmacKey = Arrays.copyOfRange(keys, a2, 16);
            int i2 = i + 16;
            byte[] aesKey = Arrays.copyOfRange(keys, a2, 16);
            a = a2 + 16;
            tickets[i2] = new SessionTicketKey(name, hmacKey, aesKey);
            i = i2 + 1;
        }
        Lock writerLock = this.context.ctxLock.writeLock();
        writerLock.lock();
        try {
            SSLContext.clearOptions(this.context.ctx, SSL.SSL_OP_NO_TICKET);
            SSLContext.setSessionTicketKeys(this.context.ctx, tickets);
        } finally {
            writerLock.unlock();
        }
    }

    public void setTicketKeys(OpenSslSessionTicketKey... keys) {
        ObjectUtil.checkNotNull(keys, JsonWebKeySet.JWK_SET_MEMBER_NAME);
        SessionTicketKey[] ticketKeys = new SessionTicketKey[keys.length];
        for (int i = 0; i < ticketKeys.length; i++) {
            ticketKeys[i] = keys[i].key;
        }
        Lock writerLock = this.context.ctxLock.writeLock();
        writerLock.lock();
        try {
            SSLContext.clearOptions(this.context.ctx, SSL.SSL_OP_NO_TICKET);
            if (ticketKeys.length > 0) {
                SSLContext.setSessionTicketKeys(this.context.ctx, ticketKeys);
            }
        } finally {
            writerLock.unlock();
        }
    }

    public void setSessionCacheEnabled(boolean enabled) {
        long mode = enabled ? this.mask | SSL.SSL_SESS_CACHE_NO_INTERNAL_LOOKUP | SSL.SSL_SESS_CACHE_NO_INTERNAL_STORE : SSL.SSL_SESS_CACHE_OFF;
        Lock writerLock = this.context.ctxLock.writeLock();
        writerLock.lock();
        try {
            SSLContext.setSessionCacheMode(this.context.ctx, mode);
            if (!enabled) {
                this.sessionCache.clear();
            }
        } finally {
            writerLock.unlock();
        }
    }

    public boolean isSessionCacheEnabled() {
        Lock readerLock = this.context.ctxLock.readLock();
        readerLock.lock();
        try {
            return (SSLContext.getSessionCacheMode(this.context.ctx) & this.mask) != 0;
        } finally {
            readerLock.unlock();
        }
    }

    public OpenSslSessionStats stats() {
        return this.stats;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void removeFromCache(OpenSslSessionId id) {
        this.sessionCache.removeSessionWithId(id);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean isInCache(OpenSslSessionId id) {
        return this.sessionCache.containsSessionWithId(id);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setSessionFromCache(long ssl, OpenSslSession session, String host, int port) {
        return this.sessionCache.setSession(ssl, session, host, port);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void destroy() {
        if (this.provider != null) {
            this.provider.destroy();
        }
        this.sessionCache.clear();
    }
}
