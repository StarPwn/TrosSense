package io.netty.handler.codec.http2;

/* loaded from: classes4.dex */
public final class DefaultHttp2PushPromiseFrame implements Http2PushPromiseFrame {
    private final Http2Headers http2Headers;
    private final int padding;
    private final int promisedStreamId;
    private Http2FrameStream pushStreamFrame;
    private Http2FrameStream streamFrame;

    public DefaultHttp2PushPromiseFrame(Http2Headers http2Headers) {
        this(http2Headers, 0);
    }

    public DefaultHttp2PushPromiseFrame(Http2Headers http2Headers, int padding) {
        this(http2Headers, padding, -1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DefaultHttp2PushPromiseFrame(Http2Headers http2Headers, int padding, int promisedStreamId) {
        this.http2Headers = http2Headers;
        this.padding = padding;
        this.promisedStreamId = promisedStreamId;
    }

    @Override // io.netty.handler.codec.http2.Http2PushPromiseFrame
    public Http2StreamFrame pushStream(Http2FrameStream stream) {
        this.pushStreamFrame = stream;
        return this;
    }

    @Override // io.netty.handler.codec.http2.Http2PushPromiseFrame
    public Http2FrameStream pushStream() {
        return this.pushStreamFrame;
    }

    @Override // io.netty.handler.codec.http2.Http2PushPromiseFrame
    public Http2Headers http2Headers() {
        return this.http2Headers;
    }

    @Override // io.netty.handler.codec.http2.Http2PushPromiseFrame
    public int padding() {
        return this.padding;
    }

    @Override // io.netty.handler.codec.http2.Http2PushPromiseFrame
    public int promisedStreamId() {
        if (this.pushStreamFrame != null) {
            return this.pushStreamFrame.id();
        }
        return this.promisedStreamId;
    }

    @Override // io.netty.handler.codec.http2.Http2StreamFrame
    public Http2PushPromiseFrame stream(Http2FrameStream stream) {
        this.streamFrame = stream;
        return this;
    }

    @Override // io.netty.handler.codec.http2.Http2StreamFrame
    public Http2FrameStream stream() {
        return this.streamFrame;
    }

    @Override // io.netty.handler.codec.http2.Http2Frame
    public String name() {
        return "PUSH_PROMISE_FRAME";
    }

    public String toString() {
        return "DefaultHttp2PushPromiseFrame{pushStreamFrame=" + this.pushStreamFrame + ", http2Headers=" + this.http2Headers + ", streamFrame=" + this.streamFrame + ", padding=" + this.padding + '}';
    }
}
