package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.ints.IntHash;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/* loaded from: classes4.dex */
public class IntOpenCustomHashSet extends AbstractIntSet implements Serializable, Cloneable, Hash {
    private static final boolean ASSERTS = false;
    private static final long serialVersionUID = 0;
    protected transient boolean containsNull;
    protected final float f;
    protected transient int[] key;
    protected transient int mask;
    protected transient int maxFill;
    protected final transient int minN;
    protected transient int n;
    protected int size;
    protected IntHash.Strategy strategy;

    public IntOpenCustomHashSet(int expected, float f, IntHash.Strategy strategy) {
        this.strategy = strategy;
        if (f <= 0.0f || f >= 1.0f) {
            throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than 1");
        }
        if (expected < 0) {
            throw new IllegalArgumentException("The expected number of elements must be nonnegative");
        }
        this.f = f;
        int arraySize = HashCommon.arraySize(expected, f);
        this.n = arraySize;
        this.minN = arraySize;
        this.mask = this.n - 1;
        this.maxFill = HashCommon.maxFill(this.n, f);
        this.key = new int[this.n + 1];
    }

    public IntOpenCustomHashSet(int expected, IntHash.Strategy strategy) {
        this(expected, 0.75f, strategy);
    }

    public IntOpenCustomHashSet(IntHash.Strategy strategy) {
        this(16, 0.75f, strategy);
    }

    public IntOpenCustomHashSet(Collection<? extends Integer> c, float f, IntHash.Strategy strategy) {
        this(c.size(), f, strategy);
        addAll(c);
    }

    public IntOpenCustomHashSet(Collection<? extends Integer> c, IntHash.Strategy strategy) {
        this(c, 0.75f, strategy);
    }

    public IntOpenCustomHashSet(IntCollection c, float f, IntHash.Strategy strategy) {
        this(c.size(), f, strategy);
        addAll(c);
    }

    public IntOpenCustomHashSet(IntCollection c, IntHash.Strategy strategy) {
        this(c, 0.75f, strategy);
    }

    public IntOpenCustomHashSet(IntIterator i, float f, IntHash.Strategy strategy) {
        this(16, f, strategy);
        while (i.hasNext()) {
            add(i.nextInt());
        }
    }

    public IntOpenCustomHashSet(IntIterator i, IntHash.Strategy strategy) {
        this(i, 0.75f, strategy);
    }

    public IntOpenCustomHashSet(Iterator<?> i, float f, IntHash.Strategy strategy) {
        this(IntIterators.asIntIterator(i), f, strategy);
    }

    public IntOpenCustomHashSet(Iterator<?> i, IntHash.Strategy strategy) {
        this(IntIterators.asIntIterator(i), strategy);
    }

    public IntOpenCustomHashSet(int[] a, int offset, int length, float f, IntHash.Strategy strategy) {
        this(length < 0 ? 0 : length, f, strategy);
        IntArrays.ensureOffsetLength(a, offset, length);
        for (int i = 0; i < length; i++) {
            add(a[offset + i]);
        }
    }

    public IntOpenCustomHashSet(int[] a, int offset, int length, IntHash.Strategy strategy) {
        this(a, offset, length, 0.75f, strategy);
    }

    public IntOpenCustomHashSet(int[] a, float f, IntHash.Strategy strategy) {
        this(a, 0, a.length, f, strategy);
    }

    public IntOpenCustomHashSet(int[] a, IntHash.Strategy strategy) {
        this(a, 0.75f, strategy);
    }

