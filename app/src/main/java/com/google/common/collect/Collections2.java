package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.math.IntMath;
import com.google.common.math.LongMath;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public final class Collections2 {
    static final Joiner STANDARD_JOINER = Joiner.on(", ").useForNull("null");

    private Collections2() {
    }

    public static <E> Collection<E> filter(Collection<E> unfiltered, Predicate<? super E> predicate) {
        if (unfiltered instanceof FilteredCollection) {
            return ((FilteredCollection) unfiltered).createCombined(predicate);
        }
        return new FilteredCollection((Collection) Preconditions.checkNotNull(unfiltered), (Predicate) Preconditions.checkNotNull(predicate));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean safeContains(Collection<?> collection, @Nullable Object object) {
        Preconditions.checkNotNull(collection);
        try {
            return collection.contains(object);
        } catch (ClassCastException e) {
            return false;
        } catch (NullPointerException e2) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean safeRemove(Collection<?> collection, @Nullable Object object) {
        Preconditions.checkNotNull(collection);
        try {
            return collection.remove(object);
        } catch (ClassCastException e) {
            return false;
        } catch (NullPointerException e2) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class FilteredCollection<E> extends AbstractCollection<E> {
        final Predicate<? super E> predicate;
        final Collection<E> unfiltered;

        /* JADX INFO: Access modifiers changed from: package-private */
        public FilteredCollection(Collection<E> unfiltered, Predicate<? super E> predicate) {
            this.unfiltered = unfiltered;
            this.predicate = predicate;
        }

        FilteredCollection<E> createCombined(Predicate<? super E> newPredicate) {
            return new FilteredCollection<>(this.unfiltered, Predicates.and(this.predicate, newPredicate));
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean add(E element) {
            Preconditions.checkArgument(this.predicate.apply(element));
            return this.unfiltered.add(element);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean addAll(Collection<? extends E> collection) {
            for (E element : collection) {
                Preconditions.checkArgument(this.predicate.apply(element));
            }
            return this.unfiltered.addAll(collection);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public void clear() {
            Iterables.removeIf(this.unfiltered, this.predicate);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean contains(@Nullable Object element) {
            if (Collections2.safeContains(this.unfiltered, element)) {
                return this.predicate.apply(element);
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean containsAll(Collection<?> collection) {
            return Collections2.containsAllImpl(this, collection);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return !Iterables.any(this.unfiltered, this.predicate);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<E> iterator() {
            return Iterators.filter(this.unfiltered.iterator(), this.predicate);
        }

        @Override // java.util.Collection, java.lang.Iterable
        public Spliterator<E> spliterator() {
            return CollectSpliterators.filter(this.unfiltered.spliterator(), this.predicate);
        }

        @Override // java.lang.Iterable
        public void forEach(final Consumer<? super E> action) {
            Preconditions.checkNotNull(action);
            this.unfiltered.forEach(new Consumer() { // from class: com.google.common.collect.Collections2$FilteredCollection$$ExternalSyntheticLambda2
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    Collections2.FilteredCollection.this.m156xea94214(action, obj);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$forEach$0$com-google-common-collect-Collections2$FilteredCollection, reason: not valid java name */
        public /* synthetic */ void m156xea94214(Consumer action, Object e) {
            if (this.predicate.test(e)) {
                action.accept(e);
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean remove(Object element) {
            return contains(element) && this.unfiltered.remove(element);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean removeAll(final Collection<?> collection) {
            collection.getClass();
            return removeIf(new java.util.function.Predicate() { // from class: com.google.common.collect.Collections2$FilteredCollection$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return collection.contains(obj);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static /* synthetic */ boolean lambda$retainAll$1(Collection collection, Object element) {
            return !collection.contains(element);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean retainAll(final Collection<?> collection) {
            return removeIf(new java.util.function.Predicate() { // from class: com.google.common.collect.Collections2$FilteredCollection$$ExternalSyntheticLambda1
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return Collections2.FilteredCollection.lambda$retainAll$1(collection, obj);
                }
            });
        }

        @Override // java.util.Collection
        public boolean removeIf(final java.util.function.Predicate<? super E> filter) {
            Preconditions.checkNotNull(filter);
            return this.unfiltered.removeIf(new java.util.function.Predicate() { // from class: com.google.common.collect.Collections2$FilteredCollection$$ExternalSyntheticLambda3
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return Collections2.FilteredCollection.this.m157xbd057a87(filter, obj);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$removeIf$2$com-google-common-collect-Collections2$FilteredCollection, reason: not valid java name */
        public /* synthetic */ boolean m157xbd057a87(java.util.function.Predicate filter, Object element) {
            return this.predicate.apply(element) && filter.test(element);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return Iterators.size(iterator());
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public Object[] toArray() {
            return Lists.newArrayList(iterator()).toArray();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            return (T[]) Lists.newArrayList(iterator()).toArray(tArr);
        }
    }

    public static <F, T> Collection<T> transform(Collection<F> fromCollection, Function<? super F, T> function) {
        return new TransformedCollection(fromCollection, function);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class TransformedCollection<F, T> extends AbstractCollection<T> {
        final Collection<F> fromCollection;
        final Function<? super F, ? extends T> function;

        TransformedCollection(Collection<F> fromCollection, Function<? super F, ? extends T> function) {
            this.fromCollection = (Collection) Preconditions.checkNotNull(fromCollection);
            this.function = (Function) Preconditions.checkNotNull(function);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public void clear() {
            this.fromCollection.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return this.fromCollection.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<T> iterator() {
            return Iterators.transform(this.fromCollection.iterator(), this.function);
        }

        @Override // java.util.Collection, java.lang.Iterable
        public Spliterator<T> spliterator() {
            return CollectSpliterators.map(this.fromCollection.spliterator(), this.function);
        }

        @Override // java.lang.Iterable
        public void forEach(final Consumer<? super T> action) {
            Preconditions.checkNotNull(action);
            this.fromCollection.forEach(new Consumer() { // from class: com.google.common.collect.Collections2$TransformedCollection$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    Collections2.TransformedCollection.this.m158x9cac3aa(action, obj);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$forEach$0$com-google-common-collect-Collections2$TransformedCollection, reason: not valid java name */
        public /* synthetic */ void m158x9cac3aa(Consumer action, Object f) {
            action.accept(this.function.apply(f));
        }

        @Override // java.util.Collection
        public boolean removeIf(final java.util.function.Predicate<? super T> filter) {
            Preconditions.checkNotNull(filter);
            return this.fromCollection.removeIf(new java.util.function.Predicate() { // from class: com.google.common.collect.Collections2$TransformedCollection$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return Collections2.TransformedCollection.this.m159xf35962b8(filter, obj);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$removeIf$1$com-google-common-collect-Collections2$TransformedCollection, reason: not valid java name */
        public /* synthetic */ boolean m159xf35962b8(java.util.function.Predicate filter, Object element) {
            return filter.test(this.function.apply(element));
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return this.fromCollection.size();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean containsAllImpl(Collection<?> self, Collection<?> c) {
        return Iterables.all(c, Predicates.in(self));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String toStringImpl(final Collection<?> collection) {
        StringBuilder sb = newStringBuilderForCollection(collection.size()).append('[');
        STANDARD_JOINER.appendTo(sb, Iterables.transform(collection, new Function<Object, Object>() { // from class: com.google.common.collect.Collections2.1
            @Override // com.google.common.base.Function, java.util.function.Function
            public Object apply(Object input) {
                return input == collection ? "(this Collection)" : input;
            }
        }));
        return sb.append(']').toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static StringBuilder newStringBuilderForCollection(int size) {
        CollectPreconditions.checkNonnegative(size, "size");
        return new StringBuilder((int) Math.min(size * 8, 1073741824L));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> Collection<T> cast(Iterable<T> iterable) {
        return (Collection) iterable;
    }

    public static <E extends Comparable<? super E>> Collection<List<E>> orderedPermutations(Iterable<E> elements) {
        return orderedPermutations(elements, Ordering.natural());
    }

    public static <E> Collection<List<E>> orderedPermutations(Iterable<E> elements, Comparator<? super E> comparator) {
        return new OrderedPermutationCollection(elements, comparator);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class OrderedPermutationCollection<E> extends AbstractCollection<List<E>> {
        final Comparator<? super E> comparator;
        final ImmutableList<E> inputList;
        final int size;

        OrderedPermutationCollection(Iterable<E> input, Comparator<? super E> comparator) {
            this.inputList = Ordering.from(comparator).immutableSortedCopy(input);
            this.comparator = comparator;
            this.size = calculateSize(this.inputList, comparator);
        }

        private static <E> int calculateSize(List<E> sortedInputList, Comparator<? super E> comparator) {
            long permutations = 1;
            int n = 1;
            int r = 1;
            while (n < sortedInputList.size()) {
                int comparison = comparator.compare(sortedInputList.get(n - 1), sortedInputList.get(n));
                if (comparison < 0) {
                    permutations *= LongMath.binomial(n, r);
                    r = 0;
                    if (!Collections2.isPositiveInt(permutations)) {
                        return Integer.MAX_VALUE;
                    }
                }
                n++;
                r++;
            }
            long permutations2 = permutations * LongMath.binomial(n, r);
            if (Collections2.isPositiveInt(permutations2)) {
                return (int) permutations2;
            }
            return Integer.MAX_VALUE;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return this.size;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<List<E>> iterator() {
            return new OrderedPermutationIterator(this.inputList, this.comparator);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean contains(@Nullable Object obj) {
            if (obj instanceof List) {
                List<?> list = (List) obj;
                return Collections2.isPermutation(this.inputList, list);
            }
            return false;
        }

        @Override // java.util.AbstractCollection
        public String toString() {
            return "orderedPermutationCollection(" + this.inputList + ")";
        }
    }

    /* loaded from: classes.dex */
    private static final class OrderedPermutationIterator<E> extends AbstractIterator<List<E>> {
        final Comparator<? super E> comparator;
        List<E> nextPermutation;

        OrderedPermutationIterator(List<E> list, Comparator<? super E> comparator) {
            this.nextPermutation = Lists.newArrayList(list);
            this.comparator = comparator;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.collect.AbstractIterator
        public List<E> computeNext() {
            if (this.nextPermutation == null) {
                return endOfData();
            }
            ImmutableList<E> next = ImmutableList.copyOf((Collection) this.nextPermutation);
            calculateNextPermutation();
            return next;
        }

        void calculateNextPermutation() {
            int j = findNextJ();
            if (j == -1) {
                this.nextPermutation = null;
                return;
            }
            int l = findNextL(j);
            Collections.swap(this.nextPermutation, j, l);
            int n = this.nextPermutation.size();
            Collections.reverse(this.nextPermutation.subList(j + 1, n));
        }

        int findNextJ() {
            for (int size = this.nextPermutation.size() - 2; size >= 0; size--) {
                if (this.comparator.compare(this.nextPermutation.get(size), this.nextPermutation.get(size + 1)) < 0) {
                    return size;
                }
            }
            return -1;
        }

        int findNextL(int i) {
            E e = this.nextPermutation.get(i);
            for (int size = this.nextPermutation.size() - 1; size > i; size--) {
                if (this.comparator.compare(e, this.nextPermutation.get(size)) < 0) {
                    return size;
                }
            }
            throw new AssertionError("this statement should be unreachable");
        }
    }

    public static <E> Collection<List<E>> permutations(Collection<E> elements) {
        return new PermutationCollection(ImmutableList.copyOf((Collection) elements));
    }

    /* loaded from: classes.dex */
    private static final class PermutationCollection<E> extends AbstractCollection<List<E>> {
        final ImmutableList<E> inputList;

        PermutationCollection(ImmutableList<E> input) {
            this.inputList = input;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return IntMath.factorial(this.inputList.size());
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<List<E>> iterator() {
            return new PermutationIterator(this.inputList);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean contains(@Nullable Object obj) {
            if (obj instanceof List) {
                List<?> list = (List) obj;
                return Collections2.isPermutation(this.inputList, list);
            }
            return false;
        }

        @Override // java.util.AbstractCollection
        public String toString() {
            return "permutations(" + this.inputList + ")";
        }
    }

    /* loaded from: classes.dex */
    private static class PermutationIterator<E> extends AbstractIterator<List<E>> {
        final int[] c;
        int j;
        final List<E> list;
        final int[] o;

        PermutationIterator(List<E> list) {
            this.list = new ArrayList(list);
            int n = list.size();
            this.c = new int[n];
            this.o = new int[n];
            Arrays.fill(this.c, 0);
            Arrays.fill(this.o, 1);
            this.j = Integer.MAX_VALUE;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.collect.AbstractIterator
        public List<E> computeNext() {
            if (this.j <= 0) {
                return endOfData();
            }
            ImmutableList<E> next = ImmutableList.copyOf((Collection) this.list);
            calculateNextPermutation();
            return next;
        }

        void calculateNextPermutation() {
            this.j = this.list.size() - 1;
            int s = 0;
            if (this.j == -1) {
                return;
            }
            while (true) {
                int q = this.c[this.j] + this.o[this.j];
                if (q < 0) {
                    switchDirection();
                } else if (q == this.j + 1) {
                    if (this.j != 0) {
                        s++;
                        switchDirection();
                    } else {
                        return;
                    }
                } else {
                    Collections.swap(this.list, (this.j - this.c[this.j]) + s, (this.j - q) + s);
                    this.c[this.j] = q;
                    return;
                }
            }
        }

        void switchDirection() {
            this.o[this.j] = -this.o[this.j];
            this.j--;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isPermutation(List<?> first, List<?> second) {
        if (first.size() != second.size()) {
            return false;
        }
        Multiset<?> firstMultiset = HashMultiset.create(first);
        Multiset<?> secondMultiset = HashMultiset.create(second);
        return firstMultiset.equals(secondMultiset);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isPositiveInt(long n) {
        return n >= 0 && n <= 2147483647L;
    }
}
