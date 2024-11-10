package io.netty.resolver.dns;

import io.netty.channel.EventLoop;
import io.netty.handler.codec.dns.DnsRecord;
import java.net.InetAddress;
import java.util.Collections;
import java.util.List;

/* loaded from: classes4.dex */
public final class NoopDnsCache implements DnsCache {
    public static final NoopDnsCache INSTANCE = new NoopDnsCache();

    private NoopDnsCache() {
    }

    @Override // io.netty.resolver.dns.DnsCache
    public void clear() {
    }

    @Override // io.netty.resolver.dns.DnsCache
    public boolean clear(String hostname) {
        return false;
    }

    @Override // io.netty.resolver.dns.DnsCache
    public List<? extends DnsCacheEntry> get(String hostname, DnsRecord[] additionals) {
        return Collections.emptyList();
    }

    @Override // io.netty.resolver.dns.DnsCache
    public DnsCacheEntry cache(String hostname, DnsRecord[] additional, InetAddress address, long originalTtl, EventLoop loop) {
        return new NoopDnsCacheEntry(address);
    }

    @Override // io.netty.resolver.dns.DnsCache
    public DnsCacheEntry cache(String hostname, DnsRecord[] additional, Throwable cause, EventLoop loop) {
        return null;
    }

    public String toString() {
        return NoopDnsCache.class.getSimpleName();
    }

    /* loaded from: classes4.dex */
    private static final class NoopDnsCacheEntry implements DnsCacheEntry {
        private final InetAddress address;

        NoopDnsCacheEntry(InetAddress address) {
            this.address = address;
        }

        @Override // io.netty.resolver.dns.DnsCacheEntry
        public InetAddress address() {
            return this.address;
        }

        @Override // io.netty.resolver.dns.DnsCacheEntry
        public Throwable cause() {
            return null;
        }

        public String toString() {
            return this.address.toString();
        }
    }
}
