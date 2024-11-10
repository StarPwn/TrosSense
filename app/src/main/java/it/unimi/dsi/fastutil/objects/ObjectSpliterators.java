package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.SafeMath;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;

/* loaded from: classes4.dex */
public final class ObjectSpliterators {
    static final int BASE_SPLITERATOR_CHARACTERISTICS = 0;
    public static final int COLLECTION_SPLITERATOR_CHARACTERISTICS = 64;
    public static final EmptySpliterator EMPTY_SPLITERATOR = new EmptySpliterator();
    public static final int LIST_SPLITERATOR_CHARACTERISTICS = 16464;
    public static final int SET_SPLITERATOR_CHARACTERISTICS = 65;
    private static final int SORTED_CHARACTERISTICS = 20;
    public static final int SORTED_SET_SPLITERATOR_CHARACTERISTICS = 85;

    private ObjectSpliterators() {
    }

    /* loaded from: classes4.dex */
    public static class EmptySpliterator<K> implements ObjectSpliterator<K>, Serializable, Cloneable {
        private static final int CHARACTERISTICS = 16448;
        private static final long serialVersionUID = 8379247926738230492L;

        protected EmptySpliterator() {
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super K> action) {
            return false;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterator, java.util.Spliterator
        public ObjectSpliterator<K> trySplit() {
            return null;
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return 0L;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return CHARACTERISTICS;
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super K> action) {
        }

        public Object clone() {
            return ObjectSpliterators.EMPTY_SPLITERATOR;
        }

        private Object readResolve() {
            return ObjectSpliterators.EMPTY_SPLITERATOR;
        }
    }

    public static <K> ObjectSpliterator<K> emptySpliterator() {
        return EMPTY_SPLITERATOR;
    }

    /* loaded from: classes4.dex */
    private static class SingletonSpliterator<K> implements ObjectSpliterator<K> {
        private static final int CHARACTERISTICS = 17493;
        private final Comparator<? super K> comparator;
        private boolean consumed;
        private final K element;

        public SingletonSpliterator(K element) {
            this(element, null);
        }

