package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.objects.ObjectCollections;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import it.unimi.dsi.fastutil.objects.ObjectSpliterators;
import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.stream.Collector;

/* loaded from: classes4.dex */
public class ObjectImmutableList<K> extends ObjectLists.ImmutableListBase<K> implements ObjectList<K>, RandomAccess, Cloneable, Serializable {
    static final ObjectImmutableList EMPTY = new ObjectImmutableList(ObjectArrays.EMPTY_ARRAY);
    private static final Collector<Object, ?, ObjectImmutableList<Object>> TO_LIST_COLLECTOR = Collector.of(new ObjectArrayList$$ExternalSyntheticLambda0(), new ObjectArrayList$$ExternalSyntheticLambda1(), new ObjectArrayList$$ExternalSyntheticLambda2(), new ObjectImmutableList$$ExternalSyntheticLambda1(), new Collector.Characteristics[0]);
    private static final long serialVersionUID = 0;
    private final K[] a;

    private static final <K> K[] emptyArray() {
        return (K[]) ObjectArrays.EMPTY_ARRAY;
    }

    public ObjectImmutableList(K[] a) {
        this.a = a;
    }

    public ObjectImmutableList(Collection<? extends K> c) {
        this(c.isEmpty() ? emptyArray() : ObjectIterators.unwrap(c.iterator()));
    }

    public ObjectImmutableList(ObjectCollection<? extends K> c) {
        this(c.isEmpty() ? emptyArray() : ObjectIterators.unwrap(c.iterator()));
    }

    public ObjectImmutableList(ObjectList<? extends K> l) {
        this(l.isEmpty() ? emptyArray() : new Object[l.size()]);
        l.getElements(0, this.a, 0, l.size());
    }

    public ObjectImmutableList(K[] a, int offset, int length) {
        this(length == 0 ? emptyArray() : new Object[length]);
        System.arraycopy(a, offset, this.a, 0, length);
    }

    public ObjectImmutableList(ObjectIterator<? extends K> i) {
        this(i.hasNext() ? ObjectIterators.unwrap(i) : emptyArray());
    }

    public static <K> ObjectImmutableList<K> of() {
        return EMPTY;
    }

    @SafeVarargs
    public static <K> ObjectImmutableList<K> of(K... init) {
        return init.length == 0 ? of() : new ObjectImmutableList<>(init);
    }

    public static <K> ObjectImmutableList<K> convertTrustedToImmutableList(ObjectArrayList<K> arrayList) {
        if (arrayList.isEmpty()) {
            return of();
        }
        Object[] elements = arrayList.elements();
        if (arrayList.size() != elements.length) {
            elements = Arrays.copyOf(elements, arrayList.size());
        }
        return new ObjectImmutableList<>(elements);
    }

    public static <K> Collector<K, ?, ObjectImmutableList<K>> toList() {
        return (Collector<K, ?, ObjectImmutableList<K>>) TO_LIST_COLLECTOR;
    }

    public static <K> Collector<K, ?, ObjectImmutableList<K>> toListWithExpectedSize(int expectedSize) {
        if (expectedSize <= 10) {
            return toList();
        }
        return Collector.of(new ObjectCollections.SizeDecreasingSupplier(expectedSize, new IntFunction() { // from class: it.unimi.dsi.fastutil.objects.ObjectImmutableList$$ExternalSyntheticLambda0
            @Override // java.util.function.IntFunction
            public final Object apply(int i) {
                return ObjectImmutableList.lambda$toListWithExpectedSize$0(i);
            }
        }), new ObjectArrayList$$ExternalSyntheticLambda1(), new ObjectArrayList$$ExternalSyntheticLambda2(), new ObjectImmutableList$$ExternalSyntheticLambda1(), new Collector.Characteristics[0]);
    }

    public static /* synthetic */ ObjectArrayList lambda$toListWithExpectedSize$0(int size) {
        return size <= 10 ? new ObjectArrayList() : new ObjectArrayList(size);
    }

