package org.cloudburstmc.netty.handler.codec.raknet.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

@ChannelHandler.Sharable
/* loaded from: classes5.dex */
public class RakServerTailHandler extends ChannelInboundHandlerAdapter {
    public static final String NAME = "rak-server-tail-handler";
    public static final RakServerTailHandler INSTANCE = new RakServerTailHandler();
    private static final InternalLogger log = InternalLoggerFactory.getInstance((Class<?>) RakServerTailHandler.class);

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Channel) {
            super.channelRead(ctx, msg);
        } else {
            ReferenceCountUtil.release(msg);
            log.trace("Received unexpected message in server channel: {}", msg);
        }
    }
}
