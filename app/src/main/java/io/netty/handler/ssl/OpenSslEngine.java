package io.netty.handler.ssl;

import io.netty.buffer.ByteBufAllocator;

/* loaded from: classes4.dex */
public final class OpenSslEngine extends ReferenceCountedOpenSslEngine {
    /* JADX INFO: Access modifiers changed from: package-private */
    public OpenSslEngine(OpenSslContext context, ByteBufAllocator alloc, String peerHost, int peerPort, boolean jdkCompatibilityMode) {
        super(context, alloc, peerHost, peerPort, jdkCompatibilityMode, false);
    }

    protected void finalize() throws Throwable {
        super.finalize();
        OpenSsl.releaseIfNeeded(this);
    }
}
