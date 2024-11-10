package okio.internal;

import java.util.Arrays;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import okio.Buffer;
import okio.ByteString;
import okio._Base64Kt;
import okio._JvmPlatformKt;
import okio._UtilKt;
import org.msgpack.core.MessagePack;

/* compiled from: -ByteString.kt */
@Metadata(d1 = {"\u0000R\n\u0000\n\u0002\u0010\u0019\n\u0002\b\u0005\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\f\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0005\n\u0002\b\u0017\n\u0002\u0018\u0002\n\u0000\u001a\u0018\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u0007H\u0002\u001a\u0011\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\tH\u0080\b\u001a\u0010\u0010\u000e\u001a\u00020\u00072\u0006\u0010\u000f\u001a\u00020\u0010H\u0002\u001a\r\u0010\u0011\u001a\u00020\u0012*\u00020\fH\u0080\b\u001a\r\u0010\u0013\u001a\u00020\u0012*\u00020\fH\u0080\b\u001a\u0015\u0010\u0014\u001a\u00020\u0007*\u00020\f2\u0006\u0010\u0015\u001a\u00020\fH\u0080\b\u001a-\u0010\u0016\u001a\u00020\u0017*\u00020\f2\u0006\u0010\u0018\u001a\u00020\u00072\u0006\u0010\u0019\u001a\u00020\t2\u0006\u0010\u001a\u001a\u00020\u00072\u0006\u0010\u001b\u001a\u00020\u0007H\u0080\b\u001a\u000f\u0010\u001c\u001a\u0004\u0018\u00010\f*\u00020\u0012H\u0080\b\u001a\r\u0010\u001d\u001a\u00020\f*\u00020\u0012H\u0080\b\u001a\r\u0010\u001e\u001a\u00020\f*\u00020\u0012H\u0080\b\u001a\u0015\u0010\u001f\u001a\u00020 *\u00020\f2\u0006\u0010!\u001a\u00020\tH\u0080\b\u001a\u0015\u0010\u001f\u001a\u00020 *\u00020\f2\u0006\u0010!\u001a\u00020\fH\u0080\b\u001a\u0017\u0010\"\u001a\u00020 *\u00020\f2\b\u0010\u0015\u001a\u0004\u0018\u00010#H\u0080\b\u001a\u0015\u0010$\u001a\u00020%*\u00020\f2\u0006\u0010&\u001a\u00020\u0007H\u0080\b\u001a\r\u0010'\u001a\u00020\u0007*\u00020\fH\u0080\b\u001a\r\u0010(\u001a\u00020\u0007*\u00020\fH\u0080\b\u001a\r\u0010)\u001a\u00020\u0012*\u00020\fH\u0080\b\u001a\u001d\u0010*\u001a\u00020\u0007*\u00020\f2\u0006\u0010\u0015\u001a\u00020\t2\u0006\u0010+\u001a\u00020\u0007H\u0080\b\u001a\r\u0010,\u001a\u00020\t*\u00020\fH\u0080\b\u001a\u001d\u0010-\u001a\u00020\u0007*\u00020\f2\u0006\u0010\u0015\u001a\u00020\t2\u0006\u0010+\u001a\u00020\u0007H\u0080\b\u001a\u001d\u0010-\u001a\u00020\u0007*\u00020\f2\u0006\u0010\u0015\u001a\u00020\f2\u0006\u0010+\u001a\u00020\u0007H\u0080\b\u001a-\u0010.\u001a\u00020 *\u00020\f2\u0006\u0010\u0018\u001a\u00020\u00072\u0006\u0010\u0015\u001a\u00020\t2\u0006\u0010/\u001a\u00020\u00072\u0006\u0010\u001b\u001a\u00020\u0007H\u0080\b\u001a-\u0010.\u001a\u00020 *\u00020\f2\u0006\u0010\u0018\u001a\u00020\u00072\u0006\u0010\u0015\u001a\u00020\f2\u0006\u0010/\u001a\u00020\u00072\u0006\u0010\u001b\u001a\u00020\u0007H\u0080\b\u001a\u0015\u00100\u001a\u00020 *\u00020\f2\u0006\u00101\u001a\u00020\tH\u0080\b\u001a\u0015\u00100\u001a\u00020 *\u00020\f2\u0006\u00101\u001a\u00020\fH\u0080\b\u001a\u001d\u00102\u001a\u00020\f*\u00020\f2\u0006\u00103\u001a\u00020\u00072\u0006\u00104\u001a\u00020\u0007H\u0080\b\u001a\r\u00105\u001a\u00020\f*\u00020\fH\u0080\b\u001a\r\u00106\u001a\u00020\f*\u00020\fH\u0080\b\u001a\r\u00107\u001a\u00020\t*\u00020\fH\u0080\b\u001a\u001d\u00108\u001a\u00020\f*\u00020\t2\u0006\u0010\u0018\u001a\u00020\u00072\u0006\u0010\u001b\u001a\u00020\u0007H\u0080\b\u001a\r\u00109\u001a\u00020\u0012*\u00020\fH\u0080\b\u001a\r\u0010:\u001a\u00020\u0012*\u00020\fH\u0080\b\u001a$\u0010;\u001a\u00020\u0017*\u00020\f2\u0006\u0010<\u001a\u00020=2\u0006\u0010\u0018\u001a\u00020\u00072\u0006\u0010\u001b\u001a\u00020\u0007H\u0000\"\u001c\u0010\u0000\u001a\u00020\u00018\u0000X\u0081\u0004¢\u0006\u000e\n\u0000\u0012\u0004\b\u0002\u0010\u0003\u001a\u0004\b\u0004\u0010\u0005¨\u0006>"}, d2 = {"HEX_DIGIT_CHARS", "", "getHEX_DIGIT_CHARS$annotations", "()V", "getHEX_DIGIT_CHARS", "()[C", "codePointIndexToCharIndex", "", "s", "", "codePointCount", "commonOf", "Lokio/ByteString;", "data", "decodeHexDigit", "c", "", "commonBase64", "", "commonBase64Url", "commonCompareTo", "other", "commonCopyInto", "", "offset", "target", "targetOffset", "byteCount", "commonDecodeBase64", "commonDecodeHex", "commonEncodeUtf8", "commonEndsWith", "", "suffix", "commonEquals", "", "commonGetByte", "", "pos", "commonGetSize", "commonHashCode", "commonHex", "commonIndexOf", "fromIndex", "commonInternalArray", "commonLastIndexOf", "commonRangeEquals", "otherOffset", "commonStartsWith", "prefix", "commonSubstring", "beginIndex", "endIndex", "commonToAsciiLowercase", "commonToAsciiUppercase", "commonToByteArray", "commonToByteString", "commonToString", "commonUtf8", "commonWrite", "buffer", "Lokio/Buffer;", "okio"}, k = 2, mv = {1, 6, 0}, xi = 48)
/* loaded from: classes5.dex */
public final class _ByteStringKt {
    private static final char[] HEX_DIGIT_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static /* synthetic */ void getHEX_DIGIT_CHARS$annotations() {
    }