    public IntHash.Strategy strategy() {
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

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
    public boolean addAll(IntCollection c) {
        if (this.f <= 0.5d) {
            ensureCapacity(c.size());
        } else {
            tryCapacity(size() + c.size());
        }
        return super.addAll(c);
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, java.util.AbstractCollection, java.util.Collection
    public boolean addAll(Collection<? extends Integer> c) {
        if (this.f <= 0.5d) {
            ensureCapacity(c.size());
        } else {
            tryCapacity(size() + c.size());
        }
        return super.addAll(c);
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
    public boolean add(int k) {
        int curr;
        if (this.strategy.equals(k, 0)) {
            if (this.containsNull) {
                return false;
            }
            this.containsNull = true;
            this.key[this.n] = k;
        } else {
            int[] key = this.key;
            int mix = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
            int pos = mix;
            int curr2 = key[mix];
            if (curr2 != 0) {
                if (this.strategy.equals(curr2, k)) {
                    return false;
                }
                do {
                    int i = (pos + 1) & this.mask;
                    pos = i;
                    curr = key[i];
                    if (curr != 0) {
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

    protected final void shiftKeys(int pos) {
        int curr;
        int[] key = this.key;
        while (true) {
            int last = pos;
            pos = (pos + 1) & this.mask;
            while (true) {
                curr = key[pos];
                if (curr == 0) {
                    key[last] = 0;
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
        this.key[this.n] = 0;
        this.size--;
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            rehash(this.n / 2);
        }
        return true;
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntSet, it.unimi.dsi.fastutil.ints.IntSet
    public boolean remove(int k) {
        int curr;
        if (this.strategy.equals(k, 0)) {
            if (this.containsNull) {
                return removeNullEntry();
            }
            return false;
        }
        int[] key = this.key;
        int mix = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
        int pos = mix;
        int curr2 = key[mix];
        if (curr2 == 0) {
            return false;
        }
        if (this.strategy.equals(k, curr2)) {
            return removeEntry(pos);
        }
        do {
            int i = (pos + 1) & this.mask;
            pos = i;
            curr = key[i];
            if (curr == 0) {
                return false;
            }
        } while (!this.strategy.equals(k, curr));
        return removeEntry(pos);
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntCollection, it.unimi.dsi.fastutil.ints.IntCollection
    public boolean contains(int k) {
        int curr;
        if (this.strategy.equals(k, 0)) {
            return this.containsNull;
        }
        int[] key = this.key;
        int mix = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
        int pos = mix;
        int curr2 = key[mix];
        if (curr2 == 0) {
            return false;
        }
        if (this.strategy.equals(k, curr2)) {
            return true;
        }
        do {
            int i = (pos + 1) & this.mask;
            pos = i;
            curr = key[i];
            if (curr == 0) {
                return false;
            }
        } while (!this.strategy.equals(k, curr));
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public void clear() {
        if (this.size == 0) {
            return;
        }
        this.size = 0;
        this.containsNull = false;
        Arrays.fill(this.key, 0);
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
    public final class SetIterator implements IntIterator {
        int c;
        int last;
        boolean mustReturnNull;
        int pos;
        IntArrayList wrapped;

        private SetIterator() {
            this.pos = IntOpenCustomHashSet.this.n;
            this.last = -1;
            this.c = IntOpenCustomHashSet.this.size;
            this.mustReturnNull = IntOpenCustomHashSet.this.containsNull;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.c != 0;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntIterator, java.util.PrimitiveIterator.OfInt
        public int nextInt() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            this.c--;
            if (this.mustReturnNull) {
                this.mustReturnNull = false;
                this.last = IntOpenCustomHashSet.this.n;
                return IntOpenCustomHashSet.this.key[IntOpenCustomHashSet.this.n];
            }
            int[] key = IntOpenCustomHashSet.this.key;
            do {
                int i = this.pos - 1;
                this.pos = i;
                if (i < 0) {
                    this.last = Integer.MIN_VALUE;
                    return this.wrapped.getInt((-this.pos) - 1);
                }
            } while (key[this.pos] == 0);
            int i2 = this.pos;
            this.last = i2;
            return key[i2];
        }

        private final void shiftKeys(int pos) {
            int curr;
            int[] key = IntOpenCustomHashSet.this.key;
            while (true) {
                int last = pos;
                pos = (pos + 1) & IntOpenCustomHashSet.this.mask;
                while (true) {
                    curr = key[pos];
                    if (curr == 0) {
                        key[last] = 0;
                        return;
                    }
                    int slot = HashCommon.mix(IntOpenCustomHashSet.this.strategy.hashCode(curr)) & IntOpenCustomHashSet.this.mask;
                    if (last > pos) {
                        if (last >= slot && slot > pos) {
                            break;
                        }
                        pos = (pos + 1) & IntOpenCustomHashSet.this.mask;
                    } else if (last >= slot || slot > pos) {
                        break;
                    } else {
                        pos = (pos + 1) & IntOpenCustomHashSet.this.mask;
                    }
                }
                if (pos < last) {
                    if (this.wrapped == null) {
                        this.wrapped = new IntArrayList(2);
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
            if (this.last == IntOpenCustomHashSet.this.n) {
                IntOpenCustomHashSet.this.containsNull = false;
                IntOpenCustomHashSet.this.key[IntOpenCustomHashSet.this.n] = 0;
            } else {
                if (this.pos < 0) {
                    IntOpenCustomHashSet.this.remove(this.wrapped.getInt((-this.pos) - 1));
                    this.last = -1;
                    return;
                }
                shiftKeys(this.last);
            }
            IntOpenCustomHashSet intOpenCustomHashSet = IntOpenCustomHashSet.this;
            intOpenCustomHashSet.size--;
            this.last = -1;
        }

        @Override // java.util.PrimitiveIterator
        public void forEachRemaining(java.util.function.IntConsumer action) {
            int[] key = IntOpenCustomHashSet.this.key;
            if (this.mustReturnNull) {
                this.mustReturnNull = false;
                this.last = IntOpenCustomHashSet.this.n;
                action.accept(key[IntOpenCustomHashSet.this.n]);
                this.c--;
            }
            while (this.c != 0) {
                int i = this.pos - 1;
                this.pos = i;
                if (i < 0) {
                    this.last = Integer.MIN_VALUE;
                    action.accept(this.wrapped.getInt((-this.pos) - 1));
                    this.c--;
                } else if (key[this.pos] != 0) {
                    int i2 = this.pos;
                    this.last = i2;
                    action.accept(key[i2]);
                    this.c--;
                }
            }
        }
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntSet, it.unimi.dsi.fastutil.ints.AbstractIntCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
    public IntIterator iterator() {
        return new SetIterator();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class SetSpliterator implements IntSpliterator {
        private static final int POST_SPLIT_CHARACTERISTICS = 257;
        int c;
        boolean hasSplit;
        int max;
        boolean mustReturnNull;
        int pos;

        SetSpliterator() {
            this.pos = 0;
            this.max = IntOpenCustomHashSet.this.n;
            this.c = 0;
            this.mustReturnNull = IntOpenCustomHashSet.this.containsNull;
            this.hasSplit = false;
        }

        SetSpliterator(int pos, int max, boolean mustReturnNull, boolean hasSplit) {
            this.pos = 0;
            this.max = IntOpenCustomHashSet.this.n;
            this.c = 0;
            this.mustReturnNull = IntOpenCustomHashSet.this.containsNull;
            this.hasSplit = false;
            this.pos = pos;
            this.max = max;
            this.mustReturnNull = mustReturnNull;
            this.hasSplit = hasSplit;
        }

        @Override // java.util.Spliterator.OfPrimitive
        public boolean tryAdvance(java.util.function.IntConsumer action) {
            if (this.mustReturnNull) {
                this.mustReturnNull = false;
                this.c++;
                action.accept(IntOpenCustomHashSet.this.key[IntOpenCustomHashSet.this.n]);
                return true;
            }
            int[] key = IntOpenCustomHashSet.this.key;
            while (this.pos < this.max) {
                if (key[this.pos] != 0) {
                    this.c++;
                    int i = this.pos;
                    this.pos = i + 1;
                    action.accept(key[i]);
                    return true;
                }
                this.pos++;
            }
            return false;
        }

        @Override // java.util.Spliterator.OfPrimitive
        public void forEachRemaining(java.util.function.IntConsumer action) {
            int[] key = IntOpenCustomHashSet.this.key;
            if (this.mustReturnNull) {
                this.mustReturnNull = false;
                action.accept(key[IntOpenCustomHashSet.this.n]);
                this.c++;
            }
            while (this.pos < this.max) {
                if (key[this.pos] != 0) {
                    action.accept(key[this.pos]);
                    this.c++;
                }
                this.pos++;
            }
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return this.hasSplit ? 257 : 321;
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            if (this.hasSplit) {
                return Math.min(IntOpenCustomHashSet.this.size - this.c, ((long) ((IntOpenCustomHashSet.this.realSize() / IntOpenCustomHashSet.this.n) * (this.max - this.pos))) + (this.mustReturnNull ? 1L : 0L));
            }
            return IntOpenCustomHashSet.this.size - this.c;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSpliterator, java.util.Spliterator.OfInt, java.util.Spliterator.OfPrimitive, java.util.Spliterator
        public SetSpliterator trySplit() {
            int retLen;
            if (this.pos >= this.max - 1 || (retLen = (this.max - this.pos) >> 1) <= 1) {
                return null;
            }
            int myNewPos = this.pos + retLen;
            int retPos = this.pos;
            SetSpliterator split = new SetSpliterator(retPos, myNewPos, this.mustReturnNull, true);
            this.pos = myNewPos;
            this.mustReturnNull = false;
            this.hasSplit = true;
            return split;
        }

        @Override // it.unimi.dsi.fastutil.ints.IntSpliterator
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
            int[] key = IntOpenCustomHashSet.this.key;
            while (this.pos < this.max && n > 0) {
                int i = this.pos;
                this.pos = i + 1;
                if (key[i] != 0) {
                    skipped++;
                    n--;
                }
            }
            return skipped;
        }
    }

    @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.ints.IntCollection, it.unimi.dsi.fastutil.ints.IntIterable, it.unimi.dsi.fastutil.ints.IntSet, java.util.Set
    public IntSpliterator spliterator() {
        return new SetSpliterator();
    }

    @Override // it.unimi.dsi.fastutil.ints.IntIterable
    public void forEach(java.util.function.IntConsumer action) {
        if (this.containsNull) {
            action.accept(this.key[this.n]);
        }
        int[] key = this.key;
        int pos = this.n;
        while (true) {
            int pos2 = pos - 1;
            if (pos == 0) {
                return;
            }
            if (key[pos2] != 0) {
                action.accept(key[pos2]);
            }
            pos = pos2;
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

    protected void rehash(int newN) {
        int i;
        int[] key = this.key;
        int mask = newN - 1;
        int[] newKey = new int[newN + 1];
        int i2 = this.n;
        int j = realSize();
        while (true) {
            int j2 = j - 1;
            if (j == 0) {
                this.n = newN;
                this.mask = mask;
                this.maxFill = HashCommon.maxFill(this.n, this.f);
                this.key = newKey;
                return;
            }
            do {
                i2--;
            } while (key[i2] == 0);
            int mix = HashCommon.mix(this.strategy.hashCode(key[i2])) & mask;
            int pos = mix;
            if (newKey[mix] == 0) {
                newKey[pos] = key[i2];
                j = j2;
            }
            do {
                i = (pos + 1) & mask;
                pos = i;
            } while (newKey[i] != 0);
            newKey[pos] = key[i2];
            j = j2;
        }
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public IntOpenCustomHashSet m233clone() {
        try {
            IntOpenCustomHashSet c = (IntOpenCustomHashSet) super.clone();
            c.key = (int[]) this.key.clone();
            c.containsNull = this.containsNull;
            c.strategy = this.strategy;
            return c;
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    @Override // it.unimi.dsi.fastutil.ints.AbstractIntSet, java.util.Collection, java.util.Set
    public int hashCode() {
        int h = 0;
        int j = realSize();
        int i = 0;
        while (true) {
            int j2 = j - 1;
            if (j != 0) {
                while (this.key[i] == 0) {
                    i++;
                }
                h += this.strategy.hashCode(this.key[i]);
                i++;
                j = j2;
            } else {
                return h;
            }
        }
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        IntIterator i = iterator();
        s.defaultWriteObject();
        int j = this.size;
        while (true) {
            int j2 = j - 1;
            if (j == 0) {
                return;
            }
            s.writeInt(i.nextInt());
            j = j2;
        }
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        int pos;
        int i;
        s.defaultReadObject();
        this.n = HashCommon.arraySize(this.size, this.f);
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.mask = this.n - 1;
        int[] key = new int[this.n + 1];
        this.key = key;
        int k = this.size;
        while (true) {
            int i2 = k - 1;
            if (k != 0) {
                int k2 = s.readInt();
                if (this.strategy.equals(k2, 0)) {
                    pos = this.n;
                    this.containsNull = true;
                } else {
                    int mix = HashCommon.mix(this.strategy.hashCode(k2)) & this.mask;
                    int pos2 = mix;
                    if (key[mix] == 0) {
                        pos = pos2;
                    }
                    do {
                        i = (pos2 + 1) & this.mask;
                        pos2 = i;
                    } while (key[i] != 0);
                    pos = pos2;
                }
                key[pos] = k2;
                k = i2;
            } else {
                return;
            }
        }
    }

    private void checkTable() {
    }
}
