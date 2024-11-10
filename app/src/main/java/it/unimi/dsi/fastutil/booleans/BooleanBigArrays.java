package it.unimi.dsi.fastutil.booleans;

import io.netty.handler.codec.http2.Http2CodecUtil;
import it.unimi.dsi.fastutil.BigArrays;
import it.unimi.dsi.fastutil.Hash;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

/* loaded from: classes4.dex */
public final class BooleanBigArrays {
    private static final int MEDIUM = 40;
    private static final int PARALLEL_QUICKSORT_NO_FORK = 8192;
    private static final int QUICKSORT_NO_REC = 7;
    public static final boolean[][] EMPTY_BIG_ARRAY = new boolean[0];
    public static final boolean[][] DEFAULT_EMPTY_BIG_ARRAY = new boolean[0];
    public static final Hash.Strategy HASH_STRATEGY = new BigArrayHashStrategy();

    private BooleanBigArrays() {
    }

    @Deprecated
    public static boolean get(boolean[][] array, long index) {
        return array[BigArrays.segment(index)][BigArrays.displacement(index)];
    }

    @Deprecated
    public static void set(boolean[][] array, long index, boolean value) {
        array[BigArrays.segment(index)][BigArrays.displacement(index)] = value;
    }

    @Deprecated
    public static void swap(boolean[][] array, long first, long second) {
        boolean t = array[BigArrays.segment(first)][BigArrays.displacement(first)];
        array[BigArrays.segment(first)][BigArrays.displacement(first)] = array[BigArrays.segment(second)][BigArrays.displacement(second)];
        array[BigArrays.segment(second)][BigArrays.displacement(second)] = t;
    }

    @Deprecated
    public static long length(boolean[][] array) {
        int length = array.length;
        if (length == 0) {
            return 0L;
        }
        return BigArrays.start(length - 1) + array[length - 1].length;
    }

    @Deprecated
    public static void copy(boolean[][] srcArray, long srcPos, boolean[][] destArray, long destPos, long length) {
        BigArrays.copy(srcArray, srcPos, destArray, destPos, length);
    }

    @Deprecated
    public static void copyFromBig(boolean[][] srcArray, long srcPos, boolean[] destArray, int destPos, int length) {
        BigArrays.copyFromBig(srcArray, srcPos, destArray, destPos, length);
    }

    @Deprecated
    public static void copyToBig(boolean[] srcArray, int srcPos, boolean[][] destArray, long destPos, long length) {
        BigArrays.copyToBig(srcArray, srcPos, destArray, destPos, length);
    }

    public static boolean[][] newBigArray(long length) {
        if (length == 0) {
            return EMPTY_BIG_ARRAY;
        }
        BigArrays.ensureLength(length);
        int baseLength = (int) ((length + 134217727) >>> 27);
        boolean[][] base = new boolean[baseLength];
        int residual = (int) (134217727 & length);
        if (residual != 0) {
            for (int i = 0; i < baseLength - 1; i++) {
                base[i] = new boolean[BigArrays.SEGMENT_SIZE];
            }
            base[baseLength - 1] = new boolean[residual];
        } else {
            for (int i2 = 0; i2 < baseLength; i2++) {
                base[i2] = new boolean[BigArrays.SEGMENT_SIZE];
            }
        }
        return base;
    }

    @Deprecated
    public static boolean[][] wrap(boolean[] array) {
        return BigArrays.wrap(array);
    }

    @Deprecated
    public static boolean[][] ensureCapacity(boolean[][] array, long length) {
        return ensureCapacity(array, length, length(array));
    }

    @Deprecated
    public static boolean[][] forceCapacity(boolean[][] array, long length, long preserve) {
        return BigArrays.forceCapacity(array, length, preserve);
    }

    @Deprecated
    public static boolean[][] ensureCapacity(boolean[][] array, long length, long preserve) {
        return length > length(array) ? forceCapacity(array, length, preserve) : array;
    }

    @Deprecated
    public static boolean[][] grow(boolean[][] array, long length) {
        long oldLength = length(array);
        return length > oldLength ? grow(array, length, oldLength) : array;
    }

    @Deprecated
    public static boolean[][] grow(boolean[][] array, long length, long preserve) {
        long oldLength = length(array);
        return length > oldLength ? ensureCapacity(array, Math.max((oldLength >> 1) + oldLength, length), preserve) : array;
    }

    @Deprecated
    public static boolean[][] trim(boolean[][] array, long length) {
        BigArrays.ensureLength(length);
        long oldLength = length(array);
        if (length >= oldLength) {
            return array;
        }
        int baseLength = (int) ((length + 134217727) >>> 27);
        boolean[][] base = (boolean[][]) Arrays.copyOf(array, baseLength);
        int residual = (int) (134217727 & length);
        if (residual != 0) {
            base[baseLength - 1] = BooleanArrays.trim(base[baseLength - 1], residual);
        }
        return base;
    }

