package io.netty.handler.flow;

import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.ObjectPool;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.ArrayDeque;

/* loaded from: classes4.dex */
public class FlowControlHandler extends ChannelDuplexHandler {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) FlowControlHandler.class);
    private ChannelConfig config;
    private RecyclableArrayDeque queue;
    private final boolean releaseMessages;
    private boolean shouldConsume;

    public FlowControlHandler() {
        this(true);
    }

    public FlowControlHandler(boolean releaseMessages) {
        this.releaseMessages = releaseMessages;
    }

    boolean isQueueEmpty() {
        return this.queue == null || this.queue.isEmpty();
    }

    private void destroy() {
        if (this.queue != null) {
            if (!this.queue.isEmpty()) {
                logger.trace("Non-empty queue: {}", this.queue);
                if (this.releaseMessages) {
                    while (true) {
                        Object msg = this.queue.poll();
                        if (msg == null) {
                            break;
                        } else {
                            ReferenceCountUtil.safeRelease(msg);
                        }
                    }
                }
            }
            this.queue.recycle();
            this.queue = null;
        }
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        this.config = ctx.channel().config();
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        if (!isQueueEmpty()) {
            dequeue(ctx, this.queue.size());
        }
        destroy();
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        destroy();
        ctx.fireChannelInactive();
    }

    @Override // io.netty.channel.ChannelDuplexHandler, io.netty.channel.ChannelOutboundHandler
    public void read(ChannelHandlerContext ctx) throws Exception {
        if (dequeue(ctx, 1) == 0) {
            this.shouldConsume = true;
            ctx.read();
        } else if (this.config.isAutoRead()) {
            ctx.read();
        }
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object obj) throws Exception {
        if (this.queue == null) {
            this.queue = RecyclableArrayDeque.newInstance();
        }
        this.queue.offer(obj);
        boolean z = this.shouldConsume;
        this.shouldConsume = false;
        dequeue(channelHandlerContext, z ? 1 : 0);
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        if (isQueueEmpty()) {
            ctx.fireChannelReadComplete();
        }
    }

    private int dequeue(ChannelHandlerContext ctx, int minConsume) {
        Object msg;
        int consumed = 0;
        while (this.queue != null && ((consumed < minConsume || this.config.isAutoRead()) && (msg = this.queue.poll()) != null)) {
            consumed++;
            ctx.fireChannelRead(msg);
        }
        if (this.queue != null && this.queue.isEmpty()) {
            this.queue.recycle();
            this.queue = null;
            if (consumed > 0) {
                ctx.fireChannelReadComplete();
            }
        }
        return consumed;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class RecyclableArrayDeque extends ArrayDeque<Object> {
        private static final int DEFAULT_NUM_ELEMENTS = 2;
        private static final ObjectPool<RecyclableArrayDeque> RECYCLER = ObjectPool.newPool(new ObjectPool.ObjectCreator<RecyclableArrayDeque>() { // from class: io.netty.handler.flow.FlowControlHandler.RecyclableArrayDeque.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // io.netty.util.internal.ObjectPool.ObjectCreator
            public RecyclableArrayDeque newObject(ObjectPool.Handle<RecyclableArrayDeque> handle) {
                return new RecyclableArrayDeque(2, handle);
            }
        });
        private static final long serialVersionUID = 0;
        private final ObjectPool.Handle<RecyclableArrayDeque> handle;

        public static RecyclableArrayDeque newInstance() {
            return RECYCLER.get();
        }

        private RecyclableArrayDeque(int numElements, ObjectPool.Handle<RecyclableArrayDeque> handle) {
            super(numElements);
            this.handle = handle;
        }

        public void recycle() {
            clear();
            this.handle.recycle(this);
        }
    }
}
