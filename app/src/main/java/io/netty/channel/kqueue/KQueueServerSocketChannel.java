package io.netty.channel.kqueue;

import io.netty.channel.Channel;
import io.netty.channel.EventLoop;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.unix.NativeInetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/* loaded from: classes4.dex */
public final class KQueueServerSocketChannel extends AbstractKQueueServerChannel implements ServerSocketChannel {
    private final KQueueServerSocketChannelConfig config;

    public KQueueServerSocketChannel() {
        super(BsdSocket.newSocketStream(), false);
        this.config = new KQueueServerSocketChannelConfig(this);
    }

    public KQueueServerSocketChannel(int fd) {
        this(new BsdSocket(fd));
    }

    KQueueServerSocketChannel(BsdSocket fd) {
        super(fd);
        this.config = new KQueueServerSocketChannelConfig(this);
    }

    KQueueServerSocketChannel(BsdSocket fd, boolean active) {
        super(fd, active);
        this.config = new KQueueServerSocketChannelConfig(this);
    }

    @Override // io.netty.channel.kqueue.AbstractKQueueServerChannel, io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.AbstractChannel
    protected boolean isCompatible(EventLoop loop) {
        return loop instanceof KQueueEventLoop;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.AbstractChannel
    public void doBind(SocketAddress localAddress) throws Exception {
        super.doBind(localAddress);
        this.socket.listen(this.config.getBacklog());
        if (this.config.isTcpFastOpen()) {
            this.socket.setTcpFastOpen(true);
        }
        this.active = true;
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public InetSocketAddress remoteAddress() {
        return (InetSocketAddress) super.remoteAddress();
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public InetSocketAddress localAddress() {
        return (InetSocketAddress) super.localAddress();
    }

    @Override // io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.Channel
    public KQueueServerSocketChannelConfig config() {
        return this.config;
    }

    @Override // io.netty.channel.kqueue.AbstractKQueueServerChannel
    protected Channel newChildChannel(int fd, byte[] address, int offset, int len) throws Exception {
        return new KQueueSocketChannel(this, new BsdSocket(fd), NativeInetAddress.address(address, offset, len));
    }
}
