package it.unimi.dsi.fastutil.objects;

import java.util.Set;

/* loaded from: classes4.dex */
public abstract class AbstractObjectSet<K> extends AbstractObjectCollection<K> implements Cloneable, ObjectSet<K> {
    @Override // it.unimi.dsi.fastutil.objects.AbstractObjectCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
    public abstract ObjectIterator<K> iterator();

    @Override // java.util.Collection, java.util.Set
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Set)) {
            return false;
        }
        Set<?> s = (Set) o;
        if (s.size() != size()) {
            return false;
        }
        return containsAll(s);
    }

    @Override // java.util.Collection, java.util.Set
    public int hashCode() {
        int h = 0;
        int n = size();
        ObjectIterator<K> i = iterator();
        while (true) {
            int n2 = n - 1;
            if (n != 0) {
                K k = i.next();
                h += k == null ? 0 : k.hashCode();
                n = n2;
            } else {
                return h;
            }
        }
    }
}
