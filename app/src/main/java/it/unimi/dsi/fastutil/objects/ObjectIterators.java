package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.BigArrays;
import java.io.Serializable;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

/* loaded from: classes4.dex */
public final class ObjectIterators {
    public static final EmptyIterator EMPTY_ITERATOR = new EmptyIterator();

    private ObjectIterators() {
    }

    /* loaded from: classes4.dex */
    public static class EmptyIterator<K> implements ObjectListIterator<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;

        protected EmptyIterator() {
        }

        @Override // java.util.Iterator, java.util.ListIterator
        public boolean hasNext() {
            return false;
        }

        @Override // it.unimi.dsi.fastutil.BidirectionalIterator
        public boolean hasPrevious() {
            return false;
        }

        @Override // java.util.Iterator, java.util.ListIterator
        public K next() {
            throw new NoSuchElementException();
        }

        @Override // it.unimi.dsi.fastutil.BidirectionalIterator, java.util.ListIterator
        public K previous() {
            throw new NoSuchElementException();
        }

        @Override // java.util.ListIterator
        public int nextIndex() {
            return 0;
        }

        @Override // java.util.ListIterator
        public int previousIndex() {
            return -1;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectIterator
        public int skip(int n) {
            return 0;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator
        public int back(int n) {
            return 0;
        }

        @Override // java.util.Iterator
        public void forEachRemaining(Consumer<? super K> action) {
        }

        public Object clone() {
            return ObjectIterators.EMPTY_ITERATOR;
        }

        private Object readResolve() {
            return ObjectIterators.EMPTY_ITERATOR;
        }
    }

    public static <K> ObjectIterator<K> emptyIterator() {
        return EMPTY_ITERATOR;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class SingletonIterator<K> implements ObjectListIterator<K> {
        private byte curr;
        private final K element;

        public SingletonIterator(K element) {
            this.element = element;
        }

        @Override // java.util.Iterator, java.util.ListIterator
        public boolean hasNext() {
            return this.curr == 0;
        }

        @Override // it.unimi.dsi.fastutil.BidirectionalIterator
        public boolean hasPrevious() {
            return this.curr == 1;
        }

        @Override // java.util.Iterator, java.util.ListIterator
        public K next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            this.curr = (byte) 1;
            return this.element;
        }

        @Override // it.unimi.dsi.fastutil.BidirectionalIterator, java.util.ListIterator
        public K previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            this.curr = (byte) 0;
            return this.element;
        }

        @Override // java.util.Iterator
        public void forEachRemaining(Consumer<? super K> consumer) {
            Objects.requireNonNull(consumer);
            if (this.curr == 0) {
                consumer.accept(this.element);
                this.curr = (byte) 1;
            }
        }

        @Override // java.util.ListIterator
        public int nextIndex() {
            return this.curr;
        }

        @Override // java.util.ListIterator
        public int previousIndex() {
            return this.curr - 1;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator
        public int back(int n) {
            if (n < 0) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
            if (n == 0 || this.curr < 1) {
                return 0;
            }
            this.curr = (byte) 1;
            return 1;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectIterator
        public int skip(int n) {
            if (n < 0) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
            if (n == 0 || this.curr > 0) {
                return 0;
            }
            this.curr = (byte) 0;
            return 1;
        }
    }

    public static <K> ObjectListIterator<K> singleton(K element) {
        return new SingletonIterator(element);
    }

    /* loaded from: classes4.dex */
    private static class ArrayIterator<K> implements ObjectListIterator<K> {
        private final K[] array;
        private int curr;
        private final int length;
        private final int offset;

        public ArrayIterator(K[] array, int offset, int length) {
            this.array = array;
            this.offset = offset;
            this.length = length;
        }

        @Override // java.util.Iterator, java.util.ListIterator
        public boolean hasNext() {
            return this.curr < this.length;
        }

        @Override // it.unimi.dsi.fastutil.BidirectionalIterator
        public boolean hasPrevious() {
            return this.curr > 0;
        }

        @Override // java.util.Iterator, java.util.ListIterator
        public K next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            K[] kArr = this.array;
            int i = this.offset;
            int i2 = this.curr;
            this.curr = i2 + 1;
            return kArr[i + i2];
        }

        @Override // it.unimi.dsi.fastutil.BidirectionalIterator, java.util.ListIterator
        public K previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            K[] kArr = this.array;
            int i = this.offset;
            int i2 = this.curr - 1;
            this.curr = i2;
            return kArr[i + i2];
        }

        @Override // java.util.Iterator
        public void forEachRemaining(Consumer<? super K> consumer) {
            Objects.requireNonNull(consumer);
            while (this.curr < this.length) {
                consumer.accept(this.array[this.offset + this.curr]);
                this.curr++;
            }
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectIterator
        public int skip(int n) {
            if (n < 0) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
            if (n <= this.length - this.curr) {
                this.curr += n;
                return n;
            }
            int n2 = this.length - this.curr;
            this.curr = this.length;
            return n2;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator
        public int back(int n) {
            if (n < 0) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
            if (n <= this.curr) {
                this.curr -= n;
                return n;
            }
            int n2 = this.curr;
            this.curr = 0;
            return n2;
        }

        @Override // java.util.ListIterator
        public int nextIndex() {
            return this.curr;
        }

        @Override // java.util.ListIterator
        public int previousIndex() {
            return this.curr - 1;
        }
    }

    public static <K> ObjectListIterator<K> wrap(K[] array, int offset, int length) {
        ObjectArrays.ensureOffsetLength(array, offset, length);
        return new ArrayIterator(array, offset, length);
    }

    public static <K> ObjectListIterator<K> wrap(K[] array) {
        return new ArrayIterator(array, 0, array.length);
    }

    public static <K> int unwrap(Iterator<? extends K> i, K[] array, int offset, int max) {
        int j;
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        if (offset < 0 || offset + max > array.length) {
            throw new IllegalArgumentException();
        }
        int offset2 = max;
        while (true) {
            j = offset2 - 1;
            if (offset2 == 0 || !i.hasNext()) {
                break;
            }
            array[offset] = i.next();
            offset++;
            offset2 = j;
        }
        return (max - j) - 1;
    }

    public static <K> int unwrap(Iterator<? extends K> i, K[] array) {
        return unwrap(i, array, 0, array.length);
    }

    public static <K> K[] unwrap(Iterator<? extends K> it2, int i) {
        if (i < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + i + ") is negative");
        }
        Object[] objArr = new Object[16];
        int i2 = 0;
        while (true) {
            int i3 = i - 1;
            if (i == 0 || !it2.hasNext()) {
                break;
            }
            if (i2 == objArr.length) {
                objArr = ObjectArrays.grow(objArr, i2 + 1);
            }
            objArr[i2] = it2.next();
            i2++;
            i = i3;
        }
        return (K[]) ObjectArrays.trim(objArr, i2);
    }

    public static <K> K[] unwrap(Iterator<? extends K> it2) {
        return (K[]) unwrap(it2, Integer.MAX_VALUE);
    }

    public static <K> long unwrap(Iterator<? extends K> i, K[][] array, long offset, long max) {
        long j;
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        if (offset < 0 || offset + max > BigArrays.length(array)) {
            throw new IllegalArgumentException();
        }
        long offset2 = max;
        while (true) {
            j = offset2 - 1;
            if (offset2 == 0 || !i.hasNext()) {
                break;
            }
            BigArrays.set(array, offset, i.next());
            offset++;
            offset2 = j;
        }
        return (max - j) - 1;
    }

    public static <K> long unwrap(Iterator<? extends K> i, K[][] array) {
        return unwrap(i, array, 0L, BigArrays.length(array));
    }

    public static <K> int unwrap(Iterator<K> i, ObjectCollection<? super K> c, int max) {
        int j;
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        int j2 = max;
        while (true) {
            j = j2 - 1;
            if (j2 == 0 || !i.hasNext()) {
                break;
            }
            c.add(i.next());
            j2 = j;
        }
        return (max - j) - 1;
    }

    public static <K> K[][] unwrapBig(Iterator<? extends K> it2, long j) {
        if (j < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + j + ") is negative");
        }
        Object[][] newBigArray = ObjectBigArrays.newBigArray(16L);
        long j2 = 0;
        while (true) {
            long j3 = j - 1;
            if (j == 0 || !it2.hasNext()) {
                break;
            }
            if (j2 == BigArrays.length(newBigArray)) {
                newBigArray = BigArrays.grow(newBigArray, j2 + 1);
            }
            BigArrays.set(newBigArray, j2, it2.next());
            j2++;
            j = j3;
        }
        return (K[][]) BigArrays.trim(newBigArray, j2);
    }

