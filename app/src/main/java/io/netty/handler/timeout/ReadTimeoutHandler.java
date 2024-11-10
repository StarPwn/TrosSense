package io.netty.handler.timeout;

import io.netty.channel.ChannelHandlerContext;
import java.util.concurrent.TimeUnit;

/* loaded from: classes4.dex */
public class ReadTimeoutHandler extends IdleStateHandler {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private boolean closed;

    public ReadTimeoutHandler(int timeoutSeconds) {
        this(timeoutSeconds, TimeUnit.SECONDS);
    }

    public ReadTimeoutHandler(long timeout, TimeUnit unit) {
        super(timeout, 0L, 0L, unit);
    }

    @Override // io.netty.handler.timeout.IdleStateHandler
    protected final void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        if (evt.state() != IdleState.READER_IDLE) {
            throw new AssertionError();
        }
        readTimedOut(ctx);
    }

    protected void readTimedOut(ChannelHandlerContext ctx) throws Exception {
        if (!this.closed) {
            ctx.fireExceptionCaught((Throwable) ReadTimeoutException.INSTANCE);
            ctx.close();
            this.closed = true;
        }
    }
}
