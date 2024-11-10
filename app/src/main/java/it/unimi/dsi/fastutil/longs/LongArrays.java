package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.Arrays;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.ints.IntArrays;
import java.io.Serializable;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes4.dex */
public final class LongArrays {
    private static final int DIGITS_PER_ELEMENT = 8;
    private static final int DIGIT_BITS = 8;
    private static final int DIGIT_MASK = 255;
    private static final int MERGESORT_NO_REC = 16;
    private static final int PARALLEL_QUICKSORT_NO_FORK = 8192;
    private static final int PARALLEL_RADIXSORT_NO_FORK = 1024;
    private static final int QUICKSORT_MEDIAN_OF_9 = 128;
    private static final int QUICKSORT_NO_REC = 16;
    private static final int RADIXSORT_NO_REC = 1024;
    private static final int RADIXSORT_NO_REC_SMALL = 64;
    static final int RADIX_SORT_MIN_THRESHOLD = 4000;
    public static final long[] EMPTY_ARRAY = new long[0];
    public static final long[] DEFAULT_EMPTY_ARRAY = new long[0];
    protected static final Segment POISON_PILL = new Segment(-1, -1, -1);
    public static final Hash.Strategy<long[]> HASH_STRATEGY = new ArrayHashStrategy();

    private LongArrays() {
    }

    public static long[] forceCapacity(long[] array, int length, int preserve) {
        long[] t = new long[length];
        System.arraycopy(array, 0, t, 0, preserve);
        return t;
    }

    public static long[] ensureCapacity(long[] array, int length) {
        return ensureCapacity(array, length, array.length);
    }

    public static long[] ensureCapacity(long[] array, int length, int preserve) {
        return length > array.length ? forceCapacity(array, length, preserve) : array;
    }

    public static long[] grow(long[] array, int length) {
        return grow(array, length, array.length);
    }

    public static long[] grow(long[] array, int length, int preserve) {
        if (length > array.length) {
            int newLength = (int) Math.max(Math.min(array.length + (array.length >> 1), 2147483639L), length);
            long[] t = new long[newLength];
            System.arraycopy(array, 0, t, 0, preserve);
            return t;
        }
        return array;
    }

    public static long[] trim(long[] array, int length) {
        if (length >= array.length) {
            return array;
        }
        long[] t = length == 0 ? EMPTY_ARRAY : new long[length];
        System.arraycopy(array, 0, t, 0, length);
        return t;
    }

    public static long[] setLength(long[] array, int length) {
        return length == array.length ? array : length < array.length ? trim(array, length) : ensureCapacity(array, length);
    }

    public static long[] copy(long[] array, int offset, int length) {
        ensureOffsetLength(array, offset, length);
        long[] a = length == 0 ? EMPTY_ARRAY : new long[length];
        System.arraycopy(array, offset, a, 0, length);
        return a;
    }

    public static long[] copy(long[] array) {
        return (long[]) array.clone();
    }

    @Deprecated
    public static void fill(long[] array, long value) {
        int i = array.length;
        while (true) {
            int i2 = i - 1;
            if (i == 0) {
                return;
            }
            array[i2] = value;
            i = i2;
        }
    }

    @Deprecated
    public static void fill(long[] array, int from, int to, long value) {
        ensureFromTo(array, from, to);
        if (from != 0) {
            for (int i = from; i < to; i++) {
                array[i] = value;
            }
            return;
        }
        while (true) {
            int to2 = to - 1;
            if (to == 0) {
                return;
            }
            array[to2] = value;
            to = to2;
        }
    }

    @Deprecated
    public static boolean equals(long[] a1, long[] a2) {
        int i = a1.length;
        if (i != a2.length) {
            return false;
        }
        while (true) {
            int i2 = i - 1;
            if (i == 0) {
                return true;
            }
            if (a1[i2] != a2[i2]) {
                return false;
            }
            i = i2;
        }
    }

    public static void ensureFromTo(long[] a, int from, int to) {
        Arrays.ensureFromTo(a.length, from, to);
    }

    public static void ensureOffsetLength(long[] a, int offset, int length) {
        Arrays.ensureOffsetLength(a.length, offset, length);
    }

    public static void ensureSameLength(long[] a, long[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException("Array size mismatch: " + a.length + " != " + b.length);
        }
    }

    private static ForkJoinPool getPool() {
        ForkJoinPool current = ForkJoinTask.getPool();
        return current == null ? ForkJoinPool.commonPool() : current;
    }

    public static void swap(long[] x, int a, int b) {
        long t = x[a];
        x[a] = x[b];
        x[b] = t;
    }

