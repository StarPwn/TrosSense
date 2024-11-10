package io.netty.util;

import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.InternalThreadLocalMap;
import io.netty.util.internal.MathUtil;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/* loaded from: classes4.dex */
public final class AsciiString implements CharSequence, Comparable<CharSequence> {
    public static final int INDEX_NOT_FOUND = -1;
    private static final char MAX_CHAR_VALUE = 255;
    private int hash;
    private final int length;
    private final int offset;
    private String string;
    private final byte[] value;
    public static final AsciiString EMPTY_STRING = cached("");
    public static final HashingStrategy<CharSequence> CASE_INSENSITIVE_HASHER = new HashingStrategy<CharSequence>() { // from class: io.netty.util.AsciiString.1
        @Override // io.netty.util.HashingStrategy
        public int hashCode(CharSequence o) {
            return AsciiString.hashCode(o);
        }

        @Override // io.netty.util.HashingStrategy
        public boolean equals(CharSequence a, CharSequence b) {
            return AsciiString.contentEqualsIgnoreCase(a, b);
        }
    };
    public static final HashingStrategy<CharSequence> CASE_SENSITIVE_HASHER = new HashingStrategy<CharSequence>() { // from class: io.netty.util.AsciiString.2
        @Override // io.netty.util.HashingStrategy
        public int hashCode(CharSequence o) {
            return AsciiString.hashCode(o);
        }

        @Override // io.netty.util.HashingStrategy
        public boolean equals(CharSequence a, CharSequence b) {
            return AsciiString.contentEquals(a, b);
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public interface CharEqualityComparator {
        boolean equals(char c, char c2);
    }

    public AsciiString(byte[] value) {
        this(value, true);
    }

    public AsciiString(byte[] value, boolean copy) {
        this(value, 0, value.length, copy);
    }

    public AsciiString(byte[] value, int start, int length, boolean copy) {
        if (copy) {
            byte[] rangedCopy = new byte[length];
            System.arraycopy(value, start, rangedCopy, 0, rangedCopy.length);
            this.value = rangedCopy;
            this.offset = 0;
        } else {
            if (MathUtil.isOutOfBounds(start, length, value.length)) {
                throw new IndexOutOfBoundsException("expected: 0 <= start(" + start + ") <= start + length(" + length + ") <= value.length(" + value.length + ')');
            }
            this.value = value;
            this.offset = start;
        }
        this.length = length;
    }

    public AsciiString(ByteBuffer value) {
        this(value, true);
    }

    public AsciiString(ByteBuffer value, boolean copy) {
        this(value, value.position(), value.remaining(), copy);
    }

    public AsciiString(ByteBuffer value, int start, int length, boolean copy) {
        if (MathUtil.isOutOfBounds(start, length, value.capacity())) {
            throw new IndexOutOfBoundsException("expected: 0 <= start(" + start + ") <= start + length(" + length + ") <= value.capacity(" + value.capacity() + ')');
        }
        if (value.hasArray()) {
            if (copy) {
                int bufferOffset = value.arrayOffset() + start;
                this.value = Arrays.copyOfRange(value.array(), bufferOffset, bufferOffset + length);
                this.offset = 0;
            } else {
                this.value = value.array();
                this.offset = start;
            }
        } else {
            this.value = PlatformDependent.allocateUninitializedArray(length);
            int oldPos = value.position();
            value.get(this.value, 0, length);
            value.position(oldPos);
            this.offset = 0;
        }
        this.length = length;
    }

    public AsciiString(char[] value) {
        this(value, 0, value.length);
    }

    public AsciiString(char[] value, int start, int length) {
        if (MathUtil.isOutOfBounds(start, length, value.length)) {
            throw new IndexOutOfBoundsException("expected: 0 <= start(" + start + ") <= start + length(" + length + ") <= value.length(" + value.length + ')');
        }
        this.value = PlatformDependent.allocateUninitializedArray(length);
        int i = 0;
        int j = start;
        while (i < length) {
            this.value[i] = c2b(value[j]);
            i++;
            j++;
        }
        this.offset = 0;
        this.length = length;
    }

    public AsciiString(char[] value, Charset charset) {
        this(value, charset, 0, value.length);
    }

    public AsciiString(char[] value, Charset charset, int start, int length) {
        CharBuffer cbuf = CharBuffer.wrap(value, start, length);
        CharsetEncoder encoder = CharsetUtil.encoder(charset);
        ByteBuffer nativeBuffer = ByteBuffer.allocate((int) (encoder.maxBytesPerChar() * length));
        encoder.encode(cbuf, nativeBuffer, true);
        int bufferOffset = nativeBuffer.arrayOffset();
        this.value = Arrays.copyOfRange(nativeBuffer.array(), bufferOffset, nativeBuffer.position() + bufferOffset);
        this.offset = 0;
        this.length = this.value.length;
    }

    public AsciiString(CharSequence value) {
        this(value, 0, value.length());
    }

    public AsciiString(CharSequence value, int start, int length) {
        if (MathUtil.isOutOfBounds(start, length, value.length())) {
            throw new IndexOutOfBoundsException("expected: 0 <= start(" + start + ") <= start + length(" + length + ") <= value.length(" + value.length() + ')');
        }
        this.value = PlatformDependent.allocateUninitializedArray(length);
        int i = 0;
        int j = start;
        while (i < length) {
            this.value[i] = c2b(value.charAt(j));
            i++;
            j++;
        }
        this.offset = 0;
        this.length = length;
    }

    public AsciiString(CharSequence value, Charset charset) {
        this(value, charset, 0, value.length());
    }

    public AsciiString(CharSequence value, Charset charset, int start, int length) {
        CharBuffer cbuf = CharBuffer.wrap(value, start, start + length);
        CharsetEncoder encoder = CharsetUtil.encoder(charset);
        ByteBuffer nativeBuffer = ByteBuffer.allocate((int) (encoder.maxBytesPerChar() * length));
        encoder.encode(cbuf, nativeBuffer, true);
        int offset = nativeBuffer.arrayOffset();
        this.value = Arrays.copyOfRange(nativeBuffer.array(), offset, nativeBuffer.position() + offset);
        this.offset = 0;
        this.length = this.value.length;
    }

    public int forEachByte(ByteProcessor visitor) throws Exception {
        return forEachByte0(0, length(), visitor);
    }

    public int forEachByte(int index, int length, ByteProcessor visitor) throws Exception {
        if (MathUtil.isOutOfBounds(index, length, length())) {
            throw new IndexOutOfBoundsException("expected: 0 <= index(" + index + ") <= start + length(" + length + ") <= length(" + length() + ')');
        }
        return forEachByte0(index, length, visitor);
    }

    private int forEachByte0(int index, int length, ByteProcessor visitor) throws Exception {
        int len = this.offset + index + length;
        for (int i = this.offset + index; i < len; i++) {
            if (!visitor.process(this.value[i])) {
                return i - this.offset;
            }
        }
        return -1;
    }

    public int forEachByteDesc(ByteProcessor visitor) throws Exception {
        return forEachByteDesc0(0, length(), visitor);
    }

    public int forEachByteDesc(int index, int length, ByteProcessor visitor) throws Exception {
        if (MathUtil.isOutOfBounds(index, length, length())) {
            throw new IndexOutOfBoundsException("expected: 0 <= index(" + index + ") <= start + length(" + length + ") <= length(" + length() + ')');
        }
        return forEachByteDesc0(index, length, visitor);
    }

    private int forEachByteDesc0(int index, int length, ByteProcessor visitor) throws Exception {
        int end = this.offset + index;
        for (int i = ((this.offset + index) + length) - 1; i >= end; i--) {
            if (!visitor.process(this.value[i])) {
                return i - this.offset;
            }
        }
        return -1;
    }

    public byte byteAt(int index) {
        if (index < 0 || index >= this.length) {
            throw new IndexOutOfBoundsException("index: " + index + " must be in the range [0," + this.length + ")");
        }
        if (PlatformDependent.hasUnsafe()) {
            return PlatformDependent.getByte(this.value, this.offset + index);
        }
        return this.value[this.offset + index];
    }

    public boolean isEmpty() {
        return this.length == 0;
    }

    @Override // java.lang.CharSequence
    public int length() {
        return this.length;
    }

    public void arrayChanged() {
        this.string = null;
        this.hash = 0;
    }

    public byte[] array() {
        return this.value;
    }

    public int arrayOffset() {
        return this.offset;
    }

    public boolean isEntireArrayUsed() {
        return this.offset == 0 && this.length == this.value.length;
    }

    public byte[] toByteArray() {
        return toByteArray(0, length());
    }

    public byte[] toByteArray(int start, int end) {
        return Arrays.copyOfRange(this.value, this.offset + start, this.offset + end);
    }

    public void copy(int srcIdx, byte[] dst, int dstIdx, int length) {
        if (MathUtil.isOutOfBounds(srcIdx, length, length())) {
            throw new IndexOutOfBoundsException("expected: 0 <= srcIdx(" + srcIdx + ") <= srcIdx + length(" + length + ") <= srcLen(" + length() + ')');
        }
        System.arraycopy(this.value, this.offset + srcIdx, ObjectUtil.checkNotNull(dst, "dst"), dstIdx, length);
    }

    @Override // java.lang.CharSequence
    public char charAt(int index) {
        return b2c(byteAt(index));
    }

    public boolean contains(CharSequence cs) {
        return indexOf(cs) >= 0;
    }

    @Override // java.lang.Comparable
    public int compareTo(CharSequence string) {
        if (this == string) {
            return 0;
        }
        int length1 = length();
        int length2 = string.length();
        int minLength = Math.min(length1, length2);
        int i = 0;
        int j = arrayOffset();
        while (i < minLength) {
            int result = b2c(this.value[j]) - string.charAt(i);
            if (result == 0) {
                i++;
                j++;
            } else {
                return result;
            }
        }
        int i2 = length1 - length2;
        return i2;
    }

    public AsciiString concat(CharSequence string) {
        int thisLen = length();
        int thatLen = string.length();
        if (thatLen == 0) {
            return this;
        }
        if (string instanceof AsciiString) {
            AsciiString that = (AsciiString) string;
            if (isEmpty()) {
                return that;
            }
            byte[] newValue = PlatformDependent.allocateUninitializedArray(thisLen + thatLen);
            System.arraycopy(this.value, arrayOffset(), newValue, 0, thisLen);
            System.arraycopy(that.value, that.arrayOffset(), newValue, thisLen, thatLen);
            return new AsciiString(newValue, false);
        }
        if (isEmpty()) {
            return new AsciiString(string);
        }
        byte[] newValue2 = PlatformDependent.allocateUninitializedArray(thisLen + thatLen);
        System.arraycopy(this.value, arrayOffset(), newValue2, 0, thisLen);
        int i = thisLen;
        int j = 0;
        while (i < newValue2.length) {
            newValue2[i] = c2b(string.charAt(j));
            i++;
            j++;
        }
        return new AsciiString(newValue2, false);
    }

    public boolean endsWith(CharSequence suffix) {
        int suffixLen = suffix.length();
        return regionMatches(length() - suffixLen, suffix, 0, suffixLen);
    }

    public boolean contentEqualsIgnoreCase(CharSequence string) {
        if (this == string) {
            return true;
        }
        if (string == null || string.length() != length()) {
            return false;
        }
        if (string instanceof AsciiString) {
            AsciiString other = (AsciiString) string;
            byte[] value = this.value;
            if (this.offset == 0 && other.offset == 0 && this.length == value.length) {
                byte[] otherValue = other.value;
                for (int i = 0; i < value.length; i++) {
                    if (!equalsIgnoreCase(value[i], otherValue[i])) {
                        return false;
                    }
                }
                return true;
            }
            return misalignedEqualsIgnoreCase(other);
        }
        byte[] value2 = this.value;
        int i2 = this.offset;
        for (int j = 0; j < string.length(); j++) {
            if (!equalsIgnoreCase(b2c(value2[i2]), string.charAt(j))) {
                return false;
            }
            i2++;
        }
        return true;
    }

    private boolean misalignedEqualsIgnoreCase(AsciiString other) {
        byte[] value = this.value;
        byte[] otherValue = other.value;
        int i = this.offset;
        int j = other.offset;
        int end = this.offset + this.length;
        while (i < end) {
            if (equalsIgnoreCase(value[i], otherValue[j])) {
                i++;
                j++;
            } else {
                return false;
            }
        }
        return true;
    }

    public char[] toCharArray() {
        return toCharArray(0, length());
    }

    public char[] toCharArray(int start, int end) {
        int length = end - start;
        if (length == 0) {
            return EmptyArrays.EMPTY_CHARS;
        }
        if (MathUtil.isOutOfBounds(start, length, length())) {
            throw new IndexOutOfBoundsException("expected: 0 <= start(" + start + ") <= srcIdx + length(" + length + ") <= srcLen(" + length() + ')');
        }
        char[] buffer = new char[length];
        int i = 0;
        int j = arrayOffset() + start;
        while (i < length) {
            buffer[i] = b2c(this.value[j]);
            i++;
            j++;
        }
        return buffer;
    }

    public void copy(int srcIdx, char[] dst, int dstIdx, int length) {
        ObjectUtil.checkNotNull(dst, "dst");
        if (MathUtil.isOutOfBounds(srcIdx, length, length())) {
            throw new IndexOutOfBoundsException("expected: 0 <= srcIdx(" + srcIdx + ") <= srcIdx + length(" + length + ") <= srcLen(" + length() + ')');
        }
        int dstEnd = dstIdx + length;
        int i = dstIdx;
        int j = arrayOffset() + srcIdx;
        while (i < dstEnd) {
            dst[i] = b2c(this.value[j]);
            i++;
            j++;
        }
    }

    public AsciiString subSequence(int start) {
        return subSequence(start, length());
    }

    @Override // java.lang.CharSequence
    public AsciiString subSequence(int start, int end) {
        return subSequence(start, end, true);
    }

    public AsciiString subSequence(int start, int end, boolean copy) {
        if (MathUtil.isOutOfBounds(start, end - start, length())) {
            throw new IndexOutOfBoundsException("expected: 0 <= start(" + start + ") <= end (" + end + ") <= length(" + length() + ')');
        }
        if (start == 0 && end == length()) {
            return this;
        }
        if (end == start) {
            return EMPTY_STRING;
        }
        return new AsciiString(this.value, this.offset + start, end - start, copy);
    }

    public int indexOf(CharSequence string) {
        return indexOf(string, 0);
    }

    public int indexOf(CharSequence subString, int start) {
        char firstChar;
        int subCount = subString.length();
        if (start < 0) {
            start = 0;
        }
        if (subCount <= 0) {
            return start < this.length ? start : this.length;
        }
        if (subCount > this.length - start || (firstChar = subString.charAt(0)) > 255) {
            return -1;
        }
        byte firstCharAsByte = c2b0(firstChar);
        int len = (this.offset + this.length) - subCount;
        for (int i = this.offset + start; i <= len; i++) {
            if (this.value[i] == firstCharAsByte) {
                int o1 = i;
                int o2 = 0;
                do {
                    o2++;
                    if (o2 >= subCount) {
                        break;
                    }
                    o1++;
                } while (b2c(this.value[o1]) == subString.charAt(o2));
                if (o2 == subCount) {
                    return i - this.offset;
                }
            }
        }
        return -1;
    }

    public int indexOf(char ch, int start) {
        if (ch > 255) {
            return -1;
        }
        if (start < 0) {
            start = 0;
        }
        byte chAsByte = c2b0(ch);
        int len = this.offset + this.length;
        for (int i = this.offset + start; i < len; i++) {
            if (this.value[i] == chAsByte) {
                return i - this.offset;
            }
        }
        return -1;
    }

    public int lastIndexOf(CharSequence string) {
        return lastIndexOf(string, this.length);
    }

    public int lastIndexOf(CharSequence subString, int start) {
        int subCount = subString.length();
        int start2 = Math.min(start, this.length - subCount);
        if (start2 < 0) {
            return -1;
        }
        if (subCount == 0) {
            return start2;
        }
        char firstChar = subString.charAt(0);
        if (firstChar > 255) {
            return -1;
        }
        byte firstCharAsByte = c2b0(firstChar);
        for (int i = this.offset + start2; i >= this.offset; i--) {
            if (this.value[i] == firstCharAsByte) {
                int o1 = i;
                int o2 = 0;
                do {
                    o2++;
                    if (o2 >= subCount) {
                        break;
                    }
                    o1++;
                } while (b2c(this.value[o1]) == subString.charAt(o2));
                if (o2 == subCount) {
                    return i - this.offset;
                }
            }
        }
        return -1;
    }

    public boolean regionMatches(int thisStart, CharSequence string, int start, int length) {
        ObjectUtil.checkNotNull(string, "string");
        if (start < 0 || string.length() - start < length) {
            return false;
        }
        int thisLen = length();
        if (thisStart < 0 || thisLen - thisStart < length) {
            return false;
        }
        if (length <= 0) {
            return true;
        }
        int thatEnd = start + length;
        int i = start;
        int j = arrayOffset() + thisStart;
        while (i < thatEnd) {
            if (b2c(this.value[j]) != string.charAt(i)) {
                return false;
            }
            i++;
            j++;
        }
        return true;
    }

    public boolean regionMatches(boolean ignoreCase, int thisStart, CharSequence string, int start, int length) {
        if (!ignoreCase) {
            return regionMatches(thisStart, string, start, length);
        }
        ObjectUtil.checkNotNull(string, "string");
        int thisLen = length();
        if (thisStart < 0 || length > thisLen - thisStart || start < 0 || length > string.length() - start) {
            return false;
        }
        int thisStart2 = thisStart + arrayOffset();
        int thisEnd = thisStart2 + length;
        while (thisStart2 < thisEnd) {
            int thisStart3 = thisStart2 + 1;
            int start2 = start + 1;
            if (!equalsIgnoreCase(b2c(this.value[thisStart2]), string.charAt(start))) {
                return false;
            }
            start = start2;
            thisStart2 = thisStart3;
        }
        return true;
    }

    public AsciiString replace(char oldChar, char newChar) {
        if (oldChar > 255) {
            return this;
        }
        byte oldCharAsByte = c2b0(oldChar);
        byte newCharAsByte = c2b(newChar);
        int len = this.offset + this.length;
        for (int i = this.offset; i < len; i++) {
            if (this.value[i] == oldCharAsByte) {
                byte[] buffer = PlatformDependent.allocateUninitializedArray(length());
                System.arraycopy(this.value, this.offset, buffer, 0, i - this.offset);
                buffer[i - this.offset] = newCharAsByte;
                for (int i2 = i + 1; i2 < len; i2++) {
                    byte oldValue = this.value[i2];
                    buffer[i2 - this.offset] = oldValue != oldCharAsByte ? oldValue : newCharAsByte;
                }
                return new AsciiString(buffer, false);
            }
        }
        return this;
    }

    public boolean startsWith(CharSequence prefix) {
        return startsWith(prefix, 0);
    }

    public boolean startsWith(CharSequence prefix, int start) {
        return regionMatches(start, prefix, 0, prefix.length());
    }

    public AsciiString toLowerCase() {
        return AsciiStringUtil.toLowerCase(this);
    }

    public AsciiString toUpperCase() {
        return AsciiStringUtil.toUpperCase(this);
    }

    public static CharSequence trim(CharSequence c) {
        if (c instanceof AsciiString) {
            return ((AsciiString) c).trim();
        }
        if (c instanceof String) {
            return ((String) c).trim();
        }
        int start = 0;
        int last = c.length() - 1;
        int end = last;
        while (start <= end && c.charAt(start) <= ' ') {
            start++;
        }
        while (end >= start && c.charAt(end) <= ' ') {
            end--;
        }
        if (start == 0 && end == last) {
            return c;
        }
        return c.subSequence(start, end);
    }

    public AsciiString trim() {
        int start = arrayOffset();
        int last = (arrayOffset() + length()) - 1;
        int end = last;
        while (start <= end && this.value[start] <= 32) {
            start++;
        }
        while (end >= start && this.value[end] <= 32) {
            end--;
        }
        if (start == 0 && end == last) {
            return this;
        }
        return new AsciiString(this.value, start, (end - start) + 1, false);
    }

    public boolean contentEquals(CharSequence a) {
        if (this == a) {
            return true;
        }
        if (a == null || a.length() != length()) {
            return false;
        }
        if (a instanceof AsciiString) {
            return equals(a);
        }
        int i = arrayOffset();
        for (int j = 0; j < a.length(); j++) {
            if (b2c(this.value[i]) != a.charAt(j)) {
                return false;
            }
            i++;
        }
        return true;
    }

    public boolean matches(String expr) {
        return Pattern.matches(expr, this);
    }

    public AsciiString[] split(String expr, int max) {
        return toAsciiStringArray(Pattern.compile(expr).split(this, max));
    }

    public AsciiString[] split(char delim) {
        List<AsciiString> res = InternalThreadLocalMap.get().arrayList();
        int start = 0;
        int length = length();
        for (int i = 0; i < length; i++) {
            if (charAt(i) == delim) {
                if (start == i) {
                    res.add(EMPTY_STRING);
                } else {
                    res.add(new AsciiString(this.value, arrayOffset() + start, i - start, false));
                }
                start = i + 1;
            }
        }
        if (start == 0) {
            res.add(this);
        } else if (start != length) {
            res.add(new AsciiString(this.value, arrayOffset() + start, length - start, false));
        } else {
            for (int i2 = res.size() - 1; i2 >= 0 && res.get(i2).isEmpty(); i2--) {
                res.remove(i2);
            }
        }
        return (AsciiString[]) res.toArray(EmptyArrays.EMPTY_ASCII_STRINGS);
    }

    public int hashCode() {
        int h = this.hash;
        if (h == 0) {
            int h2 = PlatformDependent.hashCodeAscii(this.value, this.offset, this.length);
            this.hash = h2;
            return h2;
        }
        return h;
    }

    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != AsciiString.class) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        AsciiString other = (AsciiString) obj;
        if (length() != other.length() || hashCode() != other.hashCode() || !PlatformDependent.equals(array(), arrayOffset(), other.array(), other.arrayOffset(), length())) {
            return false;
        }
        return true;
    }

