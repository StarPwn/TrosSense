package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.objects.ObjectCollections;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.RandomAccess;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import okhttp3.HttpUrl;

/* loaded from: classes4.dex */
public final class ObjectLists {
    public static final EmptyList EMPTY_LIST = new EmptyList();

    private ObjectLists() {
    }

    public static <K> ObjectList<K> shuffle(ObjectList<K> l, Random random) {
        int p = l.size();
        while (true) {
            int i = p - 1;
            if (p != 0) {
                int p2 = random.nextInt(i + 1);
                K t = l.get(i);
                l.set(i, l.get(p2));
                l.set(p2, t);
                p = i;
            } else {
                return l;
            }
        }
    }

    /* loaded from: classes4.dex */
    public static class EmptyList<K> extends ObjectCollections.EmptyCollection<K> implements ObjectList<K>, RandomAccess, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;

        protected EmptyList() {
        }

        @Override // java.util.List
        public K get(int i) {
            throw new IndexOutOfBoundsException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean remove(Object k) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.List
        public K remove(int i) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.List
        public void add(int index, K k) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.List
        public K set(int index, K k) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.List
        public int indexOf(Object k) {
            return -1;
        }

        @Override // java.util.List
        public int lastIndexOf(Object k) {
            return -1;
        }

        @Override // java.util.List
        public boolean addAll(int i, Collection<? extends K> c) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.List
        public void replaceAll(UnaryOperator<K> operator) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList, java.util.List
        public void sort(Comparator<? super K> comparator) {
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList
        public void unstableSort(Comparator<? super K> comparator) {
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList, java.util.List
        public ObjectListIterator<K> listIterator() {
            return ObjectIterators.EMPTY_ITERATOR;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.EmptyCollection, it.unimi.dsi.fastutil.objects.AbstractObjectCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
        public ObjectListIterator<K> iterator() {
            return ObjectIterators.EMPTY_ITERATOR;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList, java.util.List
        public ObjectListIterator<K> listIterator(int i) {
            if (i == 0) {
                return ObjectIterators.EMPTY_ITERATOR;
            }
            throw new IndexOutOfBoundsException(String.valueOf(i));
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList, java.util.List
        public ObjectList<K> subList(int from, int to) {
            if (from == 0 && to == 0) {
                return this;
            }
            throw new IndexOutOfBoundsException();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList
        public void getElements(int from, Object[] a, int offset, int length) {
            if (from != 0 || length != 0 || offset < 0 || offset > a.length) {
                throw new IndexOutOfBoundsException();
            }
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList
        public void removeElements(int from, int to) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList
        public void addElements(int index, K[] a, int offset, int length) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList
        public void addElements(int index, K[] a) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList
        public void setElements(K[] a) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList
        public void setElements(int index, K[] a) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList
        public void setElements(int index, K[] a, int offset, int length) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList
        public void size(int s) {
            throw new UnsupportedOperationException();
        }

        @Override // java.lang.Comparable
        public int compareTo(List<? extends K> o) {
            return (o == this || o.isEmpty()) ? 0 : -1;
        }

        public Object clone() {
            return ObjectLists.EMPTY_LIST;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.EmptyCollection, java.util.Collection
        public int hashCode() {
            return 1;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.EmptyCollection, java.util.Collection
        public boolean equals(Object o) {
            return (o instanceof List) && ((List) o).isEmpty();
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectCollection, java.util.AbstractCollection
        public String toString() {
            return HttpUrl.PATH_SEGMENT_ENCODE_SET_URI;
        }

        private Object readResolve() {
            return ObjectLists.EMPTY_LIST;
        }
    }

    public static <K> ObjectList<K> emptyList() {
        return EMPTY_LIST;
    }

    /* loaded from: classes4.dex */
    public static class Singleton<K> extends AbstractObjectList<K> implements RandomAccess, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        private final K element;

        protected Singleton(K element) {
            this.element = element;
        }

        @Override // java.util.List
        public K get(int i) {
            if (i == 0) {
                return this.element;
            }
            throw new IndexOutOfBoundsException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean remove(Object k) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.util.List
        public K remove(int i) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean contains(Object k) {
            return Objects.equals(k, this.element);
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.util.List
        public int indexOf(Object k) {
            return Objects.equals(k, this.element) ? 0 : -1;
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.util.AbstractCollection, java.util.Collection, java.util.List
        public Object[] toArray() {
            return new Object[]{this.element};
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, it.unimi.dsi.fastutil.objects.ObjectList, java.util.List
        public ObjectListIterator<K> listIterator() {
            return ObjectIterators.singleton(this.element);
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, it.unimi.dsi.fastutil.objects.AbstractObjectCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
        public ObjectListIterator<K> iterator() {
            return listIterator();
        }

        @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
        /* renamed from: spliterator */
        public ObjectSpliterator<K> mo221spliterator() {
            return ObjectSpliterators.singleton(this.element);
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, it.unimi.dsi.fastutil.objects.ObjectList, java.util.List
        public ObjectListIterator<K> listIterator(int i) {
            if (i > 1 || i < 0) {
                throw new IndexOutOfBoundsException();
            }
            ObjectListIterator<K> l = listIterator();
            if (i == 1) {
                l.next();
            }
            return l;
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, it.unimi.dsi.fastutil.objects.ObjectList, java.util.List
        public ObjectList<K> subList(int from, int to) {
            ensureIndex(from);
            ensureIndex(to);
            if (from > to) {
                throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            if (from != 0 || to != 1) {
                return ObjectLists.EMPTY_LIST;
            }
            return this;
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.lang.Iterable
        public void forEach(Consumer<? super K> consumer) {
            consumer.accept(this.element);
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.util.List
        public boolean addAll(int i, Collection<? extends K> c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean addAll(Collection<? extends K> c) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Collection
        public boolean removeIf(Predicate<? super K> filter) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.List
        public void replaceAll(UnaryOperator<K> operator) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList, java.util.List
        public void sort(Comparator<? super K> comparator) {
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList
        public void unstableSort(Comparator<? super K> comparator) {
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, it.unimi.dsi.fastutil.objects.ObjectList
        public void getElements(int from, Object[] a, int offset, int length) {
            if (offset < 0) {
                throw new ArrayIndexOutOfBoundsException("Offset (" + offset + ") is negative");
            }
            if (offset + length > a.length) {
                throw new ArrayIndexOutOfBoundsException("End index (" + (offset + length) + ") is greater than array length (" + a.length + ")");
            }
            if (from + length > size()) {
                throw new IndexOutOfBoundsException("End index (" + (from + length) + ") is greater than list size (" + size() + ")");
            }
            if (length <= 0) {
                return;
            }
            a[offset] = this.element;
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, it.unimi.dsi.fastutil.objects.ObjectList
        public void removeElements(int from, int to) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, it.unimi.dsi.fastutil.objects.ObjectList
        public void addElements(int index, K[] a) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, it.unimi.dsi.fastutil.objects.ObjectList
        public void addElements(int index, K[] a, int offset, int length) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList
        public void setElements(K[] a) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList
        public void setElements(int index, K[] a) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, it.unimi.dsi.fastutil.objects.ObjectList
        public void setElements(int index, K[] a, int offset, int length) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return 1;
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, it.unimi.dsi.fastutil.objects.ObjectList
        public void size(int size) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.util.AbstractCollection, java.util.Collection, java.util.List
        public void clear() {
            throw new UnsupportedOperationException();
        }

        public Object clone() {
            return this;
        }
    }

    public static <K> ObjectList<K> singleton(K element) {
        return new Singleton(element);
    }

    /* loaded from: classes4.dex */
    public static class SynchronizedList<K> extends ObjectCollections.SynchronizedCollection<K> implements ObjectList<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ObjectList<K> list;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean add(Object obj) {
            return super.add(obj);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean addAll(Collection collection) {
            return super.addAll(collection);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ void clear() {
            super.clear();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean contains(Object obj) {
            return super.contains(obj);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean containsAll(Collection collection) {
            return super.containsAll(collection);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.SynchronizedCollection, java.lang.Iterable
        public /* bridge */ /* synthetic */ void forEach(Consumer consumer) {
            super.forEach(consumer);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean isEmpty() {
            return super.isEmpty();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ Stream parallelStream() {
            return super.parallelStream();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean remove(Object obj) {
            return super.remove(obj);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean removeAll(Collection collection) {
            return super.removeAll(collection);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean retainAll(Collection collection) {
            return super.retainAll(collection);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ int size() {
            return super.size();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.SynchronizedCollection, it.unimi.dsi.fastutil.objects.ObjectCollection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectIterable
        /* renamed from: spliterator */
        public /* bridge */ /* synthetic */ ObjectSpliterator mo221spliterator() {
            return super.mo221spliterator();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ Stream stream() {
            return super.stream();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ Object[] toArray() {
            return super.toArray();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.SynchronizedCollection, java.util.Collection
        public /* bridge */ /* synthetic */ Object[] toArray(Object[] objArr) {
            return super.toArray(objArr);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.SynchronizedCollection
        public /* bridge */ /* synthetic */ String toString() {
            return super.toString();
        }

        protected SynchronizedList(ObjectList<K> l, Object sync) {
            super(l, sync);
            this.list = l;
        }

        protected SynchronizedList(ObjectList<K> l) {
            super(l);
            this.list = l;
        }

        @Override // java.util.List
        public K get(int i) {
            K k;
            synchronized (this.sync) {
                k = this.list.get(i);
            }
            return k;
        }

        @Override // java.util.List
        public K set(int i, K k) {
            K k2;
            synchronized (this.sync) {
                k2 = this.list.set(i, k);
            }
            return k2;
        }

        @Override // java.util.List
        public void add(int i, K k) {
            synchronized (this.sync) {
                this.list.add(i, k);
            }
        }

        @Override // java.util.List
        public K remove(int i) {
            K remove;
            synchronized (this.sync) {
                remove = this.list.remove(i);
            }
            return remove;
        }

        @Override // java.util.List
        public int indexOf(Object k) {
            int indexOf;
            synchronized (this.sync) {
                indexOf = this.list.indexOf(k);
            }
            return indexOf;
        }

        @Override // java.util.List
        public int lastIndexOf(Object k) {
            int lastIndexOf;
            synchronized (this.sync) {
                lastIndexOf = this.list.lastIndexOf(k);
            }
            return lastIndexOf;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.SynchronizedCollection, java.util.Collection
        public boolean removeIf(Predicate<? super K> filter) {
            boolean removeIf;
            synchronized (this.sync) {
                removeIf = this.list.removeIf(filter);
            }
            return removeIf;
        }

        @Override // java.util.List
        public void replaceAll(UnaryOperator<K> operator) {
            synchronized (this.sync) {
                this.list.replaceAll(operator);
            }
        }

        @Override // java.util.List
        public boolean addAll(int index, Collection<? extends K> c) {
            boolean addAll;
            synchronized (this.sync) {
                addAll = this.list.addAll(index, c);
            }
            return addAll;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList
        public void getElements(int from, Object[] a, int offset, int length) {
            synchronized (this.sync) {
                this.list.getElements(from, a, offset, length);
            }
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList
        public void removeElements(int from, int to) {
            synchronized (this.sync) {
                this.list.removeElements(from, to);
            }
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList
        public void addElements(int index, K[] a, int offset, int length) {
            synchronized (this.sync) {
                this.list.addElements(index, a, offset, length);
            }
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList
        public void addElements(int index, K[] a) {
            synchronized (this.sync) {
                this.list.addElements(index, a);
            }
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList
        public void setElements(K[] a) {
            synchronized (this.sync) {
                this.list.setElements(a);
            }
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList
        public void setElements(int index, K[] a) {
            synchronized (this.sync) {
                this.list.setElements(index, a);
            }
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList
        public void setElements(int index, K[] a, int offset, int length) {
            synchronized (this.sync) {
                this.list.setElements(index, a, offset, length);
            }
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList
        public void size(int size) {
            synchronized (this.sync) {
                this.list.size(size);
            }
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList, java.util.List
        public ObjectListIterator<K> listIterator() {
            return this.list.listIterator();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.SynchronizedCollection, it.unimi.dsi.fastutil.objects.ObjectCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectIterable
        public ObjectListIterator<K> iterator() {
            return listIterator();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList, java.util.List
        public ObjectListIterator<K> listIterator(int i) {
            return this.list.listIterator(i);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList, java.util.List
        public ObjectList<K> subList(int from, int to) {
            SynchronizedList synchronizedList;
            synchronized (this.sync) {
                synchronizedList = new SynchronizedList(this.list.subList(from, to), this.sync);
            }
            return synchronizedList;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.SynchronizedCollection, java.util.Collection
        public boolean equals(Object o) {
            boolean equals;
            if (o == this) {
                return true;
            }
            synchronized (this.sync) {
                equals = this.collection.equals(o);
            }
            return equals;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.SynchronizedCollection, java.util.Collection
        public int hashCode() {
            int hashCode;
            synchronized (this.sync) {
                hashCode = this.collection.hashCode();
            }
            return hashCode;
        }

        @Override // java.lang.Comparable
        public int compareTo(List<? extends K> o) {
            int compareTo;
            synchronized (this.sync) {
                compareTo = this.list.compareTo(o);
            }
            return compareTo;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList, java.util.List
        public void sort(Comparator<? super K> comparator) {
            synchronized (this.sync) {
                this.list.sort(comparator);
            }
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList
        public void unstableSort(Comparator<? super K> comparator) {
            synchronized (this.sync) {
                this.list.unstableSort(comparator);
            }
        }

        private void writeObject(ObjectOutputStream s) throws IOException {
            synchronized (this.sync) {
                s.defaultWriteObject();
            }
        }
    }

    /* loaded from: classes4.dex */
    public static class SynchronizedRandomAccessList<K> extends SynchronizedList<K> implements RandomAccess, Serializable {
        private static final long serialVersionUID = 0;

        protected SynchronizedRandomAccessList(ObjectList<K> l, Object sync) {
            super(l, sync);
        }

        protected SynchronizedRandomAccessList(ObjectList<K> l) {
            super(l);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectLists.SynchronizedList, it.unimi.dsi.fastutil.objects.ObjectList, java.util.List
        public ObjectList<K> subList(int from, int to) {
            SynchronizedRandomAccessList synchronizedRandomAccessList;
            synchronized (this.sync) {
                synchronizedRandomAccessList = new SynchronizedRandomAccessList(this.list.subList(from, to), this.sync);
            }
            return synchronizedRandomAccessList;
        }
    }

    public static <K> ObjectList<K> synchronize(ObjectList<K> l) {
        return l instanceof RandomAccess ? new SynchronizedRandomAccessList(l) : new SynchronizedList(l);
    }

    public static <K> ObjectList<K> synchronize(ObjectList<K> l, Object sync) {
        return l instanceof RandomAccess ? new SynchronizedRandomAccessList(l, sync) : new SynchronizedList(l, sync);
    }

    /* loaded from: classes4.dex */
    public static class UnmodifiableList<K> extends ObjectCollections.UnmodifiableCollection<K> implements ObjectList<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ObjectList<? extends K> list;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean add(Object obj) {
            return super.add(obj);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean addAll(Collection collection) {
            return super.addAll(collection);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ void clear() {
            super.clear();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean contains(Object obj) {
            return super.contains(obj);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean containsAll(Collection collection) {
            return super.containsAll(collection);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.UnmodifiableCollection, java.lang.Iterable
        public /* bridge */ /* synthetic */ void forEach(Consumer consumer) {
            super.forEach(consumer);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean isEmpty() {
            return super.isEmpty();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ Stream parallelStream() {
            return super.parallelStream();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean remove(Object obj) {
            return super.remove(obj);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean removeAll(Collection collection) {
            return super.removeAll(collection);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean removeIf(Predicate predicate) {
            return super.removeIf(predicate);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ boolean retainAll(Collection collection) {
            return super.retainAll(collection);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ int size() {
            return super.size();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.objects.ObjectCollection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectIterable
        /* renamed from: spliterator */
        public /* bridge */ /* synthetic */ ObjectSpliterator mo221spliterator() {
            return super.mo221spliterator();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ Stream stream() {
            return super.stream();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ Object[] toArray() {
            return super.toArray();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.UnmodifiableCollection, java.util.Collection
        public /* bridge */ /* synthetic */ Object[] toArray(Object[] objArr) {
            return super.toArray(objArr);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.UnmodifiableCollection
        public /* bridge */ /* synthetic */ String toString() {
            return super.toString();
        }

        protected UnmodifiableList(ObjectList<? extends K> l) {
            super(l);
            this.list = l;
        }

        @Override // java.util.List
        public K get(int i) {
            return this.list.get(i);
        }

        @Override // java.util.List
        public K set(int i, K k) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.List
        public void add(int i, K k) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.List
        public K remove(int i) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.List
        public int indexOf(Object k) {
            return this.list.indexOf(k);
        }

        @Override // java.util.List
        public int lastIndexOf(Object k) {
            return this.list.lastIndexOf(k);
        }

        @Override // java.util.List
        public boolean addAll(int index, Collection<? extends K> c) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.List
        public void replaceAll(UnaryOperator<K> operator) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList
        public void getElements(int from, Object[] a, int offset, int length) {
            this.list.getElements(from, a, offset, length);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList
        public void removeElements(int from, int to) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList
        public void addElements(int index, K[] a, int offset, int length) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList
        public void addElements(int index, K[] a) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList
        public void setElements(K[] a) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList
        public void setElements(int index, K[] a) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList
        public void setElements(int index, K[] a, int offset, int length) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList
        public void size(int size) {
            this.list.size(size);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList, java.util.List
        public ObjectListIterator<K> listIterator() {
            return ObjectIterators.unmodifiable((ObjectListIterator) this.list.listIterator());
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.UnmodifiableCollection, it.unimi.dsi.fastutil.objects.ObjectCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectIterable
        public ObjectListIterator<K> iterator() {
            return listIterator();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList, java.util.List
        public ObjectListIterator<K> listIterator(int i) {
            return ObjectIterators.unmodifiable((ObjectListIterator) this.list.listIterator(i));
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList, java.util.List
        public ObjectList<K> subList(int from, int to) {
            return new UnmodifiableList(this.list.subList(from, to));
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.UnmodifiableCollection, java.util.Collection
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            return this.collection.equals(o);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectCollections.UnmodifiableCollection, java.util.Collection
        public int hashCode() {
            return this.collection.hashCode();
        }

        @Override // java.lang.Comparable
        public int compareTo(List<? extends K> o) {
            return this.list.compareTo(o);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList, java.util.List
        public void sort(Comparator<? super K> comparator) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList
        public void unstableSort(Comparator<? super K> comparator) {
            throw new UnsupportedOperationException();
        }
    }

    /* loaded from: classes4.dex */
    public static class UnmodifiableRandomAccessList<K> extends UnmodifiableList<K> implements RandomAccess, Serializable {
        private static final long serialVersionUID = 0;

        protected UnmodifiableRandomAccessList(ObjectList<? extends K> l) {
            super(l);
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectLists.UnmodifiableList, it.unimi.dsi.fastutil.objects.ObjectList, java.util.List
        public ObjectList<K> subList(int from, int to) {
            return new UnmodifiableRandomAccessList(this.list.subList(from, to));
        }
    }

    public static <K> ObjectList<K> unmodifiable(ObjectList<? extends K> l) {
        return l instanceof RandomAccess ? new UnmodifiableRandomAccessList(l) : new UnmodifiableList(l);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static abstract class ImmutableListBase<K> extends AbstractObjectList<K> implements ObjectList<K> {
        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.util.List
        @Deprecated
        public final void add(int index, K k) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.util.AbstractCollection, java.util.Collection, java.util.List
        @Deprecated
        public final boolean add(K k) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.util.AbstractCollection, java.util.Collection, java.util.List
        @Deprecated
        public final boolean addAll(Collection<? extends K> c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.util.List
        @Deprecated
        public final boolean addAll(int index, Collection<? extends K> c) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.util.List
        @Deprecated
        public final K remove(int index) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        @Deprecated
        public final boolean remove(Object k) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        @Deprecated
        public final boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        @Deprecated
        public final boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Collection
        @Deprecated
        public final boolean removeIf(Predicate<? super K> c) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.List
        @Deprecated
        public final void replaceAll(UnaryOperator<K> operator) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.util.List
        @Deprecated
        public final K set(int index, K k) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, java.util.AbstractCollection, java.util.Collection, java.util.List
        @Deprecated
        public final void clear() {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, it.unimi.dsi.fastutil.objects.ObjectList
        @Deprecated
        public final void size(int size) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, it.unimi.dsi.fastutil.objects.ObjectList
        @Deprecated
        public final void removeElements(int from, int to) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, it.unimi.dsi.fastutil.objects.ObjectList
        @Deprecated
        public final void addElements(int index, K[] a, int offset, int length) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObjectList, it.unimi.dsi.fastutil.objects.ObjectList
        @Deprecated
        public final void setElements(int index, K[] a, int offset, int length) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList, java.util.List
        @Deprecated
        public final void sort(Comparator<? super K> comparator) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectList
        @Deprecated
        public final void unstableSort(Comparator<? super K> comparator) {
            throw new UnsupportedOperationException();
        }
    }
}
