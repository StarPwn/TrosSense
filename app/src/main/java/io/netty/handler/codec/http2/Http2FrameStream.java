package io.netty.handler.codec.http2;

import io.netty.handler.codec.http2.Http2Stream;

/* loaded from: classes4.dex */
public interface Http2FrameStream {
    int id();

    Http2Stream.State state();
}
