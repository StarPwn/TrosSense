package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Size64;
import java.util.Set;

/* loaded from: classes4.dex */
public interface ObjectSet<K> extends ObjectCollection<K>, Set<K> {
    @Override // it.unimi.dsi.fastutil.objects.ObjectCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectIterable
    ObjectIterator<K> iterator();

    @Override // it.unimi.dsi.fastutil.objects.ObjectCollection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectIterable
    /* renamed from: spliterator */
    default ObjectSpliterator<K> mo221spliterator() {
        return ObjectSpliterators.asSpliterator(iterator(), Size64.sizeOf(this), 65);
    }

    static <K> ObjectSet<K> of() {
        return ObjectSets.UNMODIFIABLE_EMPTY_SET;
    }

    static <K> ObjectSet<K> of(K e) {
        return ObjectSets.singleton(e);
    }

    static <K> ObjectSet<K> of(K e0, K e1) {
        ObjectArraySet<K> innerSet = new ObjectArraySet<>(2);
        innerSet.add(e0);
        if (!innerSet.add(e1)) {
            throw new IllegalArgumentException("Duplicate element: " + e1);
        }
        return ObjectSets.unmodifiable(innerSet);
    }

    static <K> ObjectSet<K> of(K e0, K e1, K e2) {
        ObjectArraySet<K> innerSet = new ObjectArraySet<>(3);
        innerSet.add(e0);
        if (!innerSet.add(e1)) {
            throw new IllegalArgumentException("Duplicate element: " + e1);
        }
        if (!innerSet.add(e2)) {
            throw new IllegalArgumentException("Duplicate element: " + e2);
        }
        return ObjectSets.unmodifiable(innerSet);
    }

    @SafeVarargs
    static <K> ObjectSet<K> of(K... a) {
        switch (a.length) {
            case 0:
                return of();
            case 1:
                return of((Object) a[0]);
            case 2:
                return of((Object) a[0], (Object) a[1]);
            case 3:
                return of((Object) a[0], (Object) a[1], (Object) a[2]);
            default:
                ObjectSet<K> innerSet = a.length <= 4 ? new ObjectArraySet<>(a.length) : new ObjectOpenHashSet<>(a.length);
                for (K element : a) {
                    if (!innerSet.add(element)) {
                        throw new IllegalArgumentException("Duplicate element: " + element);
                    }
                }
                return ObjectSets.unmodifiable(innerSet);
        }
    }
}
