package io.netty.handler.codec.http2;

/* loaded from: classes4.dex */
public final class DefaultHttp2PriorityFrame extends AbstractHttp2StreamFrame implements Http2PriorityFrame {
    private final boolean exclusive;
    private final int streamDependency;
    private final short weight;

    public DefaultHttp2PriorityFrame(int streamDependency, short weight, boolean exclusive) {
        this.streamDependency = streamDependency;
        this.weight = weight;
        this.exclusive = exclusive;
    }

    @Override // io.netty.handler.codec.http2.Http2PriorityFrame
    public int streamDependency() {
        return this.streamDependency;
    }

    @Override // io.netty.handler.codec.http2.Http2PriorityFrame
    public short weight() {
        return this.weight;
    }

    @Override // io.netty.handler.codec.http2.Http2PriorityFrame
    public boolean exclusive() {
        return this.exclusive;
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2StreamFrame, io.netty.handler.codec.http2.Http2StreamFrame
    public DefaultHttp2PriorityFrame stream(Http2FrameStream stream) {
        super.stream(stream);
        return this;
    }

    @Override // io.netty.handler.codec.http2.Http2Frame
    public String name() {
        return "PRIORITY_FRAME";
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2StreamFrame
    public boolean equals(Object o) {
        if (!(o instanceof DefaultHttp2PriorityFrame)) {
            return false;
        }
        DefaultHttp2PriorityFrame other = (DefaultHttp2PriorityFrame) o;
        boolean same = super.equals(other);
        return same && this.streamDependency == other.streamDependency && this.weight == other.weight && this.exclusive == other.exclusive;
    }

    @Override // io.netty.handler.codec.http2.AbstractHttp2StreamFrame
    public int hashCode() {
        return (((((super.hashCode() * 31) + this.streamDependency) * 31) + this.weight) * 31) + (this.exclusive ? 1 : 0);
    }

    public String toString() {
        return "DefaultHttp2PriorityFrame(stream=" + stream() + ", streamDependency=" + this.streamDependency + ", weight=" + ((int) this.weight) + ", exclusive=" + this.exclusive + ')';
    }
}
