package org.cloudburstmc.netty.handler.codec.raknet.common;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.ScheduledFuture;
import io.netty.util.internal.PlatformDependent;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import org.cloudburstmc.netty.channel.raknet.RakChannel;
import org.cloudburstmc.netty.channel.raknet.packet.EncapsulatedPacket;

/* loaded from: classes5.dex */
public class RakUnhandledMessagesQueue extends SimpleChannelInboundHandler<EncapsulatedPacket> {
    public static final String NAME = "rak-unhandled-messages-queue";
    private final RakChannel channel;
    private ScheduledFuture<?> future;
    private final Queue<EncapsulatedPacket> messages = PlatformDependent.newMpscQueue();

    public RakUnhandledMessagesQueue(RakChannel channel) {
        this.channel = channel;
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
        this.future = ctx.channel().eventLoop().scheduleAtFixedRate(new Runnable() { // from class: org.cloudburstmc.netty.handler.codec.raknet.common.RakUnhandledMessagesQueue$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                RakUnhandledMessagesQueue.this.m2048x162b8fc5(ctx);
            }
        }, 0L, 50L, TimeUnit.MILLISECONDS);
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        if (this.future != null) {
            this.future.cancel(false);
        }
        while (true) {
            EncapsulatedPacket message = this.messages.poll();
            if (message != null) {
                ReferenceCountUtil.release(message);
            } else {
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: trySendMessages, reason: merged with bridge method [inline-methods] */
    public void m2048x162b8fc5(ChannelHandlerContext ctx) {
        if (!this.channel.isActive()) {
            return;
        }
        while (true) {
            EncapsulatedPacket message = this.messages.poll();
            if (message != null) {
                ctx.fireChannelRead((Object) message);
            } else {
                ctx.pipeline().remove(this);
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.SimpleChannelInboundHandler
    public void channelRead0(ChannelHandlerContext ctx, EncapsulatedPacket msg) throws Exception {
        if (!this.channel.isActive()) {
            this.messages.offer(msg.retain());
        } else {
            m2048x162b8fc5(ctx);
            ctx.fireChannelRead((Object) msg.retain());
        }
    }
}
