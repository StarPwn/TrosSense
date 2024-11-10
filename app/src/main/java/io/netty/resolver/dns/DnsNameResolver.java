package io.netty.resolver.dns;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.AddressedEnvelope;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFactory;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoop;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.InternetProtocolFamily;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.handler.codec.dns.DatagramDnsQueryEncoder;
import io.netty.handler.codec.dns.DatagramDnsResponse;
import io.netty.handler.codec.dns.DatagramDnsResponseDecoder;
import io.netty.handler.codec.dns.DefaultDnsRawRecord;
import io.netty.handler.codec.dns.DnsQuestion;
import io.netty.handler.codec.dns.DnsRawRecord;
import io.netty.handler.codec.dns.DnsRecord;
import io.netty.handler.codec.dns.DnsRecordType;
import io.netty.handler.codec.dns.DnsResponse;
import io.netty.resolver.DefaultHostsFileEntriesResolver;
import io.netty.resolver.HostsFileEntriesResolver;
import io.netty.resolver.InetNameResolver;
import io.netty.resolver.ResolvedAddressTypes;
import io.netty.util.AttributeKey;
import io.netty.util.NetUtil;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.lang.reflect.Method;
import java.net.IDN;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/* loaded from: classes4.dex */
public class DnsNameResolver extends InetNameResolver {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final DatagramDnsResponseDecoder DATAGRAM_DECODER;
    private static final DatagramDnsQueryEncoder DATAGRAM_ENCODER;
    private static final UnixResolverOptions DEFAULT_OPTIONS;
    static final ResolvedAddressTypes DEFAULT_RESOLVE_ADDRESS_TYPES;
    static final String[] DEFAULT_SEARCH_DOMAINS;
    private static final String LOCALHOST = "localhost";
    private static final InetAddress LOCALHOST_ADDRESS;
    private static final String WINDOWS_HOST_NAME;
    private final AuthoritativeDnsServerCache authoritativeDnsServerCache;
    private final Channel ch;
    private final Promise<Channel> channelReadyPromise;
    private final DnsCnameCache cnameCache;
    private final boolean completeOncePreferredResolved;
    private final boolean decodeIdn;
    private final DnsQueryLifecycleObserverFactory dnsQueryLifecycleObserverFactory;
    private final DnsServerAddressStreamProvider dnsServerAddressStreamProvider;
    private final HostsFileEntriesResolver hostsFileEntriesResolver;
    private final Map<String, Future<List<InetAddress>>> inflightLookups;
    private final int maxNumConsolidation;
    private final int maxPayloadSize;
    private final int maxQueriesPerResolve;
    private final Comparator<InetSocketAddress> nameServerComparator;
    private final int ndots;
    private final boolean optResourceEnabled;
    private final InternetProtocolFamily preferredAddressType;
    private final DnsQueryContextManager queryContextManager;
    private final DnsServerAddressStream queryDnsServerAddressStream;
    private final long queryTimeoutMillis;
    private final boolean recursionDesired;
    private final DnsCache resolveCache;
    private final DnsRecordType[] resolveRecordTypes;
    private final ResolvedAddressTypes resolvedAddressTypes;
    private final InternetProtocolFamily[] resolvedInternetProtocolFamilies;
    private final boolean retryWithTcpOnTimeout;
    private final String[] searchDomains;
    private final Bootstrap socketBootstrap;
    private final boolean supportsAAAARecords;
    private final boolean supportsARecords;
    public static final AttributeKey<Boolean> DNS_PIPELINE_ATTRIBUTE = AttributeKey.newInstance("io.netty.resolver.dns.pipeline");
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) DnsNameResolver.class);
    private static final DnsRecord[] EMPTY_ADDITIONALS = new DnsRecord[0];
    private static final DnsRecordType[] IPV4_ONLY_RESOLVED_RECORD_TYPES = {DnsRecordType.A};
    private static final InternetProtocolFamily[] IPV4_ONLY_RESOLVED_PROTOCOL_FAMILIES = {InternetProtocolFamily.IPv4};
    private static final DnsRecordType[] IPV4_PREFERRED_RESOLVED_RECORD_TYPES = {DnsRecordType.A, DnsRecordType.AAAA};
    private static final InternetProtocolFamily[] IPV4_PREFERRED_RESOLVED_PROTOCOL_FAMILIES = {InternetProtocolFamily.IPv4, InternetProtocolFamily.IPv6};
    private static final DnsRecordType[] IPV6_ONLY_RESOLVED_RECORD_TYPES = {DnsRecordType.AAAA};
    private static final InternetProtocolFamily[] IPV6_ONLY_RESOLVED_PROTOCOL_FAMILIES = {InternetProtocolFamily.IPv6};
    private static final DnsRecordType[] IPV6_PREFERRED_RESOLVED_RECORD_TYPES = {DnsRecordType.AAAA, DnsRecordType.A};
    private static final InternetProtocolFamily[] IPV6_PREFERRED_RESOLVED_PROTOCOL_FAMILIES = {InternetProtocolFamily.IPv6, InternetProtocolFamily.IPv4};
    private static final ChannelHandler NOOP_HANDLER = new ChannelHandlerAdapter() { // from class: io.netty.resolver.dns.DnsNameResolver.1
        @Override // io.netty.channel.ChannelHandlerAdapter
        public boolean isSharable() {
            return true;
        }
    };

    static {
        String hostName;
        String[] searchDomains;
        UnixResolverOptions options;
        List<String> list;
        if (NetUtil.isIpV4StackPreferred() || !anyInterfaceSupportsIpV6()) {
            DEFAULT_RESOLVE_ADDRESS_TYPES = ResolvedAddressTypes.IPV4_ONLY;
            LOCALHOST_ADDRESS = NetUtil.LOCALHOST4;
        } else if (NetUtil.isIpV6AddressesPreferred()) {
            DEFAULT_RESOLVE_ADDRESS_TYPES = ResolvedAddressTypes.IPV6_PREFERRED;
            LOCALHOST_ADDRESS = NetUtil.LOCALHOST6;
        } else {
            DEFAULT_RESOLVE_ADDRESS_TYPES = ResolvedAddressTypes.IPV4_PREFERRED;
            LOCALHOST_ADDRESS = NetUtil.LOCALHOST4;
        }
        logger.debug("Default ResolvedAddressTypes: {}", DEFAULT_RESOLVE_ADDRESS_TYPES);
        logger.debug("Localhost address: {}", LOCALHOST_ADDRESS);
        try {
            hostName = PlatformDependent.isWindows() ? InetAddress.getLocalHost().getHostName() : null;
        } catch (Exception e) {
            hostName = null;
        }
        WINDOWS_HOST_NAME = hostName;
        logger.debug("Windows hostname: {}", WINDOWS_HOST_NAME);
        try {
            if (PlatformDependent.isWindows()) {
                list = getSearchDomainsHack();
            } else {
                list = UnixResolverDnsServerAddressStreamProvider.parseEtcResolverSearchDomains();
            }
            searchDomains = (String[]) list.toArray(EmptyArrays.EMPTY_STRINGS);
        } catch (Exception e2) {
            searchDomains = EmptyArrays.EMPTY_STRINGS;
        }
        DEFAULT_SEARCH_DOMAINS = searchDomains;
        logger.debug("Default search domains: {}", Arrays.toString(DEFAULT_SEARCH_DOMAINS));
        try {
            options = UnixResolverDnsServerAddressStreamProvider.parseEtcResolverOptions();
        } catch (Exception e3) {
            options = UnixResolverOptions.newBuilder().build();
        }
        DEFAULT_OPTIONS = options;
        logger.debug("Default {}", DEFAULT_OPTIONS);
        DATAGRAM_DECODER = new DatagramDnsResponseDecoder() { // from class: io.netty.resolver.dns.DnsNameResolver.2
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // io.netty.handler.codec.dns.DatagramDnsResponseDecoder
            public DnsResponse decodeResponse(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
                DnsResponse response = super.decodeResponse(ctx, packet);
                if (((ByteBuf) packet.content()).isReadable()) {
                    response.setTruncated(true);
                    if (DnsNameResolver.logger.isDebugEnabled()) {
                        DnsNameResolver.logger.debug("{} RECEIVED: UDP [{}: {}] truncated packet received, consider adjusting maxPayloadSize for the {}.", ctx.channel(), Integer.valueOf(response.id()), packet.sender(), StringUtil.simpleClassName((Class<?>) DnsNameResolver.class));
                    }
                }
                return response;
            }
        };
        DATAGRAM_ENCODER = new DatagramDnsQueryEncoder();
    }

    private static boolean anyInterfaceSupportsIpV6() {
        for (NetworkInterface iface : NetUtil.NETWORK_INTERFACES) {
            Enumeration<InetAddress> addresses = iface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress inetAddress = addresses.nextElement();
                if ((inetAddress instanceof Inet6Address) && !inetAddress.isAnyLocalAddress() && !inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                    return true;
                }
            }
        }
        return false;
    }

    private static List<String> getSearchDomainsHack() throws Exception {
        if (PlatformDependent.javaVersion() < 9) {
            Class<?> configClass = Class.forName("sun.net.dns.ResolverConfiguration");
            Method open = configClass.getMethod("open", new Class[0]);
            Method nameservers = configClass.getMethod("searchlist", new Class[0]);
            Object instance = open.invoke(null, new Object[0]);
            return (List) nameservers.invoke(instance, new Object[0]);
        }
        return Collections.emptyList();
    }

    @Deprecated
    public DnsNameResolver(EventLoop eventLoop, ChannelFactory<? extends DatagramChannel> channelFactory, DnsCache resolveCache, DnsCache authoritativeDnsServerCache, DnsQueryLifecycleObserverFactory dnsQueryLifecycleObserverFactory, long queryTimeoutMillis, ResolvedAddressTypes resolvedAddressTypes, boolean recursionDesired, int maxQueriesPerResolve, boolean traceEnabled, int maxPayloadSize, boolean optResourceEnabled, HostsFileEntriesResolver hostsFileEntriesResolver, DnsServerAddressStreamProvider dnsServerAddressStreamProvider, String[] searchDomains, int ndots, boolean decodeIdn) {
        this(eventLoop, channelFactory, resolveCache, new AuthoritativeDnsServerCacheAdapter(authoritativeDnsServerCache), dnsQueryLifecycleObserverFactory, queryTimeoutMillis, resolvedAddressTypes, recursionDesired, maxQueriesPerResolve, traceEnabled, maxPayloadSize, optResourceEnabled, hostsFileEntriesResolver, dnsServerAddressStreamProvider, searchDomains, ndots, decodeIdn);
    }

    @Deprecated
    public DnsNameResolver(EventLoop eventLoop, ChannelFactory<? extends DatagramChannel> channelFactory, DnsCache resolveCache, AuthoritativeDnsServerCache authoritativeDnsServerCache, DnsQueryLifecycleObserverFactory dnsQueryLifecycleObserverFactory, long queryTimeoutMillis, ResolvedAddressTypes resolvedAddressTypes, boolean recursionDesired, int maxQueriesPerResolve, boolean traceEnabled, int maxPayloadSize, boolean optResourceEnabled, HostsFileEntriesResolver hostsFileEntriesResolver, DnsServerAddressStreamProvider dnsServerAddressStreamProvider, String[] searchDomains, int ndots, boolean decodeIdn) {
        this(eventLoop, channelFactory, null, false, resolveCache, NoopDnsCnameCache.INSTANCE, authoritativeDnsServerCache, null, dnsQueryLifecycleObserverFactory, queryTimeoutMillis, resolvedAddressTypes, recursionDesired, maxQueriesPerResolve, traceEnabled, maxPayloadSize, optResourceEnabled, hostsFileEntriesResolver, dnsServerAddressStreamProvider, new ThreadLocalNameServerAddressStream(dnsServerAddressStreamProvider), searchDomains, ndots, decodeIdn, false, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DnsNameResolver(EventLoop eventLoop, ChannelFactory<? extends DatagramChannel> channelFactory, ChannelFactory<? extends SocketChannel> socketChannelFactory, boolean retryWithTcpOnTimeout, final DnsCache resolveCache, final DnsCnameCache cnameCache, final AuthoritativeDnsServerCache authoritativeDnsServerCache, SocketAddress localAddress, DnsQueryLifecycleObserverFactory dnsQueryLifecycleObserverFactory, long queryTimeoutMillis, ResolvedAddressTypes resolvedAddressTypes, boolean recursionDesired, int maxQueriesPerResolve, boolean traceEnabled, int maxPayloadSize, boolean optResourceEnabled, HostsFileEntriesResolver hostsFileEntriesResolver, DnsServerAddressStreamProvider dnsServerAddressStreamProvider, DnsServerAddressStream queryDnsServerAddressStream, String[] searchDomains, int ndots, boolean decodeIdn, boolean completeOncePreferredResolved, int maxNumConsolidation) {
        super(eventLoop);
        DnsQueryLifecycleObserverFactory dnsQueryLifecycleObserverFactory2;
        this.queryContextManager = new DnsQueryContextManager();
        this.queryTimeoutMillis = queryTimeoutMillis >= 0 ? queryTimeoutMillis : TimeUnit.SECONDS.toMillis(DEFAULT_OPTIONS.timeout());
        this.resolvedAddressTypes = resolvedAddressTypes != null ? resolvedAddressTypes : DEFAULT_RESOLVE_ADDRESS_TYPES;
        this.recursionDesired = recursionDesired;
        this.maxQueriesPerResolve = maxQueriesPerResolve > 0 ? maxQueriesPerResolve : DEFAULT_OPTIONS.attempts();
        this.maxPayloadSize = ObjectUtil.checkPositive(maxPayloadSize, "maxPayloadSize");
        this.optResourceEnabled = optResourceEnabled;
        this.hostsFileEntriesResolver = (HostsFileEntriesResolver) ObjectUtil.checkNotNull(hostsFileEntriesResolver, "hostsFileEntriesResolver");
        this.dnsServerAddressStreamProvider = (DnsServerAddressStreamProvider) ObjectUtil.checkNotNull(dnsServerAddressStreamProvider, "dnsServerAddressStreamProvider");
        this.queryDnsServerAddressStream = (DnsServerAddressStream) ObjectUtil.checkNotNull(queryDnsServerAddressStream, "queryDnsServerAddressStream");
        this.resolveCache = (DnsCache) ObjectUtil.checkNotNull(resolveCache, "resolveCache");
        this.cnameCache = (DnsCnameCache) ObjectUtil.checkNotNull(cnameCache, "cnameCache");
        if (traceEnabled) {
            dnsQueryLifecycleObserverFactory2 = dnsQueryLifecycleObserverFactory instanceof NoopDnsQueryLifecycleObserverFactory ? new LoggingDnsQueryLifeCycleObserverFactory() : new BiDnsQueryLifecycleObserverFactory(new LoggingDnsQueryLifeCycleObserverFactory(), dnsQueryLifecycleObserverFactory);
        } else {
            dnsQueryLifecycleObserverFactory2 = (DnsQueryLifecycleObserverFactory) ObjectUtil.checkNotNull(dnsQueryLifecycleObserverFactory, "dnsQueryLifecycleObserverFactory");
        }
        this.dnsQueryLifecycleObserverFactory = dnsQueryLifecycleObserverFactory2;
        this.searchDomains = searchDomains != null ? (String[]) searchDomains.clone() : DEFAULT_SEARCH_DOMAINS;
        this.ndots = ndots >= 0 ? ndots : DEFAULT_OPTIONS.ndots();
        this.decodeIdn = decodeIdn;
        this.completeOncePreferredResolved = completeOncePreferredResolved;
        this.retryWithTcpOnTimeout = retryWithTcpOnTimeout;
        if (socketChannelFactory == null) {
            this.socketBootstrap = null;
        } else {
            this.socketBootstrap = new Bootstrap();
            this.socketBootstrap.option(ChannelOption.SO_REUSEADDR, true).group(executor()).channelFactory((ChannelFactory) socketChannelFactory).attr(DNS_PIPELINE_ATTRIBUTE, Boolean.TRUE).handler(NOOP_HANDLER);
            if (queryTimeoutMillis > 0 && queryTimeoutMillis <= 2147483647L) {
                this.socketBootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, Integer.valueOf((int) queryTimeoutMillis));
            }
        }
        switch (this.resolvedAddressTypes) {
            case IPV4_ONLY:
                this.supportsAAAARecords = false;
                this.supportsARecords = true;
                this.resolveRecordTypes = IPV4_ONLY_RESOLVED_RECORD_TYPES;
                this.resolvedInternetProtocolFamilies = IPV4_ONLY_RESOLVED_PROTOCOL_FAMILIES;
                break;
            case IPV4_PREFERRED:
                this.supportsAAAARecords = true;
                this.supportsARecords = true;
                this.resolveRecordTypes = IPV4_PREFERRED_RESOLVED_RECORD_TYPES;
                this.resolvedInternetProtocolFamilies = IPV4_PREFERRED_RESOLVED_PROTOCOL_FAMILIES;
                break;
            case IPV6_ONLY:
                this.supportsAAAARecords = true;
                this.supportsARecords = false;
                this.resolveRecordTypes = IPV6_ONLY_RESOLVED_RECORD_TYPES;
                this.resolvedInternetProtocolFamilies = IPV6_ONLY_RESOLVED_PROTOCOL_FAMILIES;
                break;
            case IPV6_PREFERRED:
                this.supportsAAAARecords = true;
                this.supportsARecords = true;
                this.resolveRecordTypes = IPV6_PREFERRED_RESOLVED_RECORD_TYPES;
                this.resolvedInternetProtocolFamilies = IPV6_PREFERRED_RESOLVED_PROTOCOL_FAMILIES;
                break;
            default:
                throw new IllegalArgumentException("Unknown ResolvedAddressTypes " + resolvedAddressTypes);
        }
        this.preferredAddressType = preferredAddressType(this.resolvedAddressTypes);
        this.authoritativeDnsServerCache = (AuthoritativeDnsServerCache) ObjectUtil.checkNotNull(authoritativeDnsServerCache, "authoritativeDnsServerCache");
        this.nameServerComparator = new NameServerComparator(this.preferredAddressType.addressType());
        this.maxNumConsolidation = maxNumConsolidation;
        if (maxNumConsolidation > 0) {
            this.inflightLookups = new HashMap();
        } else {
            this.inflightLookups = null;
        }
        Bootstrap b = new Bootstrap().group(executor()).channelFactory((ChannelFactory) channelFactory).attr(DNS_PIPELINE_ATTRIBUTE, Boolean.TRUE);
        this.channelReadyPromise = executor().newPromise();
        final DnsResponseHandler responseHandler = new DnsResponseHandler(this.channelReadyPromise);
        b.handler(new ChannelInitializer<DatagramChannel>() { // from class: io.netty.resolver.dns.DnsNameResolver.3
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // io.netty.channel.ChannelInitializer
            public void initChannel(DatagramChannel ch) {
                ch.pipeline().addLast(DnsNameResolver.DATAGRAM_ENCODER, DnsNameResolver.DATAGRAM_DECODER, responseHandler);
            }
        });
        ChannelFuture future = b.bind(localAddress == null ? new InetSocketAddress(0) : localAddress);
        if (!future.isDone()) {
            future.addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.resolver.dns.DnsNameResolver.4
                @Override // io.netty.util.concurrent.GenericFutureListener
                public void operationComplete(ChannelFuture future2) {
                    Throwable cause = future2.cause();
                    if (cause != null) {
                        DnsNameResolver.this.channelReadyPromise.tryFailure(cause);
                    }
                }
            });
        } else {
            Throwable cause = future.cause();
            if (cause != null) {
                if (cause instanceof RuntimeException) {
                    throw ((RuntimeException) cause);
                }
                if (cause instanceof Error) {
                    throw ((Error) cause);
                }
                throw new IllegalStateException("Unable to create / register Channel", cause);
            }
        }
        this.ch = future.channel();
        this.ch.config().setRecvByteBufAllocator(new FixedRecvByteBufAllocator(maxPayloadSize));
        this.ch.closeFuture().addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.resolver.dns.DnsNameResolver.5
            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(ChannelFuture future2) {
                resolveCache.clear();
                cnameCache.clear();
                authoritativeDnsServerCache.clear();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static InternetProtocolFamily preferredAddressType(ResolvedAddressTypes resolvedAddressTypes) {
        switch (resolvedAddressTypes) {
            case IPV4_ONLY:
            case IPV4_PREFERRED:
                return InternetProtocolFamily.IPv4;
            case IPV6_ONLY:
            case IPV6_PREFERRED:
                return InternetProtocolFamily.IPv6;
            default:
                throw new IllegalArgumentException("Unknown ResolvedAddressTypes " + resolvedAddressTypes);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InetSocketAddress newRedirectServerAddress(InetAddress server) {
        return new InetSocketAddress(server, 53);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final DnsQueryLifecycleObserverFactory dnsQueryLifecycleObserverFactory() {
        return this.dnsQueryLifecycleObserverFactory;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public DnsServerAddressStream newRedirectDnsServerStream(String hostname, List<InetSocketAddress> nameservers) {
        DnsServerAddressStream cached = authoritativeDnsServerCache().get(hostname);
        if (cached == null || cached.size() == 0) {
            Collections.sort(nameservers, this.nameServerComparator);
            return new SequentialDnsServerAddressStream(nameservers, 0);
        }
        return cached;
    }

    public DnsCache resolveCache() {
        return this.resolveCache;
    }

    public DnsCnameCache cnameCache() {
        return this.cnameCache;
    }

    public AuthoritativeDnsServerCache authoritativeDnsServerCache() {
        return this.authoritativeDnsServerCache;
    }

    public long queryTimeoutMillis() {
        return this.queryTimeoutMillis;
    }

    public DnsServerAddressStream queryDnsServerAddressStream() {
        return this.queryDnsServerAddressStream;
    }

    public ResolvedAddressTypes resolvedAddressTypes() {
        return this.resolvedAddressTypes;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InternetProtocolFamily[] resolvedInternetProtocolFamiliesUnsafe() {
        return this.resolvedInternetProtocolFamilies;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final String[] searchDomains() {
        return this.searchDomains;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int ndots() {
        return this.ndots;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean supportsAAAARecords() {
        return this.supportsAAAARecords;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean supportsARecords() {
        return this.supportsARecords;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final InternetProtocolFamily preferredAddressType() {
        return this.preferredAddressType;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final DnsRecordType[] resolveRecordTypes() {
        return this.resolveRecordTypes;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean isDecodeIdn() {
        return this.decodeIdn;
    }

    public boolean isRecursionDesired() {
        return this.recursionDesired;
    }

    public int maxQueriesPerResolve() {
        return this.maxQueriesPerResolve;
    }

    public int maxPayloadSize() {
        return this.maxPayloadSize;
    }

    public boolean isOptResourceEnabled() {
        return this.optResourceEnabled;
    }

    public HostsFileEntriesResolver hostsFileEntriesResolver() {
        return this.hostsFileEntriesResolver;
    }

    @Override // io.netty.resolver.SimpleNameResolver, io.netty.resolver.NameResolver, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        if (this.ch.isOpen()) {
            this.ch.close();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.resolver.SimpleNameResolver
    public EventLoop executor() {
        return (EventLoop) super.executor();
    }

    private InetAddress resolveHostsFileEntry(String hostname) {
        if (this.hostsFileEntriesResolver == null) {
            return null;
        }
        InetAddress address = this.hostsFileEntriesResolver.address(hostname, this.resolvedAddressTypes);
        return (address == null && isLocalWindowsHost(hostname)) ? LOCALHOST_ADDRESS : address;
    }

    private List<InetAddress> resolveHostsFileEntries(String hostname) {
        List<InetAddress> addresses;
        if (this.hostsFileEntriesResolver == null) {
            return null;
        }
        if (this.hostsFileEntriesResolver instanceof DefaultHostsFileEntriesResolver) {
            addresses = ((DefaultHostsFileEntriesResolver) this.hostsFileEntriesResolver).addresses(hostname, this.resolvedAddressTypes);
        } else {
            InetAddress address = this.hostsFileEntriesResolver.address(hostname, this.resolvedAddressTypes);
            addresses = address != null ? Collections.singletonList(address) : null;
        }
        return (addresses == null && isLocalWindowsHost(hostname)) ? Collections.singletonList(LOCALHOST_ADDRESS) : addresses;
    }

    private static boolean isLocalWindowsHost(String hostname) {
        return PlatformDependent.isWindows() && (LOCALHOST.equalsIgnoreCase(hostname) || (WINDOWS_HOST_NAME != null && WINDOWS_HOST_NAME.equalsIgnoreCase(hostname)));
    }

    public final Future<InetAddress> resolve(String inetHost, Iterable<DnsRecord> additionals) {
        return resolve(inetHost, additionals, executor().newPromise());
    }

    public final Future<InetAddress> resolve(String inetHost, Iterable<DnsRecord> additionals, Promise<InetAddress> promise) {
        ObjectUtil.checkNotNull(promise, "promise");
        DnsRecord[] additionalsArray = toArray(additionals, true);
        try {
            doResolve(inetHost, additionalsArray, promise, this.resolveCache);
            return promise;
        } catch (Exception e) {
            return promise.setFailure(e);
        }
    }

    public final Future<List<InetAddress>> resolveAll(String inetHost, Iterable<DnsRecord> additionals) {
        return resolveAll(inetHost, additionals, executor().newPromise());
    }

    public final Future<List<InetAddress>> resolveAll(String inetHost, Iterable<DnsRecord> additionals, Promise<List<InetAddress>> promise) {
        ObjectUtil.checkNotNull(promise, "promise");
        DnsRecord[] additionalsArray = toArray(additionals, true);
        try {
            doResolveAll(inetHost, additionalsArray, promise, this.resolveCache);
            return promise;
        } catch (Exception e) {
            return promise.setFailure(e);
        }
    }

    @Override // io.netty.resolver.SimpleNameResolver
    protected void doResolve(String inetHost, Promise<InetAddress> promise) throws Exception {
        doResolve(inetHost, EMPTY_ADDITIONALS, promise, this.resolveCache);
    }

    public final Future<List<DnsRecord>> resolveAll(DnsQuestion question) {
        return resolveAll(question, EMPTY_ADDITIONALS, executor().newPromise());
    }

    public final Future<List<DnsRecord>> resolveAll(DnsQuestion question, Iterable<DnsRecord> additionals) {
        return resolveAll(question, additionals, executor().newPromise());
    }

    public final Future<List<DnsRecord>> resolveAll(DnsQuestion question, Iterable<DnsRecord> additionals, Promise<List<DnsRecord>> promise) {
        DnsRecord[] additionalsArray = toArray(additionals, true);
        return resolveAll(question, additionalsArray, promise);
    }

    private Future<List<DnsRecord>> resolveAll(DnsQuestion question, DnsRecord[] additionals, Promise<List<DnsRecord>> promise) {
        List<InetAddress> hostsFileEntries;
        ByteBuf content;
        List<InetAddress> hostsFileEntries2;
        ObjectUtil.checkNotNull(question, "question");
        ObjectUtil.checkNotNull(promise, "promise");
        DnsRecordType type = question.type();
        String hostname = question.name();
        if ((type == DnsRecordType.A || type == DnsRecordType.AAAA) && (hostsFileEntries = resolveHostsFileEntries(hostname)) != null) {
            List<DnsRecord> result = new ArrayList<>();
            for (InetAddress hostsFileEntry : hostsFileEntries) {
                if (hostsFileEntry instanceof Inet4Address) {
                    if (type == DnsRecordType.A) {
                        ByteBuf content2 = Unpooled.wrappedBuffer(hostsFileEntry.getAddress());
                        content = content2;
                    }
                    content = null;
                } else {
                    if ((hostsFileEntry instanceof Inet6Address) && type == DnsRecordType.AAAA) {
                        ByteBuf content3 = Unpooled.wrappedBuffer(hostsFileEntry.getAddress());
                        content = content3;
                    }
                    content = null;
                }
                if (content == null) {
                    hostsFileEntries2 = hostsFileEntries;
                } else {
                    hostsFileEntries2 = hostsFileEntries;
                    result.add(new DefaultDnsRawRecord(hostname, type, 86400L, content));
                }
                hostsFileEntries = hostsFileEntries2;
            }
            if (!result.isEmpty()) {
                if (!trySuccess(promise, result)) {
                    for (DnsRecord r : result) {
                        ReferenceCountUtil.safeRelease(r);
                    }
                }
                return promise;
            }
        }
        DnsServerAddressStream nameServerAddrs = this.dnsServerAddressStreamProvider.nameServerAddressStream(hostname);
        new DnsRecordResolveContext(this, this.ch, this.channelReadyPromise, promise, question, additionals, nameServerAddrs, this.maxQueriesPerResolve).resolve(promise);
        return promise;
    }

    private static DnsRecord[] toArray(Iterable<DnsRecord> additionals, boolean validateType) {
        ObjectUtil.checkNotNull(additionals, "additionals");
        if (additionals instanceof Collection) {
            Collection<DnsRecord> records = (Collection) additionals;
            Iterator<DnsRecord> it2 = additionals.iterator();
            while (it2.hasNext()) {
                validateAdditional(it2.next(), validateType);
            }
            return (DnsRecord[]) records.toArray(new DnsRecord[records.size()]);
        }
        Iterator<DnsRecord> additionalsIt = additionals.iterator();
        if (!additionalsIt.hasNext()) {
            return EMPTY_ADDITIONALS;
        }
        List<DnsRecord> records2 = new ArrayList<>();
        do {
            DnsRecord r = additionalsIt.next();
            validateAdditional(r, validateType);
            records2.add(r);
        } while (additionalsIt.hasNext());
        return (DnsRecord[]) records2.toArray(new DnsRecord[records2.size()]);
    }

    private static void validateAdditional(DnsRecord record, boolean validateType) {
        ObjectUtil.checkNotNull(record, "record");
        if (validateType && (record instanceof DnsRawRecord)) {
            throw new IllegalArgumentException("DnsRawRecord implementations not allowed: " + record);
        }
    }

    private InetAddress loopbackAddress() {
        return preferredAddressType().localhost();
    }

    protected void doResolve(String inetHost, DnsRecord[] additionals, Promise<InetAddress> promise, DnsCache resolveCache) throws Exception {
        if (inetHost == null || inetHost.isEmpty()) {
            promise.setSuccess(loopbackAddress());
            return;
        }
        InetAddress address = NetUtil.createInetAddressFromIpAddressString(inetHost);
        if (address != null) {
            promise.setSuccess(address);
            return;
        }
        String hostname = hostname(inetHost);
        InetAddress hostsFileEntry = resolveHostsFileEntry(hostname);
        if (hostsFileEntry != null) {
            promise.setSuccess(hostsFileEntry);
        } else if (!doResolveCached(hostname, additionals, promise, resolveCache)) {
            doResolveUncached(hostname, additionals, promise, resolveCache, this.completeOncePreferredResolved);
        }
    }

    private boolean doResolveCached(String hostname, DnsRecord[] additionals, Promise<InetAddress> promise, DnsCache resolveCache) {
        List<? extends DnsCacheEntry> cachedEntries = resolveCache.get(hostname, additionals);
        if (cachedEntries != null && !cachedEntries.isEmpty()) {
            Throwable cause = cachedEntries.get(0).cause();
            if (cause != null) {
                tryFailure(promise, cause);
                return true;
            }
            int numEntries = cachedEntries.size();
            for (InternetProtocolFamily f : this.resolvedInternetProtocolFamilies) {
                for (int i = 0; i < numEntries; i++) {
                    DnsCacheEntry e = cachedEntries.get(i);
                    if (f.addressType().isInstance(e.address())) {
                        trySuccess(promise, e.address());
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> boolean trySuccess(Promise<T> promise, T result) {
        boolean notifiedRecords = promise.trySuccess(result);
        if (!notifiedRecords) {
            logger.trace("Failed to notify success ({}) to a promise: {}", result, promise);
        }
        return notifiedRecords;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void tryFailure(Promise<?> promise, Throwable cause) {
        if (!promise.tryFailure(cause)) {
            logger.trace("Failed to notify failure to a promise: {}", promise, cause);
        }
    }

    private void doResolveUncached(String hostname, DnsRecord[] additionals, final Promise<InetAddress> promise, DnsCache resolveCache, boolean completeEarlyIfPossible) {
        Promise<List<InetAddress>> allPromise = executor().newPromise();
        doResolveAllUncached(hostname, additionals, promise, allPromise, resolveCache, completeEarlyIfPossible);
        allPromise.addListener((GenericFutureListener<? extends Future<? super List<InetAddress>>>) new FutureListener<List<InetAddress>>() { // from class: io.netty.resolver.dns.DnsNameResolver.6
            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(Future<List<InetAddress>> future) {
                if (!future.isSuccess()) {
                    DnsNameResolver.tryFailure(promise, future.cause());
                } else {
                    DnsNameResolver.trySuccess(promise, future.getNow().get(0));
                }
            }
        });
    }

    @Override // io.netty.resolver.SimpleNameResolver
    protected void doResolveAll(String inetHost, Promise<List<InetAddress>> promise) throws Exception {
        doResolveAll(inetHost, EMPTY_ADDITIONALS, promise, this.resolveCache);
    }

    protected void doResolveAll(String inetHost, DnsRecord[] additionals, Promise<List<InetAddress>> promise, DnsCache resolveCache) throws Exception {
        if (inetHost == null || inetHost.isEmpty()) {
            promise.setSuccess(Collections.singletonList(loopbackAddress()));
            return;
        }
        InetAddress address = NetUtil.createInetAddressFromIpAddressString(inetHost);
        if (address != null) {
            promise.setSuccess(Collections.singletonList(address));
            return;
        }
        String hostname = hostname(inetHost);
        List<InetAddress> hostsFileEntries = resolveHostsFileEntries(hostname);
        if (hostsFileEntries != null) {
            promise.setSuccess(hostsFileEntries);
        } else if (!doResolveAllCached(hostname, additionals, promise, resolveCache, this.resolvedInternetProtocolFamilies)) {
            doResolveAllUncached(hostname, additionals, promise, promise, resolveCache, this.completeOncePreferredResolved);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean doResolveAllCached(String hostname, DnsRecord[] additionals, Promise<List<InetAddress>> promise, DnsCache resolveCache, InternetProtocolFamily[] resolvedInternetProtocolFamilies) {
        List<? extends DnsCacheEntry> cachedEntries = resolveCache.get(hostname, additionals);
        if (cachedEntries == null || cachedEntries.isEmpty()) {
            return false;
        }
        Throwable cause = cachedEntries.get(0).cause();
        if (cause != null) {
            tryFailure(promise, cause);
            return true;
        }
        List<InetAddress> result = null;
        int numEntries = cachedEntries.size();
        for (InternetProtocolFamily f : resolvedInternetProtocolFamilies) {
            for (int i = 0; i < numEntries; i++) {
                DnsCacheEntry e = cachedEntries.get(i);
                if (f.addressType().isInstance(e.address())) {
                    if (result == null) {
                        result = new ArrayList<>(numEntries);
                    }
                    result.add(e.address());
                }
            }
        }
        if (result != null) {
            trySuccess(promise, result);
            return true;
        }
        return false;
    }

    private void doResolveAllUncached(final String hostname, final DnsRecord[] additionals, final Promise<?> originalPromise, final Promise<List<InetAddress>> promise, final DnsCache resolveCache, final boolean completeEarlyIfPossible) {
        EventExecutor executor = executor();
        if (executor.inEventLoop()) {
            doResolveAllUncached0(hostname, additionals, originalPromise, promise, resolveCache, completeEarlyIfPossible);
        } else {
            executor.execute(new Runnable() { // from class: io.netty.resolver.dns.DnsNameResolver.7
                @Override // java.lang.Runnable
                public void run() {
                    DnsNameResolver.this.doResolveAllUncached0(hostname, additionals, originalPromise, promise, resolveCache, completeEarlyIfPossible);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doResolveAllUncached0(final String hostname, final DnsRecord[] additionals, final Promise<?> originalPromise, final Promise<List<InetAddress>> promise, final DnsCache resolveCache, final boolean completeEarlyIfPossible) {
        if (!executor().inEventLoop()) {
            throw new AssertionError();
        }
        if (this.inflightLookups != null && (additionals == null || additionals.length == 0)) {
            Future<List<InetAddress>> inflightFuture = this.inflightLookups.get(hostname);
            if (inflightFuture == null) {
                if (this.inflightLookups.size() < this.maxNumConsolidation) {
                    this.inflightLookups.put(hostname, promise);
                    promise.addListener((GenericFutureListener<? extends Future<? super List<InetAddress>>>) new GenericFutureListener<Future<? super List<InetAddress>>>() { // from class: io.netty.resolver.dns.DnsNameResolver.9
                        @Override // io.netty.util.concurrent.GenericFutureListener
                        public void operationComplete(Future<? super List<InetAddress>> future) {
                            DnsNameResolver.this.inflightLookups.remove(hostname);
                        }
                    });
                }
            } else {
                inflightFuture.addListener(new GenericFutureListener<Future<? super List<InetAddress>>>() { // from class: io.netty.resolver.dns.DnsNameResolver.8
                    @Override // io.netty.util.concurrent.GenericFutureListener
                    public void operationComplete(Future<? super List<InetAddress>> future) {
                        if (future.isSuccess()) {
                            promise.setSuccess(future.getNow());
                            return;
                        }
                        Throwable cause = future.cause();
                        if (DnsNameResolver.isTimeoutError(cause)) {
                            DnsNameResolver.this.resolveNow(hostname, additionals, originalPromise, promise, resolveCache, completeEarlyIfPossible);
                        } else {
                            promise.setFailure(cause);
                        }
                    }
                });
                return;
            }
        }
        resolveNow(hostname, additionals, originalPromise, promise, resolveCache, completeEarlyIfPossible);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resolveNow(String hostname, DnsRecord[] additionals, Promise<?> originalPromise, Promise<List<InetAddress>> promise, DnsCache resolveCache, boolean completeEarlyIfPossible) {
        DnsServerAddressStream nameServerAddrs = this.dnsServerAddressStreamProvider.nameServerAddressStream(hostname);
        DnsAddressResolveContext ctx = new DnsAddressResolveContext(this, this.ch, this.channelReadyPromise, originalPromise, hostname, additionals, nameServerAddrs, this.maxQueriesPerResolve, resolveCache, this.authoritativeDnsServerCache, completeEarlyIfPossible);
        ctx.resolve(promise);
    }

    private static String hostname(String inetHost) {
        String hostname = IDN.toASCII(inetHost);
        if (StringUtil.endsWith(inetHost, '.') && !StringUtil.endsWith(hostname, '.')) {
            return hostname + ".";
        }
        return hostname;
    }

    public Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query(DnsQuestion question) {
        return query(nextNameServerAddress(), question);
    }

    public Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query(DnsQuestion question, Iterable<DnsRecord> additionals) {
        return query(nextNameServerAddress(), question, additionals);
    }

    public Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query(DnsQuestion question, Promise<AddressedEnvelope<? extends DnsResponse, InetSocketAddress>> promise) {
        return query(nextNameServerAddress(), question, Collections.emptyList(), promise);
    }

    private InetSocketAddress nextNameServerAddress() {
        return this.queryDnsServerAddressStream.next();
    }

    public Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query(InetSocketAddress nameServerAddr, DnsQuestion question) {
        return doQuery(this.ch, this.channelReadyPromise, nameServerAddr, question, NoopDnsQueryLifecycleObserver.INSTANCE, EMPTY_ADDITIONALS, true, this.ch.eventLoop().newPromise());
    }

    public Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query(InetSocketAddress nameServerAddr, DnsQuestion question, Iterable<DnsRecord> additionals) {
        return doQuery(this.ch, this.channelReadyPromise, nameServerAddr, question, NoopDnsQueryLifecycleObserver.INSTANCE, toArray(additionals, false), true, this.ch.eventLoop().newPromise());
    }

    public Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query(InetSocketAddress nameServerAddr, DnsQuestion question, Promise<AddressedEnvelope<? extends DnsResponse, InetSocketAddress>> promise) {
        return doQuery(this.ch, this.channelReadyPromise, nameServerAddr, question, NoopDnsQueryLifecycleObserver.INSTANCE, EMPTY_ADDITIONALS, true, promise);
    }

    public Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query(InetSocketAddress nameServerAddr, DnsQuestion question, Iterable<DnsRecord> additionals, Promise<AddressedEnvelope<? extends DnsResponse, InetSocketAddress>> promise) {
        return doQuery(this.ch, this.channelReadyPromise, nameServerAddr, question, NoopDnsQueryLifecycleObserver.INSTANCE, toArray(additionals, false), true, promise);
    }

    public static boolean isTransportOrTimeoutError(Throwable cause) {
        return cause != null && (cause.getCause() instanceof DnsNameResolverException);
    }

    public static boolean isTimeoutError(Throwable cause) {
        return cause != null && (cause.getCause() instanceof DnsNameResolverTimeoutException);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> doQuery(Channel channel, Future<? extends Channel> channelReadyFuture, InetSocketAddress nameServerAddr, DnsQuestion question, DnsQueryLifecycleObserver queryLifecycleObserver, DnsRecord[] additionals, boolean flush, Promise<AddressedEnvelope<? extends DnsResponse, InetSocketAddress>> promise) {
        Promise<AddressedEnvelope<DnsResponse, InetSocketAddress>> castPromise;
        Promise<AddressedEnvelope<DnsResponse, InetSocketAddress>> castPromise2 = cast((Promise) ObjectUtil.checkNotNull(promise, "promise"));
        int payloadSize = isOptResourceEnabled() ? maxPayloadSize() : 0;
        try {
            castPromise = castPromise2;
            try {
                DnsQueryContext queryContext = new DatagramDnsQueryContext(channel, channelReadyFuture, nameServerAddr, this.queryContextManager, payloadSize, isRecursionDesired(), queryTimeoutMillis(), question, additionals, castPromise2, this.socketBootstrap, this.retryWithTcpOnTimeout);
                try {
                    ChannelFuture future = queryContext.writeQuery(flush);
                    try {
                        queryLifecycleObserver.queryWritten(nameServerAddr, future);
                        return castPromise;
                    } catch (Exception e) {
                        e = e;
                        return castPromise.setFailure(e);
                    }
                } catch (Exception e2) {
                    e = e2;
                }
            } catch (Exception e3) {
                e = e3;
            }
        } catch (Exception e4) {
            e = e4;
            castPromise = castPromise2;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static Promise<AddressedEnvelope<DnsResponse, InetSocketAddress>> cast(Promise<?> promise) {
        return promise;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final DnsServerAddressStream newNameServerAddressStream(String hostname) {
        return this.dnsServerAddressStreamProvider.nameServerAddressStream(hostname);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class DnsResponseHandler extends ChannelInboundHandlerAdapter {
        private final Promise<Channel> channelActivePromise;

        DnsResponseHandler(Promise<Channel> channelActivePromise) {
            this.channelActivePromise = channelActivePromise;
        }

        @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            Channel qCh = ctx.channel();
            DatagramDnsResponse res = (DatagramDnsResponse) msg;
            int queryId = res.id();
            DnsNameResolver.logger.debug("{} RECEIVED: UDP [{}: {}], {}", qCh, Integer.valueOf(queryId), res.sender(), res);
            DnsQueryContext qCtx = DnsNameResolver.this.queryContextManager.get(res.sender(), queryId);
            if (qCtx == null) {
                DnsNameResolver.logger.debug("{} Received a DNS response with an unknown ID: UDP [{}: {}]", qCh, Integer.valueOf(queryId), res.sender());
                res.release();
            } else if (qCtx.isDone()) {
                DnsNameResolver.logger.debug("{} Received a DNS response for a query that was timed out or cancelled: UDP [{}: {}]", qCh, Integer.valueOf(queryId), res.sender());
                res.release();
            } else {
                qCtx.finishSuccess(res, res.isTruncated());
            }
        }

        @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            this.channelActivePromise.trySuccess(ctx.channel());
        }

        @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler, io.netty.channel.ChannelInboundHandler
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            if (cause instanceof CorruptedFrameException) {
                DnsNameResolver.logger.debug("{} Unable to decode DNS response: UDP", ctx.channel(), cause);
            } else {
                DnsNameResolver.logger.warn("{} Unexpected exception: UDP", ctx.channel(), cause);
            }
        }
    }
}
