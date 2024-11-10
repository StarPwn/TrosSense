package io.netty.resolver.dns.macos;

import io.netty.channel.unix.NativeInetAddress;
import java.net.InetSocketAddress;

/* loaded from: classes4.dex */
final class DnsResolver {
    private final String domain;
    private final InetSocketAddress[] nameservers;
    private final String options;
    private final int port;
    private final int searchOrder;
    private final String[] searches;
    private final int timeout;

    DnsResolver(String domain, byte[][] nameservers, int port, String[] searches, String options, int timeout, int searchOrder) {
        this.domain = domain;
        if (nameservers == null) {
            this.nameservers = new InetSocketAddress[0];
        } else {
            this.nameservers = new InetSocketAddress[nameservers.length];
            for (int i = 0; i < nameservers.length; i++) {
                byte[] addr = nameservers[i];
                this.nameservers[i] = NativeInetAddress.address(addr, 0, addr.length);
            }
        }
        this.port = port;
        this.searches = searches;
        this.options = options;
        this.timeout = timeout;
        this.searchOrder = searchOrder;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String domain() {
        return this.domain;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InetSocketAddress[] nameservers() {
        return this.nameservers;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int port() {
        return this.port;
    }

    String[] searches() {
        return this.searches;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String options() {
        return this.options;
    }

    int timeout() {
        return this.timeout;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int searchOrder() {
        return this.searchOrder;
    }
}
