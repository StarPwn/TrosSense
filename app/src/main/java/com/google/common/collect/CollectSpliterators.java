package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.CollectSpliterators;
import com.trossense.bl;
import java.util.Comparator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import javax.annotation.Nullable;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class CollectSpliterators {
    private CollectSpliterators() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> Spliterator<T> indexed(int size, int extraCharacteristics, IntFunction<T> function) {
        return indexed(size, extraCharacteristics, function, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> Spliterator<T> indexed(int size, int extraCharacteristics, IntFunction<T> function, Comparator<? super T> comparator) {
        if (comparator != null) {
            Preconditions.checkArgument((extraCharacteristics & 4) != 0);
        }
        return new C1WithCharacteristics(IntStream.range(0, size).mapToObj(function).spliterator(), extraCharacteristics, comparator);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: Add missing generic type declarations: [T] */
    /* renamed from: com.google.common.collect.CollectSpliterators$1WithCharacteristics, reason: invalid class name */
    /* loaded from: classes.dex */
    public class C1WithCharacteristics<T> implements Spliterator<T> {
        private final Spliterator<T> delegate;
        final /* synthetic */ Comparator val$comparator;
        final /* synthetic */ int val$extraCharacteristics;

        C1WithCharacteristics(Spliterator spliterator, int i, Comparator comparator) {
            this.val$extraCharacteristics = i;
            this.val$comparator = comparator;
            this.delegate = spliterator;
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super T> action) {
            return this.delegate.tryAdvance(action);
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super T> action) {
            this.delegate.forEachRemaining(action);
        }

        @Override // java.util.Spliterator
        @Nullable
        public Spliterator<T> trySplit() {
            Spliterator<T> split = this.delegate.trySplit();
            if (split == null) {
                return null;
            }
            return new C1WithCharacteristics(split, this.val$extraCharacteristics, this.val$comparator);
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return this.delegate.estimateSize();
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return this.delegate.characteristics() | this.val$extraCharacteristics;
        }

        @Override // java.util.Spliterator
        public Comparator<? super T> getComparator() {
            if (hasCharacteristics(4)) {
                return this.val$comparator;
            }
            throw new IllegalStateException();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <F, T> Spliterator<T> map(Spliterator<F> fromSpliterator, Function<? super F, ? extends T> function) {
        Preconditions.checkNotNull(fromSpliterator);
        Preconditions.checkNotNull(function);
        return new AnonymousClass1(fromSpliterator, function);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: Add missing generic type declarations: [T] */
    /* renamed from: com.google.common.collect.CollectSpliterators$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public static class AnonymousClass1<T> implements Spliterator<T> {
        final /* synthetic */ Spliterator val$fromSpliterator;
        final /* synthetic */ Function val$function;

        AnonymousClass1(Spliterator spliterator, Function function) {
            this.val$fromSpliterator = spliterator;
            this.val$function = function;
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(final Consumer<? super T> action) {
            Spliterator spliterator = this.val$fromSpliterator;
            final Function function = this.val$function;
            return spliterator.tryAdvance(new Consumer() { // from class: com.google.common.collect.CollectSpliterators$1$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    action.accept(function.apply(obj));
                }
            });
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(final Consumer<? super T> action) {
            Spliterator spliterator = this.val$fromSpliterator;
            final Function function = this.val$function;
            spliterator.forEachRemaining(new Consumer() { // from class: com.google.common.collect.CollectSpliterators$1$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    action.accept(function.apply(obj));
                }
            });
        }

        @Override // java.util.Spliterator
        public Spliterator<T> trySplit() {
            Spliterator<T> trySplit = this.val$fromSpliterator.trySplit();
            if (trySplit != null) {
                return CollectSpliterators.map(trySplit, this.val$function);
            }
            return null;
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return this.val$fromSpliterator.estimateSize();
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return this.val$fromSpliterator.characteristics() & (-262);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> Spliterator<T> filter(Spliterator<T> fromSpliterator, Predicate<? super T> predicate) {
        Preconditions.checkNotNull(fromSpliterator);
        Preconditions.checkNotNull(predicate);
        return new C1Splitr(fromSpliterator, predicate);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: Add missing generic type declarations: [T] */
    /* renamed from: com.google.common.collect.CollectSpliterators$1Splitr, reason: invalid class name */
    /* loaded from: classes.dex */
    public class C1Splitr<T> implements Spliterator<T>, Consumer<T> {
        T holder = null;
        final /* synthetic */ Spliterator val$fromSpliterator;
        final /* synthetic */ Predicate val$predicate;

        C1Splitr(Spliterator spliterator, Predicate predicate) {
            this.val$fromSpliterator = spliterator;
            this.val$predicate = predicate;
        }

        @Override // java.util.function.Consumer
        public void accept(T t) {
            this.holder = t;
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super T> consumer) {
            while (this.val$fromSpliterator.tryAdvance(this)) {
                try {
                    if (this.val$predicate.test(this.holder)) {
                        consumer.accept(this.holder);
                        this.holder = null;
                        return true;
                    }
                } finally {
                    this.holder = null;
                }
            }
            return false;
        }

        @Override // java.util.Spliterator
        public Spliterator<T> trySplit() {
            Spliterator<T> fromSplit = this.val$fromSpliterator.trySplit();
            if (fromSplit == null) {
                return null;
            }
            return CollectSpliterators.filter(fromSplit, this.val$predicate);
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return this.val$fromSpliterator.estimateSize() / 2;
        }

        @Override // java.util.Spliterator
        public Comparator<? super T> getComparator() {
            return this.val$fromSpliterator.getComparator();
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return this.val$fromSpliterator.characteristics() & bl.dN;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <F, T> Spliterator<T> flatMap(Spliterator<F> fromSpliterator, Function<? super F, Spliterator<T>> function, int topCharacteristics, long topSize) {
        Preconditions.checkArgument((topCharacteristics & 16384) == 0, "flatMap does not support SUBSIZED characteristic");
        Preconditions.checkArgument((topCharacteristics & 4) == 0, "flatMap does not support SORTED characteristic");
        Preconditions.checkNotNull(fromSpliterator);
        Preconditions.checkNotNull(function);
        return new C1FlatMapSpliterator(null, fromSpliterator, topCharacteristics, topSize, function);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: Add missing generic type declarations: [T] */
    /* renamed from: com.google.common.collect.CollectSpliterators$1FlatMapSpliterator, reason: invalid class name */
    /* loaded from: classes.dex */
    public class C1FlatMapSpliterator<T> implements Spliterator<T> {
        final int characteristics;
        long estimatedSize;
        final Spliterator<F> from;

        @Nullable
        Spliterator<T> prefix;
        final /* synthetic */ Function val$function;

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Not initialized variable reg: 6, insn: 0x0000: IPUT (r6 I:java.util.function.Function), (r0 I:com.google.common.collect.CollectSpliterators$1FlatMapSpliterator) (LINE:216) com.google.common.collect.CollectSpliterators.1FlatMapSpliterator.val$function java.util.function.Function, block:B:1:0x0000 */
        C1FlatMapSpliterator(Spliterator spliterator, Spliterator<T> spliterator2, Spliterator<F> spliterator3, int i, long j) {
            Function function;
            this.val$function = function;
            this.prefix = spliterator;
            this.from = spliterator2;
            this.characteristics = spliterator3;
            this.estimatedSize = i;
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super T> action) {
            Spliterator<F> spliterator;
            final Function function;
            do {
                if (this.prefix != null && this.prefix.tryAdvance(action)) {
                    if (this.estimatedSize != Long.MAX_VALUE) {
                        this.estimatedSize--;
                        return true;
                    }
                    return true;
                }
                this.prefix = null;
                spliterator = this.from;
                function = this.val$function;
            } while (spliterator.tryAdvance(new Consumer() { // from class: com.google.common.collect.CollectSpliterators$1FlatMapSpliterator$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    CollectSpliterators.C1FlatMapSpliterator.this.m155x2277fef6(function, obj);
                }
            }));
            return false;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$tryAdvance$0$com-google-common-collect-CollectSpliterators$1FlatMapSpliterator, reason: not valid java name */
        public /* synthetic */ void m155x2277fef6(Function function, Object fromElement) {
            this.prefix = (Spliterator) function.apply(fromElement);
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(final Consumer<? super T> action) {
            if (this.prefix != null) {
                this.prefix.forEachRemaining(action);
                this.prefix = null;
            }
            Spliterator<F> spliterator = this.from;
            final Function function = this.val$function;
            spliterator.forEachRemaining(new Consumer() { // from class: com.google.common.collect.CollectSpliterators$1FlatMapSpliterator$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ((Spliterator) function.apply(obj)).forEachRemaining(action);
                }
            });
            this.estimatedSize = 0L;
        }

        @Override // java.util.Spliterator
        public Spliterator<T> trySplit() {
            long estSplitSize;
            Spliterator trySplit = this.from.trySplit();
            if (trySplit != null) {
                int splitCharacteristics = this.characteristics & (-65);
                long estSplitSize2 = estimateSize();
                if (estSplitSize2 >= Long.MAX_VALUE) {
                    estSplitSize = estSplitSize2;
                } else {
                    long estSplitSize3 = estSplitSize2 / 2;
                    this.estimatedSize -= estSplitSize3;
                    estSplitSize = estSplitSize3;
                }
                Spliterator<T> result = new C1FlatMapSpliterator<>(this.prefix, trySplit, splitCharacteristics, estSplitSize, this.val$function);
                this.prefix = null;
                return result;
            }
            Spliterator<T> result2 = this.prefix;
            if (result2 == null) {
                return null;
            }
            Spliterator<T> result3 = this.prefix;
            this.prefix = null;
            return result3;
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            if (this.prefix != null) {
                this.estimatedSize = Math.max(this.estimatedSize, this.prefix.estimateSize());
            }
            return Math.max(this.estimatedSize, 0L);
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return this.characteristics;
        }
    }
}
