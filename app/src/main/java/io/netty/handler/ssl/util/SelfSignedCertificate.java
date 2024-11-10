package io.netty.handler.ssl.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.ThrowableUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;

/* loaded from: classes4.dex */
public final class SelfSignedCertificate {
    private final X509Certificate cert;
    private final File certificate;
    private final PrivateKey key;
    private final File privateKey;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) SelfSignedCertificate.class);
    private static final Date DEFAULT_NOT_BEFORE = new Date(SystemPropertyUtil.getLong("io.netty.selfSignedCertificate.defaultNotBefore", System.currentTimeMillis() - 31536000000L));
    private static final Date DEFAULT_NOT_AFTER = new Date(SystemPropertyUtil.getLong("io.netty.selfSignedCertificate.defaultNotAfter", 253402300799000L));
    private static final int DEFAULT_KEY_LENGTH_BITS = SystemPropertyUtil.getInt("io.netty.handler.ssl.util.selfSignedKeyStrength", 2048);

    public SelfSignedCertificate() throws CertificateException {
        this(DEFAULT_NOT_BEFORE, DEFAULT_NOT_AFTER, "RSA", DEFAULT_KEY_LENGTH_BITS);
    }

    public SelfSignedCertificate(Date notBefore, Date notAfter) throws CertificateException {
        this("localhost", notBefore, notAfter, "RSA", DEFAULT_KEY_LENGTH_BITS);
    }

    public SelfSignedCertificate(Date notBefore, Date notAfter, String algorithm, int bits) throws CertificateException {
        this("localhost", notBefore, notAfter, algorithm, bits);
    }

    public SelfSignedCertificate(String fqdn) throws CertificateException {
        this(fqdn, DEFAULT_NOT_BEFORE, DEFAULT_NOT_AFTER, "RSA", DEFAULT_KEY_LENGTH_BITS);
    }

    public SelfSignedCertificate(String fqdn, String algorithm, int bits) throws CertificateException {
        this(fqdn, DEFAULT_NOT_BEFORE, DEFAULT_NOT_AFTER, algorithm, bits);
    }

    public SelfSignedCertificate(String fqdn, Date notBefore, Date notAfter) throws CertificateException {
        this(fqdn, ThreadLocalInsecureRandom.current(), DEFAULT_KEY_LENGTH_BITS, notBefore, notAfter, "RSA");
    }

    public SelfSignedCertificate(String fqdn, Date notBefore, Date notAfter, String algorithm, int bits) throws CertificateException {
        this(fqdn, ThreadLocalInsecureRandom.current(), bits, notBefore, notAfter, algorithm);
    }

    public SelfSignedCertificate(String fqdn, SecureRandom random, int bits) throws CertificateException {
        this(fqdn, random, bits, DEFAULT_NOT_BEFORE, DEFAULT_NOT_AFTER, "RSA");
    }

    public SelfSignedCertificate(String fqdn, SecureRandom random, String algorithm, int bits) throws CertificateException {
        this(fqdn, random, bits, DEFAULT_NOT_BEFORE, DEFAULT_NOT_AFTER, algorithm);
    }

    public SelfSignedCertificate(String fqdn, SecureRandom random, int bits, Date notBefore, Date notAfter) throws CertificateException {
        this(fqdn, random, bits, notBefore, notAfter, "RSA");
    }

    public SelfSignedCertificate(String fqdn, SecureRandom random, int bits, Date notBefore, Date notAfter, String algorithm) throws CertificateException {
        KeyPairGenerator keyGen;
        String[] paths;
        if (!"EC".equalsIgnoreCase(algorithm) && !"RSA".equalsIgnoreCase(algorithm)) {
            throw new IllegalArgumentException("Algorithm not valid: " + algorithm);
        }
        try {
            keyGen = KeyPairGenerator.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            t2 = e;
        }
        try {
            keyGen.initialize(bits, random);
            KeyPair keypair = keyGen.generateKeyPair();
            try {
                String[] paths2 = BouncyCastleSelfSignedCertGenerator.generate(fqdn, keypair, random, notBefore, notAfter, algorithm);
                paths = paths2;
            } catch (Throwable t) {
                if (!isBouncyCastleAvailable()) {
                    logger.debug("Failed to generate a self-signed X.509 certificate because BouncyCastle PKIX is not available in classpath");
                } else {
                    logger.debug("Failed to generate a self-signed X.509 certificate using Bouncy Castle:", t);
                }
                try {
                    String[] paths3 = OpenJdkSelfSignedCertGenerator.generate(fqdn, keypair, random, notBefore, notAfter, algorithm);
                    paths = paths3;
                } catch (Throwable t2) {
                    logger.debug("Failed to generate a self-signed X.509 certificate using sun.security.x509:", t2);
                    CertificateException certificateException = new CertificateException("No provider succeeded to generate a self-signed certificate. See debug log for the root cause.", t2);
                    ThrowableUtil.addSuppressed(certificateException, t);
                    throw certificateException;
                }
            }
            this.certificate = new File(paths[0]);
            this.privateKey = new File(paths[1]);
            this.key = keypair.getPrivate();
            FileInputStream certificateInput = null;
            try {
                try {
                    certificateInput = new FileInputStream(this.certificate);
                    this.cert = (X509Certificate) CertificateFactory.getInstance("X509").generateCertificate(certificateInput);
                    try {
                        certificateInput.close();
                    } catch (IOException e2) {
                        if (logger.isWarnEnabled()) {
                            logger.warn("Failed to close a file: " + this.certificate, (Throwable) e2);
                        }
                    }
                } catch (Exception e3) {
                    throw new CertificateEncodingException(e3);
                }
            } finally {
            }
        } catch (NoSuchAlgorithmException e4) {
            t2 = e4;
            throw new Error(t2);
        }
    }

    public File certificate() {
        return this.certificate;
    }

    public File privateKey() {
        return this.privateKey;
    }

    public X509Certificate cert() {
        return this.cert;
    }

    public PrivateKey key() {
        return this.key;
    }

    public void delete() {
        safeDelete(this.certificate);
        safeDelete(this.privateKey);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String[] newSelfSignedCertificate(String fqdn, PrivateKey key, X509Certificate cert) throws IOException, CertificateEncodingException {
        ByteBuf wrappedBuf = Unpooled.wrappedBuffer(key.getEncoded());
        try {
            ByteBuf encodedBuf = Base64.encode(wrappedBuf, true);
            try {
                String keyText = "-----BEGIN PRIVATE KEY-----\n" + encodedBuf.toString(CharsetUtil.US_ASCII) + "\n-----END PRIVATE KEY-----\n";
                wrappedBuf.release();
                String fqdn2 = fqdn.replaceAll("[^\\w.-]", "x");
                File keyFile = PlatformDependent.createTempFile("keyutil_" + fqdn2 + '_', ".key", null);
                keyFile.deleteOnExit();
                OutputStream keyOut = new FileOutputStream(keyFile);
                try {
                    keyOut.write(keyText.getBytes(CharsetUtil.US_ASCII));
                    keyOut.close();
                    keyOut = null;
                    if (keyOut != null) {
                    }
                    wrappedBuf = Unpooled.wrappedBuffer(cert.getEncoded());
                    try {
                        encodedBuf = Base64.encode(wrappedBuf, true);
                        try {
                            String certText = "-----BEGIN CERTIFICATE-----\n" + encodedBuf.toString(CharsetUtil.US_ASCII) + "\n-----END CERTIFICATE-----\n";
                            wrappedBuf.release();
                            File certFile = PlatformDependent.createTempFile("keyutil_" + fqdn2 + '_', ".crt", null);
                            certFile.deleteOnExit();
                            OutputStream certOut = new FileOutputStream(certFile);
                            try {
                                certOut.write(certText.getBytes(CharsetUtil.US_ASCII));
                                certOut.close();
                                certOut = null;
                                if (certOut != null) {
                                }
                                return new String[]{certFile.getPath(), keyFile.getPath()};
                            } finally {
                                safeClose(certFile, certOut);
                                safeDelete(certFile);
                                safeDelete(keyFile);
                            }
                        } finally {
                        }
                    } finally {
                    }
                } finally {
                    safeClose(keyFile, keyOut);
                    safeDelete(keyFile);
                }
            } finally {
            }
        } finally {
        }
    }

    private static void safeDelete(File certFile) {
        if (!certFile.delete() && logger.isWarnEnabled()) {
            logger.warn("Failed to delete a file: " + certFile);
        }
    }

    private static void safeClose(File keyFile, OutputStream keyOut) {
        try {
            keyOut.close();
        } catch (IOException e) {
            if (logger.isWarnEnabled()) {
                logger.warn("Failed to close a file: " + keyFile, (Throwable) e);
            }
        }
    }

    private static boolean isBouncyCastleAvailable() {
        try {
            Class.forName("org.bouncycastle.cert.X509v3CertificateBuilder");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
