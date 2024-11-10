package io.netty.util;

import io.netty.util.NetUtilInitializations;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collection;

/* loaded from: classes4.dex */
public final class NetUtil {
    private static final int IPV4_MAX_CHAR_BETWEEN_SEPARATOR = 3;
    private static final int IPV4_SEPARATORS = 3;
    private static final boolean IPV6_ADDRESSES_PREFERRED;
    private static final int IPV6_BYTE_COUNT = 16;
    private static final int IPV6_MAX_CHAR_BETWEEN_SEPARATOR = 4;
    private static final int IPV6_MAX_CHAR_COUNT = 39;
    private static final int IPV6_MAX_SEPARATORS = 8;
    private static final int IPV6_MIN_SEPARATORS = 2;
    private static final int IPV6_WORD_COUNT = 8;
    public static final InetAddress LOCALHOST;
    public static final Inet4Address LOCALHOST4;
    public static final Inet6Address LOCALHOST6;
    public static final NetworkInterface LOOPBACK_IF;
    public static final Collection<NetworkInterface> NETWORK_INTERFACES;
    public static final int SOMAXCONN;
    private static final boolean IPV4_PREFERRED = SystemPropertyUtil.getBoolean("java.net.preferIPv4Stack", false);
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) NetUtil.class);

    static {
        String prefer = SystemPropertyUtil.get("java.net.preferIPv6Addresses", "false");
        if ("true".equalsIgnoreCase(prefer.trim())) {
            IPV6_ADDRESSES_PREFERRED = true;
        } else {
            IPV6_ADDRESSES_PREFERRED = false;
        }
        logger.debug("-Djava.net.preferIPv4Stack: {}", Boolean.valueOf(IPV4_PREFERRED));
        logger.debug("-Djava.net.preferIPv6Addresses: {}", prefer);
        NETWORK_INTERFACES = NetUtilInitializations.networkInterfaces();
        LOCALHOST4 = NetUtilInitializations.createLocalhost4();
        LOCALHOST6 = NetUtilInitializations.createLocalhost6();
        NetUtilInitializations.NetworkIfaceAndInetAddress loopback = NetUtilInitializations.determineLoopback(NETWORK_INTERFACES, LOCALHOST4, LOCALHOST6);
        LOOPBACK_IF = loopback.iface();
        LOCALHOST = loopback.address();
        SOMAXCONN = ((Integer) AccessController.doPrivileged(new SoMaxConnAction())).intValue();
    }

    /* loaded from: classes4.dex */
    private static final class SoMaxConnAction implements PrivilegedAction<Integer> {
        private SoMaxConnAction() {
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        public Integer run() {
            int somaxconn = PlatformDependent.isWindows() ? 200 : 128;
            File file = new File("/proc/sys/net/core/somaxconn");
            BufferedReader in = null;
            try {
                try {
                    if (file.exists()) {
                        in = new BufferedReader(new FileReader(file));
                        somaxconn = Integer.parseInt(in.readLine());
                        if (NetUtil.logger.isDebugEnabled()) {
                            NetUtil.logger.debug("{}: {}", file, Integer.valueOf(somaxconn));
                        }
                    } else {
                        Integer tmp = null;
                        if (SystemPropertyUtil.getBoolean("io.netty.net.somaxconn.trySysctl", false)) {
                            tmp = NetUtil.sysctlGetInt("kern.ipc.somaxconn");
                            if (tmp == null) {
                                tmp = NetUtil.sysctlGetInt("kern.ipc.soacceptqueue");
                                if (tmp != null) {
                                    somaxconn = tmp.intValue();
                                }
                            } else {
                                somaxconn = tmp.intValue();
                            }
                        }
                        if (tmp == null) {
                            NetUtil.logger.debug("Failed to get SOMAXCONN from sysctl and file {}. Default: {}", file, Integer.valueOf(somaxconn));
                        }
                    }
                } catch (Exception e) {
                    if (NetUtil.logger.isDebugEnabled()) {
                        NetUtil.logger.debug("Failed to get SOMAXCONN from sysctl and file {}. Default: {}", file, Integer.valueOf(somaxconn), e);
                    }
                    if (0 != 0) {
                        in.close();
                    }
                }
                if (in != null) {
                    in.close();
                }
                return Integer.valueOf(somaxconn);
            } catch (Throwable th) {
                if (0 != 0) {
                    try {
                        in.close();
                    } catch (Exception e2) {
                    }
                }
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Integer sysctlGetInt(String sysctlKey) throws IOException {
        Process process = new ProcessBuilder("sysctl", sysctlKey).start();
        try {
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            try {
                String line = br.readLine();
                if (line != null && line.startsWith(sysctlKey)) {
                    int i = line.length();
                    do {
                        i--;
                        if (i > sysctlKey.length()) {
                        }
                    } while (Character.isDigit(line.charAt(i)));
                    return Integer.valueOf(line.substring(i + 1));
                }
                process.destroy();
                return null;
            } finally {
                br.close();
            }
        } finally {
            process.destroy();
        }
    }

    public static boolean isIpV4StackPreferred() {
        return IPV4_PREFERRED;
    }

    public static boolean isIpV6AddressesPreferred() {
        return IPV6_ADDRESSES_PREFERRED;
    }

    public static byte[] createByteArrayFromIpAddressString(String ipAddressString) {
        if (isValidIpV4Address(ipAddressString)) {
            return validIpV4ToBytes(ipAddressString);
        }
        if (isValidIpV6Address(ipAddressString)) {
            if (ipAddressString.charAt(0) == '[') {
                ipAddressString = ipAddressString.substring(1, ipAddressString.length() - 1);
            }
            int percentPos = ipAddressString.indexOf(37);
            if (percentPos >= 0) {
                ipAddressString = ipAddressString.substring(0, percentPos);
            }
            return getIPv6ByName(ipAddressString, true);
        }
        return null;
    }

    public static InetAddress createInetAddressFromIpAddressString(String ipAddressString) {
        if (isValidIpV4Address(ipAddressString)) {
            try {
                return InetAddress.getByAddress(validIpV4ToBytes(ipAddressString));
            } catch (UnknownHostException e) {
                throw new IllegalStateException(e);
            }
        }
        if (!isValidIpV6Address(ipAddressString)) {
            return null;
        }
        if (ipAddressString.charAt(0) == '[') {
            ipAddressString = ipAddressString.substring(1, ipAddressString.length() - 1);
        }
        int percentPos = ipAddressString.indexOf(37);
        if (percentPos >= 0) {
            try {
                int scopeId = Integer.parseInt(ipAddressString.substring(percentPos + 1));
                byte[] bytes = getIPv6ByName(ipAddressString.substring(0, percentPos), true);
                if (bytes == null) {
                    return null;
                }
                try {
                    return Inet6Address.getByAddress((String) null, bytes, scopeId);
                } catch (UnknownHostException e2) {
                    throw new IllegalStateException(e2);
                }
            } catch (NumberFormatException e3) {
                return null;
            }
        }
        byte[] bytes2 = getIPv6ByName(ipAddressString, true);
        if (bytes2 == null) {
            return null;
        }
        try {
            return InetAddress.getByAddress(bytes2);
        } catch (UnknownHostException e4) {
            throw new IllegalStateException(e4);
        }
    }

    private static int decimalDigit(String str, int pos) {
        return str.charAt(pos) - '0';
    }

    private static byte ipv4WordToByte(String ip, int from, int toExclusive) {
        int ret = decimalDigit(ip, from);
        int from2 = from + 1;
        if (from2 == toExclusive) {
            return (byte) ret;
        }
        int ret2 = (ret * 10) + decimalDigit(ip, from2);
        int from3 = from2 + 1;
        if (from3 == toExclusive) {
            return (byte) ret2;
        }
        return (byte) ((ret2 * 10) + decimalDigit(ip, from3));
    }

    static byte[] validIpV4ToBytes(String ip) {
        int i = ip.indexOf(46, 1);
        int i2 = ip.indexOf(46, i + 2);
        int i3 = ip.indexOf(46, i2 + 2);
        return new byte[]{ipv4WordToByte(ip, 0, i), ipv4WordToByte(ip, i + 1, i2), ipv4WordToByte(ip, i2 + 1, i3), ipv4WordToByte(ip, i3 + 1, ip.length())};
    }

    public static int ipv4AddressToInt(Inet4Address ipAddress) {
        byte[] octets = ipAddress.getAddress();
        return ((octets[0] & 255) << 24) | ((octets[1] & 255) << 16) | ((octets[2] & 255) << 8) | (octets[3] & 255);
    }

    public static String intToIpAddress(int i) {
        StringBuilder buf = new StringBuilder(15);
        buf.append((i >> 24) & 255);
        buf.append('.');
        buf.append((i >> 16) & 255);
        buf.append('.');
        buf.append((i >> 8) & 255);
        buf.append('.');
        buf.append(i & 255);
        return buf.toString();
    }

    public static String bytesToIpAddress(byte[] bytes) {
        return bytesToIpAddress(bytes, 0, bytes.length);
    }

    public static String bytesToIpAddress(byte[] bytes, int offset, int length) {
        switch (length) {
            case 4:
                return new StringBuilder(15).append(bytes[offset] & 255).append('.').append(bytes[offset + 1] & 255).append('.').append(bytes[offset + 2] & 255).append('.').append(bytes[offset + 3] & 255).toString();
            case 16:
                return toAddressString(bytes, offset, false);
            default:
                throw new IllegalArgumentException("length: " + length + " (expected: 4 or 16)");
        }
    }

    public static boolean isValidIpV6Address(String ip) {
        return isValidIpV6Address((CharSequence) ip);
    }

    /* JADX WARN: Code restructure failed: missing block: B:80:0x00cf, code lost:            if (r7 >= 0) goto L75;     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x00d1, code lost:            if (r6 != 7) goto L97;     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x00d3, code lost:            if (r8 <= 0) goto L98;     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x00d6, code lost:            return true;     */
    /* JADX WARN: Code restructure failed: missing block: B:85:?, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:86:?, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x00d9, code lost:            if ((r7 + 2) == r0) goto L81;     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x00db, code lost:            if (r8 <= 0) goto L99;     */
    /* JADX WARN: Code restructure failed: missing block: B:91:0x00df, code lost:            if (r6 < 8) goto L81;     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x00e1, code lost:            if (r7 > r3) goto L100;     */
    /* JADX WARN: Code restructure failed: missing block: B:93:?, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:94:?, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x00e4, code lost:            return true;     */
    /* JADX WARN: Failed to find 'out' block for switch in B:29:0x004e. Please report as an issue. */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean isValidIpV6Address(java.lang.CharSequence r13) {
        /*
            Method dump skipped, instructions count: 244
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.util.NetUtil.isValidIpV6Address(java.lang.CharSequence):boolean");
    }

    private static boolean isValidIpV4Word(CharSequence word, int from, int toExclusive) {
        char c0;
        char c2;
        int len = toExclusive - from;
        if (len < 1 || len > 3 || (c0 = word.charAt(from)) < '0') {
            return false;
        }
        if (len == 3) {
            char c1 = word.charAt(from + 1);
            if (c1 < '0' || (c2 = word.charAt(from + 2)) < '0') {
                return false;
            }
            if (c0 > '1' || c1 > '9' || c2 > '9') {
                if (c0 != '2' || c1 > '5') {
                    return false;
                }
                if (c2 > '5' && (c1 >= '5' || c2 > '9')) {
                    return false;
                }
            }
            return true;
        }
        if (c0 <= '9') {
            return len == 1 || isValidNumericChar(word.charAt(from + 1));
        }
        return false;
    }

    private static boolean isValidHexChar(char c) {
        return (c >= '0' && c <= '9') || (c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f');
    }

    private static boolean isValidNumericChar(char c) {
        return c >= '0' && c <= '9';
    }

    private static boolean isValidIPv4MappedChar(char c) {
        return c == 'f' || c == 'F';
    }

    private static boolean isValidIPv4MappedSeparators(byte b0, byte b1, boolean mustBeZero) {
        return b0 == b1 && (b0 == 0 || (!mustBeZero && b1 == -1));
    }

    private static boolean isValidIPv4Mapped(byte[] bytes, int currentIndex, int compressBegin, int compressLength) {
        boolean mustBeZero = compressBegin + compressLength >= 14;
        return currentIndex <= 12 && currentIndex >= 2 && (!mustBeZero || compressBegin < 12) && isValidIPv4MappedSeparators(bytes[currentIndex + (-1)], bytes[currentIndex + (-2)], mustBeZero) && PlatformDependent.isZero(bytes, 0, currentIndex + (-3));
    }

    public static boolean isValidIpV4Address(CharSequence ip) {
        return isValidIpV4Address(ip, 0, ip.length());
    }

    public static boolean isValidIpV4Address(String ip) {
        return isValidIpV4Address(ip, 0, ip.length());
    }

    private static boolean isValidIpV4Address(CharSequence ip, int from, int toExcluded) {
        if (ip instanceof String) {
            return isValidIpV4Address((String) ip, from, toExcluded);
        }
        if (ip instanceof AsciiString) {
            return isValidIpV4Address((AsciiString) ip, from, toExcluded);
        }
        return isValidIpV4Address0(ip, from, toExcluded);
    }

    private static boolean isValidIpV4Address(String ip, int from, int toExcluded) {
        int i;
        int from2;
        int i2;
        int from3;
        int i3;
        int len = toExcluded - from;
        return len <= 15 && len >= 7 && (i = ip.indexOf(46, from + 1)) > 0 && isValidIpV4Word(ip, from, i) && (i2 = ip.indexOf(46, (from2 = i + 2))) > 0 && isValidIpV4Word(ip, from2 + (-1), i2) && (i3 = ip.indexOf(46, (from3 = i2 + 2))) > 0 && isValidIpV4Word(ip, from3 + (-1), i3) && isValidIpV4Word(ip, i3 + 1, toExcluded);
    }

    private static boolean isValidIpV4Address(AsciiString ip, int from, int toExcluded) {
        int i;
        int from2;
        int i2;
        int from3;
        int i3;
        int len = toExcluded - from;
        return len <= 15 && len >= 7 && (i = ip.indexOf('.', from + 1)) > 0 && isValidIpV4Word(ip, from, i) && (i2 = ip.indexOf('.', (from2 = i + 2))) > 0 && isValidIpV4Word(ip, from2 + (-1), i2) && (i3 = ip.indexOf('.', (from3 = i2 + 2))) > 0 && isValidIpV4Word(ip, from3 + (-1), i3) && isValidIpV4Word(ip, i3 + 1, toExcluded);
    }

    private static boolean isValidIpV4Address0(CharSequence ip, int from, int toExcluded) {
        int i;
        int from2;
        int i2;
        int from3;
        int i3;
        int len = toExcluded - from;
        return len <= 15 && len >= 7 && (i = AsciiString.indexOf(ip, '.', from + 1)) > 0 && isValidIpV4Word(ip, from, i) && (i2 = AsciiString.indexOf(ip, '.', (from2 = i + 2))) > 0 && isValidIpV4Word(ip, from2 + (-1), i2) && (i3 = AsciiString.indexOf(ip, '.', (from3 = i2 + 2))) > 0 && isValidIpV4Word(ip, from3 + (-1), i3) && isValidIpV4Word(ip, i3 + 1, toExcluded);
    }

    public static Inet6Address getByName(CharSequence ip) {
        return getByName(ip, true);
    }

    public static Inet6Address getByName(CharSequence ip, boolean ipv4Mapped) {
        byte[] bytes = getIPv6ByName(ip, ipv4Mapped);
        if (bytes == null) {
            return null;
        }
        try {
            return Inet6Address.getByAddress((String) null, bytes, -1);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:116:0x015f, code lost:            if ((r8 - r7) <= 3) goto L96;     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    static byte[] getIPv6ByName(java.lang.CharSequence r21, boolean r22) {
        /*
            Method dump skipped, instructions count: 602
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.util.NetUtil.getIPv6ByName(java.lang.CharSequence, boolean):byte[]");
    }

    public static String toSocketAddressString(InetSocketAddress addr) {
        StringBuilder sb;
        String port = String.valueOf(addr.getPort());
        if (addr.isUnresolved()) {
            String hostname = getHostname(addr);
            sb = newSocketAddressStringBuilder(hostname, port, !isValidIpV6Address(hostname));
        } else {
            InetAddress address = addr.getAddress();
            String hostString = toAddressString(address);
            sb = newSocketAddressStringBuilder(hostString, port, address instanceof Inet4Address);
        }
        return sb.append(':').append(port).toString();
    }

    public static String toSocketAddressString(String host, int port) {
        String portStr = String.valueOf(port);
        return newSocketAddressStringBuilder(host, portStr, !isValidIpV6Address(host)).append(':').append(portStr).toString();
    }

    private static StringBuilder newSocketAddressStringBuilder(String host, String port, boolean ipv4) {
        int hostLen = host.length();
        if (ipv4) {
            return new StringBuilder(hostLen + 1 + port.length()).append(host);
        }
        StringBuilder stringBuilder = new StringBuilder(hostLen + 3 + port.length());
        if (hostLen <= 1 || host.charAt(0) != '[' || host.charAt(hostLen - 1) != ']') {
            return stringBuilder.append('[').append(host).append(']');
        }
        return stringBuilder.append(host);
    }

    public static String toAddressString(InetAddress ip) {
        return toAddressString(ip, false);
    }

    public static String toAddressString(InetAddress ip, boolean ipv4Mapped) {
        if (ip instanceof Inet4Address) {
            return ip.getHostAddress();
        }
        if (!(ip instanceof Inet6Address)) {
            throw new IllegalArgumentException("Unhandled type: " + ip);
        }
        return toAddressString(ip.getAddress(), 0, ipv4Mapped);
    }

    private static String toAddressString(byte[] bytes, int offset, boolean ipv4Mapped) {
        int currentLength;
        int[] words = new int[8];
        for (int i = 0; i < words.length; i++) {
            int idx = (i << 1) + offset;
            words[i] = ((bytes[idx] & 255) << 8) | (bytes[idx + 1] & 255);
        }
        int currentStart = -1;
        int shortestStart = -1;
        int shortestLength = 0;
        for (int i2 = 0; i2 < words.length; i2++) {
            if (words[i2] == 0) {
                if (currentStart < 0) {
                    currentStart = i2;
                }
            } else if (currentStart >= 0) {
                int currentLength2 = i2 - currentStart;
                if (currentLength2 > shortestLength) {
                    shortestStart = currentStart;
                    shortestLength = currentLength2;
                }
                currentStart = -1;
            }
        }
        if (currentStart >= 0 && (currentLength = words.length - currentStart) > shortestLength) {
            shortestStart = currentStart;
            shortestLength = currentLength;
        }
        int currentLength3 = 1;
        if (shortestLength == 1) {
            shortestLength = 0;
            shortestStart = -1;
        }
        int shortestEnd = shortestStart + shortestLength;
        StringBuilder b = new StringBuilder(39);
        if (shortestEnd < 0) {
            b.append(Integer.toHexString(words[0]));
            for (int i3 = 1; i3 < words.length; i3++) {
                b.append(':');
                b.append(Integer.toHexString(words[i3]));
            }
        } else {
            if (inRangeEndExclusive(0, shortestStart, shortestEnd)) {
                b.append("::");
                if (!ipv4Mapped || shortestEnd != 5 || words[5] != 65535) {
                    currentLength3 = 0;
                }
            } else {
                b.append(Integer.toHexString(words[0]));
                currentLength3 = 0;
            }
            for (int i4 = 1; i4 < words.length; i4++) {
                if (!inRangeEndExclusive(i4, shortestStart, shortestEnd)) {
                    if (!inRangeEndExclusive(i4 - 1, shortestStart, shortestEnd)) {
                        if (currentLength3 == 0 || i4 == 6) {
                            b.append(':');
                        } else {
                            b.append('.');
                        }
                    }
                    if (currentLength3 != 0 && i4 > 5) {
                        b.append(words[i4] >> 8);
                        b.append('.');
                        b.append(words[i4] & 255);
                    } else {
                        b.append(Integer.toHexString(words[i4]));
                    }
                } else if (!inRangeEndExclusive(i4 - 1, shortestStart, shortestEnd)) {
                    b.append("::");
                }
            }
        }
        return b.toString();
    }

    public static String getHostname(InetSocketAddress addr) {
        return PlatformDependent.javaVersion() >= 7 ? addr.getHostString() : addr.getHostName();
    }

    private static boolean inRangeEndExclusive(int value, int start, int end) {
        return value >= start && value < end;
    }

    private NetUtil() {
    }
}
