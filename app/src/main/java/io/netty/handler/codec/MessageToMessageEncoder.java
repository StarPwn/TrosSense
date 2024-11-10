package io.netty.handler.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.PromiseCombiner;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.TypeParameterMatcher;
import java.util.List;

/* loaded from: classes4.dex */
public abstract class MessageToMessageEncoder<I> extends ChannelOutboundHandlerAdapter {
    private final TypeParameterMatcher matcher;

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void encode(ChannelHandlerContext channelHandlerContext, I i, List<Object> list) throws Exception;

    /* JADX INFO: Access modifiers changed from: protected */
    public MessageToMessageEncoder() {
        this.matcher = TypeParameterMatcher.find(this, MessageToMessageEncoder.class, "I");
    }

    protected MessageToMessageEncoder(Class<? extends I> outboundMessageType) {
        this.matcher = TypeParameterMatcher.get(outboundMessageType);
    }

    public boolean acceptOutboundMessage(Object msg) throws Exception {
        return this.matcher.match(msg);
    }

    @Override // io.netty.channel.ChannelOutboundHandlerAdapter, io.netty.channel.ChannelOutboundHandler
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        CodecOutputList out = null;
        try {
            try {
                try {
                    if (acceptOutboundMessage(msg)) {
                        out = CodecOutputList.newInstance();
                        try {
                            encode(ctx, msg, out);
                        } catch (Throwable th) {
                            ReferenceCountUtil.safeRelease(msg);
                            PlatformDependent.throwException(th);
                        }
                        ReferenceCountUtil.release(msg);
                        if (out.isEmpty()) {
                            throw new EncoderException(StringUtil.simpleClassName(this) + " must produce at least one message.");
                        }
                    } else {
                        ctx.write(msg, promise);
                    }
                    if (out != null) {
                        try {
                            int sizeMinusOne = out.size() - 1;
                            if (sizeMinusOne == 0) {
                                ctx.write(out.getUnsafe(0), promise);
                            } else if (sizeMinusOne > 0) {
                                if (promise == ctx.voidPromise()) {
                                    writeVoidPromise(ctx, out);
                                } else {
                                    writePromiseCombiner(ctx, out, promise);
                                }
                            }
                        } finally {
                        }
                    }
                } catch (Throwable t) {
                    throw new EncoderException(t);
                }
            } catch (EncoderException e) {
                throw e;
            }
        } catch (Throwable e2) {
            if (0 != 0) {
                try {
                    int sizeMinusOne2 = out.size() - 1;
                    if (sizeMinusOne2 == 0) {
                        ctx.write(out.getUnsafe(0), promise);
                    } else if (sizeMinusOne2 > 0) {
                        if (promise == ctx.voidPromise()) {
                            writeVoidPromise(ctx, null);
                        } else {
                            writePromiseCombiner(ctx, null, promise);
                        }
                    }
                } finally {
                }
            }
            throw e2;
        }
    }

    private static void writeVoidPromise(ChannelHandlerContext ctx, CodecOutputList out) {
        ChannelPromise voidPromise = ctx.voidPromise();
        for (int i = 0; i < out.size(); i++) {
            ctx.write(out.getUnsafe(i), voidPromise);
        }
    }

    private static void writePromiseCombiner(ChannelHandlerContext ctx, CodecOutputList out, ChannelPromise promise) {
        PromiseCombiner combiner = new PromiseCombiner(ctx.executor());
        for (int i = 0; i < out.size(); i++) {
            combiner.add(ctx.write(out.getUnsafe(i)));
        }
        combiner.finish(promise);
    }
}
