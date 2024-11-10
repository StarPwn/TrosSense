package io.netty.handler.ssl;

import io.netty.internal.tcnative.SSL;
import io.netty.internal.tcnative.SSLContext;
import java.util.concurrent.locks.Lock;

/* loaded from: classes4.dex */
public final class OpenSslServerSessionContext extends OpenSslSessionContext {
    /* JADX INFO: Access modifiers changed from: package-private */
    public OpenSslServerSessionContext(ReferenceCountedOpenSslContext context, OpenSslKeyMaterialProvider provider) {
        super(context, provider, SSL.SSL_SESS_CACHE_SERVER, new OpenSslSessionCache(context.engineMap));
    }

    public boolean setSessionIdContext(byte[] sidCtx) {
        Lock writerLock = this.context.ctxLock.writeLock();
        writerLock.lock();
        try {
            return SSLContext.setSessionIdContext(this.context.ctx, sidCtx);
        } finally {
            writerLock.unlock();
        }
    }
}
