package io.netty.resolver.dns;

import java.net.InetSocketAddress;

/* loaded from: classes4.dex */
public interface DnsServerAddressStream {
    DnsServerAddressStream duplicate();

    InetSocketAddress next();

    int size();
}
