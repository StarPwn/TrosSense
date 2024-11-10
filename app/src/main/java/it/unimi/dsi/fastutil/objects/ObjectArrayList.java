package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.objects.AbstractObjectList;
import it.unimi.dsi.fastutil.objects.ObjectCollections;
import it.unimi.dsi.fastutil.objects.ObjectIterators;
import it.unimi.dsi.fastutil.objects.ObjectSpliterators;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Collector;

/* loaded from: classes4.dex */
public class ObjectArrayList<K> extends AbstractObjectList<K> implements RandomAccess, Cloneable, Serializable {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final int DEFAULT_INITIAL_CAPACITY = 10;
    private static final Collector<Object, ?, ObjectArrayList<Object>> TO_LIST_COLLECTOR = Collector.of(new ObjectArrayList$$ExternalSyntheticLambda0(), new ObjectArrayList$$ExternalSyntheticLambda1(), new ObjectArrayList$$ExternalSyntheticLambda2(), new Collector.Characteristics[0]);
    private static final long serialVersionUID = -7046029254386353131L;
    protected transient K[] a;
    protected int size;
    protected final boolean wrapped;

    private static final <K> K[] copyArraySafe(K[] kArr, int i) {
        return i == 0 ? (K[]) ObjectArrays.EMPTY_ARRAY : (K[]) Arrays.copyOf(kArr, i, Object[].class);
    }

    private static final <K> K[] copyArrayFromSafe(ObjectArrayList<K> objectArrayList) {
        return (K[]) copyArraySafe(objectArrayList.a, objectArrayList.size);
    }

    protected ObjectArrayList(K[] a, boolean wrapped) {
        this.a = a;
        this.wrapped = wrapped;
    }

