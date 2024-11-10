package io.netty.handler.traffic;

import com.trossense.bl;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPromise;
import io.netty.channel.FileRegion;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.concurrent.TimeUnit;

/* loaded from: classes4.dex */
public abstract class AbstractTrafficShapingHandler extends ChannelDuplexHandler {
    static final int CHANNEL_DEFAULT_USER_DEFINED_WRITABILITY_INDEX = 1;
    public static final long DEFAULT_CHECK_INTERVAL = 1000;
    static final long DEFAULT_MAX_SIZE = 4194304;
    public static final long DEFAULT_MAX_TIME = 15000;
    static final int GLOBALCHANNEL_DEFAULT_USER_DEFINED_WRITABILITY_INDEX = 3;
    static final int GLOBAL_DEFAULT_USER_DEFINED_WRITABILITY_INDEX = 2;
    static final long MINIMAL_WAIT = 10;
    protected volatile long checkInterval;
    protected volatile long maxTime;
    volatile long maxWriteDelay;
    volatile long maxWriteSize;
    private volatile long readLimit;
    protected TrafficCounter trafficCounter;
    final int userDefinedWritabilityIndex;
    private volatile long writeLimit;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) AbstractTrafficShapingHandler.class);
    static final AttributeKey<Boolean> READ_SUSPENDED = AttributeKey.valueOf(AbstractTrafficShapingHandler.class.getName() + ".READ_SUSPENDED");
    static final AttributeKey<Runnable> REOPEN_TASK = AttributeKey.valueOf(AbstractTrafficShapingHandler.class.getName() + ".REOPEN_TASK");

    abstract void submitWrite(ChannelHandlerContext channelHandlerContext, Object obj, long j, long j2, long j3, ChannelPromise channelPromise);

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setTrafficCounter(TrafficCounter newTrafficCounter) {
        this.trafficCounter = newTrafficCounter;
    }

    protected int userDefinedWritabilityIndex() {
        return 1;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractTrafficShapingHandler(long writeLimit, long readLimit, long checkInterval, long maxTime) {
        this.maxTime = DEFAULT_MAX_TIME;
        this.checkInterval = 1000L;
        this.maxWriteDelay = 4000L;
        this.maxWriteSize = DEFAULT_MAX_SIZE;
        this.maxTime = ObjectUtil.checkPositive(maxTime, "maxTime");
        this.userDefinedWritabilityIndex = userDefinedWritabilityIndex();
        this.writeLimit = writeLimit;
        this.readLimit = readLimit;
        this.checkInterval = checkInterval;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractTrafficShapingHandler(long writeLimit, long readLimit, long checkInterval) {
        this(writeLimit, readLimit, checkInterval, DEFAULT_MAX_TIME);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractTrafficShapingHandler(long writeLimit, long readLimit) {
        this(writeLimit, readLimit, 1000L, DEFAULT_MAX_TIME);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractTrafficShapingHandler() {
        this(0L, 0L, 1000L, DEFAULT_MAX_TIME);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractTrafficShapingHandler(long checkInterval) {
        this(0L, 0L, checkInterval, DEFAULT_MAX_TIME);
    }

    public void configure(long newWriteLimit, long newReadLimit, long newCheckInterval) {
        configure(newWriteLimit, newReadLimit);
        configure(newCheckInterval);
    }

    public void configure(long newWriteLimit, long newReadLimit) {
        this.writeLimit = newWriteLimit;
        this.readLimit = newReadLimit;
        if (this.trafficCounter != null) {
            this.trafficCounter.resetAccounting(TrafficCounter.milliSecondFromNano());
        }
    }

    public void configure(long newCheckInterval) {
        this.checkInterval = newCheckInterval;
        if (this.trafficCounter != null) {
            this.trafficCounter.configure(this.checkInterval);
        }
    }

    public long getWriteLimit() {
        return this.writeLimit;
    }

    public void setWriteLimit(long writeLimit) {
        this.writeLimit = writeLimit;
        if (this.trafficCounter != null) {
            this.trafficCounter.resetAccounting(TrafficCounter.milliSecondFromNano());
        }
    }

    public long getReadLimit() {
        return this.readLimit;
    }

    public void setReadLimit(long readLimit) {
        this.readLimit = readLimit;
        if (this.trafficCounter != null) {
            this.trafficCounter.resetAccounting(TrafficCounter.milliSecondFromNano());
        }
    }

    public long getCheckInterval() {
        return this.checkInterval;
    }

    public void setCheckInterval(long checkInterval) {
        this.checkInterval = checkInterval;
        if (this.trafficCounter != null) {
            this.trafficCounter.configure(checkInterval);
        }
    }

    public void setMaxTimeWait(long maxTime) {
        this.maxTime = ObjectUtil.checkPositive(maxTime, "maxTime");
    }

    public long getMaxTimeWait() {
        return this.maxTime;
    }

    public long getMaxWriteDelay() {
        return this.maxWriteDelay;
    }

    public void setMaxWriteDelay(long maxWriteDelay) {
        this.maxWriteDelay = ObjectUtil.checkPositive(maxWriteDelay, "maxWriteDelay");
    }

    public long getMaxWriteSize() {
        return this.maxWriteSize;
    }

    public void setMaxWriteSize(long maxWriteSize) {
        this.maxWriteSize = maxWriteSize;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void doAccounting(TrafficCounter counter) {
    }

    /* loaded from: classes4.dex */
    static final class ReopenReadTimerTask implements Runnable {
        final ChannelHandlerContext ctx;

        /* JADX INFO: Access modifiers changed from: package-private */
        public ReopenReadTimerTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override // java.lang.Runnable
        public void run() {
            Channel channel = this.ctx.channel();
            ChannelConfig config = channel.config();
            if (config.isAutoRead() || !AbstractTrafficShapingHandler.isHandlerActive(this.ctx)) {
                if (AbstractTrafficShapingHandler.logger.isDebugEnabled()) {
                    if (!config.isAutoRead() || AbstractTrafficShapingHandler.isHandlerActive(this.ctx)) {
                        if (AbstractTrafficShapingHandler.logger.isDebugEnabled()) {
                            AbstractTrafficShapingHandler.logger.debug("Normal unsuspend: " + config.isAutoRead() + ':' + AbstractTrafficShapingHandler.isHandlerActive(this.ctx));
                        }
                    } else if (AbstractTrafficShapingHandler.logger.isDebugEnabled()) {
                        AbstractTrafficShapingHandler.logger.debug("Unsuspend: " + config.isAutoRead() + ':' + AbstractTrafficShapingHandler.isHandlerActive(this.ctx));
                    }
                }
                channel.attr(AbstractTrafficShapingHandler.READ_SUSPENDED).set(false);
                config.setAutoRead(true);
                channel.read();
            } else {
                if (AbstractTrafficShapingHandler.logger.isDebugEnabled()) {
                    AbstractTrafficShapingHandler.logger.debug("Not unsuspend: " + config.isAutoRead() + ':' + AbstractTrafficShapingHandler.isHandlerActive(this.ctx));
                }
                channel.attr(AbstractTrafficShapingHandler.READ_SUSPENDED).set(false);
            }
            if (AbstractTrafficShapingHandler.logger.isDebugEnabled()) {
                AbstractTrafficShapingHandler.logger.debug("Unsuspend final status => " + config.isAutoRead() + ':' + AbstractTrafficShapingHandler.isHandlerActive(this.ctx));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void releaseReadSuspended(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        channel.attr(READ_SUSPENDED).set(false);
        channel.config().setAutoRead(true);
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        long now;
        long size = calculateSize(msg);
        long now2 = TrafficCounter.milliSecondFromNano();
        if (size <= 0) {
            now = now2;
        } else {
            now = now2;
            long wait = checkWaitReadTime(ctx, this.trafficCounter.readTimeToWait(size, this.readLimit, this.maxTime, now2), now2);
            if (wait >= 10) {
                Channel channel = ctx.channel();
                ChannelConfig config = channel.config();
                if (logger.isDebugEnabled()) {
                    logger.debug("Read suspend: " + wait + ':' + config.isAutoRead() + ':' + isHandlerActive(ctx));
                }
                if (config.isAutoRead() && isHandlerActive(ctx)) {
                    config.setAutoRead(false);
                    channel.attr(READ_SUSPENDED).set(true);
                    Attribute<Runnable> attr = channel.attr(REOPEN_TASK);
                    Runnable reopenTask = attr.get();
                    if (reopenTask == null) {
                        reopenTask = new ReopenReadTimerTask(ctx);
                        attr.set(reopenTask);
                    }
                    ctx.executor().schedule(reopenTask, wait, TimeUnit.MILLISECONDS);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Suspend final status => " + config.isAutoRead() + ':' + isHandlerActive(ctx) + " will reopened at: " + wait);
                    }
                }
            }
        }
        informReadOperation(ctx, now);
        ctx.fireChannelRead(msg);
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        if (channel.hasAttr(REOPEN_TASK)) {
            channel.attr(REOPEN_TASK).set(null);
        }
        super.handlerRemoved(ctx);
    }

    long checkWaitReadTime(ChannelHandlerContext ctx, long wait, long now) {
        return wait;
    }

    void informReadOperation(ChannelHandlerContext ctx, long now) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static boolean isHandlerActive(ChannelHandlerContext ctx) {
        Boolean suspended = (Boolean) ctx.channel().attr(READ_SUSPENDED).get();
        return suspended == null || Boolean.FALSE.equals(suspended);
    }

    @Override // io.netty.channel.ChannelDuplexHandler, io.netty.channel.ChannelOutboundHandler
    public void read(ChannelHandlerContext ctx) {
        if (isHandlerActive(ctx)) {
            ctx.read();
        }
    }

    @Override // io.netty.channel.ChannelDuplexHandler, io.netty.channel.ChannelOutboundHandler
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        long size = calculateSize(msg);
        long now = TrafficCounter.milliSecondFromNano();
        if (size > 0) {
            long wait = this.trafficCounter.writeTimeToWait(size, this.writeLimit, this.maxTime, now);
            if (wait >= 10) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Write suspend: " + wait + ':' + ctx.channel().config().isAutoRead() + ':' + isHandlerActive(ctx));
                }
                submitWrite(ctx, msg, size, wait, now, promise);
                return;
            }
        }
        submitWrite(ctx, msg, size, 0L, now, promise);
    }

    @Deprecated
    protected void submitWrite(ChannelHandlerContext ctx, Object msg, long delay, ChannelPromise promise) {
        submitWrite(ctx, msg, calculateSize(msg), delay, TrafficCounter.milliSecondFromNano(), promise);
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        setUserDefinedWritability(ctx, true);
        super.channelRegistered(ctx);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setUserDefinedWritability(ChannelHandlerContext ctx, boolean writable) {
        ChannelOutboundBuffer cob = ctx.channel().unsafe().outboundBuffer();
        if (cob != null) {
            cob.setUserDefinedWritability(this.userDefinedWritabilityIndex, writable);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void checkWriteSuspend(ChannelHandlerContext ctx, long delay, long queueSize) {
        if (queueSize > this.maxWriteSize || delay > this.maxWriteDelay) {
            setUserDefinedWritability(ctx, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void releaseWriteSuspended(ChannelHandlerContext ctx) {
        setUserDefinedWritability(ctx, true);
    }

    public TrafficCounter trafficCounter() {
        return this.trafficCounter;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder(bl.d0).append("TrafficShaping with Write Limit: ").append(this.writeLimit).append(" Read Limit: ").append(this.readLimit).append(" CheckInterval: ").append(this.checkInterval).append(" maxDelay: ").append(this.maxWriteDelay).append(" maxSize: ").append(this.maxWriteSize).append(" and Counter: ");
        if (this.trafficCounter != null) {
            builder.append(this.trafficCounter);
        } else {
            builder.append("none");
        }
        return builder.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public long calculateSize(Object msg) {
        if (msg instanceof ByteBuf) {
            return ((ByteBuf) msg).readableBytes();
        }
        if (msg instanceof ByteBufHolder) {
            return ((ByteBufHolder) msg).content().readableBytes();
        }
        if (msg instanceof FileRegion) {
            return ((FileRegion) msg).count();
        }
        return -1L;
    }
}
