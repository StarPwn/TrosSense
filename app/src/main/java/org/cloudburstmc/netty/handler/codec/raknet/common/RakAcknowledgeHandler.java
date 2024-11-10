package org.cloudburstmc.netty.handler.codec.raknet.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.Queue;
import org.cloudburstmc.netty.channel.raknet.RakDisconnectReason;
import org.cloudburstmc.netty.channel.raknet.config.RakMetrics;
import org.cloudburstmc.netty.util.IntRange;

/* loaded from: classes5.dex */
public class RakAcknowledgeHandler extends SimpleChannelInboundHandler<ByteBuf> {
    public static final String NAME = "rak-acknowledge-handler";
    private static final InternalLogger log = InternalLoggerFactory.getInstance((Class<?>) RakAcknowledgeHandler.class);
    private final RakSessionCodec sessionCodec;

    public RakAcknowledgeHandler(RakSessionCodec sessionCodec) {
        this.sessionCodec = sessionCodec;
    }

    @Override // io.netty.channel.SimpleChannelInboundHandler
    public boolean acceptInboundMessage(Object msg) throws Exception {
        if (!super.acceptInboundMessage(msg)) {
            return false;
        }
        ByteBuf buffer = (ByteBuf) msg;
        byte potentialFlags = buffer.getByte(buffer.readerIndex());
        if ((potentialFlags & Byte.MIN_VALUE) == 0) {
            return false;
        }
        return ((potentialFlags & 64) == 0 && (potentialFlags & 32) == 0) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.SimpleChannelInboundHandler
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
        boolean nack = (buffer.readByte() & 32) != 0;
        int entriesCount = buffer.readUnsignedShort();
        Queue<IntRange> queue = this.sessionCodec.getAcknowledgeQueue(nack);
        for (int i = 0; i < entriesCount; i++) {
            boolean singleton = buffer.readBoolean();
            int start = buffer.readUnsignedMediumLE();
            int end = singleton ? start : buffer.readUnsignedMediumLE();
            if (start <= end) {
                queue.offer(new IntRange(start, end));
            } else {
                if (log.isTraceEnabled()) {
                    log.trace("{} sent an IntRange with a start value {} greater than an end value of {}", this.sessionCodec.getChannel().remoteAddress(), Integer.valueOf(start), Integer.valueOf(end));
                }
                this.sessionCodec.disconnect(RakDisconnectReason.BAD_PACKET);
                return;
            }
        }
        RakMetrics metrics = this.sessionCodec.getMetrics();
        if (metrics != null) {
            if (nack) {
                metrics.nackIn(entriesCount);
            } else {
                metrics.ackIn(entriesCount);
            }
        }
    }
}
