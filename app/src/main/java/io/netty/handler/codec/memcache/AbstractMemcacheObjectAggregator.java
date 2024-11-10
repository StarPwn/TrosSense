package io.netty.handler.codec.memcache;

import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.MessageAggregator;
import io.netty.handler.codec.memcache.MemcacheMessage;

/* loaded from: classes4.dex */
public abstract class AbstractMemcacheObjectAggregator<H extends MemcacheMessage> extends MessageAggregator<MemcacheObject, H, MemcacheContent, FullMemcacheMessage> {
    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractMemcacheObjectAggregator(int maxContentLength) {
        super(maxContentLength);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageAggregator
    public boolean isContentMessage(MemcacheObject msg) throws Exception {
        return msg instanceof MemcacheContent;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageAggregator
    public boolean isLastContentMessage(MemcacheContent msg) throws Exception {
        return msg instanceof LastMemcacheContent;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageAggregator
    public boolean isAggregated(MemcacheObject msg) throws Exception {
        return msg instanceof FullMemcacheMessage;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageAggregator
    public boolean isContentLengthInvalid(H start, int maxContentLength) {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageAggregator
    public Object newContinueResponse(H start, int maxContentLength, ChannelPipeline pipeline) {
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
}
