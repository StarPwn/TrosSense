package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Stack;

/* loaded from: classes4.dex */
public interface IntStack extends Stack<Integer> {
    int peekInt(int i);

    int popInt();

    void push(int i);

    int topInt();

    @Override // it.unimi.dsi.fastutil.Stack
    @Deprecated
    default void push(Integer o) {
        push(o.intValue());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Stack
    @Deprecated
    default Integer pop() {
        return Integer.valueOf(popInt());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Stack
    @Deprecated
    default Integer top() {
        return Integer.valueOf(topInt());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Stack
    @Deprecated
    default Integer peek(int i) {
        return Integer.valueOf(peekInt(i));
    }
}
