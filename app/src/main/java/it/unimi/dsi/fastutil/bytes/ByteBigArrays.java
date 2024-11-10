package it.unimi.dsi.fastutil.bytes;

import io.netty.handler.codec.http2.Http2CodecUtil;
import it.unimi.dsi.fastutil.BigArrays;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.longs.LongBigArrays;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import okhttp3.internal.ws.RealWebSocket;

/* loaded from: classes4.dex */
public final class ByteBigArrays {
    private static final int DIGITS_PER_ELEMENT = 1;
    private static final int DIGIT_BITS = 8;
    private static final int DIGIT_MASK = 255;
    private static final int MEDIUM = 40;
    private static final int PARALLEL_QUICKSORT_NO_FORK = 8192;
    private static final int QUICKSORT_NO_REC = 7;
    private static final int RADIXSORT_NO_REC = 1024;
    public static final byte[][] EMPTY_BIG_ARRAY = new byte[0];
    public static final byte[][] DEFAULT_EMPTY_BIG_ARRAY = new byte[0];
    public static final Hash.Strategy HASH_STRATEGY = new BigArrayHashStrategy();

    private ByteBigArrays() {
    }

    @Deprecated
    public static byte get(byte[][] array, long index) {
        return array[BigArrays.segment(index)][BigArrays.displacement(index)];
    }

    @Deprecated
    public static void set(byte[][] array, long index, byte value) {
        array[BigArrays.segment(index)][BigArrays.displacement(index)] = value;
    }

    @Deprecated
    public static void swap(byte[][] array, long first, long second) {
        byte t = array[BigArrays.segment(first)][BigArrays.displacement(first)];
        array[BigArrays.segment(first)][BigArrays.displacement(first)] = array[BigArrays.segment(second)][BigArrays.displacement(second)];
        array[BigArrays.segment(second)][BigArrays.displacement(second)] = t;
    }

    @Deprecated
    public static void add(byte[][] array, long index, byte incr) {
        byte[] bArr = array[BigArrays.segment(index)];
        int displacement = BigArrays.displacement(index);
        bArr[displacement] = (byte) (bArr[displacement] + incr);
    }

    @Deprecated
    public static void mul(byte[][] array, long index, byte factor) {
        byte[] bArr = array[BigArrays.segment(index)];
        int displacement = BigArrays.displacement(index);
        bArr[displacement] = (byte) (bArr[displacement] * factor);
    }

    @Deprecated
    public static void incr(byte[][] array, long index) {
        byte[] bArr = array[BigArrays.segment(index)];
        int displacement = BigArrays.displacement(index);
        bArr[displacement] = (byte) (bArr[displacement] + 1);
    }

    @Deprecated
    public static void decr(byte[][] array, long index) {
        array[BigArrays.segment(index)][BigArrays.displacement(index)] = (byte) (r0[r1] - 1);
    }

    @Deprecated
    public static long length(byte[][] array) {
        int length = array.length;
        if (length == 0) {
            return 0L;
        }
        return BigArrays.start(length - 1) + array[length - 1].length;
    }

    @Deprecated
    public static void copy(byte[][] srcArray, long srcPos, byte[][] destArray, long destPos, long length) {
        BigArrays.copy(srcArray, srcPos, destArray, destPos, length);
    }

    @Deprecated
    public static void copyFromBig(byte[][] srcArray, long srcPos, byte[] destArray, int destPos, int length) {
        BigArrays.copyFromBig(srcArray, srcPos, destArray, destPos, length);
    }

    @Deprecated
    public static void copyToBig(byte[] srcArray, int srcPos, byte[][] destArray, long destPos, long length) {
        BigArrays.copyToBig(srcArray, srcPos, destArray, destPos, length);
    }

    public static byte[][] newBigArray(long length) {
        if (length == 0) {
            return EMPTY_BIG_ARRAY;
        }
        BigArrays.ensureLength(length);
        int baseLength = (int) ((length + 134217727) >>> 27);
        byte[][] base = new byte[baseLength];
        int residual = (int) (134217727 & length);
        if (residual != 0) {
            for (int i = 0; i < baseLength - 1; i++) {
                base[i] = new byte[BigArrays.SEGMENT_SIZE];
            }
            base[baseLength - 1] = new byte[residual];
        } else {
            for (int i2 = 0; i2 < baseLength; i2++) {
                base[i2] = new byte[BigArrays.SEGMENT_SIZE];
            }
        }
        return base;
    }

