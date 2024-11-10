package io.netty.handler.codec.http;

import io.netty.util.AsciiString;
import kotlin.text.Typography;

/* loaded from: classes4.dex */
public final class HttpHeaderValidationUtil {
    private static final long TOKEN_CHARS_HIGH;
    private static final long TOKEN_CHARS_LOW;

    private HttpHeaderValidationUtil() {
    }

    public static boolean isConnectionHeader(CharSequence name, boolean ignoreTeHeader) {
        int len = name.length();
        switch (len) {
            case 2:
                if (ignoreTeHeader) {
                    return false;
                }
                return AsciiString.contentEqualsIgnoreCase(name, HttpHeaderNames.TE);
            case 7:
                return AsciiString.contentEqualsIgnoreCase(name, HttpHeaderNames.UPGRADE);
            case 10:
                return AsciiString.contentEqualsIgnoreCase(name, HttpHeaderNames.CONNECTION) || AsciiString.contentEqualsIgnoreCase(name, HttpHeaderNames.KEEP_ALIVE);
            case 16:
                return AsciiString.contentEqualsIgnoreCase(name, HttpHeaderNames.PROXY_CONNECTION);
            case 17:
                return AsciiString.contentEqualsIgnoreCase(name, HttpHeaderNames.TRANSFER_ENCODING);
            default:
                return false;
        }
    }

    public static boolean isTeNotTrailers(CharSequence name, CharSequence value) {
        return name.length() == 2 && AsciiString.contentEqualsIgnoreCase(name, HttpHeaderNames.TE) && !AsciiString.contentEqualsIgnoreCase(value, HttpHeaderValues.TRAILERS);
    }

    public static int validateValidHeaderValue(CharSequence value) {
        int length = value.length();
        if (length == 0) {
            return -1;
        }
        if (value instanceof AsciiString) {
            return verifyValidHeaderValueAsciiString((AsciiString) value);
        }
        return verifyValidHeaderValueCharSequence(value);
    }

    private static int verifyValidHeaderValueAsciiString(AsciiString value) {
        byte[] array = value.array();
        int start = value.arrayOffset();
        int b = array[start] & 255;
        if (b < 33 || b == 127) {
            return 0;
        }
        int length = value.length();
        for (int i = start + 1; i < length; i++) {
            int b2 = array[i] & 255;
            if ((b2 < 32 && b2 != 9) || b2 == 127) {
                return i - start;
            }
        }
        return -1;
    }

    private static int verifyValidHeaderValueCharSequence(CharSequence value) {
        int b = value.charAt(0);
        if (b < 33 || b == 127) {
            return 0;
        }
        int length = value.length();
        for (int i = 1; i < length; i++) {
            int b2 = value.charAt(i);
            if ((b2 < 32 && b2 != 9) || b2 == 127) {
                return i;
            }
        }
        return -1;
    }

    public static int validateToken(CharSequence token) {
        if (token instanceof AsciiString) {
            return validateAsciiStringToken((AsciiString) token);
        }
        return validateCharSequenceToken(token);
    }

    private static int validateAsciiStringToken(AsciiString token) {
        byte[] array = token.array();
        int len = token.arrayOffset() + token.length();
        for (int i = token.arrayOffset(); i < len; i++) {
            if (!BitSet128.contains(array[i], TOKEN_CHARS_HIGH, TOKEN_CHARS_LOW)) {
                return i - token.arrayOffset();
            }
        }
        return -1;
    }

    private static int validateCharSequenceToken(CharSequence token) {
        int len = token.length();
        for (int i = 0; i < len; i++) {
            byte value = (byte) token.charAt(i);
            if (!BitSet128.contains(value, TOKEN_CHARS_HIGH, TOKEN_CHARS_LOW)) {
                return i;
            }
        }
        return -1;
    }

    static {
        BitSet128 tokenChars = new BitSet128().range('0', '9').range('a', 'z').range('A', 'Z').bits('-', '.', '_', '~').bits('!', '#', Typography.dollar, '%', Typography.amp, '\'', '*', '+', '^', '`', '|');
        TOKEN_CHARS_HIGH = tokenChars.high();
        TOKEN_CHARS_LOW = tokenChars.low();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class BitSet128 {
        private long high;
        private long low;

        private BitSet128() {
        }

        BitSet128 range(char fromInc, char toInc) {
            for (int bit = fromInc; bit <= toInc; bit++) {
                if (bit < 64) {
                    this.low = (1 << bit) | this.low;
                } else {
                    this.high = (1 << (bit - 64)) | this.high;
                }
            }
            return this;
        }

        BitSet128 bits(char... bits) {
            for (char bit : bits) {
                if (bit < '@') {
                    this.low = (1 << bit) | this.low;
                } else {
                    this.high = (1 << (bit - '@')) | this.high;
                }
            }
            return this;
        }

        long high() {
            return this.high;
        }

        long low() {
            return this.low;
        }

        static boolean contains(byte bit, long high, long low) {
            if (bit < 0) {
                return false;
            }
            if (bit < 64) {
                if (0 == ((1 << bit) & low)) {
                    return false;
                }
                return true;
            }
            if (0 == ((1 << (bit - 64)) & high)) {
                return false;
            }
            return true;
        }
    }
}
