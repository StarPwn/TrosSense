package com.google.common.collect;

import com.google.common.collect.ImmutableSet;
import java.util.Spliterator;
import java.util.Spliterators;
import javax.annotation.Nullable;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class RegularImmutableSet<E> extends ImmutableSet.Indexed<E> {
    static final RegularImmutableSet<Object> EMPTY = new RegularImmutableSet<>(ObjectArrays.EMPTY_ARRAY, 0, null, 0);
    private final transient Object[] elements;
    private final transient int hashCode;
    private final transient int mask;
    final transient Object[] table;

    /* JADX INFO: Access modifiers changed from: package-private */
    public RegularImmutableSet(Object[] elements, int hashCode, Object[] table, int mask) {
        this.elements = elements;
        this.table = table;
        this.mask = mask;
        this.hashCode = hashCode;
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(@Nullable Object target) {
        Object[] table = this.table;
        if (target == null || table == null) {
            return false;
        }
        int i = Hashing.smearedHash(target);
        while (true) {
            int i2 = i & this.mask;
            Object candidate = table[i2];
            if (candidate == null) {
                return false;
            }
            if (!candidate.equals(target)) {
                i = i2 + 1;
            } else {
                return true;
            }
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.elements.length;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableSet.Indexed
    public E get(int i) {
        return (E) this.elements[i];
    }

    @Override // com.google.common.collect.ImmutableSet.Indexed, com.google.common.collect.ImmutableCollection, java.util.Collection, java.lang.Iterable
    public Spliterator<E> spliterator() {
        return Spliterators.spliterator(this.elements, 1297);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableCollection
    public int copyIntoArray(Object[] dst, int offset) {
        System.arraycopy(this.elements, 0, dst, offset, this.elements.length);
        return this.elements.length + offset;
    }

    @Override // com.google.common.collect.ImmutableSet.Indexed, com.google.common.collect.ImmutableSet
    ImmutableList<E> createAsList() {
        return this.table == null ? ImmutableList.of() : new RegularImmutableAsList(this, this.elements);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableCollection
    public boolean isPartialView() {
        return false;
    }

    @Override // com.google.common.collect.ImmutableSet, java.util.Collection, java.util.Set
    public int hashCode() {
        return this.hashCode;
    }

    @Override // com.google.common.collect.ImmutableSet
    boolean isHashCodeFast() {
        return true;
    }
}