    private void initArrayFromCapacity(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("Initial capacity (" + i + ") is negative");
        }
        if (i != 0) {
            this.a = (K[]) new Object[i];
        } else {
            this.a = (K[]) ObjectArrays.EMPTY_ARRAY;
        }
    }

    public ObjectArrayList(int capacity) {
        initArrayFromCapacity(capacity);
        this.wrapped = false;
    }

    public ObjectArrayList() {
        this.a = (K[]) ObjectArrays.DEFAULT_EMPTY_ARRAY;
        this.wrapped = false;
    }

    public ObjectArrayList(Collection<? extends K> collection) {
        if (collection instanceof ObjectArrayList) {
            this.a = (K[]) copyArrayFromSafe((ObjectArrayList) collection);
            this.size = this.a.length;
        } else {
            initArrayFromCapacity(collection.size());
            if (collection instanceof ObjectList) {
                K[] kArr = this.a;
                int size = collection.size();
                this.size = size;
                ((ObjectList) collection).getElements(0, kArr, 0, size);
            } else {
                this.size = ObjectIterators.unwrap(collection.iterator(), this.a);
            }
        }
        this.wrapped = false;
    }

    public ObjectArrayList(ObjectCollection<? extends K> objectCollection) {
        if (objectCollection instanceof ObjectArrayList) {
            this.a = (K[]) copyArrayFromSafe((ObjectArrayList) objectCollection);
            this.size = this.a.length;
        } else {
            initArrayFromCapacity(objectCollection.size());
            if (objectCollection instanceof ObjectList) {
                K[] kArr = this.a;
                int size = objectCollection.size();
                this.size = size;
                ((ObjectList) objectCollection).getElements(0, kArr, 0, size);
            } else {
                this.size = ObjectIterators.unwrap(objectCollection.iterator(), this.a);
            }
        }
        this.wrapped = false;
    }

    public ObjectArrayList(ObjectList<? extends K> objectList) {
        if (objectList instanceof ObjectArrayList) {
            this.a = (K[]) copyArrayFromSafe((ObjectArrayList) objectList);
            this.size = this.a.length;
        } else {
            initArrayFromCapacity(objectList.size());
            K[] kArr = this.a;
            int size = objectList.size();
            this.size = size;
            objectList.getElements(0, kArr, 0, size);
        }
        this.wrapped = false;
    }

    public ObjectArrayList(K[] a) {
        this(a, 0, a.length);
    }

    public ObjectArrayList(K[] a, int offset, int length) {
        this(length);
        System.arraycopy(a, offset, this.a, 0, length);
        this.size = length;
    }

    public ObjectArrayList(Iterator<? extends K> i) {
        this();
        while (i.hasNext()) {
            add(i.next());
        }
    }

    public ObjectArrayList(ObjectIterator<? extends K> i) {
        this();
        while (i.hasNext()) {
            add(i.next());
        }
    }

    public K[] elements() {
        return this.a;
    }

    public static <K> ObjectArrayList<K> wrap(K[] a, int length) {
        if (length > a.length) {
            throw new IllegalArgumentException("The specified length (" + length + ") is greater than the array size (" + a.length + ")");
        }
        ObjectArrayList<K> l = new ObjectArrayList<>(a, true);
        l.size = length;
        return l;
    }

    public static <K> ObjectArrayList<K> wrap(K[] a) {
        return wrap(a, a.length);
    }

    public static <K> ObjectArrayList<K> of() {
        return new ObjectArrayList<>();
    }

    @SafeVarargs
    public static <K> ObjectArrayList<K> of(K... init) {
        return wrap(init);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ObjectArrayList<K> combine(ObjectArrayList<? extends K> toAddFrom) {
        addAll((ObjectList) toAddFrom);
        return this;
    }

    public static <K> Collector<K, ?, ObjectArrayList<K>> toList() {
        return (Collector<K, ?, ObjectArrayList<K>>) TO_LIST_COLLECTOR;
    }

    public static <K> Collector<K, ?, ObjectArrayList<K>> toListWithExpectedSize(int expectedSize) {
        if (expectedSize <= 10) {
            return toList();
        }
        return Collector.of(new ObjectCollections.SizeDecreasingSupplier(expectedSize, new IntFunction() { // from class: it.unimi.dsi.fastutil.objects.ObjectArrayList$$ExternalSyntheticLambda3
            @Override // java.util.function.IntFunction
            public final Object apply(int i) {
                return ObjectArrayList.lambda$toListWithExpectedSize$0(i);
            }
        }), new ObjectArrayList$$ExternalSyntheticLambda1(), new ObjectArrayList$$ExternalSyntheticLambda2(), new Collector.Characteristics[0]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ ObjectArrayList lambda$toListWithExpectedSize$0(int size) {
        return size <= 10 ? new ObjectArrayList() : new ObjectArrayList(size);
    }

    public void ensureCapacity(int i) {
        if (i > this.a.length) {
            if (this.a == ObjectArrays.DEFAULT_EMPTY_ARRAY && i <= 10) {
                return;
            }
            if (this.wrapped) {
                this.a = (K[]) ObjectArrays.ensureCapacity(this.a, i, this.size);
            } else if (i > this.a.length) {
                Object[] objArr = new Object[i];
                System.arraycopy(this.a, 0, objArr, 0, this.size);
                this.a = (K[]) objArr;
            }
            if (this.size > this.a.length) {
                throw new AssertionError();
            }
        }
    }

    private void grow(int i) {
        if (i <= this.a.length) {
            return;
        }
        if (this.a != ObjectArrays.DEFAULT_EMPTY_ARRAY) {
            i = (int) Math.max(Math.min(this.a.length + (this.a.length >> 1), 2147483639L), i);
        } else if (i < 10) {
            i = 10;
        }
        if (this.wrapped) {
            this.a = (K[]) ObjectArrays.forceCapacity(this.a, i, this.size);
        } else {
            Object[] objArr = new Object[i];
            System.arraycopy(this.a, 0, objArr, 0, this.size);
            this.a = (K[]) objArr;
        }
        if (this.size > this.a.length) {
            throw new AssertionError();
        }
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.util.List
    public void add(int index, K k) {
        ensureIndex(index);
        grow(this.size + 1);
        if (index != this.size) {
            System.arraycopy(this.a, index, this.a, index + 1, this.size - index);
        }
        this.a[index] = k;
        this.size++;
        if (this.size > this.a.length) {
            throw new AssertionError();
        }
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(K k) {
        grow(this.size + 1);
        K[] kArr = this.a;
        int i = this.size;
        this.size = i + 1;
        kArr[i] = k;
        if (this.size <= this.a.length) {
            return true;
        }
        throw new AssertionError();
    }

    @Override // java.util.List
    public K get(int index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        return this.a[index];
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.util.List
    public int indexOf(Object k) {
        for (int i = 0; i < this.size; i++) {
            if (Objects.equals(k, this.a[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.util.List
    public int lastIndexOf(Object k) {
        int i = this.size;
        while (true) {
            int i2 = i - 1;
            if (i == 0) {
                return -1;
            }
            if (Objects.equals(k, this.a[i2])) {
                return i2;
            }
            i = i2;
        }
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.util.List
    public K remove(int index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        K old = this.a[index];
        this.size--;
        if (index != this.size) {
            System.arraycopy(this.a, index + 1, this.a, index, this.size - index);
        }
        this.a[this.size] = null;
        if (this.size > this.a.length) {
            throw new AssertionError();
        }
        return old;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean remove(Object k) {
        int index = indexOf(k);
        if (index == -1) {
            return false;
        }
        remove(index);
        if (this.size > this.a.length) {
            throw new AssertionError();
        }
        return true;
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.util.List
    public K set(int index, K k) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        K old = this.a[index];
        this.a[index] = k;
        return old;
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        Arrays.fill(this.a, 0, this.size, (Object) null);
        this.size = 0;
        if (this.size > this.a.length) {
            throw new AssertionError();
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public int size() {
        return this.size;
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, it.unimi.dsi.fastutil.objects.ObjectList
    public void size(int i) {
        if (i > this.a.length) {
            this.a = (K[]) ObjectArrays.forceCapacity(this.a, i, this.size);
        }
        if (i > this.size) {
            Arrays.fill(this.a, this.size, i, (Object) null);
        } else {
            Arrays.fill(this.a, i, this.size, (Object) null);
        }
        this.size = i;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List, it.unimi.dsi.fastutil.Stack
    public boolean isEmpty() {
        return this.size == 0;
    }

    public void trim() {
        trim(0);
    }

    public void trim(int i) {
        if (i >= this.a.length || this.size == this.a.length) {
            return;
        }
        K[] kArr = (K[]) new Object[Math.max(i, this.size)];
        System.arraycopy(this.a, 0, kArr, 0, this.size);
        this.a = kArr;
        if (this.size > this.a.length) {
            throw new AssertionError();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class SubList extends AbstractObjectList.ObjectRandomAccessSubList<K> {
        private static final long serialVersionUID = -3185226345314976296L;

        protected SubList(int from, int to) {
            super(ObjectArrayList.this, from, to);
        }

        private K[] getParentArray() {
            return ObjectArrayList.this.a;
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList.ObjectSubList, java.util.List
        public K get(int i) {
            ensureRestrictedIndex(i);
            return ObjectArrayList.this.a[this.from + i];
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public final class SubListIterator extends ObjectIterators.AbstractIndexBasedListIterator<K> {
            SubListIterator(int index) {
                super(0, index);
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectIterators.AbstractIndexBasedIterator
            protected final K get(int i) {
                return ObjectArrayList.this.a[SubList.this.from + i];
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectIterators.AbstractIndexBasedListIterator
            protected final void add(int i, K k) {
                SubList.this.add(i, k);
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectIterators.AbstractIndexBasedListIterator
            protected final void set(int i, K k) {
                SubList.this.set(i, k);
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectIterators.AbstractIndexBasedIterator
            protected final void remove(int i) {
                SubList.this.remove(i);
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectIterators.AbstractIndexBasedIterator
            protected final int getMaxPos() {
                return SubList.this.to - SubList.this.from;
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectIterators.AbstractIndexBasedIterator, java.util.Iterator, java.util.ListIterator
            public K next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                K[] kArr = ObjectArrayList.this.a;
                int i = SubList.this.from;
                int i2 = this.pos;
                this.pos = i2 + 1;
                this.lastReturned = i2;
                return kArr[i + i2];
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectIterators.AbstractIndexBasedListIterator, it.unimi.dsi.fastutil.BidirectionalIterator, java.util.ListIterator
            public K previous() {
                if (!hasPrevious()) {
                    throw new NoSuchElementException();
                }
                K[] kArr = ObjectArrayList.this.a;
                int i = SubList.this.from;
                int i2 = this.pos - 1;
                this.pos = i2;
                this.lastReturned = i2;
                return kArr[i + i2];
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectIterators.AbstractIndexBasedIterator, java.util.Iterator
            public void forEachRemaining(Consumer<? super K> consumer) {
                int i = SubList.this.to - SubList.this.from;
                while (this.pos < i) {
                    K[] kArr = ObjectArrayList.this.a;
                    int i2 = SubList.this.from;
                    int i3 = this.pos;
                    this.pos = i3 + 1;
                    this.lastReturned = i3;
                    consumer.accept(kArr[i2 + i3]);
                }
            }
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList.ObjectSubList, it.unimi.dsi.fastutil.objects.AbstractObjectList, it.unimi.dsi.fastutil.objects.ObjectList, java.util.List
        public ObjectListIterator<K> listIterator(int index) {
            return new SubListIterator(index);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public final class SubListSpliterator extends ObjectSpliterators.LateBindingSizeIndexBasedSpliterator<K> {
            SubListSpliterator() {
                super(SubList.this.from);
            }

            private SubListSpliterator(int pos, int maxPos) {
                super(pos, maxPos);
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterators.LateBindingSizeIndexBasedSpliterator
            protected final int getMaxPosFromBackingStore() {
                return SubList.this.to;
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterators.AbstractIndexBasedSpliterator
            protected final K get(int i) {
                return ObjectArrayList.this.a[i];
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterators.AbstractIndexBasedSpliterator
            public final ObjectArrayList<K>.SubList.SubListSpliterator makeForSplit(int pos, int maxPos) {
                return new SubListSpliterator(pos, maxPos);
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterators.AbstractIndexBasedSpliterator, java.util.Spliterator
            public boolean tryAdvance(Consumer<? super K> consumer) {
                if (this.pos >= getMaxPos()) {
                    return false;
                }
                K[] kArr = ObjectArrayList.this.a;
                int i = this.pos;
                this.pos = i + 1;
                consumer.accept(kArr[i]);
                return true;
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterators.AbstractIndexBasedSpliterator, java.util.Spliterator
            public void forEachRemaining(Consumer<? super K> consumer) {
                int maxPos = getMaxPos();
                while (this.pos < maxPos) {
                    K[] kArr = ObjectArrayList.this.a;
                    int i = this.pos;
                    this.pos = i + 1;
                    consumer.accept(kArr[i]);
                }
            }
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList.ObjectSubList, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
        /* renamed from: spliterator */
        public ObjectSpliterator<K> mo221spliterator() {
            return new SubListSpliterator();
        }

        boolean contentsEquals(K[] otherA, int otherAFrom, int otherATo) {
            if (ObjectArrayList.this.a == otherA && this.from == otherAFrom && this.to == otherATo) {
                return true;
            }
            if (otherATo - otherAFrom != size()) {
                return false;
            }
            int pos = this.from;
            int otherPos = otherAFrom;
            while (pos < this.to) {
                int pos2 = pos + 1;
                int otherPos2 = otherPos + 1;
                if (!Objects.equals(ObjectArrayList.this.a[pos], otherA[otherPos])) {
                    return false;
                }
                otherPos = otherPos2;
                pos = pos2;
            }
            return true;
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.util.Collection, java.util.List
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o == null || !(o instanceof List)) {
                return false;
            }
            if (o instanceof ObjectArrayList) {
                ObjectArrayList<K> other = (ObjectArrayList) o;
                return contentsEquals(other.a, 0, other.size());
            }
            if (o instanceof SubList) {
                ObjectArrayList<K>.SubList other2 = (SubList) o;
                return contentsEquals(other2.getParentArray(), other2.from, other2.to);
            }
            return super.equals(o);
        }

        int contentsCompareTo(K[] otherA, int otherAFrom, int otherATo) {
            int i = this.from;
            int j = otherAFrom;
            while (i < this.to && i < otherATo) {
                K e1 = ObjectArrayList.this.a[i];
                K e2 = otherA[j];
                int r = ((Comparable) e1).compareTo(e2);
                if (r != 0) {
                    return r;
                }
                i++;
                j++;
            }
            if (i < otherATo) {
                return -1;
            }
            return i < this.to ? 1 : 0;
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList.ObjectSubList, it.unimi.dsi.fastutil.objects.AbstractObjectList, java.lang.Comparable
        public int compareTo(List<? extends K> l) {
            if (l instanceof ObjectArrayList) {
                ObjectArrayList<K> other = (ObjectArrayList) l;
                return contentsCompareTo(other.a, 0, other.size());
            }
            if (l instanceof SubList) {
                ObjectArrayList<K>.SubList other2 = (SubList) l;
                return contentsCompareTo(other2.getParentArray(), other2.from, other2.to);
            }
            return super.compareTo((List) l);
        }
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, it.unimi.dsi.fastutil.objects.ObjectList, java.util.List
    public ObjectList<K> subList(int from, int to) {
        if (from == 0 && to == size()) {
            return this;
        }
        ensureIndex(from);
        ensureIndex(to);
        if (from > to) {
            throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        return new SubList(from, to);
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, it.unimi.dsi.fastutil.objects.ObjectList
    public void getElements(int from, Object[] a, int offset, int length) {
        ObjectArrays.ensureOffsetLength(a, offset, length);
        System.arraycopy(this.a, from, a, offset, length);
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, it.unimi.dsi.fastutil.objects.ObjectList
    public void removeElements(int from, int to) {
        it.unimi.dsi.fastutil.Arrays.ensureFromTo(this.size, from, to);
        System.arraycopy(this.a, to, this.a, from, this.size - to);
        this.size -= to - from;
        int i = to - from;
        while (true) {
            int i2 = i - 1;
            if (i == 0) {
                return;
            }
            this.a[this.size + i2] = null;
            i = i2;
        }
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, it.unimi.dsi.fastutil.objects.ObjectList
    public void addElements(int index, K[] a, int offset, int length) {
        ensureIndex(index);
        ObjectArrays.ensureOffsetLength(a, offset, length);
        grow(this.size + length);
        System.arraycopy(this.a, index, this.a, index + length, this.size - index);
        System.arraycopy(a, offset, this.a, index, length);
        this.size += length;
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, it.unimi.dsi.fastutil.objects.ObjectList
    public void setElements(int index, K[] a, int offset, int length) {
        ensureIndex(index);
        ObjectArrays.ensureOffsetLength(a, offset, length);
        if (index + length > this.size) {
            throw new IndexOutOfBoundsException("End index (" + (index + length) + ") is greater than list size (" + this.size + ")");
        }
        System.arraycopy(a, offset, this.a, index, length);
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.lang.Iterable
    public void forEach(Consumer<? super K> consumer) {
        for (int i = 0; i < this.size; i++) {
            consumer.accept(this.a[i]);
        }
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.util.List
    public boolean addAll(int index, Collection<? extends K> c) {
        if (c instanceof ObjectList) {
            return addAll(index, (ObjectList) c);
        }
        ensureIndex(index);
        int n = c.size();
        if (n == 0) {
            return false;
        }
        grow(this.size + n);
        System.arraycopy(this.a, index, this.a, index + n, this.size - index);
        Iterator<? extends K> i = c.iterator();
        this.size += n;
        while (true) {
            int n2 = n - 1;
            if (n == 0) {
                break;
            }
            this.a[index] = i.next();
            n = n2;
            index++;
        }
        if (this.size > this.a.length) {
            throw new AssertionError();
        }
        return true;
    }

    @Override // it.unimi.dsi.fastutil.objects.ObjectList
    public boolean addAll(int index, ObjectList<? extends K> l) {
        ensureIndex(index);
        int n = l.size();
        if (n == 0) {
            return false;
        }
        grow(this.size + n);
        System.arraycopy(this.a, index, this.a, index + n, this.size - index);
        l.getElements(0, this.a, index, n);
        this.size += n;
        if (this.size > this.a.length) {
            throw new AssertionError();
        }
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean removeAll(Collection<?> c) {
        Object[] a = this.a;
        int j = 0;
        for (int i = 0; i < this.size; i++) {
            if (!c.contains(a[i])) {
                a[j] = a[i];
                j++;
            }
        }
        int i2 = this.size;
        Arrays.fill(a, j, i2, (Object) null);
        boolean modified = this.size != j;
        this.size = j;
        return modified;
    }

    @Override // java.util.Collection
    public boolean removeIf(Predicate<? super K> predicate) {
        K[] kArr = this.a;
        int i = 0;
        for (int i2 = 0; i2 < this.size; i2++) {
            if (!predicate.test(kArr[i2])) {
                kArr[i] = kArr[i2];
                i++;
            }
        }
        Arrays.fill(kArr, i, this.size, (Object) null);
        boolean z = this.size != i;
        this.size = i;
        return z;
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public Object[] toArray() {
        int size = size();
        return size == 0 ? ObjectArrays.EMPTY_ARRAY : Arrays.copyOf(this.a, size, Object[].class);
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public <T> T[] toArray(T[] tArr) {
        if (tArr == null) {
            tArr = (T[]) new Object[size()];
        } else if (tArr.length < size()) {
            tArr = (T[]) ((Object[]) Array.newInstance(tArr.getClass().getComponentType(), size()));
        }
        System.arraycopy(this.a, 0, tArr, 0, size());
        if (tArr.length > size()) {
            tArr[size()] = null;
        }
        return tArr;
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, it.unimi.dsi.fastutil.objects.ObjectList, java.util.List
    public ObjectListIterator<K> listIterator(final int index) {
        ensureIndex(index);
        return new ObjectListIterator<K>() { // from class: it.unimi.dsi.fastutil.objects.ObjectArrayList.1
            int last = -1;
            int pos;

            {
                this.pos = index;
            }

            @Override // java.util.Iterator, java.util.ListIterator
            public boolean hasNext() {
                return this.pos < ObjectArrayList.this.size;
            }

            @Override // it.unimi.dsi.fastutil.BidirectionalIterator
            public boolean hasPrevious() {
                return this.pos > 0;
            }

            @Override // java.util.Iterator, java.util.ListIterator
            public K next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                K[] kArr = ObjectArrayList.this.a;
                int i = this.pos;
                this.pos = i + 1;
                this.last = i;
                return kArr[i];
            }

            @Override // it.unimi.dsi.fastutil.BidirectionalIterator, java.util.ListIterator
            public K previous() {
                if (!hasPrevious()) {
                    throw new NoSuchElementException();
                }
                K[] kArr = ObjectArrayList.this.a;
                int i = this.pos - 1;
                this.pos = i;
                this.last = i;
                return kArr[i];
            }

            @Override // java.util.ListIterator
            public int nextIndex() {
                return this.pos;
            }

            @Override // java.util.ListIterator
            public int previousIndex() {
                return this.pos - 1;
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectListIterator, java.util.ListIterator
            public void add(K k) {
                ObjectArrayList objectArrayList = ObjectArrayList.this;
                int i = this.pos;
                this.pos = i + 1;
                objectArrayList.add(i, k);
                this.last = -1;
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectListIterator, java.util.ListIterator
            public void set(K k) {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                ObjectArrayList.this.set(this.last, k);
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectListIterator, java.util.Iterator, java.util.ListIterator
            public void remove() {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                ObjectArrayList.this.remove(this.last);
                if (this.last < this.pos) {
                    this.pos--;
                }
                this.last = -1;
            }

            @Override // java.util.Iterator
            public void forEachRemaining(Consumer<? super K> consumer) {
                while (this.pos < ObjectArrayList.this.size) {
                    K[] kArr = ObjectArrayList.this.a;
                    int i = this.pos;
                    this.pos = i + 1;
                    this.last = i;
                    consumer.accept(kArr[i]);
                }
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator
            public int back(int n) {
                if (n < 0) {
                    throw new IllegalArgumentException("Argument must be nonnegative: " + n);
                }
                int remaining = ObjectArrayList.this.size - this.pos;
                if (n < remaining) {
                    this.pos -= n;
                } else {
                    n = remaining;
                    this.pos = 0;
                }
                this.last = this.pos;
                return n;
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectIterator
            public int skip(int n) {
                if (n < 0) {
                    throw new IllegalArgumentException("Argument must be nonnegative: " + n);
                }
                int remaining = ObjectArrayList.this.size - this.pos;
                if (n < remaining) {
                    this.pos += n;
                } else {
                    n = remaining;
                    this.pos = ObjectArrayList.this.size;
                }
                this.last = this.pos - 1;
                return n;
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class Spliterator implements ObjectSpliterator<K> {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        boolean hasSplit;
        int max;
        int pos;

        public Spliterator(ObjectArrayList objectArrayList) {
            this(0, objectArrayList.size, false);
        }

        private Spliterator(int pos, int max, boolean hasSplit) {
            this.hasSplit = false;
            if (pos > max) {
                throw new AssertionError("pos " + pos + " must be <= max " + max);
            }
            this.pos = pos;
            this.max = max;
            this.hasSplit = hasSplit;
        }

        private int getWorkingMax() {
            return this.hasSplit ? this.max : ObjectArrayList.this.size;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return ObjectSpliterators.LIST_SPLITERATOR_CHARACTERISTICS;
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return getWorkingMax() - this.pos;
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super K> consumer) {
            if (this.pos >= getWorkingMax()) {
                return false;
            }
            K[] kArr = ObjectArrayList.this.a;
            int i = this.pos;
            this.pos = i + 1;
            consumer.accept(kArr[i]);
            return true;
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super K> consumer) {
            int workingMax = getWorkingMax();
            while (this.pos < workingMax) {
                consumer.accept(ObjectArrayList.this.a[this.pos]);
                this.pos++;
            }
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterator
        public long skip(long n) {
            if (n < 0) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
            int max = getWorkingMax();
            if (this.pos >= max) {
                return 0L;
            }
            int remaining = max - this.pos;
            if (n < remaining) {
                this.pos = SafeMath.safeLongToInt(this.pos + n);
                return n;
            }
            long n2 = remaining;
            this.pos = max;
            return n2;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterator, java.util.Spliterator
        public ObjectSpliterator<K> trySplit() {
            int max = getWorkingMax();
            int retLen = (max - this.pos) >> 1;
            if (retLen <= 1) {
                return null;
            }
            this.max = max;
            int myNewPos = this.pos + retLen;
            int oldPos = this.pos;
            this.pos = myNewPos;
            this.hasSplit = true;
            return new Spliterator(oldPos, myNewPos, true);
        }
    }

    @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
    /* renamed from: spliterator */
    public ObjectSpliterator<K> mo221spliterator() {
        return new Spliterator(this);
    }

    @Override // it.unimi.dsi.fastutil.objects.ObjectList, java.util.List
    public void sort(Comparator<? super K> comp) {
        if (comp == null) {
            ObjectArrays.stableSort(this.a, 0, this.size);
        } else {
            ObjectArrays.stableSort(this.a, 0, this.size, comp);
        }
    }

    @Override // it.unimi.dsi.fastutil.objects.ObjectList
    public void unstableSort(Comparator<? super K> comp) {
        if (comp == null) {
            ObjectArrays.unstableSort(this.a, 0, this.size);
        } else {
            ObjectArrays.unstableSort(this.a, 0, this.size, comp);
        }
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public ObjectArrayList<K> m267clone() {
        if (getClass() == ObjectArrayList.class) {
            ObjectArrayList<K> objectArrayList = new ObjectArrayList<>(copyArraySafe(this.a, this.size), false);
            objectArrayList.size = this.size;
            return objectArrayList;
        }
        try {
            ObjectArrayList<K> objectArrayList2 = (ObjectArrayList) super.clone();
            objectArrayList2.a = (K[]) copyArraySafe(this.a, this.size);
            return objectArrayList2;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }

    public boolean equals(ObjectArrayList<K> l) {
        if (l == this) {
            return true;
        }
        int s = size();
        if (s != l.size()) {
            return false;
        }
        K[] a1 = this.a;
        K[] a2 = l.a;
        if (a1 == a2 && s == l.size()) {
            return true;
        }
        while (true) {
            int s2 = s - 1;
            if (s == 0) {
                return true;
            }
            if (!Objects.equals(a1[s2], a2[s2])) {
                return false;
            }
            s = s2;
        }
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.util.Collection, java.util.List
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || !(o instanceof List)) {
            return false;
        }
        if (o instanceof ObjectArrayList) {
            return equals((ObjectArrayList) o);
        }
        if (o instanceof SubList) {
            return ((SubList) o).equals(this);
        }
        return super.equals(o);
    }

    public int compareTo(ObjectArrayList<? extends K> objectArrayList) {
        int size = size();
        int size2 = objectArrayList.size();
        K[] kArr = this.a;
        Object[] objArr = objectArrayList.a;
        int i = 0;
        while (i < size && i < size2) {
            int compareTo = ((Comparable) kArr[i]).compareTo(objArr[i]);
            if (compareTo != 0) {
                return compareTo;
            }
            i++;
        }
        if (i < size2) {
            return -1;
        }
        return i < size ? 1 : 0;
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.lang.Comparable
    public int compareTo(List<? extends K> l) {
        if (l instanceof ObjectArrayList) {
            return compareTo((ObjectArrayList) l);
        }
        if (l instanceof SubList) {
            return -((SubList) l).compareTo((List) this);
        }
        return super.compareTo((List) l);
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        for (int i = 0; i < this.size; i++) {
            s.writeObject(this.a[i]);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.a = (K[]) new Object[this.size];
        for (int i = 0; i < this.size; i++) {
            ((K[]) this.a)[i] = objectInputStream.readObject();
        }
    }
}
