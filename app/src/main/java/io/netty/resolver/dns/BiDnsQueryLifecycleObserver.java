package io.netty.resolver.dns;

import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.dns.DnsQuestion;
import io.netty.handler.codec.dns.DnsResponseCode;
import io.netty.util.internal.ObjectUtil;
import java.net.InetSocketAddress;
import java.util.List;

/* loaded from: classes4.dex */
public final class BiDnsQueryLifecycleObserver implements DnsQueryLifecycleObserver {
    private final DnsQueryLifecycleObserver a;
    private final DnsQueryLifecycleObserver b;

    public BiDnsQueryLifecycleObserver(DnsQueryLifecycleObserver a, DnsQueryLifecycleObserver b) {
        this.a = (DnsQueryLifecycleObserver) ObjectUtil.checkNotNull(a, "a");
        this.b = (DnsQueryLifecycleObserver) ObjectUtil.checkNotNull(b, "b");
    }

    @Override // io.netty.resolver.dns.DnsQueryLifecycleObserver
    public void queryWritten(InetSocketAddress dnsServerAddress, ChannelFuture future) {
        try {
            this.a.queryWritten(dnsServerAddress, future);
        } finally {
            this.b.queryWritten(dnsServerAddress, future);
        }
    }

    @Override // io.netty.resolver.dns.DnsQueryLifecycleObserver
    public void queryCancelled(int queriesRemaining) {
        try {
            this.a.queryCancelled(queriesRemaining);
        } finally {
            this.b.queryCancelled(queriesRemaining);
        }
    }

    @Override // io.netty.resolver.dns.DnsQueryLifecycleObserver
    public DnsQueryLifecycleObserver queryRedirected(List<InetSocketAddress> nameServers) {
        try {
            this.a.queryRedirected(nameServers);
            return this;
        } finally {
            this.b.queryRedirected(nameServers);
        }
    }

    @Override // io.netty.resolver.dns.DnsQueryLifecycleObserver
    public DnsQueryLifecycleObserver queryCNAMEd(DnsQuestion cnameQuestion) {
        try {
            this.a.queryCNAMEd(cnameQuestion);
            return this;
        } finally {
            this.b.queryCNAMEd(cnameQuestion);
        }
    }

    @Override // io.netty.resolver.dns.DnsQueryLifecycleObserver
    public DnsQueryLifecycleObserver queryNoAnswer(DnsResponseCode code) {
        try {
            this.a.queryNoAnswer(code);
            return this;
        } finally {
            this.b.queryNoAnswer(code);
        }
    }

    @Override // io.netty.resolver.dns.DnsQueryLifecycleObserver
    public void queryFailed(Throwable cause) {
        try {
            this.a.queryFailed(cause);
        } finally {
            this.b.queryFailed(cause);
        }
    }

    @Override // io.netty.resolver.dns.DnsQueryLifecycleObserver
    public void querySucceed() {
        try {
            this.a.querySucceed();
        } finally {
            this.b.querySucceed();
        }
    }
}
