package io.netty.resolver.dns;

import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/* loaded from: classes4.dex */
public final class DnsServerAddressStreamProviders {
    private static final InternalLogger LOGGER = InternalLoggerFactory.getInstance((Class<?>) DnsServerAddressStreamProviders.class);
    private static final String MACOS_PROVIDER_CLASS_NAME = "io.netty.resolver.dns.macos.MacOSDnsServerAddressStreamProvider";
    private static final Constructor<? extends DnsServerAddressStreamProvider> STREAM_PROVIDER_CONSTRUCTOR;

    static {
        Constructor<? extends DnsServerAddressStreamProvider> constructor = null;
        if (PlatformDependent.isOsx()) {
            try {
                Object maybeProvider = AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: io.netty.resolver.dns.DnsServerAddressStreamProviders.1
                    @Override // java.security.PrivilegedAction
                    public Object run() {
                        try {
                            return Class.forName(DnsServerAddressStreamProviders.MACOS_PROVIDER_CLASS_NAME, true, DnsServerAddressStreamProviders.class.getClassLoader());
                        } catch (Throwable cause) {
                            return cause;
                        }
                    }
                });
                if (maybeProvider instanceof Class) {
                    Class<? extends DnsServerAddressStreamProvider> providerClass = (Class) maybeProvider;
                    constructor = providerClass.getConstructor(new Class[0]);
                    constructor.newInstance(new Object[0]);
                    LOGGER.debug("{}: available", MACOS_PROVIDER_CLASS_NAME);
                } else {
                    throw ((Throwable) maybeProvider);
                }
            } catch (ClassNotFoundException e) {
                LOGGER.warn("Can not find {} in the classpath, fallback to system defaults. This may result in incorrect DNS resolutions on MacOS. Check whether you have a dependency on 'io.netty:netty-resolver-dns-native-macos'", MACOS_PROVIDER_CLASS_NAME);
            } catch (Throwable cause) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.error("Unable to load {}, fallback to system defaults. This may result in incorrect DNS resolutions on MacOS. Check whether you have a dependency on 'io.netty:netty-resolver-dns-native-macos'", MACOS_PROVIDER_CLASS_NAME, cause);
                } else {
                    LOGGER.error("Unable to load {}, fallback to system defaults. This may result in incorrect DNS resolutions on MacOS. Check whether you have a dependency on 'io.netty:netty-resolver-dns-native-macos'. Use DEBUG level to see the full stack: {}", MACOS_PROVIDER_CLASS_NAME, cause.getCause() != null ? cause.getCause().toString() : cause.toString());
                }
                constructor = null;
            }
        }
        STREAM_PROVIDER_CONSTRUCTOR = constructor;
    }

    private DnsServerAddressStreamProviders() {
    }

    public static DnsServerAddressStreamProvider platformDefault() {
        if (STREAM_PROVIDER_CONSTRUCTOR != null) {
            try {
                return STREAM_PROVIDER_CONSTRUCTOR.newInstance(new Object[0]);
            } catch (IllegalAccessException e) {
            } catch (InstantiationException e2) {
            } catch (InvocationTargetException e3) {
            }
        }
        return unixDefault();
    }

    public static DnsServerAddressStreamProvider unixDefault() {
        return DefaultProviderHolder.DEFAULT_DNS_SERVER_ADDRESS_STREAM_PROVIDER;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class DefaultProviderHolder {
        private static final long REFRESH_INTERVAL = TimeUnit.MINUTES.toNanos(5);
        static final DnsServerAddressStreamProvider DEFAULT_DNS_SERVER_ADDRESS_STREAM_PROVIDER = new DnsServerAddressStreamProvider() { // from class: io.netty.resolver.dns.DnsServerAddressStreamProviders.DefaultProviderHolder.1
            private volatile DnsServerAddressStreamProvider currentProvider = provider();
            private final AtomicLong lastRefresh = new AtomicLong(System.nanoTime());

            @Override // io.netty.resolver.dns.DnsServerAddressStreamProvider
            public DnsServerAddressStream nameServerAddressStream(String hostname) {
                long last = this.lastRefresh.get();
                DnsServerAddressStreamProvider current = this.currentProvider;
                if (System.nanoTime() - last > DefaultProviderHolder.REFRESH_INTERVAL && this.lastRefresh.compareAndSet(last, System.nanoTime())) {
                    DnsServerAddressStreamProvider provider = provider();
                    this.currentProvider = provider;
                    current = provider;
                }
                return current.nameServerAddressStream(hostname);
            }

            private DnsServerAddressStreamProvider provider() {
                return PlatformDependent.isWindows() ? DefaultDnsServerAddressStreamProvider.INSTANCE : UnixResolverDnsServerAddressStreamProvider.parseSilently();
            }
        };

        private DefaultProviderHolder() {
        }
    }
}
