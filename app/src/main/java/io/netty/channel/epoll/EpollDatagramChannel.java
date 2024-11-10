package io.netty.channel.epoll;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.AddressedEnvelope;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultAddressedEnvelope;
import io.netty.channel.epoll.AbstractEpollChannel;
import io.netty.channel.epoll.NativeDatagramPacketArray;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.InternetProtocolFamily;
import io.netty.channel.unix.Errors;
import io.netty.channel.unix.UnixChannelUtil;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.RecyclableArrayList;
import io.netty.util.internal.StringUtil;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.PortUnreachableException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.UnresolvedAddressException;
import kotlin.text.Typography;

/* loaded from: classes4.dex */
public final class EpollDatagramChannel extends AbstractEpollChannel implements DatagramChannel {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final EpollDatagramChannelConfig config;
    private volatile boolean connected;
    private static final ChannelMetadata METADATA = new ChannelMetadata(true, 16);
    private static final String EXPECTED_TYPES = " (expected: " + StringUtil.simpleClassName((Class<?>) DatagramPacket.class) + ", " + StringUtil.simpleClassName((Class<?>) AddressedEnvelope.class) + Typography.less + StringUtil.simpleClassName((Class<?>) ByteBuf.class) + ", " + StringUtil.simpleClassName((Class<?>) InetSocketAddress.class) + ">, " + StringUtil.simpleClassName((Class<?>) ByteBuf.class) + ')';

    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.Channel
    public /* bridge */ /* synthetic */ boolean isOpen() {
        return super.isOpen();
    }

    public static boolean isSegmentedDatagramPacketSupported() {
        return Epoll.isAvailable() && Native.IS_SUPPORTING_SENDMMSG && Native.IS_SUPPORTING_UDP_SEGMENT;
    }

    public EpollDatagramChannel() {
        this((InternetProtocolFamily) null);
    }

    public EpollDatagramChannel(InternetProtocolFamily family) {
        this(LinuxSocket.newSocketDgram(family), false);
    }

    public EpollDatagramChannel(int fd) {
        this(new LinuxSocket(fd), true);
    }

