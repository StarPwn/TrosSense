package io.netty.channel.epoll;

import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoop;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.AbstractEpollChannel;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/* loaded from: classes4.dex */
public abstract class AbstractEpollServerChannel extends AbstractEpollChannel implements ServerChannel {
    private static final ChannelMetadata METADATA = new ChannelMetadata(false, 16);

    protected abstract Channel newChildChannel(int i, byte[] bArr, int i2, int i3) throws Exception;

    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.Channel
    public /* bridge */ /* synthetic */ boolean isActive() {
        return super.isActive();
    }

    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.Channel
    public /* bridge */ /* synthetic */ boolean isOpen() {
        return super.isOpen();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractEpollServerChannel(int fd) {
        this(new LinuxSocket(fd), false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractEpollServerChannel(LinuxSocket fd) {
        this(fd, isSoErrorZero(fd));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractEpollServerChannel(LinuxSocket fd, boolean active) {
        super((Channel) null, fd, active);
    }

    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.Channel
    public ChannelMetadata metadata() {
        return METADATA;
    }

    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.AbstractChannel
    protected boolean isCompatible(EventLoop loop) {
        return loop instanceof EpollEventLoop;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.AbstractChannel
    public InetSocketAddress remoteAddress0() {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.AbstractChannel
    public AbstractEpollChannel.AbstractEpollUnsafe newUnsafe() {
        return new EpollServerSocketUnsafe();
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doWrite(ChannelOutboundBuffer in) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override // io.netty.channel.AbstractChannel
    protected Object filterOutboundMessage(Object msg) throws Exception {
        throw new UnsupportedOperationException();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public final class EpollServerSocketUnsafe extends AbstractEpollChannel.AbstractEpollUnsafe {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private final byte[] acceptedAddress;

        EpollServerSocketUnsafe() {
            super();
            this.acceptedAddress = new byte[26];
        }

        @Override // io.netty.channel.epoll.AbstractEpollChannel.AbstractEpollUnsafe, io.netty.channel.Channel.Unsafe
        public void connect(SocketAddress socketAddress, SocketAddress socketAddress2, ChannelPromise channelPromise) {
            channelPromise.setFailure((Throwable) new UnsupportedOperationException());
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // io.netty.channel.epoll.AbstractEpollChannel.AbstractEpollUnsafe
        public void epollInReady() {
            if (!AbstractEpollServerChannel.this.eventLoop().inEventLoop()) {
                throw new AssertionError();
            }
            ChannelConfig config = AbstractEpollServerChannel.this.config();
            if (AbstractEpollServerChannel.this.shouldBreakEpollInReady(config)) {
                clearEpollIn0();
                return;
            }
            EpollRecvByteAllocatorHandle allocHandle = recvBufAllocHandle();
            allocHandle.edgeTriggered(AbstractEpollServerChannel.this.isFlagSet(Native.EPOLLET));
            ChannelPipeline pipeline = AbstractEpollServerChannel.this.pipeline();
            allocHandle.reset(config);
            allocHandle.attemptedBytesRead(1);
            epollInBefore();
            Throwable exception = null;
            do {
                try {
                    allocHandle.lastBytesRead(AbstractEpollServerChannel.this.socket.accept(this.acceptedAddress));
                    if (allocHandle.lastBytesRead() == -1) {
                        break;
                    }
                    allocHandle.incMessagesRead(1);
                    this.readPending = false;
                    pipeline.fireChannelRead((Object) AbstractEpollServerChannel.this.newChildChannel(allocHandle.lastBytesRead(), this.acceptedAddress, 1, this.acceptedAddress[0]));
                } catch (Throwable t) {
                    exception = t;
                }
            } while (allocHandle.continueReading());
            try {
                allocHandle.readComplete();
                pipeline.fireChannelReadComplete();
                if (exception != null) {
                    pipeline.fireExceptionCaught(exception);
                }
            } finally {
                epollInFinally(config);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.epoll.AbstractEpollChannel
    public boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
        throw new UnsupportedOperationException();
    }
}
