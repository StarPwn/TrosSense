package it.unimi.dsi.fastutil.objects;

/* loaded from: classes4.dex */
public interface ObjectIterable<K> extends Iterable<K> {
    @Override // java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
    ObjectIterator<K> iterator();

    @Override // it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
    /* renamed from: spliterator */
    default ObjectSpliterator<K> mo221spliterator() {
        return ObjectSpliterators.asSpliteratorUnknownSize(iterator(), 0);
    }
}
