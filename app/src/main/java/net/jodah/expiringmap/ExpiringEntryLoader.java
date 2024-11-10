package net.jodah.expiringmap;

/* loaded from: classes5.dex */
public interface ExpiringEntryLoader<K, V> {
    ExpiringValue<V> load(K k);
}
