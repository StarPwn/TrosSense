package io.netty.resolver.dns;

import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class DnsQueryContextManager {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Map<InetSocketAddress, DnsQueryContextMap> map = new HashMap();

    /* JADX INFO: Access modifiers changed from: package-private */
    public int add(InetSocketAddress nameServerAddr, DnsQueryContext qCtx) {
        DnsQueryContextMap contexts = getOrCreateContextMap(nameServerAddr);
        return contexts.add(qCtx);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DnsQueryContext get(InetSocketAddress nameServerAddr, int id) {
        DnsQueryContextMap contexts = getContextMap(nameServerAddr);
        if (contexts == null) {
            return null;
        }
        return contexts.get(id);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DnsQueryContext remove(InetSocketAddress nameServerAddr, int id) {
        DnsQueryContextMap contexts = getContextMap(nameServerAddr);
        if (contexts == null) {
            return null;
        }
        return contexts.remove(id);
    }

    private DnsQueryContextMap getContextMap(InetSocketAddress nameServerAddr) {
        DnsQueryContextMap dnsQueryContextMap;
        synchronized (this.map) {
            dnsQueryContextMap = this.map.get(nameServerAddr);
        }
        return dnsQueryContextMap;
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0071 A[Catch: all -> 0x00b1, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x000d, B:9:0x000f, B:11:0x0027, B:13:0x002c, B:15:0x0035, B:18:0x0071, B:21:0x007d, B:22:0x0095, B:23:0x0096, B:25:0x003e, B:26:0x0049, B:28:0x004d, B:30:0x0056, B:31:0x005f, B:33:0x0065, B:34:0x0098, B:35:0x00b0), top: B:3:0x0003 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private io.netty.resolver.dns.DnsQueryContextManager.DnsQueryContextMap getOrCreateContextMap(java.net.InetSocketAddress r11) {
        /*
            r10 = this;
            java.util.Map<java.net.InetSocketAddress, io.netty.resolver.dns.DnsQueryContextManager$DnsQueryContextMap> r0 = r10.map
            monitor-enter(r0)
            java.util.Map<java.net.InetSocketAddress, io.netty.resolver.dns.DnsQueryContextManager$DnsQueryContextMap> r1 = r10.map     // Catch: java.lang.Throwable -> Lb1
            java.lang.Object r1 = r1.get(r11)     // Catch: java.lang.Throwable -> Lb1
            io.netty.resolver.dns.DnsQueryContextManager$DnsQueryContextMap r1 = (io.netty.resolver.dns.DnsQueryContextManager.DnsQueryContextMap) r1     // Catch: java.lang.Throwable -> Lb1
            if (r1 == 0) goto Lf
            monitor-exit(r0)     // Catch: java.lang.Throwable -> Lb1
            return r1
        Lf:
            io.netty.resolver.dns.DnsQueryContextManager$DnsQueryContextMap r2 = new io.netty.resolver.dns.DnsQueryContextManager$DnsQueryContextMap     // Catch: java.lang.Throwable -> Lb1
            r3 = 0
            r2.<init>()     // Catch: java.lang.Throwable -> Lb1
            java.net.InetAddress r3 = r11.getAddress()     // Catch: java.lang.Throwable -> Lb1
            int r4 = r11.getPort()     // Catch: java.lang.Throwable -> Lb1
            java.util.Map<java.net.InetSocketAddress, io.netty.resolver.dns.DnsQueryContextManager$DnsQueryContextMap> r5 = r10.map     // Catch: java.lang.Throwable -> Lb1
            java.lang.Object r5 = r5.put(r11, r2)     // Catch: java.lang.Throwable -> Lb1
            io.netty.resolver.dns.DnsQueryContextManager$DnsQueryContextMap r5 = (io.netty.resolver.dns.DnsQueryContextManager.DnsQueryContextMap) r5     // Catch: java.lang.Throwable -> Lb1
            if (r5 != 0) goto L98
            r6 = 0
            boolean r7 = r3 instanceof java.net.Inet4Address     // Catch: java.lang.Throwable -> Lb1
            if (r7 == 0) goto L49
            r7 = r3
            java.net.Inet4Address r7 = (java.net.Inet4Address) r7     // Catch: java.lang.Throwable -> Lb1
            boolean r8 = r7.isLoopbackAddress()     // Catch: java.lang.Throwable -> Lb1
            if (r8 == 0) goto L3e
            java.net.InetSocketAddress r8 = new java.net.InetSocketAddress     // Catch: java.lang.Throwable -> Lb1
            java.net.Inet6Address r9 = io.netty.util.NetUtil.LOCALHOST6     // Catch: java.lang.Throwable -> Lb1
            r8.<init>(r9, r4)     // Catch: java.lang.Throwable -> Lb1
            r6 = r8
            goto L48
        L3e:
            java.net.InetSocketAddress r8 = new java.net.InetSocketAddress     // Catch: java.lang.Throwable -> Lb1
            java.net.Inet6Address r9 = toCompactAddress(r7)     // Catch: java.lang.Throwable -> Lb1
            r8.<init>(r9, r4)     // Catch: java.lang.Throwable -> Lb1
            r6 = r8
        L48:
            goto L6f
        L49:
            boolean r7 = r3 instanceof java.net.Inet6Address     // Catch: java.lang.Throwable -> Lb1
            if (r7 == 0) goto L48
            r7 = r3
            java.net.Inet6Address r7 = (java.net.Inet6Address) r7     // Catch: java.lang.Throwable -> Lb1
            boolean r8 = r7.isLoopbackAddress()     // Catch: java.lang.Throwable -> Lb1
            if (r8 == 0) goto L5f
            java.net.InetSocketAddress r8 = new java.net.InetSocketAddress     // Catch: java.lang.Throwable -> Lb1
            java.net.Inet4Address r9 = io.netty.util.NetUtil.LOCALHOST4     // Catch: java.lang.Throwable -> Lb1
            r8.<init>(r9, r4)     // Catch: java.lang.Throwable -> Lb1
            r6 = r8
            goto L6f
        L5f:
            boolean r8 = r7.isIPv4CompatibleAddress()     // Catch: java.lang.Throwable -> Lb1
            if (r8 == 0) goto L6f
            java.net.InetSocketAddress r8 = new java.net.InetSocketAddress     // Catch: java.lang.Throwable -> Lb1
            java.net.Inet4Address r9 = toIPv4Address(r7)     // Catch: java.lang.Throwable -> Lb1
            r8.<init>(r9, r4)     // Catch: java.lang.Throwable -> Lb1
            r6 = r8
        L6f:
            if (r6 == 0) goto L96
            java.util.Map<java.net.InetSocketAddress, io.netty.resolver.dns.DnsQueryContextManager$DnsQueryContextMap> r7 = r10.map     // Catch: java.lang.Throwable -> Lb1
            java.lang.Object r7 = r7.put(r6, r2)     // Catch: java.lang.Throwable -> Lb1
            io.netty.resolver.dns.DnsQueryContextManager$DnsQueryContextMap r7 = (io.netty.resolver.dns.DnsQueryContextManager.DnsQueryContextMap) r7     // Catch: java.lang.Throwable -> Lb1
            r5 = r7
            if (r5 != 0) goto L7d
            goto L96
        L7d:
            java.lang.AssertionError r7 = new java.lang.AssertionError     // Catch: java.lang.Throwable -> Lb1
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lb1
            r8.<init>()     // Catch: java.lang.Throwable -> Lb1
            java.lang.String r9 = "DnsQueryContextMap already exists for "
            java.lang.StringBuilder r8 = r8.append(r9)     // Catch: java.lang.Throwable -> Lb1
            java.lang.StringBuilder r8 = r8.append(r6)     // Catch: java.lang.Throwable -> Lb1
            java.lang.String r8 = r8.toString()     // Catch: java.lang.Throwable -> Lb1
            r7.<init>(r8)     // Catch: java.lang.Throwable -> Lb1
            throw r7     // Catch: java.lang.Throwable -> Lb1
        L96:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> Lb1
            return r2
        L98:
            java.lang.AssertionError r6 = new java.lang.AssertionError     // Catch: java.lang.Throwable -> Lb1
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lb1
            r7.<init>()     // Catch: java.lang.Throwable -> Lb1
            java.lang.String r8 = "DnsQueryContextMap already exists for "
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch: java.lang.Throwable -> Lb1
            java.lang.StringBuilder r7 = r7.append(r11)     // Catch: java.lang.Throwable -> Lb1
            java.lang.String r7 = r7.toString()     // Catch: java.lang.Throwable -> Lb1
            r6.<init>(r7)     // Catch: java.lang.Throwable -> Lb1
            throw r6     // Catch: java.lang.Throwable -> Lb1
        Lb1:
            r1 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> Lb1
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.resolver.dns.DnsQueryContextManager.getOrCreateContextMap(java.net.InetSocketAddress):io.netty.resolver.dns.DnsQueryContextManager$DnsQueryContextMap");
    }

    private static Inet6Address toCompactAddress(Inet4Address a4) {
        byte[] b4 = a4.getAddress();
        byte[] b6 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, b4[0], b4[1], b4[2], b4[3]};
        try {
            return (Inet6Address) InetAddress.getByAddress(b6);
        } catch (UnknownHostException e) {
            throw new Error(e);
        }
    }

    private static Inet4Address toIPv4Address(Inet6Address a6) {
        if (!a6.isIPv4CompatibleAddress()) {
            throw new AssertionError();
        }
        byte[] b6 = a6.getAddress();
        byte[] b4 = {b6[12], b6[13], b6[14], b6[15]};
        try {
            return (Inet4Address) InetAddress.getByAddress(b4);
        } catch (UnknownHostException e) {
            throw new Error(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class DnsQueryContextMap {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private final DnsQueryIdSpace idSpace;
        private final IntObjectMap<DnsQueryContext> map;

        private DnsQueryContextMap() {
            this.idSpace = new DnsQueryIdSpace();
            this.map = new IntObjectHashMap();
        }

        synchronized int add(DnsQueryContext ctx) {
            int id;
            id = this.idSpace.nextId();
            DnsQueryContext oldCtx = this.map.put(id, (int) ctx);
            if (oldCtx != null) {
                throw new AssertionError();
            }
            return id;
        }

        synchronized DnsQueryContext get(int id) {
            return this.map.get(id);
        }

        synchronized DnsQueryContext remove(int id) {
            this.idSpace.pushId(id);
            return this.map.remove(id);
        }
    }
}
