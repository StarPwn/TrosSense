package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.bytes.ByteSpliterator;
import it.unimi.dsi.fastutil.chars.CharSpliterator;
import it.unimi.dsi.fastutil.ints.IntConsumer;
import it.unimi.dsi.fastutil.ints.IntSpliterator;
import it.unimi.dsi.fastutil.shorts.ShortSpliterator;
import java.io.Serializable;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;

/* loaded from: classes4.dex */
public final class LongSpliterators {
    static final int BASE_SPLITERATOR_CHARACTERISTICS = 256;
    public static final int COLLECTION_SPLITERATOR_CHARACTERISTICS = 320;
    public static final EmptySpliterator EMPTY_SPLITERATOR = new EmptySpliterator();
    public static final int LIST_SPLITERATOR_CHARACTERISTICS = 16720;
    public static final int SET_SPLITERATOR_CHARACTERISTICS = 321;
    private static final int SORTED_CHARACTERISTICS = 20;
    public static final int SORTED_SET_SPLITERATOR_CHARACTERISTICS = 341;

    private LongSpliterators() {
    }

    /* loaded from: classes4.dex */
    public static class EmptySpliterator implements LongSpliterator, Serializable, Cloneable {
        private static final int CHARACTERISTICS = 16448;
        private static final long serialVersionUID = 8379247926738230492L;

        protected EmptySpliterator() {
        }

