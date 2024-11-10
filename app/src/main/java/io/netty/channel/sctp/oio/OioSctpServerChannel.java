package io.netty.channel.sctp.oio;

import com.sun.nio.sctp.SctpChannel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPromise;
import io.netty.channel.oio.AbstractOioMessageChannel;
import io.netty.channel.sctp.DefaultSctpServerChannelConfig;
import io.netty.channel.sctp.SctpServerChannel;
import io.netty.channel.sctp.SctpServerChannelConfig;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Deprecated
/* loaded from: classes4.dex */
public class OioSctpServerChannel extends AbstractOioMessageChannel implements SctpServerChannel {
    private final SctpServerChannelConfig config;
    private final com.sun.nio.sctp.SctpServerChannel sch;
    private final Selector selector;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) OioSctpServerChannel.class);
    private static final ChannelMetadata METADATA = new ChannelMetadata(false, 1);

    private static com.sun.nio.sctp.SctpServerChannel newServerSocket() {
        try {
            return com.sun.nio.sctp.SctpServerChannel.open();
        } catch (IOException e) {
            throw new ChannelException("failed to create a sctp server channel", e);
        }
    }

    public OioSctpServerChannel() {
        this(newServerSocket());
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public OioSctpServerChannel(com.sun.nio.sctp.SctpServerChannel sch) {
        super(null);
        this.sch = (com.sun.nio.sctp.SctpServerChannel) ObjectUtil.checkNotNull(sch, "sctp server channel");
        try {
            try {
                sch.configureBlocking(false);
                this.selector = Selector.open();
                sch.register(this.selector, 16);
                this.config = new OioSctpServerChannelConfig(this, sch);
                if (1 == 0) {
                    try {
                        sch.close();
                    } catch (IOException e) {
                        logger.warn("Failed to close a sctp server channel.", (Throwable) e);
                    }
                }
            } catch (Exception e2) {
                throw new ChannelException("failed to initialize a sctp server channel", e2);
            }
        } catch (Throwable th) {
            if (0 == 0) {
                try {
                    sch.close();
                } catch (IOException e3) {
                    logger.warn("Failed to close a sctp server channel.", (Throwable) e3);
                }
            }
            throw th;
        }
    }

    @Override // io.netty.channel.Channel
    public ChannelMetadata metadata() {
        return METADATA;
    }

    @Override // io.netty.channel.Channel
    public SctpServerChannelConfig config() {
        return this.config;
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public InetSocketAddress remoteAddress() {
        return null;
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public InetSocketAddress localAddress() {
        return (InetSocketAddress) super.localAddress();
    }

    @Override // io.netty.channel.Channel
    public boolean isOpen() {
        return this.sch.isOpen();
    }

    @Override // io.netty.channel.AbstractChannel
    protected SocketAddress localAddress0() {
        try {
            Iterator<SocketAddress> i = this.sch.getAllLocalAddresses().iterator();
            if (i.hasNext()) {
                return i.next();
            }
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    @Override // io.netty.channel.sctp.SctpServerChannel
    public Set<InetSocketAddress> allLocalAddresses() {
        try {
            Set<SocketAddress> allLocalAddresses = this.sch.getAllLocalAddresses();
            Set<InetSocketAddress> addresses = new LinkedHashSet<>(allLocalAddresses.size());
            for (SocketAddress socketAddress : allLocalAddresses) {
                addresses.add((InetSocketAddress) socketAddress);
            }
            return addresses;
        } catch (Throwable th) {
            return Collections.emptySet();
        }
    }

    @Override // io.netty.channel.Channel
    public boolean isActive() {
        return isOpen() && localAddress0() != null;
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doBind(SocketAddress localAddress) throws Exception {
        this.sch.bind(localAddress, this.config.getBacklog());
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doClose() throws Exception {
        try {
            this.selector.close();
        } catch (IOException e) {
            logger.warn("Failed to close a selector.", (Throwable) e);
        }
        this.sch.close();
    }

    @Override // io.netty.channel.oio.AbstractOioMessageChannel
    protected int doReadMessages(List<Object> buf) throws Exception {
        if (!isActive()) {
            return -1;
        }
        SctpChannel s = null;
        int acceptedChannels = 0;
        try {
            int selectedKeys = this.selector.select(1000L);
            if (selectedKeys > 0) {
                Iterator<SelectionKey> selectionKeys = this.selector.selectedKeys().iterator();
                do {
                    SelectionKey key = selectionKeys.next();
                    selectionKeys.remove();
                    if (key.isAcceptable() && (s = this.sch.accept()) != null) {
                        buf.add(new OioSctpChannel(this, s));
                        acceptedChannels++;
                    }
                } while (selectionKeys.hasNext());
                return acceptedChannels;
            }
        } catch (Throwable t) {
            logger.warn("Failed to create a new channel from an accepted sctp channel.", t);
            if (s != null) {
                try {
                    s.close();
                } catch (Throwable t2) {
                    logger.warn("Failed to close a sctp channel.", t2);
                }
            }
        }
        return acceptedChannels;
    }

    @Override // io.netty.channel.sctp.SctpServerChannel
    public ChannelFuture bindAddress(InetAddress localAddress) {
        return bindAddress(localAddress, newPromise());
    }

    @Override // io.netty.channel.sctp.SctpServerChannel
    public ChannelFuture bindAddress(final InetAddress localAddress, final ChannelPromise promise) {
        if (eventLoop().inEventLoop()) {
            try {
                this.sch.bindAddress(localAddress);
                promise.setSuccess();
            } catch (Throwable t) {
                promise.setFailure(t);
            }
        } else {
            eventLoop().execute(new Runnable() { // from class: io.netty.channel.sctp.oio.OioSctpServerChannel.1
                @Override // java.lang.Runnable
                public void run() {
                    OioSctpServerChannel.this.bindAddress(localAddress, promise);
                }
            });
        }
        return promise;
    }

    @Override // io.netty.channel.sctp.SctpServerChannel
    public ChannelFuture unbindAddress(InetAddress localAddress) {
        return unbindAddress(localAddress, newPromise());
    }

    @Override // io.netty.channel.sctp.SctpServerChannel
    public ChannelFuture unbindAddress(final InetAddress localAddress, final ChannelPromise promise) {
        if (eventLoop().inEventLoop()) {
            try {
                this.sch.unbindAddress(localAddress);
                promise.setSuccess();
            } catch (Throwable t) {
                promise.setFailure(t);
            }
        } else {
            eventLoop().execute(new Runnable() { // from class: io.netty.channel.sctp.oio.OioSctpServerChannel.2
                @Override // java.lang.Runnable
                public void run() {
                    OioSctpServerChannel.this.unbindAddress(localAddress, promise);
                }
            });
        }
        return promise;
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

    @Override // io.netty.channel.AbstractChannel
    protected void doWrite(ChannelOutboundBuffer in) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override // io.netty.channel.AbstractChannel
    protected Object filterOutboundMessage(Object msg) throws Exception {
        throw new UnsupportedOperationException();
    }

    /* loaded from: classes4.dex */
    private final class OioSctpServerChannelConfig extends DefaultSctpServerChannelConfig {
        private OioSctpServerChannelConfig(OioSctpServerChannel channel, com.sun.nio.sctp.SctpServerChannel javaChannel) {
            super(channel, javaChannel);
        }

        @Override // io.netty.channel.DefaultChannelConfig
        protected void autoReadCleared() {
            OioSctpServerChannel.this.clearReadPending();
        }
    }
}
