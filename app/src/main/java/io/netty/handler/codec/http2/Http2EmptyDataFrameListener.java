package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
final class Http2EmptyDataFrameListener extends Http2FrameListenerDecorator {
    private int emptyDataFrames;
    private final int maxConsecutiveEmptyFrames;
    private boolean violationDetected;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Http2EmptyDataFrameListener(Http2FrameListener listener, int maxConsecutiveEmptyFrames) {
        super(listener);
        this.maxConsecutiveEmptyFrames = ObjectUtil.checkPositive(maxConsecutiveEmptyFrames, "maxConsecutiveEmptyFrames");
    }

    @Override // io.netty.handler.codec.http2.Http2FrameListenerDecorator, io.netty.handler.codec.http2.Http2FrameListener
    public int onDataRead(ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding, boolean endOfStream) throws Http2Exception {
        if (endOfStream || data.isReadable()) {
            this.emptyDataFrames = 0;
        } else {
            int i = this.emptyDataFrames;
            this.emptyDataFrames = i + 1;
            if (i == this.maxConsecutiveEmptyFrames && !this.violationDetected) {
                this.violationDetected = true;
                throw Http2Exception.connectionError(Http2Error.ENHANCE_YOUR_CALM, "Maximum number %d of empty data frames without end_of_stream flag received", Integer.valueOf(this.maxConsecutiveEmptyFrames));
            }
        }
        return super.onDataRead(ctx, streamId, data, padding, endOfStream);
    }

    @Override // io.netty.handler.codec.http2.Http2FrameListenerDecorator, io.netty.handler.codec.http2.Http2FrameListener
    public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding, boolean endStream) throws Http2Exception {
        this.emptyDataFrames = 0;
        super.onHeadersRead(ctx, streamId, headers, padding, endStream);
    }

    @Override // io.netty.handler.codec.http2.Http2FrameListenerDecorator, io.netty.handler.codec.http2.Http2FrameListener
    public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endStream) throws Http2Exception {
        this.emptyDataFrames = 0;
        super.onHeadersRead(ctx, streamId, headers, streamDependency, weight, exclusive, padding, endStream);
    }
}
