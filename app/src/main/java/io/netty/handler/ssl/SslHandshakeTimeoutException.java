package io.netty.handler.ssl;

import javax.net.ssl.SSLHandshakeException;

/* loaded from: classes4.dex */
public final class SslHandshakeTimeoutException extends SSLHandshakeException {
    public SslHandshakeTimeoutException(String reason) {
        super(reason);
    }
}
