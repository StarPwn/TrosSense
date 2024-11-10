package io.netty.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.AddressedEnvelope;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;

/* loaded from: classes4.dex */
public class DatagramPacketEncoder<M> extends MessageToMessageEncoder<AddressedEnvelope<M, InetSocketAddress>> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final MessageToMessageEncoder<? super M> encoder;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageToMessageEncoder
    public /* bridge */ /* synthetic */ void encode(ChannelHandlerContext channelHandlerContext, Object obj, List list) throws Exception {
        encode(channelHandlerContext, (AddressedEnvelope) obj, (List<Object>) list);
    }

    public DatagramPacketEncoder(MessageToMessageEncoder<? super M> encoder) {
        this.encoder = (MessageToMessageEncoder) ObjectUtil.checkNotNull(encoder, "encoder");
    }

    @Override // io.netty.handler.codec.MessageToMessageEncoder
    public boolean acceptOutboundMessage(Object msg) throws Exception {
        if (!super.acceptOutboundMessage(msg)) {
            return false;
        }
        AddressedEnvelope envelope = (AddressedEnvelope) msg;
        if (this.encoder.acceptOutboundMessage(envelope.content())) {
            return ((envelope.sender() instanceof InetSocketAddress) || envelope.sender() == null) && (envelope.recipient() instanceof InetSocketAddress);
        }
        return false;
    }

    protected void encode(ChannelHandlerContext ctx, AddressedEnvelope<M, InetSocketAddress> msg, List<Object> out) throws Exception {
        if (!out.isEmpty()) {
            throw new AssertionError();
        }
        this.encoder.encode(ctx, msg.content(), out);
        if (out.size() != 1) {
            throw new EncoderException(StringUtil.simpleClassName(this.encoder) + " must produce only one message.");
        }
        Object content = out.get(0);
        if (content instanceof ByteBuf) {
            out.set(0, new DatagramPacket((ByteBuf) content, msg.recipient(), msg.sender()));
            return;
        }
        throw new EncoderException(StringUtil.simpleClassName(this.encoder) + " must produce only ByteBuf.");
    }

    @Override // io.netty.channel.ChannelOutboundHandlerAdapter, io.netty.channel.ChannelOutboundHandler
    public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        this.encoder.bind(ctx, localAddress, promise);
    }

    @Override // io.netty.channel.ChannelOutboundHandlerAdapter, io.netty.channel.ChannelOutboundHandler
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        this.encoder.connect(ctx, remoteAddress, localAddress, promise);
    }

    @Override // io.netty.channel.ChannelOutboundHandlerAdapter, io.netty.channel.ChannelOutboundHandler
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        this.encoder.disconnect(ctx, promise);
    }

    @Override // io.netty.channel.ChannelOutboundHandlerAdapter, io.netty.channel.ChannelOutboundHandler
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        this.encoder.close(ctx, promise);
    }

    @Override // io.netty.channel.ChannelOutboundHandlerAdapter, io.netty.channel.ChannelOutboundHandler
    public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        this.encoder.deregister(ctx, promise);
    }

    @Override // io.netty.channel.ChannelOutboundHandlerAdapter, io.netty.channel.ChannelOutboundHandler
    public void read(ChannelHandlerContext ctx) throws Exception {
        this.encoder.read(ctx);
    }

    @Override // io.netty.channel.ChannelOutboundHandlerAdapter, io.netty.channel.ChannelOutboundHandler
    public void flush(ChannelHandlerContext ctx) throws Exception {
        this.encoder.flush(ctx);
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        this.encoder.handlerAdded(ctx);
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        this.encoder.handlerRemoved(ctx);
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler, io.netty.channel.ChannelInboundHandler
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        this.encoder.exceptionCaught(ctx, cause);
    }

    @Override // io.netty.channel.ChannelHandlerAdapter
    public boolean isSharable() {
        return this.encoder.isSharable();
    }
}
