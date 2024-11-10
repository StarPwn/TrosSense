package net.jodah.expiringmap;

/* loaded from: classes5.dex */
public interface ExpirationListener<K, V> {
    void expired(K k, V v);
}
