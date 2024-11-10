package io.netty.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.TypeParameterMatcher;

/* loaded from: classes4.dex */
public abstract class MessageToByteEncoder<I> extends ChannelOutboundHandlerAdapter {
    private final TypeParameterMatcher matcher;
    private final boolean preferDirect;

    protected abstract void encode(ChannelHandlerContext channelHandlerContext, I i, ByteBuf byteBuf) throws Exception;

    /* JADX INFO: Access modifiers changed from: protected */
    public MessageToByteEncoder() {
        this(true);
    }

    protected MessageToByteEncoder(Class<? extends I> outboundMessageType) {
        this(outboundMessageType, true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public MessageToByteEncoder(boolean preferDirect) {
        this.matcher = TypeParameterMatcher.find(this, MessageToByteEncoder.class, "I");
        this.preferDirect = preferDirect;
    }

    protected MessageToByteEncoder(Class<? extends I> outboundMessageType, boolean preferDirect) {
        this.matcher = TypeParameterMatcher.get(outboundMessageType);
        this.preferDirect = preferDirect;
    }

    public boolean acceptOutboundMessage(Object msg) throws Exception {
        return this.matcher.match(msg);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.channel.ChannelOutboundHandlerAdapter, io.netty.channel.ChannelOutboundHandler
    public void write(ChannelHandlerContext ctx, Object obj, ChannelPromise promise) throws Exception {
        ByteBuf buf = null;
        try {
            try {
                if (acceptOutboundMessage(obj)) {
                    ByteBuf buf2 = allocateBuffer(ctx, obj, this.preferDirect);
                    try {
                        encode(ctx, obj, buf2);
                        ReferenceCountUtil.release(obj);
                        if (buf2.isReadable()) {
                            ctx.write(buf2, promise);
                        } else {
                            buf2.release();
                            ctx.write(Unpooled.EMPTY_BUFFER, promise);
                        }
                        buf = null;
                    } catch (Throwable th) {
                        ReferenceCountUtil.release(obj);
                        throw th;
                    }
                } else {
                    ctx.write(obj, promise);
                }
            } catch (EncoderException e) {
                throw e;
            } catch (Throwable e2) {
                throw new EncoderException(e2);
            }
        } finally {
            if (0 != 0) {
                buf.release();
            }
        }
    }

    protected ByteBuf allocateBuffer(ChannelHandlerContext ctx, I msg, boolean preferDirect) throws Exception {
        if (preferDirect) {
            return ctx.alloc().ioBuffer();
        }
        return ctx.alloc().heapBuffer();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isPreferDirect() {
        return this.preferDirect;
    }
}
