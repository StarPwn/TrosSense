package io.netty.handler.codec.http2;

import io.netty.handler.codec.CharSequenceValueConverter;
import io.netty.handler.codec.Headers;
import io.netty.handler.codec.http2.Http2Headers;
import io.netty.util.AsciiString;
import io.netty.util.HashingStrategy;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.ObjectUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/* loaded from: classes4.dex */
public final class ReadOnlyHttp2Headers implements Http2Headers {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final byte PSEUDO_HEADER_TOKEN = 58;
    private final AsciiString[] otherHeaders;
    private final AsciiString[] pseudoHeaders;

    @Override // io.netty.handler.codec.Headers
    public /* bridge */ /* synthetic */ Http2Headers addObject(CharSequence charSequence, Iterable iterable) {
        return addObject2(charSequence, (Iterable<?>) iterable);
    }

    @Override // io.netty.handler.codec.Headers
    public /* bridge */ /* synthetic */ Http2Headers setObject(CharSequence charSequence, Iterable iterable) {
        return setObject2(charSequence, (Iterable<?>) iterable);
    }

    public static ReadOnlyHttp2Headers trailers(boolean validateHeaders, AsciiString... otherHeaders) {
        return new ReadOnlyHttp2Headers(validateHeaders, EmptyArrays.EMPTY_ASCII_STRINGS, otherHeaders);
    }

    public static ReadOnlyHttp2Headers clientHeaders(boolean validateHeaders, AsciiString method, AsciiString path, AsciiString scheme, AsciiString authority, AsciiString... otherHeaders) {
        return new ReadOnlyHttp2Headers(validateHeaders, new AsciiString[]{Http2Headers.PseudoHeaderName.METHOD.value(), method, Http2Headers.PseudoHeaderName.PATH.value(), path, Http2Headers.PseudoHeaderName.SCHEME.value(), scheme, Http2Headers.PseudoHeaderName.AUTHORITY.value(), authority}, otherHeaders);
    }

    public static ReadOnlyHttp2Headers serverHeaders(boolean validateHeaders, AsciiString status, AsciiString... otherHeaders) {
        return new ReadOnlyHttp2Headers(validateHeaders, new AsciiString[]{Http2Headers.PseudoHeaderName.STATUS.value(), status}, otherHeaders);
    }

    private ReadOnlyHttp2Headers(boolean validateHeaders, AsciiString[] pseudoHeaders, AsciiString... otherHeaders) {
        if ((pseudoHeaders.length & 1) != 0) {
            throw new AssertionError();
        }
        if ((otherHeaders.length & 1) != 0) {
            throw newInvalidArraySizeException();
        }
        if (validateHeaders) {
            validateHeaders(pseudoHeaders, otherHeaders);
        }
        this.pseudoHeaders = pseudoHeaders;
        this.otherHeaders = otherHeaders;
    }

    private static IllegalArgumentException newInvalidArraySizeException() {
        return new IllegalArgumentException("pseudoHeaders and otherHeaders must be arrays of [name, value] pairs");
    }

    private static void validateHeaders(AsciiString[] pseudoHeaders, AsciiString... otherHeaders) {
        for (int i = 1; i < pseudoHeaders.length; i += 2) {
            ObjectUtil.checkNotNullArrayParam(pseudoHeaders[i], i, "pseudoHeaders");
        }
        int i2 = 0;
        int otherHeadersEnd = otherHeaders.length - 1;
        for (int i3 = 0; i3 < otherHeadersEnd; i3 += 2) {
            AsciiString name = otherHeaders[i3];
            DefaultHttp2Headers.HTTP2_NAME_VALIDATOR.validateName(name);
            if (i2 == 0 && !name.isEmpty() && name.byteAt(0) != 58) {
                i2 = 1;
            } else if (i2 != 0 && !name.isEmpty() && name.byteAt(0) == 58) {
                throw new IllegalArgumentException("otherHeaders name at index " + i3 + " is a pseudo header that appears after non-pseudo headers.");
            }
            ObjectUtil.checkNotNullArrayParam(otherHeaders[i3 + 1], i3 + 1, "otherHeaders");
        }
    }

