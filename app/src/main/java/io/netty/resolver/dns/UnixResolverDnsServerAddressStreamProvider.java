package io.netty.resolver.dns;

import io.netty.resolver.dns.UnixResolverOptions;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/* loaded from: classes4.dex */
public final class UnixResolverDnsServerAddressStreamProvider implements DnsServerAddressStreamProvider {
    private static final String DOMAIN_ROW_LABEL = "domain";
    private static final String ETC_RESOLVER_DIR = "/etc/resolver";
    private static final String ETC_RESOLV_CONF_FILE = "/etc/resolv.conf";
    private static final String NAMESERVER_ROW_LABEL = "nameserver";
    private static final String OPTIONS_ROTATE_FLAG = "rotate";
    private static final String OPTIONS_ROW_LABEL = "options ";
    private static final String PORT_ROW_LABEL = "port";
    private static final String SEARCH_ROW_LABEL = "search";
    private static final String SORTLIST_ROW_LABEL = "sortlist";
    private final DnsServerAddresses defaultNameServerAddresses;
    private final Map<String, DnsServerAddresses> domainToNameServerStreamMap;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) UnixResolverDnsServerAddressStreamProvider.class);
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");
    private static final String RES_OPTIONS = System.getenv("RES_OPTIONS");

    /* JADX INFO: Access modifiers changed from: package-private */
    public static DnsServerAddressStreamProvider parseSilently() {
        try {
            UnixResolverDnsServerAddressStreamProvider nameServerCache = new UnixResolverDnsServerAddressStreamProvider(ETC_RESOLV_CONF_FILE, ETC_RESOLVER_DIR);
            return nameServerCache.mayOverrideNameServers() ? nameServerCache : DefaultDnsServerAddressStreamProvider.INSTANCE;
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug("failed to parse {} and/or {}", ETC_RESOLV_CONF_FILE, ETC_RESOLVER_DIR, e);
            }
            return DefaultDnsServerAddressStreamProvider.INSTANCE;
        }
    }

    public UnixResolverDnsServerAddressStreamProvider(File etcResolvConf, File... etcResolverFiles) throws IOException {
        Map<String, DnsServerAddresses> etcResolvConfMap = parse((File) ObjectUtil.checkNotNull(etcResolvConf, "etcResolvConf"));
        boolean useEtcResolverFiles = (etcResolverFiles == null || etcResolverFiles.length == 0) ? false : true;
        this.domainToNameServerStreamMap = useEtcResolverFiles ? parse(etcResolverFiles) : etcResolvConfMap;
        DnsServerAddresses defaultNameServerAddresses = etcResolvConfMap.get(etcResolvConf.getName());
        if (defaultNameServerAddresses == null) {
            Collection<DnsServerAddresses> values = etcResolvConfMap.values();
            if (values.isEmpty()) {
                throw new IllegalArgumentException(etcResolvConf + " didn't provide any name servers");
            }
            this.defaultNameServerAddresses = values.iterator().next();
        } else {
            this.defaultNameServerAddresses = defaultNameServerAddresses;
        }
        if (useEtcResolverFiles) {
            this.domainToNameServerStreamMap.putAll(etcResolvConfMap);
        }
    }

    public UnixResolverDnsServerAddressStreamProvider(String etcResolvConf, String etcResolverDir) throws IOException {
        this(etcResolvConf == null ? null : new File(etcResolvConf), etcResolverDir != null ? new File(etcResolverDir).listFiles() : null);
    }

    @Override // io.netty.resolver.dns.DnsServerAddressStreamProvider
    public DnsServerAddressStream nameServerAddressStream(String hostname) {
        while (true) {
            int i = hostname.indexOf(46, 1);
            if (i < 0 || i == hostname.length() - 1) {
                break;
            }
            DnsServerAddresses addresses = this.domainToNameServerStreamMap.get(hostname);
            if (addresses != null) {
                return addresses.stream();
            }
            hostname = hostname.substring(i + 1);
        }
        return this.defaultNameServerAddresses.stream();
    }

    private boolean mayOverrideNameServers() {
        return (this.domainToNameServerStreamMap.isEmpty() && this.defaultNameServerAddresses.stream().next() == null) ? false : true;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(7:(3:32|33|(5:35|36|37|38|39))|51|52|(3:54|55|(11:57|58|59|(3:107|108|109)(4:61|62|63|(1:67)(1:98))|68|(3:79|80|(2:82|(9:84|85|86|87|88|71|72|(1:74)|75)(3:92|93|94)))|70|71|72|(0)|75)(3:117|118|119))(3:120|121|(2:123|(9:125|126|127|(2:137|138)|129|130|131|132|133)(4:145|146|147|148))(3:149|150|(2:152|(1:154)(3:155|156|157))(2:158|(1:160))))|37|38|39) */
    /* JADX WARN: Code restructure failed: missing block: B:165:0x0289, code lost:            r0 = e;     */
    /* JADX WARN: Code restructure failed: missing block: B:166:0x028a, code lost:            r20 = r5;        r22 = r8;     */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0330  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0334  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x016f A[Catch: IllegalArgumentException -> 0x0177, all -> 0x0327, TRY_LEAVE, TryCatch #3 {all -> 0x0327, blocks: (B:68:0x010c, B:80:0x0112, B:82:0x0118, B:86:0x0129, B:88:0x0134, B:72:0x0165, B:74:0x016f, B:43:0x02e1, B:93:0x0141, B:94:0x0162, B:65:0x00fe, B:67:0x0108, B:99:0x017f, B:100:0x019d, B:118:0x01a4, B:119:0x01c8, B:120:0x01c9, B:123:0x01db, B:125:0x01e5, B:127:0x01e9, B:138:0x01ef, B:129:0x01f9, B:132:0x01fc, B:147:0x020e, B:148:0x022e, B:150:0x022f, B:152:0x0236, B:154:0x0240, B:156:0x024d, B:157:0x026d, B:158:0x026e, B:160:0x0274, B:182:0x02f9, B:184:0x030b), top: B:79:0x0112 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static java.util.Map<java.lang.String, io.netty.resolver.dns.DnsServerAddresses> parse(java.io.File... r26) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 825
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.resolver.dns.UnixResolverDnsServerAddressStreamProvider.parse(java.io.File[]):java.util.Map");
    }

    private static void putIfAbsent(Map<String, DnsServerAddresses> domainToNameServerStreamMap, String domainName, List<InetSocketAddress> addresses, boolean rotate) {
        DnsServerAddresses addrs;
        if (rotate) {
            addrs = DnsServerAddresses.rotational(addresses);
        } else {
            addrs = DnsServerAddresses.sequential(addresses);
        }
        putIfAbsent(domainToNameServerStreamMap, domainName, addrs);
    }

    private static void putIfAbsent(Map<String, DnsServerAddresses> domainToNameServerStreamMap, String domainName, DnsServerAddresses addresses) {
        DnsServerAddresses existingAddresses = domainToNameServerStreamMap.put(domainName, addresses);
        if (existingAddresses != null) {
            domainToNameServerStreamMap.put(domainName, existingAddresses);
            if (logger.isDebugEnabled()) {
                logger.debug("Domain name {} already maps to addresses {} so new addresses {} will be discarded", domainName, existingAddresses, addresses);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static UnixResolverOptions parseEtcResolverOptions() throws IOException {
        return parseEtcResolverOptions(new File(ETC_RESOLV_CONF_FILE));
    }

    /* JADX WARN: Code restructure failed: missing block: B:9:0x001f, code lost:            parseResOptions(r4.substring(io.netty.resolver.dns.UnixResolverDnsServerAddressStreamProvider.OPTIONS_ROW_LABEL.length()), r1);     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    static io.netty.resolver.dns.UnixResolverOptions parseEtcResolverOptions(java.io.File r6) throws java.io.IOException {
        /*
            java.lang.String r0 = "options "
            io.netty.resolver.dns.UnixResolverOptions$Builder r1 = io.netty.resolver.dns.UnixResolverOptions.newBuilder()
            java.io.FileReader r2 = new java.io.FileReader
            r2.<init>(r6)
            r3 = 0
            java.io.BufferedReader r4 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> L3e
            r4.<init>(r2)     // Catch: java.lang.Throwable -> L3e
            r3 = r4
        L12:
            java.lang.String r4 = r3.readLine()     // Catch: java.lang.Throwable -> L3e
            r5 = r4
            if (r4 == 0) goto L2b
            boolean r4 = r5.startsWith(r0)     // Catch: java.lang.Throwable -> L3e
            if (r4 == 0) goto L12
            int r0 = r0.length()     // Catch: java.lang.Throwable -> L3e
            java.lang.String r0 = r5.substring(r0)     // Catch: java.lang.Throwable -> L3e
            parseResOptions(r0, r1)     // Catch: java.lang.Throwable -> L3e
        L2b:
            r3.close()
            java.lang.String r0 = io.netty.resolver.dns.UnixResolverDnsServerAddressStreamProvider.RES_OPTIONS
            if (r0 == 0) goto L39
            java.lang.String r0 = io.netty.resolver.dns.UnixResolverDnsServerAddressStreamProvider.RES_OPTIONS
            parseResOptions(r0, r1)
        L39:
            io.netty.resolver.dns.UnixResolverOptions r0 = r1.build()
            return r0
        L3e:
            r0 = move-exception
            if (r3 != 0) goto L45
            r2.close()
            goto L48
        L45:
            r3.close()
        L48:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.resolver.dns.UnixResolverDnsServerAddressStreamProvider.parseEtcResolverOptions(java.io.File):io.netty.resolver.dns.UnixResolverOptions");
    }

    private static void parseResOptions(String line, UnixResolverOptions.Builder builder) {
        String[] opts = WHITESPACE_PATTERN.split(line);
        for (String opt : opts) {
            try {
                if (opt.startsWith("ndots:")) {
                    builder.setNdots(parseResIntOption(opt, "ndots:"));
                } else if (opt.startsWith("attempts:")) {
                    builder.setAttempts(parseResIntOption(opt, "attempts:"));
                } else if (opt.startsWith("timeout:")) {
                    builder.setTimeout(parseResIntOption(opt, "timeout:"));
                }
            } catch (NumberFormatException e) {
            }
        }
    }

    private static int parseResIntOption(String opt, String fullLabel) {
        String optValue = opt.substring(fullLabel.length());
        return Integer.parseInt(optValue);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static List<String> parseEtcResolverSearchDomains() throws IOException {
        return parseEtcResolverSearchDomains(new File(ETC_RESOLV_CONF_FILE));
    }

    static List<String> parseEtcResolverSearchDomains(File etcResolvConf) throws IOException {
        String localDomain = null;
        List<String> searchDomains = new ArrayList<>();
        FileReader fr = new FileReader(etcResolvConf);
        BufferedReader br = null;
        try {
            br = new BufferedReader(fr);
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                if (localDomain == null && line.startsWith(DOMAIN_ROW_LABEL)) {
                    int i = StringUtil.indexOfNonWhiteSpace(line, DOMAIN_ROW_LABEL.length());
                    if (i >= 0) {
                        localDomain = line.substring(i);
                    }
                } else if (line.startsWith(SEARCH_ROW_LABEL)) {
                    int i2 = StringUtil.indexOfNonWhiteSpace(line, SEARCH_ROW_LABEL.length());
                    if (i2 >= 0) {
                        String[] domains = WHITESPACE_PATTERN.split(line.substring(i2));
                        Collections.addAll(searchDomains, domains);
                    }
                }
            }
            br.close();
            return (localDomain == null || !searchDomains.isEmpty()) ? searchDomains : Collections.singletonList(localDomain);
        } catch (Throwable th) {
            if (br == null) {
                fr.close();
            } else {
                br.close();
            }
            throw th;
        }
    }
}
