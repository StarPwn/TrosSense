package io.netty.handler.codec.http2;

/* loaded from: classes4.dex */
public final class Http2FrameStreamEvent {
    private final Http2FrameStream stream;
    private final Type type;

    /* loaded from: classes4.dex */
    public enum Type {
        State,
        Writability
    }

    private Http2FrameStreamEvent(Http2FrameStream stream, Type type) {
        this.stream = stream;
        this.type = type;
    }

    public Http2FrameStream stream() {
        return this.stream;
    }

    public Type type() {
        return this.type;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Http2FrameStreamEvent stateChanged(Http2FrameStream stream) {
        return new Http2FrameStreamEvent(stream, Type.State);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Http2FrameStreamEvent writabilityChanged(Http2FrameStream stream) {
        return new Http2FrameStreamEvent(stream, Type.Writability);
    }
}
