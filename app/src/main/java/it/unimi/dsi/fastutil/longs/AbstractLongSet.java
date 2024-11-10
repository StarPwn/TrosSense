package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.HashCommon;
import java.util.Set;

/* loaded from: classes4.dex */
public abstract class AbstractLongSet extends AbstractLongCollection implements Cloneable, LongSet {
    @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
    public abstract LongIterator iterator();

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
        if (s instanceof LongSet) {
            return containsAll((LongCollection) s);
        }
        return containsAll(s);
    }

    @Override // java.util.Collection, java.util.Set
    public int hashCode() {
        int h = 0;
        int n = size();
        LongIterator i = iterator();
        while (true) {
            int n2 = n - 1;
            if (n != 0) {
                long k = i.nextLong();
                h += HashCommon.long2int(k);
                n = n2;
            } else {
                return h;
            }
        }
    }

    @Override // it.unimi.dsi.fastutil.longs.LongSet
    public boolean remove(long k) {
        return super.rem(k);
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
    @Deprecated
    public boolean rem(long k) {
        return remove(k);
    }
}
