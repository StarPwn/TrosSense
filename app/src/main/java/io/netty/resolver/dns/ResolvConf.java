package io.netty.resolver.dns;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes4.dex */
final class ResolvConf {
    private final List<InetSocketAddress> nameservers;

    static ResolvConf fromReader(BufferedReader reader) throws IOException {
        return new ResolvConf(reader);
    }

    static ResolvConf fromFile(String file) throws IOException {
        FileReader fileReader = new FileReader(file);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            return fromReader(reader);
        } finally {
            fileReader.close();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ResolvConf system() {
        ResolvConf resolvConv = ResolvConfLazy.machineResolvConf;
        if (resolvConv != null) {
            return resolvConv;
        }
        throw new IllegalStateException("/etc/resolv.conf could not be read");
    }

    private ResolvConf(BufferedReader reader) throws IOException {
        List<InetSocketAddress> nameservers = new ArrayList<>();
        while (true) {
            String ln = reader.readLine();
            if (ln != null) {
                String ln2 = ln.trim();
                if (!ln2.isEmpty() && ln2.startsWith("nameserver")) {
                    nameservers.add(new InetSocketAddress(ln2.substring("nameserver".length()).trim(), 53));
                }
            } else {
                this.nameservers = Collections.unmodifiableList(nameservers);
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<InetSocketAddress> getNameservers() {
        return this.nameservers;
    }

    /* loaded from: classes4.dex */
    private static final class ResolvConfLazy {
        static final ResolvConf machineResolvConf;

        private ResolvConfLazy() {
        }

        static {
            ResolvConf resolvConf;
            try {
                resolvConf = ResolvConf.fromFile("/etc/resolv.conf");
            } catch (IOException e) {
                resolvConf = null;
            }
            machineResolvConf = resolvConf;
        }
    }
}
