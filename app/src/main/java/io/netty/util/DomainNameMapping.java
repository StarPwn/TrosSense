package io.netty.util;

import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;
import java.net.IDN;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

@Deprecated
/* loaded from: classes4.dex */
public class DomainNameMapping<V> implements Mapping<String, V> {
    final V defaultValue;
    private final Map<String, V> map;
    private final Map<String, V> unmodifiableMap;

    @Deprecated
    public DomainNameMapping(V defaultValue) {
        this(4, defaultValue);
    }

    @Deprecated
    public DomainNameMapping(int initialCapacity, V defaultValue) {
        this(new LinkedHashMap(initialCapacity), defaultValue);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DomainNameMapping(Map<String, V> map, V v) {
        this.defaultValue = (V) ObjectUtil.checkNotNull(v, "defaultValue");
        this.map = map;
        this.unmodifiableMap = map != null ? Collections.unmodifiableMap(map) : null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Deprecated
    public DomainNameMapping<V> add(String str, V v) {
        this.map.put(normalizeHostname((String) ObjectUtil.checkNotNull(str, "hostname")), ObjectUtil.checkNotNull(v, "output"));
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean matches(String template, String hostName) {
        if (template.startsWith("*.")) {
            return template.regionMatches(2, hostName, 0, hostName.length()) || StringUtil.commonSuffixOfLength(hostName, template, template.length() - 1);
        }
        return template.equals(hostName);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String normalizeHostname(String hostname) {
        if (needsNormalization(hostname)) {
            hostname = IDN.toASCII(hostname, 1);
        }
        return hostname.toLowerCase(Locale.US);
    }

    private static boolean needsNormalization(String hostname) {
        int length = hostname.length();
        for (int i = 0; i < length; i++) {
            int c = hostname.charAt(i);
            if (c > 127) {
                return true;
            }
        }
        return false;
    }

    @Override // io.netty.util.Mapping
    public V map(String hostname) {
        if (hostname != null) {
            String hostname2 = normalizeHostname(hostname);
            for (Map.Entry<String, V> entry : this.map.entrySet()) {
                if (matches(entry.getKey(), hostname2)) {
                    return entry.getValue();
                }
            }
        }
        return this.defaultValue;
    }

    public Map<String, V> asMap() {
        return this.unmodifiableMap;
    }

    public String toString() {
        return StringUtil.simpleClassName(this) + "(default: " + this.defaultValue + ", map: " + this.map + ')';
    }
}
