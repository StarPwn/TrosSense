package io.netty.channel.sctp.nio;

import com.sun.nio.sctp.SctpChannel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPromise;
import io.netty.channel.nio.AbstractNioMessageChannel;
import io.netty.channel.sctp.DefaultSctpServerChannelConfig;
import io.netty.channel.sctp.SctpServerChannel;
import io.netty.channel.sctp.SctpServerChannelConfig;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/* loaded from: classes4.dex */
public class NioSctpServerChannel extends AbstractNioMessageChannel implements SctpServerChannel {
    private static final ChannelMetadata METADATA = new ChannelMetadata(false, 16);
    private final SctpServerChannelConfig config;

    private static com.sun.nio.sctp.SctpServerChannel newSocket() {
        try {
            return com.sun.nio.sctp.SctpServerChannel.open();
        } catch (IOException e) {
            throw new ChannelException("Failed to open a server socket.", e);
        }
    }

    public NioSctpServerChannel() {
        super(null, newSocket(), 16);
        this.config = new NioSctpServerChannelConfig(this, mo214javaChannel());
    }

    @Override // io.netty.channel.Channel
    public ChannelMetadata metadata() {
        return METADATA;
    }

    @Override // io.netty.channel.sctp.SctpServerChannel
    public Set<InetSocketAddress> allLocalAddresses() {
        try {
            Set<SocketAddress> allLocalAddresses = mo214javaChannel().getAllLocalAddresses();
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
    public SctpServerChannelConfig config() {
        return this.config;
    }

    @Override // io.netty.channel.Channel
    public boolean isActive() {
        return isOpen() && !allLocalAddresses().isEmpty();
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public InetSocketAddress remoteAddress() {
        return null;
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public InetSocketAddress localAddress() {
        return (InetSocketAddress) super.localAddress();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.nio.AbstractNioChannel
    /* renamed from: javaChannel, reason: merged with bridge method [inline-methods] */
    public com.sun.nio.sctp.SctpServerChannel mo214javaChannel() {
        return super.mo214javaChannel();
    }

    @Override // io.netty.channel.AbstractChannel
    protected SocketAddress localAddress0() {
        try {
            Iterator<SocketAddress> i = mo214javaChannel().getAllLocalAddresses().iterator();
            if (i.hasNext()) {
                return i.next();
            }
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doBind(SocketAddress localAddress) throws Exception {
        mo214javaChannel().bind(localAddress, this.config.getBacklog());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.nio.AbstractNioChannel, io.netty.channel.AbstractChannel
    public void doClose() throws Exception {
        mo214javaChannel().close();
    }

    @Override // io.netty.channel.nio.AbstractNioMessageChannel
    protected int doReadMessages(List<Object> buf) throws Exception {
        SctpChannel ch = mo214javaChannel().accept();
        if (ch == null) {
            return 0;
        }
        buf.add(new NioSctpChannel(this, ch));
        return 1;
    }

    @Override // io.netty.channel.sctp.SctpServerChannel
    public ChannelFuture bindAddress(InetAddress localAddress) {
        return bindAddress(localAddress, newPromise());
    }

    @Override // io.netty.channel.sctp.SctpServerChannel
    public ChannelFuture bindAddress(final InetAddress localAddress, final ChannelPromise promise) {
        if (eventLoop().inEventLoop()) {
            try {
                mo214javaChannel().bindAddress(localAddress);
                promise.setSuccess();
            } catch (Throwable t) {
                promise.setFailure(t);
            }
        } else {
            eventLoop().execute(new Runnable() { // from class: io.netty.channel.sctp.nio.NioSctpServerChannel.1
                @Override // java.lang.Runnable
                public void run() {
                    NioSctpServerChannel.this.bindAddress(localAddress, promise);
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
                mo214javaChannel().unbindAddress(localAddress);
                promise.setSuccess();
            } catch (Throwable t) {
                promise.setFailure(t);
            }
        } else {
            eventLoop().execute(new Runnable() { // from class: io.netty.channel.sctp.nio.NioSctpServerChannel.2
                @Override // java.lang.Runnable
                public void run() {
                    NioSctpServerChannel.this.unbindAddress(localAddress, promise);
                }
            });
        }
        return promise;
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
    protected Object filterOutboundMessage(Object msg) throws Exception {
        throw new UnsupportedOperationException();
    }

    /* loaded from: classes4.dex */
    private final class NioSctpServerChannelConfig extends DefaultSctpServerChannelConfig {
        private NioSctpServerChannelConfig(NioSctpServerChannel channel, com.sun.nio.sctp.SctpServerChannel javaChannel) {
            super(channel, javaChannel);
        }

        @Override // io.netty.channel.DefaultChannelConfig
        protected void autoReadCleared() {
            NioSctpServerChannel.this.clearReadPending();
        }
    }
}
