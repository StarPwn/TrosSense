package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Arrays;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.ints.IntArrays;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

/* loaded from: classes4.dex */
public final class ObjectArrays {
    private static final int MERGESORT_NO_REC = 16;
    private static final int PARALLEL_QUICKSORT_NO_FORK = 8192;
    private static final int QUICKSORT_MEDIAN_OF_9 = 128;
    private static final int QUICKSORT_NO_REC = 16;
    public static final Object[] EMPTY_ARRAY = new Object[0];
    public static final Object[] DEFAULT_EMPTY_ARRAY = new Object[0];
    public static final Hash.Strategy HASH_STRATEGY = new ArrayHashStrategy();

    private ObjectArrays() {
    }

    private static <K> K[] newArray(K[] kArr, int i) {
        Class<?> cls = kArr.getClass();
        if (cls == Object[].class) {
            return (K[]) (i == 0 ? EMPTY_ARRAY : new Object[i]);
        }
        return (K[]) ((Object[]) Array.newInstance(cls.getComponentType(), i));
    }

    public static <K> K[] forceCapacity(K[] kArr, int i, int i2) {
        K[] kArr2 = (K[]) newArray(kArr, i);
        System.arraycopy(kArr, 0, kArr2, 0, i2);
        return kArr2;
    }

    public static <K> K[] ensureCapacity(K[] kArr, int i) {
        return (K[]) ensureCapacity(kArr, i, kArr.length);
    }

    public static <K> K[] ensureCapacity(K[] kArr, int i, int i2) {
        return i > kArr.length ? (K[]) forceCapacity(kArr, i, i2) : kArr;
    }

    public static <K> K[] grow(K[] kArr, int i) {
        return (K[]) grow(kArr, i, kArr.length);
    }

    public static <K> K[] grow(K[] kArr, int i, int i2) {
        if (i > kArr.length) {
            K[] kArr2 = (K[]) newArray(kArr, (int) Math.max(Math.min(kArr.length + (kArr.length >> 1), 2147483639L), i));
            System.arraycopy(kArr, 0, kArr2, 0, i2);
            return kArr2;
        }
        return kArr;
    }

    public static <K> K[] trim(K[] kArr, int i) {
        if (i >= kArr.length) {
            return kArr;
        }
        K[] kArr2 = (K[]) newArray(kArr, i);
        System.arraycopy(kArr, 0, kArr2, 0, i);
        return kArr2;
    }

    public static <K> K[] setLength(K[] kArr, int i) {
        return i == kArr.length ? kArr : i < kArr.length ? (K[]) trim(kArr, i) : (K[]) ensureCapacity(kArr, i);
    }

    public static <K> K[] copy(K[] kArr, int i, int i2) {
        ensureOffsetLength(kArr, i, i2);
        K[] kArr2 = (K[]) newArray(kArr, i2);
        System.arraycopy(kArr, i, kArr2, 0, i2);
        return kArr2;
    }

    public static <K> K[] copy(K[] kArr) {
        return (K[]) ((Object[]) kArr.clone());
    }

