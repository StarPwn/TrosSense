package org.msgpack.value.impl;

import java.io.IOException;
import java.math.BigInteger;
import org.msgpack.core.MessageFormat;
import org.msgpack.core.MessageIntegerOverflowException;
import org.msgpack.core.MessagePacker;
import org.msgpack.value.ImmutableArrayValue;
import org.msgpack.value.ImmutableBinaryValue;
import org.msgpack.value.ImmutableBooleanValue;
import org.msgpack.value.ImmutableExtensionValue;
import org.msgpack.value.ImmutableFloatValue;
import org.msgpack.value.ImmutableIntegerValue;
import org.msgpack.value.ImmutableMapValue;
import org.msgpack.value.ImmutableNilValue;
import org.msgpack.value.ImmutableNumberValue;
import org.msgpack.value.ImmutableRawValue;
import org.msgpack.value.ImmutableStringValue;
import org.msgpack.value.ImmutableTimestampValue;
import org.msgpack.value.IntegerValue;
import org.msgpack.value.Value;
import org.msgpack.value.ValueType;

/* loaded from: classes5.dex */
public class ImmutableLongValueImpl extends AbstractImmutableValue implements ImmutableIntegerValue {
    private static final long BYTE_MAX = 127;
    private static final long BYTE_MIN = -128;
    private static final long INT_MAX = 2147483647L;
    private static final long INT_MIN = -2147483648L;
    private static final long SHORT_MAX = 32767;
    private static final long SHORT_MIN = -32768;
    private final long value;

    @Override // org.msgpack.value.impl.AbstractImmutableValue, org.msgpack.value.ImmutableValue, org.msgpack.value.Value
    public /* bridge */ /* synthetic */ ImmutableArrayValue asArrayValue() {
        return super.asArrayValue();
    }

    @Override // org.msgpack.value.impl.AbstractImmutableValue, org.msgpack.value.ImmutableValue, org.msgpack.value.Value
    public /* bridge */ /* synthetic */ ImmutableBinaryValue asBinaryValue() {
        return super.asBinaryValue();
    }

    @Override // org.msgpack.value.impl.AbstractImmutableValue, org.msgpack.value.ImmutableValue, org.msgpack.value.Value
    public /* bridge */ /* synthetic */ ImmutableBooleanValue asBooleanValue() {
        return super.asBooleanValue();
    }

    @Override // org.msgpack.value.impl.AbstractImmutableValue, org.msgpack.value.Value
    public /* bridge */ /* synthetic */ ImmutableExtensionValue asExtensionValue() {
        return super.asExtensionValue();
    }

    @Override // org.msgpack.value.impl.AbstractImmutableValue, org.msgpack.value.ImmutableValue, org.msgpack.value.Value
    public /* bridge */ /* synthetic */ ImmutableFloatValue asFloatValue() {
        return super.asFloatValue();
    }

    @Override // org.msgpack.value.impl.AbstractImmutableValue, org.msgpack.value.ImmutableValue, org.msgpack.value.Value
    public /* bridge */ /* synthetic */ ImmutableMapValue asMapValue() {
        return super.asMapValue();
    }

    @Override // org.msgpack.value.impl.AbstractImmutableValue, org.msgpack.value.ImmutableValue, org.msgpack.value.Value
    public /* bridge */ /* synthetic */ ImmutableNilValue asNilValue() {
        return super.asNilValue();
    }

    @Override // org.msgpack.value.impl.AbstractImmutableValue, org.msgpack.value.ImmutableValue, org.msgpack.value.Value
    public /* bridge */ /* synthetic */ ImmutableRawValue asRawValue() {
        return super.asRawValue();
    }

    @Override // org.msgpack.value.impl.AbstractImmutableValue, org.msgpack.value.ImmutableValue, org.msgpack.value.Value
    public /* bridge */ /* synthetic */ ImmutableStringValue asStringValue() {
        return super.asStringValue();
    }

    @Override // org.msgpack.value.impl.AbstractImmutableValue, org.msgpack.value.ImmutableValue, org.msgpack.value.Value
    public /* bridge */ /* synthetic */ ImmutableTimestampValue asTimestampValue() {
        return super.asTimestampValue();
    }

    @Override // org.msgpack.value.impl.AbstractImmutableValue, org.msgpack.value.Value
    public /* bridge */ /* synthetic */ boolean isArrayValue() {
        return super.isArrayValue();
    }

    @Override // org.msgpack.value.impl.AbstractImmutableValue, org.msgpack.value.Value
    public /* bridge */ /* synthetic */ boolean isBinaryValue() {
        return super.isBinaryValue();
    }

    @Override // org.msgpack.value.impl.AbstractImmutableValue, org.msgpack.value.Value
    public /* bridge */ /* synthetic */ boolean isBooleanValue() {
        return super.isBooleanValue();
    }

    @Override // org.msgpack.value.impl.AbstractImmutableValue, org.msgpack.value.Value
    public /* bridge */ /* synthetic */ boolean isExtensionValue() {
        return super.isExtensionValue();
    }

    @Override // org.msgpack.value.impl.AbstractImmutableValue, org.msgpack.value.Value
    public /* bridge */ /* synthetic */ boolean isFloatValue() {
        return super.isFloatValue();
    }

    @Override // org.msgpack.value.impl.AbstractImmutableValue, org.msgpack.value.Value
    public /* bridge */ /* synthetic */ boolean isIntegerValue() {
        return super.isIntegerValue();
    }

