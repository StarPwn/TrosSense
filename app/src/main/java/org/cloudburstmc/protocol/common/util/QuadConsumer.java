package org.cloudburstmc.protocol.common.util;

@FunctionalInterface
/* loaded from: classes5.dex */
public interface QuadConsumer<T, U, V, R> {
    void accept(T t, U u, V v, R r);
}
