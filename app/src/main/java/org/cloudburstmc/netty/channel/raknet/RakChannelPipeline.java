package org.cloudburstmc.netty.channel.raknet;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultChannelPipeline;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import org.cloudburstmc.netty.channel.raknet.packet.EncapsulatedPacket;

/* loaded from: classes5.dex */
public class RakChannelPipeline extends DefaultChannelPipeline {
    private static final InternalLogger log = InternalLoggerFactory.getInstance((Class<?>) RakChannelPipeline.class);
    private final RakChildChannel child;

    /* JADX INFO: Access modifiers changed from: protected */
    public RakChannelPipeline(Channel parent, RakChildChannel child) {
        super(parent);
        this.child = child;
    }

    @Override // io.netty.channel.DefaultChannelPipeline
    protected void onUnhandledInboundChannelActive() {
    }

    @Override // io.netty.channel.DefaultChannelPipeline
    protected void onUnhandledInboundChannelInactive() {
        if (this.child.isActive()) {
            this.child.setActive(false);
            this.child.pipeline().fireChannelInactive();
        }
    }

    @Override // io.netty.channel.DefaultChannelPipeline
    protected void onUnhandledInboundMessage(ChannelHandlerContext ctx, Object msg) {
        try {
            final Object message = msg instanceof EncapsulatedPacket ? ((EncapsulatedPacket) msg).toMessage() : msg;
            ReferenceCountUtil.retain(message);
            if (this.child.eventLoop().inEventLoop()) {
                this.child.pipeline().fireChannelRead(message).fireChannelReadComplete();
            } else {
                this.child.eventLoop().execute(new Runnable() { // from class: org.cloudburstmc.netty.channel.raknet.RakChannelPipeline$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        RakChannelPipeline.this.m2034x70d90c20(message);
                    }
                });
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onUnhandledInboundMessage$0$org-cloudburstmc-netty-channel-raknet-RakChannelPipeline, reason: not valid java name */
    public /* synthetic */ void m2034x70d90c20(Object message) {
        this.child.pipeline().fireChannelRead(message).fireChannelReadComplete();
    }

    @Override // io.netty.channel.DefaultChannelPipeline
    protected void onUnhandledInboundUserEventTriggered(Object evt) {
        this.child.pipeline().fireUserEventTriggered(evt);
        if (evt instanceof RakDisconnectReason) {
            this.child.close();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.DefaultChannelPipeline
    public void onUnhandledInboundException(Throwable cause) {
        log.error("Exception thrown in RakNet pipeline", cause);
    }
}
