package com.google.common.base;

import javax.annotation.Nullable;

@FunctionalInterface
/* loaded from: classes.dex */
public interface Predicate<T> extends java.util.function.Predicate<T> {
    boolean apply(@Nullable T t);

    boolean equals(@Nullable Object obj);

    @Override // java.util.function.Predicate
    default boolean test(@Nullable T input) {
        return apply(input);
    }
}
