package io.netty.handler.ssl;

import javax.net.ssl.SSLException;

/* loaded from: classes4.dex */
public final class SslClosedEngineException extends SSLException {
    private static final long serialVersionUID = -5204207600474401904L;

    public SslClosedEngineException(String reason) {
        super(reason);
    }
}
