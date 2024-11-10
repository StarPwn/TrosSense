package io.netty.handler.ssl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class PemReader {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) PemReader.class);
    private static final Pattern CERT_HEADER = Pattern.compile("-+BEGIN\\s[^-\\r\\n]*CERTIFICATE[^-\\r\\n]*-+(?:\\s|\\r|\\n)+");
    private static final Pattern CERT_FOOTER = Pattern.compile("-+END\\s[^-\\r\\n]*CERTIFICATE[^-\\r\\n]*-+(?:\\s|\\r|\\n)*");
    private static final Pattern KEY_HEADER = Pattern.compile("-+BEGIN\\s[^-\\r\\n]*PRIVATE\\s+KEY[^-\\r\\n]*-+(?:\\s|\\r|\\n)+");
    private static final Pattern KEY_FOOTER = Pattern.compile("-+END\\s[^-\\r\\n]*PRIVATE\\s+KEY[^-\\r\\n]*-+(?:\\s|\\r|\\n)*");
    private static final Pattern BODY = Pattern.compile("[a-z0-9+/=][a-z0-9+/=\\r\\n]*", 2);

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ByteBuf[] readCertificates(File file) throws CertificateException {
        try {
            InputStream in = new FileInputStream(file);
            try {
                return readCertificates(in);
            } finally {
                safeClose(in);
            }
        } catch (FileNotFoundException e) {
            throw new CertificateException("could not find certificate file: " + file);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ByteBuf[] readCertificates(InputStream in) throws CertificateException {
        try {
            String content = readContent(in);
            List<ByteBuf> certs = new ArrayList<>();
            Matcher m = CERT_HEADER.matcher(content);
            int start = 0;
            while (m.find(start)) {
                int start2 = m.end();
                m.usePattern(BODY);
                if (!m.find(start2)) {
                    break;
                }
                ByteBuf base64 = Unpooled.copiedBuffer(m.group(0), CharsetUtil.US_ASCII);
                int start3 = m.end();
                m.usePattern(CERT_FOOTER);
                if (!m.find(start3)) {
                    break;
                }
                ByteBuf der = Base64.decode(base64);
                base64.release();
                certs.add(der);
                start = m.end();
                m.usePattern(CERT_HEADER);
            }
            if (certs.isEmpty()) {
                throw new CertificateException("found no certificates in input stream");
            }
            return (ByteBuf[]) certs.toArray(new ByteBuf[0]);
        } catch (IOException e) {
            throw new CertificateException("failed to read certificate input stream", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ByteBuf readPrivateKey(File file) throws KeyException {
        try {
            InputStream in = new FileInputStream(file);
            try {
                return readPrivateKey(in);
            } finally {
                safeClose(in);
            }
        } catch (FileNotFoundException e) {
            throw new KeyException("could not find key file: " + file);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ByteBuf readPrivateKey(InputStream in) throws KeyException {
        try {
            String content = readContent(in);
            Matcher m = KEY_HEADER.matcher(content);
            if (!m.find(0)) {
                throw keyNotFoundException();
            }
            int start = m.end();
            m.usePattern(BODY);
            if (!m.find(start)) {
                throw keyNotFoundException();
            }
            ByteBuf base64 = Unpooled.copiedBuffer(m.group(0), CharsetUtil.US_ASCII);
            int start2 = m.end();
            m.usePattern(KEY_FOOTER);
            if (!m.find(start2)) {
                throw keyNotFoundException();
            }
            ByteBuf der = Base64.decode(base64);
            base64.release();
            return der;
        } catch (IOException e) {
            throw new KeyException("failed to read key input stream", e);
        }
    }

    private static KeyException keyNotFoundException() {
        return new KeyException("could not find a PKCS #8 private key in input stream (see https://netty.io/wiki/sslcontextbuilder-and-private-key.html for more information)");
    }

    private static String readContent(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            byte[] buf = new byte[8192];
            while (true) {
                int ret = in.read(buf);
                if (ret >= 0) {
                    out.write(buf, 0, ret);
                } else {
                    return out.toString(CharsetUtil.US_ASCII.name());
                }
            }
        } finally {
            safeClose(out);
        }
    }

    private static void safeClose(InputStream in) {
        try {
            in.close();
        } catch (IOException e) {
            logger.warn("Failed to close a stream.", (Throwable) e);
        }
    }

    private static void safeClose(OutputStream out) {
        try {
            out.close();
        } catch (IOException e) {
            logger.warn("Failed to close a stream.", (Throwable) e);
        }
    }

    private PemReader() {
    }
}
