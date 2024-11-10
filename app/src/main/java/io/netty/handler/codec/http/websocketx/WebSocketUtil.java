package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.internal.PlatformDependent;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/* loaded from: classes4.dex */
final class WebSocketUtil {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final FastThreadLocal<MessageDigest> MD5 = new FastThreadLocal<MessageDigest>() { // from class: io.netty.handler.codec.http.websocketx.WebSocketUtil.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // io.netty.util.concurrent.FastThreadLocal
        public MessageDigest initialValue() throws Exception {
            try {
                return MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                throw new InternalError("MD5 not supported on this platform - Outdated?");
            }
        }
    };
    private static final FastThreadLocal<MessageDigest> SHA1 = new FastThreadLocal<MessageDigest>() { // from class: io.netty.handler.codec.http.websocketx.WebSocketUtil.2
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // io.netty.util.concurrent.FastThreadLocal
        public MessageDigest initialValue() throws Exception {
            try {
                return MessageDigest.getInstance("SHA1");
            } catch (NoSuchAlgorithmException e) {
                throw new InternalError("SHA-1 not supported on this platform - Outdated?");
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] md5(byte[] data) {
        return digest(MD5, data);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] sha1(byte[] data) {
        return digest(SHA1, data);
    }

    private static byte[] digest(FastThreadLocal<MessageDigest> digestFastThreadLocal, byte[] data) {
        MessageDigest digest = digestFastThreadLocal.get();
        digest.reset();
        return digest.digest(data);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String base64(byte[] data) {
        if (PlatformDependent.javaVersion() >= 8) {
            return Base64.getEncoder().encodeToString(data);
        }
        ByteBuf encodedData = Unpooled.wrappedBuffer(data);
        try {
            ByteBuf encoded = io.netty.handler.codec.base64.Base64.encode(encodedData);
            try {
                String encodedString = encoded.toString(CharsetUtil.UTF_8);
                return encodedString;
            } finally {
                encoded.release();
            }
        } finally {
            encodedData.release();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] randomBytes(int size) {
        byte[] bytes = new byte[size];
        PlatformDependent.threadLocalRandom().nextBytes(bytes);
        return bytes;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int randomNumber(int minimum, int maximum) {
        if (minimum >= maximum) {
            throw new AssertionError();
        }
        double fraction = PlatformDependent.threadLocalRandom().nextDouble();
        return (int) (minimum + ((maximum - minimum) * fraction));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int byteAtIndex(int mask, int index) {
        return (mask >> ((3 - index) * 8)) & 255;
    }

    private WebSocketUtil() {
    }
}
