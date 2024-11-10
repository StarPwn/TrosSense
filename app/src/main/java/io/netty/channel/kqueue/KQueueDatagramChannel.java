package io.netty.channel.kqueue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.AddressedEnvelope;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultAddressedEnvelope;
import io.netty.channel.kqueue.AbstractKQueueChannel;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramChannelConfig;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.InternetProtocolFamily;
import io.netty.channel.unix.DatagramSocketAddress;
import io.netty.channel.unix.Errors;
import io.netty.channel.unix.IovArray;
import io.netty.channel.unix.UnixChannelUtil;
import io.netty.util.UncheckedBooleanSupplier;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.PortUnreachableException;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.UnresolvedAddressException;
import kotlin.text.Typography;

/* loaded from: classes4.dex */
public final class KQueueDatagramChannel extends AbstractKQueueDatagramChannel implements DatagramChannel {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final String EXPECTED_TYPES = " (expected: " + StringUtil.simpleClassName((Class<?>) DatagramPacket.class) + ", " + StringUtil.simpleClassName((Class<?>) AddressedEnvelope.class) + Typography.less + StringUtil.simpleClassName((Class<?>) ByteBuf.class) + ", " + StringUtil.simpleClassName((Class<?>) InetSocketAddress.class) + ">, " + StringUtil.simpleClassName((Class<?>) ByteBuf.class) + ')';
    private final KQueueDatagramChannelConfig config;
    private volatile boolean connected;

    @Override // io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.Channel
    public /* bridge */ /* synthetic */ boolean isOpen() {
        return super.isOpen();
    }

    @Override // io.netty.channel.kqueue.AbstractKQueueDatagramChannel, io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.Channel
    public /* bridge */ /* synthetic */ ChannelMetadata metadata() {
        return super.metadata();
    }

    public KQueueDatagramChannel() {
        super(null, BsdSocket.newSocketDgram(), false);
        this.config = new KQueueDatagramChannelConfig(this);
    }

    public KQueueDatagramChannel(InternetProtocolFamily protocol) {
        super(null, BsdSocket.newSocketDgram(protocol), false);
        this.config = new KQueueDatagramChannelConfig(this);
    }

    public KQueueDatagramChannel(int fd) {
        this(new BsdSocket(fd), true);
    }

