package io.netty.channel.epoll;

import io.netty.buffer.ByteBuf;
import io.netty.channel.AddressedEnvelope;
import io.netty.channel.Channel;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.DefaultAddressedEnvelope;
import io.netty.channel.epoll.AbstractEpollChannel;
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
public final class EpollDomainDatagramChannel extends AbstractEpollChannel implements DomainDatagramChannel {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final EpollDomainDatagramChannelConfig config;
    private volatile boolean connected;
    private volatile DomainSocketAddress local;
    private volatile DomainSocketAddress remote;
    private static final ChannelMetadata METADATA = new ChannelMetadata(true, 16);
    private static final String EXPECTED_TYPES = " (expected: " + StringUtil.simpleClassName((Class<?>) DomainDatagramPacket.class) + ", " + StringUtil.simpleClassName((Class<?>) AddressedEnvelope.class) + Typography.less + StringUtil.simpleClassName((Class<?>) ByteBuf.class) + ", " + StringUtil.simpleClassName((Class<?>) DomainSocketAddress.class) + ">, " + StringUtil.simpleClassName((Class<?>) ByteBuf.class) + ')';

    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.Channel
    public /* bridge */ /* synthetic */ boolean isOpen() {
        return super.isOpen();
    }

    public EpollDomainDatagramChannel() {
        this(LinuxSocket.newSocketDomainDgram(), false);
    }

    public EpollDomainDatagramChannel(int fd) {
        this(new LinuxSocket(fd), true);
    }

    private EpollDomainDatagramChannel(LinuxSocket socket, boolean active) {
        super((Channel) null, socket, active);
        this.config = new EpollDomainDatagramChannelConfig(this);
    }

    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.Channel
    public EpollDomainDatagramChannelConfig config() {
        return this.config;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.AbstractChannel
    public void doBind(SocketAddress localAddress) throws Exception {
        super.doBind(localAddress);
        this.local = (DomainSocketAddress) localAddress;
        this.active = true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.AbstractChannel
    public void doClose() throws Exception {
        super.doClose();
        this.active = false;
        this.connected = false;
        this.local = null;
        this.remote = null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.epoll.AbstractEpollChannel
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

    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.AbstractChannel
    protected void doDisconnect() throws Exception {
        doClose();
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doWrite(ChannelOutboundBuffer in) throws Exception {
        int maxMessagesPerWrite = maxMessagesPerWrite();
        while (maxMessagesPerWrite > 0) {
            Object msg = in.current();
            if (msg != null) {
                boolean done = false;
                try {
                    int i = config().getWriteSpinCount();
                    while (true) {
                        if (i <= 0) {
                            break;
                        }
                        if (!doWriteMessage(msg)) {
                            i--;
                        } else {
                            done = true;
                            break;
                        }
                    }
                } catch (IOException e) {
                    maxMessagesPerWrite--;
                    in.remove(e);
                }
                if (!done) {
                    break;
                }
                in.remove();
                maxMessagesPerWrite--;
            } else {
                break;
            }
        }
        if (in.isEmpty()) {
            clearFlag(Native.EPOLLOUT);
        } else {
            setFlag(Native.EPOLLOUT);
        }
    }

    private boolean doWriteMessage(Object msg) throws Exception {
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
                IovArray array = ((EpollEventLoop) eventLoop()).cleanIovArray();
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
                    writtenBytes = this.socket.send(nioData, nioData.position(), nioData.limit());
                } else {
                    writtenBytes = this.socket.sendToDomainSocket(nioData, nioData.position(), nioData.limit(), remoteAddress.path().getBytes(CharsetUtil.UTF_8));
                }
            }
        } else {
            long memoryAddress = data.memoryAddress();
            if (remoteAddress == null) {
                writtenBytes = this.socket.sendAddress(memoryAddress, data.readerIndex(), data.writerIndex());
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

    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.Channel
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
    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.AbstractChannel
    public DomainSocketAddress localAddress0() {
        return this.local;
    }

    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.Channel
    public ChannelMetadata metadata() {
        return METADATA;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.AbstractChannel
    public AbstractEpollChannel.AbstractEpollUnsafe newUnsafe() {
        return new EpollDomainDatagramChannelUnsafe();
    }

    public PeerCredentials peerCredentials() throws IOException {
        return this.socket.getPeerCredentials();
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public DomainSocketAddress remoteAddress() {
        return (DomainSocketAddress) super.remoteAddress();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.AbstractChannel
    public DomainSocketAddress remoteAddress0() {
        return this.remote;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public final class EpollDomainDatagramChannelUnsafe extends AbstractEpollChannel.AbstractEpollUnsafe {
        static final /* synthetic */ boolean $assertionsDisabled = false;

        EpollDomainDatagramChannelUnsafe() {
            super();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* JADX WARN: Removed duplicated region for block: B:22:0x0108 A[Catch: all -> 0x00fd, TRY_LEAVE, TryCatch #1 {all -> 0x00fd, blocks: (B:20:0x0100, B:22:0x0108, B:42:0x00f9, B:11:0x003f, B:12:0x0045, B:14:0x0053, B:28:0x0062, B:16:0x0067, B:17:0x00e2, B:29:0x0079, B:31:0x007f, B:38:0x00b4, B:33:0x00bc, B:35:0x00c2, B:36:0x00c9, B:39:0x0094), top: B:9:0x003f, inners: #0 }] */
        @Override // io.netty.channel.epoll.AbstractEpollChannel.AbstractEpollUnsafe
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void epollInReady() {
            /*
                Method dump skipped, instructions count: 282
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: io.netty.channel.epoll.EpollDomainDatagramChannel.EpollDomainDatagramChannelUnsafe.epollInReady():void");
        }
    }
}
