package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class TopKSelector<T> {
    private final T[] buffer;
    private int bufferSize;
    private final Comparator<? super T> comparator;
    private final int k;
    private T threshold;

    public static <T extends Comparable<? super T>> TopKSelector<T> least(int k) {
        return least(k, Ordering.natural());
    }

    public static <T extends Comparable<? super T>> TopKSelector<T> greatest(int k) {
        return greatest(k, Ordering.natural());
    }

    public static <T> TopKSelector<T> least(int k, Comparator<? super T> comparator) {
        return new TopKSelector<>(comparator, k);
    }

    public static <T> TopKSelector<T> greatest(int k, Comparator<? super T> comparator) {
        return new TopKSelector<>(Ordering.from(comparator).reverse(), k);
    }

    private TopKSelector(Comparator<? super T> comparator, int i) {
        this.comparator = (Comparator) Preconditions.checkNotNull(comparator, "comparator");
        this.k = i;
        Preconditions.checkArgument(i >= 0, "k must be nonnegative, was %s", i);
        this.buffer = (T[]) new Object[i * 2];
        this.bufferSize = 0;
        this.threshold = null;
    }

    public void offer(@Nullable T t) {
        if (this.k == 0) {
            return;
        }
        if (this.bufferSize == 0) {
            this.buffer[0] = t;
            this.threshold = t;
            this.bufferSize = 1;
            return;
        }
        if (this.bufferSize < this.k) {
            T[] tArr = this.buffer;
            int i = this.bufferSize;
            this.bufferSize = i + 1;
            tArr[i] = t;
            if (this.comparator.compare(t, this.threshold) > 0) {
                this.threshold = t;
                return;
            }
            return;
        }
        if (this.comparator.compare(t, this.threshold) < 0) {
            T[] tArr2 = this.buffer;
            int i2 = this.bufferSize;
            this.bufferSize = i2 + 1;
            tArr2[i2] = t;
            if (this.bufferSize == this.k * 2) {
                trim();
            }
        }
    }

    private void trim() {
        int i = 0;
        int i2 = (this.k * 2) - 1;
        int i3 = 0;
        int i4 = 0;
        int log2 = IntMath.log2(i2 - 0, RoundingMode.CEILING) * 3;
        while (true) {
            if (i >= i2) {
                break;
            }
            int partition = partition(i, i2, ((i + i2) + 1) >>> 1);
            if (partition > this.k) {
                i2 = partition - 1;
            } else {
                if (partition >= this.k) {
                    break;
                }
                i = Math.max(partition, i + 1);
                i3 = partition;
            }
            i4++;
            if (i4 >= log2) {
                Arrays.sort(this.buffer, i, i2, this.comparator);
                break;
            }
        }
        this.bufferSize = this.k;
        this.threshold = this.buffer[i3];
        for (int i5 = i3 + 1; i5 < this.k; i5++) {
            if (this.comparator.compare(this.buffer[i5], this.threshold) > 0) {
                this.threshold = this.buffer[i5];
            }
        }
    }

    private int partition(int i, int i2, int i3) {
        T t = this.buffer[i3];
        this.buffer[i3] = this.buffer[i2];
        int i4 = i;
        for (int i5 = i; i5 < i2; i5++) {
            if (this.comparator.compare(this.buffer[i5], t) < 0) {
                swap(i4, i5);
                i4++;
            }
        }
        this.buffer[i2] = this.buffer[i4];
        this.buffer[i4] = t;
        return i4;
    }

    private void swap(int i, int j) {
        T tmp = this.buffer[i];
        this.buffer[i] = this.buffer[j];
        this.buffer[j] = tmp;
    }

    TopKSelector<T> combine(TopKSelector<T> other) {
        for (int i = 0; i < other.bufferSize; i++) {
            offer(other.buffer[i]);
        }
        return this;
    }

    public void offerAll(Iterable<? extends T> elements) {
        offerAll(elements.iterator());
    }

    public void offerAll(Iterator<? extends T> elements) {
        while (elements.hasNext()) {
            offer(elements.next());
        }
    }

    public List<T> topK() {
        Arrays.sort(this.buffer, 0, this.bufferSize, this.comparator);
        if (this.bufferSize > this.k) {
            Arrays.fill(this.buffer, this.k, this.buffer.length, (Object) null);
            this.bufferSize = this.k;
            this.threshold = this.buffer[this.k - 1];
        }
        return Collections.unmodifiableList(Arrays.asList(Arrays.copyOf(this.buffer, this.bufferSize)));
    }
}
