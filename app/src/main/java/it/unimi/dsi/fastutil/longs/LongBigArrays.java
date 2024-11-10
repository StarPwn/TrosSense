package it.unimi.dsi.fastutil.longs;

import io.netty.handler.codec.http2.Http2CodecUtil;
import it.unimi.dsi.fastutil.BigArrays;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.bytes.ByteBigArrays;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicLongArray;
import okhttp3.internal.ws.RealWebSocket;

/* loaded from: classes4.dex */
public final class LongBigArrays {
    private static final int DIGITS_PER_ELEMENT = 8;
    private static final int DIGIT_BITS = 8;
    private static final int DIGIT_MASK = 255;
    private static final int MEDIUM = 40;
    private static final int PARALLEL_QUICKSORT_NO_FORK = 8192;
    private static final int QUICKSORT_NO_REC = 7;
    private static final int RADIXSORT_NO_REC = 1024;
    public static final long[][] EMPTY_BIG_ARRAY = new long[0];
    public static final long[][] DEFAULT_EMPTY_BIG_ARRAY = new long[0];
    public static final AtomicLongArray[] EMPTY_BIG_ATOMIC_ARRAY = new AtomicLongArray[0];
    public static final Hash.Strategy HASH_STRATEGY = new BigArrayHashStrategy();

    private LongBigArrays() {
    }

    @Deprecated
    public static long get(long[][] array, long index) {
        return array[BigArrays.segment(index)][BigArrays.displacement(index)];
    }

    @Deprecated
    public static void set(long[][] array, long index, long value) {
        array[BigArrays.segment(index)][BigArrays.displacement(index)] = value;
    }

    @Deprecated
    public static void swap(long[][] array, long first, long second) {
        long t = array[BigArrays.segment(first)][BigArrays.displacement(first)];
        array[BigArrays.segment(first)][BigArrays.displacement(first)] = array[BigArrays.segment(second)][BigArrays.displacement(second)];
        array[BigArrays.segment(second)][BigArrays.displacement(second)] = t;
    }

    @Deprecated
    public static void add(long[][] array, long index, long incr) {
        long[] jArr = array[BigArrays.segment(index)];
        int displacement = BigArrays.displacement(index);
        jArr[displacement] = jArr[displacement] + incr;
    }

    @Deprecated
    public static void mul(long[][] array, long index, long factor) {
        long[] jArr = array[BigArrays.segment(index)];
        int displacement = BigArrays.displacement(index);
        jArr[displacement] = jArr[displacement] * factor;
    }

    @Deprecated
    public static void incr(long[][] array, long index) {
        long[] jArr = array[BigArrays.segment(index)];
        int displacement = BigArrays.displacement(index);
        jArr[displacement] = jArr[displacement] + 1;
    }

    @Deprecated
    public static void decr(long[][] array, long index) {
        long[] jArr = array[BigArrays.segment(index)];
        int displacement = BigArrays.displacement(index);
        jArr[displacement] = jArr[displacement] - 1;
    }

    @Deprecated
    public static long length(long[][] array) {
        int length = array.length;
        if (length == 0) {
            return 0L;
        }
        return BigArrays.start(length - 1) + array[length - 1].length;
    }

    @Deprecated
    public static void copy(long[][] srcArray, long srcPos, long[][] destArray, long destPos, long length) {
        BigArrays.copy(srcArray, srcPos, destArray, destPos, length);
    }

    @Deprecated
    public static void copyFromBig(long[][] srcArray, long srcPos, long[] destArray, int destPos, int length) {
        BigArrays.copyFromBig(srcArray, srcPos, destArray, destPos, length);
    }

    @Deprecated
    public static void copyToBig(long[] srcArray, int srcPos, long[][] destArray, long destPos, long length) {
        BigArrays.copyToBig(srcArray, srcPos, destArray, destPos, length);
    }

    public static long[][] newBigArray(long length) {
        if (length == 0) {
            return EMPTY_BIG_ARRAY;
        }
        BigArrays.ensureLength(length);
        int baseLength = (int) ((length + 134217727) >>> 27);
        long[][] base = new long[baseLength];
        int residual = (int) (134217727 & length);
        if (residual != 0) {
            for (int i = 0; i < baseLength - 1; i++) {
                base[i] = new long[BigArrays.SEGMENT_SIZE];
            }
            base[baseLength - 1] = new long[residual];
        } else {
            for (int i2 = 0; i2 < baseLength; i2++) {
                base[i2] = new long[BigArrays.SEGMENT_SIZE];
            }
        }
        return base;
    }

    public static AtomicLongArray[] newBigAtomicArray(long length) {
        if (length == 0) {
            return EMPTY_BIG_ATOMIC_ARRAY;
        }
        BigArrays.ensureLength(length);
        int baseLength = (int) ((length + 134217727) >>> 27);
        AtomicLongArray[] base = new AtomicLongArray[baseLength];
        int residual = (int) (134217727 & length);
        if (residual != 0) {
            for (int i = 0; i < baseLength - 1; i++) {
                base[i] = new AtomicLongArray(BigArrays.SEGMENT_SIZE);
            }
            base[baseLength - 1] = new AtomicLongArray(residual);
        } else {
            for (int i2 = 0; i2 < baseLength; i2++) {
                base[i2] = new AtomicLongArray(BigArrays.SEGMENT_SIZE);
            }
        }
        return base;
    }

    @Deprecated
    public static long[][] wrap(long[] array) {
        return BigArrays.wrap(array);
    }

    @Deprecated
    public static long[][] ensureCapacity(long[][] array, long length) {
        return ensureCapacity(array, length, length(array));
    }

    @Deprecated
    public static long[][] forceCapacity(long[][] array, long length, long preserve) {
        return BigArrays.forceCapacity(array, length, preserve);
    }

