package io.netty.channel;

import io.netty.channel.DefaultMaxMessagesRecvByteBufAllocator;
import io.netty.channel.RecvByteBufAllocator;

/* loaded from: classes4.dex */
public final class ServerChannelRecvByteBufAllocator extends DefaultMaxMessagesRecvByteBufAllocator {
    public ServerChannelRecvByteBufAllocator() {
        super(1, true);
    }

    @Override // io.netty.channel.RecvByteBufAllocator
    public RecvByteBufAllocator.Handle newHandle() {
        return new DefaultMaxMessagesRecvByteBufAllocator.MaxMessageHandle() { // from class: io.netty.channel.ServerChannelRecvByteBufAllocator.1
            @Override // io.netty.channel.RecvByteBufAllocator.Handle
            public int guess() {
                return 128;
            }
        };
    }
}
