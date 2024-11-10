package io.netty.resolver.dns;

import java.net.InetSocketAddress;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public abstract class DefaultDnsServerAddresses extends DnsServerAddresses {
    protected final List<InetSocketAddress> addresses;
    private final String strVal;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DefaultDnsServerAddresses(String type, List<InetSocketAddress> addresses) {
        this.addresses = addresses;
        StringBuilder buf = new StringBuilder(type.length() + 2 + (addresses.size() * 16));
        buf.append(type).append('(');
        for (InetSocketAddress a : addresses) {
            buf.append(a).append(", ");
        }
        buf.setLength(buf.length() - 2);
        buf.append(')');
        this.strVal = buf.toString();
    }

    public String toString() {
        return this.strVal;
    }
}