    @Deprecated
    public static byte[][] wrap(byte[] array) {
        return BigArrays.wrap(array);
    }

    @Deprecated
    public static byte[][] ensureCapacity(byte[][] array, long length) {
        return ensureCapacity(array, length, length(array));
    }

    @Deprecated
    public static byte[][] forceCapacity(byte[][] array, long length, long preserve) {
        return BigArrays.forceCapacity(array, length, preserve);
    }

    @Deprecated
    public static byte[][] ensureCapacity(byte[][] array, long length, long preserve) {
        return length > length(array) ? forceCapacity(array, length, preserve) : array;
    }

    @Deprecated
    public static byte[][] grow(byte[][] array, long length) {
        long oldLength = length(array);
        return length > oldLength ? grow(array, length, oldLength) : array;
    }

    @Deprecated
    public static byte[][] grow(byte[][] array, long length, long preserve) {
        long oldLength = length(array);
        return length > oldLength ? ensureCapacity(array, Math.max((oldLength >> 1) + oldLength, length), preserve) : array;
    }

    @Deprecated
    public static byte[][] trim(byte[][] array, long length) {
        BigArrays.ensureLength(length);
        long oldLength = length(array);
        if (length >= oldLength) {
            return array;
        }
        int baseLength = (int) ((length + 134217727) >>> 27);
        byte[][] base = (byte[][]) Arrays.copyOf(array, baseLength);
        int residual = (int) (134217727 & length);
        if (residual != 0) {
            base[baseLength - 1] = ByteArrays.trim(base[baseLength - 1], residual);
        }
        return base;
    }

    @Deprecated
    public static byte[][] setLength(byte[][] array, long length) {
        return BigArrays.setLength(array, length);
    }

    @Deprecated
    public static byte[][] copy(byte[][] array, long offset, long length) {
        return BigArrays.copy(array, offset, length);
    }

    @Deprecated
    public static byte[][] copy(byte[][] array) {
        return BigArrays.copy(array);
    }

    @Deprecated
    public static void fill(byte[][] array, byte value) {
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
    public static void fill(byte[][] array, long from, long to, byte value) {
        BigArrays.fill(array, from, to, value);
    }

    @Deprecated
    public static boolean equals(byte[][] a1, byte[][] a2) {
        return BigArrays.equals(a1, a2);
    }

    @Deprecated
    public static String toString(byte[][] a) {
        return BigArrays.toString(a);
    }

    @Deprecated
    public static void ensureFromTo(byte[][] a, long from, long to) {
        BigArrays.ensureFromTo(length(a), from, to);
    }

    @Deprecated
    public static void ensureOffsetLength(byte[][] a, long offset, long length) {
        BigArrays.ensureOffsetLength(length(a), offset, length);
    }

    @Deprecated
    public static void ensureSameLength(byte[][] a, byte[][] b) {
        if (length(a) != length(b)) {
            throw new IllegalArgumentException("Array size mismatch: " + length(a) + " != " + length(b));
        }
    }

    /* loaded from: classes4.dex */
    private static final class BigArrayHashStrategy implements Hash.Strategy<byte[][]>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;

        private BigArrayHashStrategy() {
        }

        @Override // it.unimi.dsi.fastutil.Hash.Strategy
        public int hashCode(byte[][] o) {
            return Arrays.deepHashCode(o);
        }

        @Override // it.unimi.dsi.fastutil.Hash.Strategy
        public boolean equals(byte[][] a, byte[][] b) {
            return ByteBigArrays.equals(a, b);
        }
    }

