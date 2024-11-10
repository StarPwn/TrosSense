package io.netty.handler.ssl.util;

import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.AccessController;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PrivilegedAction;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.Date;
import sun.security.x509.AlgorithmId;
import sun.security.x509.CertificateAlgorithmId;
import sun.security.x509.CertificateSerialNumber;
import sun.security.x509.CertificateSubjectName;
import sun.security.x509.CertificateValidity;
import sun.security.x509.CertificateVersion;
import sun.security.x509.CertificateX509Key;
import sun.security.x509.X500Name;
import sun.security.x509.X509CertImpl;
import sun.security.x509.X509CertInfo;

/* loaded from: classes4.dex */
final class OpenJdkSelfSignedCertGenerator {
    private static final Constructor<X509CertImpl> CERT_IMPL_CONSTRUCTOR;
    private static final Method CERT_IMPL_GET_METHOD;
    private static final Method CERT_IMPL_SIGN_METHOD;
    private static final Method CERT_INFO_SET_METHOD;
    private static final Constructor<?> ISSUER_NAME_CONSTRUCTOR;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) OpenJdkSelfSignedCertGenerator.class);

    static {
        Object maybeCertInfoSetMethod;
        Method certInfoSetMethod = null;
        Constructor<?> issuerNameConstructor = null;
        Constructor<X509CertImpl> certImplConstructor = null;
        Method certImplGetMethod = null;
        Method certImplSignMethod = null;
        try {
            maybeCertInfoSetMethod = AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: io.netty.handler.ssl.util.OpenJdkSelfSignedCertGenerator.1
                @Override // java.security.PrivilegedAction
                public Object run() {
                    try {
                        return X509CertInfo.class.getMethod("set", String.class, Object.class);
                    } catch (Throwable cause) {
                        return cause;
                    }
                }
            });
        } catch (Throwable cause) {
            logger.debug(OpenJdkSelfSignedCertGenerator.class.getSimpleName() + " not supported", cause);
        }
        if (maybeCertInfoSetMethod instanceof Method) {
            certInfoSetMethod = (Method) maybeCertInfoSetMethod;
            Object maybeIssuerNameConstructor = AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: io.netty.handler.ssl.util.OpenJdkSelfSignedCertGenerator.2
                @Override // java.security.PrivilegedAction
                public Object run() {
                    try {
                        Class<?> issuerName = Class.forName("sun.security.x509.CertificateIssuerName", false, PlatformDependent.getClassLoader(OpenJdkSelfSignedCertGenerator.class));
                        return issuerName.getConstructor(X500Name.class);
                    } catch (Throwable cause2) {
                        return cause2;
                    }
                }
            });
            if (maybeIssuerNameConstructor instanceof Constructor) {
                issuerNameConstructor = (Constructor) maybeIssuerNameConstructor;
                Object maybeCertImplConstructor = AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: io.netty.handler.ssl.util.OpenJdkSelfSignedCertGenerator.3
                    @Override // java.security.PrivilegedAction
                    public Object run() {
                        try {
                            return X509CertImpl.class.getConstructor(X509CertInfo.class);
                        } catch (Throwable cause2) {
                            return cause2;
                        }
                    }
                });
                if (maybeCertImplConstructor instanceof Constructor) {
                    Constructor<X509CertImpl> constructor = (Constructor) maybeCertImplConstructor;
                    certImplConstructor = constructor;
                    Object maybeCertImplGetMethod = AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: io.netty.handler.ssl.util.OpenJdkSelfSignedCertGenerator.4
                        @Override // java.security.PrivilegedAction
                        public Object run() {
                            try {
                                return X509CertImpl.class.getMethod("get", String.class);
                            } catch (Throwable cause2) {
                                return cause2;
                            }
                        }
                    });
                    if (maybeCertImplGetMethod instanceof Method) {
                        certImplGetMethod = (Method) maybeCertImplGetMethod;
                        Object maybeCertImplSignMethod = AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: io.netty.handler.ssl.util.OpenJdkSelfSignedCertGenerator.5
                            @Override // java.security.PrivilegedAction
                            public Object run() {
                                try {
                                    return X509CertImpl.class.getMethod("sign", PrivateKey.class, String.class);
                                } catch (Throwable cause2) {
                                    return cause2;
                                }
                            }
                        });
                        if (maybeCertImplSignMethod instanceof Method) {
                            certImplSignMethod = (Method) maybeCertImplSignMethod;
                            CERT_INFO_SET_METHOD = certInfoSetMethod;
                            ISSUER_NAME_CONSTRUCTOR = issuerNameConstructor;
                            CERT_IMPL_CONSTRUCTOR = certImplConstructor;
                            CERT_IMPL_GET_METHOD = certImplGetMethod;
                            CERT_IMPL_SIGN_METHOD = certImplSignMethod;
                            return;
                        }
                        throw ((Throwable) maybeCertImplSignMethod);
                    }
                    throw ((Throwable) maybeCertImplGetMethod);
                }
                throw ((Throwable) maybeCertImplConstructor);
            }
            throw ((Throwable) maybeIssuerNameConstructor);
        }
        throw ((Throwable) maybeCertInfoSetMethod);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String[] generate(String fqdn, KeyPair keypair, SecureRandom random, Date notBefore, Date notAfter, String algorithm) throws Exception {
        if (CERT_INFO_SET_METHOD == null || ISSUER_NAME_CONSTRUCTOR == null || CERT_IMPL_CONSTRUCTOR == null || CERT_IMPL_GET_METHOD == null || CERT_IMPL_SIGN_METHOD == null) {
            throw new UnsupportedOperationException(OpenJdkSelfSignedCertGenerator.class.getSimpleName() + " not supported on the used JDK version");
        }
        PrivateKey key = keypair.getPrivate();
        X509CertInfo info = new X509CertInfo();
        X500Name owner = new X500Name("CN=" + fqdn);
        CERT_INFO_SET_METHOD.invoke(info, "version", new CertificateVersion(2));
        CERT_INFO_SET_METHOD.invoke(info, "serialNumber", new CertificateSerialNumber(new BigInteger(64, random)));
        try {
            CERT_INFO_SET_METHOD.invoke(info, "subject", new CertificateSubjectName(owner));
        } catch (InvocationTargetException ex) {
            if (!(ex.getCause() instanceof CertificateException)) {
                throw ex;
            }
            CERT_INFO_SET_METHOD.invoke(info, "subject", owner);
        }
        try {
            CERT_INFO_SET_METHOD.invoke(info, "issuer", ISSUER_NAME_CONSTRUCTOR.newInstance(owner));
        } catch (InvocationTargetException ex2) {
            if (!(ex2.getCause() instanceof CertificateException)) {
                throw ex2;
            }
            CERT_INFO_SET_METHOD.invoke(info, "issuer", owner);
        }
        CERT_INFO_SET_METHOD.invoke(info, "validity", new CertificateValidity(notBefore, notAfter));
        CERT_INFO_SET_METHOD.invoke(info, "key", new CertificateX509Key(keypair.getPublic()));
        CERT_INFO_SET_METHOD.invoke(info, "algorithmID", new CertificateAlgorithmId(AlgorithmId.get("1.2.840.113549.1.1.11")));
        X509CertImpl cert = CERT_IMPL_CONSTRUCTOR.newInstance(info);
        Method method = CERT_IMPL_SIGN_METHOD;
        Object[] objArr = new Object[2];
        objArr[0] = key;
        objArr[1] = algorithm.equalsIgnoreCase("EC") ? "SHA256withECDSA" : "SHA256withRSA";
        method.invoke(cert, objArr);
        CERT_INFO_SET_METHOD.invoke(info, "algorithmID.algorithm", CERT_IMPL_GET_METHOD.invoke(cert, "x509.algorithm"));
        X509CertImpl cert2 = CERT_IMPL_CONSTRUCTOR.newInstance(info);
        Method method2 = CERT_IMPL_SIGN_METHOD;
        Object[] objArr2 = new Object[2];
        objArr2[0] = key;
        objArr2[1] = algorithm.equalsIgnoreCase("EC") ? "SHA256withECDSA" : "SHA256withRSA";
        method2.invoke(cert2, objArr2);
        cert2.verify(keypair.getPublic());
        return SelfSignedCertificate.newSelfSignedCertificate(fqdn, key, cert2);
    }

    private OpenJdkSelfSignedCertGenerator() {
    }
}
