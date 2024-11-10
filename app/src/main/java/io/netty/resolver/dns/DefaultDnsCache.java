package io.netty.resolver.dns;

import io.netty.channel.EventLoop;
import io.netty.handler.codec.dns.DnsRecord;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.AbstractList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes4.dex */
public class DefaultDnsCache implements DnsCache {
    private final int maxTtl;
    private final int minTtl;
    private final int negativeTtl;
    private final Cache<DefaultDnsCacheEntry> resolveCache;

    public DefaultDnsCache() {
        this(0, Cache.MAX_SUPPORTED_TTL_SECS, 0);
    }

    public DefaultDnsCache(int minTtl, int maxTtl, int negativeTtl) {
        this.resolveCache = new Cache<DefaultDnsCacheEntry>() { // from class: io.netty.resolver.dns.DefaultDnsCache.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // io.netty.resolver.dns.Cache
            public boolean shouldReplaceAll(DefaultDnsCacheEntry entry) {
                return entry.cause() != null;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // io.netty.resolver.dns.Cache
            public boolean equals(DefaultDnsCacheEntry entry, DefaultDnsCacheEntry otherEntry) {
                if (entry.address() != null) {
                    return entry.address().equals(otherEntry.address());
                }
                if (otherEntry.address() != null) {
                    return false;
                }
                return entry.cause().equals(otherEntry.cause());
            }
        };
        this.minTtl = Math.min(Cache.MAX_SUPPORTED_TTL_SECS, ObjectUtil.checkPositiveOrZero(minTtl, "minTtl"));
        this.maxTtl = Math.min(Cache.MAX_SUPPORTED_TTL_SECS, ObjectUtil.checkPositiveOrZero(maxTtl, "maxTtl"));
        if (minTtl > maxTtl) {
            throw new IllegalArgumentException("minTtl: " + minTtl + ", maxTtl: " + maxTtl + " (expected: 0 <= minTtl <= maxTtl)");
        }
        this.negativeTtl = Math.min(Cache.MAX_SUPPORTED_TTL_SECS, ObjectUtil.checkPositiveOrZero(negativeTtl, "negativeTtl"));
    }

    public int minTtl() {
        return this.minTtl;
    }

    public int maxTtl() {
        return this.maxTtl;
    }

    public int negativeTtl() {
        return this.negativeTtl;
    }

    @Override // io.netty.resolver.dns.DnsCache
    public void clear() {
        this.resolveCache.clear();
    }

    @Override // io.netty.resolver.dns.DnsCache
    public boolean clear(String hostname) {
        ObjectUtil.checkNotNull(hostname, "hostname");
        return this.resolveCache.clear(appendDot(hostname));
    }

    private static boolean emptyAdditionals(DnsRecord[] additionals) {
        return additionals == null || additionals.length == 0;
    }

    @Override // io.netty.resolver.dns.DnsCache
    public List<? extends DnsCacheEntry> get(String hostname, DnsRecord[] additionals) {
        ObjectUtil.checkNotNull(hostname, "hostname");
        if (!emptyAdditionals(additionals)) {
            return Collections.emptyList();
        }
        List<? extends DnsCacheEntry> entries = this.resolveCache.get(appendDot(hostname));
        if (entries == null || entries.isEmpty()) {
            return entries;
        }
        return new DnsCacheEntryList(entries);
    }

    @Override // io.netty.resolver.dns.DnsCache
    public DnsCacheEntry cache(String hostname, DnsRecord[] additionals, InetAddress address, long originalTtl, EventLoop loop) {
        ObjectUtil.checkNotNull(hostname, "hostname");
        ObjectUtil.checkNotNull(address, "address");
        ObjectUtil.checkNotNull(loop, "loop");
        DefaultDnsCacheEntry e = new DefaultDnsCacheEntry(hostname, address);
        if (this.maxTtl == 0 || !emptyAdditionals(additionals)) {
            return e;
        }
        this.resolveCache.cache(appendDot(hostname), e, Math.max(this.minTtl, (int) Math.min(this.maxTtl, originalTtl)), loop);
        return e;
    }

    @Override // io.netty.resolver.dns.DnsCache
    public DnsCacheEntry cache(String hostname, DnsRecord[] additionals, Throwable cause, EventLoop loop) {
        ObjectUtil.checkNotNull(hostname, "hostname");
        ObjectUtil.checkNotNull(cause, "cause");
        ObjectUtil.checkNotNull(loop, "loop");
        DefaultDnsCacheEntry e = new DefaultDnsCacheEntry(hostname, cause);
        if (this.negativeTtl == 0 || !emptyAdditionals(additionals)) {
            return e;
        }
        this.resolveCache.cache(appendDot(hostname), e, this.negativeTtl, loop);
        return e;
    }

    public String toString() {
        return "DefaultDnsCache(minTtl=" + this.minTtl + ", maxTtl=" + this.maxTtl + ", negativeTtl=" + this.negativeTtl + ", cached resolved hostname=" + this.resolveCache.size() + ')';
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class DefaultDnsCacheEntry implements DnsCacheEntry {
        private final InetAddress address;
        private final Throwable cause;
        private final int hash;
        private final String hostname;

        DefaultDnsCacheEntry(String hostname, InetAddress address) {
            this.hostname = hostname;
            this.address = address;
            this.cause = null;
            this.hash = System.identityHashCode(this);
        }

        DefaultDnsCacheEntry(String hostname, Throwable cause) {
            this.hostname = hostname;
            this.cause = cause;
            this.address = null;
            this.hash = System.identityHashCode(this);
        }

        private DefaultDnsCacheEntry(DefaultDnsCacheEntry entry) {
            this.hostname = entry.hostname;
            if (entry.cause == null) {
                this.address = entry.address;
                this.cause = null;
            } else {
                this.address = null;
                this.cause = DefaultDnsCache.copyThrowable(entry.cause);
            }
            this.hash = entry.hash;
        }

        @Override // io.netty.resolver.dns.DnsCacheEntry
        public InetAddress address() {
            return this.address;
        }

        @Override // io.netty.resolver.dns.DnsCacheEntry
        public Throwable cause() {
            return this.cause;
        }

        String hostname() {
            return this.hostname;
        }

        public String toString() {
            if (this.cause != null) {
                return this.hostname + '/' + this.cause;
            }
            return this.address.toString();
        }

        public int hashCode() {
            return this.hash;
        }

        public boolean equals(Object obj) {
            return (obj instanceof DefaultDnsCacheEntry) && ((DefaultDnsCacheEntry) obj).hash == this.hash;
        }

        DnsCacheEntry copyIfNeeded() {
            if (this.cause == null) {
                return this;
            }
            return new DefaultDnsCacheEntry(this);
        }
    }

    private static String appendDot(String hostname) {
        return StringUtil.endsWith(hostname, '.') ? hostname : hostname + '.';
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Throwable copyThrowable(Throwable error) {
        if (error.getClass() == UnknownHostException.class) {
            UnknownHostException copy = new UnknownHostException(error.getMessage()) { // from class: io.netty.resolver.dns.DefaultDnsCache.2
                @Override // java.lang.Throwable
                public Throwable fillInStackTrace() {
                    return this;
                }
            };
            copy.initCause(error.getCause());
            copy.setStackTrace(error.getStackTrace());
            return copy;
        }
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try {
            try {
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    oos = new ObjectOutputStream(baos);
                    oos.writeObject(error);
                    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                    ois = new ObjectInputStream(bais);
                    Throwable th = (Throwable) ois.readObject();
                    try {
                        oos.close();
                    } catch (IOException e) {
                    }
                    try {
                        ois.close();
                    } catch (IOException e2) {
                    }
                    return th;
                } catch (IOException e3) {
                    throw new IllegalStateException(e3);
                }
            } catch (ClassNotFoundException e4) {
                throw new IllegalStateException(e4);
            }
        } finally {
        }
    }

    /* loaded from: classes4.dex */
    private static final class DnsCacheEntryList extends AbstractList<DnsCacheEntry> {
        private final List<? extends DnsCacheEntry> entries;

        DnsCacheEntryList(List<? extends DnsCacheEntry> entries) {
            this.entries = entries;
        }

        @Override // java.util.AbstractList, java.util.List
        public DnsCacheEntry get(int index) {
            DefaultDnsCacheEntry entry = (DefaultDnsCacheEntry) this.entries.get(index);
            return entry.copyIfNeeded();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.entries.size();
        }

        @Override // java.util.AbstractList, java.util.Collection, java.util.List
        public int hashCode() {
            return super.hashCode();
        }

        @Override // java.util.AbstractList, java.util.Collection, java.util.List
        public boolean equals(Object o) {
            if (o instanceof DnsCacheEntryList) {
                return this.entries.equals(((DnsCacheEntryList) o).entries);
            }
            return super.equals(o);
        }
    }
}
