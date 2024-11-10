package io.netty.util;

import com.oracle.svm.core.annotate.Alias;
import com.oracle.svm.core.annotate.InjectAccessors;
import com.oracle.svm.core.annotate.TargetClass;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collection;

@TargetClass(NetUtil.class)
/* loaded from: classes4.dex */
final class NetUtilSubstitutions {

    @Alias
    @InjectAccessors(NetUtilLocalhostAccessor.class)
    public static InetAddress LOCALHOST;

    @Alias
    @InjectAccessors(NetUtilLocalhost4Accessor.class)
    public static Inet4Address LOCALHOST4;

    @Alias
    @InjectAccessors(NetUtilLocalhost6Accessor.class)
    public static Inet6Address LOCALHOST6;

    @Alias
    @InjectAccessors(NetUtilNetworkInterfacesAccessor.class)
    public static Collection<NetworkInterface> NETWORK_INTERFACES;

    private NetUtilSubstitutions() {
    }

    /* loaded from: classes4.dex */
    private static final class NetUtilLocalhost4Accessor {
        private NetUtilLocalhost4Accessor() {
        }

        static Inet4Address get() {
            return NetUtilLocalhost4LazyHolder.LOCALHOST4;
        }

        static void set(Inet4Address ignored) {
        }
    }

    /* loaded from: classes4.dex */
    private static final class NetUtilLocalhost4LazyHolder {
        private static final Inet4Address LOCALHOST4 = NetUtilInitializations.createLocalhost4();

        private NetUtilLocalhost4LazyHolder() {
        }
    }

    /* loaded from: classes4.dex */
    private static final class NetUtilLocalhost6Accessor {
        private NetUtilLocalhost6Accessor() {
        }

        static Inet6Address get() {
            return NetUtilLocalhost6LazyHolder.LOCALHOST6;
        }

        static void set(Inet6Address ignored) {
        }
    }

    /* loaded from: classes4.dex */
    private static final class NetUtilLocalhost6LazyHolder {
        private static final Inet6Address LOCALHOST6 = NetUtilInitializations.createLocalhost6();

        private NetUtilLocalhost6LazyHolder() {
        }
    }

    /* loaded from: classes4.dex */
    private static final class NetUtilLocalhostAccessor {
        private NetUtilLocalhostAccessor() {
        }

        static InetAddress get() {
            return NetUtilLocalhostLazyHolder.LOCALHOST;
        }

        static void set(InetAddress ignored) {
        }
    }

    /* loaded from: classes4.dex */
    private static final class NetUtilLocalhostLazyHolder {
        private static final InetAddress LOCALHOST = NetUtilInitializations.determineLoopback(NetUtilNetworkInterfacesLazyHolder.NETWORK_INTERFACES, NetUtilLocalhost4LazyHolder.LOCALHOST4, NetUtilLocalhost6LazyHolder.LOCALHOST6).address();

        private NetUtilLocalhostLazyHolder() {
        }
    }

    /* loaded from: classes4.dex */
    private static final class NetUtilNetworkInterfacesAccessor {
        private NetUtilNetworkInterfacesAccessor() {
        }

        static Collection<NetworkInterface> get() {
            return NetUtilNetworkInterfacesLazyHolder.NETWORK_INTERFACES;
        }

        static void set(Collection<NetworkInterface> ignored) {
        }
    }

    /* loaded from: classes4.dex */
    private static final class NetUtilNetworkInterfacesLazyHolder {
        private static final Collection<NetworkInterface> NETWORK_INTERFACES = NetUtilInitializations.networkInterfaces();

        private NetUtilNetworkInterfacesLazyHolder() {
        }
    }
}