        @Override // java.util.Spliterator.OfPrimitive
        public boolean tryAdvance(java.util.function.LongConsumer action) {
            return false;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterator, java.util.Spliterator.OfLong, java.util.Spliterator
        @Deprecated
        public boolean tryAdvance(Consumer<? super Long> action) {
            return false;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterator, java.util.Spliterator.OfLong, java.util.Spliterator.OfPrimitive, java.util.Spliterator
        public LongSpliterator trySplit() {
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

        @Override // java.util.Spliterator.OfPrimitive
        public void forEachRemaining(java.util.function.LongConsumer action) {
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterator, java.util.Spliterator.OfLong, java.util.Spliterator
        @Deprecated
        public void forEachRemaining(Consumer<? super Long> action) {
        }

        public Object clone() {
            return LongSpliterators.EMPTY_SPLITERATOR;
        }

        private Object readResolve() {
            return LongSpliterators.EMPTY_SPLITERATOR;
        }
    }

    /* loaded from: classes4.dex */
    private static class SingletonSpliterator implements LongSpliterator {
        private static final int CHARACTERISTICS = 17749;
        private final LongComparator comparator;
        private boolean consumed;
        private final long element;

        public SingletonSpliterator(long element) {
            this(element, null);
        }

        public SingletonSpliterator(long element, LongComparator comparator) {
            this.consumed = false;
            this.element = element;
            this.comparator = comparator;
        }

        @Override // java.util.Spliterator.OfPrimitive
        public boolean tryAdvance(java.util.function.LongConsumer action) {
            Objects.requireNonNull(action);
            if (this.consumed) {
                return false;
            }
            this.consumed = true;
            action.accept(this.element);
            return true;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterator, java.util.Spliterator.OfLong, java.util.Spliterator.OfPrimitive, java.util.Spliterator
        public LongSpliterator trySplit() {
            return null;
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return this.consumed ? 0L : 1L;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return CHARACTERISTICS;
        }

        @Override // java.util.Spliterator.OfPrimitive
        public void forEachRemaining(java.util.function.LongConsumer action) {
            Objects.requireNonNull(action);
            if (!this.consumed) {
                this.consumed = true;
                action.accept(this.element);
            }
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterator, java.util.Spliterator
        public LongComparator getComparator() {
            return this.comparator;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterator
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

    public static LongSpliterator singleton(long element) {
        return new SingletonSpliterator(element);
    }

    public static LongSpliterator singleton(long element, LongComparator comparator) {
        return new SingletonSpliterator(element, comparator);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class ArraySpliterator implements LongSpliterator {
        private static final int BASE_CHARACTERISTICS = 16720;
        final long[] array;
        final int characteristics;
        private int curr;
        private int length;
        private final int offset;

        public ArraySpliterator(long[] array, int offset, int length, int additionalCharacteristics) {
            this.array = array;
            this.offset = offset;
            this.length = length;
            this.characteristics = additionalCharacteristics | 16720;
        }

        @Override // java.util.Spliterator.OfPrimitive
        public boolean tryAdvance(java.util.function.LongConsumer action) {
            if (this.curr >= this.length) {
                return false;
            }
            Objects.requireNonNull(action);
            long[] jArr = this.array;
            int i = this.offset;
            int i2 = this.curr;
            this.curr = i2 + 1;
            action.accept(jArr[i + i2]);
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

        protected ArraySpliterator makeForSplit(int newOffset, int newLength) {
            return new ArraySpliterator(this.array, newOffset, newLength, this.characteristics);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterator, java.util.Spliterator.OfLong, java.util.Spliterator.OfPrimitive, java.util.Spliterator
        public LongSpliterator trySplit() {
            int retLength = (this.length - this.curr) >> 1;
            if (retLength <= 1) {
                return null;
            }
            int myNewCurr = this.curr + retLength;
            int retOffset = this.offset + this.curr;
            this.curr = myNewCurr;
            return makeForSplit(retOffset, retLength);
        }

        @Override // java.util.Spliterator.OfPrimitive
        public void forEachRemaining(java.util.function.LongConsumer action) {
            Objects.requireNonNull(action);
            while (this.curr < this.length) {
                action.accept(this.array[this.offset + this.curr]);
                this.curr++;
            }
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterator
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
    public static class ArraySpliteratorWithComparator extends ArraySpliterator {
        private final LongComparator comparator;

        public ArraySpliteratorWithComparator(long[] array, int offset, int length, int additionalCharacteristics, LongComparator comparator) {
            super(array, offset, length, additionalCharacteristics | 20);
            this.comparator = comparator;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // it.unimi.dsi.fastutil.longs.LongSpliterators.ArraySpliterator
        public ArraySpliteratorWithComparator makeForSplit(int newOffset, int newLength) {
            return new ArraySpliteratorWithComparator(this.array, newOffset, newLength, this.characteristics, this.comparator);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterator, java.util.Spliterator
        public LongComparator getComparator() {
            return this.comparator;
        }
    }

    public static LongSpliterator wrap(long[] array, int offset, int length) {
        LongArrays.ensureOffsetLength(array, offset, length);
        return new ArraySpliterator(array, offset, length, 0);
    }

    public static LongSpliterator wrap(long[] array) {
        return new ArraySpliterator(array, 0, array.length, 0);
    }

    public static LongSpliterator wrap(long[] array, int offset, int length, int additionalCharacteristics) {
        LongArrays.ensureOffsetLength(array, offset, length);
        return new ArraySpliterator(array, offset, length, additionalCharacteristics);
    }

    public static LongSpliterator wrapPreSorted(long[] array, int offset, int length, int additionalCharacteristics, LongComparator comparator) {
        LongArrays.ensureOffsetLength(array, offset, length);
        return new ArraySpliteratorWithComparator(array, offset, length, additionalCharacteristics, comparator);
    }

    public static LongSpliterator wrapPreSorted(long[] array, int offset, int length, LongComparator comparator) {
        return wrapPreSorted(array, offset, length, 0, comparator);
    }

    public static LongSpliterator wrapPreSorted(long[] array, LongComparator comparator) {
        return wrapPreSorted(array, 0, array.length, comparator);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class SpliteratorWrapper implements LongSpliterator {
        final Spliterator<Long> i;

        public SpliteratorWrapper(Spliterator<Long> i) {
            this.i = i;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterator
        public boolean tryAdvance(LongConsumer action) {
            return this.i.tryAdvance(action);
        }

        @Override // java.util.Spliterator.OfPrimitive
        public boolean tryAdvance(java.util.function.LongConsumer action) {
            Consumer<? super Long> longIterators$IteratorWrapper$$ExternalSyntheticLambda0;
            Objects.requireNonNull(action);
            Spliterator<Long> spliterator = this.i;
            if (action instanceof Consumer) {
                longIterators$IteratorWrapper$$ExternalSyntheticLambda0 = (Consumer) action;
            } else {
                action.getClass();
                longIterators$IteratorWrapper$$ExternalSyntheticLambda0 = new LongIterators$IteratorWrapper$$ExternalSyntheticLambda0(action);
            }
            return spliterator.tryAdvance(longIterators$IteratorWrapper$$ExternalSyntheticLambda0);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterator, java.util.Spliterator.OfLong, java.util.Spliterator
        @Deprecated
        public boolean tryAdvance(Consumer<? super Long> action) {
            return this.i.tryAdvance(action);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterator
        public void forEachRemaining(LongConsumer action) {
            this.i.forEachRemaining(action);
        }

        @Override // java.util.Spliterator.OfPrimitive
        public void forEachRemaining(java.util.function.LongConsumer action) {
            Consumer<? super Long> longIterators$IteratorWrapper$$ExternalSyntheticLambda0;
            Objects.requireNonNull(action);
            Spliterator<Long> spliterator = this.i;
            if (action instanceof Consumer) {
                longIterators$IteratorWrapper$$ExternalSyntheticLambda0 = (Consumer) action;
            } else {
                action.getClass();
                longIterators$IteratorWrapper$$ExternalSyntheticLambda0 = new LongIterators$IteratorWrapper$$ExternalSyntheticLambda0(action);
            }
            spliterator.forEachRemaining(longIterators$IteratorWrapper$$ExternalSyntheticLambda0);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterator, java.util.Spliterator.OfLong, java.util.Spliterator
        @Deprecated
        public void forEachRemaining(Consumer<? super Long> action) {
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

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterator, java.util.Spliterator
        public LongComparator getComparator() {
            return LongComparators.asLongComparator(this.i.getComparator());
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterator, java.util.Spliterator.OfLong, java.util.Spliterator.OfPrimitive, java.util.Spliterator
        public LongSpliterator trySplit() {
            Spliterator<Long> innerSplit = this.i.trySplit();
            if (innerSplit == null) {
                return null;
            }
            return new SpliteratorWrapper(innerSplit);
        }
    }

    /* loaded from: classes4.dex */
    private static class SpliteratorWrapperWithComparator extends SpliteratorWrapper {
        final LongComparator comparator;

        public SpliteratorWrapperWithComparator(Spliterator<Long> i, LongComparator comparator) {
            super(i);
            this.comparator = comparator;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterators.SpliteratorWrapper, it.unimi.dsi.fastutil.longs.LongSpliterator, java.util.Spliterator
        public LongComparator getComparator() {
            return this.comparator;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterators.SpliteratorWrapper, it.unimi.dsi.fastutil.longs.LongSpliterator, java.util.Spliterator.OfLong, java.util.Spliterator.OfPrimitive, java.util.Spliterator
        public LongSpliterator trySplit() {
            Spliterator<Long> innerSplit = this.i.trySplit();
            if (innerSplit == null) {
                return null;
            }
            return new SpliteratorWrapperWithComparator(innerSplit, this.comparator);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class PrimitiveSpliteratorWrapper implements LongSpliterator {
        final Spliterator.OfLong i;

        public PrimitiveSpliteratorWrapper(Spliterator.OfLong i) {
            this.i = i;
        }

        @Override // java.util.Spliterator.OfPrimitive
        public boolean tryAdvance(java.util.function.LongConsumer action) {
            return this.i.tryAdvance(action);
        }

        @Override // java.util.Spliterator.OfPrimitive
        public void forEachRemaining(java.util.function.LongConsumer action) {
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

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterator, java.util.Spliterator
        public LongComparator getComparator() {
            return LongComparators.asLongComparator(this.i.getComparator());
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterator, java.util.Spliterator.OfLong, java.util.Spliterator.OfPrimitive, java.util.Spliterator
        public LongSpliterator trySplit() {
            Spliterator.OfLong innerSplit = this.i.trySplit();
            if (innerSplit == null) {
                return null;
            }
            return new PrimitiveSpliteratorWrapper(innerSplit);
        }
    }

    /* loaded from: classes4.dex */
    private static class PrimitiveSpliteratorWrapperWithComparator extends PrimitiveSpliteratorWrapper {
        final LongComparator comparator;

        public PrimitiveSpliteratorWrapperWithComparator(Spliterator.OfLong i, LongComparator comparator) {
            super(i);
            this.comparator = comparator;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterators.PrimitiveSpliteratorWrapper, it.unimi.dsi.fastutil.longs.LongSpliterator, java.util.Spliterator
        public LongComparator getComparator() {
            return this.comparator;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterators.PrimitiveSpliteratorWrapper, it.unimi.dsi.fastutil.longs.LongSpliterator, java.util.Spliterator.OfLong, java.util.Spliterator.OfPrimitive, java.util.Spliterator
        public LongSpliterator trySplit() {
            Spliterator.OfLong innerSplit = this.i.trySplit();
            if (innerSplit == null) {
                return null;
            }
            return new PrimitiveSpliteratorWrapperWithComparator(innerSplit, this.comparator);
        }
    }

    public static LongSpliterator asLongSpliterator(Spliterator i) {
        return i instanceof LongSpliterator ? (LongSpliterator) i : i instanceof Spliterator.OfLong ? new PrimitiveSpliteratorWrapper((Spliterator.OfLong) i) : new SpliteratorWrapper(i);
    }

    public static LongSpliterator asLongSpliterator(Spliterator i, LongComparator comparatorOverride) {
        if (i instanceof LongSpliterator) {
            throw new IllegalArgumentException("Cannot override comparator on instance that is already a " + LongSpliterator.class.getSimpleName());
        }
        return i instanceof Spliterator.OfLong ? new PrimitiveSpliteratorWrapperWithComparator((Spliterator.OfLong) i, comparatorOverride) : new SpliteratorWrapperWithComparator(i, comparatorOverride);
    }

    public static void onEachMatching(LongSpliterator spliterator, final java.util.function.LongPredicate predicate, final java.util.function.LongConsumer action) {
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(action);
        spliterator.forEachRemaining(new LongConsumer() { // from class: it.unimi.dsi.fastutil.longs.LongSpliterators$$ExternalSyntheticLambda0
            @Override // java.util.function.LongConsumer
            public final void accept(long j) {
                LongSpliterators.lambda$onEachMatching$0(predicate, action, j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$onEachMatching$0(java.util.function.LongPredicate predicate, java.util.function.LongConsumer action, long value) {
        if (predicate.test(value)) {
            action.accept(value);
        }
    }

    /* loaded from: classes4.dex */
    public static abstract class AbstractIndexBasedSpliterator extends AbstractLongSpliterator {
        protected int pos;

        protected abstract long get(int i);

        protected abstract int getMaxPos();

        protected abstract LongSpliterator makeForSplit(int i, int i2);

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

        @Override // java.util.Spliterator
        public int characteristics() {
            return 16720;
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return getMaxPos() - this.pos;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Spliterator.OfPrimitive
        public boolean tryAdvance(java.util.function.LongConsumer action) {
            if (this.pos >= getMaxPos()) {
                return false;
            }
            int i = this.pos;
            this.pos = i + 1;
            action.accept(get(i));
            return true;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Spliterator.OfPrimitive
        public void forEachRemaining(java.util.function.LongConsumer action) {
            int max = getMaxPos();
            while (this.pos < max) {
                action.accept(get(this.pos));
                this.pos++;
            }
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterator
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

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterator, java.util.Spliterator.OfLong, java.util.Spliterator.OfPrimitive, java.util.Spliterator
        public LongSpliterator trySplit() {
            int max = getMaxPos();
            int splitPoint = computeSplitPoint();
            if (splitPoint == this.pos || splitPoint == max) {
                return null;
            }
            splitPointCheck(splitPoint, max);
            int oldPos = this.pos;
            LongSpliterator maybeSplit = makeForSplit(oldPos, splitPoint);
            if (maybeSplit != null) {
                this.pos = splitPoint;
            }
            return maybeSplit;
        }
    }

    /* loaded from: classes4.dex */
    public static abstract class EarlyBindingSizeIndexBasedSpliterator extends AbstractIndexBasedSpliterator {
        protected final int maxPos;

        /* JADX INFO: Access modifiers changed from: protected */
        public EarlyBindingSizeIndexBasedSpliterator(int initialPos, int maxPos) {
            super(initialPos);
            this.maxPos = maxPos;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterators.AbstractIndexBasedSpliterator
        protected final int getMaxPos() {
            return this.maxPos;
        }
    }

    /* loaded from: classes4.dex */
    public static abstract class LateBindingSizeIndexBasedSpliterator extends AbstractIndexBasedSpliterator {
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
        @Override // it.unimi.dsi.fastutil.longs.LongSpliterators.AbstractIndexBasedSpliterator
        public final int getMaxPos() {
            return this.maxPosFixed ? this.maxPos : getMaxPosFromBackingStore();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterators.AbstractIndexBasedSpliterator, it.unimi.dsi.fastutil.longs.LongSpliterator, java.util.Spliterator.OfLong, java.util.Spliterator.OfPrimitive, java.util.Spliterator
        public LongSpliterator trySplit() {
            LongSpliterator maybeSplit = super.trySplit();
            if (!this.maxPosFixed && maybeSplit != null) {
                this.maxPos = getMaxPosFromBackingStore();
                this.maxPosFixed = true;
            }
            return maybeSplit;
        }
    }

    /* loaded from: classes4.dex */
    private static class IntervalSpliterator implements LongSpliterator {
        private static final int CHARACTERISTICS = 17749;
        private static final int DONT_SPLIT_THRESHOLD = 2;
        private static final long MAX_SPLIT_SIZE = 1073741824;
        private long curr;
        private long to;

        public IntervalSpliterator(long from, long to) {
            this.curr = from;
            this.to = to;
        }

        @Override // java.util.Spliterator.OfPrimitive
        public boolean tryAdvance(java.util.function.LongConsumer action) {
            if (this.curr >= this.to) {
                return false;
            }
            long j = this.curr;
            this.curr = 1 + j;
            action.accept(j);
            return true;
        }

        @Override // java.util.Spliterator.OfPrimitive
        public void forEachRemaining(java.util.function.LongConsumer action) {
            Objects.requireNonNull(action);
            while (this.curr < this.to) {
                action.accept(this.curr);
                this.curr++;
            }
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return this.to - this.curr;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return CHARACTERISTICS;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterator, java.util.Spliterator
        public LongComparator getComparator() {
            return null;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterator, java.util.Spliterator.OfLong, java.util.Spliterator.OfPrimitive, java.util.Spliterator
        public LongSpliterator trySplit() {
            long remaining = this.to - this.curr;
            long mid = this.curr + (remaining >> 1);
            if (remaining > 2147483648L || remaining < 0) {
                mid = this.curr + MAX_SPLIT_SIZE;
            }
            if (remaining >= 0 && remaining <= 2) {
                return null;
            }
            long old_curr = this.curr;
            this.curr = mid;
            return new IntervalSpliterator(old_curr, mid);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterator
        public long skip(long n) {
            if (n < 0) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
            if (this.curr >= this.to) {
                return 0L;
            }
            long newCurr = this.curr + n;
            if (newCurr <= this.to && newCurr >= this.curr) {
                this.curr = newCurr;
                return n;
            }
            long n2 = this.to - this.curr;
            this.curr = this.to;
            return n2;
        }
    }

    public static LongSpliterator fromTo(long from, long to) {
        return new IntervalSpliterator(from, to);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class SpliteratorConcatenator implements LongSpliterator {
        private static final int CHARACTERISTICS_NOT_SUPPORTED_WHILE_MULTIPLE = 5;
        private static final int EMPTY_CHARACTERISTICS = 16448;
        final LongSpliterator[] a;
        int characteristics;
        int length;
        int offset;
        long remainingEstimatedExceptCurrent;

        public SpliteratorConcatenator(LongSpliterator[] a, int offset, int length) {
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

        @Override // java.util.Spliterator.OfPrimitive
        public boolean tryAdvance(java.util.function.LongConsumer action) {
            while (this.length > 0) {
                if (this.a[this.offset].tryAdvance(action)) {
                    return true;
                }
                advanceNextSpliterator();
            }
            return false;
        }

        @Override // java.util.Spliterator.OfPrimitive
        public void forEachRemaining(java.util.function.LongConsumer action) {
            while (this.length > 0) {
                this.a[this.offset].forEachRemaining(action);
                advanceNextSpliterator();
            }
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterator, java.util.Spliterator.OfLong, java.util.Spliterator
        @Deprecated
        public void forEachRemaining(Consumer<? super Long> action) {
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

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterator, java.util.Spliterator
        public LongComparator getComparator() {
            if (this.length == 1 && (this.characteristics & 4) != 0) {
                return this.a[this.offset].getComparator();
            }
            throw new IllegalStateException();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterator, java.util.Spliterator.OfLong, java.util.Spliterator.OfPrimitive, java.util.Spliterator
        public LongSpliterator trySplit() {
            switch (this.length) {
                case 0:
                    return null;
                case 1:
                    LongSpliterator split = this.a[this.offset].trySplit();
                    this.characteristics = this.a[this.offset].characteristics();
                    return split;
                case 2:
                    LongSpliterator[] longSpliteratorArr = this.a;
                    int i = this.offset;
                    this.offset = i + 1;
                    LongSpliterator split2 = longSpliteratorArr[i];
                    this.length--;
                    this.characteristics = this.a[this.offset].characteristics();
                    this.remainingEstimatedExceptCurrent = 0L;
                    return split2;
                default:
                    int mid = this.length >> 1;
                    int ret_offset = this.offset;
                    int new_offset = this.offset + mid;
                    int new_length = this.length - mid;
                    this.offset = new_offset;
                    this.length = new_length;
                    this.remainingEstimatedExceptCurrent = recomputeRemaining();
                    this.characteristics = computeCharacteristics();
                    return new SpliteratorConcatenator(this.a, ret_offset, mid);
            }
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterator
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

    public static LongSpliterator concat(LongSpliterator... a) {
        return concat(a, 0, a.length);
    }

    public static LongSpliterator concat(LongSpliterator[] a, int offset, int length) {
        return new SpliteratorConcatenator(a, offset, length);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class SpliteratorFromIterator implements LongSpliterator {
        private static final int BATCH_INCREMENT_SIZE = 1024;
        private static final int BATCH_MAX_SIZE = 33554432;
        final int characteristics;
        private LongSpliterator delegate;
        private final LongIterator iter;
        private final boolean knownSize;
        private int nextBatchSize;
        private long size;

        SpliteratorFromIterator(LongIterator iter, int characteristics) {
            this.size = Long.MAX_VALUE;
            this.nextBatchSize = 1024;
            this.delegate = null;
            this.iter = iter;
            this.characteristics = characteristics | 256;
            this.knownSize = false;
        }

        SpliteratorFromIterator(LongIterator iter, long size, int additionalCharacteristics) {
            this.size = Long.MAX_VALUE;
            this.nextBatchSize = 1024;
            this.delegate = null;
            this.iter = iter;
            this.knownSize = true;
            this.size = size;
            if ((additionalCharacteristics & 4096) != 0) {
                this.characteristics = additionalCharacteristics | 256;
            } else {
                this.characteristics = additionalCharacteristics | 16704;
            }
        }

        @Override // java.util.Spliterator.OfPrimitive
        public boolean tryAdvance(java.util.function.LongConsumer action) {
            if (this.delegate != null) {
                boolean hadRemaining = this.delegate.tryAdvance(action);
                if (!hadRemaining) {
                    this.delegate = null;
                }
                return hadRemaining;
            }
            if (!this.iter.hasNext()) {
                return false;
            }
            this.size--;
            action.accept(this.iter.nextLong());
            return true;
        }

        @Override // java.util.Spliterator.OfPrimitive
        public void forEachRemaining(java.util.function.LongConsumer action) {
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

        protected LongSpliterator makeForSplit(long[] batch, int len) {
            return LongSpliterators.wrap(batch, 0, len, this.characteristics);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterator, java.util.Spliterator.OfLong, java.util.Spliterator.OfPrimitive, java.util.Spliterator
        public LongSpliterator trySplit() {
            if (!this.iter.hasNext()) {
                return null;
            }
            int batchSizeEst = (!this.knownSize || this.size <= 0) ? this.nextBatchSize : (int) Math.min(this.nextBatchSize, this.size);
            long[] batch = new long[batchSizeEst];
            int actualSeen = 0;
            while (actualSeen < batchSizeEst && this.iter.hasNext()) {
                batch[actualSeen] = this.iter.nextLong();
                this.size--;
                actualSeen++;
            }
            int actualSeen2 = this.nextBatchSize;
            if (batchSizeEst < actualSeen2 && this.iter.hasNext()) {
                batch = Arrays.copyOf(batch, this.nextBatchSize);
                while (this.iter.hasNext() && actualSeen < this.nextBatchSize) {
                    batch[actualSeen] = this.iter.nextLong();
                    this.size--;
                    actualSeen++;
                }
            }
            this.nextBatchSize = Math.min(BATCH_MAX_SIZE, this.nextBatchSize + 1024);
            LongSpliterator split = makeForSplit(batch, actualSeen);
            if (!this.iter.hasNext()) {
                this.delegate = split;
                return split.trySplit();
            }
            return split;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterator
        public long skip(long n) {
            if (n < 0) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
            if (this.iter instanceof LongBigListIterator) {
                long skipped = ((LongBigListIterator) this.iter).skip(n);
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
    private static class SpliteratorFromIteratorWithComparator extends SpliteratorFromIterator {
        private final LongComparator comparator;

        SpliteratorFromIteratorWithComparator(LongIterator iter, int additionalCharacteristics, LongComparator comparator) {
            super(iter, additionalCharacteristics | 20);
            this.comparator = comparator;
        }

        SpliteratorFromIteratorWithComparator(LongIterator iter, long size, int additionalCharacteristics, LongComparator comparator) {
            super(iter, size, additionalCharacteristics | 20);
            this.comparator = comparator;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterator, java.util.Spliterator
        public LongComparator getComparator() {
            return this.comparator;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterators.SpliteratorFromIterator
        protected LongSpliterator makeForSplit(long[] array, int len) {
            return LongSpliterators.wrapPreSorted(array, 0, len, this.characteristics, this.comparator);
        }
    }

    public static LongSpliterator asSpliterator(LongIterator iter, long size, int additionalCharacterisitcs) {
        return new SpliteratorFromIterator(iter, size, additionalCharacterisitcs);
    }

    public static LongSpliterator asSpliteratorFromSorted(LongIterator iter, long size, int additionalCharacterisitcs, LongComparator comparator) {
        return new SpliteratorFromIteratorWithComparator(iter, size, additionalCharacterisitcs, comparator);
    }

    public static LongSpliterator asSpliteratorUnknownSize(LongIterator iter, int characterisitcs) {
        return new SpliteratorFromIterator(iter, characterisitcs);
    }

    public static LongSpliterator asSpliteratorFromSortedUnknownSize(LongIterator iter, int additionalCharacterisitcs, LongComparator comparator) {
        return new SpliteratorFromIteratorWithComparator(iter, additionalCharacterisitcs, comparator);
    }

    /* loaded from: classes4.dex */
    private static final class IteratorFromSpliterator implements LongIterator, LongConsumer {
        private final LongSpliterator spliterator;
        private long holder = 0;
        private boolean hasPeeked = false;

        IteratorFromSpliterator(LongSpliterator spliterator) {
            this.spliterator = spliterator;
        }

        @Override // java.util.function.LongConsumer
        public void accept(long item) {
            this.holder = item;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.hasPeeked) {
                return true;
            }
            boolean hadElement = this.spliterator.tryAdvance((LongConsumer) this);
            if (!hadElement) {
                return false;
            }
            this.hasPeeked = true;
            return true;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongIterator, java.util.PrimitiveIterator.OfLong
        public long nextLong() {
            if (this.hasPeeked) {
                this.hasPeeked = false;
                return this.holder;
            }
            boolean hadElement = this.spliterator.tryAdvance((LongConsumer) this);
            if (!hadElement) {
                throw new NoSuchElementException();
            }
            return this.holder;
        }

        @Override // java.util.PrimitiveIterator
        public void forEachRemaining(java.util.function.LongConsumer action) {
            if (this.hasPeeked) {
                this.hasPeeked = false;
                action.accept(this.holder);
            }
            this.spliterator.forEachRemaining(action);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongIterator, it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator, it.unimi.dsi.fastutil.objects.ObjectIterator
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

    public static LongIterator asIterator(LongSpliterator spliterator) {
        return new IteratorFromSpliterator(spliterator);
    }

    /* loaded from: classes4.dex */
    private static final class ByteSpliteratorWrapper implements LongSpliterator {
        final ByteSpliterator spliterator;

        public ByteSpliteratorWrapper(ByteSpliterator spliterator) {
            this.spliterator = spliterator;
        }

        @Override // java.util.Spliterator.OfPrimitive
        public boolean tryAdvance(java.util.function.LongConsumer action) {
            Objects.requireNonNull(action);
            ByteSpliterator byteSpliterator = this.spliterator;
            action.getClass();
            return byteSpliterator.tryAdvance((ByteSpliterator) new LongIterators$ByteIteratorWrapper$$ExternalSyntheticLambda0(action));
        }

        @Override // java.util.Spliterator.OfPrimitive
        public void forEachRemaining(java.util.function.LongConsumer action) {
            Objects.requireNonNull(action);
            ByteSpliterator byteSpliterator = this.spliterator;
            action.getClass();
            byteSpliterator.forEachRemaining((ByteSpliterator) new LongIterators$ByteIteratorWrapper$$ExternalSyntheticLambda0(action));
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return this.spliterator.estimateSize();
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return this.spliterator.characteristics();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterator
        public long skip(long n) {
            return this.spliterator.skip(n);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterator, java.util.Spliterator.OfLong, java.util.Spliterator.OfPrimitive, java.util.Spliterator
        public LongSpliterator trySplit() {
            ByteSpliterator possibleSplit = this.spliterator.trySplit();
            if (possibleSplit == null) {
                return null;
            }
            return new ByteSpliteratorWrapper(possibleSplit);
        }
    }

    public static LongSpliterator wrap(ByteSpliterator spliterator) {
        return new ByteSpliteratorWrapper(spliterator);
    }

    /* loaded from: classes4.dex */
    private static final class ShortSpliteratorWrapper implements LongSpliterator {
        final ShortSpliterator spliterator;

        public ShortSpliteratorWrapper(ShortSpliterator spliterator) {
            this.spliterator = spliterator;
        }

        @Override // java.util.Spliterator.OfPrimitive
        public boolean tryAdvance(java.util.function.LongConsumer action) {
            Objects.requireNonNull(action);
            ShortSpliterator shortSpliterator = this.spliterator;
            action.getClass();
            return shortSpliterator.tryAdvance((ShortSpliterator) new LongIterators$ShortIteratorWrapper$$ExternalSyntheticLambda0(action));
        }

        @Override // java.util.Spliterator.OfPrimitive
        public void forEachRemaining(java.util.function.LongConsumer action) {
            Objects.requireNonNull(action);
            ShortSpliterator shortSpliterator = this.spliterator;
            action.getClass();
            shortSpliterator.forEachRemaining((ShortSpliterator) new LongIterators$ShortIteratorWrapper$$ExternalSyntheticLambda0(action));
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return this.spliterator.estimateSize();
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return this.spliterator.characteristics();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterator
        public long skip(long n) {
            return this.spliterator.skip(n);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterator, java.util.Spliterator.OfLong, java.util.Spliterator.OfPrimitive, java.util.Spliterator
        public LongSpliterator trySplit() {
            ShortSpliterator possibleSplit = this.spliterator.trySplit();
            if (possibleSplit == null) {
                return null;
            }
            return new ShortSpliteratorWrapper(possibleSplit);
        }
    }

    public static LongSpliterator wrap(ShortSpliterator spliterator) {
        return new ShortSpliteratorWrapper(spliterator);
    }

    /* loaded from: classes4.dex */
    private static final class CharSpliteratorWrapper implements LongSpliterator {
        final CharSpliterator spliterator;

        public CharSpliteratorWrapper(CharSpliterator spliterator) {
            this.spliterator = spliterator;
        }

        @Override // java.util.Spliterator.OfPrimitive
        public boolean tryAdvance(java.util.function.LongConsumer action) {
            Objects.requireNonNull(action);
            CharSpliterator charSpliterator = this.spliterator;
            action.getClass();
            return charSpliterator.tryAdvance((CharSpliterator) new LongIterators$CharIteratorWrapper$$ExternalSyntheticLambda0(action));
        }

        @Override // java.util.Spliterator.OfPrimitive
        public void forEachRemaining(java.util.function.LongConsumer action) {
            Objects.requireNonNull(action);
            CharSpliterator charSpliterator = this.spliterator;
            action.getClass();
            charSpliterator.forEachRemaining((CharSpliterator) new LongIterators$CharIteratorWrapper$$ExternalSyntheticLambda0(action));
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return this.spliterator.estimateSize();
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return this.spliterator.characteristics();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterator
        public long skip(long n) {
            return this.spliterator.skip(n);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterator, java.util.Spliterator.OfLong, java.util.Spliterator.OfPrimitive, java.util.Spliterator
        public LongSpliterator trySplit() {
            CharSpliterator possibleSplit = this.spliterator.trySplit();
            if (possibleSplit == null) {
                return null;
            }
            return new CharSpliteratorWrapper(possibleSplit);
        }
    }

    public static LongSpliterator wrap(CharSpliterator spliterator) {
        return new CharSpliteratorWrapper(spliterator);
    }

    /* loaded from: classes4.dex */
    private static final class IntSpliteratorWrapper implements LongSpliterator {
        final IntSpliterator spliterator;

        public IntSpliteratorWrapper(IntSpliterator spliterator) {
            this.spliterator = spliterator;
        }

        @Override // java.util.Spliterator.OfPrimitive
        public boolean tryAdvance(java.util.function.LongConsumer action) {
            Objects.requireNonNull(action);
            IntSpliterator intSpliterator = this.spliterator;
            action.getClass();
            return intSpliterator.tryAdvance((IntConsumer) new LongIterators$IntIteratorWrapper$$ExternalSyntheticLambda0(action));
        }

        @Override // java.util.Spliterator.OfPrimitive
        public void forEachRemaining(java.util.function.LongConsumer action) {
            Objects.requireNonNull(action);
            IntSpliterator intSpliterator = this.spliterator;
            action.getClass();
            intSpliterator.forEachRemaining((IntConsumer) new LongIterators$IntIteratorWrapper$$ExternalSyntheticLambda0(action));
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return this.spliterator.estimateSize();
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return this.spliterator.characteristics();
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterator
        public long skip(long n) {
            return this.spliterator.skip(n);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterator, java.util.Spliterator.OfLong, java.util.Spliterator.OfPrimitive, java.util.Spliterator
        public LongSpliterator trySplit() {
            IntSpliterator possibleSplit = this.spliterator.trySplit();
            if (possibleSplit == null) {
                return null;
            }
            return new IntSpliteratorWrapper(possibleSplit);
        }
    }

    public static LongSpliterator wrap(IntSpliterator spliterator) {
        return new IntSpliteratorWrapper(spliterator);
    }
}