    @Deprecated
    public static boolean[][] setLength(boolean[][] array, long length) {
        return BigArrays.setLength(array, length);
    }

    @Deprecated
    public static boolean[][] copy(boolean[][] array, long offset, long length) {
        return BigArrays.copy(array, offset, length);
    }

    @Deprecated
    public static boolean[][] copy(boolean[][] array) {
        return BigArrays.copy(array);
    }

    @Deprecated
    public static void fill(boolean[][] array, boolean value) {
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
    public static void fill(boolean[][] array, long from, long to, boolean value) {
        BigArrays.fill(array, from, to, value);
    }

    @Deprecated
    public static boolean equals(boolean[][] a1, boolean[][] a2) {
        return BigArrays.equals(a1, a2);
    }

    @Deprecated
    public static String toString(boolean[][] a) {
        return BigArrays.toString(a);
    }

    @Deprecated
    public static void ensureFromTo(boolean[][] a, long from, long to) {
        BigArrays.ensureFromTo(length(a), from, to);
    }

    @Deprecated
    public static void ensureOffsetLength(boolean[][] a, long offset, long length) {
        BigArrays.ensureOffsetLength(length(a), offset, length);
    }

    @Deprecated
    public static void ensureSameLength(boolean[][] a, boolean[][] b) {
        if (length(a) != length(b)) {
            throw new IllegalArgumentException("Array size mismatch: " + length(a) + " != " + length(b));
        }
    }

    /* loaded from: classes4.dex */
    private static final class BigArrayHashStrategy implements Hash.Strategy<boolean[][]>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;

        private BigArrayHashStrategy() {
        }

        @Override // it.unimi.dsi.fastutil.Hash.Strategy
        public int hashCode(boolean[][] o) {
            return Arrays.deepHashCode(o);
        }

        @Override // it.unimi.dsi.fastutil.Hash.Strategy
        public boolean equals(boolean[][] a, boolean[][] b) {
            return BooleanBigArrays.equals(a, b);
        }
    }

