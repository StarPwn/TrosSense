package io.netty.handler.codec.memcache.binary;

import io.netty.buffer.ByteBuf;

/* loaded from: classes4.dex */
public class BinaryMemcacheResponseEncoder extends AbstractBinaryMemcacheEncoder<BinaryMemcacheResponse> {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.memcache.binary.AbstractBinaryMemcacheEncoder
    public void encodeHeader(ByteBuf buf, BinaryMemcacheResponse msg) {
        buf.writeByte(msg.magic());
        buf.writeByte(msg.opcode());
        buf.writeShort(msg.keyLength());
        buf.writeByte(msg.extrasLength());
        buf.writeByte(msg.dataType());
        buf.writeShort(msg.status());
        buf.writeInt(msg.totalBodyLength());
        buf.writeInt(msg.opaque());
        buf.writeLong(msg.cas());
    }
}
