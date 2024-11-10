package it.unimi.dsi.fastutil.floats;

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
public final class FloatArrays {
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
    public static final float[] EMPTY_ARRAY = new float[0];
    public static final float[] DEFAULT_EMPTY_ARRAY = new float[0];
    protected static final Segment POISON_PILL = new Segment(-1, -1, -1);
    public static final Hash.Strategy<float[]> HASH_STRATEGY = new ArrayHashStrategy();

    private FloatArrays() {
    }

    public static float[] forceCapacity(float[] array, int length, int preserve) {
        float[] t = new float[length];
        System.arraycopy(array, 0, t, 0, preserve);
        return t;
    }

    public static float[] ensureCapacity(float[] array, int length) {
        return ensureCapacity(array, length, array.length);
    }

    public static float[] ensureCapacity(float[] array, int length, int preserve) {
        return length > array.length ? forceCapacity(array, length, preserve) : array;
    }

    public static float[] grow(float[] array, int length) {
        return grow(array, length, array.length);
    }

    public static float[] grow(float[] array, int length, int preserve) {
        if (length > array.length) {
            int newLength = (int) Math.max(Math.min(array.length + (array.length >> 1), 2147483639L), length);
            float[] t = new float[newLength];
            System.arraycopy(array, 0, t, 0, preserve);
            return t;
        }
        return array;
    }

    public static float[] trim(float[] array, int length) {
        if (length >= array.length) {
            return array;
        }
        float[] t = length == 0 ? EMPTY_ARRAY : new float[length];
        System.arraycopy(array, 0, t, 0, length);
        return t;
    }

    public static float[] setLength(float[] array, int length) {
        return length == array.length ? array : length < array.length ? trim(array, length) : ensureCapacity(array, length);
    }

    public static float[] copy(float[] array, int offset, int length) {
        ensureOffsetLength(array, offset, length);
        float[] a = length == 0 ? EMPTY_ARRAY : new float[length];
        System.arraycopy(array, offset, a, 0, length);
        return a;
    }

    public static float[] copy(float[] array) {
        return (float[]) array.clone();
    }

