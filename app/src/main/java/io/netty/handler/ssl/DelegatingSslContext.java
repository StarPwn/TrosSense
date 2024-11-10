package io.netty.handler.ssl;

import io.netty.buffer.ByteBufAllocator;
import io.netty.util.internal.ObjectUtil;
import java.util.List;
import java.util.concurrent.Executor;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSessionContext;

/* loaded from: classes4.dex */
public abstract class DelegatingSslContext extends SslContext {
    private final SslContext ctx;

    protected abstract void initEngine(SSLEngine sSLEngine);

    protected DelegatingSslContext(SslContext ctx) {
        this.ctx = (SslContext) ObjectUtil.checkNotNull(ctx, "ctx");
    }

    @Override // io.netty.handler.ssl.SslContext
    public final boolean isClient() {
        return this.ctx.isClient();
    }

    @Override // io.netty.handler.ssl.SslContext
    public final List<String> cipherSuites() {
        return this.ctx.cipherSuites();
    }

    @Override // io.netty.handler.ssl.SslContext
    public final long sessionCacheSize() {
        return this.ctx.sessionCacheSize();
    }

    @Override // io.netty.handler.ssl.SslContext
    public final long sessionTimeout() {
        return this.ctx.sessionTimeout();
    }

    @Override // io.netty.handler.ssl.SslContext
    public final ApplicationProtocolNegotiator applicationProtocolNegotiator() {
        return this.ctx.applicationProtocolNegotiator();
    }

    @Override // io.netty.handler.ssl.SslContext
    public final SSLEngine newEngine(ByteBufAllocator alloc) {
        SSLEngine engine = this.ctx.newEngine(alloc);
        initEngine(engine);
        return engine;
    }

    @Override // io.netty.handler.ssl.SslContext
    public final SSLEngine newEngine(ByteBufAllocator alloc, String peerHost, int peerPort) {
        SSLEngine engine = this.ctx.newEngine(alloc, peerHost, peerPort);
        initEngine(engine);
        return engine;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.ssl.SslContext
    public final SslHandler newHandler(ByteBufAllocator alloc, boolean startTls) {
        SslHandler handler = this.ctx.newHandler(alloc, startTls);
        initHandler(handler);
        return handler;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.ssl.SslContext
    public final SslHandler newHandler(ByteBufAllocator alloc, String peerHost, int peerPort, boolean startTls) {
        SslHandler handler = this.ctx.newHandler(alloc, peerHost, peerPort, startTls);
        initHandler(handler);
        return handler;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.ssl.SslContext
    public SslHandler newHandler(ByteBufAllocator alloc, boolean startTls, Executor executor) {
        SslHandler handler = this.ctx.newHandler(alloc, startTls, executor);
        initHandler(handler);
        return handler;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.ssl.SslContext
    public SslHandler newHandler(ByteBufAllocator alloc, String peerHost, int peerPort, boolean startTls, Executor executor) {
        SslHandler handler = this.ctx.newHandler(alloc, peerHost, peerPort, startTls, executor);
        initHandler(handler);
        return handler;
    }

    @Override // io.netty.handler.ssl.SslContext
    public final SSLSessionContext sessionContext() {
        return this.ctx.sessionContext();
    }

    protected void initHandler(SslHandler handler) {
        initEngine(handler.engine());
    }
}
