package com.google.common.collect;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public final class Iterables {
    private Iterables() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T> Iterable<T> unmodifiableIterable(Iterable<? extends T> iterable) {
        Preconditions.checkNotNull(iterable);
        if ((iterable instanceof UnmodifiableIterable) || (iterable instanceof ImmutableCollection)) {
            return iterable;
        }
        return new UnmodifiableIterable(iterable, null);
    }

    @Deprecated
    public static <E> Iterable<E> unmodifiableIterable(ImmutableCollection<E> iterable) {
        return (Iterable) Preconditions.checkNotNull(iterable);
    }

    /* loaded from: classes.dex */
    private static final class UnmodifiableIterable<T> extends FluentIterable<T> {
        private final Iterable<? extends T> iterable;

        /* synthetic */ UnmodifiableIterable(Iterable x0, AnonymousClass1 x1) {
            this(x0);
        }

        private UnmodifiableIterable(Iterable<? extends T> iterable) {
            this.iterable = iterable;
        }

        @Override // java.lang.Iterable
        public Iterator<T> iterator() {
            return Iterators.unmodifiableIterator(this.iterable.iterator());
        }

        @Override // java.lang.Iterable
        public void forEach(Consumer<? super T> action) {
            this.iterable.forEach(action);
        }

        @Override // java.lang.Iterable
        public Spliterator<T> spliterator() {
            return this.iterable.spliterator();
        }

        @Override // com.google.common.collect.FluentIterable
        public String toString() {
            return this.iterable.toString();
        }
    }

    public static int size(Iterable<?> iterable) {
        if (iterable instanceof Collection) {
            return ((Collection) iterable).size();
        }
        return Iterators.size(iterable.iterator());
    }

    public static boolean contains(Iterable<?> iterable, @Nullable Object element) {
        if (iterable instanceof Collection) {
            Collection<?> collection = (Collection) iterable;
            return Collections2.safeContains(collection, element);
        }
        return Iterators.contains(iterable.iterator(), element);
    }

    public static boolean removeAll(Iterable<?> removeFrom, Collection<?> elementsToRemove) {
        if (removeFrom instanceof Collection) {
            return ((Collection) removeFrom).removeAll((Collection) Preconditions.checkNotNull(elementsToRemove));
        }
        return Iterators.removeAll(removeFrom.iterator(), elementsToRemove);
    }

    public static boolean retainAll(Iterable<?> removeFrom, Collection<?> elementsToRetain) {
        if (removeFrom instanceof Collection) {
            return ((Collection) removeFrom).retainAll((Collection) Preconditions.checkNotNull(elementsToRetain));
        }
        return Iterators.retainAll(removeFrom.iterator(), elementsToRetain);
    }

    public static <T> boolean removeIf(Iterable<T> removeFrom, Predicate<? super T> predicate) {
        if (removeFrom instanceof Collection) {
            return ((Collection) removeFrom).removeIf(predicate);
        }
        return Iterators.removeIf(removeFrom.iterator(), predicate);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Nullable
    public static <T> T removeFirstMatching(Iterable<T> removeFrom, Predicate<? super T> predicate) {
        Preconditions.checkNotNull(predicate);
        Iterator<T> iterator = removeFrom.iterator();
        while (iterator.hasNext()) {
            T next = iterator.next();
            if (predicate.apply(next)) {
                iterator.remove();
                return next;
            }
        }
        return null;
    }

    public static boolean elementsEqual(Iterable<?> iterable1, Iterable<?> iterable2) {
        if ((iterable1 instanceof Collection) && (iterable2 instanceof Collection)) {
            Collection<?> collection1 = (Collection) iterable1;
            Collection<?> collection2 = (Collection) iterable2;
            if (collection1.size() != collection2.size()) {
                return false;
            }
        }
        return Iterators.elementsEqual(iterable1.iterator(), iterable2.iterator());
    }

    public static String toString(Iterable<?> iterable) {
        return Iterators.toString(iterable.iterator());
    }

    public static <T> T getOnlyElement(Iterable<T> iterable) {
        return (T) Iterators.getOnlyElement(iterable.iterator());
    }

    @Nullable
    public static <T> T getOnlyElement(Iterable<? extends T> iterable, @Nullable T t) {
        return (T) Iterators.getOnlyElement(iterable.iterator(), t);
    }

    public static <T> T[] toArray(Iterable<? extends T> iterable, Class<T> cls) {
        return (T[]) toArray(iterable, ObjectArrays.newArray(cls, 0));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> T[] toArray(Iterable<? extends T> iterable, T[] tArr) {
        return (T[]) castOrCopyToCollection(iterable).toArray(tArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Object[] toArray(Iterable<?> iterable) {
        return castOrCopyToCollection(iterable).toArray();
    }

    private static <E> Collection<E> castOrCopyToCollection(Iterable<E> iterable) {
        return iterable instanceof Collection ? (Collection) iterable : Lists.newArrayList(iterable.iterator());
    }

    public static <T> boolean addAll(Collection<T> addTo, Iterable<? extends T> elementsToAdd) {
        if (elementsToAdd instanceof Collection) {
            Collection<? extends T> c = Collections2.cast(elementsToAdd);
            return addTo.addAll(c);
        }
        return Iterators.addAll(addTo, ((Iterable) Preconditions.checkNotNull(elementsToAdd)).iterator());
    }

    public static int frequency(Iterable<?> iterable, @Nullable Object obj) {
        if (iterable instanceof Multiset) {
            return ((Multiset) iterable).count(obj);
        }
        if (iterable instanceof Set) {
            return ((Set) iterable).contains(obj) ? 1 : 0;
        }
        return Iterators.frequency(iterable.iterator(), obj);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: Add missing generic type declarations: [T] */
    /* renamed from: com.google.common.collect.Iterables$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public static class AnonymousClass1<T> extends FluentIterable<T> {
        final /* synthetic */ Iterable val$iterable;

        AnonymousClass1(Iterable iterable) {
            this.val$iterable = iterable;
        }

        @Override // java.lang.Iterable
        public Iterator<T> iterator() {
            return Iterators.cycle(this.val$iterable);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static /* synthetic */ Iterable lambda$spliterator$0(Iterable iterable) {
            return iterable;
        }

        @Override // java.lang.Iterable
        public Spliterator<T> spliterator() {
            final Iterable iterable = this.val$iterable;
            return Stream.generate(new Supplier() { // from class: com.google.common.collect.Iterables$1$$ExternalSyntheticLambda0
                @Override // java.util.function.Supplier
                public final Object get() {
                    return Iterables.AnonymousClass1.lambda$spliterator$0(iterable);
                }
            }).flatMap(new Function() { // from class: com.google.common.collect.Iterables$1$$ExternalSyntheticLambda1
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return Streams.stream((Iterable) obj);
                }
            }).spliterator();
        }

        @Override // com.google.common.collect.FluentIterable
        public String toString() {
            return this.val$iterable.toString() + " (cycled)";
        }
    }

    public static <T> Iterable<T> cycle(Iterable<T> iterable) {
        Preconditions.checkNotNull(iterable);
        return new AnonymousClass1(iterable);
    }

    public static <T> Iterable<T> cycle(T... elements) {
        return cycle(Lists.newArrayList(elements));
    }

    public static <T> Iterable<T> concat(Iterable<? extends T> a, Iterable<? extends T> b) {
        return FluentIterable.concat(a, b);
    }

    public static <T> Iterable<T> concat(Iterable<? extends T> a, Iterable<? extends T> b, Iterable<? extends T> c) {
        return FluentIterable.concat(a, b, c);
    }

    public static <T> Iterable<T> concat(Iterable<? extends T> a, Iterable<? extends T> b, Iterable<? extends T> c, Iterable<? extends T> d) {
        return FluentIterable.concat(a, b, c, d);
    }

    public static <T> Iterable<T> concat(Iterable<? extends T>... inputs) {
        return concat(ImmutableList.copyOf(inputs));
    }

    public static <T> Iterable<T> concat(Iterable<? extends Iterable<? extends T>> inputs) {
        return FluentIterable.concat(inputs);
    }

    public static <T> Iterable<List<T>> partition(final Iterable<T> iterable, final int size) {
        Preconditions.checkNotNull(iterable);
        Preconditions.checkArgument(size > 0);
        return new FluentIterable<List<T>>() { // from class: com.google.common.collect.Iterables.2
            @Override // java.lang.Iterable
            public Iterator<List<T>> iterator() {
                return Iterators.partition(iterable.iterator(), size);
            }
        };
    }

    public static <T> Iterable<List<T>> paddedPartition(final Iterable<T> iterable, final int size) {
        Preconditions.checkNotNull(iterable);
        Preconditions.checkArgument(size > 0);
        return new FluentIterable<List<T>>() { // from class: com.google.common.collect.Iterables.3
            @Override // java.lang.Iterable
            public Iterator<List<T>> iterator() {
                return Iterators.paddedPartition(iterable.iterator(), size);
            }
        };
    }

    public static <T> Iterable<T> filter(Iterable<T> unfiltered, Predicate<? super T> retainIfTrue) {
        Preconditions.checkNotNull(unfiltered);
        Preconditions.checkNotNull(retainIfTrue);
        return new AnonymousClass4(unfiltered, retainIfTrue);
    }

    /* JADX INFO: Add missing generic type declarations: [T] */
    /* renamed from: com.google.common.collect.Iterables$4, reason: invalid class name */
    /* loaded from: classes.dex */
    static class AnonymousClass4<T> extends FluentIterable<T> {
        final /* synthetic */ Predicate val$retainIfTrue;
        final /* synthetic */ Iterable val$unfiltered;

        AnonymousClass4(Iterable iterable, Predicate predicate) {
            this.val$unfiltered = iterable;
            this.val$retainIfTrue = predicate;
        }

        @Override // java.lang.Iterable
        public Iterator<T> iterator() {
            return Iterators.filter(this.val$unfiltered.iterator(), this.val$retainIfTrue);
        }

        @Override // java.lang.Iterable
        public void forEach(final Consumer<? super T> action) {
            Preconditions.checkNotNull(action);
            Iterable iterable = this.val$unfiltered;
            final Predicate predicate = this.val$retainIfTrue;
            iterable.forEach(new Consumer() { // from class: com.google.common.collect.Iterables$4$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    Iterables.AnonymousClass4.lambda$forEach$0(Predicate.this, action, obj);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static /* synthetic */ void lambda$forEach$0(Predicate retainIfTrue, Consumer action, Object a) {
            if (retainIfTrue.test(a)) {
                action.accept(a);
            }
        }

        @Override // java.lang.Iterable
        public Spliterator<T> spliterator() {
            return CollectSpliterators.filter(this.val$unfiltered.spliterator(), this.val$retainIfTrue);
        }
    }

    public static <T> Iterable<T> filter(Iterable<?> unfiltered, Class<T> desiredType) {
        Preconditions.checkNotNull(unfiltered);
        Preconditions.checkNotNull(desiredType);
        return new AnonymousClass5(unfiltered, desiredType);
    }

    /* JADX INFO: Add missing generic type declarations: [T] */
    /* renamed from: com.google.common.collect.Iterables$5, reason: invalid class name */
    /* loaded from: classes.dex */
    static class AnonymousClass5<T> extends FluentIterable<T> {
        final /* synthetic */ Class val$desiredType;
        final /* synthetic */ Iterable val$unfiltered;

        AnonymousClass5(Iterable iterable, Class cls) {
            this.val$unfiltered = iterable;
            this.val$desiredType = cls;
        }

        @Override // java.lang.Iterable
        public Iterator<T> iterator() {
            return Iterators.filter((Iterator<?>) this.val$unfiltered.iterator(), this.val$desiredType);
        }

        @Override // java.lang.Iterable
        public void forEach(final Consumer<? super T> action) {
            Preconditions.checkNotNull(action);
            Iterable iterable = this.val$unfiltered;
            final Class cls = this.val$desiredType;
            iterable.forEach(new Consumer() { // from class: com.google.common.collect.Iterables$5$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    Iterables.AnonymousClass5.lambda$forEach$0(cls, action, obj);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ void lambda$forEach$0(Class desiredType, Consumer consumer, Object o) {
            if (desiredType.isInstance(o)) {
                consumer.accept(desiredType.cast(o));
            }
        }

        @Override // java.lang.Iterable
        public Spliterator<T> spliterator() {
            Spliterator<T> spliterator = this.val$unfiltered.spliterator();
            final Class cls = this.val$desiredType;
            cls.getClass();
            return CollectSpliterators.filter(spliterator, new java.util.function.Predicate() { // from class: com.google.common.collect.Iterables$5$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean isInstance;
                    isInstance = cls.isInstance(obj);
                    return isInstance;
                }
            });
        }
    }

    public static <T> boolean any(Iterable<T> iterable, Predicate<? super T> predicate) {
        return Iterators.any(iterable.iterator(), predicate);
    }

    public static <T> boolean all(Iterable<T> iterable, Predicate<? super T> predicate) {
        return Iterators.all(iterable.iterator(), predicate);
    }

    public static <T> T find(Iterable<T> iterable, Predicate<? super T> predicate) {
        return (T) Iterators.find(iterable.iterator(), predicate);
    }

    @Nullable
    public static <T> T find(Iterable<? extends T> iterable, Predicate<? super T> predicate, @Nullable T t) {
        return (T) Iterators.find(iterable.iterator(), predicate, t);
    }

    public static <T> Optional<T> tryFind(Iterable<T> iterable, Predicate<? super T> predicate) {
        return Iterators.tryFind(iterable.iterator(), predicate);
    }

    public static <T> int indexOf(Iterable<T> iterable, Predicate<? super T> predicate) {
        return Iterators.indexOf(iterable.iterator(), predicate);
    }

    public static <F, T> Iterable<T> transform(Iterable<F> fromIterable, com.google.common.base.Function<? super F, ? extends T> function) {
        Preconditions.checkNotNull(fromIterable);
        Preconditions.checkNotNull(function);
        return new AnonymousClass6(fromIterable, function);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: Add missing generic type declarations: [T] */
    /* renamed from: com.google.common.collect.Iterables$6, reason: invalid class name */
    /* loaded from: classes.dex */
    public static class AnonymousClass6<T> extends FluentIterable<T> {
        final /* synthetic */ Iterable val$fromIterable;
        final /* synthetic */ com.google.common.base.Function val$function;

        AnonymousClass6(Iterable iterable, com.google.common.base.Function function) {
            this.val$fromIterable = iterable;
            this.val$function = function;
        }

        @Override // java.lang.Iterable
        public Iterator<T> iterator() {
            return Iterators.transform(this.val$fromIterable.iterator(), this.val$function);
        }

        @Override // java.lang.Iterable
        public void forEach(final Consumer<? super T> action) {
            Preconditions.checkNotNull(action);
            Iterable iterable = this.val$fromIterable;
            final com.google.common.base.Function function = this.val$function;
            iterable.forEach(new Consumer() { // from class: com.google.common.collect.Iterables$6$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    action.accept(function.apply(obj));
                }
            });
        }

        @Override // java.lang.Iterable
        public Spliterator<T> spliterator() {
            return CollectSpliterators.map(this.val$fromIterable.spliterator(), this.val$function);
        }
    }

    public static <T> T get(Iterable<T> iterable, int i) {
        Preconditions.checkNotNull(iterable);
        if (iterable instanceof List) {
            return (T) ((List) iterable).get(i);
        }
        return (T) Iterators.get(iterable.iterator(), i);
    }

    @Nullable
    public static <T> T get(Iterable<? extends T> iterable, int i, @Nullable T t) {
        Preconditions.checkNotNull(iterable);
        Iterators.checkNonnegative(i);
        if (iterable instanceof List) {
            List cast = Lists.cast(iterable);
            return i < cast.size() ? (T) cast.get(i) : t;
        }
        Iterator<? extends T> it2 = iterable.iterator();
        Iterators.advance(it2, i);
        return (T) Iterators.getNext(it2, t);
    }

    @Nullable
    public static <T> T getFirst(Iterable<? extends T> iterable, @Nullable T t) {
        return (T) Iterators.getNext(iterable.iterator(), t);
    }

    public static <T> T getLast(Iterable<T> iterable) {
        if (iterable instanceof List) {
            List list = (List) iterable;
            if (list.isEmpty()) {
                throw new NoSuchElementException();
            }
            return (T) getLastInNonemptyList(list);
        }
        return (T) Iterators.getLast(iterable.iterator());
    }

    @Nullable
    public static <T> T getLast(Iterable<? extends T> iterable, @Nullable T t) {
        if (iterable instanceof Collection) {
            if (Collections2.cast(iterable).isEmpty()) {
                return t;
            }
            if (iterable instanceof List) {
                return (T) getLastInNonemptyList(Lists.cast(iterable));
            }
        }
        return (T) Iterators.getLast(iterable.iterator(), t);
    }

    private static <T> T getLastInNonemptyList(List<T> list) {
        return list.get(list.size() - 1);
    }

    public static <T> Iterable<T> skip(final Iterable<T> iterable, final int numberToSkip) {
        Preconditions.checkNotNull(iterable);
        Preconditions.checkArgument(numberToSkip >= 0, "number to skip cannot be negative");
        if (iterable instanceof List) {
            final List<T> list = (List) iterable;
            return new FluentIterable<T>() { // from class: com.google.common.collect.Iterables.7
                @Override // java.lang.Iterable
                public Iterator<T> iterator() {
                    int toSkip = Math.min(list.size(), numberToSkip);
                    return list.subList(toSkip, list.size()).iterator();
                }
            };
        }
        return new FluentIterable<T>() { // from class: com.google.common.collect.Iterables.8
            @Override // java.lang.Iterable
            public Iterator<T> iterator() {
                final Iterator<T> iterator = iterable.iterator();
                Iterators.advance(iterator, numberToSkip);
                return new Iterator<T>() { // from class: com.google.common.collect.Iterables.8.1
                    boolean atStart = true;

                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return iterator.hasNext();
                    }

                    @Override // java.util.Iterator
                    public T next() {
                        T t = (T) iterator.next();
                        this.atStart = false;
                        return t;
                    }

                    @Override // java.util.Iterator
                    public void remove() {
                        CollectPreconditions.checkRemove(!this.atStart);
                        iterator.remove();
                    }
                };
            }

            @Override // java.lang.Iterable
            public Spliterator<T> spliterator() {
                return Streams.stream(iterable).skip(numberToSkip).spliterator();
            }
        };
    }

    public static <T> Iterable<T> limit(final Iterable<T> iterable, final int limitSize) {
        Preconditions.checkNotNull(iterable);
        Preconditions.checkArgument(limitSize >= 0, "limit is negative");
        return new FluentIterable<T>() { // from class: com.google.common.collect.Iterables.9
            @Override // java.lang.Iterable
            public Iterator<T> iterator() {
                return Iterators.limit(iterable.iterator(), limitSize);
            }

            @Override // java.lang.Iterable
            public Spliterator<T> spliterator() {
                return Streams.stream(iterable).limit(limitSize).spliterator();
            }
        };
    }

    public static <T> Iterable<T> consumingIterable(final Iterable<T> iterable) {
        if (iterable instanceof Queue) {
            return new FluentIterable<T>() { // from class: com.google.common.collect.Iterables.10
                @Override // java.lang.Iterable
                public Iterator<T> iterator() {
                    return new ConsumingQueueIterator((Queue) iterable);
                }

                @Override // com.google.common.collect.FluentIterable
                public String toString() {
                    return "Iterables.consumingIterable(...)";
                }
            };
        }
        Preconditions.checkNotNull(iterable);
        return new FluentIterable<T>() { // from class: com.google.common.collect.Iterables.11
            @Override // java.lang.Iterable
            public Iterator<T> iterator() {
                return Iterators.consumingIterator(iterable.iterator());
            }

            @Override // com.google.common.collect.FluentIterable
            public String toString() {
                return "Iterables.consumingIterable(...)";
            }
        };
    }

    public static boolean isEmpty(Iterable<?> iterable) {
        if (iterable instanceof Collection) {
            return ((Collection) iterable).isEmpty();
        }
        return !iterable.iterator().hasNext();
    }

    public static <T> Iterable<T> mergeSorted(final Iterable<? extends Iterable<? extends T>> iterables, final Comparator<? super T> comparator) {
        Preconditions.checkNotNull(iterables, "iterables");
        Preconditions.checkNotNull(comparator, "comparator");
        Iterable<T> iterable = new FluentIterable<T>() { // from class: com.google.common.collect.Iterables.12
            @Override // java.lang.Iterable
            public Iterator<T> iterator() {
                return Iterators.mergeSorted(Iterables.transform(iterables, Iterables.toIterator()), comparator);
            }
        };
        return new UnmodifiableIterable(iterable, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> com.google.common.base.Function<Iterable<? extends T>, Iterator<? extends T>> toIterator() {
        return new com.google.common.base.Function<Iterable<? extends T>, Iterator<? extends T>>() { // from class: com.google.common.collect.Iterables.13
            @Override // com.google.common.base.Function, java.util.function.Function
            public Iterator<? extends T> apply(Iterable<? extends T> iterable) {
                return iterable.iterator();
            }
        };
    }
}
