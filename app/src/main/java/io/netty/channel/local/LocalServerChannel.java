package io.netty.channel.local;

import io.netty.channel.AbstractServerChannel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.EventLoop;
import io.netty.channel.PreferHeapByteBufAllocator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.ServerChannelRecvByteBufAllocator;
import io.netty.channel.SingleThreadEventLoop;
import io.netty.util.concurrent.SingleThreadEventExecutor;
import java.net.SocketAddress;
import java.util.ArrayDeque;
import java.util.Queue;

/* loaded from: classes4.dex */
public class LocalServerChannel extends AbstractServerChannel {
    private volatile boolean acceptInProgress;
    private volatile LocalAddress localAddress;
    private volatile int state;
    private final ChannelConfig config = new DefaultChannelConfig(this, new ServerChannelRecvByteBufAllocator()) { // from class: io.netty.channel.local.LocalServerChannel.1
    };
    private final Queue<Object> inboundBuffer = new ArrayDeque();
    private final Runnable shutdownHook = new Runnable() { // from class: io.netty.channel.local.LocalServerChannel.2
        @Override // java.lang.Runnable
        public void run() {
            LocalServerChannel.this.unsafe().close(LocalServerChannel.this.unsafe().voidPromise());
        }
    };

    public LocalServerChannel() {
        config().setAllocator(new PreferHeapByteBufAllocator(this.config.getAllocator()));
    }

    @Override // io.netty.channel.Channel
    public ChannelConfig config() {
        return this.config;
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public LocalAddress localAddress() {
        return (LocalAddress) super.localAddress();
    }

    @Override // io.netty.channel.AbstractServerChannel, io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public LocalAddress remoteAddress() {
        return (LocalAddress) super.remoteAddress();
    }

    @Override // io.netty.channel.Channel
    public boolean isOpen() {
        return this.state < 2;
    }

    @Override // io.netty.channel.Channel
    public boolean isActive() {
        return this.state == 1;
    }

    @Override // io.netty.channel.AbstractChannel
    protected boolean isCompatible(EventLoop loop) {
        return loop instanceof SingleThreadEventLoop;
    }

    @Override // io.netty.channel.AbstractChannel
    protected SocketAddress localAddress0() {
        return this.localAddress;
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doRegister() throws Exception {
        ((SingleThreadEventExecutor) eventLoop()).addShutdownHook(this.shutdownHook);
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doBind(SocketAddress localAddress) throws Exception {
        this.localAddress = LocalChannelRegistry.register(this, this.localAddress, localAddress);
        this.state = 1;
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doClose() throws Exception {
        if (this.state <= 1) {
            if (this.localAddress != null) {
                LocalChannelRegistry.unregister(this.localAddress);
                this.localAddress = null;
            }
            this.state = 2;
        }
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doDeregister() throws Exception {
        ((SingleThreadEventExecutor) eventLoop()).removeShutdownHook(this.shutdownHook);
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doBeginRead() throws Exception {
        if (this.acceptInProgress) {
            return;
        }
        Queue<Object> inboundBuffer = this.inboundBuffer;
        if (inboundBuffer.isEmpty()) {
            this.acceptInProgress = true;
        } else {
            readInbound();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LocalChannel serve(LocalChannel peer) {
        final LocalChannel child = newLocalChannel(peer);
        if (eventLoop().inEventLoop()) {
            serve0(child);
        } else {
            eventLoop().execute(new Runnable() { // from class: io.netty.channel.local.LocalServerChannel.3
                @Override // java.lang.Runnable
                public void run() {
                    LocalServerChannel.this.serve0(child);
                }
            });
        }
        return child;
    }

    private void readInbound() {
        RecvByteBufAllocator.Handle handle = unsafe().recvBufAllocHandle();
        handle.reset(config());
        ChannelPipeline pipeline = pipeline();
        do {
            Object m = this.inboundBuffer.poll();
            if (m == null) {
                break;
            } else {
                pipeline.fireChannelRead(m);
            }
        } while (handle.continueReading());
        handle.readComplete();
        pipeline.fireChannelReadComplete();
    }

    protected LocalChannel newLocalChannel(LocalChannel peer) {
        return new LocalChannel(this, peer);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void serve0(LocalChannel child) {
        this.inboundBuffer.add(child);
        if (this.acceptInProgress) {
            this.acceptInProgress = false;
            readInbound();
        }
    }
}
