package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.Stack;

/* loaded from: classes4.dex */
public interface LongStack extends Stack<Long> {
    long peekLong(int i);

    long popLong();

    void push(long j);

    long topLong();

    @Override // it.unimi.dsi.fastutil.Stack
    @Deprecated
    default void push(Long o) {
        push(o.longValue());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Stack
    @Deprecated
    default Long pop() {
        return Long.valueOf(popLong());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Stack
    @Deprecated
    default Long top() {
        return Long.valueOf(topLong());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Stack
    @Deprecated
    default Long peek(int i) {
        return Long.valueOf(peekLong(i));
    }
}