    @Override // java.lang.CharSequence
    public String toString() {
        String cache = this.string;
        if (cache == null) {
            String cache2 = toString(0);
            this.string = cache2;
            return cache2;
        }
        return cache;
    }

    public String toString(int start) {
        return toString(start, length());
    }

    public String toString(int start, int end) {
        int length = end - start;
        if (length == 0) {
            return "";
        }
        if (MathUtil.isOutOfBounds(start, length, length())) {
            throw new IndexOutOfBoundsException("expected: 0 <= start(" + start + ") <= srcIdx + length(" + length + ") <= srcLen(" + length() + ')');
        }
        String str = new String(this.value, 0, this.offset + start, length);
        return str;
    }

    public boolean parseBoolean() {
        return this.length >= 1 && this.value[this.offset] != 0;
    }

    public char parseChar() {
        return parseChar(0);
    }

    public char parseChar(int start) {
        if (start + 1 >= length()) {
            throw new IndexOutOfBoundsException("2 bytes required to convert to character. index " + start + " would go out of bounds.");
        }
        int startWithOffset = this.offset + start;
        return (char) ((b2c(this.value[startWithOffset]) << '\b') | b2c(this.value[startWithOffset + 1]));
    }

    public short parseShort() {
        return parseShort(0, length(), 10);
    }

