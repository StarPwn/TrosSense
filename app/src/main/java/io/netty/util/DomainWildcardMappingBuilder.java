package io.netty.util;

import io.netty.util.internal.ObjectUtil;
import java.util.LinkedHashMap;
import java.util.Map;

/* loaded from: classes4.dex */
public class DomainWildcardMappingBuilder<V> {
    private final V defaultValue;
    private final Map<String, V> map;

    public DomainWildcardMappingBuilder(V defaultValue) {
        this(4, defaultValue);
    }

    public DomainWildcardMappingBuilder(int i, V v) {
        this.defaultValue = (V) ObjectUtil.checkNotNull(v, "defaultValue");
        this.map = new LinkedHashMap(i);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public DomainWildcardMappingBuilder<V> add(String str, V v) {
        this.map.put(normalizeHostName(str), ObjectUtil.checkNotNull(v, "output"));
        return this;
    }

    private String normalizeHostName(String hostname) {
        ObjectUtil.checkNotNull(hostname, "hostname");
        if (hostname.isEmpty() || hostname.charAt(0) == '.') {
            throw new IllegalArgumentException("Hostname '" + hostname + "' not valid");
        }
        String hostname2 = ImmutableDomainWildcardMapping.normalize((String) ObjectUtil.checkNotNull(hostname, "hostname"));
        if (hostname2.charAt(0) == '*') {
            if (hostname2.length() < 3 || hostname2.charAt(1) != '.') {
                throw new IllegalArgumentException("Wildcard Hostname '" + hostname2 + "'not valid");
            }
            return hostname2.substring(1);
        }
        return hostname2;
    }

    public Mapping<String, V> build() {
        return new ImmutableDomainWildcardMapping(this.defaultValue, this.map);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class ImmutableDomainWildcardMapping<V> implements Mapping<String, V> {
        private static final String REPR_HEADER = "ImmutableDomainWildcardMapping(default: ";
        private static final String REPR_MAP_CLOSING = ")";
        private static final String REPR_MAP_OPENING = ", map: ";
        private final V defaultValue;
        private final Map<String, V> map;

        ImmutableDomainWildcardMapping(V defaultValue, Map<String, V> map) {
            this.defaultValue = defaultValue;
            this.map = new LinkedHashMap(map);
        }

        @Override // io.netty.util.Mapping
        public V map(String hostname) {
            V value;
            if (hostname != null) {
                String hostname2 = normalize(hostname);
                V value2 = this.map.get(hostname2);
                if (value2 != null) {
                    return value2;
                }
                int idx = hostname2.indexOf(46);
                if (idx != -1 && (value = this.map.get(hostname2.substring(idx))) != null) {
                    return value;
                }
            }
            return this.defaultValue;
        }

        static String normalize(String hostname) {
            return DomainNameMapping.normalizeHostname(hostname);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(REPR_HEADER).append(this.defaultValue).append(REPR_MAP_OPENING).append('{');
            for (Map.Entry<String, V> entry : this.map.entrySet()) {
                String hostname = entry.getKey();
                if (hostname.charAt(0) == '.') {
                    hostname = '*' + hostname;
                }
                sb.append(hostname).append('=').append(entry.getValue()).append(", ");
            }
            sb.setLength(sb.length() - 2);
            return sb.append('}').append(REPR_MAP_CLOSING).toString();
        }
    }
}