    public static final String commonUtf8(ByteString $this$commonUtf8) {
        Intrinsics.checkNotNullParameter($this$commonUtf8, "<this>");
        String result = $this$commonUtf8.getUtf8();
        if (result == null) {
            String result2 = _JvmPlatformKt.toUtf8String($this$commonUtf8.internalArray$okio());
            $this$commonUtf8.setUtf8$okio(result2);
            return result2;
        }
        return result;
    }

    public static final String commonBase64(ByteString $this$commonBase64) {
        Intrinsics.checkNotNullParameter($this$commonBase64, "<this>");
        return _Base64Kt.encodeBase64$default($this$commonBase64.getData(), null, 1, null);
    }

    public static final String commonBase64Url(ByteString $this$commonBase64Url) {
        Intrinsics.checkNotNullParameter($this$commonBase64Url, "<this>");
        return _Base64Kt.encodeBase64($this$commonBase64Url.getData(), _Base64Kt.getBASE64_URL_SAFE());
    }

    public static final char[] getHEX_DIGIT_CHARS() {
        return HEX_DIGIT_CHARS;
    }

    public static final String commonHex(ByteString $this$commonHex) {
        Intrinsics.checkNotNullParameter($this$commonHex, "<this>");
        char[] result = new char[$this$commonHex.getData().length * 2];
        int c = 0;
        for (int b : $this$commonHex.getData()) {
            int c2 = c + 1;
            int other$iv = b >> 4;
            result[c] = getHEX_DIGIT_CHARS()[other$iv & 15];
            c = c2 + 1;
            int other$iv2 = 15 & b;
            result[c2] = getHEX_DIGIT_CHARS()[other$iv2];
        }
        return StringsKt.concatToString(result);
    }