    @Deprecated
    public static void fill(float[] array, float value) {
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
    public static void fill(float[] array, int from, int to, float value) {
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
    public static boolean equals(float[] a1, float[] a2) {
        int i = a1.length;
        if (i != a2.length) {
            return false;
        }
        while (true) {
            int i2 = i - 1;
            if (i == 0) {
                return true;
            }
            if (Float.floatToIntBits(a1[i2]) != Float.floatToIntBits(a2[i2])) {
                return false;
            }
            i = i2;
        }
    }

    public static void ensureFromTo(float[] a, int from, int to) {
        Arrays.ensureFromTo(a.length, from, to);
    }

    public static void ensureOffsetLength(float[] a, int offset, int length) {
        Arrays.ensureOffsetLength(a.length, offset, length);
    }

    public static void ensureSameLength(float[] a, float[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException("Array size mismatch: " + a.length + " != " + b.length);
        }
    }

    private static ForkJoinPool getPool() {
        ForkJoinPool current = ForkJoinTask.getPool();
        return current == null ? ForkJoinPool.commonPool() : current;
    }

    public static void swap(float[] x, int a, int b) {
        float t = x[a];
        x[a] = x[b];
        x[b] = t;
    }

    public static void swap(float[] x, int a, int b, int n) {
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
    public static int med3(float[] r4, int r5, int r6, int r7, it.unimi.dsi.fastutil.floats.FloatComparator r8) {
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
        throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.floats.FloatArrays.med3(float[], int, int, int, it.unimi.dsi.fastutil.floats.FloatComparator):int");
    }

    private static void selectionSort(float[] a, int from, int to, FloatComparator comp) {
        for (int i = from; i < to - 1; i++) {
            int m = i;
            for (int j = i + 1; j < to; j++) {
                if (comp.compare(a[j], a[m]) < 0) {
                    m = j;
                }
            }
            if (m != i) {
                float u = a[i];
                a[i] = a[m];
                a[m] = u;
            }
        }
    }

    private static void insertionSort(float[] a, int from, int to, FloatComparator comp) {
        int i = from;
        while (true) {
            i++;
            if (i < to) {
                float t = a[i];
                int j = i;
                float u = a[j - 1];
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

    public static void quickSort(float[] x, int from, int to, FloatComparator comp) {
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
        float v = x[med3(x, l, m, n, comp)];
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

    public static void quickSort(float[] x, FloatComparator comp) {
        quickSort(x, 0, x.length, comp);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public static class ForkJoinQuickSortComp extends RecursiveAction {
        private static final long serialVersionUID = 1;
        private final FloatComparator comp;
        private final int from;
        private final int to;
        private final float[] x;

        public ForkJoinQuickSortComp(float[] x, int from, int to, FloatComparator comp) {
            this.from = from;
            this.to = to;
            this.x = x;
            this.comp = comp;
        }

        @Override // java.util.concurrent.RecursiveAction
        protected void compute() {
            float[] x = this.x;
            int b = this.to - this.from;
            if (b < 8192) {
                FloatArrays.quickSort(x, this.from, this.to, this.comp);
                return;
            }
            int m = this.from + (b / 2);
            int l = this.from;
            int n = this.to - 1;
            int s = b / 8;
            int c = FloatArrays.med3(x, FloatArrays.med3(x, l, l + s, (s * 2) + l, this.comp), FloatArrays.med3(x, m - s, m, m + s, this.comp), FloatArrays.med3(x, n - (s * 2), n - s, n, this.comp), this.comp);
            float v = x[c];
            int a = this.from;
            int b2 = a;
            int c2 = this.to - 1;
            int d = c2;
            while (true) {
                if (b2 <= c2) {
                    int comparison = this.comp.compare(x[b2], v);
                    if (comparison <= 0) {
                        if (comparison == 0) {
                            FloatArrays.swap(x, a, b2);
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
                        FloatArrays.swap(x, c2, d);
                        d--;
                    }
                    c2--;
                }
                if (b2 > c2) {
                    break;
                }
                int len = b;
                int len2 = b2 + 1;
                FloatArrays.swap(x, b2, c2);
                b2 = len2;
                c2--;
                b = len;
                c = c;
            }
            int s2 = Math.min(a - this.from, b2 - a);
            FloatArrays.swap(x, this.from, b2 - s2, s2);
            int s3 = Math.min(d - c2, (this.to - d) - 1);
            FloatArrays.swap(x, b2, this.to - s3, s3);
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

    public static void parallelQuickSort(float[] x, int from, int to, FloatComparator comp) {
        ForkJoinPool pool = getPool();
        if (to - from < 8192 || pool.getParallelism() == 1) {
            quickSort(x, from, to, comp);
        } else {
            pool.invoke(new ForkJoinQuickSortComp(x, from, to, comp));
        }
    }

    public static void parallelQuickSort(float[] x, FloatComparator comp) {
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
    public static int med3(float[] r4, int r5, int r6, int r7) {
        /*
            r0 = r4[r5]
            r1 = r4[r6]
            int r0 = java.lang.Float.compare(r0, r1)
            r1 = r4[r5]
            r2 = r4[r7]
            int r1 = java.lang.Float.compare(r1, r2)
            r2 = r4[r6]
            r3 = r4[r7]
            int r2 = java.lang.Float.compare(r2, r3)
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
        throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.floats.FloatArrays.med3(float[], int, int, int):int");
    }

    private static void selectionSort(float[] a, int from, int to) {
        for (int i = from; i < to - 1; i++) {
            int m = i;
            for (int j = i + 1; j < to; j++) {
                if (Float.compare(a[j], a[m]) < 0) {
                    m = j;
                }
            }
            if (m != i) {
                float u = a[i];
                a[i] = a[m];
                a[m] = u;
            }
        }
    }

    private static void insertionSort(float[] a, int from, int to) {
        int i = from;
        while (true) {
            i++;
            if (i < to) {
                float t = a[i];
                int j = i;
                float u = a[j - 1];
                while (true) {
                    if (Float.compare(t, u) < 0) {
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

    public static void quickSort(float[] x, int from, int to) {
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
        float v = x[med3(x, l, m, n)];
        int a = from;
        int b = a;
        int c = to - 1;
        int d = c;
        while (true) {
            if (b <= c) {
                int comparison = Float.compare(x[b], v);
                if (comparison <= 0) {
                    if (comparison == 0) {
                        swap(x, a, b);
                        a++;
                    }
                    b++;
                }
            }
            while (c >= b) {
                int comparison2 = Float.compare(x[c], v);
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

    public static void quickSort(float[] x) {
        quickSort(x, 0, x.length);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public static class ForkJoinQuickSort extends RecursiveAction {
        private static final long serialVersionUID = 1;
        private final int from;
        private final int to;
        private final float[] x;

        public ForkJoinQuickSort(float[] x, int from, int to) {
            this.from = from;
            this.to = to;
            this.x = x;
        }

        @Override // java.util.concurrent.RecursiveAction
        protected void compute() {
            float[] x = this.x;
            int b = this.to - this.from;
            if (b < 8192) {
                FloatArrays.quickSort(x, this.from, this.to);
                return;
            }
            int m = this.from + (b / 2);
            int l = this.from;
            int n = this.to - 1;
            int s = b / 8;
            int c = FloatArrays.med3(x, FloatArrays.med3(x, l, l + s, (s * 2) + l), FloatArrays.med3(x, m - s, m, m + s), FloatArrays.med3(x, n - (s * 2), n - s, n));
            float v = x[c];
            int a = this.from;
            int b2 = a;
            int c2 = this.to - 1;
            int d = c2;
            while (true) {
                if (b2 <= c2) {
                    int comparison = Float.compare(x[b2], v);
                    if (comparison <= 0) {
                        if (comparison == 0) {
                            FloatArrays.swap(x, a, b2);
                            a++;
                        }
                        b2++;
                    }
                }
                while (c2 >= b2) {
                    int comparison2 = Float.compare(x[c2], v);
                    if (comparison2 < 0) {
                        break;
                    }
                    if (comparison2 == 0) {
                        FloatArrays.swap(x, c2, d);
                        d--;
                    }
                    c2--;
                }
                if (b2 > c2) {
                    break;
                }
                int len = b;
                int len2 = b2 + 1;
                FloatArrays.swap(x, b2, c2);
                b2 = len2;
                c2--;
                b = len;
                c = c;
            }
            int s2 = Math.min(a - this.from, b2 - a);
            FloatArrays.swap(x, this.from, b2 - s2, s2);
            int s3 = Math.min(d - c2, (this.to - d) - 1);
            FloatArrays.swap(x, b2, this.to - s3, s3);
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

    public static void parallelQuickSort(float[] x, int from, int to) {
        ForkJoinPool pool = getPool();
        if (to - from < 8192 || pool.getParallelism() == 1) {
            quickSort(x, from, to);
        } else {
            pool.invoke(new ForkJoinQuickSort(x, from, to));
        }
    }

    public static void parallelQuickSort(float[] x) {
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
    public static int med3Indirect(int[] r7, float[] r8, int r9, int r10, int r11) {
        /*
            r0 = r7[r9]
            r0 = r8[r0]
            r1 = r7[r10]
            r1 = r8[r1]
            r2 = r7[r11]
            r2 = r8[r2]
            int r3 = java.lang.Float.compare(r0, r1)
            int r4 = java.lang.Float.compare(r0, r2)
            int r5 = java.lang.Float.compare(r1, r2)
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
        throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.floats.FloatArrays.med3Indirect(int[], float[], int, int, int):int");
    }

    private static void insertionSortIndirect(int[] perm, float[] a, int from, int to) {
        int i = from;
        while (true) {
            i++;
            if (i < to) {
                int t = perm[i];
                int j = i;
                int u = perm[j - 1];
                while (true) {
                    if (Float.compare(a[t], a[u]) < 0) {
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

    public static void quickSortIndirect(int[] perm, float[] x, int from, int to) {
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
        float v = x[perm[med3Indirect(perm, x, l, m, n)]];
        int a = from;
        int b2 = a;
        int c = to - 1;
        int d = c;
        while (true) {
            if (b2 <= c) {
                int comparison = Float.compare(x[perm[b2]], v);
                if (comparison <= 0) {
                    if (comparison == 0) {
                        IntArrays.swap(perm, a, b2);
                        a++;
                    }
                    b2++;
                }
            }
            while (c >= b2) {
                int comparison2 = Float.compare(x[perm[c]], v);
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

    public static void quickSortIndirect(int[] perm, float[] x) {
        quickSortIndirect(perm, x, 0, x.length);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public static class ForkJoinQuickSortIndirect extends RecursiveAction {
        private static final long serialVersionUID = 1;
        private final int from;
        private final int[] perm;
        private final int to;
        private final float[] x;

        public ForkJoinQuickSortIndirect(int[] perm, float[] x, int from, int to) {
            this.from = from;
            this.to = to;
            this.x = x;
            this.perm = perm;
        }

        @Override // java.util.concurrent.RecursiveAction
        protected void compute() {
            float[] x = this.x;
            int len = this.to - this.from;
            if (len < 8192) {
                FloatArrays.quickSortIndirect(this.perm, x, this.from, this.to);
                return;
            }
            int m = this.from + (len / 2);
            int l = this.from;
            int n = this.to - 1;
            int s = len / 8;
            int c = FloatArrays.med3Indirect(this.perm, x, l, l + s, (s * 2) + l);
            int b = FloatArrays.med3Indirect(this.perm, x, c, FloatArrays.med3Indirect(this.perm, x, m - s, m, m + s), FloatArrays.med3Indirect(this.perm, x, n - (s * 2), n - s, n));
            float v = x[this.perm[b]];
            int a = this.from;
            int b2 = a;
            int c2 = this.to - 1;
            int d = c2;
            while (true) {
                if (b2 <= c2) {
                    int comparison = Float.compare(x[this.perm[b2]], v);
                    if (comparison <= 0) {
                        if (comparison == 0) {
                            IntArrays.swap(this.perm, a, b2);
                            a++;
                        }
                        b2++;
                    }
                }
                while (c2 >= b2) {
                    int comparison2 = Float.compare(x[this.perm[c2]], v);
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

    public static void parallelQuickSortIndirect(int[] perm, float[] x, int from, int to) {
        ForkJoinPool pool = getPool();
        if (to - from < 8192 || pool.getParallelism() == 1) {
            quickSortIndirect(perm, x, from, to);
        } else {
            pool.invoke(new ForkJoinQuickSortIndirect(perm, x, from, to));
        }
    }

    public static void parallelQuickSortIndirect(int[] perm, float[] x) {
        parallelQuickSortIndirect(perm, x, 0, x.length);
    }

    public static void stabilize(int[] perm, float[] x, int from, int to) {
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

    public static void stabilize(int[] perm, float[] x) {
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
    public static int med3(float[] r5, float[] r6, int r7, int r8, int r9) {
        /*
            r0 = r5[r7]
            r1 = r5[r8]
            int r0 = java.lang.Float.compare(r0, r1)
            r1 = r0
            if (r0 != 0) goto L14
            r0 = r6[r7]
            r2 = r6[r8]
            int r0 = java.lang.Float.compare(r0, r2)
            goto L15
        L14:
            r0 = r1
        L15:
            r2 = r5[r7]
            r3 = r5[r9]
            int r2 = java.lang.Float.compare(r2, r3)
            r1 = r2
            if (r2 != 0) goto L29
            r2 = r6[r7]
            r3 = r6[r9]
            int r2 = java.lang.Float.compare(r2, r3)
            goto L2a
        L29:
            r2 = r1
        L2a:
            r3 = r5[r8]
            r4 = r5[r9]
            int r3 = java.lang.Float.compare(r3, r4)
            r1 = r3
            if (r3 != 0) goto L3e
            r3 = r6[r8]
            r4 = r6[r9]
            int r3 = java.lang.Float.compare(r3, r4)
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
        throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.floats.FloatArrays.med3(float[], float[], int, int, int):int");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void swap(float[] x, float[] y, int a, int b) {
        float t = x[a];
        float u = y[a];
        x[a] = x[b];
        y[a] = y[b];
        x[b] = t;
        y[b] = u;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void swap(float[] x, float[] y, int a, int b, int n) {
        int i = 0;
        while (i < n) {
            swap(x, y, a, b);
            i++;
            a++;
            b++;
        }
    }

    private static void selectionSort(float[] a, float[] b, int from, int to) {
        for (int i = from; i < to - 1; i++) {
            int m = i;
            for (int j = i + 1; j < to; j++) {
                int u = Float.compare(a[j], a[m]);
                if (u < 0 || (u == 0 && Float.compare(b[j], b[m]) < 0)) {
                    m = j;
                }
            }
            if (m != i) {
                float t = a[i];
                a[i] = a[m];
                a[m] = t;
                float t2 = b[i];
                b[i] = b[m];
                b[m] = t2;
            }
        }
    }

    public static void quickSort(float[] x, float[] y, int from, int to) {
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
        float v = x[c];
        float w = y[c];
        int a = from;
        int b2 = a;
        int c2 = to - 1;
        int d = c2;
        while (true) {
            if (b2 <= c2) {
                int t = Float.compare(x[b2], v);
                int compare = t == 0 ? Float.compare(y[b2], w) : t;
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
                int t2 = Float.compare(x[c2], v);
                int compare2 = t2 == 0 ? Float.compare(y[c2], w) : t2;
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

    public static void quickSort(float[] x, float[] y) {
        ensureSameLength(x, y);
        quickSort(x, y, 0, x.length);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public static class ForkJoinQuickSort2 extends RecursiveAction {
        private static final long serialVersionUID = 1;
        private final int from;
        private final int to;
        private final float[] x;
        private final float[] y;

        public ForkJoinQuickSort2(float[] x, float[] y, int from, int to) {
            this.from = from;
            this.to = to;
            this.x = x;
            this.y = y;
        }

        @Override // java.util.concurrent.RecursiveAction
        protected void compute() {
            float[] x = this.x;
            float[] y = this.y;
            int b = this.to - this.from;
            if (b < 8192) {
                FloatArrays.quickSort(x, y, this.from, this.to);
                return;
            }
            int m = this.from + (b / 2);
            int l = this.from;
            int n = this.to - 1;
            int s = b / 8;
            int c = FloatArrays.med3(x, y, l, l + s, (s * 2) + l);
            int m2 = FloatArrays.med3(x, y, c, FloatArrays.med3(x, y, m - s, m, m + s), FloatArrays.med3(x, y, n - (s * 2), n - s, n));
            float v = x[m2];
            float w = y[m2];
            int a = this.from;
            int b2 = a;
            int c2 = this.to - 1;
            int d = c2;
            while (true) {
                if (b2 <= c2) {
                    int t = Float.compare(x[b2], v);
                    int compare = t == 0 ? Float.compare(y[b2], w) : t;
                    int comparison = compare;
                    if (compare <= 0) {
                        if (comparison == 0) {
                            FloatArrays.swap(x, y, a, b2);
                            a++;
                        }
                        b2++;
                    }
                }
                while (c2 >= b2) {
                    int t2 = Float.compare(x[c2], v);
                    int compare2 = t2 == 0 ? Float.compare(y[c2], w) : t2;
                    int comparison2 = compare2;
                    if (compare2 < 0) {
                        break;
                    }
                    if (comparison2 == 0) {
                        FloatArrays.swap(x, y, c2, d);
                        d--;
                    }
                    c2--;
                }
                if (b2 > c2) {
                    break;
                }
                int len = b;
                int len2 = b2 + 1;
                FloatArrays.swap(x, y, b2, c2);
                b2 = len2;
                c2--;
                b = len;
                m2 = m2;
                c = c;
            }
            int s2 = Math.min(a - this.from, b2 - a);
            FloatArrays.swap(x, y, this.from, b2 - s2, s2);
            int s3 = Math.min(d - c2, (this.to - d) - 1);
            FloatArrays.swap(x, y, b2, this.to - s3, s3);
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

    public static void parallelQuickSort(float[] x, float[] y, int from, int to) {
        ForkJoinPool pool = getPool();
        if (to - from < 8192 || pool.getParallelism() == 1) {
            quickSort(x, y, from, to);
        } else {
            pool.invoke(new ForkJoinQuickSort2(x, y, from, to));
        }
    }

    public static void parallelQuickSort(float[] x, float[] y) {
        ensureSameLength(x, y);
        parallelQuickSort(x, y, 0, x.length);
    }

    public static void unstableSort(float[] a, int from, int to) {
        if (to - from >= RADIX_SORT_MIN_THRESHOLD) {
            radixSort(a, from, to);
        } else {
            quickSort(a, from, to);
        }
    }

    public static void unstableSort(float[] a) {
        unstableSort(a, 0, a.length);
    }

    public static void unstableSort(float[] a, int from, int to, FloatComparator comp) {
        quickSort(a, from, to, comp);
    }

    public static void unstableSort(float[] a, FloatComparator comp) {
        unstableSort(a, 0, a.length, comp);
    }

    public static void mergeSort(float[] a, int from, int to, float[] supp) {
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
        if (Float.compare(supp[mid - 1], supp[mid]) <= 0) {
            System.arraycopy(supp, from, a, from, len);
            return;
        }
        int p = from;
        int q = mid;
        for (int i = from; i < to; i++) {
            if (q >= to || (p < mid && Float.compare(supp[p], supp[q]) <= 0)) {
                int q2 = p + 1;
                a[i] = supp[p];
                p = q2;
            } else {
                a[i] = supp[q];
                q++;
            }
        }
    }

    public static void mergeSort(float[] a, int from, int to) {
        mergeSort(a, from, to, (float[]) null);
    }

    public static void mergeSort(float[] a) {
        mergeSort(a, 0, a.length);
    }

    public static void mergeSort(float[] a, int from, int to, FloatComparator comp, float[] supp) {
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

    public static void mergeSort(float[] a, int from, int to, FloatComparator comp) {
        mergeSort(a, from, to, comp, null);
    }

    public static void mergeSort(float[] a, FloatComparator comp) {
        mergeSort(a, 0, a.length, comp);
    }

    public static void stableSort(float[] a, int from, int to) {
        mergeSort(a, from, to);
    }

    public static void stableSort(float[] a) {
        stableSort(a, 0, a.length);
    }

    public static void stableSort(float[] a, int from, int to, FloatComparator comp) {
        mergeSort(a, from, to, comp);
    }

    public static void stableSort(float[] a, FloatComparator comp) {
        stableSort(a, 0, a.length, comp);
    }

    public static int binarySearch(float[] a, int from, int to, float key) {
        int to2 = to - 1;
        while (from <= to2) {
            int mid = (from + to2) >>> 1;
            float midVal = a[mid];
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

    public static int binarySearch(float[] a, float key) {
        return binarySearch(a, 0, a.length, key);
    }

    public static int binarySearch(float[] a, int from, int to, float key, FloatComparator c) {
        int to2 = to - 1;
        while (from <= to2) {
            int mid = (from + to2) >>> 1;
            float midVal = a[mid];
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

    public static int binarySearch(float[] a, float key, FloatComparator c) {
        return binarySearch(a, 0, a.length, key, c);
    }

    private static final int fixFloat(float f) {
        int i = Float.floatToIntBits(f);
        return i >= 0 ? i : Integer.MAX_VALUE ^ i;
    }

    public static void radixSort(float[] a) {
        radixSort(a, 0, a.length);
    }

    public static void radixSort(float[] a, int from, int to) {
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
                int fixFloat = ((fixFloat(a[i3]) >>> shift) & 255) ^ signMask;
                count[fixFloat] = count[fixFloat] + 1;
                i2 = i3;
            }
            int lastUsed2 = -1;
            int p = first;
            for (int i4 = 0; i4 < i; i4++) {
                if (count[i4] != 0) {
                    lastUsed2 = i4;
                }
                int i5 = p + count[i4];
                p = i5;
                pos[i4] = i5;
            }
            int i6 = first + length;
            int end2 = i6 - count[lastUsed2];
            int i7 = first;
            while (i7 <= end2) {
                float t = a[i7];
                int maxLevel2 = maxLevel;
                int maxLevel3 = fixFloat(t) >>> shift;
                int c = (maxLevel3 & 255) ^ signMask;
                if (i7 < end2) {
                    while (true) {
                        lastUsed = lastUsed2;
                        int lastUsed3 = pos[c] - 1;
                        pos[c] = lastUsed3;
                        if (lastUsed3 <= i7) {
                            break;
                        }
                        float z = t;
                        t = a[lastUsed3];
                        a[lastUsed3] = z;
                        c = ((fixFloat(t) >>> shift) & 255) ^ signMask;
                        lastUsed2 = lastUsed;
                    }
                    a[i7] = t;
                    d = c;
                } else {
                    lastUsed = lastUsed2;
                    d = c;
                }
                if (level < 3 && count[d] > 1) {
                    if (count[d] < 1024) {
                        quickSort(a, i7, count[d] + i7);
                    } else {
                        offsetStack[stackPos] = i7;
                        lengthStack[stackPos] = count[d];
                        levelStack[stackPos] = level + 1;
                        stackPos++;
                    }
                }
                i7 += count[d];
                count[d] = 0;
                lastUsed2 = lastUsed;
                maxLevel = maxLevel2;
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

    public static void parallelRadixSort(final float[] a, int from, int to) {
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
            executorCompletionService.submit(new Callable() { // from class: it.unimi.dsi.fastutil.floats.FloatArrays$$ExternalSyntheticLambda0
                @Override // java.util.concurrent.Callable
                public final Object call() {
                    return FloatArrays.lambda$parallelRadixSort$0(queueSize, numberOfThreads, queue, a);
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
    public static /* synthetic */ Void lambda$parallelRadixSort$0(AtomicInteger queueSize, int numberOfThreads, LinkedBlockingQueue queue, float[] a) throws Exception {
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
                int fixFloat = ((fixFloat(a[i5]) >>> shift) & 255) ^ signMask;
                count[fixFloat] = count[fixFloat] + 1;
                i4 = i5;
            }
            int lastUsed = -1;
            int p = first;
            for (int i6 = 0; i6 < i; i6++) {
                if (count[i6] != 0) {
                    lastUsed = i6;
                }
                int i7 = p + count[i6];
                p = i7;
                pos[i6] = i7;
            }
            int i8 = first + length;
            int end = i8 - count[lastUsed];
            int i9 = first;
            while (i9 <= end) {
                float t = a[i9];
                int c = ((fixFloat(t) >>> shift) & 255) ^ signMask;
                if (i9 >= end) {
                    d = c;
                } else {
                    while (true) {
                        int d2 = pos[c] - 1;
                        pos[c] = d2;
                        if (d2 <= i9) {
                            break;
                        }
                        float z = t;
                        t = a[d2];
                        a[d2] = z;
                        c = ((fixFloat(t) >>> shift) & 255) ^ signMask;
                    }
                    a[i9] = t;
                    d = c;
                }
                if (level < 3 && count[d] > 1) {
                    if (count[d] < 1024) {
                        quickSort(a, i9, count[d] + i9);
                    } else {
                        queueSize.incrementAndGet();
                        queue.add(new Segment(i9, count[d], level + 1));
                    }
                }
                i9 += count[d];
                count[d] = 0;
            }
            queueSize.decrementAndGet();
            i = 256;
        }
    }

    public static void parallelRadixSort(float[] a) {
        parallelRadixSort(a, 0, a.length);
    }

    public static void radixSortIndirect(int[] perm, float[] a, boolean stable) {
        radixSortIndirect(perm, a, 0, perm.length, stable);
    }

    public static void radixSortIndirect(int[] perm, float[] a, int from, int to, boolean stable) {
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
                int fixFloat = ((fixFloat(a[perm[i3]]) >>> shift) & 255) ^ signMask;
                count[fixFloat] = count[fixFloat] + 1;
                i2 = i3;
            }
            int lastUsed2 = -1;
            int p2 = stable ? 0 : first;
            int i4 = 0;
            while (true) {
                maxLevel = maxLevel2;
                if (i4 >= 256) {
                    break;
                }
                if (count[i4] != 0) {
                    lastUsed2 = i4;
                }
                int i5 = p2 + count[i4];
                p2 = i5;
                pos[i4] = i5;
                i4++;
                maxLevel2 = maxLevel;
            }
            if (stable) {
                int i6 = first + length;
                while (true) {
                    int i7 = i6 - 1;
                    if (i6 == first) {
                        break;
                    }
                    int fixFloat2 = ((fixFloat(a[perm[i7]]) >>> shift) & 255) ^ signMask;
                    int i8 = pos[fixFloat2] - 1;
                    pos[fixFloat2] = i8;
                    support3[i8] = perm[i7];
                    i6 = i7;
                }
                System.arraycopy(support3, 0, perm, first, length);
                int i9 = 0;
                int p3 = first;
                while (i9 <= lastUsed2) {
                    int stackSize3 = stackSize2;
                    if (level < 3) {
                        support2 = support3;
                        if (count[i9] > 1) {
                            if (count[i9] < 1024) {
                                quickSortIndirect(perm, a, p3, count[i9] + p3);
                                if (stable) {
                                    stabilize(perm, a, p3, count[i9] + p3);
                                }
                            } else {
                                offsetStack[stackPos] = p3;
                                lengthStack[stackPos] = count[i9];
                                levelStack[stackPos] = level + 1;
                                stackPos++;
                            }
                        }
                    } else {
                        support2 = support3;
                    }
                    p3 += count[i9];
                    i9++;
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
                int i10 = first;
                int c = -1;
                while (i10 <= end2) {
                    int t = perm[i10];
                    int c2 = fixFloat(a[t]) >>> shift;
                    c = (c2 & 255) ^ signMask;
                    if (i10 >= end2) {
                        end = end2;
                    } else {
                        while (true) {
                            end = end2;
                            int end3 = pos[c] - 1;
                            pos[c] = end3;
                            if (end3 <= i10) {
                                break;
                            }
                            int z = t;
                            t = perm[end3];
                            perm[end3] = z;
                            int z2 = fixFloat(a[t]) >>> shift;
                            c = (z2 & 255) ^ signMask;
                            end2 = end;
                        }
                        perm[i10] = t;
                    }
                    if (level < 3) {
                        lastUsed = lastUsed2;
                        if (count[c] > 1) {
                            if (count[c] < 1024) {
                                quickSortIndirect(perm, a, i10, count[c] + i10);
                                if (stable) {
                                    stabilize(perm, a, i10, count[c] + i10);
                                }
                            } else {
                                offsetStack[stackPos] = i10;
                                lengthStack[stackPos] = count[c];
                                levelStack[stackPos] = level + 1;
                                stackPos++;
                            }
                        }
                    } else {
                        lastUsed = lastUsed2;
                    }
                    i10 += count[c];
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

    public static void parallelRadixSortIndirect(final int[] perm, final float[] a, int from, int to, final boolean stable) {
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
                executorCompletionService.submit(new Callable() { // from class: it.unimi.dsi.fastutil.floats.FloatArrays$$ExternalSyntheticLambda1
                    @Override // java.util.concurrent.Callable
                    public final Object call() {
                        return FloatArrays.lambda$parallelRadixSortIndirect$1(queueSize, numberOfThreads, queue, a, perm, stable, support);
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
    public static /* synthetic */ Void lambda$parallelRadixSortIndirect$1(AtomicInteger queueSize, int numberOfThreads, LinkedBlockingQueue queue, float[] a, int[] perm, boolean stable, int[] support) throws Exception {
        int end;
        Segment segment;
        int p;
        float[] fArr = a;
        int[] iArr = perm;
        boolean z = stable;
        int[] iArr2 = support;
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
                int fixFloat = ((fixFloat(fArr[iArr[i5]]) >>> shift) & 255) ^ signMask;
                count[fixFloat] = count[fixFloat] + 1;
                i4 = i5;
            }
            int lastUsed = -1;
            int p2 = first;
            for (int i6 = 0; i6 < i; i6++) {
                if (count[i6] != 0) {
                    lastUsed = i6;
                }
                int i7 = p2 + count[i6];
                p2 = i7;
                pos[i6] = i7;
            }
            if (z) {
                int i8 = first + length;
                while (true) {
                    int i9 = i8;
                    i8 = i9 - 1;
                    if (i9 == first) {
                        break;
                    }
                    int fixFloat2 = ((fixFloat(fArr[iArr[i8]]) >>> shift) & 255) ^ signMask;
                    int i10 = pos[fixFloat2] - 1;
                    pos[fixFloat2] = i10;
                    iArr2[i10] = iArr[i8];
                }
                System.arraycopy(iArr2, first, iArr, first, length);
                int i11 = 0;
                int p3 = first;
                while (i11 <= lastUsed) {
                    if (level >= 3 || count[i11] <= 1) {
                        segment = segment2;
                        p = p3;
                    } else if (count[i11] < 1024) {
                        p = p3;
                        radixSortIndirect(iArr, fArr, p, count[i11] + p, z);
                        segment = segment2;
                    } else {
                        p = p3;
                        queueSize.incrementAndGet();
                        segment = segment2;
                        queue.add(new Segment(p, count[i11], level + 1));
                    }
                    p3 = p + count[i11];
                    i11++;
                    z = stable;
                    segment2 = segment;
                }
                java.util.Arrays.fill(count, 0);
            } else {
                int end2 = (first + length) - count[lastUsed];
                int i12 = first;
                while (i12 <= end2) {
                    int t = iArr[i12];
                    int c = ((fixFloat(fArr[t]) >>> shift) & 255) ^ signMask;
                    if (i12 >= end2) {
                        end = end2;
                    } else {
                        while (true) {
                            int d = pos[c] - 1;
                            pos[c] = d;
                            if (d <= i12) {
                                break;
                            }
                            int z2 = t;
                            t = iArr[d];
                            iArr[d] = z2;
                            int end3 = end2;
                            int end4 = fixFloat(fArr[t]) >>> shift;
                            c = (end4 & 255) ^ signMask;
                            end2 = end3;
                        }
                        end = end2;
                        iArr[i12] = t;
                    }
                    if (level < 3 && count[c] > 1) {
                        if (count[c] < 1024) {
                            radixSortIndirect(iArr, fArr, i12, count[c] + i12, stable);
                        } else {
                            queueSize.incrementAndGet();
                            queue.add(new Segment(i12, count[c], level + 1));
                        }
                    }
                    i12 += count[c];
                    count[c] = 0;
                    fArr = a;
                    iArr = perm;
                    end2 = end;
                }
            }
            z = stable;
            queueSize.decrementAndGet();
            fArr = a;
            iArr = perm;
            iArr2 = support;
            i = 256;
        }
    }

    public static void parallelRadixSortIndirect(int[] perm, float[] a, boolean stable) {
        parallelRadixSortIndirect(perm, a, 0, a.length, stable);
    }

    public static void radixSort(float[] a, float[] b) {
        ensureSameLength(a, b);
        radixSort(a, b, 0, a.length);
    }

    public static void radixSort(float[] a, float[] b, int from, int to) {
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
            float[] k = level < 4 ? a : b;
            int shift = (3 - (level % 4)) * 8;
            int i = first + length;
            while (true) {
                int i2 = i - 1;
                layers = layers2;
                if (i == first) {
                    break;
                }
                int fixFloat = ((fixFloat(k[i2]) >>> shift) & 255) ^ signMask;
                count[fixFloat] = count[fixFloat] + 1;
                i = i2;
                layers2 = layers;
            }
            int lastUsed = -1;
            int p = first;
            int i3 = 0;
            while (true) {
                maxLevel = maxLevel2;
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
                maxLevel2 = maxLevel;
            }
            int i5 = first + length;
            int end2 = i5 - count[lastUsed];
            int i6 = first;
            while (i6 <= end2) {
                float t = a[i6];
                float u = b[i6];
                int stackSize2 = stackSize;
                int stackSize3 = fixFloat(k[i6]) >>> shift;
                int c = (stackSize3 & 255) ^ signMask;
                if (i6 >= end2) {
                    end = end2;
                    d = c;
                } else {
                    while (true) {
                        end = end2;
                        int end3 = pos[c] - 1;
                        pos[c] = end3;
                        if (end3 <= i6) {
                            break;
                        }
                        c = ((fixFloat(k[end3]) >>> shift) & 255) ^ signMask;
                        float z = t;
                        t = a[end3];
                        a[end3] = z;
                        float z2 = u;
                        u = b[end3];
                        b[end3] = z2;
                        end2 = end;
                    }
                    a[i6] = t;
                    b[i6] = u;
                    d = c;
                }
                if (level < 7 && count[d] > 1) {
                    if (count[d] < 1024) {
                        quickSort(a, b, i6, count[d] + i6);
                    } else {
                        offsetStack[stackPos] = i6;
                        lengthStack[stackPos] = count[d];
                        levelStack[stackPos] = level + 1;
                        stackPos++;
                    }
                }
                i6 += count[d];
                count[d] = 0;
                end2 = end;
                stackSize = stackSize2;
            }
            layers2 = layers;
            maxLevel2 = maxLevel;
            stackSize = stackSize;
        }
    }

    public static void parallelRadixSort(final float[] a, final float[] b, int from, int to) {
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
                executorCompletionService.submit(new Callable() { // from class: it.unimi.dsi.fastutil.floats.FloatArrays$$ExternalSyntheticLambda2
                    @Override // java.util.concurrent.Callable
                    public final Object call() {
                        return FloatArrays.lambda$parallelRadixSort$2(queueSize, numberOfThreads, queue, a, b);
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
    public static /* synthetic */ Void lambda$parallelRadixSort$2(AtomicInteger queueSize, int numberOfThreads, LinkedBlockingQueue queue, float[] a, float[] b) throws Exception {
        Segment segment;
        int d;
        float[] fArr = a;
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
            float[] k = level < 4 ? fArr : b;
            int shift = (3 - (level % 4)) * 8;
            int i4 = first + length;
            while (true) {
                int i5 = i4 - 1;
                if (i4 == first) {
                    break;
                }
                int fixFloat = ((fixFloat(k[i5]) >>> shift) & 255) ^ signMask;
                count[fixFloat] = count[fixFloat] + 1;
                i4 = i5;
            }
            int lastUsed = -1;
            int p = first;
            for (int i6 = 0; i6 < i; i6++) {
                if (count[i6] != 0) {
                    lastUsed = i6;
                }
                int i7 = p + count[i6];
                p = i7;
                pos[i6] = i7;
            }
            int i8 = first + length;
            int end = i8 - count[lastUsed];
            int i9 = first;
            while (i9 <= end) {
                float t = fArr[i9];
                float u = b[i9];
                int c = ((fixFloat(k[i9]) >>> shift) & 255) ^ signMask;
                if (i9 >= end) {
                    segment = segment2;
                    d = c;
                } else {
                    while (true) {
                        segment = segment2;
                        int d2 = pos[c] - 1;
                        pos[c] = d2;
                        if (d2 <= i9) {
                            break;
                        }
                        c = ((fixFloat(k[d2]) >>> shift) & 255) ^ signMask;
                        float z = t;
                        float w = u;
                        t = fArr[d2];
                        u = b[d2];
                        fArr[d2] = z;
                        b[d2] = w;
                        segment2 = segment;
                    }
                    fArr[i9] = t;
                    b[i9] = u;
                    d = c;
                }
                if (level < 7 && count[d] > 1) {
                    if (count[d] < 1024) {
                        quickSort(fArr, b, i9, count[d] + i9);
                    } else {
                        queueSize.incrementAndGet();
                        queue.add(new Segment(i9, count[d], level + 1));
                    }
                }
                i9 += count[d];
                count[d] = 0;
                fArr = a;
                segment2 = segment;
            }
            queueSize.decrementAndGet();
            fArr = a;
            i = 256;
        }
    }

    public static void parallelRadixSort(float[] a, float[] b) {
        ensureSameLength(a, b);
        parallelRadixSort(a, b, 0, a.length);
    }

    private static void insertionSortIndirect(int[] perm, float[] a, float[] b, int from, int to) {
        int i = from;
        while (true) {
            i++;
            if (i < to) {
                int t = perm[i];
                int j = i;
                int u = perm[j - 1];
                while (true) {
                    if (Float.compare(a[t], a[u]) < 0 || (Float.compare(a[t], a[u]) == 0 && Float.compare(b[t], b[u]) < 0)) {
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

    public static void radixSortIndirect(int[] perm, float[] a, float[] b, boolean stable) {
        ensureSameLength(a, b);
        radixSortIndirect(perm, a, b, 0, a.length, stable);
    }

    public static void radixSortIndirect(int[] perm, float[] a, float[] b, int from, int to, boolean stable) {
        int stackSize;
        int stackPos;
        int[] support;
        char c;
        int first;
        float[] k;
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
            float[] k2 = level < 4 ? a : b;
            int shift = (3 - (level % 4)) * 8;
            int maxLevel2 = maxLevel;
            int maxLevel3 = first2 + length;
            while (true) {
                int i2 = maxLevel3 - 1;
                stackSize = stackSize2;
                if (maxLevel3 == first2) {
                    break;
                }
                int fixFloat = ((fixFloat(k2[perm[i2]]) >>> shift) & 255) ^ signMask;
                count[fixFloat] = count[fixFloat] + 1;
                maxLevel3 = i2;
                stackSize2 = stackSize;
            }
            int lastUsed = -1;
            int p2 = stable ? 0 : first2;
            int i3 = 0;
            while (true) {
                stackPos = stackPos2;
                if (i3 >= 256) {
                    break;
                }
                if (count[i3] != 0) {
                    lastUsed = i3;
                }
                int i4 = p2 + count[i3];
                p2 = i4;
                pos[i3] = i4;
                i3++;
                stackPos2 = stackPos;
            }
            if (stable) {
                int i5 = first2 + length;
                while (true) {
                    int i6 = i5 - 1;
                    if (i5 == first2) {
                        break;
                    }
                    int fixFloat2 = ((fixFloat(k2[perm[i6]]) >>> shift) & 255) ^ signMask;
                    int i7 = pos[fixFloat2] - 1;
                    pos[fixFloat2] = i7;
                    support3[i7] = perm[i6];
                    i5 = i6;
                }
                System.arraycopy(support3, 0, perm, first2, length);
                int i8 = 0;
                int p3 = first2;
                while (i8 < 256) {
                    if (level < 7) {
                        support2 = support3;
                        if (count[i8] <= 1) {
                            p = p3;
                        } else if (count[i8] < 64) {
                            p = p3;
                            insertionSortIndirect(perm, a, b, p, count[i8] + p);
                        } else {
                            p = p3;
                            offsetStack[stackPos] = p;
                            lengthStack[stackPos] = count[i8];
                            levelStack[stackPos] = level + 1;
                            stackPos++;
                        }
                    } else {
                        support2 = support3;
                        p = p3;
                    }
                    p3 = p + count[i8];
                    i8++;
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
                int i9 = first2;
                while (i9 <= end) {
                    int t = perm[i9];
                    int first3 = first2;
                    int c3 = ((fixFloat(k2[t]) >>> shift) & 255) ^ signMask;
                    if (i9 >= end) {
                        k = k2;
                        c2 = c3;
                    } else {
                        while (true) {
                            int d = pos[c3] - 1;
                            pos[c3] = d;
                            if (d <= i9) {
                                break;
                            }
                            int z = t;
                            t = perm[d];
                            perm[d] = z;
                            c3 = ((fixFloat(k2[t]) >>> shift) & 255) ^ signMask;
                            k2 = k2;
                        }
                        k = k2;
                        perm[i9] = t;
                        c2 = c3;
                    }
                    if (level < 7 && count[c2] > 1) {
                        if (count[c2] < 64) {
                            insertionSortIndirect(perm, a, b, i9, count[c2] + i9);
                        } else {
                            offsetStack[stackPos] = i9;
                            lengthStack[stackPos] = count[c2];
                            levelStack[stackPos] = level + 1;
                            stackPos++;
                        }
                    }
                    i9 += count[c2];
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

    private static void selectionSort(float[][] a, int from, int to, int level) {
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
                int p2 = layers;
                while (true) {
                    int p3 = p2 - 1;
                    if (p2 != 0) {
                        float u = a[p3][i];
                        a[p3][i] = a[p3][m];
                        a[p3][m] = u;
                        p2 = p3;
                    }
                }
            }
        }
    }

    public static void radixSort(float[][] a) {
        radixSort(a, 0, a[0].length);
    }

    public static void radixSort(float[][] a, int from, int to) {
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
                float[] t = new float[layers];
                while (stackPos > 0) {
                    stackPos--;
                    int first = offsetStack[stackPos];
                    int length = lengthStack[stackPos];
                    int level = levelStack[stackPos];
                    int signMask = level % 4 == 0 ? 128 : 0;
                    float[] k = a[level / 4];
                    int shift = (3 - (level % 4)) * 8;
                    int i = first + length;
                    while (true) {
                        int i2 = i;
                        i = i2 - 1;
                        if (i2 == first) {
                            break;
                        }
                        int fixFloat = ((fixFloat(k[i]) >>> shift) & 255) ^ signMask;
                        count[fixFloat] = count[fixFloat] + 1;
                    }
                    int lastUsed = -1;
                    int p3 = first;
                    for (int i3 = 0; i3 < 256; i3++) {
                        if (count[i3] != 0) {
                            lastUsed = i3;
                        }
                        int i4 = p3 + count[i3];
                        p3 = i4;
                        pos[i3] = i4;
                    }
                    int i5 = first + length;
                    int end2 = i5 - count[lastUsed];
                    int i6 = first;
                    while (i6 <= end2) {
                        int p4 = layers;
                        while (true) {
                            int p5 = p4 - 1;
                            if (p4 == 0) {
                                break;
                            }
                            t[p5] = a[p5][i6];
                            p4 = p5;
                        }
                        int first2 = first;
                        int c = ((fixFloat(k[i6]) >>> shift) & 255) ^ signMask;
                        if (i6 >= end2) {
                            end = end2;
                            d = c;
                        } else {
                            while (true) {
                                end = end2;
                                int end3 = pos[c] - 1;
                                pos[c] = end3;
                                if (end3 <= i6) {
                                    break;
                                }
                                c = ((fixFloat(k[end3]) >>> shift) & 255) ^ signMask;
                                int p6 = layers;
                                while (true) {
                                    int p7 = p6 - 1;
                                    if (p6 != 0) {
                                        float u = t[p7];
                                        t[p7] = a[p7][end3];
                                        a[p7][end3] = u;
                                        p6 = p7;
                                    }
                                }
                                end2 = end;
                            }
                            int p8 = layers;
                            while (true) {
                                int p9 = p8 - 1;
                                if (p8 == 0) {
                                    break;
                                }
                                a[p9][i6] = t[p9];
                                p8 = p9;
                            }
                            d = c;
                        }
                        if (level < maxLevel && count[d] > 1) {
                            if (count[d] < 64) {
                                selectionSort(a, i6, count[d] + i6, level + 1);
                            } else {
                                offsetStack[stackPos] = i6;
                                lengthStack[stackPos] = count[d];
                                levelStack[stackPos] = level + 1;
                                stackPos++;
                            }
                        }
                        int stackPos2 = count[d];
                        i6 += stackPos2;
                        count[d] = 0;
                        end2 = end;
                        first = first2;
                    }
                }
                return;
            }
        }
    }

    public static float[] shuffle(float[] a, int from, int to, Random random) {
        int p = to - from;
        while (true) {
            int i = p - 1;
            if (p != 0) {
                int p2 = random.nextInt(i + 1);
                float t = a[from + i];
                a[from + i] = a[from + p2];
                a[from + p2] = t;
                p = i;
            } else {
                return a;
            }
        }
    }

    public static float[] shuffle(float[] a, Random random) {
        int p = a.length;
        while (true) {
            int i = p - 1;
            if (p != 0) {
                int p2 = random.nextInt(i + 1);
                float t = a[i];
                a[i] = a[p2];
                a[p2] = t;
                p = i;
            } else {
                return a;
            }
        }
    }

    public static float[] reverse(float[] a) {
        int length = a.length;
        int i = length / 2;
        while (true) {
            int i2 = i - 1;
            if (i != 0) {
                float t = a[(length - i2) - 1];
                a[(length - i2) - 1] = a[i2];
                a[i2] = t;
                i = i2;
            } else {
                return a;
            }
        }
    }

    public static float[] reverse(float[] a, int from, int to) {
        int length = to - from;
        int i = length / 2;
        while (true) {
            int i2 = i - 1;
            if (i != 0) {
                float t = a[((from + length) - i2) - 1];
                a[((from + length) - i2) - 1] = a[from + i2];
                a[from + i2] = t;
                i = i2;
            } else {
                return a;
            }
        }
    }

    /* loaded from: classes4.dex */
    private static final class ArrayHashStrategy implements Hash.Strategy<float[]>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;

        private ArrayHashStrategy() {
        }

        @Override // it.unimi.dsi.fastutil.Hash.Strategy
        public int hashCode(float[] o) {
            return java.util.Arrays.hashCode(o);
        }

        @Override // it.unimi.dsi.fastutil.Hash.Strategy
        public boolean equals(float[] a, float[] b) {
            return java.util.Arrays.equals(a, b);
        }
    }
}
