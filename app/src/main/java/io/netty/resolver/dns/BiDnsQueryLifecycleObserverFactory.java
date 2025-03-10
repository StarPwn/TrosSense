package io.netty.resolver.dns;

import io.netty.handler.codec.dns.DnsQuestion;
import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public final class BiDnsQueryLifecycleObserverFactory implements DnsQueryLifecycleObserverFactory {
    private final DnsQueryLifecycleObserverFactory a;
    private final DnsQueryLifecycleObserverFactory b;

    public BiDnsQueryLifecycleObserverFactory(DnsQueryLifecycleObserverFactory a, DnsQueryLifecycleObserverFactory b) {
        this.a = (DnsQueryLifecycleObserverFactory) ObjectUtil.checkNotNull(a, "a");
        this.b = (DnsQueryLifecycleObserverFactory) ObjectUtil.checkNotNull(b, "b");
    }

    @Override // io.netty.resolver.dns.DnsQueryLifecycleObserverFactory
    public DnsQueryLifecycleObserver newDnsQueryLifecycleObserver(DnsQuestion question) {
        return new BiDnsQueryLifecycleObserver(this.a.newDnsQueryLifecycleObserver(question), this.b.newDnsQueryLifecycleObserver(question));
    }
}
