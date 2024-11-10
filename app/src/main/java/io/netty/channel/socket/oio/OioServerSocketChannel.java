package io.netty.channel.socket.oio;

import io.netty.channel.ChannelException;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.oio.AbstractOioMessageChannel;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.SocketUtils;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.List;

@Deprecated
/* loaded from: classes4.dex */
public class OioServerSocketChannel extends AbstractOioMessageChannel implements ServerSocketChannel {
    private final OioServerSocketChannelConfig config;
    final ServerSocket socket;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) OioServerSocketChannel.class);
    private static final ChannelMetadata METADATA = new ChannelMetadata(false, 1);

    private static ServerSocket newServerSocket() {
        try {
            return new ServerSocket();
        } catch (IOException e) {
            throw new ChannelException("failed to create a server socket", e);
        }
    }

    public OioServerSocketChannel() {
        this(newServerSocket());
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public OioServerSocketChannel(ServerSocket socket) {
        super(null);
        ObjectUtil.checkNotNull(socket, "socket");
        boolean success = false;
        try {
            try {
                socket.setSoTimeout(1000);
                success = true;
                this.socket = socket;
                this.config = new DefaultOioServerSocketChannelConfig(this, socket);
            } catch (IOException e) {
                throw new ChannelException("Failed to set the server socket timeout.", e);
            }
        } finally {
            if (!success) {
                try {
                    socket.close();
                } catch (IOException e2) {
                    if (logger.isWarnEnabled()) {
                        logger.warn("Failed to close a partially initialized socket.", (Throwable) e2);
                    }
                }
            }
        }
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public InetSocketAddress localAddress() {
        return (InetSocketAddress) super.localAddress();
    }

    @Override // io.netty.channel.Channel
    public ChannelMetadata metadata() {
        return METADATA;
    }

    @Override // io.netty.channel.Channel
    public OioServerSocketChannelConfig config() {
        return this.config;
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public InetSocketAddress remoteAddress() {
        return null;
    }

    @Override // io.netty.channel.Channel
    public boolean isOpen() {
        return !this.socket.isClosed();
    }

    @Override // io.netty.channel.Channel
    public boolean isActive() {
        return isOpen() && this.socket.isBound();
    }

    @Override // io.netty.channel.AbstractChannel
    protected SocketAddress localAddress0() {
        return SocketUtils.localSocketAddress(this.socket);
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doBind(SocketAddress localAddress) throws Exception {
        this.socket.bind(localAddress, this.config.getBacklog());
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doClose() throws Exception {
        this.socket.close();
    }

    @Override // io.netty.channel.oio.AbstractOioMessageChannel
    protected int doReadMessages(List<Object> buf) throws Exception {
        if (this.socket.isClosed()) {
            return -1;
        }
        try {
            Socket s = this.socket.accept();
            try {
                buf.add(new OioSocketChannel(this, s));
                return 1;
            } catch (Throwable t) {
                logger.warn("Failed to create a new channel from an accepted socket.", t);
                try {
                    s.close();
                    return 0;
                } catch (Throwable t2) {
                    logger.warn("Failed to close a socket.", t2);
                    return 0;
                }
            }
        } catch (SocketTimeoutException e) {
            return 0;
        }
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doWrite(ChannelOutboundBuffer in) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override // io.netty.channel.AbstractChannel
    protected Object filterOutboundMessage(Object msg) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override // io.netty.channel.oio.AbstractOioChannel
    protected void doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override // io.netty.channel.AbstractChannel
    protected SocketAddress remoteAddress0() {
        return null;
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doDisconnect() throws Exception {
        throw new UnsupportedOperationException();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.oio.AbstractOioChannel
    @Deprecated
    public void setReadPending(boolean readPending) {
        super.setReadPending(readPending);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void clearReadPending0() {
        super.clearReadPending();
    }
}
