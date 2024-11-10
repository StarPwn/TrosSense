package it.unimi.dsi.fastutil.objects;

import java.util.AbstractCollection;

/* loaded from: classes4.dex */
public abstract class AbstractObjectCollection<K> extends AbstractCollection<K> implements ObjectCollection<K> {
    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
    public abstract ObjectIterator<K> iterator();

    @Override // java.util.AbstractCollection
    public String toString() {
        StringBuilder s = new StringBuilder();
        ObjectIterator<K> i = iterator();
        int n = size();
        boolean first = true;
        s.append("{");
        while (true) {
            int n2 = n - 1;
            if (n != 0) {
                if (first) {
                    first = false;
                } else {
                    s.append(", ");
                }
                Object k = i.next();
                if (this == k) {
                    s.append("(this collection)");
                } else {
                    s.append(String.valueOf(k));
                }
                n = n2;
            } else {
                s.append("}");
                return s.toString();
            }
        }
    }
}
