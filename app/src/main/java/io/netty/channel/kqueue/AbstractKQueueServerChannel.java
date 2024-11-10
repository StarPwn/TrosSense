package io.netty.channel.kqueue;

import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoop;
import io.netty.channel.ServerChannel;
import io.netty.channel.kqueue.AbstractKQueueChannel;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/* loaded from: classes4.dex */
public abstract class AbstractKQueueServerChannel extends AbstractKQueueChannel implements ServerChannel {
    private static final ChannelMetadata METADATA = new ChannelMetadata(false, 16);

    abstract Channel newChildChannel(int i, byte[] bArr, int i2, int i3) throws Exception;

    @Override // io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.Channel
    public /* bridge */ /* synthetic */ boolean isActive() {
        return super.isActive();
    }

    @Override // io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.Channel
    public /* bridge */ /* synthetic */ boolean isOpen() {
        return super.isOpen();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AbstractKQueueServerChannel(BsdSocket fd) {
        this(fd, isSoErrorZero(fd));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AbstractKQueueServerChannel(BsdSocket fd, boolean active) {
        super((Channel) null, fd, active);
    }

    @Override // io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.Channel
    public ChannelMetadata metadata() {
        return METADATA;
    }

    @Override // io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.AbstractChannel
    protected boolean isCompatible(EventLoop loop) {
        return loop instanceof KQueueEventLoop;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.AbstractChannel
    public InetSocketAddress remoteAddress0() {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.AbstractChannel
    public AbstractKQueueChannel.AbstractKQueueUnsafe newUnsafe() {
        return new KQueueServerSocketUnsafe();
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doWrite(ChannelOutboundBuffer in) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override // io.netty.channel.AbstractChannel
    protected Object filterOutboundMessage(Object msg) throws Exception {
        throw new UnsupportedOperationException();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.kqueue.AbstractKQueueChannel
    public boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
        throw new UnsupportedOperationException();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public final class KQueueServerSocketUnsafe extends AbstractKQueueChannel.AbstractKQueueUnsafe {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private final byte[] acceptedAddress;

        KQueueServerSocketUnsafe() {
            super();
            this.acceptedAddress = new byte[26];
        }

        @Override // io.netty.channel.kqueue.AbstractKQueueChannel.AbstractKQueueUnsafe
        void readReady(KQueueRecvByteAllocatorHandle allocHandle) {
            if (!AbstractKQueueServerChannel.this.eventLoop().inEventLoop()) {
                throw new AssertionError();
            }
            ChannelConfig config = AbstractKQueueServerChannel.this.config();
            if (AbstractKQueueServerChannel.this.shouldBreakReadReady(config)) {
                clearReadFilter0();
                return;
            }
            ChannelPipeline pipeline = AbstractKQueueServerChannel.this.pipeline();
            allocHandle.reset(config);
            allocHandle.attemptedBytesRead(1);
            readReadyBefore();
            Throwable exception = null;
            do {
                try {
                    int acceptFd = AbstractKQueueServerChannel.this.socket.accept(this.acceptedAddress);
                    if (acceptFd == -1) {
                        allocHandle.lastBytesRead(-1);
                        break;
                    }
                    allocHandle.lastBytesRead(1);
                    allocHandle.incMessagesRead(1);
                    this.readPending = false;
                    pipeline.fireChannelRead((Object) AbstractKQueueServerChannel.this.newChildChannel(acceptFd, this.acceptedAddress, 1, this.acceptedAddress[0]));
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
                readReadyFinally(config);
            }
        }
    }
}
