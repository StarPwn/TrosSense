package io.netty.resolver.dns;

import io.netty.channel.Channel;
import io.netty.channel.EventLoop;
import io.netty.handler.codec.dns.DnsRecord;
import io.netty.handler.codec.dns.DnsRecordType;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class DnsAddressResolveContext extends DnsResolveContext<InetAddress> {
    private final AuthoritativeDnsServerCache authoritativeDnsServerCache;
    private final boolean completeEarlyIfPossible;
    private final DnsCache resolveCache;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DnsAddressResolveContext(DnsNameResolver parent, Channel channel, Future<? extends Channel> channelReadyFuture, Promise<?> originalPromise, String hostname, DnsRecord[] additionals, DnsServerAddressStream nameServerAddrs, int allowedQueries, DnsCache resolveCache, AuthoritativeDnsServerCache authoritativeDnsServerCache, boolean completeEarlyIfPossible) {
        super(parent, channel, channelReadyFuture, originalPromise, hostname, 1, parent.resolveRecordTypes(), additionals, nameServerAddrs, allowedQueries);
        this.resolveCache = resolveCache;
        this.authoritativeDnsServerCache = authoritativeDnsServerCache;
        this.completeEarlyIfPossible = completeEarlyIfPossible;
    }

    @Override // io.netty.resolver.dns.DnsResolveContext
    DnsResolveContext<InetAddress> newResolverContext(DnsNameResolver parent, Channel channel, Future<? extends Channel> channelReadyFuture, Promise<?> originalPromise, String hostname, int dnsClass, DnsRecordType[] expectedTypes, DnsRecord[] additionals, DnsServerAddressStream nameServerAddrs, int allowedQueries) {
        return new DnsAddressResolveContext(parent, channel, channelReadyFuture, originalPromise, hostname, additionals, nameServerAddrs, allowedQueries, this.resolveCache, this.authoritativeDnsServerCache, this.completeEarlyIfPossible);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // io.netty.resolver.dns.DnsResolveContext
    public InetAddress convertRecord(DnsRecord record, String hostname, DnsRecord[] additionals, EventLoop eventLoop) {
        return DnsAddressDecoder.decodeAddress(record, hostname, this.parent.isDecodeIdn());
    }

    @Override // io.netty.resolver.dns.DnsResolveContext
    List<InetAddress> filterResults(List<InetAddress> unfiltered) {
        Collections.sort(unfiltered, PreferredAddressTypeComparator.comparator(this.parent.preferredAddressType()));
        return unfiltered;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // io.netty.resolver.dns.DnsResolveContext
    public boolean isCompleteEarly(InetAddress resolved) {
        return this.completeEarlyIfPossible && this.parent.preferredAddressType().addressType() == resolved.getClass();
    }

    @Override // io.netty.resolver.dns.DnsResolveContext
    boolean isDuplicateAllowed() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // io.netty.resolver.dns.DnsResolveContext
    public void cache(String hostname, DnsRecord[] additionals, DnsRecord result, InetAddress convertedResult) {
        this.resolveCache.cache(hostname, additionals, convertedResult, result.timeToLive(), channel().eventLoop());
    }

    @Override // io.netty.resolver.dns.DnsResolveContext
    void cache(String hostname, DnsRecord[] additionals, UnknownHostException cause) {
        this.resolveCache.cache(hostname, additionals, cause, channel().eventLoop());
    }

    @Override // io.netty.resolver.dns.DnsResolveContext
    void doSearchDomainQuery(String hostname, Promise<List<InetAddress>> nextPromise) {
        if (!DnsNameResolver.doResolveAllCached(hostname, this.additionals, nextPromise, this.resolveCache, this.parent.resolvedInternetProtocolFamiliesUnsafe())) {
            super.doSearchDomainQuery(hostname, nextPromise);
        }
    }

    @Override // io.netty.resolver.dns.DnsResolveContext
    DnsCache resolveCache() {
        return this.resolveCache;
    }

    @Override // io.netty.resolver.dns.DnsResolveContext
    AuthoritativeDnsServerCache authoritativeDnsServerCache() {
        return this.authoritativeDnsServerCache;
    }
}
