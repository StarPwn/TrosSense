package it.unimi.dsi.fastutil.longs;

import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Collection;

/* loaded from: classes4.dex */
public abstract class AbstractLongCollection extends AbstractCollection<Long> implements LongCollection {
    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
    public abstract LongIterator iterator();

    @Override // it.unimi.dsi.fastutil.longs.LongCollection
    public boolean add(long k) {
        throw new UnsupportedOperationException();
    }

    public boolean contains(long k) {
        LongIterator iterator = iterator();
        while (iterator.hasNext()) {
            if (k == iterator.nextLong()) {
                return true;
            }
        }
        return false;
    }

    @Override // it.unimi.dsi.fastutil.longs.LongCollection
    public boolean rem(long k) {
        LongIterator iterator = iterator();
        while (iterator.hasNext()) {
            if (k == iterator.nextLong()) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.AbstractCollection, java.util.Collection, it.unimi.dsi.fastutil.longs.LongCollection
    @Deprecated
    public boolean add(Long key) {
        return super.add(key);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, it.unimi.dsi.fastutil.longs.LongCollection
    @Deprecated
    public boolean contains(Object key) {
        return super.contains(key);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, it.unimi.dsi.fastutil.longs.LongCollection
    @Deprecated
    public boolean remove(Object key) {
        return super.remove(key);
    }

    @Override // it.unimi.dsi.fastutil.longs.LongCollection
    public long[] toArray(long[] a) {
        int size = size();
        if (a == null) {
            a = new long[size];
        } else if (a.length < size) {
            a = Arrays.copyOf(a, size);
        }
        LongIterators.unwrap(iterator(), a);
        return a;
    }

    @Override // it.unimi.dsi.fastutil.longs.LongCollection
    public long[] toLongArray() {
        int size = size();
        if (size == 0) {
            return LongArrays.EMPTY_ARRAY;
        }
        long[] a = new long[size];
        LongIterators.unwrap(iterator(), a);
        return a;
    }

    @Override // it.unimi.dsi.fastutil.longs.LongCollection
    @Deprecated
    public long[] toLongArray(long[] a) {
        return toArray(a);
    }

    @Override // it.unimi.dsi.fastutil.longs.LongIterable
    public final void forEach(LongConsumer action) {
        super.forEach(action);
    }

    @Override // it.unimi.dsi.fastutil.longs.LongCollection
    public final boolean removeIf(LongPredicate filter) {
        return super.removeIf(filter);
    }

    @Override // it.unimi.dsi.fastutil.longs.LongCollection
    public boolean addAll(LongCollection c) {
        boolean retVal = false;
        LongIterator i = c.iterator();
        while (i.hasNext()) {
            if (add(i.nextLong())) {
                retVal = true;
            }
        }
        return retVal;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean addAll(Collection<? extends Long> c) {
        if (c instanceof LongCollection) {
            return addAll((LongCollection) c);
        }
        return super.addAll(c);
    }

    @Override // it.unimi.dsi.fastutil.longs.LongCollection
    public boolean containsAll(LongCollection c) {
        LongIterator i = c.iterator();
        while (i.hasNext()) {
            if (!contains(i.nextLong())) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean containsAll(Collection<?> c) {
        if (c instanceof LongCollection) {
            return containsAll((LongCollection) c);
        }
        return super.containsAll(c);
    }

    @Override // it.unimi.dsi.fastutil.longs.LongCollection
    public boolean removeAll(LongCollection c) {
        boolean retVal = false;
        LongIterator i = c.iterator();
        while (i.hasNext()) {
            if (rem(i.nextLong())) {
                retVal = true;
            }
        }
        return retVal;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean removeAll(Collection<?> c) {
        if (c instanceof LongCollection) {
            return removeAll((LongCollection) c);
        }
        return super.removeAll(c);
    }

    @Override // it.unimi.dsi.fastutil.longs.LongCollection
    public boolean retainAll(LongCollection c) {
        boolean retVal = false;
        LongIterator i = iterator();
        while (i.hasNext()) {
            if (!c.contains(i.nextLong())) {
                i.remove();
                retVal = true;
            }
        }
        return retVal;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean retainAll(Collection<?> c) {
        if (c instanceof LongCollection) {
            return retainAll((LongCollection) c);
        }
        return super.retainAll(c);
    }

    @Override // java.util.AbstractCollection
    public String toString() {
        StringBuilder s = new StringBuilder();
        LongIterator i = iterator();
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
                long k = i.nextLong();
                s.append(String.valueOf(k));
                n = n2;
            } else {
                s.append("}");
                return s.toString();
            }
        }
    }
}
