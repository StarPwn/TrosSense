package io.netty.bootstrap;

import io.netty.channel.Channel;
import io.netty.resolver.AddressResolverGroup;
import java.net.SocketAddress;

/* loaded from: classes.dex */
public final class BootstrapConfig extends AbstractBootstrapConfig<Bootstrap, Channel> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public BootstrapConfig(Bootstrap bootstrap) {
        super(bootstrap);
    }

    public SocketAddress remoteAddress() {
        return ((Bootstrap) this.bootstrap).remoteAddress();
    }

    public AddressResolverGroup<?> resolver() {
        return ((Bootstrap) this.bootstrap).resolver();
    }

    @Override // io.netty.bootstrap.AbstractBootstrapConfig
    public String toString() {
        StringBuilder buf = new StringBuilder(super.toString());
        buf.setLength(buf.length() - 1);
        AddressResolverGroup<?> resolver = resolver();
        if (resolver != null) {
            buf.append(", resolver: ").append(resolver);
        }
        SocketAddress remoteAddress = remoteAddress();
        if (remoteAddress != null) {
            buf.append(", remoteAddress: ").append(remoteAddress);
        }
        return buf.append(')').toString();
    }
}
