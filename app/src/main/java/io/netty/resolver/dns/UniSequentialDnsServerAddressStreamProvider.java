package io.netty.resolver.dns;

import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
abstract class UniSequentialDnsServerAddressStreamProvider implements DnsServerAddressStreamProvider {
    private final DnsServerAddresses addresses;

    /* JADX INFO: Access modifiers changed from: package-private */
    public UniSequentialDnsServerAddressStreamProvider(DnsServerAddresses addresses) {
        this.addresses = (DnsServerAddresses) ObjectUtil.checkNotNull(addresses, "addresses");
    }

    @Override // io.netty.resolver.dns.DnsServerAddressStreamProvider
    public final DnsServerAddressStream nameServerAddressStream(String hostname) {
        return this.addresses.stream();
    }
}
