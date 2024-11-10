package io.netty.resolver.dns;

import io.netty.util.internal.PlatformDependent;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.List;

/* loaded from: classes4.dex */
final class ShuffledDnsServerAddressStream implements DnsServerAddressStream {
    private final List<InetSocketAddress> addresses;
    private int i;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ShuffledDnsServerAddressStream(List<InetSocketAddress> addresses) {
        this.addresses = addresses;
        shuffle();
    }

    private ShuffledDnsServerAddressStream(List<InetSocketAddress> addresses, int startIdx) {
        this.addresses = addresses;
        this.i = startIdx;
    }

    private void shuffle() {
        Collections.shuffle(this.addresses, PlatformDependent.threadLocalRandom());
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
            shuffle();
        }
        return next;
    }

    @Override // io.netty.resolver.dns.DnsServerAddressStream
    public int size() {
        return this.addresses.size();
    }

    @Override // io.netty.resolver.dns.DnsServerAddressStream
    public ShuffledDnsServerAddressStream duplicate() {
        return new ShuffledDnsServerAddressStream(this.addresses, this.i);
    }

    public String toString() {
        return SequentialDnsServerAddressStream.toString("shuffled", this.i, this.addresses);
    }
}
