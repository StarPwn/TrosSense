package io.netty.handler.codec;

/* loaded from: classes4.dex */
public class CodecException extends RuntimeException {
    private static final long serialVersionUID = -1464830400709348473L;

    public CodecException() {
    }

    public CodecException(String message, Throwable cause) {
        super(message, cause);
    }

    public CodecException(String message) {
        super(message);
    }

    public CodecException(Throwable cause) {
        super(cause);
    }
}
