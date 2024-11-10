package org.msgpack.value.impl;

import java.io.IOException;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import okhttp3.HttpUrl;
import org.msgpack.core.MessagePacker;
import org.msgpack.value.ArrayValue;
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
import org.msgpack.value.Value;
import org.msgpack.value.ValueType;

/* loaded from: classes5.dex */
public class ImmutableArrayValueImpl extends AbstractImmutableValue implements ImmutableArrayValue {
    private static final ImmutableArrayValueImpl EMPTY = new ImmutableArrayValueImpl(new Value[0]);
    private final Value[] array;

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
    public /* bridge */ /* synthetic */ ImmutableIntegerValue asIntegerValue() {
        return super.asIntegerValue();
    }

    @Override // org.msgpack.value.impl.AbstractImmutableValue, org.msgpack.value.ImmutableValue, org.msgpack.value.Value
    public /* bridge */ /* synthetic */ ImmutableMapValue asMapValue() {
        return super.asMapValue();
    }

    @Override // org.msgpack.value.impl.AbstractImmutableValue, org.msgpack.value.ImmutableValue, org.msgpack.value.Value
    public /* bridge */ /* synthetic */ ImmutableNilValue asNilValue() {
        return super.asNilValue();
    }

    @Override // org.msgpack.value.impl.AbstractImmutableValue, org.msgpack.value.Value
    public /* bridge */ /* synthetic */ ImmutableNumberValue asNumberValue() {
        return super.asNumberValue();
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

    public static ImmutableArrayValue empty() {
        return EMPTY;
    }

    public ImmutableArrayValueImpl(Value[] valueArr) {
        this.array = valueArr;
    }

    @Override // org.msgpack.value.Value
    public ValueType getValueType() {
        return ValueType.ARRAY;
    }

    @Override // org.msgpack.value.Value
    public ImmutableArrayValue immutableValue() {
        return this;
    }

    @Override // org.msgpack.value.impl.AbstractImmutableValue, org.msgpack.value.ImmutableValue, org.msgpack.value.Value
    public ImmutableArrayValue asArrayValue() {
        return this;
    }

    @Override // org.msgpack.value.ArrayValue
    public int size() {
        return this.array.length;
    }

    @Override // org.msgpack.value.ArrayValue
    public Value get(int i) {
        return this.array[i];
    }

    @Override // org.msgpack.value.ArrayValue
    public Value getOrNilValue(int i) {
        if (i < this.array.length && i >= 0) {
            return this.array[i];
        }
        return ImmutableNilValueImpl.get();
    }

    @Override // org.msgpack.value.ImmutableArrayValue, org.msgpack.value.ArrayValue, java.lang.Iterable
    public Iterator<Value> iterator() {
        return new Ite(this.array);
    }

    @Override // org.msgpack.value.ImmutableArrayValue, org.msgpack.value.ArrayValue
    public List<Value> list() {
        return new ImmutableArrayValueList(this.array);
    }

    @Override // org.msgpack.value.Value
    public void writeTo(MessagePacker messagePacker) throws IOException {
        messagePacker.packArrayHeader(this.array.length);
        for (int i = 0; i < this.array.length; i++) {
            this.array[i].writeTo(messagePacker);
        }
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
        if (value instanceof ImmutableArrayValueImpl) {
            return Arrays.equals(this.array, ((ImmutableArrayValueImpl) value).array);
        }
        if (!value.isArrayValue()) {
            return false;
        }
        ArrayValue asArrayValue = value.asArrayValue();
        if (size() != asArrayValue.size()) {
            return false;
        }
        Iterator<Value> it2 = asArrayValue.iterator();
        for (int i = 0; i < this.array.length; i++) {
            if (!it2.hasNext() || !this.array[i].equals(it2.next())) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int i = 1;
        for (int i2 = 0; i2 < this.array.length; i2++) {
            i = (i * 31) + this.array[i2].hashCode();
        }
        return i;
    }

    @Override // org.msgpack.value.Value
    public String toJson() {
        if (this.array.length == 0) {
            return HttpUrl.PATH_SEGMENT_ENCODE_SET_URI;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(this.array[0].toJson());
        for (int i = 1; i < this.array.length; i++) {
            sb.append(",");
            sb.append(this.array[i].toJson());
        }
        sb.append("]");
        return sb.toString();
    }

    public String toString() {
        if (this.array.length == 0) {
            return HttpUrl.PATH_SEGMENT_ENCODE_SET_URI;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        appendString(sb, this.array[0]);
        for (int i = 1; i < this.array.length; i++) {
            sb.append(",");
            appendString(sb, this.array[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    private static void appendString(StringBuilder sb, Value value) {
        if (value.isRawValue()) {
            sb.append(value.toJson());
        } else {
            sb.append(value.toString());
        }
    }

    /* loaded from: classes5.dex */
    private static class ImmutableArrayValueList extends AbstractList<Value> {
        private final Value[] array;

        public ImmutableArrayValueList(Value[] valueArr) {
            this.array = valueArr;
        }

        @Override // java.util.AbstractList, java.util.List
        public Value get(int i) {
            return this.array[i];
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.array.length;
        }
    }

    /* loaded from: classes5.dex */
    private static class Ite implements Iterator<Value> {
        private final Value[] array;
        private int index = 0;

        public Ite(Value[] valueArr) {
            this.array = valueArr;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.index != this.array.length;
        }

        @Override // java.util.Iterator
        public Value next() {
            int i = this.index;
            if (i >= this.array.length) {
                throw new NoSuchElementException();
            }
            this.index = i + 1;
            return this.array[i];
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
