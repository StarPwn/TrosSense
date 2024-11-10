package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.longs.LongIterators;
import it.unimi.dsi.fastutil.longs.LongSpliterators;
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
public abstract class AbstractLongList extends AbstractLongCollection implements LongList, LongStack {
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

    @Override // it.unimi.dsi.fastutil.longs.LongList
    public void add(int index, long k) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
    public boolean add(long k) {
        add(size(), k);
        return true;
    }

    @Override // it.unimi.dsi.fastutil.longs.LongList
    public long removeLong(int i) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.longs.LongList
    public long set(int index, long k) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public boolean addAll(int index, Collection<? extends Long> c) {
        if (c instanceof LongCollection) {
            return addAll(index, (LongCollection) c);
        }
        ensureIndex(index);
        Iterator<? extends Long> i = c.iterator();
        boolean retVal = i.hasNext();
        while (i.hasNext()) {
            add(index, i.next().longValue());
            index++;
        }
        return retVal;
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, java.util.AbstractCollection, java.util.Collection
    public boolean addAll(Collection<? extends Long> c) {
        return addAll(size(), c);
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [it.unimi.dsi.fastutil.longs.LongListIterator] */
    @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
    public LongListIterator iterator() {
        return listIterator();
    }

    @Override // it.unimi.dsi.fastutil.longs.LongList, java.util.List
    public ListIterator<Long> listIterator2() {
        return listIterator2(0);
    }

    @Override // it.unimi.dsi.fastutil.longs.LongList, java.util.List
    /* renamed from: listIterator */
    public ListIterator<Long> listIterator2(int index) {
        ensureIndex(index);
        return new LongIterators.AbstractIndexBasedListIterator(0, index) { // from class: it.unimi.dsi.fastutil.longs.AbstractLongList.1
            @Override // it.unimi.dsi.fastutil.longs.LongIterators.AbstractIndexBasedIterator
            protected final long get(int i) {
                return AbstractLongList.this.getLong(i);
            }

            @Override // it.unimi.dsi.fastutil.longs.LongIterators.AbstractIndexBasedListIterator
            protected final void add(int i, long k) {
                AbstractLongList.this.add(i, k);
            }

            @Override // it.unimi.dsi.fastutil.longs.LongIterators.AbstractIndexBasedListIterator
            protected final void set(int i, long k) {
                AbstractLongList.this.set(i, k);
            }

            @Override // it.unimi.dsi.fastutil.longs.LongIterators.AbstractIndexBasedIterator
            protected final void remove(int i) {
                AbstractLongList.this.removeLong(i);
            }

            @Override // it.unimi.dsi.fastutil.longs.LongIterators.AbstractIndexBasedIterator
            protected final int getMaxPos() {
                return AbstractLongList.this.size();
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static final class IndexBasedSpliterator extends LongSpliterators.LateBindingSizeIndexBasedSpliterator {
        final LongList l;

        /* JADX INFO: Access modifiers changed from: package-private */
        public IndexBasedSpliterator(LongList l, int pos) {
            super(pos);
            this.l = l;
        }

        IndexBasedSpliterator(LongList l, int pos, int maxPos) {
            super(pos, maxPos);
            this.l = l;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterators.LateBindingSizeIndexBasedSpliterator
        protected final int getMaxPosFromBackingStore() {
            return this.l.size();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterators.AbstractIndexBasedSpliterator
        protected final long get(int i) {
            return this.l.getLong(i);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // it.unimi.dsi.fastutil.longs.LongSpliterators.AbstractIndexBasedSpliterator
        public final IndexBasedSpliterator makeForSplit(int pos, int maxPos) {
            return new IndexBasedSpliterator(this.l, pos, maxPos);
        }
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
    public boolean contains(long k) {
        return indexOf(k) >= 0;
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [it.unimi.dsi.fastutil.longs.LongListIterator] */
    @Override // it.unimi.dsi.fastutil.longs.LongList
    public int indexOf(long k) {
        ?? listIterator = listIterator();
        while (listIterator.hasNext()) {
            long e = listIterator.nextLong();
            if (k == e) {
                return listIterator.previousIndex();
            }
        }
        return -1;
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [it.unimi.dsi.fastutil.longs.LongListIterator] */
    @Override // it.unimi.dsi.fastutil.longs.LongList
    public int lastIndexOf(long k) {
        ?? listIterator2 = listIterator2(size());
        while (listIterator2.hasPrevious()) {
            long e = listIterator2.previousLong();
            if (k == e) {
                return listIterator2.nextIndex();
            }
        }
        return -1;
    }

    @Override // it.unimi.dsi.fastutil.longs.LongList
    public void size(int size) {
        int i = size();
        if (size > i) {
            while (true) {
                int i2 = i + 1;
                if (i >= size) {
                    return;
                }
                add(0L);
                i = i2;
            }
        } else {
            while (true) {
                int i3 = i - 1;
                if (i == size) {
                    return;
                }
                removeLong(i3);
                i = i3;
            }
        }
    }

    @Override // it.unimi.dsi.fastutil.longs.LongList, java.util.List
    /* renamed from: subList, reason: merged with bridge method [inline-methods] */
    public List<Long> subList2(int from, int to) {
        ensureIndex(from);
        ensureIndex(to);
        if (from <= to) {
            return this instanceof RandomAccess ? new LongRandomAccessSubList(this, from, to) : new LongSubList(this, from, to);
        }
        throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
    }

    @Override // it.unimi.dsi.fastutil.longs.LongIterable
    public void forEach(java.util.function.LongConsumer action) {
        if (this instanceof RandomAccess) {
            int max = size();
            for (int i = 0; i < max; i++) {
                action.accept(getLong(i));
            }
            return;
        }
        super.forEach(action);
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [it.unimi.dsi.fastutil.longs.LongListIterator] */
    @Override // it.unimi.dsi.fastutil.longs.LongList
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
                listIterator2.nextLong();
                listIterator2.remove();
                n = n2;
            } else {
                return;
            }
        }
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [it.unimi.dsi.fastutil.longs.LongListIterator] */
    @Override // it.unimi.dsi.fastutil.longs.LongList
    public void addElements(int index, long[] a, int offset, int index2) {
        ensureIndex(index);
        LongArrays.ensureOffsetLength(a, offset, index2);
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

    @Override // it.unimi.dsi.fastutil.longs.LongList
    public void addElements(int index, long[] a) {
        addElements(index, a, 0, a.length);
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [it.unimi.dsi.fastutil.longs.LongListIterator] */
    @Override // it.unimi.dsi.fastutil.longs.LongList
    public void getElements(int from, long[] a, int offset, int offset2) {
        ensureIndex(from);
        LongArrays.ensureOffsetLength(a, offset, offset2);
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
                a[offset] = getLong(current);
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
                a[offset] = listIterator2.nextLong();
                offset++;
                offset2 = length2;
            }
        }
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [it.unimi.dsi.fastutil.longs.LongListIterator] */
    @Override // it.unimi.dsi.fastutil.longs.LongList
    public void setElements(int index, long[] a, int offset, int length) {
        ensureIndex(index);
        LongArrays.ensureOffsetLength(a, offset, length);
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
            listIterator2.nextLong();
            listIterator2.set(a[i2 + offset]);
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        removeElements(0, size());
    }

    @Override // java.util.Collection, java.util.List
    public int hashCode() {
        LongIterator i = iterator();
        int h = 1;
        int s = size();
        while (true) {
            int s2 = s - 1;
            if (s != 0) {
                long k = i.nextLong();
                h = (h * 31) + HashCommon.long2int(k);
                s = s2;
            } else {
                return h;
            }
        }
    }

    /* JADX WARN: Type inference failed for: r4v3, types: [it.unimi.dsi.fastutil.longs.LongListIterator] */
    /* JADX WARN: Type inference failed for: r5v3, types: [it.unimi.dsi.fastutil.longs.LongListIterator] */
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
        if (l instanceof LongList) {
            ?? listIterator = listIterator();
            ?? listIterator2 = ((LongList) l).listIterator();
            while (true) {
                int s2 = s - 1;
                if (s == 0) {
                    return true;
                }
                if (listIterator.nextLong() != listIterator2.nextLong()) {
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

    /* JADX WARN: Type inference failed for: r1v2, types: [it.unimi.dsi.fastutil.longs.LongListIterator] */
    /* JADX WARN: Type inference failed for: r4v3, types: [it.unimi.dsi.fastutil.longs.LongListIterator] */
    @Override // java.lang.Comparable
    public int compareTo(List<? extends Long> l) {
        if (l == this) {
            return 0;
        }
        if (l instanceof LongList) {
            ?? listIterator = listIterator();
            ?? listIterator2 = ((LongList) l).listIterator();
            while (listIterator.hasNext() && listIterator2.hasNext()) {
                long e1 = listIterator.nextLong();
                long e2 = listIterator2.nextLong();
                int r = Long.compare(e1, e2);
                if (r != 0) {
                    return r;
                }
            }
            if (listIterator2.hasNext()) {
                return -1;
            }
            return listIterator.hasNext() ? 1 : 0;
        }
        ListIterator<? extends Long> i1 = listIterator();
        ListIterator<? extends Long> i2 = l.listIterator();
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

    @Override // it.unimi.dsi.fastutil.longs.LongStack
    public void push(long o) {
        add(o);
    }

    @Override // it.unimi.dsi.fastutil.longs.LongStack
    public long popLong() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return removeLong(size() - 1);
    }

    @Override // it.unimi.dsi.fastutil.longs.LongStack
    public long topLong() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return getLong(size() - 1);
    }

    @Override // it.unimi.dsi.fastutil.longs.LongStack
    public long peekLong(int i) {
        return getLong((size() - 1) - i);
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
    public boolean rem(long k) {
        int index = indexOf(k);
        if (index == -1) {
            return false;
        }
        removeLong(index);
        return true;
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
    public long[] toLongArray() {
        int size = size();
        if (size == 0) {
            return LongArrays.EMPTY_ARRAY;
        }
        long[] ret = new long[size];
        getElements(0, ret, 0, size);
        return ret;
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
    public long[] toArray(long[] a) {
        int size = size();
        if (a.length < size) {
            a = Arrays.copyOf(a, size);
        }
        getElements(0, a, 0, size);
        return a;
    }

    @Override // it.unimi.dsi.fastutil.longs.LongList
    public boolean addAll(int index, LongCollection c) {
        ensureIndex(index);
        LongIterator i = c.iterator();
        boolean retVal = i.hasNext();
        while (i.hasNext()) {
            add(index, i.nextLong());
            index++;
        }
        return retVal;
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
    public boolean addAll(LongCollection c) {
        return addAll(size(), c);
    }

    @Override // it.unimi.dsi.fastutil.longs.LongList
    public final void replaceAll(LongUnaryOperator operator) {
        replaceAll((java.util.function.LongUnaryOperator) operator);
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, java.util.AbstractCollection
    public String toString() {
        StringBuilder s = new StringBuilder();
        LongIterator i = iterator();
        int n = size();
        boolean first = true;
        s.append("[");
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
                s.append("]");
                return s.toString();
            }
        }
    }

    /* loaded from: classes4.dex */
    public static class LongSubList extends AbstractLongList implements Serializable {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private static final long serialVersionUID = -7046029254386353129L;
        protected final int from;
        protected final LongList l;
        protected int to;

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, java.lang.Comparable
        public /* bridge */ /* synthetic */ int compareTo(List<? extends Long> list) {
            return super.compareTo(list);
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.AbstractLongCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
        public /* bridge */ /* synthetic */ LongIterator iterator() {
            return super.iterator();
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.AbstractLongCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
        public /* bridge */ /* synthetic */ Iterator iterator() {
            return super.iterator();
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList, java.util.List
        public /* bridge */ /* synthetic */ ListIterator<Long> listIterator() {
            return super.listIterator();
        }

        public LongSubList(LongList l, int from, int to) {
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

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public boolean add(long k) {
            this.l.add(this.to, k);
            this.to++;
            if (assertRange()) {
                return true;
            }
            throw new AssertionError();
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList
        public void add(int index, long k) {
            ensureIndex(index);
            this.l.add(this.from + index, k);
            this.to++;
            if (!assertRange()) {
                throw new AssertionError();
            }
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, java.util.List
        public boolean addAll(int index, Collection<? extends Long> c) {
            ensureIndex(index);
            this.to += c.size();
            return this.l.addAll(this.from + index, c);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public long getLong(int index) {
            ensureRestrictedIndex(index);
            return this.l.getLong(this.from + index);
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList
        public long removeLong(int index) {
            ensureRestrictedIndex(index);
            this.to--;
            return this.l.removeLong(this.from + index);
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList
        public long set(int index, long k) {
            ensureRestrictedIndex(index);
            return this.l.set(this.from + index, k);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.to - this.from;
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList
        public void getElements(int from, long[] a, int offset, int length) {
            ensureIndex(from);
            if (from + length > size()) {
                throw new IndexOutOfBoundsException("End index (" + from + length + ") is greater than list size (" + size() + ")");
            }
            this.l.getElements(this.from + from, a, offset, length);
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList
        public void removeElements(int from, int to) {
            ensureIndex(from);
            ensureIndex(to);
            this.l.removeElements(this.from + from, this.from + to);
            this.to -= to - from;
            if (!assertRange()) {
                throw new AssertionError();
            }
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList
        public void addElements(int index, long[] a, int offset, int length) {
            ensureIndex(index);
            this.l.addElements(this.from + index, a, offset, length);
            this.to += length;
            if (!assertRange()) {
                throw new AssertionError();
            }
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList
        public void setElements(int index, long[] a, int offset, int length) {
            ensureIndex(index);
            this.l.setElements(this.from + index, a, offset, length);
            if (!assertRange()) {
                throw new AssertionError();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public final class RandomAccessIter extends LongIterators.AbstractIndexBasedListIterator {
            static final /* synthetic */ boolean $assertionsDisabled = false;

            RandomAccessIter(int pos) {
                super(0, pos);
            }

            @Override // it.unimi.dsi.fastutil.longs.LongIterators.AbstractIndexBasedIterator
            protected final long get(int i) {
                return LongSubList.this.l.getLong(LongSubList.this.from + i);
            }

            @Override // it.unimi.dsi.fastutil.longs.LongIterators.AbstractIndexBasedListIterator
            protected final void add(int i, long k) {
                LongSubList.this.add(i, k);
            }

            @Override // it.unimi.dsi.fastutil.longs.LongIterators.AbstractIndexBasedListIterator
            protected final void set(int i, long k) {
                LongSubList.this.set(i, k);
            }

            @Override // it.unimi.dsi.fastutil.longs.LongIterators.AbstractIndexBasedIterator
            protected final void remove(int i) {
                LongSubList.this.removeLong(i);
            }

            @Override // it.unimi.dsi.fastutil.longs.LongIterators.AbstractIndexBasedIterator
            protected final int getMaxPos() {
                return LongSubList.this.to - LongSubList.this.from;
            }

            @Override // it.unimi.dsi.fastutil.longs.LongIterators.AbstractIndexBasedListIterator, it.unimi.dsi.fastutil.longs.LongListIterator
            public void add(long k) {
                super.add(k);
                if (!LongSubList.this.assertRange()) {
                    throw new AssertionError();
                }
            }

            @Override // it.unimi.dsi.fastutil.longs.LongIterators.AbstractIndexBasedIterator, java.util.Iterator, it.unimi.dsi.fastutil.longs.LongListIterator, java.util.ListIterator
            public void remove() {
                super.remove();
                if (!LongSubList.this.assertRange()) {
                    throw new AssertionError();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public class ParentWrappingIter implements LongListIterator {
            private LongListIterator parent;

            ParentWrappingIter(LongListIterator parent) {
                this.parent = parent;
            }

            @Override // java.util.ListIterator
            public int nextIndex() {
                return this.parent.nextIndex() - LongSubList.this.from;
            }

            @Override // java.util.ListIterator
            public int previousIndex() {
                return this.parent.previousIndex() - LongSubList.this.from;
            }

            @Override // java.util.Iterator, java.util.ListIterator
            public boolean hasNext() {
                return this.parent.nextIndex() < LongSubList.this.to;
            }

            @Override // it.unimi.dsi.fastutil.BidirectionalIterator
            public boolean hasPrevious() {
                return this.parent.previousIndex() >= LongSubList.this.from;
            }

            @Override // it.unimi.dsi.fastutil.longs.LongIterator, java.util.PrimitiveIterator.OfLong
            public long nextLong() {
                if (hasNext()) {
                    return this.parent.nextLong();
                }
                throw new NoSuchElementException();
            }

            @Override // it.unimi.dsi.fastutil.longs.LongBidirectionalIterator
            public long previousLong() {
                if (hasPrevious()) {
                    return this.parent.previousLong();
                }
                throw new NoSuchElementException();
            }

            @Override // it.unimi.dsi.fastutil.longs.LongListIterator
            public void add(long k) {
                this.parent.add(k);
            }

            @Override // it.unimi.dsi.fastutil.longs.LongListIterator
            public void set(long k) {
                this.parent.set(k);
            }

            @Override // it.unimi.dsi.fastutil.longs.LongListIterator, java.util.Iterator, java.util.ListIterator
            public void remove() {
                this.parent.remove();
            }

            @Override // it.unimi.dsi.fastutil.longs.LongBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator
            public int back(int n) {
                if (n < 0) {
                    throw new IllegalArgumentException("Argument must be nonnegative: " + n);
                }
                int currentPos = this.parent.previousIndex();
                int parentNewPos = currentPos - n;
                if (parentNewPos < LongSubList.this.from - 1) {
                    parentNewPos = LongSubList.this.from - 1;
                }
                int toSkip = parentNewPos - currentPos;
                return this.parent.back(toSkip);
            }

            @Override // it.unimi.dsi.fastutil.longs.LongBidirectionalIterator, it.unimi.dsi.fastutil.longs.LongIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectIterator
            public int skip(int n) {
                if (n < 0) {
                    throw new IllegalArgumentException("Argument must be nonnegative: " + n);
                }
                int currentPos = this.parent.nextIndex();
                int parentNewPos = currentPos + n;
                if (parentNewPos > LongSubList.this.to) {
                    parentNewPos = LongSubList.this.to;
                }
                int toSkip = parentNewPos - currentPos;
                return this.parent.skip(toSkip);
            }
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [it.unimi.dsi.fastutil.longs.LongListIterator] */
        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList, java.util.List
        /* renamed from: listIterator, reason: merged with bridge method [inline-methods] */
        public ListIterator<Long> listIterator2(int index) {
            ensureIndex(index);
            return this.l instanceof RandomAccess ? new RandomAccessIter(index) : new ParentWrappingIter(this.l.listIterator2(this.from + index));
        }

        @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
        public LongSpliterator spliterator() {
            return this.l instanceof RandomAccess ? new IndexBasedSpliterator(this.l, this.from, this.to) : super.spliterator();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList, java.util.List
        /* renamed from: subList, reason: merged with bridge method [inline-methods] */
        public List<Long> subList(int from, int to) {
            ensureIndex(from);
            ensureIndex(to);
            if (from > to) {
                throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            return new LongSubList(this, from, to);
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
        public boolean rem(long k) {
            int index = indexOf(k);
            if (index == -1) {
                return false;
            }
            this.to--;
            this.l.removeLong(this.from + index);
            if (assertRange()) {
                return true;
            }
            throw new AssertionError();
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList
        public boolean addAll(int index, LongCollection c) {
            ensureIndex(index);
            return super.addAll(index, c);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongList
        public boolean addAll(int index, LongList l) {
            ensureIndex(index);
            return super.addAll(index, l);
        }
    }

    /* loaded from: classes4.dex */
    public static class LongRandomAccessSubList extends LongSubList implements RandomAccess {
        private static final long serialVersionUID = -107070782945191929L;

        public LongRandomAccessSubList(LongList l, int from, int to) {
            super(l, from, to);
        }

        @Override // it.unimi.dsi.fastutil.longs.AbstractLongList.LongSubList, it.unimi.dsi.fastutil.longs.AbstractLongList, it.unimi.dsi.fastutil.longs.LongList, java.util.List
        /* renamed from: subList, reason: merged with bridge method [inline-methods] */
        public List<Long> subList2(int from, int to) {
            ensureIndex(from);
            ensureIndex(to);
            if (from > to) {
                throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            return new LongRandomAccessSubList(this, from, to);
        }
    }
}
