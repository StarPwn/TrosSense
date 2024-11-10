package io.netty.handler.codec.memcache.binary;

import io.netty.buffer.ByteBuf;

/* loaded from: classes4.dex */
public class BinaryMemcacheRequestEncoder extends AbstractBinaryMemcacheEncoder<BinaryMemcacheRequest> {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.memcache.binary.AbstractBinaryMemcacheEncoder
    public void encodeHeader(ByteBuf buf, BinaryMemcacheRequest msg) {
        buf.writeByte(msg.magic());
        buf.writeByte(msg.opcode());
        buf.writeShort(msg.keyLength());
        buf.writeByte(msg.extrasLength());
        buf.writeByte(msg.dataType());
        buf.writeShort(msg.reserved());
        buf.writeInt(msg.totalBodyLength());
        buf.writeInt(msg.opaque());
        buf.writeLong(msg.cas());
    }
}
