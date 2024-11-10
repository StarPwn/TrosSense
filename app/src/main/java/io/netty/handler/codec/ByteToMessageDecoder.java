package io.netty.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.ChannelInputShutdownEvent;
import io.netty.util.IllegalReferenceCountException;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;
import java.util.List;

/* loaded from: classes4.dex */
public abstract class ByteToMessageDecoder extends ChannelInboundHandlerAdapter {
    private static final byte STATE_CALLING_CHILD_DECODE = 1;
    private static final byte STATE_HANDLER_REMOVED_PENDING = 2;
    private static final byte STATE_INIT = 0;
    ByteBuf cumulation;
    private Cumulator cumulator = MERGE_CUMULATOR;
    private byte decodeState = 0;
    private int discardAfterReads = 16;
    private boolean firedChannelRead;
    private boolean first;
    private int numReads;
    private boolean selfFiredChannelRead;
    private boolean singleDecode;
    public static final Cumulator MERGE_CUMULATOR = new Cumulator() { // from class: io.netty.handler.codec.ByteToMessageDecoder.1
        @Override // io.netty.handler.codec.ByteToMessageDecoder.Cumulator
        public ByteBuf cumulate(ByteBufAllocator alloc, ByteBuf cumulation, ByteBuf in) {
            if (cumulation == in) {
                return cumulation;
            }
            if (!cumulation.isReadable() && in.isContiguous()) {
                cumulation.release();
                return in;
            }
            try {
                int required = in.readableBytes();
                if (required <= cumulation.maxWritableBytes() && ((required <= cumulation.maxFastWritableBytes() || cumulation.refCnt() <= 1) && !cumulation.isReadOnly())) {
                    cumulation.writeBytes(in, in.readerIndex(), required);
                    in.readerIndex(in.writerIndex());
                    return cumulation;
                }
                return ByteToMessageDecoder.expandCumulation(alloc, cumulation, in);
            } finally {
                in.release();
            }
        }
    };
    public static final Cumulator COMPOSITE_CUMULATOR = new Cumulator() { // from class: io.netty.handler.codec.ByteToMessageDecoder.2
        @Override // io.netty.handler.codec.ByteToMessageDecoder.Cumulator
        public ByteBuf cumulate(ByteBufAllocator alloc, ByteBuf cumulation, ByteBuf in) {
            if (cumulation == in) {
                in.release();
                return cumulation;
            }
            if (!cumulation.isReadable()) {
                cumulation.release();
                return in;
            }
            CompositeByteBuf composite = null;
            try {
                if ((cumulation instanceof CompositeByteBuf) && cumulation.refCnt() == 1) {
                    composite = (CompositeByteBuf) cumulation;
                    if (composite.writerIndex() != composite.capacity()) {
                        composite.capacity(composite.writerIndex());
                    }
                } else {
                    composite = alloc.compositeBuffer(Integer.MAX_VALUE).addFlattenedComponents(true, cumulation);
                }
                composite.addFlattenedComponents(true, in);
                in = null;
                return composite;
            } finally {
                if (in != null) {
                    in.release();
                    if (composite != null && composite != cumulation) {
                        composite.release();
                    }
                }
            }
        }
    };

    /* loaded from: classes4.dex */
    public interface Cumulator {
        ByteBuf cumulate(ByteBufAllocator byteBufAllocator, ByteBuf byteBuf, ByteBuf byteBuf2);
    }

    protected abstract void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception;

    /* JADX INFO: Access modifiers changed from: protected */
    public ByteToMessageDecoder() {
        ensureNotSharable();
    }

    public void setSingleDecode(boolean singleDecode) {
        this.singleDecode = singleDecode;
    }

    public boolean isSingleDecode() {
        return this.singleDecode;
    }

    public void setCumulator(Cumulator cumulator) {
        this.cumulator = (Cumulator) ObjectUtil.checkNotNull(cumulator, "cumulator");
    }

