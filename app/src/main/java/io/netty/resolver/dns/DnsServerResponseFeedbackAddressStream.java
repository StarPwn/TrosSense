package io.netty.resolver.dns;

import java.net.InetSocketAddress;

/* loaded from: classes4.dex */
public interface DnsServerResponseFeedbackAddressStream extends DnsServerAddressStream {
    void feedbackFailure(InetSocketAddress inetSocketAddress, Throwable th, long j);

    void feedbackSuccess(InetSocketAddress inetSocketAddress, long j);
}