    @Deprecated
    public static <K> void fill(K[] array, K value) {
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
    public static <K> void fill(K[] array, int from, int to, K value) {
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
    public static <K> boolean equals(K[] a1, K[] a2) {
        int i = a1.length;
        if (i != a2.length) {
            return false;
        }
        while (true) {
            int i2 = i - 1;
            if (i == 0) {
                return true;
            }
            if (!Objects.equals(a1[i2], a2[i2])) {
                return false;
            }
            i = i2;
        }
    }

    public static <K> void ensureFromTo(K[] a, int from, int to) {
        Arrays.ensureFromTo(a.length, from, to);
    }

    public static <K> void ensureOffsetLength(K[] a, int offset, int length) {
        Arrays.ensureOffsetLength(a.length, offset, length);
    }

    public static <K> void ensureSameLength(K[] a, K[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException("Array size mismatch: " + a.length + " != " + b.length);
        }
    }

    private static ForkJoinPool getPool() {
        ForkJoinPool current = ForkJoinTask.getPool();
        return current == null ? ForkJoinPool.commonPool() : current;
    }

    public static <K> void swap(K[] x, int a, int b) {
        K t = x[a];
        x[a] = x[b];
        x[b] = t;
    }

    public static <K> void swap(K[] x, int a, int b, int n) {
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
    public static <K> int med3(K[] r4, int r5, int r6, int r7, java.util.Comparator<K> r8) {
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
        throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.objects.ObjectArrays.med3(java.lang.Object[], int, int, int, java.util.Comparator):int");
    }

    private static <K> void selectionSort(K[] a, int from, int to, Comparator<K> comp) {
        for (int i = from; i < to - 1; i++) {
            int m = i;
            for (int j = i + 1; j < to; j++) {
                if (comp.compare(a[j], a[m]) < 0) {
                    m = j;
                }
            }
            if (m != i) {
                K u = a[i];
                a[i] = a[m];
                a[m] = u;
            }
        }
    }

    private static <K> void insertionSort(K[] a, int from, int to, Comparator<K> comp) {
        int i = from;
        while (true) {
            i++;
            if (i < to) {
                K t = a[i];
                int j = i;
                K u = a[j - 1];
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

    public static <K> void quickSort(K[] x, int from, int to, Comparator<K> comp) {
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
        K v = x[med3(x, l, m, n, comp)];
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

    public static <K> void quickSort(K[] x, Comparator<K> comp) {
        quickSort(x, 0, x.length, comp);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public static class ForkJoinQuickSortComp<K> extends RecursiveAction {
        private static final long serialVersionUID = 1;
        private final Comparator<K> comp;
        private final int from;
        private final int to;
        private final K[] x;

        public ForkJoinQuickSortComp(K[] x, int from, int to, Comparator<K> comp) {
            this.from = from;
            this.to = to;
            this.x = x;
            this.comp = comp;
        }

        @Override // java.util.concurrent.RecursiveAction
        protected void compute() {
            K[] x = this.x;
            int b = this.to - this.from;
            if (b < 8192) {
                ObjectArrays.quickSort(x, this.from, this.to, this.comp);
                return;
            }
            int m = this.from + (b / 2);
            int l = this.from;
            int n = this.to - 1;
            int s = b / 8;
            int c = ObjectArrays.med3(x, ObjectArrays.med3(x, l, l + s, (s * 2) + l, this.comp), ObjectArrays.med3(x, m - s, m, m + s, this.comp), ObjectArrays.med3(x, n - (s * 2), n - s, n, this.comp), this.comp);
            K v = x[c];
            int a = this.from;
            int b2 = a;
            int c2 = this.to - 1;
            int d = c2;
            while (true) {
                if (b2 <= c2) {
                    int comparison = this.comp.compare(x[b2], v);
                    if (comparison <= 0) {
                        if (comparison == 0) {
                            ObjectArrays.swap(x, a, b2);
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
                        ObjectArrays.swap(x, c2, d);
                        d--;
                    }
                    c2--;
                }
                if (b2 > c2) {
                    break;
                }
                int len = b;
                int len2 = b2 + 1;
                ObjectArrays.swap(x, b2, c2);
                b2 = len2;
                c2--;
                b = len;
                c = c;
            }
            int s2 = Math.min(a - this.from, b2 - a);
            ObjectArrays.swap(x, this.from, b2 - s2, s2);
            int s3 = Math.min(d - c2, (this.to - d) - 1);
            ObjectArrays.swap(x, b2, this.to - s3, s3);
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

    public static <K> void parallelQuickSort(K[] x, int from, int to, Comparator<K> comp) {
        ForkJoinPool pool = getPool();
        if (to - from < 8192 || pool.getParallelism() == 1) {
            quickSort(x, from, to, comp);
        } else {
            pool.invoke(new ForkJoinQuickSortComp(x, from, to, comp));
        }
    }

    public static <K> void parallelQuickSort(K[] x, Comparator<K> comp) {
        parallelQuickSort(x, 0, x.length, comp);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0033, code lost:            if (r1 > 0) goto L11;     */
    /* JADX WARN: Code restructure failed: missing block: B:4:0x002c, code lost:            if (r1 < 0) goto L11;     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x0038, code lost:            return r5;     */
    /* JADX WARN: Code restructure failed: missing block: B:9:?, code lost:            return r7;     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static <K> int med3(K[] r4, int r5, int r6, int r7) {
        /*
            r0 = r4[r5]
            java.lang.Comparable r0 = (java.lang.Comparable) r0
            r1 = r0
            java.lang.Comparable r1 = (java.lang.Comparable) r1
            r1 = r4[r6]
            int r0 = r0.compareTo(r1)
            r1 = r4[r5]
            java.lang.Comparable r1 = (java.lang.Comparable) r1
            r2 = r1
            java.lang.Comparable r2 = (java.lang.Comparable) r2
            r2 = r4[r7]
            int r1 = r1.compareTo(r2)
            r2 = r4[r6]
            java.lang.Comparable r2 = (java.lang.Comparable) r2
            r3 = r2
            java.lang.Comparable r3 = (java.lang.Comparable) r3
            r3 = r4[r7]
            int r2 = r2.compareTo(r3)
            if (r0 >= 0) goto L2f
            if (r2 >= 0) goto L2c
            goto L31
        L2c:
            if (r1 >= 0) goto L37
            goto L35
        L2f:
            if (r2 <= 0) goto L33
        L31:
            r3 = r6
            goto L38
        L33:
            if (r1 <= 0) goto L37
        L35:
            r3 = r7
            goto L38
        L37:
            r3 = r5
        L38:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.objects.ObjectArrays.med3(java.lang.Object[], int, int, int):int");
    }

    private static <K> void selectionSort(K[] a, int from, int to) {
        for (int i = from; i < to - 1; i++) {
            int m = i;
            for (int j = i + 1; j < to; j++) {
                if (((Comparable) a[j]).compareTo(a[m]) < 0) {
                    m = j;
                }
            }
            if (m != i) {
                K u = a[i];
                a[i] = a[m];
                a[m] = u;
            }
        }
    }

    private static <K> void insertionSort(K[] a, int from, int to) {
        int i = from;
        while (true) {
            i++;
            if (i < to) {
                K t = a[i];
                int j = i;
                K u = a[j - 1];
                while (true) {
                    if (((Comparable) t).compareTo(u) < 0) {
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

    public static <K> void quickSort(K[] x, int from, int to) {
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
        K v = x[med3(x, l, m, n)];
        int a = from;
        int b = a;
        int c = to - 1;
        int d = c;
        while (true) {
            if (b <= c) {
                int comparison = ((Comparable) x[b]).compareTo(v);
                if (comparison <= 0) {
                    if (comparison == 0) {
                        swap(x, a, b);
                        a++;
                    }
                    b++;
                }
            }
            while (c >= b) {
                int comparison2 = ((Comparable) x[c]).compareTo(v);
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

    public static <K> void quickSort(K[] x) {
        quickSort(x, 0, x.length);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public static class ForkJoinQuickSort<K> extends RecursiveAction {
        private static final long serialVersionUID = 1;
        private final int from;
        private final int to;
        private final K[] x;

        public ForkJoinQuickSort(K[] x, int from, int to) {
            this.from = from;
            this.to = to;
            this.x = x;
        }

        @Override // java.util.concurrent.RecursiveAction
        protected void compute() {
            K[] x = this.x;
            int b = this.to - this.from;
            if (b < 8192) {
                ObjectArrays.quickSort(x, this.from, this.to);
                return;
            }
            int m = this.from + (b / 2);
            int l = this.from;
            int n = this.to - 1;
            int s = b / 8;
            int c = ObjectArrays.med3(x, ObjectArrays.med3(x, l, l + s, (s * 2) + l), ObjectArrays.med3(x, m - s, m, m + s), ObjectArrays.med3(x, n - (s * 2), n - s, n));
            K v = x[c];
            int a = this.from;
            int b2 = a;
            int c2 = this.to - 1;
            int d = c2;
            while (true) {
                if (b2 <= c2) {
                    int comparison = ((Comparable) x[b2]).compareTo(v);
                    if (comparison <= 0) {
                        if (comparison == 0) {
                            ObjectArrays.swap(x, a, b2);
                            a++;
                        }
                        b2++;
                    }
                }
                while (c2 >= b2) {
                    int comparison2 = ((Comparable) x[c2]).compareTo(v);
                    if (comparison2 < 0) {
                        break;
                    }
                    if (comparison2 == 0) {
                        ObjectArrays.swap(x, c2, d);
                        d--;
                    }
                    c2--;
                }
                if (b2 > c2) {
                    break;
                }
                int len = b;
                int len2 = b2 + 1;
                ObjectArrays.swap(x, b2, c2);
                b2 = len2;
                c2--;
                b = len;
                c = c;
            }
            int s2 = Math.min(a - this.from, b2 - a);
            ObjectArrays.swap(x, this.from, b2 - s2, s2);
            int s3 = Math.min(d - c2, (this.to - d) - 1);
            ObjectArrays.swap(x, b2, this.to - s3, s3);
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

    public static <K> void parallelQuickSort(K[] x, int from, int to) {
        ForkJoinPool pool = getPool();
        if (to - from < 8192 || pool.getParallelism() == 1) {
            quickSort(x, from, to);
        } else {
            pool.invoke(new ForkJoinQuickSort(x, from, to));
        }
    }

    public static <K> void parallelQuickSort(K[] x) {
        parallelQuickSort(x, 0, x.length);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0036, code lost:            if (r4 > 0) goto L11;     */
    /* JADX WARN: Code restructure failed: missing block: B:4:0x002f, code lost:            if (r4 < 0) goto L11;     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x003b, code lost:            return r9;     */
    /* JADX WARN: Code restructure failed: missing block: B:9:?, code lost:            return r11;     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static <K> int med3Indirect(int[] r7, K[] r8, int r9, int r10, int r11) {
        /*
            r0 = r7[r9]
            r0 = r8[r0]
            r1 = r7[r10]
            r1 = r8[r1]
            r2 = r7[r11]
            r2 = r8[r2]
            r3 = r0
            java.lang.Comparable r3 = (java.lang.Comparable) r3
            r4 = r3
            java.lang.Comparable r4 = (java.lang.Comparable) r4
            int r3 = r3.compareTo(r1)
            r4 = r0
            java.lang.Comparable r4 = (java.lang.Comparable) r4
            r5 = r4
            java.lang.Comparable r5 = (java.lang.Comparable) r5
            int r4 = r4.compareTo(r2)
            r5 = r1
            java.lang.Comparable r5 = (java.lang.Comparable) r5
            r6 = r5
            java.lang.Comparable r6 = (java.lang.Comparable) r6
            int r5 = r5.compareTo(r2)
            if (r3 >= 0) goto L32
            if (r5 >= 0) goto L2f
            goto L34
        L2f:
            if (r4 >= 0) goto L3a
            goto L38
        L32:
            if (r5 <= 0) goto L36
        L34:
            r6 = r10
            goto L3b
        L36:
            if (r4 <= 0) goto L3a
        L38:
            r6 = r11
            goto L3b
        L3a:
            r6 = r9
        L3b:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.objects.ObjectArrays.med3Indirect(int[], java.lang.Object[], int, int, int):int");
    }

    private static <K> void insertionSortIndirect(int[] perm, K[] a, int from, int to) {
        int i = from;
        while (true) {
            i++;
            if (i < to) {
                int t = perm[i];
                int j = i;
                int u = perm[j - 1];
                while (true) {
                    if (((Comparable) a[t]).compareTo(a[u]) < 0) {
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

    public static <K> void quickSortIndirect(int[] perm, K[] x, int from, int to) {
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
        K v = x[perm[med3Indirect(perm, x, l, m, n)]];
        int a = from;
        int b2 = a;
        int c = to - 1;
        int d = c;
        while (true) {
            if (b2 <= c) {
                int comparison = ((Comparable) x[perm[b2]]).compareTo(v);
                if (comparison <= 0) {
                    if (comparison == 0) {
                        IntArrays.swap(perm, a, b2);
                        a++;
                    }
                    b2++;
                }
            }
            while (c >= b2) {
                int comparison2 = ((Comparable) x[perm[c]]).compareTo(v);
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

    public static <K> void quickSortIndirect(int[] perm, K[] x) {
        quickSortIndirect(perm, x, 0, x.length);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public static class ForkJoinQuickSortIndirect<K> extends RecursiveAction {
        private static final long serialVersionUID = 1;
        private final int from;
        private final int[] perm;
        private final int to;
        private final K[] x;

        public ForkJoinQuickSortIndirect(int[] perm, K[] x, int from, int to) {
            this.from = from;
            this.to = to;
            this.x = x;
            this.perm = perm;
        }

        @Override // java.util.concurrent.RecursiveAction
        protected void compute() {
            K[] x = this.x;
            int len = this.to - this.from;
            if (len < 8192) {
                ObjectArrays.quickSortIndirect(this.perm, x, this.from, this.to);
                return;
            }
            int m = this.from + (len / 2);
            int l = this.from;
            int n = this.to - 1;
            int s = len / 8;
            int c = ObjectArrays.med3Indirect(this.perm, x, l, l + s, (s * 2) + l);
            int b = ObjectArrays.med3Indirect(this.perm, x, c, ObjectArrays.med3Indirect(this.perm, x, m - s, m, m + s), ObjectArrays.med3Indirect(this.perm, x, n - (s * 2), n - s, n));
            K v = x[this.perm[b]];
            int a = this.from;
            int b2 = a;
            int c2 = this.to - 1;
            int d = c2;
            while (true) {
                if (b2 <= c2) {
                    int comparison = ((Comparable) x[this.perm[b2]]).compareTo(v);
                    if (comparison <= 0) {
                        if (comparison == 0) {
                            IntArrays.swap(this.perm, a, b2);
                            a++;
                        }
                        b2++;
                    }
                }
                while (c2 >= b2) {
                    int comparison2 = ((Comparable) x[this.perm[c2]]).compareTo(v);
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

    public static <K> void parallelQuickSortIndirect(int[] perm, K[] x, int from, int to) {
        ForkJoinPool pool = getPool();
        if (to - from < 8192 || pool.getParallelism() == 1) {
            quickSortIndirect(perm, x, from, to);
        } else {
            pool.invoke(new ForkJoinQuickSortIndirect(perm, x, from, to));
        }
    }

    public static <K> void parallelQuickSortIndirect(int[] perm, K[] x) {
        parallelQuickSortIndirect(perm, x, 0, x.length);
    }

    public static <K> void stabilize(int[] perm, K[] x, int from, int to) {
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

    public static <K> void stabilize(int[] perm, K[] x) {
        stabilize(perm, x, 0, perm.length);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0062, code lost:            if (r2 < 0) goto L22;     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x006e, code lost:            return r7;     */
    /* JADX WARN: Code restructure failed: missing block: B:17:?, code lost:            return r9;     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0069, code lost:            if (r2 > 0) goto L22;     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static <K> int med3(K[] r5, K[] r6, int r7, int r8, int r9) {
        /*
            r0 = r5[r7]
            java.lang.Comparable r0 = (java.lang.Comparable) r0
            r1 = r0
            java.lang.Comparable r1 = (java.lang.Comparable) r1
            r1 = r5[r8]
            int r0 = r0.compareTo(r1)
            r1 = r0
            if (r0 != 0) goto L1e
            r0 = r6[r7]
            java.lang.Comparable r0 = (java.lang.Comparable) r0
            r2 = r0
            java.lang.Comparable r2 = (java.lang.Comparable) r2
            r2 = r6[r8]
            int r0 = r0.compareTo(r2)
            goto L1f
        L1e:
            r0 = r1
        L1f:
            r2 = r5[r7]
            java.lang.Comparable r2 = (java.lang.Comparable) r2
            r3 = r2
            java.lang.Comparable r3 = (java.lang.Comparable) r3
            r3 = r5[r9]
            int r2 = r2.compareTo(r3)
            r1 = r2
            if (r2 != 0) goto L3d
            r2 = r6[r7]
            java.lang.Comparable r2 = (java.lang.Comparable) r2
            r3 = r2
            java.lang.Comparable r3 = (java.lang.Comparable) r3
            r3 = r6[r9]
            int r2 = r2.compareTo(r3)
            goto L3e
        L3d:
            r2 = r1
        L3e:
            r3 = r5[r8]
            java.lang.Comparable r3 = (java.lang.Comparable) r3
            r4 = r3
            java.lang.Comparable r4 = (java.lang.Comparable) r4
            r4 = r5[r9]
            int r3 = r3.compareTo(r4)
            r1 = r3
            if (r3 != 0) goto L5c
            r3 = r6[r8]
            java.lang.Comparable r3 = (java.lang.Comparable) r3
            r4 = r3
            java.lang.Comparable r4 = (java.lang.Comparable) r4
            r4 = r6[r9]
            int r3 = r3.compareTo(r4)
            goto L5d
        L5c:
            r3 = r1
        L5d:
            if (r0 >= 0) goto L65
            if (r3 >= 0) goto L62
            goto L67
        L62:
            if (r2 >= 0) goto L6d
            goto L6b
        L65:
            if (r3 <= 0) goto L69
        L67:
            r4 = r8
            goto L6e
        L69:
            if (r2 <= 0) goto L6d
        L6b:
            r4 = r9
            goto L6e
        L6d:
            r4 = r7
        L6e:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.objects.ObjectArrays.med3(java.lang.Object[], java.lang.Object[], int, int, int):int");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <K> void swap(K[] x, K[] y, int a, int b) {
        K t = x[a];
        K u = y[a];
        x[a] = x[b];
        y[a] = y[b];
        x[b] = t;
        y[b] = u;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <K> void swap(K[] x, K[] y, int a, int b, int n) {
        int i = 0;
        while (i < n) {
            swap(x, y, a, b);
            i++;
            a++;
            b++;
        }
    }

    private static <K> void selectionSort(K[] a, K[] b, int from, int to) {
        for (int i = from; i < to - 1; i++) {
            int m = i;
            for (int j = i + 1; j < to; j++) {
                int u = ((Comparable) a[j]).compareTo(a[m]);
                if (u < 0 || (u == 0 && ((Comparable) b[j]).compareTo(b[m]) < 0)) {
                    m = j;
                }
            }
            if (m != i) {
                K t = a[i];
                a[i] = a[m];
                a[m] = t;
                K t2 = b[i];
                b[i] = b[m];
                b[m] = t2;
            }
        }
    }

    public static <K> void quickSort(K[] x, K[] y, int from, int to) {
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
        K v = x[c];
        K w = y[c];
        int a = from;
        int b2 = a;
        int c2 = to - 1;
        int d = c2;
        while (true) {
            if (b2 <= c2) {
                int t = ((Comparable) x[b2]).compareTo(v);
                int compareTo = t == 0 ? ((Comparable) y[b2]).compareTo(w) : t;
                int comparison = compareTo;
                if (compareTo <= 0) {
                    if (comparison == 0) {
                        swap(x, y, a, b2);
                        a++;
                    }
                    b2++;
                }
            }
            while (c2 >= b2) {
                int t2 = ((Comparable) x[c2]).compareTo(v);
                int compareTo2 = t2 == 0 ? ((Comparable) y[c2]).compareTo(w) : t2;
                int comparison2 = compareTo2;
                if (compareTo2 < 0) {
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

    public static <K> void quickSort(K[] x, K[] y) {
        ensureSameLength(x, y);
        quickSort(x, y, 0, x.length);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public static class ForkJoinQuickSort2<K> extends RecursiveAction {
        private static final long serialVersionUID = 1;
        private final int from;
        private final int to;
        private final K[] x;
        private final K[] y;

        public ForkJoinQuickSort2(K[] x, K[] y, int from, int to) {
            this.from = from;
            this.to = to;
            this.x = x;
            this.y = y;
        }

        @Override // java.util.concurrent.RecursiveAction
        protected void compute() {
            K[] x = this.x;
            K[] y = this.y;
            int b = this.to - this.from;
            if (b < 8192) {
                ObjectArrays.quickSort(x, y, this.from, this.to);
                return;
            }
            int m = this.from + (b / 2);
            int l = this.from;
            int n = this.to - 1;
            int s = b / 8;
            int c = ObjectArrays.med3(x, y, l, l + s, (s * 2) + l);
            int m2 = ObjectArrays.med3(x, y, c, ObjectArrays.med3(x, y, m - s, m, m + s), ObjectArrays.med3(x, y, n - (s * 2), n - s, n));
            K v = x[m2];
            K w = y[m2];
            int a = this.from;
            int b2 = a;
            int c2 = this.to - 1;
            int d = c2;
            while (true) {
                if (b2 <= c2) {
                    int t = ((Comparable) x[b2]).compareTo(v);
                    int compareTo = t == 0 ? ((Comparable) y[b2]).compareTo(w) : t;
                    int comparison = compareTo;
                    if (compareTo <= 0) {
                        if (comparison == 0) {
                            ObjectArrays.swap(x, y, a, b2);
                            a++;
                        }
                        b2++;
                    }
                }
                while (c2 >= b2) {
                    int t2 = ((Comparable) x[c2]).compareTo(v);
                    int compareTo2 = t2 == 0 ? ((Comparable) y[c2]).compareTo(w) : t2;
                    int comparison2 = compareTo2;
                    if (compareTo2 < 0) {
                        break;
                    }
                    if (comparison2 == 0) {
                        ObjectArrays.swap(x, y, c2, d);
                        d--;
                    }
                    c2--;
                }
                if (b2 > c2) {
                    break;
                }
                int len = b;
                int len2 = b2 + 1;
                ObjectArrays.swap(x, y, b2, c2);
                b2 = len2;
                c2--;
                b = len;
                m2 = m2;
                c = c;
            }
            int s2 = Math.min(a - this.from, b2 - a);
            ObjectArrays.swap(x, y, this.from, b2 - s2, s2);
            int s3 = Math.min(d - c2, (this.to - d) - 1);
            ObjectArrays.swap(x, y, b2, this.to - s3, s3);
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

    public static <K> void parallelQuickSort(K[] x, K[] y, int from, int to) {
        ForkJoinPool pool = getPool();
        if (to - from < 8192 || pool.getParallelism() == 1) {
            quickSort(x, y, from, to);
        } else {
            pool.invoke(new ForkJoinQuickSort2(x, y, from, to));
        }
    }

    public static <K> void parallelQuickSort(K[] x, K[] y) {
        ensureSameLength(x, y);
        parallelQuickSort(x, y, 0, x.length);
    }

    public static <K> void unstableSort(K[] a, int from, int to) {
        quickSort(a, from, to);
    }

    public static <K> void unstableSort(K[] a) {
        unstableSort(a, 0, a.length);
    }

    public static <K> void unstableSort(K[] a, int from, int to, Comparator<K> comp) {
        quickSort(a, from, to, comp);
    }

    public static <K> void unstableSort(K[] a, Comparator<K> comp) {
        unstableSort(a, 0, a.length, comp);
    }

    public static <K> void mergeSort(K[] kArr, int i, int i2, K[] kArr2) {
        int i3 = i2 - i;
        if (i3 < 16) {
            insertionSort(kArr, i, i2);
            return;
        }
        if (kArr2 == null) {
            kArr2 = (K[]) java.util.Arrays.copyOf(kArr, i2);
        }
        int i4 = (i + i2) >>> 1;
        mergeSort(kArr2, i, i4, kArr);
        mergeSort(kArr2, i4, i2, kArr);
        if (((Comparable) kArr2[i4 - 1]).compareTo(kArr2[i4]) <= 0) {
            System.arraycopy(kArr2, i, kArr, i, i3);
            return;
        }
        int i5 = i;
        int i6 = i4;
        for (int i7 = i; i7 < i2; i7++) {
            if (i6 >= i2 || (i5 < i4 && ((Comparable) kArr2[i5]).compareTo(kArr2[i6]) <= 0)) {
                kArr[i7] = kArr2[i5];
                i5++;
            } else {
                kArr[i7] = kArr2[i6];
                i6++;
            }
        }
    }

    public static <K> void mergeSort(K[] a, int from, int to) {
        mergeSort(a, from, to, (Object[]) null);
    }

    public static <K> void mergeSort(K[] a) {
        mergeSort(a, 0, a.length);
    }

    public static <K> void mergeSort(K[] kArr, int i, int i2, Comparator<K> comparator, K[] kArr2) {
        int i3 = i2 - i;
        if (i3 < 16) {
            insertionSort(kArr, i, i2, comparator);
            return;
        }
        if (kArr2 == null) {
            kArr2 = (K[]) java.util.Arrays.copyOf(kArr, i2);
        }
        int i4 = (i + i2) >>> 1;
        mergeSort(kArr2, i, i4, comparator, kArr);
        mergeSort(kArr2, i4, i2, comparator, kArr);
        if (comparator.compare(kArr2[i4 - 1], kArr2[i4]) <= 0) {
            System.arraycopy(kArr2, i, kArr, i, i3);
            return;
        }
        int i5 = i;
        int i6 = i4;
        for (int i7 = i; i7 < i2; i7++) {
            if (i6 >= i2 || (i5 < i4 && comparator.compare(kArr2[i5], kArr2[i6]) <= 0)) {
                kArr[i7] = kArr2[i5];
                i5++;
            } else {
                kArr[i7] = kArr2[i6];
                i6++;
            }
        }
    }

    public static <K> void mergeSort(K[] a, int from, int to, Comparator<K> comp) {
        mergeSort(a, from, to, comp, null);
    }

    public static <K> void mergeSort(K[] a, Comparator<K> comp) {
        mergeSort(a, 0, a.length, comp);
    }

    public static <K> void stableSort(K[] a, int from, int to) {
        java.util.Arrays.sort(a, from, to);
    }

    public static <K> void stableSort(K[] a) {
        stableSort(a, 0, a.length);
    }

    public static <K> void stableSort(K[] a, int from, int to, Comparator<K> comp) {
        java.util.Arrays.sort(a, from, to, comp);
    }

    public static <K> void stableSort(K[] a, Comparator<K> comp) {
        stableSort(a, 0, a.length, comp);
    }

    public static <K> int binarySearch(K[] a, int from, int to, K key) {
        int to2 = to - 1;
        while (from <= to2) {
            int mid = (from + to2) >>> 1;
            K midVal = a[mid];
            int cmp = ((Comparable) midVal).compareTo(key);
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

    public static <K> int binarySearch(K[] a, K key) {
        return binarySearch(a, 0, a.length, key);
    }

    public static <K> int binarySearch(K[] a, int from, int to, K key, Comparator<K> c) {
        int to2 = to - 1;
        while (from <= to2) {
            int mid = (from + to2) >>> 1;
            K midVal = a[mid];
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

    public static <K> int binarySearch(K[] a, K key, Comparator<K> c) {
        return binarySearch(a, 0, a.length, key, c);
    }

    public static <K> K[] shuffle(K[] a, int from, int to, Random random) {
        int p = to - from;
        while (true) {
            int i = p - 1;
            if (p != 0) {
                int p2 = random.nextInt(i + 1);
                K t = a[from + i];
                a[from + i] = a[from + p2];
                a[from + p2] = t;
                p = i;
            } else {
                return a;
            }
        }
    }

    public static <K> K[] shuffle(K[] a, Random random) {
        int p = a.length;
        while (true) {
            int i = p - 1;
            if (p != 0) {
                int p2 = random.nextInt(i + 1);
                K t = a[i];
                a[i] = a[p2];
                a[p2] = t;
                p = i;
            } else {
                return a;
            }
        }
    }

    public static <K> K[] reverse(K[] a) {
        int length = a.length;
        int i = length / 2;
        while (true) {
            int i2 = i - 1;
            if (i != 0) {
                K t = a[(length - i2) - 1];
                a[(length - i2) - 1] = a[i2];
                a[i2] = t;
                i = i2;
            } else {
                return a;
            }
        }
    }

    public static <K> K[] reverse(K[] a, int from, int to) {
        int length = to - from;
        int i = length / 2;
        while (true) {
            int i2 = i - 1;
            if (i != 0) {
                K t = a[((from + length) - i2) - 1];
                a[((from + length) - i2) - 1] = a[from + i2];
                a[from + i2] = t;
                i = i2;
            } else {
                return a;
            }
        }
    }

    /* loaded from: classes4.dex */
    private static final class ArrayHashStrategy<K> implements Hash.Strategy<K[]>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;

        private ArrayHashStrategy() {
        }

        @Override // it.unimi.dsi.fastutil.Hash.Strategy
        public int hashCode(K[] o) {
            return java.util.Arrays.hashCode(o);
        }

        @Override // it.unimi.dsi.fastutil.Hash.Strategy
        public boolean equals(K[] a, K[] b) {
            return java.util.Arrays.equals(a, b);
        }
    }
}
