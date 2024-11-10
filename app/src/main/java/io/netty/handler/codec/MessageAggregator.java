package io.netty.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.ObjectUtil;
import java.util.List;

/* loaded from: classes4.dex */
public abstract class MessageAggregator<I, S, C extends ByteBufHolder, O extends ByteBufHolder> extends MessageToMessageDecoder<I> {
    private static final int DEFAULT_MAX_COMPOSITEBUFFER_COMPONENTS = 1024;
    private boolean aggregating;
    private ChannelFutureListener continueResponseWriteListener;
    private ChannelHandlerContext ctx;
    private O currentMessage;
    private boolean handleIncompleteAggregateDuringClose;
    private boolean handlingOversizedMessage;
    private final int maxContentLength;
    private int maxCumulationBufferComponents;

    protected abstract O beginAggregation(S s, ByteBuf byteBuf) throws Exception;

    protected abstract boolean closeAfterContinueResponse(Object obj) throws Exception;

    protected abstract boolean ignoreContentAfterContinueResponse(Object obj) throws Exception;

    protected abstract boolean isAggregated(I i) throws Exception;

    protected abstract boolean isContentLengthInvalid(S s, int i) throws Exception;

    protected abstract boolean isContentMessage(I i) throws Exception;

    protected abstract boolean isLastContentMessage(C c) throws Exception;

    protected abstract boolean isStartMessage(I i) throws Exception;

    protected abstract Object newContinueResponse(S s, int i, ChannelPipeline channelPipeline) throws Exception;

    /* JADX INFO: Access modifiers changed from: protected */
    public MessageAggregator(int maxContentLength) {
        this.maxCumulationBufferComponents = 1024;
        this.handleIncompleteAggregateDuringClose = true;
        validateMaxContentLength(maxContentLength);
        this.maxContentLength = maxContentLength;
    }

    protected MessageAggregator(int maxContentLength, Class<? extends I> inboundMessageType) {
        super(inboundMessageType);
        this.maxCumulationBufferComponents = 1024;
        this.handleIncompleteAggregateDuringClose = true;
        validateMaxContentLength(maxContentLength);
        this.maxContentLength = maxContentLength;
    }

