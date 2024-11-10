package io.netty.handler.codec.memcache.binary;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.memcache.AbstractMemcacheObjectEncoder;
import io.netty.handler.codec.memcache.binary.BinaryMemcacheMessage;

/* loaded from: classes4.dex */
public abstract class AbstractBinaryMemcacheEncoder<M extends BinaryMemcacheMessage> extends AbstractMemcacheObjectEncoder<M> {
    private static final int MINIMUM_HEADER_SIZE = 24;

    protected abstract void encodeHeader(ByteBuf byteBuf, M m);

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.memcache.AbstractMemcacheObjectEncoder
    public ByteBuf encodeMessage(ChannelHandlerContext ctx, M msg) {
        ByteBuf buf = ctx.alloc().buffer(msg.extrasLength() + 24 + msg.keyLength());
        encodeHeader(buf, msg);
        encodeExtras(buf, msg.extras());
        encodeKey(buf, msg.key());
        return buf;
    }

    private static void encodeExtras(ByteBuf buf, ByteBuf extras) {
        if (extras == null || !extras.isReadable()) {
            return;
        }
        buf.writeBytes(extras);
    }

    private static void encodeKey(ByteBuf buf, ByteBuf key) {
        if (key == null || !key.isReadable()) {
            return;
        }
        buf.writeBytes(key);
    }
}
