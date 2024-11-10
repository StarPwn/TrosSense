package io.netty.resolver.dns;

import io.netty.channel.EventLoop;
import io.netty.handler.codec.dns.DnsRecord;
import io.netty.util.internal.ObjectUtil;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes4.dex */
final class AuthoritativeDnsServerCacheAdapter implements AuthoritativeDnsServerCache {
    private static final DnsRecord[] EMPTY = new DnsRecord[0];
    private final DnsCache cache;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AuthoritativeDnsServerCacheAdapter(DnsCache cache) {
        this.cache = (DnsCache) ObjectUtil.checkNotNull(cache, "cache");
    }

    @Override // io.netty.resolver.dns.AuthoritativeDnsServerCache
    public DnsServerAddressStream get(String hostname) {
        List<? extends DnsCacheEntry> entries = this.cache.get(hostname, EMPTY);
        if (entries == null || entries.isEmpty() || entries.get(0).cause() != null) {
            return null;
        }
        List<InetSocketAddress> addresses = new ArrayList<>(entries.size());
        int i = 0;
        do {
            InetAddress addr = entries.get(i).address();
            addresses.add(new InetSocketAddress(addr, 53));
            i++;
        } while (i < entries.size());
        return new SequentialDnsServerAddressStream(addresses, 0);
    }

    @Override // io.netty.resolver.dns.AuthoritativeDnsServerCache
    public void cache(String hostname, InetSocketAddress address, long originalTtl, EventLoop loop) {
        if (!address.isUnresolved()) {
            this.cache.cache(hostname, EMPTY, address.getAddress(), originalTtl, loop);
        }
    }

    @Override // io.netty.resolver.dns.AuthoritativeDnsServerCache
    public void clear() {
        this.cache.clear();
    }

    @Override // io.netty.resolver.dns.AuthoritativeDnsServerCache
    public boolean clear(String hostname) {
        return this.cache.clear(hostname);
    }
}
