package io.netty.channel.kqueue;

import io.netty.channel.Channel;
import io.netty.channel.unix.DomainSocketAddress;
import io.netty.channel.unix.ServerDomainSocketChannel;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.File;
import java.net.SocketAddress;

/* loaded from: classes4.dex */
public final class KQueueServerDomainSocketChannel extends AbstractKQueueServerChannel implements ServerDomainSocketChannel {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) KQueueServerDomainSocketChannel.class);
    private final KQueueServerChannelConfig config;
    private volatile DomainSocketAddress local;

    public KQueueServerDomainSocketChannel() {
        super(BsdSocket.newSocketDomain(), false);
        this.config = new KQueueServerChannelConfig(this);
    }

    public KQueueServerDomainSocketChannel(int fd) {
        this(new BsdSocket(fd), false);
    }

    KQueueServerDomainSocketChannel(BsdSocket socket, boolean active) {
        super(socket, active);
        this.config = new KQueueServerChannelConfig(this);
    }

    @Override // io.netty.channel.kqueue.AbstractKQueueServerChannel
    protected Channel newChildChannel(int fd, byte[] addr, int offset, int len) throws Exception {
        return new KQueueDomainSocketChannel(this, new BsdSocket(fd));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.AbstractChannel
    public DomainSocketAddress localAddress0() {
        return this.local;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.AbstractChannel
    public void doBind(SocketAddress localAddress) throws Exception {
        this.socket.bind(localAddress);
        this.socket.listen(this.config.getBacklog());
        this.local = (DomainSocketAddress) localAddress;
        this.active = true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.AbstractChannel
    public void doClose() throws Exception {
        DomainSocketAddress local;
        boolean success;
        try {
            super.doClose();
            if (local != null) {
                if (success) {
                    return;
                }
            }
        } finally {
            local = this.local;
            if (local != null) {
                File socketFile = new File(local.path());
                success = socketFile.delete();
                if (!success && logger.isDebugEnabled()) {
                    logger.debug("Failed to delete a domain socket file: {}", local.path());
                }
            }
        }
    }

    @Override // io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.Channel
    public KQueueServerChannelConfig config() {
        return this.config;
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public DomainSocketAddress remoteAddress() {
        return (DomainSocketAddress) super.remoteAddress();
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public DomainSocketAddress localAddress() {
        return (DomainSocketAddress) super.localAddress();
    }
}
