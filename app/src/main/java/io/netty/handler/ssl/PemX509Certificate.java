package io.netty.handler.ssl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import io.netty.util.IllegalReferenceCountException;
import io.netty.util.internal.ObjectUtil;
import java.math.BigInteger;
import java.security.Principal;
import java.security.PublicKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

/* loaded from: classes4.dex */
public final class PemX509Certificate extends X509Certificate implements PemEncoded {
    private static final byte[] BEGIN_CERT = "-----BEGIN CERTIFICATE-----\n".getBytes(CharsetUtil.US_ASCII);
    private static final byte[] END_CERT = "\n-----END CERTIFICATE-----\n".getBytes(CharsetUtil.US_ASCII);
    private final ByteBuf content;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    public static PemEncoded toPEM(ByteBufAllocator allocator, boolean useDirect, X509Certificate... x509CertificateArr) throws CertificateEncodingException {
        ObjectUtil.checkNonEmpty(x509CertificateArr, "chain");
        if (x509CertificateArr.length == 1) {
            Object[] objArr = x509CertificateArr[0];
            if (objArr instanceof PemEncoded) {
                return ((PemEncoded) objArr).retain();
            }
        }
        boolean success = null;
        ByteBuf pem = null;
        try {
            for (PemX509Certificate pemX509Certificate : x509CertificateArr) {
                if (pemX509Certificate == 0) {
                    throw new IllegalArgumentException("Null element in chain: " + Arrays.toString(x509CertificateArr));
                }
                if (pemX509Certificate instanceof PemEncoded) {
                    pem = append(allocator, useDirect, (PemEncoded) pemX509Certificate, x509CertificateArr.length, pem);
                } else {
                    pem = append(allocator, useDirect, (X509Certificate) pemX509Certificate, x509CertificateArr.length, pem);
                }
            }
            PemValue value = new PemValue(pem, false);
            success = true;
            return value;
        } finally {
            if (success == null && 0 != 0) {
                pem.release();
            }
        }
    }

    private static ByteBuf append(ByteBufAllocator allocator, boolean useDirect, PemEncoded encoded, int count, ByteBuf pem) {
        ByteBuf content = encoded.content();
        if (pem == null) {
            pem = newBuffer(allocator, useDirect, content.readableBytes() * count);
        }
        pem.writeBytes(content.slice());
        return pem;
    }

    private static ByteBuf append(ByteBufAllocator allocator, boolean useDirect, X509Certificate cert, int count, ByteBuf pem) throws CertificateEncodingException {
        ByteBuf encoded = Unpooled.wrappedBuffer(cert.getEncoded());
        try {
            ByteBuf base64 = SslUtils.toBase64(allocator, encoded);
            if (pem == null) {
                try {
                    pem = newBuffer(allocator, useDirect, (BEGIN_CERT.length + base64.readableBytes() + END_CERT.length) * count);
                } finally {
                    base64.release();
                }
            }
            pem.writeBytes(BEGIN_CERT);
            pem.writeBytes(base64);
            pem.writeBytes(END_CERT);
            return pem;
        } finally {
            encoded.release();
        }
    }

    private static ByteBuf newBuffer(ByteBufAllocator allocator, boolean useDirect, int initialCapacity) {
        return useDirect ? allocator.directBuffer(initialCapacity) : allocator.buffer(initialCapacity);
    }

    public static PemX509Certificate valueOf(byte[] key) {
        return valueOf(Unpooled.wrappedBuffer(key));
    }

    public static PemX509Certificate valueOf(ByteBuf key) {
        return new PemX509Certificate(key);
    }

    private PemX509Certificate(ByteBuf content) {
        this.content = (ByteBuf) ObjectUtil.checkNotNull(content, "content");
    }

    @Override // io.netty.handler.ssl.PemEncoded
    public boolean isSensitive() {
        return false;
    }

    @Override // io.netty.util.ReferenceCounted
    public int refCnt() {
        return this.content.refCnt();
    }

    @Override // io.netty.buffer.ByteBufHolder
    public ByteBuf content() {
        int count = refCnt();
        if (count <= 0) {
            throw new IllegalReferenceCountException(count);
        }
        return this.content;
    }

    @Override // io.netty.handler.ssl.PemEncoded, io.netty.buffer.ByteBufHolder
    public PemX509Certificate copy() {
        return replace(this.content.copy());
    }

    @Override // io.netty.handler.ssl.PemEncoded, io.netty.buffer.ByteBufHolder
    public PemX509Certificate duplicate() {
        return replace(this.content.duplicate());
    }

    @Override // io.netty.handler.ssl.PemEncoded, io.netty.buffer.ByteBufHolder
    public PemX509Certificate retainedDuplicate() {
        return replace(this.content.retainedDuplicate());
    }