    @Deprecated
    public static long[][] ensureCapacity(long[][] array, long length, long preserve) {
        return length > length(array) ? forceCapacity(array, length, preserve) : array;
    }

    @Deprecated
    public static long[][] grow(long[][] array, long length) {
        long oldLength = length(array);
        return length > oldLength ? grow(array, length, oldLength) : array;
    }

    @Deprecated
    public static long[][] grow(long[][] array, long length, long preserve) {
        long oldLength = length(array);
        return length > oldLength ? ensureCapacity(array, Math.max((oldLength >> 1) + oldLength, length), preserve) : array;
    }

    @Deprecated
    public static long[][] trim(long[][] array, long length) {
        BigArrays.ensureLength(length);
        long oldLength = length(array);
        if (length >= oldLength) {
            return array;
        }
        int baseLength = (int) ((length + 134217727) >>> 27);
        long[][] base = (long[][]) Arrays.copyOf(array, baseLength);
        int residual = (int) (134217727 & length);
        if (residual != 0) {
            base[baseLength - 1] = LongArrays.trim(base[baseLength - 1], residual);
        }
        return base;
    }

    @Deprecated
    public static long[][] setLength(long[][] array, long length) {
        return BigArrays.setLength(array, length);
    }

    @Deprecated
    public static long[][] copy(long[][] array, long offset, long length) {
        return BigArrays.copy(array, offset, length);
    }

    @Deprecated
    public static long[][] copy(long[][] array) {
        return BigArrays.copy(array);
    }

    @Deprecated
    public static void fill(long[][] array, long value) {
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
    public static void fill(long[][] array, long from, long to, long value) {
        BigArrays.fill(array, from, to, value);
    }

    @Deprecated
    public static boolean equals(long[][] a1, long[][] a2) {
        return BigArrays.equals(a1, a2);
    }

    @Deprecated
    public static String toString(long[][] a) {
        return BigArrays.toString(a);
    }

    @Deprecated
    public static void ensureFromTo(long[][] a, long from, long to) {
        BigArrays.ensureFromTo(length(a), from, to);
    }

    @Deprecated
    public static void ensureOffsetLength(long[][] a, long offset, long length) {
        BigArrays.ensureOffsetLength(length(a), offset, length);
    }

    @Deprecated
    public static void ensureSameLength(long[][] a, long[][] b) {
        if (length(a) != length(b)) {
            throw new IllegalArgumentException("Array size mismatch: " + length(a) + " != " + length(b));
        }
    }

    /* loaded from: classes4.dex */
    private static final class BigArrayHashStrategy implements Hash.Strategy<long[][]>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;

        private BigArrayHashStrategy() {
        }

        @Override // it.unimi.dsi.fastutil.Hash.Strategy
        public int hashCode(long[][] o) {
            return Arrays.deepHashCode(o);
        }

        @Override // it.unimi.dsi.fastutil.Hash.Strategy
        public boolean equals(long[][] a, long[][] b) {
            return LongBigArrays.equals(a, b);
        }
    }

