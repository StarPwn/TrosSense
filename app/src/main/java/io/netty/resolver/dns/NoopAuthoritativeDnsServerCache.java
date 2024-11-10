package io.netty.resolver.dns;

import io.netty.channel.EventLoop;
import java.net.InetSocketAddress;

/* loaded from: classes4.dex */
public final class NoopAuthoritativeDnsServerCache implements AuthoritativeDnsServerCache {
    public static final NoopAuthoritativeDnsServerCache INSTANCE = new NoopAuthoritativeDnsServerCache();

    private NoopAuthoritativeDnsServerCache() {
    }

    @Override // io.netty.resolver.dns.AuthoritativeDnsServerCache
    public DnsServerAddressStream get(String hostname) {
        return null;
    }

    @Override // io.netty.resolver.dns.AuthoritativeDnsServerCache
    public void cache(String hostname, InetSocketAddress address, long originalTtl, EventLoop loop) {
    }

    @Override // io.netty.resolver.dns.AuthoritativeDnsServerCache
    public void clear() {
    }

    @Override // io.netty.resolver.dns.AuthoritativeDnsServerCache
    public boolean clear(String hostname) {
        return false;
    }
}
