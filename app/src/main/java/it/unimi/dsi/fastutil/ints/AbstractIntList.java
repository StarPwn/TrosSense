package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.ints.IntIterators;
import it.unimi.dsi.fastutil.ints.IntSpliterators;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.RandomAccess;

/* loaded from: classes4.dex */
public abstract class AbstractIntList extends AbstractIntCollection implements IntList, IntStack {
    /* JADX INFO: Access modifiers changed from: protected */
    public void ensureIndex(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is negative");
        }
        if (index > size()) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than list size (" + size() + ")");
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void ensureRestrictedIndex(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is negative");
        }
        if (index >= size()) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + size() + ")");
        }
    }

    @Override // it.unimi.dsi.fastutil.ints.IntList
    public void add(int index, int k) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
    public boolean add(int k) {
        add(size(), k);
        return true;
    }

    @Override // it.unimi.dsi.fastutil.ints.IntList
    public int removeInt(int i) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.ints.IntList
    public int set(int index, int k) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public boolean addAll(int index, Collection<? extends Integer> c) {
        if (c instanceof IntCollection) {
            return addAll(index, (IntCollection) c);
        }
        ensureIndex(index);
        Iterator<? extends Integer> i = c.iterator();
        boolean retVal = i.hasNext();
        while (i.hasNext()) {
            add(index, i.next().intValue());
            index++;
        }
        return retVal;
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, java.util.AbstractCollection, java.util.Collection
    public boolean addAll(Collection<? extends Integer> c) {
        return addAll(size(), c);
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [it.unimi.dsi.fastutil.ints.IntListIterator] */
    @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
    public IntListIterator iterator() {
        return listIterator();
    }

    @Override // it.unimi.dsi.fastutil.ints.IntList, java.util.List
    public ListIterator<Integer> listIterator2() {
        return listIterator2(0);
    }

    @Override // it.unimi.dsi.fastutil.ints.IntList, java.util.List
    /* renamed from: listIterator */
    public ListIterator<Integer> listIterator2(int index) {
        ensureIndex(index);
        return new IntIterators.AbstractIndexBasedListIterator(0, index) { // from class: it.unimi.dsi.fastutil.ints.AbstractIntList.1
            @Override // it.unimi.dsi.fastutil.ints.IntIterators.AbstractIndexBasedIterator
            protected final int get(int i) {
                return AbstractIntList.this.getInt(i);
            }

            @Override // it.unimi.dsi.fastutil.ints.IntIterators.AbstractIndexBasedListIterator
            protected final void add(int i, int k) {
                AbstractIntList.this.add(i, k);
            }

            @Override // it.unimi.dsi.fastutil.ints.IntIterators.AbstractIndexBasedListIterator
            protected final void set(int i, int k) {
                AbstractIntList.this.set(i, k);
            }

            @Override // it.unimi.dsi.fastutil.ints.IntIterators.AbstractIndexBasedIterator
            protected final void remove(int i) {
                AbstractIntList.this.removeInt(i);
            }

            @Override // it.unimi.dsi.fastutil.ints.IntIterators.AbstractIndexBasedIterator
            protected final int getMaxPos() {
                return AbstractIntList.this.size();
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static final class IndexBasedSpliterator extends IntSpliterators.LateBindingSizeIndexBasedSpliterator {
        final IntList l;

        /* JADX INFO: Access modifiers changed from: package-private */
        public IndexBasedSpliterator(IntList l, int pos) {
            super(pos);
            this.l = l;
        }

        IndexBasedSpliterator(IntList l, int pos, int maxPos) {
            super(pos, maxPos);
            this.l = l;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSpliterators.LateBindingSizeIndexBasedSpliterator
        protected final int getMaxPosFromBackingStore() {
            return this.l.size();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSpliterators.AbstractIndexBasedSpliterator
        protected final int get(int i) {
            return this.l.getInt(i);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // it.unimi.dsi.fastutil.ints.IntSpliterators.AbstractIndexBasedSpliterator
        public final IndexBasedSpliterator makeForSplit(int pos, int maxPos) {
            return new IndexBasedSpliterator(this.l, pos, maxPos);
        }
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
    public boolean contains(int k) {
        return indexOf(k) >= 0;
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [it.unimi.dsi.fastutil.ints.IntListIterator] */
    @Override // it.unimi.dsi.fastutil.ints.IntList
    public int indexOf(int k) {
        ?? listIterator = listIterator();
        while (listIterator.hasNext()) {
            int e = listIterator.nextInt();
            if (k == e) {
                return listIterator.previousIndex();
            }
        }
        return -1;
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [it.unimi.dsi.fastutil.ints.IntListIterator] */
    @Override // it.unimi.dsi.fastutil.ints.IntList
    public int lastIndexOf(int k) {
        ?? listIterator2 = listIterator2(size());
        while (listIterator2.hasPrevious()) {
            int e = listIterator2.previousInt();
            if (k == e) {
                return listIterator2.nextIndex();
            }
        }
        return -1;
    }

    @Override // it.unimi.dsi.fastutil.ints.IntList
    public void size(int size) {
        int i = size();
        if (size > i) {
            while (true) {
                int i2 = i + 1;
                if (i >= size) {
                    return;
                }
                add(0);
                i = i2;
            }
        } else {
            while (true) {
                int i3 = i - 1;
                if (i == size) {
                    return;
                }
                removeInt(i3);
                i = i3;
            }
        }
    }

    @Override // it.unimi.dsi.fastutil.ints.IntList, java.util.List
    /* renamed from: subList, reason: merged with bridge method [inline-methods] */
    public List<Integer> subList2(int from, int to) {
        ensureIndex(from);
        ensureIndex(to);
        if (from <= to) {
            return this instanceof RandomAccess ? new IntRandomAccessSubList(this, from, to) : new IntSubList(this, from, to);
        }
        throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
    }

    @Override // it.unimi.dsi.fastutil.ints.IntIterable
    public void forEach(java.util.function.IntConsumer action) {
        if (this instanceof RandomAccess) {
            int max = size();
            for (int i = 0; i < max; i++) {
                action.accept(getInt(i));
            }
            return;
        }
        super.forEach(action);
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [it.unimi.dsi.fastutil.ints.IntListIterator] */
    @Override // it.unimi.dsi.fastutil.ints.IntList
    public void removeElements(int from, int to) {
        ensureIndex(to);
        ?? listIterator2 = listIterator2(from);
        int n = to - from;
        if (n < 0) {
            throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        while (true) {
            int n2 = n - 1;
            if (n != 0) {
                listIterator2.nextInt();
                listIterator2.remove();
                n = n2;
            } else {
                return;
            }
        }
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [it.unimi.dsi.fastutil.ints.IntListIterator] */
    @Override // it.unimi.dsi.fastutil.ints.IntList
    public void addElements(int index, int[] a, int offset, int index2) {
        ensureIndex(index);
        IntArrays.ensureOffsetLength(a, offset, index2);
        if (this instanceof RandomAccess) {
            while (true) {
                int length = index2 - 1;
                if (index2 == 0) {
                    return;
                }
                add(index, a[offset]);
                index++;
                index2 = length;
                offset++;
            }
        } else {
            ?? listIterator2 = listIterator2(index);
            while (true) {
                int length2 = index2 - 1;
                if (index2 == 0) {
                    return;
                }
                listIterator2.add(a[offset]);
                offset++;
                index2 = length2;
            }
        }
    }

    @Override // it.unimi.dsi.fastutil.ints.IntList
    public void addElements(int index, int[] a) {
        addElements(index, a, 0, a.length);
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [it.unimi.dsi.fastutil.ints.IntListIterator] */
    @Override // it.unimi.dsi.fastutil.ints.IntList
    public void getElements(int from, int[] a, int offset, int offset2) {
        ensureIndex(from);
        IntArrays.ensureOffsetLength(a, offset, offset2);
        if (from + offset2 > size()) {
            throw new IndexOutOfBoundsException("End index (" + (from + offset2) + ") is greater than list size (" + size() + ")");
        }
        if (this instanceof RandomAccess) {
            int current = from;
            while (true) {
                int length = offset2 - 1;
                if (offset2 == 0) {
                    return;
                }
                a[offset] = getInt(current);
                offset++;
                offset2 = length;
                current++;
            }
        } else {
            ?? listIterator2 = listIterator2(from);
            while (true) {
                int length2 = offset2 - 1;
                if (offset2 == 0) {
                    return;
                }
                a[offset] = listIterator2.nextInt();
                offset++;
                offset2 = length2;
            }
        }
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [it.unimi.dsi.fastutil.ints.IntListIterator] */
    @Override // it.unimi.dsi.fastutil.ints.IntList
    public void setElements(int index, int[] a, int offset, int length) {
        ensureIndex(index);
        IntArrays.ensureOffsetLength(a, offset, length);
        if (index + length > size()) {
            throw new IndexOutOfBoundsException("End index (" + (index + length) + ") is greater than list size (" + size() + ")");
        }
        if (this instanceof RandomAccess) {
            for (int i = 0; i < length; i++) {
                set(i + index, a[i + offset]);
            }
            return;
        }
        ?? listIterator2 = listIterator2(index);
        for (int i2 = 0; i2 < length; i2++) {
            listIterator2.nextInt();
            listIterator2.set(a[i2 + offset]);
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        removeElements(0, size());
    }

    @Override // java.util.Collection, java.util.List
    public int hashCode() {
        IntIterator i = iterator();
        int h = 1;
        int k = size();
        while (true) {
            int s = k - 1;
            if (k != 0) {
                int k2 = i.nextInt();
                h = (h * 31) + k2;
                k = s;
            } else {
                return h;
            }
        }
    }

    /* JADX WARN: Type inference failed for: r4v3, types: [it.unimi.dsi.fastutil.ints.IntListIterator] */
    /* JADX WARN: Type inference failed for: r5v3, types: [it.unimi.dsi.fastutil.ints.IntListIterator] */
    @Override // java.util.Collection, java.util.List
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof List)) {
            return false;
        }
        List<?> l = (List) o;
        int s = size();
        if (s != l.size()) {
            return false;
        }
        if (l instanceof IntList) {
            ?? listIterator = listIterator();
            ?? listIterator2 = ((IntList) l).listIterator();
            while (true) {
                int s2 = s - 1;
                if (s == 0) {
                    return true;
                }
                if (listIterator.nextInt() != listIterator2.nextInt()) {
                    return false;
                }
                s = s2;
            }
        } else {
            ListIterator<?> i1 = listIterator();
            ListIterator<?> i2 = l.listIterator();
            while (true) {
                int s3 = s - 1;
                if (s == 0) {
                    return true;
                }
                if (!Objects.equals(i1.next(), i2.next())) {
                    return false;
                }
                s = s3;
            }
        }
    }

    /* JADX WARN: Type inference failed for: r1v2, types: [it.unimi.dsi.fastutil.ints.IntListIterator] */
    /* JADX WARN: Type inference failed for: r4v3, types: [it.unimi.dsi.fastutil.ints.IntListIterator] */
    @Override // java.lang.Comparable
    public int compareTo(List<? extends Integer> l) {
        if (l == this) {
            return 0;
        }
        if (l instanceof IntList) {
            ?? listIterator = listIterator();
            ?? listIterator2 = ((IntList) l).listIterator();
            while (listIterator.hasNext() && listIterator2.hasNext()) {
                int e1 = listIterator.nextInt();
                int e2 = listIterator2.nextInt();
                int r = Integer.compare(e1, e2);
                if (r != 0) {
                    return r;
                }
            }
            if (listIterator2.hasNext()) {
                return -1;
            }
            return listIterator.hasNext() ? 1 : 0;
        }
        ListIterator<? extends Integer> i1 = listIterator();
        ListIterator<? extends Integer> i2 = l.listIterator();
        while (i1.hasNext() && i2.hasNext()) {
            int r2 = i1.next().compareTo(i2.next());
            if (r2 != 0) {
                return r2;
            }
        }
        if (i2.hasNext()) {
            return -1;
        }
        return i1.hasNext() ? 1 : 0;
    }

    @Override // it.unimi.dsi.fastutil.ints.IntStack
    public void push(int o) {
        add(o);
    }

    @Override // it.unimi.dsi.fastutil.ints.IntStack
    public int popInt() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return removeInt(size() - 1);
    }

    @Override // it.unimi.dsi.fastutil.ints.IntStack
    public int topInt() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return getInt(size() - 1);
    }

    @Override // it.unimi.dsi.fastutil.ints.IntStack
    public int peekInt(int i) {
        return getInt((size() - 1) - i);
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
    public boolean rem(int k) {
        int index = indexOf(k);
        if (index == -1) {
            return false;
        }
        removeInt(index);
        return true;
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
    public int[] toIntArray() {
        int size = size();
        if (size == 0) {
            return IntArrays.EMPTY_ARRAY;
        }
        int[] ret = new int[size];
        getElements(0, ret, 0, size);
        return ret;
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
    public int[] toArray(int[] a) {
        int size = size();
        if (a.length < size) {
            a = Arrays.copyOf(a, size);
        }
        getElements(0, a, 0, size);
        return a;
    }

    @Override // it.unimi.dsi.fastutil.ints.IntList
    public boolean addAll(int index, IntCollection c) {
        ensureIndex(index);
        IntIterator i = c.iterator();
        boolean retVal = i.hasNext();
        while (i.hasNext()) {
            add(index, i.nextInt());
            index++;
        }
        return retVal;
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
    public boolean addAll(IntCollection c) {
        return addAll(size(), c);
    }

    @Override // it.unimi.dsi.fastutil.ints.IntList
    public final void replaceAll(IntUnaryOperator operator) {
        replaceAll((java.util.function.IntUnaryOperator) operator);
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, java.util.AbstractCollection
    public String toString() {
        StringBuilder s = new StringBuilder();
        IntIterator i = iterator();
        int k = size();
        boolean first = true;
        s.append("[");
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
                s.append("]");
                return s.toString();
            }
        }
    }

    /* loaded from: classes4.dex */
    public static class IntSubList extends AbstractIntList implements Serializable {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private static final long serialVersionUID = -7046029254386353129L;
        protected final int from;
        protected final IntList l;
        protected int to;

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, java.lang.Comparable
        public /* bridge */ /* synthetic */ int compareTo(List<? extends Integer> list) {
            return super.compareTo(list);
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.AbstractIntCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
        public /* bridge */ /* synthetic */ IntIterator iterator() {
            return super.iterator();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.AbstractIntCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
        public /* bridge */ /* synthetic */ Iterator iterator() {
            return super.iterator();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList, java.util.List
        public /* bridge */ /* synthetic */ ListIterator<Integer> listIterator() {
            return super.listIterator();
        }

        public IntSubList(IntList l, int from, int to) {
            this.l = l;
            this.from = from;
            this.to = to;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean assertRange() {
            if (this.from > this.l.size()) {
                throw new AssertionError();
            }
            if (this.to > this.l.size()) {
                throw new AssertionError();
            }
            if (this.to < this.from) {
                throw new AssertionError();
            }
            return true;
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public boolean add(int k) {
            this.l.add(this.to, k);
            this.to++;
            if (assertRange()) {
                return true;
            }
            throw new AssertionError();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList
        public void add(int index, int k) {
            ensureIndex(index);
            this.l.add(this.from + index, k);
            this.to++;
            if (!assertRange()) {
                throw new AssertionError();
            }
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, java.util.List
        public boolean addAll(int index, Collection<? extends Integer> c) {
            ensureIndex(index);
            this.to += c.size();
            return this.l.addAll(this.from + index, c);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public int getInt(int index) {
            ensureRestrictedIndex(index);
            return this.l.getInt(this.from + index);
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList
        public int removeInt(int index) {
            ensureRestrictedIndex(index);
            this.to--;
            return this.l.removeInt(this.from + index);
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList
        public int set(int index, int k) {
            ensureRestrictedIndex(index);
            return this.l.set(this.from + index, k);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.to - this.from;
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList
        public void getElements(int from, int[] a, int offset, int length) {
            ensureIndex(from);
            if (from + length > size()) {
                throw new IndexOutOfBoundsException("End index (" + from + length + ") is greater than list size (" + size() + ")");
            }
            this.l.getElements(this.from + from, a, offset, length);
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList
        public void removeElements(int from, int to) {
            ensureIndex(from);
            ensureIndex(to);
            this.l.removeElements(this.from + from, this.from + to);
            this.to -= to - from;
            if (!assertRange()) {
                throw new AssertionError();
            }
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList
        public void addElements(int index, int[] a, int offset, int length) {
            ensureIndex(index);
            this.l.addElements(this.from + index, a, offset, length);
            this.to += length;
            if (!assertRange()) {
                throw new AssertionError();
            }
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList
        public void setElements(int index, int[] a, int offset, int length) {
            ensureIndex(index);
            this.l.setElements(this.from + index, a, offset, length);
            if (!assertRange()) {
                throw new AssertionError();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public final class RandomAccessIter extends IntIterators.AbstractIndexBasedListIterator {
            static final /* synthetic */ boolean $assertionsDisabled = false;

            RandomAccessIter(int pos) {
                super(0, pos);
            }

            @Override // it.unimi.dsi.fastutil.ints.IntIterators.AbstractIndexBasedIterator
            protected final int get(int i) {
                return IntSubList.this.l.getInt(IntSubList.this.from + i);
            }

            @Override // it.unimi.dsi.fastutil.ints.IntIterators.AbstractIndexBasedListIterator
            protected final void add(int i, int k) {
                IntSubList.this.add(i, k);
            }

            @Override // it.unimi.dsi.fastutil.ints.IntIterators.AbstractIndexBasedListIterator
            protected final void set(int i, int k) {
                IntSubList.this.set(i, k);
            }

            @Override // it.unimi.dsi.fastutil.ints.IntIterators.AbstractIndexBasedIterator
            protected final void remove(int i) {
                IntSubList.this.removeInt(i);
            }

            @Override // it.unimi.dsi.fastutil.ints.IntIterators.AbstractIndexBasedIterator
            protected final int getMaxPos() {
                return IntSubList.this.to - IntSubList.this.from;
            }

            @Override // it.unimi.dsi.fastutil.ints.IntIterators.AbstractIndexBasedListIterator, it.unimi.dsi.fastutil.ints.IntListIterator
            public void add(int k) {
                super.add(k);
                if (!IntSubList.this.assertRange()) {
                    throw new AssertionError();
                }
            }

            @Override // it.unimi.dsi.fastutil.ints.IntIterators.AbstractIndexBasedIterator, java.util.Iterator, it.unimi.dsi.fastutil.ints.IntListIterator, java.util.ListIterator
            public void remove() {
                super.remove();
                if (!IntSubList.this.assertRange()) {
                    throw new AssertionError();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public class ParentWrappingIter implements IntListIterator {
            private IntListIterator parent;

            ParentWrappingIter(IntListIterator parent) {
                this.parent = parent;
            }

            @Override // java.util.ListIterator
            public int nextIndex() {
                return this.parent.nextIndex() - IntSubList.this.from;
            }

            @Override // java.util.ListIterator
            public int previousIndex() {
                return this.parent.previousIndex() - IntSubList.this.from;
            }

            @Override // java.util.Iterator, java.util.ListIterator
            public boolean hasNext() {
                return this.parent.nextIndex() < IntSubList.this.to;
            }

            @Override // it.unimi.dsi.fastutil.BidirectionalIterator
            public boolean hasPrevious() {
                return this.parent.previousIndex() >= IntSubList.this.from;
            }

            @Override // it.unimi.dsi.fastutil.ints.IntIterator, java.util.PrimitiveIterator.OfInt
            public int nextInt() {
                if (hasNext()) {
                    return this.parent.nextInt();
                }
                throw new NoSuchElementException();
            }

            @Override // it.unimi.dsi.fastutil.ints.IntBidirectionalIterator
            public int previousInt() {
                if (hasPrevious()) {
                    return this.parent.previousInt();
                }
                throw new NoSuchElementException();
            }

            @Override // it.unimi.dsi.fastutil.ints.IntListIterator
            public void add(int k) {
                this.parent.add(k);
            }

            @Override // it.unimi.dsi.fastutil.ints.IntListIterator
            public void set(int k) {
                this.parent.set(k);
            }

            @Override // it.unimi.dsi.fastutil.ints.IntListIterator, java.util.Iterator, java.util.ListIterator
            public void remove() {
                this.parent.remove();
            }

            @Override // it.unimi.dsi.fastutil.ints.IntBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator
            public int back(int n) {
                if (n < 0) {
                    throw new IllegalArgumentException("Argument must be nonnegative: " + n);
                }
                int currentPos = this.parent.previousIndex();
                int parentNewPos = currentPos - n;
                if (parentNewPos < IntSubList.this.from - 1) {
                    parentNewPos = IntSubList.this.from - 1;
                }
                int toSkip = parentNewPos - currentPos;
                return this.parent.back(toSkip);
            }

            @Override // it.unimi.dsi.fastutil.ints.IntBidirectionalIterator, it.unimi.dsi.fastutil.ints.IntIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectIterator
            public int skip(int n) {
                if (n < 0) {
                    throw new IllegalArgumentException("Argument must be nonnegative: " + n);
                }
                int currentPos = this.parent.nextIndex();
                int parentNewPos = currentPos + n;
                if (parentNewPos > IntSubList.this.to) {
                    parentNewPos = IntSubList.this.to;
                }
                int toSkip = parentNewPos - currentPos;
                return this.parent.skip(toSkip);
            }
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [it.unimi.dsi.fastutil.ints.IntListIterator] */
        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList, java.util.List
        /* renamed from: listIterator, reason: merged with bridge method [inline-methods] */
        public ListIterator<Integer> listIterator2(int index) {
            ensureIndex(index);
            return this.l instanceof RandomAccess ? new RandomAccessIter(index) : new ParentWrappingIter(this.l.listIterator2(this.from + index));
        }

        @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
        public IntSpliterator spliterator() {
            return this.l instanceof RandomAccess ? new IndexBasedSpliterator(this.l, this.from, this.to) : super.spliterator();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList, java.util.List
        /* renamed from: subList, reason: merged with bridge method [inline-methods] */
        public List<Integer> subList(int from, int to) {
            ensureIndex(from);
            ensureIndex(to);
            if (from > to) {
                throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            return new IntSubList(this, from, to);
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
        public boolean rem(int k) {
            int index = indexOf(k);
            if (index == -1) {
                return false;
            }
            this.to--;
            this.l.removeInt(this.from + index);
            if (assertRange()) {
                return true;
            }
            throw new AssertionError();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList
        public boolean addAll(int index, IntCollection c) {
            ensureIndex(index);
            return super.addAll(index, c);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntList
        public boolean addAll(int index, IntList l) {
            ensureIndex(index);
            return super.addAll(index, l);
        }
    }

    /* loaded from: classes4.dex */
    public static class IntRandomAccessSubList extends IntSubList implements RandomAccess {
        private static final long serialVersionUID = -107070782945191929L;

        public IntRandomAccessSubList(IntList l, int from, int to) {
            super(l, from, to);
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractIntList.IntSubList, it.unimi.dsi.fastutil.ints.AbstractIntList, it.unimi.dsi.fastutil.ints.IntList, java.util.List
        /* renamed from: subList, reason: merged with bridge method [inline-methods] */
        public List<Integer> subList2(int from, int to) {
            ensureIndex(from);
            ensureIndex(to);
            if (from > to) {
                throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            return new IntRandomAccessSubList(this, from, to);
        }
    }
}