        public SingletonSpliterator(K element, Comparator<? super K> comparator) {
            this.consumed = false;
            this.element = element;
            this.comparator = comparator;
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super K> consumer) {
            Objects.requireNonNull(consumer);
            if (this.consumed) {
                return false;
            }
            this.consumed = true;
            consumer.accept(this.element);
            return true;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterator, java.util.Spliterator
        public ObjectSpliterator<K> trySplit() {
            return null;
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return this.consumed ? 0L : 1L;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return (this.element != null ? 256 : 0) | CHARACTERISTICS;
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super K> consumer) {
            Objects.requireNonNull(consumer);
            if (!this.consumed) {
                this.consumed = true;
                consumer.accept(this.element);
            }
        }

        @Override // java.util.Spliterator
        public Comparator<? super K> getComparator() {
            return this.comparator;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterator
        public long skip(long n) {
            if (n < 0) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
            if (n == 0 || this.consumed) {
                return 0L;
            }
            this.consumed = true;
            return 1L;
        }
    }

    public static <K> ObjectSpliterator<K> singleton(K element) {
        return new SingletonSpliterator(element);
    }

    public static <K> ObjectSpliterator<K> singleton(K element, Comparator<? super K> comparator) {
        return new SingletonSpliterator(element, comparator);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class ArraySpliterator<K> implements ObjectSpliterator<K> {
        private static final int BASE_CHARACTERISTICS = 16464;
        final K[] array;
        final int characteristics;
        private int curr;
        private int length;
        private final int offset;

        public ArraySpliterator(K[] array, int offset, int length, int additionalCharacteristics) {
            this.array = array;
            this.offset = offset;
            this.length = length;
            this.characteristics = additionalCharacteristics | 16464;
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super K> consumer) {
            if (this.curr >= this.length) {
                return false;
            }
            Objects.requireNonNull(consumer);
            K[] kArr = this.array;
            int i = this.offset;
            int i2 = this.curr;
            this.curr = i2 + 1;
            consumer.accept(kArr[i + i2]);
            return true;
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return this.length - this.curr;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return this.characteristics;
        }

        protected ArraySpliterator<K> makeForSplit(int newOffset, int newLength) {
            return new ArraySpliterator<>(this.array, newOffset, newLength, this.characteristics);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterator, java.util.Spliterator
        public ObjectSpliterator<K> trySplit() {
            int retLength = (this.length - this.curr) >> 1;
            if (retLength <= 1) {
                return null;
            }
            int myNewCurr = this.curr + retLength;
            int retOffset = this.offset + this.curr;
            this.curr = myNewCurr;
            return makeForSplit(retOffset, retLength);
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super K> consumer) {
            Objects.requireNonNull(consumer);
            while (this.curr < this.length) {
                consumer.accept(this.array[this.offset + this.curr]);
                this.curr++;
            }
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterator
        public long skip(long n) {
            if (n < 0) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
            if (this.curr >= this.length) {
                return 0L;
            }
            int remaining = this.length - this.curr;
            if (n < remaining) {
                this.curr = SafeMath.safeLongToInt(this.curr + n);
                return n;
            }
            long n2 = remaining;
            this.curr = this.length;
            return n2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class ArraySpliteratorWithComparator<K> extends ArraySpliterator<K> {
        private final Comparator<? super K> comparator;

        public ArraySpliteratorWithComparator(K[] array, int offset, int length, int additionalCharacteristics, Comparator<? super K> comparator) {
            super(array, offset, length, additionalCharacteristics | 20);
            this.comparator = comparator;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterators.ArraySpliterator
        public ArraySpliteratorWithComparator<K> makeForSplit(int newOffset, int newLength) {
            return new ArraySpliteratorWithComparator<>(this.array, newOffset, newLength, this.characteristics, this.comparator);
        }

        @Override // java.util.Spliterator
        public Comparator<? super K> getComparator() {
            return this.comparator;
        }
    }

    public static <K> ObjectSpliterator<K> wrap(K[] array, int offset, int length) {
        ObjectArrays.ensureOffsetLength(array, offset, length);
        return new ArraySpliterator(array, offset, length, 0);
    }

    public static <K> ObjectSpliterator<K> wrap(K[] array) {
        return new ArraySpliterator(array, 0, array.length, 0);
    }

    public static <K> ObjectSpliterator<K> wrap(K[] array, int offset, int length, int additionalCharacteristics) {
        ObjectArrays.ensureOffsetLength(array, offset, length);
        return new ArraySpliterator(array, offset, length, additionalCharacteristics);
    }

    public static <K> ObjectSpliterator<K> wrapPreSorted(K[] array, int offset, int length, int additionalCharacteristics, Comparator<? super K> comparator) {
        ObjectArrays.ensureOffsetLength(array, offset, length);
        return new ArraySpliteratorWithComparator(array, offset, length, additionalCharacteristics, comparator);
    }

    public static <K> ObjectSpliterator<K> wrapPreSorted(K[] array, int offset, int length, Comparator<? super K> comparator) {
        return wrapPreSorted(array, offset, length, 0, comparator);
    }

    public static <K> ObjectSpliterator<K> wrapPreSorted(K[] array, Comparator<? super K> comparator) {
        return wrapPreSorted(array, 0, array.length, comparator);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class SpliteratorWrapper<K> implements ObjectSpliterator<K> {
        final Spliterator<K> i;

        public SpliteratorWrapper(Spliterator<K> i) {
            this.i = i;
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super K> action) {
            return this.i.tryAdvance(action);
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super K> action) {
            this.i.forEachRemaining(action);
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return this.i.estimateSize();
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return this.i.characteristics();
        }

        @Override // java.util.Spliterator
        public Comparator<? super K> getComparator() {
            return ObjectComparators.asObjectComparator(this.i.getComparator());
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterator, java.util.Spliterator
        public ObjectSpliterator<K> trySplit() {
            Spliterator<K> innerSplit = this.i.trySplit();
            if (innerSplit == null) {
                return null;
            }
            return new SpliteratorWrapper(innerSplit);
        }
    }

    /* loaded from: classes4.dex */
    private static class SpliteratorWrapperWithComparator<K> extends SpliteratorWrapper<K> {
        final Comparator<? super K> comparator;

        public SpliteratorWrapperWithComparator(Spliterator<K> i, Comparator<? super K> comparator) {
            super(i);
            this.comparator = comparator;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterators.SpliteratorWrapper, java.util.Spliterator
        public Comparator<? super K> getComparator() {
            return this.comparator;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterators.SpliteratorWrapper, it.unimi.dsi.fastutil.objects.ObjectSpliterator, java.util.Spliterator
        public ObjectSpliterator<K> trySplit() {
            Spliterator<K> innerSplit = this.i.trySplit();
            if (innerSplit == null) {
                return null;
            }
            return new SpliteratorWrapperWithComparator(innerSplit, this.comparator);
        }
    }

    public static <K> ObjectSpliterator<K> asObjectSpliterator(Spliterator<K> i) {
        return i instanceof ObjectSpliterator ? (ObjectSpliterator) i : new SpliteratorWrapper(i);
    }

    public static <K> ObjectSpliterator<K> asObjectSpliterator(Spliterator<K> i, Comparator<? super K> comparatorOverride) {
        if (i instanceof ObjectSpliterator) {
            throw new IllegalArgumentException("Cannot override comparator on instance that is already a " + ObjectSpliterator.class.getSimpleName());
        }
        return new SpliteratorWrapperWithComparator(i, comparatorOverride);
    }

    public static <K> void onEachMatching(Spliterator<K> spliterator, final Predicate<? super K> predicate, final Consumer<? super K> action) {
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(action);
        spliterator.forEachRemaining(new Consumer() { // from class: it.unimi.dsi.fastutil.objects.ObjectSpliterators$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ObjectSpliterators.lambda$onEachMatching$0(predicate, action, obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$onEachMatching$0(Predicate predicate, Consumer action, Object value) {
        if (predicate.test(value)) {
            action.accept(value);
        }
    }

    /* loaded from: classes4.dex */
    public static abstract class AbstractIndexBasedSpliterator<K> extends AbstractObjectSpliterator<K> {
        protected int pos;

        protected abstract K get(int i);

        protected abstract int getMaxPos();

        protected abstract ObjectSpliterator<K> makeForSplit(int i, int i2);

        protected AbstractIndexBasedSpliterator(int initialPos) {
            this.pos = initialPos;
        }

        protected int computeSplitPoint() {
            return this.pos + ((getMaxPos() - this.pos) / 2);
        }

        private void splitPointCheck(int splitPoint, int observedMax) {
            if (splitPoint < this.pos || splitPoint > observedMax) {
                throw new IndexOutOfBoundsException("splitPoint " + splitPoint + " outside of range of current position " + this.pos + " and range end " + observedMax);
            }
        }

        public int characteristics() {
            return ObjectSpliterators.LIST_SPLITERATOR_CHARACTERISTICS;
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return getMaxPos() - this.pos;
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super K> action) {
            if (this.pos >= getMaxPos()) {
                return false;
            }
            int i = this.pos;
            this.pos = i + 1;
            action.accept(get(i));
            return true;
        }

        public void forEachRemaining(Consumer<? super K> action) {
            int max = getMaxPos();
            while (this.pos < max) {
                action.accept(get(this.pos));
                this.pos++;
            }
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterator
        public long skip(long n) {
            if (n < 0) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
            int max = getMaxPos();
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
            int max = getMaxPos();
            int splitPoint = computeSplitPoint();
            if (splitPoint == this.pos || splitPoint == max) {
                return null;
            }
            splitPointCheck(splitPoint, max);
            int oldPos = this.pos;
            ObjectSpliterator<K> maybeSplit = makeForSplit(oldPos, splitPoint);
            if (maybeSplit != null) {
                this.pos = splitPoint;
            }
            return maybeSplit;
        }
    }

    /* loaded from: classes4.dex */
    public static abstract class EarlyBindingSizeIndexBasedSpliterator<K> extends AbstractIndexBasedSpliterator<K> {
        protected final int maxPos;

        /* JADX INFO: Access modifiers changed from: protected */
        public EarlyBindingSizeIndexBasedSpliterator(int initialPos, int maxPos) {
            super(initialPos);
            this.maxPos = maxPos;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterators.AbstractIndexBasedSpliterator
        protected final int getMaxPos() {
            return this.maxPos;
        }
    }

    /* loaded from: classes4.dex */
    public static abstract class LateBindingSizeIndexBasedSpliterator<K> extends AbstractIndexBasedSpliterator<K> {
        protected int maxPos;
        private boolean maxPosFixed;

        protected abstract int getMaxPosFromBackingStore();

        /* JADX INFO: Access modifiers changed from: protected */
        public LateBindingSizeIndexBasedSpliterator(int initialPos) {
            super(initialPos);
            this.maxPos = -1;
            this.maxPosFixed = false;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public LateBindingSizeIndexBasedSpliterator(int initialPos, int fixedMaxPos) {
            super(initialPos);
            this.maxPos = -1;
            this.maxPos = fixedMaxPos;
            this.maxPosFixed = true;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterators.AbstractIndexBasedSpliterator
        public final int getMaxPos() {
            return this.maxPosFixed ? this.maxPos : getMaxPosFromBackingStore();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterators.AbstractIndexBasedSpliterator, it.unimi.dsi.fastutil.objects.ObjectSpliterator, java.util.Spliterator
        public ObjectSpliterator<K> trySplit() {
            ObjectSpliterator<K> maybeSplit = super.trySplit();
            if (!this.maxPosFixed && maybeSplit != null) {
                this.maxPos = getMaxPosFromBackingStore();
                this.maxPosFixed = true;
            }
            return maybeSplit;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class SpliteratorConcatenator<K> implements ObjectSpliterator<K> {
        private static final int CHARACTERISTICS_NOT_SUPPORTED_WHILE_MULTIPLE = 5;
        private static final int EMPTY_CHARACTERISTICS = 16448;
        final ObjectSpliterator<? extends K>[] a;
        int characteristics;
        int length;
        int offset;
        long remainingEstimatedExceptCurrent;

        public SpliteratorConcatenator(ObjectSpliterator<? extends K>[] a, int offset, int length) {
            this.remainingEstimatedExceptCurrent = Long.MAX_VALUE;
            this.characteristics = 0;
            this.a = a;
            this.offset = offset;
            this.length = length;
            this.remainingEstimatedExceptCurrent = recomputeRemaining();
            this.characteristics = computeCharacteristics();
        }

        private long recomputeRemaining() {
            int curLength = this.length - 1;
            int curOffset = this.offset + 1;
            long result = 0;
            while (curLength > 0) {
                int curOffset2 = curOffset + 1;
                long cur = this.a[curOffset].estimateSize();
                curLength--;
                if (cur == Long.MAX_VALUE) {
                    return Long.MAX_VALUE;
                }
                result += cur;
                if (result == Long.MAX_VALUE || result < 0) {
                    return Long.MAX_VALUE;
                }
                curOffset = curOffset2;
            }
            return result;
        }

        private int computeCharacteristics() {
            if (this.length <= 0) {
                return EMPTY_CHARACTERISTICS;
            }
            int current = -1;
            int curLength = this.length;
            int curOffset = this.offset;
            if (curLength > 1) {
                current = (-1) & (-6);
            }
            while (curLength > 0) {
                current &= this.a[curOffset].characteristics();
                curLength--;
                curOffset++;
            }
            return current;
        }

        private void advanceNextSpliterator() {
            if (this.length <= 0) {
                throw new AssertionError("advanceNextSpliterator() called with none remaining");
            }
            this.offset++;
            this.length--;
            this.remainingEstimatedExceptCurrent = recomputeRemaining();
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super K> action) {
            while (this.length > 0) {
                if (this.a[this.offset].tryAdvance(action)) {
                    return true;
                }
                advanceNextSpliterator();
            }
            return false;
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super K> action) {
            while (this.length > 0) {
                this.a[this.offset].forEachRemaining(action);
                advanceNextSpliterator();
            }
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            if (this.length <= 0) {
                return 0L;
            }
            long est = this.a[this.offset].estimateSize() + this.remainingEstimatedExceptCurrent;
            if (est < 0) {
                return Long.MAX_VALUE;
            }
            return est;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return this.characteristics;
        }

        @Override // java.util.Spliterator
        public Comparator<? super K> getComparator() {
            if (this.length == 1 && (this.characteristics & 4) != 0) {
                return this.a[this.offset].getComparator();
            }
            throw new IllegalStateException();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterator, java.util.Spliterator
        public ObjectSpliterator<K> trySplit() {
            switch (this.length) {
                case 0:
                    return null;
                case 1:
                    ObjectSpliterator<? extends K> trySplit = this.a[this.offset].trySplit();
                    this.characteristics = this.a[this.offset].characteristics();
                    return trySplit;
                case 2:
                    ObjectSpliterator<? extends K>[] objectSpliteratorArr = this.a;
                    int i = this.offset;
                    this.offset = i + 1;
                    ObjectSpliterator<K> objectSpliterator = objectSpliteratorArr[i];
                    this.length--;
                    this.characteristics = this.a[this.offset].characteristics();
                    this.remainingEstimatedExceptCurrent = 0L;
                    return objectSpliterator;
                default:
                    int i2 = this.length >> 1;
                    int i3 = this.offset;
                    int i4 = this.offset + i2;
                    int i5 = this.length - i2;
                    this.offset = i4;
                    this.length = i5;
                    this.remainingEstimatedExceptCurrent = recomputeRemaining();
                    this.characteristics = computeCharacteristics();
                    return new SpliteratorConcatenator(this.a, i3, i2);
            }
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterator
        public long skip(long n) {
            long skipped = 0;
            if (this.length <= 0) {
                return 0L;
            }
            while (skipped < n && this.length >= 0) {
                long curSkipped = this.a[this.offset].skip(n - skipped);
                skipped += curSkipped;
                if (skipped < n) {
                    advanceNextSpliterator();
                }
            }
            return skipped;
        }
    }

    @SafeVarargs
    public static <K> ObjectSpliterator<K> concat(ObjectSpliterator<? extends K>... a) {
        return concat(a, 0, a.length);
    }

    public static <K> ObjectSpliterator<K> concat(ObjectSpliterator<? extends K>[] a, int offset, int length) {
        return new SpliteratorConcatenator(a, offset, length);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class SpliteratorFromIterator<K> implements ObjectSpliterator<K> {
        private static final int BATCH_INCREMENT_SIZE = 1024;
        private static final int BATCH_MAX_SIZE = 33554432;
        final int characteristics;
        private ObjectSpliterator<K> delegate;
        private final ObjectIterator<? extends K> iter;
        private final boolean knownSize;
        private int nextBatchSize;
        private long size;

        SpliteratorFromIterator(ObjectIterator<? extends K> iter, int characteristics) {
            this.size = Long.MAX_VALUE;
            this.nextBatchSize = 1024;
            this.delegate = null;
            this.iter = iter;
            this.characteristics = characteristics | 0;
            this.knownSize = false;
        }

        SpliteratorFromIterator(ObjectIterator<? extends K> iter, long size, int additionalCharacteristics) {
            this.size = Long.MAX_VALUE;
            this.nextBatchSize = 1024;
            this.delegate = null;
            this.iter = iter;
            this.knownSize = true;
            this.size = size;
            if ((additionalCharacteristics & 4096) != 0) {
                this.characteristics = additionalCharacteristics | 0;
            } else {
                this.characteristics = additionalCharacteristics | 16448;
            }
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super K> consumer) {
            if (this.delegate != null) {
                boolean tryAdvance = this.delegate.tryAdvance(consumer);
                if (!tryAdvance) {
                    this.delegate = null;
                }
                return tryAdvance;
            }
            if (!this.iter.hasNext()) {
                return false;
            }
            this.size--;
            consumer.accept(this.iter.next());
            return true;
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super K> action) {
            if (this.delegate != null) {
                this.delegate.forEachRemaining(action);
                this.delegate = null;
            }
            this.iter.forEachRemaining(action);
            this.size = 0L;
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            if (this.delegate != null) {
                return this.delegate.estimateSize();
            }
            if (!this.iter.hasNext()) {
                return 0L;
            }
            if (!this.knownSize || this.size < 0) {
                return Long.MAX_VALUE;
            }
            return this.size;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return this.characteristics;
        }

        protected ObjectSpliterator<K> makeForSplit(K[] batch, int len) {
            return ObjectSpliterators.wrap(batch, 0, len, this.characteristics);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterator, java.util.Spliterator
        public ObjectSpliterator<K> trySplit() {
            if (!this.iter.hasNext()) {
                return null;
            }
            int batchSizeEst = (!this.knownSize || this.size <= 0) ? this.nextBatchSize : (int) Math.min(this.nextBatchSize, this.size);
            Object[] objArr = new Object[batchSizeEst];
            int actualSeen = 0;
            while (actualSeen < batchSizeEst && this.iter.hasNext()) {
                objArr[actualSeen] = this.iter.next();
                this.size--;
                actualSeen++;
            }
            int actualSeen2 = this.nextBatchSize;
            if (batchSizeEst < actualSeen2 && this.iter.hasNext()) {
                objArr = Arrays.copyOf(objArr, this.nextBatchSize);
                while (this.iter.hasNext() && actualSeen < this.nextBatchSize) {
                    objArr[actualSeen] = this.iter.next();
                    this.size--;
                    actualSeen++;
                }
            }
            this.nextBatchSize = Math.min(BATCH_MAX_SIZE, this.nextBatchSize + 1024);
            ObjectSpliterator<K> split = makeForSplit(objArr, actualSeen);
            if (!this.iter.hasNext()) {
                this.delegate = split;
                return split.trySplit();
            }
            return split;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterator
        public long skip(long n) {
            if (n < 0) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
            if (this.iter instanceof ObjectBigListIterator) {
                long skipped = ((ObjectBigListIterator) this.iter).skip(n);
                this.size -= skipped;
                return skipped;
            }
            long skippedSoFar = 0;
            while (skippedSoFar < n && this.iter.hasNext()) {
                int skipped2 = this.iter.skip(SafeMath.safeLongToInt(Math.min(n, 2147483647L)));
                this.size -= skipped2;
                skippedSoFar += skipped2;
            }
            return skippedSoFar;
        }
    }

    /* loaded from: classes4.dex */
    private static class SpliteratorFromIteratorWithComparator<K> extends SpliteratorFromIterator<K> {
        private final Comparator<? super K> comparator;

        SpliteratorFromIteratorWithComparator(ObjectIterator<? extends K> iter, int additionalCharacteristics, Comparator<? super K> comparator) {
            super(iter, additionalCharacteristics | 20);
            this.comparator = comparator;
        }

        SpliteratorFromIteratorWithComparator(ObjectIterator<? extends K> iter, long size, int additionalCharacteristics, Comparator<? super K> comparator) {
            super(iter, size, additionalCharacteristics | 20);
            this.comparator = comparator;
        }

        @Override // java.util.Spliterator
        public Comparator<? super K> getComparator() {
            return this.comparator;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterators.SpliteratorFromIterator
        protected ObjectSpliterator<K> makeForSplit(K[] array, int len) {
            return ObjectSpliterators.wrapPreSorted(array, 0, len, this.characteristics, this.comparator);
        }
    }

    public static <K> ObjectSpliterator<K> asSpliterator(ObjectIterator<? extends K> iter, long size, int additionalCharacterisitcs) {
        return new SpliteratorFromIterator(iter, size, additionalCharacterisitcs);
    }

    public static <K> ObjectSpliterator<K> asSpliteratorFromSorted(ObjectIterator<? extends K> iter, long size, int additionalCharacterisitcs, Comparator<? super K> comparator) {
        return new SpliteratorFromIteratorWithComparator(iter, size, additionalCharacterisitcs, comparator);
    }

    public static <K> ObjectSpliterator<K> asSpliteratorUnknownSize(ObjectIterator<? extends K> iter, int characterisitcs) {
        return new SpliteratorFromIterator(iter, characterisitcs);
    }

    public static <K> ObjectSpliterator<K> asSpliteratorFromSortedUnknownSize(ObjectIterator<? extends K> iter, int additionalCharacterisitcs, Comparator<? super K> comparator) {
        return new SpliteratorFromIteratorWithComparator(iter, additionalCharacterisitcs, comparator);
    }

    /* loaded from: classes4.dex */
    private static final class IteratorFromSpliterator<K> implements ObjectIterator<K>, Consumer<K> {
        private final ObjectSpliterator<? extends K> spliterator;
        private K holder = null;
        private boolean hasPeeked = false;

        IteratorFromSpliterator(ObjectSpliterator<? extends K> spliterator) {
            this.spliterator = spliterator;
        }

        @Override // java.util.function.Consumer
        public void accept(K item) {
            this.holder = item;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.hasPeeked) {
                return true;
            }
            boolean hadElement = this.spliterator.tryAdvance(this);
            if (!hadElement) {
                return false;
            }
            this.hasPeeked = true;
            return true;
        }

        @Override // java.util.Iterator
        public K next() {
            if (this.hasPeeked) {
                this.hasPeeked = false;
                return this.holder;
            }
            boolean hadElement = this.spliterator.tryAdvance(this);
            if (!hadElement) {
                throw new NoSuchElementException();
            }
            return this.holder;
        }

        @Override // java.util.Iterator
        public void forEachRemaining(Consumer<? super K> consumer) {
            if (this.hasPeeked) {
                this.hasPeeked = false;
                consumer.accept(this.holder);
            }
            this.spliterator.forEachRemaining(consumer);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectIterator
        public int skip(int n) {
            if (n < 0) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
            int skipped = 0;
            if (this.hasPeeked) {
                this.hasPeeked = false;
                this.spliterator.skip(1L);
                skipped = 0 + 1;
                n--;
            }
            if (n > 0) {
                return skipped + SafeMath.safeLongToInt(this.spliterator.skip(n));
            }
            return skipped;
        }
    }

    public static <K> ObjectIterator<K> asIterator(ObjectSpliterator<? extends K> spliterator) {
        return new IteratorFromSpliterator(spliterator);
    }
}
