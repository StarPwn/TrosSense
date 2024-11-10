package io.netty.util;

import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/* loaded from: classes4.dex */
final class NetUtilInitializations {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) NetUtilInitializations.class);

    private NetUtilInitializations() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Inet4Address createLocalhost4() {
        byte[] LOCALHOST4_BYTES = {Byte.MAX_VALUE, 0, 0, 1};
        try {
            Inet4Address localhost4 = (Inet4Address) InetAddress.getByAddress("localhost", LOCALHOST4_BYTES);
            return localhost4;
        } catch (Exception e) {
            PlatformDependent.throwException(e);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Inet6Address createLocalhost6() {
        byte[] LOCALHOST6_BYTES = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1};
        try {
            Inet6Address localhost6 = (Inet6Address) InetAddress.getByAddress("localhost", LOCALHOST6_BYTES);
            return localhost6;
        } catch (Exception e) {
            PlatformDependent.throwException(e);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Collection<NetworkInterface> networkInterfaces() {
        List<NetworkInterface> networkInterfaces = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (interfaces != null) {
                while (interfaces.hasMoreElements()) {
                    networkInterfaces.add(interfaces.nextElement());
                }
            }
        } catch (NullPointerException e) {
            if (!PlatformDependent.isAndroid()) {
                throw e;
            }
        } catch (SocketException e2) {
            logger.warn("Failed to retrieve the list of available network interfaces", (Throwable) e2);
        }
        return Collections.unmodifiableList(networkInterfaces);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00b9, code lost:            if (r4 == null) goto L50;     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x00c9, code lost:            io.netty.util.NetUtilInitializations.logger.debug("Using hard-coded IPv4 localhost address: {}", r11);        r4 = r11;     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00c7, code lost:            if (r4 != null) goto L51;     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static io.netty.util.NetUtilInitializations.NetworkIfaceAndInetAddress determineLoopback(java.util.Collection<java.net.NetworkInterface> r10, java.net.Inet4Address r11, java.net.Inet6Address r12) {
        /*
            java.lang.String r0 = "Failed to find the loopback interface"
            java.lang.String r1 = "Using hard-coded IPv4 localhost address: {}"
            java.util.ArrayList r2 = new java.util.ArrayList
            r2.<init>()
            java.util.Iterator r3 = r10.iterator()
        Ld:
            boolean r4 = r3.hasNext()
            if (r4 == 0) goto L27
            java.lang.Object r4 = r3.next()
            java.net.NetworkInterface r4 = (java.net.NetworkInterface) r4
            java.util.Enumeration r5 = io.netty.util.internal.SocketUtils.addressesFromNetworkInterface(r4)
            boolean r5 = r5.hasMoreElements()
            if (r5 == 0) goto L26
            r2.add(r4)
        L26:
            goto Ld
        L27:
            r3 = 0
            r4 = 0
            java.util.Iterator r5 = r2.iterator()
        L2d:
            boolean r6 = r5.hasNext()
            if (r6 == 0) goto L54
            java.lang.Object r6 = r5.next()
            java.net.NetworkInterface r6 = (java.net.NetworkInterface) r6
            java.util.Enumeration r7 = io.netty.util.internal.SocketUtils.addressesFromNetworkInterface(r6)
        L3d:
            boolean r8 = r7.hasMoreElements()
            if (r8 == 0) goto L53
            java.lang.Object r8 = r7.nextElement()
            java.net.InetAddress r8 = (java.net.InetAddress) r8
            boolean r9 = r8.isLoopbackAddress()
            if (r9 == 0) goto L52
            r3 = r6
            r4 = r8
            goto L54
        L52:
            goto L3d
        L53:
            goto L2d
        L54:
            if (r3 != 0) goto L8e
            java.util.Iterator r5 = r2.iterator()     // Catch: java.net.SocketException -> L88
        L5a:
            boolean r6 = r5.hasNext()     // Catch: java.net.SocketException -> L88
            if (r6 == 0) goto L80
            java.lang.Object r6 = r5.next()     // Catch: java.net.SocketException -> L88
            java.net.NetworkInterface r6 = (java.net.NetworkInterface) r6     // Catch: java.net.SocketException -> L88
            boolean r7 = r6.isLoopback()     // Catch: java.net.SocketException -> L88
            if (r7 == 0) goto L7f
            java.util.Enumeration r7 = io.netty.util.internal.SocketUtils.addressesFromNetworkInterface(r6)     // Catch: java.net.SocketException -> L88
            boolean r8 = r7.hasMoreElements()     // Catch: java.net.SocketException -> L88
            if (r8 == 0) goto L7f
            r3 = r6
            java.lang.Object r5 = r7.nextElement()     // Catch: java.net.SocketException -> L88
            java.net.InetAddress r5 = (java.net.InetAddress) r5     // Catch: java.net.SocketException -> L88
            r4 = r5
            goto L80
        L7f:
            goto L5a
        L80:
            if (r3 != 0) goto L87
            io.netty.util.internal.logging.InternalLogger r5 = io.netty.util.NetUtilInitializations.logger     // Catch: java.net.SocketException -> L88
            r5.warn(r0)     // Catch: java.net.SocketException -> L88
        L87:
            goto L8e
        L88:
            r5 = move-exception
            io.netty.util.internal.logging.InternalLogger r6 = io.netty.util.NetUtilInitializations.logger
            r6.warn(r0, r5)
        L8e:
            if (r3 == 0) goto La8
            io.netty.util.internal.logging.InternalLogger r0 = io.netty.util.NetUtilInitializations.logger
            java.lang.String r1 = r3.getName()
            java.lang.String r5 = r3.getDisplayName()
            java.lang.String r6 = r4.getHostAddress()
            java.lang.Object[] r1 = new java.lang.Object[]{r1, r5, r6}
            java.lang.String r5 = "Loopback interface: {} ({}, {})"
            r0.debug(r5, r1)
            goto Lcf
        La8:
            if (r4 != 0) goto Lcf
            java.net.NetworkInterface r0 = java.net.NetworkInterface.getByInetAddress(r12)     // Catch: java.lang.Throwable -> Lbc java.lang.Exception -> Lc6
            if (r0 == 0) goto Lb9
            io.netty.util.internal.logging.InternalLogger r0 = io.netty.util.NetUtilInitializations.logger     // Catch: java.lang.Throwable -> Lbc java.lang.Exception -> Lc6
            java.lang.String r5 = "Using hard-coded IPv6 localhost address: {}"
            r0.debug(r5, r12)     // Catch: java.lang.Throwable -> Lbc java.lang.Exception -> Lc6
            r0 = r12
            r4 = r0
        Lb9:
            if (r4 != 0) goto Lcf
            goto Lc9
        Lbc:
            r0 = move-exception
            if (r4 != 0) goto Lc5
            io.netty.util.internal.logging.InternalLogger r5 = io.netty.util.NetUtilInitializations.logger
            r5.debug(r1, r11)
            r4 = r11
        Lc5:
            throw r0
        Lc6:
            r0 = move-exception
            if (r4 != 0) goto Lcf
        Lc9:
            io.netty.util.internal.logging.InternalLogger r0 = io.netty.util.NetUtilInitializations.logger
            r0.debug(r1, r11)
            r4 = r11
        Lcf:
            io.netty.util.NetUtilInitializations$NetworkIfaceAndInetAddress r0 = new io.netty.util.NetUtilInitializations$NetworkIfaceAndInetAddress
            r0.<init>(r3, r4)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.util.NetUtilInitializations.determineLoopback(java.util.Collection, java.net.Inet4Address, java.net.Inet6Address):io.netty.util.NetUtilInitializations$NetworkIfaceAndInetAddress");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static final class NetworkIfaceAndInetAddress {
        private final InetAddress address;
        private final NetworkInterface iface;

        NetworkIfaceAndInetAddress(NetworkInterface iface, InetAddress address) {
            this.iface = iface;
            this.address = address;
        }

        public NetworkInterface iface() {
            return this.iface;
        }

        public InetAddress address() {
            return this.address;
        }
    }
}
