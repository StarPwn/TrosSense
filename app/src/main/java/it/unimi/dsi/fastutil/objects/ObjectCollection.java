package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Size64;
import java.util.Collection;

/* loaded from: classes4.dex */
public interface ObjectCollection<K> extends Collection<K>, ObjectIterable<K> {
    @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
    ObjectIterator<K> iterator();

    @Override // java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
    /* renamed from: spliterator */
    default ObjectSpliterator<K> mo221spliterator() {
        return ObjectSpliterators.asSpliterator(iterator(), Size64.sizeOf(this), 64);
    }
}
