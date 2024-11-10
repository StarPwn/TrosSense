package io.netty.handler.stream;

import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelProgressivePromise;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.nio.channels.ClosedChannelException;
import java.util.ArrayDeque;
import java.util.Queue;

/* loaded from: classes4.dex */
public class ChunkedWriteHandler extends ChannelDuplexHandler {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) ChunkedWriteHandler.class);
    private volatile ChannelHandlerContext ctx;
    private Queue<PendingWrite> queue;

    public ChunkedWriteHandler() {
    }

    @Deprecated
    public ChunkedWriteHandler(int maxPendingWrites) {
        ObjectUtil.checkPositive(maxPendingWrites, "maxPendingWrites");
    }

    private void allocateQueue() {
        if (this.queue == null) {
            this.queue = new ArrayDeque();
        }
    }

    private boolean queueIsEmpty() {
        return this.queue == null || this.queue.isEmpty();
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
    }

    public void resumeTransfer() {
        final ChannelHandlerContext ctx = this.ctx;
        if (ctx == null) {
            return;
        }
        if (ctx.executor().inEventLoop()) {
            resumeTransfer0(ctx);
        } else {
            ctx.executor().execute(new Runnable() { // from class: io.netty.handler.stream.ChunkedWriteHandler.1
                @Override // java.lang.Runnable
                public void run() {
                    ChunkedWriteHandler.this.resumeTransfer0(ctx);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resumeTransfer0(ChannelHandlerContext ctx) {
        try {
            doFlush(ctx);
        } catch (Exception e) {
            logger.warn("Unexpected exception while sending chunks.", (Throwable) e);
        }
    }

    @Override // io.netty.channel.ChannelDuplexHandler, io.netty.channel.ChannelOutboundHandler
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (!queueIsEmpty() || (msg instanceof ChunkedInput)) {
            allocateQueue();
            this.queue.add(new PendingWrite(msg, promise));
        } else {
            ctx.write(msg, promise);
        }
    }

    @Override // io.netty.channel.ChannelDuplexHandler, io.netty.channel.ChannelOutboundHandler
    public void flush(ChannelHandlerContext ctx) throws Exception {
        doFlush(ctx);
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        doFlush(ctx);
        ctx.fireChannelInactive();
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        if (ctx.channel().isWritable()) {
            doFlush(ctx);
        }
        ctx.fireChannelWritabilityChanged();
    }

    private void discard(Throwable cause) {
        if (queueIsEmpty()) {
            return;
        }
        while (true) {
            PendingWrite currentWrite = this.queue.poll();
            if (currentWrite != null) {
                Object message = currentWrite.msg;
                if (message instanceof ChunkedInput) {
                    ChunkedInput<?> in = (ChunkedInput) message;
                    try {
                        boolean endOfInput = in.isEndOfInput();
                        long inputLength = in.length();
                        closeInput(in);
                        if (!endOfInput) {
                            if (cause == null) {
                                cause = new ClosedChannelException();
                            }
                            currentWrite.fail(cause);
                        } else {
                            currentWrite.success(inputLength);
                        }
                    } catch (Exception e) {
                        closeInput(in);
                        currentWrite.fail(e);
                        logger.warn("ChunkedInput failed", (Throwable) e);
                    }
                } else {
                    if (cause == null) {
                        cause = new ClosedChannelException();
                    }
                    currentWrite.fail(cause);
                }
            } else {
                return;
            }
        }
    }

    private void doFlush(ChannelHandlerContext ctx) {
        final PendingWrite currentWrite;
        Channel channel = ctx.channel();
        if (!channel.isActive()) {
            discard(null);
            return;
        }
        if (queueIsEmpty()) {
            ctx.flush();
            return;
        }
        boolean requiresFlush = true;
        ByteBufAllocator allocator = ctx.alloc();
        while (true) {
            if (!channel.isWritable() || (currentWrite = this.queue.peek()) == null) {
                break;
            }
            if (currentWrite.promise.isDone()) {
                this.queue.remove();
            } else {
                Object pendingMessage = currentWrite.msg;
                if (pendingMessage instanceof ChunkedInput) {
                    final ChunkedInput<?> chunks = (ChunkedInput) pendingMessage;
                    Object message = null;
                    try {
                        message = chunks.readChunk(allocator);
                        boolean endOfInput = chunks.isEndOfInput();
                        boolean suspend = message == null && !endOfInput;
                        if (suspend) {
                            break;
                        }
                        if (message == null) {
                            message = Unpooled.EMPTY_BUFFER;
                        }
                        if (endOfInput) {
                            this.queue.remove();
                        }
                        ChannelFuture f = ctx.writeAndFlush(message);
                        if (!endOfInput) {
                            final boolean resume = true ^ channel.isWritable();
                            if (f.isDone()) {
                                handleFuture(f, chunks, currentWrite, resume);
                            } else {
                                f.addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.handler.stream.ChunkedWriteHandler.3
                                    @Override // io.netty.util.concurrent.GenericFutureListener
                                    public void operationComplete(ChannelFuture future) {
                                        ChunkedWriteHandler.this.handleFuture(future, chunks, currentWrite, resume);
                                    }
                                });
                            }
                        } else if (f.isDone()) {
                            handleEndOfInputFuture(f, chunks, currentWrite);
                        } else {
                            f.addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.handler.stream.ChunkedWriteHandler.2
                                @Override // io.netty.util.concurrent.GenericFutureListener
                                public void operationComplete(ChannelFuture future) {
                                    ChunkedWriteHandler.handleEndOfInputFuture(future, chunks, currentWrite);
                                }
                            });
                        }
                        requiresFlush = false;
                    } catch (Throwable t) {
                        this.queue.remove();
                        if (message != null) {
                            ReferenceCountUtil.release(message);
                        }
                        closeInput(chunks);
                        currentWrite.fail(t);
                    }
                } else {
                    this.queue.remove();
                    ctx.write(pendingMessage, currentWrite.promise);
                    requiresFlush = true;
                }
                if (!channel.isActive()) {
                    discard(new ClosedChannelException());
                    break;
                }
            }
        }
        if (requiresFlush) {
            ctx.flush();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void handleEndOfInputFuture(ChannelFuture future, ChunkedInput<?> input, PendingWrite currentWrite) {
        if (!future.isSuccess()) {
            closeInput(input);
            currentWrite.fail(future.cause());
            return;
        }
        long inputProgress = input.progress();
        long inputLength = input.length();
        closeInput(input);
        currentWrite.progress(inputProgress, inputLength);
        currentWrite.success(inputLength);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleFuture(ChannelFuture future, ChunkedInput<?> input, PendingWrite currentWrite, boolean resume) {
        if (!future.isSuccess()) {
            closeInput(input);
            currentWrite.fail(future.cause());
            return;
        }
        currentWrite.progress(input.progress(), input.length());
        if (resume && future.channel().isWritable()) {
            resumeTransfer();
        }
    }

    private static void closeInput(ChunkedInput<?> chunks) {
        try {
            chunks.close();
        } catch (Throwable t) {
            logger.warn("Failed to close a ChunkedInput.", t);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class PendingWrite {
        final Object msg;
        final ChannelPromise promise;

        PendingWrite(Object msg, ChannelPromise promise) {
            this.msg = msg;
            this.promise = promise;
        }

        void fail(Throwable cause) {
            ReferenceCountUtil.release(this.msg);
            this.promise.tryFailure(cause);
        }

        void success(long total) {
            if (this.promise.isDone()) {
                return;
            }
            progress(total, total);
            this.promise.trySuccess();
        }

        void progress(long progress, long total) {
            if (this.promise instanceof ChannelProgressivePromise) {
                ((ChannelProgressivePromise) this.promise).tryProgress(progress, total);
            }
        }
    }
}
