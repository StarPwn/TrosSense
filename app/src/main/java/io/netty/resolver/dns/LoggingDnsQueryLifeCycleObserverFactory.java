package io.netty.resolver.dns;

import io.netty.handler.codec.dns.DnsQuestion;
import io.netty.handler.logging.LogLevel;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.logging.InternalLogLevel;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/* loaded from: classes4.dex */
public final class LoggingDnsQueryLifeCycleObserverFactory implements DnsQueryLifecycleObserverFactory {
    private static final InternalLogger DEFAULT_LOGGER = InternalLoggerFactory.getInstance((Class<?>) LoggingDnsQueryLifeCycleObserverFactory.class);
    private final InternalLogLevel level;
    private final InternalLogger logger;

    public LoggingDnsQueryLifeCycleObserverFactory() {
        this(LogLevel.DEBUG);
    }

    public LoggingDnsQueryLifeCycleObserverFactory(LogLevel level) {
        this.level = checkAndConvertLevel(level);
        this.logger = DEFAULT_LOGGER;
    }

    public LoggingDnsQueryLifeCycleObserverFactory(Class<?> classContext, LogLevel level) {
        this.level = checkAndConvertLevel(level);
        this.logger = InternalLoggerFactory.getInstance((Class<?>) ObjectUtil.checkNotNull(classContext, "classContext"));
    }

    public LoggingDnsQueryLifeCycleObserverFactory(String name, LogLevel level) {
        this.level = checkAndConvertLevel(level);
        this.logger = InternalLoggerFactory.getInstance((String) ObjectUtil.checkNotNull(name, "name"));
    }

    private static InternalLogLevel checkAndConvertLevel(LogLevel level) {
        return ((LogLevel) ObjectUtil.checkNotNull(level, "level")).toInternalLevel();
    }

    @Override // io.netty.resolver.dns.DnsQueryLifecycleObserverFactory
    public DnsQueryLifecycleObserver newDnsQueryLifecycleObserver(DnsQuestion question) {
        return new LoggingDnsQueryLifecycleObserver(question, this.logger, this.level);
    }
}
