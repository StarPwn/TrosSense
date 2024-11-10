package com.google.common.base;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public abstract class Optional<T> implements Serializable {
    private static final long serialVersionUID = 0;

    public abstract Set<T> asSet();

    public abstract boolean equals(@Nullable Object obj);

    public abstract T get();

    public abstract int hashCode();

    public abstract boolean isPresent();

    public abstract Optional<T> or(Optional<? extends T> optional);

    public abstract T or(Supplier<? extends T> supplier);

    public abstract T or(T t);

    @Nullable
    public abstract T orNull();

    public abstract String toString();

    public abstract <V> Optional<V> transform(Function<? super T, V> function);

    public static <T> Optional<T> absent() {
        return Absent.withType();
    }

    public static <T> Optional<T> of(T reference) {
        return new Present(Preconditions.checkNotNull(reference));
    }

    public static <T> Optional<T> fromNullable(@Nullable T nullableReference) {
        return nullableReference == null ? absent() : new Present(nullableReference);
    }

    @Nullable
    public static <T> Optional<T> fromJavaUtil(@Nullable java.util.Optional<T> javaUtilOptional) {
        if (javaUtilOptional == null) {
            return null;
        }
        return fromNullable(javaUtilOptional.orElse(null));
    }

    @Nullable
    public static <T> java.util.Optional<T> toJavaUtil(@Nullable Optional<T> googleOptional) {
        if (googleOptional == null) {
            return null;
        }
        return googleOptional.toJavaUtil();
    }

    public java.util.Optional<T> toJavaUtil() {
        return java.util.Optional.ofNullable(orNull());
    }

    public static <T> Iterable<T> presentInstances(final Iterable<? extends Optional<? extends T>> optionals) {
        Preconditions.checkNotNull(optionals);
        return new Iterable<T>() { // from class: com.google.common.base.Optional.1
            @Override // java.lang.Iterable
            public Iterator<T> iterator() {
                return new AbstractIterator<T>() { // from class: com.google.common.base.Optional.1.1
                    private final Iterator<? extends Optional<? extends T>> iterator;

                    {
                        this.iterator = (Iterator) Preconditions.checkNotNull(optionals.iterator());
                    }

                    @Override // com.google.common.base.AbstractIterator
                    protected T computeNext() {
                        while (this.iterator.hasNext()) {
                            Optional<? extends T> optional = this.iterator.next();
                            if (optional.isPresent()) {
                                return optional.get();
                            }
                        }
                        return endOfData();
                    }
                };
            }
        };
    }
}
