package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.longs.LongCollections;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.LongStream;

/* loaded from: classes4.dex */
public class LongOpenHashSet extends AbstractLongSet implements Serializable, Cloneable, Hash {
    private static final boolean ASSERTS = false;
    private static final long serialVersionUID = 0;
    protected transient boolean containsNull;
    protected final float f;
    protected transient long[] key;
    protected transient int mask;
    protected transient int maxFill;
    protected final transient int minN;
    protected transient int n;
    protected int size;

    public LongOpenHashSet(int expected, float f) {
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
        this.key = new long[this.n + 1];
    }

    public LongOpenHashSet(int expected) {
        this(expected, 0.75f);
    }

    public LongOpenHashSet() {
        this(16, 0.75f);
    }

    public LongOpenHashSet(Collection<? extends Long> c, float f) {
        this(c.size(), f);
        addAll(c);
    }

    public LongOpenHashSet(Collection<? extends Long> c) {
        this(c, 0.75f);
    }

    public LongOpenHashSet(LongCollection c, float f) {
        this(c.size(), f);
        addAll(c);
    }

    public LongOpenHashSet(LongCollection c) {
        this(c, 0.75f);
    }

    public LongOpenHashSet(LongIterator i, float f) {
        this(16, f);
        while (i.hasNext()) {
            add(i.nextLong());
        }
    }

    public LongOpenHashSet(LongIterator i) {
        this(i, 0.75f);
    }

    public LongOpenHashSet(Iterator<?> i, float f) {
        this(LongIterators.asLongIterator(i), f);
    }

    public LongOpenHashSet(Iterator<?> i) {
        this(LongIterators.asLongIterator(i));
    }

    public LongOpenHashSet(long[] a, int offset, int length, float f) {
        this(length < 0 ? 0 : length, f);
        LongArrays.ensureOffsetLength(a, offset, length);
        for (int i = 0; i < length; i++) {
            add(a[offset + i]);
        }
    }

    public LongOpenHashSet(long[] a, int offset, int length) {
        this(a, offset, length, 0.75f);
    }

    public LongOpenHashSet(long[] a, float f) {
        this(a, 0, a.length, f);
    }

    public LongOpenHashSet(long[] a) {
        this(a, 0.75f);
    }

    public static LongOpenHashSet of() {
        return new LongOpenHashSet();
    }

    public static LongOpenHashSet of(long e) {
        LongOpenHashSet result = new LongOpenHashSet(1, 0.75f);
        result.add(e);
        return result;
    }

    public static LongOpenHashSet of(long e0, long e1) {
        LongOpenHashSet result = new LongOpenHashSet(2, 0.75f);
        result.add(e0);
        if (!result.add(e1)) {
            throw new IllegalArgumentException("Duplicate element: " + e1);
        }
        return result;
    }

    public static LongOpenHashSet of(long e0, long e1, long e2) {
        LongOpenHashSet result = new LongOpenHashSet(3, 0.75f);
        result.add(e0);
        if (!result.add(e1)) {
            throw new IllegalArgumentException("Duplicate element: " + e1);
        }
        if (!result.add(e2)) {
            throw new IllegalArgumentException("Duplicate element: " + e2);
        }
        return result;
    }

    public static LongOpenHashSet of(long... a) {
        LongOpenHashSet result = new LongOpenHashSet(a.length, 0.75f);
        for (long element : a) {
            if (!result.add(element)) {
                throw new IllegalArgumentException("Duplicate element " + element);
            }
        }
        return result;
    }

    public static LongOpenHashSet toSet(LongStream stream) {
        return (LongOpenHashSet) stream.collect(new Supplier() { // from class: it.unimi.dsi.fastutil.longs.LongOpenHashSet$$ExternalSyntheticLambda0
            @Override // java.util.function.Supplier
            public final Object get() {
                return new LongOpenHashSet();
            }
        }, new LongOpenHashSet$$ExternalSyntheticLambda1(), new LongOpenHashSet$$ExternalSyntheticLambda2());
    }

