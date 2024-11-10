package io.netty.handler.ssl;

import io.netty.internal.tcnative.SSL;
import io.netty.util.AbstractReferenceCounted;
import io.netty.util.IllegalReferenceCountException;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.ResourceLeakDetectorFactory;
import io.netty.util.ResourceLeakTracker;
import java.security.cert.X509Certificate;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class DefaultOpenSslKeyMaterial extends AbstractReferenceCounted implements OpenSslKeyMaterial {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final ResourceLeakDetector<DefaultOpenSslKeyMaterial> leakDetector = ResourceLeakDetectorFactory.instance().newResourceLeakDetector(DefaultOpenSslKeyMaterial.class);
    private long chain;
    private final ResourceLeakTracker<DefaultOpenSslKeyMaterial> leak = leakDetector.track(this);
    private long privateKey;
    private final X509Certificate[] x509CertificateChain;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DefaultOpenSslKeyMaterial(long chain, long privateKey, X509Certificate[] x509CertificateChain) {
        this.chain = chain;
        this.privateKey = privateKey;
        this.x509CertificateChain = x509CertificateChain;
    }

    @Override // io.netty.handler.ssl.OpenSslKeyMaterial
    public X509Certificate[] certificateChain() {
        return (X509Certificate[]) this.x509CertificateChain.clone();
    }

    @Override // io.netty.handler.ssl.OpenSslKeyMaterial
    public long certificateChainAddress() {
        if (refCnt() <= 0) {
            throw new IllegalReferenceCountException();
        }
        return this.chain;
    }

    @Override // io.netty.handler.ssl.OpenSslKeyMaterial
    public long privateKeyAddress() {
        if (refCnt() <= 0) {
            throw new IllegalReferenceCountException();
        }
        return this.privateKey;
    }

    @Override // io.netty.util.AbstractReferenceCounted
    protected void deallocate() {
        SSL.freeX509Chain(this.chain);
        this.chain = 0L;
        SSL.freePrivateKey(this.privateKey);
        this.privateKey = 0L;
        if (this.leak != null) {
            boolean closed = this.leak.close(this);
            if (!closed) {
                throw new AssertionError();
            }
        }
    }

    @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public DefaultOpenSslKeyMaterial retain() {
        if (this.leak != null) {
            this.leak.record();
        }
        super.retain();
        return this;
    }

    @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public DefaultOpenSslKeyMaterial retain(int increment) {
        if (this.leak != null) {
            this.leak.record();
        }
        super.retain(increment);
        return this;
    }

    @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public DefaultOpenSslKeyMaterial touch() {
        if (this.leak != null) {
            this.leak.record();
        }
        super.touch();
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public DefaultOpenSslKeyMaterial touch(Object hint) {
        if (this.leak != null) {
            this.leak.record(hint);
        }
        return this;
    }

    @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public boolean release() {
        if (this.leak != null) {
            this.leak.record();
        }
        return super.release();
    }

    @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public boolean release(int decrement) {
        if (this.leak != null) {
            this.leak.record();
        }
        return super.release(decrement);
    }
}