    public static final ByteString commonToAsciiLowercase(ByteString $this$commonToAsciiLowercase) {
        byte b;
        Intrinsics.checkNotNullParameter($this$commonToAsciiLowercase, "<this>");
        for (int i = 0; i < $this$commonToAsciiLowercase.getData().length; i++) {
            byte c = $this$commonToAsciiLowercase.getData()[i];
            byte b2 = (byte) 65;
            if (c >= b2 && c <= (b = (byte) 90)) {
                byte[] data = $this$commonToAsciiLowercase.getData();
                byte[] lowercase = Arrays.copyOf(data, data.length);
                Intrinsics.checkNotNullExpressionValue(lowercase, "copyOf(this, size)");
                int i2 = i + 1;
                lowercase[i] = (byte) (c + 32);
                while (i2 < lowercase.length) {
                    byte c2 = lowercase[i2];
                    if (c2 < b2 || c2 > b) {
                        i2++;
                    } else {
                        lowercase[i2] = (byte) (c2 + 32);
                        i2++;
                    }
                }
                return new ByteString(lowercase);
            }
        }
        return $this$commonToAsciiLowercase;
    }

    public static final ByteString commonToAsciiUppercase(ByteString $this$commonToAsciiUppercase) {
        byte b;
        Intrinsics.checkNotNullParameter($this$commonToAsciiUppercase, "<this>");
        for (int i = 0; i < $this$commonToAsciiUppercase.getData().length; i++) {
            byte c = $this$commonToAsciiUppercase.getData()[i];
            byte b2 = (byte) 97;
            if (c >= b2 && c <= (b = (byte) 122)) {
                byte[] data = $this$commonToAsciiUppercase.getData();
                byte[] lowercase = Arrays.copyOf(data, data.length);
                Intrinsics.checkNotNullExpressionValue(lowercase, "copyOf(this, size)");
                int i2 = i + 1;
                lowercase[i] = (byte) (c + MessagePack.Code.NEGFIXINT_PREFIX);
                while (i2 < lowercase.length) {
                    byte c2 = lowercase[i2];
                    if (c2 < b2 || c2 > b) {
                        i2++;
                    } else {
                        lowercase[i2] = (byte) (c2 + MessagePack.Code.NEGFIXINT_PREFIX);
                        i2++;
                    }
                }
                return new ByteString(lowercase);
            }
        }
        return $this$commonToAsciiUppercase;
    }

    public static final ByteString commonSubstring(ByteString $this$commonSubstring, int beginIndex, int endIndex) {
        Intrinsics.checkNotNullParameter($this$commonSubstring, "<this>");
        int endIndex2 = _UtilKt.resolveDefaultParameter($this$commonSubstring, endIndex);
        if (!(beginIndex >= 0)) {
            throw new IllegalArgumentException("beginIndex < 0".toString());
        }
        if (!(endIndex2 <= $this$commonSubstring.getData().length)) {
            throw new IllegalArgumentException(("endIndex > length(" + $this$commonSubstring.getData().length + ')').toString());
        }
        int subLen = endIndex2 - beginIndex;
        if (!(subLen >= 0)) {
            throw new IllegalArgumentException("endIndex < beginIndex".toString());
        }
        if (beginIndex == 0 && endIndex2 == $this$commonSubstring.getData().length) {
            return $this$commonSubstring;
        }
        return new ByteString(ArraysKt.copyOfRange($this$commonSubstring.getData(), beginIndex, endIndex2));
    }

