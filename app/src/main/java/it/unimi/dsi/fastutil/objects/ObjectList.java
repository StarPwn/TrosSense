package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Size64;
import it.unimi.dsi.fastutil.objects.AbstractObjectList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;

/* loaded from: classes4.dex */
public interface ObjectList<K> extends List<K>, Comparable<List<? extends K>>, ObjectCollection<K> {
    void addElements(int i, K[] kArr);

    void addElements(int i, K[] kArr, int i2, int i3);

    void getElements(int i, Object[] objArr, int i2, int i3);

    @Override // java.util.List, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
    ObjectListIterator<K> iterator();

    @Override // java.util.List
    ObjectListIterator<K> listIterator();

    @Override // java.util.List
    ObjectListIterator<K> listIterator(int i);

    void removeElements(int i, int i2);

    void size(int i);

    @Override // java.util.List
    ObjectList<K> subList(int i, int i2);

    @Override // java.util.List, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
    /* renamed from: spliterator */
    default ObjectSpliterator<K> mo221spliterator() {
        if (this instanceof RandomAccess) {
            return new AbstractObjectList.IndexBasedSpliterator(this, 0);
        }
        return ObjectSpliterators.asSpliterator(iterator(), Size64.sizeOf(this), ObjectSpliterators.LIST_SPLITERATOR_CHARACTERISTICS);
    }

    default void setElements(K[] a) {
        setElements(0, a);
    }

    default void setElements(int index, K[] a) {
        setElements(index, a, 0, a.length);
    }

    default void setElements(int index, K[] a, int offset, int length) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is negative");
        }
        if (index > size()) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than list size (" + size() + ")");
        }
        ObjectArrays.ensureOffsetLength(a, offset, length);
        if (index + length > size()) {
            throw new IndexOutOfBoundsException("End index (" + (index + length) + ") is greater than list size (" + size() + ")");
        }
        ObjectListIterator<K> iter = listIterator(index);
        for (int i = 0; i < length; i++) {
            iter.next();
            iter.set(a[i + offset]);
        }
    }

    default boolean addAll(int index, ObjectList<? extends K> l) {
        return addAll(index, (Collection) l);
    }

    default boolean addAll(ObjectList<? extends K> l) {
        return addAll(size(), (ObjectList) l);
    }

    static <K> ObjectList<K> of() {
        return ObjectImmutableList.of();
    }

    static <K> ObjectList<K> of(K e) {
        return ObjectLists.singleton(e);
    }

    static <K> ObjectList<K> of(K e0, K e1) {
        return ObjectImmutableList.of(e0, e1);
    }

    static <K> ObjectList<K> of(K e0, K e1, K e2) {
        return ObjectImmutableList.of(e0, e1, e2);
    }

    @SafeVarargs
    static <K> ObjectList<K> of(K... a) {
        switch (a.length) {
            case 0:
                return of();
            case 1:
                return of((Object) a[0]);
            default:
                return ObjectImmutableList.of((Object[]) a);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.List
    default void sort(Comparator<? super K> comparator) {
        Object[] array = toArray();
        if (comparator == null) {
            ObjectArrays.stableSort(array);
        } else {
            ObjectArrays.stableSort(array, comparator);
        }
        setElements(array);
    }

    /* JADX WARN: Multi-variable type inference failed */
    default void unstableSort(Comparator<? super K> comparator) {
        Object[] array = toArray();
        if (comparator == null) {
            ObjectArrays.unstableSort(array);
        } else {
            ObjectArrays.unstableSort(array, comparator);
        }
        setElements(array);
    }
}
