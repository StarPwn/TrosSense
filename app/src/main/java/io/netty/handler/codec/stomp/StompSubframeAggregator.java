package io.netty.handler.codec.stomp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.MessageAggregator;

/* loaded from: classes4.dex */
public class StompSubframeAggregator extends MessageAggregator<StompSubframe, StompHeadersSubframe, StompContentSubframe, StompFrame> {
    public StompSubframeAggregator(int maxContentLength) {
        super(maxContentLength);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageAggregator
    public boolean isStartMessage(StompSubframe msg) throws Exception {
        return msg instanceof StompHeadersSubframe;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageAggregator
    public boolean isContentMessage(StompSubframe msg) throws Exception {
        return msg instanceof StompContentSubframe;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageAggregator
    public boolean isLastContentMessage(StompContentSubframe msg) throws Exception {
        return msg instanceof LastStompContentSubframe;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageAggregator
    public boolean isAggregated(StompSubframe msg) throws Exception {
        return msg instanceof StompFrame;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageAggregator
    public boolean isContentLengthInvalid(StompHeadersSubframe start, int maxContentLength) {
        return ((int) Math.min(2147483647L, start.headers().getLong(StompHeaders.CONTENT_LENGTH, -1L))) > maxContentLength;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageAggregator
    public Object newContinueResponse(StompHeadersSubframe start, int maxContentLength, ChannelPipeline pipeline) {
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
    public StompFrame beginAggregation(StompHeadersSubframe start, ByteBuf content) throws Exception {
        StompFrame ret = new DefaultStompFrame(start.command(), content);
        ret.headers().set(start.headers());
        return ret;
    }
}
