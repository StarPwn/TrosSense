package io.netty.resolver.dns;

import io.netty.util.internal.ObjectUtil;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes4.dex */
public abstract class DnsServerAddresses {
    public abstract DnsServerAddressStream stream();

    @Deprecated
    public static List<InetSocketAddress> defaultAddressList() {
        return DefaultDnsServerAddressStreamProvider.defaultAddressList();
    }

    @Deprecated
    public static DnsServerAddresses defaultAddresses() {
        return DefaultDnsServerAddressStreamProvider.defaultAddresses();
    }

    public static DnsServerAddresses sequential(Iterable<? extends InetSocketAddress> addresses) {
        return sequential0(sanitize(addresses));
    }

    public static DnsServerAddresses sequential(InetSocketAddress... addresses) {
        return sequential0(sanitize(addresses));
    }

    private static DnsServerAddresses sequential0(List<InetSocketAddress> addresses) {
        if (addresses.size() == 1) {
            return singleton(addresses.get(0));
        }
        return new DefaultDnsServerAddresses("sequential", addresses) { // from class: io.netty.resolver.dns.DnsServerAddresses.1
            @Override // io.netty.resolver.dns.DnsServerAddresses
            public DnsServerAddressStream stream() {
                return new SequentialDnsServerAddressStream(this.addresses, 0);
            }
        };
    }

    public static DnsServerAddresses shuffled(Iterable<? extends InetSocketAddress> addresses) {
        return shuffled0(sanitize(addresses));
    }

    public static DnsServerAddresses shuffled(InetSocketAddress... addresses) {
        return shuffled0(sanitize(addresses));
    }

    private static DnsServerAddresses shuffled0(List<InetSocketAddress> addresses) {
        if (addresses.size() == 1) {
            return singleton(addresses.get(0));
        }
        return new DefaultDnsServerAddresses("shuffled", addresses) { // from class: io.netty.resolver.dns.DnsServerAddresses.2
            @Override // io.netty.resolver.dns.DnsServerAddresses
            public DnsServerAddressStream stream() {
                return new ShuffledDnsServerAddressStream(this.addresses);
            }
        };
    }

    public static DnsServerAddresses rotational(Iterable<? extends InetSocketAddress> addresses) {
        return rotational0(sanitize(addresses));
    }

    public static DnsServerAddresses rotational(InetSocketAddress... addresses) {
        return rotational0(sanitize(addresses));
    }

    private static DnsServerAddresses rotational0(List<InetSocketAddress> addresses) {
        if (addresses.size() == 1) {
            return singleton(addresses.get(0));
        }
        return new RotationalDnsServerAddresses(addresses);
    }

    public static DnsServerAddresses singleton(InetSocketAddress address) {
        ObjectUtil.checkNotNull(address, "address");
        if (address.isUnresolved()) {
            throw new IllegalArgumentException("cannot use an unresolved DNS server address: " + address);
        }
        return new SingletonDnsServerAddresses(address);
    }

    private static List<InetSocketAddress> sanitize(Iterable<? extends InetSocketAddress> addresses) {
        List<InetSocketAddress> list;
        InetSocketAddress a;
        ObjectUtil.checkNotNull(addresses, "addresses");
        if (addresses instanceof Collection) {
            list = new ArrayList<>(((Collection) addresses).size());
        } else {
            list = new ArrayList<>(4);
        }
        Iterator<? extends InetSocketAddress> it2 = addresses.iterator();
        while (it2.hasNext() && (a = it2.next()) != null) {
            if (a.isUnresolved()) {
                throw new IllegalArgumentException("cannot use an unresolved DNS server address: " + a);
            }
            list.add(a);
        }
        return (List) ObjectUtil.checkNonEmpty(list, "list");
    }

    private static List<InetSocketAddress> sanitize(InetSocketAddress[] addresses) {
        ObjectUtil.checkNotNull(addresses, "addresses");
        List<InetSocketAddress> list = new ArrayList<>(addresses.length);
        for (InetSocketAddress a : addresses) {
            if (a == null) {
                break;
            }
            if (a.isUnresolved()) {
                throw new IllegalArgumentException("cannot use an unresolved DNS server address: " + a);
            }
            list.add(a);
        }
        if (list.isEmpty()) {
            return DefaultDnsServerAddressStreamProvider.defaultAddressList();
        }
        return list;
    }
}
