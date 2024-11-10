package it.unimi.dsi.fastutil.objects;

import io.netty.handler.codec.http2.Http2CodecUtil;
import it.unimi.dsi.fastutil.BigArrays;
import it.unimi.dsi.fastutil.Hash;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

/* loaded from: classes4.dex */
public final class ObjectBigArrays {
    private static final int MEDIUM = 40;
    private static final int PARALLEL_QUICKSORT_NO_FORK = 8192;
    private static final int QUICKSORT_NO_REC = 7;
    public static final Object[][] EMPTY_BIG_ARRAY = new Object[0];
    public static final Object[][] DEFAULT_EMPTY_BIG_ARRAY = new Object[0];
    public static final Hash.Strategy HASH_STRATEGY = new BigArrayHashStrategy();

    private ObjectBigArrays() {
    }

    @Deprecated
    public static <K> K get(K[][] array, long index) {
        return array[BigArrays.segment(index)][BigArrays.displacement(index)];
    }

    @Deprecated
    public static <K> void set(K[][] array, long index, K value) {
        array[BigArrays.segment(index)][BigArrays.displacement(index)] = value;
    }

    @Deprecated
    public static <K> void swap(K[][] array, long first, long second) {
        K t = array[BigArrays.segment(first)][BigArrays.displacement(first)];
        array[BigArrays.segment(first)][BigArrays.displacement(first)] = array[BigArrays.segment(second)][BigArrays.displacement(second)];
        array[BigArrays.segment(second)][BigArrays.displacement(second)] = t;
    }

    @Deprecated
    public static <K> long length(K[][] array) {
        int length = array.length;
        if (length == 0) {
            return 0L;
        }
        return BigArrays.start(length - 1) + array[length - 1].length;
    }

    @Deprecated
    public static <K> void copy(K[][] srcArray, long srcPos, K[][] destArray, long destPos, long length) {
        BigArrays.copy(srcArray, srcPos, destArray, destPos, length);
    }

    @Deprecated
    public static <K> void copyFromBig(K[][] srcArray, long srcPos, K[] destArray, int destPos, int length) {
        BigArrays.copyFromBig(srcArray, srcPos, destArray, destPos, length);
    }

    @Deprecated
    public static <K> void copyToBig(K[] srcArray, int srcPos, K[][] destArray, long destPos, long length) {
        BigArrays.copyToBig(srcArray, srcPos, destArray, destPos, length);
    }

    public static <K> K[][] newBigArray(K[][] kArr, long j) {
        return (K[][]) newBigArray(kArr.getClass().getComponentType(), j);
    }

    public static Object[][] newBigArray(Class<?> componentType, long length) {
        if (length == 0 && componentType == Object[].class) {
            return EMPTY_BIG_ARRAY;
        }
        BigArrays.ensureLength(length);
        int baseLength = (int) ((length + 134217727) >>> 27);
        Object[][] base = (Object[][]) Array.newInstance(componentType, baseLength);
        int residual = (int) (134217727 & length);
        if (residual != 0) {
            for (int i = 0; i < baseLength - 1; i++) {
                base[i] = (Object[]) Array.newInstance(componentType.getComponentType(), BigArrays.SEGMENT_SIZE);
            }
            base[baseLength - 1] = (Object[]) Array.newInstance(componentType.getComponentType(), residual);
        } else {
            for (int i2 = 0; i2 < baseLength; i2++) {
                base[i2] = (Object[]) Array.newInstance(componentType.getComponentType(), BigArrays.SEGMENT_SIZE);
            }
        }
        return base;
    }

    public static Object[][] newBigArray(long length) {
        if (length == 0) {
            return EMPTY_BIG_ARRAY;
        }
        BigArrays.ensureLength(length);
        int baseLength = (int) ((length + 134217727) >>> 27);
        Object[][] base = new Object[baseLength];
        int residual = (int) (134217727 & length);
        if (residual != 0) {
            for (int i = 0; i < baseLength - 1; i++) {
                base[i] = new Object[BigArrays.SEGMENT_SIZE];
            }
            base[baseLength - 1] = new Object[residual];
        } else {
            for (int i2 = 0; i2 < baseLength; i2++) {
                base[i2] = new Object[BigArrays.SEGMENT_SIZE];
            }
        }
        return base;
    }