    public short parseShort(int radix) {
        return parseShort(0, length(), radix);
    }

    public short parseShort(int start, int end) {
        return parseShort(start, end, 10);
    }

    public short parseShort(int start, int end, int radix) {
        int intValue = parseInt(start, end, radix);
        short result = (short) intValue;
        if (result != intValue) {
            throw new NumberFormatException(subSequence(start, end, false).toString());
        }
        return result;
    }

    public int parseInt() {
        return parseInt(0, length(), 10);
    }

    public int parseInt(int radix) {
        return parseInt(0, length(), radix);
    }

    public int parseInt(int start, int end) {
        return parseInt(start, end, 10);
    }

    public int parseInt(int start, int end, int radix) {
        if (radix < 2 || radix > 36) {
            throw new NumberFormatException();
        }
        if (start == end) {
            throw new NumberFormatException();
        }
        int i = start;
        boolean negative = byteAt(i) == 45;
        if (negative && (i = i + 1) == end) {
            throw new NumberFormatException(subSequence(start, end, false).toString());
        }
        return parseInt(i, end, radix, negative);
    }

    private int parseInt(int start, int end, int radix, boolean negative) {
        int max = Integer.MIN_VALUE / radix;
        int result = 0;
        int digit = start;
        while (digit < end) {
            int currOffset = digit + 1;
            int digit2 = Character.digit((char) (this.value[digit + this.offset] & 255), radix);
            if (digit2 == -1) {
                throw new NumberFormatException(subSequence(start, end, false).toString());
            }
            if (max > result) {
                throw new NumberFormatException(subSequence(start, end, false).toString());
            }
            int next = (result * radix) - digit2;
            if (next > result) {
                throw new NumberFormatException(subSequence(start, end, false).toString());
            }
            result = next;
            digit = currOffset;
        }
        if (!negative && (result = -result) < 0) {
            throw new NumberFormatException(subSequence(start, end, false).toString());
        }
        return result;
    }