    KQueueDatagramChannel(BsdSocket socket, boolean active) {
        super(null, socket, active);
        this.config = new KQueueDatagramChannelConfig(this);
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
    public boolean isActive() {
        return this.socket.isOpen() && ((this.config.getActiveOnOpen() && isRegistered()) || this.active);
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public boolean isConnected() {
        return this.connected;
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture joinGroup(InetAddress multicastAddress) {
        return joinGroup(multicastAddress, newPromise());
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture joinGroup(InetAddress multicastAddress, ChannelPromise promise) {
        try {
            NetworkInterface iface = config().getNetworkInterface();
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
        ObjectUtil.checkNotNull(multicastAddress, "multicastAddress");
        ObjectUtil.checkNotNull(networkInterface, "networkInterface");
        promise.setFailure((Throwable) new UnsupportedOperationException("Multicast not supported"));
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
        ObjectUtil.checkNotNull(multicastAddress, "multicastAddress");
        ObjectUtil.checkNotNull(networkInterface, "networkInterface");
        promise.setFailure((Throwable) new UnsupportedOperationException("Multicast not supported"));
        return promise;
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture block(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress sourceToBlock) {
        return block(multicastAddress, networkInterface, sourceToBlock, newPromise());
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture block(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress sourceToBlock, ChannelPromise promise) {
        ObjectUtil.checkNotNull(multicastAddress, "multicastAddress");
        ObjectUtil.checkNotNull(sourceToBlock, "sourceToBlock");
        ObjectUtil.checkNotNull(networkInterface, "networkInterface");
        promise.setFailure((Throwable) new UnsupportedOperationException("Multicast not supported"));
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
        } catch (Throwable e) {
            promise.setFailure(e);
            return promise;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.AbstractChannel
    public AbstractKQueueChannel.AbstractKQueueUnsafe newUnsafe() {
        return new KQueueDatagramChannelUnsafe();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.AbstractChannel
    public void doBind(SocketAddress localAddress) throws Exception {
        super.doBind(localAddress);
        this.active = true;
    }

    @Override // io.netty.channel.kqueue.AbstractKQueueDatagramChannel
    protected boolean doWriteMessage(Object msg) throws Exception {
        ByteBuf data;
        InetSocketAddress remoteAddress;
        long writtenBytes;
        if (msg instanceof AddressedEnvelope) {
            AddressedEnvelope<ByteBuf, InetSocketAddress> envelope = (AddressedEnvelope) msg;
            data = envelope.content();
            remoteAddress = envelope.recipient();
        } else {
            data = (ByteBuf) msg;
            remoteAddress = null;
        }
        int dataLen = data.readableBytes();
        if (dataLen == 0) {
            return true;
        }
        if (!data.hasMemoryAddress()) {
            if (data.nioBufferCount() > 1) {
                IovArray array = ((KQueueEventLoop) eventLoop()).cleanArray();
                array.add(data, data.readerIndex(), data.readableBytes());
                int cnt = array.count();
                if (cnt == 0) {
                    throw new AssertionError();
                }
                if (remoteAddress == null) {
                    writtenBytes = this.socket.writevAddresses(array.memoryAddress(0), cnt);
                } else {
                    writtenBytes = this.socket.sendToAddresses(array.memoryAddress(0), cnt, remoteAddress.getAddress(), remoteAddress.getPort());
                }
            } else {
                ByteBuffer nioData = data.internalNioBuffer(data.readerIndex(), data.readableBytes());
                if (remoteAddress == null) {
                    writtenBytes = this.socket.write(nioData, nioData.position(), nioData.limit());
                } else {
                    writtenBytes = this.socket.sendTo(nioData, nioData.position(), nioData.limit(), remoteAddress.getAddress(), remoteAddress.getPort());
                }
            }
        } else {
            long memoryAddress = data.memoryAddress();
            if (remoteAddress == null) {
                writtenBytes = this.socket.writeAddress(memoryAddress, data.readerIndex(), data.writerIndex());
            } else {
                writtenBytes = this.socket.sendToAddress(memoryAddress, data.readerIndex(), data.writerIndex(), remoteAddress.getAddress(), remoteAddress.getPort());
            }
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
            DatagramPacket packet = (DatagramPacket) msg;
            checkUnresolved(packet);
            ByteBuf content = (ByteBuf) packet.content();
            return UnixChannelUtil.isBufferCopyNeededForWrite(content) ? new DatagramPacket(newDirectBuffer(packet, content), packet.recipient()) : msg;
        }
        if (msg instanceof ByteBuf) {
            ByteBuf buf = (ByteBuf) msg;
            return UnixChannelUtil.isBufferCopyNeededForWrite(buf) ? newDirectBuffer(buf) : buf;
        }
        if (msg instanceof AddressedEnvelope) {
            AddressedEnvelope<Object, SocketAddress> e = (AddressedEnvelope) msg;
            checkUnresolved(e);
            if ((e.content() instanceof ByteBuf) && (e.recipient() == null || (e.recipient() instanceof InetSocketAddress))) {
                ByteBuf content2 = (ByteBuf) e.content();
                return UnixChannelUtil.isBufferCopyNeededForWrite(content2) ? new DefaultAddressedEnvelope(newDirectBuffer(e, content2), (InetSocketAddress) e.recipient()) : e;
            }
        }
        throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg) + EXPECTED_TYPES);
    }

    @Override // io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.Channel
    public KQueueDatagramChannelConfig config() {
        return this.config;
    }

    @Override // io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.AbstractChannel
    protected void doDisconnect() throws Exception {
        this.socket.disconnect();
        this.active = false;
        this.connected = false;
        resetCachedAddresses();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.kqueue.AbstractKQueueChannel
    public boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
        if (super.doConnect(remoteAddress, localAddress)) {
            this.connected = true;
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.AbstractChannel
    public void doClose() throws Exception {
        super.doClose();
        this.connected = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public final class KQueueDatagramChannelUnsafe extends AbstractKQueueChannel.AbstractKQueueUnsafe {
        static final /* synthetic */ boolean $assertionsDisabled = false;

        KQueueDatagramChannelUnsafe() {
            super();
        }

        @Override // io.netty.channel.kqueue.AbstractKQueueChannel.AbstractKQueueUnsafe
        void readReady(KQueueRecvByteAllocatorHandle allocHandle) {
            DatagramPacket packet;
            DatagramSocketAddress remoteAddress;
            if (!KQueueDatagramChannel.this.eventLoop().inEventLoop()) {
                throw new AssertionError();
            }
            DatagramChannelConfig config = KQueueDatagramChannel.this.config();
            if (KQueueDatagramChannel.this.shouldBreakReadReady(config)) {
                clearReadFilter0();
                return;
            }
            ChannelPipeline pipeline = KQueueDatagramChannel.this.pipeline();
            ByteBufAllocator allocator = config.getAllocator();
            allocHandle.reset(config);
            readReadyBefore();
            Throwable exception = null;
            ByteBuf byteBuf = null;
            try {
                try {
                    boolean connected = KQueueDatagramChannel.this.isConnected();
                    while (true) {
                        byteBuf = allocHandle.allocate(allocator);
                        allocHandle.attemptedBytesRead(byteBuf.writableBytes());
                        if (connected) {
                            try {
                                allocHandle.lastBytesRead(KQueueDatagramChannel.this.doReadBytes(byteBuf));
                                if (allocHandle.lastBytesRead() <= 0) {
                                    byteBuf.release();
                                    break;
                                }
                                packet = new DatagramPacket(byteBuf, (InetSocketAddress) localAddress(), (InetSocketAddress) remoteAddress());
                            } catch (Errors.NativeIoException e) {
                                if (e.expectedErr() != Errors.ERROR_ECONNREFUSED_NEGATIVE) {
                                    throw e;
                                }
                                PortUnreachableException error = new PortUnreachableException(e.getMessage());
                                error.initCause(e);
                                throw error;
                            }
                        } else {
                            if (byteBuf.hasMemoryAddress()) {
                                remoteAddress = KQueueDatagramChannel.this.socket.recvFromAddress(byteBuf.memoryAddress(), byteBuf.writerIndex(), byteBuf.capacity());
                            } else {
                                ByteBuffer nioData = byteBuf.internalNioBuffer(byteBuf.writerIndex(), byteBuf.writableBytes());
                                remoteAddress = KQueueDatagramChannel.this.socket.recvFrom(nioData, nioData.position(), nioData.limit());
                            }
                            if (remoteAddress == null) {
                                allocHandle.lastBytesRead(-1);
                                byteBuf.release();
                                break;
                            }
                            InetSocketAddress localAddress = remoteAddress.localAddress();
                            if (localAddress == null) {
                                localAddress = (InetSocketAddress) localAddress();
                            }
                            allocHandle.lastBytesRead(remoteAddress.receivedAmount());
                            byteBuf.writerIndex(byteBuf.writerIndex() + allocHandle.lastBytesRead());
                            packet = new DatagramPacket(byteBuf, localAddress, remoteAddress);
                        }
                        allocHandle.incMessagesRead(1);
                        this.readPending = false;
                        pipeline.fireChannelRead((Object) packet);
                        if (!allocHandle.continueReading(UncheckedBooleanSupplier.TRUE_SUPPLIER)) {
                            break;
                        }
                    }
                } catch (Throwable th) {
                    readReadyFinally(config);
                    throw th;
                }
            } catch (Throwable t) {
                if (byteBuf != null) {
                    byteBuf.release();
                }
                exception = t;
            }
            allocHandle.readComplete();
            pipeline.fireChannelReadComplete();
            if (exception != null) {
                pipeline.fireExceptionCaught(exception);
            }
            readReadyFinally(config);
        }
    }
}
