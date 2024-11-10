package io.netty.handler.codec.marshalling;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.handler.codec.marshalling.LimitingByteInput;
import java.util.List;
import org.jboss.marshalling.ByteInput;
import org.jboss.marshalling.Unmarshaller;

/* loaded from: classes4.dex */
public class CompatibleMarshallingDecoder extends ReplayingDecoder<Void> {
    private boolean discardingTooLongFrame;
    protected final int maxObjectSize;
    protected final UnmarshallerProvider provider;

    public CompatibleMarshallingDecoder(UnmarshallerProvider provider, int maxObjectSize) {
        this.provider = provider;
        this.maxObjectSize = maxObjectSize;
    }

    @Override // io.netty.handler.codec.ByteToMessageDecoder
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
        if (this.discardingTooLongFrame) {
            buffer.skipBytes(actualReadableBytes());
            checkpoint();
            return;
        }
        Unmarshaller unmarshaller = this.provider.getUnmarshaller(ctx);
        ByteInput input = new ChannelBufferByteInput(buffer);
        if (this.maxObjectSize != Integer.MAX_VALUE) {
            input = new LimitingByteInput(input, this.maxObjectSize);
        }
        try {
            try {
                unmarshaller.start(input);
                Object obj = unmarshaller.readObject();
                unmarshaller.finish();
                out.add(obj);
            } catch (LimitingByteInput.TooBigObjectException e) {
                this.discardingTooLongFrame = true;
                throw new TooLongFrameException();
            }
        } finally {
            unmarshaller.close();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.ByteToMessageDecoder
    public void decodeLast(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
        switch (buffer.readableBytes()) {
            case 0:
                return;
            case 1:
                if (buffer.getByte(buffer.readerIndex()) == 121) {
                    buffer.skipBytes(1);
                    return;
                }
                break;
        }
        decode(ctx, buffer, out);
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler, io.netty.channel.ChannelInboundHandler
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof TooLongFrameException) {
            ctx.close();
        } else {
            super.exceptionCaught(ctx, cause);
        }
    }
}
