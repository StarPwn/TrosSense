package io.netty.channel.epoll;

import io.netty.channel.Channel;
import io.netty.channel.unix.DomainSocketAddress;
import io.netty.channel.unix.ServerDomainSocketChannel;
import io.netty.channel.unix.Socket;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.File;
import java.net.SocketAddress;

/* loaded from: classes4.dex */
public final class EpollServerDomainSocketChannel extends AbstractEpollServerChannel implements ServerDomainSocketChannel {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) EpollServerDomainSocketChannel.class);
    private final EpollServerChannelConfig config;
    private volatile DomainSocketAddress local;

    public EpollServerDomainSocketChannel() {
        super(LinuxSocket.newSocketDomain(), false);
        this.config = new EpollServerChannelConfig(this);
    }

    public EpollServerDomainSocketChannel(int fd) {
        super(fd);
        this.config = new EpollServerChannelConfig(this);
    }

    public EpollServerDomainSocketChannel(int fd, boolean active) {
        super(new LinuxSocket(fd), active);
        this.config = new EpollServerChannelConfig(this);
    }

    EpollServerDomainSocketChannel(LinuxSocket fd) {
        super(fd);
        this.config = new EpollServerChannelConfig(this);
    }

    EpollServerDomainSocketChannel(LinuxSocket fd, boolean active) {
        super(fd, active);
        this.config = new EpollServerChannelConfig(this);
    }

    @Override // io.netty.channel.epoll.AbstractEpollServerChannel
    protected Channel newChildChannel(int fd, byte[] addr, int offset, int len) throws Exception {
        return new EpollDomainSocketChannel(this, new Socket(fd));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.AbstractChannel
    public DomainSocketAddress localAddress0() {
        return this.local;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.AbstractChannel
    public void doBind(SocketAddress localAddress) throws Exception {
        this.socket.bind(localAddress);
        this.socket.listen(this.config.getBacklog());
        this.local = (DomainSocketAddress) localAddress;
        this.active = true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.AbstractChannel
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

    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.Channel
    public EpollServerChannelConfig config() {
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
