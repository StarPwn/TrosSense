package io.netty.handler.codec.redis;

import io.netty.handler.codec.http2.Http2CodecUtil;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.PlatformDependent;

/* loaded from: classes4.dex */
final class RedisCodecUtil {
    private RedisCodecUtil() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] longToAsciiBytes(long value) {
        return Long.toString(value).getBytes(CharsetUtil.US_ASCII);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static short makeShort(char first, char second) {
        return (short) (PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? (second << '\b') | first : (first << '\b') | second);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] shortToBytes(short value) {
        byte[] bytes = new byte[2];
        if (PlatformDependent.BIG_ENDIAN_NATIVE_ORDER) {
            bytes[1] = (byte) ((value >> 8) & 255);
            bytes[0] = (byte) (value & Http2CodecUtil.MAX_UNSIGNED_BYTE);
        } else {
            bytes[0] = (byte) ((value >> 8) & 255);
            bytes[1] = (byte) (value & Http2CodecUtil.MAX_UNSIGNED_BYTE);
        }
        return bytes;
    }
}
