package io.netty.channel.epoll;

import io.netty.channel.Channel;
import io.netty.channel.EventLoop;
import io.netty.channel.socket.InternetProtocolFamily;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.unix.NativeInetAddress;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/* loaded from: classes4.dex */
public final class EpollServerSocketChannel extends AbstractEpollServerChannel implements ServerSocketChannel {
    private final EpollServerSocketChannelConfig config;
    private volatile Collection<InetAddress> tcpMd5SigAddresses;

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public EpollServerSocketChannel() {
        this((InternetProtocolFamily) null);
    }

    public EpollServerSocketChannel(InternetProtocolFamily protocol) {
        super(LinuxSocket.newSocketStream(protocol), false);
        this.tcpMd5SigAddresses = Collections.emptyList();
        this.config = new EpollServerSocketChannelConfig(this);
    }

    public EpollServerSocketChannel(int fd) {
        this(new LinuxSocket(fd));
    }

    EpollServerSocketChannel(LinuxSocket fd) {
        super(fd);
        this.tcpMd5SigAddresses = Collections.emptyList();
        this.config = new EpollServerSocketChannelConfig(this);
    }

    EpollServerSocketChannel(LinuxSocket fd, boolean active) {
        super(fd, active);
        this.tcpMd5SigAddresses = Collections.emptyList();
        this.config = new EpollServerSocketChannelConfig(this);
    }

    @Override // io.netty.channel.epoll.AbstractEpollServerChannel, io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.AbstractChannel
    protected boolean isCompatible(EventLoop loop) {
        return loop instanceof EpollEventLoop;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.AbstractChannel
    public void doBind(SocketAddress localAddress) throws Exception {
        int tcpFastopen;
        super.doBind(localAddress);
        if (Native.IS_SUPPORTING_TCP_FASTOPEN_SERVER && (tcpFastopen = this.config.getTcpFastopen()) > 0) {
            this.socket.setTcpFastOpen(tcpFastopen);
        }
        this.socket.listen(this.config.getBacklog());
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

    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.Channel
    public EpollServerSocketChannelConfig config() {
        return this.config;
    }

    @Override // io.netty.channel.epoll.AbstractEpollServerChannel
    protected Channel newChildChannel(int fd, byte[] address, int offset, int len) throws Exception {
        return new EpollSocketChannel(this, new LinuxSocket(fd), NativeInetAddress.address(address, offset, len));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Collection<InetAddress> tcpMd5SigAddresses() {
        return this.tcpMd5SigAddresses;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setTcpMd5Sig(Map<InetAddress, byte[]> keys) throws IOException {
        synchronized (this) {
            this.tcpMd5SigAddresses = TcpMd5Util.newTcpMd5Sigs(this, this.tcpMd5SigAddresses, keys);
        }
    }
}
