package it.unimi.dsi.fastutil;

/* loaded from: classes4.dex */
public interface Stack<K> {
    boolean isEmpty();

    K pop();

    void push(K k);

    default K top() {
        return peek(0);
    }

    default K peek(int i) {
        throw new UnsupportedOperationException();
    }
}
