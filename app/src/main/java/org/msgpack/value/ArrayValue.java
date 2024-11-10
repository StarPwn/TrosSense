package org.msgpack.value;

import java.util.Iterator;
import java.util.List;

/* loaded from: classes5.dex */
public interface ArrayValue extends Value, Iterable<Value> {
    Value get(int i);

    Value getOrNilValue(int i);

    @Override // java.lang.Iterable
    Iterator<Value> iterator();

    List<Value> list();

    int size();
}