    private static void validateMaxContentLength(int maxContentLength) {
        ObjectUtil.checkPositiveOrZero(maxContentLength, "maxContentLength");
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.handler.codec.MessageToMessageDecoder
    public boolean acceptInboundMessage(Object obj) throws Exception {
        if (!super.acceptInboundMessage(obj) || isAggregated(obj)) {
            return false;
        }
        if (isStartMessage(obj)) {
            return true;
        }
        return this.aggregating && isContentMessage(obj);
    }

    public final int maxContentLength() {
        return this.maxContentLength;
    }

    public final int maxCumulationBufferComponents() {
        return this.maxCumulationBufferComponents;
    }

    public final void setMaxCumulationBufferComponents(int maxCumulationBufferComponents) {
        if (maxCumulationBufferComponents < 2) {
            throw new IllegalArgumentException("maxCumulationBufferComponents: " + maxCumulationBufferComponents + " (expected: >= 2)");
        }
        if (this.ctx == null) {
            this.maxCumulationBufferComponents = maxCumulationBufferComponents;
            return;
        }
        throw new IllegalStateException("decoder properties cannot be changed once the decoder is added to a pipeline.");
    }

    @Deprecated
    public final boolean isHandlingOversizedMessage() {
        return this.handlingOversizedMessage;
    }

    protected final ChannelHandlerContext ctx() {
        if (this.ctx == null) {
            throw new IllegalStateException("not added to a pipeline yet");
        }
        return this.ctx;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.handler.codec.MessageToMessageDecoder
    public void decode(final ChannelHandlerContext channelHandlerContext, I i, List<Object> list) throws Exception {
        boolean isLastContentMessage;
        if (isStartMessage(i)) {
            this.aggregating = true;
            this.handlingOversizedMessage = false;
            if (this.currentMessage != null) {
                this.currentMessage.release();
                this.currentMessage = null;
                throw new MessageAggregationException();
            }
            Object newContinueResponse = newContinueResponse(i, this.maxContentLength, channelHandlerContext.pipeline());
            if (newContinueResponse == null) {
                if (isContentLengthInvalid(i, this.maxContentLength)) {
                    invokeHandleOversizedMessage(channelHandlerContext, i);
                    return;
                }
            } else {
                ChannelFutureListener channelFutureListener = this.continueResponseWriteListener;
                if (channelFutureListener == null) {
                    ChannelFutureListener channelFutureListener2 = new ChannelFutureListener() { // from class: io.netty.handler.codec.MessageAggregator.1
                        @Override // io.netty.util.concurrent.GenericFutureListener
                        public void operationComplete(ChannelFuture future) throws Exception {
                            if (!future.isSuccess()) {
                                channelHandlerContext.fireExceptionCaught(future.cause());
                            }
                        }
                    };
                    channelFutureListener = channelFutureListener2;
                    this.continueResponseWriteListener = channelFutureListener2;
                }
                boolean closeAfterContinueResponse = closeAfterContinueResponse(newContinueResponse);
                this.handlingOversizedMessage = ignoreContentAfterContinueResponse(newContinueResponse);
                Future<Void> addListener = channelHandlerContext.writeAndFlush(newContinueResponse).addListener((GenericFutureListener<? extends Future<? super Void>>) channelFutureListener);
                if (closeAfterContinueResponse) {
                    this.handleIncompleteAggregateDuringClose = false;
                    addListener.addListener((GenericFutureListener<? extends Future<? super Void>>) ChannelFutureListener.CLOSE);
                    return;
                } else if (this.handlingOversizedMessage) {
                    return;
                }
            }
            if ((i instanceof DecoderResultProvider) && !((DecoderResultProvider) i).decoderResult().isSuccess()) {
                ByteBufHolder beginAggregation = i instanceof ByteBufHolder ? beginAggregation(i, ((ByteBufHolder) i).content().retain()) : beginAggregation(i, Unpooled.EMPTY_BUFFER);
                finishAggregation0(beginAggregation);
                list.add(beginAggregation);
                return;
            } else {
                CompositeByteBuf compositeBuffer = channelHandlerContext.alloc().compositeBuffer(this.maxCumulationBufferComponents);
                if (i instanceof ByteBufHolder) {
                    appendPartialContent(compositeBuffer, ((ByteBufHolder) i).content());
                }
                this.currentMessage = (O) beginAggregation(i, compositeBuffer);
                return;
            }
        }
        if (isContentMessage(i)) {
            if (this.currentMessage == null) {
                return;
            }
            CompositeByteBuf compositeByteBuf = (CompositeByteBuf) this.currentMessage.content();
            ByteBufHolder byteBufHolder = (ByteBufHolder) i;
            if (compositeByteBuf.readableBytes() > this.maxContentLength - byteBufHolder.content().readableBytes()) {
                invokeHandleOversizedMessage(channelHandlerContext, this.currentMessage);
                return;
            }
            appendPartialContent(compositeByteBuf, byteBufHolder.content());
            aggregate(this.currentMessage, byteBufHolder);
            if (byteBufHolder instanceof DecoderResultProvider) {
                DecoderResult decoderResult = ((DecoderResultProvider) byteBufHolder).decoderResult();
                if (!decoderResult.isSuccess()) {
                    if (this.currentMessage instanceof DecoderResultProvider) {
                        ((DecoderResultProvider) this.currentMessage).setDecoderResult(DecoderResult.failure(decoderResult.cause()));
                    }
                    isLastContentMessage = true;
                } else {
                    isLastContentMessage = isLastContentMessage(byteBufHolder);
                }
            } else {
                isLastContentMessage = isLastContentMessage(byteBufHolder);
            }
            if (isLastContentMessage) {
                finishAggregation0(this.currentMessage);
                list.add(this.currentMessage);
                this.currentMessage = null;
                return;
            }
            return;
        }
        throw new MessageAggregationException();
    }

    private static void appendPartialContent(CompositeByteBuf content, ByteBuf partialContent) {
        if (partialContent.isReadable()) {
            content.addComponent(true, partialContent.retain());
        }
    }

    protected void aggregate(O aggregated, C content) throws Exception {
    }

    private void finishAggregation0(O aggregated) throws Exception {
        this.aggregating = false;
        finishAggregation(aggregated);
    }

    protected void finishAggregation(O aggregated) throws Exception {
    }

    private void invokeHandleOversizedMessage(ChannelHandlerContext ctx, S oversized) throws Exception {
        this.handlingOversizedMessage = true;
        this.currentMessage = null;
        this.handleIncompleteAggregateDuringClose = false;
        try {
            handleOversizedMessage(ctx, oversized);
        } finally {
            ReferenceCountUtil.release(oversized);
        }
    }

    protected void handleOversizedMessage(ChannelHandlerContext ctx, S oversized) throws Exception {
        ctx.fireExceptionCaught((Throwable) new TooLongFrameException("content length exceeded " + maxContentLength() + " bytes."));
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        if (this.currentMessage != null && !ctx.channel().config().isAutoRead()) {
            ctx.read();
        }
        ctx.fireChannelReadComplete();
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (this.aggregating && this.handleIncompleteAggregateDuringClose) {
            ctx.fireExceptionCaught((Throwable) new PrematureChannelClosureException("Channel closed while still aggregating message"));
        }
        try {
            super.channelInactive(ctx);
        } finally {
            releaseCurrentMessage();
        }
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        try {
            super.handlerRemoved(ctx);
        } finally {
            releaseCurrentMessage();
        }
    }

    private void releaseCurrentMessage() {
        if (this.currentMessage != null) {
            this.currentMessage.release();
            this.currentMessage = null;
            this.handlingOversizedMessage = false;
            this.aggregating = false;
        }
    }
}