    public static void swap(long[] x, int a, int b, int n) {
        int i = 0;
        while (i < n) {
            swap(x, a, b);
            i++;
            a++;
            b++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0024, code lost:            if (r1 > 0) goto L11;     */
    /* JADX WARN: Code restructure failed: missing block: B:4:0x001d, code lost:            if (r1 < 0) goto L11;     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x0029, code lost:            return r7;     */
    /* JADX WARN: Code restructure failed: missing block: B:9:?, code lost:            return r9;     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int med3(long[] r6, int r7, int r8, int r9, it.unimi.dsi.fastutil.longs.LongComparator r10) {
        /*
            r0 = r6[r7]
            r2 = r6[r8]
            int r0 = r10.compare(r0, r2)
            r1 = r6[r7]
            r3 = r6[r9]
            int r1 = r10.compare(r1, r3)
            r2 = r6[r8]
            r4 = r6[r9]
            int r2 = r10.compare(r2, r4)
            if (r0 >= 0) goto L20
            if (r2 >= 0) goto L1d
            goto L22
        L1d:
            if (r1 >= 0) goto L28
            goto L26
        L20:
            if (r2 <= 0) goto L24
        L22:
            r3 = r8
            goto L29
        L24:
            if (r1 <= 0) goto L28
        L26:
            r3 = r9
            goto L29
        L28:
            r3 = r7
        L29:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.longs.LongArrays.med3(long[], int, int, int, it.unimi.dsi.fastutil.longs.LongComparator):int");
    }

    private static void selectionSort(long[] a, int from, int to, LongComparator comp) {
        for (int i = from; i < to - 1; i++) {
            int m = i;
            for (int j = i + 1; j < to; j++) {
                if (comp.compare(a[j], a[m]) < 0) {
                    m = j;
                }
            }
            if (m != i) {
                long u = a[i];
                a[i] = a[m];
                a[m] = u;
            }
        }
    }

    private static void insertionSort(long[] a, int from, int to, LongComparator comp) {
        int i = from;
        while (true) {
            i++;
            if (i < to) {
                long t = a[i];
                int j = i;
                long u = a[j - 1];
                while (true) {
                    if (comp.compare(t, u) < 0) {
                        a[j] = u;
                        if (from != j - 1) {
                            j--;
                            u = a[j - 1];
                        } else {
                            j--;
                            break;
                        }
                    }
                }
                a[j] = t;
            } else {
                return;
            }
        }
    }

    public static void quickSort(long[] x, int from, int to, LongComparator comp) {
        int b = to - from;
        if (b < 16) {
            selectionSort(x, from, to, comp);
            return;
        }
        int m = (b / 2) + from;
        int l = from;
        int n = to - 1;
        if (b > 128) {
            int s = b / 8;
            l = med3(x, l, l + s, (s * 2) + l, comp);
            m = med3(x, m - s, m, m + s, comp);
            n = med3(x, n - (s * 2), n - s, n, comp);
        }
        int c = med3(x, l, m, n, comp);
        long v = x[c];
        int a = from;
        int b2 = a;
        int c2 = to - 1;
        int d = c2;
        while (true) {
            if (b2 <= c2) {
                int comparison = comp.compare(x[b2], v);
                if (comparison <= 0) {
                    if (comparison == 0) {
                        swap(x, a, b2);
                        a++;
                    }
                    b2++;
                }
            }
            while (c2 >= b2) {
                int comparison2 = comp.compare(x[c2], v);
                if (comparison2 < 0) {
                    break;
                }
                if (comparison2 == 0) {
                    swap(x, c2, d);
                    d--;
                }
                c2--;
            }
            if (b2 > c2) {
                break;
            }
            int len = b;
            int len2 = b2 + 1;
            swap(x, b2, c2);
            b2 = len2;
            c2--;
            b = len;
            c = c;
        }
        int s2 = Math.min(a - from, b2 - a);
        swap(x, from, b2 - s2, s2);
        int s3 = Math.min(d - c2, (to - d) - 1);
        swap(x, b2, to - s3, s3);
        int s4 = b2 - a;
        if (s4 > 1) {
            quickSort(x, from, from + s4, comp);
        }
        int s5 = d - c2;
        if (s5 > 1) {
            quickSort(x, to - s5, to, comp);
        }
    }

    public static void quickSort(long[] x, LongComparator comp) {
        quickSort(x, 0, x.length, comp);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public static class ForkJoinQuickSortComp extends RecursiveAction {
        private static final long serialVersionUID = 1;
        private final LongComparator comp;
        private final int from;
        private final int to;
        private final long[] x;

        public ForkJoinQuickSortComp(long[] x, int from, int to, LongComparator comp) {
            this.from = from;
            this.to = to;
            this.x = x;
            this.comp = comp;
        }

        @Override // java.util.concurrent.RecursiveAction
        protected void compute() {
            int s;
            int len;
            int m;
            long[] x = this.x;
            int len2 = this.to - this.from;
            if (len2 < 8192) {
                LongArrays.quickSort(x, this.from, this.to, this.comp);
                return;
            }
            int m2 = this.from + (len2 / 2);
            int l = this.from;
            int n = this.to - 1;
            int comparison = len2 / 8;
            int l2 = LongArrays.med3(x, l, l + comparison, (comparison * 2) + l, this.comp);
            int m3 = LongArrays.med3(x, m2 - comparison, m2, m2 + comparison, this.comp);
            int n2 = LongArrays.med3(x, n - (comparison * 2), n - comparison, n, this.comp);
            int comparison2 = LongArrays.med3(x, l2, m3, n2, this.comp);
            long v = x[comparison2];
            int a = this.from;
            int b = a;
            int c = this.to - 1;
            int d = c;
            while (true) {
                if (b <= c) {
                    s = comparison;
                    int comparison3 = this.comp.compare(x[b], v);
                    if (comparison3 <= 0) {
                        if (comparison3 == 0) {
                            LongArrays.swap(x, a, b);
                            a++;
                        }
                        b++;
                        comparison = s;
                    }
                } else {
                    s = comparison;
                }
                while (true) {
                    if (c >= b) {
                        len = len2;
                        m = comparison2;
                        int comparison4 = this.comp.compare(x[c], v);
                        if (comparison4 < 0) {
                            break;
                        }
                        if (comparison4 == 0) {
                            LongArrays.swap(x, c, d);
                            d--;
                        }
                        c--;
                        len2 = len;
                        comparison2 = m;
                    } else {
                        len = len2;
                        m = comparison2;
                        break;
                    }
                }
                if (b > c) {
                    break;
                }
                int len3 = len;
                LongArrays.swap(x, b, c);
                b++;
                c--;
                comparison2 = m;
                comparison = s;
                n2 = n2;
                len2 = len3;
            }
            int s2 = Math.min(a - this.from, b - a);
            LongArrays.swap(x, this.from, b - s2, s2);
            int s3 = Math.min(d - c, (this.to - d) - 1);
            LongArrays.swap(x, b, this.to - s3, s3);
            int s4 = b - a;
            int t = d - c;
            if (s4 > 1 && t > 1) {
                invokeAll(new ForkJoinQuickSortComp(x, this.from, this.from + s4, this.comp), new ForkJoinQuickSortComp(x, this.to - t, this.to, this.comp));
            } else if (s4 > 1) {
                invokeAll(new ForkJoinTask[]{new ForkJoinQuickSortComp(x, this.from, this.from + s4, this.comp)});
            } else {
                invokeAll(new ForkJoinTask[]{new ForkJoinQuickSortComp(x, this.to - t, this.to, this.comp)});
            }
        }
    }

    public static void parallelQuickSort(long[] x, int from, int to, LongComparator comp) {
        ForkJoinPool pool = getPool();
        if (to - from < 8192 || pool.getParallelism() == 1) {
            quickSort(x, from, to, comp);
        } else {
            pool.invoke(new ForkJoinQuickSortComp(x, from, to, comp));
        }
    }

    public static void parallelQuickSort(long[] x, LongComparator comp) {
        parallelQuickSort(x, 0, x.length, comp);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0024, code lost:            if (r1 > 0) goto L11;     */
    /* JADX WARN: Code restructure failed: missing block: B:4:0x001d, code lost:            if (r1 < 0) goto L11;     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x0029, code lost:            return r7;     */
    /* JADX WARN: Code restructure failed: missing block: B:9:?, code lost:            return r9;     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int med3(long[] r6, int r7, int r8, int r9) {
        /*
            r0 = r6[r7]
            r2 = r6[r8]
            int r0 = java.lang.Long.compare(r0, r2)
            r1 = r6[r7]
            r3 = r6[r9]
            int r1 = java.lang.Long.compare(r1, r3)
            r2 = r6[r8]
            r4 = r6[r9]
            int r2 = java.lang.Long.compare(r2, r4)
            if (r0 >= 0) goto L20
            if (r2 >= 0) goto L1d
            goto L22
        L1d:
            if (r1 >= 0) goto L28
            goto L26
        L20:
            if (r2 <= 0) goto L24
        L22:
            r3 = r8
            goto L29
        L24:
            if (r1 <= 0) goto L28
        L26:
            r3 = r9
            goto L29
        L28:
            r3 = r7
        L29:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.longs.LongArrays.med3(long[], int, int, int):int");
    }

    private static void selectionSort(long[] a, int from, int to) {
        for (int i = from; i < to - 1; i++) {
            int m = i;
            for (int j = i + 1; j < to; j++) {
                if (a[j] < a[m]) {
                    m = j;
                }
            }
            if (m != i) {
                long u = a[i];
                a[i] = a[m];
                a[m] = u;
            }
        }
    }

    private static void insertionSort(long[] a, int from, int to) {
        int i = from;
        while (true) {
            i++;
            if (i < to) {
                long t = a[i];
                int j = i;
                long u = a[j - 1];
                while (true) {
                    if (t < u) {
                        a[j] = u;
                        if (from != j - 1) {
                            j--;
                            u = a[j - 1];
                        } else {
                            j--;
                            break;
                        }
                    }
                }
                a[j] = t;
            } else {
                return;
            }
        }
    }

    public static void quickSort(long[] x, int from, int to) {
        int b = to - from;
        if (b < 16) {
            selectionSort(x, from, to);
            return;
        }
        int m = (b / 2) + from;
        int l = from;
        int n = to - 1;
        if (b > 128) {
            int s = b / 8;
            l = med3(x, l, l + s, (s * 2) + l);
            m = med3(x, m - s, m, m + s);
            n = med3(x, n - (s * 2), n - s, n);
        }
        long v = x[med3(x, l, m, n)];
        int a = from;
        int b2 = a;
        int c = to - 1;
        int d = c;
        while (true) {
            if (b2 <= c) {
                int comparison = Long.compare(x[b2], v);
                if (comparison <= 0) {
                    if (comparison == 0) {
                        swap(x, a, b2);
                        a++;
                    }
                    b2++;
                }
            }
            while (c >= b2) {
                int comparison2 = Long.compare(x[c], v);
                if (comparison2 < 0) {
                    break;
                }
                if (comparison2 == 0) {
                    swap(x, c, d);
                    d--;
                }
                c--;
            }
            if (b2 > c) {
                break;
            }
            int len = b;
            int len2 = b2 + 1;
            swap(x, b2, c);
            b2 = len2;
            c--;
            b = len;
        }
        int s2 = Math.min(a - from, b2 - a);
        swap(x, from, b2 - s2, s2);
        int s3 = Math.min(d - c, (to - d) - 1);
        swap(x, b2, to - s3, s3);
        int s4 = b2 - a;
        if (s4 > 1) {
            quickSort(x, from, from + s4);
        }
        int s5 = d - c;
        if (s5 > 1) {
            quickSort(x, to - s5, to);
        }
    }

    public static void quickSort(long[] x) {
        quickSort(x, 0, x.length);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public static class ForkJoinQuickSort extends RecursiveAction {
        private static final long serialVersionUID = 1;
        private final int from;
        private final int to;
        private final long[] x;

        public ForkJoinQuickSort(long[] x, int from, int to) {
            this.from = from;
            this.to = to;
            this.x = x;
        }

        @Override // java.util.concurrent.RecursiveAction
        protected void compute() {
            long[] x = this.x;
            int b = this.to - this.from;
            if (b < 8192) {
                LongArrays.quickSort(x, this.from, this.to);
                return;
            }
            int m = this.from + (b / 2);
            int l = this.from;
            int n = this.to - 1;
            int s = b / 8;
            int c = LongArrays.med3(x, LongArrays.med3(x, l, l + s, (s * 2) + l), LongArrays.med3(x, m - s, m, m + s), LongArrays.med3(x, n - (s * 2), n - s, n));
            long v = x[c];
            int a = this.from;
            int b2 = a;
            int c2 = this.to - 1;
            int d = c2;
            while (true) {
                if (b2 <= c2) {
                    int comparison = Long.compare(x[b2], v);
                    if (comparison <= 0) {
                        if (comparison == 0) {
                            LongArrays.swap(x, a, b2);
                            a++;
                        }
                        b2++;
                    }
                }
                while (c2 >= b2) {
                    int comparison2 = Long.compare(x[c2], v);
                    if (comparison2 < 0) {
                        break;
                    }
                    if (comparison2 == 0) {
                        LongArrays.swap(x, c2, d);
                        d--;
                    }
                    c2--;
                }
                if (b2 > c2) {
                    break;
                }
                int len = b;
                int len2 = b2 + 1;
                LongArrays.swap(x, b2, c2);
                b2 = len2;
                c2--;
                b = len;
                c = c;
            }
            int s2 = Math.min(a - this.from, b2 - a);
            LongArrays.swap(x, this.from, b2 - s2, s2);
            int s3 = Math.min(d - c2, (this.to - d) - 1);
            LongArrays.swap(x, b2, this.to - s3, s3);
            int s4 = b2 - a;
            int t = d - c2;
            if (s4 > 1 && t > 1) {
                int i = this.from;
                int len3 = this.from;
                invokeAll(new ForkJoinQuickSort(x, i, len3 + s4), new ForkJoinQuickSort(x, this.to - t, this.to));
            } else if (s4 > 1) {
                invokeAll(new ForkJoinTask[]{new ForkJoinQuickSort(x, this.from, this.from + s4)});
            } else {
                invokeAll(new ForkJoinTask[]{new ForkJoinQuickSort(x, this.to - t, this.to)});
            }
        }
    }

    public static void parallelQuickSort(long[] x, int from, int to) {
        ForkJoinPool pool = getPool();
        if (to - from < 8192 || pool.getParallelism() == 1) {
            quickSort(x, from, to);
        } else {
            pool.invoke(new ForkJoinQuickSort(x, from, to));
        }
    }

    public static void parallelQuickSort(long[] x) {
        parallelQuickSort(x, 0, x.length);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0024, code lost:            if (r7 > 0) goto L11;     */
    /* JADX WARN: Code restructure failed: missing block: B:4:0x001d, code lost:            if (r7 < 0) goto L11;     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x0029, code lost:            return r12;     */
    /* JADX WARN: Code restructure failed: missing block: B:9:?, code lost:            return r14;     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int med3Indirect(int[] r10, long[] r11, int r12, int r13, int r14) {
        /*
            r0 = r10[r12]
            r0 = r11[r0]
            r2 = r10[r13]
            r2 = r11[r2]
            r4 = r10[r14]
            r4 = r11[r4]
            int r6 = java.lang.Long.compare(r0, r2)
            int r7 = java.lang.Long.compare(r0, r4)
            int r8 = java.lang.Long.compare(r2, r4)
            if (r6 >= 0) goto L20
            if (r8 >= 0) goto L1d
            goto L22
        L1d:
            if (r7 >= 0) goto L28
            goto L26
        L20:
            if (r8 <= 0) goto L24
        L22:
            r9 = r13
            goto L29
        L24:
            if (r7 <= 0) goto L28
        L26:
            r9 = r14
            goto L29
        L28:
            r9 = r12
        L29:
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.longs.LongArrays.med3Indirect(int[], long[], int, int, int):int");
    }

    private static void insertionSortIndirect(int[] perm, long[] a, int from, int to) {
        int i = from;
        while (true) {
            i++;
            if (i < to) {
                int t = perm[i];
                int j = i;
                int u = perm[j - 1];
                while (true) {
                    if (a[t] < a[u]) {
                        perm[j] = u;
                        if (from != j - 1) {
                            j--;
                            u = perm[j - 1];
                        } else {
                            j--;
                            break;
                        }
                    }
                }
                perm[j] = t;
            } else {
                return;
            }
        }
    }

    public static void quickSortIndirect(int[] perm, long[] x, int from, int to) {
        int b = to - from;
        if (b < 16) {
            insertionSortIndirect(perm, x, from, to);
            return;
        }
        int m = (b / 2) + from;
        int l = from;
        int n = to - 1;
        if (b > 128) {
            int s = b / 8;
            l = med3Indirect(perm, x, l, l + s, (s * 2) + l);
            m = med3Indirect(perm, x, m - s, m, m + s);
            n = med3Indirect(perm, x, n - (s * 2), n - s, n);
        }
        int c = med3Indirect(perm, x, l, m, n);
        long v = x[perm[c]];
        int a = from;
        int b2 = a;
        int c2 = to - 1;
        int d = c2;
        while (true) {
            if (b2 <= c2) {
                int comparison = Long.compare(x[perm[b2]], v);
                if (comparison <= 0) {
                    if (comparison == 0) {
                        IntArrays.swap(perm, a, b2);
                        a++;
                    }
                    b2++;
                }
            }
            while (c2 >= b2) {
                int comparison2 = Long.compare(x[perm[c2]], v);
                if (comparison2 < 0) {
                    break;
                }
                if (comparison2 == 0) {
                    IntArrays.swap(perm, c2, d);
                    d--;
                }
                c2--;
            }
            if (b2 > c2) {
                break;
            }
            int len = b;
            int len2 = b2 + 1;
            IntArrays.swap(perm, b2, c2);
            b2 = len2;
            c2--;
            b = len;
            c = c;
        }
        int s2 = Math.min(a - from, b2 - a);
        IntArrays.swap(perm, from, b2 - s2, s2);
        int s3 = Math.min(d - c2, (to - d) - 1);
        IntArrays.swap(perm, b2, to - s3, s3);
        int s4 = b2 - a;
        if (s4 > 1) {
            quickSortIndirect(perm, x, from, from + s4);
        }
        int s5 = d - c2;
        if (s5 > 1) {
            quickSortIndirect(perm, x, to - s5, to);
        }
    }

    public static void quickSortIndirect(int[] perm, long[] x) {
        quickSortIndirect(perm, x, 0, x.length);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public static class ForkJoinQuickSortIndirect extends RecursiveAction {
        private static final long serialVersionUID = 1;
        private final int from;
        private final int[] perm;
        private final int to;
        private final long[] x;

        public ForkJoinQuickSortIndirect(int[] perm, long[] x, int from, int to) {
            this.from = from;
            this.to = to;
            this.x = x;
            this.perm = perm;
        }

        @Override // java.util.concurrent.RecursiveAction
        protected void compute() {
            long[] x = this.x;
            int len = this.to - this.from;
            if (len < 8192) {
                LongArrays.quickSortIndirect(this.perm, x, this.from, this.to);
                return;
            }
            int m = this.from + (len / 2);
            int l = this.from;
            int n = this.to - 1;
            int s = len / 8;
            int b = LongArrays.med3Indirect(this.perm, x, l, l + s, (s * 2) + l);
            int m2 = LongArrays.med3Indirect(this.perm, x, m - s, m, m + s);
            int c = LongArrays.med3Indirect(this.perm, x, n - (s * 2), n - s, n);
            int m3 = LongArrays.med3Indirect(this.perm, x, b, m2, c);
            long v = x[this.perm[m3]];
            int a = this.from;
            int b2 = a;
            int c2 = this.to - 1;
            int d = c2;
            while (true) {
                if (b2 <= c2) {
                    int comparison = Long.compare(x[this.perm[b2]], v);
                    if (comparison <= 0) {
                        if (comparison == 0) {
                            IntArrays.swap(this.perm, a, b2);
                            a++;
                        }
                        b2++;
                    }
                }
                while (c2 >= b2) {
                    int comparison2 = Long.compare(x[this.perm[c2]], v);
                    if (comparison2 < 0) {
                        break;
                    }
                    if (comparison2 == 0) {
                        IntArrays.swap(this.perm, c2, d);
                        d--;
                    }
                    c2--;
                }
                if (b2 > c2) {
                    break;
                }
                IntArrays.swap(this.perm, b2, c2);
                b2++;
                c2--;
                c = c;
                len = len;
                m3 = m3;
                b = b;
            }
            int s2 = Math.min(a - this.from, b2 - a);
            IntArrays.swap(this.perm, this.from, b2 - s2, s2);
            int s3 = Math.min(d - c2, (this.to - d) - 1);
            IntArrays.swap(this.perm, b2, this.to - s3, s3);
            int s4 = b2 - a;
            int t = d - c2;
            if (s4 > 1 && t > 1) {
                invokeAll(new ForkJoinQuickSortIndirect(this.perm, x, this.from, this.from + s4), new ForkJoinQuickSortIndirect(this.perm, x, this.to - t, this.to));
            } else if (s4 > 1) {
                invokeAll(new ForkJoinTask[]{new ForkJoinQuickSortIndirect(this.perm, x, this.from, this.from + s4)});
            } else {
                invokeAll(new ForkJoinTask[]{new ForkJoinQuickSortIndirect(this.perm, x, this.to - t, this.to)});
            }
        }
    }

    public static void parallelQuickSortIndirect(int[] perm, long[] x, int from, int to) {
        ForkJoinPool pool = getPool();
        if (to - from < 8192 || pool.getParallelism() == 1) {
            quickSortIndirect(perm, x, from, to);
        } else {
            pool.invoke(new ForkJoinQuickSortIndirect(perm, x, from, to));
        }
    }

    public static void parallelQuickSortIndirect(int[] perm, long[] x) {
        parallelQuickSortIndirect(perm, x, 0, x.length);
    }

    public static void stabilize(int[] perm, long[] x, int from, int to) {
        int curr = from;
        for (int i = from + 1; i < to; i++) {
            if (x[perm[i]] != x[perm[curr]]) {
                if (i - curr > 1) {
                    IntArrays.parallelQuickSort(perm, curr, i);
                }
                curr = i;
            }
        }
        int i2 = to - curr;
        if (i2 > 1) {
            IntArrays.parallelQuickSort(perm, curr, to);
        }
    }

    public static void stabilize(int[] perm, long[] x) {
        stabilize(perm, x, 0, perm.length);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0044, code lost:            if (r2 < 0) goto L22;     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0050, code lost:            return r9;     */
    /* JADX WARN: Code restructure failed: missing block: B:17:?, code lost:            return r11;     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x004b, code lost:            if (r2 > 0) goto L22;     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int med3(long[] r7, long[] r8, int r9, int r10, int r11) {
        /*
            r0 = r7[r9]
            r2 = r7[r10]
            int r0 = java.lang.Long.compare(r0, r2)
            r1 = r0
            if (r0 != 0) goto L14
            r2 = r8[r9]
            r4 = r8[r10]
            int r0 = java.lang.Long.compare(r2, r4)
            goto L15
        L14:
            r0 = r1
        L15:
            r2 = r7[r9]
            r4 = r7[r11]
            int r2 = java.lang.Long.compare(r2, r4)
            r1 = r2
            if (r2 != 0) goto L29
            r2 = r8[r9]
            r4 = r8[r11]
            int r2 = java.lang.Long.compare(r2, r4)
            goto L2a
        L29:
            r2 = r1
        L2a:
            r3 = r7[r10]
            r5 = r7[r11]
            int r3 = java.lang.Long.compare(r3, r5)
            r1 = r3
            if (r3 != 0) goto L3e
            r3 = r8[r10]
            r5 = r8[r11]
            int r3 = java.lang.Long.compare(r3, r5)
            goto L3f
        L3e:
            r3 = r1
        L3f:
            if (r0 >= 0) goto L47
            if (r3 >= 0) goto L44
            goto L49
        L44:
            if (r2 >= 0) goto L4f
            goto L4d
        L47:
            if (r3 <= 0) goto L4b
        L49:
            r4 = r10
            goto L50
        L4b:
            if (r2 <= 0) goto L4f
        L4d:
            r4 = r11
            goto L50
        L4f:
            r4 = r9
        L50:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.longs.LongArrays.med3(long[], long[], int, int, int):int");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void swap(long[] x, long[] y, int a, int b) {
        long t = x[a];
        long u = y[a];
        x[a] = x[b];
        y[a] = y[b];
        x[b] = t;
        y[b] = u;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void swap(long[] x, long[] y, int a, int b, int n) {
        int i = 0;
        while (i < n) {
            swap(x, y, a, b);
            i++;
            a++;
            b++;
        }
    }

    private static void selectionSort(long[] a, long[] b, int from, int to) {
        for (int i = from; i < to - 1; i++) {
            int m = i;
            for (int j = i + 1; j < to; j++) {
                int u = Long.compare(a[j], a[m]);
                if (u < 0 || (u == 0 && b[j] < b[m])) {
                    m = j;
                }
            }
            if (m != i) {
                long t = a[i];
                a[i] = a[m];
                a[m] = t;
                long t2 = b[i];
                b[i] = b[m];
                b[m] = t2;
            }
        }
    }

    public static void quickSort(long[] x, long[] y, int from, int to) {
        int len;
        int m;
        int len2 = to - from;
        if (len2 < 16) {
            selectionSort(x, y, from, to);
            return;
        }
        int m2 = (len2 / 2) + from;
        int l = from;
        int n = to - 1;
        if (len2 > 128) {
            int s = len2 / 8;
            l = med3(x, y, l, l + s, (s * 2) + l);
            m2 = med3(x, y, m2 - s, m2, m2 + s);
            n = med3(x, y, n - (s * 2), n - s, n);
        }
        int comparison = med3(x, y, l, m2, n);
        long v = x[comparison];
        long w = y[comparison];
        int a = from;
        int b = a;
        int c = to - 1;
        int d = c;
        while (true) {
            if (b <= c) {
                len = len2;
                m = comparison;
                int t = Long.compare(x[b], v);
                int compare = t == 0 ? Long.compare(y[b], w) : t;
                int comparison2 = compare;
                if (compare <= 0) {
                    if (comparison2 == 0) {
                        swap(x, y, a, b);
                        a++;
                    }
                    b++;
                    len2 = len;
                    comparison = m;
                }
            } else {
                len = len2;
                m = comparison;
            }
            while (c >= b) {
                int t2 = Long.compare(x[c], v);
                int compare2 = t2 == 0 ? Long.compare(y[c], w) : t2;
                int comparison3 = compare2;
                if (compare2 < 0) {
                    break;
                }
                if (comparison3 == 0) {
                    swap(x, y, c, d);
                    d--;
                }
                c--;
            }
            if (b > c) {
                break;
            }
            swap(x, y, b, c);
            b++;
            c--;
            len2 = len;
            comparison = m;
        }
        int s2 = Math.min(a - from, b - a);
        swap(x, y, from, b - s2, s2);
        int s3 = Math.min(d - c, (to - d) - 1);
        swap(x, y, b, to - s3, s3);
        int s4 = b - a;
        if (s4 > 1) {
            quickSort(x, y, from, from + s4);
        }
        int s5 = d - c;
        if (s5 > 1) {
            quickSort(x, y, to - s5, to);
        }
    }

    public static void quickSort(long[] x, long[] y) {
        ensureSameLength(x, y);
        quickSort(x, y, 0, x.length);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public static class ForkJoinQuickSort2 extends RecursiveAction {
        private static final long serialVersionUID = 1;
        private final int from;
        private final int to;
        private final long[] x;
        private final long[] y;

        public ForkJoinQuickSort2(long[] x, long[] y, int from, int to) {
            this.from = from;
            this.to = to;
            this.x = x;
            this.y = y;
        }

        @Override // java.util.concurrent.RecursiveAction
        protected void compute() {
            int len;
            int m;
            int s;
            int d;
            int l;
            int len2;
            long[] x = this.x;
            long[] y = this.y;
            int len3 = this.to - this.from;
            if (len3 < 8192) {
                LongArrays.quickSort(x, y, this.from, this.to);
                return;
            }
            int m2 = this.from + (len3 / 2);
            int l2 = this.from;
            int n = this.to - 1;
            int t = len3 / 8;
            int comparison = LongArrays.med3(x, y, l2, l2 + t, (t * 2) + l2);
            int comparison2 = LongArrays.med3(x, y, comparison, LongArrays.med3(x, y, m2 - t, m2, m2 + t), LongArrays.med3(x, y, n - (t * 2), n - t, n));
            long v = x[comparison2];
            long w = y[comparison2];
            int a = this.from;
            int b = a;
            int c = this.to - 1;
            int n2 = c;
            while (true) {
                if (b <= c) {
                    s = t;
                    int t2 = Long.compare(x[b], v);
                    if (t2 == 0) {
                        len = len3;
                        m = comparison2;
                        len2 = Long.compare(y[b], w);
                    } else {
                        len = len3;
                        m = comparison2;
                        len2 = t2;
                    }
                    int comparison3 = len2;
                    if (len2 <= 0) {
                        if (comparison3 == 0) {
                            LongArrays.swap(x, y, a, b);
                            a++;
                        }
                        b++;
                        len3 = len;
                        t = s;
                        comparison2 = m;
                    }
                } else {
                    len = len3;
                    m = comparison2;
                    s = t;
                }
                d = n2;
                while (true) {
                    if (c < b) {
                        l = comparison;
                        break;
                    }
                    l = comparison;
                    int t3 = Long.compare(x[c], v);
                    int compare = t3 == 0 ? Long.compare(y[c], w) : t3;
                    int comparison4 = compare;
                    if (compare < 0) {
                        break;
                    }
                    if (comparison4 == 0) {
                        LongArrays.swap(x, y, c, d);
                        d--;
                    }
                    c--;
                    comparison = l;
                }
                if (b > c) {
                    break;
                }
                int d2 = d;
                int l3 = l;
                int d3 = b + 1;
                LongArrays.swap(x, y, b, c);
                b = d3;
                c--;
                t = s;
                comparison2 = m;
                n2 = d2;
                len3 = len;
                comparison = l3;
            }
            int s2 = Math.min(a - this.from, b - a);
            LongArrays.swap(x, y, this.from, b - s2, s2);
            int s3 = Math.min(d - c, (this.to - d) - 1);
            LongArrays.swap(x, y, b, this.to - s3, s3);
            int s4 = b - a;
            int t4 = d - c;
            if (s4 > 1 && t4 > 1) {
                int d4 = this.from;
                invokeAll(new ForkJoinQuickSort2(x, y, d4, this.from + s4), new ForkJoinQuickSort2(x, y, this.to - t4, this.to));
            } else {
                if (s4 <= 1) {
                    int i = this.to - t4;
                    int s5 = this.to;
                    invokeAll(new ForkJoinTask[]{new ForkJoinQuickSort2(x, y, i, s5)});
                    return;
                }
                invokeAll(new ForkJoinTask[]{new ForkJoinQuickSort2(x, y, this.from, this.from + s4)});
            }
        }
    }

    public static void parallelQuickSort(long[] x, long[] y, int from, int to) {
        ForkJoinPool pool = getPool();
        if (to - from < 8192 || pool.getParallelism() == 1) {
            quickSort(x, y, from, to);
        } else {
            pool.invoke(new ForkJoinQuickSort2(x, y, from, to));
        }
    }

    public static void parallelQuickSort(long[] x, long[] y) {
        ensureSameLength(x, y);
        parallelQuickSort(x, y, 0, x.length);
    }

    public static void unstableSort(long[] a, int from, int to) {
        if (to - from >= RADIX_SORT_MIN_THRESHOLD) {
            radixSort(a, from, to);
        } else {
            quickSort(a, from, to);
        }
    }

    public static void unstableSort(long[] a) {
        unstableSort(a, 0, a.length);
    }

    public static void unstableSort(long[] a, int from, int to, LongComparator comp) {
        quickSort(a, from, to, comp);
    }

    public static void unstableSort(long[] a, LongComparator comp) {
        unstableSort(a, 0, a.length, comp);
    }

    public static void mergeSort(long[] a, int from, int to, long[] supp) {
        int len = to - from;
        if (len < 16) {
            insertionSort(a, from, to);
            return;
        }
        if (supp == null) {
            supp = java.util.Arrays.copyOf(a, to);
        }
        int mid = (from + to) >>> 1;
        mergeSort(supp, from, mid, a);
        mergeSort(supp, mid, to, a);
        if (supp[mid - 1] <= supp[mid]) {
            System.arraycopy(supp, from, a, from, len);
            return;
        }
        int p = from;
        int q = mid;
        for (int i = from; i < to; i++) {
            if (q >= to || (p < mid && supp[p] <= supp[q])) {
                int q2 = p + 1;
                a[i] = supp[p];
                p = q2;
            } else {
                a[i] = supp[q];
                q++;
            }
        }
    }

    public static void mergeSort(long[] a, int from, int to) {
        mergeSort(a, from, to, (long[]) null);
    }

    public static void mergeSort(long[] a) {
        mergeSort(a, 0, a.length);
    }

    public static void mergeSort(long[] a, int from, int to, LongComparator comp, long[] supp) {
        int len = to - from;
        if (len < 16) {
            insertionSort(a, from, to, comp);
            return;
        }
        if (supp == null) {
            supp = java.util.Arrays.copyOf(a, to);
        }
        int mid = (from + to) >>> 1;
        mergeSort(supp, from, mid, comp, a);
        mergeSort(supp, mid, to, comp, a);
        if (comp.compare(supp[mid - 1], supp[mid]) <= 0) {
            System.arraycopy(supp, from, a, from, len);
            return;
        }
        int p = from;
        int q = mid;
        for (int i = from; i < to; i++) {
            if (q >= to || (p < mid && comp.compare(supp[p], supp[q]) <= 0)) {
                int q2 = p + 1;
                a[i] = supp[p];
                p = q2;
            } else {
                a[i] = supp[q];
                q++;
            }
        }
    }

    public static void mergeSort(long[] a, int from, int to, LongComparator comp) {
        mergeSort(a, from, to, comp, null);
    }

    public static void mergeSort(long[] a, LongComparator comp) {
        mergeSort(a, 0, a.length, comp);
    }

    public static void stableSort(long[] a, int from, int to) {
        unstableSort(a, from, to);
    }

    public static void stableSort(long[] a) {
        stableSort(a, 0, a.length);
    }

    public static void stableSort(long[] a, int from, int to, LongComparator comp) {
        mergeSort(a, from, to, comp);
    }

    public static void stableSort(long[] a, LongComparator comp) {
        stableSort(a, 0, a.length, comp);
    }

    public static int binarySearch(long[] a, int from, int to, long key) {
        int to2 = to - 1;
        while (from <= to2) {
            int mid = (from + to2) >>> 1;
            long midVal = a[mid];
            if (midVal < key) {
                from = mid + 1;
            } else {
                if (midVal <= key) {
                    return mid;
                }
                to2 = mid - 1;
            }
        }
        return -(from + 1);
    }

    public static int binarySearch(long[] a, long key) {
        return binarySearch(a, 0, a.length, key);
    }

    public static int binarySearch(long[] a, int from, int to, long key, LongComparator c) {
        int to2 = to - 1;
        while (from <= to2) {
            int mid = (from + to2) >>> 1;
            long midVal = a[mid];
            int cmp = c.compare(midVal, key);
            if (cmp < 0) {
                from = mid + 1;
            } else {
                if (cmp <= 0) {
                    return mid;
                }
                to2 = mid - 1;
            }
        }
        return -(from + 1);
    }

    public static int binarySearch(long[] a, long key, LongComparator c) {
        return binarySearch(a, 0, a.length, key, c);
    }

    public static void radixSort(long[] a) {
        radixSort(a, 0, a.length);
    }

    public static void radixSort(long[] a, int from, int to) {
        int d;
        if (to - from < 1024) {
            quickSort(a, from, to);
            return;
        }
        int stackSize = 1786;
        int[] offsetStack = new int[1786];
        int[] lengthStack = new int[1786];
        int[] levelStack = new int[1786];
        offsetStack[0] = from;
        lengthStack[0] = to - from;
        int stackPos = 0 + 1;
        int i = 0;
        levelStack[0] = 0;
        int[] count = new int[256];
        int[] pos = new int[256];
        while (stackPos > 0) {
            stackPos--;
            int first = offsetStack[stackPos];
            int length = lengthStack[stackPos];
            int level = levelStack[stackPos];
            int signMask = level % 8 == 0 ? 128 : i;
            int shift = (7 - (level % 8)) * 8;
            int i2 = first + length;
            while (true) {
                int i3 = i2 - 1;
                if (i2 == first) {
                    break;
                }
                int i4 = (int) (((a[i3] >>> shift) & 255) ^ signMask);
                count[i4] = count[i4] + 1;
                levelStack = levelStack;
                i2 = i3;
            }
            int[] levelStack2 = levelStack;
            int lastUsed = -1;
            int p = first;
            for (int i5 = 0; i5 < 256; i5++) {
                if (count[i5] != 0) {
                    lastUsed = i5;
                }
                int i6 = p + count[i5];
                p = i6;
                pos[i5] = i6;
            }
            int i7 = first + length;
            int end = i7 - count[lastUsed];
            int i8 = first;
            while (i8 <= end) {
                long t = a[i8];
                int stackSize2 = stackSize;
                int lastUsed2 = lastUsed;
                int c = (int) (((t >>> shift) & 255) ^ signMask);
                if (i8 < end) {
                    while (true) {
                        int d2 = pos[c] - 1;
                        pos[c] = d2;
                        if (d2 <= i8) {
                            break;
                        }
                        long z = t;
                        t = a[d2];
                        a[d2] = z;
                        c = (int) (((t >>> shift) & 255) ^ signMask);
                    }
                    a[i8] = t;
                    d = c;
                } else {
                    d = c;
                }
                if (level < 7 && count[d] > 1) {
                    if (count[d] < 1024) {
                        quickSort(a, i8, count[d] + i8);
                    } else {
                        offsetStack[stackPos] = i8;
                        lengthStack[stackPos] = count[d];
                        levelStack2[stackPos] = level + 1;
                        stackPos++;
                    }
                }
                i8 += count[d];
                count[d] = 0;
                stackSize = stackSize2;
                lastUsed = lastUsed2;
            }
            levelStack = levelStack2;
            i = 0;
            stackSize = stackSize;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public static final class Segment {
        protected final int length;
        protected final int level;
        protected final int offset;

        protected Segment(int offset, int length, int level) {
            this.offset = offset;
            this.length = length;
            this.level = level;
        }

        public String toString() {
            return "Segment [offset=" + this.offset + ", length=" + this.length + ", level=" + this.level + "]";
        }
    }

    public static void parallelRadixSort(final long[] a, int from, int to) {
        ForkJoinPool pool = getPool();
        if (to - from < 1024 || pool.getParallelism() == 1) {
            quickSort(a, from, to);
            return;
        }
        final LinkedBlockingQueue<Segment> queue = new LinkedBlockingQueue<>();
        queue.add(new Segment(from, to - from, 0));
        final AtomicInteger queueSize = new AtomicInteger(1);
        final int numberOfThreads = pool.getParallelism();
        ExecutorCompletionService<Void> executorCompletionService = new ExecutorCompletionService<>(pool);
        int j = numberOfThreads;
        while (true) {
            int j2 = j - 1;
            if (j == 0) {
                break;
            }
            executorCompletionService.submit(new Callable() { // from class: it.unimi.dsi.fastutil.longs.LongArrays$$ExternalSyntheticLambda0
                @Override // java.util.concurrent.Callable
                public final Object call() {
                    return LongArrays.lambda$parallelRadixSort$0(queueSize, numberOfThreads, queue, a);
                }
            });
            j = j2;
        }
        Throwable problem = null;
        int i = numberOfThreads;
        while (true) {
            int i2 = i - 1;
            if (i == 0) {
                break;
            }
            try {
                executorCompletionService.take().get();
            } catch (Exception e) {
                problem = e.getCause();
            }
            i = i2;
        }
        if (problem != null) {
            if (!(problem instanceof RuntimeException)) {
                throw new RuntimeException(problem);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ Void lambda$parallelRadixSort$0(AtomicInteger queueSize, int numberOfThreads, LinkedBlockingQueue queue, long[] a) throws Exception {
        int i;
        int shift;
        int d;
        int i2 = 256;
        int[] count = new int[256];
        int[] pos = new int[256];
        while (true) {
            if (queueSize.get() == 0) {
                int i3 = numberOfThreads;
                while (true) {
                    int i4 = i3 - 1;
                    if (i3 == 0) {
                        break;
                    }
                    queue.add(POISON_PILL);
                    i3 = i4;
                }
            }
            Segment segment = (Segment) queue.take();
            if (segment == POISON_PILL) {
                return null;
            }
            int first = segment.offset;
            int length = segment.length;
            int level = segment.level;
            int signMask = level % 8 == 0 ? 128 : 0;
            int shift2 = (7 - (level % 8)) * 8;
            int i5 = first + length;
            while (true) {
                int i6 = i5 - 1;
                i = 1;
                if (i5 == first) {
                    break;
                }
                int i7 = (int) (signMask ^ ((a[i6] >>> shift2) & 255));
                count[i7] = count[i7] + 1;
                i5 = i6;
            }
            int lastUsed = -1;
            int p = first;
            for (int i8 = 0; i8 < i2; i8++) {
                if (count[i8] != 0) {
                    lastUsed = i8;
                }
                int i9 = p + count[i8];
                p = i9;
                pos[i8] = i9;
            }
            int i10 = first + length;
            int end = i10 - count[lastUsed];
            int i11 = first;
            while (i11 <= end) {
                long t = a[i11];
                int[] count2 = count;
                int c = (int) (((t >>> shift2) & 255) ^ signMask);
                if (i11 >= end) {
                    shift = shift2;
                    d = c;
                } else {
                    while (true) {
                        int d2 = pos[c] - i;
                        pos[c] = d2;
                        if (d2 <= i11) {
                            break;
                        }
                        long z = t;
                        t = a[d2];
                        a[d2] = z;
                        c = (int) (((t >>> shift2) & 255) ^ signMask);
                        shift2 = shift2;
                        i = 1;
                    }
                    shift = shift2;
                    a[i11] = t;
                    d = c;
                }
                if (level < 7 && count2[d] > 1) {
                    if (count2[d] < 1024) {
                        quickSort(a, i11, count2[d] + i11);
                    } else {
                        queueSize.incrementAndGet();
                        queue.add(new Segment(i11, count2[d], level + 1));
                    }
                }
                i11 += count2[d];
                count2[d] = 0;
                shift2 = shift;
                count = count2;
                i = 1;
            }
            queueSize.decrementAndGet();
            count = count;
            i2 = 256;
        }
    }

    public static void parallelRadixSort(long[] a) {
        parallelRadixSort(a, 0, a.length);
    }

    public static void radixSortIndirect(int[] perm, long[] a, boolean stable) {
        radixSortIndirect(perm, a, 0, perm.length, stable);
    }

    public static void radixSortIndirect(int[] perm, long[] a, int from, int to, boolean stable) {
        int maxLevel;
        int stackSize;
        int[] support;
        int end;
        int lastUsed;
        int end2;
        int[] support2;
        if (to - from < 1024) {
            quickSortIndirect(perm, a, from, to);
            if (stable) {
                stabilize(perm, a, from, to);
                return;
            }
            return;
        }
        int maxLevel2 = 7;
        int stackSize2 = 1786;
        int[] offsetStack = new int[1786];
        int[] lengthStack = new int[1786];
        int[] levelStack = new int[1786];
        offsetStack[0] = from;
        lengthStack[0] = to - from;
        int stackPos = 0 + 1;
        int i = 0;
        levelStack[0] = 0;
        int[] count = new int[256];
        int[] pos = new int[256];
        int[] support3 = stable ? new int[perm.length] : null;
        while (stackPos > 0) {
            stackPos--;
            int first = offsetStack[stackPos];
            int length = lengthStack[stackPos];
            int level = levelStack[stackPos];
            int signMask = level % 8 == 0 ? 128 : i;
            int shift = (7 - (level % 8)) * 8;
            int i2 = first + length;
            while (true) {
                int i3 = i2;
                i2 = i3 - 1;
                if (i3 == first) {
                    break;
                }
                int signMask2 = signMask;
                int i4 = (int) (((a[perm[i2]] >>> shift) & 255) ^ signMask2);
                count[i4] = count[i4] + 1;
                levelStack = levelStack;
                signMask = signMask2;
            }
            int signMask3 = signMask;
            int[] levelStack2 = levelStack;
            int lastUsed2 = -1;
            int i5 = 0;
            int p = stable ? 0 : first;
            while (true) {
                maxLevel = maxLevel2;
                if (i5 >= 256) {
                    break;
                }
                if (count[i5] != 0) {
                    lastUsed2 = i5;
                }
                int i6 = p + count[i5];
                p = i6;
                pos[i5] = i6;
                i5++;
                maxLevel2 = maxLevel;
            }
            if (stable) {
                int level2 = first + length;
                while (true) {
                    int i7 = level2 - 1;
                    if (level2 == first) {
                        break;
                    }
                    int level3 = level;
                    int i8 = (int) (((a[perm[i7]] >>> shift) & 255) ^ signMask3);
                    int i9 = pos[i8] - 1;
                    pos[i8] = i9;
                    support3[i9] = perm[i7];
                    level = level3;
                    level2 = i7;
                }
                int level4 = level;
                System.arraycopy(support3, 0, perm, first, length);
                int i10 = 0;
                int p2 = first;
                while (i10 <= lastUsed2) {
                    int stackSize3 = stackSize2;
                    if (level4 < 7) {
                        support2 = support3;
                        if (count[i10] > 1) {
                            if (count[i10] < 1024) {
                                quickSortIndirect(perm, a, p2, count[i10] + p2);
                                if (stable) {
                                    stabilize(perm, a, p2, count[i10] + p2);
                                }
                            } else {
                                offsetStack[stackPos] = p2;
                                lengthStack[stackPos] = count[i10];
                                levelStack2[stackPos] = level4 + 1;
                                stackPos++;
                            }
                        }
                    } else {
                        support2 = support3;
                    }
                    p2 += count[i10];
                    i10++;
                    stackSize2 = stackSize3;
                    support3 = support2;
                }
                stackSize = stackSize2;
                support = support3;
                java.util.Arrays.fill(count, 0);
                lastUsed = 0;
                end = 1024;
            } else {
                stackSize = stackSize2;
                support = support3;
                int end3 = (first + length) - count[lastUsed2];
                int i11 = first;
                int c = -1;
                while (i11 <= end3) {
                    int t = perm[i11];
                    int lastUsed3 = lastUsed2;
                    c = (int) (((a[t] >>> shift) & 255) ^ signMask3);
                    if (i11 < end3) {
                        while (true) {
                            int d = pos[c] - 1;
                            pos[c] = d;
                            if (d <= i11) {
                                break;
                            }
                            int z = t;
                            t = perm[d];
                            perm[d] = z;
                            c = (int) (((a[t] >>> shift) & 255) ^ signMask3);
                        }
                        perm[i11] = t;
                    }
                    if (level < 7) {
                        end2 = end3;
                        if (count[c] > 1) {
                            if (count[c] < 1024) {
                                quickSortIndirect(perm, a, i11, count[c] + i11);
                                if (stable) {
                                    stabilize(perm, a, i11, count[c] + i11);
                                }
                            } else {
                                offsetStack[stackPos] = i11;
                                lengthStack[stackPos] = count[c];
                                levelStack2[stackPos] = level + 1;
                                stackPos++;
                            }
                        }
                    } else {
                        end2 = end3;
                    }
                    i11 += count[c];
                    count[c] = 0;
                    end3 = end2;
                    lastUsed2 = lastUsed3;
                }
                end = 1024;
                lastUsed = 0;
            }
            i = lastUsed;
            levelStack = levelStack2;
            stackSize2 = stackSize;
            maxLevel2 = maxLevel;
            support3 = support;
        }
    }

    public static void parallelRadixSortIndirect(final int[] perm, final long[] a, int from, int to, final boolean stable) {
        ForkJoinPool pool = getPool();
        if (to - from >= 1024 && pool.getParallelism() != 1) {
            final LinkedBlockingQueue<Segment> queue = new LinkedBlockingQueue<>();
            queue.add(new Segment(from, to - from, 0));
            final AtomicInteger queueSize = new AtomicInteger(1);
            final int numberOfThreads = pool.getParallelism();
            ExecutorCompletionService<Void> executorCompletionService = new ExecutorCompletionService<>(pool);
            final int[] support = stable ? new int[perm.length] : null;
            int j = numberOfThreads;
            while (true) {
                int j2 = j - 1;
                if (j == 0) {
                    break;
                }
                executorCompletionService.submit(new Callable() { // from class: it.unimi.dsi.fastutil.longs.LongArrays$$ExternalSyntheticLambda2
                    @Override // java.util.concurrent.Callable
                    public final Object call() {
                        return LongArrays.lambda$parallelRadixSortIndirect$1(queueSize, numberOfThreads, queue, a, perm, stable, support);
                    }
                });
                j = j2;
            }
            Throwable problem = null;
            int i = numberOfThreads;
            while (true) {
                int i2 = i - 1;
                if (i == 0) {
                    break;
                }
                try {
                    executorCompletionService.take().get();
                } catch (Exception e) {
                    problem = e.getCause();
                }
                i = i2;
            }
            if (problem != null) {
                if (!(problem instanceof RuntimeException)) {
                    throw new RuntimeException(problem);
                }
                throw ((RuntimeException) problem);
            }
            return;
        }
        radixSortIndirect(perm, a, from, to, stable);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ Void lambda$parallelRadixSortIndirect$1(AtomicInteger queueSize, int numberOfThreads, LinkedBlockingQueue queue, long[] a, int[] perm, boolean stable, int[] support) throws Exception {
        int[] pos;
        int[] count;
        boolean z;
        int end;
        int lastUsed;
        Segment segment;
        long[] jArr = a;
        boolean z2 = stable;
        int[] iArr = support;
        int[] count2 = new int[256];
        int[] pos2 = new int[256];
        while (true) {
            if (queueSize.get() == 0) {
                int i = numberOfThreads;
                while (true) {
                    int i2 = i - 1;
                    if (i == 0) {
                        break;
                    }
                    queue.add(POISON_PILL);
                    i = i2;
                }
            }
            Segment segment2 = (Segment) queue.take();
            if (segment2 == POISON_PILL) {
                return null;
            }
            int first = segment2.offset;
            int length = segment2.length;
            int level = segment2.level;
            int signMask = level % 8 == 0 ? 128 : 0;
            int shift = (7 - (level % 8)) * 8;
            int i3 = first + length;
            while (true) {
                int i4 = i3 - 1;
                if (i3 == first) {
                    break;
                }
                int[] count3 = count2;
                int i5 = (int) (((jArr[perm[i4]] >>> shift) & 255) ^ signMask);
                count3[i5] = count3[i5] + 1;
                count2 = count3;
                i3 = i4;
            }
            int[] count4 = count2;
            int lastUsed2 = -1;
            int p = first;
            for (int i6 = 0; i6 < 256; i6++) {
                if (count4[i6] != 0) {
                    lastUsed2 = i6;
                }
                int i7 = p + count4[i6];
                p = i7;
                pos2[i6] = i7;
            }
            if (z2) {
                int i8 = first + length;
                while (true) {
                    int i9 = i8;
                    i8 = i9 - 1;
                    if (i9 == first) {
                        break;
                    }
                    int[] pos3 = pos2;
                    int i10 = (int) (((jArr[perm[i8]] >>> shift) & 255) ^ signMask);
                    int i11 = pos3[i10] - 1;
                    pos3[i10] = i11;
                    iArr[i11] = perm[i8];
                    pos2 = pos3;
                }
                pos = pos2;
                System.arraycopy(iArr, first, perm, first, length);
                int i12 = 0;
                int p2 = first;
                while (i12 <= lastUsed2) {
                    if (level < 7) {
                        segment = segment2;
                        if (count4[i12] > 1) {
                            if (count4[i12] < 1024) {
                                radixSortIndirect(perm, jArr, p2, count4[i12] + p2, z2);
                            } else {
                                queueSize.incrementAndGet();
                                queue.add(new Segment(p2, count4[i12], level + 1));
                            }
                        }
                    } else {
                        segment = segment2;
                    }
                    p2 += count4[i12];
                    i12++;
                    z2 = stable;
                    segment2 = segment;
                }
                count = count4;
                java.util.Arrays.fill(count, 0);
                z = stable;
            } else {
                pos = pos2;
                count = count4;
                int end2 = (first + length) - count[lastUsed2];
                int i13 = first;
                int c = -1;
                while (i13 <= end2) {
                    int t = perm[i13];
                    c = (int) (((jArr[t] >>> shift) & 255) ^ signMask);
                    if (i13 >= end2) {
                        end = end2;
                        lastUsed = lastUsed2;
                    } else {
                        int t2 = t;
                        while (true) {
                            int d = pos[c] - 1;
                            pos[c] = d;
                            if (d <= i13) {
                                break;
                            }
                            int z3 = t2;
                            t2 = perm[d];
                            perm[d] = z3;
                            c = (int) (((jArr[t2] >>> shift) & 255) ^ signMask);
                            lastUsed2 = lastUsed2;
                            end2 = end2;
                        }
                        end = end2;
                        lastUsed = lastUsed2;
                        perm[i13] = t2;
                    }
                    if (level < 7 && count[c] > 1) {
                        if (count[c] < 1024) {
                            radixSortIndirect(perm, jArr, i13, count[c] + i13, stable);
                        } else {
                            queueSize.incrementAndGet();
                            queue.add(new Segment(i13, count[c], level + 1));
                        }
                    }
                    i13 += count[c];
                    count[c] = 0;
                    jArr = a;
                    lastUsed2 = lastUsed;
                    end2 = end;
                }
                z = stable;
            }
            queueSize.decrementAndGet();
            jArr = a;
            count2 = count;
            z2 = z;
            pos2 = pos;
            iArr = support;
        }
    }

    public static void parallelRadixSortIndirect(int[] perm, long[] a, boolean stable) {
        parallelRadixSortIndirect(perm, a, 0, a.length, stable);
    }

    public static void radixSort(long[] a, long[] b) {
        ensureSameLength(a, b);
        radixSort(a, b, 0, a.length);
    }

    public static void radixSort(long[] a, long[] b, int from, int to) {
        int layers;
        int d;
        if (to - from < 1024) {
            quickSort(a, b, from, to);
            return;
        }
        int layers2 = 2;
        int maxLevel = 15;
        int stackSize = 3826;
        int[] offsetStack = new int[3826];
        int[] lengthStack = new int[3826];
        int[] levelStack = new int[3826];
        offsetStack[0] = from;
        lengthStack[0] = to - from;
        int stackPos = 0 + 1;
        levelStack[0] = 0;
        int[] count = new int[256];
        int[] pos = new int[256];
        while (stackPos > 0) {
            stackPos--;
            int first = offsetStack[stackPos];
            int length = lengthStack[stackPos];
            int level = levelStack[stackPos];
            int signMask = level % 8 == 0 ? 128 : 0;
            long[] k = level < 8 ? a : b;
            int shift = (7 - (level % 8)) * 8;
            int signMask2 = first + length;
            while (true) {
                int i = signMask2 - 1;
                if (signMask2 == first) {
                    break;
                }
                int signMask3 = signMask;
                int i2 = (int) (((k[i] >>> shift) & 255) ^ signMask3);
                count[i2] = count[i2] + 1;
                levelStack = levelStack;
                signMask = signMask3;
                signMask2 = i;
            }
            int signMask4 = signMask;
            int[] levelStack2 = levelStack;
            int lastUsed = -1;
            int i3 = 0;
            int p = first;
            while (true) {
                layers = layers2;
                if (i3 >= 256) {
                    break;
                }
                if (count[i3] != 0) {
                    lastUsed = i3;
                }
                int i4 = p + count[i3];
                p = i4;
                pos[i3] = i4;
                i3++;
                layers2 = layers;
            }
            int end = (first + length) - count[lastUsed];
            int i5 = first;
            while (i5 <= end) {
                long t = a[i5];
                long u = b[i5];
                int maxLevel2 = maxLevel;
                int stackSize2 = stackSize;
                int c = (int) (((k[i5] >>> shift) & 255) ^ signMask4);
                if (i5 < end) {
                    while (true) {
                        int d2 = pos[c] - 1;
                        pos[c] = d2;
                        if (d2 <= i5) {
                            break;
                        }
                        c = (int) (((k[d2] >>> shift) & 255) ^ signMask4);
                        long z = t;
                        t = a[d2];
                        a[d2] = z;
                        long z2 = u;
                        u = b[d2];
                        b[d2] = z2;
                    }
                    a[i5] = t;
                    b[i5] = u;
                    d = c;
                } else {
                    d = c;
                }
                if (level < 15 && count[d] > 1) {
                    if (count[d] < 1024) {
                        quickSort(a, b, i5, count[d] + i5);
                    } else {
                        offsetStack[stackPos] = i5;
                        lengthStack[stackPos] = count[d];
                        levelStack2[stackPos] = level + 1;
                        stackPos++;
                    }
                }
                i5 += count[d];
                count[d] = 0;
                maxLevel = maxLevel2;
                stackSize = stackSize2;
            }
            levelStack = levelStack2;
            layers2 = layers;
            maxLevel = maxLevel;
            stackSize = stackSize;
        }
    }

    public static void parallelRadixSort(final long[] a, final long[] b, int from, int to) {
        ForkJoinPool pool = getPool();
        if (to - from >= 1024 && pool.getParallelism() != 1) {
            if (a.length != b.length) {
                throw new IllegalArgumentException("Array size mismatch.");
            }
            final LinkedBlockingQueue<Segment> queue = new LinkedBlockingQueue<>();
            queue.add(new Segment(from, to - from, 0));
            final AtomicInteger queueSize = new AtomicInteger(1);
            final int numberOfThreads = pool.getParallelism();
            ExecutorCompletionService<Void> executorCompletionService = new ExecutorCompletionService<>(pool);
            int j = numberOfThreads;
            while (true) {
                int j2 = j - 1;
                if (j == 0) {
                    break;
                }
                executorCompletionService.submit(new Callable() { // from class: it.unimi.dsi.fastutil.longs.LongArrays$$ExternalSyntheticLambda1
                    @Override // java.util.concurrent.Callable
                    public final Object call() {
                        return LongArrays.lambda$parallelRadixSort$2(queueSize, numberOfThreads, queue, a, b);
                    }
                });
                j = j2;
            }
            Throwable problem = null;
            int i = numberOfThreads;
            while (true) {
                int i2 = i - 1;
                if (i == 0) {
                    break;
                }
                try {
                    executorCompletionService.take().get();
                } catch (Exception e) {
                    problem = e.getCause();
                }
                i = i2;
            }
            if (problem != null) {
                if (!(problem instanceof RuntimeException)) {
                    throw new RuntimeException(problem);
                }
                throw ((RuntimeException) problem);
            }
            return;
        }
        quickSort(a, b, from, to);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ Void lambda$parallelRadixSort$2(AtomicInteger queueSize, int numberOfThreads, LinkedBlockingQueue queue, long[] a, long[] b) throws Exception {
        int d;
        long[] jArr = a;
        int i = 256;
        int[] count = new int[256];
        int[] pos = new int[256];
        while (true) {
            if (queueSize.get() == 0) {
                int i2 = numberOfThreads;
                while (true) {
                    int i3 = i2 - 1;
                    if (i2 == 0) {
                        break;
                    }
                    queue.add(POISON_PILL);
                    i2 = i3;
                }
            }
            Segment segment = (Segment) queue.take();
            if (segment == POISON_PILL) {
                return null;
            }
            int first = segment.offset;
            int length = segment.length;
            int level = segment.level;
            int signMask = level % 8 == 0 ? 128 : 0;
            long[] k = level < 8 ? jArr : b;
            int shift = (7 - (level % 8)) * 8;
            int i4 = first + length;
            while (true) {
                int i5 = i4 - 1;
                if (i4 == first) {
                    break;
                }
                int i6 = (int) (((k[i5] >>> shift) & 255) ^ signMask);
                count[i6] = count[i6] + 1;
                i4 = i5;
            }
            int lastUsed = -1;
            int p = first;
            for (int i7 = 0; i7 < i; i7++) {
                if (count[i7] != 0) {
                    lastUsed = i7;
                }
                int i8 = p + count[i7];
                p = i8;
                pos[i7] = i8;
            }
            int i9 = first + length;
            int end = i9 - count[lastUsed];
            int i10 = first;
            while (i10 <= end) {
                long t = jArr[i10];
                long u = b[i10];
                int[] count2 = count;
                int c = (int) (((k[i10] >>> shift) & 255) ^ signMask);
                if (i10 >= end) {
                    d = c;
                } else {
                    while (true) {
                        int d2 = pos[c] - 1;
                        pos[c] = d2;
                        if (d2 <= i10) {
                            break;
                        }
                        c = (int) (((k[d2] >>> shift) & 255) ^ signMask);
                        long z = t;
                        long w = u;
                        t = jArr[d2];
                        u = b[d2];
                        jArr[d2] = z;
                        b[d2] = w;
                    }
                    jArr[i10] = t;
                    b[i10] = u;
                    d = c;
                }
                if (level < 15 && count2[d] > 1) {
                    if (count2[d] < 1024) {
                        quickSort(jArr, b, i10, count2[d] + i10);
                    } else {
                        queueSize.incrementAndGet();
                        queue.add(new Segment(i10, count2[d], level + 1));
                    }
                }
                i10 += count2[d];
                count2[d] = 0;
                jArr = a;
                count = count2;
            }
            queueSize.decrementAndGet();
            jArr = a;
            i = 256;
        }
    }

    public static void parallelRadixSort(long[] a, long[] b) {
        ensureSameLength(a, b);
        parallelRadixSort(a, b, 0, a.length);
    }

    private static void insertionSortIndirect(int[] perm, long[] a, long[] b, int from, int to) {
        int i = from;
        while (true) {
            i++;
            if (i < to) {
                int t = perm[i];
                int j = i;
                int u = perm[j - 1];
                while (true) {
                    if (a[t] < a[u] || (a[t] == a[u] && b[t] < b[u])) {
                        perm[j] = u;
                        if (from != j - 1) {
                            j--;
                            u = perm[j - 1];
                        } else {
                            j--;
                            break;
                        }
                    }
                }
                perm[j] = t;
            } else {
                return;
            }
        }
    }

    public static void radixSortIndirect(int[] perm, long[] a, long[] b, boolean stable) {
        ensureSameLength(a, b);
        radixSortIndirect(perm, a, b, 0, a.length, stable);
    }

    public static void radixSortIndirect(int[] perm, long[] a, long[] b, int from, int to, boolean stable) {
        int maxLevel;
        int stackPos;
        int[] pos;
        int[] support;
        int lastUsed;
        int d;
        int[] pos2;
        if (to - from < 64) {
            insertionSortIndirect(perm, a, b, from, to);
            return;
        }
        int layers = 2;
        int maxLevel2 = 15;
        int stackSize = 3826;
        int[] offsetStack = new int[3826];
        int[] lengthStack = new int[3826];
        int[] levelStack = new int[3826];
        offsetStack[0] = from;
        lengthStack[0] = to - from;
        int level = 0 + 1;
        levelStack[0] = 0;
        int[] count = new int[256];
        int[] pos3 = new int[256];
        int[] support2 = stable ? new int[perm.length] : null;
        while (level > 0) {
            int stackPos2 = level - 1;
            int first = offsetStack[stackPos2];
            int length = lengthStack[stackPos2];
            int level2 = levelStack[stackPos2];
            int stackSize2 = level2 % 8 == 0 ? 128 : 0;
            int layers2 = layers;
            long[] k = level2 < 8 ? a : b;
            int shift = (7 - (level2 % 8)) * 8;
            int signMask = first + length;
            while (true) {
                int i = signMask - 1;
                maxLevel = maxLevel2;
                if (signMask == first) {
                    break;
                }
                int signMask2 = stackSize2;
                int signMask3 = stackSize;
                int i2 = (int) (((k[perm[i]] >>> shift) & 255) ^ signMask2);
                count[i2] = count[i2] + 1;
                stackSize = signMask3;
                maxLevel2 = maxLevel;
                stackSize2 = signMask2;
                signMask = i;
            }
            int signMask4 = stackSize2;
            int signMask5 = stackSize;
            int lastUsed2 = -1;
            int i3 = 0;
            int p = stable ? 0 : first;
            while (true) {
                stackPos = stackPos2;
                if (i3 >= 256) {
                    break;
                }
                if (count[i3] != 0) {
                    lastUsed2 = i3;
                }
                int i4 = p + count[i3];
                p = i4;
                pos3[i3] = i4;
                i3++;
                stackPos2 = stackPos;
            }
            if (stable) {
                int level3 = first + length;
                while (true) {
                    int i5 = level3 - 1;
                    if (level3 == first) {
                        break;
                    }
                    int level4 = level2;
                    int i6 = (int) (((k[perm[i5]] >>> shift) & 255) ^ signMask4);
                    int i7 = pos3[i6] - 1;
                    pos3[i6] = i7;
                    support2[i7] = perm[i5];
                    level2 = level4;
                    level3 = i5;
                }
                int level5 = level2;
                System.arraycopy(support2, 0, perm, first, length);
                int i8 = 0;
                int p2 = first;
                while (true) {
                    int level6 = level5;
                    if (i8 >= 256) {
                        break;
                    }
                    level5 = level6;
                    int[] support3 = support2;
                    if (level5 < 15) {
                        pos2 = pos3;
                        if (count[i8] > 1) {
                            if (count[i8] < 64) {
                                insertionSortIndirect(perm, a, b, p2, count[i8] + p2);
                            } else {
                                offsetStack[stackPos] = p2;
                                lengthStack[stackPos] = count[i8];
                                levelStack[stackPos] = level5 + 1;
                                stackPos++;
                            }
                        }
                    } else {
                        pos2 = pos3;
                    }
                    p2 += count[i8];
                    i8++;
                    support2 = support3;
                    pos3 = pos2;
                }
                pos = pos3;
                support = support2;
                java.util.Arrays.fill(count, 0);
                lastUsed = 0;
                level = stackPos;
            } else {
                pos = pos3;
                support = support2;
                int end = (first + length) - count[lastUsed2];
                int i9 = first;
                while (i9 <= end) {
                    int t = perm[i9];
                    int first2 = first;
                    int lastUsed3 = lastUsed2;
                    int c = (int) (((k[t] >>> shift) & 255) ^ signMask4);
                    if (i9 < end) {
                        while (true) {
                            int d2 = pos[c] - 1;
                            pos[c] = d2;
                            if (d2 <= i9) {
                                break;
                            }
                            int z = t;
                            t = perm[d2];
                            perm[d2] = z;
                            c = (int) (((k[t] >>> shift) & 255) ^ signMask4);
                        }
                        perm[i9] = t;
                        d = c;
                    } else {
                        d = c;
                    }
                    if (level2 < 15 && count[d] > 1) {
                        if (count[d] < 64) {
                            insertionSortIndirect(perm, a, b, i9, count[d] + i9);
                        } else {
                            offsetStack[stackPos] = i9;
                            lengthStack[stackPos] = count[d];
                            levelStack[stackPos] = level2 + 1;
                            stackPos++;
                        }
                    }
                    i9 += count[d];
                    count[d] = 0;
                    first = first2;
                    lastUsed2 = lastUsed3;
                }
                lastUsed = 0;
                level = stackPos;
            }
            layers = layers2;
            stackSize = signMask5;
            support2 = support;
            maxLevel2 = maxLevel;
            pos3 = pos;
        }
    }

    private static void selectionSort(long[][] a, int from, int to, int level) {
        int layers = a.length;
        int firstLayer = level / 8;
        for (int i = from; i < to - 1; i++) {
            int m = i;
            for (int j = i + 1; j < to; j++) {
                int p = firstLayer;
                while (true) {
                    if (p >= layers) {
                        break;
                    }
                    if (a[p][j] < a[p][m]) {
                        m = j;
                        break;
                    } else if (a[p][j] > a[p][m]) {
                        break;
                    } else {
                        p++;
                    }
                }
            }
            if (m != i) {
                int p2 = layers;
                while (true) {
                    int p3 = p2 - 1;
                    if (p2 != 0) {
                        long u = a[p3][i];
                        a[p3][i] = a[p3][m];
                        a[p3][m] = u;
                        p2 = p3;
                    }
                }
            }
        }
    }

    public static void radixSort(long[][] a) {
        radixSort(a, 0, a[0].length);
    }

    public static void radixSort(long[][] a, int from, int to) {
        int stackSize;
        int lastUsed;
        int end;
        int d;
        if (to - from < 64) {
            selectionSort(a, from, to, 0);
            return;
        }
        int layers = a.length;
        int maxLevel = (layers * 8) - 1;
        int p = layers;
        int l = a[0].length;
        while (true) {
            int p2 = p - 1;
            if (p != 0) {
                if (a[p2].length != l) {
                    throw new IllegalArgumentException("The array of index " + p2 + " has not the same length of the array of index 0.");
                }
                p = p2;
            } else {
                int stackSize2 = (((layers * 8) - 1) * 255) + 1;
                int[] offsetStack = new int[stackSize2];
                int[] lengthStack = new int[stackSize2];
                int[] levelStack = new int[stackSize2];
                offsetStack[0] = from;
                lengthStack[0] = to - from;
                int stackPos = 0 + 1;
                levelStack[0] = 0;
                int[] count = new int[256];
                int[] pos = new int[256];
                long[] t = new long[layers];
                while (stackPos > 0) {
                    stackPos--;
                    int first = offsetStack[stackPos];
                    int length = lengthStack[stackPos];
                    int level = levelStack[stackPos];
                    int signMask = level % 8 == 0 ? 128 : 0;
                    long[] k = a[level / 8];
                    int shift = (7 - (level % 8)) * 8;
                    int i = first + length;
                    while (true) {
                        int i2 = i;
                        i = i2 - 1;
                        if (i2 == first) {
                            break;
                        }
                        int signMask2 = signMask;
                        int i3 = (int) (((k[i] >>> shift) & 255) ^ signMask2);
                        count[i3] = count[i3] + 1;
                        signMask = signMask2;
                    }
                    int signMask3 = signMask;
                    int lastUsed2 = -1;
                    int i4 = 0;
                    int p3 = first;
                    while (true) {
                        stackSize = stackSize2;
                        if (i4 >= 256) {
                            break;
                        }
                        if (count[i4] != 0) {
                            lastUsed2 = i4;
                        }
                        int i5 = p3 + count[i4];
                        p3 = i5;
                        pos[i4] = i5;
                        i4++;
                        stackSize2 = stackSize;
                    }
                    int i6 = first + length;
                    int end2 = i6 - count[lastUsed2];
                    int i7 = first;
                    while (i7 <= end2) {
                        int p4 = layers;
                        while (true) {
                            int p5 = p4 - 1;
                            if (p4 == 0) {
                                break;
                            }
                            t[p5] = a[p5][i7];
                            p4 = p5;
                        }
                        int[] lengthStack2 = lengthStack;
                        int[] levelStack2 = levelStack;
                        int c = (int) (((k[i7] >>> shift) & 255) ^ signMask3);
                        if (i7 >= end2) {
                            lastUsed = lastUsed2;
                            end = end2;
                            d = c;
                        } else {
                            while (true) {
                                int d2 = pos[c] - 1;
                                pos[c] = d2;
                                if (d2 <= i7) {
                                    break;
                                }
                                long j = (k[d2] >>> shift) & 255;
                                int lastUsed3 = lastUsed2;
                                int end3 = end2;
                                c = (int) (j ^ signMask3);
                                int p6 = layers;
                                while (true) {
                                    int p7 = p6 - 1;
                                    if (p6 != 0) {
                                        long u = t[p7];
                                        t[p7] = a[p7][d2];
                                        a[p7][d2] = u;
                                        p6 = p7;
                                    }
                                }
                                lastUsed2 = lastUsed3;
                                end2 = end3;
                            }
                            lastUsed = lastUsed2;
                            end = end2;
                            int p8 = layers;
                            while (true) {
                                int p9 = p8 - 1;
                                if (p8 == 0) {
                                    break;
                                }
                                a[p9][i7] = t[p9];
                                p8 = p9;
                            }
                            d = c;
                        }
                        if (level < maxLevel && count[d] > 1) {
                            if (count[d] < 64) {
                                selectionSort(a, i7, count[d] + i7, level + 1);
                            } else {
                                offsetStack[stackPos] = i7;
                                lengthStack2[stackPos] = count[d];
                                levelStack2[stackPos] = level + 1;
                                stackPos++;
                            }
                        }
                        i7 += count[d];
                        count[d] = 0;
                        lastUsed2 = lastUsed;
                        lengthStack = lengthStack2;
                        levelStack = levelStack2;
                        end2 = end;
                    }
                    stackSize2 = stackSize;
                    lengthStack = lengthStack;
                    levelStack = levelStack;
                }
                return;
            }
        }
    }

    public static long[] shuffle(long[] a, int from, int to, Random random) {
        int p = to - from;
        while (true) {
            int i = p - 1;
            if (p != 0) {
                int p2 = random.nextInt(i + 1);
                long t = a[from + i];
                a[from + i] = a[from + p2];
                a[from + p2] = t;
                p = i;
            } else {
                return a;
            }
        }
    }

    public static long[] shuffle(long[] a, Random random) {
        int p = a.length;
        while (true) {
            int i = p - 1;
            if (p != 0) {
                int p2 = random.nextInt(i + 1);
                long t = a[i];
                a[i] = a[p2];
                a[p2] = t;
                p = i;
            } else {
                return a;
            }
        }
    }

    public static long[] reverse(long[] a) {
        int length = a.length;
        int i = length / 2;
        while (true) {
            int i2 = i - 1;
            if (i != 0) {
                long t = a[(length - i2) - 1];
                a[(length - i2) - 1] = a[i2];
                a[i2] = t;
                i = i2;
            } else {
                return a;
            }
        }
    }

    public static long[] reverse(long[] a, int from, int to) {
        int length = to - from;
        int i = length / 2;
        while (true) {
            int i2 = i - 1;
            if (i != 0) {
                long t = a[((from + length) - i2) - 1];
                a[((from + length) - i2) - 1] = a[from + i2];
                a[from + i2] = t;
                i = i2;
            } else {
                return a;
            }
        }
    }

    /* loaded from: classes4.dex */
    private static final class ArrayHashStrategy implements Hash.Strategy<long[]>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;

        private ArrayHashStrategy() {
        }

        @Override // it.unimi.dsi.fastutil.Hash.Strategy
        public int hashCode(long[] o) {
            return java.util.Arrays.hashCode(o);
        }

        @Override // it.unimi.dsi.fastutil.Hash.Strategy
        public boolean equals(long[] a, long[] b) {
            return java.util.Arrays.equals(a, b);
        }
    }
}
