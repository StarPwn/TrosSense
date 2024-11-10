package com.google.common.graph;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import java.util.Comparator;
import java.util.Map;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public final class ElementOrder<T> {

    @Nullable
    private final Comparator<T> comparator;
    private final Type type;

    /* loaded from: classes.dex */
    public enum Type {
        UNORDERED,
        INSERTION,
        SORTED
    }

    private ElementOrder(Type type, @Nullable Comparator<T> comparator) {
        this.type = (Type) Preconditions.checkNotNull(type);
        this.comparator = comparator;
        Preconditions.checkState((type == Type.SORTED) == (comparator != null));
    }

    public static <S> ElementOrder<S> unordered() {
        return new ElementOrder<>(Type.UNORDERED, null);
    }

    public static <S> ElementOrder<S> insertion() {
        return new ElementOrder<>(Type.INSERTION, null);
    }

    public static <S extends Comparable<? super S>> ElementOrder<S> natural() {
        return new ElementOrder<>(Type.SORTED, Ordering.natural());
    }

    public static <S> ElementOrder<S> sorted(Comparator<S> comparator) {
        return new ElementOrder<>(Type.SORTED, comparator);
    }

    public Type type() {
        return this.type;
    }

    public Comparator<T> comparator() {
        if (this.comparator != null) {
            return this.comparator;
        }
        throw new UnsupportedOperationException("This ordering does not define a comparator.");
    }

    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ElementOrder)) {
            return false;
        }
        ElementOrder<?> other = (ElementOrder) obj;
        return this.type == other.type && Objects.equal(this.comparator, other.comparator);
    }

    public int hashCode() {
        return Objects.hashCode(this.type, this.comparator);
    }

    public String toString() {
        MoreObjects.ToStringHelper helper = MoreObjects.toStringHelper(this).add("type", this.type);
        if (this.comparator != null) {
            helper.add("comparator", this.comparator);
        }
        return helper.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public <K extends T, V> Map<K, V> createMap(int expectedSize) {
        switch (this.type) {
            case UNORDERED:
                return Maps.newHashMapWithExpectedSize(expectedSize);
            case INSERTION:
                return Maps.newLinkedHashMapWithExpectedSize(expectedSize);
            case SORTED:
                return Maps.newTreeMap(comparator());
            default:
                throw new AssertionError();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    public <T1 extends T> ElementOrder<T1> cast() {
        return this;
    }
}
