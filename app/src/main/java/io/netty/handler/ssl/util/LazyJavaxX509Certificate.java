package io.netty.handler.ssl.util;

import io.netty.util.internal.ObjectUtil;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Principal;
import java.security.PublicKey;
import java.security.SignatureException;
import java.util.Date;
import javax.security.cert.CertificateException;
import javax.security.cert.CertificateExpiredException;
import javax.security.cert.CertificateNotYetValidException;
import javax.security.cert.X509Certificate;

/* loaded from: classes4.dex */
public final class LazyJavaxX509Certificate extends X509Certificate {
    private final byte[] bytes;
    private X509Certificate wrapped;

    public LazyJavaxX509Certificate(byte[] bytes) {
        this.bytes = (byte[]) ObjectUtil.checkNotNull(bytes, "bytes");
    }

    @Override // javax.security.cert.X509Certificate
    public void checkValidity() throws CertificateExpiredException, CertificateNotYetValidException {
        unwrap().checkValidity();
    }

    @Override // javax.security.cert.X509Certificate
    public void checkValidity(Date date) throws CertificateExpiredException, CertificateNotYetValidException {
        unwrap().checkValidity(date);
    }

    @Override // javax.security.cert.X509Certificate
    public int getVersion() {
        return unwrap().getVersion();
    }

    @Override // javax.security.cert.X509Certificate
    public BigInteger getSerialNumber() {
        return unwrap().getSerialNumber();
    }

    @Override // javax.security.cert.X509Certificate
    public Principal getIssuerDN() {
        return unwrap().getIssuerDN();
    }

    @Override // javax.security.cert.X509Certificate
    public Principal getSubjectDN() {
        return unwrap().getSubjectDN();
    }

    @Override // javax.security.cert.X509Certificate
    public Date getNotBefore() {
        return unwrap().getNotBefore();
    }

    @Override // javax.security.cert.X509Certificate
    public Date getNotAfter() {
        return unwrap().getNotAfter();
    }

    @Override // javax.security.cert.X509Certificate
    public String getSigAlgName() {
        return unwrap().getSigAlgName();
    }

    @Override // javax.security.cert.X509Certificate
    public String getSigAlgOID() {
        return unwrap().getSigAlgOID();
    }

    @Override // javax.security.cert.X509Certificate
    public byte[] getSigAlgParams() {
        return unwrap().getSigAlgParams();
    }

    @Override // javax.security.cert.Certificate
    public byte[] getEncoded() {
        return (byte[]) this.bytes.clone();
    }

    byte[] getBytes() {
        return this.bytes;
    }

    @Override // javax.security.cert.Certificate
    public void verify(PublicKey key) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
        unwrap().verify(key);
    }

    @Override // javax.security.cert.Certificate
    public void verify(PublicKey key, String sigProvider) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
        unwrap().verify(key, sigProvider);
    }

    @Override // javax.security.cert.Certificate
    public String toString() {
        return unwrap().toString();
    }

    @Override // javax.security.cert.Certificate
    public PublicKey getPublicKey() {
        return unwrap().getPublicKey();
    }

    private X509Certificate unwrap() {
        X509Certificate wrapped = this.wrapped;
        if (wrapped == null) {
            try {
                X509Certificate wrapped2 = X509Certificate.getInstance(this.bytes);
                this.wrapped = wrapped2;
                return wrapped2;
            } catch (CertificateException e) {
                throw new IllegalStateException(e);
            }
        }
        return wrapped;
    }
}
