package org.msgpack.value;

import java.util.Iterator;
import java.util.List;

/* loaded from: classes5.dex */
public interface ImmutableArrayValue extends ArrayValue, ImmutableValue {
    @Override // org.msgpack.value.ArrayValue, java.lang.Iterable
    Iterator<Value> iterator();

    @Override // org.msgpack.value.ArrayValue
    List<Value> list();
}
