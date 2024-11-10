package com.google.common.base;

import javax.annotation.Nullable;

@FunctionalInterface
/* loaded from: classes.dex */
public interface Function<F, T> extends java.util.function.Function<F, T> {
    @Override // java.util.function.Function
    @Nullable
    T apply(@Nullable F f);

    boolean equals(@Nullable Object obj);
}
