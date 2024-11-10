package io.netty.resolver.dns;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.List;

/* loaded from: classes4.dex */
final class SequentialDnsServerAddressStream implements DnsServerAddressStream {
    private final List<? extends InetSocketAddress> addresses;
    private int i;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SequentialDnsServerAddressStream(List<? extends InetSocketAddress> addresses, int startIdx) {
        this.addresses = addresses;
        this.i = startIdx;
    }

    @Override // io.netty.resolver.dns.DnsServerAddressStream
    public InetSocketAddress next() {
        int i = this.i;
        InetSocketAddress next = this.addresses.get(i);
        int i2 = i + 1;
        if (i2 < this.addresses.size()) {
            this.i = i2;
        } else {
            this.i = 0;
        }
        return next;
    }

    @Override // io.netty.resolver.dns.DnsServerAddressStream
    public int size() {
        return this.addresses.size();
    }

    @Override // io.netty.resolver.dns.DnsServerAddressStream
    public SequentialDnsServerAddressStream duplicate() {
        return new SequentialDnsServerAddressStream(this.addresses, this.i);
    }

    public String toString() {
        return toString("sequential", this.i, this.addresses);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String toString(String type, int index, Collection<? extends InetSocketAddress> addresses) {
        StringBuilder buf = new StringBuilder(type.length() + 2 + (addresses.size() * 16));
        buf.append(type).append("(index: ").append(index);
        buf.append(", addrs: (");
        for (InetSocketAddress a : addresses) {
            buf.append(a).append(", ");
        }
        buf.setLength(buf.length() - 2);
        buf.append("))");
        return buf.toString();
    }
}
