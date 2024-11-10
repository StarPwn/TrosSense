package io.netty.resolver.dns;

import io.netty.channel.AddressedEnvelope;
import io.netty.channel.Channel;
import io.netty.handler.codec.dns.DefaultDnsQuery;
import io.netty.handler.codec.dns.DnsQuery;
import io.netty.handler.codec.dns.DnsQuestion;
import io.netty.handler.codec.dns.DnsRecord;
import io.netty.handler.codec.dns.DnsResponse;
import io.netty.handler.codec.rtsp.RtspHeaders;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;
import java.net.InetSocketAddress;

/* loaded from: classes4.dex */
final class TcpDnsQueryContext extends DnsQueryContext {
    /* JADX INFO: Access modifiers changed from: package-private */
    public TcpDnsQueryContext(Channel channel, Future<? extends Channel> channelReadyFuture, InetSocketAddress nameServerAddr, DnsQueryContextManager queryContextManager, int maxPayLoadSize, boolean recursionDesired, long queryTimeoutMillis, DnsQuestion question, DnsRecord[] additionals, Promise<AddressedEnvelope<DnsResponse, InetSocketAddress>> promise) {
        super(channel, channelReadyFuture, nameServerAddr, queryContextManager, maxPayLoadSize, recursionDesired, queryTimeoutMillis, question, additionals, promise, null, false);
    }

    @Override // io.netty.resolver.dns.DnsQueryContext
    protected DnsQuery newQuery(int id, InetSocketAddress nameServerAddr) {
        return new DefaultDnsQuery(id);
    }

    @Override // io.netty.resolver.dns.DnsQueryContext
    protected String protocol() {
        return RtspHeaders.Values.TCP;
    }
}
