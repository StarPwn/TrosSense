package org.jose4j.http;

import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import org.jose4j.lang.StringUtil;
import org.jose4j.lang.UncheckedJoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: classes5.dex */
public class Get implements SimpleGet {
    private static final long MAX_RETRY_WAIT = 8000;
    private static final Logger log = LoggerFactory.getLogger((Class<?>) Get.class);
    private HostnameVerifier hostnameVerifier;
    private Proxy proxy;
    private SSLSocketFactory sslSocketFactory;
    private int connectTimeout = AccessibilityNodeInfoCompat.EXTRA_DATA_TEXT_CHARACTER_LOCATION_ARG_MAX_LENGTH;
    private int readTimeout = AccessibilityNodeInfoCompat.EXTRA_DATA_TEXT_CHARACTER_LOCATION_ARG_MAX_LENGTH;
    private int retries = 3;
    private long initialRetryWaitTime = 180;
    private boolean progressiveRetryWait = true;
    private int responseBodySizeLimit = 524288;

    @Override // org.jose4j.http.SimpleGet
    public SimpleResponse get(String location) throws IOException {
        int attempts = 0;
        log.debug("HTTP GET of {}", location);
        URL url = new URL(location);
        while (true) {
            try {
                URLConnection urlConnection = this.proxy == null ? url.openConnection() : url.openConnection(this.proxy);
                urlConnection.setConnectTimeout(this.connectTimeout);
                urlConnection.setReadTimeout(this.readTimeout);
                preventHttpCaching(urlConnection);
                setUpTls(urlConnection);
                HttpURLConnection httpUrlConnection = (HttpURLConnection) urlConnection;
                int code = httpUrlConnection.getResponseCode();
                String msg = httpUrlConnection.getResponseMessage();
                if (code != 200) {
                    throw new IOException("Non 200 status code (" + code + " " + msg + ") returned from " + url);
                }
                String charset = getCharset(urlConnection);
                String body = getBody(urlConnection, charset);
                Map<String, List<String>> headers = httpUrlConnection.getHeaderFields();
                SimpleResponse simpleResponse = new Response(code, msg, headers, body);
                log.debug("HTTP GET of {} returned {}", url, simpleResponse);
                return simpleResponse;
            } catch (FileNotFoundException e) {
                throw e;
            } catch (SSLHandshakeException e2) {
                throw e2;
            } catch (SSLPeerUnverifiedException e3) {
                throw e3;
            } catch (ResponseBodyTooLargeException e4) {
                throw e4;
            } catch (IOException e5) {
                attempts++;
                if (attempts > this.retries) {
                    throw e5;
                }
                long retryWaitTime = getRetryWaitTime(attempts);
                log.debug("Waiting {}ms before retrying ({} of {}) HTTP GET of {} after failed attempt: {}", Long.valueOf(retryWaitTime), Integer.valueOf(attempts), Integer.valueOf(this.retries), url, e5);
                try {
                    Thread.sleep(retryWaitTime);
                } catch (InterruptedException e6) {
                }
            }
        }
    }

    private void preventHttpCaching(URLConnection urlConnection) {
        urlConnection.setUseCaches(false);
        urlConnection.setRequestProperty("Cache-Control", "no-cache");
    }

    private String getBody(URLConnection urlConnection, String charset) throws IOException {
        StringWriter writer = new StringWriter();
        InputStream is = urlConnection.getInputStream();
        try {
            InputStreamReader isr = new InputStreamReader(is, charset);
            int charactersRead = 0;
            try {
                char[] buffer = new char[1024];
                while (true) {
                    int n = isr.read(buffer);
                    if (-1 != n) {
                        writer.write(buffer, 0, n);
                        charactersRead += n;
                        if (this.responseBodySizeLimit > 0 && charactersRead > this.responseBodySizeLimit) {
                            throw new ResponseBodyTooLargeException("More than " + this.responseBodySizeLimit + " characters have been read from the response body.");
                        }
                    } else {
                        log.debug("read {} characters", Integer.valueOf(charactersRead));
                        isr.close();
                        if (is != null) {
                            is.close();
                        }
                        return writer.toString();
                    }
                }
            } finally {
            }
        } catch (Throwable th) {
            if (is != null) {
                try {
                    is.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    private void setUpTls(URLConnection urlConnection) {
        if (urlConnection instanceof HttpsURLConnection) {
            HttpsURLConnection httpsUrlConnection = (HttpsURLConnection) urlConnection;
            if (this.sslSocketFactory != null) {
                httpsUrlConnection.setSSLSocketFactory(this.sslSocketFactory);
            }
            if (this.hostnameVerifier != null) {
                httpsUrlConnection.setHostnameVerifier(this.hostnameVerifier);
            }
        }
    }

    private String getCharset(URLConnection urlConnection) {
        String contentType = urlConnection.getHeaderField("Content-Type");
        String charset = StringUtil.UTF_8;
        if (contentType == null) {
            return StringUtil.UTF_8;
        }
        try {
            String[] split = contentType.replace(" ", "").split(";");
            int length = split.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                String part = split[i];
                if (!part.startsWith("charset=")) {
                    i++;
                } else {
                    charset = part.substring("charset=".length());
                    break;
                }
            }
            Charset.forName(charset);
            return charset;
        } catch (Exception e) {
            log.debug("Unexpected problem attempted to determine the charset from the Content-Type ({}) so will default to using UTF8: {}", contentType, e);
            return StringUtil.UTF_8;
        }
    }

    private long getRetryWaitTime(int attempt) {
        if (this.progressiveRetryWait) {
            double pow = Math.pow(2.0d, attempt - 1);
            long wait = (long) (this.initialRetryWaitTime * pow);
            return Math.min(wait, MAX_RETRY_WAIT);
        }
        return this.initialRetryWaitTime;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public void setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        this.hostnameVerifier = hostnameVerifier;
    }

    public void setTrustedCertificates(X509Certificate... certificates) {
        setTrustedCertificates(Arrays.asList(certificates));
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public void setProgressiveRetryWait(boolean progressiveRetryWait) {
        this.progressiveRetryWait = progressiveRetryWait;
    }

    public void setInitialRetryWaitTime(long initialRetryWaitTime) {
        this.initialRetryWaitTime = initialRetryWaitTime;
    }

    public void setResponseBodySizeLimit(int responseBodySizeLimit) {
        this.responseBodySizeLimit = responseBodySizeLimit;
    }

    public void setTrustedCertificates(Collection<X509Certificate> certificates) {
        try {
            TrustManagerFactory trustMgrFactory = TrustManagerFactory.getInstance("PKIX");
            KeyStore keyStore = KeyStore.getInstance("jks");
            keyStore.load(null, null);
            int i = 0;
            for (X509Certificate certificate : certificates) {
                keyStore.setCertificateEntry("alias" + i, certificate);
                i++;
            }
            trustMgrFactory.init(keyStore);
            TrustManager[] customTrustManagers = trustMgrFactory.getTrustManagers();
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, customTrustManagers, null);
            this.sslSocketFactory = sslContext.getSocketFactory();
        } catch (IOException | KeyManagementException | KeyStoreException | NoSuchAlgorithmException | CertificateException e) {
            throw new UncheckedJoseException("Unable to initialize socket factory with custom trusted  certificates.", e);
        }
    }

    public void setSslSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.sslSocketFactory = sslSocketFactory;
    }

    public void setHttpProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static class ResponseBodyTooLargeException extends IOException {
        public ResponseBodyTooLargeException(String message) {
            super(message);
        }
    }
}