    public static LongOpenHashSet toSetWithExpectedSize(LongStream stream, int expectedSize) {
        if (expectedSize <= 16) {
            return toSet(stream);
        }
        return (LongOpenHashSet) stream.collect(new LongCollections.SizeDecreasingSupplier(expectedSize, new IntFunction() { // from class: it.unimi.dsi.fastutil.longs.LongOpenHashSet$$ExternalSyntheticLambda3
            @Override // java.util.function.IntFunction
            public final Object apply(int i) {
                return LongOpenHashSet.lambda$toSetWithExpectedSize$0(i);
            }
        }), new LongOpenHashSet$$ExternalSyntheticLambda1(), new LongOpenHashSet$$ExternalSyntheticLambda2());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ LongOpenHashSet lambda$toSetWithExpectedSize$0(int size) {
        return size <= 16 ? new LongOpenHashSet() : new LongOpenHashSet(size);
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

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
    public boolean addAll(LongCollection c) {
        if (this.f <= 0.5d) {
            ensureCapacity(c.size());
        } else {
            tryCapacity(size() + c.size());
        }
        return super.addAll(c);
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, java.util.AbstractCollection, java.util.Collection
    public boolean addAll(Collection<? extends Long> c) {
        if (this.f <= 0.5d) {
            ensureCapacity(c.size());
        } else {
            tryCapacity(size() + c.size());
        }
        return super.addAll(c);
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
    public boolean add(long k) {
        long curr;
        if (k == 0) {
            if (this.containsNull) {
                return false;
            }
            this.containsNull = true;
        } else {
            long[] key = this.key;
            int mix = ((int) HashCommon.mix(k)) & this.mask;
            int pos = mix;
            long curr2 = key[mix];
            if (curr2 != 0) {
                if (curr2 == k) {
                    return false;
                }
                do {
                    int i = (pos + 1) & this.mask;
                    pos = i;
                    curr = key[i];
                    if (curr != 0) {
                    }
                } while (curr != k);
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
        long curr;
        long[] key = this.key;
        while (true) {
            int last = pos;
            pos = (pos + 1) & this.mask;
            while (true) {
                curr = key[pos];
                if (curr == 0) {
                    key[last] = 0;
                    return;
                }
                int slot = ((int) HashCommon.mix(curr)) & this.mask;
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

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongSet, it.unimi.dsi.fastutil.longs.LongSet
    public boolean remove(long k) {
        long curr;
        if (k == 0) {
            if (this.containsNull) {
                return removeNullEntry();
            }
            return false;
        }
        long[] key = this.key;
        int mix = ((int) HashCommon.mix(k)) & this.mask;
        int pos = mix;
        long curr2 = key[mix];
        if (curr2 == 0) {
            return false;
        }
        if (k == curr2) {
            return removeEntry(pos);
        }
        do {
            int i = (pos + 1) & this.mask;
            pos = i;
            curr = key[i];
            if (curr == 0) {
                return false;
            }
        } while (k != curr);
        return removeEntry(pos);
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongCollection, it.unimi.dsi.fastutil.longs.LongCollection
    public boolean contains(long k) {
        long curr;
        if (k == 0) {
            return this.containsNull;
        }
        long[] key = this.key;
        int mix = ((int) HashCommon.mix(k)) & this.mask;
        int pos = mix;
        long curr2 = key[mix];
        if (curr2 == 0) {
            return false;
        }
        if (k == curr2) {
            return true;
        }
        do {
            int i = (pos + 1) & this.mask;
            pos = i;
            curr = key[i];
            if (curr == 0) {
                return false;
            }
        } while (k != curr);
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public void clear() {
        if (this.size == 0) {
            return;
        }
        this.size = 0;
        this.containsNull = false;
        Arrays.fill(this.key, 0L);
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
    public final class SetIterator implements LongIterator {
        int c;
        int last;
        boolean mustReturnNull;
        int pos;
        LongArrayList wrapped;

        private SetIterator() {
            this.pos = LongOpenHashSet.this.n;
            this.last = -1;
            this.c = LongOpenHashSet.this.size;
            this.mustReturnNull = LongOpenHashSet.this.containsNull;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.c != 0;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongIterator, java.util.PrimitiveIterator.OfLong
        public long nextLong() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            this.c--;
            if (this.mustReturnNull) {
                this.mustReturnNull = false;
                this.last = LongOpenHashSet.this.n;
                return LongOpenHashSet.this.key[LongOpenHashSet.this.n];
            }
            long[] key = LongOpenHashSet.this.key;
            do {
                int i = this.pos - 1;
                this.pos = i;
                if (i < 0) {
                    this.last = Integer.MIN_VALUE;
                    return this.wrapped.getLong((-this.pos) - 1);
                }
            } while (key[this.pos] == 0);
            int i2 = this.pos;
            this.last = i2;
            return key[i2];
        }

        private final void shiftKeys(int pos) {
            long curr;
            long[] key = LongOpenHashSet.this.key;
            while (true) {
                int last = pos;
                pos = (pos + 1) & LongOpenHashSet.this.mask;
                while (true) {
                    curr = key[pos];
                    if (curr == 0) {
                        key[last] = 0;
                        return;
                    }
                    int slot = ((int) HashCommon.mix(curr)) & LongOpenHashSet.this.mask;
                    if (last > pos) {
                        if (last >= slot && slot > pos) {
                            break;
                        }
                        pos = (pos + 1) & LongOpenHashSet.this.mask;
                    } else if (last >= slot || slot > pos) {
                        break;
                    } else {
                        pos = (pos + 1) & LongOpenHashSet.this.mask;
                    }
                }
                if (pos < last) {
                    if (this.wrapped == null) {
                        this.wrapped = new LongArrayList(2);
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
            if (this.last == LongOpenHashSet.this.n) {
                LongOpenHashSet.this.containsNull = false;
                LongOpenHashSet.this.key[LongOpenHashSet.this.n] = 0;
            } else {
                if (this.pos < 0) {
                    LongOpenHashSet.this.remove(this.wrapped.getLong((-this.pos) - 1));
                    this.last = -1;
                    return;
                }
                shiftKeys(this.last);
            }
            LongOpenHashSet longOpenHashSet = LongOpenHashSet.this;
            longOpenHashSet.size--;
            this.last = -1;
        }

        @Override // java.util.PrimitiveIterator
        public void forEachRemaining(java.util.function.LongConsumer action) {
            long[] key = LongOpenHashSet.this.key;
            if (this.mustReturnNull) {
                this.mustReturnNull = false;
                this.last = LongOpenHashSet.this.n;
                action.accept(key[LongOpenHashSet.this.n]);
                this.c--;
            }
            while (this.c != 0) {
                int i = this.pos - 1;
                this.pos = i;
                if (i < 0) {
                    this.last = Integer.MIN_VALUE;
                    action.accept(this.wrapped.getLong((-this.pos) - 1));
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

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongSet, it.unimi.dsi.fastutil.longs.AbstractLongCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
    public LongIterator iterator() {
        return new SetIterator();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class SetSpliterator implements LongSpliterator {
        private static final int POST_SPLIT_CHARACTERISTICS = 257;
        int c;
        boolean hasSplit;
        int max;
        boolean mustReturnNull;
        int pos;

        SetSpliterator() {
            this.pos = 0;
            this.max = LongOpenHashSet.this.n;
            this.c = 0;
            this.mustReturnNull = LongOpenHashSet.this.containsNull;
            this.hasSplit = false;
        }

        SetSpliterator(int pos, int max, boolean mustReturnNull, boolean hasSplit) {
            this.pos = 0;
            this.max = LongOpenHashSet.this.n;
            this.c = 0;
            this.mustReturnNull = LongOpenHashSet.this.containsNull;
            this.hasSplit = false;
            this.pos = pos;
            this.max = max;
            this.mustReturnNull = mustReturnNull;
            this.hasSplit = hasSplit;
        }

        @Override // java.util.Spliterator.OfPrimitive
        public boolean tryAdvance(java.util.function.LongConsumer action) {
            if (this.mustReturnNull) {
                this.mustReturnNull = false;
                this.c++;
                action.accept(LongOpenHashSet.this.key[LongOpenHashSet.this.n]);
                return true;
            }
            long[] key = LongOpenHashSet.this.key;
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
        public void forEachRemaining(java.util.function.LongConsumer action) {
            long[] key = LongOpenHashSet.this.key;
            if (this.mustReturnNull) {
                this.mustReturnNull = false;
                action.accept(key[LongOpenHashSet.this.n]);
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
                return Math.min(LongOpenHashSet.this.size - this.c, ((long) ((LongOpenHashSet.this.realSize() / LongOpenHashSet.this.n) * (this.max - this.pos))) + (this.mustReturnNull ? 1L : 0L));
            }
            return LongOpenHashSet.this.size - this.c;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterator, java.util.Spliterator.OfLong, java.util.Spliterator.OfPrimitive, java.util.Spliterator
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

        @Override // it.unimi.dsi.fastutil.longs.LongSpliterator
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
            long[] key = LongOpenHashSet.this.key;
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

    @Override // java.util.Collection, java.lang.Iterable, it.unimi.dsi.fastutil.longs.LongCollection, it.unimi.dsi.fastutil.longs.LongIterable, it.unimi.dsi.fastutil.longs.LongSet, java.util.Set
    public LongSpliterator spliterator() {
        return new SetSpliterator();
    }

    @Override // it.unimi.dsi.fastutil.longs.LongIterable
    public void forEach(java.util.function.LongConsumer action) {
        if (this.containsNull) {
            action.accept(this.key[this.n]);
        }
        long[] key = this.key;
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
        long[] key = this.key;
        int mask = newN - 1;
        long[] newKey = new long[newN + 1];
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
            int mix = ((int) HashCommon.mix(key[i2])) & mask;
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
    public LongOpenHashSet m253clone() {
        try {
            LongOpenHashSet c = (LongOpenHashSet) super.clone();
            c.key = (long[]) this.key.clone();
            c.containsNull = this.containsNull;
            return c;
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    @Override // it.unimi.dsi.fastutil.longs.AbstractLongSet, java.util.Collection, java.util.Set
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
                h += HashCommon.long2int(this.key[i]);
                i++;
                j = j2;
            } else {
                return h;
            }
        }
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        LongIterator i = iterator();
        s.defaultWriteObject();
        int j = this.size;
        while (true) {
            int j2 = j - 1;
            if (j == 0) {
                return;
            }
            s.writeLong(i.nextLong());
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
        long[] key = new long[this.n + 1];
        this.key = key;
        int pos2 = this.size;
        while (true) {
            int i2 = pos2 - 1;
            if (pos2 != 0) {
                long k = s.readLong();
                if (k == 0) {
                    pos = this.n;
                    this.containsNull = true;
                } else {
                    int mix = ((int) HashCommon.mix(k)) & this.mask;
                    int pos3 = mix;
                    if (key[mix] == 0) {
                        pos = pos3;
                    }
                    do {
                        i = (pos3 + 1) & this.mask;
                        pos3 = i;
                    } while (key[i] != 0);
                    pos = pos3;
                }
                key[pos] = k;
                pos2 = i2;
            } else {
                return;
            }
        }
    }

    private void checkTable() {
    }
}
