package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Comparator;
import java.util.Iterator;

/* loaded from: classes.dex */
public final class Comparators {
    private Comparators() {
    }

    public static <T, S extends T> Comparator<Iterable<S>> lexicographical(Comparator<T> comparator) {
        return new LexicographicalOrdering((Comparator) Preconditions.checkNotNull(comparator));
    }

    public static <T> boolean isInOrder(Iterable<? extends T> iterable, Comparator<T> comparator) {
        Preconditions.checkNotNull(comparator);
        Iterator<? extends T> it2 = iterable.iterator();
        if (it2.hasNext()) {
            T prev = it2.next();
            while (it2.hasNext()) {
                T next = it2.next();
                if (comparator.compare(prev, next) > 0) {
                    return false;
                }
                prev = next;
            }
            return true;
        }
        return true;
    }

    public static <T> boolean isInStrictOrder(Iterable<? extends T> iterable, Comparator<T> comparator) {
        Preconditions.checkNotNull(comparator);
        Iterator<? extends T> it2 = iterable.iterator();
        if (it2.hasNext()) {
            T prev = it2.next();
            while (it2.hasNext()) {
                T next = it2.next();
                if (comparator.compare(prev, next) >= 0) {
                    return false;
                }
                prev = next;
            }
            return true;
        }
        return true;
    }
}
