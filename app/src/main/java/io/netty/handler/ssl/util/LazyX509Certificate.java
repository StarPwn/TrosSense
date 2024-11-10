package io.netty.handler.ssl.util;

import io.netty.util.internal.ObjectUtil;
import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Principal;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.security.auth.x500.X500Principal;

/* loaded from: classes4.dex */
public final class LazyX509Certificate extends X509Certificate {
    static final CertificateFactory X509_CERT_FACTORY;
    private final byte[] bytes;
    private X509Certificate wrapped;

    static {
        try {
            X509_CERT_FACTORY = CertificateFactory.getInstance("X.509");
        } catch (CertificateException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public LazyX509Certificate(byte[] bytes) {
        this.bytes = (byte[]) ObjectUtil.checkNotNull(bytes, "bytes");
    }

    @Override // java.security.cert.X509Certificate
    public void checkValidity() throws CertificateExpiredException, CertificateNotYetValidException {
        unwrap().checkValidity();
    }

    @Override // java.security.cert.X509Certificate
    public void checkValidity(Date date) throws CertificateExpiredException, CertificateNotYetValidException {
        unwrap().checkValidity(date);
    }

    @Override // java.security.cert.X509Certificate
    public X500Principal getIssuerX500Principal() {
        return unwrap().getIssuerX500Principal();
    }

    @Override // java.security.cert.X509Certificate
    public X500Principal getSubjectX500Principal() {
        return unwrap().getSubjectX500Principal();
    }

    @Override // java.security.cert.X509Certificate
    public List<String> getExtendedKeyUsage() throws CertificateParsingException {
        return unwrap().getExtendedKeyUsage();
    }

    @Override // java.security.cert.X509Certificate
    public Collection<List<?>> getSubjectAlternativeNames() throws CertificateParsingException {
        return unwrap().getSubjectAlternativeNames();
    }

    @Override // java.security.cert.X509Certificate
    public Collection<List<?>> getIssuerAlternativeNames() throws CertificateParsingException {
        return unwrap().getIssuerAlternativeNames();
    }

    @Override // java.security.cert.X509Certificate, java.security.cert.Certificate
    public void verify(PublicKey key, Provider sigProvider) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        unwrap().verify(key, sigProvider);
    }

    @Override // java.security.cert.X509Certificate
    public int getVersion() {
        return unwrap().getVersion();
    }

    @Override // java.security.cert.X509Certificate
    public BigInteger getSerialNumber() {
        return unwrap().getSerialNumber();
    }

    @Override // java.security.cert.X509Certificate
    public Principal getIssuerDN() {
        return unwrap().getIssuerDN();
    }

    @Override // java.security.cert.X509Certificate
    public Principal getSubjectDN() {
        return unwrap().getSubjectDN();
    }

    @Override // java.security.cert.X509Certificate
    public Date getNotBefore() {
        return unwrap().getNotBefore();
    }

    @Override // java.security.cert.X509Certificate
    public Date getNotAfter() {
        return unwrap().getNotAfter();
    }

    @Override // java.security.cert.X509Certificate
    public byte[] getTBSCertificate() throws CertificateEncodingException {
        return unwrap().getTBSCertificate();
    }

    @Override // java.security.cert.X509Certificate
    public byte[] getSignature() {
        return unwrap().getSignature();
    }

    @Override // java.security.cert.X509Certificate
    public String getSigAlgName() {
        return unwrap().getSigAlgName();
    }

    @Override // java.security.cert.X509Certificate
    public String getSigAlgOID() {
        return unwrap().getSigAlgOID();
    }

    @Override // java.security.cert.X509Certificate
    public byte[] getSigAlgParams() {
        return unwrap().getSigAlgParams();
    }

    @Override // java.security.cert.X509Certificate
    public boolean[] getIssuerUniqueID() {
        return unwrap().getIssuerUniqueID();
    }

    @Override // java.security.cert.X509Certificate
    public boolean[] getSubjectUniqueID() {
        return unwrap().getSubjectUniqueID();
    }

    @Override // java.security.cert.X509Certificate
    public boolean[] getKeyUsage() {
        return unwrap().getKeyUsage();
    }

    @Override // java.security.cert.X509Certificate
    public int getBasicConstraints() {
        return unwrap().getBasicConstraints();
    }

    @Override // java.security.cert.Certificate
    public byte[] getEncoded() {
        return (byte[]) this.bytes.clone();
    }

    @Override // java.security.cert.Certificate
    public void verify(PublicKey key) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
        unwrap().verify(key);
    }

    @Override // java.security.cert.Certificate
    public void verify(PublicKey key, String sigProvider) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
        unwrap().verify(key, sigProvider);
    }

    @Override // java.security.cert.Certificate
    public String toString() {
        return unwrap().toString();
    }

    @Override // java.security.cert.Certificate
    public PublicKey getPublicKey() {
        return unwrap().getPublicKey();
    }

    @Override // java.security.cert.X509Extension
    public boolean hasUnsupportedCriticalExtension() {
        return unwrap().hasUnsupportedCriticalExtension();
    }

    @Override // java.security.cert.X509Extension
    public Set<String> getCriticalExtensionOIDs() {
        return unwrap().getCriticalExtensionOIDs();
    }

    @Override // java.security.cert.X509Extension
    public Set<String> getNonCriticalExtensionOIDs() {
        return unwrap().getNonCriticalExtensionOIDs();
    }

    @Override // java.security.cert.X509Extension
    public byte[] getExtensionValue(String oid) {
        return unwrap().getExtensionValue(oid);
    }

    private X509Certificate unwrap() {
        X509Certificate wrapped = this.wrapped;
        if (wrapped == null) {
            try {
                X509Certificate wrapped2 = (X509Certificate) X509_CERT_FACTORY.generateCertificate(new ByteArrayInputStream(this.bytes));
                this.wrapped = wrapped2;
                return wrapped2;
            } catch (CertificateException e) {
                throw new IllegalStateException(e);
            }
        }
        return wrapped;
    }
}
