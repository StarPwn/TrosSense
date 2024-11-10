package io.netty.resolver.dns;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.AddressedEnvelope;
import io.netty.channel.Channel;
import io.netty.channel.EventLoop;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.handler.codec.dns.DefaultDnsQuestion;
import io.netty.handler.codec.dns.DefaultDnsRecordDecoder;
import io.netty.handler.codec.dns.DnsQuestion;
import io.netty.handler.codec.dns.DnsRawRecord;
import io.netty.handler.codec.dns.DnsRecord;
import io.netty.handler.codec.dns.DnsRecordType;
import io.netty.handler.codec.dns.DnsResponse;
import io.netty.handler.codec.dns.DnsResponseCode;
import io.netty.handler.codec.dns.DnsSection;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.ThrowableUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public abstract class DnsResolveContext<T> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final RuntimeException CNAME_NOT_FOUND_QUERY_FAILED_EXCEPTION;
    private static final RuntimeException NAME_SERVERS_EXHAUSTED_EXCEPTION;
    private static final RuntimeException NO_MATCHING_RECORD_QUERY_FAILED_EXCEPTION;
    private static final RuntimeException NXDOMAIN_CAUSE_QUERY_FAILED_EXCEPTION;
    private static final RuntimeException NXDOMAIN_QUERY_FAILED_EXCEPTION;
    private static final RuntimeException SERVFAIL_QUERY_FAILED_EXCEPTION;
    private static final RuntimeException UNRECOGNIZED_TYPE_QUERY_FAILED_EXCEPTION;
    final DnsRecord[] additionals;
    private int allowedQueries;
    private final Channel channel;
    private final Future<? extends Channel> channelReadyFuture;
    private boolean completeEarly;
    private final int dnsClass;
    private final DnsRecordType[] expectedTypes;
    private List<T> finalResult;
    private final String hostname;
    private final DnsServerAddressStream nameServerAddrs;
    private final Promise<?> originalPromise;
    final DnsNameResolver parent;
    private final Set<Future<AddressedEnvelope<DnsResponse, InetSocketAddress>>> queriesInProgress = Collections.newSetFromMap(new IdentityHashMap());
    private boolean triedCNAME;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) DnsResolveContext.class);
    private static final String PROP_TRY_FINAL_CNAME_ON_ADDRESS_LOOKUPS = "io.netty.resolver.dns.tryCnameOnAddressLookups";
    static boolean TRY_FINAL_CNAME_ON_ADDRESS_LOOKUPS = SystemPropertyUtil.getBoolean(PROP_TRY_FINAL_CNAME_ON_ADDRESS_LOOKUPS, false);

    abstract void cache(String str, DnsRecord[] dnsRecordArr, DnsRecord dnsRecord, T t);

    abstract void cache(String str, DnsRecord[] dnsRecordArr, UnknownHostException unknownHostException);

    abstract T convertRecord(DnsRecord dnsRecord, String str, DnsRecord[] dnsRecordArr, EventLoop eventLoop);

    abstract List<T> filterResults(List<T> list);

    abstract boolean isCompleteEarly(T t);

    abstract boolean isDuplicateAllowed();

    abstract DnsResolveContext<T> newResolverContext(DnsNameResolver dnsNameResolver, Channel channel, Future<? extends Channel> future, Promise<?> promise, String str, int i, DnsRecordType[] dnsRecordTypeArr, DnsRecord[] dnsRecordArr, DnsServerAddressStream dnsServerAddressStream, int i2);

    static {
        if (logger.isDebugEnabled()) {
            logger.debug("-D{}: {}", PROP_TRY_FINAL_CNAME_ON_ADDRESS_LOOKUPS, Boolean.valueOf(TRY_FINAL_CNAME_ON_ADDRESS_LOOKUPS));
        }
        NXDOMAIN_QUERY_FAILED_EXCEPTION = DnsResolveContextException.newStatic("No answer found and NXDOMAIN response code returned", DnsResolveContext.class, "onResponse(..)");
        CNAME_NOT_FOUND_QUERY_FAILED_EXCEPTION = DnsResolveContextException.newStatic("No matching CNAME record found", DnsResolveContext.class, "onResponseCNAME(..)");
        NO_MATCHING_RECORD_QUERY_FAILED_EXCEPTION = DnsResolveContextException.newStatic("No matching record type found", DnsResolveContext.class, "onResponseAorAAAA(..)");
        UNRECOGNIZED_TYPE_QUERY_FAILED_EXCEPTION = DnsResolveContextException.newStatic("Response type was unrecognized", DnsResolveContext.class, "onResponse(..)");
        NAME_SERVERS_EXHAUSTED_EXCEPTION = DnsResolveContextException.newStatic("No name servers returned an answer", DnsResolveContext.class, "tryToFinishResolve(..)");
        SERVFAIL_QUERY_FAILED_EXCEPTION = DnsErrorCauseException.newStatic("Query failed with SERVFAIL", DnsResponseCode.SERVFAIL, DnsResolveContext.class, "onResponse(..)");
        NXDOMAIN_CAUSE_QUERY_FAILED_EXCEPTION = DnsErrorCauseException.newStatic("Query failed with NXDOMAIN", DnsResponseCode.NXDOMAIN, DnsResolveContext.class, "onResponse(..)");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DnsResolveContext(DnsNameResolver parent, Channel channel, Future<? extends Channel> channelReadyFuture, Promise<?> originalPromise, String hostname, int dnsClass, DnsRecordType[] expectedTypes, DnsRecord[] additionals, DnsServerAddressStream nameServerAddrs, int allowedQueries) {
        if (expectedTypes.length <= 0) {
            throw new AssertionError();
        }
        this.parent = parent;
        this.channel = channel;
        this.channelReadyFuture = channelReadyFuture;
        this.originalPromise = originalPromise;
        this.hostname = hostname;
        this.dnsClass = dnsClass;
        this.expectedTypes = expectedTypes;
        this.additionals = additionals;
        this.nameServerAddrs = (DnsServerAddressStream) ObjectUtil.checkNotNull(nameServerAddrs, "nameServerAddrs");
        this.allowedQueries = allowedQueries;
    }

    /* loaded from: classes4.dex */
    static final class DnsResolveContextException extends RuntimeException {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private static final long serialVersionUID = 1209303419266433003L;

        private DnsResolveContextException(String message) {
            super(message);
        }

        private DnsResolveContextException(String message, boolean shared) {
            super(message, null, false, true);
            if (!shared) {
                throw new AssertionError();
            }
        }

        @Override // java.lang.Throwable
        public Throwable fillInStackTrace() {
            return this;
        }

        static DnsResolveContextException newStatic(String message, Class<?> clazz, String method) {
            DnsResolveContextException exception;
            if (PlatformDependent.javaVersion() >= 7) {
                exception = new DnsResolveContextException(message, true);
            } else {
                exception = new DnsResolveContextException(message);
            }
            return (DnsResolveContextException) ThrowableUtil.unknownStackTrace(exception, clazz, method);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Channel channel() {
        return this.channel;
    }

    DnsCache resolveCache() {
        return this.parent.resolveCache();
    }

    DnsCnameCache cnameCache() {
        return this.parent.cnameCache();
    }

    AuthoritativeDnsServerCache authoritativeDnsServerCache() {
        return this.parent.authoritativeDnsServerCache();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resolve(final Promise<List<T>> promise) {
        final String[] searchDomains = this.parent.searchDomains();
        if (searchDomains.length == 0 || this.parent.ndots() == 0 || StringUtil.endsWith(this.hostname, '.')) {
            internalResolve(this.hostname, promise);
            return;
        }
        final boolean hasNDots = hasNDots();
        String str = hasNDots ? this.hostname : this.hostname + '.' + searchDomains[0];
        final int i = !hasNDots ? 1 : 0;
        Promise<List<T>> newPromise = this.parent.executor().newPromise();
        newPromise.addListener((GenericFutureListener<? extends Future<? super List<T>>>) new FutureListener<List<T>>() { // from class: io.netty.resolver.dns.DnsResolveContext.1
            private int searchDomainIdx;

            {
                this.searchDomainIdx = i;
            }

            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(Future<List<T>> future) {
                Throwable cause = future.cause();
                if (cause == null) {
                    List<T> result = future.getNow();
                    if (!promise.trySuccess(result)) {
                        for (T item : result) {
                            ReferenceCountUtil.safeRelease(item);
                        }
                        return;
                    }
                    return;
                }
                if (DnsNameResolver.isTransportOrTimeoutError(cause)) {
                    promise.tryFailure(new SearchDomainUnknownHostException(cause, DnsResolveContext.this.hostname, DnsResolveContext.this.expectedTypes, searchDomains));
                    return;
                }
                if (this.searchDomainIdx >= searchDomains.length) {
                    if (!hasNDots) {
                        DnsResolveContext.this.internalResolve(DnsResolveContext.this.hostname, promise);
                        return;
                    } else {
                        promise.tryFailure(new SearchDomainUnknownHostException(cause, DnsResolveContext.this.hostname, DnsResolveContext.this.expectedTypes, searchDomains));
                        return;
                    }
                }
                Promise<List<T>> newPromise2 = DnsResolveContext.this.parent.executor().newPromise();
                newPromise2.addListener((GenericFutureListener<? extends Future<? super List<T>>>) this);
                DnsResolveContext dnsResolveContext = DnsResolveContext.this;
                StringBuilder append = new StringBuilder().append(DnsResolveContext.this.hostname).append('.');
                String[] strArr = searchDomains;
                int i2 = this.searchDomainIdx;
                this.searchDomainIdx = i2 + 1;
                dnsResolveContext.doSearchDomainQuery(append.append(strArr[i2]).toString(), newPromise2);
            }
        });
        doSearchDomainQuery(str, newPromise);
    }

    private boolean hasNDots() {
        int dots = 0;
        for (int idx = this.hostname.length() - 1; idx >= 0; idx--) {
            if (this.hostname.charAt(idx) == '.' && (dots = dots + 1) >= this.parent.ndots()) {
                return true;
            }
        }
        return false;
    }

    /* loaded from: classes4.dex */
    private static final class SearchDomainUnknownHostException extends UnknownHostException {
        private static final long serialVersionUID = -8573510133644997085L;

        SearchDomainUnknownHostException(Throwable cause, String originalHostname, DnsRecordType[] queryTypes, String[] searchDomains) {
            super("Failed to resolve '" + originalHostname + "' " + Arrays.toString(queryTypes) + " and search domain query for configured domains failed as well: " + Arrays.toString(searchDomains));
            setStackTrace(cause.getStackTrace());
            initCause(cause.getCause());
        }

        @Override // java.lang.Throwable
        public Throwable fillInStackTrace() {
            return this;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void doSearchDomainQuery(String hostname, Promise<List<T>> nextPromise) {
        DnsResolveContext<T> nextContext = newResolverContext(this.parent, this.channel, this.channelReadyFuture, this.originalPromise, hostname, this.dnsClass, this.expectedTypes, this.additionals, this.nameServerAddrs, this.parent.maxQueriesPerResolve());
        nextContext.internalResolve(hostname, nextPromise);
    }

    private static String hostnameWithDot(String name) {
        if (!StringUtil.endsWith(name, '.')) {
            return name + '.';
        }
        return name;
    }

    static String cnameResolveFromCache(DnsCnameCache cnameCache, String name) throws UnknownHostException {
        String first = cnameCache.get(hostnameWithDot(name));
        if (first == null) {
            return name;
        }
        String second = cnameCache.get(hostnameWithDot(first));
        if (second == null) {
            return first;
        }
        checkCnameLoop(name, first, second);
        return cnameResolveFromCacheLoop(cnameCache, name, first, second);
    }

    private static String cnameResolveFromCacheLoop(DnsCnameCache cnameCache, String hostname, String first, String mapping) throws UnknownHostException {
        boolean advance = false;
        String name = mapping;
        while (true) {
            String mapping2 = cnameCache.get(hostnameWithDot(name));
            if (mapping2 != null) {
                checkCnameLoop(hostname, first, mapping2);
                name = mapping2;
                if (advance) {
                    first = cnameCache.get(first);
                }
                advance = !advance;
            } else {
                return name;
            }
        }
    }

    private static void checkCnameLoop(String hostname, String first, String second) throws UnknownHostException {
        if (first.equals(second)) {
            throw new UnknownHostException("CNAME loop detected for '" + hostname + '\'');
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void internalResolve(String name, Promise<List<T>> promise) {
        try {
            String name2 = cnameResolveFromCache(cnameCache(), name);
            try {
                DnsServerAddressStream nameServerAddressStream = getNameServers(name2);
                int end = this.expectedTypes.length - 1;
                for (int i = 0; i < end; i++) {
                    if (!query(name2, this.expectedTypes[i], nameServerAddressStream.duplicate(), false, promise)) {
                        return;
                    }
                }
                query(name2, this.expectedTypes[end], nameServerAddressStream, false, promise);
            } finally {
                this.channel.flush();
            }
        } catch (Throwable cause) {
            promise.tryFailure(cause);
        }
    }

    private DnsServerAddressStream getNameServersFromCache(String hostname) {
        DnsServerAddressStream entries;
        int len = hostname.length();
        if (len == 0) {
            return null;
        }
        if (hostname.charAt(len - 1) != '.') {
            hostname = hostname + ".";
        }
        int idx = hostname.indexOf(46);
        if (idx == hostname.length() - 1) {
            return null;
        }
        do {
            hostname = hostname.substring(idx + 1);
            int idx2 = hostname.indexOf(46);
            if (idx2 <= 0 || idx2 == hostname.length() - 1) {
                return null;
            }
            idx = idx2;
            entries = authoritativeDnsServerCache().get(hostname);
        } while (entries == null);
        return entries;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void query(final DnsServerAddressStream nameServerAddrStream, final int nameServerAddrStreamIndex, final DnsQuestion question, final DnsQueryLifecycleObserver queryLifecycleObserver, boolean flush, final Promise<List<T>> promise, Throwable cause) {
        long queryStartTimeNanos;
        boolean isFeedbackAddressStream;
        if (!this.completeEarly && nameServerAddrStreamIndex < nameServerAddrStream.size() && this.allowedQueries != 0) {
            if (!this.originalPromise.isCancelled() && !promise.isCancelled()) {
                this.allowedQueries--;
                final InetSocketAddress nameServerAddr = nameServerAddrStream.next();
                if (nameServerAddr.isUnresolved()) {
                    queryUnresolvedNameServer(nameServerAddr, nameServerAddrStream, nameServerAddrStreamIndex, question, queryLifecycleObserver, promise, cause);
                    return;
                }
                Promise<AddressedEnvelope<? extends DnsResponse, InetSocketAddress>> queryPromise = this.channel.eventLoop().newPromise();
                if (nameServerAddrStream instanceof DnsServerResponseFeedbackAddressStream) {
                    long queryStartTimeNanos2 = System.nanoTime();
                    queryStartTimeNanos = queryStartTimeNanos2;
                    isFeedbackAddressStream = true;
                } else {
                    queryStartTimeNanos = -1;
                    isFeedbackAddressStream = false;
                }
                Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> f = this.parent.doQuery(this.channel, this.channelReadyFuture, nameServerAddr, question, queryLifecycleObserver, this.additionals, flush, queryPromise);
                this.queriesInProgress.add(f);
                final boolean z = isFeedbackAddressStream;
                final long j = queryStartTimeNanos;
                f.addListener(new FutureListener<AddressedEnvelope<DnsResponse, InetSocketAddress>>() { // from class: io.netty.resolver.dns.DnsResolveContext.2
                    @Override // io.netty.util.concurrent.GenericFutureListener
                    public void operationComplete(Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> future) {
                        DnsResolveContext.this.queriesInProgress.remove(future);
                        if (promise.isDone() || future.isCancelled()) {
                            queryLifecycleObserver.queryCancelled(DnsResolveContext.this.allowedQueries);
                            AddressedEnvelope<DnsResponse, InetSocketAddress> result = future.getNow();
                            if (result != null) {
                                result.release();
                                return;
                            }
                            return;
                        }
                        Throwable queryCause = future.cause();
                        try {
                            if (queryCause == null) {
                                if (z) {
                                    DnsServerResponseFeedbackAddressStream feedbackNameServerAddrStream = (DnsServerResponseFeedbackAddressStream) nameServerAddrStream;
                                    feedbackNameServerAddrStream.feedbackSuccess(nameServerAddr, System.nanoTime() - j);
                                }
                                DnsResolveContext.this.onResponse(nameServerAddrStream, nameServerAddrStreamIndex, question, future.getNow(), queryLifecycleObserver, promise);
                            } else {
                                if (z) {
                                    DnsServerResponseFeedbackAddressStream feedbackNameServerAddrStream2 = (DnsServerResponseFeedbackAddressStream) nameServerAddrStream;
                                    feedbackNameServerAddrStream2.feedbackFailure(nameServerAddr, queryCause, System.nanoTime() - j);
                                }
                                queryLifecycleObserver.queryFailed(queryCause);
                                DnsResolveContext.this.query(nameServerAddrStream, nameServerAddrStreamIndex + 1, question, DnsResolveContext.this.newDnsQueryLifecycleObserver(question), true, promise, queryCause);
                            }
                        } finally {
                            DnsResolveContext.this.tryToFinishResolve(nameServerAddrStream, nameServerAddrStreamIndex, question, NoopDnsQueryLifecycleObserver.INSTANCE, promise, queryCause);
                        }
                    }
                });
                return;
            }
        }
        tryToFinishResolve(nameServerAddrStream, nameServerAddrStreamIndex, question, queryLifecycleObserver, promise, cause);
    }

    private void queryUnresolvedNameServer(final InetSocketAddress nameServerAddr, final DnsServerAddressStream nameServerAddrStream, final int nameServerAddrStreamIndex, final DnsQuestion question, final DnsQueryLifecycleObserver queryLifecycleObserver, final Promise<List<T>> promise, final Throwable cause) {
        String nameServerName = PlatformDependent.javaVersion() >= 7 ? nameServerAddr.getHostString() : nameServerAddr.getHostName();
        if (nameServerName == null) {
            throw new AssertionError();
        }
        final Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> resolveFuture = this.parent.executor().newSucceededFuture(null);
        this.queriesInProgress.add(resolveFuture);
        Promise<List<T>> newPromise = this.parent.executor().newPromise();
        newPromise.addListener((GenericFutureListener<? extends Future<? super List<T>>>) new FutureListener<List<InetAddress>>() { // from class: io.netty.resolver.dns.DnsResolveContext.3
            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(Future<List<InetAddress>> future) {
                DnsResolveContext.this.queriesInProgress.remove(resolveFuture);
                if (!future.isSuccess()) {
                    DnsResolveContext.this.query(nameServerAddrStream, nameServerAddrStreamIndex + 1, question, queryLifecycleObserver, true, promise, cause);
                    return;
                }
                List<InetAddress> resolvedAddresses = future.getNow();
                DnsServerAddressStream addressStream = new CombinedDnsServerAddressStream(nameServerAddr, resolvedAddresses, nameServerAddrStream);
                DnsResolveContext.this.query(addressStream, nameServerAddrStreamIndex, question, queryLifecycleObserver, true, promise, cause);
            }
        });
        DnsCache resolveCache = resolveCache();
        if (!DnsNameResolver.doResolveAllCached(nameServerName, this.additionals, newPromise, resolveCache, this.parent.resolvedInternetProtocolFamiliesUnsafe())) {
            new DnsAddressResolveContext(this.parent, this.channel, this.channelReadyFuture, this.originalPromise, nameServerName, this.additionals, this.parent.newNameServerAddressStream(nameServerName), this.allowedQueries, resolveCache, redirectAuthoritativeDnsServerCache(authoritativeDnsServerCache()), false).resolve(newPromise);
        }
    }

    private static AuthoritativeDnsServerCache redirectAuthoritativeDnsServerCache(AuthoritativeDnsServerCache authoritativeDnsServerCache) {
        if (authoritativeDnsServerCache instanceof RedirectAuthoritativeDnsServerCache) {
            return authoritativeDnsServerCache;
        }
        return new RedirectAuthoritativeDnsServerCache(authoritativeDnsServerCache);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class RedirectAuthoritativeDnsServerCache implements AuthoritativeDnsServerCache {
        private final AuthoritativeDnsServerCache wrapped;

        RedirectAuthoritativeDnsServerCache(AuthoritativeDnsServerCache authoritativeDnsServerCache) {
            this.wrapped = authoritativeDnsServerCache;
        }

        @Override // io.netty.resolver.dns.AuthoritativeDnsServerCache
        public DnsServerAddressStream get(String hostname) {
            return null;
        }

        @Override // io.netty.resolver.dns.AuthoritativeDnsServerCache
        public void cache(String hostname, InetSocketAddress address, long originalTtl, EventLoop loop) {
            this.wrapped.cache(hostname, address, originalTtl, loop);
        }

        @Override // io.netty.resolver.dns.AuthoritativeDnsServerCache
        public void clear() {
            this.wrapped.clear();
        }

        @Override // io.netty.resolver.dns.AuthoritativeDnsServerCache
        public boolean clear(String hostname) {
            return this.wrapped.clear(hostname);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onResponse(DnsServerAddressStream nameServerAddrStream, int nameServerAddrStreamIndex, DnsQuestion question, AddressedEnvelope<DnsResponse, InetSocketAddress> envelope, DnsQueryLifecycleObserver queryLifecycleObserver, Promise<List<T>> promise) {
        try {
            DnsResponse res = envelope.content();
            DnsResponseCode code = res.code();
            if (code != DnsResponseCode.NOERROR) {
                if (code != DnsResponseCode.NXDOMAIN) {
                    query(nameServerAddrStream, nameServerAddrStreamIndex + 1, question, queryLifecycleObserver.queryNoAnswer(code), true, promise, cause(code));
                } else {
                    queryLifecycleObserver.queryFailed(NXDOMAIN_QUERY_FAILED_EXCEPTION);
                    if (res.isAuthoritativeAnswer()) {
                        tryToFinishResolve(nameServerAddrStream, nameServerAddrStreamIndex, question, queryLifecycleObserver, promise, NXDOMAIN_CAUSE_QUERY_FAILED_EXCEPTION);
                    } else {
                        query(nameServerAddrStream, nameServerAddrStreamIndex + 1, question, newDnsQueryLifecycleObserver(question), true, promise, cause(code));
                    }
                }
                return;
            }
            if (handleRedirect(question, envelope, queryLifecycleObserver, promise)) {
                return;
            }
            DnsRecordType type = question.type();
            if (type == DnsRecordType.CNAME) {
                onResponseCNAME(question, buildAliasMap(envelope.content(), cnameCache(), this.parent.executor()), queryLifecycleObserver, promise);
                return;
            }
            for (DnsRecordType expectedType : this.expectedTypes) {
                if (type == expectedType) {
                    onExpectedResponse(question, envelope, queryLifecycleObserver, promise);
                    return;
                }
            }
            queryLifecycleObserver.queryFailed(UNRECOGNIZED_TYPE_QUERY_FAILED_EXCEPTION);
        } finally {
            ReferenceCountUtil.safeRelease(envelope);
        }
    }

    private boolean handleRedirect(DnsQuestion question, AddressedEnvelope<DnsResponse, InetSocketAddress> envelope, DnsQueryLifecycleObserver queryLifecycleObserver, Promise<List<T>> promise) {
        AuthoritativeNameServerList serverNames;
        DnsResponse res = envelope.content();
        if (res.count(DnsSection.ANSWER) == 0 && (serverNames = extractAuthoritativeNameServers(question.name(), res)) != null) {
            int additionalCount = res.count(DnsSection.ADDITIONAL);
            AuthoritativeDnsServerCache authoritativeDnsServerCache = authoritativeDnsServerCache();
            for (int i = 0; i < additionalCount; i++) {
                DnsRecord r = res.recordAt(DnsSection.ADDITIONAL, i);
                if ((r.type() != DnsRecordType.A || this.parent.supportsARecords()) && (r.type() != DnsRecordType.AAAA || this.parent.supportsAAAARecords())) {
                    serverNames.handleWithAdditional(this.parent, r, authoritativeDnsServerCache);
                }
            }
            serverNames.handleWithoutAdditionals(this.parent, resolveCache(), authoritativeDnsServerCache);
            List<InetSocketAddress> addresses = serverNames.addressList();
            DnsServerAddressStream serverStream = this.parent.newRedirectDnsServerStream(question.name(), addresses);
            if (serverStream != null) {
                query(serverStream, 0, question, queryLifecycleObserver.queryRedirected(new DnsAddressStreamList(serverStream)), true, promise, null);
                return true;
            }
            return false;
        }
        return false;
    }

    private static Throwable cause(DnsResponseCode code) {
        if (code == null) {
            throw new AssertionError();
        }
        if (DnsResponseCode.SERVFAIL.intValue() == code.intValue()) {
            return SERVFAIL_QUERY_FAILED_EXCEPTION;
        }
        if (DnsResponseCode.NXDOMAIN.intValue() == code.intValue()) {
            return NXDOMAIN_CAUSE_QUERY_FAILED_EXCEPTION;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class DnsAddressStreamList extends AbstractList<InetSocketAddress> {
        private List<InetSocketAddress> addresses;
        private final DnsServerAddressStream duplicate;

        DnsAddressStreamList(DnsServerAddressStream stream) {
            this.duplicate = stream.duplicate();
        }

        @Override // java.util.AbstractList, java.util.List
        public InetSocketAddress get(int index) {
            if (this.addresses == null) {
                DnsServerAddressStream stream = this.duplicate.duplicate();
                this.addresses = new ArrayList(size());
                for (int i = 0; i < stream.size(); i++) {
                    this.addresses.add(stream.next());
                }
            }
            return this.addresses.get(index);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.duplicate.size();
        }

        @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<InetSocketAddress> iterator() {
            return new Iterator<InetSocketAddress>() { // from class: io.netty.resolver.dns.DnsResolveContext.DnsAddressStreamList.1
                private int i;
                private final DnsServerAddressStream stream;

                {
                    this.stream = DnsAddressStreamList.this.duplicate.duplicate();
                }

                @Override // java.util.Iterator
                public boolean hasNext() {
                    return this.i < this.stream.size();
                }

                @Override // java.util.Iterator
                public InetSocketAddress next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    this.i++;
                    return this.stream.next();
                }

                @Override // java.util.Iterator
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }
    }

    private static AuthoritativeNameServerList extractAuthoritativeNameServers(String questionName, DnsResponse res) {
        int authorityCount = res.count(DnsSection.AUTHORITY);
        if (authorityCount == 0) {
            return null;
        }
        AuthoritativeNameServerList serverNames = new AuthoritativeNameServerList(questionName);
        for (int i = 0; i < authorityCount; i++) {
            serverNames.add(res.recordAt(DnsSection.AUTHORITY, i));
        }
        if (serverNames.isEmpty()) {
            return null;
        }
        return serverNames;
    }

    private void onExpectedResponse(DnsQuestion question, AddressedEnvelope<DnsResponse, InetSocketAddress> envelope, DnsQueryLifecycleObserver queryLifecycleObserver, Promise<List<T>> promise) {
        boolean matches;
        int answerCount;
        boolean cnameNeedsFollow;
        String resolved;
        String resolved2;
        String[] strArr;
        String fqdn;
        DnsResponse response = envelope.content();
        Map<String, String> cnames = buildAliasMap(response, cnameCache(), this.parent.executor());
        int answerCount2 = response.count(DnsSection.ANSWER);
        boolean completeEarly = this.completeEarly;
        int i = 1;
        boolean cnameNeedsFollow2 = !cnames.isEmpty();
        boolean found = false;
        boolean completeEarly2 = completeEarly;
        int i2 = 0;
        while (i2 < answerCount2) {
            DnsRecord r = response.recordAt(DnsSection.ANSWER, i2);
            DnsRecordType type = r.type();
            DnsRecordType[] dnsRecordTypeArr = this.expectedTypes;
            int length = dnsRecordTypeArr.length;
            int i3 = 0;
            while (true) {
                if (i3 >= length) {
                    matches = false;
                    break;
                }
                DnsRecordType expectedType = dnsRecordTypeArr[i3];
                if (type != expectedType) {
                    i3++;
                } else {
                    matches = true;
                    break;
                }
            }
            if (!matches) {
                answerCount = answerCount2;
            } else {
                String questionName = question.name().toLowerCase(Locale.US);
                String recordName = r.name().toLowerCase(Locale.US);
                if (recordName.equals(questionName)) {
                    answerCount = answerCount2;
                } else {
                    Map<String, String> cnamesCopy = new HashMap<>(cnames);
                    String resolved3 = questionName;
                    while (true) {
                        resolved3 = cnamesCopy.remove(resolved3);
                        if (recordName.equals(resolved3)) {
                            cnameNeedsFollow = false;
                            break;
                        } else {
                            if (resolved3 == null) {
                                cnameNeedsFollow = cnameNeedsFollow2;
                                break;
                            }
                            i = 1;
                        }
                    }
                    if (resolved3 != null) {
                        answerCount = answerCount2;
                    } else {
                        if (!questionName.isEmpty() && questionName.charAt(questionName.length() - i) != '.') {
                            throw new AssertionError();
                        }
                        String[] searchDomains = this.parent.searchDomains();
                        int length2 = searchDomains.length;
                        int i4 = 0;
                        while (true) {
                            if (i4 >= length2) {
                                answerCount = answerCount2;
                                resolved = resolved3;
                                break;
                            }
                            answerCount = answerCount2;
                            String searchDomain = searchDomains[i4];
                            if (searchDomain.isEmpty()) {
                                resolved2 = resolved3;
                                strArr = searchDomains;
                            } else {
                                resolved2 = resolved3;
                                strArr = searchDomains;
                                if (searchDomain.charAt(searchDomain.length() - 1) == '.') {
                                    fqdn = questionName + searchDomain;
                                } else {
                                    fqdn = questionName + searchDomain + '.';
                                }
                                if (recordName.equals(fqdn)) {
                                    resolved = recordName;
                                    break;
                                }
                            }
                            i4++;
                            answerCount2 = answerCount;
                            resolved3 = resolved2;
                            searchDomains = strArr;
                        }
                        if (resolved == null) {
                            if (logger.isDebugEnabled()) {
                                logger.debug("{} Ignoring record {} for [{}: {}] as it contains a different name than the question name [{}]. Cnames: {}, Search domains: {}", this.channel, r.toString(), Integer.valueOf(response.id()), envelope.sender(), questionName, cnames, this.parent.searchDomains());
                            }
                            cnameNeedsFollow2 = cnameNeedsFollow;
                        }
                    }
                    cnameNeedsFollow2 = cnameNeedsFollow;
                }
                T converted = convertRecord(r, this.hostname, this.additionals, this.parent.executor());
                if (converted == null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("{} Ignoring record {} for [{}: {}] as the converted record is null. Hostname [{}], Additionals: {}", this.channel, r.toString(), Integer.valueOf(response.id()), envelope.sender(), this.hostname, this.additionals);
                    }
                } else {
                    boolean shouldRelease = false;
                    if (!completeEarly2) {
                        completeEarly2 = isCompleteEarly(converted);
                    }
                    if (!promise.isDone()) {
                        if (this.finalResult == null) {
                            this.finalResult = new ArrayList(8);
                            this.finalResult.add(converted);
                        } else if (isDuplicateAllowed() || !this.finalResult.contains(converted)) {
                            this.finalResult.add(converted);
                        } else {
                            shouldRelease = true;
                        }
                    } else {
                        shouldRelease = true;
                    }
                    cache(this.hostname, this.additionals, r, converted);
                    if (shouldRelease) {
                        ReferenceCountUtil.release(converted);
                    }
                    found = true;
                }
            }
            i2++;
            answerCount2 = answerCount;
            i = 1;
        }
        if (found && !cnameNeedsFollow2) {
            if (completeEarly2) {
                this.completeEarly = true;
            }
            queryLifecycleObserver.querySucceed();
        } else if (!cnames.isEmpty()) {
            queryLifecycleObserver.querySucceed();
            onResponseCNAME(question, cnames, newDnsQueryLifecycleObserver(question), promise);
        } else {
            queryLifecycleObserver.queryFailed(NO_MATCHING_RECORD_QUERY_FAILED_EXCEPTION);
        }
    }

    private void onResponseCNAME(DnsQuestion question, Map<String, String> cnames, DnsQueryLifecycleObserver queryLifecycleObserver, Promise<List<T>> promise) {
        String next;
        String resolved = question.name().toLowerCase(Locale.US);
        boolean found = false;
        while (!cnames.isEmpty() && (next = cnames.remove(resolved)) != null) {
            found = true;
            resolved = next;
        }
        if (found) {
            followCname(question, resolved, queryLifecycleObserver, promise);
        } else {
            queryLifecycleObserver.queryFailed(CNAME_NOT_FOUND_QUERY_FAILED_EXCEPTION);
        }
    }

    private static Map<String, String> buildAliasMap(DnsResponse response, DnsCnameCache cache, EventLoop loop) {
        int answerCount = response.count(DnsSection.ANSWER);
        Map<String, String> cnames = null;
        for (int i = 0; i < answerCount; i++) {
            DnsRecord r = response.recordAt(DnsSection.ANSWER, i);
            DnsRecordType type = r.type();
            if (type == DnsRecordType.CNAME && (r instanceof DnsRawRecord)) {
                ByteBuf recordContent = ((ByteBufHolder) r).content();
                String domainName = decodeDomainName(recordContent);
                if (domainName != null) {
                    if (cnames == null) {
                        cnames = new HashMap<>(Math.min(8, answerCount));
                    }
                    String name = r.name().toLowerCase(Locale.US);
                    String mapping = domainName.toLowerCase(Locale.US);
                    String nameWithDot = hostnameWithDot(name);
                    String mappingWithDot = hostnameWithDot(mapping);
                    if (!nameWithDot.equalsIgnoreCase(mappingWithDot)) {
                        cache.cache(nameWithDot, mappingWithDot, r.timeToLive(), loop);
                        cnames.put(name, mapping);
                    }
                }
            }
        }
        return cnames != null ? cnames : Collections.emptyMap();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tryToFinishResolve(DnsServerAddressStream nameServerAddrStream, int nameServerAddrStreamIndex, DnsQuestion question, DnsQueryLifecycleObserver queryLifecycleObserver, Promise<List<T>> promise, Throwable cause) {
        if (!this.completeEarly && !this.queriesInProgress.isEmpty()) {
            queryLifecycleObserver.queryCancelled(this.allowedQueries);
            return;
        }
        if (this.finalResult != null) {
            queryLifecycleObserver.queryCancelled(this.allowedQueries);
        } else {
            if (nameServerAddrStreamIndex < nameServerAddrStream.size()) {
                if (queryLifecycleObserver == NoopDnsQueryLifecycleObserver.INSTANCE) {
                    query(nameServerAddrStream, nameServerAddrStreamIndex + 1, question, newDnsQueryLifecycleObserver(question), true, promise, cause);
                    return;
                } else {
                    query(nameServerAddrStream, nameServerAddrStreamIndex + 1, question, queryLifecycleObserver, true, promise, cause);
                    return;
                }
            }
            queryLifecycleObserver.queryFailed(NAME_SERVERS_EXHAUSTED_EXCEPTION);
            if (TRY_FINAL_CNAME_ON_ADDRESS_LOOKUPS) {
                boolean isValidResponse = cause == NXDOMAIN_CAUSE_QUERY_FAILED_EXCEPTION || cause == SERVFAIL_QUERY_FAILED_EXCEPTION;
                if ((cause == null || isValidResponse) && !this.triedCNAME && (question.type() == DnsRecordType.A || question.type() == DnsRecordType.AAAA)) {
                    this.triedCNAME = true;
                    query(this.hostname, DnsRecordType.CNAME, getNameServers(this.hostname), true, promise);
                    return;
                }
            }
        }
        finishResolve(promise, cause);
    }

    private void finishResolve(Promise<List<T>> promise, Throwable cause) {
        if (!this.completeEarly && !this.queriesInProgress.isEmpty()) {
            Iterator<Future<AddressedEnvelope<DnsResponse, InetSocketAddress>>> i = this.queriesInProgress.iterator();
            while (i.hasNext()) {
                Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> f = i.next();
                i.remove();
                f.cancel(false);
            }
        }
        if (this.finalResult != null) {
            if (!promise.isDone()) {
                List<T> result = filterResults(this.finalResult);
                this.finalResult = Collections.emptyList();
                if (!DnsNameResolver.trySuccess(promise, result)) {
                    for (T item : result) {
                        ReferenceCountUtil.safeRelease(item);
                    }
                    return;
                }
                return;
            }
            if (!this.finalResult.isEmpty()) {
                throw new AssertionError();
            }
            return;
        }
        int maxAllowedQueries = this.parent.maxQueriesPerResolve();
        int tries = maxAllowedQueries - this.allowedQueries;
        StringBuilder buf = new StringBuilder(64);
        buf.append("Failed to resolve '").append(this.hostname).append("' ").append(Arrays.toString(this.expectedTypes));
        if (tries > 1) {
            if (tries < maxAllowedQueries) {
                buf.append(" after ").append(tries).append(" queries ");
            } else {
                buf.append(". Exceeded max queries per resolve ").append(maxAllowedQueries).append(' ');
            }
        }
        UnknownHostException unknownHostException = new UnknownHostException(buf.toString());
        if (cause == null) {
            cache(this.hostname, this.additionals, unknownHostException);
        } else {
            unknownHostException.initCause(cause);
        }
        promise.tryFailure(unknownHostException);
    }

    static String decodeDomainName(ByteBuf in) {
        in.markReaderIndex();
        try {
            String decodeName = DefaultDnsRecordDecoder.decodeName(in);
            in.resetReaderIndex();
            return decodeName;
        } catch (CorruptedFrameException e) {
            in.resetReaderIndex();
            return null;
        } catch (Throwable th) {
            in.resetReaderIndex();
            throw th;
        }
    }

    private DnsServerAddressStream getNameServers(String name) {
        DnsServerAddressStream stream = getNameServersFromCache(name);
        if (stream == null) {
            if (name.equals(this.hostname)) {
                return this.nameServerAddrs.duplicate();
            }
            return this.parent.newNameServerAddressStream(name);
        }
        return stream;
    }

    private void followCname(DnsQuestion question, String cname, DnsQueryLifecycleObserver queryLifecycleObserver, Promise<List<T>> promise) {
        try {
            String cname2 = cnameResolveFromCache(cnameCache(), cname);
            DnsServerAddressStream stream = getNameServers(cname2);
            DnsQuestion cnameQuestion = new DefaultDnsQuestion(cname2, question.type(), this.dnsClass);
            query(stream, 0, cnameQuestion, queryLifecycleObserver.queryCNAMEd(cnameQuestion), true, promise, null);
        } catch (Throwable cause) {
            queryLifecycleObserver.queryFailed(cause);
            PlatformDependent.throwException(cause);
        }
    }

    private boolean query(String hostname, DnsRecordType type, DnsServerAddressStream dnsServerAddressStream, boolean flush, Promise<List<T>> promise) {
        try {
            DnsQuestion question = new DefaultDnsQuestion(hostname, type, this.dnsClass);
            query(dnsServerAddressStream, 0, question, newDnsQueryLifecycleObserver(question), flush, promise, null);
            return true;
        } catch (Throwable cause) {
            promise.tryFailure(new IllegalArgumentException("Unable to create DNS Question for: [" + hostname + ", " + type + ']', cause));
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public DnsQueryLifecycleObserver newDnsQueryLifecycleObserver(DnsQuestion question) {
        return this.parent.dnsQueryLifecycleObserverFactory().newDnsQueryLifecycleObserver(question);
    }

    /* loaded from: classes4.dex */
    private final class CombinedDnsServerAddressStream implements DnsServerAddressStream {
        private final DnsServerAddressStream originalStream;
        private final InetSocketAddress replaced;
        private Iterator<InetAddress> resolved;
        private final List<InetAddress> resolvedAddresses;

        CombinedDnsServerAddressStream(InetSocketAddress replaced, List<InetAddress> resolvedAddresses, DnsServerAddressStream originalStream) {
            this.replaced = replaced;
            this.resolvedAddresses = resolvedAddresses;
            this.originalStream = originalStream;
            this.resolved = resolvedAddresses.iterator();
        }

        @Override // io.netty.resolver.dns.DnsServerAddressStream
        public InetSocketAddress next() {
            if (this.resolved.hasNext()) {
                return nextResolved0();
            }
            InetSocketAddress address = this.originalStream.next();
            if (address.equals(this.replaced)) {
                this.resolved = this.resolvedAddresses.iterator();
                return nextResolved0();
            }
            return address;
        }

        private InetSocketAddress nextResolved0() {
            return DnsResolveContext.this.parent.newRedirectServerAddress(this.resolved.next());
        }

        @Override // io.netty.resolver.dns.DnsServerAddressStream
        public int size() {
            return (this.originalStream.size() + this.resolvedAddresses.size()) - 1;
        }

        @Override // io.netty.resolver.dns.DnsServerAddressStream
        public DnsServerAddressStream duplicate() {
            return new CombinedDnsServerAddressStream(this.replaced, this.resolvedAddresses, this.originalStream.duplicate());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class AuthoritativeNameServerList {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private AuthoritativeNameServer head;
        private int nameServerCount;
        private final String questionName;

        AuthoritativeNameServerList(String questionName) {
            this.questionName = questionName.toLowerCase(Locale.US);
        }

        void add(DnsRecord r) {
            if (r.type() != DnsRecordType.NS || !(r instanceof DnsRawRecord) || this.questionName.length() < r.name().length()) {
                return;
            }
            String recordName = r.name().toLowerCase(Locale.US);
            int dots = 0;
            int a = recordName.length() - 1;
            int b = this.questionName.length() - 1;
            while (a >= 0) {
                char c = recordName.charAt(a);
                if (this.questionName.charAt(b) != c) {
                    return;
                }
                if (c == '.') {
                    dots++;
                }
                a--;
                b--;
            }
            if (this.head != null && this.head.dots > dots) {
                return;
            }
            ByteBuf recordContent = ((ByteBufHolder) r).content();
            String domainName = DnsResolveContext.decodeDomainName(recordContent);
            if (domainName == null) {
                return;
            }
            if (this.head == null || this.head.dots < dots) {
                this.nameServerCount = 1;
                this.head = new AuthoritativeNameServer(dots, r.timeToLive(), recordName, domainName);
            } else if (this.head.dots == dots) {
                AuthoritativeNameServer serverName = this.head;
                while (serverName.next != null) {
                    serverName = serverName.next;
                }
                serverName.next = new AuthoritativeNameServer(dots, r.timeToLive(), recordName, domainName);
                this.nameServerCount++;
            }
        }

        void handleWithAdditional(DnsNameResolver parent, DnsRecord r, AuthoritativeDnsServerCache authoritativeCache) {
            AuthoritativeNameServer serverName = this.head;
            String nsName = r.name();
            InetAddress resolved = DnsAddressDecoder.decodeAddress(r, nsName, parent.isDecodeIdn());
            if (resolved == null) {
                return;
            }
            while (serverName != null) {
                if (!serverName.nsName.equalsIgnoreCase(nsName)) {
                    serverName = serverName.next;
                } else {
                    if (serverName.address != null) {
                        while (serverName.next != null && serverName.next.isCopy) {
                            serverName = serverName.next;
                        }
                        AuthoritativeNameServer server = new AuthoritativeNameServer(serverName);
                        server.next = serverName.next;
                        serverName.next = server;
                        serverName = server;
                        this.nameServerCount++;
                    }
                    serverName.update(parent.newRedirectServerAddress(resolved), r.timeToLive());
                    cache(serverName, authoritativeCache, parent.executor());
                    return;
                }
            }
        }

        void handleWithoutAdditionals(DnsNameResolver parent, DnsCache cache, AuthoritativeDnsServerCache authoritativeCache) {
            InetAddress address;
            AuthoritativeNameServer serverName = this.head;
            while (serverName != null) {
                if (serverName.address == null) {
                    cacheUnresolved(serverName, authoritativeCache, parent.executor());
                    List<? extends DnsCacheEntry> entries = cache.get(serverName.nsName, null);
                    if (entries != null && !entries.isEmpty() && (address = entries.get(0).address()) != null) {
                        serverName.update(parent.newRedirectServerAddress(address));
                        for (int i = 1; i < entries.size(); i++) {
                            InetAddress address2 = entries.get(i).address();
                            if (address2 == null) {
                                throw new AssertionError("Cache returned a cached failure, should never return anything else");
                            }
                            AuthoritativeNameServer server = new AuthoritativeNameServer(serverName);
                            server.next = serverName.next;
                            serverName.next = server;
                            serverName = server;
                            serverName.update(parent.newRedirectServerAddress(address2));
                            this.nameServerCount++;
                        }
                    }
                }
                serverName = serverName.next;
            }
        }

        private static void cacheUnresolved(AuthoritativeNameServer server, AuthoritativeDnsServerCache authoritativeCache, EventLoop loop) {
            server.address = InetSocketAddress.createUnresolved(server.nsName, 53);
            cache(server, authoritativeCache, loop);
        }

        private static void cache(AuthoritativeNameServer server, AuthoritativeDnsServerCache cache, EventLoop loop) {
            if (server.isRootServer()) {
                return;
            }
            cache.cache(server.domainName, server.address, server.ttl, loop);
        }

        boolean isEmpty() {
            return this.nameServerCount == 0;
        }

        List<InetSocketAddress> addressList() {
            List<InetSocketAddress> addressList = new ArrayList<>(this.nameServerCount);
            for (AuthoritativeNameServer server = this.head; server != null; server = server.next) {
                if (server.address != null) {
                    addressList.add(server.address);
                }
            }
            return addressList;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class AuthoritativeNameServer {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private InetSocketAddress address;
        private final String domainName;
        private final int dots;
        final boolean isCopy = false;
        AuthoritativeNameServer next;
        final String nsName;
        private long ttl;

        AuthoritativeNameServer(int dots, long ttl, String domainName, String nsName) {
            this.dots = dots;
            this.ttl = ttl;
            this.nsName = nsName;
            this.domainName = domainName;
        }

        AuthoritativeNameServer(AuthoritativeNameServer server) {
            this.dots = server.dots;
            this.ttl = server.ttl;
            this.nsName = server.nsName;
            this.domainName = server.domainName;
        }

        boolean isRootServer() {
            return this.dots == 1;
        }

        void update(InetSocketAddress address, long ttl) {
            if (this.address != null && !this.address.isUnresolved()) {
                throw new AssertionError();
            }
            this.address = address;
            this.ttl = Math.min(this.ttl, ttl);
        }

        void update(InetSocketAddress address) {
            update(address, Long.MAX_VALUE);
        }
    }
}
