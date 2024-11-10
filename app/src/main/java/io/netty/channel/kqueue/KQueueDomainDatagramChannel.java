package io.netty.channel.kqueue;

import io.netty.buffer.ByteBuf;
import io.netty.channel.AddressedEnvelope;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.DefaultAddressedEnvelope;
import io.netty.channel.kqueue.AbstractKQueueChannel;
import io.netty.channel.unix.DomainDatagramChannel;
import io.netty.channel.unix.DomainDatagramPacket;
import io.netty.channel.unix.DomainSocketAddress;
import io.netty.channel.unix.IovArray;
import io.netty.channel.unix.PeerCredentials;
import io.netty.channel.unix.UnixChannelUtil;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.StringUtil;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import kotlin.text.Typography;

/* loaded from: classes4.dex */
public final class KQueueDomainDatagramChannel extends AbstractKQueueDatagramChannel implements DomainDatagramChannel {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final String EXPECTED_TYPES = " (expected: " + StringUtil.simpleClassName((Class<?>) DomainDatagramPacket.class) + ", " + StringUtil.simpleClassName((Class<?>) AddressedEnvelope.class) + Typography.less + StringUtil.simpleClassName((Class<?>) ByteBuf.class) + ", " + StringUtil.simpleClassName((Class<?>) DomainSocketAddress.class) + ">, " + StringUtil.simpleClassName((Class<?>) ByteBuf.class) + ')';
    private final KQueueDomainDatagramChannelConfig config;
    private volatile boolean connected;
    private volatile DomainSocketAddress local;
    private volatile DomainSocketAddress remote;

    @Override // io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.Channel
    public /* bridge */ /* synthetic */ boolean isOpen() {
        return super.isOpen();
    }

    @Override // io.netty.channel.kqueue.AbstractKQueueDatagramChannel, io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.Channel
    public /* bridge */ /* synthetic */ ChannelMetadata metadata() {
        return super.metadata();
    }

    public KQueueDomainDatagramChannel() {
        this(BsdSocket.newSocketDomainDgram(), false);
    }

    public KQueueDomainDatagramChannel(int fd) {
        this(new BsdSocket(fd), true);
    }

    private KQueueDomainDatagramChannel(BsdSocket socket, boolean active) {
        super(null, socket, active);
        this.config = new KQueueDomainDatagramChannelConfig(this);
    }

    @Override // io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.Channel
    public KQueueDomainDatagramChannelConfig config() {
        return this.config;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.AbstractChannel
    public void doBind(SocketAddress localAddress) throws Exception {
        super.doBind(localAddress);
        this.local = (DomainSocketAddress) localAddress;
        this.active = true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.AbstractChannel
    public void doClose() throws Exception {
        super.doClose();
        this.active = false;
        this.connected = false;
        this.local = null;
        this.remote = null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.kqueue.AbstractKQueueChannel
    public boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
        if (super.doConnect(remoteAddress, localAddress)) {
            if (localAddress != null) {
                this.local = (DomainSocketAddress) localAddress;
            }
            this.remote = (DomainSocketAddress) remoteAddress;
            this.connected = true;
            return true;
        }
        return false;
    }

    @Override // io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.AbstractChannel
    protected void doDisconnect() throws Exception {
        doClose();
    }

    @Override // io.netty.channel.kqueue.AbstractKQueueDatagramChannel
    protected boolean doWriteMessage(Object msg) throws Exception {
        ByteBuf data;
        DomainSocketAddress remoteAddress;
        long writtenBytes;
        if (msg instanceof AddressedEnvelope) {
            AddressedEnvelope<ByteBuf, DomainSocketAddress> envelope = (AddressedEnvelope) msg;
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
                    writtenBytes = this.socket.sendToAddressesDomainSocket(array.memoryAddress(0), cnt, remoteAddress.path().getBytes(CharsetUtil.UTF_8));
                }
            } else {
                ByteBuffer nioData = data.internalNioBuffer(data.readerIndex(), data.readableBytes());
                if (remoteAddress == null) {
                    writtenBytes = this.socket.write(nioData, nioData.position(), nioData.limit());
                } else {
                    writtenBytes = this.socket.sendToDomainSocket(nioData, nioData.position(), nioData.limit(), remoteAddress.path().getBytes(CharsetUtil.UTF_8));
                }
            }
        } else {
            long memoryAddress = data.memoryAddress();
            if (remoteAddress == null) {
                writtenBytes = this.socket.writeAddress(memoryAddress, data.readerIndex(), data.writerIndex());
            } else {
                writtenBytes = this.socket.sendToAddressDomainSocket(memoryAddress, data.readerIndex(), data.writerIndex(), remoteAddress.path().getBytes(CharsetUtil.UTF_8));
            }
        }
        return writtenBytes > 0;
    }

