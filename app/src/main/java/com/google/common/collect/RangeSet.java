package com.google.common.collect;

import java.lang.Comparable;
import java.util.Set;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public interface RangeSet<C extends Comparable> {
    void add(Range<C> range);

    void addAll(RangeSet<C> rangeSet);

    Set<Range<C>> asDescendingSetOfRanges();

    Set<Range<C>> asRanges();

    void clear();

    RangeSet<C> complement();

    boolean contains(C c);

    boolean encloses(Range<C> range);

    boolean enclosesAll(RangeSet<C> rangeSet);

    boolean equals(@Nullable Object obj);

    int hashCode();

    boolean intersects(Range<C> range);

    boolean isEmpty();

    Range<C> rangeContaining(C c);

    void remove(Range<C> range);

    void removeAll(RangeSet<C> rangeSet);

    Range<C> span();

    RangeSet<C> subRangeSet(Range<C> range);

    String toString();

    default boolean enclosesAll(Iterable<Range<C>> other) {
        for (Range<C> range : other) {
            if (!encloses(range)) {
                return false;
            }
        }
        return true;
    }

    default void addAll(Iterable<Range<C>> ranges) {
        for (Range<C> range : ranges) {
            add(range);
        }
    }

    default void removeAll(Iterable<Range<C>> ranges) {
        for (Range<C> range : ranges) {
            remove(range);
        }
    }
}
