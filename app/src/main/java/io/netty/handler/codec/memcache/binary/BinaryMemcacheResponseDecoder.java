package io.netty.handler.codec.memcache.binary;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/* loaded from: classes4.dex */
public class BinaryMemcacheResponseDecoder extends AbstractBinaryMemcacheDecoder<BinaryMemcacheResponse> {
    public BinaryMemcacheResponseDecoder() {
        this(8192);
    }

    public BinaryMemcacheResponseDecoder(int chunkSize) {
        super(chunkSize);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.memcache.binary.AbstractBinaryMemcacheDecoder
    public BinaryMemcacheResponse decodeHeader(ByteBuf in) {
        DefaultBinaryMemcacheResponse header = new DefaultBinaryMemcacheResponse();
        header.setMagic(in.readByte());
        header.setOpcode(in.readByte());
        header.setKeyLength(in.readShort());
        header.setExtrasLength(in.readByte());
        header.setDataType(in.readByte());
        header.setStatus(in.readShort());
        header.setTotalBodyLength(in.readInt());
        header.setOpaque(in.readInt());
        header.setCas(in.readLong());
        return header;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.memcache.binary.AbstractBinaryMemcacheDecoder
    public BinaryMemcacheResponse buildInvalidMessage() {
        return new DefaultBinaryMemcacheResponse(Unpooled.EMPTY_BUFFER, Unpooled.EMPTY_BUFFER);
    }
}
