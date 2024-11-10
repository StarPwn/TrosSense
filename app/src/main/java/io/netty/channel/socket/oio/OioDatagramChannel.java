package io.netty.channel.socket.oio;

import io.netty.buffer.ByteBuf;
import io.netty.channel.AddressedEnvelope;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPromise;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.oio.AbstractOioMessageChannel;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramChannelConfig;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.channels.UnresolvedAddressException;
import java.util.List;
import java.util.Locale;
import kotlin.text.Typography;

@Deprecated
/* loaded from: classes4.dex */
public class OioDatagramChannel extends AbstractOioMessageChannel implements DatagramChannel {
    private final OioDatagramChannelConfig config;
    private final MulticastSocket socket;
    private final DatagramPacket tmpPacket;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) OioDatagramChannel.class);
    private static final ChannelMetadata METADATA = new ChannelMetadata(true);
    private static final String EXPECTED_TYPES = " (expected: " + StringUtil.simpleClassName((Class<?>) io.netty.channel.socket.DatagramPacket.class) + ", " + StringUtil.simpleClassName((Class<?>) AddressedEnvelope.class) + Typography.less + StringUtil.simpleClassName((Class<?>) ByteBuf.class) + ", " + StringUtil.simpleClassName((Class<?>) SocketAddress.class) + ">, " + StringUtil.simpleClassName((Class<?>) ByteBuf.class) + ')';

    private static MulticastSocket newSocket() {
        try {
            return new MulticastSocket((SocketAddress) null);
        } catch (Exception e) {
            throw new ChannelException("failed to create a new socket", e);
        }
    }

    public OioDatagramChannel() {
        this(newSocket());
    }

    public OioDatagramChannel(MulticastSocket socket) {
        super(null);
        this.tmpPacket = new DatagramPacket(EmptyArrays.EMPTY_BYTES, 0);
        boolean success = false;
        try {
            try {
                socket.setSoTimeout(1000);
                socket.setBroadcast(false);
                success = true;
                this.socket = socket;
                this.config = new DefaultOioDatagramChannelConfig(this, socket);
            } catch (SocketException e) {
                throw new ChannelException("Failed to configure the datagram socket timeout.", e);
            }
        } finally {
            if (!success) {
                socket.close();
            }
        }
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
    public boolean isOpen() {
        return !this.socket.isClosed();
    }

    @Override // io.netty.channel.Channel
    public boolean isActive() {
        return isOpen() && ((((Boolean) this.config.getOption(ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION)).booleanValue() && isRegistered()) || this.socket.isBound());
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public boolean isConnected() {
        return this.socket.isConnected();
    }

    @Override // io.netty.channel.AbstractChannel
    protected SocketAddress localAddress0() {
        return this.socket.getLocalSocketAddress();
    }

    @Override // io.netty.channel.AbstractChannel
    protected SocketAddress remoteAddress0() {
        return this.socket.getRemoteSocketAddress();
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doBind(SocketAddress localAddress) throws Exception {
        this.socket.bind(localAddress);
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public InetSocketAddress localAddress() {
        return (InetSocketAddress) super.localAddress();
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public InetSocketAddress remoteAddress() {
        return (InetSocketAddress) super.remoteAddress();
    }

    @Override // io.netty.channel.oio.AbstractOioChannel
    protected void doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
        if (localAddress != null) {
            this.socket.bind(localAddress);
        }
        try {
            this.socket.connect(remoteAddress);
            if (1 == 0) {
                try {
                    this.socket.close();
                } catch (Throwable t) {
                    logger.warn("Failed to close a socket.", t);
                }
            }
        } catch (Throwable th) {
            if (0 == 0) {
                try {
                    this.socket.close();
                } catch (Throwable t2) {
                    logger.warn("Failed to close a socket.", t2);
                }
            }
            throw th;
        }
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doDisconnect() throws Exception {
        this.socket.disconnect();
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doClose() throws Exception {
        this.socket.close();
    }

    @Override // io.netty.channel.oio.AbstractOioMessageChannel
    protected int doReadMessages(List<Object> buf) throws Exception {
        DatagramChannelConfig config = config();
        RecvByteBufAllocator.Handle allocHandle = unsafe().recvBufAllocHandle();
        ByteBuf data = config.getAllocator().heapBuffer(allocHandle.guess());
        boolean free = true;
        try {
            try {
                this.tmpPacket.setAddress(null);
                this.tmpPacket.setData(data.array(), data.arrayOffset(), data.capacity());
                this.socket.receive(this.tmpPacket);
                InetSocketAddress remoteAddr = (InetSocketAddress) this.tmpPacket.getSocketAddress();
                allocHandle.lastBytesRead(this.tmpPacket.getLength());
                buf.add(new io.netty.channel.socket.DatagramPacket(data.writerIndex(allocHandle.lastBytesRead()), localAddress(), remoteAddr));
                free = false;
            } catch (SocketException e) {
                if (!e.getMessage().toLowerCase(Locale.US).contains("socket closed")) {
                    throw e;
                }
                if (free) {
                    data.release();
                }
                return -1;
            } catch (SocketTimeoutException e2) {
                if (!free) {
                    return 0;
                }
                data.release();
                return 0;
            } catch (Throwable cause) {
                PlatformDependent.throwException(cause);
                if (free) {
                    data.release();
                }
                return -1;
            }
        } finally {
            if (free) {
                data.release();
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x003e A[Catch: Exception -> 0x006f, TryCatch #0 {Exception -> 0x006f, blocks: (B:29:0x0026, B:22:0x0038, B:24:0x003e, B:25:0x005e, B:27:0x0051, B:8:0x002c, B:21:0x0032, B:11:0x0069, B:12:0x006e), top: B:28:0x0026 }] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0051 A[Catch: Exception -> 0x006f, TryCatch #0 {Exception -> 0x006f, blocks: (B:29:0x0026, B:22:0x0038, B:24:0x003e, B:25:0x005e, B:27:0x0051, B:8:0x002c, B:21:0x0032, B:11:0x0069, B:12:0x006e), top: B:28:0x0026 }] */
    @Override // io.netty.channel.AbstractChannel
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void doWrite(io.netty.channel.ChannelOutboundBuffer r9) throws java.lang.Exception {
        /*
            r8 = this;
        L1:
            java.lang.Object r0 = r9.current()
            if (r0 != 0) goto L9
        L8:
            return
        L9:
            boolean r1 = r0 instanceof io.netty.channel.AddressedEnvelope
            if (r1 == 0) goto L1c
            r1 = r0
            io.netty.channel.AddressedEnvelope r1 = (io.netty.channel.AddressedEnvelope) r1
            java.net.SocketAddress r2 = r1.recipient()
            java.lang.Object r3 = r1.content()
            r1 = r3
            io.netty.buffer.ByteBuf r1 = (io.netty.buffer.ByteBuf) r1
            goto L20
        L1c:
            r1 = r0
            io.netty.buffer.ByteBuf r1 = (io.netty.buffer.ByteBuf) r1
            r2 = 0
        L20:
            int r3 = r1.readableBytes()
            if (r2 == 0) goto L2c
            java.net.DatagramPacket r4 = r8.tmpPacket     // Catch: java.lang.Exception -> L6f
            r4.setSocketAddress(r2)     // Catch: java.lang.Exception -> L6f
            goto L38
        L2c:
            boolean r4 = r8.isConnected()     // Catch: java.lang.Exception -> L6f
            if (r4 == 0) goto L69
            java.net.DatagramPacket r4 = r8.tmpPacket     // Catch: java.lang.Exception -> L6f
            r5 = 0
            r4.setAddress(r5)     // Catch: java.lang.Exception -> L6f
        L38:
            boolean r4 = r1.hasArray()     // Catch: java.lang.Exception -> L6f
            if (r4 == 0) goto L51
            java.net.DatagramPacket r4 = r8.tmpPacket     // Catch: java.lang.Exception -> L6f
            byte[] r5 = r1.array()     // Catch: java.lang.Exception -> L6f
            int r6 = r1.arrayOffset()     // Catch: java.lang.Exception -> L6f
            int r7 = r1.readerIndex()     // Catch: java.lang.Exception -> L6f
            int r6 = r6 + r7
            r4.setData(r5, r6, r3)     // Catch: java.lang.Exception -> L6f
            goto L5e
        L51:
            java.net.DatagramPacket r4 = r8.tmpPacket     // Catch: java.lang.Exception -> L6f
            int r5 = r1.readerIndex()     // Catch: java.lang.Exception -> L6f
            byte[] r5 = io.netty.buffer.ByteBufUtil.getBytes(r1, r5, r3)     // Catch: java.lang.Exception -> L6f
            r4.setData(r5)     // Catch: java.lang.Exception -> L6f
        L5e:
            java.net.MulticastSocket r4 = r8.socket     // Catch: java.lang.Exception -> L6f
            java.net.DatagramPacket r5 = r8.tmpPacket     // Catch: java.lang.Exception -> L6f
            r4.send(r5)     // Catch: java.lang.Exception -> L6f
            r9.remove()     // Catch: java.lang.Exception -> L6f
            goto L73
        L69:
            java.nio.channels.NotYetConnectedException r4 = new java.nio.channels.NotYetConnectedException     // Catch: java.lang.Exception -> L6f
            r4.<init>()     // Catch: java.lang.Exception -> L6f
            throw r4     // Catch: java.lang.Exception -> L6f
        L6f:
            r4 = move-exception
            r9.remove(r4)
        L73:
            goto L1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.channel.socket.oio.OioDatagramChannel.doWrite(io.netty.channel.ChannelOutboundBuffer):void");
    }

    private static void checkUnresolved(AddressedEnvelope<?, ?> envelope) {
        if ((envelope.recipient() instanceof InetSocketAddress) && ((InetSocketAddress) envelope.recipient()).isUnresolved()) {
            throw new UnresolvedAddressException();
        }
    }

    @Override // io.netty.channel.AbstractChannel
    protected Object filterOutboundMessage(Object msg) {
        if (msg instanceof io.netty.channel.socket.DatagramPacket) {
            checkUnresolved((io.netty.channel.socket.DatagramPacket) msg);
            return msg;
        }
        if (msg instanceof ByteBuf) {
            return msg;
        }
        if (msg instanceof AddressedEnvelope) {
            AddressedEnvelope<Object, SocketAddress> e = (AddressedEnvelope) msg;
            checkUnresolved(e);
            if (e.content() instanceof ByteBuf) {
                return msg;
            }
        }
        throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg) + EXPECTED_TYPES);
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture joinGroup(InetAddress multicastAddress) {
        return joinGroup(multicastAddress, newPromise());
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture joinGroup(InetAddress multicastAddress, ChannelPromise promise) {
        ensureBound();
        try {
            this.socket.joinGroup(multicastAddress);
            promise.setSuccess();
        } catch (IOException e) {
            promise.setFailure((Throwable) e);
        }
        return promise;
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture joinGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface) {
        return joinGroup(multicastAddress, networkInterface, newPromise());
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture joinGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface, ChannelPromise promise) {
        ensureBound();
        try {
            this.socket.joinGroup(multicastAddress, networkInterface);
            promise.setSuccess();
        } catch (IOException e) {
            promise.setFailure((Throwable) e);
        }
        return promise;
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture joinGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source) {
        return newFailedFuture(new UnsupportedOperationException());
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture joinGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source, ChannelPromise promise) {
        promise.setFailure((Throwable) new UnsupportedOperationException());
        return promise;
    }

    private void ensureBound() {
        if (!isActive()) {
            throw new IllegalStateException(DatagramChannel.class.getName() + " must be bound to join a group.");
        }
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture leaveGroup(InetAddress multicastAddress) {
        return leaveGroup(multicastAddress, newPromise());
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture leaveGroup(InetAddress multicastAddress, ChannelPromise promise) {
        try {
            this.socket.leaveGroup(multicastAddress);
            promise.setSuccess();
        } catch (IOException e) {
            promise.setFailure((Throwable) e);
        }
        return promise;
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture leaveGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface) {
        return leaveGroup(multicastAddress, networkInterface, newPromise());
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture leaveGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface, ChannelPromise promise) {
        try {
            this.socket.leaveGroup(multicastAddress, networkInterface);
            promise.setSuccess();
        } catch (IOException e) {
            promise.setFailure((Throwable) e);
        }
        return promise;
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture leaveGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source) {
        return newFailedFuture(new UnsupportedOperationException());
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture leaveGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source, ChannelPromise promise) {
        promise.setFailure((Throwable) new UnsupportedOperationException());
        return promise;
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture block(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress sourceToBlock) {
        return newFailedFuture(new UnsupportedOperationException());
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture block(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress sourceToBlock, ChannelPromise promise) {
        promise.setFailure((Throwable) new UnsupportedOperationException());
        return promise;
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture block(InetAddress multicastAddress, InetAddress sourceToBlock) {
        return newFailedFuture(new UnsupportedOperationException());
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture block(InetAddress multicastAddress, InetAddress sourceToBlock, ChannelPromise promise) {
        promise.setFailure((Throwable) new UnsupportedOperationException());
        return promise;
    }
}
