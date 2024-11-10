package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.Multiset;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.ObjIntConsumer;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public interface Multiset<E> extends Collection<E> {

    /* loaded from: classes.dex */
    public interface Entry<E> {
        boolean equals(Object obj);

        int getCount();

        E getElement();

        int hashCode();

        String toString();
    }

    int add(@Nullable E e, int i);

    boolean add(E e);

    boolean contains(@Nullable Object obj);

    @Override // java.util.Collection
    boolean containsAll(Collection<?> collection);

    int count(@Nullable Object obj);

    Set<E> elementSet();

    Set<Entry<E>> entrySet();

    boolean equals(@Nullable Object obj);

    int hashCode();

    Iterator<E> iterator();

    int remove(@Nullable Object obj, int i);

    boolean remove(@Nullable Object obj);

    boolean removeAll(Collection<?> collection);

    boolean retainAll(Collection<?> collection);

    int setCount(E e, int i);

    boolean setCount(E e, int i, int i2);

    int size();

    String toString();

    default void forEachEntry(final ObjIntConsumer<? super E> action) {
        Preconditions.checkNotNull(action);
        entrySet().forEach(new Consumer() { // from class: com.google.common.collect.Multiset$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                action.accept(r2.getElement(), ((Multiset.Entry) obj).getCount());
            }
        });
    }

    default void forEach(final Consumer<? super E> action) {
        Preconditions.checkNotNull(action);
        entrySet().forEach(new Consumer() { // from class: com.google.common.collect.Multiset$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                Multiset.lambda$forEach$1(action, (Multiset.Entry) obj);
            }
        });
    }

    static /* synthetic */ void lambda$forEach$1(Consumer action, Entry entry) {
        Object element = entry.getElement();
        int count = entry.getCount();
        for (int i = 0; i < count; i++) {
            action.accept(element);
        }
    }

    default Spliterator<E> spliterator() {
        return Multisets.spliteratorImpl(this);
    }
}