    @Override // org.msgpack.value.impl.AbstractImmutableValue, org.msgpack.value.Value
    public /* bridge */ /* synthetic */ boolean isMapValue() {
        return super.isMapValue();
    }

    @Override // org.msgpack.value.impl.AbstractImmutableValue, org.msgpack.value.Value
    public /* bridge */ /* synthetic */ boolean isNilValue() {
        return super.isNilValue();
    }

    @Override // org.msgpack.value.impl.AbstractImmutableValue, org.msgpack.value.Value
    public /* bridge */ /* synthetic */ boolean isNumberValue() {
        return super.isNumberValue();
    }

    @Override // org.msgpack.value.impl.AbstractImmutableValue, org.msgpack.value.Value
    public /* bridge */ /* synthetic */ boolean isRawValue() {
        return super.isRawValue();
    }

    @Override // org.msgpack.value.impl.AbstractImmutableValue, org.msgpack.value.Value
    public /* bridge */ /* synthetic */ boolean isStringValue() {
        return super.isStringValue();
    }

    @Override // org.msgpack.value.impl.AbstractImmutableValue, org.msgpack.value.Value
    public /* bridge */ /* synthetic */ boolean isTimestampValue() {
        return super.isTimestampValue();
    }

    public ImmutableLongValueImpl(long j) {
        this.value = j;
    }

    @Override // org.msgpack.value.Value
    public ValueType getValueType() {
        return ValueType.INTEGER;
    }

    @Override // org.msgpack.value.Value
    public ImmutableIntegerValue immutableValue() {
        return this;
    }

    @Override // org.msgpack.value.impl.AbstractImmutableValue, org.msgpack.value.Value
    public ImmutableNumberValue asNumberValue() {
        return this;
    }

    @Override // org.msgpack.value.impl.AbstractImmutableValue, org.msgpack.value.ImmutableValue, org.msgpack.value.Value
    public ImmutableIntegerValue asIntegerValue() {
        return this;
    }

    @Override // org.msgpack.value.NumberValue
    public byte toByte() {
        return (byte) this.value;
    }

    @Override // org.msgpack.value.NumberValue
    public short toShort() {
        return (short) this.value;
    }

    @Override // org.msgpack.value.NumberValue
    public int toInt() {
        return (int) this.value;
    }

    @Override // org.msgpack.value.NumberValue
    public long toLong() {
        return this.value;
    }

    @Override // org.msgpack.value.NumberValue
    public BigInteger toBigInteger() {
        return BigInteger.valueOf(this.value);
    }

    @Override // org.msgpack.value.NumberValue
    public float toFloat() {
        return (float) this.value;
    }

    @Override // org.msgpack.value.NumberValue
    public double toDouble() {
        return this.value;
    }

    @Override // org.msgpack.value.IntegerValue
    public boolean isInByteRange() {
        return BYTE_MIN <= this.value && this.value <= BYTE_MAX;
    }

    @Override // org.msgpack.value.IntegerValue
    public boolean isInShortRange() {
        return SHORT_MIN <= this.value && this.value <= SHORT_MAX;
    }

    @Override // org.msgpack.value.IntegerValue
    public boolean isInIntRange() {
        return INT_MIN <= this.value && this.value <= INT_MAX;
    }

    @Override // org.msgpack.value.IntegerValue
    public boolean isInLongRange() {
        return true;
    }

    @Override // org.msgpack.value.IntegerValue
    public MessageFormat mostSuccinctMessageFormat() {
        return ImmutableBigIntegerValueImpl.mostSuccinctMessageFormat(this);
    }

    @Override // org.msgpack.value.IntegerValue
    public byte asByte() {
        if (!isInByteRange()) {
            throw new MessageIntegerOverflowException(this.value);
        }
        return (byte) this.value;
    }

    @Override // org.msgpack.value.IntegerValue
    public short asShort() {
        if (!isInShortRange()) {
            throw new MessageIntegerOverflowException(this.value);
        }
        return (short) this.value;
    }

    @Override // org.msgpack.value.IntegerValue
    public int asInt() {
        if (!isInIntRange()) {
            throw new MessageIntegerOverflowException(this.value);
        }
        return (int) this.value;
    }

    @Override // org.msgpack.value.IntegerValue
    public long asLong() {
        return this.value;
    }

    @Override // org.msgpack.value.IntegerValue
    public BigInteger asBigInteger() {
        return BigInteger.valueOf(this.value);
    }

    @Override // org.msgpack.value.Value
    public void writeTo(MessagePacker messagePacker) throws IOException {
        messagePacker.packLong(this.value);
    }

    @Override // org.msgpack.value.Value
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Value)) {
            return false;
        }
        Value value = (Value) obj;
        if (!value.isIntegerValue()) {
            return false;
        }
        IntegerValue asIntegerValue = value.asIntegerValue();
        return asIntegerValue.isInLongRange() && this.value == asIntegerValue.toLong();
    }

    public int hashCode() {
        if (INT_MIN <= this.value && this.value <= INT_MAX) {
            return (int) this.value;
        }
        return (int) (this.value ^ (this.value >>> 32));
    }

    @Override // org.msgpack.value.Value
    public String toJson() {
        return Long.toString(this.value);
    }

    public String toString() {
        return toJson();
    }
}
