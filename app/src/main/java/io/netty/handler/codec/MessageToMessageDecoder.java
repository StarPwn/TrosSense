package io.netty.handler.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.TypeParameterMatcher;
import java.util.List;

/* loaded from: classes4.dex */
public abstract class MessageToMessageDecoder<I> extends ChannelInboundHandlerAdapter {
    private final TypeParameterMatcher matcher;

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void decode(ChannelHandlerContext channelHandlerContext, I i, List<Object> list) throws Exception;

    /* JADX INFO: Access modifiers changed from: protected */
    public MessageToMessageDecoder() {
        this.matcher = TypeParameterMatcher.find(this, MessageToMessageDecoder.class, "I");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public MessageToMessageDecoder(Class<? extends I> inboundMessageType) {
        this.matcher = TypeParameterMatcher.get(inboundMessageType);
    }

    public boolean acceptInboundMessage(Object msg) throws Exception {
        return this.matcher.match(msg);
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        CodecOutputList out = CodecOutputList.newInstance();
        try {
            try {
                if (acceptInboundMessage(msg)) {
                    try {
                        decode(ctx, msg, out);
                        ReferenceCountUtil.release(msg);
                    } catch (Throwable th) {
                        ReferenceCountUtil.release(msg);
                        throw th;
                    }
                } else {
                    out.add(msg);
                }
                try {
                    int size = out.size();
                    for (int i = 0; i < size; i++) {
                        ctx.fireChannelRead(out.getUnsafe(i));
                    }
                } finally {
                }
            } catch (DecoderException e) {
                throw e;
            } catch (Exception e2) {
                throw new DecoderException(e2);
            }
        } catch (Throwable th2) {
            try {
                int size2 = out.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    ctx.fireChannelRead(out.getUnsafe(i2));
                }
                throw th2;
            } finally {
            }
        }
    }
}
