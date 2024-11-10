package io.netty.channel.epoll;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.AbstractChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.EventLoop;
import io.netty.channel.FileRegion;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.epoll.AbstractEpollChannel;
import io.netty.channel.socket.DuplexChannel;
import io.netty.channel.unix.FileDescriptor;
import io.netty.channel.unix.IovArray;
import io.netty.channel.unix.SocketWritableByteChannel;
import io.netty.channel.unix.UnixChannelUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.WritableByteChannel;
import java.util.Queue;
import java.util.concurrent.Executor;

/* loaded from: classes4.dex */
public abstract class AbstractEpollStreamChannel extends AbstractEpollChannel implements DuplexChannel {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private WritableByteChannel byteChannel;
    private final Runnable flushTask;
    private FileDescriptor pipeIn;
    private FileDescriptor pipeOut;
    private volatile Queue<SpliceInTask> spliceQueue;
    private static final ChannelMetadata METADATA = new ChannelMetadata(false, 16);
    private static final String EXPECTED_TYPES = " (expected: " + StringUtil.simpleClassName((Class<?>) ByteBuf.class) + ", " + StringUtil.simpleClassName((Class<?>) DefaultFileRegion.class) + ')';
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) AbstractEpollStreamChannel.class);

    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.Channel
    public /* bridge */ /* synthetic */ boolean isActive() {
        return super.isActive();
    }

    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.Channel
    public /* bridge */ /* synthetic */ boolean isOpen() {
        return super.isOpen();
    }

    protected AbstractEpollStreamChannel(Channel parent, int fd) {
        this(parent, new LinuxSocket(fd));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractEpollStreamChannel(int fd) {
        this(new LinuxSocket(fd));
    }

    AbstractEpollStreamChannel(LinuxSocket fd) {
        this(fd, isSoErrorZero(fd));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AbstractEpollStreamChannel(Channel parent, LinuxSocket fd) {
        super(parent, fd, true);
        this.flushTask = new Runnable() { // from class: io.netty.channel.epoll.AbstractEpollStreamChannel.1
            @Override // java.lang.Runnable
            public void run() {
                ((AbstractEpollChannel.AbstractEpollUnsafe) AbstractEpollStreamChannel.this.unsafe()).flush0();
            }
        };
        this.flags |= Native.EPOLLRDHUP;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractEpollStreamChannel(Channel parent, LinuxSocket fd, SocketAddress remote) {
        super(parent, fd, remote);
        this.flushTask = new Runnable() { // from class: io.netty.channel.epoll.AbstractEpollStreamChannel.1
            @Override // java.lang.Runnable
            public void run() {
                ((AbstractEpollChannel.AbstractEpollUnsafe) AbstractEpollStreamChannel.this.unsafe()).flush0();
            }
        };
        this.flags |= Native.EPOLLRDHUP;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractEpollStreamChannel(LinuxSocket fd, boolean active) {
        super((Channel) null, fd, active);
        this.flushTask = new Runnable() { // from class: io.netty.channel.epoll.AbstractEpollStreamChannel.1
            @Override // java.lang.Runnable
            public void run() {
                ((AbstractEpollChannel.AbstractEpollUnsafe) AbstractEpollStreamChannel.this.unsafe()).flush0();
            }
        };
        this.flags |= Native.EPOLLRDHUP;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.AbstractChannel
    public AbstractEpollChannel.AbstractEpollUnsafe newUnsafe() {
        return new EpollStreamUnsafe();
    }

    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.Channel
    public ChannelMetadata metadata() {
        return METADATA;
    }

    public final ChannelFuture spliceTo(AbstractEpollStreamChannel ch, int len) {
        return spliceTo(ch, len, newPromise());
    }

    public final ChannelFuture spliceTo(AbstractEpollStreamChannel ch, int len, ChannelPromise promise) {
        if (ch.eventLoop() != eventLoop()) {
            throw new IllegalArgumentException("EventLoops are not the same.");
        }
        ObjectUtil.checkPositiveOrZero(len, "len");
        if (ch.config().getEpollMode() != EpollMode.LEVEL_TRIGGERED || config().getEpollMode() != EpollMode.LEVEL_TRIGGERED) {
            throw new IllegalStateException("spliceTo() supported only when using " + EpollMode.LEVEL_TRIGGERED);
        }
        ObjectUtil.checkNotNull(promise, "promise");
        if (!isOpen()) {
            promise.tryFailure(new ClosedChannelException());
        } else {
            addToSpliceQueue(new SpliceInChannelTask(ch, len, promise));
            failSpliceIfClosed(promise);
        }
        return promise;
    }

    public final ChannelFuture spliceTo(FileDescriptor ch, int offset, int len) {
        return spliceTo(ch, offset, len, newPromise());
    }

    public final ChannelFuture spliceTo(FileDescriptor ch, int offset, int len, ChannelPromise promise) {
        ObjectUtil.checkPositiveOrZero(len, "len");
        ObjectUtil.checkPositiveOrZero(offset, "offset");
        if (config().getEpollMode() != EpollMode.LEVEL_TRIGGERED) {
            throw new IllegalStateException("spliceTo() supported only when using " + EpollMode.LEVEL_TRIGGERED);
        }
        ObjectUtil.checkNotNull(promise, "promise");
        if (!isOpen()) {
            promise.tryFailure(new ClosedChannelException());
        } else {
            addToSpliceQueue(new SpliceFdTask(ch, offset, len, promise));
            failSpliceIfClosed(promise);
        }
        return promise;
    }

    private void failSpliceIfClosed(ChannelPromise promise) {
        if (!isOpen() && !promise.isDone()) {
            final ClosedChannelException ex = new ClosedChannelException();
            if (promise.tryFailure(ex)) {
                eventLoop().execute(new Runnable() { // from class: io.netty.channel.epoll.AbstractEpollStreamChannel.2
                    @Override // java.lang.Runnable
                    public void run() {
                        AbstractEpollStreamChannel.this.clearSpliceQueue(ex);
                    }
                });
            }
        }
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
        long offset = region.transferred();
        long regionCount = region.count();
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
            this.byteChannel = new EpollSocketWritableByteChannel();
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
                    clearFlag(Native.EPOLLOUT);
                    return;
                }
                writeSpinCount -= doWriteSingle(in);
            }
        } while (writeSpinCount > 0);
        if (writeSpinCount == 0) {
            clearFlag(Native.EPOLLOUT);
            eventLoop().execute(this.flushTask);
        } else {
            setFlag(Native.EPOLLOUT);
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
        if (msg instanceof SpliceOutTask) {
            if (!((SpliceOutTask) msg).spliceOut()) {
                return Integer.MAX_VALUE;
            }
            in.remove();
            return 1;
        }
        throw new Error();
    }

    private int doWriteMultiple(ChannelOutboundBuffer in) throws Exception {
        long maxBytesPerGatheringWrite = config().getMaxBytesPerGatheringWrite();
        IovArray array = ((EpollEventLoop) eventLoop()).cleanIovArray();
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
        if ((msg instanceof FileRegion) || (msg instanceof SpliceOutTask)) {
            return msg;
        }
        throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg) + EXPECTED_TYPES);
    }

    @Override // io.netty.channel.AbstractChannel
    protected final void doShutdownOutput() throws Exception {
        this.socket.shutdown(false, true);
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
            loop.execute(new Runnable() { // from class: io.netty.channel.epoll.AbstractEpollStreamChannel.3
                @Override // java.lang.Runnable
                public void run() {
                    ((AbstractChannel.AbstractUnsafe) AbstractEpollStreamChannel.this.unsafe()).shutdownOutput(promise);
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
        Executor closeExecutor = ((EpollStreamUnsafe) unsafe()).prepareToClose();
        if (closeExecutor != null) {
            closeExecutor.execute(new Runnable() { // from class: io.netty.channel.epoll.AbstractEpollStreamChannel.4
                @Override // java.lang.Runnable
                public void run() {
                    AbstractEpollStreamChannel.this.shutdownInput0(promise);
                }
            });
        } else {
            EventLoop loop = eventLoop();
            if (loop.inEventLoop()) {
                shutdownInput0(promise);
            } else {
                loop.execute(new Runnable() { // from class: io.netty.channel.epoll.AbstractEpollStreamChannel.5
                    @Override // java.lang.Runnable
                    public void run() {
                        AbstractEpollStreamChannel.this.shutdownInput0(promise);
                    }
                });
            }
        }
        return promise;
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
            shutdownOutputFuture.addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.channel.epoll.AbstractEpollStreamChannel.6
                @Override // io.netty.util.concurrent.GenericFutureListener
                public void operationComplete(ChannelFuture shutdownOutputFuture2) throws Exception {
                    AbstractEpollStreamChannel.this.shutdownOutputDone(shutdownOutputFuture2, promise);
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
            shutdownInputFuture.addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.channel.epoll.AbstractEpollStreamChannel.7
                @Override // io.netty.util.concurrent.GenericFutureListener
                public void operationComplete(ChannelFuture shutdownInputFuture2) throws Exception {
                    AbstractEpollStreamChannel.shutdownDone(shutdownOutputFuture, shutdownInputFuture2, promise);
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

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.AbstractChannel
    public void doClose() throws Exception {
        try {
            super.doClose();
        } finally {
            safeClosePipe(this.pipeIn);
            safeClosePipe(this.pipeOut);
            clearSpliceQueue(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearSpliceQueue(ClosedChannelException exception) {
        Queue<SpliceInTask> sQueue = this.spliceQueue;
        if (sQueue == null) {
            return;
        }
        while (true) {
            SpliceInTask task = sQueue.poll();
            if (task != null) {
                if (exception == null) {
                    exception = new ClosedChannelException();
                }
                task.promise.tryFailure(exception);
            } else {
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void safeClosePipe(FileDescriptor fd) {
        if (fd != null) {
            try {
                fd.close();
            } catch (IOException e) {
                logger.warn("Error while closing a pipe", (Throwable) e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class EpollStreamUnsafe extends AbstractEpollChannel.AbstractEpollUnsafe {
        /* JADX INFO: Access modifiers changed from: package-private */
        public EpollStreamUnsafe() {
            super();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // io.netty.channel.AbstractChannel.AbstractUnsafe
        public Executor prepareToClose() {
            return super.prepareToClose();
        }

        private void handleReadException(ChannelPipeline pipeline, ByteBuf byteBuf, Throwable cause, boolean close, EpollRecvByteAllocatorHandle allocHandle) {
            if (byteBuf != null) {
                if (byteBuf.isReadable()) {
                    this.readPending = false;
                    pipeline.fireChannelRead((Object) byteBuf);
                } else {
                    byteBuf.release();
                }
            }
            allocHandle.readComplete();
            pipeline.fireChannelReadComplete();
            pipeline.fireExceptionCaught(cause);
            if (close || (cause instanceof OutOfMemoryError) || (cause instanceof IOException)) {
                shutdownInput(false);
            }
        }

        @Override // io.netty.channel.epoll.AbstractEpollChannel.AbstractEpollUnsafe
        EpollRecvByteAllocatorHandle newEpollHandle(RecvByteBufAllocator.ExtendedHandle handle) {
            return new EpollRecvByteAllocatorStreamingHandle(handle);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* JADX WARN: Code restructure failed: missing block: B:70:0x003f, code lost:            if (r7 != null) goto L11;     */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:20:0x00a2 A[EDGE_INSN: B:20:0x00a2->B:21:0x00a2 BREAK  A[LOOP:0: B:7:0x0034->B:52:?], SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:52:? A[LOOP:0: B:7:0x0034->B:52:?, LOOP_END, SYNTHETIC] */
        @Override // io.netty.channel.epoll.AbstractEpollChannel.AbstractEpollUnsafe
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void epollInReady() {
            /*
                Method dump skipped, instructions count: 243
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: io.netty.channel.epoll.AbstractEpollStreamChannel.EpollStreamUnsafe.epollInReady():void");
        }
    }

    private void addToSpliceQueue(SpliceInTask task) {
        Queue<SpliceInTask> sQueue = this.spliceQueue;
        if (sQueue == null) {
            synchronized (this) {
                sQueue = this.spliceQueue;
                if (sQueue == null) {
                    Queue<SpliceInTask> newMpscQueue = PlatformDependent.newMpscQueue();
                    sQueue = newMpscQueue;
                    this.spliceQueue = newMpscQueue;
                }
            }
        }
        sQueue.add(task);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public abstract class SpliceInTask {
        int len;
        final ChannelPromise promise;

        abstract boolean spliceIn(RecvByteBufAllocator.Handle handle);

        protected SpliceInTask(int len, ChannelPromise promise) {
            this.promise = promise;
            this.len = len;
        }

        protected final int spliceIn(FileDescriptor pipeOut, RecvByteBufAllocator.Handle handle) throws IOException {
            int length = Math.min(handle.guess(), this.len);
            int splicedIn = 0;
            while (true) {
                int localSplicedIn = Native.splice(AbstractEpollStreamChannel.this.socket.intValue(), -1L, pipeOut.intValue(), -1L, length);
                handle.lastBytesRead(localSplicedIn);
                if (localSplicedIn != 0) {
                    splicedIn += localSplicedIn;
                    length -= localSplicedIn;
                } else {
                    return splicedIn;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class SpliceInChannelTask extends SpliceInTask implements ChannelFutureListener {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private final AbstractEpollStreamChannel ch;

        SpliceInChannelTask(AbstractEpollStreamChannel ch, int len, ChannelPromise promise) {
            super(len, promise);
            this.ch = ch;
        }

        @Override // io.netty.util.concurrent.GenericFutureListener
        public void operationComplete(ChannelFuture future) throws Exception {
            if (!future.isSuccess()) {
                this.promise.tryFailure(future.cause());
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r4v13 */
        /* JADX WARN: Type inference failed for: r4v14 */
        /* JADX WARN: Type inference failed for: r4v6, types: [io.netty.channel.ChannelPromise] */
        /* JADX WARN: Type inference failed for: r6v1, types: [io.netty.channel.Channel$Unsafe] */
        @Override // io.netty.channel.epoll.AbstractEpollStreamChannel.SpliceInTask
        public boolean spliceIn(RecvByteBufAllocator.Handle handle) {
            ?? r4;
            if (!this.ch.eventLoop().inEventLoop()) {
                throw new AssertionError();
            }
            if (this.len != 0) {
                try {
                    FileDescriptor fileDescriptor = this.ch.pipeOut;
                    if (fileDescriptor == null) {
                        FileDescriptor[] pipe = FileDescriptor.pipe();
                        this.ch.pipeIn = pipe[0];
                        fileDescriptor = this.ch.pipeOut = pipe[1];
                    }
                    int spliceIn = spliceIn(fileDescriptor, handle);
                    if (spliceIn > 0) {
                        if (this.len != Integer.MAX_VALUE) {
                            this.len -= spliceIn;
                        }
                        if (this.len == 0) {
                            r4 = this.promise;
                        } else {
                            r4 = this.ch.newPromise().addListener((GenericFutureListener<? extends Future<? super Void>>) this);
                        }
                        boolean isAutoRead = AbstractEpollStreamChannel.this.config().isAutoRead();
                        this.ch.unsafe().write(new SpliceOutTask(this.ch, spliceIn, isAutoRead), r4);
                        this.ch.unsafe().flush();
                        if (isAutoRead && !r4.isDone()) {
                            AbstractEpollStreamChannel.this.config().setAutoRead(false);
                        }
                    }
                    return this.len == 0;
                } catch (Throwable th) {
                    this.promise.tryFailure(th);
                    return true;
                }
            }
            this.promise.trySuccess();
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class SpliceOutTask {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private final boolean autoRead;
        private final AbstractEpollStreamChannel ch;
        private int len;

        SpliceOutTask(AbstractEpollStreamChannel ch, int len, boolean autoRead) {
            this.ch = ch;
            this.len = len;
            this.autoRead = autoRead;
        }

        public boolean spliceOut() throws Exception {
            if (this.ch.eventLoop().inEventLoop()) {
                try {
                    int splicedOut = Native.splice(this.ch.pipeIn.intValue(), -1L, this.ch.socket.intValue(), -1L, this.len);
                    this.len -= splicedOut;
                    if (this.len == 0) {
                        if (this.autoRead) {
                            AbstractEpollStreamChannel.this.config().setAutoRead(true);
                        }
                        return true;
                    }
                    return false;
                } catch (IOException e) {
                    if (this.autoRead) {
                        AbstractEpollStreamChannel.this.config().setAutoRead(true);
                    }
                    throw e;
                }
            }
            throw new AssertionError();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class SpliceFdTask extends SpliceInTask {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private final FileDescriptor fd;
        private int offset;
        private final ChannelPromise promise;

        SpliceFdTask(FileDescriptor fd, int offset, int len, ChannelPromise promise) {
            super(len, promise);
            this.fd = fd;
            this.promise = promise;
            this.offset = offset;
        }

        @Override // io.netty.channel.epoll.AbstractEpollStreamChannel.SpliceInTask
        public boolean spliceIn(RecvByteBufAllocator.Handle handle) {
            if (!AbstractEpollStreamChannel.this.eventLoop().inEventLoop()) {
                throw new AssertionError();
            }
            if (this.len == 0) {
                this.promise.trySuccess();
                return true;
            }
            try {
                FileDescriptor[] pipe = FileDescriptor.pipe();
                FileDescriptor pipeIn = pipe[0];
                FileDescriptor pipeOut = pipe[1];
                try {
                    int splicedIn = spliceIn(pipeOut, handle);
                    if (splicedIn > 0) {
                        if (this.len != Integer.MAX_VALUE) {
                            this.len -= splicedIn;
                        }
                        do {
                            int splicedOut = Native.splice(pipeIn.intValue(), -1L, this.fd.intValue(), this.offset, splicedIn);
                            this.offset += splicedOut;
                            splicedIn -= splicedOut;
                        } while (splicedIn > 0);
                        if (this.len == 0) {
                            this.promise.trySuccess();
                            return true;
                        }
                    }
                    return false;
                } finally {
                    AbstractEpollStreamChannel.safeClosePipe(pipeIn);
                    AbstractEpollStreamChannel.safeClosePipe(pipeOut);
                }
            } catch (Throwable cause) {
                this.promise.tryFailure(cause);
                return true;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class EpollSocketWritableByteChannel extends SocketWritableByteChannel {
        static final /* synthetic */ boolean $assertionsDisabled = false;

        EpollSocketWritableByteChannel() {
            super(AbstractEpollStreamChannel.this.socket);
            if (this.fd != AbstractEpollStreamChannel.this.socket) {
                throw new AssertionError();
            }
        }

        @Override // io.netty.channel.unix.SocketWritableByteChannel
        protected int write(ByteBuffer buf, int pos, int limit) throws IOException {
            return AbstractEpollStreamChannel.this.socket.send(buf, pos, limit);
        }

        @Override // io.netty.channel.unix.SocketWritableByteChannel
        protected ByteBufAllocator alloc() {
            return AbstractEpollStreamChannel.this.alloc();
        }
    }
}