    public static final byte commonGetByte(ByteString $this$commonGetByte, int pos) {
        Intrinsics.checkNotNullParameter($this$commonGetByte, "<this>");
        return $this$commonGetByte.getData()[pos];
    }

    public static final int commonGetSize(ByteString $this$commonGetSize) {
        Intrinsics.checkNotNullParameter($this$commonGetSize, "<this>");
        return $this$commonGetSize.getData().length;
    }

    public static final byte[] commonToByteArray(ByteString $this$commonToByteArray) {
        Intrinsics.checkNotNullParameter($this$commonToByteArray, "<this>");
        byte[] data = $this$commonToByteArray.getData();
        byte[] copyOf = Arrays.copyOf(data, data.length);
        Intrinsics.checkNotNullExpressionValue(copyOf, "copyOf(this, size)");
        return copyOf;
    }

    public static final byte[] commonInternalArray(ByteString $this$commonInternalArray) {
        Intrinsics.checkNotNullParameter($this$commonInternalArray, "<this>");
        return $this$commonInternalArray.getData();
    }

    public static final boolean commonRangeEquals(ByteString $this$commonRangeEquals, int offset, ByteString other, int otherOffset, int byteCount) {
        Intrinsics.checkNotNullParameter($this$commonRangeEquals, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        return other.rangeEquals(otherOffset, $this$commonRangeEquals.getData(), offset, byteCount);
    }

    public static final boolean commonRangeEquals(ByteString $this$commonRangeEquals, int offset, byte[] other, int otherOffset, int byteCount) {
        Intrinsics.checkNotNullParameter($this$commonRangeEquals, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        return offset >= 0 && offset <= $this$commonRangeEquals.getData().length - byteCount && otherOffset >= 0 && otherOffset <= other.length - byteCount && _UtilKt.arrayRangeEquals($this$commonRangeEquals.getData(), offset, other, otherOffset, byteCount);
    }

    public static final void commonCopyInto(ByteString $this$commonCopyInto, int offset, byte[] target, int targetOffset, int byteCount) {
        Intrinsics.checkNotNullParameter($this$commonCopyInto, "<this>");
        Intrinsics.checkNotNullParameter(target, "target");
        ArraysKt.copyInto($this$commonCopyInto.getData(), target, targetOffset, offset, offset + byteCount);
    }

    public static final boolean commonStartsWith(ByteString $this$commonStartsWith, ByteString prefix) {
        Intrinsics.checkNotNullParameter($this$commonStartsWith, "<this>");
        Intrinsics.checkNotNullParameter(prefix, "prefix");
        return $this$commonStartsWith.rangeEquals(0, prefix, 0, prefix.size());
    }

    public static final boolean commonStartsWith(ByteString $this$commonStartsWith, byte[] prefix) {
        Intrinsics.checkNotNullParameter($this$commonStartsWith, "<this>");
        Intrinsics.checkNotNullParameter(prefix, "prefix");
        return $this$commonStartsWith.rangeEquals(0, prefix, 0, prefix.length);
    }

    public static final boolean commonEndsWith(ByteString $this$commonEndsWith, ByteString suffix) {
        Intrinsics.checkNotNullParameter($this$commonEndsWith, "<this>");
        Intrinsics.checkNotNullParameter(suffix, "suffix");
        return $this$commonEndsWith.rangeEquals($this$commonEndsWith.size() - suffix.size(), suffix, 0, suffix.size());
    }

    public static final boolean commonEndsWith(ByteString $this$commonEndsWith, byte[] suffix) {
        Intrinsics.checkNotNullParameter($this$commonEndsWith, "<this>");
        Intrinsics.checkNotNullParameter(suffix, "suffix");
        return $this$commonEndsWith.rangeEquals($this$commonEndsWith.size() - suffix.length, suffix, 0, suffix.length);
    }

    public static final int commonIndexOf(ByteString $this$commonIndexOf, byte[] other, int fromIndex) {
        Intrinsics.checkNotNullParameter($this$commonIndexOf, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        int limit = $this$commonIndexOf.getData().length - other.length;
        int i = Math.max(fromIndex, 0);
        if (i <= limit) {
            while (!_UtilKt.arrayRangeEquals($this$commonIndexOf.getData(), i, other, 0, other.length)) {
                if (i == limit) {
                    return -1;
                }
                i++;
            }
            return i;
        }
        return -1;
    }

    public static final int commonLastIndexOf(ByteString $this$commonLastIndexOf, ByteString other, int fromIndex) {
        Intrinsics.checkNotNullParameter($this$commonLastIndexOf, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        return $this$commonLastIndexOf.lastIndexOf(other.internalArray$okio(), fromIndex);
    }

    public static final int commonLastIndexOf(ByteString $this$commonLastIndexOf, byte[] other, int fromIndex) {
        Intrinsics.checkNotNullParameter($this$commonLastIndexOf, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        int fromIndex2 = _UtilKt.resolveDefaultParameter($this$commonLastIndexOf, fromIndex);
        int limit = $this$commonLastIndexOf.getData().length - other.length;
        for (int i = Math.min(fromIndex2, limit); -1 < i; i--) {
            if (_UtilKt.arrayRangeEquals($this$commonLastIndexOf.getData(), i, other, 0, other.length)) {
                return i;
            }
        }
        return -1;
    }

    public static final boolean commonEquals(ByteString $this$commonEquals, Object other) {
        Intrinsics.checkNotNullParameter($this$commonEquals, "<this>");
        if (other == $this$commonEquals) {
            return true;
        }
        if (other instanceof ByteString) {
            return ((ByteString) other).size() == $this$commonEquals.getData().length && ((ByteString) other).rangeEquals(0, $this$commonEquals.getData(), 0, $this$commonEquals.getData().length);
        }
        return false;
    }

    public static final int commonHashCode(ByteString $this$commonHashCode) {
        Intrinsics.checkNotNullParameter($this$commonHashCode, "<this>");
        int result = $this$commonHashCode.getHashCode();
        if (result != 0) {
            return result;
        }
        int it2 = Arrays.hashCode($this$commonHashCode.getData());
        $this$commonHashCode.setHashCode$okio(it2);
        return it2;
    }

    public static final int commonCompareTo(ByteString $this$commonCompareTo, ByteString other) {
        Intrinsics.checkNotNullParameter($this$commonCompareTo, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        int sizeA = $this$commonCompareTo.size();
        int sizeB = other.size();
        int size = Math.min(sizeA, sizeB);
        for (int i = 0; i < size; i++) {
            int $this$and$iv = $this$commonCompareTo.getByte(i);
            int byteA = $this$and$iv & 255;
            byte $this$and$iv2 = other.getByte(i);
            int byteB = $this$and$iv2 & 255;
            if (byteA != byteB) {
                return byteA < byteB ? -1 : 1;
            }
        }
        if (sizeA == sizeB) {
            return 0;
        }
        return sizeA < sizeB ? -1 : 1;
    }

    public static final ByteString commonOf(byte[] data) {
        Intrinsics.checkNotNullParameter(data, "data");
        byte[] copyOf = Arrays.copyOf(data, data.length);
        Intrinsics.checkNotNullExpressionValue(copyOf, "copyOf(this, size)");
        return new ByteString(copyOf);
    }

    public static final ByteString commonToByteString(byte[] $this$commonToByteString, int offset, int byteCount) {
        Intrinsics.checkNotNullParameter($this$commonToByteString, "<this>");
        int byteCount2 = _UtilKt.resolveDefaultParameter($this$commonToByteString, byteCount);
        _UtilKt.checkOffsetAndCount($this$commonToByteString.length, offset, byteCount2);
        return new ByteString(ArraysKt.copyOfRange($this$commonToByteString, offset, offset + byteCount2));
    }

    public static final ByteString commonEncodeUtf8(String $this$commonEncodeUtf8) {
        Intrinsics.checkNotNullParameter($this$commonEncodeUtf8, "<this>");
        ByteString byteString = new ByteString(_JvmPlatformKt.asUtf8ToByteArray($this$commonEncodeUtf8));
        byteString.setUtf8$okio($this$commonEncodeUtf8);
        return byteString;
    }

    public static final ByteString commonDecodeBase64(String $this$commonDecodeBase64) {
        Intrinsics.checkNotNullParameter($this$commonDecodeBase64, "<this>");
        byte[] decoded = _Base64Kt.decodeBase64ToArray($this$commonDecodeBase64);
        if (decoded != null) {
            return new ByteString(decoded);
        }
        return null;
    }

    public static final ByteString commonDecodeHex(String $this$commonDecodeHex) {
        Intrinsics.checkNotNullParameter($this$commonDecodeHex, "<this>");
        if (!($this$commonDecodeHex.length() % 2 == 0)) {
            throw new IllegalArgumentException(("Unexpected hex string: " + $this$commonDecodeHex).toString());
        }
        byte[] result = new byte[$this$commonDecodeHex.length() / 2];
        int length = result.length;
        for (int i = 0; i < length; i++) {
            int d1 = decodeHexDigit($this$commonDecodeHex.charAt(i * 2)) << 4;
            int d2 = decodeHexDigit($this$commonDecodeHex.charAt((i * 2) + 1));
            result[i] = (byte) (d1 + d2);
        }
        return new ByteString(result);
    }

    public static final void commonWrite(ByteString $this$commonWrite, Buffer buffer, int offset, int byteCount) {
        Intrinsics.checkNotNullParameter($this$commonWrite, "<this>");
        Intrinsics.checkNotNullParameter(buffer, "buffer");
        buffer.write($this$commonWrite.getData(), offset, byteCount);
    }

    public static final int decodeHexDigit(char c) {
        if ('0' <= c && c < ':') {
            return c - '0';
        }
        if ('a' <= c && c < 'g') {
            return (c - 'a') + 10;
        }
        if ('A' <= c && c < 'G') {
            return (c - 'A') + 10;
        }
        throw new IllegalArgumentException("Unexpected hex digit: " + c);
    }

    public static final String commonToString(ByteString $this$commonToString) {
        Intrinsics.checkNotNullParameter($this$commonToString, "<this>");
        if ($this$commonToString.getData().length == 0) {
            return "[size=0]";
        }
        int i = codePointIndexToCharIndex($this$commonToString.getData(), 64);
        if (i == -1) {
            if ($this$commonToString.getData().length <= 64) {
                return "[hex=" + $this$commonToString.hex() + ']';
            }
            StringBuilder append = new StringBuilder().append("[size=").append($this$commonToString.getData().length).append(" hex=");
            ByteString $this$commonSubstring$iv = $this$commonToString;
            int endIndex$iv = _UtilKt.resolveDefaultParameter($this$commonSubstring$iv, 64);
            if (!(endIndex$iv <= $this$commonSubstring$iv.getData().length)) {
                throw new IllegalArgumentException(("endIndex > length(" + $this$commonSubstring$iv.getData().length + ')').toString());
            }
            int subLen$iv = endIndex$iv - 0;
            if (!(subLen$iv >= 0)) {
                throw new IllegalArgumentException("endIndex < beginIndex".toString());
            }
            if (endIndex$iv != $this$commonSubstring$iv.getData().length) {
                $this$commonSubstring$iv = new ByteString(ArraysKt.copyOfRange($this$commonSubstring$iv.getData(), 0, endIndex$iv));
            }
            return append.append($this$commonSubstring$iv.hex()).append("…]").toString();
        }
        String text = $this$commonToString.utf8();
        String substring = text.substring(0, i);
        Intrinsics.checkNotNullExpressionValue(substring, "this as java.lang.String…ing(startIndex, endIndex)");
        String safeText = StringsKt.replace$default(StringsKt.replace$default(StringsKt.replace$default(substring, "\\", "\\\\", false, 4, (Object) null), "\n", "\\n", false, 4, (Object) null), "\r", "\\r", false, 4, (Object) null);
        if (i < text.length()) {
            return "[size=" + $this$commonToString.getData().length + " text=" + safeText + "…]";
        }
        return "[text=" + safeText + ']';
    }

    /* JADX WARN: Code restructure failed: missing block: B:129:0x055b, code lost:            if ((127(0x7f, float:1.78E-43) <= 65533(0xfffd, float:9.1831E-41) && 65533(0xfffd, float:9.1831E-41) < 160(0xa0, float:2.24E-43)) != false) goto L1391;     */
    /* JADX WARN: Code restructure failed: missing block: B:168:0x05c0, code lost:            if ((127(0x7f, float:1.78E-43) <= 65533(0xfffd, float:9.1831E-41) && 65533(0xfffd, float:9.1831E-41) < 160(0xa0, float:2.24E-43)) != false) goto L1428;     */
    /* JADX WARN: Code restructure failed: missing block: B:207:0x0628, code lost:            if ((127(0x7f, float:1.78E-43) <= 65533(0xfffd, float:9.1831E-41) && 65533(0xfffd, float:9.1831E-41) < 160(0xa0, float:2.24E-43)) != false) goto L1466;     */
    /* JADX WARN: Code restructure failed: missing block: B:244:0x0692, code lost:            if ((127(0x7f, float:1.78E-43) <= 65533(0xfffd, float:9.1831E-41) && 65533(0xfffd, float:9.1831E-41) < 160(0xa0, float:2.24E-43)) != false) goto L1500;     */
    /* JADX WARN: Code restructure failed: missing block: B:286:0x06f0, code lost:            if ((127(0x7f, float:1.78E-43) <= 65533(0xfffd, float:9.1831E-41) && 65533(0xfffd, float:9.1831E-41) < 160(0xa0, float:2.24E-43)) != false) goto L1539;     */
    /* JADX WARN: Code restructure failed: missing block: B:297:0x06ff, code lost:            if (65533(0xfffd, float:9.1831E-41) < 65536(0x10000, float:9.1835E-41)) goto L1547;     */
    /* JADX WARN: Code restructure failed: missing block: B:298:0x0701, code lost:            r16 = 1;     */
    /* JADX WARN: Code restructure failed: missing block: B:299:0x0703, code lost:            r1 = r1 + r16;     */
    /* JADX WARN: Code restructure failed: missing block: B:323:0x073f, code lost:            if ((127(0x7f, float:1.78E-43) <= 65533(0xfffd, float:9.1831E-41) && 65533(0xfffd, float:9.1831E-41) < 160(0xa0, float:2.24E-43)) != false) goto L1573;     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x07cd, code lost:            if ((127(0x7f, float:1.78E-43) <= 65533(0xfffd, float:9.1831E-41) && 65533(0xfffd, float:9.1831E-41) < 160(0xa0, float:2.24E-43)) != false) goto L1636;     */
    /* JADX WARN: Code restructure failed: missing block: B:334:0x074e, code lost:            if (65533(0xfffd, float:9.1831E-41) < 65536(0x10000, float:9.1835E-41)) goto L1547;     */
    /* JADX WARN: Code restructure failed: missing block: B:356:0x0782, code lost:            if ((127 <= r3 && r3 < 160) != false) goto L1604;     */
    /* JADX WARN: Code restructure failed: missing block: B:367:0x0791, code lost:            if (r3 < 65536) goto L1547;     */
    /* JADX WARN: Code restructure failed: missing block: B:445:0x02ee, code lost:            if ((127(0x7f, float:1.78E-43) <= 65533(0xfffd, float:9.1831E-41) && 65533(0xfffd, float:9.1831E-41) < 160(0xa0, float:2.24E-43)) != false) goto L1155;     */
    /* JADX WARN: Code restructure failed: missing block: B:484:0x0355, code lost:            if ((127(0x7f, float:1.78E-43) <= 65533(0xfffd, float:9.1831E-41) && 65533(0xfffd, float:9.1831E-41) < 160(0xa0, float:2.24E-43)) != false) goto L1192;     */
    /* JADX WARN: Code restructure failed: missing block: B:521:0x03ba, code lost:            if ((127(0x7f, float:1.78E-43) <= 65533(0xfffd, float:9.1831E-41) && 65533(0xfffd, float:9.1831E-41) < 160(0xa0, float:2.24E-43)) != false) goto L1227;     */
    /* JADX WARN: Code restructure failed: missing block: B:563:0x0418, code lost:            if ((127(0x7f, float:1.78E-43) <= 65533(0xfffd, float:9.1831E-41) && 65533(0xfffd, float:9.1831E-41) < 160(0xa0, float:2.24E-43)) != false) goto L1266;     */
    /* JADX WARN: Code restructure failed: missing block: B:574:0x0427, code lost:            if (65533(0xfffd, float:9.1831E-41) < 65536(0x10000, float:9.1835E-41)) goto L1305;     */
    /* JADX WARN: Code restructure failed: missing block: B:575:0x046e, code lost:            r1 = r1 + r16;     */
    /* JADX WARN: Code restructure failed: missing block: B:576:0x046c, code lost:            r16 = 1;     */
    /* JADX WARN: Code restructure failed: missing block: B:598:0x045b, code lost:            if ((127 <= r10 && r10 < 160) != false) goto L1297;     */
    /* JADX WARN: Code restructure failed: missing block: B:609:0x046a, code lost:            if (r10 < 65536) goto L1305;     */
    /* JADX WARN: Code restructure failed: missing block: B:635:0x00ef, code lost:            if ((127(0x7f, float:1.78E-43) <= 65533(0xfffd, float:9.1831E-41) && 65533(0xfffd, float:9.1831E-41) < 160(0xa0, float:2.24E-43)) != false) goto L965;     */
    /* JADX WARN: Code restructure failed: missing block: B:675:0x0160, code lost:            if ((127(0x7f, float:1.78E-43) <= 65533(0xfffd, float:9.1831E-41) && 65533(0xfffd, float:9.1831E-41) < 160(0xa0, float:2.24E-43)) != false) goto L1002;     */
    /* JADX WARN: Code restructure failed: missing block: B:712:0x01c0, code lost:            if ((127(0x7f, float:1.78E-43) <= 65533(0xfffd, float:9.1831E-41) && 65533(0xfffd, float:9.1831E-41) < 160(0xa0, float:2.24E-43)) != false) goto L1036;     */
    /* JADX WARN: Code restructure failed: missing block: B:748:0x0209, code lost:            if ((127 <= r3 && r3 < 160) != false) goto L1069;     */
    /* JADX WARN: Removed duplicated region for block: B:124:0x054e  */
    /* JADX WARN: Removed duplicated region for block: B:131:0x0566 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:163:0x05b3  */
    /* JADX WARN: Removed duplicated region for block: B:170:0x05cb A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:202:0x061b  */
    /* JADX WARN: Removed duplicated region for block: B:209:0x0633 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:239:0x0685  */
    /* JADX WARN: Removed duplicated region for block: B:246:0x069d A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:281:0x06e3  */
    /* JADX WARN: Removed duplicated region for block: B:288:0x06fb A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:318:0x0732  */
    /* JADX WARN: Removed duplicated region for block: B:325:0x074a A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:351:0x0775  */
    /* JADX WARN: Removed duplicated region for block: B:358:0x078d A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:399:0x0276 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:440:0x02e1  */
    /* JADX WARN: Removed duplicated region for block: B:447:0x02f9 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:479:0x0348  */
    /* JADX WARN: Removed duplicated region for block: B:486:0x0360 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:516:0x03ad  */
    /* JADX WARN: Removed duplicated region for block: B:523:0x03c5 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:558:0x040b  */
    /* JADX WARN: Removed duplicated region for block: B:565:0x0423 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:593:0x044e  */
    /* JADX WARN: Removed duplicated region for block: B:600:0x0466 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:670:0x0153  */
    /* JADX WARN: Removed duplicated region for block: B:677:0x016b A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:707:0x01b3  */
    /* JADX WARN: Removed duplicated region for block: B:714:0x01cb A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:743:0x01fc  */
    /* JADX WARN: Removed duplicated region for block: B:750:0x0214 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:75:0x04c7 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:784:0x0059 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:813:0x00a3 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static final int codePointIndexToCharIndex(byte[] r30, int r31) {
        /*
            Method dump skipped, instructions count: 2030
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.internal._ByteStringKt.codePointIndexToCharIndex(byte[], int):int");
    }
}
