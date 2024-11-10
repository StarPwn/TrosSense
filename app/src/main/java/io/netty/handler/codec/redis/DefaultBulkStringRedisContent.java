package io.netty.handler.codec.redis;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.DefaultByteBufHolder;
import io.netty.util.internal.StringUtil;

/* loaded from: classes4.dex */
public class DefaultBulkStringRedisContent extends DefaultByteBufHolder implements BulkStringRedisContent {
    public DefaultBulkStringRedisContent(ByteBuf content) {
        super(content);
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public BulkStringRedisContent copy() {
        return (BulkStringRedisContent) super.copy();
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public BulkStringRedisContent duplicate() {
        return (BulkStringRedisContent) super.duplicate();
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public BulkStringRedisContent retainedDuplicate() {
        return (BulkStringRedisContent) super.retainedDuplicate();
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public BulkStringRedisContent replace(ByteBuf content) {
        return new DefaultBulkStringRedisContent(content);
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public BulkStringRedisContent retain() {
        super.retain();
        return this;
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public BulkStringRedisContent retain(int increment) {
        super.retain(increment);
        return this;
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public BulkStringRedisContent touch() {
        super.touch();
        return this;
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public BulkStringRedisContent touch(Object hint) {
        super.touch(hint);
        return this;
    }

    @Override // io.netty.buffer.DefaultByteBufHolder
    public String toString() {
        return StringUtil.simpleClassName(this) + "[content=" + content() + ']';
    }
}
