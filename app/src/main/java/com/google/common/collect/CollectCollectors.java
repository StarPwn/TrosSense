package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedSet;
import java.util.Comparator;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/* loaded from: classes.dex */
final class CollectCollectors {
    CollectCollectors() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T, K, V> Collector<T, ?, ImmutableBiMap<K, V>> toImmutableBiMap(final Function<? super T, ? extends K> keyFunction, final Function<? super T, ? extends V> valueFunction) {
        Preconditions.checkNotNull(keyFunction);
        Preconditions.checkNotNull(valueFunction);
        return Collector.of(new Supplier() { // from class: com.google.common.collect.CollectCollectors$$ExternalSyntheticLambda22
            @Override // java.util.function.Supplier
            public final Object get() {
                return new ImmutableBiMap.Builder();
            }
        }, new BiConsumer() { // from class: com.google.common.collect.CollectCollectors$$ExternalSyntheticLambda23
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((ImmutableBiMap.Builder) obj).put((ImmutableBiMap.Builder) keyFunction.apply(obj2), (ImmutableBiMap.Builder) valueFunction.apply(obj2));
            }
        }, new BinaryOperator() { // from class: com.google.common.collect.CollectCollectors$$ExternalSyntheticLambda1
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((ImmutableBiMap.Builder) obj).combine((ImmutableMap.Builder) obj2);
            }
        }, new Function() { // from class: com.google.common.collect.CollectCollectors$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ((ImmutableBiMap.Builder) obj).build();
            }
        }, new Collector.Characteristics[0]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <E> Collector<E, ?, ImmutableList<E>> toImmutableList() {
        return Collector.of(new Supplier() { // from class: com.google.common.collect.CollectCollectors$$ExternalSyntheticLambda0
            @Override // java.util.function.Supplier
            public final Object get() {
                return ImmutableList.builder();
            }
        }, new BiConsumer() { // from class: com.google.common.collect.CollectCollectors$$ExternalSyntheticLambda11
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((ImmutableList.Builder) obj).add((ImmutableList.Builder) obj2);
            }
        }, new BinaryOperator() { // from class: com.google.common.collect.CollectCollectors$$ExternalSyntheticLambda16
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((ImmutableList.Builder) obj).combine((ImmutableCollection.ArrayBasedBuilder) obj2);
            }
        }, new Function() { // from class: com.google.common.collect.CollectCollectors$$ExternalSyntheticLambda17
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ((ImmutableList.Builder) obj).build();
            }
        }, new Collector.Characteristics[0]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(final Function<? super T, ? extends K> keyFunction, final Function<? super T, ? extends V> valueFunction) {
        Preconditions.checkNotNull(keyFunction);
        Preconditions.checkNotNull(valueFunction);
        return Collector.of(new Supplier() { // from class: com.google.common.collect.CollectCollectors$$ExternalSyntheticLambda3
            @Override // java.util.function.Supplier
            public final Object get() {
                return new ImmutableMap.Builder();
            }
        }, new BiConsumer() { // from class: com.google.common.collect.CollectCollectors$$ExternalSyntheticLambda4
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((ImmutableMap.Builder) obj).put(keyFunction.apply(obj2), valueFunction.apply(obj2));
            }
        }, new BinaryOperator() { // from class: com.google.common.collect.CollectCollectors$$ExternalSyntheticLambda5
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((ImmutableMap.Builder) obj).combine((ImmutableMap.Builder) obj2);
            }
        }, new Function() { // from class: com.google.common.collect.CollectCollectors$$ExternalSyntheticLambda6
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ((ImmutableMap.Builder) obj).build();
            }
        }, new Collector.Characteristics[0]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <E> Collector<E, ?, ImmutableSet<E>> toImmutableSet() {
        return Collector.of(new Supplier() { // from class: com.google.common.collect.CollectCollectors$$ExternalSyntheticLambda18
            @Override // java.util.function.Supplier
            public final Object get() {
                return ImmutableSet.builder();
            }
        }, new BiConsumer() { // from class: com.google.common.collect.CollectCollectors$$ExternalSyntheticLambda19
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((ImmutableSet.Builder) obj).add((ImmutableSet.Builder) obj2);
            }
        }, new BinaryOperator() { // from class: com.google.common.collect.CollectCollectors$$ExternalSyntheticLambda20
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((ImmutableSet.Builder) obj).combine((ImmutableCollection.ArrayBasedBuilder) obj2);
            }
        }, new Function() { // from class: com.google.common.collect.CollectCollectors$$ExternalSyntheticLambda21
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ((ImmutableSet.Builder) obj).build();
            }
        }, new Collector.Characteristics[0]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T, K, V> Collector<T, ?, ImmutableSortedMap<K, V>> toImmutableSortedMap(final Comparator<? super K> comparator, final Function<? super T, ? extends K> keyFunction, final Function<? super T, ? extends V> valueFunction) {
        Preconditions.checkNotNull(comparator);
        Preconditions.checkNotNull(keyFunction);
        Preconditions.checkNotNull(valueFunction);
        return Collector.of(new Supplier() { // from class: com.google.common.collect.CollectCollectors$$ExternalSyntheticLambda7
            @Override // java.util.function.Supplier
            public final Object get() {
                return CollectCollectors.lambda$toImmutableSortedMap$2(comparator);
            }
        }, new BiConsumer() { // from class: com.google.common.collect.CollectCollectors$$ExternalSyntheticLambda8
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((ImmutableSortedMap.Builder) obj).put((ImmutableSortedMap.Builder) keyFunction.apply(obj2), (ImmutableSortedMap.Builder) valueFunction.apply(obj2));
            }
        }, new BinaryOperator() { // from class: com.google.common.collect.CollectCollectors$$ExternalSyntheticLambda9
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((ImmutableSortedMap.Builder) obj).combine((ImmutableMap.Builder) obj2);
            }
        }, new Function() { // from class: com.google.common.collect.CollectCollectors$$ExternalSyntheticLambda10
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ((ImmutableSortedMap.Builder) obj).build();
            }
        }, Collector.Characteristics.UNORDERED);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ ImmutableSortedMap.Builder lambda$toImmutableSortedMap$2(Comparator comparator) {
        return new ImmutableSortedMap.Builder(comparator);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <E> Collector<E, ?, ImmutableSortedSet<E>> toImmutableSortedSet(final Comparator<? super E> comparator) {
        Preconditions.checkNotNull(comparator);
        return Collector.of(new Supplier() { // from class: com.google.common.collect.CollectCollectors$$ExternalSyntheticLambda12
            @Override // java.util.function.Supplier
            public final Object get() {
                return CollectCollectors.lambda$toImmutableSortedSet$4(comparator);
            }
        }, new BiConsumer() { // from class: com.google.common.collect.CollectCollectors$$ExternalSyntheticLambda13
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((ImmutableSortedSet.Builder) obj).add((ImmutableSortedSet.Builder) obj2);
            }
        }, new BinaryOperator() { // from class: com.google.common.collect.CollectCollectors$$ExternalSyntheticLambda14
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((ImmutableSortedSet.Builder) obj).combine((ImmutableCollection.ArrayBasedBuilder) obj2);
            }
        }, new Function() { // from class: com.google.common.collect.CollectCollectors$$ExternalSyntheticLambda15
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ((ImmutableSortedSet.Builder) obj).build();
            }
        }, new Collector.Characteristics[0]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ ImmutableSortedSet.Builder lambda$toImmutableSortedSet$4(Comparator comparator) {
        return new ImmutableSortedSet.Builder(comparator);
    }
}
