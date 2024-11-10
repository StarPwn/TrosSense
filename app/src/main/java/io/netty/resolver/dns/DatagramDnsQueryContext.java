package io.netty.resolver.dns;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.AddressedEnvelope;
import io.netty.channel.Channel;
import io.netty.handler.codec.dns.DatagramDnsQuery;
import io.netty.handler.codec.dns.DnsQuery;
import io.netty.handler.codec.dns.DnsQuestion;
import io.netty.handler.codec.dns.DnsRecord;
import io.netty.handler.codec.dns.DnsResponse;
import io.netty.handler.codec.rtsp.RtspHeaders;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;
import java.net.InetSocketAddress;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class DatagramDnsQueryContext extends DnsQueryContext {
    /* JADX INFO: Access modifiers changed from: package-private */
    public DatagramDnsQueryContext(Channel channel, Future<? extends Channel> channelReadyFuture, InetSocketAddress nameServerAddr, DnsQueryContextManager queryContextManager, int maxPayLoadSize, boolean recursionDesired, long queryTimeoutMillis, DnsQuestion question, DnsRecord[] additionals, Promise<AddressedEnvelope<DnsResponse, InetSocketAddress>> promise, Bootstrap socketBootstrap, boolean retryWithTcpOnTimeout) {
        super(channel, channelReadyFuture, nameServerAddr, queryContextManager, maxPayLoadSize, recursionDesired, queryTimeoutMillis, question, additionals, promise, socketBootstrap, retryWithTcpOnTimeout);
    }

    @Override // io.netty.resolver.dns.DnsQueryContext
    protected DnsQuery newQuery(int id, InetSocketAddress nameServerAddr) {
        return new DatagramDnsQuery(null, nameServerAddr, id);
    }

    @Override // io.netty.resolver.dns.DnsQueryContext
    protected String protocol() {
        return RtspHeaders.Values.UDP;
    }
}
