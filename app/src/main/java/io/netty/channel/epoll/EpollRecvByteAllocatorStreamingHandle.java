package io.netty.channel.epoll;

import io.netty.channel.RecvByteBufAllocator;

/* loaded from: classes4.dex */
final class EpollRecvByteAllocatorStreamingHandle extends EpollRecvByteAllocatorHandle {
    /* JADX INFO: Access modifiers changed from: package-private */
    public EpollRecvByteAllocatorStreamingHandle(RecvByteBufAllocator.ExtendedHandle handle) {
        super(handle);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // io.netty.channel.epoll.EpollRecvByteAllocatorHandle
    public boolean maybeMoreDataToRead() {
        return lastBytesRead() == attemptedBytesRead() || isReceivedRdHup();
    }
}
