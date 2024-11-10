package io.netty.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.internal.TypeParameterMatcher;
import java.util.List;

/* loaded from: classes4.dex */
public abstract class ByteToMessageCodec<I> extends ChannelDuplexHandler {
    private final ByteToMessageDecoder decoder;
    private final MessageToByteEncoder<I> encoder;
    private final TypeParameterMatcher outboundMsgMatcher;

    protected abstract void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception;

    protected abstract void encode(ChannelHandlerContext channelHandlerContext, I i, ByteBuf byteBuf) throws Exception;

    protected ByteToMessageCodec() {
        this(true);
    }

    protected ByteToMessageCodec(Class<? extends I> outboundMessageType) {
        this(outboundMessageType, true);
    }

    protected ByteToMessageCodec(boolean preferDirect) {
        this.decoder = new ByteToMessageDecoder() { // from class: io.netty.handler.codec.ByteToMessageCodec.1
            @Override // io.netty.handler.codec.ByteToMessageDecoder
            public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
                ByteToMessageCodec.this.decode(ctx, in, out);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // io.netty.handler.codec.ByteToMessageDecoder
            public void decodeLast(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
                ByteToMessageCodec.this.decodeLast(ctx, in, out);
            }
        };
        ensureNotSharable();
        this.outboundMsgMatcher = TypeParameterMatcher.find(this, ByteToMessageCodec.class, "I");
        this.encoder = new Encoder(preferDirect);
    }

    protected ByteToMessageCodec(Class<? extends I> outboundMessageType, boolean preferDirect) {
        this.decoder = new ByteToMessageDecoder() { // from class: io.netty.handler.codec.ByteToMessageCodec.1
            @Override // io.netty.handler.codec.ByteToMessageDecoder
            public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
                ByteToMessageCodec.this.decode(ctx, in, out);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // io.netty.handler.codec.ByteToMessageDecoder
            public void decodeLast(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
                ByteToMessageCodec.this.decodeLast(ctx, in, out);
            }
        };
        ensureNotSharable();
        this.outboundMsgMatcher = TypeParameterMatcher.get(outboundMessageType);
        this.encoder = new Encoder(preferDirect);
    }

    public boolean acceptOutboundMessage(Object msg) throws Exception {
        return this.outboundMsgMatcher.match(msg);
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.decoder.channelRead(ctx, msg);
    }

    @Override // io.netty.channel.ChannelDuplexHandler, io.netty.channel.ChannelOutboundHandler
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        this.encoder.write(ctx, msg, promise);
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        this.decoder.channelReadComplete(ctx);
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        this.decoder.channelInactive(ctx);
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        try {
            this.decoder.handlerAdded(ctx);
        } finally {
            this.encoder.handlerAdded(ctx);
        }
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        try {
            this.decoder.handlerRemoved(ctx);
        } finally {
            this.encoder.handlerRemoved(ctx);
        }
    }

    protected void decodeLast(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.isReadable()) {
            decode(ctx, in, out);
        }
    }

    /* loaded from: classes4.dex */
    private final class Encoder extends MessageToByteEncoder<I> {
        Encoder(boolean preferDirect) {
            super(preferDirect);
        }

        @Override // io.netty.handler.codec.MessageToByteEncoder
        public boolean acceptOutboundMessage(Object msg) throws Exception {
            return ByteToMessageCodec.this.acceptOutboundMessage(msg);
        }

        @Override // io.netty.handler.codec.MessageToByteEncoder
        protected void encode(ChannelHandlerContext ctx, I msg, ByteBuf out) throws Exception {
            ByteToMessageCodec.this.encode(ctx, msg, out);
        }
    }
}
