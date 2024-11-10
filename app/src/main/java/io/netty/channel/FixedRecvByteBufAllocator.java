package io.netty.channel;

import io.netty.channel.DefaultMaxMessagesRecvByteBufAllocator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public class FixedRecvByteBufAllocator extends DefaultMaxMessagesRecvByteBufAllocator {
    private final int bufferSize;

    /* loaded from: classes4.dex */
    private final class HandleImpl extends DefaultMaxMessagesRecvByteBufAllocator.MaxMessageHandle {
        private final int bufferSize;

        HandleImpl(int bufferSize) {
            super();
            this.bufferSize = bufferSize;
        }

        @Override // io.netty.channel.RecvByteBufAllocator.Handle
        public int guess() {
            return this.bufferSize;
        }
    }

    public FixedRecvByteBufAllocator(int bufferSize) {
        ObjectUtil.checkPositive(bufferSize, "bufferSize");
        this.bufferSize = bufferSize;
    }

    @Override // io.netty.channel.RecvByteBufAllocator
    public RecvByteBufAllocator.Handle newHandle() {
        return new HandleImpl(this.bufferSize);
    }

    @Override // io.netty.channel.DefaultMaxMessagesRecvByteBufAllocator
    public FixedRecvByteBufAllocator respectMaybeMoreData(boolean respectMaybeMoreData) {
        super.respectMaybeMoreData(respectMaybeMoreData);
        return this;
    }
}
