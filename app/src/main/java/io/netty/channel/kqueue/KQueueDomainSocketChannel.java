package io.netty.channel.kqueue;

import io.netty.channel.Channel;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.kqueue.AbstractKQueueChannel;
import io.netty.channel.kqueue.AbstractKQueueStreamChannel;
import io.netty.channel.unix.DomainSocketAddress;
import io.netty.channel.unix.DomainSocketChannel;
import io.netty.channel.unix.FileDescriptor;
import io.netty.channel.unix.PeerCredentials;
import java.io.IOException;
import java.net.SocketAddress;

/* loaded from: classes4.dex */
public final class KQueueDomainSocketChannel extends AbstractKQueueStreamChannel implements DomainSocketChannel {
    private final KQueueDomainSocketChannelConfig config;
    private volatile DomainSocketAddress local;
    private volatile DomainSocketAddress remote;

    public KQueueDomainSocketChannel() {
        super((Channel) null, BsdSocket.newSocketDomain(), false);
        this.config = new KQueueDomainSocketChannelConfig(this);
    }

    public KQueueDomainSocketChannel(int fd) {
        this(null, new BsdSocket(fd));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public KQueueDomainSocketChannel(Channel parent, BsdSocket fd) {
        super(parent, fd, true);
        this.config = new KQueueDomainSocketChannelConfig(this);
        this.local = fd.localDomainSocketAddress();
        this.remote = fd.remoteDomainSocketAddress();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.kqueue.AbstractKQueueStreamChannel, io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.AbstractChannel
    public AbstractKQueueChannel.AbstractKQueueUnsafe newUnsafe() {
        return new KQueueDomainUnsafe();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.AbstractChannel
    public DomainSocketAddress localAddress0() {
        return this.local;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.AbstractChannel
    public DomainSocketAddress remoteAddress0() {
        return this.remote;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.AbstractChannel
    public void doBind(SocketAddress localAddress) throws Exception {
        this.socket.bind(localAddress);
        this.local = (DomainSocketAddress) localAddress;
    }

    @Override // io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.Channel
    public KQueueDomainSocketChannelConfig config() {
        return this.config;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.kqueue.AbstractKQueueChannel
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
    @Override // io.netty.channel.kqueue.AbstractKQueueStreamChannel
    public int doWriteSingle(ChannelOutboundBuffer in) throws Exception {
        Object msg = in.current();
        if ((msg instanceof FileDescriptor) && this.socket.sendFd(((FileDescriptor) msg).intValue()) > 0) {
            in.remove();
            return 1;
        }
        return super.doWriteSingle(in);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.kqueue.AbstractKQueueStreamChannel, io.netty.channel.AbstractChannel
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
    public final class KQueueDomainUnsafe extends AbstractKQueueStreamChannel.KQueueStreamUnsafe {
        private KQueueDomainUnsafe() {
            super();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // io.netty.channel.kqueue.AbstractKQueueStreamChannel.KQueueStreamUnsafe, io.netty.channel.kqueue.AbstractKQueueChannel.AbstractKQueueUnsafe
        public void readReady(KQueueRecvByteAllocatorHandle allocHandle) {
            switch (KQueueDomainSocketChannel.this.config().getReadMode()) {
                case BYTES:
                    super.readReady(allocHandle);
                    return;
                case FILE_DESCRIPTORS:
                    readReadyFd();
                    return;
                default:
                    throw new Error();
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:14:0x005c, code lost:            r1.readComplete();        r2.fireChannelReadComplete();     */
        /* JADX WARN: Failed to find 'out' block for switch in B:9:0x002d. Please report as an issue. */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        private void readReadyFd() {
            /*
                r6 = this;
                io.netty.channel.kqueue.KQueueDomainSocketChannel r0 = io.netty.channel.kqueue.KQueueDomainSocketChannel.this
                io.netty.channel.kqueue.BsdSocket r0 = r0.socket
                boolean r0 = r0.isInputShutdown()
                if (r0 == 0) goto Le
                super.clearReadFilter0()
                return
            Le:
                io.netty.channel.kqueue.KQueueDomainSocketChannel r0 = io.netty.channel.kqueue.KQueueDomainSocketChannel.this
                io.netty.channel.kqueue.KQueueDomainSocketChannelConfig r0 = r0.config()
                io.netty.channel.kqueue.KQueueRecvByteAllocatorHandle r1 = r6.recvBufAllocHandle()
                io.netty.channel.kqueue.KQueueDomainSocketChannel r2 = io.netty.channel.kqueue.KQueueDomainSocketChannel.this
                io.netty.channel.ChannelPipeline r2 = r2.pipeline()
                r1.reset(r0)
                r6.readReadyBefore()
            L24:
                io.netty.channel.kqueue.KQueueDomainSocketChannel r3 = io.netty.channel.kqueue.KQueueDomainSocketChannel.this     // Catch: java.lang.Throwable -> L63
                io.netty.channel.kqueue.BsdSocket r3 = r3.socket     // Catch: java.lang.Throwable -> L63
                int r3 = r3.recvFd()     // Catch: java.lang.Throwable -> L63
                r4 = 0
                switch(r3) {
                    case -1: goto L39;
                    case 0: goto L35;
                    default: goto L30;
                }     // Catch: java.lang.Throwable -> L63
            L30:
                r5 = 1
                r1.lastBytesRead(r5)     // Catch: java.lang.Throwable -> L63
                goto L48
            L35:
                r1.lastBytesRead(r4)     // Catch: java.lang.Throwable -> L63
                goto L5c
            L39:
                r4 = -1
                r1.lastBytesRead(r4)     // Catch: java.lang.Throwable -> L63
                io.netty.channel.ChannelPromise r4 = r6.voidPromise()     // Catch: java.lang.Throwable -> L63
                r6.close(r4)     // Catch: java.lang.Throwable -> L63
                r6.readReadyFinally(r0)
                return
            L48:
                r1.incMessagesRead(r5)     // Catch: java.lang.Throwable -> L63
                r6.readPending = r4     // Catch: java.lang.Throwable -> L63
                io.netty.channel.unix.FileDescriptor r4 = new io.netty.channel.unix.FileDescriptor     // Catch: java.lang.Throwable -> L63
                r4.<init>(r3)     // Catch: java.lang.Throwable -> L63
                r2.fireChannelRead(r4)     // Catch: java.lang.Throwable -> L63
                boolean r3 = r1.continueReading()     // Catch: java.lang.Throwable -> L63
                if (r3 != 0) goto L24
            L5c:
                r1.readComplete()     // Catch: java.lang.Throwable -> L63
                r2.fireChannelReadComplete()     // Catch: java.lang.Throwable -> L63
                goto L6e
            L63:
                r3 = move-exception
                r1.readComplete()     // Catch: java.lang.Throwable -> L73
                r2.fireChannelReadComplete()     // Catch: java.lang.Throwable -> L73
                r2.fireExceptionCaught(r3)     // Catch: java.lang.Throwable -> L73
            L6e:
                r6.readReadyFinally(r0)
                return
            L73:
                r3 = move-exception
                r6.readReadyFinally(r0)
                throw r3
            */
            throw new UnsupportedOperationException("Method not decompiled: io.netty.channel.kqueue.KQueueDomainSocketChannel.KQueueDomainUnsafe.readReadyFd():void");
        }
    }
}
