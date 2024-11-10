package io.netty.channel.oio;

import io.netty.channel.AbstractChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoop;
import io.netty.channel.ThreadPerChannelEventLoop;
import java.net.SocketAddress;

@Deprecated
/* loaded from: classes4.dex */
public abstract class AbstractOioChannel extends AbstractChannel {
    protected static final int SO_TIMEOUT = 1000;
    private final Runnable clearReadPendingRunnable;
    boolean readPending;
    final Runnable readTask;
    boolean readWhenInactive;

    protected abstract void doConnect(SocketAddress socketAddress, SocketAddress socketAddress2) throws Exception;

    protected abstract void doRead();

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractOioChannel(Channel parent) {
        super(parent);
        this.readTask = new Runnable() { // from class: io.netty.channel.oio.AbstractOioChannel.1
            @Override // java.lang.Runnable
            public void run() {
                AbstractOioChannel.this.doRead();
            }
        };
        this.clearReadPendingRunnable = new Runnable() { // from class: io.netty.channel.oio.AbstractOioChannel.2
            @Override // java.lang.Runnable
            public void run() {
                AbstractOioChannel.this.readPending = false;
            }
        };
    }

    @Override // io.netty.channel.AbstractChannel
    protected AbstractChannel.AbstractUnsafe newUnsafe() {
        return new DefaultOioUnsafe();
    }

    /* loaded from: classes4.dex */
    private final class DefaultOioUnsafe extends AbstractChannel.AbstractUnsafe {
        private DefaultOioUnsafe() {
            super();
        }

        @Override // io.netty.channel.Channel.Unsafe
        public void connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
            if (!promise.setUncancellable() || !ensureOpen(promise)) {
                return;
            }
            try {
                boolean wasActive = AbstractOioChannel.this.isActive();
                AbstractOioChannel.this.doConnect(remoteAddress, localAddress);
                boolean active = AbstractOioChannel.this.isActive();
                safeSetSuccess(promise);
                if (!wasActive && active) {
                    AbstractOioChannel.this.pipeline().fireChannelActive();
                }
            } catch (Throwable t) {
                safeSetFailure(promise, annotateConnectException(t, remoteAddress));
                closeIfClosed();
            }
        }
    }

    @Override // io.netty.channel.AbstractChannel
    protected boolean isCompatible(EventLoop loop) {
        return loop instanceof ThreadPerChannelEventLoop;
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doBeginRead() throws Exception {
        if (this.readPending) {
            return;
        }
        if (!isActive()) {
            this.readWhenInactive = true;
        } else {
            this.readPending = true;
            eventLoop().execute(this.readTask);
        }
    }

    @Deprecated
    protected boolean isReadPending() {
        return this.readPending;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Deprecated
    public void setReadPending(final boolean readPending) {
        if (isRegistered()) {
            EventLoop eventLoop = eventLoop();
            if (eventLoop.inEventLoop()) {
                this.readPending = readPending;
                return;
            } else {
                eventLoop.execute(new Runnable() { // from class: io.netty.channel.oio.AbstractOioChannel.3
                    @Override // java.lang.Runnable
                    public void run() {
                        AbstractOioChannel.this.readPending = readPending;
                    }
                });
                return;
            }
        }
        this.readPending = readPending;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void clearReadPending() {
        if (isRegistered()) {
            EventLoop eventLoop = eventLoop();
            if (eventLoop.inEventLoop()) {
                this.readPending = false;
                return;
            } else {
                eventLoop.execute(this.clearReadPendingRunnable);
                return;
            }
        }
        this.readPending = false;
    }
}
