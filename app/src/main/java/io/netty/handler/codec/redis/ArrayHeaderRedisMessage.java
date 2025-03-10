package io.netty.handler.codec.redis;

import io.netty.util.internal.StringUtil;

/* loaded from: classes4.dex */
public class ArrayHeaderRedisMessage implements RedisMessage {
    private final long length;

    public ArrayHeaderRedisMessage(long length) {
        if (length < -1) {
            throw new RedisCodecException("length: " + length + " (expected: >= -1)");
        }
        this.length = length;
    }

    public final long length() {
        return this.length;
    }

    public boolean isNull() {
        return this.length == -1;
    }

    public String toString() {
        return StringUtil.simpleClassName(this) + "[length=" + this.length + ']';
    }
}
