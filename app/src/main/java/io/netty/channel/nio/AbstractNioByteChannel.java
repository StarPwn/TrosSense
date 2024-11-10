package io.netty.channel.nio;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.FileRegion;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.nio.AbstractNioChannel;
import io.netty.channel.socket.ChannelInputShutdownEvent;
import io.netty.channel.socket.ChannelInputShutdownReadComplete;
import io.netty.channel.socket.SocketChannelConfig;
import io.netty.util.internal.StringUtil;
import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;

/* loaded from: classes4.dex */
public abstract class AbstractNioByteChannel extends AbstractNioChannel {
    private final Runnable flushTask;
    private boolean inputClosedSeenErrorOnRead;
    private static final ChannelMetadata METADATA = new ChannelMetadata(false, 16);
    private static final String EXPECTED_TYPES = " (expected: " + StringUtil.simpleClassName((Class<?>) ByteBuf.class) + ", " + StringUtil.simpleClassName((Class<?>) FileRegion.class) + ')';

    protected abstract int doReadBytes(ByteBuf byteBuf) throws Exception;

    protected abstract int doWriteBytes(ByteBuf byteBuf) throws Exception;

    protected abstract long doWriteFileRegion(FileRegion fileRegion) throws Exception;

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract ChannelFuture shutdownInput();

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractNioByteChannel(Channel parent, SelectableChannel ch) {
        super(parent, ch, 1);
        this.flushTask = new Runnable() { // from class: io.netty.channel.nio.AbstractNioByteChannel.1
            @Override // java.lang.Runnable
            public void run() {
                ((AbstractNioChannel.AbstractNioUnsafe) AbstractNioByteChannel.this.unsafe()).flush0();
            }
        };
    }

    protected boolean isInputShutdown0() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.AbstractChannel
    public AbstractNioChannel.AbstractNioUnsafe newUnsafe() {
        return new NioByteUnsafe();
    }

    @Override // io.netty.channel.Channel
    public ChannelMetadata metadata() {
        return METADATA;
    }

