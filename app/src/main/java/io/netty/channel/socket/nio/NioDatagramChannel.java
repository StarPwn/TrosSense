package io.netty.channel.socket.nio;

import io.netty.buffer.ByteBuf;
import io.netty.channel.AddressedEnvelope;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultAddressedEnvelope;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.nio.AbstractNioMessageChannel;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramChannelConfig;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.InternetProtocolFamily;
import io.netty.util.UncheckedBooleanSupplier;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.SocketUtils;
import io.netty.util.internal.StringUtil;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.MembershipKey;
import java.nio.channels.UnresolvedAddressException;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import kotlin.text.Typography;

/* loaded from: classes4.dex */
public final class NioDatagramChannel extends AbstractNioMessageChannel implements DatagramChannel {
    private final DatagramChannelConfig config;
    private Map<InetAddress, List<MembershipKey>> memberships;
    private static final ChannelMetadata METADATA = new ChannelMetadata(true, 16);
    private static final SelectorProvider DEFAULT_SELECTOR_PROVIDER = SelectorProvider.provider();
    private static final String EXPECTED_TYPES = " (expected: " + StringUtil.simpleClassName((Class<?>) DatagramPacket.class) + ", " + StringUtil.simpleClassName((Class<?>) AddressedEnvelope.class) + Typography.less + StringUtil.simpleClassName((Class<?>) ByteBuf.class) + ", " + StringUtil.simpleClassName((Class<?>) SocketAddress.class) + ">, " + StringUtil.simpleClassName((Class<?>) ByteBuf.class) + ')';

    private static java.nio.channels.DatagramChannel newSocket(SelectorProvider provider) {
        try {
            return provider.openDatagramChannel();
        } catch (IOException e) {
            throw new ChannelException("Failed to open a socket.", e);
        }
    }

    private static java.nio.channels.DatagramChannel newSocket(SelectorProvider provider, InternetProtocolFamily ipFamily) {
        if (ipFamily == null) {
            return newSocket(provider);
        }
        checkJavaVersion();
        try {
            return provider.openDatagramChannel(ProtocolFamilyConverter.convert(ipFamily));
        } catch (IOException e) {
            throw new ChannelException("Failed to open a socket.", e);
        }
    }

    private static void checkJavaVersion() {
        if (PlatformDependent.javaVersion() < 7) {
            throw new UnsupportedOperationException("Only supported on java 7+.");
        }
    }

    public NioDatagramChannel() {
        this(newSocket(DEFAULT_SELECTOR_PROVIDER));
    }

    public NioDatagramChannel(SelectorProvider provider) {
        this(newSocket(provider));
    }

    public NioDatagramChannel(InternetProtocolFamily ipFamily) {
        this(newSocket(DEFAULT_SELECTOR_PROVIDER, ipFamily));
    }

    public NioDatagramChannel(SelectorProvider provider, InternetProtocolFamily ipFamily) {
        this(newSocket(provider, ipFamily));
    }

    public NioDatagramChannel(java.nio.channels.DatagramChannel socket) {
        super(null, socket, 1);
        this.config = new NioDatagramChannelConfig(this, socket);
    }

    @Override // io.netty.channel.Channel
    public ChannelMetadata metadata() {
        return METADATA;
    }

    @Override // io.netty.channel.Channel
    public DatagramChannelConfig config() {
        return this.config;
    }

