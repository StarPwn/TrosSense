package io.netty.channel.sctp.nio;

import com.sun.nio.sctp.Association;
import com.sun.nio.sctp.MessageInfo;
import com.sun.nio.sctp.NotificationHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPromise;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.nio.AbstractNioMessageChannel;
import io.netty.channel.sctp.DefaultSctpChannelConfig;
import io.netty.channel.sctp.SctpChannel;
import io.netty.channel.sctp.SctpChannelConfig;
import io.netty.channel.sctp.SctpMessage;
import io.netty.channel.sctp.SctpNotificationHandler;
import io.netty.channel.sctp.SctpServerChannel;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/* loaded from: classes4.dex */
public class NioSctpChannel extends AbstractNioMessageChannel implements SctpChannel {
    private static final ChannelMetadata METADATA = new ChannelMetadata(false);
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) NioSctpChannel.class);
    private final SctpChannelConfig config;
    private final NotificationHandler<?> notificationHandler;

    private static com.sun.nio.sctp.SctpChannel newSctpChannel() {
        try {
            return com.sun.nio.sctp.SctpChannel.open();
        } catch (IOException e) {
            throw new ChannelException("Failed to open a sctp channel.", e);
        }
    }

    public NioSctpChannel() {
        this(newSctpChannel());
    }

    public NioSctpChannel(com.sun.nio.sctp.SctpChannel sctpChannel) {
        this(null, sctpChannel);
    }

    public NioSctpChannel(Channel parent, com.sun.nio.sctp.SctpChannel sctpChannel) {
        super(parent, sctpChannel, 1);
        try {
            sctpChannel.configureBlocking(false);
            this.config = new NioSctpChannelConfig(this, sctpChannel);
            this.notificationHandler = new SctpNotificationHandler(this);
        } catch (IOException e) {
            try {
                sctpChannel.close();
            } catch (IOException e2) {
                if (logger.isWarnEnabled()) {
                    logger.warn("Failed to close a partially initialized sctp channel.", (Throwable) e2);
                }
            }
            throw new ChannelException("Failed to enter non-blocking mode.", e);
        }
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public InetSocketAddress localAddress() {
        return (InetSocketAddress) super.localAddress();
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public InetSocketAddress remoteAddress() {
        return (InetSocketAddress) super.remoteAddress();
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public SctpServerChannel parent() {
        return (SctpServerChannel) super.parent();
    }

    @Override // io.netty.channel.Channel
    public ChannelMetadata metadata() {
        return METADATA;
    }

    @Override // io.netty.channel.sctp.SctpChannel
    public Association association() {
        try {
            return mo214javaChannel().association();
        } catch (IOException e) {
            return null;
        }
    }

    @Override // io.netty.channel.sctp.SctpChannel
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
    public SctpChannelConfig config() {
        return this.config;
    }

    @Override // io.netty.channel.sctp.SctpChannel
    public Set<InetSocketAddress> allRemoteAddresses() {
        try {
            Set<SocketAddress> allLocalAddresses = mo214javaChannel().getRemoteAddresses();
            Set<InetSocketAddress> addresses = new HashSet<>(allLocalAddresses.size());
            for (SocketAddress socketAddress : allLocalAddresses) {
                addresses.add((InetSocketAddress) socketAddress);
            }
            return addresses;
        } catch (Throwable th) {
            return Collections.emptySet();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.nio.AbstractNioChannel
    /* renamed from: javaChannel, reason: merged with bridge method [inline-methods] */
    public com.sun.nio.sctp.SctpChannel mo214javaChannel() {
        return super.mo214javaChannel();
    }

    @Override // io.netty.channel.Channel
    public boolean isActive() {
        com.sun.nio.sctp.SctpChannel ch = mo214javaChannel();
        return ch.isOpen() && association() != null;
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
    protected SocketAddress remoteAddress0() {
        try {
            Iterator<SocketAddress> i = mo214javaChannel().getRemoteAddresses().iterator();
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
        mo214javaChannel().bind(localAddress);
    }

    @Override // io.netty.channel.nio.AbstractNioChannel
    protected boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
        if (localAddress != null) {
            mo214javaChannel().bind(localAddress);
        }
        boolean success = false;
        try {
            boolean connected = mo214javaChannel().connect(remoteAddress);
            if (!connected) {
                selectionKey().interestOps(8);
            }
            success = true;
            return connected;
        } finally {
            if (!success) {
                doClose();
            }
        }
    }

    @Override // io.netty.channel.nio.AbstractNioChannel
    protected void doFinishConnect() throws Exception {
        if (!mo214javaChannel().finishConnect()) {
            throw new Error();
        }
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doDisconnect() throws Exception {
        doClose();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.nio.AbstractNioChannel, io.netty.channel.AbstractChannel
    public void doClose() throws Exception {
        mo214javaChannel().close();
    }

    @Override // io.netty.channel.nio.AbstractNioMessageChannel
    protected int doReadMessages(List<Object> buf) throws Exception {
        com.sun.nio.sctp.SctpChannel ch = mo214javaChannel();
        RecvByteBufAllocator.Handle allocHandle = unsafe().recvBufAllocHandle();
        ByteBuf buffer = allocHandle.allocate(config().getAllocator());
        try {
            ByteBuffer data = buffer.internalNioBuffer(buffer.writerIndex(), buffer.writableBytes());
            int pos = data.position();
            MessageInfo messageInfo = ch.receive(data, (Object) null, this.notificationHandler);
            if (messageInfo == null) {
            }
            allocHandle.lastBytesRead(data.position() - pos);
            buf.add(new SctpMessage(messageInfo, buffer.writerIndex(buffer.writerIndex() + allocHandle.lastBytesRead())));
            if (0 == 0) {
                return 1;
            }
            buffer.release();
            return 1;
        } catch (Throwable cause) {
            try {
                PlatformDependent.throwException(cause);
                if (1 == 0) {
                    return -1;
                }
                buffer.release();
                return -1;
            } finally {
                if (1 != 0) {
                    buffer.release();
                }
            }
        }
    }

    @Override // io.netty.channel.nio.AbstractNioMessageChannel
    protected boolean doWriteMessage(Object msg, ChannelOutboundBuffer in) throws Exception {
        SctpMessage packet = (SctpMessage) msg;
        ByteBuf data = packet.content();
        int dataLen = data.readableBytes();
        if (dataLen == 0) {
            return true;
        }
        ByteBufAllocator alloc = alloc();
        boolean needsCopy = data.nioBufferCount() != 1;
        if (!needsCopy && !data.isDirect() && alloc.isDirectBufferPooled()) {
            needsCopy = true;
        }
        if (needsCopy) {
            data = alloc.directBuffer(dataLen).writeBytes(data);
        }
        ByteBuffer nioData = data.nioBuffer();
        MessageInfo mi = MessageInfo.createOutgoing(association(), (SocketAddress) null, packet.streamIdentifier());
        mi.payloadProtocolID(packet.protocolIdentifier());
        mi.streamNumber(packet.streamIdentifier());
        mi.unordered(packet.isUnordered());
        int writtenBytes = mo214javaChannel().send(nioData, mi);
        return writtenBytes > 0;
    }

    @Override // io.netty.channel.AbstractChannel
    protected final Object filterOutboundMessage(Object msg) throws Exception {
        if (msg instanceof SctpMessage) {
            SctpMessage m = (SctpMessage) msg;
            ByteBuf buf = m.content();
            if (buf.isDirect() && buf.nioBufferCount() == 1) {
                return m;
            }
            return new SctpMessage(m.protocolIdentifier(), m.streamIdentifier(), m.isUnordered(), newDirectBuffer(m, buf));
        }
        throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg) + " (expected: " + StringUtil.simpleClassName((Class<?>) SctpMessage.class));
    }

    @Override // io.netty.channel.sctp.SctpChannel
    public ChannelFuture bindAddress(InetAddress localAddress) {
        return bindAddress(localAddress, newPromise());
    }

    @Override // io.netty.channel.sctp.SctpChannel
    public ChannelFuture bindAddress(final InetAddress localAddress, final ChannelPromise promise) {
        if (eventLoop().inEventLoop()) {
            try {
                mo214javaChannel().bindAddress(localAddress);
                promise.setSuccess();
            } catch (Throwable t) {
                promise.setFailure(t);
            }
        } else {
            eventLoop().execute(new Runnable() { // from class: io.netty.channel.sctp.nio.NioSctpChannel.1
                @Override // java.lang.Runnable
                public void run() {
                    NioSctpChannel.this.bindAddress(localAddress, promise);
                }
            });
        }
        return promise;
    }

    @Override // io.netty.channel.sctp.SctpChannel
    public ChannelFuture unbindAddress(InetAddress localAddress) {
        return unbindAddress(localAddress, newPromise());
    }

    @Override // io.netty.channel.sctp.SctpChannel
    public ChannelFuture unbindAddress(final InetAddress localAddress, final ChannelPromise promise) {
        if (eventLoop().inEventLoop()) {
            try {
                mo214javaChannel().unbindAddress(localAddress);
                promise.setSuccess();
            } catch (Throwable t) {
                promise.setFailure(t);
            }
        } else {
            eventLoop().execute(new Runnable() { // from class: io.netty.channel.sctp.nio.NioSctpChannel.2
                @Override // java.lang.Runnable
                public void run() {
                    NioSctpChannel.this.unbindAddress(localAddress, promise);
                }
            });
        }
        return promise;
    }

    /* loaded from: classes4.dex */
    private final class NioSctpChannelConfig extends DefaultSctpChannelConfig {
        private NioSctpChannelConfig(NioSctpChannel channel, com.sun.nio.sctp.SctpChannel javaChannel) {
            super(channel, javaChannel);
        }

        @Override // io.netty.channel.DefaultChannelConfig
        protected void autoReadCleared() {
            NioSctpChannel.this.clearReadPending();
        }
    }
}
