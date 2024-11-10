package io.netty.handler.codec.redis;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.DefaultByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.util.internal.StringUtil;

/* loaded from: classes4.dex */
public class FullBulkStringRedisMessage extends DefaultByteBufHolder implements LastBulkStringRedisContent {
    public static final FullBulkStringRedisMessage NULL_INSTANCE = new FullBulkStringRedisMessage() { // from class: io.netty.handler.codec.redis.FullBulkStringRedisMessage.1
        @Override // io.netty.handler.codec.redis.FullBulkStringRedisMessage, io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
        public /* bridge */ /* synthetic */ ByteBufHolder replace(ByteBuf byteBuf) {
            return super.replace(byteBuf);
        }

        @Override // io.netty.handler.codec.redis.FullBulkStringRedisMessage, io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
        public /* bridge */ /* synthetic */ BulkStringRedisContent replace(ByteBuf byteBuf) {
            return super.replace(byteBuf);
        }

        @Override // io.netty.handler.codec.redis.FullBulkStringRedisMessage, io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
        public /* bridge */ /* synthetic */ LastBulkStringRedisContent replace(ByteBuf byteBuf) {
            return super.replace(byteBuf);
        }

        @Override // io.netty.handler.codec.redis.FullBulkStringRedisMessage
        public boolean isNull() {
            return true;
        }

        @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
        public ByteBuf content() {
            return Unpooled.EMPTY_BUFFER;
        }

        @Override // io.netty.handler.codec.redis.FullBulkStringRedisMessage, io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
        public FullBulkStringRedisMessage copy() {
            return this;
        }

        @Override // io.netty.handler.codec.redis.FullBulkStringRedisMessage, io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
        public FullBulkStringRedisMessage duplicate() {
            return this;
        }

        @Override // io.netty.handler.codec.redis.FullBulkStringRedisMessage, io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
        public FullBulkStringRedisMessage retainedDuplicate() {
            return this;
        }

        @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
        public int refCnt() {
            return 1;
        }

        @Override // io.netty.handler.codec.redis.FullBulkStringRedisMessage, io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
        public FullBulkStringRedisMessage retain() {
            return this;
        }

        @Override // io.netty.handler.codec.redis.FullBulkStringRedisMessage, io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
        public FullBulkStringRedisMessage retain(int increment) {
            return this;
        }

        @Override // io.netty.handler.codec.redis.FullBulkStringRedisMessage, io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
        public FullBulkStringRedisMessage touch() {
            return this;
        }

        @Override // io.netty.handler.codec.redis.FullBulkStringRedisMessage, io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
        public FullBulkStringRedisMessage touch(Object hint) {
            return this;
        }

        @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
        public boolean release() {
            return false;
        }

        @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
        public boolean release(int decrement) {
            return false;
        }
    };
    public static final FullBulkStringRedisMessage EMPTY_INSTANCE = new FullBulkStringRedisMessage() { // from class: io.netty.handler.codec.redis.FullBulkStringRedisMessage.2
        @Override // io.netty.handler.codec.redis.FullBulkStringRedisMessage, io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
        public /* bridge */ /* synthetic */ ByteBufHolder replace(ByteBuf byteBuf) {
            return super.replace(byteBuf);
        }

        @Override // io.netty.handler.codec.redis.FullBulkStringRedisMessage, io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
        public /* bridge */ /* synthetic */ BulkStringRedisContent replace(ByteBuf byteBuf) {
            return super.replace(byteBuf);
        }

        @Override // io.netty.handler.codec.redis.FullBulkStringRedisMessage, io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
        public /* bridge */ /* synthetic */ LastBulkStringRedisContent replace(ByteBuf byteBuf) {
            return super.replace(byteBuf);
        }

        @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
        public ByteBuf content() {
            return Unpooled.EMPTY_BUFFER;
        }

        @Override // io.netty.handler.codec.redis.FullBulkStringRedisMessage, io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
        public FullBulkStringRedisMessage copy() {
            return this;
        }

        @Override // io.netty.handler.codec.redis.FullBulkStringRedisMessage, io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
        public FullBulkStringRedisMessage duplicate() {
            return this;
        }

        @Override // io.netty.handler.codec.redis.FullBulkStringRedisMessage, io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
        public FullBulkStringRedisMessage retainedDuplicate() {
            return this;
        }

        @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
        public int refCnt() {
            return 1;
        }

        @Override // io.netty.handler.codec.redis.FullBulkStringRedisMessage, io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
        public FullBulkStringRedisMessage retain() {
            return this;
        }

        @Override // io.netty.handler.codec.redis.FullBulkStringRedisMessage, io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
        public FullBulkStringRedisMessage retain(int increment) {
            return this;
        }

        @Override // io.netty.handler.codec.redis.FullBulkStringRedisMessage, io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
        public FullBulkStringRedisMessage touch() {
            return this;
        }

        @Override // io.netty.handler.codec.redis.FullBulkStringRedisMessage, io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
        public FullBulkStringRedisMessage touch(Object hint) {
            return this;
        }

        @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
        public boolean release() {
            return false;
        }

        @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
        public boolean release(int decrement) {
            return false;
        }
    };

    private FullBulkStringRedisMessage() {
        this(Unpooled.EMPTY_BUFFER);
    }

    public FullBulkStringRedisMessage(ByteBuf content) {
        super(content);
    }

    public boolean isNull() {
        return false;
    }

    @Override // io.netty.buffer.DefaultByteBufHolder
    public String toString() {
        return StringUtil.simpleClassName(this) + "[content=" + content() + ']';
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public FullBulkStringRedisMessage copy() {
        return (FullBulkStringRedisMessage) super.copy();
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public FullBulkStringRedisMessage duplicate() {
        return (FullBulkStringRedisMessage) super.duplicate();
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public FullBulkStringRedisMessage retainedDuplicate() {
        return (FullBulkStringRedisMessage) super.retainedDuplicate();
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public FullBulkStringRedisMessage replace(ByteBuf content) {
        return new FullBulkStringRedisMessage(content);
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public FullBulkStringRedisMessage retain() {
        super.retain();
        return this;
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public FullBulkStringRedisMessage retain(int increment) {
        super.retain(increment);
        return this;
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public FullBulkStringRedisMessage touch() {
        super.touch();
        return this;
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public FullBulkStringRedisMessage touch(Object hint) {
        super.touch(hint);
        return this;
    }
}
