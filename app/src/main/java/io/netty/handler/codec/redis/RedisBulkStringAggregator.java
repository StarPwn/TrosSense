package io.netty.handler.codec.redis;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.MessageAggregator;

/* loaded from: classes4.dex */
public final class RedisBulkStringAggregator extends MessageAggregator<RedisMessage, BulkStringHeaderRedisMessage, BulkStringRedisContent, FullBulkStringRedisMessage> {
    public RedisBulkStringAggregator() {
        super(536870912);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageAggregator
    public boolean isStartMessage(RedisMessage msg) throws Exception {
        return (msg instanceof BulkStringHeaderRedisMessage) && !isAggregated(msg);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageAggregator
    public boolean isContentMessage(RedisMessage msg) throws Exception {
        return msg instanceof BulkStringRedisContent;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageAggregator
    public boolean isLastContentMessage(BulkStringRedisContent msg) throws Exception {
        return msg instanceof LastBulkStringRedisContent;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageAggregator
    public boolean isAggregated(RedisMessage msg) throws Exception {
        return msg instanceof FullBulkStringRedisMessage;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageAggregator
    public boolean isContentLengthInvalid(BulkStringHeaderRedisMessage start, int maxContentLength) throws Exception {
        return start.bulkStringLength() > maxContentLength;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageAggregator
    public Object newContinueResponse(BulkStringHeaderRedisMessage start, int maxContentLength, ChannelPipeline pipeline) throws Exception {
        return null;
    }

    @Override // io.netty.handler.codec.MessageAggregator
    protected boolean closeAfterContinueResponse(Object msg) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override // io.netty.handler.codec.MessageAggregator
    protected boolean ignoreContentAfterContinueResponse(Object msg) throws Exception {
        throw new UnsupportedOperationException();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageAggregator
    public FullBulkStringRedisMessage beginAggregation(BulkStringHeaderRedisMessage start, ByteBuf content) throws Exception {
        return new FullBulkStringRedisMessage(content);
    }
}
