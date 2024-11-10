package io.netty.channel.nio;

import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.ServerChannel;
import io.netty.channel.nio.AbstractNioChannel;
import java.io.IOException;
import java.net.PortUnreachableException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes4.dex */
public abstract class AbstractNioMessageChannel extends AbstractNioChannel {
    boolean inputShutdown;

    protected abstract int doReadMessages(List<Object> list) throws Exception;

    protected abstract boolean doWriteMessage(Object obj, ChannelOutboundBuffer channelOutboundBuffer) throws Exception;

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractNioMessageChannel(Channel parent, SelectableChannel ch, int readInterestOp) {
        super(parent, ch, readInterestOp);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.AbstractChannel
    public AbstractNioChannel.AbstractNioUnsafe newUnsafe() {
        return new NioMessageUnsafe();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.nio.AbstractNioChannel, io.netty.channel.AbstractChannel
    public void doBeginRead() throws Exception {
        if (this.inputShutdown) {
            return;
        }
        super.doBeginRead();
    }

    protected boolean continueReading(RecvByteBufAllocator.Handle allocHandle) {
        return allocHandle.continueReading();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class NioMessageUnsafe extends AbstractNioChannel.AbstractNioUnsafe {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private final List<Object> readBuf;

        private NioMessageUnsafe() {
            super();
            this.readBuf = new ArrayList();
        }

        @Override // io.netty.channel.nio.AbstractNioChannel.NioUnsafe
        public void read() {
            boolean z;
            if (!AbstractNioMessageChannel.this.eventLoop().inEventLoop()) {
                throw new AssertionError();
            }
            ChannelConfig config = AbstractNioMessageChannel.this.config();
            ChannelPipeline pipeline = AbstractNioMessageChannel.this.pipeline();
            RecvByteBufAllocator.Handle allocHandle = AbstractNioMessageChannel.this.unsafe().recvBufAllocHandle();
            allocHandle.reset(config);
            boolean closed = false;
            Throwable exception = null;
            do {
                try {
                    int localRead = AbstractNioMessageChannel.this.doReadMessages(this.readBuf);
                    if (localRead == 0) {
                        break;
                    }
                    if (localRead < 0) {
                        closed = true;
                        break;
                    }
                    allocHandle.incMessagesRead(localRead);
                } catch (Throwable t) {
                    exception = t;
                }
            } while (AbstractNioMessageChannel.this.continueReading(allocHandle));
            try {
                int size = this.readBuf.size();
                for (int i = 0; i < size; i++) {
                    AbstractNioMessageChannel.this.readPending = false;
                    pipeline.fireChannelRead(this.readBuf.get(i));
                }
                this.readBuf.clear();
                allocHandle.readComplete();
                pipeline.fireChannelReadComplete();
                if (exception != null) {
                    closed = AbstractNioMessageChannel.this.closeOnReadError(exception);
                    pipeline.fireExceptionCaught(exception);
                }
                if (closed) {
                    AbstractNioMessageChannel.this.inputShutdown = true;
                    if (AbstractNioMessageChannel.this.isOpen()) {
                        close(voidPromise());
                    }
                }
                if (z) {
                    return;
                }
            } finally {
                if (!AbstractNioMessageChannel.this.readPending && !config.isAutoRead()) {
                    removeReadOp();
                }
            }
        }
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doWrite(ChannelOutboundBuffer in) throws Exception {
        SelectionKey key = selectionKey();
        int interestOps = key.interestOps();
        int maxMessagesPerWrite = maxMessagesPerWrite();
        while (maxMessagesPerWrite > 0) {
            Object msg = in.current();
            if (msg != null) {
                boolean done = false;
                try {
                    int i = config().getWriteSpinCount() - 1;
                    while (true) {
                        if (i < 0) {
                            break;
                        }
                        if (!doWriteMessage(msg, in)) {
                            i--;
                        } else {
                            done = true;
                            break;
                        }
                    }
                } catch (Exception e) {
                    if (continueOnWriteError()) {
                        maxMessagesPerWrite--;
                        in.remove(e);
                    } else {
                        throw e;
                    }
                }
                if (!done) {
                    break;
                }
                maxMessagesPerWrite--;
                in.remove();
            } else {
                break;
            }
        }
        if (in.isEmpty()) {
            if ((interestOps & 4) != 0) {
                key.interestOps(interestOps & (-5));
            }
        } else if ((interestOps & 4) == 0) {
            key.interestOps(interestOps | 4);
        }
    }

    protected boolean continueOnWriteError() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean closeOnReadError(Throwable cause) {
        if (!isActive()) {
            return true;
        }
        if (cause instanceof PortUnreachableException) {
            return false;
        }
        if (cause instanceof IOException) {
            return !(this instanceof ServerChannel);
        }
        return true;
    }
}
