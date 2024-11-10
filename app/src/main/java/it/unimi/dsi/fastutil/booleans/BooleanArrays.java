package it.unimi.dsi.fastutil.booleans;

import it.unimi.dsi.fastutil.Arrays;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.ints.IntArrays;
import java.io.Serializable;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

/* loaded from: classes4.dex */
public final class BooleanArrays {
    private static final int MERGESORT_NO_REC = 16;
    private static final int PARALLEL_QUICKSORT_NO_FORK = 8192;
    private static final int QUICKSORT_MEDIAN_OF_9 = 128;
    private static final int QUICKSORT_NO_REC = 16;
    public static final boolean[] EMPTY_ARRAY = new boolean[0];
    public static final boolean[] DEFAULT_EMPTY_ARRAY = new boolean[0];
    public static final Hash.Strategy<boolean[]> HASH_STRATEGY = new ArrayHashStrategy();

    private BooleanArrays() {
    }

    public static boolean[] forceCapacity(boolean[] array, int length, int preserve) {
        boolean[] t = new boolean[length];
        System.arraycopy(array, 0, t, 0, preserve);
        return t;
    }

    public static boolean[] ensureCapacity(boolean[] array, int length) {
        return ensureCapacity(array, length, array.length);
    }

    public static boolean[] ensureCapacity(boolean[] array, int length, int preserve) {
        return length > array.length ? forceCapacity(array, length, preserve) : array;
    }

    public static boolean[] grow(boolean[] array, int length) {
        return grow(array, length, array.length);
    }

    public static boolean[] grow(boolean[] array, int length, int preserve) {
        if (length > array.length) {
            int newLength = (int) Math.max(Math.min(array.length + (array.length >> 1), 2147483639L), length);
            boolean[] t = new boolean[newLength];
            System.arraycopy(array, 0, t, 0, preserve);
            return t;
        }
        return array;
    }

    public static boolean[] trim(boolean[] array, int length) {
        if (length >= array.length) {
            return array;
        }
        boolean[] t = length == 0 ? EMPTY_ARRAY : new boolean[length];
        System.arraycopy(array, 0, t, 0, length);
        return t;
    }

    public static boolean[] setLength(boolean[] array, int length) {
        return length == array.length ? array : length < array.length ? trim(array, length) : ensureCapacity(array, length);
    }

    public static boolean[] copy(boolean[] array, int offset, int length) {
        ensureOffsetLength(array, offset, length);
        boolean[] a = length == 0 ? EMPTY_ARRAY : new boolean[length];
        System.arraycopy(array, offset, a, 0, length);
        return a;
    }

    public static boolean[] copy(boolean[] array) {
        return (boolean[]) array.clone();
    }