    private static ForkJoinPool getPool() {
        ForkJoinPool current = ForkJoinTask.getPool();
        return current == null ? ForkJoinPool.commonPool() : current;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void swap(byte[][] x, long a, long b, long n) {
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
    public static long med3(byte[][] r5, long r6, long r8, long r10, it.unimi.dsi.fastutil.bytes.ByteComparator r12) {
        /*
            byte r0 = it.unimi.dsi.fastutil.BigArrays.get(r5, r6)
            byte r1 = it.unimi.dsi.fastutil.BigArrays.get(r5, r8)
            int r0 = r12.compare(r0, r1)
            byte r1 = it.unimi.dsi.fastutil.BigArrays.get(r5, r6)
            byte r2 = it.unimi.dsi.fastutil.BigArrays.get(r5, r10)
            int r1 = r12.compare(r1, r2)
            byte r2 = it.unimi.dsi.fastutil.BigArrays.get(r5, r8)
            byte r3 = it.unimi.dsi.fastutil.BigArrays.get(r5, r10)
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
        throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.bytes.ByteBigArrays.med3(byte[][], long, long, long, it.unimi.dsi.fastutil.bytes.ByteComparator):long");
    }

    private static void selectionSort(byte[][] a, long from, long to, ByteComparator comp) {
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

    public static void quickSort(byte[][] x, long from, long to, ByteComparator comp) {
        long d;
        long c;
        int comparison;
        int comparison2;
        ByteComparator byteComparator = comp;
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
        byte v = BigArrays.get(x, m);
        long c2 = to - 1;
        long c3 = c2;
        long a = from;
        long b = c2;
        long b2 = from;
        while (true) {
            if (b2 <= b && (comparison2 = byteComparator.compare(BigArrays.get(x, b2), v)) <= 0) {
                if (comparison2 == 0) {
                    BigArrays.swap(x, a, b2);
                    a++;
                }
                b2++;
            } else {
                d = c3;
                c = b;
                while (c >= b2 && (comparison = byteComparator.compare(BigArrays.get(x, c), v)) >= 0) {
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
                byteComparator = comp;
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
    public static long med3(byte[][] r5, long r6, long r8, long r10) {
        /*
            byte r0 = it.unimi.dsi.fastutil.BigArrays.get(r5, r6)
            byte r1 = it.unimi.dsi.fastutil.BigArrays.get(r5, r8)
            int r0 = java.lang.Byte.compare(r0, r1)
            byte r1 = it.unimi.dsi.fastutil.BigArrays.get(r5, r6)
            byte r2 = it.unimi.dsi.fastutil.BigArrays.get(r5, r10)
            int r1 = java.lang.Byte.compare(r1, r2)
            byte r2 = it.unimi.dsi.fastutil.BigArrays.get(r5, r8)
            byte r3 = it.unimi.dsi.fastutil.BigArrays.get(r5, r10)
            int r2 = java.lang.Byte.compare(r2, r3)
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
        throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.bytes.ByteBigArrays.med3(byte[][], long, long, long):long");
    }

    private static void selectionSort(byte[][] a, long from, long to) {
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

    public static void quickSort(byte[][] x, ByteComparator comp) {
        quickSort(x, 0L, BigArrays.length(x), comp);
    }

    public static void quickSort(byte[][] x, long from, long to) {
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
        byte v = BigArrays.get(x, m);
        long c2 = to - 1;
        long len = c2;
        long b = c2;
        long a = from;
        long b2 = from;
        while (true) {
            if (b2 <= b && (comparison2 = Byte.compare(BigArrays.get(x, b2), v)) <= 0) {
                if (comparison2 == 0) {
                    BigArrays.swap(x, a, b2);
                    a++;
                }
                b2++;
            } else {
                c = b;
                long len2 = d2;
                d = len;
                while (c >= b2 && (comparison = Byte.compare(BigArrays.get(x, c), v)) >= 0) {
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

    public static void quickSort(byte[][] x) {
        quickSort(x, 0L, BigArrays.length(x));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public static class ForkJoinQuickSort extends RecursiveAction {
        private static final long serialVersionUID = 1;
        private final long from;
        private final long to;
        private final byte[][] x;

        public ForkJoinQuickSort(byte[][] x, long from, long to) {
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
            byte[][] x = this.x;
            long len = this.to - this.from;
            if (len < Http2CodecUtil.DEFAULT_HEADER_LIST_SIZE) {
                ByteBigArrays.quickSort(x, this.from, this.to);
                return;
            }
            long m = this.from + (len / 2);
            long l = this.from;
            long n = this.to - serialVersionUID;
            long s = len / 8;
            long l2 = ByteBigArrays.med3(x, l, l + s, l + (s * 2));
            long m2 = ByteBigArrays.med3(x, m - s, m, m + s);
            long d2 = ByteBigArrays.med3(x, n - (2 * s), n - s, n);
            long m3 = ByteBigArrays.med3(x, l2, m2, d2);
            byte v = BigArrays.get(x, m3);
            long a = this.from;
            long c2 = this.to - serialVersionUID;
            long n2 = c2;
            long a2 = a;
            long len2 = a;
            while (true) {
                if (len2 <= c2 && (comparison2 = Byte.compare(BigArrays.get(x, len2), v)) <= 0) {
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
                    while (c >= len2 && (comparison = Byte.compare(BigArrays.get(x, c), v)) >= 0) {
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
            ByteBigArrays.swap(x, this.from, len2 - s2, s2);
            long s3 = Math.min(d - c3, (this.to - d) - serialVersionUID);
            ByteBigArrays.swap(x, len2, this.to - s3, s3);
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

    public static void parallelQuickSort(byte[][] x, long from, long to) {
        ForkJoinPool pool = getPool();
        if (to - from < Http2CodecUtil.DEFAULT_HEADER_LIST_SIZE || pool.getParallelism() == 1) {
            quickSort(x, from, to);
        } else {
            pool.invoke(new ForkJoinQuickSort(x, from, to));
        }
    }

    public static void parallelQuickSort(byte[][] x) {
        parallelQuickSort(x, 0L, BigArrays.length(x));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public static class ForkJoinQuickSortComp extends RecursiveAction {
        private static final long serialVersionUID = 1;
        private final ByteComparator comp;
        private final long from;
        private final long to;
        private final byte[][] x;

        public ForkJoinQuickSortComp(byte[][] x, long from, long to, ByteComparator comp) {
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
            byte[][] x = this.x;
            long len = this.to - this.from;
            if (len < Http2CodecUtil.DEFAULT_HEADER_LIST_SIZE) {
                ByteBigArrays.quickSort(x, this.from, this.to, this.comp);
                return;
            }
            long m = this.from + (len / 2);
            long l = this.from;
            long n2 = this.to - serialVersionUID;
            long s = len / 8;
            long l2 = ByteBigArrays.med3(x, l, l + s, l + (s * 2), this.comp);
            long m2 = ByteBigArrays.med3(x, m - s, m, m + s, this.comp);
            long n3 = ByteBigArrays.med3(x, n2 - (2 * s), n2 - s, n2, this.comp);
            byte v = BigArrays.get(x, ByteBigArrays.med3(x, l2, m2, n3, this.comp));
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
            ByteBigArrays.swap(x, this.from, b - s2, s2);
            long s3 = Math.min(d - c5, (this.to - d) - serialVersionUID);
            ByteBigArrays.swap(x, c6, this.to - s3, s3);
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

    public static void parallelQuickSort(byte[][] x, long from, long to, ByteComparator comp) {
        ForkJoinPool pool = getPool();
        if (to - from < Http2CodecUtil.DEFAULT_HEADER_LIST_SIZE || pool.getParallelism() == 1) {
            quickSort(x, from, to, comp);
        } else {
            pool.invoke(new ForkJoinQuickSortComp(x, from, to, comp));
        }
    }

    public static void parallelQuickSort(byte[][] x, ByteComparator comp) {
        parallelQuickSort(x, 0L, BigArrays.length(x), comp);
    }

    public static long binarySearch(byte[][] a, long from, long to, byte key) {
        long to2 = to - 1;
        while (from <= to2) {
            long mid = (from + to2) >>> 1;
            byte midVal = BigArrays.get(a, mid);
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

    public static long binarySearch(byte[][] a, byte key) {
        return binarySearch(a, 0L, BigArrays.length(a), key);
    }

    public static long binarySearch(byte[][] a, long from, long to, byte key, ByteComparator c) {
        long to2 = to - 1;
        while (from <= to2) {
            long mid = (from + to2) >>> 1;
            byte midVal = BigArrays.get(a, mid);
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

    public static long binarySearch(byte[][] a, byte key, ByteComparator c) {
        return binarySearch(a, 0L, BigArrays.length(a), key, c);
    }

    public static void radixSort(byte[][] a) {
        radixSort(a, 0L, BigArrays.length(a));
    }

    public static void radixSort(byte[][] a, long from, long to) {
        long[] count;
        int maxLevel = 0;
        int stackSize = 1;
        int offsetPos = 0 + 1;
        long[] offsetStack = {from};
        int levelPos = 0 + 1;
        long[] lengthStack = {to - from};
        int levelPos2 = 0 + 1;
        int i = 0;
        int[] levelStack = {0};
        long[] count2 = new long[256];
        long[] pos = new long[256];
        byte[][] digit = newBigArray(to - from);
        while (offsetPos > 0) {
            int offsetPos2 = offsetPos - 1;
            long first = offsetStack[offsetPos2];
            levelPos--;
            long length = lengthStack[levelPos];
            levelPos2--;
            int level = levelStack[levelPos2];
            int signMask = level % 1 == 0 ? 128 : i;
            if (length < 40) {
                selectionSort(a, first, first + length);
                offsetPos = offsetPos2;
            } else {
                int shift = (0 - (level % 1)) * 8;
                long i2 = length;
                while (true) {
                    count = count2;
                    long i3 = i2 - 1;
                    if (i2 == 0) {
                        break;
                    }
                    BigArrays.set(digit, i3, (byte) (((BigArrays.get(a, first + i3) >>> shift) & 255) ^ signMask));
                    maxLevel = maxLevel;
                    stackSize = stackSize;
                    i2 = i3;
                    count2 = count;
                }
                int maxLevel2 = maxLevel;
                int stackSize2 = stackSize;
                long i4 = length;
                while (true) {
                    long i5 = i4 - 1;
                    if (i4 == 0) {
                        break;
                    }
                    int i6 = BigArrays.get(digit, i5) & 255;
                    count[i6] = count[i6] + 1;
                    i4 = i5;
                }
                int lastUsed = -1;
                int i7 = 0;
                long p = 0;
                int lengthPos = offsetPos2;
                while (true) {
                    int offsetPos3 = shift;
                    if (i7 >= 256) {
                        break;
                    }
                    if (count[i7] != 0) {
                        lastUsed = i7;
                        if (level < 0 && count[i7] > 1) {
                            int offsetPos4 = lengthPos + 1;
                            offsetStack[lengthPos] = p + first;
                            int lengthPos2 = levelPos + 1;
                            lengthStack[levelPos] = count[i7];
                            levelStack[levelPos2] = level + 1;
                            levelPos2++;
                            levelPos = lengthPos2;
                            lengthPos = offsetPos4;
                        }
                    }
                    long j = p + count[i7];
                    p = j;
                    pos[i7] = j;
                    i7++;
                    shift = offsetPos3;
                }
                long end = length - count[lastUsed];
                count[lastUsed] = 0;
                int c = -1;
                int offsetPos5 = lengthPos;
                long i8 = 0;
                while (i8 < end) {
                    int lastUsed2 = lastUsed;
                    byte t = BigArrays.get(a, i8 + first);
                    c = BigArrays.get(digit, i8) & 255;
                    while (true) {
                        long d = pos[c] - 1;
                        pos[c] = d;
                        if (d > i8) {
                            byte z = t;
                            int zz = c;
                            int lengthPos3 = levelPos;
                            byte t2 = BigArrays.get(a, d + first);
                            c = BigArrays.get(digit, d) & 255;
                            BigArrays.set(a, d + first, z);
                            BigArrays.set(digit, d, (byte) zz);
                            lengthStack = lengthStack;
                            levelPos = lengthPos3;
                            offsetStack = offsetStack;
                            levelStack = levelStack;
                            t = t2;
                        }
                    }
                    int lengthPos4 = levelPos;
                    BigArrays.set(a, i8 + first, t);
                    i8 += count[c];
                    count[c] = 0;
                    lastUsed = lastUsed2;
                    lengthStack = lengthStack;
                    levelPos = lengthPos4;
                    offsetStack = offsetStack;
                    levelStack = levelStack;
                }
                maxLevel = maxLevel2;
                stackSize = stackSize2;
                count2 = count;
                offsetPos = offsetPos5;
                i = 0;
            }
        }
    }

    private static void selectionSort(byte[][] a, byte[][] b, long from, long to) {
        for (long i = from; i < to - 1; i++) {
            long m = i;
            for (long j = i + 1; j < to; j++) {
                if (BigArrays.get(a, j) < BigArrays.get(a, m) || (BigArrays.get(a, j) == BigArrays.get(a, m) && BigArrays.get(b, j) < BigArrays.get(b, m))) {
                    m = j;
                }
            }
            if (m != i) {
                byte t = BigArrays.get(a, i);
                BigArrays.set(a, i, BigArrays.get(a, m));
                BigArrays.set(a, m, t);
                byte t2 = BigArrays.get(b, i);
                BigArrays.set(b, i, BigArrays.get(b, m));
                BigArrays.set(b, m, t2);
            }
        }
    }

    public static void radixSort(byte[][] a, byte[][] b) {
        radixSort(a, b, 0L, BigArrays.length(a));
    }

    public static void radixSort(byte[][] a, byte[][] b, long from, long to) {
        int c = 2;
        if (BigArrays.length(a) != BigArrays.length(b)) {
            throw new IllegalArgumentException("Array size mismatch.");
        }
        int maxLevel = 1;
        long[] offsetStack = new long[256];
        long[] lengthStack = new long[256];
        int[] levelStack = new int[256];
        int offsetPos = 0 + 1;
        offsetStack[0] = from;
        int lengthPos = 0 + 1;
        lengthStack[0] = to - from;
        int levelPos = 0 + 1;
        int level = 0;
        levelStack[0] = 0;
        long[] count = new long[256];
        long[] pos = new long[256];
        byte[][] digit = newBigArray(to - from);
        while (offsetPos > 0) {
            int offsetPos2 = offsetPos - 1;
            long first = offsetStack[offsetPos2];
            int levelPos2 = lengthPos - 1;
            long length = lengthStack[levelPos2];
            int levelPos3 = levelPos - 1;
            int level2 = levelStack[levelPos3];
            int signMask = level2 % 1 == 0 ? 128 : level;
            if (length < 40) {
                selectionSort(a, b, first, first + length);
                digit = digit;
                offsetPos = offsetPos2;
                lengthPos = levelPos2;
                levelPos = levelPos3;
                count = count;
                pos = pos;
                level = 0;
            } else {
                byte[][] digit2 = digit;
                long[] count2 = count;
                long[] pos2 = pos;
                byte[][] k = level2 < 1 ? a : b;
                int shift = (0 - (level2 % 1)) * 8;
                long i = length;
                while (true) {
                    int shift2 = shift;
                    long i2 = i - 1;
                    if (i == 0) {
                        break;
                    }
                    BigArrays.set(digit2, i2, (byte) (((BigArrays.get(k, first + i2) >>> shift2) & 255) ^ signMask));
                    i = i2;
                    shift = shift2;
                }
                long i3 = length;
                while (true) {
                    long i4 = i3 - 1;
                    if (i3 == 0) {
                        break;
                    }
                    int i5 = BigArrays.get(digit2, i4) & 255;
                    count2[i5] = count2[i5] + 1;
                    i3 = i4;
                }
                int lastUsed = -1;
                int i6 = 0;
                long p = 0;
                int lengthPos2 = offsetPos2;
                while (true) {
                    byte[][] k2 = k;
                    if (i6 >= 256) {
                        break;
                    }
                    if (count2[i6] != 0) {
                        lastUsed = i6;
                        if (level2 < 1 && count2[i6] > 1) {
                            int offsetPos3 = lengthPos2 + 1;
                            offsetStack[lengthPos2] = p + first;
                            int lengthPos3 = levelPos2 + 1;
                            lengthStack[levelPos2] = count2[i6];
                            levelStack[levelPos3] = level2 + 1;
                            levelPos3++;
                            levelPos2 = lengthPos3;
                            lengthPos2 = offsetPos3;
                        }
                    }
                    long j = p + count2[i6];
                    p = j;
                    pos2[i6] = j;
                    i6++;
                    k = k2;
                }
                long end = length - count2[lastUsed];
                count2[lastUsed] = 0;
                int offsetPos4 = lengthPos2;
                long i7 = 0;
                while (i7 < end) {
                    long end2 = end;
                    long end3 = i7 + first;
                    byte t = BigArrays.get(a, end3);
                    byte u = BigArrays.get(b, i7 + first);
                    int c2 = BigArrays.get(digit2, i7) & 255;
                    byte u2 = u;
                    byte u3 = t;
                    while (true) {
                        long d = pos2[c2] - 1;
                        pos2[c2] = d;
                        if (d > i7) {
                            byte z = u3;
                            int zz = c2;
                            int layers = c;
                            byte t2 = BigArrays.get(a, d + first);
                            BigArrays.set(a, d + first, z);
                            byte z2 = u2;
                            u2 = BigArrays.get(b, d + first);
                            BigArrays.set(b, d + first, z2);
                            int c3 = BigArrays.get(digit2, d) & 255;
                            byte z3 = (byte) zz;
                            BigArrays.set(digit2, d, z3);
                            c2 = c3;
                            c = layers;
                            maxLevel = maxLevel;
                            p = p;
                            u3 = t2;
                        }
                    }
                    int layers2 = c;
                    BigArrays.set(a, i7 + first, u3);
                    BigArrays.set(b, i7 + first, u2);
                    i7 += count2[c2];
                    count2[c2] = 0;
                    end = end2;
                    c = layers2;
                    maxLevel = maxLevel;
                    p = p;
                }
                digit = digit2;
                lengthPos = levelPos2;
                levelPos = levelPos3;
                count = count2;
                pos = pos2;
                offsetPos = offsetPos4;
                level = 0;
            }
        }
    }

    private static void insertionSortIndirect(long[][] perm, byte[][] a, byte[][] b, long from, long to) {
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

    public static void radixSortIndirect(long[][] perm, byte[][] a, byte[][] b, boolean stable) {
        ensureSameLength(a, b);
        radixSortIndirect(perm, a, b, 0L, BigArrays.length(a), stable);
    }

    public static void radixSortIndirect(long[][] perm, byte[][] a, byte[][] b, long from, long to, boolean stable) {
        long[][] jArr;
        long[] lengthStack;
        int[] levelStack;
        int i;
        long[] offsetStack;
        long[] count;
        long[] pos;
        long[][] support;
        int layers;
        long j;
        long[][] support2;
        int layers2;
        int c;
        long i2;
        long[] count2;
        if (to - from < RealWebSocket.DEFAULT_MINIMUM_DEFLATE_SIZE) {
            insertionSortIndirect(perm, a, b, from, to);
            return;
        }
        int layers3 = 2;
        int i3 = 256;
        long[] offsetStack2 = new long[256];
        long[] lengthStack2 = new long[256];
        int[] levelStack2 = new int[256];
        offsetStack2[0] = from;
        lengthStack2[0] = to - from;
        int stackPos = 0 + 1;
        levelStack2[0] = 0;
        long[] count3 = new long[256];
        long[] pos2 = new long[256];
        if (stable) {
            jArr = LongBigArrays.newBigArray(BigArrays.length(perm));
        } else {
            jArr = null;
        }
        long[][] support3 = jArr;
        while (stackPos > 0) {
            int stackPos2 = stackPos - 1;
            long first = offsetStack2[stackPos2];
            long length = lengthStack2[stackPos2];
            int level = levelStack2[stackPos2];
            int signMask = level % 1 == 0 ? 128 : 0;
            byte[][] k = level < 1 ? a : b;
            int shift = (0 - (level % 1)) * 8;
            long i4 = first + length;
            while (true) {
                long i5 = i4 - 1;
                if (i4 == first) {
                    break;
                }
                int i6 = ((BigArrays.get(k, BigArrays.get(perm, i5)) >>> shift) & 255) ^ signMask;
                count3[i6] = count3[i6] + 1;
                support3 = support3;
                i4 = i5;
            }
            long[][] support4 = support3;
            long p = stable ? 0L : first;
            long p2 = p;
            int lastUsed = -1;
            for (int i7 = 0; i7 < i3; i7++) {
                if (count3[i7] != 0) {
                    lastUsed = i7;
                }
                long j2 = p2 + count3[i7];
                p2 = j2;
                pos2[i7] = j2;
            }
            if (stable) {
                long i8 = first + length;
                while (true) {
                    long i9 = i8 - 1;
                    if (i8 == first) {
                        break;
                    }
                    int i10 = ((BigArrays.get(k, BigArrays.get(perm, i9)) >>> shift) & 255) ^ signMask;
                    long j3 = pos2[i10] - 1;
                    pos2[i10] = j3;
                    BigArrays.set(support4, j3, BigArrays.get(perm, i9));
                    i8 = i9;
                    level = level;
                    offsetStack2 = offsetStack2;
                }
                int level2 = level;
                offsetStack = offsetStack2;
                int i11 = 1;
                long[] count4 = count3;
                pos = pos2;
                lengthStack = lengthStack2;
                levelStack = levelStack2;
                BigArrays.copy(support4, 0L, perm, first, length);
                long p3 = first;
                int i12 = 0;
                while (i12 < 256) {
                    if (level2 < i11) {
                        long[] count5 = count4;
                        if (count5[i12] <= 1) {
                            count2 = count5;
                        } else if (count5[i12] < RealWebSocket.DEFAULT_MINIMUM_DEFLATE_SIZE) {
                            count2 = count5;
                            insertionSortIndirect(perm, a, b, p3, p3 + count5[i12]);
                        } else {
                            count2 = count5;
                            offsetStack[stackPos2] = p3;
                            lengthStack[stackPos2] = count2[i12];
                            levelStack[stackPos2] = level2 + 1;
                            stackPos2++;
                        }
                    } else {
                        count2 = count4;
                    }
                    p3 += count2[i12];
                    i12++;
                    count4 = count2;
                    i11 = 1;
                }
                i = 256;
                count = count4;
                Arrays.fill(count, 0L);
                support = support4;
                layers = layers3;
                stackPos = stackPos2;
                j = RealWebSocket.DEFAULT_MINIMUM_DEFLATE_SIZE;
            } else {
                byte[][] k2 = k;
                lengthStack = lengthStack2;
                levelStack = levelStack2;
                i = i3;
                offsetStack = offsetStack2;
                long[][] support5 = support4;
                count = count3;
                pos = pos2;
                long end = (first + length) - count[lastUsed];
                long i13 = first;
                while (i13 <= end) {
                    long t = BigArrays.get(perm, i13);
                    int c2 = ((BigArrays.get(k2, t) >>> shift) & 255) ^ signMask;
                    if (i13 >= end) {
                        support2 = support5;
                        layers2 = layers3;
                        c = c2;
                    } else {
                        while (true) {
                            long d = pos[c2] - 1;
                            pos[c2] = d;
                            if (d <= i13) {
                                break;
                            }
                            long z = t;
                            t = BigArrays.get(perm, d);
                            BigArrays.set(perm, d, z);
                            c2 = ((BigArrays.get(k2, t) >>> shift) & 255) ^ signMask;
                            layers3 = layers3;
                            support5 = support5;
                        }
                        support2 = support5;
                        layers2 = layers3;
                        c = c2;
                        BigArrays.set(perm, i13, t);
                    }
                    if (level >= 1 || count[c] <= 1) {
                        i2 = i13;
                    } else if (count[c] < RealWebSocket.DEFAULT_MINIMUM_DEFLATE_SIZE) {
                        i2 = i13;
                        insertionSortIndirect(perm, a, b, i13, i13 + count[c]);
                    } else {
                        i2 = i13;
                        offsetStack[stackPos2] = i2;
                        lengthStack[stackPos2] = count[c];
                        levelStack[stackPos2] = level + 1;
                        stackPos2++;
                    }
                    i13 = i2 + count[c];
                    count[c] = 0;
                    layers3 = layers2;
                    support5 = support2;
                }
                support = support5;
                layers = layers3;
                j = RealWebSocket.DEFAULT_MINIMUM_DEFLATE_SIZE;
                stackPos = stackPos2;
            }
            layers3 = layers;
            count3 = count;
            pos2 = pos;
            lengthStack2 = lengthStack;
            levelStack2 = levelStack;
            offsetStack2 = offsetStack;
            i3 = i;
            support3 = support;
        }
    }

    public static byte[][] shuffle(byte[][] a, long from, long to, Random random) {
        return BigArrays.shuffle(a, from, to, random);
    }

    public static byte[][] shuffle(byte[][] a, Random random) {
        return BigArrays.shuffle(a, random);
    }
}