    public static <K> K[][] unwrapBig(Iterator<? extends K> it2) {
        return (K[][]) unwrapBig(it2, Long.MAX_VALUE);
    }

    public static <K> long unwrap(Iterator<K> i, ObjectCollection<? super K> c) {
        long n = 0;
        while (i.hasNext()) {
            c.add(i.next());
            n++;
        }
        return n;
    }

    public static <K> int pour(Iterator<K> i, ObjectCollection<? super K> s, int max) {
        int j;
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        int j2 = max;
        while (true) {
            j = j2 - 1;
            if (j2 == 0 || !i.hasNext()) {
                break;
            }
            s.add(i.next());
            j2 = j;
        }
        return (max - j) - 1;
    }

    public static <K> int pour(Iterator<K> i, ObjectCollection<? super K> s) {
        return pour(i, s, Integer.MAX_VALUE);
    }

    public static <K> ObjectList<K> pour(Iterator<K> i, int max) {
        ObjectArrayList<K> l = new ObjectArrayList<>();
        pour(i, l, max);
        l.trim();
        return l;
    }

    public static <K> ObjectList<K> pour(Iterator<K> i) {
        return pour(i, Integer.MAX_VALUE);
    }

    /* loaded from: classes4.dex */
    private static class IteratorWrapper<K> implements ObjectIterator<K> {
        final Iterator<K> i;

