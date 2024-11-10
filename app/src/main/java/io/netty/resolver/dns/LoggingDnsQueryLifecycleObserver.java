package io.netty.resolver.dns;

import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.dns.DnsQuestion;
import io.netty.handler.codec.dns.DnsResponseCode;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.logging.InternalLogLevel;
import io.netty.util.internal.logging.InternalLogger;
import java.net.InetSocketAddress;
import java.util.List;

/* loaded from: classes4.dex */
final class LoggingDnsQueryLifecycleObserver implements DnsQueryLifecycleObserver {
    private InetSocketAddress dnsServerAddress;
    private final InternalLogLevel level;
    private final InternalLogger logger;
    private final DnsQuestion question;

    /* JADX INFO: Access modifiers changed from: package-private */
    public LoggingDnsQueryLifecycleObserver(DnsQuestion question, InternalLogger logger, InternalLogLevel level) {
        this.question = (DnsQuestion) ObjectUtil.checkNotNull(question, "question");
        this.logger = (InternalLogger) ObjectUtil.checkNotNull(logger, "logger");
        this.level = (InternalLogLevel) ObjectUtil.checkNotNull(level, "level");
    }

    @Override // io.netty.resolver.dns.DnsQueryLifecycleObserver
    public void queryWritten(InetSocketAddress dnsServerAddress, ChannelFuture future) {
        this.dnsServerAddress = dnsServerAddress;
    }

    @Override // io.netty.resolver.dns.DnsQueryLifecycleObserver
    public void queryCancelled(int queriesRemaining) {
        if (this.dnsServerAddress != null) {
            this.logger.log(this.level, "from {} : {} cancelled with {} queries remaining", this.dnsServerAddress, this.question, Integer.valueOf(queriesRemaining));
        } else {
            this.logger.log(this.level, "{} query never written and cancelled with {} queries remaining", this.question, Integer.valueOf(queriesRemaining));
        }
    }

    @Override // io.netty.resolver.dns.DnsQueryLifecycleObserver
    public DnsQueryLifecycleObserver queryRedirected(List<InetSocketAddress> nameServers) {
        this.logger.log(this.level, "from {} : {} redirected", this.dnsServerAddress, this.question);
        return this;
    }

    @Override // io.netty.resolver.dns.DnsQueryLifecycleObserver
    public DnsQueryLifecycleObserver queryCNAMEd(DnsQuestion cnameQuestion) {
        this.logger.log(this.level, "from {} : {} CNAME question {}", this.dnsServerAddress, this.question, cnameQuestion);
        return this;
    }

    @Override // io.netty.resolver.dns.DnsQueryLifecycleObserver
    public DnsQueryLifecycleObserver queryNoAnswer(DnsResponseCode code) {
        this.logger.log(this.level, "from {} : {} no answer {}", this.dnsServerAddress, this.question, code);
        return this;
    }

    @Override // io.netty.resolver.dns.DnsQueryLifecycleObserver
    public void queryFailed(Throwable cause) {
        if (this.dnsServerAddress != null) {
            this.logger.log(this.level, "from {} : {} failure", this.dnsServerAddress, this.question, cause);
        } else {
            this.logger.log(this.level, "{} query never written and failed", this.question, cause);
        }
    }

    @Override // io.netty.resolver.dns.DnsQueryLifecycleObserver
    public void querySucceed() {
    }
}