    private static ForkJoinPool getPool() {
        ForkJoinPool current = ForkJoinTask.getPool();
        return current == null ? ForkJoinPool.commonPool() : current;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void swap(long[][] x, long a, long b, long n) {
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
    /* JADX WARN: Code restructure failed: missing block: B:6:0x0035, code lost:            return r7;     */
    /* JADX WARN: Code restructure failed: missing block: B:9:?, code lost:            return r11;     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static long med3(long[][] r6, long r7, long r9, long r11, it.unimi.dsi.fastutil.longs.LongComparator r13) {
        /*
            long r0 = it.unimi.dsi.fastutil.BigArrays.get(r6, r7)
            long r2 = it.unimi.dsi.fastutil.BigArrays.get(r6, r9)
            int r0 = r13.compare(r0, r2)
            long r1 = it.unimi.dsi.fastutil.BigArrays.get(r6, r7)
            long r3 = it.unimi.dsi.fastutil.BigArrays.get(r6, r11)
            int r1 = r13.compare(r1, r3)
            long r2 = it.unimi.dsi.fastutil.BigArrays.get(r6, r9)
            long r4 = it.unimi.dsi.fastutil.BigArrays.get(r6, r11)
            int r2 = r13.compare(r2, r4)
            if (r0 >= 0) goto L2c
            if (r2 >= 0) goto L29
            goto L2e
        L29:
            if (r1 >= 0) goto L34
            goto L32
        L2c:
            if (r2 <= 0) goto L30
        L2e:
            r3 = r9
            goto L35
        L30:
            if (r1 <= 0) goto L34
        L32:
            r3 = r11
            goto L35
        L34:
            r3 = r7
        L35:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.longs.LongBigArrays.med3(long[][], long, long, long, it.unimi.dsi.fastutil.longs.LongComparator):long");
    }

    private static void selectionSort(long[][] a, long from, long to, LongComparator comp) {
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

    public static void quickSort(long[][] x, long from, long to, LongComparator comp) {
        long c;
        long c2;
        long d;
        long m;
        long[][] jArr = x;
        long d2 = to - from;
        if (d2 < 7) {
            selectionSort(x, from, to, comp);
            return;
        }
        long m2 = from + (d2 / 2);
        if (d2 > 7) {
            long l = from;
            long n = to - 1;
            if (d2 > 40) {
                long s = d2 / 8;
                l = med3(x, l, l + s, l + (s * 2), comp);
                m2 = med3(x, m2 - s, m2, m2 + s, comp);
                n = med3(x, n - (2 * s), n - s, n, comp);
            }
            m2 = med3(x, l, m2, n, comp);
        }
        long v = BigArrays.get(jArr, m2);
        long c3 = to - 1;
        long len = c3;
        long a = c3;
        long a2 = from;
        long b = from;
        while (true) {
            if (b <= a) {
                c = a;
                int comparison = comp.compare(BigArrays.get(jArr, b), v);
                if (comparison <= 0) {
                    if (comparison == 0) {
                        BigArrays.swap(jArr, a2, b);
                        a2++;
                    }
                    b++;
                    a = c;
                }
            } else {
                c = a;
            }
            c2 = c;
            long len2 = d2;
            d = len;
            while (true) {
                if (c2 < b) {
                    m = m2;
                    break;
                }
                m = m2;
                int comparison2 = comp.compare(BigArrays.get(jArr, c2), v);
                if (comparison2 < 0) {
                    break;
                }
                if (comparison2 == 0) {
                    BigArrays.swap(jArr, c2, d);
                    d--;
                }
                c2--;
                m2 = m;
            }
            if (b > c2) {
                break;
            }
            long c4 = c2;
            long v2 = v;
            long v3 = b;
            b = v3 + 1;
            a = c4 - 1;
            BigArrays.swap(x, v3, c4);
            jArr = x;
            m2 = m;
            v = v2;
            a2 = a2;
            long j = d;
            d2 = len2;
            len = j;
        }
        long c5 = c2;
        long v4 = b - a2;
        long s2 = Math.min(a2 - from, v4);
        long a3 = b - s2;
        long b2 = b;
        swap(x, from, a3, s2);
        long s3 = Math.min(d - c5, (to - d) - 1);
        swap(x, b2, to - s3, s3);
        long s4 = b2 - a2;
        if (s4 > 1) {
            quickSort(x, from, from + s4, comp);
        }
        long s5 = d - c5;
        if (s5 > 1) {
            quickSort(x, to - s5, to, comp);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0030, code lost:            if (r1 > 0) goto L11;     */
    /* JADX WARN: Code restructure failed: missing block: B:4:0x0029, code lost:            if (r1 < 0) goto L11;     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x0035, code lost:            return r7;     */
    /* JADX WARN: Code restructure failed: missing block: B:9:?, code lost:            return r11;     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static long med3(long[][] r6, long r7, long r9, long r11) {
        /*
            long r0 = it.unimi.dsi.fastutil.BigArrays.get(r6, r7)
            long r2 = it.unimi.dsi.fastutil.BigArrays.get(r6, r9)
            int r0 = java.lang.Long.compare(r0, r2)
            long r1 = it.unimi.dsi.fastutil.BigArrays.get(r6, r7)
            long r3 = it.unimi.dsi.fastutil.BigArrays.get(r6, r11)
            int r1 = java.lang.Long.compare(r1, r3)
            long r2 = it.unimi.dsi.fastutil.BigArrays.get(r6, r9)
            long r4 = it.unimi.dsi.fastutil.BigArrays.get(r6, r11)
            int r2 = java.lang.Long.compare(r2, r4)
            if (r0 >= 0) goto L2c
            if (r2 >= 0) goto L29
            goto L2e
        L29:
            if (r1 >= 0) goto L34
            goto L32
        L2c:
            if (r2 <= 0) goto L30
        L2e:
            r3 = r9
            goto L35
        L30:
            if (r1 <= 0) goto L34
        L32:
            r3 = r11
            goto L35
        L34:
            r3 = r7
        L35:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.longs.LongBigArrays.med3(long[][], long, long, long):long");
    }

    private static void selectionSort(long[][] a, long from, long to) {
        for (long i = from; i < to - 1; i++) {
            long m = i;
            for (long j = i + 1; j < to; j++) {
                if (BigArrays.get(a, j) < BigArrays.get(a, m)) {
                    m = j;
                }
            }
            if (m != i) {
                BigArrays.swap(a, i, m);
            }
        }
    }

    public static void quickSort(long[][] x, LongComparator comp) {
        quickSort(x, 0L, BigArrays.length(x), comp);
    }

    public static void quickSort(long[][] x, long from, long to) {
        long c;
        long c2;
        long d;
        long m;
        long[][] jArr;
        long c3;
        long[][] jArr2 = x;
        long c4 = from;
        long d2 = to - c4;
        if (d2 < 7) {
            selectionSort(x, from, to);
            return;
        }
        long m2 = c4 + (d2 / 2);
        if (d2 > 7) {
            long l = from;
            long n = to - 1;
            if (d2 > 40) {
                long s = d2 / 8;
                l = med3(x, l, l + s, l + (s * 2));
                m2 = med3(x, m2 - s, m2, m2 + s);
                n = med3(x, n - (2 * s), n - s, n);
            }
            m2 = med3(x, l, m2, n);
        }
        long v = BigArrays.get(jArr2, m2);
        long c5 = to - 1;
        long len = c5;
        long a = c5;
        long a2 = from;
        long b = from;
        while (true) {
            if (b <= a) {
                c = a;
                int comparison = Long.compare(BigArrays.get(jArr2, b), v);
                if (comparison <= 0) {
                    if (comparison == 0) {
                        BigArrays.swap(jArr2, a2, b);
                        a2++;
                    }
                    b++;
                    a = c;
                }
            } else {
                c = a;
            }
            c2 = c;
            long len2 = d2;
            d = len;
            while (true) {
                if (c2 < b) {
                    m = m2;
                    break;
                }
                m = m2;
                int comparison2 = Long.compare(BigArrays.get(jArr2, c2), v);
                if (comparison2 < 0) {
                    break;
                }
                if (comparison2 == 0) {
                    BigArrays.swap(jArr2, c2, d);
                    d--;
                }
                c2--;
                m2 = m;
            }
            if (b > c2) {
                break;
            }
            long v2 = v;
            long[][] jArr3 = jArr2;
            long c6 = c2;
            long b2 = b;
            BigArrays.swap(jArr3, b2, c6);
            b = b2 + 1;
            jArr2 = jArr3;
            m2 = m;
            v = v2;
            a2 = a2;
            long j = d;
            d2 = len2;
            len = j;
            a = c6 - 1;
            c4 = c4;
        }
        long c7 = c2;
        long v3 = b - a2;
        long s2 = Math.min(a2 - c4, v3);
        long a3 = b - s2;
        long b3 = b;
        swap(x, from, a3, s2);
        long s3 = Math.min(d - c7, (to - d) - 1);
        swap(x, b3, to - s3, s3);
        long s4 = b3 - a2;
        if (s4 > 1) {
            c3 = c7;
            jArr = x;
            quickSort(jArr, from, from + s4);
        } else {
            jArr = x;
            c3 = c7;
        }
        long s5 = d - c3;
        if (s5 > 1) {
            quickSort(jArr, to - s5, to);
        }
    }

    public static void quickSort(long[][] x) {
        quickSort(x, 0L, BigArrays.length(x));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public static class ForkJoinQuickSort extends RecursiveAction {
        private static final long serialVersionUID = 1;
        private final long from;
        private final long to;
        private final long[][] x;

        public ForkJoinQuickSort(long[][] x, long from, long to) {
            this.from = from;
            this.to = to;
            this.x = x;
        }

        @Override // java.util.concurrent.RecursiveAction
        protected void compute() {
            long c;
            long c2;
            long d;
            long m;
            long[][] x = this.x;
            long len = this.to - this.from;
            if (len < Http2CodecUtil.DEFAULT_HEADER_LIST_SIZE) {
                LongBigArrays.quickSort(x, this.from, this.to);
                return;
            }
            long m2 = this.from + (len / 2);
            long l = this.from;
            long n = this.to - serialVersionUID;
            long s = len / 8;
            long l2 = LongBigArrays.med3(x, l, l + s, l + (s * 2));
            long m3 = LongBigArrays.med3(x, m2 - s, m2, m2 + s);
            long d2 = LongBigArrays.med3(x, n - (2 * s), n - s, n);
            long m4 = LongBigArrays.med3(x, l2, m3, d2);
            long v = BigArrays.get(x, m4);
            long a = this.from;
            long c3 = this.to - serialVersionUID;
            long n2 = c3;
            long a2 = a;
            long len2 = a;
            while (true) {
                if (len2 <= c3) {
                    c = c3;
                    int comparison = Long.compare(BigArrays.get(x, len2), v);
                    if (comparison <= 0) {
                        if (comparison == 0) {
                            long a3 = a2 + serialVersionUID;
                            BigArrays.swap(x, a2, len2);
                            a2 = a3;
                        }
                        len2 += serialVersionUID;
                        c3 = c;
                    }
                } else {
                    c = c3;
                }
                c2 = c;
                long n3 = d2;
                d = n2;
                while (true) {
                    if (c2 < len2) {
                        m = m4;
                        break;
                    }
                    m = m4;
                    int comparison2 = Long.compare(BigArrays.get(x, c2), v);
                    if (comparison2 < 0) {
                        break;
                    }
                    if (comparison2 == 0) {
                        long d3 = d - serialVersionUID;
                        BigArrays.swap(x, c2, d);
                        d = d3;
                    }
                    c2 -= serialVersionUID;
                    m4 = m;
                }
                if (len2 > c2) {
                    break;
                }
                long b = len2;
                long b2 = c2;
                long b3 = b + serialVersionUID;
                c3 = b2 - serialVersionUID;
                BigArrays.swap(x, b, b2);
                len2 = b3;
                m4 = m;
                a2 = a2;
                v = v;
                long j = d;
                d2 = n3;
                n2 = j;
            }
            long c4 = c2;
            long s2 = Math.min(a2 - this.from, len2 - a2);
            long b4 = len2;
            LongBigArrays.swap(x, this.from, len2 - s2, s2);
            long s3 = Math.min(d - c4, (this.to - d) - serialVersionUID);
            LongBigArrays.swap(x, b4, this.to - s3, s3);
            long s4 = b4 - a2;
            long t = d - c4;
            if (s4 > serialVersionUID && t > serialVersionUID) {
                invokeAll(new ForkJoinQuickSort(x, this.from, this.from + s4), new ForkJoinQuickSort(x, this.to - t, this.to));
            } else if (s4 <= serialVersionUID) {
                invokeAll(new ForkJoinTask[]{new ForkJoinQuickSort(x, this.to - t, this.to)});
            } else {
                invokeAll(new ForkJoinTask[]{new ForkJoinQuickSort(x, this.from, this.from + s4)});
            }
        }
    }

    public static void parallelQuickSort(long[][] x, long from, long to) {
        ForkJoinPool pool = getPool();
        if (to - from < Http2CodecUtil.DEFAULT_HEADER_LIST_SIZE || pool.getParallelism() == 1) {
            quickSort(x, from, to);
        } else {
            pool.invoke(new ForkJoinQuickSort(x, from, to));
        }
    }

    public static void parallelQuickSort(long[][] x) {
        parallelQuickSort(x, 0L, BigArrays.length(x));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public static class ForkJoinQuickSortComp extends RecursiveAction {
        private static final long serialVersionUID = 1;
        private final LongComparator comp;
        private final long from;
        private final long to;
        private final long[][] x;

        public ForkJoinQuickSortComp(long[][] x, long from, long to, LongComparator comp) {
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
            long m;
            long[][] x = this.x;
            long len = this.to - this.from;
            if (len < Http2CodecUtil.DEFAULT_HEADER_LIST_SIZE) {
                LongBigArrays.quickSort(x, this.from, this.to, this.comp);
                return;
            }
            long m2 = this.from + (len / 2);
            long l = this.from;
            long n = this.to - serialVersionUID;
            long s = len / 8;
            long l2 = LongBigArrays.med3(x, l, l + s, l + (s * 2), this.comp);
            long m3 = LongBigArrays.med3(x, m2 - s, m2, m2 + s, this.comp);
            long n2 = LongBigArrays.med3(x, n - (2 * s), n - s, n, this.comp);
            long m4 = LongBigArrays.med3(x, l2, m3, n2, this.comp);
            long v = BigArrays.get(x, m4);
            long a = this.from;
            long c3 = this.to - serialVersionUID;
            long n3 = c3;
            long a2 = a;
            long len2 = a;
            while (true) {
                if (len2 <= c3) {
                    c = c3;
                    int comparison = this.comp.compare(BigArrays.get(x, len2), v);
                    if (comparison <= 0) {
                        if (comparison == 0) {
                            long a3 = a2 + serialVersionUID;
                            BigArrays.swap(x, a2, len2);
                            a2 = a3;
                        }
                        len2 += serialVersionUID;
                        c3 = c;
                    }
                } else {
                    c = c3;
                }
                c2 = c;
                long n4 = n2;
                d = n3;
                while (true) {
                    if (c2 >= len2) {
                        m = m4;
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
                        m4 = m;
                    } else {
                        m = m4;
                        break;
                    }
                }
                if (len2 > c2) {
                    break;
                }
                long b = len2;
                long d3 = d;
                long b2 = c2;
                long b3 = b + serialVersionUID;
                c3 = b2 - serialVersionUID;
                BigArrays.swap(x, b, b2);
                len2 = b3;
                n2 = n4;
                m4 = m;
                a2 = a2;
                v = v;
                n3 = d3;
            }
            long c4 = c2;
            long s2 = Math.min(a2 - this.from, len2 - a2);
            long b4 = len2;
            LongBigArrays.swap(x, this.from, len2 - s2, s2);
            long s3 = Math.min(d - c4, (this.to - d) - serialVersionUID);
            LongBigArrays.swap(x, b4, this.to - s3, s3);
            long s4 = b4 - a2;
            long t = d - c4;
            if (s4 > serialVersionUID && t > serialVersionUID) {
                invokeAll(new ForkJoinQuickSortComp(x, this.from, this.from + s4, this.comp), new ForkJoinQuickSortComp(x, this.to - t, this.to, this.comp));
            } else if (s4 <= serialVersionUID) {
                invokeAll(new ForkJoinTask[]{new ForkJoinQuickSortComp(x, this.to - t, this.to, this.comp)});
            } else {
                invokeAll(new ForkJoinTask[]{new ForkJoinQuickSortComp(x, this.from, this.from + s4, this.comp)});
            }
        }
    }

    public static void parallelQuickSort(long[][] x, long from, long to, LongComparator comp) {
        ForkJoinPool pool = getPool();
        if (to - from < Http2CodecUtil.DEFAULT_HEADER_LIST_SIZE || pool.getParallelism() == 1) {
            quickSort(x, from, to, comp);
        } else {
            pool.invoke(new ForkJoinQuickSortComp(x, from, to, comp));
        }
    }

    public static void parallelQuickSort(long[][] x, LongComparator comp) {
        parallelQuickSort(x, 0L, BigArrays.length(x), comp);
    }

    public static long binarySearch(long[][] a, long from, long to, long key) {
        long to2 = to - 1;
        while (from <= to2) {
            long mid = (from + to2) >>> 1;
            long midVal = BigArrays.get(a, mid);
            if (midVal < key) {
                from = mid + 1;
            } else if (midVal > key) {
                to2 = mid - 1;
            } else {
                return mid;
            }
        }
        return -(1 + from);
    }

    public static long binarySearch(long[][] a, long key) {
        return binarySearch(a, 0L, BigArrays.length(a), key);
    }

    public static long binarySearch(long[][] a, long from, long to, long key, LongComparator c) {
        long to2 = to - 1;
        while (from <= to2) {
            long mid = (from + to2) >>> 1;
            long midVal = BigArrays.get(a, mid);
            int cmp = c.compare(midVal, key);
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

    public static long binarySearch(long[][] a, long key, LongComparator c) {
        return binarySearch(a, 0L, BigArrays.length(a), key, c);
    }

    public static void radixSort(long[][] a) {
        radixSort(a, 0L, BigArrays.length(a));
    }

    public static void radixSort(long[][] a, long from, long to) {
        int level;
        int signMask;
        int level2;
        int maxLevel = 7;
        int stackSize = 1786;
        long[] offsetStack = new long[1786];
        long[] lengthStack = new long[1786];
        int[] levelStack = new int[1786];
        int lengthPos = 0 + 1;
        offsetStack[0] = from;
        int signMask2 = 0 + 1;
        lengthStack[0] = to - from;
        int levelPos = 0 + 1;
        levelStack[0] = 0;
        long[] count = new long[256];
        long[] pos = new long[256];
        byte[][] digit = ByteBigArrays.newBigArray(to - from);
        while (lengthPos > 0) {
            int offsetPos = lengthPos - 1;
            long first = offsetStack[offsetPos];
            signMask2--;
            long length = lengthStack[signMask2];
            levelPos--;
            int level3 = levelStack[levelPos];
            int levelPos2 = level3 % 8 == 0 ? 128 : 0;
            if (length < 40) {
                selectionSort(a, first, first + length);
                lengthPos = offsetPos;
            } else {
                int shift = (7 - (level3 % 8)) * 8;
                long i = length;
                while (true) {
                    level = level3;
                    long i2 = i - 1;
                    if (i == 0) {
                        break;
                    }
                    int lengthPos2 = signMask2;
                    int lengthPos3 = levelPos2;
                    int signMask3 = levelPos;
                    BigArrays.set(digit, i2, (byte) (((BigArrays.get(a, first + i2) >>> shift) & 255) ^ lengthPos3));
                    levelPos = signMask3;
                    maxLevel = maxLevel;
                    stackSize = stackSize;
                    shift = shift;
                    levelPos2 = lengthPos3;
                    i = i2;
                    level3 = level;
                    signMask2 = lengthPos2;
                }
                int maxLevel2 = maxLevel;
                int stackSize2 = stackSize;
                int lengthPos4 = signMask2;
                int signMask4 = levelPos2;
                int levelPos3 = levelPos;
                long i3 = length;
                while (true) {
                    long i4 = i3 - 1;
                    if (i3 == 0) {
                        break;
                    }
                    int i5 = BigArrays.get(digit, i4) & 255;
                    count[i5] = count[i5] + 1;
                    i3 = i4;
                }
                int lastUsed = -1;
                long p = 0;
                int i6 = 0;
                lengthPos = offsetPos;
                while (i6 < 256) {
                    if (count[i6] == 0) {
                        signMask = signMask4;
                        level2 = level;
                    } else {
                        lastUsed = i6;
                        signMask = signMask4;
                        level2 = level;
                        if (level2 < 7 && count[i6] > 1) {
                            offsetStack[lengthPos] = p + first;
                            lengthStack[lengthPos4] = count[i6];
                            levelStack[levelPos3] = level2 + 1;
                            lengthPos4++;
                            lengthPos++;
                            levelPos3++;
                        }
                    }
                    long j = p + count[i6];
                    p = j;
                    pos[i6] = j;
                    i6++;
                    level = level2;
                    signMask4 = signMask;
                }
                long end = length - count[lastUsed];
                count[lastUsed] = 0;
                int lastUsed2 = lastUsed;
                long i7 = 0;
                while (i7 < end) {
                    int[] levelStack2 = levelStack;
                    long[] offsetStack2 = offsetStack;
                    long t = BigArrays.get(a, i7 + first);
                    int c = BigArrays.get(digit, i7) & 255;
                    long t2 = t;
                    while (true) {
                        long t3 = pos[c];
                        long d = t3 - 1;
                        pos[c] = d;
                        if (d > i7) {
                            long z = t2;
                            int zz = c;
                            long d2 = p;
                            long p2 = d + first;
                            long t4 = BigArrays.get(a, p2);
                            int c2 = BigArrays.get(digit, d) & 255;
                            long t5 = d + first;
                            c = c2;
                            BigArrays.set(a, t5, z);
                            BigArrays.set(digit, d, (byte) zz);
                            p = d2;
                            lengthStack = lengthStack;
                            lastUsed2 = lastUsed2;
                            t2 = t4;
                        }
                    }
                    long d3 = p;
                    long p3 = i7 + first;
                    BigArrays.set(a, p3, t2);
                    long t6 = count[c];
                    i7 += t6;
                    count[c] = 0;
                    offsetStack = offsetStack2;
                    levelStack = levelStack2;
                    p = d3;
                    lengthStack = lengthStack;
                    lastUsed2 = lastUsed2;
                }
                levelPos = levelPos3;
                maxLevel = maxLevel2;
                stackSize = stackSize2;
                signMask2 = lengthPos4;
            }
        }
    }

    private static void selectionSort(long[][] a, long[][] b, long from, long to) {
        for (long i = from; i < to - 1; i++) {
            long m = i;
            for (long j = i + 1; j < to; j++) {
                if (BigArrays.get(a, j) < BigArrays.get(a, m) || (BigArrays.get(a, j) == BigArrays.get(a, m) && BigArrays.get(b, j) < BigArrays.get(b, m))) {
                    m = j;
                }
            }
            if (m != i) {
                long t = BigArrays.get(a, i);
                BigArrays.set(a, i, BigArrays.get(a, m));
                BigArrays.set(a, m, t);
                long t2 = BigArrays.get(b, i);
                BigArrays.set(b, i, BigArrays.get(b, m));
                BigArrays.set(b, m, t2);
            }
        }
    }

    public static void radixSort(long[][] a, long[][] b) {
        radixSort(a, b, 0L, BigArrays.length(a));
    }

    public static void radixSort(long[][] a, long[][] b, long from, long to) {
        int maxLevel;
        int stackSize;
        int layers = 2;
        if (BigArrays.length(a) != BigArrays.length(b)) {
            throw new IllegalArgumentException("Array size mismatch.");
        }
        int maxLevel2 = 15;
        int stackSize2 = 3826;
        long[] offsetStack = new long[3826];
        long[] lengthStack = new long[3826];
        int[] levelStack = new int[3826];
        int lengthPos = 0 + 1;
        offsetStack[0] = from;
        int lengthPos2 = 0 + 1;
        lengthStack[0] = to - from;
        int levelPos = 0 + 1;
        int i = 0;
        levelStack[0] = 0;
        long[] count = new long[256];
        long[] pos = new long[256];
        byte[][] digit = ByteBigArrays.newBigArray(to - from);
        while (lengthPos > 0) {
            int offsetPos = lengthPos - 1;
            long first = offsetStack[offsetPos];
            int lengthPos3 = lengthPos2 - 1;
            long length = lengthStack[lengthPos3];
            int levelPos2 = levelPos - 1;
            int levelPos3 = levelStack[levelPos2];
            int signMask = levelPos3 % 8 == 0 ? 128 : i;
            if (length < 40) {
                selectionSort(a, b, first, first + length);
                digit = digit;
                lengthPos = offsetPos;
                lengthPos2 = lengthPos3;
                levelPos = levelPos2;
                layers = layers;
                count = count;
                pos = pos;
                i = 0;
            } else {
                byte[][] digit2 = digit;
                long[] count2 = count;
                long[] pos2 = pos;
                int layers2 = layers;
                int c = signMask;
                long[][] k = levelPos3 < 8 ? a : b;
                int shift = (7 - (levelPos3 % 8)) * 8;
                long i2 = length;
                while (true) {
                    maxLevel = maxLevel2;
                    stackSize = stackSize2;
                    long i3 = i2 - 1;
                    if (i2 == 0) {
                        break;
                    }
                    BigArrays.set(digit2, i3, (byte) (c ^ ((BigArrays.get(k, first + i3) >>> shift) & 255)));
                    i2 = i3;
                    k = k;
                    maxLevel2 = maxLevel;
                    stackSize2 = stackSize;
                }
                long i4 = length;
                while (true) {
                    long i5 = i4 - 1;
                    if (i4 == 0) {
                        break;
                    }
                    int i6 = BigArrays.get(digit2, i5) & 255;
                    count2[i6] = count2[i6] + 1;
                    i4 = i5;
                }
                int lastUsed = -1;
                long p = 0;
                lengthPos = offsetPos;
                for (int i7 = 0; i7 < 256; i7++) {
                    if (count2[i7] != 0) {
                        lastUsed = i7;
                        if (levelPos3 < 15 && count2[i7] > 1) {
                            offsetStack[lengthPos] = p + first;
                            lengthStack[lengthPos3] = count2[i7];
                            levelStack[levelPos2] = levelPos3 + 1;
                            lengthPos3++;
                            lengthPos++;
                            levelPos2++;
                        }
                    }
                    long j = p + count2[i7];
                    p = j;
                    pos2[i7] = j;
                }
                long end = length - count2[lastUsed];
                count2[lastUsed] = 0;
                int lastUsed2 = lastUsed;
                long i8 = 0;
                while (i8 < end) {
                    int shift2 = shift;
                    int offsetPos2 = lengthPos;
                    long t = BigArrays.get(a, i8 + first);
                    long t2 = i8 + first;
                    long u = BigArrays.get(b, t2);
                    int c2 = BigArrays.get(digit2, i8) & 255;
                    long t3 = t;
                    long u2 = u;
                    while (true) {
                        long u3 = pos2[c2];
                        long d = u3 - 1;
                        pos2[c2] = d;
                        if (d > i8) {
                            long z = t3;
                            int zz = c2;
                            int signMask2 = c;
                            long t4 = BigArrays.get(a, d + first);
                            BigArrays.set(a, d + first, z);
                            long z2 = u2;
                            u2 = BigArrays.get(b, d + first);
                            BigArrays.set(b, d + first, z2);
                            int c3 = BigArrays.get(digit2, d) & 255;
                            BigArrays.set(digit2, d, (byte) zz);
                            c2 = c3;
                            c = signMask2;
                            stackSize = stackSize;
                            lastUsed2 = lastUsed2;
                            p = p;
                            t3 = t4;
                        }
                    }
                    int signMask3 = c;
                    BigArrays.set(a, i8 + first, t3);
                    long t5 = u2;
                    BigArrays.set(b, i8 + first, t5);
                    long u4 = count2[c2];
                    i8 += u4;
                    count2[c2] = 0;
                    shift = shift2;
                    lengthPos = offsetPos2;
                    c = signMask3;
                    stackSize = stackSize;
                    lastUsed2 = lastUsed2;
                    p = p;
                }
                int stackSize3 = stackSize;
                digit = digit2;
                lengthPos2 = lengthPos3;
                levelPos = levelPos2;
                layers = layers2;
                count = count2;
                pos = pos2;
                maxLevel2 = maxLevel;
                stackSize2 = stackSize3;
                i = 0;
            }
        }
    }

    private static void insertionSortIndirect(long[][] perm, long[][] a, long[][] b, long from, long to) {
        long i = from;
        while (true) {
            long j = i + 1;
            i = j;
            if (j < to) {
                long t = BigArrays.get(perm, i);
                long j2 = i;
                long u = BigArrays.get(perm, j2 - 1);
                while (true) {
                    if (BigArrays.get(a, t) < BigArrays.get(a, u) || (BigArrays.get(a, t) == BigArrays.get(a, u) && BigArrays.get(b, t) < BigArrays.get(b, u))) {
                        BigArrays.set(perm, j2, u);
                        if (from != j2 - 1) {
                            long j3 = j2 - 1;
                            j2 = j3;
                            u = BigArrays.get(perm, j3 - 1);
                        } else {
                            j2--;
                            break;
                        }
                    }
                }
                BigArrays.set(perm, j2, t);
            } else {
                return;
            }
        }
    }

    public static void radixSortIndirect(long[][] perm, long[][] a, long[][] b, boolean stable) {
        ensureSameLength(a, b);
        radixSortIndirect(perm, a, b, 0L, BigArrays.length(a), stable);
    }

    public static void radixSortIndirect(long[][] perm, long[][] a, long[][] b, long from, long to, boolean stable) {
        long[][] jArr;
        long[][] support;
        long[] pos;
        int[] levelStack;
        int level;
        long[] count;
        int maxLevel;
        int stackSize;
        long j;
        int maxLevel2;
        int stackSize2;
        int c;
        long i;
        long[] count2;
        if (to - from < RealWebSocket.DEFAULT_MINIMUM_DEFLATE_SIZE) {
            insertionSortIndirect(perm, a, b, from, to);
            return;
        }
        int layers = 2;
        int maxLevel3 = 15;
        int stackSize3 = 3826;
        long[] offsetStack = new long[3826];
        long[] lengthStack = new long[3826];
        int[] levelStack2 = new int[3826];
        offsetStack[0] = from;
        lengthStack[0] = to - from;
        int stackPos = 0 + 1;
        levelStack2[0] = 0;
        long[] count3 = new long[256];
        long[] pos2 = new long[256];
        if (stable) {
            jArr = newBigArray(BigArrays.length(perm));
        } else {
            jArr = null;
        }
        long[][] support2 = jArr;
        while (stackPos > 0) {
            int stackPos2 = stackPos - 1;
            long first = offsetStack[stackPos2];
            long length = lengthStack[stackPos2];
            int level2 = levelStack2[stackPos2];
            int signMask = level2 % 8 == 0 ? 128 : 0;
            long[][] k = level2 < 8 ? a : b;
            int shift = (7 - (level2 % 8)) * 8;
            long i2 = first + length;
            while (true) {
                long i3 = i2 - 1;
                if (i2 == first) {
                    break;
                }
                long j2 = (BigArrays.get(k, BigArrays.get(perm, i3)) >>> shift) & 255;
                long i4 = signMask;
                int i5 = (int) (j2 ^ i4);
                count3[i5] = count3[i5] + 1;
                level2 = level2;
                i2 = i3;
            }
            int level3 = level2;
            long[][] k2 = k;
            long p = stable ? 0L : first;
            int lastUsed = -1;
            for (int i6 = 0; i6 < 256; i6++) {
                if (count3[i6] != 0) {
                    lastUsed = i6;
                }
                long j3 = p + count3[i6];
                p = j3;
                pos2[i6] = j3;
            }
            if (stable) {
                long i7 = first + length;
                while (true) {
                    long i8 = i7 - 1;
                    if (i7 == first) {
                        break;
                    }
                    int[] levelStack3 = levelStack2;
                    int i9 = (int) (signMask ^ ((BigArrays.get(k2, BigArrays.get(perm, i8)) >>> shift) & 255));
                    long j4 = pos2[i9] - 1;
                    pos2[i9] = j4;
                    BigArrays.set(support2, j4, BigArrays.get(perm, i8));
                    i7 = i8;
                    levelStack2 = levelStack3;
                    k2 = k2;
                }
                level = layers;
                support = support2;
                pos = pos2;
                long[] count4 = count3;
                levelStack = levelStack2;
                BigArrays.copy(support2, 0L, perm, first, length);
                long p2 = first;
                int i10 = 0;
                for (int i11 = 256; i10 < i11; i11 = 256) {
                    if (level3 < 15) {
                        long[] count5 = count4;
                        if (count5[i10] <= 1) {
                            count2 = count5;
                        } else if (count5[i10] < RealWebSocket.DEFAULT_MINIMUM_DEFLATE_SIZE) {
                            count2 = count5;
                            insertionSortIndirect(perm, a, b, p2, p2 + count5[i10]);
                        } else {
                            count2 = count5;
                            offsetStack[stackPos2] = p2;
                            lengthStack[stackPos2] = count2[i10];
                            levelStack[stackPos2] = level3 + 1;
                            stackPos2++;
                        }
                    } else {
                        count2 = count4;
                    }
                    p2 += count2[i10];
                    i10++;
                    count4 = count2;
                }
                count = count4;
                Arrays.fill(count, 0L);
                maxLevel = maxLevel3;
                stackSize = stackSize3;
                stackPos = stackPos2;
                j = RealWebSocket.DEFAULT_MINIMUM_DEFLATE_SIZE;
            } else {
                support = support2;
                pos = pos2;
                levelStack = levelStack2;
                long[][] k3 = k2;
                level = layers;
                count = count3;
                long end = (first + length) - count[lastUsed];
                int c2 = -1;
                long i12 = first;
                while (i12 <= end) {
                    long t = BigArrays.get(perm, i12);
                    long[][] k4 = k3;
                    int c3 = (int) (signMask ^ ((BigArrays.get(k4, t) >>> shift) & 255));
                    if (i12 >= end) {
                        maxLevel2 = maxLevel3;
                        stackSize2 = stackSize3;
                        c = c3;
                    } else {
                        long t2 = t;
                        while (true) {
                            long d = pos[c3] - 1;
                            pos[c3] = d;
                            if (d <= i12) {
                                break;
                            }
                            long z = t2;
                            long t3 = BigArrays.get(perm, d);
                            BigArrays.set(perm, d, z);
                            c3 = (int) (((BigArrays.get(k4, t3) >>> shift) & 255) ^ signMask);
                            maxLevel3 = maxLevel3;
                            stackSize3 = stackSize3;
                            t2 = t3;
                        }
                        c = c3;
                        maxLevel2 = maxLevel3;
                        stackSize2 = stackSize3;
                        BigArrays.set(perm, i12, t2);
                    }
                    if (level3 >= 15 || count[c] <= 1) {
                        i = i12;
                    } else if (count[c] < RealWebSocket.DEFAULT_MINIMUM_DEFLATE_SIZE) {
                        i = i12;
                        insertionSortIndirect(perm, a, b, i12, i12 + count[c]);
                    } else {
                        i = i12;
                        offsetStack[stackPos2] = i;
                        lengthStack[stackPos2] = count[c];
                        levelStack[stackPos2] = level3 + 1;
                        stackPos2++;
                    }
                    i12 = i + count[c];
                    count[c] = 0;
                    k3 = k4;
                    c2 = c;
                    maxLevel3 = maxLevel2;
                    stackSize3 = stackSize2;
                }
                maxLevel = maxLevel3;
                stackSize = stackSize3;
                j = RealWebSocket.DEFAULT_MINIMUM_DEFLATE_SIZE;
                stackPos = stackPos2;
            }
            count3 = count;
            layers = level;
            levelStack2 = levelStack;
            support2 = support;
            pos2 = pos;
            maxLevel3 = maxLevel;
            stackSize3 = stackSize;
        }
    }

    public static long[][] shuffle(long[][] a, long from, long to, Random random) {
        return BigArrays.shuffle(a, from, to, random);
    }

    public static long[][] shuffle(long[][] a, Random random) {
        return BigArrays.shuffle(a, random);
    }
}
