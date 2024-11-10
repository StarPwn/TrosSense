package io.netty.resolver.dns;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class RotationalDnsServerAddresses extends DefaultDnsServerAddresses {
    private static final AtomicIntegerFieldUpdater<RotationalDnsServerAddresses> startIdxUpdater = AtomicIntegerFieldUpdater.newUpdater(RotationalDnsServerAddresses.class, "startIdx");
    private volatile int startIdx;

    /* JADX INFO: Access modifiers changed from: package-private */
    public RotationalDnsServerAddresses(List<InetSocketAddress> addresses) {
        super("rotational", addresses);
    }

    @Override // io.netty.resolver.dns.DnsServerAddresses
    public DnsServerAddressStream stream() {
        int curStartIdx;
        int nextStartIdx;
        do {
            curStartIdx = this.startIdx;
            nextStartIdx = curStartIdx + 1;
            if (nextStartIdx >= this.addresses.size()) {
                nextStartIdx = 0;
            }
        } while (!startIdxUpdater.compareAndSet(this, curStartIdx, nextStartIdx));
        return new SequentialDnsServerAddressStream(this.addresses, curStartIdx);
    }
}
