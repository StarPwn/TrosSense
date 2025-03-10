package io.netty.handler.ssl;

import io.netty.util.CharsetUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.AccessController;
import java.security.PrivateKey;
import java.security.PrivilegedAction;
import java.security.Provider;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8DecryptorProviderBuilder;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;
import org.bouncycastle.operator.InputDecryptorProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;
import org.bouncycastle.pkcs.PKCSException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class BouncyCastlePemReader {
    private static final String BC_PEMPARSER = "org.bouncycastle.openssl.PEMParser";
    private static final String BC_PROVIDER = "org.bouncycastle.jce.provider.BouncyCastleProvider";
    private static volatile boolean attemptedLoading;
    private static volatile Provider bcProvider;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) BouncyCastlePemReader.class);
    private static volatile Throwable unavailabilityCause;

    public static boolean hasAttemptedLoading() {
        return attemptedLoading;
    }

    public static boolean isAvailable() {
        if (!hasAttemptedLoading()) {
            tryLoading();
        }
        return unavailabilityCause == null;
    }

    public static Throwable unavailabilityCause() {
        return unavailabilityCause;
    }

    private static void tryLoading() {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: io.netty.handler.ssl.BouncyCastlePemReader.1
            @Override // java.security.PrivilegedAction
            public Void run() {
                try {
                    ClassLoader classLoader = getClass().getClassLoader();
                    Class<?> cls = Class.forName(BouncyCastlePemReader.BC_PROVIDER, true, classLoader);
                    Class.forName(BouncyCastlePemReader.BC_PEMPARSER, true, classLoader);
                    Provider unused = BouncyCastlePemReader.bcProvider = (Provider) cls.getConstructor(new Class[0]).newInstance(new Object[0]);
                    BouncyCastlePemReader.logger.debug("Bouncy Castle provider available");
                    boolean unused2 = BouncyCastlePemReader.attemptedLoading = true;
                    return null;
                } catch (Throwable e) {
                    BouncyCastlePemReader.logger.debug("Cannot load Bouncy Castle provider", e);
                    Throwable unused3 = BouncyCastlePemReader.unavailabilityCause = e;
                    boolean unused4 = BouncyCastlePemReader.attemptedLoading = true;
                    return null;
                }
            }
        });
    }

    public static PrivateKey getPrivateKey(InputStream keyInputStream, String keyPassword) {
        if (!isAvailable()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Bouncy castle provider is unavailable.", unavailabilityCause());
            }
            return null;
        }
        try {
            PEMParser parser = newParser(keyInputStream);
            return getPrivateKey(parser, keyPassword);
        } catch (Exception e) {
            logger.debug("Unable to extract private key", (Throwable) e);
            return null;
        }
    }

    public static PrivateKey getPrivateKey(File keyFile, String keyPassword) {
        if (!isAvailable()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Bouncy castle provider is unavailable.", unavailabilityCause());
            }
            return null;
        }
        try {
            PEMParser parser = newParser(keyFile);
            return getPrivateKey(parser, keyPassword);
        } catch (Exception e) {
            logger.debug("Unable to extract private key", (Throwable) e);
            return null;
        }
    }

    private static JcaPEMKeyConverter newConverter() {
        return new JcaPEMKeyConverter().setProvider(bcProvider);
    }

    private static PrivateKey getPrivateKey(PEMParser pemParser, String keyPassword) throws IOException, PKCSException, OperatorCreationException {
        try {
            JcaPEMKeyConverter converter = newConverter();
            PrivateKey pk = null;
            Object object = pemParser.readObject();
            while (object != null && pk == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Parsed PEM object of type {} and assume key is {}encrypted", object.getClass().getName(), keyPassword == null ? "not " : "");
                }
                if (keyPassword == null) {
                    if (object instanceof PrivateKeyInfo) {
                        pk = converter.getPrivateKey((PrivateKeyInfo) object);
                    } else if (object instanceof PEMKeyPair) {
                        pk = converter.getKeyPair((PEMKeyPair) object).getPrivate();
                    } else {
                        logger.debug("Unable to handle PEM object of type {} as a non encrypted key", object.getClass());
                    }
                } else if (object instanceof PEMEncryptedKeyPair) {
                    PEMDecryptorProvider decProv = new JcePEMDecryptorProviderBuilder().setProvider(bcProvider).build(keyPassword.toCharArray());
                    pk = converter.getKeyPair(((PEMEncryptedKeyPair) object).decryptKeyPair(decProv)).getPrivate();
                } else if (object instanceof PKCS8EncryptedPrivateKeyInfo) {
                    InputDecryptorProvider pkcs8InputDecryptorProvider = new JceOpenSSLPKCS8DecryptorProviderBuilder().setProvider(bcProvider).build(keyPassword.toCharArray());
                    pk = converter.getPrivateKey(((PKCS8EncryptedPrivateKeyInfo) object).decryptPrivateKeyInfo(pkcs8InputDecryptorProvider));
                } else {
                    logger.debug("Unable to handle PEM object of type {} as a encrypted key", object.getClass());
                }
                if (pk == null) {
                    object = pemParser.readObject();
                }
            }
            if (pk == null && logger.isDebugEnabled()) {
                logger.debug("No key found");
            }
            return pk;
        } finally {
            if (pemParser != null) {
                try {
                    pemParser.close();
                } catch (Exception exception) {
                    logger.debug("Failed closing pem parser", (Throwable) exception);
                }
            }
        }
    }

    private static PEMParser newParser(File keyFile) throws FileNotFoundException {
        return new PEMParser(new FileReader(keyFile));
    }

    private static PEMParser newParser(InputStream keyInputStream) {
        return new PEMParser(new InputStreamReader(keyInputStream, CharsetUtil.US_ASCII));
    }

    private BouncyCastlePemReader() {
    }
}