    @Override // java.util.List
    public K get(int index) {
        if (index >= this.a.length) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.a.length + ")");
        }
        return this.a[index];
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.util.List
    public int indexOf(Object k) {
        int size = this.a.length;
        for (int i = 0; i < size; i++) {
            if (Objects.equals(k, this.a[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.util.List
    public int lastIndexOf(Object k) {
        int i = this.a.length;
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

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public int size() {
        return this.a.length;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List, it.unimi.dsi.fastutil.Stack
    public boolean isEmpty() {
        return this.a.length == 0;
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, it.unimi.dsi.fastutil.objects.ObjectList
    public void getElements(int from, Object[] a, int offset, int length) {
        ObjectArrays.ensureOffsetLength(a, offset, length);
        System.arraycopy(this.a, from, a, offset, length);
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.lang.Iterable
    public void forEach(Consumer<? super K> consumer) {
        for (int i = 0; i < this.a.length; i++) {
            consumer.accept(this.a[i]);
        }
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public Object[] toArray() {
        return this.a.length == 0 ? ObjectArrays.EMPTY_ARRAY : this.a.getClass() == Object[].class ? (Object[]) this.a.clone() : Arrays.copyOf(this.a, this.a.length, Object[].class);
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

    /* renamed from: it.unimi.dsi.fastutil.objects.ObjectImmutableList$1 */
    /* loaded from: classes4.dex */
    public class AnonymousClass1 implements ObjectListIterator<K> {
        int pos;
        final /* synthetic */ int val$index;

        AnonymousClass1(int i) {
            r2 = i;
            this.pos = r2;
        }

        @Override // java.util.Iterator, java.util.ListIterator
        public boolean hasNext() {
            return this.pos < ObjectImmutableList.this.a.length;
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
            Object[] objArr = ObjectImmutableList.this.a;
            int i = this.pos;
            this.pos = i + 1;
            return (K) objArr[i];
        }

        @Override // it.unimi.dsi.fastutil.BidirectionalIterator, java.util.ListIterator
        public K previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            Object[] objArr = ObjectImmutableList.this.a;
            int i = this.pos - 1;
            this.pos = i;
            return (K) objArr[i];
        }

        @Override // java.util.ListIterator
        public int nextIndex() {
            return this.pos;
        }

        @Override // java.util.ListIterator
        public int previousIndex() {
            return this.pos - 1;
        }

        @Override // java.util.Iterator
        public void forEachRemaining(Consumer<? super K> action) {
            while (this.pos < ObjectImmutableList.this.a.length) {
                Object[] objArr = ObjectImmutableList.this.a;
                int i = this.pos;
                this.pos = i + 1;
                action.accept(objArr[i]);
            }
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectListIterator, java.util.ListIterator
        public void add(K k) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectListIterator, java.util.ListIterator
        public void set(K k) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectListIterator, java.util.Iterator, java.util.ListIterator
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator
        public int back(int n) {
            if (n < 0) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
            int remaining = this.pos;
            if (n < remaining) {
                this.pos -= n;
                return n;
            }
            this.pos = 0;
            return remaining;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectIterator
        public int skip(int n) {
            if (n >= 0) {
                int remaining = ObjectImmutableList.this.a.length - this.pos;
                if (n >= remaining) {
                    this.pos = ObjectImmutableList.this.a.length;
                    return remaining;
                }
                this.pos += n;
                return n;
            }
            throw new IllegalArgumentException("Argument must be nonnegative: " + n);
        }
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, it.unimi.dsi.fastutil.objects.ObjectList, java.util.List
    public ObjectListIterator<K> listIterator(int index) {
        ensureIndex(index);
        return new ObjectListIterator<K>() { // from class: it.unimi.dsi.fastutil.objects.ObjectImmutableList.1
            int pos;
            final /* synthetic */ int val$index;

            AnonymousClass1(int index2) {
                r2 = index2;
                this.pos = r2;
            }

            @Override // java.util.Iterator, java.util.ListIterator
            public boolean hasNext() {
                return this.pos < ObjectImmutableList.this.a.length;
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
                Object[] objArr = ObjectImmutableList.this.a;
                int i = this.pos;
                this.pos = i + 1;
                return (K) objArr[i];
            }

            @Override // it.unimi.dsi.fastutil.BidirectionalIterator, java.util.ListIterator
            public K previous() {
                if (!hasPrevious()) {
                    throw new NoSuchElementException();
                }
                Object[] objArr = ObjectImmutableList.this.a;
                int i = this.pos - 1;
                this.pos = i;
                return (K) objArr[i];
            }

            @Override // java.util.ListIterator
            public int nextIndex() {
                return this.pos;
            }

            @Override // java.util.ListIterator
            public int previousIndex() {
                return this.pos - 1;
            }

            @Override // java.util.Iterator
            public void forEachRemaining(Consumer<? super K> action) {
                while (this.pos < ObjectImmutableList.this.a.length) {
                    Object[] objArr = ObjectImmutableList.this.a;
                    int i = this.pos;
                    this.pos = i + 1;
                    action.accept(objArr[i]);
                }
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectListIterator, java.util.ListIterator
            public void add(K k) {
                throw new UnsupportedOperationException();
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectListIterator, java.util.ListIterator
            public void set(K k) {
                throw new UnsupportedOperationException();
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectListIterator, java.util.Iterator, java.util.ListIterator
            public void remove() {
                throw new UnsupportedOperationException();
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator
            public int back(int n) {
                if (n < 0) {
                    throw new IllegalArgumentException("Argument must be nonnegative: " + n);
                }
                int remaining = this.pos;
                if (n < remaining) {
                    this.pos -= n;
                    return n;
                }
                this.pos = 0;
                return remaining;
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectIterator
            public int skip(int n) {
                if (n >= 0) {
                    int remaining = ObjectImmutableList.this.a.length - this.pos;
                    if (n >= remaining) {
                        this.pos = ObjectImmutableList.this.a.length;
                        return remaining;
                    }
                    this.pos += n;
                    return n;
                }
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
        };
    }

    /* loaded from: classes4.dex */
    public final class Spliterator implements ObjectSpliterator<K> {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        int max;
        int pos;

        public Spliterator(ObjectImmutableList objectImmutableList) {
            this(0, objectImmutableList.a.length);
        }

        private Spliterator(int pos, int max) {
            if (pos > max) {
                throw new AssertionError("pos " + pos + " must be <= max " + max);
            }
            this.pos = pos;
            this.max = max;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return 17488;
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return this.max - this.pos;
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super K> action) {
            if (this.pos >= this.max) {
                return false;
            }
            Object[] objArr = ObjectImmutableList.this.a;
            int i = this.pos;
            this.pos = i + 1;
            action.accept(objArr[i]);
            return true;
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super K> action) {
            while (this.pos < this.max) {
                action.accept(ObjectImmutableList.this.a[this.pos]);
                this.pos++;
            }
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterator
        public long skip(long n) {
            if (n < 0) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
            if (this.pos >= this.max) {
                return 0L;
            }
            int remaining = this.max - this.pos;
            if (n < remaining) {
                this.pos = SafeMath.safeLongToInt(this.pos + n);
                return n;
            }
            long n2 = remaining;
            this.pos = this.max;
            return n2;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterator, java.util.Spliterator
        public ObjectSpliterator<K> trySplit() {
            int retLen = (this.max - this.pos) >> 1;
            if (retLen <= 1) {
                return null;
            }
            int myNewPos = this.pos + retLen;
            int oldPos = this.pos;
            this.pos = myNewPos;
            return new Spliterator(oldPos, myNewPos);
        }
    }

    @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
    /* renamed from: spliterator */
    public ObjectSpliterator<K> mo221spliterator() {
        return new Spliterator(this);
    }

    /* loaded from: classes4.dex */
    public static final class ImmutableSubList<K> extends ObjectLists.ImmutableListBase<K> implements RandomAccess, Serializable {
        private static final long serialVersionUID = 7054639518438982401L;
        final transient K[] a;
        final int from;
        final ObjectImmutableList<K> innerList;
        final int to;

        ImmutableSubList(ObjectImmutableList<K> objectImmutableList, int i, int i2) {
            this.innerList = objectImmutableList;
            this.from = i;
            this.to = i2;
            this.a = (K[]) ((ObjectImmutableList) objectImmutableList).a;
        }

        @Override // java.util.List
        public K get(int index) {
            ensureRestrictedIndex(index);
            return this.a[this.from + index];
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.util.List
        public int indexOf(Object k) {
            for (int i = this.from; i < this.to; i++) {
                if (Objects.equals(k, this.a[i])) {
                    return i - this.from;
                }
            }
            return -1;
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.util.List
        public int lastIndexOf(Object k) {
            int i = this.to;
            while (true) {
                int i2 = i - 1;
                if (i == this.from) {
                    return -1;
                }
                if (Objects.equals(k, this.a[i2])) {
                    return i2 - this.from;
                }
                i = i2;
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.to - this.from;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List, it.unimi.dsi.fastutil.Stack
        public boolean isEmpty() {
            return this.to <= this.from;
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, it.unimi.dsi.fastutil.objects.ObjectList
        public void getElements(int fromSublistIndex, Object[] a, int offset, int length) {
            ObjectArrays.ensureOffsetLength(a, offset, length);
            ensureRestrictedIndex(fromSublistIndex);
            if (this.from + length > this.to) {
                throw new IndexOutOfBoundsException("Final index " + (this.from + length) + " (startingIndex: " + this.from + " + length: " + length + ") is greater then list length " + size());
            }
            System.arraycopy(this.a, this.from + fromSublistIndex, a, offset, length);
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.lang.Iterable
        public void forEach(Consumer<? super K> consumer) {
            for (int i = this.from; i < this.to; i++) {
                consumer.accept(this.a[i]);
            }
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.util.AbstractCollection, java.util.Collection, java.util.List
        public Object[] toArray() {
            return Arrays.copyOfRange(this.a, this.from, this.to, Object[].class);
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.util.AbstractCollection, java.util.Collection, java.util.List
        public <K> K[] toArray(K[] kArr) {
            int size = size();
            if (kArr == null) {
                kArr = (K[]) new Object[size];
            } else if (kArr.length < size) {
                kArr = (K[]) ((Object[]) Array.newInstance(kArr.getClass().getComponentType(), size));
            }
            System.arraycopy(this.a, this.from, kArr, 0, size);
            if (kArr.length > size) {
                kArr[size] = null;
            }
            return kArr;
        }

        /* renamed from: it.unimi.dsi.fastutil.objects.ObjectImmutableList$ImmutableSubList$1 */
        /* loaded from: classes4.dex */
        public class AnonymousClass1 implements ObjectListIterator<K> {
            int pos;
            final /* synthetic */ int val$index;

            AnonymousClass1(int i) {
                r3 = i;
                this.pos = r3 + ImmutableSubList.this.from;
            }

            @Override // java.util.Iterator, java.util.ListIterator
            public boolean hasNext() {
                return this.pos < ImmutableSubList.this.to;
            }

            @Override // it.unimi.dsi.fastutil.BidirectionalIterator
            public boolean hasPrevious() {
                return this.pos > ImmutableSubList.this.from;
            }

            @Override // java.util.Iterator, java.util.ListIterator
            public K next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                K[] kArr = ImmutableSubList.this.a;
                int i = this.pos;
                this.pos = i + 1;
                return kArr[i];
            }

            @Override // it.unimi.dsi.fastutil.BidirectionalIterator, java.util.ListIterator
            public K previous() {
                if (!hasPrevious()) {
                    throw new NoSuchElementException();
                }
                K[] kArr = ImmutableSubList.this.a;
                int i = this.pos - 1;
                this.pos = i;
                return kArr[i];
            }

            @Override // java.util.ListIterator
            public int nextIndex() {
                return this.pos - ImmutableSubList.this.from;
            }

            @Override // java.util.ListIterator
            public int previousIndex() {
                return (this.pos - ImmutableSubList.this.from) - 1;
            }

            @Override // java.util.Iterator
            public void forEachRemaining(Consumer<? super K> consumer) {
                while (this.pos < ImmutableSubList.this.to) {
                    K[] kArr = ImmutableSubList.this.a;
                    int i = this.pos;
                    this.pos = i + 1;
                    consumer.accept(kArr[i]);
                }
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectListIterator, java.util.ListIterator
            public void add(K k) {
                throw new UnsupportedOperationException();
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectListIterator, java.util.ListIterator
            public void set(K k) {
                throw new UnsupportedOperationException();
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectListIterator, java.util.Iterator, java.util.ListIterator
            public void remove() {
                throw new UnsupportedOperationException();
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator
            public int back(int n) {
                if (n < 0) {
                    throw new IllegalArgumentException("Argument must be nonnegative: " + n);
                }
                int remaining = this.pos - ImmutableSubList.this.from;
                if (n < remaining) {
                    this.pos -= n;
                    return n;
                }
                this.pos = ImmutableSubList.this.from;
                return remaining;
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectIterator
            public int skip(int n) {
                if (n < 0) {
                    throw new IllegalArgumentException("Argument must be nonnegative: " + n);
                }
                int remaining = ImmutableSubList.this.to - this.pos;
                if (n < remaining) {
                    this.pos += n;
                    return n;
                }
                this.pos = ImmutableSubList.this.to;
                return remaining;
            }
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, it.unimi.dsi.fastutil.objects.ObjectList, java.util.List
        public ObjectListIterator<K> listIterator(int index) {
            ensureRestrictedIndex(this.from + index);
            return new ObjectListIterator<K>() { // from class: it.unimi.dsi.fastutil.objects.ObjectImmutableList.ImmutableSubList.1
                int pos;
                final /* synthetic */ int val$index;

                AnonymousClass1(int index2) {
                    r3 = index2;
                    this.pos = r3 + ImmutableSubList.this.from;
                }

                @Override // java.util.Iterator, java.util.ListIterator
                public boolean hasNext() {
                    return this.pos < ImmutableSubList.this.to;
                }

                @Override // it.unimi.dsi.fastutil.BidirectionalIterator
                public boolean hasPrevious() {
                    return this.pos > ImmutableSubList.this.from;
                }

                @Override // java.util.Iterator, java.util.ListIterator
                public K next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    K[] kArr = ImmutableSubList.this.a;
                    int i = this.pos;
                    this.pos = i + 1;
                    return kArr[i];
                }

                @Override // it.unimi.dsi.fastutil.BidirectionalIterator, java.util.ListIterator
                public K previous() {
                    if (!hasPrevious()) {
                        throw new NoSuchElementException();
                    }
                    K[] kArr = ImmutableSubList.this.a;
                    int i = this.pos - 1;
                    this.pos = i;
                    return kArr[i];
                }

                @Override // java.util.ListIterator
                public int nextIndex() {
                    return this.pos - ImmutableSubList.this.from;
                }

                @Override // java.util.ListIterator
                public int previousIndex() {
                    return (this.pos - ImmutableSubList.this.from) - 1;
                }

                @Override // java.util.Iterator
                public void forEachRemaining(Consumer<? super K> consumer) {
                    while (this.pos < ImmutableSubList.this.to) {
                        K[] kArr = ImmutableSubList.this.a;
                        int i = this.pos;
                        this.pos = i + 1;
                        consumer.accept(kArr[i]);
                    }
                }

                @Override // it.unimi.dsi.fastutil.objects.ObjectListIterator, java.util.ListIterator
                public void add(K k) {
                    throw new UnsupportedOperationException();
                }

                @Override // it.unimi.dsi.fastutil.objects.ObjectListIterator, java.util.ListIterator
                public void set(K k) {
                    throw new UnsupportedOperationException();
                }

                @Override // it.unimi.dsi.fastutil.objects.ObjectListIterator, java.util.Iterator, java.util.ListIterator
                public void remove() {
                    throw new UnsupportedOperationException();
                }

                @Override // it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator
                public int back(int n) {
                    if (n < 0) {
                        throw new IllegalArgumentException("Argument must be nonnegative: " + n);
                    }
                    int remaining = this.pos - ImmutableSubList.this.from;
                    if (n < remaining) {
                        this.pos -= n;
                        return n;
                    }
                    this.pos = ImmutableSubList.this.from;
                    return remaining;
                }

                @Override // it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectIterator
                public int skip(int n) {
                    if (n < 0) {
                        throw new IllegalArgumentException("Argument must be nonnegative: " + n);
                    }
                    int remaining = ImmutableSubList.this.to - this.pos;
                    if (n < remaining) {
                        this.pos += n;
                        return n;
                    }
                    this.pos = ImmutableSubList.this.to;
                    return remaining;
                }
            };
        }

        /* loaded from: classes4.dex */
        public final class SubListSpliterator extends ObjectSpliterators.EarlyBindingSizeIndexBasedSpliterator<K> {
            SubListSpliterator() {
                super(ImmutableSubList.this.from, ImmutableSubList.this.to);
            }

            private SubListSpliterator(int pos, int maxPos) {
                super(pos, maxPos);
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterators.AbstractIndexBasedSpliterator
            protected final K get(int i) {
                return ImmutableSubList.this.a[i];
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterators.AbstractIndexBasedSpliterator
            public final ImmutableSubList<K>.SubListSpliterator makeForSplit(int pos, int maxPos) {
                return new SubListSpliterator(pos, maxPos);
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterators.AbstractIndexBasedSpliterator, java.util.Spliterator
            public boolean tryAdvance(Consumer<? super K> consumer) {
                if (this.pos >= this.maxPos) {
                    return false;
                }
                K[] kArr = ImmutableSubList.this.a;
                int i = this.pos;
                this.pos = i + 1;
                consumer.accept(kArr[i]);
                return true;
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterators.AbstractIndexBasedSpliterator, java.util.Spliterator
            public void forEachRemaining(Consumer<? super K> consumer) {
                int i = this.maxPos;
                while (this.pos < i) {
                    K[] kArr = ImmutableSubList.this.a;
                    int i2 = this.pos;
                    this.pos = i2 + 1;
                    consumer.accept(kArr[i2]);
                }
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterators.AbstractIndexBasedSpliterator, java.util.Spliterator
            public int characteristics() {
                return 17488;
            }
        }

        @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
        /* renamed from: spliterator */
        public ObjectSpliterator<K> mo221spliterator() {
            return new SubListSpliterator();
        }

        boolean contentsEquals(K[] otherA, int otherAFrom, int otherATo) {
            if (this.a == otherA && this.from == otherAFrom && this.to == otherATo) {
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
                if (!Objects.equals(this.a[pos], otherA[otherPos])) {
                    return false;
                }
                otherPos = otherPos2;
                pos = pos2;
            }
            return true;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.util.Collection, java.util.List
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o == null || !(o instanceof List)) {
                return false;
            }
            if (o instanceof ObjectImmutableList) {
                ObjectImmutableList<K> other = (ObjectImmutableList) o;
                return contentsEquals(((ObjectImmutableList) other).a, 0, other.size());
            }
            if (o instanceof ImmutableSubList) {
                ImmutableSubList<K> other2 = (ImmutableSubList) o;
                return contentsEquals(other2.a, other2.from, other2.to);
            }
            return super.equals(o);
        }

        int contentsCompareTo(K[] otherA, int otherAFrom, int otherATo) {
            int i = this.from;
            int j = otherAFrom;
            while (i < this.to && i < otherATo) {
                K e1 = this.a[i];
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

        /* JADX WARN: Multi-variable type inference failed */
        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.lang.Comparable
        public int compareTo(List<? extends K> l) {
            if (l instanceof ObjectImmutableList) {
                ObjectImmutableList<K> other = (ObjectImmutableList) l;
                return contentsCompareTo(((ObjectImmutableList) other).a, 0, other.size());
            }
            if (l instanceof ImmutableSubList) {
                ImmutableSubList<K> other2 = (ImmutableSubList) l;
                return contentsCompareTo(other2.a, other2.from, other2.to);
            }
            return super.compareTo((List) l);
        }

        private Object readResolve() throws ObjectStreamException {
            try {
                return this.innerList.subList(this.from, this.to);
            } catch (IllegalArgumentException | IndexOutOfBoundsException ex) {
                throw ((InvalidObjectException) new InvalidObjectException(ex.getMessage()).initCause(ex));
            }
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, it.unimi.dsi.fastutil.objects.ObjectList, java.util.List
        public ObjectList<K> subList(int from, int to) {
            ensureIndex(from);
            ensureIndex(to);
            if (from == to) {
                return ObjectImmutableList.EMPTY;
            }
            if (from > to) {
                throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            return new ImmutableSubList(this.innerList, this.from + from, this.from + to);
        }
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, it.unimi.dsi.fastutil.objects.ObjectList, java.util.List
    public ObjectList<K> subList(int from, int to) {
        if (from == 0 && to == size()) {
            return this;
        }
        ensureIndex(from);
        ensureIndex(to);
        if (from == to) {
            return EMPTY;
        }
        if (from > to) {
            throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        return new ImmutableSubList(this, from, to);
    }

    /* renamed from: clone */
    public ObjectImmutableList<K> m270clone() {
        return this;
    }

    public boolean equals(ObjectImmutableList<K> l) {
        if (l == this || this.a == l.a) {
            return true;
        }
        int s = size();
        if (s != l.size()) {
            return false;
        }
        K[] a1 = this.a;
        K[] a2 = l.a;
        return Arrays.equals(a1, a2);
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.util.Collection, java.util.List
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || !(o instanceof List)) {
            return false;
        }
        if (o instanceof ObjectImmutableList) {
            return equals((ObjectImmutableList) o);
        }
        if (o instanceof ImmutableSubList) {
            return ((ImmutableSubList) o).equals(this);
        }
        return super.equals(o);
    }

    public int compareTo(ObjectImmutableList<? extends K> objectImmutableList) {
        int size = size();
        int size2 = objectImmutableList.size();
        K[] kArr = this.a;
        Object[] objArr = objectImmutableList.a;
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
        if (l instanceof ObjectImmutableList) {
            return compareTo((ObjectImmutableList) l);
        }
        if (l instanceof ImmutableSubList) {
            ImmutableSubList<K> other = (ImmutableSubList) l;
            return -other.compareTo((List) this);
        }
        return super.compareTo((List) l);
    }
}
