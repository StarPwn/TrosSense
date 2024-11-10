package io.netty.channel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.MessageSizeEstimator;
import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public final class DefaultMessageSizeEstimator implements MessageSizeEstimator {
    public static final MessageSizeEstimator DEFAULT = new DefaultMessageSizeEstimator(8);
    private final MessageSizeEstimator.Handle handle;

    /* loaded from: classes4.dex */
    private static final class HandleImpl implements MessageSizeEstimator.Handle {
        private final int unknownSize;

        private HandleImpl(int unknownSize) {
            this.unknownSize = unknownSize;
        }

        @Override // io.netty.channel.MessageSizeEstimator.Handle
        public int size(Object msg) {
            if (msg instanceof ByteBuf) {
                return ((ByteBuf) msg).readableBytes();
            }
            if (msg instanceof ByteBufHolder) {
                return ((ByteBufHolder) msg).content().readableBytes();
            }
            if (msg instanceof FileRegion) {
                return 0;
            }
            return this.unknownSize;
        }
    }

    public DefaultMessageSizeEstimator(int unknownSize) {
        ObjectUtil.checkPositiveOrZero(unknownSize, "unknownSize");
        this.handle = new HandleImpl(unknownSize);
    }

    @Override // io.netty.channel.MessageSizeEstimator
    public MessageSizeEstimator.Handle newHandle() {
        return this.handle;
    }
}
