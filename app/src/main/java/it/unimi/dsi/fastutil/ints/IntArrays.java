package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Arrays;
import it.unimi.dsi.fastutil.Hash;
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
public final class IntArrays {
    private static final int DIGITS_PER_ELEMENT = 4;
    private static final int DIGIT_BITS = 8;
    private static final int DIGIT_MASK = 255;
    private static final int MERGESORT_NO_REC = 16;
    private static final int PARALLEL_QUICKSORT_NO_FORK = 8192;
    private static final int PARALLEL_RADIXSORT_NO_FORK = 1024;
    private static final int QUICKSORT_MEDIAN_OF_9 = 128;
    private static final int QUICKSORT_NO_REC = 16;
    private static final int RADIXSORT_NO_REC = 1024;
    private static final int RADIXSORT_NO_REC_SMALL = 64;
    static final int RADIX_SORT_MIN_THRESHOLD = 2000;
    public static final int[] EMPTY_ARRAY = new int[0];
    public static final int[] DEFAULT_EMPTY_ARRAY = new int[0];
    protected static final Segment POISON_PILL = new Segment(-1, -1, -1);
    public static final Hash.Strategy<int[]> HASH_STRATEGY = new ArrayHashStrategy();

    private IntArrays() {
    }

    public static int[] forceCapacity(int[] array, int length, int preserve) {
        int[] t = new int[length];
        System.arraycopy(array, 0, t, 0, preserve);
        return t;
    }

    public static int[] ensureCapacity(int[] array, int length) {
        return ensureCapacity(array, length, array.length);
    }

    public static int[] ensureCapacity(int[] array, int length, int preserve) {
        return length > array.length ? forceCapacity(array, length, preserve) : array;
    }

    public static int[] grow(int[] array, int length) {
        return grow(array, length, array.length);
    }

    public static int[] grow(int[] array, int length, int preserve) {
        if (length > array.length) {
            int newLength = (int) Math.max(Math.min(array.length + (array.length >> 1), 2147483639L), length);
            int[] t = new int[newLength];
            System.arraycopy(array, 0, t, 0, preserve);
            return t;
        }
        return array;
    }

    public static int[] trim(int[] array, int length) {
        if (length >= array.length) {
            return array;
        }
        int[] t = length == 0 ? EMPTY_ARRAY : new int[length];
        System.arraycopy(array, 0, t, 0, length);
        return t;
    }

    public static int[] setLength(int[] array, int length) {
        return length == array.length ? array : length < array.length ? trim(array, length) : ensureCapacity(array, length);
    }

    public static int[] copy(int[] array, int offset, int length) {
        ensureOffsetLength(array, offset, length);
        int[] a = length == 0 ? EMPTY_ARRAY : new int[length];
        System.arraycopy(array, offset, a, 0, length);
        return a;
    }

    public static int[] copy(int[] array) {
        return (int[]) array.clone();
    }

