package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.BigArrays;
import it.unimi.dsi.fastutil.bytes.ByteConsumer;
import it.unimi.dsi.fastutil.bytes.ByteConsumer$$ExternalSyntheticLambda0;
import it.unimi.dsi.fastutil.bytes.ByteIterator;
import it.unimi.dsi.fastutil.chars.CharConsumer;
import it.unimi.dsi.fastutil.chars.CharConsumer$$ExternalSyntheticLambda1;
import it.unimi.dsi.fastutil.chars.CharIterator;
import it.unimi.dsi.fastutil.shorts.ShortConsumer;
import it.unimi.dsi.fastutil.shorts.ShortIterator;
import java.io.Serializable;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;

/* loaded from: classes4.dex */
public final class IntIterators {
    public static final EmptyIterator EMPTY_ITERATOR = new EmptyIterator();

    private IntIterators() {
    }

    /* loaded from: classes4.dex */
    public static class EmptyIterator implements IntListIterator, Serializable, Cloneable {
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

        @Override // it.unimi.dsi.fastutil.ints.IntIterator, java.util.PrimitiveIterator.OfInt
        public int nextInt() {
            throw new NoSuchElementException();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntBidirectionalIterator
        public int previousInt() {
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

        @Override // it.unimi.dsi.fastutil.ints.IntBidirectionalIterator, it.unimi.dsi.fastutil.ints.IntIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectIterator
        public int skip(int n) {
            return 0;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator
        public int back(int n) {
            return 0;
        }

        @Override // java.util.PrimitiveIterator
        public void forEachRemaining(java.util.function.IntConsumer action) {
        }

        @Override // it.unimi.dsi.fastutil.ints.IntIterator, java.util.PrimitiveIterator.OfInt, java.util.Iterator
        @Deprecated
        public void forEachRemaining(Consumer<? super Integer> action) {
        }

        public Object clone() {
            return IntIterators.EMPTY_ITERATOR;
        }

        private Object readResolve() {
            return IntIterators.EMPTY_ITERATOR;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class SingletonIterator implements IntListIterator {
        private byte curr;
        private final int element;

        public SingletonIterator(int element) {
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

        @Override // it.unimi.dsi.fastutil.ints.IntIterator, java.util.PrimitiveIterator.OfInt
        public int nextInt() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            this.curr = (byte) 1;
            return this.element;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntBidirectionalIterator
        public int previousInt() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            this.curr = (byte) 0;
            return this.element;
        }

        @Override // java.util.PrimitiveIterator
        public void forEachRemaining(java.util.function.IntConsumer action) {
            Objects.requireNonNull(action);
            if (this.curr == 0) {
                action.accept(this.element);
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

        @Override // it.unimi.dsi.fastutil.ints.IntBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator
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

        @Override // it.unimi.dsi.fastutil.ints.IntBidirectionalIterator, it.unimi.dsi.fastutil.ints.IntIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectIterator
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

    public static IntListIterator singleton(int element) {
        return new SingletonIterator(element);
    }

    /* loaded from: classes4.dex */
    private static class ArrayIterator implements IntListIterator {
        private final int[] array;
        private int curr;
        private final int length;
        private final int offset;

        public ArrayIterator(int[] array, int offset, int length) {
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

        @Override // it.unimi.dsi.fastutil.ints.IntIterator, java.util.PrimitiveIterator.OfInt
        public int nextInt() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            int[] iArr = this.array;
            int i = this.offset;
            int i2 = this.curr;
            this.curr = i2 + 1;
            return iArr[i + i2];
        }

        @Override // it.unimi.dsi.fastutil.ints.IntBidirectionalIterator
        public int previousInt() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            int[] iArr = this.array;
            int i = this.offset;
            int i2 = this.curr - 1;
            this.curr = i2;
            return iArr[i + i2];
        }

        @Override // java.util.PrimitiveIterator
        public void forEachRemaining(java.util.function.IntConsumer action) {
            Objects.requireNonNull(action);
            while (this.curr < this.length) {
                action.accept(this.array[this.offset + this.curr]);
                this.curr++;
            }
        }

        @Override // it.unimi.dsi.fastutil.ints.IntBidirectionalIterator, it.unimi.dsi.fastutil.ints.IntIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectIterator
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

        @Override // it.unimi.dsi.fastutil.ints.IntBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator
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

    public static IntListIterator wrap(int[] array, int offset, int length) {
        IntArrays.ensureOffsetLength(array, offset, length);
        return new ArrayIterator(array, offset, length);
    }

    public static IntListIterator wrap(int[] array) {
        return new ArrayIterator(array, 0, array.length);
    }

    public static int unwrap(IntIterator i, int[] array, int offset, int max) {
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
            array[offset] = i.nextInt();
            offset++;
            offset2 = j;
        }
        return (max - j) - 1;
    }

    public static int unwrap(IntIterator i, int[] array) {
        return unwrap(i, array, 0, array.length);
    }

    public static int[] unwrap(IntIterator i, int j) {
        if (j < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + j + ") is negative");
        }
        int[] array = new int[16];
        int j2 = 0;
        while (true) {
            int max = j - 1;
            if (j == 0 || !i.hasNext()) {
                break;
            }
            if (j2 == array.length) {
                array = IntArrays.grow(array, j2 + 1);
            }
            array[j2] = i.nextInt();
            j2++;
            j = max;
        }
        return IntArrays.trim(array, j2);
    }

    public static int[] unwrap(IntIterator i) {
        return unwrap(i, Integer.MAX_VALUE);
    }

    public static long unwrap(IntIterator i, int[][] array, long offset, long max) {
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
            BigArrays.set(array, offset, i.nextInt());
            offset++;
            offset2 = j;
        }
        return (max - j) - 1;
    }

    public static long unwrap(IntIterator i, int[][] array) {
        return unwrap(i, array, 0L, BigArrays.length(array));
    }

    public static int unwrap(IntIterator i, IntCollection c, int max) {
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
            c.add(i.nextInt());
            j2 = j;
        }
        return (max - j) - 1;
    }

    public static int[][] unwrapBig(IntIterator i, long j) {
        if (j < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + j + ") is negative");
        }
        int[][] array = IntBigArrays.newBigArray(16L);
        long j2 = 0;
        while (true) {
            long max = j - 1;
            if (j == 0 || !i.hasNext()) {
                break;
            }
            if (j2 == BigArrays.length(array)) {
                array = BigArrays.grow(array, j2 + 1);
            }
            BigArrays.set(array, j2, i.nextInt());
            j2++;
            j = max;
        }
        return BigArrays.trim(array, j2);
    }

    public static int[][] unwrapBig(IntIterator i) {
        return unwrapBig(i, Long.MAX_VALUE);
    }

    public static long unwrap(IntIterator i, IntCollection c) {
        long n = 0;
        while (i.hasNext()) {
            c.add(i.nextInt());
            n++;
        }
        return n;
    }

    public static int pour(IntIterator i, IntCollection s, int max) {
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
            s.add(i.nextInt());
            j2 = j;
        }
        return (max - j) - 1;
    }

    public static int pour(IntIterator i, IntCollection s) {
        return pour(i, s, Integer.MAX_VALUE);
    }

    public static IntList pour(IntIterator i, int max) {
        IntArrayList l = new IntArrayList();
        pour(i, l, max);
        l.trim();
        return l;
    }

    public static IntList pour(IntIterator i) {
        return pour(i, Integer.MAX_VALUE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class IteratorWrapper implements IntIterator {
        final Iterator<Integer> i;

        public IteratorWrapper(Iterator<Integer> i) {
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

        @Override // it.unimi.dsi.fastutil.ints.IntIterator, java.util.PrimitiveIterator.OfInt
        public int nextInt() {
            return this.i.next().intValue();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntIterator
        public void forEachRemaining(IntConsumer action) {
            this.i.forEachRemaining(action);
        }

        @Override // java.util.PrimitiveIterator
        public void forEachRemaining(java.util.function.IntConsumer action) {
            Consumer<? super Integer> intIterators$IteratorWrapper$$ExternalSyntheticLambda0;
            Objects.requireNonNull(action);
            Iterator<Integer> it2 = this.i;
            if (action instanceof Consumer) {
                intIterators$IteratorWrapper$$ExternalSyntheticLambda0 = (Consumer) action;
            } else {
                action.getClass();
                intIterators$IteratorWrapper$$ExternalSyntheticLambda0 = new IntIterators$IteratorWrapper$$ExternalSyntheticLambda0(action);
            }
            it2.forEachRemaining(intIterators$IteratorWrapper$$ExternalSyntheticLambda0);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntIterator, java.util.PrimitiveIterator.OfInt, java.util.Iterator
        @Deprecated
        public void forEachRemaining(Consumer<? super Integer> action) {
            this.i.forEachRemaining(action);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class PrimitiveIteratorWrapper implements IntIterator {
        final PrimitiveIterator.OfInt i;

        public PrimitiveIteratorWrapper(PrimitiveIterator.OfInt i) {
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

        @Override // it.unimi.dsi.fastutil.ints.IntIterator, java.util.PrimitiveIterator.OfInt
        public int nextInt() {
            return this.i.nextInt();
        }

        @Override // java.util.PrimitiveIterator
        public void forEachRemaining(java.util.function.IntConsumer action) {
            this.i.forEachRemaining(action);
        }
    }

    public static IntIterator asIntIterator(Iterator i) {
        return i instanceof IntIterator ? (IntIterator) i : i instanceof PrimitiveIterator.OfInt ? new PrimitiveIteratorWrapper((PrimitiveIterator.OfInt) i) : new IteratorWrapper(i);
    }

    /* loaded from: classes4.dex */
    private static class ListIteratorWrapper implements IntListIterator {
        final ListIterator<Integer> i;

        public ListIteratorWrapper(ListIterator<Integer> i) {
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

        @Override // it.unimi.dsi.fastutil.ints.IntListIterator
        public void set(int k) {
            this.i.set(Integer.valueOf(k));
        }

        @Override // it.unimi.dsi.fastutil.ints.IntListIterator
        public void add(int k) {
            this.i.add(Integer.valueOf(k));
        }

        @Override // it.unimi.dsi.fastutil.ints.IntListIterator, java.util.Iterator, java.util.ListIterator
        public void remove() {
            this.i.remove();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntIterator, java.util.PrimitiveIterator.OfInt
        public int nextInt() {
            return this.i.next().intValue();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntBidirectionalIterator
        public int previousInt() {
            return this.i.previous().intValue();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntIterator
        public void forEachRemaining(IntConsumer action) {
            this.i.forEachRemaining(action);
        }

        @Override // java.util.PrimitiveIterator
        public void forEachRemaining(java.util.function.IntConsumer action) {
            Consumer<? super Integer> intIterators$IteratorWrapper$$ExternalSyntheticLambda0;
            Objects.requireNonNull(action);
            ListIterator<Integer> listIterator = this.i;
            if (action instanceof Consumer) {
                intIterators$IteratorWrapper$$ExternalSyntheticLambda0 = (Consumer) action;
            } else {
                action.getClass();
                intIterators$IteratorWrapper$$ExternalSyntheticLambda0 = new IntIterators$IteratorWrapper$$ExternalSyntheticLambda0(action);
            }
            listIterator.forEachRemaining(intIterators$IteratorWrapper$$ExternalSyntheticLambda0);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntIterator, java.util.PrimitiveIterator.OfInt, java.util.Iterator
        @Deprecated
        public void forEachRemaining(Consumer<? super Integer> action) {
            this.i.forEachRemaining(action);
        }
    }

    public static IntListIterator asIntIterator(ListIterator i) {
        return i instanceof IntListIterator ? (IntListIterator) i : new ListIteratorWrapper(i);
    }

    public static boolean any(IntIterator iterator, java.util.function.IntPredicate predicate) {
        return indexOf(iterator, predicate) != -1;
    }

    public static boolean all(IntIterator iterator, java.util.function.IntPredicate predicate) {
        Objects.requireNonNull(predicate);
        while (iterator.hasNext()) {
            if (!predicate.test(iterator.nextInt())) {
                return false;
            }
        }
        return true;
    }

    public static int indexOf(IntIterator iterator, java.util.function.IntPredicate predicate) {
        Objects.requireNonNull(predicate);
        int i = 0;
        while (iterator.hasNext()) {
            if (predicate.test(iterator.nextInt())) {
                return i;
            }
            i++;
        }
        return -1;
    }

    /* loaded from: classes4.dex */
    public static abstract class AbstractIndexBasedIterator extends AbstractIntIterator {
        protected int lastReturned;
        protected final int minPos;
        protected int pos;

        protected abstract int get(int i);

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

        @Override // it.unimi.dsi.fastutil.ints.IntIterator, java.util.PrimitiveIterator.OfInt
        public int nextInt() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            int i = this.pos;
            this.pos = i + 1;
            this.lastReturned = i;
            return get(i);
        }

        @Override // java.util.Iterator, it.unimi.dsi.fastutil.ints.IntListIterator, java.util.ListIterator
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

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.PrimitiveIterator
        public void forEachRemaining(java.util.function.IntConsumer action) {
            while (this.pos < getMaxPos()) {
                int i = this.pos;
                this.pos = i + 1;
                this.lastReturned = i;
                action.accept(get(i));
            }
        }

        @Override // it.unimi.dsi.fastutil.ints.IntIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectIterator
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
    public static abstract class AbstractIndexBasedListIterator extends AbstractIndexBasedIterator implements IntListIterator {
        protected abstract void add(int i, int i2);

        protected abstract void set(int i, int i2);

        /* JADX INFO: Access modifiers changed from: protected */
        public AbstractIndexBasedListIterator(int minPos, int initialPos) {
            super(minPos, initialPos);
        }

        @Override // it.unimi.dsi.fastutil.BidirectionalIterator
        public boolean hasPrevious() {
            return this.pos > this.minPos;
        }

        public int previousInt() {
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

        public void add(int k) {
            int i = this.pos;
            this.pos = i + 1;
            add(i, k);
            this.lastReturned = -1;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntListIterator
        public void set(int k) {
            if (this.lastReturned == -1) {
                throw new IllegalStateException();
            }
            set(this.lastReturned, k);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator
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

    /* loaded from: classes4.dex */
    private static class IntervalIterator implements IntListIterator {
        int curr;
        private final int from;
        private final int to;

        public IntervalIterator(int from, int to) {
            this.curr = from;
            this.from = from;
            this.to = to;
        }

        @Override // java.util.Iterator, java.util.ListIterator
        public boolean hasNext() {
            return this.curr < this.to;
        }

        @Override // it.unimi.dsi.fastutil.BidirectionalIterator
        public boolean hasPrevious() {
            return this.curr > this.from;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntIterator, java.util.PrimitiveIterator.OfInt
        public int nextInt() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            int i = this.curr;
            this.curr = i + 1;
            return i;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntBidirectionalIterator
        public int previousInt() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            int i = this.curr - 1;
            this.curr = i;
            return i;
        }

        @Override // java.util.PrimitiveIterator
        public void forEachRemaining(java.util.function.IntConsumer action) {
            Objects.requireNonNull(action);
            while (this.curr < this.to) {
                action.accept(this.curr);
                this.curr++;
            }
        }

        @Override // java.util.ListIterator
        public int nextIndex() {
            return this.curr - this.from;
        }

        @Override // java.util.ListIterator
        public int previousIndex() {
            return (this.curr - this.from) - 1;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntBidirectionalIterator, it.unimi.dsi.fastutil.ints.IntIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectIterator
        public int skip(int n) {
            if (n < 0) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
            if (this.curr + n <= this.to) {
                this.curr += n;
                return n;
            }
            int n2 = this.to - this.curr;
            this.curr = this.to;
            return n2;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator
        public int back(int n) {
            if (this.curr - n >= this.from) {
                this.curr -= n;
                return n;
            }
            int n2 = this.curr - this.from;
            this.curr = this.from;
            return n2;
        }
    }

    public static IntListIterator fromTo(int from, int to) {
        return new IntervalIterator(from, to);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class IteratorConcatenator implements IntIterator {
        final IntIterator[] a;
        int lastOffset = -1;
        int length;
        int offset;

        public IteratorConcatenator(IntIterator[] a, int offset, int length) {
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

        @Override // it.unimi.dsi.fastutil.ints.IntIterator, java.util.PrimitiveIterator.OfInt
        public int nextInt() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            IntIterator[] intIteratorArr = this.a;
            int i = this.offset;
            this.lastOffset = i;
            int next = intIteratorArr[i].nextInt();
            advance();
            return next;
        }

        @Override // java.util.PrimitiveIterator
        public void forEachRemaining(java.util.function.IntConsumer action) {
            while (this.length > 0) {
                IntIterator[] intIteratorArr = this.a;
                int i = this.offset;
                this.lastOffset = i;
                intIteratorArr[i].forEachRemaining(action);
                advance();
            }
        }

        @Override // it.unimi.dsi.fastutil.ints.IntIterator, java.util.PrimitiveIterator.OfInt, java.util.Iterator
        @Deprecated
        public void forEachRemaining(Consumer<? super Integer> action) {
            while (this.length > 0) {
                IntIterator[] intIteratorArr = this.a;
                int i = this.offset;
                this.lastOffset = i;
                intIteratorArr[i].forEachRemaining(action);
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

        @Override // it.unimi.dsi.fastutil.ints.IntIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectIterator
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

    public static IntIterator concat(IntIterator... a) {
        return concat(a, 0, a.length);
    }

    public static IntIterator concat(IntIterator[] a, int offset, int length) {
        return new IteratorConcatenator(a, offset, length);
    }

    /* loaded from: classes4.dex */
    public static class UnmodifiableIterator implements IntIterator {
        protected final IntIterator i;

        public UnmodifiableIterator(IntIterator i) {
            this.i = i;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.i.hasNext();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntIterator, java.util.PrimitiveIterator.OfInt
        public int nextInt() {
            return this.i.nextInt();
        }

        @Override // java.util.PrimitiveIterator
        public void forEachRemaining(java.util.function.IntConsumer action) {
            this.i.forEachRemaining(action);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntIterator, java.util.PrimitiveIterator.OfInt, java.util.Iterator
        @Deprecated
        public void forEachRemaining(Consumer<? super Integer> action) {
            this.i.forEachRemaining(action);
        }
    }

    public static IntIterator unmodifiable(IntIterator i) {
        return new UnmodifiableIterator(i);
    }

    /* loaded from: classes4.dex */
    public static class UnmodifiableBidirectionalIterator implements IntBidirectionalIterator {
        protected final IntBidirectionalIterator i;

        public UnmodifiableBidirectionalIterator(IntBidirectionalIterator i) {
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

        @Override // it.unimi.dsi.fastutil.ints.IntIterator, java.util.PrimitiveIterator.OfInt
        public int nextInt() {
            return this.i.nextInt();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntBidirectionalIterator
        public int previousInt() {
            return this.i.previousInt();
        }

        @Override // java.util.PrimitiveIterator
        public void forEachRemaining(java.util.function.IntConsumer action) {
            this.i.forEachRemaining(action);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntIterator, java.util.PrimitiveIterator.OfInt, java.util.Iterator
        @Deprecated
        public void forEachRemaining(Consumer<? super Integer> action) {
            this.i.forEachRemaining(action);
        }
    }

    public static IntBidirectionalIterator unmodifiable(IntBidirectionalIterator i) {
        return new UnmodifiableBidirectionalIterator(i);
    }

    /* loaded from: classes4.dex */
    public static class UnmodifiableListIterator implements IntListIterator {
        protected final IntListIterator i;

        public UnmodifiableListIterator(IntListIterator i) {
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

        @Override // it.unimi.dsi.fastutil.ints.IntIterator, java.util.PrimitiveIterator.OfInt
        public int nextInt() {
            return this.i.nextInt();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntBidirectionalIterator
        public int previousInt() {
            return this.i.previousInt();
        }

        @Override // java.util.ListIterator
        public int nextIndex() {
            return this.i.nextIndex();
        }

        @Override // java.util.ListIterator
        public int previousIndex() {
            return this.i.previousIndex();
        }

        @Override // java.util.PrimitiveIterator
        public void forEachRemaining(java.util.function.IntConsumer action) {
            this.i.forEachRemaining(action);
        }

        @Override // it.unimi.dsi.fastutil.ints.IntIterator, java.util.PrimitiveIterator.OfInt, java.util.Iterator
        @Deprecated
        public void forEachRemaining(Consumer<? super Integer> action) {
            this.i.forEachRemaining(action);
        }
    }

    public static IntListIterator unmodifiable(IntListIterator i) {
        return new UnmodifiableListIterator(i);
    }

    /* loaded from: classes4.dex */
    private static final class ByteIteratorWrapper implements IntIterator {
        final ByteIterator iterator;

        public ByteIteratorWrapper(ByteIterator iterator) {
            this.iterator = iterator;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntIterator, java.util.PrimitiveIterator.OfInt, java.util.Iterator
        @Deprecated
        public Integer next() {
            return Integer.valueOf(this.iterator.nextByte());
        }

        @Override // it.unimi.dsi.fastutil.ints.IntIterator, java.util.PrimitiveIterator.OfInt
        public int nextInt() {
            return this.iterator.nextByte();
        }

        @Override // java.util.PrimitiveIterator
        public void forEachRemaining(java.util.function.IntConsumer action) {
            Objects.requireNonNull(action);
            ByteIterator byteIterator = this.iterator;
            action.getClass();
            byteIterator.forEachRemaining((ByteConsumer) new ByteConsumer$$ExternalSyntheticLambda0(action));
        }

        @Override // java.util.Iterator
        public void remove() {
            this.iterator.remove();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectIterator
        public int skip(int n) {
            return this.iterator.skip(n);
        }
    }

    public static IntIterator wrap(ByteIterator iterator) {
        return new ByteIteratorWrapper(iterator);
    }

    /* loaded from: classes4.dex */
    private static final class ShortIteratorWrapper implements IntIterator {
        final ShortIterator iterator;

        public ShortIteratorWrapper(ShortIterator iterator) {
            this.iterator = iterator;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntIterator, java.util.PrimitiveIterator.OfInt, java.util.Iterator
        @Deprecated
        public Integer next() {
            return Integer.valueOf(this.iterator.nextShort());
        }

        @Override // it.unimi.dsi.fastutil.ints.IntIterator, java.util.PrimitiveIterator.OfInt
        public int nextInt() {
            return this.iterator.nextShort();
        }

        @Override // java.util.PrimitiveIterator
        public void forEachRemaining(java.util.function.IntConsumer action) {
            Objects.requireNonNull(action);
            ShortIterator shortIterator = this.iterator;
            action.getClass();
            shortIterator.forEachRemaining((ShortConsumer) new IntIterators$ShortIteratorWrapper$$ExternalSyntheticLambda0(action));
        }

        @Override // java.util.Iterator
        public void remove() {
            this.iterator.remove();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectIterator
        public int skip(int n) {
            return this.iterator.skip(n);
        }
    }

    public static IntIterator wrap(ShortIterator iterator) {
        return new ShortIteratorWrapper(iterator);
    }

    /* loaded from: classes4.dex */
    private static final class CharIteratorWrapper implements IntIterator {
        final CharIterator iterator;

        public CharIteratorWrapper(CharIterator iterator) {
            this.iterator = iterator;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntIterator, java.util.PrimitiveIterator.OfInt, java.util.Iterator
        @Deprecated
        public Integer next() {
            return Integer.valueOf(this.iterator.nextChar());
        }

        @Override // it.unimi.dsi.fastutil.ints.IntIterator, java.util.PrimitiveIterator.OfInt
        public int nextInt() {
            return this.iterator.nextChar();
        }

        @Override // java.util.PrimitiveIterator
        public void forEachRemaining(java.util.function.IntConsumer action) {
            Objects.requireNonNull(action);
            CharIterator charIterator = this.iterator;
            action.getClass();
            charIterator.forEachRemaining((CharConsumer) new CharConsumer$$ExternalSyntheticLambda1(action));
        }

        @Override // java.util.Iterator
        public void remove() {
            this.iterator.remove();
        }

        @Override // it.unimi.dsi.fastutil.ints.IntIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectIterator
        public int skip(int n) {
            return this.iterator.skip(n);
        }
    }

    public static IntIterator wrap(CharIterator iterator) {
        return new CharIteratorWrapper(iterator);
    }
}
