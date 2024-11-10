package io.netty.handler.ssl;

import io.netty.buffer.ByteBuf;
import io.netty.internal.tcnative.Buffer;
import io.netty.internal.tcnative.Library;
import io.netty.internal.tcnative.SSL;
import io.netty.internal.tcnative.SSLContext;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.ReferenceCounted;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.NativeLibraryLoader;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/* loaded from: classes4.dex */
public final class OpenSsl {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    static final Set<String> AVAILABLE_CIPHER_SUITES;
    private static final Set<String> AVAILABLE_JAVA_CIPHER_SUITES;
    private static final Set<String> AVAILABLE_OPENSSL_CIPHER_SUITES;
    private static final Set<String> CLIENT_DEFAULT_PROTOCOLS;
    static final List<String> DEFAULT_CIPHERS;
    static final String[] EXTRA_SUPPORTED_TLS_1_3_CIPHERS;
    static final String EXTRA_SUPPORTED_TLS_1_3_CIPHERS_STRING;
    private static final boolean IS_BORINGSSL;
    static final boolean JAVAX_CERTIFICATE_CREATION_SUPPORTED;
    static final String[] NAMED_GROUPS;
    private static final Set<String> SERVER_DEFAULT_PROTOCOLS;
    static final Set<String> SUPPORTED_PROTOCOLS_SET;
    private static final boolean SUPPORTS_KEYMANAGER_FACTORY;
    private static final boolean SUPPORTS_OCSP;
    private static final boolean TLSV13_SUPPORTED;
    private static final Throwable UNAVAILABILITY_CAUSE;
    private static final boolean USE_KEYMANAGER_FACTORY;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) OpenSsl.class);
    private static final String[] DEFAULT_NAMED_GROUPS = {"x25519", "secp256r1", "secp384r1", "secp521r1"};

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:102:0x036b A[Catch: all -> 0x0495, TRY_ENTER, TryCatch #31 {all -> 0x0495, blocks: (B:276:0x01ba, B:278:0x01c0, B:102:0x036b, B:105:0x0372, B:108:0x0379, B:111:0x0380, B:52:0x0474, B:54:0x047b, B:57:0x0482, B:60:0x0489, B:63:0x0490, B:64:0x0494, B:287:0x01d5, B:290:0x01e0), top: B:275:0x01ba }] */
    /* JADX WARN: Removed duplicated region for block: B:105:0x0372 A[Catch: all -> 0x0495, TryCatch #31 {all -> 0x0495, blocks: (B:276:0x01ba, B:278:0x01c0, B:102:0x036b, B:105:0x0372, B:108:0x0379, B:111:0x0380, B:52:0x0474, B:54:0x047b, B:57:0x0482, B:60:0x0489, B:63:0x0490, B:64:0x0494, B:287:0x01d5, B:290:0x01e0), top: B:275:0x01ba }] */
    /* JADX WARN: Removed duplicated region for block: B:108:0x0379 A[Catch: all -> 0x0495, TryCatch #31 {all -> 0x0495, blocks: (B:276:0x01ba, B:278:0x01c0, B:102:0x036b, B:105:0x0372, B:108:0x0379, B:111:0x0380, B:52:0x0474, B:54:0x047b, B:57:0x0482, B:60:0x0489, B:63:0x0490, B:64:0x0494, B:287:0x01d5, B:290:0x01e0), top: B:275:0x01ba }] */
    /* JADX WARN: Removed duplicated region for block: B:111:0x0380 A[Catch: all -> 0x0495, TRY_LEAVE, TryCatch #31 {all -> 0x0495, blocks: (B:276:0x01ba, B:278:0x01c0, B:102:0x036b, B:105:0x0372, B:108:0x0379, B:111:0x0380, B:52:0x0474, B:54:0x047b, B:57:0x0482, B:60:0x0489, B:63:0x0490, B:64:0x0494, B:287:0x01d5, B:290:0x01e0), top: B:275:0x01ba }] */
    /* JADX WARN: Removed duplicated region for block: B:114:0x038c A[Catch: all -> 0x0466, TRY_LEAVE, TryCatch #3 {all -> 0x0466, blocks: (B:99:0x0364, B:112:0x0383, B:114:0x038c), top: B:98:0x0364 }] */
    /* JADX WARN: Removed duplicated region for block: B:136:0x04d8  */
    /* JADX WARN: Removed duplicated region for block: B:148:0x0551  */
    /* JADX WARN: Removed duplicated region for block: B:151:0x055f  */
    /* JADX WARN: Removed duplicated region for block: B:154:0x056d  */
    /* JADX WARN: Removed duplicated region for block: B:157:0x057c  */
    /* JADX WARN: Removed duplicated region for block: B:160:0x058b  */
    /* JADX WARN: Removed duplicated region for block: B:162:0x0592  */
    /* JADX WARN: Removed duplicated region for block: B:167:0x05bc  */
    /* JADX WARN: Removed duplicated region for block: B:209:0x0455  */
    /* JADX WARN: Removed duplicated region for block: B:232:0x02b6  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0216 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:54:0x047b A[Catch: all -> 0x0495, TryCatch #31 {all -> 0x0495, blocks: (B:276:0x01ba, B:278:0x01c0, B:102:0x036b, B:105:0x0372, B:108:0x0379, B:111:0x0380, B:52:0x0474, B:54:0x047b, B:57:0x0482, B:60:0x0489, B:63:0x0490, B:64:0x0494, B:287:0x01d5, B:290:0x01e0), top: B:275:0x01ba }] */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0482 A[Catch: all -> 0x0495, TryCatch #31 {all -> 0x0495, blocks: (B:276:0x01ba, B:278:0x01c0, B:102:0x036b, B:105:0x0372, B:108:0x0379, B:111:0x0380, B:52:0x0474, B:54:0x047b, B:57:0x0482, B:60:0x0489, B:63:0x0490, B:64:0x0494, B:287:0x01d5, B:290:0x01e0), top: B:275:0x01ba }] */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0489 A[Catch: all -> 0x0495, TryCatch #31 {all -> 0x0495, blocks: (B:276:0x01ba, B:278:0x01c0, B:102:0x036b, B:105:0x0372, B:108:0x0379, B:111:0x0380, B:52:0x0474, B:54:0x047b, B:57:0x0482, B:60:0x0489, B:63:0x0490, B:64:0x0494, B:287:0x01d5, B:290:0x01e0), top: B:275:0x01ba }] */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0490 A[Catch: all -> 0x0495, TryCatch #31 {all -> 0x0495, blocks: (B:276:0x01ba, B:278:0x01c0, B:102:0x036b, B:105:0x0372, B:108:0x0379, B:111:0x0380, B:52:0x0474, B:54:0x047b, B:57:0x0482, B:60:0x0489, B:63:0x0490, B:64:0x0494, B:287:0x01d5, B:290:0x01e0), top: B:275:0x01ba }] */
    /* JADX WARN: Removed duplicated region for block: B:69:0x023c A[Catch: all -> 0x0235, TRY_ENTER, TRY_LEAVE, TryCatch #11 {all -> 0x0235, blocks: (B:34:0x0216, B:36:0x021a, B:38:0x0220, B:41:0x0228, B:46:0x022f, B:69:0x023c, B:228:0x046b, B:229:0x046f, B:225:0x0360), top: B:33:0x0216 }] */
    /* JADX WARN: Removed duplicated region for block: B:90:0x02a2  */
    /* JADX WARN: Type inference failed for: r1v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r1v25 */
    /* JADX WARN: Type inference failed for: r1v26 */
    /* JADX WARN: Type inference failed for: r1v27 */
    /* JADX WARN: Type inference failed for: r1v28 */
    /* JADX WARN: Type inference failed for: r1v29 */
    /* JADX WARN: Type inference failed for: r1v30 */
    /* JADX WARN: Type inference failed for: r1v32 */
    /* JADX WARN: Type inference failed for: r1v35 */
    /* JADX WARN: Type inference failed for: r1v36 */
    /* JADX WARN: Type inference failed for: r1v37 */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:188:0x02ca -> B:59:0x02d1). Please report as a decompilation issue!!! */
    static {
        /*
            Method dump skipped, instructions count: 1561
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.ssl.OpenSsl.<clinit>():void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String checkTls13Ciphers(InternalLogger logger2, String ciphers) {
        if (IS_BORINGSSL && !ciphers.isEmpty()) {
            if (EXTRA_SUPPORTED_TLS_1_3_CIPHERS.length <= 0) {
                throw new AssertionError();
            }
            Set<String> boringsslTlsv13Ciphers = new HashSet<>(EXTRA_SUPPORTED_TLS_1_3_CIPHERS.length);
            Collections.addAll(boringsslTlsv13Ciphers, EXTRA_SUPPORTED_TLS_1_3_CIPHERS);
            boolean ciphersNotMatch = false;
            String[] split = ciphers.split(":");
            int length = split.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                String cipher = split[i];
                if (boringsslTlsv13Ciphers.isEmpty()) {
                    ciphersNotMatch = true;
                    break;
                }
                if (boringsslTlsv13Ciphers.remove(cipher) || boringsslTlsv13Ciphers.remove(CipherSuiteConverter.toJava(cipher, "TLS"))) {
                    i++;
                } else {
                    ciphersNotMatch = true;
                    break;
                }
            }
            if (ciphersNotMatch | (!boringsslTlsv13Ciphers.isEmpty())) {
                if (logger2.isInfoEnabled()) {
                    StringBuilder javaCiphers = new StringBuilder(128);
                    for (String str : ciphers.split(":")) {
                        javaCiphers.append(CipherSuiteConverter.toJava(str, "TLS")).append(":");
                    }
                    javaCiphers.setLength(javaCiphers.length() - 1);
                    logger2.info("BoringSSL doesn't allow to enable or disable TLSv1.3 ciphers explicitly. Provided TLSv1.3 ciphers: '{}', default TLSv1.3 ciphers that will be used: '{}'.", javaCiphers, EXTRA_SUPPORTED_TLS_1_3_CIPHERS_STRING);
                }
                return EXTRA_SUPPORTED_TLS_1_3_CIPHERS_STRING;
            }
        }
        return ciphers;
    }

    static boolean isSessionCacheSupported() {
        return ((long) version()) >= 269484032;
    }

    static X509Certificate selfSignedCertificate() throws CertificateException {
        return (X509Certificate) SslContext.X509_CERT_FACTORY.generateCertificate(new ByteArrayInputStream("-----BEGIN CERTIFICATE-----\nMIICrjCCAZagAwIBAgIIdSvQPv1QAZQwDQYJKoZIhvcNAQELBQAwFjEUMBIGA1UEAxMLZXhhbXBs\nZS5jb20wIBcNMTgwNDA2MjIwNjU5WhgPOTk5OTEyMzEyMzU5NTlaMBYxFDASBgNVBAMTC2V4YW1w\nbGUuY29tMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAggbWsmDQ6zNzRZ5AW8E3eoGl\nqWvOBDb5Fs1oBRrVQHuYmVAoaqwDzXYJ0LOwa293AgWEQ1jpcbZ2hpoYQzqEZBTLnFhMrhRFlH6K\nbJND8Y33kZ/iSVBBDuGbdSbJShlM+4WwQ9IAso4MZ4vW3S1iv5fGGpLgbtXRmBf/RU8omN0Gijlv\nWlLWHWijLN8xQtySFuBQ7ssW8RcKAary3pUm6UUQB+Co6lnfti0Tzag8PgjhAJq2Z3wbsGRnP2YS\nvYoaK6qzmHXRYlp/PxrjBAZAmkLJs4YTm/XFF+fkeYx4i9zqHbyone5yerRibsHaXZWLnUL+rFoe\nMdKvr0VS3sGmhQIDAQABMA0GCSqGSIb3DQEBCwUAA4IBAQADQi441pKmXf9FvUV5EHU4v8nJT9Iq\nyqwsKwXnr7AsUlDGHBD7jGrjAXnG5rGxuNKBQ35wRxJATKrUtyaquFUL6H8O6aGQehiFTk6zmPbe\n12Gu44vqqTgIUxnv3JQJiox8S2hMxsSddpeCmSdvmalvD6WG4NthH6B9ZaBEiep1+0s0RUaBYn73\nI7CCUaAtbjfR6pcJjrFk5ei7uwdQZFSJtkP2z8r7zfeANJddAKFlkaMWn7u+OIVuB4XPooWicObk\nNAHFtP65bocUYnDpTVdiyvn8DdqyZ/EO8n1bBKBzuSLplk2msW4pdgaFgY7Vw/0wzcFXfUXmL1uy\nG8sQD/wx\n-----END CERTIFICATE-----".getBytes(CharsetUtil.US_ASCII)));
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0032, code lost:            if (r1 == (-1)) goto L18;     */
    /* JADX WARN: Code restructure failed: missing block: B:7:0x0020, code lost:            if (r1 != (-1)) goto L8;     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x0022, code lost:            io.netty.internal.tcnative.SSLContext.free(r1);     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static boolean doesSupportOcsp() {
        /*
            r0 = 0
            int r1 = version()
            long r1 = (long) r1
            r3 = 268443648(0x10002000, double:1.326287843E-315)
            int r1 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r1 < 0) goto L35
            r1 = -1
            r3 = 16
            r4 = 1
            r5 = -1
            long r3 = io.netty.internal.tcnative.SSLContext.make(r3, r4)     // Catch: java.lang.Throwable -> L26 java.lang.Exception -> L2f
            r1 = r3
            r3 = 0
            io.netty.internal.tcnative.SSLContext.enableOcsp(r1, r3)     // Catch: java.lang.Throwable -> L26 java.lang.Exception -> L2f
            r0 = 1
            int r3 = (r1 > r5 ? 1 : (r1 == r5 ? 0 : -1))
            if (r3 == 0) goto L35
        L22:
            io.netty.internal.tcnative.SSLContext.free(r1)
            goto L35
        L26:
            r3 = move-exception
            int r4 = (r1 > r5 ? 1 : (r1 == r5 ? 0 : -1))
            if (r4 == 0) goto L2e
            io.netty.internal.tcnative.SSLContext.free(r1)
        L2e:
            throw r3
        L2f:
            r3 = move-exception
            int r3 = (r1 > r5 ? 1 : (r1 == r5 ? 0 : -1))
            if (r3 == 0) goto L35
            goto L22
        L35:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.ssl.OpenSsl.doesSupportOcsp():boolean");
    }

    private static boolean doesSupportProtocol(int protocol, int opt) {
        if (opt == 0) {
            return false;
        }
        try {
            long sslCtx = SSLContext.make(protocol, 2);
            if (sslCtx == -1) {
                return true;
            }
            SSLContext.free(sslCtx);
            return true;
        } catch (Exception e) {
            if (-1 != -1) {
                SSLContext.free(-1L);
            }
            return false;
        } catch (Throwable th) {
            if (-1 != -1) {
                SSLContext.free(-1L);
            }
            throw th;
        }
    }

    public static boolean isAvailable() {
        return UNAVAILABILITY_CAUSE == null;
    }

    @Deprecated
    public static boolean isAlpnSupported() {
        return ((long) version()) >= 268443648;
    }

    public static boolean isOcspSupported() {
        return SUPPORTS_OCSP;
    }

    public static int version() {
        if (isAvailable()) {
            return SSL.version();
        }
        return -1;
    }

    public static String versionString() {
        if (isAvailable()) {
            return SSL.versionString();
        }
        return null;
    }

    public static void ensureAvailability() {
        if (UNAVAILABILITY_CAUSE != null) {
            throw ((Error) new UnsatisfiedLinkError("failed to load the required native library").initCause(UNAVAILABILITY_CAUSE));
        }
    }

    public static Throwable unavailabilityCause() {
        return UNAVAILABILITY_CAUSE;
    }

    @Deprecated
    public static Set<String> availableCipherSuites() {
        return availableOpenSslCipherSuites();
    }

    public static Set<String> availableOpenSslCipherSuites() {
        return AVAILABLE_OPENSSL_CIPHER_SUITES;
    }

    public static Set<String> availableJavaCipherSuites() {
        return AVAILABLE_JAVA_CIPHER_SUITES;
    }

    public static boolean isCipherSuiteAvailable(String cipherSuite) {
        String converted = CipherSuiteConverter.toOpenSsl(cipherSuite, IS_BORINGSSL);
        if (converted != null) {
            cipherSuite = converted;
        }
        return AVAILABLE_OPENSSL_CIPHER_SUITES.contains(cipherSuite);
    }

    public static boolean supportsKeyManagerFactory() {
        return SUPPORTS_KEYMANAGER_FACTORY;
    }

    @Deprecated
    public static boolean supportsHostnameValidation() {
        return isAvailable();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean useKeyManagerFactory() {
        return USE_KEYMANAGER_FACTORY;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long memoryAddress(ByteBuf buf) {
        if (buf.isDirect()) {
            return buf.hasMemoryAddress() ? buf.memoryAddress() : Buffer.address(buf.internalNioBuffer(0, buf.readableBytes()));
        }
        throw new AssertionError();
    }

    private OpenSsl() {
    }

    private static void loadTcNative() throws Exception {
        String os = PlatformDependent.normalizedOs();
        String arch = PlatformDependent.normalizedArch();
        Set<String> libNames = new LinkedHashSet<>(5);
        if (!"linux".equals(os)) {
            libNames.add("netty_tcnative_" + os + '_' + arch);
        } else {
            Set<String> classifiers = PlatformDependent.normalizedLinuxClassifiers();
            for (String classifier : classifiers) {
                libNames.add("netty_tcnative_" + os + '_' + arch + "_" + classifier);
            }
            libNames.add("netty_tcnative_" + os + '_' + arch);
            libNames.add("netty_tcnative_" + os + '_' + arch + "_fedora");
        }
        libNames.add("netty_tcnative_" + arch);
        libNames.add("netty_tcnative");
        NativeLibraryLoader.loadFirstAvailable(PlatformDependent.getClassLoader(SSLContext.class), (String[]) libNames.toArray(EmptyArrays.EMPTY_STRINGS));
    }

    private static boolean initializeTcNative(String engine) throws Exception {
        return Library.initialize("provided", engine);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void releaseIfNeeded(ReferenceCounted counted) {
        if (counted.refCnt() > 0) {
            ReferenceCountUtil.safeRelease(counted);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isTlsv13Supported() {
        return TLSV13_SUPPORTED;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isOptionSupported(SslContextOption<?> option) {
        if (isAvailable()) {
            if (option == OpenSslContextOption.USE_TASKS) {
                return true;
            }
            if (isBoringSSL()) {
                return option == OpenSslContextOption.ASYNC_PRIVATE_KEY_METHOD || option == OpenSslContextOption.PRIVATE_KEY_METHOD || option == OpenSslContextOption.CERTIFICATE_COMPRESSION_ALGORITHMS || option == OpenSslContextOption.TLS_FALSE_START || option == OpenSslContextOption.MAX_CERTIFICATE_LIST_BYTES;
            }
        }
        return false;
    }

    private static Set<String> defaultProtocols(String property) {
        String protocolsString = SystemPropertyUtil.get(property, null);
        Set<String> protocols = new HashSet<>();
        if (protocolsString != null) {
            for (String proto : protocolsString.split(",")) {
                String p = proto.trim();
                protocols.add(p);
            }
        } else {
            protocols.add(SslProtocols.TLS_v1_2);
            protocols.add(SslProtocols.TLS_v1_3);
        }
        return protocols;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String[] defaultProtocols(boolean isClient) {
        Collection<String> defaultProtocols = isClient ? CLIENT_DEFAULT_PROTOCOLS : SERVER_DEFAULT_PROTOCOLS;
        if (defaultProtocols == null) {
            throw new AssertionError();
        }
        List<String> protocols = new ArrayList<>(defaultProtocols.size());
        for (String proto : defaultProtocols) {
            if (SUPPORTED_PROTOCOLS_SET.contains(proto)) {
                protocols.add(proto);
            }
        }
        return (String[]) protocols.toArray(EmptyArrays.EMPTY_STRINGS);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isBoringSSL() {
        return IS_BORINGSSL;
    }
}
