package io.netty.handler.ssl;

import androidx.core.os.EnvironmentCompat;
import io.netty.internal.tcnative.SSL;
import io.netty.util.AbstractReferenceCounted;
import io.netty.util.IllegalReferenceCountException;
import io.netty.util.internal.EmptyArrays;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class OpenSslPrivateKey extends AbstractReferenceCounted implements PrivateKey {
    private long privateKeyAddress;

    /* JADX INFO: Access modifiers changed from: package-private */
    public OpenSslPrivateKey(long privateKeyAddress) {
        this.privateKeyAddress = privateKeyAddress;
    }

    @Override // java.security.Key
    public String getAlgorithm() {
        return EnvironmentCompat.MEDIA_UNKNOWN;
    }

    @Override // java.security.Key
    public String getFormat() {
        return null;
    }

    @Override // java.security.Key
    public byte[] getEncoded() {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long privateKeyAddress() {
        if (refCnt() <= 0) {
            throw new IllegalReferenceCountException();
        }
        return this.privateKeyAddress;
    }

    @Override // io.netty.util.AbstractReferenceCounted
    protected void deallocate() {
        SSL.freePrivateKey(this.privateKeyAddress);
        this.privateKeyAddress = 0L;
    }

    @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public OpenSslPrivateKey retain() {
        super.retain();
        return this;
    }

    @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public OpenSslPrivateKey retain(int increment) {
        super.retain(increment);
        return this;
    }

    @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public OpenSslPrivateKey touch() {
        super.touch();
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public OpenSslPrivateKey touch(Object hint) {
        return this;
    }

    @Override // javax.security.auth.Destroyable
    public void destroy() {
        release(refCnt());
    }

    @Override // javax.security.auth.Destroyable
    public boolean isDestroyed() {
        return refCnt() == 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public OpenSslKeyMaterial newKeyMaterial(long certificateChain, X509Certificate[] chain) {
        return new OpenSslPrivateKeyMaterial(certificateChain, chain);
    }

    /* loaded from: classes4.dex */
    final class OpenSslPrivateKeyMaterial extends AbstractReferenceCounted implements OpenSslKeyMaterial {
        long certificateChain;
        private final X509Certificate[] x509CertificateChain;

        OpenSslPrivateKeyMaterial(long certificateChain, X509Certificate[] x509CertificateChain) {
            this.certificateChain = certificateChain;
            this.x509CertificateChain = x509CertificateChain == null ? EmptyArrays.EMPTY_X509_CERTIFICATES : x509CertificateChain;
            OpenSslPrivateKey.this.retain();
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
            return this.certificateChain;
        }

        @Override // io.netty.handler.ssl.OpenSslKeyMaterial
        public long privateKeyAddress() {
            if (refCnt() > 0) {
                return OpenSslPrivateKey.this.privateKeyAddress();
            }
            throw new IllegalReferenceCountException();
        }

        @Override // io.netty.util.ReferenceCounted
        public OpenSslKeyMaterial touch(Object hint) {
            OpenSslPrivateKey.this.touch(hint);
            return this;
        }

        @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
        public OpenSslKeyMaterial retain() {
            super.retain();
            return this;
        }

        @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
        public OpenSslKeyMaterial retain(int increment) {
            super.retain(increment);
            return this;
        }

        @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
        public OpenSslKeyMaterial touch() {
            OpenSslPrivateKey.this.touch();
            return this;
        }

        @Override // io.netty.util.AbstractReferenceCounted
        protected void deallocate() {
            releaseChain();
            OpenSslPrivateKey.this.release();
        }

        private void releaseChain() {
            SSL.freeX509Chain(this.certificateChain);
            this.certificateChain = 0L;
        }
    }
}
