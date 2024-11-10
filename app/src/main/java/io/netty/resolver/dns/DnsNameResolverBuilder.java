package io.netty.resolver.dns;

import io.netty.channel.ChannelFactory;
import io.netty.channel.EventLoop;
import io.netty.channel.ReflectiveChannelFactory;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.InternetProtocolFamily;
import io.netty.channel.socket.SocketChannel;
import io.netty.resolver.HostsFileEntriesResolver;
import io.netty.resolver.ResolvedAddressTypes;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes4.dex */
public final class DnsNameResolverBuilder {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) DnsNameResolverBuilder.class);
    private AuthoritativeDnsServerCache authoritativeDnsServerCache;
    private ChannelFactory<? extends DatagramChannel> channelFactory;
    private DnsCnameCache cnameCache;
    private boolean completeOncePreferredResolved;
    volatile EventLoop eventLoop;
    private SocketAddress localAddress;
    private int maxNumConsolidation;
    private Integer maxTtl;
    private Integer minTtl;
    private Integer negativeTtl;
    private DnsServerAddressStream queryDnsServerAddressStream;
    private DnsCache resolveCache;
    private boolean retryOnTimeout;
    private String[] searchDomains;
    private ChannelFactory<? extends SocketChannel> socketChannelFactory;
    private boolean traceEnabled;
    private long queryTimeoutMillis = -1;
    private ResolvedAddressTypes resolvedAddressTypes = DnsNameResolver.DEFAULT_RESOLVE_ADDRESS_TYPES;
    private boolean recursionDesired = true;
    private int maxQueriesPerResolve = -1;
    private int maxPayloadSize = 4096;
    private boolean optResourceEnabled = true;
    private HostsFileEntriesResolver hostsFileEntriesResolver = HostsFileEntriesResolver.DEFAULT;
    private DnsServerAddressStreamProvider dnsServerAddressStreamProvider = DnsServerAddressStreamProviders.platformDefault();
    private DnsQueryLifecycleObserverFactory dnsQueryLifecycleObserverFactory = NoopDnsQueryLifecycleObserverFactory.INSTANCE;
    private int ndots = -1;
    private boolean decodeIdn = true;

    public DnsNameResolverBuilder() {
    }

    public DnsNameResolverBuilder(EventLoop eventLoop) {
        eventLoop(eventLoop);
    }

    public DnsNameResolverBuilder eventLoop(EventLoop eventLoop) {
        this.eventLoop = eventLoop;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ChannelFactory<? extends DatagramChannel> channelFactory() {
        return this.channelFactory;
    }

    public DnsNameResolverBuilder channelFactory(ChannelFactory<? extends DatagramChannel> channelFactory) {
        this.channelFactory = channelFactory;
        return this;
    }

    public DnsNameResolverBuilder channelType(Class<? extends DatagramChannel> channelType) {
        return channelFactory(new ReflectiveChannelFactory(channelType));
    }

    public DnsNameResolverBuilder socketChannelFactory(ChannelFactory<? extends SocketChannel> channelFactory) {
        return socketChannelFactory(channelFactory, false);
    }

    public DnsNameResolverBuilder socketChannelType(Class<? extends SocketChannel> channelType) {
        return socketChannelType(channelType, false);
    }

    public DnsNameResolverBuilder socketChannelFactory(ChannelFactory<? extends SocketChannel> channelFactory, boolean retryOnTimeout) {
        this.socketChannelFactory = channelFactory;
        this.retryOnTimeout = retryOnTimeout;
        return this;
    }

    public DnsNameResolverBuilder socketChannelType(Class<? extends SocketChannel> channelType, boolean retryOnTimeout) {
        if (channelType == null) {
            return socketChannelFactory(null, retryOnTimeout);
        }
        return socketChannelFactory(new ReflectiveChannelFactory(channelType), retryOnTimeout);
    }

    public DnsNameResolverBuilder resolveCache(DnsCache resolveCache) {
        this.resolveCache = resolveCache;
        return this;
    }

    public DnsNameResolverBuilder cnameCache(DnsCnameCache cnameCache) {
        this.cnameCache = cnameCache;
        return this;
    }

    public DnsNameResolverBuilder dnsQueryLifecycleObserverFactory(DnsQueryLifecycleObserverFactory lifecycleObserverFactory) {
        this.dnsQueryLifecycleObserverFactory = (DnsQueryLifecycleObserverFactory) ObjectUtil.checkNotNull(lifecycleObserverFactory, "lifecycleObserverFactory");
        return this;
    }

    @Deprecated
    public DnsNameResolverBuilder authoritativeDnsServerCache(DnsCache authoritativeDnsServerCache) {
        this.authoritativeDnsServerCache = new AuthoritativeDnsServerCacheAdapter(authoritativeDnsServerCache);
        return this;
    }

    public DnsNameResolverBuilder authoritativeDnsServerCache(AuthoritativeDnsServerCache authoritativeDnsServerCache) {
        this.authoritativeDnsServerCache = authoritativeDnsServerCache;
        return this;
    }

    public DnsNameResolverBuilder localAddress(SocketAddress localAddress) {
        this.localAddress = localAddress;
        return this;
    }

    public DnsNameResolverBuilder ttl(int minTtl, int maxTtl) {
        this.maxTtl = Integer.valueOf(maxTtl);
        this.minTtl = Integer.valueOf(minTtl);
        return this;
    }

    public DnsNameResolverBuilder negativeTtl(int negativeTtl) {
        this.negativeTtl = Integer.valueOf(negativeTtl);
        return this;
    }

    public DnsNameResolverBuilder queryTimeoutMillis(long queryTimeoutMillis) {
        this.queryTimeoutMillis = queryTimeoutMillis;
        return this;
    }

    public static ResolvedAddressTypes computeResolvedAddressTypes(InternetProtocolFamily... internetProtocolFamilies) {
        if (internetProtocolFamilies == null || internetProtocolFamilies.length == 0) {
            return DnsNameResolver.DEFAULT_RESOLVE_ADDRESS_TYPES;
        }
        if (internetProtocolFamilies.length > 2) {
            throw new IllegalArgumentException("No more than 2 InternetProtocolFamilies");
        }
        switch (internetProtocolFamilies[0]) {
            case IPv4:
                return (internetProtocolFamilies.length < 2 || internetProtocolFamilies[1] != InternetProtocolFamily.IPv6) ? ResolvedAddressTypes.IPV4_ONLY : ResolvedAddressTypes.IPV4_PREFERRED;
            case IPv6:
                return (internetProtocolFamilies.length < 2 || internetProtocolFamilies[1] != InternetProtocolFamily.IPv4) ? ResolvedAddressTypes.IPV6_ONLY : ResolvedAddressTypes.IPV6_PREFERRED;
            default:
                throw new IllegalArgumentException("Couldn't resolve ResolvedAddressTypes from InternetProtocolFamily array");
        }
    }

    public DnsNameResolverBuilder resolvedAddressTypes(ResolvedAddressTypes resolvedAddressTypes) {
        this.resolvedAddressTypes = resolvedAddressTypes;
        return this;
    }

    public DnsNameResolverBuilder completeOncePreferredResolved(boolean completeOncePreferredResolved) {
        this.completeOncePreferredResolved = completeOncePreferredResolved;
        return this;
    }

    public DnsNameResolverBuilder recursionDesired(boolean recursionDesired) {
        this.recursionDesired = recursionDesired;
        return this;
    }

    public DnsNameResolverBuilder maxQueriesPerResolve(int maxQueriesPerResolve) {
        this.maxQueriesPerResolve = maxQueriesPerResolve;
        return this;
    }

    @Deprecated
    public DnsNameResolverBuilder traceEnabled(boolean traceEnabled) {
        this.traceEnabled = traceEnabled;
        return this;
    }

    public DnsNameResolverBuilder maxPayloadSize(int maxPayloadSize) {
        this.maxPayloadSize = maxPayloadSize;
        return this;
    }

    public DnsNameResolverBuilder optResourceEnabled(boolean optResourceEnabled) {
        this.optResourceEnabled = optResourceEnabled;
        return this;
    }

    public DnsNameResolverBuilder hostsFileEntriesResolver(HostsFileEntriesResolver hostsFileEntriesResolver) {
        this.hostsFileEntriesResolver = hostsFileEntriesResolver;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public DnsServerAddressStreamProvider nameServerProvider() {
        return this.dnsServerAddressStreamProvider;
    }

    public DnsNameResolverBuilder nameServerProvider(DnsServerAddressStreamProvider dnsServerAddressStreamProvider) {
        this.dnsServerAddressStreamProvider = (DnsServerAddressStreamProvider) ObjectUtil.checkNotNull(dnsServerAddressStreamProvider, "dnsServerAddressStreamProvider");
        return this;
    }

    protected DnsServerAddressStream queryServerAddressStream() {
        return this.queryDnsServerAddressStream;
    }

    public DnsNameResolverBuilder queryServerAddressStream(DnsServerAddressStream queryServerAddressStream) {
        this.queryDnsServerAddressStream = (DnsServerAddressStream) ObjectUtil.checkNotNull(queryServerAddressStream, "queryServerAddressStream");
        return this;
    }

    public DnsNameResolverBuilder searchDomains(Iterable<String> searchDomains) {
        String f;
        ObjectUtil.checkNotNull(searchDomains, "searchDomains");
        List<String> list = new ArrayList<>(4);
        Iterator<String> it2 = searchDomains.iterator();
        while (it2.hasNext() && (f = it2.next()) != null) {
            if (!list.contains(f)) {
                list.add(f);
            }
        }
        this.searchDomains = (String[]) list.toArray(EmptyArrays.EMPTY_STRINGS);
        return this;
    }

    public DnsNameResolverBuilder ndots(int ndots) {
        this.ndots = ndots;
        return this;
    }

    private DnsCache newCache() {
        return new DefaultDnsCache(ObjectUtil.intValue(this.minTtl, 0), ObjectUtil.intValue(this.maxTtl, Integer.MAX_VALUE), ObjectUtil.intValue(this.negativeTtl, 0));
    }

    private AuthoritativeDnsServerCache newAuthoritativeDnsServerCache() {
        return new DefaultAuthoritativeDnsServerCache(ObjectUtil.intValue(this.minTtl, 0), ObjectUtil.intValue(this.maxTtl, Integer.MAX_VALUE), new NameServerComparator(DnsNameResolver.preferredAddressType(this.resolvedAddressTypes).addressType()));
    }

    private DnsServerAddressStream newQueryServerAddressStream(DnsServerAddressStreamProvider dnsServerAddressStreamProvider) {
        return new ThreadLocalNameServerAddressStream(dnsServerAddressStreamProvider);
    }

    private DnsCnameCache newCnameCache() {
        return new DefaultDnsCnameCache(ObjectUtil.intValue(this.minTtl, 0), ObjectUtil.intValue(this.maxTtl, Integer.MAX_VALUE));
    }

    public DnsNameResolverBuilder decodeIdn(boolean decodeIdn) {
        this.decodeIdn = decodeIdn;
        return this;
    }

    public DnsNameResolverBuilder consolidateCacheSize(int maxNumConsolidation) {
        this.maxNumConsolidation = ObjectUtil.checkPositiveOrZero(maxNumConsolidation, "maxNumConsolidation");
        return this;
    }

    public DnsNameResolver build() {
        AuthoritativeDnsServerCache authoritativeDnsServerCache;
        DnsServerAddressStream queryDnsServerAddressStream;
        if (this.eventLoop == null) {
            throw new IllegalStateException("eventLoop should be specified to build a DnsNameResolver.");
        }
        if (this.resolveCache != null && (this.minTtl != null || this.maxTtl != null || this.negativeTtl != null)) {
            logger.debug("resolveCache and TTLs are mutually exclusive. TTLs are ignored.");
        }
        if (this.cnameCache != null && (this.minTtl != null || this.maxTtl != null || this.negativeTtl != null)) {
            logger.debug("cnameCache and TTLs are mutually exclusive. TTLs are ignored.");
        }
        if (this.authoritativeDnsServerCache != null && (this.minTtl != null || this.maxTtl != null || this.negativeTtl != null)) {
            logger.debug("authoritativeDnsServerCache and TTLs are mutually exclusive. TTLs are ignored.");
        }
        DnsCache resolveCache = this.resolveCache != null ? this.resolveCache : newCache();
        DnsCnameCache cnameCache = this.cnameCache != null ? this.cnameCache : newCnameCache();
        if (this.authoritativeDnsServerCache != null) {
            authoritativeDnsServerCache = this.authoritativeDnsServerCache;
        } else {
            authoritativeDnsServerCache = newAuthoritativeDnsServerCache();
        }
        if (this.queryDnsServerAddressStream != null) {
            queryDnsServerAddressStream = this.queryDnsServerAddressStream;
        } else {
            queryDnsServerAddressStream = newQueryServerAddressStream(this.dnsServerAddressStreamProvider);
        }
        return new DnsNameResolver(this.eventLoop, this.channelFactory, this.socketChannelFactory, this.retryOnTimeout, resolveCache, cnameCache, authoritativeDnsServerCache, this.localAddress, this.dnsQueryLifecycleObserverFactory, this.queryTimeoutMillis, this.resolvedAddressTypes, this.recursionDesired, this.maxQueriesPerResolve, this.traceEnabled, this.maxPayloadSize, this.optResourceEnabled, this.hostsFileEntriesResolver, this.dnsServerAddressStreamProvider, queryDnsServerAddressStream, this.searchDomains, this.ndots, this.decodeIdn, this.completeOncePreferredResolved, this.maxNumConsolidation);
    }

    public DnsNameResolverBuilder copy() {
        DnsNameResolverBuilder copiedBuilder = new DnsNameResolverBuilder();
        if (this.eventLoop != null) {
            copiedBuilder.eventLoop(this.eventLoop);
        }
        if (this.channelFactory != null) {
            copiedBuilder.channelFactory(this.channelFactory);
        }
        copiedBuilder.socketChannelFactory(this.socketChannelFactory, this.retryOnTimeout);
        if (this.resolveCache != null) {
            copiedBuilder.resolveCache(this.resolveCache);
        }
        if (this.cnameCache != null) {
            copiedBuilder.cnameCache(this.cnameCache);
        }
        if (this.maxTtl != null && this.minTtl != null) {
            copiedBuilder.ttl(this.minTtl.intValue(), this.maxTtl.intValue());
        }
        if (this.negativeTtl != null) {
            copiedBuilder.negativeTtl(this.negativeTtl.intValue());
        }
        if (this.authoritativeDnsServerCache != null) {
            copiedBuilder.authoritativeDnsServerCache(this.authoritativeDnsServerCache);
        }
        if (this.dnsQueryLifecycleObserverFactory != null) {
            copiedBuilder.dnsQueryLifecycleObserverFactory(this.dnsQueryLifecycleObserverFactory);
        }
        copiedBuilder.queryTimeoutMillis(this.queryTimeoutMillis);
        copiedBuilder.resolvedAddressTypes(this.resolvedAddressTypes);
        copiedBuilder.recursionDesired(this.recursionDesired);
        copiedBuilder.maxQueriesPerResolve(this.maxQueriesPerResolve);
        copiedBuilder.traceEnabled(this.traceEnabled);
        copiedBuilder.maxPayloadSize(this.maxPayloadSize);
        copiedBuilder.optResourceEnabled(this.optResourceEnabled);
        copiedBuilder.hostsFileEntriesResolver(this.hostsFileEntriesResolver);
        if (this.dnsServerAddressStreamProvider != null) {
            copiedBuilder.nameServerProvider(this.dnsServerAddressStreamProvider);
        }
        if (this.queryDnsServerAddressStream != null) {
            copiedBuilder.queryServerAddressStream(this.queryDnsServerAddressStream);
        }
        if (this.searchDomains != null) {
            copiedBuilder.searchDomains(Arrays.asList(this.searchDomains));
        }
        copiedBuilder.ndots(this.ndots);
        copiedBuilder.decodeIdn(this.decodeIdn);
        copiedBuilder.completeOncePreferredResolved(this.completeOncePreferredResolved);
        copiedBuilder.localAddress(this.localAddress);
        copiedBuilder.consolidateCacheSize(this.maxNumConsolidation);
        return copiedBuilder;
    }
}
