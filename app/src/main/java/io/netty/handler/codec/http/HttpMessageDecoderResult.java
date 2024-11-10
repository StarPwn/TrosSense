package io.netty.handler.codec.http;

import io.netty.handler.codec.DecoderResult;

/* loaded from: classes4.dex */
public final class HttpMessageDecoderResult extends DecoderResult {
    private final int headerSize;
    private final int initialLineLength;

    /* JADX INFO: Access modifiers changed from: package-private */
    public HttpMessageDecoderResult(int initialLineLength, int headerSize) {
        super(SIGNAL_SUCCESS);
        this.initialLineLength = initialLineLength;
        this.headerSize = headerSize;
    }

    public int initialLineLength() {
        return this.initialLineLength;
    }

    public int headerSize() {
        return this.headerSize;
    }

    public int totalSize() {
        return this.initialLineLength + this.headerSize;
    }
}
