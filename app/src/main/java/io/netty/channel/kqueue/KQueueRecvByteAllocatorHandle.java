package io.netty.channel.kqueue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.unix.PreferredDirectByteBufAllocator;
import io.netty.util.UncheckedBooleanSupplier;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class KQueueRecvByteAllocatorHandle extends RecvByteBufAllocator.DelegatingHandle implements RecvByteBufAllocator.ExtendedHandle {
    private final UncheckedBooleanSupplier defaultMaybeMoreDataSupplier;
    private long numberBytesPending;
    private boolean overrideGuess;
    private final PreferredDirectByteBufAllocator preferredDirectByteBufAllocator;
    private boolean readEOF;

    /* JADX INFO: Access modifiers changed from: package-private */
    public KQueueRecvByteAllocatorHandle(RecvByteBufAllocator.ExtendedHandle handle) {
        super(handle);
        this.preferredDirectByteBufAllocator = new PreferredDirectByteBufAllocator();
        this.defaultMaybeMoreDataSupplier = new UncheckedBooleanSupplier() { // from class: io.netty.channel.kqueue.KQueueRecvByteAllocatorHandle.1
            @Override // io.netty.util.UncheckedBooleanSupplier, io.netty.util.BooleanSupplier
            public boolean get() {
                return KQueueRecvByteAllocatorHandle.this.maybeMoreDataToRead();
            }
        };
    }

    @Override // io.netty.channel.RecvByteBufAllocator.DelegatingHandle, io.netty.channel.RecvByteBufAllocator.Handle
    public int guess() {
        return this.overrideGuess ? guess0() : delegate().guess();
    }

    @Override // io.netty.channel.RecvByteBufAllocator.DelegatingHandle, io.netty.channel.RecvByteBufAllocator.Handle
    public void reset(ChannelConfig config) {
        this.overrideGuess = ((KQueueChannelConfig) config).getRcvAllocTransportProvidesGuess();
        delegate().reset(config);
    }

    @Override // io.netty.channel.RecvByteBufAllocator.DelegatingHandle, io.netty.channel.RecvByteBufAllocator.Handle
    public ByteBuf allocate(ByteBufAllocator alloc) {
        this.preferredDirectByteBufAllocator.updateAllocator(alloc);
        return this.overrideGuess ? this.preferredDirectByteBufAllocator.ioBuffer(guess0()) : delegate().allocate(this.preferredDirectByteBufAllocator);
    }

    @Override // io.netty.channel.RecvByteBufAllocator.DelegatingHandle, io.netty.channel.RecvByteBufAllocator.Handle
    public void lastBytesRead(int bytes) {
        this.numberBytesPending = bytes >= 0 ? Math.max(0L, this.numberBytesPending - bytes) : 0L;
        delegate().lastBytesRead(bytes);
    }

    @Override // io.netty.channel.RecvByteBufAllocator.ExtendedHandle
    public boolean continueReading(UncheckedBooleanSupplier maybeMoreDataSupplier) {
        return ((RecvByteBufAllocator.ExtendedHandle) delegate()).continueReading(maybeMoreDataSupplier);
    }

    @Override // io.netty.channel.RecvByteBufAllocator.DelegatingHandle, io.netty.channel.RecvByteBufAllocator.Handle
    public boolean continueReading() {
        return continueReading(this.defaultMaybeMoreDataSupplier);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void readEOF() {
        this.readEOF = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isReadEOF() {
        return this.readEOF;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void numberBytesPending(long numberBytesPending) {
        this.numberBytesPending = numberBytesPending;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean maybeMoreDataToRead() {
        return this.numberBytesPending != 0;
    }

    private int guess0() {
        return (int) Math.min(this.numberBytesPending, 2147483647L);
    }
}
