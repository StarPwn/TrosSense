package com.google.common.collect;

import java.io.Serializable;
import java.util.Comparator;

/* loaded from: classes.dex */
final class CompoundOrdering<T> extends Ordering<T> implements Serializable {
    private static final long serialVersionUID = 0;
    final ImmutableList<Comparator<? super T>> comparators;

    /* JADX INFO: Access modifiers changed from: package-private */
    public CompoundOrdering(Comparator<? super T> primary, Comparator<? super T> secondary) {
        this.comparators = ImmutableList.of(primary, secondary);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CompoundOrdering(Iterable<? extends Comparator<? super T>> comparators) {
        this.comparators = ImmutableList.copyOf(comparators);
    }

    @Override // com.google.common.collect.Ordering, java.util.Comparator
    public int compare(T left, T right) {
        int size = this.comparators.size();
        for (int i = 0; i < size; i++) {
            int result = this.comparators.get(i).compare(left, right);
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }

    @Override // java.util.Comparator
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof CompoundOrdering) {
            CompoundOrdering<?> that = (CompoundOrdering) object;
            return this.comparators.equals(that.comparators);
        }
        return false;
    }

    public int hashCode() {
        return this.comparators.hashCode();
    }

    public String toString() {
        return "Ordering.compound(" + this.comparators + ")";
    }
}