    @Deprecated
    public static <K> K[][] wrap(K[] kArr) {
        return (K[][]) BigArrays.wrap(kArr);
    }

    @Deprecated
    public static <K> K[][] ensureCapacity(K[][] kArr, long j) {
        return (K[][]) ensureCapacity(kArr, j, length(kArr));
    }

    @Deprecated
    public static <K> K[][] forceCapacity(K[][] kArr, long j, long j2) {
        return (K[][]) BigArrays.forceCapacity(kArr, j, j2);
    }

    @Deprecated
    public static <K> K[][] ensureCapacity(K[][] kArr, long j, long j2) {
        return j > length(kArr) ? (K[][]) forceCapacity(kArr, j, j2) : kArr;
    }

    @Deprecated
    public static <K> K[][] grow(K[][] kArr, long j) {
        long length = length(kArr);
        return j > length ? (K[][]) grow(kArr, j, length) : kArr;
    }

    @Deprecated
    public static <K> K[][] grow(K[][] kArr, long j, long j2) {
        long length = length(kArr);
        return j > length ? (K[][]) ensureCapacity(kArr, Math.max((length >> 1) + length, j), j2) : kArr;
    }

    @Deprecated
    public static <K> K[][] trim(K[][] kArr, long j) {
        return (K[][]) BigArrays.trim(kArr, j);
    }

    @Deprecated
    public static <K> K[][] setLength(K[][] kArr, long j) {
        return (K[][]) BigArrays.setLength(kArr, j);
    }

    @Deprecated
    public static <K> K[][] copy(K[][] kArr, long j, long j2) {
        return (K[][]) BigArrays.copy(kArr, j, j2);
    }

    @Deprecated
    public static <K> K[][] copy(K[][] kArr) {
        return (K[][]) BigArrays.copy(kArr);
    }

    @Deprecated
    public static <K> void fill(K[][] array, K value) {
        int i = array.length;
        while (true) {
            int i2 = i - 1;
            if (i == 0) {
                return;
            }
            Arrays.fill(array[i2], value);
            i = i2;
        }
    }

    @Deprecated
    public static <K> void fill(K[][] array, long from, long to, K value) {
        BigArrays.fill(array, from, to, value);
    }

    @Deprecated
    public static <K> boolean equals(K[][] a1, K[][] a2) {
        return BigArrays.equals(a1, a2);
    }

    @Deprecated
    public static <K> String toString(K[][] a) {
        return BigArrays.toString(a);
    }

    @Deprecated
    public static <K> void ensureFromTo(K[][] a, long from, long to) {
        BigArrays.ensureFromTo(length(a), from, to);
    }

    @Deprecated
    public static <K> void ensureOffsetLength(K[][] a, long offset, long length) {
        BigArrays.ensureOffsetLength(length(a), offset, length);
    }

    @Deprecated
    public static <K> void ensureSameLength(K[][] a, K[][] b) {
        if (length(a) != length(b)) {
            throw new IllegalArgumentException("Array size mismatch: " + length(a) + " != " + length(b));
        }
    }

    /* loaded from: classes4.dex */
    private static final class BigArrayHashStrategy<K> implements Hash.Strategy<K[][]>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;

        private BigArrayHashStrategy() {
        }

        @Override // it.unimi.dsi.fastutil.Hash.Strategy
        public int hashCode(K[][] o) {
            return Arrays.deepHashCode(o);
        }

