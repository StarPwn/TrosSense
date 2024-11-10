package org.cloudburstmc.netty.channel.raknet;

import io.netty.channel.AbstractChannel;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultChannelPipeline;
import io.netty.channel.EventLoop;
import io.netty.util.ReferenceCountUtil;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.NonWritableChannelException;
import org.cloudburstmc.netty.channel.raknet.config.DefaultRakSessionConfig;
import org.cloudburstmc.netty.channel.raknet.config.RakChannelConfig;
import org.cloudburstmc.netty.handler.codec.raknet.common.ConnectedPingHandler;
import org.cloudburstmc.netty.handler.codec.raknet.common.ConnectedPongHandler;
import org.cloudburstmc.netty.handler.codec.raknet.common.DisconnectNotificationHandler;
import org.cloudburstmc.netty.handler.codec.raknet.common.RakAcknowledgeHandler;
import org.cloudburstmc.netty.handler.codec.raknet.common.RakDatagramCodec;
import org.cloudburstmc.netty.handler.codec.raknet.common.RakSessionCodec;
import org.cloudburstmc.netty.handler.codec.raknet.common.RakUnhandledMessagesQueue;
import org.cloudburstmc.netty.handler.codec.raknet.server.RakChildDatagramHandler;
import org.cloudburstmc.netty.handler.codec.raknet.server.RakServerOnlineInitialHandler;

/* loaded from: classes5.dex */
public class RakChildChannel extends AbstractChannel implements RakChannel {
    private static final ChannelMetadata metadata = new ChannelMetadata(true);
    private volatile boolean active;
    private final RakChannelConfig config;
    private volatile boolean open;
    private final DefaultChannelPipeline rakPipeline;
    private final InetSocketAddress remoteAddress;

    public RakChildChannel(InetSocketAddress remoteAddress, RakServerChannel parent, long guid, int version, int mtu) {
        super(parent);
        this.open = true;
        this.remoteAddress = remoteAddress;
        this.config = new DefaultRakSessionConfig(this);
        this.config.setGuid(guid);
        this.config.setProtocolVersion(version);
        this.config.setMtu(mtu);
        this.rakPipeline = new RakChannelPipeline(parent, this);
        this.rakPipeline.addLast(RakChildDatagramHandler.NAME, new RakChildDatagramHandler(this));
        RakSessionCodec sessionCodec = new RakSessionCodec(this);
        this.rakPipeline.addLast(RakDatagramCodec.NAME, new RakDatagramCodec());
        this.rakPipeline.addLast(RakAcknowledgeHandler.NAME, new RakAcknowledgeHandler(sessionCodec));
        this.rakPipeline.addLast(RakSessionCodec.NAME, sessionCodec);
        this.rakPipeline.addLast(ConnectedPingHandler.NAME, new ConnectedPingHandler());
        this.rakPipeline.addLast(ConnectedPongHandler.NAME, new ConnectedPongHandler(sessionCodec));
        this.rakPipeline.addLast(DisconnectNotificationHandler.NAME, DisconnectNotificationHandler.INSTANCE);
        this.rakPipeline.addLast(RakServerOnlineInitialHandler.NAME, new RakServerOnlineInitialHandler(this));
        this.rakPipeline.addLast(RakUnhandledMessagesQueue.NAME, new RakUnhandledMessagesQueue(this));
        this.rakPipeline.fireChannelRegistered();
        this.rakPipeline.fireChannelActive();
    }

    @Override // org.cloudburstmc.netty.channel.raknet.RakChannel
    public ChannelPipeline rakPipeline() {
        return this.rakPipeline;
    }

    @Override // io.netty.channel.AbstractChannel
    public SocketAddress localAddress0() {
        return parent().localAddress();
    }

    @Override // io.netty.channel.AbstractChannel
    public SocketAddress remoteAddress0() {
        return this.remoteAddress;
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public InetSocketAddress localAddress() {
        return (InetSocketAddress) super.localAddress();
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public InetSocketAddress remoteAddress() {
        return (InetSocketAddress) super.remoteAddress();
    }

    @Override // io.netty.channel.Channel
    public RakChannelConfig config() {
        return this.config;
    }

    @Override // io.netty.channel.Channel
    public ChannelMetadata metadata() {
        return metadata;
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doBind(SocketAddress socketAddress) throws Exception {
        throw new UnsupportedOperationException("Can not bind child channel!");
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doBeginRead() throws Exception {
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doWrite(ChannelOutboundBuffer in) throws Exception {
        if (!this.open) {
            throw new ClosedChannelException();
        }
        if (!this.active) {
            throw new NonWritableChannelException();
        }
        ClosedChannelException exception = null;
        while (true) {
            Object msg = in.current();
            if (msg != null) {
                try {
                    if (parent().isOpen()) {
                        this.rakPipeline.write(ReferenceCountUtil.retain(msg));
                        in.remove();
                    } else {
                        if (exception == null) {
                            exception = new ClosedChannelException();
                        }
                        in.remove(exception);
                    }
                } catch (Throwable cause) {
                    in.remove(cause);
                }
            } else {
                this.rakPipeline.flush();
                return;
            }
        }
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doDisconnect() throws Exception {
        close();
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doClose() throws Exception {
        this.open = false;
    }

    @Override // io.netty.channel.Channel
    public boolean isActive() {
        return isOpen() && this.active;
    }

    @Override // io.netty.channel.Channel
    public boolean isOpen() {
        return this.open;
    }

    @Override // io.netty.channel.AbstractChannel
    protected boolean isCompatible(EventLoop eventLoop) {
        return true;
    }

    @Override // io.netty.channel.AbstractChannel
    protected AbstractChannel.AbstractUnsafe newUnsafe() {
        return new AbstractChannel.AbstractUnsafe() { // from class: org.cloudburstmc.netty.channel.raknet.RakChildChannel.1
            @Override // io.netty.channel.Channel.Unsafe
            public void connect(SocketAddress socketAddress, SocketAddress socketAddress1, ChannelPromise channelPromise) {
                throw new UnsupportedOperationException("Can not connect child channel!");
            }
        };
    }
}
