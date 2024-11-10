package io.netty.resolver.dns;

import io.netty.util.internal.SocketUtils;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Hashtable;
import java.util.List;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;

/* loaded from: classes4.dex */
final class DirContextUtils {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) DirContextUtils.class);

    private DirContextUtils() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void addNameServers(List<InetSocketAddress> defaultNameServers, int defaultPort) {
        URI uri;
        String host;
        Hashtable<String, String> env = new Hashtable<>();
        env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
        env.put("java.naming.provider.url", "dns://");
        try {
            String dnsUrls = (String) new InitialDirContext(env).getEnvironment().get("java.naming.provider.url");
            if (dnsUrls != null && !dnsUrls.isEmpty()) {
                String[] servers = dnsUrls.split(" ");
                for (String server : servers) {
                    try {
                        uri = new URI(server);
                        host = uri.getHost();
                    } catch (URISyntaxException e) {
                        logger.debug("Skipping a malformed nameserver URI: {}", server, e);
                    }
                    if (host != null && !host.isEmpty()) {
                        int port = uri.getPort();
                        defaultNameServers.add(SocketUtils.socketAddress(uri.getHost(), port == -1 ? defaultPort : port));
                    }
                    logger.debug("Skipping a nameserver URI as host portion could not be extracted: {}", server);
                }
            }
        } catch (NamingException e2) {
        }
    }
}
