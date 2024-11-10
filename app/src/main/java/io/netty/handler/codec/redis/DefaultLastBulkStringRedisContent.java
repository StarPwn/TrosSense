package io.netty.handler.codec.redis;

import io.netty.buffer.ByteBuf;

/* loaded from: classes4.dex */
public final class DefaultLastBulkStringRedisContent extends DefaultBulkStringRedisContent implements LastBulkStringRedisContent {
    public DefaultLastBulkStringRedisContent(ByteBuf content) {
        super(content);
    }

    @Override // io.netty.handler.codec.redis.DefaultBulkStringRedisContent, io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public LastBulkStringRedisContent copy() {
        return (LastBulkStringRedisContent) super.copy();
    }

    @Override // io.netty.handler.codec.redis.DefaultBulkStringRedisContent, io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public LastBulkStringRedisContent duplicate() {
        return (LastBulkStringRedisContent) super.duplicate();
    }

    @Override // io.netty.handler.codec.redis.DefaultBulkStringRedisContent, io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public LastBulkStringRedisContent retainedDuplicate() {
        return (LastBulkStringRedisContent) super.retainedDuplicate();
    }

    @Override // io.netty.handler.codec.redis.DefaultBulkStringRedisContent, io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public LastBulkStringRedisContent replace(ByteBuf content) {
        return new DefaultLastBulkStringRedisContent(content);
    }

    @Override // io.netty.handler.codec.redis.DefaultBulkStringRedisContent, io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public LastBulkStringRedisContent retain() {
        super.retain();
        return this;
    }

    @Override // io.netty.handler.codec.redis.DefaultBulkStringRedisContent, io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public LastBulkStringRedisContent retain(int increment) {
        super.retain(increment);
        return this;
    }

    @Override // io.netty.handler.codec.redis.DefaultBulkStringRedisContent, io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public LastBulkStringRedisContent touch() {
        super.touch();
        return this;
    }

    @Override // io.netty.handler.codec.redis.DefaultBulkStringRedisContent, io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public LastBulkStringRedisContent touch(Object hint) {
        super.touch(hint);
        return this;
    }
}
