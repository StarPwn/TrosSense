package io.netty.channel.epoll;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.unix.PreferredDirectByteBufAllocator;
import io.netty.util.UncheckedBooleanSupplier;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public class EpollRecvByteAllocatorHandle extends RecvByteBufAllocator.DelegatingHandle implements RecvByteBufAllocator.ExtendedHandle {
    private final UncheckedBooleanSupplier defaultMaybeMoreDataSupplier;
    private boolean isEdgeTriggered;
    private final PreferredDirectByteBufAllocator preferredDirectByteBufAllocator;
    private boolean receivedRdHup;

    /* JADX INFO: Access modifiers changed from: package-private */
    public EpollRecvByteAllocatorHandle(RecvByteBufAllocator.ExtendedHandle handle) {
        super(handle);
        this.preferredDirectByteBufAllocator = new PreferredDirectByteBufAllocator();
        this.defaultMaybeMoreDataSupplier = new UncheckedBooleanSupplier() { // from class: io.netty.channel.epoll.EpollRecvByteAllocatorHandle.1
            @Override // io.netty.util.UncheckedBooleanSupplier, io.netty.util.BooleanSupplier
            public boolean get() {
                return EpollRecvByteAllocatorHandle.this.maybeMoreDataToRead();
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void receivedRdHup() {
        this.receivedRdHup = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean isReceivedRdHup() {
        return this.receivedRdHup;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean maybeMoreDataToRead() {
        return (this.isEdgeTriggered && lastBytesRead() > 0) || (!this.isEdgeTriggered && lastBytesRead() == attemptedBytesRead());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void edgeTriggered(boolean edgeTriggered) {
        this.isEdgeTriggered = edgeTriggered;
    }

    final boolean isEdgeTriggered() {
        return this.isEdgeTriggered;
    }

    @Override // io.netty.channel.RecvByteBufAllocator.DelegatingHandle, io.netty.channel.RecvByteBufAllocator.Handle
    public final ByteBuf allocate(ByteBufAllocator alloc) {
        this.preferredDirectByteBufAllocator.updateAllocator(alloc);
        return delegate().allocate(this.preferredDirectByteBufAllocator);
    }

    @Override // io.netty.channel.RecvByteBufAllocator.ExtendedHandle
    public final boolean continueReading(UncheckedBooleanSupplier maybeMoreDataSupplier) {
        return ((RecvByteBufAllocator.ExtendedHandle) delegate()).continueReading(maybeMoreDataSupplier);
    }

    @Override // io.netty.channel.RecvByteBufAllocator.DelegatingHandle, io.netty.channel.RecvByteBufAllocator.Handle
    public final boolean continueReading() {
        return continueReading(this.defaultMaybeMoreDataSupplier);
    }
}
