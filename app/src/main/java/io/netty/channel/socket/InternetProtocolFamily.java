package io.netty.channel.socket;

import io.netty.util.NetUtil;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;

/* loaded from: classes4.dex */
public enum InternetProtocolFamily {
    IPv4(Inet4Address.class, 1),
    IPv6(Inet6Address.class, 2);

    private final int addressNumber;
    private final Class<? extends InetAddress> addressType;

    InternetProtocolFamily(Class cls, int addressNumber) {
        this.addressType = cls;
        this.addressNumber = addressNumber;
    }

    public Class<? extends InetAddress> addressType() {
        return this.addressType;
    }

    public int addressNumber() {
        return this.addressNumber;
    }

    public InetAddress localhost() {
        switch (this) {
            case IPv4:
                return NetUtil.LOCALHOST4;
            case IPv6:
                return NetUtil.LOCALHOST6;
            default:
                throw new IllegalStateException("Unsupported family " + this);
        }
    }

    public static InternetProtocolFamily of(InetAddress address) {
        if (address instanceof Inet4Address) {
            return IPv4;
        }
        if (address instanceof Inet6Address) {
            return IPv6;
        }
        throw new IllegalArgumentException("address " + address + " not supported");
    }
}
