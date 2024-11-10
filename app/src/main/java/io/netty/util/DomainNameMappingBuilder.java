package io.netty.util;

import io.netty.util.internal.ObjectUtil;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Deprecated
/* loaded from: classes4.dex */
public final class DomainNameMappingBuilder<V> {
    private final V defaultValue;
    private final Map<String, V> map;

    public DomainNameMappingBuilder(V defaultValue) {
        this(4, defaultValue);
    }

    public DomainNameMappingBuilder(int i, V v) {
        this.defaultValue = (V) ObjectUtil.checkNotNull(v, "defaultValue");
        this.map = new LinkedHashMap(i);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public DomainNameMappingBuilder<V> add(String str, V v) {
        this.map.put(ObjectUtil.checkNotNull(str, "hostname"), ObjectUtil.checkNotNull(v, "output"));
        return this;
    }

    public DomainNameMapping<V> build() {
        return new ImmutableDomainNameMapping(this.defaultValue, this.map);
    }

    /* loaded from: classes4.dex */
    private static final class ImmutableDomainNameMapping<V> extends DomainNameMapping<V> {
        private final String[] domainNamePatterns;
        private final Map<String, V> map;
        private final V[] values;
        private static final String REPR_HEADER = "ImmutableDomainNameMapping(default: ";
        private static final String REPR_MAP_OPENING = ", map: {";
        private static final String REPR_MAP_CLOSING = "})";
        private static final int REPR_CONST_PART_LENGTH = (REPR_HEADER.length() + REPR_MAP_OPENING.length()) + REPR_MAP_CLOSING.length();

        private ImmutableDomainNameMapping(V v, Map<String, V> map) {
            super((Map) null, v);
            Set<Map.Entry<String, V>> entrySet = map.entrySet();
            int size = entrySet.size();
            this.domainNamePatterns = new String[size];
            this.values = (V[]) new Object[size];
            LinkedHashMap linkedHashMap = new LinkedHashMap(map.size());
            int i = 0;
            for (Map.Entry<String, V> entry : entrySet) {
                String normalizeHostname = normalizeHostname(entry.getKey());
                V value = entry.getValue();
                this.domainNamePatterns[i] = normalizeHostname;
                this.values[i] = value;
                linkedHashMap.put(normalizeHostname, value);
                i++;
            }
            this.map = Collections.unmodifiableMap(linkedHashMap);
        }

        @Override // io.netty.util.DomainNameMapping
        @Deprecated
        public DomainNameMapping<V> add(String hostname, V output) {
            throw new UnsupportedOperationException("Immutable DomainNameMapping does not support modification after initial creation");
        }

        @Override // io.netty.util.DomainNameMapping, io.netty.util.Mapping
        public V map(String hostname) {
            if (hostname != null) {
                String hostname2 = normalizeHostname(hostname);
                int length = this.domainNamePatterns.length;
                for (int index = 0; index < length; index++) {
                    if (matches(this.domainNamePatterns[index], hostname2)) {
                        return this.values[index];
                    }
                }
            }
            return this.defaultValue;
        }

        @Override // io.netty.util.DomainNameMapping
        public Map<String, V> asMap() {
            return this.map;
        }

        @Override // io.netty.util.DomainNameMapping
        public String toString() {
            String defaultValueStr = this.defaultValue.toString();
            int numberOfMappings = this.domainNamePatterns.length;
            if (numberOfMappings == 0) {
                return REPR_HEADER + defaultValueStr + REPR_MAP_OPENING + REPR_MAP_CLOSING;
            }
            String pattern0 = this.domainNamePatterns[0];
            String value0 = this.values[0].toString();
            int oneMappingLength = pattern0.length() + value0.length() + 3;
            int estimatedBufferSize = estimateBufferSize(defaultValueStr.length(), numberOfMappings, oneMappingLength);
            StringBuilder sb = new StringBuilder(estimatedBufferSize).append(REPR_HEADER).append(defaultValueStr).append(REPR_MAP_OPENING);
            appendMapping(sb, pattern0, value0);
            for (int index = 1; index < numberOfMappings; index++) {
                sb.append(", ");
                appendMapping(sb, index);
            }
            return sb.append(REPR_MAP_CLOSING).toString();
        }

        private static int estimateBufferSize(int defaultValueLength, int numberOfMappings, int estimatedMappingLength) {
            return REPR_CONST_PART_LENGTH + defaultValueLength + ((int) (estimatedMappingLength * numberOfMappings * 1.1d));
        }

        private StringBuilder appendMapping(StringBuilder sb, int mappingIndex) {
            return appendMapping(sb, this.domainNamePatterns[mappingIndex], this.values[mappingIndex].toString());
        }

        private static StringBuilder appendMapping(StringBuilder sb, String domainNamePattern, String value) {
            return sb.append(domainNamePattern).append('=').append(value);
        }
    }
}
