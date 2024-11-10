package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.BigArrays;
import it.unimi.dsi.fastutil.bytes.ByteConsumer;
import it.unimi.dsi.fastutil.bytes.ByteIterator;
import it.unimi.dsi.fastutil.chars.CharConsumer;
import it.unimi.dsi.fastutil.chars.CharIterator;
import it.unimi.dsi.fastutil.ints.IntConsumer;
import it.unimi.dsi.fastutil.ints.IntIterator;
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
public final class LongIterators {
    public static final EmptyIterator EMPTY_ITERATOR = new EmptyIterator();

    private LongIterators() {
    }

    /* loaded from: classes4.dex */
    public static class EmptyIterator implements LongListIterator, Serializable, Cloneable {
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

        @Override // it.unimi.dsi.fastutil.longs.LongIterator, java.util.PrimitiveIterator.OfLong
        public long nextLong() {
            throw new NoSuchElementException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongBidirectionalIterator
        public long previousLong() {
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

        @Override // it.unimi.dsi.fastutil.longs.LongBidirectionalIterator, it.unimi.dsi.fastutil.longs.LongIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectIterator
        public int skip(int n) {
            return 0;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator
        public int back(int n) {
            return 0;
        }

        @Override // java.util.PrimitiveIterator
        public void forEachRemaining(java.util.function.LongConsumer action) {
        }

        @Override // it.unimi.dsi.fastutil.longs.LongIterator, java.util.PrimitiveIterator.OfLong, java.util.Iterator
        @Deprecated
        public void forEachRemaining(Consumer<? super Long> action) {
        }

        public Object clone() {
            return LongIterators.EMPTY_ITERATOR;
        }

        private Object readResolve() {
            return LongIterators.EMPTY_ITERATOR;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class SingletonIterator implements LongListIterator {
        private byte curr;
        private final long element;

        public SingletonIterator(long element) {
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

        @Override // it.unimi.dsi.fastutil.longs.LongIterator, java.util.PrimitiveIterator.OfLong
        public long nextLong() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            this.curr = (byte) 1;
            return this.element;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongBidirectionalIterator
        public long previousLong() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            this.curr = (byte) 0;
            return this.element;
        }

        @Override // java.util.PrimitiveIterator
        public void forEachRemaining(java.util.function.LongConsumer action) {
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

        @Override // it.unimi.dsi.fastutil.longs.LongBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator
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

        @Override // it.unimi.dsi.fastutil.longs.LongBidirectionalIterator, it.unimi.dsi.fastutil.longs.LongIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectIterator
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

    public static LongListIterator singleton(long element) {
        return new SingletonIterator(element);
    }

    /* loaded from: classes4.dex */
    private static class ArrayIterator implements LongListIterator {
        private final long[] array;
        private int curr;
        private final int length;
        private final int offset;

        public ArrayIterator(long[] array, int offset, int length) {
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

        @Override // it.unimi.dsi.fastutil.longs.LongIterator, java.util.PrimitiveIterator.OfLong
        public long nextLong() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            long[] jArr = this.array;
            int i = this.offset;
            int i2 = this.curr;
            this.curr = i2 + 1;
            return jArr[i + i2];
        }

        @Override // it.unimi.dsi.fastutil.longs.LongBidirectionalIterator
        public long previousLong() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            long[] jArr = this.array;
            int i = this.offset;
            int i2 = this.curr - 1;
            this.curr = i2;
            return jArr[i + i2];
        }

        @Override // java.util.PrimitiveIterator
        public void forEachRemaining(java.util.function.LongConsumer action) {
            Objects.requireNonNull(action);
            while (this.curr < this.length) {
                action.accept(this.array[this.offset + this.curr]);
                this.curr++;
            }
        }

        @Override // it.unimi.dsi.fastutil.longs.LongBidirectionalIterator, it.unimi.dsi.fastutil.longs.LongIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectIterator
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

        @Override // it.unimi.dsi.fastutil.longs.LongBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator
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

    public static LongListIterator wrap(long[] array, int offset, int length) {
        LongArrays.ensureOffsetLength(array, offset, length);
        return new ArrayIterator(array, offset, length);
    }

    public static LongListIterator wrap(long[] array) {
        return new ArrayIterator(array, 0, array.length);
    }

    public static int unwrap(LongIterator i, long[] array, int offset, int max) {
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
            array[offset] = i.nextLong();
            offset++;
            offset2 = j;
        }
        return (max - j) - 1;
    }

    public static int unwrap(LongIterator i, long[] array) {
        return unwrap(i, array, 0, array.length);
    }

    public static long[] unwrap(LongIterator i, int j) {
        if (j < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + j + ") is negative");
        }
        long[] array = new long[16];
        int j2 = 0;
        while (true) {
            int max = j - 1;
            if (j == 0 || !i.hasNext()) {
                break;
            }
            if (j2 == array.length) {
                array = LongArrays.grow(array, j2 + 1);
            }
            array[j2] = i.nextLong();
            j2++;
            j = max;
        }
        return LongArrays.trim(array, j2);
    }

    public static long[] unwrap(LongIterator i) {
        return unwrap(i, Integer.MAX_VALUE);
    }

    public static long unwrap(LongIterator i, long[][] array, long offset, long max) {
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
            BigArrays.set(array, offset, i.nextLong());
            offset++;
            offset2 = j;
        }
        return (max - j) - 1;
    }

    public static long unwrap(LongIterator i, long[][] array) {
        return unwrap(i, array, 0L, BigArrays.length(array));
    }

    public static int unwrap(LongIterator i, LongCollection c, int max) {
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
            c.add(i.nextLong());
            j2 = j;
        }
        return (max - j) - 1;
    }

    public static long[][] unwrapBig(LongIterator i, long j) {
        if (j < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + j + ") is negative");
        }
        long[][] array = LongBigArrays.newBigArray(16L);
        long j2 = 0;
        while (true) {
            long max = j - 1;
            if (j == 0 || !i.hasNext()) {
                break;
            }
            if (j2 == BigArrays.length(array)) {
                array = BigArrays.grow(array, j2 + 1);
            }
            BigArrays.set(array, j2, i.nextLong());
            j2++;
            j = max;
        }
        return BigArrays.trim(array, j2);
    }

    public static long[][] unwrapBig(LongIterator i) {
        return unwrapBig(i, Long.MAX_VALUE);
    }

    public static long unwrap(LongIterator i, LongCollection c) {
        long n = 0;
        while (i.hasNext()) {
            c.add(i.nextLong());
            n++;
        }
        return n;
    }

    public static int pour(LongIterator i, LongCollection s, int max) {
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
            s.add(i.nextLong());
            j2 = j;
        }
        return (max - j) - 1;
    }

