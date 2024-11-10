package io.netty.handler.codec.http2;

/* loaded from: classes4.dex */
public interface Http2PriorityFrame extends Http2StreamFrame {
    boolean exclusive();

    @Override // io.netty.handler.codec.http2.Http2StreamFrame
    Http2PriorityFrame stream(Http2FrameStream http2FrameStream);

    int streamDependency();

    short weight();
}
