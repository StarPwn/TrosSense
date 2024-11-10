package io.netty.channel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.UncheckedBooleanSupplier;
import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public interface RecvByteBufAllocator {

    /* loaded from: classes4.dex */
    public interface ExtendedHandle extends Handle {
        boolean continueReading(UncheckedBooleanSupplier uncheckedBooleanSupplier);
    }

    @Deprecated
    /* loaded from: classes4.dex */
    public interface Handle {
        ByteBuf allocate(ByteBufAllocator byteBufAllocator);

        int attemptedBytesRead();

        void attemptedBytesRead(int i);

        boolean continueReading();

        int guess();

        void incMessagesRead(int i);

        int lastBytesRead();

        void lastBytesRead(int i);

        void readComplete();

        void reset(ChannelConfig channelConfig);
    }

    Handle newHandle();

    /* loaded from: classes4.dex */
    public static class DelegatingHandle implements Handle {
        private final Handle delegate;

        public DelegatingHandle(Handle delegate) {
            this.delegate = (Handle) ObjectUtil.checkNotNull(delegate, "delegate");
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public final Handle delegate() {
            return this.delegate;
        }

        @Override // io.netty.channel.RecvByteBufAllocator.Handle
        public ByteBuf allocate(ByteBufAllocator alloc) {
            return this.delegate.allocate(alloc);
        }

        @Override // io.netty.channel.RecvByteBufAllocator.Handle
        public int guess() {
            return this.delegate.guess();
        }

        @Override // io.netty.channel.RecvByteBufAllocator.Handle
        public void reset(ChannelConfig config) {
            this.delegate.reset(config);
        }

        @Override // io.netty.channel.RecvByteBufAllocator.Handle
        public void incMessagesRead(int numMessages) {
            this.delegate.incMessagesRead(numMessages);
        }

        @Override // io.netty.channel.RecvByteBufAllocator.Handle
        public void lastBytesRead(int bytes) {
            this.delegate.lastBytesRead(bytes);
        }

        @Override // io.netty.channel.RecvByteBufAllocator.Handle
        public int lastBytesRead() {
            return this.delegate.lastBytesRead();
        }

        @Override // io.netty.channel.RecvByteBufAllocator.Handle
        public boolean continueReading() {
            return this.delegate.continueReading();
        }

        @Override // io.netty.channel.RecvByteBufAllocator.Handle
        public int attemptedBytesRead() {
            return this.delegate.attemptedBytesRead();
        }

        @Override // io.netty.channel.RecvByteBufAllocator.Handle
        public void attemptedBytesRead(int bytes) {
            this.delegate.attemptedBytesRead(bytes);
        }

        @Override // io.netty.channel.RecvByteBufAllocator.Handle
        public void readComplete() {
            this.delegate.readComplete();
        }
    }
}
