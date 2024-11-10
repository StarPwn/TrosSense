package org.cloudburstmc.protocol.bedrock.netty.codec.batch;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.ArrayDeque;
import java.util.Queue;
import org.cloudburstmc.protocol.bedrock.netty.BedrockPacketWrapper;

/* loaded from: classes5.dex */
public class BedrockBatchEncoder extends ChannelOutboundHandlerAdapter {
    public static final String NAME = "bedrock-batch-encoder";
    private static final InternalLogger log = InternalLoggerFactory.getInstance((Class<?>) BedrockBatchEncoder.class);
    private final Queue<BedrockPacketWrapper> messages = new ArrayDeque();

    @Override // io.netty.channel.ChannelOutboundHandlerAdapter, io.netty.channel.ChannelOutboundHandler
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (!(msg instanceof BedrockPacketWrapper)) {
            super.write(ctx, msg, promise);
        } else {
            this.messages.add((BedrockPacketWrapper) msg);
            promise.trySuccess();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x005e, code lost:            throw new java.lang.IllegalArgumentException("BedrockPacket is not encoded");     */
    @Override // io.netty.channel.ChannelOutboundHandlerAdapter, io.netty.channel.ChannelOutboundHandler
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void flush(io.netty.channel.ChannelHandlerContext r8) throws java.lang.Exception {
        /*
            r7 = this;
            java.util.Queue<org.cloudburstmc.protocol.bedrock.netty.BedrockPacketWrapper> r0 = r7.messages
            boolean r0 = r0.isEmpty()
            if (r0 == 0) goto Lc
            super.flush(r8)
            return
        Lc:
            io.netty.buffer.ByteBufAllocator r0 = r8.alloc()
            java.util.Queue<org.cloudburstmc.protocol.bedrock.netty.BedrockPacketWrapper> r1 = r7.messages
            int r1 = r1.size()
            int r1 = r1 * 2
            io.netty.buffer.CompositeByteBuf r0 = r0.compositeDirectBuffer(r1)
            org.cloudburstmc.protocol.bedrock.netty.BedrockBatchWrapper r1 = org.cloudburstmc.protocol.bedrock.netty.BedrockBatchWrapper.newInstance()
        L20:
            java.util.Queue<org.cloudburstmc.protocol.bedrock.netty.BedrockPacketWrapper> r2 = r7.messages     // Catch: java.lang.Throwable -> L7f
            java.lang.Object r2 = r2.poll()     // Catch: java.lang.Throwable -> L7f
            org.cloudburstmc.protocol.bedrock.netty.BedrockPacketWrapper r2 = (org.cloudburstmc.protocol.bedrock.netty.BedrockPacketWrapper) r2     // Catch: java.lang.Throwable -> L7f
            r3 = r2
            if (r2 == 0) goto L65
            io.netty.buffer.ByteBuf r2 = r3.getPacketBuffer()     // Catch: java.lang.Throwable -> L5f
            if (r2 == 0) goto L57
            io.netty.buffer.ByteBufAllocator r4 = r8.alloc()     // Catch: java.lang.Throwable -> L5f
            r5 = 5
            io.netty.buffer.ByteBuf r4 = r4.ioBuffer(r5)     // Catch: java.lang.Throwable -> L5f
            int r5 = r2.readableBytes()     // Catch: java.lang.Throwable -> L5f
            org.cloudburstmc.protocol.common.util.VarInts.writeUnsignedInt(r4, r5)     // Catch: java.lang.Throwable -> L5f
            r5 = 1
            r0.addComponent(r5, r4)     // Catch: java.lang.Throwable -> L5f
            io.netty.buffer.ByteBuf r6 = r2.retain()     // Catch: java.lang.Throwable -> L5f
            r0.addComponent(r5, r6)     // Catch: java.lang.Throwable -> L5f
            org.cloudburstmc.protocol.bedrock.netty.BedrockPacketWrapper r5 = r3.retain()     // Catch: java.lang.Throwable -> L5f
            r1.addPacket(r5)     // Catch: java.lang.Throwable -> L5f
            r3.release()     // Catch: java.lang.Throwable -> L7f
            goto L20
        L57:
            java.lang.IllegalArgumentException r4 = new java.lang.IllegalArgumentException     // Catch: java.lang.Throwable -> L5f
            java.lang.String r5 = "BedrockPacket is not encoded"
            r4.<init>(r5)     // Catch: java.lang.Throwable -> L5f
            throw r4     // Catch: java.lang.Throwable -> L5f
        L5f:
            r2 = move-exception
            r3.release()     // Catch: java.lang.Throwable -> L7f
            throw r2     // Catch: java.lang.Throwable -> L7f
        L65:
            io.netty.buffer.CompositeByteBuf r2 = r0.retain()     // Catch: java.lang.Throwable -> L7f
            r1.setUncompressed(r2)     // Catch: java.lang.Throwable -> L7f
            org.cloudburstmc.protocol.bedrock.netty.BedrockBatchWrapper r2 = r1.retain()     // Catch: java.lang.Throwable -> L7f
            r8.write(r2)     // Catch: java.lang.Throwable -> L7f
            r0.release()
            r1.release()
            super.flush(r8)
            return
        L7f:
            r2 = move-exception
            r0.release()
            r1.release()
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: org.cloudburstmc.protocol.bedrock.netty.codec.batch.BedrockBatchEncoder.flush(io.netty.channel.ChannelHandlerContext):void");
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        while (true) {
            BedrockPacketWrapper message = this.messages.poll();
            if (message != null) {
                message.release();
            } else {
                super.handlerRemoved(ctx);
                return;
            }
        }
    }
}