    @Override // io.netty.handler.ssl.PemEncoded, io.netty.buffer.ByteBufHolder
    public PemX509Certificate replace(ByteBuf content) {
        return new PemX509Certificate(content);
    }

    @Override // io.netty.util.ReferenceCounted
    public PemX509Certificate retain() {
        this.content.retain();
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public PemX509Certificate retain(int increment) {
        this.content.retain(increment);
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public PemX509Certificate touch() {
        this.content.touch();
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public PemX509Certificate touch(Object hint) {
        this.content.touch(hint);
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public boolean release() {
        return this.content.release();
    }

    @Override // io.netty.util.ReferenceCounted
    public boolean release(int decrement) {
        return this.content.release(decrement);
    }

    @Override // java.security.cert.Certificate
    public byte[] getEncoded() {
        throw new UnsupportedOperationException();
    }

    @Override // java.security.cert.X509Extension
    public boolean hasUnsupportedCriticalExtension() {
        throw new UnsupportedOperationException();
    }

    @Override // java.security.cert.X509Extension
    public Set<String> getCriticalExtensionOIDs() {
        throw new UnsupportedOperationException();
    }

    @Override // java.security.cert.X509Extension
    public Set<String> getNonCriticalExtensionOIDs() {
        throw new UnsupportedOperationException();
    }

    @Override // java.security.cert.X509Extension
    public byte[] getExtensionValue(String oid) {
        throw new UnsupportedOperationException();
    }

    @Override // java.security.cert.X509Certificate
    public void checkValidity() {
        throw new UnsupportedOperationException();
    }

    @Override // java.security.cert.X509Certificate
    public void checkValidity(Date date) {
        throw new UnsupportedOperationException();
    }

    @Override // java.security.cert.X509Certificate
    public int getVersion() {
        throw new UnsupportedOperationException();
    }

    @Override // java.security.cert.X509Certificate
    public BigInteger getSerialNumber() {
        throw new UnsupportedOperationException();
    }

    @Override // java.security.cert.X509Certificate
    public Principal getIssuerDN() {
        throw new UnsupportedOperationException();
    }

    @Override // java.security.cert.X509Certificate
    public Principal getSubjectDN() {
        throw new UnsupportedOperationException();
    }

    @Override // java.security.cert.X509Certificate
    public Date getNotBefore() {
        throw new UnsupportedOperationException();
    }

    @Override // java.security.cert.X509Certificate
    public Date getNotAfter() {
        throw new UnsupportedOperationException();
    }

    @Override // java.security.cert.X509Certificate
    public byte[] getTBSCertificate() {
        throw new UnsupportedOperationException();
    }

    @Override // java.security.cert.X509Certificate
    public byte[] getSignature() {
        throw new UnsupportedOperationException();
    }

    @Override // java.security.cert.X509Certificate
    public String getSigAlgName() {
        throw new UnsupportedOperationException();
    }

    @Override // java.security.cert.X509Certificate
    public String getSigAlgOID() {
        throw new UnsupportedOperationException();
    }

    @Override // java.security.cert.X509Certificate
    public byte[] getSigAlgParams() {
        throw new UnsupportedOperationException();
    }

    @Override // java.security.cert.X509Certificate
    public boolean[] getIssuerUniqueID() {
        throw new UnsupportedOperationException();
    }

    @Override // java.security.cert.X509Certificate
    public boolean[] getSubjectUniqueID() {
        throw new UnsupportedOperationException();
    }

    @Override // java.security.cert.X509Certificate
    public boolean[] getKeyUsage() {
        throw new UnsupportedOperationException();
    }

    @Override // java.security.cert.X509Certificate
    public int getBasicConstraints() {
        throw new UnsupportedOperationException();
    }

    @Override // java.security.cert.Certificate
    public void verify(PublicKey key) {
        throw new UnsupportedOperationException();
    }

    @Override // java.security.cert.Certificate
    public void verify(PublicKey key, String sigProvider) {
        throw new UnsupportedOperationException();
    }

    @Override // java.security.cert.Certificate
    public PublicKey getPublicKey() {
        throw new UnsupportedOperationException();
    }

    @Override // java.security.cert.Certificate
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PemX509Certificate)) {
            return false;
        }
        PemX509Certificate other = (PemX509Certificate) o;
        return this.content.equals(other.content);
    }

    @Override // java.security.cert.Certificate
    public int hashCode() {
        return this.content.hashCode();
    }

    @Override // java.security.cert.Certificate
    public String toString() {
        return this.content.toString(CharsetUtil.UTF_8);
    }
}
