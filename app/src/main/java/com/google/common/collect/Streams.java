package com.google.common.collect;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import com.google.common.math.LongMath;
import it.unimi.dsi.fastutil.objects.ObjectSpliterators;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public final class Streams {

    /* loaded from: classes.dex */
    public interface DoubleFunctionWithIndex<R> {
        R apply(double d, long j);
    }

    /* loaded from: classes.dex */
    public interface FunctionWithIndex<T, R> {
        R apply(T t, long j);
    }

    /* loaded from: classes.dex */
    public interface IntFunctionWithIndex<R> {
        R apply(int i, long j);
    }

    /* loaded from: classes.dex */
    public interface LongFunctionWithIndex<R> {
        R apply(long j, long j2);
    }

    public static <T> Stream<T> stream(Iterable<T> iterable) {
        if (iterable instanceof Collection) {
            return ((Collection) iterable).stream();
        }
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    @Deprecated
    public static <T> Stream<T> stream(Collection<T> collection) {
        return collection.stream();
    }

    public static <T> Stream<T> stream(Iterator<T> iterator) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 0), false);
    }

    public static <T> Stream<T> stream(Optional<T> optional) {
        return optional.isPresent() ? Stream.of(optional.get()) : Stream.of(new Object[0]);
    }

    public static <T> Stream<T> stream(java.util.Optional<T> optional) {
        return optional.isPresent() ? Stream.of(optional.get()) : Stream.of(new Object[0]);
    }

    @SafeVarargs
    public static <T> Stream<T> concat(Stream<? extends T>... streams) {
        boolean isParallel = false;
        int characteristics = 336;
        long estimatedSize = 0;
        ImmutableList.Builder<Spliterator<? extends T>> splitrsBuilder = new ImmutableList.Builder<>(streams.length);
        for (Stream<? extends T> stream : streams) {
            isParallel |= stream.isParallel();
            Spliterator<? extends T> splitr = stream.spliterator();
            splitrsBuilder.add((ImmutableList.Builder<Spliterator<? extends T>>) splitr);
            characteristics &= splitr.characteristics();
            estimatedSize = LongMath.saturatedAdd(estimatedSize, splitr.estimateSize());
        }
        return StreamSupport.stream(CollectSpliterators.flatMap(splitrsBuilder.build().spliterator(), new Function() { // from class: com.google.common.collect.Streams$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return Streams.lambda$concat$0((Spliterator) obj);
            }
        }, characteristics, estimatedSize), isParallel);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ Spliterator lambda$concat$0(Spliterator splitr) {
        return splitr;
    }

    public static IntStream concat(IntStream... streams) {
        return Stream.of((Object[]) streams).flatMapToInt(new Function() { // from class: com.google.common.collect.Streams$$ExternalSyntheticLambda4
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return Streams.lambda$concat$1((IntStream) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ IntStream lambda$concat$1(IntStream stream) {
        return stream;
    }

    public static LongStream concat(LongStream... streams) {
        return Stream.of((Object[]) streams).flatMapToLong(new Function() { // from class: com.google.common.collect.Streams$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return Streams.lambda$concat$2((LongStream) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ LongStream lambda$concat$2(LongStream stream) {
        return stream;
    }

    public static DoubleStream concat(DoubleStream... streams) {
        return Stream.of((Object[]) streams).flatMapToDouble(new Function() { // from class: com.google.common.collect.Streams$$ExternalSyntheticLambda3
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return Streams.lambda$concat$3((DoubleStream) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ DoubleStream lambda$concat$3(DoubleStream stream) {
        return stream;
    }

    public static IntStream stream(OptionalInt optional) {
        return optional.isPresent() ? IntStream.of(optional.getAsInt()) : IntStream.empty();
    }

    public static LongStream stream(OptionalLong optional) {
        return optional.isPresent() ? LongStream.of(optional.getAsLong()) : LongStream.empty();
    }

    public static DoubleStream stream(OptionalDouble optional) {
        return optional.isPresent() ? DoubleStream.of(optional.getAsDouble()) : DoubleStream.empty();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.google.common.collect.Streams$1OptionalState, reason: invalid class name */
    /* loaded from: classes.dex */
    public class C1OptionalState<T> {
        boolean set = false;
        T value = null;

        C1OptionalState() {
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void set(@Nullable T value) {
            this.set = true;
            this.value = value;
        }

        T get() {
            Preconditions.checkState(this.set);
            return this.value;
        }
    }

    public static <T> java.util.Optional<T> findLast(Stream<T> stream) {
        final C1OptionalState<T> state = new C1OptionalState<>();
        Deque<Spliterator<T>> splits = new ArrayDeque<>();
        splits.addLast(stream.spliterator());
        while (!splits.isEmpty()) {
            Spliterator<T> spliterator = splits.removeLast();
            if (spliterator.getExactSizeIfKnown() != 0) {
                if (spliterator.hasCharacteristics(16384)) {
                    while (true) {
                        Spliterator<T> prefix = spliterator.trySplit();
                        if (prefix == null || prefix.getExactSizeIfKnown() == 0) {
                            break;
                        }
                        if (spliterator.getExactSizeIfKnown() == 0) {
                            spliterator = prefix;
                            break;
                        }
                    }
                    state.getClass();
                    spliterator.forEachRemaining(new Consumer() { // from class: com.google.common.collect.Streams$$ExternalSyntheticLambda0
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            Streams.C1OptionalState.this.set(obj);
                        }
                    });
                    return java.util.Optional.of(state.get());
                }
                Spliterator<T> prefix2 = spliterator.trySplit();
                if (prefix2 == null || prefix2.getExactSizeIfKnown() == 0) {
                    state.getClass();
                    spliterator.forEachRemaining(new Consumer() { // from class: com.google.common.collect.Streams$$ExternalSyntheticLambda0
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            Streams.C1OptionalState.this.set(obj);
                        }
                    });
                    if (state.set) {
                        return java.util.Optional.of(state.get());
                    }
                } else {
                    splits.addLast(prefix2);
                    splits.addLast(spliterator);
                }
            }
        }
        return java.util.Optional.empty();
    }

    public static OptionalInt findLast(IntStream stream) {
        java.util.Optional<Integer> boxedLast = findLast(stream.boxed());
        return boxedLast.isPresent() ? OptionalInt.of(boxedLast.get().intValue()) : OptionalInt.empty();
    }

    public static OptionalLong findLast(LongStream stream) {
        java.util.Optional<Long> boxedLast = findLast(stream.boxed());
        return boxedLast.isPresent() ? OptionalLong.of(boxedLast.get().longValue()) : OptionalLong.empty();
    }

    public static OptionalDouble findLast(DoubleStream stream) {
        java.util.Optional<Double> boxedLast = findLast(stream.boxed());
        return boxedLast.isPresent() ? OptionalDouble.of(boxedLast.get().doubleValue()) : OptionalDouble.empty();
    }

    public static <A, B, R> Stream<R> zip(Stream<A> streamA, Stream<B> streamB, final BiFunction<? super A, ? super B, R> function) {
        Preconditions.checkNotNull(streamA);
        Preconditions.checkNotNull(streamB);
        Preconditions.checkNotNull(function);
        boolean isParallel = streamA.isParallel() || streamB.isParallel();
        Spliterator<A> splitrA = streamA.spliterator();
        Spliterator<B> splitrB = streamB.spliterator();
        int characteristics = splitrA.characteristics() & splitrB.characteristics() & 80;
        final Iterator<A> itrA = Spliterators.iterator(splitrA);
        final Iterator<B> itrB = Spliterators.iterator(splitrB);
        return StreamSupport.stream(new Spliterators.AbstractSpliterator<R>(Math.min(splitrA.estimateSize(), splitrB.estimateSize()), characteristics) { // from class: com.google.common.collect.Streams.1
            @Override // java.util.Spliterator
            public boolean tryAdvance(Consumer<? super R> consumer) {
                if (itrA.hasNext() && itrB.hasNext()) {
                    consumer.accept((Object) function.apply(itrA.next(), itrB.next()));
                    return true;
                }
                return false;
            }
        }, isParallel);
    }

    public static <T, R> Stream<R> mapWithIndex(Stream<T> stream, final FunctionWithIndex<? super T, ? extends R> function) {
        Preconditions.checkNotNull(stream);
        Preconditions.checkNotNull(function);
        boolean isParallel = stream.isParallel();
        Spliterator<T> fromSpliterator = stream.spliterator();
        if (!fromSpliterator.hasCharacteristics(16384)) {
            final Iterator<T> fromIterator = Spliterators.iterator(fromSpliterator);
            return StreamSupport.stream(new Spliterators.AbstractSpliterator<R>(fromSpliterator.estimateSize(), fromSpliterator.characteristics() & 80) { // from class: com.google.common.collect.Streams.2
                long index = 0;

                @Override // java.util.Spliterator
                public boolean tryAdvance(Consumer<? super R> consumer) {
                    if (fromIterator.hasNext()) {
                        FunctionWithIndex functionWithIndex = function;
                        Object next = fromIterator.next();
                        long j = this.index;
                        this.index = 1 + j;
                        consumer.accept((Object) functionWithIndex.apply(next, j));
                        return true;
                    }
                    return false;
                }
            }, isParallel);
        }
        return StreamSupport.stream(new C1Splitr(fromSpliterator, 0L, function), isParallel);
    }

    /* JADX INFO: Add missing generic type declarations: [R, T] */
    /* renamed from: com.google.common.collect.Streams$1Splitr, reason: invalid class name */
    /* loaded from: classes.dex */
    class C1Splitr<R, T> extends MapWithIndexSpliterator<Spliterator<T>, R, C1Splitr> implements Consumer<T> {
        T holder;
        final /* synthetic */ FunctionWithIndex val$function;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Not initialized variable reg: 4, insn: 0x0000: IPUT (r4 I:com.google.common.collect.Streams$FunctionWithIndex), (r0 I:com.google.common.collect.Streams$1Splitr) (LINE:440) com.google.common.collect.Streams.1Splitr.val$function com.google.common.collect.Streams$FunctionWithIndex, block:B:1:0x0000 */
        C1Splitr(Spliterator spliterator, Spliterator<T> spliterator2, long j) {
            super(spliterator, spliterator2);
            FunctionWithIndex functionWithIndex;
            this.val$function = functionWithIndex;
        }

        @Override // java.util.function.Consumer
        public void accept(@Nullable T t) {
            this.holder = t;
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super R> consumer) {
            if (this.fromSpliterator.tryAdvance(this)) {
                try {
                    FunctionWithIndex functionWithIndex = this.val$function;
                    T t = this.holder;
                    long j = this.index;
                    this.index = 1 + j;
                    consumer.accept((Object) functionWithIndex.apply(t, j));
                    this.holder = null;
                    return true;
                } catch (Throwable th) {
                    this.holder = null;
                    throw th;
                }
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.common.collect.Streams.MapWithIndexSpliterator
        public C1Splitr createSplit(Spliterator<T> from, long i) {
            return new C1Splitr(from, i, this.val$function);
        }
    }

    /* loaded from: classes.dex */
    private static abstract class MapWithIndexSpliterator<F extends Spliterator<?>, R, S extends MapWithIndexSpliterator<F, R, S>> implements Spliterator<R> {
        final F fromSpliterator;
        long index;

        abstract S createSplit(F f, long j);

        MapWithIndexSpliterator(F fromSpliterator, long index) {
            this.fromSpliterator = fromSpliterator;
            this.index = index;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Spliterator
        public S trySplit() {
            Spliterator trySplit = this.fromSpliterator.trySplit();
            if (trySplit == null) {
                return null;
            }
            S s = (S) createSplit(trySplit, this.index);
            this.index += trySplit.getExactSizeIfKnown();
            return s;
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return this.fromSpliterator.estimateSize();
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return this.fromSpliterator.characteristics() & ObjectSpliterators.LIST_SPLITERATOR_CHARACTERISTICS;
        }
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [java.util.Spliterator$OfInt] */
    public static <R> Stream<R> mapWithIndex(IntStream stream, final IntFunctionWithIndex<R> function) {
        Preconditions.checkNotNull(stream);
        Preconditions.checkNotNull(function);
        boolean isParallel = stream.isParallel();
        ?? spliterator = stream.spliterator();
        if (!spliterator.hasCharacteristics(16384)) {
            final PrimitiveIterator.OfInt fromIterator = Spliterators.iterator((Spliterator.OfInt) spliterator);
            return StreamSupport.stream(new Spliterators.AbstractSpliterator<R>(spliterator.estimateSize(), spliterator.characteristics() & 80) { // from class: com.google.common.collect.Streams.3
                long index = 0;

                @Override // java.util.Spliterator
                public boolean tryAdvance(Consumer<? super R> consumer) {
                    if (fromIterator.hasNext()) {
                        IntFunctionWithIndex intFunctionWithIndex = function;
                        int nextInt = fromIterator.nextInt();
                        long j = this.index;
                        this.index = 1 + j;
                        consumer.accept((Object) intFunctionWithIndex.apply(nextInt, j));
                        return true;
                    }
                    return false;
                }
            }, isParallel);
        }
        return StreamSupport.stream(new C2Splitr(spliterator, 0L, function), isParallel);
    }

    /* JADX INFO: Add missing generic type declarations: [R] */
    /* renamed from: com.google.common.collect.Streams$2Splitr, reason: invalid class name */
    /* loaded from: classes.dex */
    class C2Splitr<R> extends MapWithIndexSpliterator<Spliterator.OfInt, R, C2Splitr> implements IntConsumer, Spliterator<R> {
        int holder;
        final /* synthetic */ IntFunctionWithIndex val$function;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Not initialized variable reg: 4, insn: 0x0000: IPUT (r4 I:com.google.common.collect.Streams$IntFunctionWithIndex), (r0 I:com.google.common.collect.Streams$2Splitr) (LINE:572) com.google.common.collect.Streams.2Splitr.val$function com.google.common.collect.Streams$IntFunctionWithIndex, block:B:1:0x0000 */
        C2Splitr(Spliterator.OfInt splitr, Spliterator.OfInt ofInt, long j) {
            super(splitr, ofInt);
            IntFunctionWithIndex intFunctionWithIndex;
            this.val$function = intFunctionWithIndex;
        }

        @Override // java.util.function.IntConsumer
        public void accept(int t) {
            this.holder = t;
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super R> consumer) {
            if (((Spliterator.OfInt) this.fromSpliterator).tryAdvance((IntConsumer) this)) {
                IntFunctionWithIndex intFunctionWithIndex = this.val$function;
                int i = this.holder;
                long j = this.index;
                this.index = 1 + j;
                consumer.accept((Object) intFunctionWithIndex.apply(i, j));
                return true;
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.Streams.MapWithIndexSpliterator
        public C2Splitr createSplit(Spliterator.OfInt from, long i) {
            return new C2Splitr(from, i, this.val$function);
        }
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [java.util.Spliterator$OfLong] */
    public static <R> Stream<R> mapWithIndex(LongStream stream, final LongFunctionWithIndex<R> function) {
        Preconditions.checkNotNull(stream);
        Preconditions.checkNotNull(function);
        boolean isParallel = stream.isParallel();
        ?? spliterator = stream.spliterator();
        if (!spliterator.hasCharacteristics(16384)) {
            final PrimitiveIterator.OfLong fromIterator = Spliterators.iterator((Spliterator.OfLong) spliterator);
            return StreamSupport.stream(new Spliterators.AbstractSpliterator<R>(spliterator.estimateSize(), spliterator.characteristics() & 80) { // from class: com.google.common.collect.Streams.4
                long index = 0;

                @Override // java.util.Spliterator
                public boolean tryAdvance(Consumer<? super R> consumer) {
                    if (fromIterator.hasNext()) {
                        LongFunctionWithIndex longFunctionWithIndex = function;
                        long nextLong = fromIterator.nextLong();
                        long j = this.index;
                        this.index = 1 + j;
                        consumer.accept((Object) longFunctionWithIndex.apply(nextLong, j));
                        return true;
                    }
                    return false;
                }
            }, isParallel);
        }
        return StreamSupport.stream(new C3Splitr(spliterator, 0L, function), isParallel);
    }

    /* JADX INFO: Add missing generic type declarations: [R] */
    /* renamed from: com.google.common.collect.Streams$3Splitr, reason: invalid class name */
    /* loaded from: classes.dex */
    class C3Splitr<R> extends MapWithIndexSpliterator<Spliterator.OfLong, R, C3Splitr> implements LongConsumer, Spliterator<R> {
        long holder;
        final /* synthetic */ LongFunctionWithIndex val$function;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Not initialized variable reg: 4, insn: 0x0000: IPUT (r4 I:com.google.common.collect.Streams$LongFunctionWithIndex), (r0 I:com.google.common.collect.Streams$3Splitr) (LINE:663) com.google.common.collect.Streams.3Splitr.val$function com.google.common.collect.Streams$LongFunctionWithIndex, block:B:1:0x0000 */
        C3Splitr(Spliterator.OfLong splitr, Spliterator.OfLong ofLong, long j) {
            super(splitr, ofLong);
            LongFunctionWithIndex longFunctionWithIndex;
            this.val$function = longFunctionWithIndex;
        }

        @Override // java.util.function.LongConsumer
        public void accept(long t) {
            this.holder = t;
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super R> consumer) {
            if (((Spliterator.OfLong) this.fromSpliterator).tryAdvance((LongConsumer) this)) {
                LongFunctionWithIndex longFunctionWithIndex = this.val$function;
                long j = this.holder;
                long j2 = this.index;
                this.index = 1 + j2;
                consumer.accept((Object) longFunctionWithIndex.apply(j, j2));
                return true;
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.Streams.MapWithIndexSpliterator
        public C3Splitr createSplit(Spliterator.OfLong from, long i) {
            return new C3Splitr(from, i, this.val$function);
        }
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [java.util.Spliterator$OfDouble] */
    public static <R> Stream<R> mapWithIndex(DoubleStream stream, final DoubleFunctionWithIndex<R> function) {
        Preconditions.checkNotNull(stream);
        Preconditions.checkNotNull(function);
        boolean isParallel = stream.isParallel();
        ?? spliterator = stream.spliterator();
        if (!spliterator.hasCharacteristics(16384)) {
            final PrimitiveIterator.OfDouble fromIterator = Spliterators.iterator((Spliterator.OfDouble) spliterator);
            return StreamSupport.stream(new Spliterators.AbstractSpliterator<R>(spliterator.estimateSize(), spliterator.characteristics() & 80) { // from class: com.google.common.collect.Streams.5
                long index = 0;

                @Override // java.util.Spliterator
                public boolean tryAdvance(Consumer<? super R> consumer) {
                    if (fromIterator.hasNext()) {
                        DoubleFunctionWithIndex doubleFunctionWithIndex = function;
                        double nextDouble = fromIterator.nextDouble();
                        long j = this.index;
                        this.index = 1 + j;
                        consumer.accept((Object) doubleFunctionWithIndex.apply(nextDouble, j));
                        return true;
                    }
                    return false;
                }
            }, isParallel);
        }
        return StreamSupport.stream(new C4Splitr(spliterator, 0L, function), isParallel);
    }

    /* JADX INFO: Add missing generic type declarations: [R] */
    /* renamed from: com.google.common.collect.Streams$4Splitr, reason: invalid class name */
    /* loaded from: classes.dex */
    class C4Splitr<R> extends MapWithIndexSpliterator<Spliterator.OfDouble, R, C4Splitr> implements DoubleConsumer, Spliterator<R> {
        double holder;
        final /* synthetic */ DoubleFunctionWithIndex val$function;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Not initialized variable reg: 4, insn: 0x0000: IPUT (r4 I:com.google.common.collect.Streams$DoubleFunctionWithIndex), (r0 I:com.google.common.collect.Streams$4Splitr) (LINE:755) com.google.common.collect.Streams.4Splitr.val$function com.google.common.collect.Streams$DoubleFunctionWithIndex, block:B:1:0x0000 */
        C4Splitr(Spliterator.OfDouble splitr, Spliterator.OfDouble ofDouble, long j) {
            super(splitr, ofDouble);
            DoubleFunctionWithIndex doubleFunctionWithIndex;
            this.val$function = doubleFunctionWithIndex;
        }

        @Override // java.util.function.DoubleConsumer
        public void accept(double t) {
            this.holder = t;
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super R> consumer) {
            if (((Spliterator.OfDouble) this.fromSpliterator).tryAdvance((DoubleConsumer) this)) {
                DoubleFunctionWithIndex doubleFunctionWithIndex = this.val$function;
                double d = this.holder;
                long j = this.index;
                this.index = 1 + j;
                consumer.accept((Object) doubleFunctionWithIndex.apply(d, j));
                return true;
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.Streams.MapWithIndexSpliterator
        public C4Splitr createSplit(Spliterator.OfDouble from, long i) {
            return new C4Splitr(from, i, this.val$function);
        }
    }

    private Streams() {
    }
}