    public long parseLong() {
        return parseLong(0, length(), 10);
    }

    public long parseLong(int radix) {
        return parseLong(0, length(), radix);
    }

    public long parseLong(int start, int end) {
        return parseLong(start, end, 10);
    }

    public long parseLong(int start, int end, int radix) {
        if (radix < 2 || radix > 36) {
            throw new NumberFormatException();
        }
        if (start == end) {
            throw new NumberFormatException();
        }
        int i = start;
        boolean negative = byteAt(i) == 45;
        if (negative && (i = i + 1) == end) {
            throw new NumberFormatException(subSequence(start, end, false).toString());
        }
        return parseLong(i, end, radix, negative);
    }

    private long parseLong(int start, int end, int radix, boolean negative) {
        long max = Long.MIN_VALUE / radix;
        long result = 0;
        int digit = start;
        while (digit < end) {
            int currOffset = digit + 1;
            int digit2 = Character.digit((char) (this.value[digit + this.offset] & 255), radix);
            if (digit2 == -1) {
                throw new NumberFormatException(subSequence(start, end, false).toString());
            }
            if (max > result) {
                throw new NumberFormatException(subSequence(start, end, false).toString());
            }
            long next = (radix * result) - digit2;
            if (next > result) {
                throw new NumberFormatException(subSequence(start, end, false).toString());
            }
            result = next;
            digit = currOffset;
        }
        if (!negative) {
            result = -result;
            if (result < 0) {
                throw new NumberFormatException(subSequence(start, end, false).toString());
            }
        }
        return result;
    }

