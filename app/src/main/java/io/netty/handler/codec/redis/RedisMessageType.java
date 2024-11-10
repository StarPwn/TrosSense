package io.netty.handler.codec.redis;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpConstants;
import io.netty.handler.codec.memcache.binary.BinaryMemcacheOpcodes;

/* loaded from: classes4.dex */
public enum RedisMessageType {
    INLINE_COMMAND(null, true),
    SIMPLE_STRING((byte) 43, true),
    ERROR((byte) 45, true),
    INTEGER(Byte.valueOf(HttpConstants.COLON), true),
    BULK_STRING(Byte.valueOf(BinaryMemcacheOpcodes.GATKQ), false),
    ARRAY_HEADER((byte) 42, false);

    private final boolean inline;
    private final Byte value;

    RedisMessageType(Byte value, boolean inline) {
        this.value = value;
        this.inline = inline;
    }

    public int length() {
        return this.value != null ? 1 : 0;
    }

    public boolean isInline() {
        return this.inline;
    }

    public static RedisMessageType readFrom(ByteBuf in, boolean decodeInlineCommands) {
        int initialIndex = in.readerIndex();
        RedisMessageType type = valueOf(in.readByte());
        if (type == INLINE_COMMAND) {
            if (!decodeInlineCommands) {
                throw new RedisCodecException("Decoding of inline commands is disabled");
            }
            in.readerIndex(initialIndex);
        }
        return type;
    }

    public void writeTo(ByteBuf out) {
        if (this.value == null) {
            return;
        }
        out.writeByte(this.value.byteValue());
    }

    private static RedisMessageType valueOf(byte value) {
        switch (value) {
            case 36:
                return BULK_STRING;
            case 42:
                return ARRAY_HEADER;
            case 43:
                return SIMPLE_STRING;
            case 45:
                return ERROR;
            case 58:
                return INTEGER;
            default:
                return INLINE_COMMAND;
        }
    }
}
