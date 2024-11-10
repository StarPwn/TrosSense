package io.netty.handler.codec;

/* loaded from: classes4.dex */
public class DecoderException extends CodecException {
    private static final long serialVersionUID = 6926716840699621852L;

    public DecoderException() {
    }

    public DecoderException(String message, Throwable cause) {
        super(message, cause);
    }

    public DecoderException(String message) {
        super(message);
    }

    public DecoderException(Throwable cause) {
        super(cause);
    }
}
