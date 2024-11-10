package io.netty.channel.kqueue;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.kqueue.AbstractKQueueChannel;
import io.netty.channel.kqueue.AbstractKQueueStreamChannel;
import io.netty.channel.socket.InternetProtocolFamily;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.unix.IovArray;
import io.netty.util.concurrent.GlobalEventExecutor;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.Executor;

/* loaded from: classes4.dex */
public final class KQueueSocketChannel extends AbstractKQueueStreamChannel implements SocketChannel {
    private final KQueueSocketChannelConfig config;

    public KQueueSocketChannel() {
        super((Channel) null, BsdSocket.newSocketStream(), false);
        this.config = new KQueueSocketChannelConfig(this);
    }

    public KQueueSocketChannel(InternetProtocolFamily protocol) {
        super((Channel) null, BsdSocket.newSocketStream(protocol), false);
        this.config = new KQueueSocketChannelConfig(this);
    }

    public KQueueSocketChannel(int fd) {
        super(new BsdSocket(fd));
        this.config = new KQueueSocketChannelConfig(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public KQueueSocketChannel(Channel parent, BsdSocket fd, InetSocketAddress remoteAddress) {
        super(parent, fd, remoteAddress);
        this.config = new KQueueSocketChannelConfig(this);
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
    public KQueueSocketChannelConfig config() {
        return this.config;
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public ServerSocketChannel parent() {
        return (ServerSocketChannel) super.parent();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.kqueue.AbstractKQueueChannel
    public boolean doConnect0(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
        if (this.config.isTcpFastOpenConnect()) {
            ChannelOutboundBuffer outbound = unsafe().outboundBuffer();
            outbound.addFlush();
            Object curr = outbound.current();
            if (curr instanceof ByteBuf) {
                ByteBuf initialData = (ByteBuf) curr;
                if (initialData.isReadable()) {
                    IovArray iov = new IovArray(this.config.getAllocator().directBuffer());
                    try {
                        iov.add(initialData, initialData.readerIndex(), initialData.readableBytes());
                        int bytesSent = this.socket.connectx((InetSocketAddress) localAddress, (InetSocketAddress) remoteAddress, iov, true);
                        writeFilter(true);
                        outbound.removeBytes(Math.abs(bytesSent));
                        return bytesSent > 0;
                    } finally {
                        iov.release();
                    }
                }
            }
        }
        return super.doConnect0(remoteAddress, localAddress);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.kqueue.AbstractKQueueStreamChannel, io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.AbstractChannel
    public AbstractKQueueChannel.AbstractKQueueUnsafe newUnsafe() {
        return new KQueueSocketChannelUnsafe();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class KQueueSocketChannelUnsafe extends AbstractKQueueStreamChannel.KQueueStreamUnsafe {
        private KQueueSocketChannelUnsafe() {
            super();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // io.netty.channel.kqueue.AbstractKQueueStreamChannel.KQueueStreamUnsafe, io.netty.channel.AbstractChannel.AbstractUnsafe
        public Executor prepareToClose() {
            try {
                if (KQueueSocketChannel.this.isOpen() && KQueueSocketChannel.this.config().getSoLinger() > 0) {
                    ((KQueueEventLoop) KQueueSocketChannel.this.eventLoop()).remove(KQueueSocketChannel.this);
                    return GlobalEventExecutor.INSTANCE;
                }
                return null;
            } catch (Throwable th) {
                return null;
            }
        }
    }
}
