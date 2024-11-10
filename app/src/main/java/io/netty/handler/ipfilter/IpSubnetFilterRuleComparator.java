package io.netty.handler.ipfilter;

import java.net.InetSocketAddress;
import java.util.Comparator;

/* loaded from: classes4.dex */
final class IpSubnetFilterRuleComparator implements Comparator<Object> {
    static final IpSubnetFilterRuleComparator INSTANCE = new IpSubnetFilterRuleComparator();

    private IpSubnetFilterRuleComparator() {
    }

    @Override // java.util.Comparator
    public int compare(Object o1, Object o2) {
        return ((IpSubnetFilterRule) o1).compareTo((InetSocketAddress) o2);
    }
}