    @Override // io.netty.channel.Channel
    public boolean isActive() {
        java.nio.channels.DatagramChannel ch = mo214javaChannel();
        return ch.isOpen() && ((((Boolean) this.config.getOption(ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION)).booleanValue() && isRegistered()) || ch.socket().isBound());
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public boolean isConnected() {
        return mo214javaChannel().isConnected();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.nio.AbstractNioChannel
    /* renamed from: javaChannel */
    public java.nio.channels.DatagramChannel mo214javaChannel() {
        return (java.nio.channels.DatagramChannel) super.mo214javaChannel();
    }

    @Override // io.netty.channel.AbstractChannel
    protected SocketAddress localAddress0() {
        return mo214javaChannel().socket().getLocalSocketAddress();
    }

    @Override // io.netty.channel.AbstractChannel
    protected SocketAddress remoteAddress0() {
        return mo214javaChannel().socket().getRemoteSocketAddress();
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doBind(SocketAddress localAddress) throws Exception {
        doBind0(localAddress);
    }

    private void doBind0(SocketAddress localAddress) throws Exception {
        if (PlatformDependent.javaVersion() >= 7) {
            SocketUtils.bind(mo214javaChannel(), localAddress);
        } else {
            mo214javaChannel().socket().bind(localAddress);
        }
    }

    @Override // io.netty.channel.nio.AbstractNioChannel
    protected boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
        if (localAddress != null) {
            doBind0(localAddress);
        }
        boolean success = false;
        try {
            mo214javaChannel().connect(remoteAddress);
            success = true;
        } finally {
            if (!success) {
                doClose();
            }
        }
    }

    @Override // io.netty.channel.nio.AbstractNioChannel
    protected void doFinishConnect() throws Exception {
        throw new Error();
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doDisconnect() throws Exception {
        mo214javaChannel().disconnect();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.nio.AbstractNioChannel, io.netty.channel.AbstractChannel
    public void doClose() throws Exception {
        mo214javaChannel().close();
    }

    @Override // io.netty.channel.nio.AbstractNioMessageChannel
    protected int doReadMessages(List<Object> buf) throws Exception {
        java.nio.channels.DatagramChannel ch = mo214javaChannel();
        DatagramChannelConfig config = config();
        RecvByteBufAllocator.Handle allocHandle = unsafe().recvBufAllocHandle();
        ByteBuf data = allocHandle.allocate(config.getAllocator());
        allocHandle.attemptedBytesRead(data.writableBytes());
        try {
            ByteBuffer nioData = data.internalNioBuffer(data.writerIndex(), data.writableBytes());
            int pos = nioData.position();
            InetSocketAddress remoteAddress = (InetSocketAddress) ch.receive(nioData);
            if (remoteAddress == null) {
            }
            allocHandle.lastBytesRead(nioData.position() - pos);
            buf.add(new DatagramPacket(data.writerIndex(data.writerIndex() + allocHandle.lastBytesRead()), localAddress(), remoteAddress));
            if (0 == 0) {
                return 1;
            }
            data.release();
            return 1;
        } catch (Throwable cause) {
            try {
                PlatformDependent.throwException(cause);
                if (1 == 0) {
                    return -1;
                }
                data.release();
                return -1;
            } finally {
                if (1 != 0) {
                    data.release();
                }
            }
        }
    }

    @Override // io.netty.channel.nio.AbstractNioMessageChannel
    protected boolean doWriteMessage(Object msg, ChannelOutboundBuffer in) throws Exception {
        ByteBuf data;
        SocketAddress remoteAddress;
        int writtenBytes;
        if (msg instanceof AddressedEnvelope) {
            AddressedEnvelope<ByteBuf, SocketAddress> envelope = (AddressedEnvelope) msg;
            remoteAddress = envelope.recipient();
            data = envelope.content();
        } else {
            data = (ByteBuf) msg;
            remoteAddress = null;
        }
        int dataLen = data.readableBytes();
        if (dataLen == 0) {
            return true;
        }
        ByteBuffer nioData = data.nioBufferCount() == 1 ? data.internalNioBuffer(data.readerIndex(), dataLen) : data.nioBuffer(data.readerIndex(), dataLen);
        if (remoteAddress != null) {
            writtenBytes = mo214javaChannel().send(nioData, remoteAddress);
        } else {
            writtenBytes = mo214javaChannel().write(nioData);
        }
        return writtenBytes > 0;
    }

    private static void checkUnresolved(AddressedEnvelope<?, ?> envelope) {
        if ((envelope.recipient() instanceof InetSocketAddress) && ((InetSocketAddress) envelope.recipient()).isUnresolved()) {
            throw new UnresolvedAddressException();
        }
    }

    @Override // io.netty.channel.AbstractChannel
    protected Object filterOutboundMessage(Object msg) {
        if (msg instanceof DatagramPacket) {
            DatagramPacket p = (DatagramPacket) msg;
            checkUnresolved(p);
            ByteBuf content = (ByteBuf) p.content();
            if (isSingleDirectBuffer(content)) {
                return p;
            }
            return new DatagramPacket(newDirectBuffer(p, content), p.recipient());
        }
        if (msg instanceof ByteBuf) {
            ByteBuf buf = (ByteBuf) msg;
            if (isSingleDirectBuffer(buf)) {
                return buf;
            }
            return newDirectBuffer(buf);
        }
        if (msg instanceof AddressedEnvelope) {
            AddressedEnvelope<Object, SocketAddress> e = (AddressedEnvelope) msg;
            checkUnresolved(e);
            if (e.content() instanceof ByteBuf) {
                ByteBuf content2 = (ByteBuf) e.content();
                if (isSingleDirectBuffer(content2)) {
                    return e;
                }
                return new DefaultAddressedEnvelope(newDirectBuffer(e, content2), e.recipient());
            }
        }
        throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg) + EXPECTED_TYPES);
    }

    private static boolean isSingleDirectBuffer(ByteBuf buf) {
        return buf.isDirect() && buf.nioBufferCount() == 1;
    }

    @Override // io.netty.channel.nio.AbstractNioMessageChannel
    protected boolean continueOnWriteError() {
        return true;
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public InetSocketAddress localAddress() {
        return (InetSocketAddress) super.localAddress();
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public InetSocketAddress remoteAddress() {
        return (InetSocketAddress) super.remoteAddress();
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture joinGroup(InetAddress multicastAddress) {
        return joinGroup(multicastAddress, newPromise());
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture joinGroup(InetAddress multicastAddress, ChannelPromise promise) {
        try {
            NetworkInterface iface = this.config.getNetworkInterface();
            if (iface == null) {
                iface = NetworkInterface.getByInetAddress(localAddress().getAddress());
            }
            return joinGroup(multicastAddress, iface, null, promise);
        } catch (SocketException e) {
            promise.setFailure((Throwable) e);
            return promise;
        }
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture joinGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface) {
        return joinGroup(multicastAddress, networkInterface, newPromise());
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture joinGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface, ChannelPromise promise) {
        return joinGroup(multicastAddress.getAddress(), networkInterface, null, promise);
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture joinGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source) {
        return joinGroup(multicastAddress, networkInterface, source, newPromise());
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture joinGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source, ChannelPromise promise) {
        MembershipKey key;
        checkJavaVersion();
        ObjectUtil.checkNotNull(multicastAddress, "multicastAddress");
        ObjectUtil.checkNotNull(networkInterface, "networkInterface");
        try {
            if (source == null) {
                key = mo214javaChannel().join(multicastAddress, networkInterface);
            } else {
                key = mo214javaChannel().join(multicastAddress, networkInterface, source);
            }
            synchronized (this) {
                List<MembershipKey> keys = null;
                if (this.memberships == null) {
                    this.memberships = new HashMap();
                } else {
                    keys = this.memberships.get(multicastAddress);
                }
                if (keys == null) {
                    keys = new ArrayList();
                    this.memberships.put(multicastAddress, keys);
                }
                keys.add(key);
            }
            promise.setSuccess();
        } catch (Throwable e) {
            promise.setFailure(e);
        }
        return promise;
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture leaveGroup(InetAddress multicastAddress) {
        return leaveGroup(multicastAddress, newPromise());
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture leaveGroup(InetAddress multicastAddress, ChannelPromise promise) {
        try {
            return leaveGroup(multicastAddress, NetworkInterface.getByInetAddress(localAddress().getAddress()), null, promise);
        } catch (SocketException e) {
            promise.setFailure((Throwable) e);
            return promise;
        }
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture leaveGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface) {
        return leaveGroup(multicastAddress, networkInterface, newPromise());
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture leaveGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface, ChannelPromise promise) {
        return leaveGroup(multicastAddress.getAddress(), networkInterface, null, promise);
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture leaveGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source) {
        return leaveGroup(multicastAddress, networkInterface, source, newPromise());
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture leaveGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source, ChannelPromise promise) {
        List<MembershipKey> keys;
        checkJavaVersion();
        ObjectUtil.checkNotNull(multicastAddress, "multicastAddress");
        ObjectUtil.checkNotNull(networkInterface, "networkInterface");
        synchronized (this) {
            if (this.memberships != null && (keys = this.memberships.get(multicastAddress)) != null) {
                Iterator<MembershipKey> keyIt = keys.iterator();
                while (keyIt.hasNext()) {
                    MembershipKey key = keyIt.next();
                    if (networkInterface.equals(key.networkInterface()) && ((source == null && key.sourceAddress() == null) || (source != null && source.equals(key.sourceAddress())))) {
                        key.drop();
                        keyIt.remove();
                    }
                }
                if (keys.isEmpty()) {
                    this.memberships.remove(multicastAddress);
                }
            }
        }
        promise.setSuccess();
        return promise;
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture block(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress sourceToBlock) {
        return block(multicastAddress, networkInterface, sourceToBlock, newPromise());
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture block(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress sourceToBlock, ChannelPromise promise) {
        checkJavaVersion();
        ObjectUtil.checkNotNull(multicastAddress, "multicastAddress");
        ObjectUtil.checkNotNull(sourceToBlock, "sourceToBlock");
        ObjectUtil.checkNotNull(networkInterface, "networkInterface");
        synchronized (this) {
            if (this.memberships != null) {
                List<MembershipKey> keys = this.memberships.get(multicastAddress);
                for (MembershipKey key : keys) {
                    if (networkInterface.equals(key.networkInterface())) {
                        try {
                            key.block(sourceToBlock);
                        } catch (IOException e) {
                            promise.setFailure((Throwable) e);
                        }
                    }
                }
            }
        }
        promise.setSuccess();
        return promise;
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture block(InetAddress multicastAddress, InetAddress sourceToBlock) {
        return block(multicastAddress, sourceToBlock, newPromise());
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture block(InetAddress multicastAddress, InetAddress sourceToBlock, ChannelPromise promise) {
        try {
            return block(multicastAddress, NetworkInterface.getByInetAddress(localAddress().getAddress()), sourceToBlock, promise);
        } catch (SocketException e) {
            promise.setFailure((Throwable) e);
            return promise;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.nio.AbstractNioChannel
    @Deprecated
    public void setReadPending(boolean readPending) {
        super.setReadPending(readPending);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearReadPending0() {
        clearReadPending();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.nio.AbstractNioMessageChannel
    public boolean closeOnReadError(Throwable cause) {
        if (cause instanceof SocketException) {
            return false;
        }
        return super.closeOnReadError(cause);
    }

    @Override // io.netty.channel.nio.AbstractNioMessageChannel
    protected boolean continueReading(RecvByteBufAllocator.Handle allocHandle) {
        if (allocHandle instanceof RecvByteBufAllocator.ExtendedHandle) {
            return ((RecvByteBufAllocator.ExtendedHandle) allocHandle).continueReading(UncheckedBooleanSupplier.TRUE_SUPPLIER);
        }
        return allocHandle.continueReading();
    }
}
