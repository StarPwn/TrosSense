package io.netty.handler.ssl.ocsp;

import io.netty.channel.ChannelFactory;
import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public final class IoTransport {
    public static final IoTransport DEFAULT = new IoTransport(new NioEventLoopGroup(1).next(), new ChannelFactory<SocketChannel>() { // from class: io.netty.handler.ssl.ocsp.IoTransport.1
        @Override // io.netty.channel.ChannelFactory, io.netty.bootstrap.ChannelFactory
        public SocketChannel newChannel() {
            return new NioSocketChannel();
        }
    }, new ChannelFactory<DatagramChannel>() { // from class: io.netty.handler.ssl.ocsp.IoTransport.2
        @Override // io.netty.channel.ChannelFactory, io.netty.bootstrap.ChannelFactory
        public DatagramChannel newChannel() {
            return new NioDatagramChannel();
        }
    });
    private final ChannelFactory<DatagramChannel> datagramChannel;
    private final EventLoop eventLoop;
    private final ChannelFactory<SocketChannel> socketChannel;

    public static IoTransport create(EventLoop eventLoop, ChannelFactory<SocketChannel> socketChannel, ChannelFactory<DatagramChannel> datagramChannel) {
        return new IoTransport(eventLoop, socketChannel, datagramChannel);
    }

    private IoTransport(EventLoop eventLoop, ChannelFactory<SocketChannel> socketChannel, ChannelFactory<DatagramChannel> datagramChannel) {
        this.eventLoop = (EventLoop) ObjectUtil.checkNotNull(eventLoop, "EventLoop");
        this.socketChannel = (ChannelFactory) ObjectUtil.checkNotNull(socketChannel, "SocketChannel");
        this.datagramChannel = (ChannelFactory) ObjectUtil.checkNotNull(datagramChannel, "DatagramChannel");
    }

    public EventLoop eventLoop() {
        return this.eventLoop;
    }

    public ChannelFactory<SocketChannel> socketChannel() {
        return this.socketChannel;
    }

    public ChannelFactory<DatagramChannel> datagramChannel() {
        return this.datagramChannel;
    }
}
