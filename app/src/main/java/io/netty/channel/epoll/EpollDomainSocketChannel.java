package io.netty.channel.epoll;

import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.epoll.AbstractEpollChannel;
import io.netty.channel.epoll.AbstractEpollStreamChannel;
import io.netty.channel.unix.DomainSocketAddress;
import io.netty.channel.unix.DomainSocketChannel;
import io.netty.channel.unix.FileDescriptor;
import io.netty.channel.unix.PeerCredentials;
import java.io.IOException;
import java.net.SocketAddress;

/* loaded from: classes4.dex */
public final class EpollDomainSocketChannel extends AbstractEpollStreamChannel implements DomainSocketChannel {
    private final EpollDomainSocketChannelConfig config;
    private volatile DomainSocketAddress local;
    private volatile DomainSocketAddress remote;

    public EpollDomainSocketChannel() {
        super(LinuxSocket.newSocketDomain(), false);
        this.config = new EpollDomainSocketChannelConfig(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public EpollDomainSocketChannel(Channel parent, FileDescriptor fd) {
        this(parent, new LinuxSocket(fd.intValue()));
    }

    public EpollDomainSocketChannel(int fd) {
        super(fd);
        this.config = new EpollDomainSocketChannelConfig(this);
    }

    public EpollDomainSocketChannel(Channel parent, LinuxSocket fd) {
        super(parent, fd);
        this.config = new EpollDomainSocketChannelConfig(this);
        this.local = fd.localDomainSocketAddress();
        this.remote = fd.remoteDomainSocketAddress();
    }

    public EpollDomainSocketChannel(int fd, boolean active) {
        super(new LinuxSocket(fd), active);
        this.config = new EpollDomainSocketChannelConfig(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.epoll.AbstractEpollStreamChannel, io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.AbstractChannel
    public AbstractEpollChannel.AbstractEpollUnsafe newUnsafe() {
        return new EpollDomainUnsafe();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.AbstractChannel
    public DomainSocketAddress localAddress0() {
        return this.local;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.AbstractChannel
    public DomainSocketAddress remoteAddress0() {
        return this.remote;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.AbstractChannel
    public void doBind(SocketAddress localAddress) throws Exception {
        this.socket.bind(localAddress);
        this.local = (DomainSocketAddress) localAddress;
    }

    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.Channel
    public EpollDomainSocketChannelConfig config() {
        return this.config;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.epoll.AbstractEpollChannel
    public boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
        if (super.doConnect(remoteAddress, localAddress)) {
            this.local = localAddress != null ? (DomainSocketAddress) localAddress : this.socket.localDomainSocketAddress();
            this.remote = (DomainSocketAddress) remoteAddress;
            return true;
        }
        return false;
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public DomainSocketAddress remoteAddress() {
        return (DomainSocketAddress) super.remoteAddress();
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public DomainSocketAddress localAddress() {
        return (DomainSocketAddress) super.localAddress();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.epoll.AbstractEpollStreamChannel
    public int doWriteSingle(ChannelOutboundBuffer in) throws Exception {
        Object msg = in.current();
        if ((msg instanceof FileDescriptor) && this.socket.sendFd(((FileDescriptor) msg).intValue()) > 0) {
            in.remove();
            return 1;
        }
        return super.doWriteSingle(in);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.epoll.AbstractEpollStreamChannel, io.netty.channel.AbstractChannel
    public Object filterOutboundMessage(Object msg) {
        if (msg instanceof FileDescriptor) {
            return msg;
        }
        return super.filterOutboundMessage(msg);
    }

    public PeerCredentials peerCredentials() throws IOException {
        return this.socket.getPeerCredentials();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class EpollDomainUnsafe extends AbstractEpollStreamChannel.EpollStreamUnsafe {
        private EpollDomainUnsafe() {
            super();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // io.netty.channel.epoll.AbstractEpollStreamChannel.EpollStreamUnsafe, io.netty.channel.epoll.AbstractEpollChannel.AbstractEpollUnsafe
        public void epollInReady() {
            switch (EpollDomainSocketChannel.this.config().getReadMode()) {
                case BYTES:
                    super.epollInReady();
                    return;
                case FILE_DESCRIPTORS:
                    epollInReadFd();
                    return;
                default:
                    throw new Error();
            }
        }

        /* JADX WARN: Failed to find 'out' block for switch in B:9:0x003e. Please report as an issue. */
        private void epollInReadFd() {
            if (EpollDomainSocketChannel.this.socket.isInputShutdown()) {
                clearEpollIn0();
                return;
            }
            ChannelConfig config = EpollDomainSocketChannel.this.config();
            EpollRecvByteAllocatorHandle allocHandle = recvBufAllocHandle();
            allocHandle.edgeTriggered(EpollDomainSocketChannel.this.isFlagSet(Native.EPOLLET));
            ChannelPipeline pipeline = EpollDomainSocketChannel.this.pipeline();
            allocHandle.reset(config);
            epollInBefore();
            do {
                try {
                    allocHandle.lastBytesRead(EpollDomainSocketChannel.this.socket.recvFd());
                } finally {
                    try {
                    } finally {
                    }
                }
                switch (allocHandle.lastBytesRead()) {
                    case -1:
                        close(voidPromise());
                        return;
                    case 0:
                        allocHandle.readComplete();
                        pipeline.fireChannelReadComplete();
                    default:
                        allocHandle.incMessagesRead(1);
                        this.readPending = false;
                        pipeline.fireChannelRead((Object) new FileDescriptor(allocHandle.lastBytesRead()));
                        break;
                }
            } while (allocHandle.continueReading());
            allocHandle.readComplete();
            pipeline.fireChannelReadComplete();
        }
    }
}