    @Deprecated
    public static void fill(boolean[] array, boolean value) {
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
    public static void fill(boolean[] array, int from, int to, boolean value) {
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
    public static boolean equals(boolean[] a1, boolean[] a2) {
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

    public static void ensureFromTo(boolean[] a, int from, int to) {
        Arrays.ensureFromTo(a.length, from, to);
    }

    public static void ensureOffsetLength(boolean[] a, int offset, int length) {
        Arrays.ensureOffsetLength(a.length, offset, length);
    }

    public static void ensureSameLength(boolean[] a, boolean[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException("Array size mismatch: " + a.length + " != " + b.length);
        }
    }

    private static ForkJoinPool getPool() {
        ForkJoinPool current = ForkJoinTask.getPool();
        return current == null ? ForkJoinPool.commonPool() : current;
    }

    public static void swap(boolean[] x, int a, int b) {
        boolean t = x[a];
        x[a] = x[b];
        x[b] = t;
    }

    public static void swap(boolean[] x, int a, int b, int n) {
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
    public static int med3(boolean[] r4, int r5, int r6, int r7, it.unimi.dsi.fastutil.booleans.BooleanComparator r8) {
        /*
            boolean r0 = r4[r5]
            boolean r1 = r4[r6]
            int r0 = r8.compare(r0, r1)
            boolean r1 = r4[r5]
            boolean r2 = r4[r7]
            int r1 = r8.compare(r1, r2)
            boolean r2 = r4[r6]
            boolean r3 = r4[r7]
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
        throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.booleans.BooleanArrays.med3(boolean[], int, int, int, it.unimi.dsi.fastutil.booleans.BooleanComparator):int");
    }

    private static void selectionSort(boolean[] a, int from, int to, BooleanComparator comp) {
        for (int i = from; i < to - 1; i++) {
            int m = i;
            for (int j = i + 1; j < to; j++) {
                if (comp.compare(a[j], a[m]) < 0) {
                    m = j;
                }
            }
            if (m != i) {
                boolean u = a[i];
                a[i] = a[m];
                a[m] = u;
            }
        }
    }

    private static void insertionSort(boolean[] a, int from, int to, BooleanComparator comp) {
        int i = from;
        while (true) {
            i++;
            if (i < to) {
                boolean t = a[i];
                int j = i;
                boolean u = a[j - 1];
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

    public static void quickSort(boolean[] x, int from, int to, BooleanComparator comp) {
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
        boolean v = x[med3(x, l, m, n, comp)];
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

    public static void quickSort(boolean[] x, BooleanComparator comp) {
        quickSort(x, 0, x.length, comp);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public static class ForkJoinQuickSortComp extends RecursiveAction {
        private static final long serialVersionUID = 1;
        private final BooleanComparator comp;
        private final int from;
        private final int to;
        private final boolean[] x;

        public ForkJoinQuickSortComp(boolean[] x, int from, int to, BooleanComparator comp) {
            this.from = from;
            this.to = to;
            this.x = x;
            this.comp = comp;
        }

        @Override // java.util.concurrent.RecursiveAction
        protected void compute() {
            boolean[] x = this.x;
            int b = this.to - this.from;
            if (b < 8192) {
                BooleanArrays.quickSort(x, this.from, this.to, this.comp);
                return;
            }
            int m = this.from + (b / 2);
            int l = this.from;
            int n = this.to - 1;
            int s = b / 8;
            int c = BooleanArrays.med3(x, BooleanArrays.med3(x, l, l + s, (s * 2) + l, this.comp), BooleanArrays.med3(x, m - s, m, m + s, this.comp), BooleanArrays.med3(x, n - (s * 2), n - s, n, this.comp), this.comp);
            boolean v = x[c];
            int a = this.from;
            int b2 = a;
            int c2 = this.to - 1;
            int d = c2;
            while (true) {
                if (b2 <= c2) {
                    int comparison = this.comp.compare(x[b2], v);
                    if (comparison <= 0) {
                        if (comparison == 0) {
                            BooleanArrays.swap(x, a, b2);
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
                        BooleanArrays.swap(x, c2, d);
                        d--;
                    }
                    c2--;
                }
                if (b2 > c2) {
                    break;
                }
                int len = b;
                int len2 = b2 + 1;
                BooleanArrays.swap(x, b2, c2);
                b2 = len2;
                c2--;
                b = len;
                c = c;
            }
            int s2 = Math.min(a - this.from, b2 - a);
            BooleanArrays.swap(x, this.from, b2 - s2, s2);
            int s3 = Math.min(d - c2, (this.to - d) - 1);
            BooleanArrays.swap(x, b2, this.to - s3, s3);
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

    public static void parallelQuickSort(boolean[] x, int from, int to, BooleanComparator comp) {
        ForkJoinPool pool = getPool();
        if (to - from < 8192 || pool.getParallelism() == 1) {
            quickSort(x, from, to, comp);
        } else {
            pool.invoke(new ForkJoinQuickSortComp(x, from, to, comp));
        }
    }

    public static void parallelQuickSort(boolean[] x, BooleanComparator comp) {
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
    public static int med3(boolean[] r4, int r5, int r6, int r7) {
        /*
            boolean r0 = r4[r5]
            boolean r1 = r4[r6]
            int r0 = java.lang.Boolean.compare(r0, r1)
            boolean r1 = r4[r5]
            boolean r2 = r4[r7]
            int r1 = java.lang.Boolean.compare(r1, r2)
            boolean r2 = r4[r6]
            boolean r3 = r4[r7]
            int r2 = java.lang.Boolean.compare(r2, r3)
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
        throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.booleans.BooleanArrays.med3(boolean[], int, int, int):int");
    }

    private static void selectionSort(boolean[] a, int from, int to) {
        for (int i = from; i < to - 1; i++) {
            int m = i;
            for (int j = i + 1; j < to; j++) {
                if (!a[j] && a[m]) {
                    m = j;
                }
            }
            if (m != i) {
                boolean u = a[i];
                a[i] = a[m];
                a[m] = u;
            }
        }
    }

    private static void insertionSort(boolean[] a, int from, int to) {
        int i = from;
        while (true) {
            i++;
            if (i < to) {
                boolean t = a[i];
                int j = i;
                boolean u = a[j - 1];
                while (true) {
                    if (!t && u) {
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

    public static void quickSort(boolean[] x, int from, int to) {
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
        boolean v = x[med3(x, l, m, n)];
        int a = from;
        int b = a;
        int c = to - 1;
        int d = c;
        while (true) {
            if (b <= c) {
                int comparison = Boolean.compare(x[b], v);
                if (comparison <= 0) {
                    if (comparison == 0) {
                        swap(x, a, b);
                        a++;
                    }
                    b++;
                }
            }
            while (c >= b) {
                int comparison2 = Boolean.compare(x[c], v);
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

    public static void quickSort(boolean[] x) {
        quickSort(x, 0, x.length);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public static class ForkJoinQuickSort extends RecursiveAction {
        private static final long serialVersionUID = 1;
        private final int from;
        private final int to;
        private final boolean[] x;

        public ForkJoinQuickSort(boolean[] x, int from, int to) {
            this.from = from;
            this.to = to;
            this.x = x;
        }

        @Override // java.util.concurrent.RecursiveAction
        protected void compute() {
            boolean[] x = this.x;
            int b = this.to - this.from;
            if (b < 8192) {
                BooleanArrays.quickSort(x, this.from, this.to);
                return;
            }
            int m = this.from + (b / 2);
            int l = this.from;
            int n = this.to - 1;
            int s = b / 8;
            int c = BooleanArrays.med3(x, BooleanArrays.med3(x, l, l + s, (s * 2) + l), BooleanArrays.med3(x, m - s, m, m + s), BooleanArrays.med3(x, n - (s * 2), n - s, n));
            boolean v = x[c];
            int a = this.from;
            int b2 = a;
            int c2 = this.to - 1;
            int d = c2;
            while (true) {
                if (b2 <= c2) {
                    int comparison = Boolean.compare(x[b2], v);
                    if (comparison <= 0) {
                        if (comparison == 0) {
                            BooleanArrays.swap(x, a, b2);
                            a++;
                        }
                        b2++;
                    }
                }
                while (c2 >= b2) {
                    int comparison2 = Boolean.compare(x[c2], v);
                    if (comparison2 < 0) {
                        break;
                    }
                    if (comparison2 == 0) {
                        BooleanArrays.swap(x, c2, d);
                        d--;
                    }
                    c2--;
                }
                if (b2 > c2) {
                    break;
                }
                int len = b;
                int len2 = b2 + 1;
                BooleanArrays.swap(x, b2, c2);
                b2 = len2;
                c2--;
                b = len;
                c = c;
            }
            int s2 = Math.min(a - this.from, b2 - a);
            BooleanArrays.swap(x, this.from, b2 - s2, s2);
            int s3 = Math.min(d - c2, (this.to - d) - 1);
            BooleanArrays.swap(x, b2, this.to - s3, s3);
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

    public static void parallelQuickSort(boolean[] x, int from, int to) {
        ForkJoinPool pool = getPool();
        if (to - from < 8192 || pool.getParallelism() == 1) {
            quickSort(x, from, to);
        } else {
            pool.invoke(new ForkJoinQuickSort(x, from, to));
        }
    }

    public static void parallelQuickSort(boolean[] x) {
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
    public static int med3Indirect(int[] r7, boolean[] r8, int r9, int r10, int r11) {
        /*
            r0 = r7[r9]
            boolean r0 = r8[r0]
            r1 = r7[r10]
            boolean r1 = r8[r1]
            r2 = r7[r11]
            boolean r2 = r8[r2]
            int r3 = java.lang.Boolean.compare(r0, r1)
            int r4 = java.lang.Boolean.compare(r0, r2)
            int r5 = java.lang.Boolean.compare(r1, r2)
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
        throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.booleans.BooleanArrays.med3Indirect(int[], boolean[], int, int, int):int");
    }

    private static void insertionSortIndirect(int[] perm, boolean[] a, int from, int to) {
        int i = from;
        while (true) {
            i++;
            if (i < to) {
                int t = perm[i];
                int j = i;
                int u = perm[j - 1];
                while (true) {
                    if (!a[t] && a[u]) {
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

    public static void quickSortIndirect(int[] perm, boolean[] x, int from, int to) {
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
        boolean v = x[perm[med3Indirect(perm, x, l, m, n)]];
        int a = from;
        int b2 = a;
        int c = to - 1;
        int d = c;
        while (true) {
            if (b2 <= c) {
                int comparison = Boolean.compare(x[perm[b2]], v);
                if (comparison <= 0) {
                    if (comparison == 0) {
                        IntArrays.swap(perm, a, b2);
                        a++;
                    }
                    b2++;
                }
            }
            while (c >= b2) {
                int comparison2 = Boolean.compare(x[perm[c]], v);
                if (comparison2 < 0) {
                    break;
                }
                if (comparison2 == 0) {
                    IntArrays.swap(perm, c, d);
                    d--;
                }
                c--;
            }
            if (b2 > c) {
                break;
            }
            int len = b;
            int len2 = b2 + 1;
            IntArrays.swap(perm, b2, c);
            b2 = len2;
            c--;
            b = len;
        }
        int s2 = Math.min(a - from, b2 - a);
        IntArrays.swap(perm, from, b2 - s2, s2);
        int s3 = Math.min(d - c, (to - d) - 1);
        IntArrays.swap(perm, b2, to - s3, s3);
        int s4 = b2 - a;
        if (s4 > 1) {
            quickSortIndirect(perm, x, from, from + s4);
        }
        int s5 = d - c;
        if (s5 > 1) {
            quickSortIndirect(perm, x, to - s5, to);
        }
    }

    public static void quickSortIndirect(int[] perm, boolean[] x) {
        quickSortIndirect(perm, x, 0, x.length);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public static class ForkJoinQuickSortIndirect extends RecursiveAction {
        private static final long serialVersionUID = 1;
        private final int from;
        private final int[] perm;
        private final int to;
        private final boolean[] x;

        public ForkJoinQuickSortIndirect(int[] perm, boolean[] x, int from, int to) {
            this.from = from;
            this.to = to;
            this.x = x;
            this.perm = perm;
        }

        @Override // java.util.concurrent.RecursiveAction
        protected void compute() {
            boolean[] x = this.x;
            int len = this.to - this.from;
            if (len < 8192) {
                BooleanArrays.quickSortIndirect(this.perm, x, this.from, this.to);
                return;
            }
            int m = this.from + (len / 2);
            int l = this.from;
            int n = this.to - 1;
            int s = len / 8;
            int c = BooleanArrays.med3Indirect(this.perm, x, l, l + s, (s * 2) + l);
            int b = BooleanArrays.med3Indirect(this.perm, x, c, BooleanArrays.med3Indirect(this.perm, x, m - s, m, m + s), BooleanArrays.med3Indirect(this.perm, x, n - (s * 2), n - s, n));
            boolean v = x[this.perm[b]];
            int a = this.from;
            int b2 = a;
            int c2 = this.to - 1;
            int d = c2;
            while (true) {
                if (b2 <= c2) {
                    int comparison = Boolean.compare(x[this.perm[b2]], v);
                    if (comparison <= 0) {
                        if (comparison == 0) {
                            IntArrays.swap(this.perm, a, b2);
                            a++;
                        }
                        b2++;
                    }
                }
                while (c2 >= b2) {
                    int comparison2 = Boolean.compare(x[this.perm[c2]], v);
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

    public static void parallelQuickSortIndirect(int[] perm, boolean[] x, int from, int to) {
        ForkJoinPool pool = getPool();
        if (to - from < 8192 || pool.getParallelism() == 1) {
            quickSortIndirect(perm, x, from, to);
        } else {
            pool.invoke(new ForkJoinQuickSortIndirect(perm, x, from, to));
        }
    }

    public static void parallelQuickSortIndirect(int[] perm, boolean[] x) {
        parallelQuickSortIndirect(perm, x, 0, x.length);
    }

    public static void stabilize(int[] perm, boolean[] x, int from, int to) {
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

    public static void stabilize(int[] perm, boolean[] x) {
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
    public static int med3(boolean[] r5, boolean[] r6, int r7, int r8, int r9) {
        /*
            boolean r0 = r5[r7]
            boolean r1 = r5[r8]
            int r0 = java.lang.Boolean.compare(r0, r1)
            r1 = r0
            if (r0 != 0) goto L14
            boolean r0 = r6[r7]
            boolean r2 = r6[r8]
            int r0 = java.lang.Boolean.compare(r0, r2)
            goto L15
        L14:
            r0 = r1
        L15:
            boolean r2 = r5[r7]
            boolean r3 = r5[r9]
            int r2 = java.lang.Boolean.compare(r2, r3)
            r1 = r2
            if (r2 != 0) goto L29
            boolean r2 = r6[r7]
            boolean r3 = r6[r9]
            int r2 = java.lang.Boolean.compare(r2, r3)
            goto L2a
        L29:
            r2 = r1
        L2a:
            boolean r3 = r5[r8]
            boolean r4 = r5[r9]
            int r3 = java.lang.Boolean.compare(r3, r4)
            r1 = r3
            if (r3 != 0) goto L3e
            boolean r3 = r6[r8]
            boolean r4 = r6[r9]
            int r3 = java.lang.Boolean.compare(r3, r4)
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
        throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.booleans.BooleanArrays.med3(boolean[], boolean[], int, int, int):int");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void swap(boolean[] x, boolean[] y, int a, int b) {
        boolean t = x[a];
        boolean u = y[a];
        x[a] = x[b];
        y[a] = y[b];
        x[b] = t;
        y[b] = u;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void swap(boolean[] x, boolean[] y, int a, int b, int n) {
        int i = 0;
        while (i < n) {
            swap(x, y, a, b);
            i++;
            a++;
            b++;
        }
    }

    private static void selectionSort(boolean[] a, boolean[] b, int from, int to) {
        for (int i = from; i < to - 1; i++) {
            int m = i;
            for (int j = i + 1; j < to; j++) {
                int u = Boolean.compare(a[j], a[m]);
                if (u < 0 || (u == 0 && !b[j] && b[m])) {
                    m = j;
                }
            }
            if (m != i) {
                boolean t = a[i];
                a[i] = a[m];
                a[m] = t;
                boolean t2 = b[i];
                b[i] = b[m];
                b[m] = t2;
            }
        }
    }

    public static void quickSort(boolean[] x, boolean[] y, int from, int to) {
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
        boolean v = x[c];
        boolean w = y[c];
        int a = from;
        int b2 = a;
        int c2 = to - 1;
        int d = c2;
        while (true) {
            if (b2 <= c2) {
                int t = Boolean.compare(x[b2], v);
                int compare = t == 0 ? Boolean.compare(y[b2], w) : t;
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
                int t2 = Boolean.compare(x[c2], v);
                int compare2 = t2 == 0 ? Boolean.compare(y[c2], w) : t2;
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

    public static void quickSort(boolean[] x, boolean[] y) {
        ensureSameLength(x, y);
        quickSort(x, y, 0, x.length);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public static class ForkJoinQuickSort2 extends RecursiveAction {
        private static final long serialVersionUID = 1;
        private final int from;
        private final int to;
        private final boolean[] x;
        private final boolean[] y;

        public ForkJoinQuickSort2(boolean[] x, boolean[] y, int from, int to) {
            this.from = from;
            this.to = to;
            this.x = x;
            this.y = y;
        }

        @Override // java.util.concurrent.RecursiveAction
        protected void compute() {
            boolean[] x = this.x;
            boolean[] y = this.y;
            int b = this.to - this.from;
            if (b < 8192) {
                BooleanArrays.quickSort(x, y, this.from, this.to);
                return;
            }
            int m = this.from + (b / 2);
            int l = this.from;
            int n = this.to - 1;
            int s = b / 8;
            int c = BooleanArrays.med3(x, y, l, l + s, (s * 2) + l);
            int m2 = BooleanArrays.med3(x, y, c, BooleanArrays.med3(x, y, m - s, m, m + s), BooleanArrays.med3(x, y, n - (s * 2), n - s, n));
            boolean v = x[m2];
            boolean w = y[m2];
            int a = this.from;
            int b2 = a;
            int c2 = this.to - 1;
            int d = c2;
            while (true) {
                if (b2 <= c2) {
                    int t = Boolean.compare(x[b2], v);
                    int compare = t == 0 ? Boolean.compare(y[b2], w) : t;
                    int comparison = compare;
                    if (compare <= 0) {
                        if (comparison == 0) {
                            BooleanArrays.swap(x, y, a, b2);
                            a++;
                        }
                        b2++;
                    }
                }
                while (c2 >= b2) {
                    int t2 = Boolean.compare(x[c2], v);
                    int compare2 = t2 == 0 ? Boolean.compare(y[c2], w) : t2;
                    int comparison2 = compare2;
                    if (compare2 < 0) {
                        break;
                    }
                    if (comparison2 == 0) {
                        BooleanArrays.swap(x, y, c2, d);
                        d--;
                    }
                    c2--;
                }
                if (b2 > c2) {
                    break;
                }
                int len = b;
                int len2 = b2 + 1;
                BooleanArrays.swap(x, y, b2, c2);
                b2 = len2;
                c2--;
                b = len;
                m2 = m2;
                c = c;
            }
            int s2 = Math.min(a - this.from, b2 - a);
            BooleanArrays.swap(x, y, this.from, b2 - s2, s2);
            int s3 = Math.min(d - c2, (this.to - d) - 1);
            BooleanArrays.swap(x, y, b2, this.to - s3, s3);
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

    public static void parallelQuickSort(boolean[] x, boolean[] y, int from, int to) {
        ForkJoinPool pool = getPool();
        if (to - from < 8192 || pool.getParallelism() == 1) {
            quickSort(x, y, from, to);
        } else {
            pool.invoke(new ForkJoinQuickSort2(x, y, from, to));
        }
    }

    public static void parallelQuickSort(boolean[] x, boolean[] y) {
        ensureSameLength(x, y);
        parallelQuickSort(x, y, 0, x.length);
    }

    public static void unstableSort(boolean[] a, int from, int to) {
        quickSort(a, from, to);
    }

    public static void unstableSort(boolean[] a) {
        unstableSort(a, 0, a.length);
    }

    public static void unstableSort(boolean[] a, int from, int to, BooleanComparator comp) {
        quickSort(a, from, to, comp);
    }

    public static void unstableSort(boolean[] a, BooleanComparator comp) {
        unstableSort(a, 0, a.length, comp);
    }

    public static void mergeSort(boolean[] a, int from, int to, boolean[] supp) {
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
        if (!supp[mid - 1] || supp[mid]) {
            System.arraycopy(supp, from, a, from, len);
            return;
        }
        int p = from;
        int q = mid;
        for (int i = from; i < to; i++) {
            if (q >= to || (p < mid && (!supp[p] || supp[q]))) {
                int q2 = p + 1;
                a[i] = supp[p];
                p = q2;
            } else {
                a[i] = supp[q];
                q++;
            }
        }
    }

    public static void mergeSort(boolean[] a, int from, int to) {
        mergeSort(a, from, to, (boolean[]) null);
    }

    public static void mergeSort(boolean[] a) {
        mergeSort(a, 0, a.length);
    }

    public static void mergeSort(boolean[] a, int from, int to, BooleanComparator comp, boolean[] supp) {
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

    public static void mergeSort(boolean[] a, int from, int to, BooleanComparator comp) {
        mergeSort(a, from, to, comp, null);
    }

    public static void mergeSort(boolean[] a, BooleanComparator comp) {
        mergeSort(a, 0, a.length, comp);
    }

    public static void stableSort(boolean[] a, int from, int to) {
        unstableSort(a, from, to);
    }

    public static void stableSort(boolean[] a) {
        stableSort(a, 0, a.length);
    }

    public static void stableSort(boolean[] a, int from, int to, BooleanComparator comp) {
        mergeSort(a, from, to, comp);
    }

    public static void stableSort(boolean[] a, BooleanComparator comp) {
        stableSort(a, 0, a.length, comp);
    }

    public static boolean[] shuffle(boolean[] a, int from, int to, Random random) {
        int p = to - from;
        while (true) {
            int i = p - 1;
            if (p != 0) {
                int p2 = random.nextInt(i + 1);
                boolean t = a[from + i];
                a[from + i] = a[from + p2];
                a[from + p2] = t;
                p = i;
            } else {
                return a;
            }
        }
    }

    public static boolean[] shuffle(boolean[] a, Random random) {
        int p = a.length;
        while (true) {
            int i = p - 1;
            if (p != 0) {
                int p2 = random.nextInt(i + 1);
                boolean t = a[i];
                a[i] = a[p2];
                a[p2] = t;
                p = i;
            } else {
                return a;
            }
        }
    }

    public static boolean[] reverse(boolean[] a) {
        int length = a.length;
        int i = length / 2;
        while (true) {
            int i2 = i - 1;
            if (i != 0) {
                boolean t = a[(length - i2) - 1];
                a[(length - i2) - 1] = a[i2];
                a[i2] = t;
                i = i2;
            } else {
                return a;
            }
        }
    }

    public static boolean[] reverse(boolean[] a, int from, int to) {
        int length = to - from;
        int i = length / 2;
        while (true) {
            int i2 = i - 1;
            if (i != 0) {
                boolean t = a[((from + length) - i2) - 1];
                a[((from + length) - i2) - 1] = a[from + i2];
                a[from + i2] = t;
                i = i2;
            } else {
                return a;
            }
        }
    }

    /* loaded from: classes4.dex */
    private static final class ArrayHashStrategy implements Hash.Strategy<boolean[]>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;

        private ArrayHashStrategy() {
        }

        @Override // it.unimi.dsi.fastutil.Hash.Strategy
        public int hashCode(boolean[] o) {
            return java.util.Arrays.hashCode(o);
        }

        @Override // it.unimi.dsi.fastutil.Hash.Strategy
        public boolean equals(boolean[] a, boolean[] b) {
            return java.util.Arrays.equals(a, b);
        }
    }
}
