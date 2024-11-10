package io.netty.handler.codec.http2;

import io.netty.util.internal.StringUtil;

/* loaded from: classes4.dex */
public class DefaultHttp2PingFrame implements Http2PingFrame {
    private final boolean ack;
    private final long content;

    public DefaultHttp2PingFrame(long content) {
        this(content, false);
    }

    public DefaultHttp2PingFrame(long content, boolean ack) {
        this.content = content;
        this.ack = ack;
    }

    @Override // io.netty.handler.codec.http2.Http2PingFrame
    public boolean ack() {
        return this.ack;
    }

    @Override // io.netty.handler.codec.http2.Http2Frame
    public String name() {
        return "PING";
    }

    @Override // io.netty.handler.codec.http2.Http2PingFrame
    public long content() {
        return this.content;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Http2PingFrame)) {
            return false;
        }
        Http2PingFrame other = (Http2PingFrame) o;
        return this.ack == other.ack() && this.content == other.content();
    }

    public int hashCode() {
        return (super.hashCode() * 31) + (this.ack ? 1 : 0);
    }

    public String toString() {
        return StringUtil.simpleClassName(this) + "(content=" + this.content + ", ack=" + this.ack + ')';
    }
}
