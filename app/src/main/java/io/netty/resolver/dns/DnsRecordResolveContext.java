package io.netty.resolver.dns;

import io.netty.channel.Channel;
import io.netty.channel.EventLoop;
import io.netty.handler.codec.dns.DnsQuestion;
import io.netty.handler.codec.dns.DnsRecord;
import io.netty.handler.codec.dns.DnsRecordType;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;
import java.net.UnknownHostException;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class DnsRecordResolveContext extends DnsResolveContext<DnsRecord> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public DnsRecordResolveContext(DnsNameResolver parent, Channel channel, Future<? extends Channel> channelReadyFuture, Promise<?> originalPromise, DnsQuestion question, DnsRecord[] additionals, DnsServerAddressStream nameServerAddrs, int allowedQueries) {
        this(parent, channel, channelReadyFuture, originalPromise, question.name(), question.dnsClass(), new DnsRecordType[]{question.type()}, additionals, nameServerAddrs, allowedQueries);
    }

    private DnsRecordResolveContext(DnsNameResolver parent, Channel channel, Future<? extends Channel> channelReadyFuture, Promise<?> originalPromise, String hostname, int dnsClass, DnsRecordType[] expectedTypes, DnsRecord[] additionals, DnsServerAddressStream nameServerAddrs, int allowedQueries) {
        super(parent, channel, channelReadyFuture, originalPromise, hostname, dnsClass, expectedTypes, additionals, nameServerAddrs, allowedQueries);
    }

    @Override // io.netty.resolver.dns.DnsResolveContext
    DnsResolveContext<DnsRecord> newResolverContext(DnsNameResolver parent, Channel channel, Future<? extends Channel> channelReadyFuture, Promise<?> originalPromise, String hostname, int dnsClass, DnsRecordType[] expectedTypes, DnsRecord[] additionals, DnsServerAddressStream nameServerAddrs, int allowedQueries) {
        return new DnsRecordResolveContext(parent, channel, channelReadyFuture, originalPromise, hostname, dnsClass, expectedTypes, additionals, nameServerAddrs, allowedQueries);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.resolver.dns.DnsResolveContext
    public DnsRecord convertRecord(DnsRecord record, String hostname, DnsRecord[] additionals, EventLoop eventLoop) {
        return (DnsRecord) ReferenceCountUtil.retain(record);
    }

    @Override // io.netty.resolver.dns.DnsResolveContext
    List<DnsRecord> filterResults(List<DnsRecord> unfiltered) {
        return unfiltered;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // io.netty.resolver.dns.DnsResolveContext
    public boolean isCompleteEarly(DnsRecord resolved) {
        return false;
    }

    @Override // io.netty.resolver.dns.DnsResolveContext
    boolean isDuplicateAllowed() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // io.netty.resolver.dns.DnsResolveContext
    public void cache(String hostname, DnsRecord[] additionals, DnsRecord result, DnsRecord convertedResult) {
    }

    @Override // io.netty.resolver.dns.DnsResolveContext
    void cache(String hostname, DnsRecord[] additionals, UnknownHostException cause) {
    }

    @Override // io.netty.resolver.dns.DnsResolveContext
    DnsCnameCache cnameCache() {
        return NoopDnsCnameCache.INSTANCE;
    }
}
