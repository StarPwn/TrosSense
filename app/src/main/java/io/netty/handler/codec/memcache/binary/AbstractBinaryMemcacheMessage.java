package io.netty.handler.codec.memcache.binary;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.memcache.AbstractMemcacheObject;

/* loaded from: classes4.dex */
public abstract class AbstractBinaryMemcacheMessage extends AbstractMemcacheObject implements BinaryMemcacheMessage {
    private long cas;
    private byte dataType;
    private ByteBuf extras;
    private byte extrasLength;
    private ByteBuf key;
    private short keyLength;
    private byte magic;
    private int opaque;
    private byte opcode;
    private int totalBodyLength;

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractBinaryMemcacheMessage(ByteBuf key, ByteBuf extras) {
        this.key = key;
        this.keyLength = key == null ? (short) 0 : (short) key.readableBytes();
        this.extras = extras;
        this.extrasLength = extras != null ? (byte) extras.readableBytes() : (byte) 0;
        this.totalBodyLength = this.keyLength + this.extrasLength;
    }

    @Override // io.netty.handler.codec.memcache.binary.BinaryMemcacheMessage
    public ByteBuf key() {
        return this.key;
    }

    @Override // io.netty.handler.codec.memcache.binary.BinaryMemcacheMessage
    public ByteBuf extras() {
        return this.extras;
    }

    @Override // io.netty.handler.codec.memcache.binary.BinaryMemcacheMessage
    public BinaryMemcacheMessage setKey(ByteBuf key) {
        if (this.key != null) {
            this.key.release();
        }
        this.key = key;
        short oldKeyLength = this.keyLength;
        this.keyLength = key == null ? (short) 0 : (short) key.readableBytes();
        this.totalBodyLength = (this.totalBodyLength + this.keyLength) - oldKeyLength;
        return this;
    }

    @Override // io.netty.handler.codec.memcache.binary.BinaryMemcacheMessage
    public BinaryMemcacheMessage setExtras(ByteBuf extras) {
        if (this.extras != null) {
            this.extras.release();
        }
        this.extras = extras;
        short oldExtrasLength = this.extrasLength;
        this.extrasLength = extras == null ? (byte) 0 : (byte) extras.readableBytes();
        this.totalBodyLength = (this.totalBodyLength + this.extrasLength) - oldExtrasLength;
        return this;
    }

    @Override // io.netty.handler.codec.memcache.binary.BinaryMemcacheMessage
    public byte magic() {
        return this.magic;
    }

    @Override // io.netty.handler.codec.memcache.binary.BinaryMemcacheMessage
    public BinaryMemcacheMessage setMagic(byte magic) {
        this.magic = magic;
        return this;
    }

    @Override // io.netty.handler.codec.memcache.binary.BinaryMemcacheMessage
    public long cas() {
        return this.cas;
    }

    @Override // io.netty.handler.codec.memcache.binary.BinaryMemcacheMessage
    public BinaryMemcacheMessage setCas(long cas) {
        this.cas = cas;
        return this;
    }

    @Override // io.netty.handler.codec.memcache.binary.BinaryMemcacheMessage
    public int opaque() {
        return this.opaque;
    }

    @Override // io.netty.handler.codec.memcache.binary.BinaryMemcacheMessage
    public BinaryMemcacheMessage setOpaque(int opaque) {
        this.opaque = opaque;
        return this;
    }

    @Override // io.netty.handler.codec.memcache.binary.BinaryMemcacheMessage
    public int totalBodyLength() {
        return this.totalBodyLength;
    }

    @Override // io.netty.handler.codec.memcache.binary.BinaryMemcacheMessage
    public BinaryMemcacheMessage setTotalBodyLength(int totalBodyLength) {
        this.totalBodyLength = totalBodyLength;
        return this;
    }

    @Override // io.netty.handler.codec.memcache.binary.BinaryMemcacheMessage
    public byte dataType() {
        return this.dataType;
    }

    @Override // io.netty.handler.codec.memcache.binary.BinaryMemcacheMessage
    public BinaryMemcacheMessage setDataType(byte dataType) {
        this.dataType = dataType;
        return this;
    }

    @Override // io.netty.handler.codec.memcache.binary.BinaryMemcacheMessage
    public byte extrasLength() {
        return this.extrasLength;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BinaryMemcacheMessage setExtrasLength(byte extrasLength) {
        this.extrasLength = extrasLength;
        return this;
    }

    @Override // io.netty.handler.codec.memcache.binary.BinaryMemcacheMessage
    public short keyLength() {
        return this.keyLength;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BinaryMemcacheMessage setKeyLength(short keyLength) {
        this.keyLength = keyLength;
        return this;
    }

    @Override // io.netty.handler.codec.memcache.binary.BinaryMemcacheMessage
    public byte opcode() {
        return this.opcode;
    }

    @Override // io.netty.handler.codec.memcache.binary.BinaryMemcacheMessage
    public BinaryMemcacheMessage setOpcode(byte opcode) {
        this.opcode = opcode;
        return this;
    }

    @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public BinaryMemcacheMessage retain() {
        super.retain();
        return this;
    }

    @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public BinaryMemcacheMessage retain(int increment) {
        super.retain(increment);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.util.AbstractReferenceCounted
    public void deallocate() {
        if (this.key != null) {
            this.key.release();
        }
        if (this.extras != null) {
            this.extras.release();
        }
    }

    @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public BinaryMemcacheMessage touch() {
        super.touch();
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public BinaryMemcacheMessage touch(Object hint) {
        if (this.key != null) {
            this.key.touch(hint);
        }
        if (this.extras != null) {
            this.extras.touch(hint);
        }
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void copyMeta(AbstractBinaryMemcacheMessage dst) {
        dst.magic = this.magic;
        dst.opcode = this.opcode;
        dst.keyLength = this.keyLength;
        dst.extrasLength = this.extrasLength;
        dst.dataType = this.dataType;
        dst.totalBodyLength = this.totalBodyLength;
        dst.opaque = this.opaque;
        dst.cas = this.cas;
    }
}