    private static ForkJoinPool getPool() {
        ForkJoinPool current = ForkJoinTask.getPool();
        return current == null ? ForkJoinPool.commonPool() : current;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void swap(boolean[][] x, long a, long b, long n) {
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
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static long med3(boolean[][] r5, long r6, long r8, long r10, it.unimi.dsi.fastutil.booleans.BooleanComparator r12) {
        /*
            boolean r0 = it.unimi.dsi.fastutil.BigArrays.get(r5, r6)
            boolean r1 = it.unimi.dsi.fastutil.BigArrays.get(r5, r8)
            int r0 = r12.compare(r0, r1)
            boolean r1 = it.unimi.dsi.fastutil.BigArrays.get(r5, r6)
            boolean r2 = it.unimi.dsi.fastutil.BigArrays.get(r5, r10)
            int r1 = r12.compare(r1, r2)
            boolean r2 = it.unimi.dsi.fastutil.BigArrays.get(r5, r8)
            boolean r3 = it.unimi.dsi.fastutil.BigArrays.get(r5, r10)
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
        throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.booleans.BooleanBigArrays.med3(boolean[][], long, long, long, it.unimi.dsi.fastutil.booleans.BooleanComparator):long");
    }

    private static void selectionSort(boolean[][] a, long from, long to, BooleanComparator comp) {
        for (long i = from; i < to - 1; i++) {
            long m = i;
            for (long j = i + 1; j < to; j++) {
                if (comp.compare(BigArrays.get(a, j), BigArrays.get(a, m)) < 0) {
                    m = j;
                }
            }
            if (m != i) {
                BigArrays.swap(a, i, m);
            }
        }
    }

    public static void quickSort(boolean[][] x, long from, long to, BooleanComparator comp) {
        long d;
        long c;
        int comparison;
        int comparison2;
        BooleanComparator booleanComparator = comp;
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
        boolean v = BigArrays.get(x, m);
        long c2 = to - 1;
        long c3 = c2;
        long a = from;
        long b = c2;
        long b2 = from;
        while (true) {
            if (b2 <= b && (comparison2 = booleanComparator.compare(BigArrays.get(x, b2), v)) <= 0) {
                if (comparison2 == 0) {
                    BigArrays.swap(x, a, b2);
                    a++;
                }
                b2++;
            } else {
                d = c3;
                c = b;
                while (c >= b2 && (comparison = booleanComparator.compare(BigArrays.get(x, c), v)) >= 0) {
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
                booleanComparator = comp;
                b2 = b3 + 1;
                b = c5;
                v = v;
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
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0030, code lost:            if (r1 > 0) goto L11;     */
    /* JADX WARN: Code restructure failed: missing block: B:4:0x0029, code lost:            if (r1 < 0) goto L11;     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x0035, code lost:            return r6;     */
    /* JADX WARN: Code restructure failed: missing block: B:9:?, code lost:            return r10;     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static long med3(boolean[][] r5, long r6, long r8, long r10) {
        /*
            boolean r0 = it.unimi.dsi.fastutil.BigArrays.get(r5, r6)
            boolean r1 = it.unimi.dsi.fastutil.BigArrays.get(r5, r8)
            int r0 = java.lang.Boolean.compare(r0, r1)
            boolean r1 = it.unimi.dsi.fastutil.BigArrays.get(r5, r6)
            boolean r2 = it.unimi.dsi.fastutil.BigArrays.get(r5, r10)
            int r1 = java.lang.Boolean.compare(r1, r2)
            boolean r2 = it.unimi.dsi.fastutil.BigArrays.get(r5, r8)
            boolean r3 = it.unimi.dsi.fastutil.BigArrays.get(r5, r10)
            int r2 = java.lang.Boolean.compare(r2, r3)
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
        throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.booleans.BooleanBigArrays.med3(boolean[][], long, long, long):long");
    }

    private static void selectionSort(boolean[][] a, long from, long to) {
        for (long i = from; i < to - 1; i++) {
            long m = i;
            for (long j = i + 1; j < to; j++) {
                if (!BigArrays.get(a, j) && BigArrays.get(a, m)) {
                    m = j;
                }
            }
            if (m != i) {
                BigArrays.swap(a, i, m);
            }
        }
    }

    public static void quickSort(boolean[][] x, BooleanComparator comp) {
        quickSort(x, 0L, BigArrays.length(x), comp);
    }

    public static void quickSort(boolean[][] x, long from, long to) {
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
        boolean v = BigArrays.get(x, m);
        long c2 = to - 1;
        long len = c2;
        long b = c2;
        long a = from;
        long b2 = from;
        while (true) {
            if (b2 <= b && (comparison2 = Boolean.compare(BigArrays.get(x, b2), v)) <= 0) {
                if (comparison2 == 0) {
                    BigArrays.swap(x, a, b2);
                    a++;
                }
                b2++;
            } else {
                c = b;
                long len2 = d2;
                d = len;
                while (c >= b2 && (comparison = Boolean.compare(BigArrays.get(x, c), v)) >= 0) {
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

    public static void quickSort(boolean[][] x) {
        quickSort(x, 0L, BigArrays.length(x));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public static class ForkJoinQuickSort extends RecursiveAction {
        private static final long serialVersionUID = 1;
        private final long from;
        private final long to;
        private final boolean[][] x;

        public ForkJoinQuickSort(boolean[][] x, long from, long to) {
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
            boolean[][] x = this.x;
            long len = this.to - this.from;
            if (len < Http2CodecUtil.DEFAULT_HEADER_LIST_SIZE) {
                BooleanBigArrays.quickSort(x, this.from, this.to);
                return;
            }
            long m = this.from + (len / 2);
            long l = this.from;
            long n = this.to - serialVersionUID;
            long s = len / 8;
            long l2 = BooleanBigArrays.med3(x, l, l + s, l + (s * 2));
            long m2 = BooleanBigArrays.med3(x, m - s, m, m + s);
            long d2 = BooleanBigArrays.med3(x, n - (2 * s), n - s, n);
            long m3 = BooleanBigArrays.med3(x, l2, m2, d2);
            boolean v = BigArrays.get(x, m3);
            long a = this.from;
            long c2 = this.to - serialVersionUID;
            long n2 = c2;
            long a2 = a;
            long len2 = a;
            while (true) {
                if (len2 <= c2 && (comparison2 = Boolean.compare(BigArrays.get(x, len2), v)) <= 0) {
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
                    while (c >= len2 && (comparison = Boolean.compare(BigArrays.get(x, c), v)) >= 0) {
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
            BooleanBigArrays.swap(x, this.from, len2 - s2, s2);
            long s3 = Math.min(d - c3, (this.to - d) - serialVersionUID);
            BooleanBigArrays.swap(x, len2, this.to - s3, s3);
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

    public static void parallelQuickSort(boolean[][] x, long from, long to) {
        ForkJoinPool pool = getPool();
        if (to - from < Http2CodecUtil.DEFAULT_HEADER_LIST_SIZE || pool.getParallelism() == 1) {
            quickSort(x, from, to);
        } else {
            pool.invoke(new ForkJoinQuickSort(x, from, to));
        }
    }

    public static void parallelQuickSort(boolean[][] x) {
        parallelQuickSort(x, 0L, BigArrays.length(x));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public static class ForkJoinQuickSortComp extends RecursiveAction {
        private static final long serialVersionUID = 1;
        private final BooleanComparator comp;
        private final long from;
        private final long to;
        private final boolean[][] x;

        public ForkJoinQuickSortComp(boolean[][] x, long from, long to, BooleanComparator comp) {
            this.from = from;
            this.to = to;
            this.x = x;
            this.comp = comp;
        }

        @Override // java.util.concurrent.RecursiveAction
        protected void compute() {
            long c;
            long c2;
            long d;
            long n;
            boolean[][] x = this.x;
            long len = this.to - this.from;
            if (len < Http2CodecUtil.DEFAULT_HEADER_LIST_SIZE) {
                BooleanBigArrays.quickSort(x, this.from, this.to, this.comp);
                return;
            }
            long m = this.from + (len / 2);
            long l = this.from;
            long n2 = this.to - serialVersionUID;
            long s = len / 8;
            long l2 = BooleanBigArrays.med3(x, l, l + s, l + (s * 2), this.comp);
            long m2 = BooleanBigArrays.med3(x, m - s, m, m + s, this.comp);
            long n3 = BooleanBigArrays.med3(x, n2 - (2 * s), n2 - s, n2, this.comp);
            boolean v = BigArrays.get(x, BooleanBigArrays.med3(x, l2, m2, n3, this.comp));
            long a = this.from;
            long c3 = this.to - serialVersionUID;
            long len2 = c3;
            long a2 = c3;
            long a3 = a;
            long b = a;
            while (true) {
                if (b <= a2) {
                    c = a2;
                    int comparison = this.comp.compare(BigArrays.get(x, b), v);
                    if (comparison <= 0) {
                        if (comparison == 0) {
                            long a4 = a3 + serialVersionUID;
                            BigArrays.swap(x, a3, b);
                            a3 = a4;
                        }
                        b += serialVersionUID;
                        a2 = c;
                    }
                } else {
                    c = a2;
                }
                c2 = c;
                long len3 = len;
                d = len2;
                while (true) {
                    if (c2 >= b) {
                        n = n3;
                        int comparison2 = this.comp.compare(BigArrays.get(x, c2), v);
                        if (comparison2 < 0) {
                            break;
                        }
                        if (comparison2 == 0) {
                            long d2 = d - serialVersionUID;
                            BigArrays.swap(x, c2, d);
                            d = d2;
                        }
                        c2 -= serialVersionUID;
                        n3 = n;
                    } else {
                        n = n3;
                        break;
                    }
                }
                if (b > c2) {
                    break;
                }
                long c4 = c2;
                long d3 = d;
                long d4 = b;
                b = d4 + serialVersionUID;
                a2 = c4 - serialVersionUID;
                BigArrays.swap(x, d4, c4);
                len = len3;
                n3 = n;
                a3 = a3;
                len2 = d3;
            }
            long c5 = c2;
            long s2 = Math.min(a3 - this.from, b - a3);
            long c6 = b;
            BooleanBigArrays.swap(x, this.from, b - s2, s2);
            long s3 = Math.min(d - c5, (this.to - d) - serialVersionUID);
            BooleanBigArrays.swap(x, c6, this.to - s3, s3);
            long s4 = c6 - a3;
            long t = d - c5;
            if (s4 > serialVersionUID && t > serialVersionUID) {
                invokeAll(new ForkJoinQuickSortComp(x, this.from, this.from + s4, this.comp), new ForkJoinQuickSortComp(x, this.to - t, this.to, this.comp));
            } else if (s4 <= serialVersionUID) {
                invokeAll(new ForkJoinTask[]{new ForkJoinQuickSortComp(x, this.to - t, this.to, this.comp)});
            } else {
                invokeAll(new ForkJoinTask[]{new ForkJoinQuickSortComp(x, this.from, this.from + s4, this.comp)});
            }
        }
    }

    public static void parallelQuickSort(boolean[][] x, long from, long to, BooleanComparator comp) {
        ForkJoinPool pool = getPool();
        if (to - from < Http2CodecUtil.DEFAULT_HEADER_LIST_SIZE || pool.getParallelism() == 1) {
            quickSort(x, from, to, comp);
        } else {
            pool.invoke(new ForkJoinQuickSortComp(x, from, to, comp));
        }
    }

    public static void parallelQuickSort(boolean[][] x, BooleanComparator comp) {
        parallelQuickSort(x, 0L, BigArrays.length(x), comp);
    }

    public static boolean[][] shuffle(boolean[][] a, long from, long to, Random random) {
        return BigArrays.shuffle(a, from, to, random);
    }

    public static boolean[][] shuffle(boolean[][] a, Random random) {
        return BigArrays.shuffle(a, random);
    }
}