    private AsciiString get0(CharSequence name) {
        int nameHash = AsciiString.hashCode(name);
        int pseudoHeadersEnd = this.pseudoHeaders.length - 1;
        for (int i = 0; i < pseudoHeadersEnd; i += 2) {
            AsciiString roName = this.pseudoHeaders[i];
            if (roName.hashCode() == nameHash && roName.contentEqualsIgnoreCase(name)) {
                return this.pseudoHeaders[i + 1];
            }
        }
        int otherHeadersEnd = this.otherHeaders.length - 1;
        for (int i2 = 0; i2 < otherHeadersEnd; i2 += 2) {
            AsciiString roName2 = this.otherHeaders[i2];
            if (roName2.hashCode() == nameHash && roName2.contentEqualsIgnoreCase(name)) {
                return this.otherHeaders[i2 + 1];
            }
        }
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public CharSequence get(CharSequence name) {
        return get0(name);
    }

    @Override // io.netty.handler.codec.Headers
    public CharSequence get(CharSequence name, CharSequence defaultValue) {
        CharSequence value = get(name);
        return value != null ? value : defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public CharSequence getAndRemove(CharSequence name) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public CharSequence getAndRemove(CharSequence name, CharSequence defaultValue) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public List<CharSequence> getAll(CharSequence name) {
        int nameHash = AsciiString.hashCode(name);
        List<CharSequence> values = new ArrayList<>();
        int pseudoHeadersEnd = this.pseudoHeaders.length - 1;
        for (int i = 0; i < pseudoHeadersEnd; i += 2) {
            AsciiString roName = this.pseudoHeaders[i];
            if (roName.hashCode() == nameHash && roName.contentEqualsIgnoreCase(name)) {
                values.add(this.pseudoHeaders[i + 1]);
            }
        }
        int otherHeadersEnd = this.otherHeaders.length - 1;
        for (int i2 = 0; i2 < otherHeadersEnd; i2 += 2) {
            AsciiString roName2 = this.otherHeaders[i2];
            if (roName2.hashCode() == nameHash && roName2.contentEqualsIgnoreCase(name)) {
                values.add(this.otherHeaders[i2 + 1]);
            }
        }
        return values;
    }

    @Override // io.netty.handler.codec.Headers
    public List<CharSequence> getAllAndRemove(CharSequence name) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public Boolean getBoolean(CharSequence name) {
        AsciiString value = get0(name);
        if (value != null) {
            return Boolean.valueOf(CharSequenceValueConverter.INSTANCE.convertToBoolean((CharSequence) value));
        }
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public boolean getBoolean(CharSequence name, boolean defaultValue) {
        Boolean value = getBoolean(name);
        return value != null ? value.booleanValue() : defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Byte getByte(CharSequence name) {
        AsciiString value = get0(name);
        if (value != null) {
            return Byte.valueOf(CharSequenceValueConverter.INSTANCE.convertToByte((CharSequence) value));
        }
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public byte getByte(CharSequence name, byte defaultValue) {
        Byte value = getByte(name);
        return value != null ? value.byteValue() : defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Character getChar(CharSequence name) {
        AsciiString value = get0(name);
        if (value != null) {
            return Character.valueOf(CharSequenceValueConverter.INSTANCE.convertToChar((CharSequence) value));
        }
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public char getChar(CharSequence name, char defaultValue) {
        Character value = getChar(name);
        return value != null ? value.charValue() : defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Short getShort(CharSequence name) {
        AsciiString value = get0(name);
        if (value != null) {
            return Short.valueOf(CharSequenceValueConverter.INSTANCE.convertToShort((CharSequence) value));
        }
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public short getShort(CharSequence name, short defaultValue) {
        Short value = getShort(name);
        return value != null ? value.shortValue() : defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Integer getInt(CharSequence name) {
        AsciiString value = get0(name);
        if (value != null) {
            return Integer.valueOf(CharSequenceValueConverter.INSTANCE.convertToInt((CharSequence) value));
        }
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public int getInt(CharSequence name, int defaultValue) {
        Integer value = getInt(name);
        return value != null ? value.intValue() : defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Long getLong(CharSequence name) {
        AsciiString value = get0(name);
        if (value != null) {
            return Long.valueOf(CharSequenceValueConverter.INSTANCE.convertToLong((CharSequence) value));
        }
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public long getLong(CharSequence name, long defaultValue) {
        Long value = getLong(name);
        return value != null ? value.longValue() : defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Float getFloat(CharSequence name) {
        AsciiString value = get0(name);
        if (value != null) {
            return Float.valueOf(CharSequenceValueConverter.INSTANCE.convertToFloat((CharSequence) value));
        }
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public float getFloat(CharSequence name, float defaultValue) {
        Float value = getFloat(name);
        return value != null ? value.floatValue() : defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Double getDouble(CharSequence name) {
        AsciiString value = get0(name);
        if (value != null) {
            return Double.valueOf(CharSequenceValueConverter.INSTANCE.convertToDouble((CharSequence) value));
        }
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public double getDouble(CharSequence name, double defaultValue) {
        Double value = getDouble(name);
        return value != null ? value.doubleValue() : defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Long getTimeMillis(CharSequence name) {
        AsciiString value = get0(name);
        if (value != null) {
            return Long.valueOf(CharSequenceValueConverter.INSTANCE.convertToTimeMillis((CharSequence) value));
        }
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public long getTimeMillis(CharSequence name, long defaultValue) {
        Long value = getTimeMillis(name);
        return value != null ? value.longValue() : defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Boolean getBooleanAndRemove(CharSequence name) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public boolean getBooleanAndRemove(CharSequence name, boolean defaultValue) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public Byte getByteAndRemove(CharSequence name) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public byte getByteAndRemove(CharSequence name, byte defaultValue) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public Character getCharAndRemove(CharSequence name) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public char getCharAndRemove(CharSequence name, char defaultValue) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public Short getShortAndRemove(CharSequence name) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public short getShortAndRemove(CharSequence name, short defaultValue) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public Integer getIntAndRemove(CharSequence name) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public int getIntAndRemove(CharSequence name, int defaultValue) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public Long getLongAndRemove(CharSequence name) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public long getLongAndRemove(CharSequence name, long defaultValue) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public Float getFloatAndRemove(CharSequence name) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public float getFloatAndRemove(CharSequence name, float defaultValue) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public Double getDoubleAndRemove(CharSequence name) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public double getDoubleAndRemove(CharSequence name, double defaultValue) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public Long getTimeMillisAndRemove(CharSequence name) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public long getTimeMillisAndRemove(CharSequence name, long defaultValue) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public boolean contains(CharSequence name) {
        return get(name) != null;
    }

    @Override // io.netty.handler.codec.Headers
    public boolean contains(CharSequence name, CharSequence value) {
        return contains(name, value, false);
    }

    @Override // io.netty.handler.codec.Headers
    public boolean containsObject(CharSequence name, Object value) {
        if (value instanceof CharSequence) {
            return contains(name, (CharSequence) value);
        }
        return contains(name, (CharSequence) value.toString());
    }

    @Override // io.netty.handler.codec.Headers
    public boolean containsBoolean(CharSequence name, boolean value) {
        return contains(name, (CharSequence) String.valueOf(value));
    }

    @Override // io.netty.handler.codec.Headers
    public boolean containsByte(CharSequence name, byte value) {
        return contains(name, (CharSequence) String.valueOf((int) value));
    }

    @Override // io.netty.handler.codec.Headers
    public boolean containsChar(CharSequence name, char value) {
        return contains(name, (CharSequence) String.valueOf(value));
    }

    @Override // io.netty.handler.codec.Headers
    public boolean containsShort(CharSequence name, short value) {
        return contains(name, (CharSequence) String.valueOf((int) value));
    }

    @Override // io.netty.handler.codec.Headers
    public boolean containsInt(CharSequence name, int value) {
        return contains(name, (CharSequence) String.valueOf(value));
    }

    @Override // io.netty.handler.codec.Headers
    public boolean containsLong(CharSequence name, long value) {
        return contains(name, (CharSequence) String.valueOf(value));
    }

    @Override // io.netty.handler.codec.Headers
    public boolean containsFloat(CharSequence name, float value) {
        return false;
    }

    @Override // io.netty.handler.codec.Headers
    public boolean containsDouble(CharSequence name, double value) {
        return contains(name, (CharSequence) String.valueOf(value));
    }

    @Override // io.netty.handler.codec.Headers
    public boolean containsTimeMillis(CharSequence name, long value) {
        return contains(name, (CharSequence) String.valueOf(value));
    }

    @Override // io.netty.handler.codec.Headers
    public int size() {
        return (this.pseudoHeaders.length + this.otherHeaders.length) >>> 1;
    }

    @Override // io.netty.handler.codec.Headers
    public boolean isEmpty() {
        return this.pseudoHeaders.length == 0 && this.otherHeaders.length == 0;
    }

    @Override // io.netty.handler.codec.Headers
    public Set<CharSequence> names() {
        if (isEmpty()) {
            return Collections.emptySet();
        }
        Set<CharSequence> names = new LinkedHashSet<>(size());
        int pseudoHeadersEnd = this.pseudoHeaders.length - 1;
        for (int i = 0; i < pseudoHeadersEnd; i += 2) {
            names.add(this.pseudoHeaders[i]);
        }
        int otherHeadersEnd = this.otherHeaders.length - 1;
        for (int i2 = 0; i2 < otherHeadersEnd; i2 += 2) {
            names.add(this.otherHeaders[i2]);
        }
        return names;
    }

    @Override // io.netty.handler.codec.Headers
    public Http2Headers add(CharSequence name, CharSequence value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public Http2Headers add(CharSequence name, Iterable<? extends CharSequence> values) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public Http2Headers add(CharSequence name, CharSequence... values) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public Http2Headers addObject(CharSequence name, Object value) {
        throw new UnsupportedOperationException("read only");
    }

    /* renamed from: addObject, reason: avoid collision after fix types in other method */
    public Http2Headers addObject2(CharSequence name, Iterable<?> values) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public Http2Headers addObject(CharSequence name, Object... values) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public Http2Headers addBoolean(CharSequence name, boolean value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public Http2Headers addByte(CharSequence name, byte value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public Http2Headers addChar(CharSequence name, char value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public Http2Headers addShort(CharSequence name, short value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public Http2Headers addInt(CharSequence name, int value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public Http2Headers addLong(CharSequence name, long value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public Http2Headers addFloat(CharSequence name, float value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public Http2Headers addDouble(CharSequence name, double value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public Http2Headers addTimeMillis(CharSequence name, long value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public Http2Headers add(Headers<? extends CharSequence, ? extends CharSequence, ?> headers) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public Http2Headers set(CharSequence name, CharSequence value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public Http2Headers set(CharSequence name, Iterable<? extends CharSequence> values) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public Http2Headers set(CharSequence name, CharSequence... values) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public Http2Headers setObject(CharSequence name, Object value) {
        throw new UnsupportedOperationException("read only");
    }

    /* renamed from: setObject, reason: avoid collision after fix types in other method */
    public Http2Headers setObject2(CharSequence name, Iterable<?> values) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public Http2Headers setObject(CharSequence name, Object... values) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public Http2Headers setBoolean(CharSequence name, boolean value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public Http2Headers setByte(CharSequence name, byte value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public Http2Headers setChar(CharSequence name, char value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public Http2Headers setShort(CharSequence name, short value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public Http2Headers setInt(CharSequence name, int value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public Http2Headers setLong(CharSequence name, long value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public Http2Headers setFloat(CharSequence name, float value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public Http2Headers setDouble(CharSequence name, double value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public Http2Headers setTimeMillis(CharSequence name, long value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public Http2Headers set(Headers<? extends CharSequence, ? extends CharSequence, ?> headers) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public Http2Headers setAll(Headers<? extends CharSequence, ? extends CharSequence, ?> headers) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public boolean remove(CharSequence name) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public Http2Headers clear() {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.http2.Http2Headers, io.netty.handler.codec.Headers, java.lang.Iterable
    public Iterator<Map.Entry<CharSequence, CharSequence>> iterator() {
        return new ReadOnlyIterator();
    }

    @Override // io.netty.handler.codec.http2.Http2Headers
    public Iterator<CharSequence> valueIterator(CharSequence name) {
        return new ReadOnlyValueIterator(name);
    }

    @Override // io.netty.handler.codec.http2.Http2Headers
    public Http2Headers method(CharSequence value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.http2.Http2Headers
    public Http2Headers scheme(CharSequence value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.http2.Http2Headers
    public Http2Headers authority(CharSequence value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.http2.Http2Headers
    public Http2Headers path(CharSequence value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.http2.Http2Headers
    public Http2Headers status(CharSequence value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.http2.Http2Headers
    public CharSequence method() {
        return get((CharSequence) Http2Headers.PseudoHeaderName.METHOD.value());
    }

    @Override // io.netty.handler.codec.http2.Http2Headers
    public CharSequence scheme() {
        return get((CharSequence) Http2Headers.PseudoHeaderName.SCHEME.value());
    }

    @Override // io.netty.handler.codec.http2.Http2Headers
    public CharSequence authority() {
        return get((CharSequence) Http2Headers.PseudoHeaderName.AUTHORITY.value());
    }

    @Override // io.netty.handler.codec.http2.Http2Headers
    public CharSequence path() {
        return get((CharSequence) Http2Headers.PseudoHeaderName.PATH.value());
    }

    @Override // io.netty.handler.codec.http2.Http2Headers
    public CharSequence status() {
        return get((CharSequence) Http2Headers.PseudoHeaderName.STATUS.value());
    }

    @Override // io.netty.handler.codec.http2.Http2Headers
    public boolean contains(CharSequence name, CharSequence value, boolean caseInsensitive) {
        int nameHash = AsciiString.hashCode(name);
        HashingStrategy<CharSequence> strategy = caseInsensitive ? AsciiString.CASE_INSENSITIVE_HASHER : AsciiString.CASE_SENSITIVE_HASHER;
        int valueHash = strategy.hashCode(value);
        return contains(name, nameHash, value, valueHash, strategy, this.otherHeaders) || contains(name, nameHash, value, valueHash, strategy, this.pseudoHeaders);
    }

    private static boolean contains(CharSequence name, int nameHash, CharSequence value, int valueHash, HashingStrategy<CharSequence> hashingStrategy, AsciiString[] headers) {
        int headersEnd = headers.length - 1;
        for (int i = 0; i < headersEnd; i += 2) {
            AsciiString roName = headers[i];
            AsciiString roValue = headers[i + 1];
            if (roName.hashCode() == nameHash && roValue.hashCode() == valueHash && roName.contentEqualsIgnoreCase(name) && hashingStrategy.equals(roValue, value)) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder(getClass().getSimpleName()).append('[');
        String separator = "";
        Iterator<Map.Entry<CharSequence, CharSequence>> it2 = iterator();
        while (it2.hasNext()) {
            Map.Entry<CharSequence, CharSequence> entry = it2.next();
            builder.append(separator);
            builder.append(entry.getKey()).append(": ").append(entry.getValue());
            separator = ", ";
        }
        return builder.append(']').toString();
    }

    /* loaded from: classes4.dex */
    private final class ReadOnlyValueIterator implements Iterator<CharSequence> {
        private AsciiString[] current;
        private int i;
        private final CharSequence name;
        private final int nameHash;
        private AsciiString next;

        ReadOnlyValueIterator(CharSequence name) {
            this.current = ReadOnlyHttp2Headers.this.pseudoHeaders.length != 0 ? ReadOnlyHttp2Headers.this.pseudoHeaders : ReadOnlyHttp2Headers.this.otherHeaders;
            this.nameHash = AsciiString.hashCode(name);
            this.name = name;
            calculateNext();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.next != null;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public CharSequence next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            CharSequence current = this.next;
            calculateNext();
            return current;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("read only");
        }

        private void calculateNext() {
            while (this.i < this.current.length) {
                AsciiString roName = this.current[this.i];
                if (roName.hashCode() != this.nameHash || !roName.contentEqualsIgnoreCase(this.name)) {
                    this.i += 2;
                } else {
                    if (this.i + 1 < this.current.length) {
                        this.next = this.current[this.i + 1];
                        this.i += 2;
                        return;
                    }
                    return;
                }
            }
            if (this.current == ReadOnlyHttp2Headers.this.pseudoHeaders) {
                this.i = 0;
                this.current = ReadOnlyHttp2Headers.this.otherHeaders;
                calculateNext();
                return;
            }
            this.next = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class ReadOnlyIterator implements Map.Entry<CharSequence, CharSequence>, Iterator<Map.Entry<CharSequence, CharSequence>> {
        private AsciiString[] current;
        private int i;
        private AsciiString key;
        private AsciiString value;

        private ReadOnlyIterator() {
            this.current = ReadOnlyHttp2Headers.this.pseudoHeaders.length != 0 ? ReadOnlyHttp2Headers.this.pseudoHeaders : ReadOnlyHttp2Headers.this.otherHeaders;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.i != this.current.length;
        }

        @Override // java.util.Iterator
        public Map.Entry<CharSequence, CharSequence> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            this.key = this.current[this.i];
            this.value = this.current[this.i + 1];
            this.i += 2;
            if (this.i == this.current.length && this.current == ReadOnlyHttp2Headers.this.pseudoHeaders) {
                this.current = ReadOnlyHttp2Headers.this.otherHeaders;
                this.i = 0;
            }
            return this;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Map.Entry
        public CharSequence getKey() {
            return this.key;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Map.Entry
        public CharSequence getValue() {
            return this.value;
        }

        @Override // java.util.Map.Entry
        public CharSequence setValue(CharSequence value) {
            throw new UnsupportedOperationException("read only");
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("read only");
        }

        public String toString() {
            return this.key.toString() + '=' + this.value.toString();
        }
    }
}
