package io.netty.handler.codec.redis;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import io.netty.util.collection.LongObjectHashMap;
import io.netty.util.collection.LongObjectMap;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes4.dex */
public final class FixedRedisMessagePool implements RedisMessagePool {
    public static final FixedRedisMessagePool INSTANCE = new FixedRedisMessagePool();
    private static final long MAX_CACHED_INTEGER_NUMBER = 128;
    private static final long MIN_CACHED_INTEGER_NUMBER = -1;
    private static final int SIZE_CACHED_INTEGER_NUMBER = 129;
    private final Map<ByteBuf, ErrorRedisMessage> byteBufToErrors;
    private final Map<ByteBuf, IntegerRedisMessage> byteBufToIntegers;
    private final Map<RedisErrorKey, ErrorRedisMessage> keyToErrors;
    private final LongObjectMap<byte[]> longToByteBufs;
    private final LongObjectMap<IntegerRedisMessage> longToIntegers;
    private final Map<String, ErrorRedisMessage> stringToErrors;
    private final Map<RedisReplyKey, SimpleStringRedisMessage> keyToSimpleStrings = new HashMap(RedisReplyKey.values().length, 1.0f);
    private final Map<String, SimpleStringRedisMessage> stringToSimpleStrings = new HashMap(RedisReplyKey.values().length, 1.0f);
    private final Map<ByteBuf, SimpleStringRedisMessage> byteBufToSimpleStrings = new HashMap(RedisReplyKey.values().length, 1.0f);

    /* loaded from: classes4.dex */
    public enum RedisReplyKey {
        OK,
        PONG,
        QUEUED
    }

    /* loaded from: classes4.dex */
    public enum RedisErrorKey {
        ERR("ERR"),
        ERR_IDX("ERR index out of range"),
        ERR_NOKEY("ERR no such key"),
        ERR_SAMEOBJ("ERR source and destination objects are the same"),
        ERR_SYNTAX("ERR syntax error"),
        BUSY("BUSY Redis is busy running a script. You can only call SCRIPT KILL or SHUTDOWN NOSAVE."),
        BUSYKEY("BUSYKEY Target key name already exists."),
        EXECABORT("EXECABORT Transaction discarded because of previous errors."),
        LOADING("LOADING Redis is loading the dataset in memory"),
        MASTERDOWN("MASTERDOWN Link with MASTER is down and slave-serve-stale-data is set to 'no'."),
        MISCONF("MISCONF Redis is configured to save RDB snapshots, but is currently not able to persist on disk. Commands that may modify the data set are disabled. Please check Redis logs for details about the error."),
        NOREPLICAS("NOREPLICAS Not enough good slaves to write."),
        NOSCRIPT("NOSCRIPT No matching script. Please use EVAL."),
        OOM("OOM command not allowed when used memory > 'maxmemory'."),
        READONLY("READONLY You can't write against a read only slave."),
        WRONGTYPE("WRONGTYPE Operation against a key holding the wrong kind of value"),
        NOT_AUTH("NOAUTH Authentication required.");

        private final String msg;

        RedisErrorKey(String msg) {
            this.msg = msg;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.msg;
        }
    }

    private FixedRedisMessagePool() {
        for (RedisReplyKey value : RedisReplyKey.values()) {
            ByteBuf key = Unpooled.unreleasableBuffer(Unpooled.wrappedBuffer(value.name().getBytes(CharsetUtil.UTF_8))).asReadOnly();
            SimpleStringRedisMessage message = new SimpleStringRedisMessage(new String(Unpooled.unreleasableBuffer(Unpooled.wrappedBuffer(value.name().getBytes(CharsetUtil.UTF_8))).array()));
            this.stringToSimpleStrings.put(value.name(), message);
            this.keyToSimpleStrings.put(value, message);
            this.byteBufToSimpleStrings.put(key, message);
        }
        this.keyToErrors = new HashMap(RedisErrorKey.values().length, 1.0f);
        this.stringToErrors = new HashMap(RedisErrorKey.values().length, 1.0f);
        this.byteBufToErrors = new HashMap(RedisErrorKey.values().length, 1.0f);
        for (RedisErrorKey value2 : RedisErrorKey.values()) {
            ByteBuf key2 = Unpooled.unreleasableBuffer(Unpooled.wrappedBuffer(value2.toString().getBytes(CharsetUtil.UTF_8))).asReadOnly();
            ErrorRedisMessage message2 = new ErrorRedisMessage(new String(Unpooled.unreleasableBuffer(Unpooled.wrappedBuffer(value2.toString().getBytes(CharsetUtil.UTF_8))).array()));
            this.stringToErrors.put(value2.toString(), message2);
            this.keyToErrors.put(value2, message2);
            this.byteBufToErrors.put(key2, message2);
        }
        this.byteBufToIntegers = new HashMap(129, 1.0f);
        this.longToIntegers = new LongObjectHashMap(129, 1.0f);
        this.longToByteBufs = new LongObjectHashMap(129, 1.0f);
        for (long value3 = -1; value3 < MAX_CACHED_INTEGER_NUMBER; value3++) {
            byte[] keyBytes = RedisCodecUtil.longToAsciiBytes(value3);
            ByteBuf keyByteBuf = Unpooled.unreleasableBuffer(Unpooled.wrappedBuffer(keyBytes)).asReadOnly();
            IntegerRedisMessage cached = new IntegerRedisMessage(value3);
            this.byteBufToIntegers.put(keyByteBuf, cached);
            this.longToIntegers.put(value3, (long) cached);
            this.longToByteBufs.put(value3, (long) keyBytes);
        }
    }

    @Override // io.netty.handler.codec.redis.RedisMessagePool
    public SimpleStringRedisMessage getSimpleString(String content) {
        return this.stringToSimpleStrings.get(content);
    }

    public SimpleStringRedisMessage getSimpleString(RedisReplyKey key) {
        return this.keyToSimpleStrings.get(key);
    }

    @Override // io.netty.handler.codec.redis.RedisMessagePool
    public SimpleStringRedisMessage getSimpleString(ByteBuf content) {
        return this.byteBufToSimpleStrings.get(content);
    }

    @Override // io.netty.handler.codec.redis.RedisMessagePool
    public ErrorRedisMessage getError(String content) {
        return this.stringToErrors.get(content);
    }

    public ErrorRedisMessage getError(RedisErrorKey key) {
        return this.keyToErrors.get(key);
    }

    @Override // io.netty.handler.codec.redis.RedisMessagePool
    public ErrorRedisMessage getError(ByteBuf content) {
        return this.byteBufToErrors.get(content);
    }

    @Override // io.netty.handler.codec.redis.RedisMessagePool
    public IntegerRedisMessage getInteger(long value) {
        return this.longToIntegers.get(value);
    }

    @Override // io.netty.handler.codec.redis.RedisMessagePool
    public IntegerRedisMessage getInteger(ByteBuf content) {
        return this.byteBufToIntegers.get(content);
    }

    @Override // io.netty.handler.codec.redis.RedisMessagePool
    public byte[] getByteBufOfInteger(long value) {
        return this.longToByteBufs.get(value);
    }
}
