package io.netty.handler.codec.memcache.binary;

import io.netty.buffer.ByteBuf;

/* loaded from: classes4.dex */
public class DefaultBinaryMemcacheResponse extends AbstractBinaryMemcacheMessage implements BinaryMemcacheResponse {
    public static final byte RESPONSE_MAGIC_BYTE = -127;
    private short status;

    public DefaultBinaryMemcacheResponse() {
        this(null, null);
    }

    public DefaultBinaryMemcacheResponse(ByteBuf key) {
        this(key, null);
    }

    public DefaultBinaryMemcacheResponse(ByteBuf key, ByteBuf extras) {
        super(key, extras);
        setMagic(RESPONSE_MAGIC_BYTE);
    }

    @Override // io.netty.handler.codec.memcache.binary.BinaryMemcacheResponse
    public short status() {
        return this.status;
    }

    @Override // io.netty.handler.codec.memcache.binary.BinaryMemcacheResponse
    public BinaryMemcacheResponse setStatus(short status) {
        this.status = status;
        return this;
    }

    @Override // io.netty.handler.codec.memcache.binary.AbstractBinaryMemcacheMessage, io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public BinaryMemcacheResponse retain() {
        super.retain();
        return this;
    }

    @Override // io.netty.handler.codec.memcache.binary.AbstractBinaryMemcacheMessage, io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public BinaryMemcacheResponse retain(int increment) {
        super.retain(increment);
        return this;
    }

    @Override // io.netty.handler.codec.memcache.binary.AbstractBinaryMemcacheMessage, io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public BinaryMemcacheResponse touch() {
        super.touch();
        return this;
    }

    @Override // io.netty.handler.codec.memcache.binary.AbstractBinaryMemcacheMessage, io.netty.util.ReferenceCounted
    public BinaryMemcacheResponse touch(Object hint) {
        super.touch(hint);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void copyMeta(DefaultBinaryMemcacheResponse dst) {
        super.copyMeta((AbstractBinaryMemcacheMessage) dst);
        dst.status = this.status;
    }
}
