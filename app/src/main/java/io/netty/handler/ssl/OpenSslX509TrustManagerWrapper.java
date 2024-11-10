package io.netty.handler.ssl;

import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivilegedAction;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;
import javax.net.ssl.X509TrustManager;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class OpenSslX509TrustManagerWrapper {
    private static final InternalLogger LOGGER = InternalLoggerFactory.getInstance((Class<?>) OpenSslX509TrustManagerWrapper.class);
    private static final TrustManagerWrapper WRAPPER;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public interface TrustManagerWrapper {
        X509TrustManager wrapIfNeeded(X509TrustManager x509TrustManager);
    }

    static /* synthetic */ SSLContext access$000() throws NoSuchAlgorithmException, NoSuchProviderException {
        return newSSLContext();
    }

    static {
        SSLContext context;
        TrustManagerWrapper wrapper = new TrustManagerWrapper() { // from class: io.netty.handler.ssl.OpenSslX509TrustManagerWrapper.1
            @Override // io.netty.handler.ssl.OpenSslX509TrustManagerWrapper.TrustManagerWrapper
            public X509TrustManager wrapIfNeeded(X509TrustManager manager) {
                return manager;
            }
        };
        Throwable cause = null;
        Throwable unsafeCause = PlatformDependent.getUnsafeUnavailabilityCause();
        if (unsafeCause != null) {
            LOGGER.debug("Unable to access wrapped TrustManager", (Throwable) null);
        } else {
            try {
                context = newSSLContext();
                context.init(null, new TrustManager[]{new X509TrustManager() { // from class: io.netty.handler.ssl.OpenSslX509TrustManagerWrapper.2
                    @Override // javax.net.ssl.X509TrustManager
                    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                        throw new CertificateException();
                    }

                    @Override // javax.net.ssl.X509TrustManager
                    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                        throw new CertificateException();
                    }

                    @Override // javax.net.ssl.X509TrustManager
                    public X509Certificate[] getAcceptedIssuers() {
                        return EmptyArrays.EMPTY_X509_CERTIFICATES;
                    }
                }}, null);
            } catch (Throwable error) {
                cause = error;
                context = null;
            }
            if (cause != null) {
                LOGGER.debug("Unable to access wrapped TrustManager", cause);
            } else {
                final SSLContext finalContext = context;
                Object maybeWrapper = AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: io.netty.handler.ssl.OpenSslX509TrustManagerWrapper.3
                    @Override // java.security.PrivilegedAction
                    public Object run() {
                        try {
                            try {
                                Field contextSpiField = SSLContext.class.getDeclaredField("contextSpi");
                                long spiOffset = PlatformDependent.objectFieldOffset(contextSpiField);
                                Object spi = PlatformDependent.getObject(finalContext, spiOffset);
                                if (spi != null) {
                                    Class<?> clazz = spi.getClass();
                                    do {
                                        try {
                                            Field trustManagerField = clazz.getDeclaredField("trustManager");
                                            long tmOffset = PlatformDependent.objectFieldOffset(trustManagerField);
                                            Object trustManager = PlatformDependent.getObject(spi, tmOffset);
                                            if (trustManager instanceof X509ExtendedTrustManager) {
                                                return new UnsafeTrustManagerWrapper(spiOffset, tmOffset);
                                            }
                                        } catch (NoSuchFieldException e) {
                                        }
                                        clazz = clazz.getSuperclass();
                                    } while (clazz != null);
                                }
                                throw new NoSuchFieldException();
                            } catch (SecurityException e2) {
                                return e2;
                            }
                        } catch (NoSuchFieldException e3) {
                            return e3;
                        }
                    }
                });
                if (maybeWrapper instanceof Throwable) {
                    LOGGER.debug("Unable to access wrapped TrustManager", (Throwable) maybeWrapper);
                } else {
                    wrapper = (TrustManagerWrapper) maybeWrapper;
                }
            }
        }
        WRAPPER = wrapper;
    }

    private OpenSslX509TrustManagerWrapper() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static X509TrustManager wrapIfNeeded(X509TrustManager trustManager) {
        return WRAPPER.wrapIfNeeded(trustManager);
    }

    private static SSLContext newSSLContext() throws NoSuchAlgorithmException, NoSuchProviderException {
        return SSLContext.getInstance("TLS", "SunJSSE");
    }

    /* loaded from: classes4.dex */
    private static final class UnsafeTrustManagerWrapper implements TrustManagerWrapper {
        private final long spiOffset;
        private final long tmOffset;

        UnsafeTrustManagerWrapper(long spiOffset, long tmOffset) {
            this.spiOffset = spiOffset;
            this.tmOffset = tmOffset;
        }

        @Override // io.netty.handler.ssl.OpenSslX509TrustManagerWrapper.TrustManagerWrapper
        public X509TrustManager wrapIfNeeded(X509TrustManager manager) {
            if (!(manager instanceof X509ExtendedTrustManager)) {
                try {
                    SSLContext ctx = OpenSslX509TrustManagerWrapper.access$000();
                    ctx.init(null, new TrustManager[]{manager}, null);
                    Object spi = PlatformDependent.getObject(ctx, this.spiOffset);
                    if (spi != null) {
                        Object tm = PlatformDependent.getObject(spi, this.tmOffset);
                        if (tm instanceof X509ExtendedTrustManager) {
                            return (X509TrustManager) tm;
                        }
                    }
                } catch (KeyManagementException e) {
                    PlatformDependent.throwException(e);
                } catch (NoSuchAlgorithmException e2) {
                    PlatformDependent.throwException(e2);
                } catch (NoSuchProviderException e3) {
                    PlatformDependent.throwException(e3);
                }
            }
            return manager;
        }
    }
}
