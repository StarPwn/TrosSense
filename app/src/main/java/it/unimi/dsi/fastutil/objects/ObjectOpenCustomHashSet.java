package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.HashCommon;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

/* loaded from: classes4.dex */
public class ObjectOpenCustomHashSet<K> extends AbstractObjectSet<K> implements Serializable, Cloneable, Hash {
    private static final boolean ASSERTS = false;
    private static final long serialVersionUID = 0;
    protected transient boolean containsNull;
    protected final float f;
    protected transient K[] key;
    protected transient int mask;
    protected transient int maxFill;
    protected final transient int minN;
    protected transient int n;
    protected int size;
    protected Hash.Strategy<? super K> strategy;

    public ObjectOpenCustomHashSet(int i, float f, Hash.Strategy<? super K> strategy) {
        this.strategy = strategy;
        if (f <= 0.0f || f >= 1.0f) {
            throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than 1");
        }
        if (i < 0) {
            throw new IllegalArgumentException("The expected number of elements must be nonnegative");
        }
        this.f = f;
        int arraySize = HashCommon.arraySize(i, f);
        this.n = arraySize;
        this.minN = arraySize;
        this.mask = this.n - 1;
        this.maxFill = HashCommon.maxFill(this.n, f);
        this.key = (K[]) new Object[this.n + 1];
    }

    public ObjectOpenCustomHashSet(int expected, Hash.Strategy<? super K> strategy) {
        this(expected, 0.75f, strategy);
    }

    public ObjectOpenCustomHashSet(Hash.Strategy<? super K> strategy) {
        this(16, 0.75f, strategy);
    }

    public ObjectOpenCustomHashSet(Collection<? extends K> c, float f, Hash.Strategy<? super K> strategy) {
        this(c.size(), f, strategy);
        addAll(c);
    }

    public ObjectOpenCustomHashSet(Collection<? extends K> c, Hash.Strategy<? super K> strategy) {
        this(c, 0.75f, strategy);
    }

    public ObjectOpenCustomHashSet(ObjectCollection<? extends K> c, float f, Hash.Strategy<? super K> strategy) {
        this(c.size(), f, strategy);
        addAll(c);
    }

    public ObjectOpenCustomHashSet(ObjectCollection<? extends K> c, Hash.Strategy<? super K> strategy) {
        this((ObjectCollection) c, 0.75f, (Hash.Strategy) strategy);
    }

    public ObjectOpenCustomHashSet(Iterator<? extends K> i, float f, Hash.Strategy<? super K> strategy) {
        this(16, f, strategy);
        while (i.hasNext()) {
            add(i.next());
        }
    }

    public ObjectOpenCustomHashSet(Iterator<? extends K> i, Hash.Strategy<? super K> strategy) {
        this(i, 0.75f, strategy);
    }

    public ObjectOpenCustomHashSet(K[] a, int offset, int length, float f, Hash.Strategy<? super K> strategy) {
        this(length < 0 ? 0 : length, f, strategy);
        ObjectArrays.ensureOffsetLength(a, offset, length);
        for (int i = 0; i < length; i++) {
            add(a[offset + i]);
        }
    }

    public ObjectOpenCustomHashSet(K[] a, int offset, int length, Hash.Strategy<? super K> strategy) {
        this(a, offset, length, 0.75f, strategy);
    }

    public ObjectOpenCustomHashSet(K[] a, float f, Hash.Strategy<? super K> strategy) {
        this(a, 0, a.length, f, strategy);
    }

    public ObjectOpenCustomHashSet(K[] a, Hash.Strategy<? super K> strategy) {
        this(a, 0.75f, strategy);
    }

