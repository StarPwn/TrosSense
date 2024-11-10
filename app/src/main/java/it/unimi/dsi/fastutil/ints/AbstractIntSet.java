package it.unimi.dsi.fastutil.ints;

import java.util.Set;

/* loaded from: classes4.dex */
public abstract class AbstractIntSet extends AbstractIntCollection implements Cloneable, IntSet {
    @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
    public abstract IntIterator iterator();

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
        if (s instanceof IntSet) {
            return containsAll((IntCollection) s);
        }
        return containsAll(s);
    }

    @Override // java.util.Collection, java.util.Set
    public int hashCode() {
        int h = 0;
        int k = size();
        IntIterator i = iterator();
        while (true) {
            int n = k - 1;
            if (k != 0) {
                int k2 = i.nextInt();
                h += k2;
                k = n;
            } else {
                return h;
            }
        }
    }

    @Override // it.unimi.dsi.fastutil.ints.IntSet
    public boolean remove(int k) {
        return super.rem(k);
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
    @Deprecated
    public boolean rem(int k) {
        return remove(k);
    }
}
