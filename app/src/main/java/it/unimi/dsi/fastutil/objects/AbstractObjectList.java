package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Stack;
import it.unimi.dsi.fastutil.objects.ObjectIterators;
import it.unimi.dsi.fastutil.objects.ObjectSpliterators;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.function.Consumer;

/* loaded from: classes4.dex */
public abstract class AbstractObjectList<K> extends AbstractObjectCollection<K> implements ObjectList<K>, Stack<K> {
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

    @Override // java.util.List
    public void add(int index, K k) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(K k) {
        add(size(), k);
        return true;
    }

    @Override // java.util.List
    public K remove(int i) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public K set(int index, K k) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public boolean addAll(int index, Collection<? extends K> c) {
        ensureIndex(index);
        Iterator<? extends K> i = c.iterator();
        boolean retVal = i.hasNext();
        while (i.hasNext()) {
            add(index, i.next());
            index++;
        }
        return retVal;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean addAll(Collection<? extends K> c) {
        return addAll(size(), c);
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObjectCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
    public ObjectListIterator<K> iterator() {
        return listIterator();
    }

    @Override // it.unimi.dsi.fastutil.objects.ObjectList, java.util.List
    public ObjectListIterator<K> listIterator() {
        return listIterator(0);
    }

    @Override // it.unimi.dsi.fastutil.objects.ObjectList, java.util.List
    public ObjectListIterator<K> listIterator(int index) {
        ensureIndex(index);
        return new ObjectIterators.AbstractIndexBasedListIterator<K>(0, index) { // from class: it.unimi.dsi.fastutil.objects.AbstractObjectList.1
            @Override // it.unimi.dsi.fastutil.objects.ObjectIterators.AbstractIndexBasedIterator
            protected final K get(int i) {
                return (K) AbstractObjectList.this.get(i);
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectIterators.AbstractIndexBasedListIterator
            protected final void add(int i, K k) {
                AbstractObjectList.this.add(i, k);
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectIterators.AbstractIndexBasedListIterator
            protected final void set(int i, K k) {
                AbstractObjectList.this.set(i, k);
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectIterators.AbstractIndexBasedIterator
            protected final void remove(int i) {
                AbstractObjectList.this.remove(i);
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectIterators.AbstractIndexBasedIterator
            protected final int getMaxPos() {
                return AbstractObjectList.this.size();
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static final class IndexBasedSpliterator<K> extends ObjectSpliterators.LateBindingSizeIndexBasedSpliterator<K> {
        final ObjectList<K> l;

        /* JADX INFO: Access modifiers changed from: package-private */
        public IndexBasedSpliterator(ObjectList<K> l, int pos) {
            super(pos);
            this.l = l;
        }

        IndexBasedSpliterator(ObjectList<K> l, int pos, int maxPos) {
            super(pos, maxPos);
            this.l = l;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterators.LateBindingSizeIndexBasedSpliterator
        protected final int getMaxPosFromBackingStore() {
            return this.l.size();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterators.AbstractIndexBasedSpliterator
        protected final K get(int i) {
            return this.l.get(i);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterators.AbstractIndexBasedSpliterator
        public final IndexBasedSpliterator<K> makeForSplit(int pos, int maxPos) {
            return new IndexBasedSpliterator<>(this.l, pos, maxPos);
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean contains(Object k) {
        return indexOf(k) >= 0;
    }

    @Override // java.util.List
    public int indexOf(Object k) {
        ObjectListIterator<K> i = listIterator();
        while (i.hasNext()) {
            K e = i.next();
            if (Objects.equals(k, e)) {
                return i.previousIndex();
            }
        }
        return -1;
    }

    @Override // java.util.List
    public int lastIndexOf(Object k) {
        ObjectListIterator<K> i = listIterator(size());
        while (i.hasPrevious()) {
            K e = i.previous();
            if (Objects.equals(k, e)) {
                return i.nextIndex();
            }
        }
        return -1;
    }

    @Override // it.unimi.dsi.fastutil.objects.ObjectList
    public void size(int size) {
        int i = size();
        if (size > i) {
            while (true) {
                int i2 = i + 1;
                if (i >= size) {
                    return;
                }
                add(null);
                i = i2;
            }
        } else {
            while (true) {
                int i3 = i - 1;
                if (i == size) {
                    return;
                }
                remove(i3);
                i = i3;
            }
        }
    }

    @Override // it.unimi.dsi.fastutil.objects.ObjectList, java.util.List
    public ObjectList<K> subList(int from, int to) {
        ensureIndex(from);
        ensureIndex(to);
        if (from <= to) {
            return this instanceof RandomAccess ? new ObjectRandomAccessSubList(this, from, to) : new ObjectSubList(this, from, to);
        }
        throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
    }

    @Override // java.lang.Iterable
    public void forEach(Consumer<? super K> consumer) {
        if (this instanceof RandomAccess) {
            int size = size();
            for (int i = 0; i < size; i++) {
                consumer.accept((Object) get(i));
            }
            return;
        }
        super.forEach(consumer);
    }

    @Override // it.unimi.dsi.fastutil.objects.ObjectList
    public void removeElements(int from, int to) {
        ensureIndex(to);
        ObjectListIterator<K> i = listIterator(from);
        int n = to - from;
        if (n < 0) {
            throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        while (true) {
            int n2 = n - 1;
            if (n != 0) {
                i.next();
                i.remove();
                n = n2;
            } else {
                return;
            }
        }
    }

    @Override // it.unimi.dsi.fastutil.objects.ObjectList
    public void addElements(int index, K[] a, int offset, int index2) {
        ensureIndex(index);
        ObjectArrays.ensureOffsetLength(a, offset, index2);
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
            ObjectListIterator<K> iter = listIterator(index);
            while (true) {
                int length2 = index2 - 1;
                if (index2 == 0) {
                    return;
                }
                iter.add(a[offset]);
                offset++;
                index2 = length2;
            }
        }
    }

    @Override // it.unimi.dsi.fastutil.objects.ObjectList
    public void addElements(int index, K[] a) {
        addElements(index, a, 0, a.length);
    }

    @Override // it.unimi.dsi.fastutil.objects.ObjectList
    public void getElements(int from, Object[] a, int offset, int offset2) {
        ensureIndex(from);
        ObjectArrays.ensureOffsetLength(a, offset, offset2);
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
                a[offset] = get(current);
                offset++;
                offset2 = length;
                current++;
            }
        } else {
            ObjectListIterator<K> i = listIterator(from);
            while (true) {
                int length2 = offset2 - 1;
                if (offset2 == 0) {
                    return;
                }
                a[offset] = i.next();
                offset++;
                offset2 = length2;
            }
        }
    }

    @Override // it.unimi.dsi.fastutil.objects.ObjectList
    public void setElements(int index, K[] a, int offset, int length) {
        ensureIndex(index);
        ObjectArrays.ensureOffsetLength(a, offset, length);
        if (index + length > size()) {
            throw new IndexOutOfBoundsException("End index (" + (index + length) + ") is greater than list size (" + size() + ")");
        }
        if (this instanceof RandomAccess) {
            for (int i = 0; i < length; i++) {
                set(i + index, a[i + offset]);
            }
            return;
        }
        ObjectListIterator<K> iter = listIterator(index);
        for (int i2 = 0; i2 < length; i2++) {
            iter.next();
            iter.set(a[i2 + offset]);
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        removeElements(0, size());
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public Object[] toArray() {
        int size = size();
        if (size == 0) {
            return ObjectArrays.EMPTY_ARRAY;
        }
        Object[] ret = new Object[size];
        getElements(0, ret, 0, size);
        return ret;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public <T> T[] toArray(T[] tArr) {
        int size = size();
        if (tArr.length < size) {
            tArr = (T[]) Arrays.copyOf(tArr, size);
        }
        getElements(0, tArr, 0, size);
        if (tArr.length > size) {
            tArr[size] = null;
        }
        return tArr;
    }

    @Override // java.util.Collection, java.util.List
    public int hashCode() {
        ObjectIterator<K> i = iterator();
        int h = 1;
        int s = size();
        while (true) {
            int s2 = s - 1;
            if (s != 0) {
                K k = i.next();
                h = (h * 31) + (k == null ? 0 : k.hashCode());
                s = s2;
            } else {
                return h;
            }
        }
    }

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
        ListIterator<?> i1 = listIterator();
        ListIterator<?> i2 = l.listIterator();
        while (true) {
            int s2 = s - 1;
            if (s == 0) {
                return true;
            }
            if (!Objects.equals(i1.next(), i2.next())) {
                return false;
            }
            s = s2;
        }
    }

    @Override // java.lang.Comparable
    public int compareTo(List<? extends K> l) {
        if (l == this) {
            return 0;
        }
        if (l instanceof ObjectList) {
            ObjectListIterator<K> i1 = listIterator();
            ObjectListIterator<K> i2 = ((ObjectList) l).listIterator();
            while (i1.hasNext() && i2.hasNext()) {
                K e1 = i1.next();
                K e2 = i2.next();
                int r = ((Comparable) e1).compareTo(e2);
                if (r != 0) {
                    return r;
                }
            }
            if (i2.hasNext()) {
                return -1;
            }
            return i1.hasNext() ? 1 : 0;
        }
        ListIterator<? extends K> i12 = listIterator();
        ListIterator<? extends K> i22 = l.listIterator();
        while (i12.hasNext() && i22.hasNext()) {
            int r2 = ((Comparable) i12.next()).compareTo(i22.next());
            if (r2 != 0) {
                return r2;
            }
        }
        if (i22.hasNext()) {
            return -1;
        }
        return i12.hasNext() ? 1 : 0;
    }

    @Override // it.unimi.dsi.fastutil.Stack
    public void push(K o) {
        add(o);
    }

    @Override // it.unimi.dsi.fastutil.Stack
    public K pop() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return remove(size() - 1);
    }

    @Override // it.unimi.dsi.fastutil.Stack
    public K top() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return (K) get(size() - 1);
    }

    @Override // it.unimi.dsi.fastutil.Stack
    public K peek(int i) {
        return (K) get((size() - 1) - i);
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObjectCollection, java.util.AbstractCollection
    public String toString() {
        StringBuilder s = new StringBuilder();
        ObjectIterator<K> i = iterator();
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
                K k = i.next();
                if (this == k) {
                    s.append("(this list)");
                } else {
                    s.append(String.valueOf(k));
                }
                n = n2;
            } else {
                s.append("]");
                return s.toString();
            }
        }
    }

    /* loaded from: classes4.dex */
    public static class ObjectSubList<K> extends AbstractObjectList<K> implements Serializable {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private static final long serialVersionUID = -7046029254386353129L;
        protected final int from;
        protected final ObjectList<K> l;
        protected int to;

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.lang.Comparable
        public /* bridge */ /* synthetic */ int compareTo(Object obj) {
            return super.compareTo((List) obj);
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, it.unimi.dsi.fastutil.objects.AbstractObjectCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
        public /* bridge */ /* synthetic */ ObjectIterator iterator() {
            return super.iterator();
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, it.unimi.dsi.fastutil.objects.AbstractObjectCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
        public /* bridge */ /* synthetic */ Iterator iterator() {
            return super.iterator();
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, it.unimi.dsi.fastutil.objects.ObjectList, java.util.List
        public /* bridge */ /* synthetic */ ListIterator listIterator() {
            return super.listIterator();
        }

        public ObjectSubList(ObjectList<K> l, int from, int to) {
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

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean add(K k) {
            this.l.add(this.to, k);
            this.to++;
            if (assertRange()) {
                return true;
            }
            throw new AssertionError();
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.util.List
        public void add(int index, K k) {
            ensureIndex(index);
            this.l.add(this.from + index, k);
            this.to++;
            if (!assertRange()) {
                throw new AssertionError();
            }
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.util.List
        public boolean addAll(int index, Collection<? extends K> c) {
            ensureIndex(index);
            this.to += c.size();
            return this.l.addAll(this.from + index, c);
        }

        @Override // java.util.List
        public K get(int index) {
            ensureRestrictedIndex(index);
            return this.l.get(this.from + index);
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.util.List
        public K remove(int index) {
            ensureRestrictedIndex(index);
            this.to--;
            return this.l.remove(this.from + index);
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.util.List
        public K set(int index, K k) {
            ensureRestrictedIndex(index);
            return this.l.set(this.from + index, k);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.to - this.from;
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, it.unimi.dsi.fastutil.objects.ObjectList
        public void getElements(int from, Object[] a, int offset, int length) {
            ensureIndex(from);
            if (from + length > size()) {
                throw new IndexOutOfBoundsException("End index (" + from + length + ") is greater than list size (" + size() + ")");
            }
            this.l.getElements(this.from + from, a, offset, length);
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, it.unimi.dsi.fastutil.objects.ObjectList
        public void removeElements(int from, int to) {
            ensureIndex(from);
            ensureIndex(to);
            this.l.removeElements(this.from + from, this.from + to);
            this.to -= to - from;
            if (!assertRange()) {
                throw new AssertionError();
            }
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, it.unimi.dsi.fastutil.objects.ObjectList
        public void addElements(int index, K[] a, int offset, int length) {
            ensureIndex(index);
            this.l.addElements(this.from + index, a, offset, length);
            this.to += length;
            if (!assertRange()) {
                throw new AssertionError();
            }
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, it.unimi.dsi.fastutil.objects.ObjectList
        public void setElements(int index, K[] a, int offset, int length) {
            ensureIndex(index);
            this.l.setElements(this.from + index, a, offset, length);
            if (!assertRange()) {
                throw new AssertionError();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public final class RandomAccessIter extends ObjectIterators.AbstractIndexBasedListIterator<K> {
            static final /* synthetic */ boolean $assertionsDisabled = false;

            RandomAccessIter(int pos) {
                super(0, pos);
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectIterators.AbstractIndexBasedIterator
            protected final K get(int i) {
                return ObjectSubList.this.l.get(ObjectSubList.this.from + i);
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectIterators.AbstractIndexBasedListIterator
            protected final void add(int i, K k) {
                ObjectSubList.this.add(i, k);
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectIterators.AbstractIndexBasedListIterator
            protected final void set(int i, K k) {
                ObjectSubList.this.set(i, k);
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectIterators.AbstractIndexBasedIterator
            protected final void remove(int i) {
                ObjectSubList.this.remove(i);
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectIterators.AbstractIndexBasedIterator
            protected final int getMaxPos() {
                return ObjectSubList.this.to - ObjectSubList.this.from;
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectIterators.AbstractIndexBasedListIterator, it.unimi.dsi.fastutil.objects.ObjectListIterator, java.util.ListIterator
            public void add(K k) {
                super.add(k);
                if (!ObjectSubList.this.assertRange()) {
                    throw new AssertionError();
                }
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectIterators.AbstractIndexBasedIterator, java.util.Iterator, it.unimi.dsi.fastutil.objects.ObjectListIterator, java.util.ListIterator
            public void remove() {
                super.remove();
                if (!ObjectSubList.this.assertRange()) {
                    throw new AssertionError();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public class ParentWrappingIter implements ObjectListIterator<K> {
            private ObjectListIterator<K> parent;

            ParentWrappingIter(ObjectListIterator<K> parent) {
                this.parent = parent;
            }

            @Override // java.util.ListIterator
            public int nextIndex() {
                return this.parent.nextIndex() - ObjectSubList.this.from;
            }

            @Override // java.util.ListIterator
            public int previousIndex() {
                return this.parent.previousIndex() - ObjectSubList.this.from;
            }

            @Override // java.util.Iterator, java.util.ListIterator
            public boolean hasNext() {
                return this.parent.nextIndex() < ObjectSubList.this.to;
            }

            @Override // it.unimi.dsi.fastutil.BidirectionalIterator
            public boolean hasPrevious() {
                return this.parent.previousIndex() >= ObjectSubList.this.from;
            }

            @Override // java.util.Iterator, java.util.ListIterator
            public K next() {
                if (hasNext()) {
                    return this.parent.next();
                }
                throw new NoSuchElementException();
            }

            @Override // it.unimi.dsi.fastutil.BidirectionalIterator, java.util.ListIterator
            public K previous() {
                if (hasPrevious()) {
                    return this.parent.previous();
                }
                throw new NoSuchElementException();
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectListIterator, java.util.ListIterator
            public void add(K k) {
                this.parent.add(k);
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectListIterator, java.util.ListIterator
            public void set(K k) {
                this.parent.set(k);
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectListIterator, java.util.Iterator, java.util.ListIterator
            public void remove() {
                this.parent.remove();
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator
            public int back(int n) {
                if (n < 0) {
                    throw new IllegalArgumentException("Argument must be nonnegative: " + n);
                }
                int currentPos = this.parent.previousIndex();
                int parentNewPos = currentPos - n;
                if (parentNewPos < ObjectSubList.this.from - 1) {
                    parentNewPos = ObjectSubList.this.from - 1;
                }
                int toSkip = parentNewPos - currentPos;
                return this.parent.back(toSkip);
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectIterator
            public int skip(int n) {
                if (n < 0) {
                    throw new IllegalArgumentException("Argument must be nonnegative: " + n);
                }
                int currentPos = this.parent.nextIndex();
                int parentNewPos = currentPos + n;
                if (parentNewPos > ObjectSubList.this.to) {
                    parentNewPos = ObjectSubList.this.to;
                }
                int toSkip = parentNewPos - currentPos;
                return this.parent.skip(toSkip);
            }
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, it.unimi.dsi.fastutil.objects.ObjectList, java.util.List
        public ObjectListIterator<K> listIterator(int index) {
            ensureIndex(index);
            return this.l instanceof RandomAccess ? new RandomAccessIter(index) : new ParentWrappingIter(this.l.listIterator(this.from + index));
        }

        @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
        /* renamed from: spliterator */
        public ObjectSpliterator<K> mo221spliterator() {
            return this.l instanceof RandomAccess ? new IndexBasedSpliterator(this.l, this.from, this.to) : super.mo221spliterator();
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, it.unimi.dsi.fastutil.objects.ObjectList, java.util.List
        public ObjectList<K> subList(int from, int to) {
            ensureIndex(from);
            ensureIndex(to);
            if (from > to) {
                throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            return new ObjectSubList(this, from, to);
        }
    }

    /* loaded from: classes4.dex */
    public static class ObjectRandomAccessSubList<K> extends ObjectSubList<K> implements RandomAccess {
        private static final long serialVersionUID = -107070782945191929L;

        public ObjectRandomAccessSubList(ObjectList<K> l, int from, int to) {
            super(l, from, to);
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList.ObjectSubList, it.unimi.dsi.fastutil.objects.AbstractObjectList, it.unimi.dsi.fastutil.objects.ObjectList, java.util.List
        public ObjectList<K> subList(int from, int to) {
            ensureIndex(from);
            ensureIndex(to);
            if (from > to) {
                throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            return new ObjectRandomAccessSubList(this, from, to);
        }
    }
}