    public void setDiscardAfterReads(int discardAfterReads) {
        ObjectUtil.checkPositive(discardAfterReads, "discardAfterReads");
        this.discardAfterReads = discardAfterReads;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int actualReadableBytes() {
        return internalBuffer().readableBytes();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ByteBuf internalBuffer() {
        if (this.cumulation != null) {
            return this.cumulation;
        }
        return Unpooled.EMPTY_BUFFER;
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public final void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        if (this.decodeState == 1) {
            this.decodeState = (byte) 2;
            return;
        }
        ByteBuf buf = this.cumulation;
        if (buf != null) {
            this.cumulation = null;
            this.numReads = 0;
            int readable = buf.readableBytes();
            if (readable > 0) {
                ctx.fireChannelRead((Object) buf);
                ctx.fireChannelReadComplete();
            } else {
                buf.release();
            }
        }
        handlerRemoved0(ctx);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void handlerRemoved0(ChannelHandlerContext ctx) throws Exception {
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof ByteBuf)) {
            ctx.fireChannelRead(msg);
            return;
        }
        this.selfFiredChannelRead = true;
        CodecOutputList out = CodecOutputList.newInstance();
        try {
            try {
                this.first = this.cumulation == null;
                this.cumulation = this.cumulator.cumulate(ctx.alloc(), this.first ? Unpooled.EMPTY_BUFFER : this.cumulation, (ByteBuf) msg);
                callDecode(ctx, this.cumulation, out);
                try {
                    if (this.cumulation == null || this.cumulation.isReadable()) {
                        int i = this.numReads + 1;
                        this.numReads = i;
                        if (i >= this.discardAfterReads) {
                            this.numReads = 0;
                            discardSomeReadBytes();
                        }
                    } else {
                        this.numReads = 0;
                        try {
                            this.cumulation.release();
                            this.cumulation = null;
                        } catch (IllegalReferenceCountException e) {
                            throw new IllegalReferenceCountException(getClass().getSimpleName() + "#decode() might have released its input buffer, or passed it down the pipeline without a retain() call, which is not allowed.", e);
                        }
                    }
                    int size = out.size();
                    this.firedChannelRead |= out.insertSinceRecycled();
                    fireChannelRead(ctx, out, size);
                } finally {
                }
            } catch (DecoderException e2) {
                throw e2;
            } catch (Exception e3) {
                throw new DecoderException(e3);
            }
        } catch (Throwable th) {
            try {
                if (this.cumulation != null && !this.cumulation.isReadable()) {
                    this.numReads = 0;
                    try {
                        this.cumulation.release();
                        this.cumulation = null;
                        int size2 = out.size();
                        this.firedChannelRead |= out.insertSinceRecycled();
                        fireChannelRead(ctx, out, size2);
                        throw th;
                    } catch (IllegalReferenceCountException e4) {
                        throw new IllegalReferenceCountException(getClass().getSimpleName() + "#decode() might have released its input buffer, or passed it down the pipeline without a retain() call, which is not allowed.", e4);
                    }
                }
                int i2 = this.numReads + 1;
                this.numReads = i2;
                if (i2 >= this.discardAfterReads) {
                    this.numReads = 0;
                    discardSomeReadBytes();
                }
                int size22 = out.size();
                this.firedChannelRead |= out.insertSinceRecycled();
                fireChannelRead(ctx, out, size22);
                throw th;
            } finally {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void fireChannelRead(ChannelHandlerContext ctx, List<Object> msgs, int numElements) {
        if (msgs instanceof CodecOutputList) {
            fireChannelRead(ctx, (CodecOutputList) msgs, numElements);
            return;
        }
        for (int i = 0; i < numElements; i++) {
            ctx.fireChannelRead(msgs.get(i));
        }
    }

    static void fireChannelRead(ChannelHandlerContext ctx, CodecOutputList msgs, int numElements) {
        for (int i = 0; i < numElements; i++) {
            ctx.fireChannelRead(msgs.getUnsafe(i));
        }
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        this.numReads = 0;
        discardSomeReadBytes();
        if (this.selfFiredChannelRead && !this.firedChannelRead && !ctx.channel().config().isAutoRead()) {
            ctx.read();
        }
        this.firedChannelRead = false;
        this.selfFiredChannelRead = false;
        ctx.fireChannelReadComplete();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void discardSomeReadBytes() {
        if (this.cumulation != null && !this.first && this.cumulation.refCnt() == 1) {
            this.cumulation.discardSomeReadBytes();
        }
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        channelInputClosed(ctx, true);
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof ChannelInputShutdownEvent) {
            channelInputClosed(ctx, false);
        }
        super.userEventTriggered(ctx, evt);
    }

    private void channelInputClosed(ChannelHandlerContext ctx, boolean callChannelInactive) {
        CodecOutputList out = CodecOutputList.newInstance();
        try {
            try {
                channelInputClosed(ctx, out);
                try {
                    if (this.cumulation != null) {
                        this.cumulation.release();
                        this.cumulation = null;
                    }
                    int size = out.size();
                    fireChannelRead(ctx, out, size);
                    if (size > 0) {
                        ctx.fireChannelReadComplete();
                    }
                    if (callChannelInactive) {
                        ctx.fireChannelInactive();
                    }
                } finally {
                }
            } catch (DecoderException e) {
                throw e;
            } catch (Exception e2) {
                throw new DecoderException(e2);
            }
        } catch (Throwable th) {
            try {
                if (this.cumulation != null) {
                    this.cumulation.release();
                    this.cumulation = null;
                }
                int size2 = out.size();
                fireChannelRead(ctx, out, size2);
                if (size2 > 0) {
                    ctx.fireChannelReadComplete();
                }
                if (callChannelInactive) {
                    ctx.fireChannelInactive();
                }
                throw th;
            } finally {
            }
        }
    }

    void channelInputClosed(ChannelHandlerContext ctx, List<Object> out) throws Exception {
        if (this.cumulation != null) {
            callDecode(ctx, this.cumulation, out);
            if (!ctx.isRemoved()) {
                ByteBuf buffer = this.cumulation == null ? Unpooled.EMPTY_BUFFER : this.cumulation;
                decodeLast(ctx, buffer, out);
                return;
            }
            return;
        }
        decodeLast(ctx, Unpooled.EMPTY_BUFFER, out);
    }

    protected void callDecode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        while (in.isReadable()) {
            try {
                int outSize = out.size();
                if (outSize > 0) {
                    fireChannelRead(ctx, out, outSize);
                    out.clear();
                    if (ctx.isRemoved()) {
                        return;
                    }
                }
                int oldInputLength = in.readableBytes();
                decodeRemovalReentryProtection(ctx, in, out);
                if (!ctx.isRemoved()) {
                    if (out.isEmpty()) {
                        if (oldInputLength == in.readableBytes()) {
                            return;
                        }
                    } else {
                        if (oldInputLength == in.readableBytes()) {
                            throw new DecoderException(StringUtil.simpleClassName(getClass()) + ".decode() did not read anything but decoded a message.");
                        }
                        if (isSingleDecode()) {
                            return;
                        }
                    }
                } else {
                    return;
                }
            } catch (DecoderException e) {
                throw e;
            } catch (Exception cause) {
                throw new DecoderException(cause);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void decodeRemovalReentryProtection(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        this.decodeState = (byte) 1;
        try {
            decode(ctx, in, out);
        } finally {
            r0 = this.decodeState != 2 ? (byte) 0 : (byte) 1;
            this.decodeState = (byte) 0;
            if (r0 != 0) {
                fireChannelRead(ctx, out, out.size());
                out.clear();
                handlerRemoved(ctx);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void decodeLast(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.isReadable()) {
            decodeRemovalReentryProtection(ctx, in, out);
        }
    }

    static ByteBuf expandCumulation(ByteBufAllocator alloc, ByteBuf oldCumulation, ByteBuf in) {
        int oldBytes = oldCumulation.readableBytes();
        int newBytes = in.readableBytes();
        int totalBytes = oldBytes + newBytes;
        ByteBuf newCumulation = alloc.buffer(alloc.calculateNewCapacity(totalBytes, Integer.MAX_VALUE));
        try {
            newCumulation.setBytes(0, oldCumulation, oldCumulation.readerIndex(), oldBytes).setBytes(oldBytes, in, in.readerIndex(), newBytes).writerIndex(totalBytes);
            in.readerIndex(in.writerIndex());
            oldCumulation.release();
            return newCumulation;
        } catch (Throwable th) {
            newCumulation.release();
            throw th;
        }
    }
}
