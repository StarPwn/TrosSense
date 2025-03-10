package io.netty.handler.codec;

/* loaded from: classes4.dex */
public class PrematureChannelClosureException extends CodecException {
    private static final long serialVersionUID = 4907642202594703094L;

    public PrematureChannelClosureException() {
    }

    public PrematureChannelClosureException(String message, Throwable cause) {
        super(message, cause);
    }

    public PrematureChannelClosureException(String message) {
        super(message);
    }

    public PrematureChannelClosureException(Throwable cause) {
        super(cause);
    }
}
