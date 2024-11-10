package it.unimi.dsi.fastutil.ints;

import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Collection;

/* loaded from: classes4.dex */
public abstract class AbstractIntCollection extends AbstractCollection<Integer> implements IntCollection {
    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
    public abstract IntIterator iterator();

    @Override // it.unimi.dsi.fastutil.ints.IntCollection
    public boolean add(int k) {
        throw new UnsupportedOperationException();
    }

    public boolean contains(int k) {
        IntIterator iterator = iterator();
        while (iterator.hasNext()) {
            if (k == iterator.nextInt()) {
                return true;
            }
        }
        return false;
    }

    @Override // it.unimi.dsi.fastutil.ints.IntCollection
    public boolean rem(int k) {
        IntIterator iterator = iterator();
        while (iterator.hasNext()) {
            if (k == iterator.nextInt()) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.AbstractCollection, java.util.Collection, it.unimi.dsi.fastutil.ints.IntCollection
    @Deprecated
    public boolean add(Integer key) {
        return super.add(key);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, it.unimi.dsi.fastutil.ints.IntCollection
    @Deprecated
    public boolean contains(Object key) {
        return super.contains(key);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, it.unimi.dsi.fastutil.ints.IntCollection
    @Deprecated
    public boolean remove(Object key) {
        return super.remove(key);
    }

    @Override // it.unimi.dsi.fastutil.ints.IntCollection
    public int[] toArray(int[] a) {
        int size = size();
        if (a == null) {
            a = new int[size];
        } else if (a.length < size) {
            a = Arrays.copyOf(a, size);
        }
        IntIterators.unwrap(iterator(), a);
        return a;
    }

    @Override // it.unimi.dsi.fastutil.ints.IntCollection
    public int[] toIntArray() {
        int size = size();
        if (size == 0) {
            return IntArrays.EMPTY_ARRAY;
        }
        int[] a = new int[size];
        IntIterators.unwrap(iterator(), a);
        return a;
    }

    @Override // it.unimi.dsi.fastutil.ints.IntCollection
    @Deprecated
    public int[] toIntArray(int[] a) {
        return toArray(a);
    }

    @Override // it.unimi.dsi.fastutil.ints.IntIterable
    public final void forEach(IntConsumer action) {
        super.forEach(action);
    }

    @Override // it.unimi.dsi.fastutil.ints.IntCollection
    public final boolean removeIf(IntPredicate filter) {
        return super.removeIf(filter);
    }

    @Override // it.unimi.dsi.fastutil.ints.IntCollection
    public boolean addAll(IntCollection c) {
        boolean retVal = false;
        IntIterator i = c.iterator();
        while (i.hasNext()) {
            if (add(i.nextInt())) {
                retVal = true;
            }
        }
        return retVal;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean addAll(Collection<? extends Integer> c) {
        if (c instanceof IntCollection) {
            return addAll((IntCollection) c);
        }
        return super.addAll(c);
    }

    @Override // it.unimi.dsi.fastutil.ints.IntCollection
    public boolean containsAll(IntCollection c) {
        IntIterator i = c.iterator();
        while (i.hasNext()) {
            if (!contains(i.nextInt())) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean containsAll(Collection<?> c) {
        if (c instanceof IntCollection) {
            return containsAll((IntCollection) c);
        }
        return super.containsAll(c);
    }

    @Override // it.unimi.dsi.fastutil.ints.IntCollection
    public boolean removeAll(IntCollection c) {
        boolean retVal = false;
        IntIterator i = c.iterator();
        while (i.hasNext()) {
            if (rem(i.nextInt())) {
                retVal = true;
            }
        }
        return retVal;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean removeAll(Collection<?> c) {
        if (c instanceof IntCollection) {
            return removeAll((IntCollection) c);
        }
        return super.removeAll(c);
    }

    @Override // it.unimi.dsi.fastutil.ints.IntCollection
    public boolean retainAll(IntCollection c) {
        boolean retVal = false;
        IntIterator i = iterator();
        while (i.hasNext()) {
            if (!c.contains(i.nextInt())) {
                i.remove();
                retVal = true;
            }
        }
        return retVal;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean retainAll(Collection<?> c) {
        if (c instanceof IntCollection) {
            return retainAll((IntCollection) c);
        }
        return super.retainAll(c);
    }

    @Override // java.util.AbstractCollection
    public String toString() {
        StringBuilder s = new StringBuilder();
        IntIterator i = iterator();
        int k = size();
        boolean first = true;
        s.append("{");
        while (true) {
            int n = k - 1;
            if (k != 0) {
                if (first) {
                    first = false;
                } else {
                    s.append(", ");
                }
                int k2 = i.nextInt();
                s.append(String.valueOf(k2));
                k = n;
            } else {
                s.append("}");
                return s.toString();
            }
        }
    }
}