    public static int pour(LongIterator i, LongCollection s) {
        return pour(i, s, Integer.MAX_VALUE);
    }

    public static LongList pour(LongIterator i, int max) {
        LongArrayList l = new LongArrayList();
        pour(i, l, max);
        l.trim();
        return l;
    }

    public static LongList pour(LongIterator i) {
        return pour(i, Integer.MAX_VALUE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class IteratorWrapper implements LongIterator {
        final Iterator<Long> i;

        public IteratorWrapper(Iterator<Long> i) {
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

        @Override // it.unimi.dsi.fastutil.longs.LongIterator, java.util.PrimitiveIterator.OfLong
        public long nextLong() {
            return this.i.next().longValue();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongIterator
        public void forEachRemaining(LongConsumer action) {
            this.i.forEachRemaining(action);
        }

        @Override // java.util.PrimitiveIterator
        public void forEachRemaining(java.util.function.LongConsumer action) {
            Consumer<? super Long> longIterators$IteratorWrapper$$ExternalSyntheticLambda0;
            Objects.requireNonNull(action);
            Iterator<Long> it2 = this.i;
            if (action instanceof Consumer) {
                longIterators$IteratorWrapper$$ExternalSyntheticLambda0 = (Consumer) action;
            } else {
                action.getClass();
                longIterators$IteratorWrapper$$ExternalSyntheticLambda0 = new LongIterators$IteratorWrapper$$ExternalSyntheticLambda0(action);
            }
            it2.forEachRemaining(longIterators$IteratorWrapper$$ExternalSyntheticLambda0);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongIterator, java.util.PrimitiveIterator.OfLong, java.util.Iterator
        @Deprecated
        public void forEachRemaining(Consumer<? super Long> action) {
            this.i.forEachRemaining(action);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class PrimitiveIteratorWrapper implements LongIterator {
        final PrimitiveIterator.OfLong i;

        public PrimitiveIteratorWrapper(PrimitiveIterator.OfLong i) {
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

        @Override // it.unimi.dsi.fastutil.longs.LongIterator, java.util.PrimitiveIterator.OfLong
        public long nextLong() {
            return this.i.nextLong();
        }

        @Override // java.util.PrimitiveIterator
        public void forEachRemaining(java.util.function.LongConsumer action) {
            this.i.forEachRemaining(action);
        }
    }

    public static LongIterator asLongIterator(Iterator i) {
        return i instanceof LongIterator ? (LongIterator) i : i instanceof PrimitiveIterator.OfLong ? new PrimitiveIteratorWrapper((PrimitiveIterator.OfLong) i) : new IteratorWrapper(i);
    }

    /* loaded from: classes4.dex */
    private static class ListIteratorWrapper implements LongListIterator {
        final ListIterator<Long> i;

        public ListIteratorWrapper(ListIterator<Long> i) {
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

        @Override // it.unimi.dsi.fastutil.longs.LongListIterator
        public void set(long k) {
            this.i.set(Long.valueOf(k));
        }

        @Override // it.unimi.dsi.fastutil.longs.LongListIterator
        public void add(long k) {
            this.i.add(Long.valueOf(k));
        }

        @Override // it.unimi.dsi.fastutil.longs.LongListIterator, java.util.Iterator, java.util.ListIterator
        public void remove() {
            this.i.remove();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongIterator, java.util.PrimitiveIterator.OfLong
        public long nextLong() {
            return this.i.next().longValue();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongBidirectionalIterator
        public long previousLong() {
            return this.i.previous().longValue();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongIterator
        public void forEachRemaining(LongConsumer action) {
            this.i.forEachRemaining(action);
        }

        @Override // java.util.PrimitiveIterator
        public void forEachRemaining(java.util.function.LongConsumer action) {
            Consumer<? super Long> longIterators$IteratorWrapper$$ExternalSyntheticLambda0;
            Objects.requireNonNull(action);
            ListIterator<Long> listIterator = this.i;
            if (action instanceof Consumer) {
                longIterators$IteratorWrapper$$ExternalSyntheticLambda0 = (Consumer) action;
            } else {
                action.getClass();
                longIterators$IteratorWrapper$$ExternalSyntheticLambda0 = new LongIterators$IteratorWrapper$$ExternalSyntheticLambda0(action);
            }
            listIterator.forEachRemaining(longIterators$IteratorWrapper$$ExternalSyntheticLambda0);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongIterator, java.util.PrimitiveIterator.OfLong, java.util.Iterator
        @Deprecated
        public void forEachRemaining(Consumer<? super Long> action) {
            this.i.forEachRemaining(action);
        }
    }

    public static LongListIterator asLongIterator(ListIterator i) {
        return i instanceof LongListIterator ? (LongListIterator) i : new ListIteratorWrapper(i);
    }

    public static boolean any(LongIterator iterator, java.util.function.LongPredicate predicate) {
        return indexOf(iterator, predicate) != -1;
    }

    public static boolean all(LongIterator iterator, java.util.function.LongPredicate predicate) {
        Objects.requireNonNull(predicate);
        while (iterator.hasNext()) {
            if (!predicate.test(iterator.nextLong())) {
                return false;
            }
        }
        return true;
    }

    public static int indexOf(LongIterator iterator, java.util.function.LongPredicate predicate) {
        Objects.requireNonNull(predicate);
        int i = 0;
        while (iterator.hasNext()) {
            if (predicate.test(iterator.nextLong())) {
                return i;
            }
            i++;
        }
        return -1;
    }

    /* loaded from: classes4.dex */
    public static abstract class AbstractIndexBasedIterator extends AbstractLongIterator {
        protected int lastReturned;
        protected final int minPos;
        protected int pos;

        protected abstract long get(int i);

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

        @Override // it.unimi.dsi.fastutil.longs.LongIterator, java.util.PrimitiveIterator.OfLong
        public long nextLong() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            int i = this.pos;
            this.pos = i + 1;
            this.lastReturned = i;
            return get(i);
        }

        @Override // java.util.Iterator, it.unimi.dsi.fastutil.longs.LongListIterator, java.util.ListIterator
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
        public void forEachRemaining(java.util.function.LongConsumer action) {
            while (this.pos < getMaxPos()) {
                int i = this.pos;
                this.pos = i + 1;
                this.lastReturned = i;
                action.accept(get(i));
            }
        }

        @Override // it.unimi.dsi.fastutil.longs.LongIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectIterator
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
    public static abstract class AbstractIndexBasedListIterator extends AbstractIndexBasedIterator implements LongListIterator {
        protected abstract void add(int i, long j);

        protected abstract void set(int i, long j);

        /* JADX INFO: Access modifiers changed from: protected */
        public AbstractIndexBasedListIterator(int minPos, int initialPos) {
            super(minPos, initialPos);
        }

        @Override // it.unimi.dsi.fastutil.BidirectionalIterator
        public boolean hasPrevious() {
            return this.pos > this.minPos;
        }

        public long previousLong() {
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

        public void add(long k) {
            int i = this.pos;
            this.pos = i + 1;
            add(i, k);
            this.lastReturned = -1;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongListIterator
        public void set(long k) {
            if (this.lastReturned == -1) {
                throw new IllegalStateException();
            }
            set(this.lastReturned, k);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator
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
    private static class IntervalIterator implements LongBidirectionalIterator {
        long curr;
        private final long from;
        private final long to;

        public IntervalIterator(long from, long to) {
            this.curr = from;
            this.from = from;
            this.to = to;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.curr < this.to;
        }

        @Override // it.unimi.dsi.fastutil.BidirectionalIterator
        public boolean hasPrevious() {
            return this.curr > this.from;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongIterator, java.util.PrimitiveIterator.OfLong
        public long nextLong() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            long j = this.curr;
            this.curr = 1 + j;
            return j;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongBidirectionalIterator
        public long previousLong() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            long j = this.curr - 1;
            this.curr = j;
            return j;
        }

        @Override // java.util.PrimitiveIterator
        public void forEachRemaining(java.util.function.LongConsumer action) {
            Objects.requireNonNull(action);
            while (this.curr < this.to) {
                action.accept(this.curr);
                this.curr++;
            }
        }

        @Override // it.unimi.dsi.fastutil.longs.LongBidirectionalIterator, it.unimi.dsi.fastutil.longs.LongIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectIterator
        public int skip(int n) {
            if (n < 0) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
            if (this.curr + n <= this.to) {
                this.curr += n;
                return n;
            }
            int n2 = (int) (this.to - this.curr);
            this.curr = this.to;
            return n2;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator
        public int back(int n) {
            if (this.curr - n >= this.from) {
                this.curr -= n;
                return n;
            }
            int n2 = (int) (this.curr - this.from);
            this.curr = this.from;
            return n2;
        }
    }

    public static LongBidirectionalIterator fromTo(long from, long to) {
        return new IntervalIterator(from, to);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class IteratorConcatenator implements LongIterator {
        final LongIterator[] a;
        int lastOffset = -1;
        int length;
        int offset;

        public IteratorConcatenator(LongIterator[] a, int offset, int length) {
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

        @Override // it.unimi.dsi.fastutil.longs.LongIterator, java.util.PrimitiveIterator.OfLong
        public long nextLong() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            LongIterator[] longIteratorArr = this.a;
            int i = this.offset;
            this.lastOffset = i;
            long next = longIteratorArr[i].nextLong();
            advance();
            return next;
        }

        @Override // java.util.PrimitiveIterator
        public void forEachRemaining(java.util.function.LongConsumer action) {
            while (this.length > 0) {
                LongIterator[] longIteratorArr = this.a;
                int i = this.offset;
                this.lastOffset = i;
                longIteratorArr[i].forEachRemaining(action);
                advance();
            }
        }

        @Override // it.unimi.dsi.fastutil.longs.LongIterator, java.util.PrimitiveIterator.OfLong, java.util.Iterator
        @Deprecated
        public void forEachRemaining(Consumer<? super Long> action) {
            while (this.length > 0) {
                LongIterator[] longIteratorArr = this.a;
                int i = this.offset;
                this.lastOffset = i;
                longIteratorArr[i].forEachRemaining(action);
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

        @Override // it.unimi.dsi.fastutil.longs.LongIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectIterator
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

    public static LongIterator concat(LongIterator... a) {
        return concat(a, 0, a.length);
    }

    public static LongIterator concat(LongIterator[] a, int offset, int length) {
        return new IteratorConcatenator(a, offset, length);
    }

    /* loaded from: classes4.dex */
    public static class UnmodifiableIterator implements LongIterator {
        protected final LongIterator i;

        public UnmodifiableIterator(LongIterator i) {
            this.i = i;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.i.hasNext();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongIterator, java.util.PrimitiveIterator.OfLong
        public long nextLong() {
            return this.i.nextLong();
        }

        @Override // java.util.PrimitiveIterator
        public void forEachRemaining(java.util.function.LongConsumer action) {
            this.i.forEachRemaining(action);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongIterator, java.util.PrimitiveIterator.OfLong, java.util.Iterator
        @Deprecated
        public void forEachRemaining(Consumer<? super Long> action) {
            this.i.forEachRemaining(action);
        }
    }

    public static LongIterator unmodifiable(LongIterator i) {
        return new UnmodifiableIterator(i);
    }

    /* loaded from: classes4.dex */
    public static class UnmodifiableBidirectionalIterator implements LongBidirectionalIterator {
        protected final LongBidirectionalIterator i;

        public UnmodifiableBidirectionalIterator(LongBidirectionalIterator i) {
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

        @Override // it.unimi.dsi.fastutil.longs.LongIterator, java.util.PrimitiveIterator.OfLong
        public long nextLong() {
            return this.i.nextLong();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongBidirectionalIterator
        public long previousLong() {
            return this.i.previousLong();
        }

        @Override // java.util.PrimitiveIterator
        public void forEachRemaining(java.util.function.LongConsumer action) {
            this.i.forEachRemaining(action);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongIterator, java.util.PrimitiveIterator.OfLong, java.util.Iterator
        @Deprecated
        public void forEachRemaining(Consumer<? super Long> action) {
            this.i.forEachRemaining(action);
        }
    }

    public static LongBidirectionalIterator unmodifiable(LongBidirectionalIterator i) {
        return new UnmodifiableBidirectionalIterator(i);
    }

    /* loaded from: classes4.dex */
    public static class UnmodifiableListIterator implements LongListIterator {
        protected final LongListIterator i;

        public UnmodifiableListIterator(LongListIterator i) {
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

        @Override // it.unimi.dsi.fastutil.longs.LongIterator, java.util.PrimitiveIterator.OfLong
        public long nextLong() {
            return this.i.nextLong();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongBidirectionalIterator
        public long previousLong() {
            return this.i.previousLong();
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
        public void forEachRemaining(java.util.function.LongConsumer action) {
            this.i.forEachRemaining(action);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongIterator, java.util.PrimitiveIterator.OfLong, java.util.Iterator
        @Deprecated
        public void forEachRemaining(Consumer<? super Long> action) {
            this.i.forEachRemaining(action);
        }
    }

    public static LongListIterator unmodifiable(LongListIterator i) {
        return new UnmodifiableListIterator(i);
    }

    /* loaded from: classes4.dex */
    private static final class ByteIteratorWrapper implements LongIterator {
        final ByteIterator iterator;

        public ByteIteratorWrapper(ByteIterator iterator) {
            this.iterator = iterator;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongIterator, java.util.PrimitiveIterator.OfLong, java.util.Iterator
        @Deprecated
        public Long next() {
            return Long.valueOf(this.iterator.nextByte());
        }

        @Override // it.unimi.dsi.fastutil.longs.LongIterator, java.util.PrimitiveIterator.OfLong
        public long nextLong() {
            return this.iterator.nextByte();
        }

        @Override // java.util.PrimitiveIterator
        public void forEachRemaining(java.util.function.LongConsumer action) {
            Objects.requireNonNull(action);
            ByteIterator byteIterator = this.iterator;
            action.getClass();
            byteIterator.forEachRemaining((ByteConsumer) new LongIterators$ByteIteratorWrapper$$ExternalSyntheticLambda0(action));
        }

        @Override // java.util.Iterator
        public void remove() {
            this.iterator.remove();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectIterator
        public int skip(int n) {
            return this.iterator.skip(n);
        }
    }

    public static LongIterator wrap(ByteIterator iterator) {
        return new ByteIteratorWrapper(iterator);
    }

    /* loaded from: classes4.dex */
    private static final class ShortIteratorWrapper implements LongIterator {
        final ShortIterator iterator;

        public ShortIteratorWrapper(ShortIterator iterator) {
            this.iterator = iterator;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongIterator, java.util.PrimitiveIterator.OfLong, java.util.Iterator
        @Deprecated
        public Long next() {
            return Long.valueOf(this.iterator.nextShort());
        }

        @Override // it.unimi.dsi.fastutil.longs.LongIterator, java.util.PrimitiveIterator.OfLong
        public long nextLong() {
            return this.iterator.nextShort();
        }

        @Override // java.util.PrimitiveIterator
        public void forEachRemaining(java.util.function.LongConsumer action) {
            Objects.requireNonNull(action);
            ShortIterator shortIterator = this.iterator;
            action.getClass();
            shortIterator.forEachRemaining((ShortConsumer) new LongIterators$ShortIteratorWrapper$$ExternalSyntheticLambda0(action));
        }

        @Override // java.util.Iterator
        public void remove() {
            this.iterator.remove();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectIterator
        public int skip(int n) {
            return this.iterator.skip(n);
        }
    }

    public static LongIterator wrap(ShortIterator iterator) {
        return new ShortIteratorWrapper(iterator);
    }

    /* loaded from: classes4.dex */
    private static final class CharIteratorWrapper implements LongIterator {
        final CharIterator iterator;

        public CharIteratorWrapper(CharIterator iterator) {
            this.iterator = iterator;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongIterator, java.util.PrimitiveIterator.OfLong, java.util.Iterator
        @Deprecated
        public Long next() {
            return Long.valueOf(this.iterator.nextChar());
        }

        @Override // it.unimi.dsi.fastutil.longs.LongIterator, java.util.PrimitiveIterator.OfLong
        public long nextLong() {
            return this.iterator.nextChar();
        }

        @Override // java.util.PrimitiveIterator
        public void forEachRemaining(java.util.function.LongConsumer action) {
            Objects.requireNonNull(action);
            CharIterator charIterator = this.iterator;
            action.getClass();
            charIterator.forEachRemaining((CharConsumer) new LongIterators$CharIteratorWrapper$$ExternalSyntheticLambda0(action));
        }

        @Override // java.util.Iterator
        public void remove() {
            this.iterator.remove();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectIterator
        public int skip(int n) {
            return this.iterator.skip(n);
        }
    }

    public static LongIterator wrap(CharIterator iterator) {
        return new CharIteratorWrapper(iterator);
    }

    /* loaded from: classes4.dex */
    private static final class IntIteratorWrapper implements LongIterator {
        final IntIterator iterator;

        public IntIteratorWrapper(IntIterator iterator) {
            this.iterator = iterator;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongIterator, java.util.PrimitiveIterator.OfLong, java.util.Iterator
        @Deprecated
        public Long next() {
            return Long.valueOf(this.iterator.nextInt());
        }

        @Override // it.unimi.dsi.fastutil.longs.LongIterator, java.util.PrimitiveIterator.OfLong
        public long nextLong() {
            return this.iterator.nextInt();
        }

        @Override // java.util.PrimitiveIterator
        public void forEachRemaining(java.util.function.LongConsumer action) {
            Objects.requireNonNull(action);
            IntIterator intIterator = this.iterator;
            action.getClass();
            intIterator.forEachRemaining((IntConsumer) new LongIterators$IntIteratorWrapper$$ExternalSyntheticLambda0(action));
        }

        @Override // java.util.Iterator
        public void remove() {
            this.iterator.remove();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectIterator
        public int skip(int n) {
            return this.iterator.skip(n);
        }
    }

    public static LongIterator wrap(IntIterator iterator) {
        return new IntIteratorWrapper(iterator);
    }
}
