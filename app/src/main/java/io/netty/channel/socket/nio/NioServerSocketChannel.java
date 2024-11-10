package io.netty.channel.socket.nio;

import io.netty.channel.ChannelException;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.nio.AbstractNioMessageChannel;
import io.netty.channel.socket.DefaultServerSocketChannelConfig;
import io.netty.channel.socket.InternetProtocolFamily;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.ServerSocketChannelConfig;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.SocketUtils;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.List;
import java.util.Map;

/* loaded from: classes4.dex */
public class NioServerSocketChannel extends AbstractNioMessageChannel implements ServerSocketChannel {
    private final ServerSocketChannelConfig config;
    private static final ChannelMetadata METADATA = new ChannelMetadata(false, 16);
    private static final SelectorProvider DEFAULT_SELECTOR_PROVIDER = SelectorProvider.provider();
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) NioServerSocketChannel.class);
    private static final Method OPEN_SERVER_SOCKET_CHANNEL_WITH_FAMILY = SelectorProviderUtil.findOpenMethod("openServerSocketChannel");

    private static java.nio.channels.ServerSocketChannel newChannel(SelectorProvider provider, InternetProtocolFamily family) {
        try {
            java.nio.channels.ServerSocketChannel channel = (java.nio.channels.ServerSocketChannel) SelectorProviderUtil.newChannel(OPEN_SERVER_SOCKET_CHANNEL_WITH_FAMILY, provider, family);
            return channel == null ? provider.openServerSocketChannel() : channel;
        } catch (IOException e) {
            throw new ChannelException("Failed to open a socket.", e);
        }
    }

    public NioServerSocketChannel() {
        this(DEFAULT_SELECTOR_PROVIDER);
    }

    public NioServerSocketChannel(SelectorProvider provider) {
        this(provider, null);
    }

    public NioServerSocketChannel(SelectorProvider provider, InternetProtocolFamily family) {
        this(newChannel(provider, family));
    }

    public NioServerSocketChannel(java.nio.channels.ServerSocketChannel channel) {
        super(null, channel, 16);
        this.config = new NioServerSocketChannelConfig(this, mo214javaChannel().socket());
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
    public ServerSocketChannelConfig config() {
        return this.config;
    }

    @Override // io.netty.channel.Channel
    public boolean isActive() {
        return isOpen() && mo214javaChannel().socket().isBound();
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public InetSocketAddress remoteAddress() {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.nio.AbstractNioChannel
    /* renamed from: javaChannel */
    public java.nio.channels.ServerSocketChannel mo214javaChannel() {
        return (java.nio.channels.ServerSocketChannel) super.mo214javaChannel();
    }

    @Override // io.netty.channel.AbstractChannel
    protected SocketAddress localAddress0() {
        return SocketUtils.localSocketAddress(mo214javaChannel().socket());
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doBind(SocketAddress localAddress) throws Exception {
        if (PlatformDependent.javaVersion() >= 7) {
            mo214javaChannel().bind(localAddress, this.config.getBacklog());
        } else {
            mo214javaChannel().socket().bind(localAddress, this.config.getBacklog());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.nio.AbstractNioChannel, io.netty.channel.AbstractChannel
    public void doClose() throws Exception {
        mo214javaChannel().close();
    }

    @Override // io.netty.channel.nio.AbstractNioMessageChannel
    protected int doReadMessages(List<Object> buf) throws Exception {
        SocketChannel ch = SocketUtils.accept(mo214javaChannel());
        if (ch != null) {
            try {
                buf.add(new NioSocketChannel(this, ch));
                return 1;
            } catch (Throwable t) {
                logger.warn("Failed to create a new channel from an accepted socket.", t);
                try {
                    ch.close();
                    return 0;
                } catch (Throwable t2) {
                    logger.warn("Failed to close a socket.", t2);
                    return 0;
                }
            }
        }
        return 0;
    }

    @Override // io.netty.channel.nio.AbstractNioChannel
    protected boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override // io.netty.channel.nio.AbstractNioChannel
    protected void doFinishConnect() throws Exception {
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

    @Override // io.netty.channel.nio.AbstractNioMessageChannel
    protected boolean doWriteMessage(Object msg, ChannelOutboundBuffer in) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override // io.netty.channel.AbstractChannel
    protected final Object filterOutboundMessage(Object msg) throws Exception {
        throw new UnsupportedOperationException();
    }

    /* loaded from: classes4.dex */
    private final class NioServerSocketChannelConfig extends DefaultServerSocketChannelConfig {
        private NioServerSocketChannelConfig(NioServerSocketChannel channel, ServerSocket javaSocket) {
            super(channel, javaSocket);
        }

        @Override // io.netty.channel.DefaultChannelConfig
        protected void autoReadCleared() {
            NioServerSocketChannel.this.clearReadPending();
        }

        @Override // io.netty.channel.socket.DefaultServerSocketChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
        public <T> boolean setOption(ChannelOption<T> option, T value) {
            if (PlatformDependent.javaVersion() >= 7 && (option instanceof NioChannelOption)) {
                return NioChannelOption.setOption(jdkChannel(), (NioChannelOption) option, value);
            }
            return super.setOption(option, value);
        }

        @Override // io.netty.channel.socket.DefaultServerSocketChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
        public <T> T getOption(ChannelOption<T> channelOption) {
            if (PlatformDependent.javaVersion() >= 7 && (channelOption instanceof NioChannelOption)) {
                return (T) NioChannelOption.getOption(jdkChannel(), (NioChannelOption) channelOption);
            }
            return (T) super.getOption(channelOption);
        }

        @Override // io.netty.channel.socket.DefaultServerSocketChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
        public Map<ChannelOption<?>, Object> getOptions() {
            if (PlatformDependent.javaVersion() >= 7) {
                return getOptions(super.getOptions(), NioChannelOption.getOptions(jdkChannel()));
            }
            return super.getOptions();
        }

        private java.nio.channels.ServerSocketChannel jdkChannel() {
            return ((NioServerSocketChannel) this.channel).mo214javaChannel();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.nio.AbstractNioMessageChannel
    public boolean closeOnReadError(Throwable cause) {
        return super.closeOnReadError(cause);
    }
}