    final boolean shouldBreakReadReady(ChannelConfig config) {
        return isInputShutdown0() && (this.inputClosedSeenErrorOnRead || !isAllowHalfClosure(config));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isAllowHalfClosure(ChannelConfig config) {
        return (config instanceof SocketChannelConfig) && ((SocketChannelConfig) config).isAllowHalfClosure();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public class NioByteUnsafe extends AbstractNioChannel.AbstractNioUnsafe {
        /* JADX INFO: Access modifiers changed from: protected */
        public NioByteUnsafe() {
            super();
        }

        private void closeOnRead(ChannelPipeline pipeline) {
            if (!AbstractNioByteChannel.this.isInputShutdown0()) {
                if (AbstractNioByteChannel.isAllowHalfClosure(AbstractNioByteChannel.this.config())) {
                    AbstractNioByteChannel.this.shutdownInput();
                    pipeline.fireUserEventTriggered((Object) ChannelInputShutdownEvent.INSTANCE);
                    return;
                } else {
                    close(voidPromise());
                    return;
                }
            }
            if (!AbstractNioByteChannel.this.inputClosedSeenErrorOnRead) {
                AbstractNioByteChannel.this.inputClosedSeenErrorOnRead = true;
                pipeline.fireUserEventTriggered((Object) ChannelInputShutdownReadComplete.INSTANCE);
            }
        }

        private void handleReadException(ChannelPipeline pipeline, ByteBuf byteBuf, Throwable cause, boolean close, RecvByteBufAllocator.Handle allocHandle) {
            if (byteBuf != null) {
                if (byteBuf.isReadable()) {
                    AbstractNioByteChannel.this.readPending = false;
                    pipeline.fireChannelRead((Object) byteBuf);
                } else {
                    byteBuf.release();
                }
            }
            allocHandle.readComplete();
            pipeline.fireChannelReadComplete();
            pipeline.fireExceptionCaught(cause);
            if (close || (cause instanceof OutOfMemoryError) || (cause instanceof IOException)) {
                closeOnRead(pipeline);
            }
        }

        @Override // io.netty.channel.nio.AbstractNioChannel.NioUnsafe
        public final void read() {
            ChannelConfig config = AbstractNioByteChannel.this.config();
            if (AbstractNioByteChannel.this.shouldBreakReadReady(config)) {
                AbstractNioByteChannel.this.clearReadPending();
                return;
            }
            ChannelPipeline pipeline = AbstractNioByteChannel.this.pipeline();
            ByteBufAllocator allocator = config.getAllocator();
            RecvByteBufAllocator.Handle allocHandle = recvBufAllocHandle();
            allocHandle.reset(config);
            ByteBuf byteBuf = null;
            boolean close = false;
            while (true) {
                try {
                    ByteBuf byteBuf2 = allocHandle.allocate(allocator);
                    allocHandle.lastBytesRead(AbstractNioByteChannel.this.doReadBytes(byteBuf2));
                    boolean z = true;
                    if (allocHandle.lastBytesRead() <= 0) {
                        byteBuf2.release();
                        byteBuf = null;
                        if (allocHandle.lastBytesRead() >= 0) {
                            z = false;
                        }
                        close = z;
                        if (close) {
                            AbstractNioByteChannel.this.readPending = false;
                        }
                    } else {
                        allocHandle.incMessagesRead(1);
                        AbstractNioByteChannel.this.readPending = false;
                        pipeline.fireChannelRead((Object) byteBuf2);
                        byteBuf = null;
                        if (!allocHandle.continueReading()) {
                            break;
                        }
                    }
                } catch (Throwable t) {
                    try {
                        handleReadException(pipeline, byteBuf, t, close, allocHandle);
                        return;
                    } finally {
                        if (!AbstractNioByteChannel.this.readPending && !config.isAutoRead()) {
                            removeReadOp();
                        }
                    }
                }
            }
            allocHandle.readComplete();
            pipeline.fireChannelReadComplete();
            if (close) {
                closeOnRead(pipeline);
            }
            if (AbstractNioByteChannel.this.readPending || config.isAutoRead()) {
                return;
            }
            removeReadOp();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final int doWrite0(ChannelOutboundBuffer in) throws Exception {
        Object msg = in.current();
        if (msg == null) {
            return 0;
        }
        return doWriteInternal(in, in.current());
    }

    private int doWriteInternal(ChannelOutboundBuffer in, Object msg) throws Exception {
        if (msg instanceof ByteBuf) {
            ByteBuf buf = (ByteBuf) msg;
            if (!buf.isReadable()) {
                in.remove();
                return 0;
            }
            int localFlushedAmount = doWriteBytes(buf);
            if (localFlushedAmount > 0) {
                in.progress(localFlushedAmount);
                if (!buf.isReadable()) {
                    in.remove();
                }
                return 1;
            }
            return Integer.MAX_VALUE;
        }
        if (msg instanceof FileRegion) {
            FileRegion region = (FileRegion) msg;
            if (region.transferred() >= region.count()) {
                in.remove();
                return 0;
            }
            long localFlushedAmount2 = doWriteFileRegion(region);
            if (localFlushedAmount2 > 0) {
                in.progress(localFlushedAmount2);
                if (region.transferred() >= region.count()) {
                    in.remove();
                }
                return 1;
            }
            return Integer.MAX_VALUE;
        }
        throw new Error();
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doWrite(ChannelOutboundBuffer in) throws Exception {
        int writeSpinCount = config().getWriteSpinCount();
        do {
            Object msg = in.current();
            if (msg == null) {
                clearOpWrite();
                return;
            }
            writeSpinCount -= doWriteInternal(in, msg);
        } while (writeSpinCount > 0);
        incompleteWrite(writeSpinCount < 0);
    }

    @Override // io.netty.channel.AbstractChannel
    protected final Object filterOutboundMessage(Object msg) {
        if (msg instanceof ByteBuf) {
            ByteBuf buf = (ByteBuf) msg;
            if (buf.isDirect()) {
                return msg;
            }
            return newDirectBuffer(buf);
        }
        if (msg instanceof FileRegion) {
            return msg;
        }
        throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg) + EXPECTED_TYPES);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void incompleteWrite(boolean setOpWrite) {
        if (setOpWrite) {
            setOpWrite();
        } else {
            clearOpWrite();
            eventLoop().execute(this.flushTask);
        }
    }

    protected final void setOpWrite() {
        SelectionKey key = selectionKey();
        if (!key.isValid()) {
            return;
        }
        int interestOps = key.interestOps();
        if ((interestOps & 4) == 0) {
            key.interestOps(interestOps | 4);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void clearOpWrite() {
        SelectionKey key = selectionKey();
        if (!key.isValid()) {
            return;
        }
        int interestOps = key.interestOps();
        if ((interestOps & 4) != 0) {
            key.interestOps(interestOps & (-5));
        }
    }
}