        @Override // it.unimi.dsi.fastutil.Hash.Strategy
        public boolean equals(K[][] a, K[][] b) {
            return ObjectBigArrays.equals(a, b);
        }
    }

    private static ForkJoinPool getPool() {
        ForkJoinPool current = ForkJoinTask.getPool();
        return current == null ? ForkJoinPool.commonPool() : current;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <K> void swap(K[][] x, long a, long b, long n) {
        int i = 0;
        while (i < n) {
            BigArrays.swap(x, a, b);
            i++;
            a++;
            b++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0030, code lost:            if (r1 > 0) goto L11;     */
    /* JADX WARN: Code restructure failed: missing block: B:4:0x0029, code lost:            if (r1 < 0) goto L11;     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x0035, code lost:            return r6;     */
    /* JADX WARN: Code restructure failed: missing block: B:9:?, code lost:            return r10;     */
    /* JADX WARN: Multi-variable type inference failed */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static <K> long med3(K[][] r5, long r6, long r8, long r10, java.util.Comparator<K> r12) {
        /*
            java.lang.Object r0 = it.unimi.dsi.fastutil.BigArrays.get(r5, r6)
            java.lang.Object r1 = it.unimi.dsi.fastutil.BigArrays.get(r5, r8)
            int r0 = r12.compare(r0, r1)
            java.lang.Object r1 = it.unimi.dsi.fastutil.BigArrays.get(r5, r6)
            java.lang.Object r2 = it.unimi.dsi.fastutil.BigArrays.get(r5, r10)
            int r1 = r12.compare(r1, r2)
            java.lang.Object r2 = it.unimi.dsi.fastutil.BigArrays.get(r5, r8)
            java.lang.Object r3 = it.unimi.dsi.fastutil.BigArrays.get(r5, r10)
            int r2 = r12.compare(r2, r3)
            if (r0 >= 0) goto L2c
            if (r2 >= 0) goto L29
            goto L2e
        L29:
            if (r1 >= 0) goto L34
            goto L32
        L2c:
            if (r2 <= 0) goto L30
        L2e:
            r3 = r8
            goto L35
        L30:
            if (r1 <= 0) goto L34
        L32:
            r3 = r10
            goto L35
        L34:
            r3 = r6
        L35:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.objects.ObjectBigArrays.med3(java.lang.Object[][], long, long, long, java.util.Comparator):long");
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static <K> void selectionSort(K[][] a, long from, long to, Comparator<K> comparator) {
        for (long i = from; i < to - 1; i++) {
            long m = i;
            for (long j = i + 1; j < to; j++) {
                if (comparator.compare(BigArrays.get(a, j), BigArrays.get(a, m)) < 0) {
                    m = j;
                }
            }
            if (m != i) {
                BigArrays.swap(a, i, m);
            }
        }
    }

    public static <K> void quickSort(K[][] x, long from, long to, Comparator<K> comp) {
        long d;
        long c;
        int comparison;
        int comparison2;
        Comparator comparator = comp;
        long len = to - from;
        if (len < 7) {
            selectionSort(x, from, to, comp);
            return;
        }
        long m = from + (len / 2);
        if (len > 7) {
            long l = from;
            long n = to - 1;
            if (len > 40) {
                long s = len / 8;
                l = med3(x, l, l + s, l + (s * 2), comp);
                m = med3(x, m - s, m, m + s, comp);
                n = med3(x, n - (2 * s), n - s, n, comp);
            }
            m = med3(x, l, m, n, comp);
        }
        Object obj = BigArrays.get(x, m);
        long c2 = to - 1;
        long c3 = c2;
        long a = from;
        long b = c2;
        long b2 = from;
        while (true) {
            if (b2 <= b && (comparison2 = comparator.compare(BigArrays.get(x, b2), obj)) <= 0) {
                if (comparison2 == 0) {
                    BigArrays.swap(x, a, b2);
                    a++;
                }
                b2++;
            } else {
                d = c3;
                c = b;
                while (c >= b2 && (comparison = comparator.compare(BigArrays.get(x, c), obj)) >= 0) {
                    if (comparison == 0) {
                        BigArrays.swap(x, c, d);
                        d--;
                    }
                    c--;
                }
                if (b2 > c) {
                    break;
                }
                long c4 = c;
                long b3 = b2;
                long c5 = c4 - 1;
                BigArrays.swap(x, b3, c4);
                comparator = comp;
                b2 = b3 + 1;
                b = c5;
                obj = obj;
                c3 = d;
                len = len;
            }
        }
        long d2 = d;
        long c6 = c;
        long s2 = Math.min(a - from, b2 - a);
        long c7 = b2;
        swap(x, from, b2 - s2, s2);
        long s3 = Math.min(d2 - c6, (to - d2) - 1);
        swap(x, c7, to - s3, s3);
        long s4 = c7 - a;
        if (s4 > 1) {
            quickSort(x, from, from + s4, comp);
        }
        long s5 = d2 - c6;
        if (s5 > 1) {
            quickSort(x, to - s5, to, comp);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x003f, code lost:            if (r1 > 0) goto L11;     */
    /* JADX WARN: Code restructure failed: missing block: B:4:0x0038, code lost:            if (r1 < 0) goto L11;     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x0044, code lost:            return r6;     */
    /* JADX WARN: Code restructure failed: missing block: B:9:?, code lost:            return r10;     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static <K> long med3(K[][] r5, long r6, long r8, long r10) {
        /*
            java.lang.Object r0 = it.unimi.dsi.fastutil.BigArrays.get(r5, r6)
            java.lang.Comparable r0 = (java.lang.Comparable) r0
            r1 = r0
            java.lang.Comparable r1 = (java.lang.Comparable) r1
            java.lang.Object r1 = it.unimi.dsi.fastutil.BigArrays.get(r5, r8)
            int r0 = r0.compareTo(r1)
            java.lang.Object r1 = it.unimi.dsi.fastutil.BigArrays.get(r5, r6)
            java.lang.Comparable r1 = (java.lang.Comparable) r1
            r2 = r1
            java.lang.Comparable r2 = (java.lang.Comparable) r2
            java.lang.Object r2 = it.unimi.dsi.fastutil.BigArrays.get(r5, r10)
            int r1 = r1.compareTo(r2)
            java.lang.Object r2 = it.unimi.dsi.fastutil.BigArrays.get(r5, r8)
            java.lang.Comparable r2 = (java.lang.Comparable) r2
            r3 = r2
            java.lang.Comparable r3 = (java.lang.Comparable) r3
            java.lang.Object r3 = it.unimi.dsi.fastutil.BigArrays.get(r5, r10)
            int r2 = r2.compareTo(r3)
            if (r0 >= 0) goto L3b
            if (r2 >= 0) goto L38
            goto L3d
        L38:
            if (r1 >= 0) goto L43
            goto L41
        L3b:
            if (r2 <= 0) goto L3f
        L3d:
            r3 = r8
            goto L44
        L3f:
            if (r1 <= 0) goto L43
        L41:
            r3 = r10
            goto L44
        L43:
            r3 = r6
        L44:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.objects.ObjectBigArrays.med3(java.lang.Object[][], long, long, long):long");
    }

    private static <K> void selectionSort(K[][] a, long from, long to) {
        for (long i = from; i < to - 1; i++) {
            long m = i;
            for (long j = i + 1; j < to; j++) {
                if (((Comparable) BigArrays.get(a, j)).compareTo(BigArrays.get(a, m)) < 0) {
                    m = j;
                }
            }
            if (m != i) {
                BigArrays.swap(a, i, m);
            }
        }
    }

    public static <K> void quickSort(K[][] x, Comparator<K> comp) {
        quickSort(x, 0L, BigArrays.length(x), comp);
    }

    public static <K> void quickSort(K[][] x, long from, long to) {
        long c;
        long d;
        int comparison;
        int comparison2;
        long d2 = to - from;
        if (d2 < 7) {
            selectionSort(x, from, to);
            return;
        }
        long m = from + (d2 / 2);
        if (d2 > 7) {
            long l = from;
            long n = to - 1;
            if (d2 > 40) {
                long s = d2 / 8;
                l = med3(x, l, l + s, l + (s * 2));
                m = med3(x, m - s, m, m + s);
                n = med3(x, n - (2 * s), n - s, n);
            }
            m = med3(x, l, m, n);
        }
        Object obj = BigArrays.get(x, m);
        long c2 = to - 1;
        long len = c2;
        long b = c2;
        long a = from;
        long b2 = from;
        while (true) {
            if (b2 <= b && (comparison2 = ((Comparable) BigArrays.get(x, b2)).compareTo(obj)) <= 0) {
                if (comparison2 == 0) {
                    BigArrays.swap(x, a, b2);
                    a++;
                }
                b2++;
            } else {
                c = b;
                long len2 = d2;
                d = len;
                while (c >= b2 && (comparison = ((Comparable) BigArrays.get(x, c)).compareTo(obj)) >= 0) {
                    if (comparison == 0) {
                        BigArrays.swap(x, c, d);
                        d--;
                    }
                    c--;
                }
                if (b2 > c) {
                    break;
                }
                long m2 = m;
                long m3 = c;
                long b3 = b2;
                long c3 = m3 - 1;
                BigArrays.swap(x, b3, m3);
                m = m2;
                a = a;
                b2 = b3 + 1;
                b = c3;
                long j = d;
                d2 = len2;
                len = j;
            }
        }
        long c4 = c;
        long s2 = Math.min(a - from, b2 - a);
        long a2 = b2 - s2;
        long b4 = b2;
        swap(x, from, a2, s2);
        long s3 = Math.min(d - c4, (to - d) - 1);
        swap(x, b4, to - s3, s3);
        long s4 = b4 - a;
        if (s4 > 1) {
            quickSort(x, from, from + s4);
        }
        long s5 = d - c4;
        if (s5 > 1) {
            quickSort(x, to - s5, to);
        }
    }

    public static <K> void quickSort(K[][] x) {
        quickSort(x, 0L, BigArrays.length(x));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public static class ForkJoinQuickSort<K> extends RecursiveAction {
        private static final long serialVersionUID = 1;
        private final long from;
        private final long to;
        private final K[][] x;

        public ForkJoinQuickSort(K[][] x, long from, long to) {
            this.from = from;
            this.to = to;
            this.x = x;
        }

        @Override // java.util.concurrent.RecursiveAction
        protected void compute() {
            long c;
            long d;
            int comparison;
            int comparison2;
            K[][] x = this.x;
            long len = this.to - this.from;
            if (len < Http2CodecUtil.DEFAULT_HEADER_LIST_SIZE) {
                ObjectBigArrays.quickSort(x, this.from, this.to);
                return;
            }
            long m = this.from + (len / 2);
            long l = this.from;
            long n = this.to - serialVersionUID;
            long s = len / 8;
            long l2 = ObjectBigArrays.med3(x, l, l + s, l + (s * 2));
            long m2 = ObjectBigArrays.med3(x, m - s, m, m + s);
            long d2 = ObjectBigArrays.med3(x, n - (2 * s), n - s, n);
            long m3 = ObjectBigArrays.med3(x, l2, m2, d2);
            Object obj = BigArrays.get(x, m3);
            long a = this.from;
            long c2 = this.to - serialVersionUID;
            long n2 = c2;
            long a2 = a;
            long len2 = a;
            while (true) {
                if (len2 <= c2 && (comparison2 = ((Comparable) BigArrays.get(x, len2)).compareTo(obj)) <= 0) {
                    if (comparison2 == 0) {
                        long a3 = a2 + serialVersionUID;
                        BigArrays.swap(x, a2, len2);
                        a2 = a3;
                    }
                    len2 += serialVersionUID;
                } else {
                    c = c2;
                    long n3 = d2;
                    d = n2;
                    while (c >= len2 && (comparison = ((Comparable) BigArrays.get(x, c)).compareTo(obj)) >= 0) {
                        if (comparison == 0) {
                            long d3 = d - serialVersionUID;
                            BigArrays.swap(x, c, d);
                            d = d3;
                        }
                        c -= serialVersionUID;
                    }
                    if (len2 > c) {
                        break;
                    }
                    long m4 = m3;
                    long m5 = c;
                    long b = len2 + serialVersionUID;
                    c2 = m5 - serialVersionUID;
                    BigArrays.swap(x, len2, m5);
                    len2 = b;
                    a2 = a2;
                    m3 = m4;
                    long j = d;
                    d2 = n3;
                    n2 = j;
                }
            }
            long c3 = c;
            long s2 = Math.min(a2 - this.from, len2 - a2);
            ObjectBigArrays.swap(x, this.from, len2 - s2, s2);
            long s3 = Math.min(d - c3, (this.to - d) - serialVersionUID);
            ObjectBigArrays.swap(x, len2, this.to - s3, s3);
            long s4 = len2 - a2;
            long t = d - c3;
            if (s4 > serialVersionUID && t > serialVersionUID) {
                invokeAll(new ForkJoinQuickSort(x, this.from, this.from + s4), new ForkJoinQuickSort(x, this.to - t, this.to));
            } else if (s4 <= serialVersionUID) {
                invokeAll(new ForkJoinTask[]{new ForkJoinQuickSort(x, this.to - t, this.to)});
            } else {
                invokeAll(new ForkJoinTask[]{new ForkJoinQuickSort(x, this.from, this.from + s4)});
            }
        }
    }

    public static <K> void parallelQuickSort(K[][] x, long from, long to) {
        ForkJoinPool pool = getPool();
        if (to - from < Http2CodecUtil.DEFAULT_HEADER_LIST_SIZE || pool.getParallelism() == 1) {
            quickSort(x, from, to);
        } else {
            pool.invoke(new ForkJoinQuickSort(x, from, to));
        }
    }

    public static <K> void parallelQuickSort(K[][] x) {
        parallelQuickSort(x, 0L, BigArrays.length(x));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public static class ForkJoinQuickSortComp<K> extends RecursiveAction {
        private static final long serialVersionUID = 1;
        private final Comparator<K> comp;
        private final long from;
        private final long to;
        private final K[][] x;

        public ForkJoinQuickSortComp(K[][] x, long from, long to, Comparator<K> comp) {
            this.from = from;
            this.to = to;
            this.x = x;
            this.comp = comp;
        }

        @Override // java.util.concurrent.RecursiveAction
        protected void compute() {
            long j;
            long j2;
            long j3;
            long j4;
            K[][] kArr = this.x;
            long j5 = this.to - this.from;
            if (j5 < Http2CodecUtil.DEFAULT_HEADER_LIST_SIZE) {
                ObjectBigArrays.quickSort(kArr, this.from, this.to, this.comp);
                return;
            }
            long j6 = this.from + (j5 / 2);
            long j7 = this.from;
            long j8 = this.to - serialVersionUID;
            long j9 = j5 / 8;
            long med3 = ObjectBigArrays.med3(kArr, j7, j7 + j9, j7 + (j9 * 2), this.comp);
            long med32 = ObjectBigArrays.med3(kArr, j6 - j9, j6, j6 + j9, this.comp);
            long med33 = ObjectBigArrays.med3(kArr, j8 - (2 * j9), j8 - j9, j8, this.comp);
            Object obj = BigArrays.get(kArr, ObjectBigArrays.med3(kArr, med3, med32, med33, this.comp));
            long j10 = this.from;
            long j11 = this.to - serialVersionUID;
            long j12 = j11;
            long j13 = j11;
            long j14 = j10;
            long j15 = j10;
            while (true) {
                if (j15 <= j13) {
                    j = j13;
                    int compare = this.comp.compare(BigArrays.get(kArr, j15), obj);
                    if (compare <= 0) {
                        if (compare == 0) {
                            long j16 = j14 + serialVersionUID;
                            BigArrays.swap(kArr, j14, j15);
                            j14 = j16;
                        }
                        j15 += serialVersionUID;
                        j13 = j;
                    }
                } else {
                    j = j13;
                }
                j2 = j;
                long j17 = j5;
                j3 = j12;
                while (true) {
                    if (j2 >= j15) {
                        j4 = med33;
                        int compare2 = this.comp.compare(BigArrays.get(kArr, j2), obj);
                        if (compare2 < 0) {
                            break;
                        }
                        if (compare2 == 0) {
                            long j18 = j3 - serialVersionUID;
                            BigArrays.swap(kArr, j2, j3);
                            j3 = j18;
                        }
                        j2 -= serialVersionUID;
                        med33 = j4;
                    } else {
                        j4 = med33;
                        break;
                    }
                }
                if (j15 > j2) {
                    break;
                }
                long j19 = j2;
                long j20 = j3;
                long j21 = j15;
                j15 = j21 + serialVersionUID;
                j13 = j19 - serialVersionUID;
                BigArrays.swap(kArr, j21, j19);
                j5 = j17;
                med33 = j4;
                j14 = j14;
                j12 = j20;
            }
            long j22 = j2;
            long min = Math.min(j14 - this.from, j15 - j14);
            long j23 = j15;
            ObjectBigArrays.swap(kArr, this.from, j15 - min, min);
            long min2 = Math.min(j3 - j22, (this.to - j3) - serialVersionUID);
            ObjectBigArrays.swap(kArr, j23, this.to - min2, min2);
            long j24 = j23 - j14;
            long j25 = j3 - j22;
            if (j24 > serialVersionUID && j25 > serialVersionUID) {
                invokeAll(new ForkJoinQuickSortComp(kArr, this.from, this.from + j24, this.comp), new ForkJoinQuickSortComp(kArr, this.to - j25, this.to, this.comp));
            } else if (j24 <= serialVersionUID) {
                invokeAll(new ForkJoinTask[]{new ForkJoinQuickSortComp(kArr, this.to - j25, this.to, this.comp)});
            } else {
                invokeAll(new ForkJoinTask[]{new ForkJoinQuickSortComp(kArr, this.from, this.from + j24, this.comp)});
            }
        }
    }

    public static <K> void parallelQuickSort(K[][] x, long from, long to, Comparator<K> comp) {
        ForkJoinPool pool = getPool();
        if (to - from < Http2CodecUtil.DEFAULT_HEADER_LIST_SIZE || pool.getParallelism() == 1) {
            quickSort(x, from, to, comp);
        } else {
            pool.invoke(new ForkJoinQuickSortComp(x, from, to, comp));
        }
    }

    public static <K> void parallelQuickSort(K[][] x, Comparator<K> comp) {
        parallelQuickSort(x, 0L, BigArrays.length(x), comp);
    }

    public static <K> long binarySearch(K[][] a, long from, long to, K key) {
        long to2 = to - 1;
        while (from <= to2) {
            long mid = (from + to2) >>> 1;
            int cmp = ((Comparable) BigArrays.get(a, mid)).compareTo(key);
            if (cmp < 0) {
                from = mid + 1;
            } else if (cmp > 0) {
                to2 = mid - 1;
            } else {
                return mid;
            }
        }
        return -(1 + from);
    }

    public static <K> long binarySearch(K[][] a, Object key) {
        return binarySearch(a, 0L, BigArrays.length(a), key);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <K> long binarySearch(K[][] a, long from, long to, K key, Comparator<K> comparator) {
        long to2 = to - 1;
        while (from <= to2) {
            long mid = (from + to2) >>> 1;
            int cmp = comparator.compare(BigArrays.get(a, mid), key);
            if (cmp < 0) {
                from = mid + 1;
            } else if (cmp > 0) {
                to2 = mid - 1;
            } else {
                return mid;
            }
        }
        return -(1 + from);
    }

    public static <K> long binarySearch(K[][] a, K key, Comparator<K> c) {
        return binarySearch(a, 0L, BigArrays.length(a), key, c);
    }

    public static <K> K[][] shuffle(K[][] kArr, long j, long j2, Random random) {
        return (K[][]) BigArrays.shuffle(kArr, j, j2, random);
    }

    public static <K> K[][] shuffle(K[][] kArr, Random random) {
        return (K[][]) BigArrays.shuffle(kArr, random);
    }
}