    @Override // io.netty.channel.AbstractChannel
    protected Object filterOutboundMessage(Object msg) {
        if (msg instanceof DomainDatagramPacket) {
            DomainDatagramPacket packet = (DomainDatagramPacket) msg;
            ByteBuf content = (ByteBuf) packet.content();
            return UnixChannelUtil.isBufferCopyNeededForWrite(content) ? new DomainDatagramPacket(newDirectBuffer(packet, content), packet.recipient()) : msg;
        }
        if (msg instanceof ByteBuf) {
            ByteBuf buf = (ByteBuf) msg;
            return UnixChannelUtil.isBufferCopyNeededForWrite(buf) ? newDirectBuffer(buf) : buf;
        }
        if (msg instanceof AddressedEnvelope) {
            AddressedEnvelope<Object, SocketAddress> e = (AddressedEnvelope) msg;
            if ((e.content() instanceof ByteBuf) && (e.recipient() == null || (e.recipient() instanceof DomainSocketAddress))) {
                ByteBuf content2 = (ByteBuf) e.content();
                return UnixChannelUtil.isBufferCopyNeededForWrite(content2) ? new DefaultAddressedEnvelope(newDirectBuffer(e, content2), (DomainSocketAddress) e.recipient()) : e;
            }
        }
        throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg) + EXPECTED_TYPES);
    }

    @Override // io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.Channel
    public boolean isActive() {
        return this.socket.isOpen() && ((this.config.getActiveOnOpen() && isRegistered()) || this.active);
    }

    @Override // io.netty.channel.unix.DomainDatagramChannel
    public boolean isConnected() {
        return this.connected;
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public DomainSocketAddress localAddress() {
        return (DomainSocketAddress) super.localAddress();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.AbstractChannel
    public DomainSocketAddress localAddress0() {
        return this.local;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.AbstractChannel
    public AbstractKQueueChannel.AbstractKQueueUnsafe newUnsafe() {
        return new KQueueDomainDatagramChannelUnsafe();
    }

    public PeerCredentials peerCredentials() throws IOException {
        return this.socket.getPeerCredentials();
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public DomainSocketAddress remoteAddress() {
        return (DomainSocketAddress) super.remoteAddress();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.AbstractChannel
    public DomainSocketAddress remoteAddress0() {
        return this.remote;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public final class KQueueDomainDatagramChannelUnsafe extends AbstractKQueueChannel.AbstractKQueueUnsafe {
        static final /* synthetic */ boolean $assertionsDisabled = false;

        KQueueDomainDatagramChannelUnsafe() {
            super();
        }

        /* JADX WARN: Removed duplicated region for block: B:22:0x00f9 A[Catch: all -> 0x00ee, TRY_LEAVE, TryCatch #1 {all -> 0x00ee, blocks: (B:20:0x00f1, B:22:0x00f9, B:42:0x00ea, B:11:0x0030, B:12:0x0036, B:14:0x0044, B:28:0x0053, B:16:0x0058, B:17:0x00d3, B:29:0x006a, B:31:0x0070, B:38:0x00a5, B:33:0x00ad, B:35:0x00b3, B:36:0x00ba, B:39:0x0085), top: B:9:0x0030, inners: #0 }] */
        @Override // io.netty.channel.kqueue.AbstractKQueueChannel.AbstractKQueueUnsafe
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        void readReady(io.netty.channel.kqueue.KQueueRecvByteAllocatorHandle r12) {
            /*
                Method dump skipped, instructions count: 267
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: io.netty.channel.kqueue.KQueueDomainDatagramChannel.KQueueDomainDatagramChannelUnsafe.readReady(io.netty.channel.kqueue.KQueueRecvByteAllocatorHandle):void");
        }
    }
}
