package io.netty.handler.codec.redis;

import io.netty.util.AbstractReferenceCounted;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;
import java.util.Collections;
import java.util.List;

/* loaded from: classes4.dex */
public class ArrayRedisMessage extends AbstractReferenceCounted implements RedisMessage {
    private final List<RedisMessage> children;
    public static final ArrayRedisMessage NULL_INSTANCE = new ArrayRedisMessage() { // from class: io.netty.handler.codec.redis.ArrayRedisMessage.1
        @Override // io.netty.handler.codec.redis.ArrayRedisMessage
        public boolean isNull() {
            return true;
        }

        @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
        public ArrayRedisMessage retain() {
            return this;
        }

        @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
        public ArrayRedisMessage retain(int increment) {
            return this;
        }

        @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
        public ArrayRedisMessage touch() {
            return this;
        }

        @Override // io.netty.handler.codec.redis.ArrayRedisMessage, io.netty.util.ReferenceCounted
        public ArrayRedisMessage touch(Object hint) {
            return this;
        }

        @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
        public boolean release() {
            return false;
        }

        @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
        public boolean release(int decrement) {
            return false;
        }

        @Override // io.netty.handler.codec.redis.ArrayRedisMessage
        public String toString() {
            return "NullArrayRedisMessage";
        }
    };
    public static final ArrayRedisMessage EMPTY_INSTANCE = new ArrayRedisMessage() { // from class: io.netty.handler.codec.redis.ArrayRedisMessage.2
        @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
        public ArrayRedisMessage retain() {
            return this;
        }

        @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
        public ArrayRedisMessage retain(int increment) {
            return this;
        }

        @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
        public ArrayRedisMessage touch() {
            return this;
        }

        @Override // io.netty.handler.codec.redis.ArrayRedisMessage, io.netty.util.ReferenceCounted
        public ArrayRedisMessage touch(Object hint) {
            return this;
        }

        @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
        public boolean release() {
            return false;
        }

        @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
        public boolean release(int decrement) {
            return false;
        }

        @Override // io.netty.handler.codec.redis.ArrayRedisMessage
        public String toString() {
            return "EmptyArrayRedisMessage";
        }
    };

    private ArrayRedisMessage() {
        this.children = Collections.emptyList();
    }

    public ArrayRedisMessage(List<RedisMessage> children) {
        this.children = (List) ObjectUtil.checkNotNull(children, "children");
    }

    public final List<RedisMessage> children() {
        return this.children;
    }

    public boolean isNull() {
        return false;
    }

    @Override // io.netty.util.AbstractReferenceCounted
    protected void deallocate() {
        for (RedisMessage msg : this.children) {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override // io.netty.util.ReferenceCounted
    public ArrayRedisMessage touch(Object hint) {
        for (RedisMessage msg : this.children) {
            ReferenceCountUtil.touch(msg);
        }
        return this;
    }

    public String toString() {
        return StringUtil.simpleClassName(this) + "[children=" + this.children.size() + ']';
    }
}
