package io.netty.resolver.dns;

import java.net.InetSocketAddress;

/* loaded from: classes4.dex */
public final class SequentialDnsServerAddressStreamProvider extends UniSequentialDnsServerAddressStreamProvider {
    public SequentialDnsServerAddressStreamProvider(InetSocketAddress... addresses) {
        super(DnsServerAddresses.sequential(addresses));
    }

    public SequentialDnsServerAddressStreamProvider(Iterable<? extends InetSocketAddress> addresses) {
        super(DnsServerAddresses.sequential(addresses));
    }
}
