package io.netty.channel.local;

import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import java.net.SocketAddress;
import java.util.concurrent.ConcurrentMap;

/* loaded from: classes4.dex */
final class LocalChannelRegistry {
    private static final ConcurrentMap<LocalAddress, Channel> boundChannels = PlatformDependent.newConcurrentHashMap();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static LocalAddress register(Channel channel, LocalAddress oldLocalAddress, SocketAddress localAddress) {
        if (oldLocalAddress != null) {
            throw new ChannelException("already bound");
        }
        if (!(localAddress instanceof LocalAddress)) {
            throw new ChannelException("unsupported address type: " + StringUtil.simpleClassName(localAddress));
        }
        LocalAddress addr = (LocalAddress) localAddress;
        if (LocalAddress.ANY.equals(addr)) {
            addr = new LocalAddress(channel);
        }
        Channel boundChannel = boundChannels.putIfAbsent(addr, channel);
        if (boundChannel != null) {
            throw new ChannelException("address already in use by: " + boundChannel);
        }
        return addr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Channel get(SocketAddress localAddress) {
        return boundChannels.get(localAddress);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void unregister(LocalAddress localAddress) {
        boundChannels.remove(localAddress);
    }

    private LocalChannelRegistry() {
    }
}
