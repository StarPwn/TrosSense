package io.netty.channel;

import io.netty.util.IntSupplier;

/* loaded from: classes4.dex */
final class DefaultSelectStrategy implements SelectStrategy {
    static final SelectStrategy INSTANCE = new DefaultSelectStrategy();

    private DefaultSelectStrategy() {
    }

    @Override // io.netty.channel.SelectStrategy
    public int calculateStrategy(IntSupplier selectSupplier, boolean hasTasks) throws Exception {
        if (hasTasks) {
            return selectSupplier.get();
        }
        return -1;
    }
}
