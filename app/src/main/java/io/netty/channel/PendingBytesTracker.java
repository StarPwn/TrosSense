package io.netty.channel;

import io.netty.channel.MessageSizeEstimator;
import io.netty.util.internal.ObjectUtil;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public abstract class PendingBytesTracker implements MessageSizeEstimator.Handle {
    private final MessageSizeEstimator.Handle estimatorHandle;

    public abstract void decrementPendingOutboundBytes(long j);

    public abstract void incrementPendingOutboundBytes(long j);

    private PendingBytesTracker(MessageSizeEstimator.Handle estimatorHandle) {
        this.estimatorHandle = (MessageSizeEstimator.Handle) ObjectUtil.checkNotNull(estimatorHandle, "estimatorHandle");
    }

    @Override // io.netty.channel.MessageSizeEstimator.Handle
    public final int size(Object msg) {
        return this.estimatorHandle.size(msg);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static PendingBytesTracker newTracker(Channel channel) {
        if (channel.pipeline() instanceof DefaultChannelPipeline) {
            return new DefaultChannelPipelinePendingBytesTracker((DefaultChannelPipeline) channel.pipeline());
        }
        ChannelOutboundBuffer buffer = channel.unsafe().outboundBuffer();
        MessageSizeEstimator.Handle handle = channel.config().getMessageSizeEstimator().newHandle();
        return buffer == null ? new NoopPendingBytesTracker(handle) : new ChannelOutboundBufferPendingBytesTracker(buffer, handle);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class DefaultChannelPipelinePendingBytesTracker extends PendingBytesTracker {
        private final DefaultChannelPipeline pipeline;

        DefaultChannelPipelinePendingBytesTracker(DefaultChannelPipeline pipeline) {
            super(pipeline.estimatorHandle());
            this.pipeline = pipeline;
        }

        @Override // io.netty.channel.PendingBytesTracker
        public void incrementPendingOutboundBytes(long bytes) {
            this.pipeline.incrementPendingOutboundBytes(bytes);
        }

        @Override // io.netty.channel.PendingBytesTracker
        public void decrementPendingOutboundBytes(long bytes) {
            this.pipeline.decrementPendingOutboundBytes(bytes);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class ChannelOutboundBufferPendingBytesTracker extends PendingBytesTracker {
        private final ChannelOutboundBuffer buffer;

        ChannelOutboundBufferPendingBytesTracker(ChannelOutboundBuffer buffer, MessageSizeEstimator.Handle estimatorHandle) {
            super(estimatorHandle);
            this.buffer = buffer;
        }

        @Override // io.netty.channel.PendingBytesTracker
        public void incrementPendingOutboundBytes(long bytes) {
            this.buffer.incrementPendingOutboundBytes(bytes);
        }

        @Override // io.netty.channel.PendingBytesTracker
        public void decrementPendingOutboundBytes(long bytes) {
            this.buffer.decrementPendingOutboundBytes(bytes);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class NoopPendingBytesTracker extends PendingBytesTracker {
        NoopPendingBytesTracker(MessageSizeEstimator.Handle estimatorHandle) {
            super(estimatorHandle);
        }

        @Override // io.netty.channel.PendingBytesTracker
        public void incrementPendingOutboundBytes(long bytes) {
        }

        @Override // io.netty.channel.PendingBytesTracker
        public void decrementPendingOutboundBytes(long bytes) {
        }
    }
}