    public Hash.Strategy<? super K> strategy() {
        return this.strategy;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int realSize() {
        return this.containsNull ? this.size - 1 : this.size;
    }

    public void ensureCapacity(int capacity) {
        int needed = HashCommon.arraySize(capacity, this.f);
        if (needed > this.n) {
            rehash(needed);
        }
    }

    private void tryCapacity(long capacity) {
        int needed = (int) Math.min(1073741824L, Math.max(2L, HashCommon.nextPowerOfTwo((long) Math.ceil(((float) capacity) / this.f))));
        if (needed > this.n) {
            rehash(needed);
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean addAll(Collection<? extends K> c) {
        if (this.f <= 0.5d) {
            ensureCapacity(c.size());
        } else {
            tryCapacity(size() + c.size());
        }
        return super.addAll(c);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean add(K k) {
        K curr;
        if (this.strategy.equals(k, null)) {
            if (this.containsNull) {
                return false;
            }
            this.containsNull = true;
            this.key[this.n] = k;
        } else {
            K[] key = this.key;
            int mix = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
            int pos = mix;
            K curr2 = key[mix];
            if (curr2 != null) {
                if (this.strategy.equals(curr2, k)) {
                    return false;
                }
                do {
                    int i = (pos + 1) & this.mask;
                    pos = i;
                    curr = key[i];
                    if (curr != null) {
                    }
                } while (!this.strategy.equals(curr, k));
                return false;
            }
            key[pos] = k;
        }
        int i2 = this.size;
        this.size = i2 + 1;
        if (i2 >= this.maxFill) {
            rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
        return true;
    }

    public K addOrGet(K k) {
        K curr;
        if (this.strategy.equals(k, null)) {
            if (this.containsNull) {
                return this.key[this.n];
            }
            this.containsNull = true;
            this.key[this.n] = k;
        } else {
            K[] key = this.key;
            int mix = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
            int pos = mix;
            K curr2 = key[mix];
            if (curr2 != null) {
                if (this.strategy.equals(curr2, k)) {
                    return curr2;
                }
                do {
                    int i = (pos + 1) & this.mask;
                    pos = i;
                    curr = key[i];
                    if (curr != null) {
                    }
                } while (!this.strategy.equals(curr, k));
                return curr;
            }
            key[pos] = k;
        }
        int i2 = this.size;
        this.size = i2 + 1;
        if (i2 >= this.maxFill) {
            rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
        return k;
    }

    protected final void shiftKeys(int pos) {
        K curr;
        K[] key = this.key;
        while (true) {
            int last = pos;
            pos = (pos + 1) & this.mask;
            while (true) {
                curr = key[pos];
                if (curr == null) {
                    key[last] = null;
                    return;
                }
                int slot = HashCommon.mix(this.strategy.hashCode(curr)) & this.mask;
                if (last > pos) {
                    if (last >= slot && slot > pos) {
                        break;
                    }
                    pos = (pos + 1) & this.mask;
                } else if (last < slot && slot <= pos) {
                    pos = (pos + 1) & this.mask;
                }
            }
            key[last] = curr;
        }
    }

    private boolean removeEntry(int pos) {
        this.size--;
        shiftKeys(pos);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            rehash(this.n / 2);
        }
        return true;
    }

    private boolean removeNullEntry() {
        this.containsNull = false;
        this.key[this.n] = null;
        this.size--;
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            rehash(this.n / 2);
        }
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(Object k) {
        K curr;
        if (this.strategy.equals(k, null)) {
            if (this.containsNull) {
                return removeNullEntry();
            }
            return false;
        }
        K[] key = this.key;
        int mix = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
        int pos = mix;
        K curr2 = key[mix];
        if (curr2 == null) {
            return false;
        }
        if (this.strategy.equals(k, curr2)) {
            return removeEntry(pos);
        }
        do {
            int i = (pos + 1) & this.mask;
            pos = i;
            curr = key[i];
            if (curr == null) {
                return false;
            }
        } while (!this.strategy.equals(k, curr));
        return removeEntry(pos);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object k) {
        K curr;
        if (this.strategy.equals(k, null)) {
            return this.containsNull;
        }
        K[] key = this.key;
        int mix = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
        int pos = mix;
        K curr2 = key[mix];
        if (curr2 == null) {
            return false;
        }
        if (this.strategy.equals(k, curr2)) {
            return true;
        }
        do {
            int i = (pos + 1) & this.mask;
            pos = i;
            curr = key[i];
            if (curr == null) {
                return false;
            }
        } while (!this.strategy.equals(k, curr));
        return true;
    }

    public K get(Object k) {
        K curr;
        if (this.strategy.equals(k, null)) {
            return this.key[this.n];
        }
        K[] key = this.key;
        int mix = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
        int pos = mix;
        K curr2 = key[mix];
        if (curr2 == null) {
            return null;
        }
        if (this.strategy.equals(k, curr2)) {
            return curr2;
        }
        do {
            int i = (pos + 1) & this.mask;
            pos = i;
            curr = key[i];
            if (curr == null) {
                return null;
            }
        } while (!this.strategy.equals(k, curr));
        return curr;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public void clear() {
        if (this.size == 0) {
            return;
        }
        this.size = 0;
        this.containsNull = false;
        Arrays.fill(this.key, (Object) null);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.size;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean isEmpty() {
        return this.size == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class SetIterator implements ObjectIterator<K> {
        int c;
        int last;
        boolean mustReturnNull;
        int pos;
        ObjectArrayList<K> wrapped;

        private SetIterator() {
            this.pos = ObjectOpenCustomHashSet.this.n;
            this.last = -1;
            this.c = ObjectOpenCustomHashSet.this.size;
            this.mustReturnNull = ObjectOpenCustomHashSet.this.containsNull;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.c != 0;
        }

        @Override // java.util.Iterator
        public K next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            this.c--;
            if (this.mustReturnNull) {
                this.mustReturnNull = false;
                this.last = ObjectOpenCustomHashSet.this.n;
                return ObjectOpenCustomHashSet.this.key[ObjectOpenCustomHashSet.this.n];
            }
            K[] key = ObjectOpenCustomHashSet.this.key;
            do {
                int i = this.pos - 1;
                this.pos = i;
                if (i < 0) {
                    this.last = Integer.MIN_VALUE;
                    return this.wrapped.get((-this.pos) - 1);
                }
            } while (key[this.pos] == null);
            int i2 = this.pos;
            this.last = i2;
            return key[i2];
        }

        private final void shiftKeys(int pos) {
            K curr;
            K[] key = ObjectOpenCustomHashSet.this.key;
            while (true) {
                int last = pos;
                pos = (pos + 1) & ObjectOpenCustomHashSet.this.mask;
                while (true) {
                    curr = key[pos];
                    if (curr == null) {
                        key[last] = null;
                        return;
                    }
                    int slot = HashCommon.mix(ObjectOpenCustomHashSet.this.strategy.hashCode(curr)) & ObjectOpenCustomHashSet.this.mask;
                    if (last > pos) {
                        if (last >= slot && slot > pos) {
                            break;
                        }
                        pos = (pos + 1) & ObjectOpenCustomHashSet.this.mask;
                    } else if (last >= slot || slot > pos) {
                        break;
                    } else {
                        pos = (pos + 1) & ObjectOpenCustomHashSet.this.mask;
                    }
                }
                if (pos < last) {
                    if (this.wrapped == null) {
                        this.wrapped = new ObjectArrayList<>(2);
                    }
                    this.wrapped.add(key[pos]);
                }
                key[last] = curr;
            }
        }

        @Override // java.util.Iterator
        public void remove() {
            if (this.last == -1) {
                throw new IllegalStateException();
            }
            if (this.last == ObjectOpenCustomHashSet.this.n) {
                ObjectOpenCustomHashSet.this.containsNull = false;
                ObjectOpenCustomHashSet.this.key[ObjectOpenCustomHashSet.this.n] = null;
            } else {
                if (this.pos < 0) {
                    ObjectOpenCustomHashSet.this.remove(this.wrapped.set((-this.pos) - 1, null));
                    this.last = -1;
                    return;
                }
                shiftKeys(this.last);
            }
            ObjectOpenCustomHashSet objectOpenCustomHashSet = ObjectOpenCustomHashSet.this;
            objectOpenCustomHashSet.size--;
            this.last = -1;
        }

        @Override // java.util.Iterator
        public void forEachRemaining(Consumer<? super K> consumer) {
            K[] kArr = ObjectOpenCustomHashSet.this.key;
            if (this.mustReturnNull) {
                this.mustReturnNull = false;
                this.last = ObjectOpenCustomHashSet.this.n;
                consumer.accept(kArr[ObjectOpenCustomHashSet.this.n]);
                this.c--;
            }
            while (this.c != 0) {
                int i = this.pos - 1;
                this.pos = i;
                if (i < 0) {
                    this.last = Integer.MIN_VALUE;
                    consumer.accept(this.wrapped.get((-this.pos) - 1));
                    this.c--;
                } else if (kArr[this.pos] != null) {
                    int i2 = this.pos;
                    this.last = i2;
                    consumer.accept(kArr[i2]);
                    this.c--;
                }
            }
        }
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObjectSet, it.unimi.dsi.fastutil.objects.AbstractObjectCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
    public ObjectIterator<K> iterator() {
        return new SetIterator();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class SetSpliterator implements ObjectSpliterator<K> {
        private static final int POST_SPLIT_CHARACTERISTICS = 1;
        int c;
        boolean hasSplit;
        int max;
        boolean mustReturnNull;
        int pos;

        SetSpliterator() {
            this.pos = 0;
            this.max = ObjectOpenCustomHashSet.this.n;
            this.c = 0;
            this.mustReturnNull = ObjectOpenCustomHashSet.this.containsNull;
            this.hasSplit = false;
        }

        SetSpliterator(int pos, int max, boolean mustReturnNull, boolean hasSplit) {
            this.pos = 0;
            this.max = ObjectOpenCustomHashSet.this.n;
            this.c = 0;
            this.mustReturnNull = ObjectOpenCustomHashSet.this.containsNull;
            this.hasSplit = false;
            this.pos = pos;
            this.max = max;
            this.mustReturnNull = mustReturnNull;
            this.hasSplit = hasSplit;
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super K> consumer) {
            if (this.mustReturnNull) {
                this.mustReturnNull = false;
                this.c++;
                consumer.accept(ObjectOpenCustomHashSet.this.key[ObjectOpenCustomHashSet.this.n]);
                return true;
            }
            K[] kArr = ObjectOpenCustomHashSet.this.key;
            while (this.pos < this.max) {
                if (kArr[this.pos] != null) {
                    this.c++;
                    int i = this.pos;
                    this.pos = i + 1;
                    consumer.accept(kArr[i]);
                    return true;
                }
                this.pos++;
            }
            return false;
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super K> consumer) {
            K[] kArr = ObjectOpenCustomHashSet.this.key;
            if (this.mustReturnNull) {
                this.mustReturnNull = false;
                consumer.accept(kArr[ObjectOpenCustomHashSet.this.n]);
                this.c++;
            }
            while (this.pos < this.max) {
                if (kArr[this.pos] != null) {
                    consumer.accept(kArr[this.pos]);
                    this.c++;
                }
                this.pos++;
            }
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return this.hasSplit ? 1 : 65;
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            if (this.hasSplit) {
                return Math.min(ObjectOpenCustomHashSet.this.size - this.c, ((long) ((ObjectOpenCustomHashSet.this.realSize() / ObjectOpenCustomHashSet.this.n) * (this.max - this.pos))) + (this.mustReturnNull ? 1L : 0L));
            }
            return ObjectOpenCustomHashSet.this.size - this.c;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterator, java.util.Spliterator
        public ObjectOpenCustomHashSet<K>.SetSpliterator trySplit() {
            int retLen;
            if (this.pos >= this.max - 1 || (retLen = (this.max - this.pos) >> 1) <= 1) {
                return null;
            }
            int myNewPos = this.pos + retLen;
            int retPos = this.pos;
            ObjectOpenCustomHashSet<K>.SetSpliterator split = new SetSpliterator(retPos, myNewPos, this.mustReturnNull, true);
            this.pos = myNewPos;
            this.mustReturnNull = false;
            this.hasSplit = true;
            return split;
        }

        @Override // it.unimi.dsi.fastutil.objects.ObjectSpliterator
        public long skip(long n) {
            if (n < 0) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
            if (n == 0) {
                return 0L;
            }
            long skipped = 0;
            if (this.mustReturnNull) {
                this.mustReturnNull = false;
                skipped = 0 + 1;
                n--;
            }
            K[] key = ObjectOpenCustomHashSet.this.key;
            while (this.pos < this.max && n > 0) {
                int i = this.pos;
                this.pos = i + 1;
                if (key[i] != null) {
                    skipped++;
                    n--;
                }
            }
            return skipped;
        }
    }

    @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.objects.ObjectCollection, it.unimi.dsi.fastutil.objects.ObjectIterable
    /* renamed from: spliterator */
    public ObjectSpliterator<K> mo221spliterator() {
        return new SetSpliterator();
    }

    @Override // java.lang.Iterable
    public void forEach(Consumer<? super K> consumer) {
        if (this.containsNull) {
            consumer.accept(this.key[this.n]);
        }
        K[] kArr = this.key;
        int i = this.n;
        while (true) {
            int i2 = i - 1;
            if (i == 0) {
                return;
            }
            if (kArr[i2] != null) {
                consumer.accept(kArr[i2]);
            }
            i = i2;
        }
    }

    public boolean trim() {
        return trim(this.size);
    }

    public boolean trim(int n) {
        int l = HashCommon.nextPowerOfTwo((int) Math.ceil(n / this.f));
        if (l >= this.n || this.size > HashCommon.maxFill(l, this.f)) {
            return true;
        }
        try {
            rehash(l);
            return true;
        } catch (OutOfMemoryError e) {
            return false;
        }
    }

    protected void rehash(int i) {
        int i2;
        K[] kArr = this.key;
        int i3 = i - 1;
        K[] kArr2 = (K[]) new Object[i + 1];
        int i4 = this.n;
        int realSize = realSize();
        while (true) {
            int i5 = realSize - 1;
            if (realSize == 0) {
                this.n = i;
                this.mask = i3;
                this.maxFill = HashCommon.maxFill(this.n, this.f);
                this.key = kArr2;
                return;
            }
            do {
                i4--;
            } while (kArr[i4] == null);
            int mix = HashCommon.mix(this.strategy.hashCode(kArr[i4])) & i3;
            int i6 = mix;
            if (kArr2[mix] == null) {
                kArr2[i6] = kArr[i4];
                realSize = i5;
            }
            do {
                i2 = (i6 + 1) & i3;
                i6 = i2;
            } while (kArr2[i2] != null);
            kArr2[i6] = kArr[i4];
            realSize = i5;
        }
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public ObjectOpenCustomHashSet<K> m273clone() {
        try {
            ObjectOpenCustomHashSet<K> objectOpenCustomHashSet = (ObjectOpenCustomHashSet) super.clone();
            objectOpenCustomHashSet.key = (K[]) ((Object[]) this.key.clone());
            objectOpenCustomHashSet.containsNull = this.containsNull;
            objectOpenCustomHashSet.strategy = this.strategy;
            return objectOpenCustomHashSet;
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    @Override // it.unimi.dsi.fastutil.objects.AbstractObjectSet, java.util.Collection, java.util.Set
    public int hashCode() {
        int i = 0;
        int realSize = realSize();
        int i2 = 0;
        while (true) {
            int i3 = realSize - 1;
            if (realSize != 0) {
                while (this.key[i2] == null) {
                    i2++;
                }
                if (this != this.key[i2]) {
                    i += this.strategy.hashCode(this.key[i2]);
                }
                i2++;
                realSize = i3;
            } else {
                return i;
            }
        }
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        ObjectIterator<K> i = iterator();
        s.defaultWriteObject();
        int j = this.size;
        while (true) {
            int j2 = j - 1;
            if (j == 0) {
                return;
            }
            s.writeObject(i.next());
            j = j2;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        int i;
        int i2;
        objectInputStream.defaultReadObject();
        this.n = HashCommon.arraySize(this.size, this.f);
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.mask = this.n - 1;
        K[] kArr = (K[]) new Object[this.n + 1];
        this.key = kArr;
        int i3 = this.size;
        while (true) {
            int i4 = i3 - 1;
            if (i3 != 0) {
                Object readObject = objectInputStream.readObject();
                if (this.strategy.equals(readObject, null)) {
                    i = this.n;
                    this.containsNull = true;
                } else {
                    int mix = HashCommon.mix(this.strategy.hashCode(readObject)) & this.mask;
                    int i5 = mix;
                    if (kArr[mix] == 0) {
                        i = i5;
                    }
                    do {
                        i2 = (i5 + 1) & this.mask;
                        i5 = i2;
                    } while (kArr[i2] != 0);
                    i = i5;
                }
                kArr[i] = readObject;
                i3 = i4;
            } else {
                return;
            }
        }
    }

    private void checkTable() {
    }
}
