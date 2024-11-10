package io.netty.handler.codec.redis;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.CodecException;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.ReferenceCountUtil;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/* loaded from: classes4.dex */
public final class RedisArrayAggregator extends MessageToMessageDecoder<RedisMessage> {
    private final Deque<AggregateState> depths = new ArrayDeque(4);

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageToMessageDecoder
    public /* bridge */ /* synthetic */ void decode(ChannelHandlerContext channelHandlerContext, RedisMessage redisMessage, List list) throws Exception {
        decode2(channelHandlerContext, redisMessage, (List<Object>) list);
    }

    /* renamed from: decode, reason: avoid collision after fix types in other method */
    protected void decode2(ChannelHandlerContext ctx, RedisMessage msg, List<Object> out) throws Exception {
        if (msg instanceof ArrayHeaderRedisMessage) {
            msg = decodeRedisArrayHeader((ArrayHeaderRedisMessage) msg);
            if (msg == null) {
                return;
            }
        } else {
            ReferenceCountUtil.retain(msg);
        }
        while (!this.depths.isEmpty()) {
            AggregateState current = this.depths.peek();
            current.children.add(msg);
            if (current.children.size() == current.length) {
                msg = new ArrayRedisMessage((List<RedisMessage>) current.children);
                this.depths.pop();
            } else {
                return;
            }
        }
        out.add(msg);
    }

    private RedisMessage decodeRedisArrayHeader(ArrayHeaderRedisMessage header) {
        if (header.isNull()) {
            return ArrayRedisMessage.NULL_INSTANCE;
        }
        if (header.length() == 0) {
            return ArrayRedisMessage.EMPTY_INSTANCE;
        }
        if (header.length() <= 0) {
            throw new CodecException("bad length: " + header.length());
        }
        if (header.length() > 2147483647L) {
            throw new CodecException("this codec doesn't support longer length than 2147483647");
        }
        this.depths.push(new AggregateState((int) header.length()));
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class AggregateState {
        private final List<RedisMessage> children;
        private final int length;

        AggregateState(int length) {
            this.length = length;
            this.children = new ArrayList(length);
        }
    }
}