        public IteratorWrapper(Iterator<K> i) {
            this.i = i;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.i.hasNext();
        }

        @Override // java.util.Iterator
        public void remove() {
            this.i.remove();
        }

        @Override // java.util.Iterator
        public K next() {
            return this.i.next();
        }

        @Override // java.util.Iterator
        public void forEachRemaining(Consumer<? super K> action) {
            this.i.forEachRemaining(action);
        }
    }

    public static <K> ObjectIterator<K> asObjectIterator(Iterator<K> i) {
        return i instanceof ObjectIterator ? (ObjectIterator) i : new IteratorWrapper(i);
    }

    /* loaded from: classes4.dex */
    private static class ListIteratorWrapper<K> implements ObjectListIterator<K> {
        final ListIterator<K> i;

        public ListIteratorWrapper(ListIterator<K> i) {
            this.i = i;
        }

        @Override // java.util.Iterator, java.util.ListIterator
        public boolean hasNext() {
            return this.i.hasNext();
        }

        @Override // it.unimi.dsi.fastutil.BidirectionalIterator
        public boolean hasPrevious() {
            return this.i.hasPrevious();
        }

        @Override // java.util.ListIterator
        public int nextIndex() {
            return this.i.nextIndex();
        }

        @Override // java.util.ListIterator
        public int previousIndex() {
            return this.i.previousIndex();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectListIterator, java.util.ListIterator
        public void set(K k) {
            this.i.set(k);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectListIterator, java.util.ListIterator
        public void add(K k) {
            this.i.add(k);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectListIterator, java.util.Iterator, java.util.ListIterator
        public void remove() {
            this.i.remove();
        }

        @Override // java.util.Iterator, java.util.ListIterator
        public K next() {
            return this.i.next();
        }

        @Override // it.unimi.dsi.fastutil.BidirectionalIterator, java.util.ListIterator
        public K previous() {
            return this.i.previous();
        }

        @Override // java.util.Iterator
        public void forEachRemaining(Consumer<? super K> action) {
            this.i.forEachRemaining(action);
        }
    }

    public static <K> ObjectListIterator<K> asObjectIterator(ListIterator<K> i) {
        return i instanceof ObjectListIterator ? (ObjectListIterator) i : new ListIteratorWrapper(i);
    }

    public static <K> boolean any(Iterator<K> iterator, Predicate<? super K> predicate) {
        return indexOf(iterator, predicate) != -1;
    }

    public static <K> boolean all(Iterator<K> iterator, Predicate<? super K> predicate) {
        Objects.requireNonNull(predicate);
        while (iterator.hasNext()) {
            if (!predicate.test(iterator.next())) {
                return false;
            }
        }
        return true;
    }

    public static <K> int indexOf(Iterator<K> iterator, Predicate<? super K> predicate) {
        Objects.requireNonNull(predicate);
        int i = 0;
        while (iterator.hasNext()) {
            if (predicate.test(iterator.next())) {
                return i;
            }
            i++;
        }
        return -1;
    }

    /* loaded from: classes4.dex */
    public static abstract class AbstractIndexBasedIterator<K> extends AbstractObjectIterator<K> {
        protected int lastReturned;
        protected final int minPos;
        protected int pos;

        protected abstract K get(int i);

        protected abstract int getMaxPos();

        protected abstract void remove(int i);

        protected AbstractIndexBasedIterator(int minPos, int initialPos) {
            this.minPos = minPos;
            this.pos = initialPos;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.pos < getMaxPos();
        }

        @Override // java.util.Iterator, java.util.ListIterator
        public K next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            int i = this.pos;
            this.pos = i + 1;
            this.lastReturned = i;
            return get(i);
        }

        @Override // java.util.Iterator, it.unimi.dsi.fastutil.objects.ObjectListIterator, java.util.ListIterator
        public void remove() {
            if (this.lastReturned == -1) {
                throw new IllegalStateException();
            }
            remove(this.lastReturned);
            if (this.lastReturned < this.pos) {
                this.pos--;
            }
            this.lastReturned = -1;
        }

        @Override // java.util.Iterator
        public void forEachRemaining(Consumer<? super K> action) {
            while (this.pos < getMaxPos()) {
                int i = this.pos;
                this.pos = i + 1;
                this.lastReturned = i;
                action.accept(get(i));
            }
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectIterator
        public int skip(int n) {
            if (n < 0) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
            int max = getMaxPos();
            int remaining = max - this.pos;
            if (n < remaining) {
                this.pos += n;
            } else {
                n = remaining;
                this.pos = max;
            }
            this.lastReturned = this.pos - 1;
            return n;
        }
    }

    /* loaded from: classes4.dex */
    public static abstract class AbstractIndexBasedListIterator<K> extends AbstractIndexBasedIterator<K> implements ObjectListIterator<K> {
        protected abstract void add(int i, K k);

        protected abstract void set(int i, K k);

        /* JADX INFO: Access modifiers changed from: protected */
        public AbstractIndexBasedListIterator(int minPos, int initialPos) {
            super(minPos, initialPos);
        }

        @Override // it.unimi.dsi.fastutil.BidirectionalIterator
        public boolean hasPrevious() {
            return this.pos > this.minPos;
        }

        public K previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            int i = this.pos - 1;
            this.pos = i;
            this.lastReturned = i;
            return get(i);
        }

        @Override // java.util.ListIterator
        public int nextIndex() {
            return this.pos;
        }

        @Override // java.util.ListIterator
        public int previousIndex() {
            return this.pos - 1;
        }

        public void add(K k) {
            int i = this.pos;
            this.pos = i + 1;
            add(i, k);
            this.lastReturned = -1;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectListIterator, java.util.ListIterator
        public void set(K k) {
            if (this.lastReturned == -1) {
                throw new IllegalStateException();
            }
            set(this.lastReturned, k);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator
        public int back(int n) {
            if (n < 0) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
            int remaining = this.pos - this.minPos;
            if (n < remaining) {
                this.pos -= n;
            } else {
                n = remaining;
                this.pos = this.minPos;
            }
            this.lastReturned = this.pos;
            return n;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class IteratorConcatenator<K> implements ObjectIterator<K> {
        final ObjectIterator<? extends K>[] a;
        int lastOffset = -1;
        int length;
        int offset;

        public IteratorConcatenator(ObjectIterator<? extends K>[] a, int offset, int length) {
            this.a = a;
            this.offset = offset;
            this.length = length;
            advance();
        }

        private void advance() {
            while (this.length != 0 && !this.a[this.offset].hasNext()) {
                this.length--;
                this.offset++;
            }
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.length > 0;
        }

        @Override // java.util.Iterator
        public K next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            ObjectIterator<? extends K>[] objectIteratorArr = this.a;
            int i = this.offset;
            this.lastOffset = i;
            K next = objectIteratorArr[i].next();
            advance();
            return next;
        }

        @Override // java.util.Iterator
        public void forEachRemaining(Consumer<? super K> action) {
            while (this.length > 0) {
                ObjectIterator<? extends K>[] objectIteratorArr = this.a;
                int i = this.offset;
                this.lastOffset = i;
                objectIteratorArr[i].forEachRemaining(action);
                advance();
            }
        }

        @Override // java.util.Iterator
        public void remove() {
            if (this.lastOffset == -1) {
                throw new IllegalStateException();
            }
            this.a[this.lastOffset].remove();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectIterator
        public int skip(int n) {
            if (n < 0) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
            this.lastOffset = -1;
            int skipped = 0;
            while (skipped < n && this.length != 0) {
                skipped += this.a[this.offset].skip(n - skipped);
                if (this.a[this.offset].hasNext()) {
                    break;
                }
                this.length--;
                this.offset++;
            }
            return skipped;
        }
    }

    @SafeVarargs
    public static <K> ObjectIterator<K> concat(ObjectIterator<? extends K>... a) {
        return concat(a, 0, a.length);
    }

    public static <K> ObjectIterator<K> concat(ObjectIterator<? extends K>[] a, int offset, int length) {
        return new IteratorConcatenator(a, offset, length);
    }

    /* loaded from: classes4.dex */
    public static class UnmodifiableIterator<K> implements ObjectIterator<K> {
        protected final ObjectIterator<? extends K> i;

        public UnmodifiableIterator(ObjectIterator<? extends K> i) {
            this.i = i;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.i.hasNext();
        }

        @Override // java.util.Iterator
        public K next() {
            return this.i.next();
        }

        @Override // java.util.Iterator
        public void forEachRemaining(Consumer<? super K> action) {
            this.i.forEachRemaining(action);
        }
    }

    public static <K> ObjectIterator<K> unmodifiable(ObjectIterator<? extends K> i) {
        return new UnmodifiableIterator(i);
    }

    /* loaded from: classes4.dex */
    public static class UnmodifiableBidirectionalIterator<K> implements ObjectBidirectionalIterator<K> {
        protected final ObjectBidirectionalIterator<? extends K> i;

        public UnmodifiableBidirectionalIterator(ObjectBidirectionalIterator<? extends K> i) {
            this.i = i;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.i.hasNext();
        }

        @Override // it.unimi.dsi.fastutil.BidirectionalIterator
        public boolean hasPrevious() {
            return this.i.hasPrevious();
        }

        @Override // java.util.Iterator
        public K next() {
            return (K) this.i.next();
        }

        @Override // it.unimi.dsi.fastutil.BidirectionalIterator, java.util.ListIterator
        public K previous() {
            return this.i.previous();
        }

        @Override // java.util.Iterator
        public void forEachRemaining(Consumer<? super K> action) {
            this.i.forEachRemaining(action);
        }
    }

    public static <K> ObjectBidirectionalIterator<K> unmodifiable(ObjectBidirectionalIterator<? extends K> i) {
        return new UnmodifiableBidirectionalIterator(i);
    }

    /* loaded from: classes4.dex */
    public static class UnmodifiableListIterator<K> implements ObjectListIterator<K> {
        protected final ObjectListIterator<? extends K> i;

        public UnmodifiableListIterator(ObjectListIterator<? extends K> i) {
            this.i = i;
        }

        @Override // java.util.Iterator, java.util.ListIterator
        public boolean hasNext() {
            return this.i.hasNext();
        }

        @Override // it.unimi.dsi.fastutil.BidirectionalIterator
        public boolean hasPrevious() {
            return this.i.hasPrevious();
        }

        @Override // java.util.Iterator, java.util.ListIterator
        public K next() {
            return this.i.next();
        }

        @Override // it.unimi.dsi.fastutil.BidirectionalIterator, java.util.ListIterator
        public K previous() {
            return this.i.previous();
        }

        @Override // java.util.ListIterator
        public int nextIndex() {
            return this.i.nextIndex();
        }

        @Override // java.util.ListIterator
        public int previousIndex() {
            return this.i.previousIndex();
        }

        @Override // java.util.Iterator
        public void forEachRemaining(Consumer<? super K> action) {
            this.i.forEachRemaining(action);
        }
    }

    public static <K> ObjectListIterator<K> unmodifiable(ObjectListIterator<? extends K> i) {
        return new UnmodifiableListIterator(i);
    }
}
