package it.unimi.dsi.fastutil;

import java.util.Collection;

/* loaded from: classes4.dex */
public interface BigList<K> extends Collection<K>, Size64 {
    void add(long j, K k);

    boolean addAll(long j, Collection<? extends K> collection);

    K get(long j);

    long indexOf(Object obj);

    long lastIndexOf(Object obj);

    BigListIterator<K> listIterator();

    BigListIterator<K> listIterator(long j);

    K remove(long j);

    K set(long j, K k);

    void size(long j);

    BigList<K> subList(long j, long j2);

    @Override // java.util.Collection, it.unimi.dsi.fastutil.Size64
    @Deprecated
    default int size() {
        return super.size();
    }
}
