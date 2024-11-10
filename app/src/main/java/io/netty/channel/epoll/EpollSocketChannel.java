package io.netty.channel.epoll;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.epoll.AbstractEpollChannel;
import io.netty.channel.epoll.AbstractEpollStreamChannel;
import io.netty.channel.socket.InternetProtocolFamily;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.Executor;

/* loaded from: classes4.dex */
public final class EpollSocketChannel extends AbstractEpollStreamChannel implements SocketChannel {
    private final EpollSocketChannelConfig config;
    private volatile Collection<InetAddress> tcpMd5SigAddresses;

    public EpollSocketChannel() {
        super(LinuxSocket.newSocketStream(), false);
        this.tcpMd5SigAddresses = Collections.emptyList();
        this.config = new EpollSocketChannelConfig(this);
    }

    public EpollSocketChannel(InternetProtocolFamily protocol) {
        super(LinuxSocket.newSocketStream(protocol), false);
        this.tcpMd5SigAddresses = Collections.emptyList();
        this.config = new EpollSocketChannelConfig(this);
    }

    public EpollSocketChannel(int fd) {
        super(fd);
        this.tcpMd5SigAddresses = Collections.emptyList();
        this.config = new EpollSocketChannelConfig(this);
    }

    EpollSocketChannel(LinuxSocket fd, boolean active) {
        super(fd, active);
        this.tcpMd5SigAddresses = Collections.emptyList();
        this.config = new EpollSocketChannelConfig(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public EpollSocketChannel(Channel parent, LinuxSocket fd, InetSocketAddress remoteAddress) {
        super(parent, fd, remoteAddress);
        this.tcpMd5SigAddresses = Collections.emptyList();
        this.config = new EpollSocketChannelConfig(this);
        if (parent instanceof EpollServerSocketChannel) {
            this.tcpMd5SigAddresses = ((EpollServerSocketChannel) parent).tcpMd5SigAddresses();
        }
    }

    public EpollTcpInfo tcpInfo() {
        return tcpInfo(new EpollTcpInfo());
    }

    public EpollTcpInfo tcpInfo(EpollTcpInfo info) {
        try {
            this.socket.getTcpInfo(info);
            return info;
        } catch (IOException e) {
            throw new ChannelException(e);
        }
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
    public EpollSocketChannelConfig config() {
        return this.config;
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public ServerSocketChannel parent() {
        return (ServerSocketChannel) super.parent();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.epoll.AbstractEpollStreamChannel, io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.AbstractChannel
    public AbstractEpollChannel.AbstractEpollUnsafe newUnsafe() {
        return new EpollSocketChannelUnsafe();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // io.netty.channel.epoll.AbstractEpollChannel
    public boolean doConnect0(SocketAddress remote) throws Exception {
        if (Native.IS_SUPPORTING_TCP_FASTOPEN_CLIENT && this.config.isTcpFastOpenConnect()) {
            ChannelOutboundBuffer outbound = unsafe().outboundBuffer();
            outbound.addFlush();
            Object curr = outbound.current();
            if (curr instanceof ByteBuf) {
                ByteBuf initialData = (ByteBuf) curr;
                long localFlushedAmount = doWriteOrSendBytes(initialData, (InetSocketAddress) remote, true);
                if (localFlushedAmount > 0) {
                    outbound.removeBytes(localFlushedAmount);
                    return true;
                }
            }
        }
        return super.doConnect0(remote);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class EpollSocketChannelUnsafe extends AbstractEpollStreamChannel.EpollStreamUnsafe {
        private EpollSocketChannelUnsafe() {
            super();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // io.netty.channel.epoll.AbstractEpollStreamChannel.EpollStreamUnsafe, io.netty.channel.AbstractChannel.AbstractUnsafe
        public Executor prepareToClose() {
            try {
                if (EpollSocketChannel.this.isOpen() && EpollSocketChannel.this.config().getSoLinger() > 0) {
                    ((EpollEventLoop) EpollSocketChannel.this.eventLoop()).remove(EpollSocketChannel.this);
                    return GlobalEventExecutor.INSTANCE;
                }
                return null;
            } catch (Throwable th) {
                return null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setTcpMd5Sig(Map<InetAddress, byte[]> keys) throws IOException {
        synchronized (this) {
            this.tcpMd5SigAddresses = TcpMd5Util.newTcpMd5Sigs(this, this.tcpMd5SigAddresses, keys);
        }
    }
}
