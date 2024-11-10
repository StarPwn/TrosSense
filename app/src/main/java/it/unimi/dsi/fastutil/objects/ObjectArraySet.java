package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.SafeMath;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/* loaded from: classes4.dex */
public class ObjectArraySet<K> extends AbstractObjectSet<K> implements Serializable, Cloneable {
    private static final long serialVersionUID = 1;
    protected transient Object[] a;
    protected int size;

    public ObjectArraySet(Object[] a) {
        this.a = a;
        this.size = a.length;
    }

    public ObjectArraySet() {
        this.a = ObjectArrays.EMPTY_ARRAY;
    }

    public ObjectArraySet(int capacity) {
        this.a = new Object[capacity];
    }

    public ObjectArraySet(ObjectCollection<K> c) {
        this(c.size());
        addAll(c);
    }

    public ObjectArraySet(Collection<? extends K> c) {
        this(c.size());
        addAll(c);
    }

    public ObjectArraySet(ObjectSet<K> c) {
        this(c.size());
        int i = 0;
        ObjectIterator<K> it2 = c.iterator();
        while (it2.hasNext()) {
            Object x = it2.next();
            this.a[i] = x;
            i++;
        }
        this.size = i;
    }

    public ObjectArraySet(Set<? extends K> c) {
        this(c.size());
        int i = 0;
        for (K x : c) {
            this.a[i] = x;
            i++;
        }
        this.size = i;
    }

    public ObjectArraySet(Object[] a, int size) {
        this.a = a;
        this.size = size;
        if (size > a.length) {
            throw new IllegalArgumentException("The provided size (" + size + ") is larger than or equal to the array size (" + a.length + ")");
        }
    }

    public static <K> ObjectArraySet<K> of() {
        return ofUnchecked();
    }

    public static <K> ObjectArraySet<K> of(K e) {
        return ofUnchecked(e);
    }

    @SafeVarargs
    public static <K> ObjectArraySet<K> of(K... a) {
        if (a.length == 2) {
            if (Objects.equals(a[0], a[1])) {
                throw new IllegalArgumentException("Duplicate element: " + a[1]);
            }
        } else if (a.length > 2) {
            ObjectOpenHashSet.of((Object[]) a);
        }
        return ofUnchecked(a);
    }

    public static <K> ObjectArraySet<K> ofUnchecked() {
        return new ObjectArraySet<>();
    }

    @SafeVarargs
    public static <K> ObjectArraySet<K> ofUnchecked(K... a) {
        return new ObjectArraySet<>(a);
    }

