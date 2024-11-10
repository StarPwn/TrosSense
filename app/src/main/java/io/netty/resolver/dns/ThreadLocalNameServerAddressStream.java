package io.netty.resolver.dns;

import io.netty.util.concurrent.FastThreadLocal;
import java.net.InetSocketAddress;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class ThreadLocalNameServerAddressStream implements DnsServerAddressStream {
    private final DnsServerAddressStreamProvider dnsServerAddressStreamProvider;
    private final String hostname;
    private final FastThreadLocal<DnsServerAddressStream> threadLocal;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ThreadLocalNameServerAddressStream(DnsServerAddressStreamProvider dnsServerAddressStreamProvider) {
        this(dnsServerAddressStreamProvider, "");
    }

    ThreadLocalNameServerAddressStream(DnsServerAddressStreamProvider dnsServerAddressStreamProvider, String hostname) {
        this.threadLocal = new FastThreadLocal<DnsServerAddressStream>() { // from class: io.netty.resolver.dns.ThreadLocalNameServerAddressStream.1
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // io.netty.util.concurrent.FastThreadLocal
            public DnsServerAddressStream initialValue() {
                return ThreadLocalNameServerAddressStream.this.dnsServerAddressStreamProvider.nameServerAddressStream(ThreadLocalNameServerAddressStream.this.hostname);
            }
        };
        this.dnsServerAddressStreamProvider = dnsServerAddressStreamProvider;
        this.hostname = hostname;
    }

    @Override // io.netty.resolver.dns.DnsServerAddressStream
    public InetSocketAddress next() {
        return this.threadLocal.get().next();
    }

    @Override // io.netty.resolver.dns.DnsServerAddressStream
    public DnsServerAddressStream duplicate() {
        return new ThreadLocalNameServerAddressStream(this.dnsServerAddressStreamProvider, this.hostname);
    }

    @Override // io.netty.resolver.dns.DnsServerAddressStream
    public int size() {
        return this.threadLocal.get().size();
    }
}