    @Deprecated
    public static void fill(int[] array, int value) {
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
    public static void fill(int[] array, int from, int to, int value) {
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
    public static boolean equals(int[] a1, int[] a2) {
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

    public static void ensureFromTo(int[] a, int from, int to) {
        Arrays.ensureFromTo(a.length, from, to);
    }

    public static void ensureOffsetLength(int[] a, int offset, int length) {
        Arrays.ensureOffsetLength(a.length, offset, length);
    }

    public static void ensureSameLength(int[] a, int[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException("Array size mismatch: " + a.length + " != " + b.length);
        }
    }

    private static ForkJoinPool getPool() {
        ForkJoinPool current = ForkJoinTask.getPool();
        return current == null ? ForkJoinPool.commonPool() : current;
    }

    public static void swap(int[] x, int a, int b) {
        int t = x[a];
        x[a] = x[b];
        x[b] = t;
    }

    public static void swap(int[] x, int a, int b, int n) {
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
    /* JADX WARN: Code restructure failed: missing block: B:6:0x0029, code lost:            return r5;     */
    /* JADX WARN: Code restructure failed: missing block: B:9:?, code lost:            return r7;     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int med3(int[] r4, int r5, int r6, int r7, it.unimi.dsi.fastutil.ints.IntComparator r8) {
        /*
            r0 = r4[r5]
            r1 = r4[r6]
            int r0 = r8.compare(r0, r1)
            r1 = r4[r5]
            r2 = r4[r7]
            int r1 = r8.compare(r1, r2)
            r2 = r4[r6]
            r3 = r4[r7]
            int r2 = r8.compare(r2, r3)
            if (r0 >= 0) goto L20
            if (r2 >= 0) goto L1d
            goto L22
        L1d:
            if (r1 >= 0) goto L28
            goto L26
        L20:
            if (r2 <= 0) goto L24
        L22:
            r3 = r6
            goto L29
        L24:
            if (r1 <= 0) goto L28
        L26:
            r3 = r7
            goto L29
        L28:
            r3 = r5
        L29:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.ints.IntArrays.med3(int[], int, int, int, it.unimi.dsi.fastutil.ints.IntComparator):int");
    }

    private static void selectionSort(int[] a, int from, int to, IntComparator comp) {
        for (int i = from; i < to - 1; i++) {
            int m = i;
            for (int j = i + 1; j < to; j++) {
                if (comp.compare(a[j], a[m]) < 0) {
                    m = j;
                }
            }
            if (m != i) {
                int u = a[i];
                a[i] = a[m];
                a[m] = u;
            }
        }
    }

    private static void insertionSort(int[] a, int from, int to, IntComparator comp) {
        int i = from;
        while (true) {
            i++;
            if (i < to) {
                int t = a[i];
                int j = i;
                int u = a[j - 1];
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

    public static void quickSort(int[] x, int from, int to, IntComparator comp) {
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
        int v = x[med3(x, l, m, n, comp)];
        int a = from;
        int b2 = a;
        int c = to - 1;
        int d = c;
        while (true) {
            if (b2 <= c) {
                int comparison = comp.compare(x[b2], v);
                if (comparison <= 0) {
                    if (comparison == 0) {
                        swap(x, a, b2);
                        a++;
                    }
                    b2++;
                }
            }
            while (c >= b2) {
                int comparison2 = comp.compare(x[c], v);
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
            quickSort(x, from, from + s4, comp);
        }
        int s5 = d - c;
        if (s5 > 1) {
            quickSort(x, to - s5, to, comp);
        }
    }

    public static void quickSort(int[] x, IntComparator comp) {
        quickSort(x, 0, x.length, comp);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public static class ForkJoinQuickSortComp extends RecursiveAction {
        private static final long serialVersionUID = 1;
        private final IntComparator comp;
        private final int from;
        private final int to;
        private final int[] x;

        public ForkJoinQuickSortComp(int[] x, int from, int to, IntComparator comp) {
            this.from = from;
            this.to = to;
            this.x = x;
            this.comp = comp;
        }

        @Override // java.util.concurrent.RecursiveAction
        protected void compute() {
            int[] x = this.x;
            int b = this.to - this.from;
            if (b < 8192) {
                IntArrays.quickSort(x, this.from, this.to, this.comp);
                return;
            }
            int m = this.from + (b / 2);
            int l = this.from;
            int n = this.to - 1;
            int s = b / 8;
            int c = IntArrays.med3(x, IntArrays.med3(x, l, l + s, (s * 2) + l, this.comp), IntArrays.med3(x, m - s, m, m + s, this.comp), IntArrays.med3(x, n - (s * 2), n - s, n, this.comp), this.comp);
            int v = x[c];
            int a = this.from;
            int b2 = a;
            int c2 = this.to - 1;
            int d = c2;
            while (true) {
                if (b2 <= c2) {
                    int comparison = this.comp.compare(x[b2], v);
                    if (comparison <= 0) {
                        if (comparison == 0) {
                            IntArrays.swap(x, a, b2);
                            a++;
                        }
                        b2++;
                    }
                }
                while (c2 >= b2) {
                    int comparison2 = this.comp.compare(x[c2], v);
                    if (comparison2 < 0) {
                        break;
                    }
                    if (comparison2 == 0) {
                        IntArrays.swap(x, c2, d);
                        d--;
                    }
                    c2--;
                }
                if (b2 > c2) {
                    break;
                }
                int len = b;
                int len2 = b2 + 1;
                IntArrays.swap(x, b2, c2);
                b2 = len2;
                c2--;
                b = len;
                c = c;
            }
            int s2 = Math.min(a - this.from, b2 - a);
            IntArrays.swap(x, this.from, b2 - s2, s2);
            int s3 = Math.min(d - c2, (this.to - d) - 1);
            IntArrays.swap(x, b2, this.to - s3, s3);
            int s4 = b2 - a;
            int t = d - c2;
            if (s4 > 1 && t > 1) {
                invokeAll(new ForkJoinQuickSortComp(x, this.from, this.from + s4, this.comp), new ForkJoinQuickSortComp(x, this.to - t, this.to, this.comp));
            } else if (s4 > 1) {
                invokeAll(new ForkJoinTask[]{new ForkJoinQuickSortComp(x, this.from, this.from + s4, this.comp)});
            } else {
                invokeAll(new ForkJoinTask[]{new ForkJoinQuickSortComp(x, this.to - t, this.to, this.comp)});
            }
        }
    }

    public static void parallelQuickSort(int[] x, int from, int to, IntComparator comp) {
        ForkJoinPool pool = getPool();
        if (to - from < 8192 || pool.getParallelism() == 1) {
            quickSort(x, from, to, comp);
        } else {
            pool.invoke(new ForkJoinQuickSortComp(x, from, to, comp));
        }
    }

    public static void parallelQuickSort(int[] x, IntComparator comp) {
        parallelQuickSort(x, 0, x.length, comp);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0024, code lost:            if (r1 > 0) goto L11;     */
    /* JADX WARN: Code restructure failed: missing block: B:4:0x001d, code lost:            if (r1 < 0) goto L11;     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x0029, code lost:            return r5;     */
    /* JADX WARN: Code restructure failed: missing block: B:9:?, code lost:            return r7;     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int med3(int[] r4, int r5, int r6, int r7) {
        /*
            r0 = r4[r5]
            r1 = r4[r6]
            int r0 = java.lang.Integer.compare(r0, r1)
            r1 = r4[r5]
            r2 = r4[r7]
            int r1 = java.lang.Integer.compare(r1, r2)
            r2 = r4[r6]
            r3 = r4[r7]
            int r2 = java.lang.Integer.compare(r2, r3)
            if (r0 >= 0) goto L20
            if (r2 >= 0) goto L1d
            goto L22
        L1d:
            if (r1 >= 0) goto L28
            goto L26
        L20:
            if (r2 <= 0) goto L24
        L22:
            r3 = r6
            goto L29
        L24:
            if (r1 <= 0) goto L28
        L26:
            r3 = r7
            goto L29
        L28:
            r3 = r5
        L29:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.ints.IntArrays.med3(int[], int, int, int):int");
    }

    private static void selectionSort(int[] a, int from, int to) {
        for (int i = from; i < to - 1; i++) {
            int m = i;
            for (int j = i + 1; j < to; j++) {
                if (a[j] < a[m]) {
                    m = j;
                }
            }
            if (m != i) {
                int u = a[i];
                a[i] = a[m];
                a[m] = u;
            }
        }
    }

    private static void insertionSort(int[] a, int from, int to) {
        int i = from;
        while (true) {
            i++;
            if (i < to) {
                int t = a[i];
                int j = i;
                int u = a[j - 1];
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

    public static void quickSort(int[] x, int from, int to) {
        int len = to - from;
        if (len < 16) {
            selectionSort(x, from, to);
            return;
        }
        int m = (len / 2) + from;
        int l = from;
        int n = to - 1;
        if (len > 128) {
            int s = len / 8;
            l = med3(x, l, l + s, (s * 2) + l);
            m = med3(x, m - s, m, m + s);
            n = med3(x, n - (s * 2), n - s, n);
        }
        int v = x[med3(x, l, m, n)];
        int a = from;
        int b = a;
        int c = to - 1;
        int d = c;
        while (true) {
            if (b <= c) {
                int comparison = Integer.compare(x[b], v);
                if (comparison <= 0) {
                    if (comparison == 0) {
                        swap(x, a, b);
                        a++;
                    }
                    b++;
                }
            }
            while (c >= b) {
                int comparison2 = Integer.compare(x[c], v);
                if (comparison2 < 0) {
                    break;
                }
                if (comparison2 == 0) {
                    swap(x, c, d);
                    d--;
                }
                c--;
            }
            if (b > c) {
                break;
            }
            swap(x, b, c);
            b++;
            c--;
        }
        int s2 = Math.min(a - from, b - a);
        swap(x, from, b - s2, s2);
        int s3 = Math.min(d - c, (to - d) - 1);
        swap(x, b, to - s3, s3);
        int s4 = b - a;
        if (s4 > 1) {
            quickSort(x, from, from + s4);
        }
        int s5 = d - c;
        if (s5 > 1) {
            quickSort(x, to - s5, to);
        }
    }

    public static void quickSort(int[] x) {
        quickSort(x, 0, x.length);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public static class ForkJoinQuickSort extends RecursiveAction {
        private static final long serialVersionUID = 1;
        private final int from;
        private final int to;
        private final int[] x;

        public ForkJoinQuickSort(int[] x, int from, int to) {
            this.from = from;
            this.to = to;
            this.x = x;
        }

        @Override // java.util.concurrent.RecursiveAction
        protected void compute() {
            int[] x = this.x;
            int b = this.to - this.from;
            if (b < 8192) {
                IntArrays.quickSort(x, this.from, this.to);
                return;
            }
            int m = this.from + (b / 2);
            int l = this.from;
            int n = this.to - 1;
            int s = b / 8;
            int c = IntArrays.med3(x, IntArrays.med3(x, l, l + s, (s * 2) + l), IntArrays.med3(x, m - s, m, m + s), IntArrays.med3(x, n - (s * 2), n - s, n));
            int v = x[c];
            int a = this.from;
            int b2 = a;
            int c2 = this.to - 1;
            int d = c2;
            while (true) {
                if (b2 <= c2) {
                    int comparison = Integer.compare(x[b2], v);
                    if (comparison <= 0) {
                        if (comparison == 0) {
                            IntArrays.swap(x, a, b2);
                            a++;
                        }
                        b2++;
                    }
                }
                while (c2 >= b2) {
                    int comparison2 = Integer.compare(x[c2], v);
                    if (comparison2 < 0) {
                        break;
                    }
                    if (comparison2 == 0) {
                        IntArrays.swap(x, c2, d);
                        d--;
                    }
                    c2--;
                }
                if (b2 > c2) {
                    break;
                }
                int len = b;
                int len2 = b2 + 1;
                IntArrays.swap(x, b2, c2);
                b2 = len2;
                c2--;
                b = len;
                c = c;
            }
            int s2 = Math.min(a - this.from, b2 - a);
            IntArrays.swap(x, this.from, b2 - s2, s2);
            int s3 = Math.min(d - c2, (this.to - d) - 1);
            IntArrays.swap(x, b2, this.to - s3, s3);
            int s4 = b2 - a;
            int t = d - c2;
            if (s4 > 1 && t > 1) {
                ForkJoinQuickSort forkJoinQuickSort = new ForkJoinQuickSort(x, this.from, this.from + s4);
                int i = this.to - t;
                int len3 = this.to;
                invokeAll(forkJoinQuickSort, new ForkJoinQuickSort(x, i, len3));
                return;
            }
            if (s4 > 1) {
                invokeAll(new ForkJoinTask[]{new ForkJoinQuickSort(x, this.from, this.from + s4)});
            } else {
                invokeAll(new ForkJoinTask[]{new ForkJoinQuickSort(x, this.to - t, this.to)});
            }
        }
    }

    public static void parallelQuickSort(int[] x, int from, int to) {
        ForkJoinPool pool = getPool();
        if (to - from < 8192 || pool.getParallelism() == 1) {
            quickSort(x, from, to);
        } else {
            pool.invoke(new ForkJoinQuickSort(x, from, to));
        }
    }

    public static void parallelQuickSort(int[] x) {
        parallelQuickSort(x, 0, x.length);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0024, code lost:            if (r4 > 0) goto L11;     */
    /* JADX WARN: Code restructure failed: missing block: B:4:0x001d, code lost:            if (r4 < 0) goto L11;     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x0029, code lost:            return r9;     */
    /* JADX WARN: Code restructure failed: missing block: B:9:?, code lost:            return r11;     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int med3Indirect(int[] r7, int[] r8, int r9, int r10, int r11) {
        /*
            r0 = r7[r9]
            r0 = r8[r0]
            r1 = r7[r10]
            r1 = r8[r1]
            r2 = r7[r11]
            r2 = r8[r2]
            int r3 = java.lang.Integer.compare(r0, r1)
            int r4 = java.lang.Integer.compare(r0, r2)
            int r5 = java.lang.Integer.compare(r1, r2)
            if (r3 >= 0) goto L20
            if (r5 >= 0) goto L1d
            goto L22
        L1d:
            if (r4 >= 0) goto L28
            goto L26
        L20:
            if (r5 <= 0) goto L24
        L22:
            r6 = r10
            goto L29
        L24:
            if (r4 <= 0) goto L28
        L26:
            r6 = r11
            goto L29
        L28:
            r6 = r9
        L29:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.ints.IntArrays.med3Indirect(int[], int[], int, int, int):int");
    }

    private static void insertionSortIndirect(int[] perm, int[] a, int from, int to) {
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

    public static void quickSortIndirect(int[] perm, int[] x, int from, int to) {
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
        int v = x[perm[med3Indirect(perm, x, l, m, n)]];
        int a = from;
        int b2 = a;
        int c = to - 1;
        int d = c;
        while (true) {
            if (b2 <= c) {
                int comparison = Integer.compare(x[perm[b2]], v);
                if (comparison <= 0) {
                    if (comparison == 0) {
                        swap(perm, a, b2);
                        a++;
                    }
                    b2++;
                }
            }
            while (c >= b2) {
                int comparison2 = Integer.compare(x[perm[c]], v);
                if (comparison2 < 0) {
                    break;
                }
                if (comparison2 == 0) {
                    swap(perm, c, d);
                    d--;
                }
                c--;
            }
            if (b2 > c) {
                break;
            }
            int len = b;
            int len2 = b2 + 1;
            swap(perm, b2, c);
            b2 = len2;
            c--;
            b = len;
        }
        int s2 = Math.min(a - from, b2 - a);
        swap(perm, from, b2 - s2, s2);
        int s3 = Math.min(d - c, (to - d) - 1);
        swap(perm, b2, to - s3, s3);
        int s4 = b2 - a;
        if (s4 > 1) {
            quickSortIndirect(perm, x, from, from + s4);
        }
        int s5 = d - c;
        if (s5 > 1) {
            quickSortIndirect(perm, x, to - s5, to);
        }
    }

    public static void quickSortIndirect(int[] perm, int[] x) {
        quickSortIndirect(perm, x, 0, x.length);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public static class ForkJoinQuickSortIndirect extends RecursiveAction {
        private static final long serialVersionUID = 1;
        private final int from;
        private final int[] perm;
        private final int to;
        private final int[] x;

        public ForkJoinQuickSortIndirect(int[] perm, int[] x, int from, int to) {
            this.from = from;
            this.to = to;
            this.x = x;
            this.perm = perm;
        }

        @Override // java.util.concurrent.RecursiveAction
        protected void compute() {
            int[] x = this.x;
            int len = this.to - this.from;
            if (len < 8192) {
                IntArrays.quickSortIndirect(this.perm, x, this.from, this.to);
                return;
            }
            int m = this.from + (len / 2);
            int l = this.from;
            int n = this.to - 1;
            int s = len / 8;
            int c = IntArrays.med3Indirect(this.perm, x, l, l + s, (s * 2) + l);
            int b = IntArrays.med3Indirect(this.perm, x, c, IntArrays.med3Indirect(this.perm, x, m - s, m, m + s), IntArrays.med3Indirect(this.perm, x, n - (s * 2), n - s, n));
            int v = x[this.perm[b]];
            int a = this.from;
            int b2 = a;
            int c2 = this.to - 1;
            int d = c2;
            while (true) {
                if (b2 <= c2) {
                    int comparison = Integer.compare(x[this.perm[b2]], v);
                    if (comparison <= 0) {
                        if (comparison == 0) {
                            IntArrays.swap(this.perm, a, b2);
                            a++;
                        }
                        b2++;
                    }
                }
                while (c2 >= b2) {
                    int comparison2 = Integer.compare(x[this.perm[c2]], v);
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
                len = len;
                b = b;
                c = c;
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

    public static void parallelQuickSortIndirect(int[] perm, int[] x, int from, int to) {
        ForkJoinPool pool = getPool();
        if (to - from < 8192 || pool.getParallelism() == 1) {
            quickSortIndirect(perm, x, from, to);
        } else {
            pool.invoke(new ForkJoinQuickSortIndirect(perm, x, from, to));
        }
    }

    public static void parallelQuickSortIndirect(int[] perm, int[] x) {
        parallelQuickSortIndirect(perm, x, 0, x.length);
    }

    public static void stabilize(int[] perm, int[] x, int from, int to) {
        int curr = from;
        for (int i = from + 1; i < to; i++) {
            if (x[perm[i]] != x[perm[curr]]) {
                if (i - curr > 1) {
                    parallelQuickSort(perm, curr, i);
                }
                curr = i;
            }
        }
        int i2 = to - curr;
        if (i2 > 1) {
            parallelQuickSort(perm, curr, to);
        }
    }

    public static void stabilize(int[] perm, int[] x) {
        stabilize(perm, x, 0, perm.length);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0044, code lost:            if (r2 < 0) goto L22;     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0050, code lost:            return r7;     */
    /* JADX WARN: Code restructure failed: missing block: B:17:?, code lost:            return r9;     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x004b, code lost:            if (r2 > 0) goto L22;     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int med3(int[] r5, int[] r6, int r7, int r8, int r9) {
        /*
            r0 = r5[r7]
            r1 = r5[r8]
            int r0 = java.lang.Integer.compare(r0, r1)
            r1 = r0
            if (r0 != 0) goto L14
            r0 = r6[r7]
            r2 = r6[r8]
            int r0 = java.lang.Integer.compare(r0, r2)
            goto L15
        L14:
            r0 = r1
        L15:
            r2 = r5[r7]
            r3 = r5[r9]
            int r2 = java.lang.Integer.compare(r2, r3)
            r1 = r2
            if (r2 != 0) goto L29
            r2 = r6[r7]
            r3 = r6[r9]
            int r2 = java.lang.Integer.compare(r2, r3)
            goto L2a
        L29:
            r2 = r1
        L2a:
            r3 = r5[r8]
            r4 = r5[r9]
            int r3 = java.lang.Integer.compare(r3, r4)
            r1 = r3
            if (r3 != 0) goto L3e
            r3 = r6[r8]
            r4 = r6[r9]
            int r3 = java.lang.Integer.compare(r3, r4)
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
            r4 = r8
            goto L50
        L4b:
            if (r2 <= 0) goto L4f
        L4d:
            r4 = r9
            goto L50
        L4f:
            r4 = r7
        L50:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.ints.IntArrays.med3(int[], int[], int, int, int):int");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void swap(int[] x, int[] y, int a, int b) {
        int t = x[a];
        int u = y[a];
        x[a] = x[b];
        y[a] = y[b];
        x[b] = t;
        y[b] = u;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void swap(int[] x, int[] y, int a, int b, int n) {
        int i = 0;
        while (i < n) {
            swap(x, y, a, b);
            i++;
            a++;
            b++;
        }
    }

    private static void selectionSort(int[] a, int[] b, int from, int to) {
        for (int i = from; i < to - 1; i++) {
            int m = i;
            for (int j = i + 1; j < to; j++) {
                int u = Integer.compare(a[j], a[m]);
                if (u < 0 || (u == 0 && b[j] < b[m])) {
                    m = j;
                }
            }
            if (m != i) {
                int t = a[i];
                a[i] = a[m];
                a[m] = t;
                int t2 = b[i];
                b[i] = b[m];
                b[m] = t2;
            }
        }
    }

    public static void quickSort(int[] x, int[] y, int from, int to) {
        int b = to - from;
        if (b < 16) {
            selectionSort(x, y, from, to);
            return;
        }
        int m = (b / 2) + from;
        int l = from;
        int n = to - 1;
        if (b > 128) {
            int s = b / 8;
            l = med3(x, y, l, l + s, (s * 2) + l);
            m = med3(x, y, m - s, m, m + s);
            n = med3(x, y, n - (s * 2), n - s, n);
        }
        int c = med3(x, y, l, m, n);
        int v = x[c];
        int w = y[c];
        int a = from;
        int b2 = a;
        int c2 = to - 1;
        int d = c2;
        while (true) {
            if (b2 <= c2) {
                int t = Integer.compare(x[b2], v);
                int compare = t == 0 ? Integer.compare(y[b2], w) : t;
                int comparison = compare;
                if (compare <= 0) {
                    if (comparison == 0) {
                        swap(x, y, a, b2);
                        a++;
                    }
                    b2++;
                }
            }
            while (c2 >= b2) {
                int t2 = Integer.compare(x[c2], v);
                int compare2 = t2 == 0 ? Integer.compare(y[c2], w) : t2;
                int comparison2 = compare2;
                if (compare2 < 0) {
                    break;
                }
                if (comparison2 == 0) {
                    swap(x, y, c2, d);
                    d--;
                }
                c2--;
            }
            if (b2 > c2) {
                break;
            }
            int len = b;
            int len2 = b2 + 1;
            swap(x, y, b2, c2);
            b2 = len2;
            c2--;
            b = len;
            c = c;
        }
        int s2 = Math.min(a - from, b2 - a);
        swap(x, y, from, b2 - s2, s2);
        int s3 = Math.min(d - c2, (to - d) - 1);
        swap(x, y, b2, to - s3, s3);
        int s4 = b2 - a;
        if (s4 > 1) {
            quickSort(x, y, from, from + s4);
        }
        int s5 = d - c2;
        if (s5 > 1) {
            quickSort(x, y, to - s5, to);
        }
    }

    public static void quickSort(int[] x, int[] y) {
        ensureSameLength(x, y);
        quickSort(x, y, 0, x.length);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public static class ForkJoinQuickSort2 extends RecursiveAction {
        private static final long serialVersionUID = 1;
        private final int from;
        private final int to;
        private final int[] x;
        private final int[] y;

        public ForkJoinQuickSort2(int[] x, int[] y, int from, int to) {
            this.from = from;
            this.to = to;
            this.x = x;
            this.y = y;
        }

        @Override // java.util.concurrent.RecursiveAction
        protected void compute() {
            int[] x = this.x;
            int[] y = this.y;
            int b = this.to - this.from;
            if (b < 8192) {
                IntArrays.quickSort(x, y, this.from, this.to);
                return;
            }
            int m = this.from + (b / 2);
            int l = this.from;
            int n = this.to - 1;
            int s = b / 8;
            int c = IntArrays.med3(x, y, l, l + s, (s * 2) + l);
            int m2 = IntArrays.med3(x, y, c, IntArrays.med3(x, y, m - s, m, m + s), IntArrays.med3(x, y, n - (s * 2), n - s, n));
            int v = x[m2];
            int w = y[m2];
            int a = this.from;
            int b2 = a;
            int c2 = this.to - 1;
            int d = c2;
            while (true) {
                if (b2 <= c2) {
                    int t = Integer.compare(x[b2], v);
                    int compare = t == 0 ? Integer.compare(y[b2], w) : t;
                    int comparison = compare;
                    if (compare <= 0) {
                        if (comparison == 0) {
                            IntArrays.swap(x, y, a, b2);
                            a++;
                        }
                        b2++;
                    }
                }
                while (c2 >= b2) {
                    int t2 = Integer.compare(x[c2], v);
                    int compare2 = t2 == 0 ? Integer.compare(y[c2], w) : t2;
                    int comparison2 = compare2;
                    if (compare2 < 0) {
                        break;
                    }
                    if (comparison2 == 0) {
                        IntArrays.swap(x, y, c2, d);
                        d--;
                    }
                    c2--;
                }
                if (b2 > c2) {
                    break;
                }
                int len = b;
                int len2 = b2 + 1;
                IntArrays.swap(x, y, b2, c2);
                b2 = len2;
                c2--;
                b = len;
                m2 = m2;
                c = c;
            }
            int s2 = Math.min(a - this.from, b2 - a);
            IntArrays.swap(x, y, this.from, b2 - s2, s2);
            int s3 = Math.min(d - c2, (this.to - d) - 1);
            IntArrays.swap(x, y, b2, this.to - s3, s3);
            int s4 = b2 - a;
            int t3 = d - c2;
            if (s4 > 1 && t3 > 1) {
                invokeAll(new ForkJoinQuickSort2(x, y, this.from, this.from + s4), new ForkJoinQuickSort2(x, y, this.to - t3, this.to));
            } else if (s4 > 1) {
                invokeAll(new ForkJoinTask[]{new ForkJoinQuickSort2(x, y, this.from, this.from + s4)});
            } else {
                invokeAll(new ForkJoinTask[]{new ForkJoinQuickSort2(x, y, this.to - t3, this.to)});
            }
        }
    }

    public static void parallelQuickSort(int[] x, int[] y, int from, int to) {
        ForkJoinPool pool = getPool();
        if (to - from < 8192 || pool.getParallelism() == 1) {
            quickSort(x, y, from, to);
        } else {
            pool.invoke(new ForkJoinQuickSort2(x, y, from, to));
        }
    }

    public static void parallelQuickSort(int[] x, int[] y) {
        ensureSameLength(x, y);
        parallelQuickSort(x, y, 0, x.length);
    }

    public static void unstableSort(int[] a, int from, int to) {
        if (to - from >= RADIX_SORT_MIN_THRESHOLD) {
            radixSort(a, from, to);
        } else {
            quickSort(a, from, to);
        }
    }

    public static void unstableSort(int[] a) {
        unstableSort(a, 0, a.length);
    }

    public static void unstableSort(int[] a, int from, int to, IntComparator comp) {
        quickSort(a, from, to, comp);
    }

    public static void unstableSort(int[] a, IntComparator comp) {
        unstableSort(a, 0, a.length, comp);
    }

    public static void mergeSort(int[] a, int from, int to, int[] supp) {
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

    public static void mergeSort(int[] a, int from, int to) {
        mergeSort(a, from, to, (int[]) null);
    }

    public static void mergeSort(int[] a) {
        mergeSort(a, 0, a.length);
    }

    public static void mergeSort(int[] a, int from, int to, IntComparator comp, int[] supp) {
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

    public static void mergeSort(int[] a, int from, int to, IntComparator comp) {
        mergeSort(a, from, to, comp, null);
    }

    public static void mergeSort(int[] a, IntComparator comp) {
        mergeSort(a, 0, a.length, comp);
    }

    public static void stableSort(int[] a, int from, int to) {
        unstableSort(a, from, to);
    }

    public static void stableSort(int[] a) {
        stableSort(a, 0, a.length);
    }

    public static void stableSort(int[] a, int from, int to, IntComparator comp) {
        mergeSort(a, from, to, comp);
    }

    public static void stableSort(int[] a, IntComparator comp) {
        stableSort(a, 0, a.length, comp);
    }

    public static int binarySearch(int[] a, int from, int to, int key) {
        int to2 = to - 1;
        while (from <= to2) {
            int mid = (from + to2) >>> 1;
            int midVal = a[mid];
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

    public static int binarySearch(int[] a, int key) {
        return binarySearch(a, 0, a.length, key);
    }

    public static int binarySearch(int[] a, int from, int to, int key, IntComparator c) {
        int to2 = to - 1;
        while (from <= to2) {
            int mid = (from + to2) >>> 1;
            int midVal = a[mid];
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

    public static int binarySearch(int[] a, int key, IntComparator c) {
        return binarySearch(a, 0, a.length, key, c);
    }

    public static void radixSort(int[] a) {
        radixSort(a, 0, a.length);
    }

    public static void radixSort(int[] a, int from, int to) {
        int lastUsed;
        int d;
        if (to - from < 1024) {
            quickSort(a, from, to);
            return;
        }
        int maxLevel = 3;
        int[] offsetStack = new int[766];
        int[] lengthStack = new int[766];
        int[] levelStack = new int[766];
        offsetStack[0] = from;
        lengthStack[0] = to - from;
        int stackPos = 0 + 1;
        int end = 0;
        levelStack[0] = 0;
        int i = 256;
        int[] count = new int[256];
        int[] pos = new int[256];
        while (stackPos > 0) {
            stackPos--;
            int first = offsetStack[stackPos];
            int length = lengthStack[stackPos];
            int level = levelStack[stackPos];
            int signMask = level % 4 == 0 ? 128 : end;
            int shift = (3 - (level % 4)) * 8;
            int i2 = first + length;
            while (true) {
                int i3 = i2 - 1;
                if (i2 == first) {
                    break;
                }
                int i4 = ((a[i3] >>> shift) & 255) ^ signMask;
                count[i4] = count[i4] + 1;
                i2 = i3;
            }
            int lastUsed2 = -1;
            int p = first;
            for (int i5 = 0; i5 < i; i5++) {
                if (count[i5] != 0) {
                    lastUsed2 = i5;
                }
                int i6 = p + count[i5];
                p = i6;
                pos[i5] = i6;
            }
            int i7 = first + length;
            int end2 = i7 - count[lastUsed2];
            int i8 = first;
            while (i8 <= end2) {
                int t = a[i8];
                int maxLevel2 = maxLevel;
                int maxLevel3 = t >>> shift;
                int c = (maxLevel3 & 255) ^ signMask;
                if (i8 < end2) {
                    while (true) {
                        lastUsed = lastUsed2;
                        int lastUsed3 = pos[c] - 1;
                        pos[c] = lastUsed3;
                        if (lastUsed3 <= i8) {
                            break;
                        }
                        int z = t;
                        t = a[lastUsed3];
                        a[lastUsed3] = z;
                        c = ((t >>> shift) & 255) ^ signMask;
                        lastUsed2 = lastUsed;
                    }
                    a[i8] = t;
                    d = c;
                } else {
                    lastUsed = lastUsed2;
                    d = c;
                }
                if (level < 3 && count[d] > 1) {
                    if (count[d] < 1024) {
                        quickSort(a, i8, count[d] + i8);
                    } else {
                        offsetStack[stackPos] = i8;
                        lengthStack[stackPos] = count[d];
                        levelStack[stackPos] = level + 1;
                        stackPos++;
                    }
                }
                i8 += count[d];
                count[d] = 0;
                maxLevel = maxLevel2;
                lastUsed2 = lastUsed;
            }
            end = 0;
            i = 256;
            maxLevel = maxLevel;
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

    public static void parallelRadixSort(final int[] a, int from, int to) {
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
            executorCompletionService.submit(new Callable() { // from class: it.unimi.dsi.fastutil.ints.IntArrays$$ExternalSyntheticLambda2
                @Override // java.util.concurrent.Callable
                public final Object call() {
                    return IntArrays.lambda$parallelRadixSort$0(queueSize, numberOfThreads, queue, a);
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
    public static /* synthetic */ Void lambda$parallelRadixSort$0(AtomicInteger queueSize, int numberOfThreads, LinkedBlockingQueue queue, int[] a) throws Exception {
        int d;
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
            int signMask = level % 4 == 0 ? 128 : 0;
            int shift = (3 - (level % 4)) * 8;
            int i4 = first + length;
            while (true) {
                int i5 = i4 - 1;
                if (i4 == first) {
                    break;
                }
                int i6 = ((a[i5] >>> shift) & 255) ^ signMask;
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
                int t = a[i10];
                int c = ((t >>> shift) & 255) ^ signMask;
                if (i10 >= end) {
                    d = c;
                } else {
                    while (true) {
                        int d2 = pos[c] - 1;
                        pos[c] = d2;
                        if (d2 <= i10) {
                            break;
                        }
                        int z = t;
                        t = a[d2];
                        a[d2] = z;
                        c = ((t >>> shift) & 255) ^ signMask;
                    }
                    a[i10] = t;
                    d = c;
                }
                if (level < 3 && count[d] > 1) {
                    if (count[d] < 1024) {
                        quickSort(a, i10, count[d] + i10);
                    } else {
                        queueSize.incrementAndGet();
                        queue.add(new Segment(i10, count[d], level + 1));
                    }
                }
                i10 += count[d];
                count[d] = 0;
            }
            queueSize.decrementAndGet();
            i = 256;
        }
    }

    public static void parallelRadixSort(int[] a) {
        parallelRadixSort(a, 0, a.length);
    }

    public static void radixSortIndirect(int[] perm, int[] a, boolean stable) {
        radixSortIndirect(perm, a, 0, perm.length, stable);
    }

    public static void radixSortIndirect(int[] perm, int[] a, int from, int to, boolean stable) {
        int maxLevel;
        int stackSize;
        int[] support;
        int p;
        int end;
        int lastUsed;
        int[] support2;
        if (to - from < 1024) {
            quickSortIndirect(perm, a, from, to);
            if (stable) {
                stabilize(perm, a, from, to);
                return;
            }
            return;
        }
        int maxLevel2 = 3;
        int stackSize2 = 766;
        int[] offsetStack = new int[766];
        int[] lengthStack = new int[766];
        int[] levelStack = new int[766];
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
            int signMask = level % 4 == 0 ? 128 : i;
            int shift = (3 - (level % 4)) * 8;
            int i2 = first + length;
            while (true) {
                int i3 = i2 - 1;
                if (i2 == first) {
                    break;
                }
                int i4 = ((a[perm[i3]] >>> shift) & 255) ^ signMask;
                count[i4] = count[i4] + 1;
                i2 = i3;
            }
            int lastUsed2 = -1;
            int p2 = stable ? 0 : first;
            int i5 = 0;
            while (true) {
                maxLevel = maxLevel2;
                if (i5 >= 256) {
                    break;
                }
                if (count[i5] != 0) {
                    lastUsed2 = i5;
                }
                int i6 = p2 + count[i5];
                p2 = i6;
                pos[i5] = i6;
                i5++;
                maxLevel2 = maxLevel;
            }
            if (stable) {
                int i7 = first + length;
                while (true) {
                    int i8 = i7 - 1;
                    if (i7 == first) {
                        break;
                    }
                    int i9 = ((a[perm[i8]] >>> shift) & 255) ^ signMask;
                    int i10 = pos[i9] - 1;
                    pos[i9] = i10;
                    support3[i10] = perm[i8];
                    i7 = i8;
                }
                System.arraycopy(support3, 0, perm, first, length);
                int i11 = 0;
                int p3 = first;
                while (i11 <= lastUsed2) {
                    int stackSize3 = stackSize2;
                    if (level < 3) {
                        support2 = support3;
                        if (count[i11] > 1) {
                            if (count[i11] < 1024) {
                                quickSortIndirect(perm, a, p3, count[i11] + p3);
                                if (stable) {
                                    stabilize(perm, a, p3, count[i11] + p3);
                                }
                            } else {
                                offsetStack[stackPos] = p3;
                                lengthStack[stackPos] = count[i11];
                                levelStack[stackPos] = level + 1;
                                stackPos++;
                            }
                        }
                    } else {
                        support2 = support3;
                    }
                    p3 += count[i11];
                    i11++;
                    stackSize2 = stackSize3;
                    support3 = support2;
                }
                stackSize = stackSize2;
                support = support3;
                p = 0;
                java.util.Arrays.fill(count, 0);
            } else {
                stackSize = stackSize2;
                support = support3;
                int end2 = (first + length) - count[lastUsed2];
                int i12 = first;
                int c = -1;
                while (i12 <= end2) {
                    int t = perm[i12];
                    int c2 = a[t] >>> shift;
                    c = (c2 & 255) ^ signMask;
                    if (i12 >= end2) {
                        end = end2;
                    } else {
                        while (true) {
                            end = end2;
                            int end3 = pos[c] - 1;
                            pos[c] = end3;
                            if (end3 <= i12) {
                                break;
                            }
                            int z = t;
                            t = perm[end3];
                            perm[end3] = z;
                            int z2 = a[t] >>> shift;
                            c = (z2 & 255) ^ signMask;
                            end2 = end;
                        }
                        perm[i12] = t;
                    }
                    if (level < 3) {
                        lastUsed = lastUsed2;
                        if (count[c] > 1) {
                            if (count[c] < 1024) {
                                quickSortIndirect(perm, a, i12, count[c] + i12);
                                if (stable) {
                                    stabilize(perm, a, i12, count[c] + i12);
                                }
                            } else {
                                offsetStack[stackPos] = i12;
                                lengthStack[stackPos] = count[c];
                                levelStack[stackPos] = level + 1;
                                stackPos++;
                            }
                        }
                    } else {
                        lastUsed = lastUsed2;
                    }
                    i12 += count[c];
                    count[c] = 0;
                    lastUsed2 = lastUsed;
                    end2 = end;
                }
                p = 0;
            }
            stackSize2 = stackSize;
            support3 = support;
            i = p;
            maxLevel2 = maxLevel;
        }
    }

    public static void parallelRadixSortIndirect(final int[] perm, final int[] a, int from, int to, final boolean stable) {
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
                executorCompletionService.submit(new Callable() { // from class: it.unimi.dsi.fastutil.ints.IntArrays$$ExternalSyntheticLambda1
                    @Override // java.util.concurrent.Callable
                    public final Object call() {
                        return IntArrays.lambda$parallelRadixSortIndirect$1(queueSize, numberOfThreads, queue, a, perm, stable, support);
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
    public static /* synthetic */ Void lambda$parallelRadixSortIndirect$1(AtomicInteger queueSize, int numberOfThreads, LinkedBlockingQueue queue, int[] a, int[] perm, boolean stable, int[] support) throws Exception {
        int end;
        Segment segment;
        int p;
        int[] iArr = a;
        int[] iArr2 = perm;
        boolean z = stable;
        int[] iArr3 = support;
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
            Segment segment2 = (Segment) queue.take();
            if (segment2 == POISON_PILL) {
                return null;
            }
            int first = segment2.offset;
            int length = segment2.length;
            int level = segment2.level;
            int signMask = level % 4 == 0 ? 128 : 0;
            int shift = (3 - (level % 4)) * 8;
            int i4 = first + length;
            while (true) {
                int i5 = i4 - 1;
                if (i4 == first) {
                    break;
                }
                int i6 = ((iArr[iArr2[i5]] >>> shift) & 255) ^ signMask;
                count[i6] = count[i6] + 1;
                i4 = i5;
            }
            int lastUsed = -1;
            int p2 = first;
            for (int i7 = 0; i7 < i; i7++) {
                if (count[i7] != 0) {
                    lastUsed = i7;
                }
                int i8 = p2 + count[i7];
                p2 = i8;
                pos[i7] = i8;
            }
            if (z) {
                int i9 = first + length;
                while (true) {
                    int i10 = i9;
                    i9 = i10 - 1;
                    if (i10 == first) {
                        break;
                    }
                    int i11 = ((iArr[iArr2[i9]] >>> shift) & 255) ^ signMask;
                    int i12 = pos[i11] - 1;
                    pos[i11] = i12;
                    iArr3[i12] = iArr2[i9];
                }
                System.arraycopy(iArr3, first, iArr2, first, length);
                int i13 = 0;
                int p3 = first;
                while (i13 <= lastUsed) {
                    if (level >= 3 || count[i13] <= 1) {
                        segment = segment2;
                        p = p3;
                    } else if (count[i13] < 1024) {
                        p = p3;
                        radixSortIndirect(iArr2, iArr, p, count[i13] + p, z);
                        segment = segment2;
                    } else {
                        p = p3;
                        queueSize.incrementAndGet();
                        segment = segment2;
                        queue.add(new Segment(p, count[i13], level + 1));
                    }
                    p3 = p + count[i13];
                    i13++;
                    z = stable;
                    segment2 = segment;
                }
                java.util.Arrays.fill(count, 0);
            } else {
                int end2 = (first + length) - count[lastUsed];
                int i14 = first;
                while (i14 <= end2) {
                    int t = iArr2[i14];
                    int c = ((iArr[t] >>> shift) & 255) ^ signMask;
                    if (i14 >= end2) {
                        end = end2;
                    } else {
                        while (true) {
                            int d = pos[c] - 1;
                            pos[c] = d;
                            if (d <= i14) {
                                break;
                            }
                            int z2 = t;
                            t = iArr2[d];
                            iArr2[d] = z2;
                            int end3 = end2;
                            int end4 = iArr[t] >>> shift;
                            c = (end4 & 255) ^ signMask;
                            end2 = end3;
                        }
                        end = end2;
                        iArr2[i14] = t;
                    }
                    if (level < 3 && count[c] > 1) {
                        if (count[c] < 1024) {
                            radixSortIndirect(iArr2, iArr, i14, count[c] + i14, stable);
                        } else {
                            queueSize.incrementAndGet();
                            queue.add(new Segment(i14, count[c], level + 1));
                        }
                    }
                    i14 += count[c];
                    count[c] = 0;
                    iArr = a;
                    iArr2 = perm;
                    end2 = end;
                }
            }
            z = stable;
            queueSize.decrementAndGet();
            iArr = a;
            iArr2 = perm;
            iArr3 = support;
            i = 256;
        }
    }

    public static void parallelRadixSortIndirect(int[] perm, int[] a, boolean stable) {
        parallelRadixSortIndirect(perm, a, 0, a.length, stable);
    }

    public static void radixSort(int[] a, int[] b) {
        ensureSameLength(a, b);
        radixSort(a, b, 0, a.length);
    }

    public static void radixSort(int[] a, int[] b, int from, int to) {
        int layers;
        int maxLevel;
        int end;
        int d;
        if (to - from < 1024) {
            quickSort(a, b, from, to);
            return;
        }
        int layers2 = 2;
        int maxLevel2 = 7;
        int stackSize = 1786;
        int[] offsetStack = new int[1786];
        int[] lengthStack = new int[1786];
        int[] levelStack = new int[1786];
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
            int signMask = level % 4 == 0 ? 128 : 0;
            int[] k = level < 4 ? a : b;
            int shift = (3 - (level % 4)) * 8;
            int i = first + length;
            while (true) {
                int i2 = i - 1;
                layers = layers2;
                if (i == first) {
                    break;
                }
                int i3 = ((k[i2] >>> shift) & 255) ^ signMask;
                count[i3] = count[i3] + 1;
                i = i2;
                layers2 = layers;
            }
            int lastUsed = -1;
            int p = first;
            int i4 = 0;
            while (true) {
                maxLevel = maxLevel2;
                if (i4 >= 256) {
                    break;
                }
                if (count[i4] != 0) {
                    lastUsed = i4;
                }
                int i5 = p + count[i4];
                p = i5;
                pos[i4] = i5;
                i4++;
                maxLevel2 = maxLevel;
            }
            int i6 = first + length;
            int z = i6 - count[lastUsed];
            int i7 = first;
            while (i7 <= z) {
                int t = a[i7];
                int u = b[i7];
                int stackSize2 = stackSize;
                int stackSize3 = k[i7] >>> shift;
                int c = (stackSize3 & 255) ^ signMask;
                if (i7 >= z) {
                    end = z;
                    d = c;
                } else {
                    while (true) {
                        end = z;
                        int end2 = pos[c] - 1;
                        pos[c] = end2;
                        if (end2 <= i7) {
                            break;
                        }
                        c = ((k[end2] >>> shift) & 255) ^ signMask;
                        int z2 = t;
                        t = a[end2];
                        a[end2] = z2;
                        int z3 = u;
                        u = b[end2];
                        b[end2] = z3;
                        z = end;
                    }
                    a[i7] = t;
                    b[i7] = u;
                    d = c;
                }
                if (level < 7 && count[d] > 1) {
                    if (count[d] < 1024) {
                        quickSort(a, b, i7, count[d] + i7);
                    } else {
                        offsetStack[stackPos] = i7;
                        lengthStack[stackPos] = count[d];
                        levelStack[stackPos] = level + 1;
                        stackPos++;
                    }
                }
                i7 += count[d];
                count[d] = 0;
                z = end;
                stackSize = stackSize2;
            }
            layers2 = layers;
            maxLevel2 = maxLevel;
            stackSize = stackSize;
        }
    }

    public static void parallelRadixSort(final int[] a, final int[] b, int from, int to) {
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
                executorCompletionService.submit(new Callable() { // from class: it.unimi.dsi.fastutil.ints.IntArrays$$ExternalSyntheticLambda0
                    @Override // java.util.concurrent.Callable
                    public final Object call() {
                        return IntArrays.lambda$parallelRadixSort$2(queueSize, numberOfThreads, queue, a, b);
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
    public static /* synthetic */ Void lambda$parallelRadixSort$2(AtomicInteger queueSize, int numberOfThreads, LinkedBlockingQueue queue, int[] a, int[] b) throws Exception {
        Segment segment;
        int d;
        int[] iArr = a;
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
            Segment segment2 = (Segment) queue.take();
            if (segment2 == POISON_PILL) {
                return null;
            }
            int first = segment2.offset;
            int length = segment2.length;
            int level = segment2.level;
            int signMask = level % 4 == 0 ? 128 : 0;
            int[] k = level < 4 ? iArr : b;
            int shift = (3 - (level % 4)) * 8;
            int i4 = first + length;
            while (true) {
                int i5 = i4 - 1;
                if (i4 == first) {
                    break;
                }
                int i6 = ((k[i5] >>> shift) & 255) ^ signMask;
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
                int t = iArr[i10];
                int u = b[i10];
                int c = ((k[i10] >>> shift) & 255) ^ signMask;
                if (i10 >= end) {
                    segment = segment2;
                    d = c;
                } else {
                    while (true) {
                        segment = segment2;
                        int d2 = pos[c] - 1;
                        pos[c] = d2;
                        if (d2 <= i10) {
                            break;
                        }
                        c = ((k[d2] >>> shift) & 255) ^ signMask;
                        int z = t;
                        int w = u;
                        t = iArr[d2];
                        u = b[d2];
                        iArr[d2] = z;
                        b[d2] = w;
                        segment2 = segment;
                    }
                    iArr[i10] = t;
                    b[i10] = u;
                    d = c;
                }
                if (level < 7 && count[d] > 1) {
                    if (count[d] < 1024) {
                        quickSort(iArr, b, i10, count[d] + i10);
                    } else {
                        queueSize.incrementAndGet();
                        queue.add(new Segment(i10, count[d], level + 1));
                    }
                }
                i10 += count[d];
                count[d] = 0;
                iArr = a;
                segment2 = segment;
            }
            queueSize.decrementAndGet();
            iArr = a;
            i = 256;
        }
    }

    public static void parallelRadixSort(int[] a, int[] b) {
        ensureSameLength(a, b);
        parallelRadixSort(a, b, 0, a.length);
    }

    private static void insertionSortIndirect(int[] perm, int[] a, int[] b, int from, int to) {
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

    public static void radixSortIndirect(int[] perm, int[] a, int[] b, boolean stable) {
        ensureSameLength(a, b);
        radixSortIndirect(perm, a, b, 0, a.length, stable);
    }

    public static void radixSortIndirect(int[] perm, int[] a, int[] b, int from, int to, boolean stable) {
        int stackSize;
        int stackPos;
        int[] support;
        char c;
        int first;
        int[] k;
        int c2;
        int[] support2;
        int p;
        if (to - from < 64) {
            insertionSortIndirect(perm, a, b, from, to);
            return;
        }
        int layers = 2;
        int maxLevel = 7;
        int stackSize2 = 1786;
        int[] offsetStack = new int[1786];
        int[] lengthStack = new int[1786];
        int[] levelStack = new int[1786];
        offsetStack[0] = from;
        lengthStack[0] = to - from;
        int i = 0 + 1;
        levelStack[0] = 0;
        int[] count = new int[256];
        int[] pos = new int[256];
        int[] support3 = stable ? new int[perm.length] : null;
        while (i > 0) {
            int stackPos2 = i - 1;
            int first2 = offsetStack[stackPos2];
            int length = lengthStack[stackPos2];
            int level = levelStack[stackPos2];
            int signMask = level % 4 == 0 ? 128 : 0;
            int layers2 = layers;
            int[] k2 = level < 4 ? a : b;
            int shift = (3 - (level % 4)) * 8;
            int maxLevel2 = maxLevel;
            int maxLevel3 = first2 + length;
            while (true) {
                int i2 = maxLevel3 - 1;
                stackSize = stackSize2;
                if (maxLevel3 == first2) {
                    break;
                }
                int i3 = ((k2[perm[i2]] >>> shift) & 255) ^ signMask;
                count[i3] = count[i3] + 1;
                maxLevel3 = i2;
                stackSize2 = stackSize;
            }
            int lastUsed = -1;
            int p2 = stable ? 0 : first2;
            int i4 = 0;
            while (true) {
                stackPos = stackPos2;
                if (i4 >= 256) {
                    break;
                }
                if (count[i4] != 0) {
                    lastUsed = i4;
                }
                int i5 = p2 + count[i4];
                p2 = i5;
                pos[i4] = i5;
                i4++;
                stackPos2 = stackPos;
            }
            if (stable) {
                int i6 = first2 + length;
                while (true) {
                    int i7 = i6 - 1;
                    if (i6 == first2) {
                        break;
                    }
                    int i8 = ((k2[perm[i7]] >>> shift) & 255) ^ signMask;
                    int i9 = pos[i8] - 1;
                    pos[i8] = i9;
                    support3[i9] = perm[i7];
                    i6 = i7;
                }
                System.arraycopy(support3, 0, perm, first2, length);
                int i10 = 0;
                int p3 = first2;
                while (i10 < 256) {
                    if (level < 7) {
                        support2 = support3;
                        if (count[i10] <= 1) {
                            p = p3;
                        } else if (count[i10] < 64) {
                            p = p3;
                            insertionSortIndirect(perm, a, b, p, count[i10] + p);
                        } else {
                            p = p3;
                            offsetStack[stackPos] = p;
                            lengthStack[stackPos] = count[i10];
                            levelStack[stackPos] = level + 1;
                            stackPos++;
                        }
                    } else {
                        support2 = support3;
                        p = p3;
                    }
                    p3 = p + count[i10];
                    i10++;
                    support3 = support2;
                }
                support = support3;
                java.util.Arrays.fill(count, 0);
                first = 0;
                i = stackPos;
                c = '@';
            } else {
                support = support3;
                int end = (first2 + length) - count[lastUsed];
                int i11 = first2;
                while (i11 <= end) {
                    int t = perm[i11];
                    int first3 = first2;
                    int c3 = ((k2[t] >>> shift) & 255) ^ signMask;
                    if (i11 >= end) {
                        k = k2;
                        c2 = c3;
                    } else {
                        while (true) {
                            int d = pos[c3] - 1;
                            pos[c3] = d;
                            if (d <= i11) {
                                break;
                            }
                            int z = t;
                            t = perm[d];
                            perm[d] = z;
                            c3 = ((k2[t] >>> shift) & 255) ^ signMask;
                            k2 = k2;
                        }
                        k = k2;
                        perm[i11] = t;
                        c2 = c3;
                    }
                    if (level < 7 && count[c2] > 1) {
                        if (count[c2] < 64) {
                            insertionSortIndirect(perm, a, b, i11, count[c2] + i11);
                        } else {
                            offsetStack[stackPos] = i11;
                            lengthStack[stackPos] = count[c2];
                            levelStack[stackPos] = level + 1;
                            stackPos++;
                        }
                    }
                    i11 += count[c2];
                    count[c2] = 0;
                    first2 = first3;
                    k2 = k;
                }
                c = '@';
                first = 0;
                i = stackPos;
            }
            maxLevel = maxLevel2;
            stackSize2 = stackSize;
            support3 = support;
            layers = layers2;
        }
    }

    private static void selectionSort(int[][] a, int from, int to, int level) {
        int layers = a.length;
        int firstLayer = level / 4;
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
                int u = layers;
                while (true) {
                    int p2 = u - 1;
                    if (u != 0) {
                        int u2 = a[p2][i];
                        a[p2][i] = a[p2][m];
                        a[p2][m] = u2;
                        u = p2;
                    }
                }
            }
        }
    }

    public static void radixSort(int[][] a) {
        radixSort(a, 0, a[0].length);
    }

    public static void radixSort(int[][] a, int from, int to) {
        int end;
        int d;
        if (to - from < 64) {
            selectionSort(a, from, to, 0);
            return;
        }
        int layers = a.length;
        int maxLevel = (layers * 4) - 1;
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
                int stackSize = (((layers * 4) - 1) * 255) + 1;
                int[] offsetStack = new int[stackSize];
                int[] lengthStack = new int[stackSize];
                int[] levelStack = new int[stackSize];
                offsetStack[0] = from;
                lengthStack[0] = to - from;
                int stackPos = 0 + 1;
                levelStack[0] = 0;
                int[] count = new int[256];
                int[] pos = new int[256];
                int[] t = new int[layers];
                while (stackPos > 0) {
                    stackPos--;
                    int first = offsetStack[stackPos];
                    int length = lengthStack[stackPos];
                    int level = levelStack[stackPos];
                    int signMask = level % 4 == 0 ? 128 : 0;
                    int[] k = a[level / 4];
                    int shift = (3 - (level % 4)) * 8;
                    int i = first + length;
                    while (true) {
                        int i2 = i;
                        i = i2 - 1;
                        if (i2 == first) {
                            break;
                        }
                        int i3 = ((k[i] >>> shift) & 255) ^ signMask;
                        count[i3] = count[i3] + 1;
                    }
                    int lastUsed = -1;
                    int p3 = first;
                    for (int i4 = 0; i4 < 256; i4++) {
                        if (count[i4] != 0) {
                            lastUsed = i4;
                        }
                        int i5 = p3 + count[i4];
                        p3 = i5;
                        pos[i4] = i5;
                    }
                    int i6 = first + length;
                    int end2 = i6 - count[lastUsed];
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
                        int first2 = first;
                        int c = ((k[i7] >>> shift) & 255) ^ signMask;
                        if (i7 >= end2) {
                            end = end2;
                            d = c;
                        } else {
                            while (true) {
                                end = end2;
                                int end3 = pos[c] - 1;
                                pos[c] = end3;
                                if (end3 <= i7) {
                                    break;
                                }
                                c = ((k[end3] >>> shift) & 255) ^ signMask;
                                int u = layers;
                                while (true) {
                                    int p6 = u - 1;
                                    if (u != 0) {
                                        int u2 = t[p6];
                                        t[p6] = a[p6][end3];
                                        a[p6][end3] = u2;
                                        u = p6;
                                    }
                                }
                                end2 = end;
                            }
                            int p7 = layers;
                            while (true) {
                                int p8 = p7 - 1;
                                if (p7 == 0) {
                                    break;
                                }
                                a[p8][i7] = t[p8];
                                p7 = p8;
                            }
                            d = c;
                        }
                        if (level < maxLevel && count[d] > 1) {
                            if (count[d] < 64) {
                                selectionSort(a, i7, count[d] + i7, level + 1);
                            } else {
                                offsetStack[stackPos] = i7;
                                lengthStack[stackPos] = count[d];
                                levelStack[stackPos] = level + 1;
                                stackPos++;
                            }
                        }
                        int stackPos2 = count[d];
                        i7 += stackPos2;
                        count[d] = 0;
                        end2 = end;
                        first = first2;
                    }
                }
                return;
            }
        }
    }

    public static int[] shuffle(int[] a, int from, int to, Random random) {
        int p = to - from;
        while (true) {
            int i = p - 1;
            if (p != 0) {
                int p2 = random.nextInt(i + 1);
                int t = a[from + i];
                a[from + i] = a[from + p2];
                a[from + p2] = t;
                p = i;
            } else {
                return a;
            }
        }
    }

    public static int[] shuffle(int[] a, Random random) {
        int p = a.length;
        while (true) {
            int i = p - 1;
            if (p != 0) {
                int p2 = random.nextInt(i + 1);
                int t = a[i];
                a[i] = a[p2];
                a[p2] = t;
                p = i;
            } else {
                return a;
            }
        }
    }

    public static int[] reverse(int[] a) {
        int length = a.length;
        int t = length / 2;
        while (true) {
            int i = t - 1;
            if (t != 0) {
                int t2 = a[(length - i) - 1];
                a[(length - i) - 1] = a[i];
                a[i] = t2;
                t = i;
            } else {
                return a;
            }
        }
    }

    public static int[] reverse(int[] a, int from, int to) {
        int length = to - from;
        int t = length / 2;
        while (true) {
            int i = t - 1;
            if (t != 0) {
                int t2 = a[((from + length) - i) - 1];
                a[((from + length) - i) - 1] = a[from + i];
                a[from + i] = t2;
                t = i;
            } else {
                return a;
            }
        }
    }

    /* loaded from: classes4.dex */
    private static final class ArrayHashStrategy implements Hash.Strategy<int[]>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;

        private ArrayHashStrategy() {
        }

        @Override // it.unimi.dsi.fastutil.Hash.Strategy
        public int hashCode(int[] o) {
            return java.util.Arrays.hashCode(o);
        }

        @Override // it.unimi.dsi.fastutil.Hash.Strategy
        public boolean equals(int[] a, int[] b) {
            return java.util.Arrays.equals(a, b);
        }
    }
}
