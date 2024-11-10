package io.netty.resolver;

import io.netty.util.NetUtil;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/* loaded from: classes4.dex */
public final class HostsFileEntriesProvider {
    static final HostsFileEntriesProvider EMPTY = new HostsFileEntriesProvider(Collections.emptyMap(), Collections.emptyMap());
    private final Map<String, List<InetAddress>> ipv4Entries;
    private final Map<String, List<InetAddress>> ipv6Entries;

    /* loaded from: classes4.dex */
    public interface Parser {
        HostsFileEntriesProvider parse() throws IOException;

        HostsFileEntriesProvider parse(File file, Charset... charsetArr) throws IOException;

        HostsFileEntriesProvider parse(Reader reader) throws IOException;

        HostsFileEntriesProvider parse(Charset... charsetArr) throws IOException;

        HostsFileEntriesProvider parseSilently();

        HostsFileEntriesProvider parseSilently(File file, Charset... charsetArr);

        HostsFileEntriesProvider parseSilently(Charset... charsetArr);
    }

    public static Parser parser() {
        return ParserImpl.INSTANCE;
    }

    HostsFileEntriesProvider(Map<String, List<InetAddress>> ipv4Entries, Map<String, List<InetAddress>> ipv6Entries) {
        this.ipv4Entries = Collections.unmodifiableMap(new HashMap(ipv4Entries));
        this.ipv6Entries = Collections.unmodifiableMap(new HashMap(ipv6Entries));
    }

    public Map<String, List<InetAddress>> ipv4Entries() {
        return this.ipv4Entries;
    }

    public Map<String, List<InetAddress>> ipv6Entries() {
        return this.ipv6Entries;
    }

    /* loaded from: classes4.dex */
    private static final class ParserImpl implements Parser {
        private static final String WINDOWS_DEFAULT_SYSTEM_ROOT = "C:\\Windows";
        private static final String WINDOWS_HOSTS_FILE_RELATIVE_PATH = "\\system32\\drivers\\etc\\hosts";
        private static final String X_PLATFORMS_HOSTS_FILE_PATH = "/etc/hosts";
        private static final Pattern WHITESPACES = Pattern.compile("[ \t]+");
        private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) Parser.class);
        static final ParserImpl INSTANCE = new ParserImpl();

        private ParserImpl() {
        }

        @Override // io.netty.resolver.HostsFileEntriesProvider.Parser
        public HostsFileEntriesProvider parse() throws IOException {
            return parse(locateHostsFile(), Charset.defaultCharset());
        }

        @Override // io.netty.resolver.HostsFileEntriesProvider.Parser
        public HostsFileEntriesProvider parse(Charset... charsets) throws IOException {
            return parse(locateHostsFile(), charsets);
        }

        @Override // io.netty.resolver.HostsFileEntriesProvider.Parser
        public HostsFileEntriesProvider parse(File file, Charset... charsets) throws IOException {
            ObjectUtil.checkNotNull(file, "file");
            ObjectUtil.checkNotNull(charsets, "charsets");
            if (charsets.length == 0) {
                charsets = new Charset[]{Charset.defaultCharset()};
            }
            if (file.exists() && file.isFile()) {
                for (Charset charset : charsets) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
                    try {
                        HostsFileEntriesProvider entries = parse(reader);
                        if (entries != HostsFileEntriesProvider.EMPTY) {
                            return entries;
                        }
                        reader.close();
                    } finally {
                        reader.close();
                    }
                }
            }
            return HostsFileEntriesProvider.EMPTY;
        }

        @Override // io.netty.resolver.HostsFileEntriesProvider.Parser
        public HostsFileEntriesProvider parse(Reader reader) throws IOException {
            byte[] ipBytes;
            List<InetAddress> addresses;
            ObjectUtil.checkNotNull(reader, "reader");
            BufferedReader buff = new BufferedReader(reader);
            try {
                Map<String, List<InetAddress>> ipv4Entries = new HashMap<>();
                Map<String, List<InetAddress>> ipv6Entries = new HashMap<>();
                while (true) {
                    String readLine = buff.readLine();
                    String line = readLine;
                    if (readLine == null) {
                        break;
                    }
                    int commentPosition = line.indexOf(35);
                    if (commentPosition != -1) {
                        line = line.substring(0, commentPosition);
                    }
                    String line2 = line.trim();
                    if (!line2.isEmpty()) {
                        List<String> lineParts = new ArrayList<>();
                        for (String s : WHITESPACES.split(line2)) {
                            if (!s.isEmpty()) {
                                lineParts.add(s);
                            }
                        }
                        if (lineParts.size() >= 2 && (ipBytes = NetUtil.createByteArrayFromIpAddressString(lineParts.get(0))) != null) {
                            for (int i = 1; i < lineParts.size(); i++) {
                                String hostname = lineParts.get(i);
                                String hostnameLower = hostname.toLowerCase(Locale.ENGLISH);
                                InetAddress address = InetAddress.getByAddress(hostname, ipBytes);
                                if (address instanceof Inet4Address) {
                                    addresses = ipv4Entries.get(hostnameLower);
                                    if (addresses == null) {
                                        addresses = new ArrayList<>();
                                        ipv4Entries.put(hostnameLower, addresses);
                                    }
                                } else {
                                    addresses = ipv6Entries.get(hostnameLower);
                                    if (addresses == null) {
                                        addresses = new ArrayList<>();
                                        ipv6Entries.put(hostnameLower, addresses);
                                    }
                                }
                                addresses.add(address);
                            }
                        }
                    }
                }
                return (ipv4Entries.isEmpty() && ipv6Entries.isEmpty()) ? HostsFileEntriesProvider.EMPTY : new HostsFileEntriesProvider(ipv4Entries, ipv6Entries);
            } finally {
                try {
                    buff.close();
                } catch (IOException e) {
                    logger.warn("Failed to close a reader", (Throwable) e);
                }
            }
        }

        @Override // io.netty.resolver.HostsFileEntriesProvider.Parser
        public HostsFileEntriesProvider parseSilently() {
            return parseSilently(locateHostsFile(), Charset.defaultCharset());
        }

        @Override // io.netty.resolver.HostsFileEntriesProvider.Parser
        public HostsFileEntriesProvider parseSilently(Charset... charsets) {
            return parseSilently(locateHostsFile(), charsets);
        }

        @Override // io.netty.resolver.HostsFileEntriesProvider.Parser
        public HostsFileEntriesProvider parseSilently(File file, Charset... charsets) {
            try {
                return parse(file, charsets);
            } catch (IOException e) {
                if (logger.isWarnEnabled()) {
                    logger.warn("Failed to load and parse hosts file at " + file.getPath(), (Throwable) e);
                }
                return HostsFileEntriesProvider.EMPTY;
            }
        }

        private static File locateHostsFile() {
            if (PlatformDependent.isWindows()) {
                File hostsFile = new File(System.getenv("SystemRoot") + WINDOWS_HOSTS_FILE_RELATIVE_PATH);
                if (!hostsFile.exists()) {
                    return new File("C:\\Windows\\system32\\drivers\\etc\\hosts");
                }
                return hostsFile;
            }
            return new File(X_PLATFORMS_HOSTS_FILE_PATH);
        }
    }
}