    private EpollDatagramChannel(LinuxSocket fd, boolean active) {
        super((Channel) null, fd, active);
        this.config = new EpollDatagramChannelConfig(this);
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public InetSocketAddress remoteAddress() {
        return (InetSocketAddress) super.remoteAddress();
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public InetSocketAddress localAddress() {
        return (InetSocketAddress) super.localAddress();
    }

    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.Channel
    public ChannelMetadata metadata() {
        return METADATA;
    }

    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.Channel
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
        } catch (IOException e) {
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
    public ChannelFuture joinGroup(final InetAddress multicastAddress, final NetworkInterface networkInterface, final InetAddress source, final ChannelPromise promise) {
        ObjectUtil.checkNotNull(multicastAddress, "multicastAddress");
        ObjectUtil.checkNotNull(networkInterface, "networkInterface");
        if (eventLoop().inEventLoop()) {
            joinGroup0(multicastAddress, networkInterface, source, promise);
        } else {
            eventLoop().execute(new Runnable() { // from class: io.netty.channel.epoll.EpollDatagramChannel.1
                @Override // java.lang.Runnable
                public void run() {
                    EpollDatagramChannel.this.joinGroup0(multicastAddress, networkInterface, source, promise);
                }
            });
        }
        return promise;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void joinGroup0(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source, ChannelPromise promise) {
        if (!eventLoop().inEventLoop()) {
            throw new AssertionError();
        }
        try {
            this.socket.joinGroup(multicastAddress, networkInterface, source);
            promise.setSuccess();
        } catch (IOException e) {
            promise.setFailure((Throwable) e);
        }
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture leaveGroup(InetAddress multicastAddress) {
        return leaveGroup(multicastAddress, newPromise());
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture leaveGroup(InetAddress multicastAddress, ChannelPromise promise) {
        try {
            return leaveGroup(multicastAddress, NetworkInterface.getByInetAddress(localAddress().getAddress()), null, promise);
        } catch (IOException e) {
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
    public ChannelFuture leaveGroup(final InetAddress multicastAddress, final NetworkInterface networkInterface, final InetAddress source, final ChannelPromise promise) {
        ObjectUtil.checkNotNull(multicastAddress, "multicastAddress");
        ObjectUtil.checkNotNull(networkInterface, "networkInterface");
        if (eventLoop().inEventLoop()) {
            leaveGroup0(multicastAddress, networkInterface, source, promise);
        } else {
            eventLoop().execute(new Runnable() { // from class: io.netty.channel.epoll.EpollDatagramChannel.2
                @Override // java.lang.Runnable
                public void run() {
                    EpollDatagramChannel.this.leaveGroup0(multicastAddress, networkInterface, source, promise);
                }
            });
        }
        return promise;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void leaveGroup0(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source, ChannelPromise promise) {
        if (!eventLoop().inEventLoop()) {
            throw new AssertionError();
        }
        try {
            this.socket.leaveGroup(multicastAddress, networkInterface, source);
            promise.setSuccess();
        } catch (IOException e) {
            promise.setFailure((Throwable) e);
        }
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
        promise.setFailure((Throwable) new UnsupportedOperationException("Multicast block not supported"));
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
    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.AbstractChannel
    public AbstractEpollChannel.AbstractEpollUnsafe newUnsafe() {
        return new EpollDatagramChannelUnsafe();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.AbstractChannel
    public void doBind(SocketAddress localAddress) throws Exception {
        if (localAddress instanceof InetSocketAddress) {
            InetSocketAddress socketAddress = (InetSocketAddress) localAddress;
            if (socketAddress.getAddress().isAnyLocalAddress() && (socketAddress.getAddress() instanceof Inet4Address) && this.socket.family() == InternetProtocolFamily.IPv6) {
                localAddress = new InetSocketAddress(Native.INET6_ANY, socketAddress.getPort());
            }
        }
        super.doBind(localAddress);
        this.active = true;
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doWrite(ChannelOutboundBuffer in) throws Exception {
        int maxMessagesPerWrite = maxMessagesPerWrite();
        while (maxMessagesPerWrite > 0) {
            Object msg = in.current();
            if (msg == null) {
                break;
            }
            try {
            } catch (IOException e) {
                maxMessagesPerWrite--;
                in.remove(e);
            }
            if ((Native.IS_SUPPORTING_SENDMMSG && in.size() > 1) || (in.current() instanceof io.netty.channel.unix.SegmentedDatagramPacket)) {
                NativeDatagramPacketArray array = cleanDatagramPacketArray();
                array.add(in, isConnected(), maxMessagesPerWrite);
                int cnt = array.count();
                if (cnt >= 1) {
                    NativeDatagramPacketArray.NativeDatagramPacket[] packets = array.packets();
                    int send = this.socket.sendmmsg(packets, 0, cnt);
                    if (send == 0) {
                        break;
                    }
                    for (int i = 0; i < send; i++) {
                        in.remove();
                    }
                    maxMessagesPerWrite -= send;
                }
            }
            boolean done = false;
            int i2 = config().getWriteSpinCount();
            while (true) {
                if (i2 <= 0) {
                    break;
                }
                if (doWriteMessage(msg)) {
                    done = true;
                    break;
                }
                i2--;
            }
            if (!done) {
                break;
            }
            in.remove();
            maxMessagesPerWrite--;
        }
        if (in.isEmpty()) {
            clearFlag(Native.EPOLLOUT);
        } else {
            setFlag(Native.EPOLLOUT);
        }
    }

    private boolean doWriteMessage(Object msg) throws Exception {
        ByteBuf data;
        InetSocketAddress remoteAddress;
        if (msg instanceof AddressedEnvelope) {
            AddressedEnvelope<ByteBuf, InetSocketAddress> envelope = (AddressedEnvelope) msg;
            data = envelope.content();
            remoteAddress = envelope.recipient();
        } else {
            data = (ByteBuf) msg;
            remoteAddress = null;
        }
        int dataLen = data.readableBytes();
        if (dataLen == 0 || doWriteOrSendBytes(data, remoteAddress, false) > 0) {
            return true;
        }
        return false;
    }

    private static void checkUnresolved(AddressedEnvelope<?, ?> envelope) {
        if ((envelope.recipient() instanceof InetSocketAddress) && ((InetSocketAddress) envelope.recipient()).isUnresolved()) {
            throw new UnresolvedAddressException();
        }
    }

    @Override // io.netty.channel.AbstractChannel
    protected Object filterOutboundMessage(Object msg) {
        if (msg instanceof io.netty.channel.unix.SegmentedDatagramPacket) {
            if (!Native.IS_SUPPORTING_UDP_SEGMENT) {
                throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg) + EXPECTED_TYPES);
            }
            io.netty.channel.unix.SegmentedDatagramPacket packet = (io.netty.channel.unix.SegmentedDatagramPacket) msg;
            checkUnresolved(packet);
            ByteBuf content = (ByteBuf) packet.content();
            return UnixChannelUtil.isBufferCopyNeededForWrite(content) ? packet.replace(newDirectBuffer(packet, content)) : msg;
        }
        if (msg instanceof DatagramPacket) {
            DatagramPacket packet2 = (DatagramPacket) msg;
            checkUnresolved(packet2);
            ByteBuf content2 = (ByteBuf) packet2.content();
            return UnixChannelUtil.isBufferCopyNeededForWrite(content2) ? new DatagramPacket(newDirectBuffer(packet2, content2), packet2.recipient()) : msg;
        }
        if (msg instanceof ByteBuf) {
            ByteBuf buf = (ByteBuf) msg;
            return UnixChannelUtil.isBufferCopyNeededForWrite(buf) ? newDirectBuffer(buf) : buf;
        }
        if (msg instanceof AddressedEnvelope) {
            AddressedEnvelope<Object, SocketAddress> e = (AddressedEnvelope) msg;
            checkUnresolved(e);
            if ((e.content() instanceof ByteBuf) && (e.recipient() == null || (e.recipient() instanceof InetSocketAddress))) {
                ByteBuf content3 = (ByteBuf) e.content();
                return UnixChannelUtil.isBufferCopyNeededForWrite(content3) ? new DefaultAddressedEnvelope(newDirectBuffer(e, content3), (InetSocketAddress) e.recipient()) : e;
            }
        }
        throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg) + EXPECTED_TYPES);
    }

    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.Channel
    public EpollDatagramChannelConfig config() {
        return this.config;
    }

    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.AbstractChannel
    protected void doDisconnect() throws Exception {
        this.socket.disconnect();
        this.active = false;
        this.connected = false;
        resetCachedAddresses();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.epoll.AbstractEpollChannel
    public boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
        if (super.doConnect(remoteAddress, localAddress)) {
            this.connected = true;
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.AbstractChannel
    public void doClose() throws Exception {
        super.doClose();
        this.connected = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public final class EpollDatagramChannelUnsafe extends AbstractEpollChannel.AbstractEpollUnsafe {
        static final /* synthetic */ boolean $assertionsDisabled = false;

        EpollDatagramChannelUnsafe() {
            super();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:46:0x00bd A[Catch: all -> 0x00c6, TryCatch #1 {all -> 0x00c6, blocks: (B:10:0x0040, B:11:0x0047, B:38:0x006d, B:41:0x0074, B:21:0x00ab, B:18:0x007f, B:46:0x00bd, B:47:0x00c3, B:48:0x00c5, B:50:0x0094, B:53:0x00a4, B:60:0x0061), top: B:9:0x0040 }] */
        /* JADX WARN: Removed duplicated region for block: B:48:0x00c5 A[Catch: all -> 0x00c6, TRY_LEAVE, TryCatch #1 {all -> 0x00c6, blocks: (B:10:0x0040, B:11:0x0047, B:38:0x006d, B:41:0x0074, B:21:0x00ab, B:18:0x007f, B:46:0x00bd, B:47:0x00c3, B:48:0x00c5, B:50:0x0094, B:53:0x00a4, B:60:0x0061), top: B:9:0x0040 }] */
        @Override // io.netty.channel.epoll.AbstractEpollChannel.AbstractEpollUnsafe
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void epollInReady() {
            /*
                Method dump skipped, instructions count: 227
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: io.netty.channel.epoll.EpollDatagramChannel.EpollDatagramChannelUnsafe.epollInReady():void");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean connectedRead(EpollRecvByteAllocatorHandle allocHandle, ByteBuf byteBuf, int maxDatagramPacketSize) throws Exception {
        int localReadAmount;
        try {
            int writable = maxDatagramPacketSize != 0 ? Math.min(byteBuf.writableBytes(), maxDatagramPacketSize) : byteBuf.writableBytes();
            allocHandle.attemptedBytesRead(writable);
            int writerIndex = byteBuf.writerIndex();
            if (byteBuf.hasMemoryAddress()) {
                localReadAmount = this.socket.recvAddress(byteBuf.memoryAddress(), writerIndex, writerIndex + writable);
            } else {
                ByteBuffer buf = byteBuf.internalNioBuffer(writerIndex, writable);
                localReadAmount = this.socket.recv(buf, buf.position(), buf.limit());
            }
            if (localReadAmount <= 0) {
                allocHandle.lastBytesRead(localReadAmount);
            }
            byteBuf.writerIndex(writerIndex + localReadAmount);
            allocHandle.lastBytesRead(maxDatagramPacketSize <= 0 ? localReadAmount : writable);
            DatagramPacket packet = new DatagramPacket(byteBuf, localAddress(), remoteAddress());
            allocHandle.incMessagesRead(1);
            pipeline().fireChannelRead((Object) packet);
            ByteBuf byteBuf2 = null;
            if (0 != 0) {
                byteBuf2.release();
            }
            return true;
        } finally {
            if (byteBuf != null) {
                byteBuf.release();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public IOException translateForConnected(Errors.NativeIoException e) {
        if (e.expectedErr() == Errors.ERROR_ECONNREFUSED_NEGATIVE) {
            PortUnreachableException error = new PortUnreachableException(e.getMessage());
            error.initCause(e);
            return error;
        }
        return e;
    }

    private static void addDatagramPacketToOut(DatagramPacket packet, RecyclableArrayList out) {
        if (packet instanceof io.netty.channel.unix.SegmentedDatagramPacket) {
            io.netty.channel.unix.SegmentedDatagramPacket segmentedDatagramPacket = (io.netty.channel.unix.SegmentedDatagramPacket) packet;
            ByteBuf content = (ByteBuf) segmentedDatagramPacket.content();
            InetSocketAddress recipient = segmentedDatagramPacket.recipient();
            InetSocketAddress sender = segmentedDatagramPacket.sender();
            int segmentSize = segmentedDatagramPacket.segmentSize();
            do {
                out.add(new DatagramPacket(content.readRetainedSlice(Math.min(content.readableBytes(), segmentSize)), recipient, sender));
            } while (content.isReadable());
            segmentedDatagramPacket.release();
            return;
        }
        out.add(packet);
    }

    private static void releaseAndRecycle(ByteBuf byteBuf, RecyclableArrayList packetList) {
        if (byteBuf != null) {
            byteBuf.release();
        }
        if (packetList != null) {
            for (int i = 0; i < packetList.size(); i++) {
                ReferenceCountUtil.release(packetList.get(i));
            }
            packetList.recycle();
        }
    }

    private static void processPacket(ChannelPipeline pipeline, EpollRecvByteAllocatorHandle handle, int bytesRead, DatagramPacket packet) {
        handle.lastBytesRead(Math.max(1, bytesRead));
        handle.incMessagesRead(1);
        pipeline.fireChannelRead((Object) packet);
    }

    private static void processPacketList(ChannelPipeline pipeline, EpollRecvByteAllocatorHandle handle, int bytesRead, RecyclableArrayList packetList) {
        int messagesRead = packetList.size();
        handle.lastBytesRead(Math.max(1, bytesRead));
        handle.incMessagesRead(messagesRead);
        for (int i = 0; i < messagesRead; i++) {
            pipeline.fireChannelRead(packetList.set(i, Unpooled.EMPTY_BUFFER));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean recvmsg(EpollRecvByteAllocatorHandle allocHandle, NativeDatagramPacketArray array, ByteBuf byteBuf) throws IOException {
        RecyclableArrayList datagramPackets = null;
        try {
            int writable = byteBuf.writableBytes();
            boolean added = array.addWritable(byteBuf, byteBuf.writerIndex(), writable);
            if (!added) {
                throw new AssertionError();
            }
            allocHandle.attemptedBytesRead(writable);
            NativeDatagramPacketArray.NativeDatagramPacket msg = array.packets()[0];
            int bytesReceived = this.socket.recvmsg(msg);
            if (!msg.hasSender()) {
                allocHandle.lastBytesRead(-1);
                return false;
            }
            byteBuf.writerIndex(bytesReceived);
            InetSocketAddress local = localAddress();
            DatagramPacket packet = msg.newDatagramPacket(byteBuf, local);
            if (!(packet instanceof io.netty.channel.unix.SegmentedDatagramPacket)) {
                processPacket(pipeline(), allocHandle, bytesReceived, packet);
            } else {
                RecyclableArrayList datagramPackets2 = RecyclableArrayList.newInstance();
                addDatagramPacketToOut(packet, datagramPackets2);
                processPacketList(pipeline(), allocHandle, bytesReceived, datagramPackets2);
                datagramPackets2.recycle();
                datagramPackets = null;
            }
            releaseAndRecycle(byteBuf, datagramPackets);
            return true;
        } finally {
            releaseAndRecycle(byteBuf, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean scatteringRead(EpollRecvByteAllocatorHandle allocHandle, NativeDatagramPacketArray array, ByteBuf byteBuf, int datagramSize, int numDatagram) throws IOException {
        RecyclableArrayList datagramPackets = null;
        try {
            int offset = byteBuf.writerIndex();
            int i = 0;
            while (i < numDatagram && array.addWritable(byteBuf, offset, datagramSize)) {
                i++;
                offset += datagramSize;
            }
            int i2 = byteBuf.writerIndex();
            allocHandle.attemptedBytesRead(offset - i2);
            NativeDatagramPacketArray.NativeDatagramPacket[] packets = array.packets();
            int received = this.socket.recvmmsg(packets, 0, array.count());
            if (received == 0) {
                allocHandle.lastBytesRead(-1);
                return false;
            }
            InetSocketAddress local = localAddress();
            int bytesReceived = received * datagramSize;
            byteBuf.writerIndex(byteBuf.writerIndex() + bytesReceived);
            if (received == 1) {
                DatagramPacket packet = packets[0].newDatagramPacket(byteBuf, local);
                if (!(packet instanceof io.netty.channel.unix.SegmentedDatagramPacket)) {
                    processPacket(pipeline(), allocHandle, datagramSize, packet);
                    return true;
                }
            }
            RecyclableArrayList datagramPackets2 = RecyclableArrayList.newInstance();
            for (int i3 = 0; i3 < received; i3++) {
                DatagramPacket packet2 = packets[i3].newDatagramPacket(byteBuf, local);
                byteBuf.skipBytes(datagramSize);
                addDatagramPacketToOut(packet2, datagramPackets2);
            }
            byteBuf.release();
            byteBuf = null;
            processPacketList(pipeline(), allocHandle, bytesReceived, datagramPackets2);
            datagramPackets2.recycle();
            datagramPackets = null;
            return true;
        } finally {
            releaseAndRecycle(byteBuf, datagramPackets);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public NativeDatagramPacketArray cleanDatagramPacketArray() {
        return ((EpollEventLoop) eventLoop()).cleanDatagramPacketArray();
    }
}
