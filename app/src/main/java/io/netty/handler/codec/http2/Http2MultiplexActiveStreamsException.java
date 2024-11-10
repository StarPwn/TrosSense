package io.netty.handler.codec.http2;

/* loaded from: classes4.dex */
public final class Http2MultiplexActiveStreamsException extends Exception {
    public Http2MultiplexActiveStreamsException(Throwable cause) {
        super(cause);
    }

    @Override // java.lang.Throwable
    public Throwable fillInStackTrace() {
        return this;
    }
}
