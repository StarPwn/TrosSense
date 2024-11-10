package io.netty.handler.codec.http2;

/* loaded from: classes4.dex */
public interface Http2PingFrame extends Http2Frame {
    boolean ack();

    long content();
}