    public float parseFloat() {
        return parseFloat(0, length());
    }

    public float parseFloat(int start, int end) {
        return Float.parseFloat(toString(start, end));
    }

    public double parseDouble() {
        return parseDouble(0, length());
    }

    public double parseDouble(int start, int end) {
        return Double.parseDouble(toString(start, end));
    }

    public static AsciiString of(CharSequence string) {
        return string instanceof AsciiString ? (AsciiString) string : new AsciiString(string);
    }

    public static AsciiString cached(String string) {
        AsciiString asciiString = new AsciiString(string);
        asciiString.string = string;
        return asciiString;
    }

    public static int hashCode(CharSequence value) {
        if (value == null) {
            return 0;
        }
        if (value instanceof AsciiString) {
            return value.hashCode();
        }
        return PlatformDependent.hashCodeAscii(value);
    }

    public static boolean contains(CharSequence a, CharSequence b) {
        return contains(a, b, DefaultCharEqualityComparator.INSTANCE);
    }

    public static boolean containsIgnoreCase(CharSequence a, CharSequence b) {
        return contains(a, b, AsciiCaseInsensitiveCharEqualityComparator.INSTANCE);
    }

    public static boolean contentEqualsIgnoreCase(CharSequence a, CharSequence b) {
        if (a == null || b == null) {
            return a == b;
        }
        if (a instanceof AsciiString) {
            return ((AsciiString) a).contentEqualsIgnoreCase(b);
        }
        if (b instanceof AsciiString) {
            return ((AsciiString) b).contentEqualsIgnoreCase(a);
        }
        if (a.length() != b.length()) {
            return false;
        }
        for (int i = 0; i < a.length(); i++) {
            if (!equalsIgnoreCase(a.charAt(i), b.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean containsContentEqualsIgnoreCase(Collection<CharSequence> collection, CharSequence value) {
        for (CharSequence v : collection) {
            if (contentEqualsIgnoreCase(value, v)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsAllContentEqualsIgnoreCase(Collection<CharSequence> a, Collection<CharSequence> b) {
        for (CharSequence v : b) {
            if (!containsContentEqualsIgnoreCase(a, v)) {
                return false;
            }
        }
        return true;
    }

    public static boolean contentEquals(CharSequence a, CharSequence b) {
        if (a == null || b == null) {
            return a == b;
        }
        if (a instanceof AsciiString) {
            return ((AsciiString) a).contentEquals(b);
        }
        if (b instanceof AsciiString) {
            return ((AsciiString) b).contentEquals(a);
        }
        if (a.length() != b.length()) {
            return false;
        }
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) != b.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    private static AsciiString[] toAsciiStringArray(String[] jdkResult) {
        AsciiString[] res = new AsciiString[jdkResult.length];
        for (int i = 0; i < jdkResult.length; i++) {
            res[i] = new AsciiString(jdkResult[i]);
        }
        return res;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class DefaultCharEqualityComparator implements CharEqualityComparator {
        static final DefaultCharEqualityComparator INSTANCE = new DefaultCharEqualityComparator();

        private DefaultCharEqualityComparator() {
        }

        @Override // io.netty.util.AsciiString.CharEqualityComparator
        public boolean equals(char a, char b) {
            return a == b;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class AsciiCaseInsensitiveCharEqualityComparator implements CharEqualityComparator {
        static final AsciiCaseInsensitiveCharEqualityComparator INSTANCE = new AsciiCaseInsensitiveCharEqualityComparator();

        private AsciiCaseInsensitiveCharEqualityComparator() {
        }

        @Override // io.netty.util.AsciiString.CharEqualityComparator
        public boolean equals(char a, char b) {
            return AsciiString.equalsIgnoreCase(a, b);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class GeneralCaseInsensitiveCharEqualityComparator implements CharEqualityComparator {
        static final GeneralCaseInsensitiveCharEqualityComparator INSTANCE = new GeneralCaseInsensitiveCharEqualityComparator();

        private GeneralCaseInsensitiveCharEqualityComparator() {
        }

        @Override // io.netty.util.AsciiString.CharEqualityComparator
        public boolean equals(char a, char b) {
            return Character.toUpperCase(a) == Character.toUpperCase(b) || Character.toLowerCase(a) == Character.toLowerCase(b);
        }
    }

    private static boolean contains(CharSequence a, CharSequence b, CharEqualityComparator cmp) {
        if (a == null || b == null || a.length() < b.length()) {
            return false;
        }
        if (b.length() == 0) {
            return true;
        }
        int bStart = 0;
        for (int i = 0; i < a.length(); i++) {
            if (cmp.equals(b.charAt(bStart), a.charAt(i))) {
                bStart++;
                if (bStart == b.length()) {
                    return true;
                }
            } else {
                if (a.length() - i < b.length()) {
                    return false;
                }
                bStart = 0;
            }
        }
        return false;
    }

    private static boolean regionMatchesCharSequences(CharSequence cs, int csStart, CharSequence string, int start, int length, CharEqualityComparator charEqualityComparator) {
        if (csStart < 0 || length > cs.length() - csStart || start < 0 || length > string.length() - start) {
            return false;
        }
        int csIndex = csStart;
        int csEnd = csIndex + length;
        int stringIndex = start;
        while (csIndex < csEnd) {
            int csIndex2 = csIndex + 1;
            char c1 = cs.charAt(csIndex);
            int stringIndex2 = stringIndex + 1;
            char c2 = string.charAt(stringIndex);
            if (!charEqualityComparator.equals(c1, c2)) {
                return false;
            }
            csIndex = csIndex2;
            stringIndex = stringIndex2;
        }
        return true;
    }

    public static boolean regionMatches(CharSequence cs, boolean ignoreCase, int csStart, CharSequence string, int start, int length) {
        if (cs == null || string == null) {
            return false;
        }
        if ((cs instanceof String) && (string instanceof String)) {
            return ((String) cs).regionMatches(ignoreCase, csStart, (String) string, start, length);
        }
        if (cs instanceof AsciiString) {
            return ((AsciiString) cs).regionMatches(ignoreCase, csStart, string, start, length);
        }
        return regionMatchesCharSequences(cs, csStart, string, start, length, ignoreCase ? GeneralCaseInsensitiveCharEqualityComparator.INSTANCE : DefaultCharEqualityComparator.INSTANCE);
    }

    public static boolean regionMatchesAscii(CharSequence cs, boolean ignoreCase, int csStart, CharSequence string, int start, int length) {
        if (cs == null || string == null) {
            return false;
        }
        if (!ignoreCase && (cs instanceof String) && (string instanceof String)) {
            return ((String) cs).regionMatches(false, csStart, (String) string, start, length);
        }
        if (cs instanceof AsciiString) {
            return ((AsciiString) cs).regionMatches(ignoreCase, csStart, string, start, length);
        }
        return regionMatchesCharSequences(cs, csStart, string, start, length, ignoreCase ? AsciiCaseInsensitiveCharEqualityComparator.INSTANCE : DefaultCharEqualityComparator.INSTANCE);
    }

    public static int indexOfIgnoreCase(CharSequence str, CharSequence searchStr, int startPos) {
        if (str == null || searchStr == null) {
            return -1;
        }
        if (startPos < 0) {
            startPos = 0;
        }
        int searchStrLen = searchStr.length();
        int endLimit = (str.length() - searchStrLen) + 1;
        if (startPos > endLimit) {
            return -1;
        }
        if (searchStrLen == 0) {
            return startPos;
        }
        for (int i = startPos; i < endLimit; i++) {
            if (regionMatches(str, true, i, searchStr, 0, searchStrLen)) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOfIgnoreCaseAscii(CharSequence str, CharSequence searchStr, int startPos) {
        if (str == null || searchStr == null) {
            return -1;
        }
        if (startPos < 0) {
            startPos = 0;
        }
        int searchStrLen = searchStr.length();
        int endLimit = (str.length() - searchStrLen) + 1;
        if (startPos > endLimit) {
            return -1;
        }
        if (searchStrLen == 0) {
            return startPos;
        }
        for (int i = startPos; i < endLimit; i++) {
            if (regionMatchesAscii(str, true, i, searchStr, 0, searchStrLen)) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(CharSequence cs, char searchChar, int start) {
        if (cs instanceof String) {
            return ((String) cs).indexOf(searchChar, start);
        }
        if (cs instanceof AsciiString) {
            return ((AsciiString) cs).indexOf(searchChar, start);
        }
        if (cs == null) {
            return -1;
        }
        int sz = cs.length();
        for (int i = start < 0 ? 0 : start; i < sz; i++) {
            if (cs.charAt(i) == searchChar) {
                return i;
            }
        }
        return -1;
    }

    private static boolean equalsIgnoreCase(byte a, byte b) {
        return a == b || AsciiStringUtil.toLowerCase(a) == AsciiStringUtil.toLowerCase(b);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean equalsIgnoreCase(char a, char b) {
        return a == b || toLowerCase(a) == toLowerCase(b);
    }

    public static char toLowerCase(char c) {
        return isUpperCase(c) ? (char) (c + ' ') : c;
    }

    private static byte toUpperCase(byte b) {
        return AsciiStringUtil.toUpperCase(b);
    }

    public static boolean isUpperCase(byte value) {
        return AsciiStringUtil.isUpperCase(value);
    }

    public static boolean isUpperCase(char value) {
        return value >= 'A' && value <= 'Z';
    }

    public static byte c2b(char c) {
        return (byte) (c > 255 ? '?' : c);
    }

    private static byte c2b0(char c) {
        return (byte) c;
    }

    public static char b2c(byte b) {
        return (char) (b & 255);
    }
}
