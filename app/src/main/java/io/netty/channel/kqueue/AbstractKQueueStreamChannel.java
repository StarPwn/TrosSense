package io.netty.channel.kqueue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.AbstractChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.EventLoop;
import io.netty.channel.FileRegion;
import io.netty.channel.kqueue.AbstractKQueueChannel;
import io.netty.channel.socket.DuplexChannel;
import io.netty.channel.unix.IovArray;
import io.netty.channel.unix.SocketWritableByteChannel;
import io.netty.channel.unix.UnixChannelUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.concurrent.Executor;

/* loaded from: classes4.dex */
public abstract class AbstractKQueueStreamChannel extends AbstractKQueueChannel implements DuplexChannel {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private WritableByteChannel byteChannel;
    private final Runnable flushTask;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) AbstractKQueueStreamChannel.class);
    private static final ChannelMetadata METADATA = new ChannelMetadata(false, 16);
    private static final String EXPECTED_TYPES = " (expected: " + StringUtil.simpleClassName((Class<?>) ByteBuf.class) + ", " + StringUtil.simpleClassName((Class<?>) DefaultFileRegion.class) + ')';

    @Override // io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.Channel
    public /* bridge */ /* synthetic */ boolean isActive() {
        return super.isActive();
    }

    @Override // io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.Channel
    public /* bridge */ /* synthetic */ boolean isOpen() {
        return super.isOpen();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AbstractKQueueStreamChannel(Channel parent, BsdSocket fd, boolean active) {
        super(parent, fd, active);
        this.flushTask = new Runnable() { // from class: io.netty.channel.kqueue.AbstractKQueueStreamChannel.1
            @Override // java.lang.Runnable
            public void run() {
                ((AbstractKQueueChannel.AbstractKQueueUnsafe) AbstractKQueueStreamChannel.this.unsafe()).flush0();
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AbstractKQueueStreamChannel(Channel parent, BsdSocket fd, SocketAddress remote) {
        super(parent, fd, remote);
        this.flushTask = new Runnable() { // from class: io.netty.channel.kqueue.AbstractKQueueStreamChannel.1
            @Override // java.lang.Runnable
            public void run() {
                ((AbstractKQueueChannel.AbstractKQueueUnsafe) AbstractKQueueStreamChannel.this.unsafe()).flush0();
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AbstractKQueueStreamChannel(BsdSocket fd) {
        this((Channel) null, fd, isSoErrorZero(fd));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.AbstractChannel
    public AbstractKQueueChannel.AbstractKQueueUnsafe newUnsafe() {
        return new KQueueStreamUnsafe();
    }

    @Override // io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.Channel
    public ChannelMetadata metadata() {
        return METADATA;
    }

    private int writeBytes(ChannelOutboundBuffer in, ByteBuf buf) throws Exception {
        int readableBytes = buf.readableBytes();
        if (readableBytes == 0) {
            in.remove();
            return 0;
        }
        if (buf.hasMemoryAddress() || buf.nioBufferCount() == 1) {
            return doWriteBytes(in, buf);
        }
        ByteBuffer[] nioBuffers = buf.nioBuffers();
        return writeBytesMultiple(in, nioBuffers, nioBuffers.length, readableBytes, config().getMaxBytesPerGatheringWrite());
    }

    private void adjustMaxBytesPerGatheringWrite(long attempted, long written, long oldMaxBytesPerGatheringWrite) {
        if (attempted == written) {
            if ((attempted << 1) > oldMaxBytesPerGatheringWrite) {
                config().setMaxBytesPerGatheringWrite(attempted << 1);
            }
        } else if (attempted > 4096 && written < (attempted >>> 1)) {
            config().setMaxBytesPerGatheringWrite(attempted >>> 1);
        }
    }

    private int writeBytesMultiple(ChannelOutboundBuffer in, IovArray array) throws IOException {
        long expectedWrittenBytes = array.size();
        if (expectedWrittenBytes == 0) {
            throw new AssertionError();
        }
        int cnt = array.count();
        if (cnt == 0) {
            throw new AssertionError();
        }
        long localWrittenBytes = this.socket.writevAddresses(array.memoryAddress(0), cnt);
        if (localWrittenBytes > 0) {
            adjustMaxBytesPerGatheringWrite(expectedWrittenBytes, localWrittenBytes, array.maxBytes());
            in.removeBytes(localWrittenBytes);
            return 1;
        }
        return Integer.MAX_VALUE;
    }

    private int writeBytesMultiple(ChannelOutboundBuffer in, ByteBuffer[] nioBuffers, int nioBufferCnt, long expectedWrittenBytes, long maxBytesPerGatheringWrite) throws IOException {
        long expectedWrittenBytes2;
        if (expectedWrittenBytes == 0) {
            throw new AssertionError();
        }
        if (expectedWrittenBytes <= maxBytesPerGatheringWrite) {
            expectedWrittenBytes2 = expectedWrittenBytes;
        } else {
            expectedWrittenBytes2 = maxBytesPerGatheringWrite;
        }
        long localWrittenBytes = this.socket.writev(nioBuffers, 0, nioBufferCnt, expectedWrittenBytes2);
        if (localWrittenBytes <= 0) {
            return Integer.MAX_VALUE;
        }
        adjustMaxBytesPerGatheringWrite(expectedWrittenBytes2, localWrittenBytes, maxBytesPerGatheringWrite);
        in.removeBytes(localWrittenBytes);
        return 1;
    }

    private int writeDefaultFileRegion(ChannelOutboundBuffer in, DefaultFileRegion region) throws Exception {
        long regionCount = region.count();
        long offset = region.transferred();
        if (offset >= regionCount) {
            in.remove();
            return 0;
        }
        long flushedAmount = this.socket.sendFile(region, region.position(), offset, regionCount - offset);
        if (flushedAmount <= 0) {
            if (flushedAmount == 0) {
                validateFileRegion(region, offset);
                return Integer.MAX_VALUE;
            }
            return Integer.MAX_VALUE;
        }
        in.progress(flushedAmount);
        if (region.transferred() >= regionCount) {
            in.remove();
            return 1;
        }
        return 1;
    }

    private int writeFileRegion(ChannelOutboundBuffer in, FileRegion region) throws Exception {
        if (region.transferred() >= region.count()) {
            in.remove();
            return 0;
        }
        if (this.byteChannel == null) {
            this.byteChannel = new KQueueSocketWritableByteChannel();
        }
        long flushedAmount = region.transferTo(this.byteChannel, region.transferred());
        if (flushedAmount > 0) {
            in.progress(flushedAmount);
            if (region.transferred() >= region.count()) {
                in.remove();
                return 1;
            }
            return 1;
        }
        return Integer.MAX_VALUE;
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doWrite(ChannelOutboundBuffer in) throws Exception {
        int writeSpinCount = config().getWriteSpinCount();
        do {
            int msgCount = in.size();
            if (msgCount > 1 && (in.current() instanceof ByteBuf)) {
                writeSpinCount -= doWriteMultiple(in);
            } else {
                if (msgCount == 0) {
                    writeFilter(false);
                    return;
                }
                writeSpinCount -= doWriteSingle(in);
            }
        } while (writeSpinCount > 0);
        if (writeSpinCount == 0) {
            writeFilter(false);
            eventLoop().execute(this.flushTask);
        } else {
            writeFilter(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int doWriteSingle(ChannelOutboundBuffer in) throws Exception {
        Object msg = in.current();
        if (msg instanceof ByteBuf) {
            return writeBytes(in, (ByteBuf) msg);
        }
        if (msg instanceof DefaultFileRegion) {
            return writeDefaultFileRegion(in, (DefaultFileRegion) msg);
        }
        if (msg instanceof FileRegion) {
            return writeFileRegion(in, (FileRegion) msg);
        }
        throw new Error();
    }

    private int doWriteMultiple(ChannelOutboundBuffer in) throws Exception {
        long maxBytesPerGatheringWrite = config().getMaxBytesPerGatheringWrite();
        IovArray array = ((KQueueEventLoop) eventLoop()).cleanArray();
        array.maxBytes(maxBytesPerGatheringWrite);
        in.forEachFlushedMessage(array);
        if (array.count() >= 1) {
            return writeBytesMultiple(in, array);
        }
        in.removeBytes(0L);
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.AbstractChannel
    public Object filterOutboundMessage(Object msg) {
        if (msg instanceof ByteBuf) {
            ByteBuf buf = (ByteBuf) msg;
            return UnixChannelUtil.isBufferCopyNeededForWrite(buf) ? newDirectBuffer(buf) : buf;
        }
        if (msg instanceof FileRegion) {
            return msg;
        }
        throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg) + EXPECTED_TYPES);
    }

    @Override // io.netty.channel.AbstractChannel
    protected final void doShutdownOutput() throws Exception {
        this.socket.shutdown(false, true);
    }

    @Override // io.netty.channel.socket.DuplexChannel
    public boolean isOutputShutdown() {
        return this.socket.isOutputShutdown();
    }

    @Override // io.netty.channel.socket.DuplexChannel
    public boolean isInputShutdown() {
        return this.socket.isInputShutdown();
    }

    @Override // io.netty.channel.socket.DuplexChannel
    public boolean isShutdown() {
        return this.socket.isShutdown();
    }

    @Override // io.netty.channel.socket.DuplexChannel
    public ChannelFuture shutdownOutput() {
        return shutdownOutput(newPromise());
    }

    @Override // io.netty.channel.socket.DuplexChannel
    public ChannelFuture shutdownOutput(final ChannelPromise promise) {
        EventLoop loop = eventLoop();
        if (loop.inEventLoop()) {
            ((AbstractChannel.AbstractUnsafe) unsafe()).shutdownOutput(promise);
        } else {
            loop.execute(new Runnable() { // from class: io.netty.channel.kqueue.AbstractKQueueStreamChannel.2
                @Override // java.lang.Runnable
                public void run() {
                    ((AbstractChannel.AbstractUnsafe) AbstractKQueueStreamChannel.this.unsafe()).shutdownOutput(promise);
                }
            });
        }
        return promise;
    }

    @Override // io.netty.channel.socket.DuplexChannel
    public ChannelFuture shutdownInput() {
        return shutdownInput(newPromise());
    }

    @Override // io.netty.channel.socket.DuplexChannel
    public ChannelFuture shutdownInput(final ChannelPromise promise) {
        EventLoop loop = eventLoop();
        if (loop.inEventLoop()) {
            shutdownInput0(promise);
        } else {
            loop.execute(new Runnable() { // from class: io.netty.channel.kqueue.AbstractKQueueStreamChannel.3
                @Override // java.lang.Runnable
                public void run() {
                    AbstractKQueueStreamChannel.this.shutdownInput0(promise);
                }
            });
        }
        return promise;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void shutdownInput0(ChannelPromise promise) {
        try {
            this.socket.shutdown(true, false);
            promise.setSuccess();
        } catch (Throwable cause) {
            promise.setFailure(cause);
        }
    }

    @Override // io.netty.channel.socket.DuplexChannel
    public ChannelFuture shutdown() {
        return shutdown(newPromise());
    }

    @Override // io.netty.channel.socket.DuplexChannel
    public ChannelFuture shutdown(final ChannelPromise promise) {
        ChannelFuture shutdownOutputFuture = shutdownOutput();
        if (shutdownOutputFuture.isDone()) {
            shutdownOutputDone(shutdownOutputFuture, promise);
        } else {
            shutdownOutputFuture.addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.channel.kqueue.AbstractKQueueStreamChannel.4
                @Override // io.netty.util.concurrent.GenericFutureListener
                public void operationComplete(ChannelFuture shutdownOutputFuture2) throws Exception {
                    AbstractKQueueStreamChannel.this.shutdownOutputDone(shutdownOutputFuture2, promise);
                }
            });
        }
        return promise;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void shutdownOutputDone(final ChannelFuture shutdownOutputFuture, final ChannelPromise promise) {
        ChannelFuture shutdownInputFuture = shutdownInput();
        if (shutdownInputFuture.isDone()) {
            shutdownDone(shutdownOutputFuture, shutdownInputFuture, promise);
        } else {
            shutdownInputFuture.addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.channel.kqueue.AbstractKQueueStreamChannel.5
                @Override // io.netty.util.concurrent.GenericFutureListener
                public void operationComplete(ChannelFuture shutdownInputFuture2) throws Exception {
                    AbstractKQueueStreamChannel.shutdownDone(shutdownOutputFuture, shutdownInputFuture2, promise);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void shutdownDone(ChannelFuture shutdownOutputFuture, ChannelFuture shutdownInputFuture, ChannelPromise promise) {
        Throwable shutdownOutputCause = shutdownOutputFuture.cause();
        Throwable shutdownInputCause = shutdownInputFuture.cause();
        if (shutdownOutputCause != null) {
            if (shutdownInputCause != null) {
                logger.debug("Exception suppressed because a previous exception occurred.", shutdownInputCause);
            }
            promise.setFailure(shutdownOutputCause);
        } else if (shutdownInputCause != null) {
            promise.setFailure(shutdownInputCause);
        } else {
            promise.setSuccess();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class KQueueStreamUnsafe extends AbstractKQueueChannel.AbstractKQueueUnsafe {
        /* JADX INFO: Access modifiers changed from: package-private */
        public KQueueStreamUnsafe() {
            super();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // io.netty.channel.AbstractChannel.AbstractUnsafe
        public Executor prepareToClose() {
            return super.prepareToClose();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // io.netty.channel.kqueue.AbstractKQueueChannel.AbstractKQueueUnsafe
        public void readReady(KQueueRecvByteAllocatorHandle allocHandle) {
            ChannelConfig config = AbstractKQueueStreamChannel.this.config();
            if (AbstractKQueueStreamChannel.this.shouldBreakReadReady(config)) {
                clearReadFilter0();
                return;
            }
            ChannelPipeline pipeline = AbstractKQueueStreamChannel.this.pipeline();
            ByteBufAllocator allocator = config.getAllocator();
            allocHandle.reset(config);
            readReadyBefore();
            ByteBuf byteBuf = null;
            boolean close = false;
            while (true) {
                try {
                    ByteBuf byteBuf2 = allocHandle.allocate(allocator);
                    allocHandle.lastBytesRead(AbstractKQueueStreamChannel.this.doReadBytes(byteBuf2));
                    boolean z = true;
                    if (allocHandle.lastBytesRead() <= 0) {
                        byteBuf2.release();
                        byteBuf = null;
                        if (allocHandle.lastBytesRead() >= 0) {
                            z = false;
                        }
                        close = z;
                        if (close) {
                            this.readPending = false;
                        }
                    } else {
                        allocHandle.incMessagesRead(1);
                        this.readPending = false;
                        pipeline.fireChannelRead((Object) byteBuf2);
                        byteBuf = null;
                        if (AbstractKQueueStreamChannel.this.shouldBreakReadReady(config) || !allocHandle.continueReading()) {
                            break;
                        }
                    }
                } catch (Throwable t) {
                    try {
                        handleReadException(pipeline, byteBuf, t, close, allocHandle);
                        return;
                    } finally {
                        readReadyFinally(config);
                    }
                }
            }
            allocHandle.readComplete();
            pipeline.fireChannelReadComplete();
            if (close) {
                shutdownInput(false);
            }
        }

        private void handleReadException(ChannelPipeline pipeline, ByteBuf byteBuf, Throwable cause, boolean close, KQueueRecvByteAllocatorHandle allocHandle) {
            if (byteBuf != null) {
                if (byteBuf.isReadable()) {
                    this.readPending = false;
                    pipeline.fireChannelRead((Object) byteBuf);
                } else {
                    byteBuf.release();
                }
            }
            if (!failConnectPromise(cause)) {
                allocHandle.readComplete();
                pipeline.fireChannelReadComplete();
                pipeline.fireExceptionCaught(cause);
                if (close || (cause instanceof OutOfMemoryError) || (cause instanceof IOException)) {
                    shutdownInput(false);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class KQueueSocketWritableByteChannel extends SocketWritableByteChannel {
        KQueueSocketWritableByteChannel() {
            super(AbstractKQueueStreamChannel.this.socket);
        }

        @Override // io.netty.channel.unix.SocketWritableByteChannel
        protected ByteBufAllocator alloc() {
            return AbstractKQueueStreamChannel.this.alloc();
        }
    }
}
