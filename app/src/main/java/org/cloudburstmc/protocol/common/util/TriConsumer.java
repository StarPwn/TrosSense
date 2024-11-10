package org.cloudburstmc.protocol.common.util;

import java.util.Objects;
import java.util.function.BiConsumer;

@FunctionalInterface
/* loaded from: classes5.dex */
public interface TriConsumer<T, U, R> {
    void accept(T t, U u, R r);

    default TriConsumer<T, U, R> andThen(final TriConsumer<? super T, ? super U, ? super R> after) {
        Objects.requireNonNull(after);
        return new TriConsumer() { // from class: org.cloudburstmc.protocol.common.util.TriConsumer$$ExternalSyntheticLambda0
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                TriConsumer.lambda$andThen$0(TriConsumer.this, after, obj, obj2, obj3);
            }
        };
    }

    static /* synthetic */ void lambda$andThen$0(TriConsumer _this, TriConsumer after, Object l, Object m, Object r) {
        _this.accept(l, m, r);
        after.accept(l, m, r);
    }

    static <T, U, R> TriConsumer<T, U, R> from(final BiConsumer<? super T, ? super R> consumer) {
        Objects.requireNonNull(consumer);
        return new TriConsumer() { // from class: org.cloudburstmc.protocol.common.util.TriConsumer$$ExternalSyntheticLambda1
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                consumer.accept(obj, obj3);
            }
        };
    }
}
