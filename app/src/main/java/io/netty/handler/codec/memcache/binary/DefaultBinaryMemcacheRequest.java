package io.netty.handler.codec.memcache.binary;

import io.netty.buffer.ByteBuf;

/* loaded from: classes4.dex */
public class DefaultBinaryMemcacheRequest extends AbstractBinaryMemcacheMessage implements BinaryMemcacheRequest {
    public static final byte REQUEST_MAGIC_BYTE = Byte.MIN_VALUE;
    private short reserved;

    public DefaultBinaryMemcacheRequest() {
        this(null, null);
    }

    public DefaultBinaryMemcacheRequest(ByteBuf key) {
        this(key, null);
    }

    public DefaultBinaryMemcacheRequest(ByteBuf key, ByteBuf extras) {
        super(key, extras);
        setMagic(Byte.MIN_VALUE);
    }

    @Override // io.netty.handler.codec.memcache.binary.BinaryMemcacheRequest
    public short reserved() {
        return this.reserved;
    }

    @Override // io.netty.handler.codec.memcache.binary.BinaryMemcacheRequest
    public BinaryMemcacheRequest setReserved(short reserved) {
        this.reserved = reserved;
        return this;
    }

    @Override // io.netty.handler.codec.memcache.binary.AbstractBinaryMemcacheMessage, io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public BinaryMemcacheRequest retain() {
        super.retain();
        return this;
    }

    @Override // io.netty.handler.codec.memcache.binary.AbstractBinaryMemcacheMessage, io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public BinaryMemcacheRequest retain(int increment) {
        super.retain(increment);
        return this;
    }

    @Override // io.netty.handler.codec.memcache.binary.AbstractBinaryMemcacheMessage, io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public BinaryMemcacheRequest touch() {
        super.touch();
        return this;
    }

    @Override // io.netty.handler.codec.memcache.binary.AbstractBinaryMemcacheMessage, io.netty.util.ReferenceCounted
    public BinaryMemcacheRequest touch(Object hint) {
        super.touch(hint);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void copyMeta(DefaultBinaryMemcacheRequest dst) {
        super.copyMeta((AbstractBinaryMemcacheMessage) dst);
        dst.reserved = this.reserved;
    }
}