    private int findKey(Object o) {
        int i = this.size;
        while (true) {
            int i2 = i - 1;
            if (i == 0) {
                return -1;
            }
            if (Objects.equals(this.a[i2], o)) {
                return i2;
            }
            i = i2;
        }
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObjectSet, it.unimi.dsi.fastutil.objects.AbstractObjectCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
    public ObjectIterator<K> iterator() {
        return new ObjectIterator<K>() { // from class: it.unimi.dsi.fastutil.objects.ObjectArraySet.1
            int next = 0;

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.next < ObjectArraySet.this.size;
            }

            @Override // java.util.Iterator
            public K next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Object[] objArr = ObjectArraySet.this.a;
                int i = this.next;
                this.next = i + 1;
                return (K) objArr[i];
            }

            @Override // java.util.Iterator
            public void remove() {
                ObjectArraySet objectArraySet = ObjectArraySet.this;
                int i = objectArraySet.size;
                objectArraySet.size = i - 1;
                int i2 = this.next;
                this.next = i2 - 1;
                int tail = i - i2;
                System.arraycopy(ObjectArraySet.this.a, this.next + 1, ObjectArraySet.this.a, this.next, tail);
                ObjectArraySet.this.a[ObjectArraySet.this.size] = null;
            }

            @Override // it.unimi.dsi.fastutil.objects.ObjectIterator
            public int skip(int n) {
                if (n < 0) {
                    throw new IllegalArgumentException("Argument must be nonnegative: " + n);
                }
                int remaining = ObjectArraySet.this.size - this.next;
                if (n < remaining) {
                    this.next += n;
                    return n;
                }
                this.next = ObjectArraySet.this.size;
                return remaining;
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class Spliterator implements ObjectSpliterator<K> {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        boolean hasSplit;
        int max;
        int pos;

        public Spliterator(ObjectArraySet objectArraySet) {
            this(0, objectArraySet.size, false);
        }

        private Spliterator(int pos, int max, boolean hasSplit) {
            this.hasSplit = false;
            if (pos > max) {
                throw new AssertionError("pos " + pos + " must be <= max " + max);
            }
            this.pos = pos;
            this.max = max;
            this.hasSplit = hasSplit;
        }

        private int getWorkingMax() {
            return this.hasSplit ? this.max : ObjectArraySet.this.size;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return 16465;
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return getWorkingMax() - this.pos;
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super K> action) {
            if (this.pos >= getWorkingMax()) {
                return false;
            }
            Object[] objArr = ObjectArraySet.this.a;
            int i = this.pos;
            this.pos = i + 1;
            action.accept(objArr[i]);
            return true;
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super K> action) {
            int max = getWorkingMax();
            while (this.pos < max) {
                action.accept(ObjectArraySet.this.a[this.pos]);
                this.pos++;
            }
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterator
        public long skip(long n) {
            if (n < 0) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
            int max = getWorkingMax();
            if (this.pos >= max) {
                return 0L;
            }
            int remaining = max - this.pos;
            if (n < remaining) {
                this.pos = SafeMath.safeLongToInt(this.pos + n);
                return n;
            }
            long n2 = remaining;
            this.pos = max;
            return n2;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterator, java.util.Spliterator
        public ObjectSpliterator<K> trySplit() {
            int max = getWorkingMax();
            int retLen = (max - this.pos) >> 1;
            if (retLen <= 1) {
                return null;
            }
            this.max = max;
            int myNewPos = this.pos + retLen;
            int oldPos = this.pos;
            this.pos = myNewPos;
            this.hasSplit = true;
            return new Spliterator(oldPos, myNewPos, true);
        }
    }

    @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
    /* renamed from: spliterator */
    public ObjectSpliterator<K> mo221spliterator() {
        return new Spliterator(this);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object k) {
        return findKey(k) != -1;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.size;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(Object k) {
        int pos = findKey(k);
        if (pos == -1) {
            return false;
        }
        int tail = (this.size - pos) - 1;
        for (int i = 0; i < tail; i++) {
            this.a[pos + i] = this.a[pos + i + 1];
        }
        int i2 = this.size;
        this.size = i2 - 1;
        this.a[this.size] = null;
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean add(K k) {
        int pos = findKey(k);
        if (pos != -1) {
            return false;
        }
        if (this.size == this.a.length) {
            Object[] b = new Object[this.size != 0 ? 2 * this.size : 2];
            int i = this.size;
            while (true) {
                int i2 = i - 1;
                if (i == 0) {
                    break;
                }
                b[i2] = this.a[i2];
                i = i2;
            }
            this.a = b;
        }
        Object[] b2 = this.a;
        int i3 = this.size;
        this.size = i3 + 1;
        b2[i3] = k;
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public void clear() {
        Arrays.fill(this.a, 0, this.size, (Object) null);
        this.size = 0;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public Object[] toArray() {
        int size = size();
        return size == 0 ? ObjectArrays.EMPTY_ARRAY : Arrays.copyOf(this.a, size, Object[].class);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public <T> T[] toArray(T[] tArr) {
        if (tArr == null) {
            tArr = (T[]) new Object[this.size];
        } else if (tArr.length < this.size) {
            tArr = (T[]) ((Object[]) Array.newInstance(tArr.getClass().getComponentType(), this.size));
        }
        System.arraycopy(this.a, 0, tArr, 0, this.size);
        if (tArr.length > this.size) {
            tArr[this.size] = null;
        }
        return tArr;
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public ObjectArraySet<K> m268clone() {
        try {
            ObjectArraySet<K> c = (ObjectArraySet) super.clone();
            c.a = (Object[]) this.a.clone();
            return c;
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        for (int i = 0; i < this.size; i++) {
            s.writeObject(this.a[i]);
        }
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.a = new Object[this.size];
        for (int i = 0; i < this.size; i++) {
            this.a[i] = s.readObject();
        }
    }
}
