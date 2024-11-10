package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.MessageAggregator;

/* loaded from: classes4.dex */
public class WebSocketFrameAggregator extends MessageAggregator<WebSocketFrame, WebSocketFrame, ContinuationWebSocketFrame, WebSocketFrame> {
    public WebSocketFrameAggregator(int maxContentLength) {
        super(maxContentLength);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageAggregator
    public boolean isStartMessage(WebSocketFrame msg) throws Exception {
        return (msg instanceof TextWebSocketFrame) || (msg instanceof BinaryWebSocketFrame);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageAggregator
    public boolean isContentMessage(WebSocketFrame msg) throws Exception {
        return msg instanceof ContinuationWebSocketFrame;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageAggregator
    public boolean isLastContentMessage(ContinuationWebSocketFrame msg) throws Exception {
        return isContentMessage((WebSocketFrame) msg) && msg.isFinalFragment();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageAggregator
    public boolean isAggregated(WebSocketFrame msg) throws Exception {
        if (msg.isFinalFragment()) {
            return !isContentMessage(msg);
        }
        return (isStartMessage(msg) || isContentMessage(msg)) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageAggregator
    public boolean isContentLengthInvalid(WebSocketFrame start, int maxContentLength) {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageAggregator
    public Object newContinueResponse(WebSocketFrame start, int maxContentLength, ChannelPipeline pipeline) {
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
    public WebSocketFrame beginAggregation(WebSocketFrame start, ByteBuf content) throws Exception {
        if (start instanceof TextWebSocketFrame) {
            return new TextWebSocketFrame(true, start.rsv(), content);
        }
        if (start instanceof BinaryWebSocketFrame) {
            return new BinaryWebSocketFrame(true, start.rsv(), content);
        }
        throw new Error();
    }
}
