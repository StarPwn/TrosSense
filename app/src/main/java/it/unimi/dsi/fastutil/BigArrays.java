package it.unimi.dsi.fastutil;

import it.unimi.dsi.fastutil.booleans.BooleanArrays;
import it.unimi.dsi.fastutil.booleans.BooleanBigArrays;
import it.unimi.dsi.fastutil.bytes.ByteArrays;
import it.unimi.dsi.fastutil.bytes.ByteBigArrays;
import it.unimi.dsi.fastutil.chars.CharArrays;
import it.unimi.dsi.fastutil.chars.CharBigArrays;
import it.unimi.dsi.fastutil.doubles.DoubleArrays;
import it.unimi.dsi.fastutil.doubles.DoubleBigArrays;
import it.unimi.dsi.fastutil.floats.FloatArrays;
import it.unimi.dsi.fastutil.floats.FloatBigArrays;
import it.unimi.dsi.fastutil.ints.IntArrays;
import it.unimi.dsi.fastutil.ints.IntBigArrays;
import it.unimi.dsi.fastutil.longs.LongArrays;
import it.unimi.dsi.fastutil.longs.LongBigArrays;
import it.unimi.dsi.fastutil.longs.LongComparator;
import it.unimi.dsi.fastutil.objects.ObjectArrays;
import it.unimi.dsi.fastutil.objects.ObjectBigArrays;
import it.unimi.dsi.fastutil.shorts.ShortArrays;
import it.unimi.dsi.fastutil.shorts.ShortBigArrays;
import java.lang.reflect.Array;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLongArray;
import okhttp3.HttpUrl;

/* loaded from: classes4.dex */
public class BigArrays {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int MEDIUM = 40;
    public static final int SEGMENT_MASK = 134217727;
    public static final int SEGMENT_SHIFT = 27;
    public static final int SEGMENT_SIZE = 134217728;
    private static final int SMALL = 7;

    protected BigArrays() {
    }

    public static int segment(long index) {
        return (int) (index >>> 27);
    }

    public static int displacement(long index) {
        return (int) (134217727 & index);
    }

    public static long start(int segment) {
        return segment << 27;
    }

    public static long nearestSegmentStart(long index, long min, long max) {
        long lower = start(segment(index));
        long upper = start(segment(index) + 1);
        if (upper >= max) {
            if (lower < min) {
                return index;
            }
            return lower;
        }
        if (lower < min) {
            return upper;
        }
        long mid = ((upper - lower) >> 1) + lower;
        return index <= mid ? lower : upper;
    }

    public static long index(int segment, int displacement) {
        return start(segment) + displacement;
    }

    public static void ensureFromTo(long bigArrayLength, long from, long to) {
        if (bigArrayLength < 0) {
            throw new AssertionError();
        }
        if (from < 0) {
            throw new ArrayIndexOutOfBoundsException("Start index (" + from + ") is negative");
        }
        if (from > to) {
            throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        if (to > bigArrayLength) {
            throw new ArrayIndexOutOfBoundsException("End index (" + to + ") is greater than big-array length (" + bigArrayLength + ")");
        }
    }

    public static void ensureOffsetLength(long bigArrayLength, long offset, long length) {
        String m;
        if (bigArrayLength < 0) {
            throw new AssertionError();
        }
        if (offset < 0) {
            throw new ArrayIndexOutOfBoundsException("Offset (" + offset + ") is negative");
        }
        if (length < 0) {
            throw new IllegalArgumentException("Length (" + length + ") is negative");
        }
        if (length > bigArrayLength - offset) {
            StringBuilder append = new StringBuilder().append("Last index (");
            m = BigArrays$$ExternalSyntheticBackport2.m(offset + length, 10);
            throw new ArrayIndexOutOfBoundsException(append.append(m).append(") is greater than big-array length (").append(bigArrayLength).append(")").toString());
        }
    }

    public static void ensureLength(long bigArrayLength) {
        if (bigArrayLength < 0) {
            throw new IllegalArgumentException("Negative big-array size: " + bigArrayLength);
        }
        if (bigArrayLength >= 288230376017494016L) {
            throw new IllegalArgumentException("Big-array size too big: " + bigArrayLength);
        }
    }

    private static void inPlaceMerge(long from, long mid, long to, LongComparator comp, BigSwapper swapper) {
        long firstCut;
        long firstCut2;
        if (from < mid && mid < to) {
            if (to - from == 2) {
                if (comp.compare(mid, from) < 0) {
                    swapper.swap(from, mid);
                    return;
                }
                return;
            }
            if (mid - from > to - mid) {
                long firstCut3 = from + ((mid - from) / 2);
                firstCut2 = firstCut3;
                firstCut = lowerBound(mid, to, firstCut3, comp);
            } else {
                long secondCut = to - mid;
                firstCut = mid + (secondCut / 2);
                firstCut2 = upperBound(from, mid, firstCut, comp);
            }
            long first2 = firstCut2;
            long last2 = firstCut;
            if (mid != first2 && mid != last2) {
                long first1 = first2;
                long last1 = mid;
                while (true) {
                    long j = last1 - 1;
                    last1 = j;
                    if (first1 >= j) {
                        break;
                    }
                    swapper.swap(first1, last1);
                    first1 = 1 + first1;
                }
                long first12 = mid;
                long last12 = last2;
                while (true) {
                    long j2 = last12 - 1;
                    last12 = j2;
                    if (first12 >= j2) {
                        break;
                    }
                    swapper.swap(first12, last12);
                    first12++;
                }
                long first13 = first2;
                long last13 = last2;
                while (true) {
                    long j3 = last13 - 1;
                    last13 = j3;
                    if (first13 >= j3) {
                        break;
                    }
                    swapper.swap(first13, last13);
                    first13++;
                }
            }
            long mid2 = firstCut2 + (firstCut - mid);
            inPlaceMerge(from, firstCut2, mid2, comp, swapper);
            inPlaceMerge(mid2, firstCut, to, comp, swapper);
        }
    }

    private static long lowerBound(long mid, long to, long firstCut, LongComparator comp) {
        long len = to - mid;
        while (len > 0) {
            long half = len / 2;
            long middle = mid + half;
            if (comp.compare(middle, firstCut) < 0) {
                mid = middle + 1;
                len -= 1 + half;
            } else {
                len = half;
            }
        }
        return mid;
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0018, code lost:            if (r1 > 0) goto L11;     */
    /* JADX WARN: Code restructure failed: missing block: B:4:0x0011, code lost:            if (r1 < 0) goto L11;     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x001d, code lost:            return r5;     */
    /* JADX WARN: Code restructure failed: missing block: B:9:?, code lost:            return r9;     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static long med3(long r5, long r7, long r9, it.unimi.dsi.fastutil.longs.LongComparator r11) {
        /*
            int r0 = r11.compare(r5, r7)
            int r1 = r11.compare(r5, r9)
            int r2 = r11.compare(r7, r9)
            if (r0 >= 0) goto L14
            if (r2 >= 0) goto L11
            goto L16
        L11:
            if (r1 >= 0) goto L1c
            goto L1a
        L14:
            if (r2 <= 0) goto L18
        L16:
            r3 = r7
            goto L1d
        L18:
            if (r1 <= 0) goto L1c
        L1a:
            r3 = r9
            goto L1d
        L1c:
            r3 = r5
        L1d:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.BigArrays.med3(long, long, long, it.unimi.dsi.fastutil.longs.LongComparator):long");
    }

    public static void mergeSort(long from, long to, LongComparator comp, BigSwapper swapper) {
        long length = to - from;
        if (length < 7) {
            for (long i = from; i < to; i++) {
                for (long j = i; j > from && comp.compare(j - 1, j) > 0; j--) {
                    swapper.swap(j, j - 1);
                }
            }
            return;
        }
        long mid = (from + to) >>> 1;
        mergeSort(from, mid, comp, swapper);
        mergeSort(mid, to, comp, swapper);
        if (comp.compare(mid - 1, mid) <= 0) {
            return;
        }
        inPlaceMerge(from, mid, to, comp, swapper);
    }

    public static void quickSort(long from, long to, LongComparator comp, BigSwapper swapper) {
        long d;
        long m;
        int comparison;
        long j;
        int comparison2;
        long j2;
        LongComparator longComparator = comp;
        long len = to - from;
        if (len < 7) {
            for (long i = from; i < to; i++) {
                for (long j3 = i; j3 > from && longComparator.compare(j3 - 1, j3) > 0; j3--) {
                    swapper.swap(j3, j3 - 1);
                }
            }
            return;
        }
        long m2 = from + (len / 2);
        if (len > 7) {
            long l = from;
            long n = to - 1;
            if (len > 40) {
                long s = len / 8;
                l = med3(l, l + s, l + (s * 2), comp);
                m2 = med3(m2 - s, m2, m2 + s, comp);
                n = med3(n - (2 * s), n - s, n, comp);
            }
            m2 = med3(l, m2, n, comp);
        }
        long c = to - 1;
        long m3 = m2;
        long b = c;
        long a = from;
        long m4 = c;
        long b2 = from;
        while (true) {
            if (b2 <= m4 && (comparison2 = longComparator.compare(b2, m3)) <= 0) {
                if (comparison2 != 0) {
                    j2 = 1;
                } else {
                    if (a == m3) {
                        m3 = b2;
                    } else if (b2 == m3) {
                        m3 = a;
                    }
                    j2 = 1;
                    swapper.swap(a, b2);
                    a++;
                }
                b2 += j2;
            } else {
                d = b;
                long m5 = m3;
                m = m4;
                while (m >= b2 && (comparison = longComparator.compare(m, m5)) >= 0) {
                    if (comparison != 0) {
                        j = 1;
                    } else {
                        if (m == m5) {
                            m5 = d;
                        } else if (d == m5) {
                            m5 = m;
                        }
                        j = 1;
                        swapper.swap(m, d);
                        d--;
                    }
                    m -= j;
                }
                if (b2 > m) {
                    break;
                }
                long m6 = m5;
                long d2 = d;
                long b3 = b2;
                long m7 = b3 == m6 ? d2 : m == m6 ? m : m6;
                long c2 = m - 1;
                swapper.swap(b3, m);
                longComparator = comp;
                m3 = m7;
                b2 = b3 + 1;
                m4 = c2;
                b = d2;
            }
        }
        long n2 = from + len;
        long d3 = d;
        long s2 = Math.min(a - from, b2 - a);
        long b4 = b2;
        vecSwap(swapper, from, b2 - s2, s2);
        long s3 = Math.min(d3 - m, (n2 - d3) - 1);
        vecSwap(swapper, b4, n2 - s3, s3);
        long s4 = b4 - a;
        if (s4 > 1) {
            quickSort(from, from + s4, comp, swapper);
        }
        long s5 = d3 - m;
        if (s5 > 1) {
            quickSort(n2 - s5, n2, comp, swapper);
        }
    }

    private static long upperBound(long from, long mid, long secondCut, LongComparator comp) {
        long len = mid - from;
        while (len > 0) {
            long half = len / 2;
            long middle = from + half;
            if (comp.compare(secondCut, middle) < 0) {
                len = half;
            } else {
                from = middle + 1;
                len -= 1 + half;
            }
        }
        return from;
    }

    private static void vecSwap(BigSwapper swapper, long from, long l, long s) {
        int i = 0;
        while (i < s) {
            swapper.swap(from, l);
            i++;
            from++;
            l++;
        }
    }

    public static byte get(byte[][] array, long index) {
        return array[segment(index)][displacement(index)];
    }

    public static void set(byte[][] array, long index, byte value) {
        array[segment(index)][displacement(index)] = value;
    }

    public static void swap(byte[][] array, long first, long second) {
        byte t = array[segment(first)][displacement(first)];
        array[segment(first)][displacement(first)] = array[segment(second)][displacement(second)];
        array[segment(second)][displacement(second)] = t;
    }

    public static byte[][] reverse(byte[][] a) {
        long length = length(a);
        long i = length / 2;
        while (true) {
            long i2 = i - 1;
            if (i == 0) {
                return a;
            }
            swap(a, i2, (length - i2) - 1);
            i = i2;
        }
    }

    public static void add(byte[][] array, long index, byte incr) {
        byte[] bArr = array[segment(index)];
        int displacement = displacement(index);
        bArr[displacement] = (byte) (bArr[displacement] + incr);
    }

    public static void mul(byte[][] array, long index, byte factor) {
        byte[] bArr = array[segment(index)];
        int displacement = displacement(index);
        bArr[displacement] = (byte) (bArr[displacement] * factor);
    }

    public static void incr(byte[][] array, long index) {
        byte[] bArr = array[segment(index)];
        int displacement = displacement(index);
        bArr[displacement] = (byte) (bArr[displacement] + 1);
    }

    public static void decr(byte[][] array, long index) {
        array[segment(index)][displacement(index)] = (byte) (r0[r1] - 1);
    }

    public static void assertBigArray(byte[][] array) {
        int l = array.length;
        if (l == 0) {
            return;
        }
        for (int i = 0; i < l - 1; i++) {
            if (array[i].length != 134217728) {
                throw new IllegalStateException("All segments except for the last one must be of length 2^" + Integer.toString(27));
            }
        }
        int i2 = l - 1;
        if (array[i2].length > 134217728) {
            throw new IllegalStateException("The last segment must be of length at most 2^" + Integer.toString(27));
        }
        if (array[l - 1].length == 0 && l == 1) {
            throw new IllegalStateException("The last segment must be of nonzero length");
        }
    }

    public static long length(byte[][] array) {
        int length = array.length;
        if (length == 0) {
            return 0L;
        }
        return start(length - 1) + array[length - 1].length;
    }

    public static void copy(byte[][] srcArray, long srcPos, byte[][] destArray, long destPos, long length) {
        if (destPos <= srcPos) {
            int srcSegment = segment(srcPos);
            int destSegment = segment(destPos);
            int srcDispl = displacement(srcPos);
            int srcDispl2 = srcDispl;
            int destDispl = displacement(destPos);
            int destSegment2 = destSegment;
            long length2 = length;
            while (length2 > 0) {
                int l = (int) Math.min(length2, Math.min(srcArray[srcSegment].length - srcDispl2, destArray[destSegment2].length - destDispl));
                if (l == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl2, destArray[destSegment2], destDispl, l);
                int i = srcDispl2 + l;
                srcDispl2 = i;
                if (i == 134217728) {
                    srcDispl2 = 0;
                    srcSegment++;
                }
                int i2 = destDispl + l;
                destDispl = i2;
                if (i2 == 134217728) {
                    destDispl = 0;
                    destSegment2++;
                }
                length2 -= l;
            }
            return;
        }
        int srcSegment2 = segment(srcPos + length);
        int destSegment3 = segment(destPos + length);
        int srcDispl3 = displacement(srcPos + length);
        int srcDispl4 = srcDispl3;
        int destDispl2 = displacement(destPos + length);
        int destSegment4 = destSegment3;
        long length3 = length;
        while (length3 > 0) {
            if (srcDispl4 == 0) {
                srcDispl4 = SEGMENT_SIZE;
                srcSegment2--;
            }
            if (destDispl2 == 0) {
                destDispl2 = SEGMENT_SIZE;
                destSegment4--;
            }
            int l2 = (int) Math.min(length3, Math.min(srcDispl4, destDispl2));
            if (l2 == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray[srcSegment2], srcDispl4 - l2, destArray[destSegment4], destDispl2 - l2, l2);
            srcDispl4 -= l2;
            destDispl2 -= l2;
            length3 -= l2;
        }
    }

    public static void copyFromBig(byte[][] srcArray, long srcPos, byte[] destArray, int destPos, int length) {
        int srcSegment = segment(srcPos);
        int srcDispl = displacement(srcPos);
        while (length > 0) {
            int l = Math.min(srcArray[srcSegment].length - srcDispl, length);
            if (l == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray[srcSegment], srcDispl, destArray, destPos, l);
            int i = srcDispl + l;
            srcDispl = i;
            if (i == 134217728) {
                srcDispl = 0;
                srcSegment++;
            }
            destPos += l;
            length -= l;
        }
    }

    public static void copyToBig(byte[] srcArray, int srcPos, byte[][] destArray, long destPos, long length) {
        int destSegment = segment(destPos);
        int destDispl = displacement(destPos);
        while (length > 0) {
            int l = (int) Math.min(destArray[destSegment].length - destDispl, length);
            if (l == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray, srcPos, destArray[destSegment], destDispl, l);
            int i = destDispl + l;
            destDispl = i;
            if (i == 134217728) {
                destDispl = 0;
                destSegment++;
            }
            srcPos += l;
            length -= l;
        }
    }

    public static byte[][] wrap(byte[] array) {
        if (array.length == 0) {
            return ByteBigArrays.EMPTY_BIG_ARRAY;
        }
        if (array.length <= 134217728) {
            return new byte[][]{array};
        }
        byte[][] bigArray = ByteBigArrays.newBigArray(array.length);
        for (int i = 0; i < bigArray.length; i++) {
            System.arraycopy(array, (int) start(i), bigArray[i], 0, bigArray[i].length);
        }
        return bigArray;
    }

    public static byte[][] ensureCapacity(byte[][] array, long length) {
        return ensureCapacity(array, length, length(array));
    }

    /* JADX WARN: Code restructure failed: missing block: B:6:0x0013, code lost:            if (r15[r15.length - 1].length == 134217728) goto L8;     */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0057  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0040  */
    /* JADX WARN: Removed duplicated region for block: B:9:0x002d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static byte[][] forceCapacity(byte[][] r15, long r16, long r18) {
        /*
            r8 = r15
            ensureLength(r16)
            int r0 = r8.length
            int r1 = r8.length
            r2 = 134217728(0x8000000, float:3.85186E-34)
            if (r1 == 0) goto L15
            int r1 = r8.length
            r3 = 1
            if (r1 <= 0) goto L16
            int r1 = r8.length
            int r1 = r1 - r3
            r1 = r8[r1]
            int r1 = r1.length
            if (r1 != r2) goto L16
        L15:
            r3 = 0
        L16:
            int r9 = r0 - r3
            r0 = 134217727(0x7ffffff, double:6.6312368E-316)
            long r3 = r16 + r0
            r5 = 27
            long r3 = r3 >>> r5
            int r10 = (int) r3
            java.lang.Object[] r3 = java.util.Arrays.copyOf(r15, r10)
            r11 = r3
            byte[][] r11 = (byte[][]) r11
            long r0 = r16 & r0
            int r12 = (int) r0
            if (r12 == 0) goto L40
            r0 = r9
        L2e:
            int r1 = r10 + (-1)
            if (r0 >= r1) goto L39
            byte[] r1 = new byte[r2]
            r11[r0] = r1
            int r0 = r0 + 1
            goto L2e
        L39:
            int r0 = r10 + (-1)
            byte[] r1 = new byte[r12]
            r11[r0] = r1
            goto L4a
        L40:
            r0 = r9
        L41:
            if (r0 >= r10) goto L4a
            byte[] r1 = new byte[r2]
            r11[r0] = r1
            int r0 = r0 + 1
            goto L41
        L4a:
            long r0 = (long) r9
            r2 = 134217728(0x8000000, double:6.63123685E-316)
            long r0 = r0 * r2
            long r0 = r18 - r0
            r4 = 0
            int r0 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r0 <= 0) goto L69
            long r0 = (long) r9
            long r4 = r0 * r2
            long r0 = (long) r9
            long r6 = r0 * r2
            long r0 = (long) r9
            long r0 = r0 * r2
            long r13 = r18 - r0
            r0 = r15
            r1 = r4
            r3 = r11
            r4 = r6
            r6 = r13
            copy(r0, r1, r3, r4, r6)
        L69:
            return r11
        */
        throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.BigArrays.forceCapacity(byte[][], long, long):byte[][]");
    }

    public static byte[][] ensureCapacity(byte[][] array, long length, long preserve) {
        return length > length(array) ? forceCapacity(array, length, preserve) : array;
    }

    public static byte[][] grow(byte[][] array, long length) {
        long oldLength = length(array);
        return length > oldLength ? grow(array, length, oldLength) : array;
    }

    public static byte[][] grow(byte[][] array, long length, long preserve) {
        long oldLength = length(array);
        return length > oldLength ? ensureCapacity(array, Math.max((oldLength >> 1) + oldLength, length), preserve) : array;
    }

    public static byte[][] trim(byte[][] array, long length) {
        ensureLength(length);
        long oldLength = length(array);
        if (length >= oldLength) {
            return array;
        }
        int baseLength = (int) ((length + 134217727) >>> 27);
        byte[][] base = (byte[][]) java.util.Arrays.copyOf(array, baseLength);
        int residual = (int) (134217727 & length);
        if (residual != 0) {
            base[baseLength - 1] = ByteArrays.trim(base[baseLength - 1], residual);
        }
        return base;
    }

    public static byte[][] setLength(byte[][] array, long length) {
        long oldLength = length(array);
        return length == oldLength ? array : length < oldLength ? trim(array, length) : ensureCapacity(array, length);
    }

    public static byte[][] copy(byte[][] array, long offset, long length) {
        ensureOffsetLength(array, offset, length);
        byte[][] a = ByteBigArrays.newBigArray(length);
        copy(array, offset, a, 0L, length);
        return a;
    }

    public static byte[][] copy(byte[][] array) {
        byte[][] base = (byte[][]) array.clone();
        int i = base.length;
        while (true) {
            int i2 = i - 1;
            if (i == 0) {
                return base;
            }
            base[i2] = (byte[]) array[i2].clone();
            i = i2;
        }
    }

    public static void fill(byte[][] array, byte value) {
        int i = array.length;
        while (true) {
            int i2 = i - 1;
            if (i == 0) {
                return;
            }
            java.util.Arrays.fill(array[i2], value);
            i = i2;
        }
    }

    public static void fill(byte[][] array, long from, long to, byte value) {
        long length = length(array);
        ensureFromTo(length, from, to);
        if (length == 0) {
            return;
        }
        int fromSegment = segment(from);
        int toSegment = segment(to);
        int fromDispl = displacement(from);
        int toDispl = displacement(to);
        if (fromSegment == toSegment) {
            java.util.Arrays.fill(array[fromSegment], fromDispl, toDispl, value);
            return;
        }
        if (toDispl != 0) {
            java.util.Arrays.fill(array[toSegment], 0, toDispl, value);
        }
        while (true) {
            toSegment--;
            if (toSegment <= fromSegment) {
                java.util.Arrays.fill(array[fromSegment], fromDispl, SEGMENT_SIZE, value);
                return;
            }
            java.util.Arrays.fill(array[toSegment], value);
        }
    }

    public static boolean equals(byte[][] a1, byte[][] a2) {
        if (length(a1) != length(a2)) {
            return false;
        }
        int i = a1.length;
        while (true) {
            int i2 = i - 1;
            if (i != 0) {
                byte[] t = a1[i2];
                byte[] u = a2[i2];
                int j = t.length;
                while (true) {
                    int j2 = j - 1;
                    if (j != 0) {
                        if (t[j2] != u[j2]) {
                            return false;
                        }
                        j = j2;
                    }
                }
            } else {
                return true;
            }
            i = i2;
        }
    }

    public static String toString(byte[][] a) {
        if (a == null) {
            return "null";
        }
        long last = length(a) - 1;
        if (last == -1) {
            return HttpUrl.PATH_SEGMENT_ENCODE_SET_URI;
        }
        StringBuilder b = new StringBuilder();
        b.append('[');
        long i = 0;
        while (true) {
            b.append(String.valueOf((int) get(a, i)));
            if (i == last) {
                return b.append(']').toString();
            }
            b.append(", ");
            i++;
        }
    }

    public static void ensureFromTo(byte[][] a, long from, long to) {
        ensureFromTo(length(a), from, to);
    }

    public static void ensureOffsetLength(byte[][] a, long offset, long length) {
        ensureOffsetLength(length(a), offset, length);
    }

    public static void ensureSameLength(byte[][] a, byte[][] b) {
        if (length(a) != length(b)) {
            throw new IllegalArgumentException("Array size mismatch: " + length(a) + " != " + length(b));
        }
    }

    public static byte[][] shuffle(byte[][] a, long from, long to, Random random) {
        long p = to - from;
        while (true) {
            long i = p - 1;
            if (p != 0) {
                long p2 = (random.nextLong() & Long.MAX_VALUE) % (1 + i);
                byte t = get(a, from + i);
                set(a, from + i, get(a, from + p2));
                set(a, from + p2, t);
                p = i;
            } else {
                return a;
            }
        }
    }

    public static byte[][] shuffle(byte[][] a, Random random) {
        long p = length(a);
        while (true) {
            long i = p - 1;
            if (p != 0) {
                long p2 = (random.nextLong() & Long.MAX_VALUE) % (1 + i);
                byte t = get(a, i);
                set(a, i, get(a, p2));
                set(a, p2, t);
                p = i;
            } else {
                return a;
            }
        }
    }

    public static int get(int[][] array, long index) {
        return array[segment(index)][displacement(index)];
    }

    public static void set(int[][] array, long index, int value) {
        array[segment(index)][displacement(index)] = value;
    }

    public static long length(AtomicIntegerArray[] array) {
        int length = array.length;
        if (length == 0) {
            return 0L;
        }
        return start(length - 1) + array[length - 1].length();
    }

    public static int get(AtomicIntegerArray[] array, long index) {
        return array[segment(index)].get(displacement(index));
    }

    public static void set(AtomicIntegerArray[] array, long index, int value) {
        array[segment(index)].set(displacement(index), value);
    }

    public static int getAndSet(AtomicIntegerArray[] array, long index, int value) {
        return array[segment(index)].getAndSet(displacement(index), value);
    }

    public static int getAndAdd(AtomicIntegerArray[] array, long index, int value) {
        return array[segment(index)].getAndAdd(displacement(index), value);
    }

    public static int addAndGet(AtomicIntegerArray[] array, long index, int value) {
        return array[segment(index)].addAndGet(displacement(index), value);
    }

    public static int getAndIncrement(AtomicIntegerArray[] array, long index) {
        return array[segment(index)].getAndDecrement(displacement(index));
    }

    public static int incrementAndGet(AtomicIntegerArray[] array, long index) {
        return array[segment(index)].incrementAndGet(displacement(index));
    }

    public static int getAndDecrement(AtomicIntegerArray[] array, long index) {
        return array[segment(index)].getAndDecrement(displacement(index));
    }

    public static int decrementAndGet(AtomicIntegerArray[] array, long index) {
        return array[segment(index)].decrementAndGet(displacement(index));
    }

    public static boolean compareAndSet(AtomicIntegerArray[] array, long index, int expected, int value) {
        return array[segment(index)].compareAndSet(displacement(index), expected, value);
    }

    public static void swap(int[][] array, long first, long second) {
        int t = array[segment(first)][displacement(first)];
        array[segment(first)][displacement(first)] = array[segment(second)][displacement(second)];
        array[segment(second)][displacement(second)] = t;
    }

    public static int[][] reverse(int[][] a) {
        long length = length(a);
        long i = length / 2;
        while (true) {
            long i2 = i - 1;
            if (i == 0) {
                return a;
            }
            swap(a, i2, (length - i2) - 1);
            i = i2;
        }
    }

    public static void add(int[][] array, long index, int incr) {
        int[] iArr = array[segment(index)];
        int displacement = displacement(index);
        iArr[displacement] = iArr[displacement] + incr;
    }

    public static void mul(int[][] array, long index, int factor) {
        int[] iArr = array[segment(index)];
        int displacement = displacement(index);
        iArr[displacement] = iArr[displacement] * factor;
    }

    public static void incr(int[][] array, long index) {
        int[] iArr = array[segment(index)];
        int displacement = displacement(index);
        iArr[displacement] = iArr[displacement] + 1;
    }

    public static void decr(int[][] array, long index) {
        array[segment(index)][displacement(index)] = r0[r1] - 1;
    }

    public static void assertBigArray(int[][] array) {
        int l = array.length;
        if (l == 0) {
            return;
        }
        for (int i = 0; i < l - 1; i++) {
            if (array[i].length != 134217728) {
                throw new IllegalStateException("All segments except for the last one must be of length 2^" + Integer.toString(27));
            }
        }
        int i2 = l - 1;
        if (array[i2].length > 134217728) {
            throw new IllegalStateException("The last segment must be of length at most 2^" + Integer.toString(27));
        }
        if (array[l - 1].length == 0 && l == 1) {
            throw new IllegalStateException("The last segment must be of nonzero length");
        }
    }

    public static long length(int[][] array) {
        int length = array.length;
        if (length == 0) {
            return 0L;
        }
        return start(length - 1) + array[length - 1].length;
    }

    public static void copy(int[][] srcArray, long srcPos, int[][] destArray, long destPos, long length) {
        if (destPos <= srcPos) {
            int srcSegment = segment(srcPos);
            int destSegment = segment(destPos);
            int srcDispl = displacement(srcPos);
            int srcDispl2 = srcDispl;
            int destDispl = displacement(destPos);
            int destSegment2 = destSegment;
            long length2 = length;
            while (length2 > 0) {
                int l = (int) Math.min(length2, Math.min(srcArray[srcSegment].length - srcDispl2, destArray[destSegment2].length - destDispl));
                if (l == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl2, destArray[destSegment2], destDispl, l);
                int i = srcDispl2 + l;
                srcDispl2 = i;
                if (i == 134217728) {
                    srcDispl2 = 0;
                    srcSegment++;
                }
                int i2 = destDispl + l;
                destDispl = i2;
                if (i2 == 134217728) {
                    destDispl = 0;
                    destSegment2++;
                }
                length2 -= l;
            }
            return;
        }
        int srcSegment2 = segment(srcPos + length);
        int destSegment3 = segment(destPos + length);
        int srcDispl3 = displacement(srcPos + length);
        int srcDispl4 = srcDispl3;
        int destDispl2 = displacement(destPos + length);
        int destSegment4 = destSegment3;
        long length3 = length;
        while (length3 > 0) {
            if (srcDispl4 == 0) {
                srcDispl4 = SEGMENT_SIZE;
                srcSegment2--;
            }
            if (destDispl2 == 0) {
                destDispl2 = SEGMENT_SIZE;
                destSegment4--;
            }
            int l2 = (int) Math.min(length3, Math.min(srcDispl4, destDispl2));
            if (l2 == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray[srcSegment2], srcDispl4 - l2, destArray[destSegment4], destDispl2 - l2, l2);
            srcDispl4 -= l2;
            destDispl2 -= l2;
            length3 -= l2;
        }
    }

    public static void copyFromBig(int[][] srcArray, long srcPos, int[] destArray, int destPos, int length) {
        int srcSegment = segment(srcPos);
        int srcDispl = displacement(srcPos);
        while (length > 0) {
            int l = Math.min(srcArray[srcSegment].length - srcDispl, length);
            if (l == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray[srcSegment], srcDispl, destArray, destPos, l);
            int i = srcDispl + l;
            srcDispl = i;
            if (i == 134217728) {
                srcDispl = 0;
                srcSegment++;
            }
            destPos += l;
            length -= l;
        }
    }

    public static void copyToBig(int[] srcArray, int srcPos, int[][] destArray, long destPos, long length) {
        int destSegment = segment(destPos);
        int destDispl = displacement(destPos);
        while (length > 0) {
            int l = (int) Math.min(destArray[destSegment].length - destDispl, length);
            if (l == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray, srcPos, destArray[destSegment], destDispl, l);
            int i = destDispl + l;
            destDispl = i;
            if (i == 134217728) {
                destDispl = 0;
                destSegment++;
            }
            srcPos += l;
            length -= l;
        }
    }

    public static int[][] wrap(int[] array) {
        if (array.length == 0) {
            return IntBigArrays.EMPTY_BIG_ARRAY;
        }
        if (array.length <= 134217728) {
            return new int[][]{array};
        }
        int[][] bigArray = IntBigArrays.newBigArray(array.length);
        for (int i = 0; i < bigArray.length; i++) {
            System.arraycopy(array, (int) start(i), bigArray[i], 0, bigArray[i].length);
        }
        return bigArray;
    }

    public static int[][] ensureCapacity(int[][] array, long length) {
        return ensureCapacity(array, length, length(array));
    }

    /* JADX WARN: Code restructure failed: missing block: B:6:0x0013, code lost:            if (r15[r15.length - 1].length == 134217728) goto L8;     */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0057  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0040  */
    /* JADX WARN: Removed duplicated region for block: B:9:0x002d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int[][] forceCapacity(int[][] r15, long r16, long r18) {
        /*
            r8 = r15
            ensureLength(r16)
            int r0 = r8.length
            int r1 = r8.length
            r2 = 134217728(0x8000000, float:3.85186E-34)
            if (r1 == 0) goto L15
            int r1 = r8.length
            r3 = 1
            if (r1 <= 0) goto L16
            int r1 = r8.length
            int r1 = r1 - r3
            r1 = r8[r1]
            int r1 = r1.length
            if (r1 != r2) goto L16
        L15:
            r3 = 0
        L16:
            int r9 = r0 - r3
            r0 = 134217727(0x7ffffff, double:6.6312368E-316)
            long r3 = r16 + r0
            r5 = 27
            long r3 = r3 >>> r5
            int r10 = (int) r3
            java.lang.Object[] r3 = java.util.Arrays.copyOf(r15, r10)
            r11 = r3
            int[][] r11 = (int[][]) r11
            long r0 = r16 & r0
            int r12 = (int) r0
            if (r12 == 0) goto L40
            r0 = r9
        L2e:
            int r1 = r10 + (-1)
            if (r0 >= r1) goto L39
            int[] r1 = new int[r2]
            r11[r0] = r1
            int r0 = r0 + 1
            goto L2e
        L39:
            int r0 = r10 + (-1)
            int[] r1 = new int[r12]
            r11[r0] = r1
            goto L4a
        L40:
            r0 = r9
        L41:
            if (r0 >= r10) goto L4a
            int[] r1 = new int[r2]
            r11[r0] = r1
            int r0 = r0 + 1
            goto L41
        L4a:
            long r0 = (long) r9
            r2 = 134217728(0x8000000, double:6.63123685E-316)
            long r0 = r0 * r2
            long r0 = r18 - r0
            r4 = 0
            int r0 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r0 <= 0) goto L69
            long r0 = (long) r9
            long r4 = r0 * r2
            long r0 = (long) r9
            long r6 = r0 * r2
            long r0 = (long) r9
            long r0 = r0 * r2
            long r13 = r18 - r0
            r0 = r15
            r1 = r4
            r3 = r11
            r4 = r6
            r6 = r13
            copy(r0, r1, r3, r4, r6)
        L69:
            return r11
        */
        throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.BigArrays.forceCapacity(int[][], long, long):int[][]");
    }

    public static int[][] ensureCapacity(int[][] array, long length, long preserve) {
        return length > length(array) ? forceCapacity(array, length, preserve) : array;
    }

    public static int[][] grow(int[][] array, long length) {
        long oldLength = length(array);
        return length > oldLength ? grow(array, length, oldLength) : array;
    }

    public static int[][] grow(int[][] array, long length, long preserve) {
        long oldLength = length(array);
        return length > oldLength ? ensureCapacity(array, Math.max((oldLength >> 1) + oldLength, length), preserve) : array;
    }

    public static int[][] trim(int[][] array, long length) {
        ensureLength(length);
        long oldLength = length(array);
        if (length >= oldLength) {
            return array;
        }
        int baseLength = (int) ((length + 134217727) >>> 27);
        int[][] base = (int[][]) java.util.Arrays.copyOf(array, baseLength);
        int residual = (int) (134217727 & length);
        if (residual != 0) {
            base[baseLength - 1] = IntArrays.trim(base[baseLength - 1], residual);
        }
        return base;
    }

    public static int[][] setLength(int[][] array, long length) {
        long oldLength = length(array);
        return length == oldLength ? array : length < oldLength ? trim(array, length) : ensureCapacity(array, length);
    }

    public static int[][] copy(int[][] array, long offset, long length) {
        ensureOffsetLength(array, offset, length);
        int[][] a = IntBigArrays.newBigArray(length);
        copy(array, offset, a, 0L, length);
        return a;
    }

    public static int[][] copy(int[][] array) {
        int[][] base = (int[][]) array.clone();
        int i = base.length;
        while (true) {
            int i2 = i - 1;
            if (i == 0) {
                return base;
            }
            base[i2] = (int[]) array[i2].clone();
            i = i2;
        }
    }

    public static void fill(int[][] array, int value) {
        int i = array.length;
        while (true) {
            int i2 = i - 1;
            if (i == 0) {
                return;
            }
            java.util.Arrays.fill(array[i2], value);
            i = i2;
        }
    }

    public static void fill(int[][] array, long from, long to, int value) {
        long length = length(array);
        ensureFromTo(length, from, to);
        if (length == 0) {
            return;
        }
        int fromSegment = segment(from);
        int toSegment = segment(to);
        int fromDispl = displacement(from);
        int toDispl = displacement(to);
        if (fromSegment == toSegment) {
            java.util.Arrays.fill(array[fromSegment], fromDispl, toDispl, value);
            return;
        }
        if (toDispl != 0) {
            java.util.Arrays.fill(array[toSegment], 0, toDispl, value);
        }
        while (true) {
            toSegment--;
            if (toSegment <= fromSegment) {
                java.util.Arrays.fill(array[fromSegment], fromDispl, SEGMENT_SIZE, value);
                return;
            }
            java.util.Arrays.fill(array[toSegment], value);
        }
    }

    public static boolean equals(int[][] a1, int[][] a2) {
        if (length(a1) != length(a2)) {
            return false;
        }
        int i = a1.length;
        while (true) {
            int i2 = i - 1;
            if (i != 0) {
                int[] t = a1[i2];
                int[] u = a2[i2];
                int j = t.length;
                while (true) {
                    int j2 = j - 1;
                    if (j != 0) {
                        if (t[j2] != u[j2]) {
                            return false;
                        }
                        j = j2;
                    }
                }
            } else {
                return true;
            }
            i = i2;
        }
    }

    public static String toString(int[][] a) {
        if (a == null) {
            return "null";
        }
        long last = length(a) - 1;
        if (last == -1) {
            return HttpUrl.PATH_SEGMENT_ENCODE_SET_URI;
        }
        StringBuilder b = new StringBuilder();
        b.append('[');
        long i = 0;
        while (true) {
            b.append(String.valueOf(get(a, i)));
            if (i == last) {
                return b.append(']').toString();
            }
            b.append(", ");
            i++;
        }
    }

    public static void ensureFromTo(int[][] a, long from, long to) {
        ensureFromTo(length(a), from, to);
    }

    public static void ensureOffsetLength(int[][] a, long offset, long length) {
        ensureOffsetLength(length(a), offset, length);
    }

    public static void ensureSameLength(int[][] a, int[][] b) {
        if (length(a) != length(b)) {
            throw new IllegalArgumentException("Array size mismatch: " + length(a) + " != " + length(b));
        }
    }

    public static int[][] shuffle(int[][] a, long from, long to, Random random) {
        long p = to - from;
        while (true) {
            long i = p - 1;
            if (p != 0) {
                long p2 = (random.nextLong() & Long.MAX_VALUE) % (1 + i);
                int t = get(a, from + i);
                set(a, from + i, get(a, from + p2));
                set(a, from + p2, t);
                p = i;
            } else {
                return a;
            }
        }
    }

    public static int[][] shuffle(int[][] a, Random random) {
        long p = length(a);
        while (true) {
            long i = p - 1;
            if (p != 0) {
                long p2 = (random.nextLong() & Long.MAX_VALUE) % (1 + i);
                int t = get(a, i);
                set(a, i, get(a, p2));
                set(a, p2, t);
                p = i;
            } else {
                return a;
            }
        }
    }

    public static long get(long[][] array, long index) {
        return array[segment(index)][displacement(index)];
    }

    public static void set(long[][] array, long index, long value) {
        array[segment(index)][displacement(index)] = value;
    }

    public static long length(AtomicLongArray[] array) {
        int length = array.length;
        if (length == 0) {
            return 0L;
        }
        return start(length - 1) + array[length - 1].length();
    }

    public static long get(AtomicLongArray[] array, long index) {
        return array[segment(index)].get(displacement(index));
    }

    public static void set(AtomicLongArray[] array, long index, long value) {
        array[segment(index)].set(displacement(index), value);
    }

    public static long getAndSet(AtomicLongArray[] array, long index, long value) {
        return array[segment(index)].getAndSet(displacement(index), value);
    }

    public static long getAndAdd(AtomicLongArray[] array, long index, long value) {
        return array[segment(index)].getAndAdd(displacement(index), value);
    }

    public static long addAndGet(AtomicLongArray[] array, long index, long value) {
        return array[segment(index)].addAndGet(displacement(index), value);
    }

    public static long getAndIncrement(AtomicLongArray[] array, long index) {
        return array[segment(index)].getAndDecrement(displacement(index));
    }

    public static long incrementAndGet(AtomicLongArray[] array, long index) {
        return array[segment(index)].incrementAndGet(displacement(index));
    }

    public static long getAndDecrement(AtomicLongArray[] array, long index) {
        return array[segment(index)].getAndDecrement(displacement(index));
    }

    public static long decrementAndGet(AtomicLongArray[] array, long index) {
        return array[segment(index)].decrementAndGet(displacement(index));
    }

    public static boolean compareAndSet(AtomicLongArray[] array, long index, long expected, long value) {
        return array[segment(index)].compareAndSet(displacement(index), expected, value);
    }

    public static void swap(long[][] array, long first, long second) {
        long t = array[segment(first)][displacement(first)];
        array[segment(first)][displacement(first)] = array[segment(second)][displacement(second)];
        array[segment(second)][displacement(second)] = t;
    }

    public static long[][] reverse(long[][] a) {
        long length = length(a);
        long i = length / 2;
        while (true) {
            long i2 = i - 1;
            if (i == 0) {
                return a;
            }
            swap(a, i2, (length - i2) - 1);
            i = i2;
        }
    }

    public static void add(long[][] array, long index, long incr) {
        long[] jArr = array[segment(index)];
        int displacement = displacement(index);
        jArr[displacement] = jArr[displacement] + incr;
    }

    public static void mul(long[][] array, long index, long factor) {
        long[] jArr = array[segment(index)];
        int displacement = displacement(index);
        jArr[displacement] = jArr[displacement] * factor;
    }

    public static void incr(long[][] array, long index) {
        long[] jArr = array[segment(index)];
        int displacement = displacement(index);
        jArr[displacement] = jArr[displacement] + 1;
    }

    public static void decr(long[][] array, long index) {
        long[] jArr = array[segment(index)];
        int displacement = displacement(index);
        jArr[displacement] = jArr[displacement] - 1;
    }

    public static void assertBigArray(long[][] array) {
        int l = array.length;
        if (l == 0) {
            return;
        }
        for (int i = 0; i < l - 1; i++) {
            if (array[i].length != 134217728) {
                throw new IllegalStateException("All segments except for the last one must be of length 2^" + Integer.toString(27));
            }
        }
        int i2 = l - 1;
        if (array[i2].length > 134217728) {
            throw new IllegalStateException("The last segment must be of length at most 2^" + Integer.toString(27));
        }
        if (array[l - 1].length == 0 && l == 1) {
            throw new IllegalStateException("The last segment must be of nonzero length");
        }
    }

    public static long length(long[][] array) {
        int length = array.length;
        if (length == 0) {
            return 0L;
        }
        return start(length - 1) + array[length - 1].length;
    }

    public static void copy(long[][] srcArray, long srcPos, long[][] destArray, long destPos, long length) {
        if (destPos <= srcPos) {
            int srcSegment = segment(srcPos);
            int destSegment = segment(destPos);
            int srcDispl = displacement(srcPos);
            int srcDispl2 = srcDispl;
            int destDispl = displacement(destPos);
            int destSegment2 = destSegment;
            long length2 = length;
            while (length2 > 0) {
                int l = (int) Math.min(length2, Math.min(srcArray[srcSegment].length - srcDispl2, destArray[destSegment2].length - destDispl));
                if (l == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl2, destArray[destSegment2], destDispl, l);
                int i = srcDispl2 + l;
                srcDispl2 = i;
                if (i == 134217728) {
                    srcDispl2 = 0;
                    srcSegment++;
                }
                int i2 = destDispl + l;
                destDispl = i2;
                if (i2 == 134217728) {
                    destDispl = 0;
                    destSegment2++;
                }
                length2 -= l;
            }
            return;
        }
        int srcSegment2 = segment(srcPos + length);
        int destSegment3 = segment(destPos + length);
        int srcDispl3 = displacement(srcPos + length);
        int srcDispl4 = srcDispl3;
        int destDispl2 = displacement(destPos + length);
        int destSegment4 = destSegment3;
        long length3 = length;
        while (length3 > 0) {
            if (srcDispl4 == 0) {
                srcDispl4 = SEGMENT_SIZE;
                srcSegment2--;
            }
            if (destDispl2 == 0) {
                destDispl2 = SEGMENT_SIZE;
                destSegment4--;
            }
            int l2 = (int) Math.min(length3, Math.min(srcDispl4, destDispl2));
            if (l2 == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray[srcSegment2], srcDispl4 - l2, destArray[destSegment4], destDispl2 - l2, l2);
            srcDispl4 -= l2;
            destDispl2 -= l2;
            length3 -= l2;
        }
    }

    public static void copyFromBig(long[][] srcArray, long srcPos, long[] destArray, int destPos, int length) {
        int srcSegment = segment(srcPos);
        int srcDispl = displacement(srcPos);
        while (length > 0) {
            int l = Math.min(srcArray[srcSegment].length - srcDispl, length);
            if (l == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray[srcSegment], srcDispl, destArray, destPos, l);
            int i = srcDispl + l;
            srcDispl = i;
            if (i == 134217728) {
                srcDispl = 0;
                srcSegment++;
            }
            destPos += l;
            length -= l;
        }
    }

    public static void copyToBig(long[] srcArray, int srcPos, long[][] destArray, long destPos, long length) {
        int destSegment = segment(destPos);
        int destDispl = displacement(destPos);
        while (length > 0) {
            int l = (int) Math.min(destArray[destSegment].length - destDispl, length);
            if (l == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray, srcPos, destArray[destSegment], destDispl, l);
            int i = destDispl + l;
            destDispl = i;
            if (i == 134217728) {
                destDispl = 0;
                destSegment++;
            }
            srcPos += l;
            length -= l;
        }
    }

    public static long[][] wrap(long[] array) {
        if (array.length == 0) {
            return LongBigArrays.EMPTY_BIG_ARRAY;
        }
        if (array.length <= 134217728) {
            return new long[][]{array};
        }
        long[][] bigArray = LongBigArrays.newBigArray(array.length);
        for (int i = 0; i < bigArray.length; i++) {
            System.arraycopy(array, (int) start(i), bigArray[i], 0, bigArray[i].length);
        }
        return bigArray;
    }

    public static long[][] ensureCapacity(long[][] array, long length) {
        return ensureCapacity(array, length, length(array));
    }

    /* JADX WARN: Code restructure failed: missing block: B:6:0x0013, code lost:            if (r15[r15.length - 1].length == 134217728) goto L8;     */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0057  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0040  */
    /* JADX WARN: Removed duplicated region for block: B:9:0x002d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static long[][] forceCapacity(long[][] r15, long r16, long r18) {
        /*
            r8 = r15
            ensureLength(r16)
            int r0 = r8.length
            int r1 = r8.length
            r2 = 134217728(0x8000000, float:3.85186E-34)
            if (r1 == 0) goto L15
            int r1 = r8.length
            r3 = 1
            if (r1 <= 0) goto L16
            int r1 = r8.length
            int r1 = r1 - r3
            r1 = r8[r1]
            int r1 = r1.length
            if (r1 != r2) goto L16
        L15:
            r3 = 0
        L16:
            int r9 = r0 - r3
            r0 = 134217727(0x7ffffff, double:6.6312368E-316)
            long r3 = r16 + r0
            r5 = 27
            long r3 = r3 >>> r5
            int r10 = (int) r3
            java.lang.Object[] r3 = java.util.Arrays.copyOf(r15, r10)
            r11 = r3
            long[][] r11 = (long[][]) r11
            long r0 = r16 & r0
            int r12 = (int) r0
            if (r12 == 0) goto L40
            r0 = r9
        L2e:
            int r1 = r10 + (-1)
            if (r0 >= r1) goto L39
            long[] r1 = new long[r2]
            r11[r0] = r1
            int r0 = r0 + 1
            goto L2e
        L39:
            int r0 = r10 + (-1)
            long[] r1 = new long[r12]
            r11[r0] = r1
            goto L4a
        L40:
            r0 = r9
        L41:
            if (r0 >= r10) goto L4a
            long[] r1 = new long[r2]
            r11[r0] = r1
            int r0 = r0 + 1
            goto L41
        L4a:
            long r0 = (long) r9
            r2 = 134217728(0x8000000, double:6.63123685E-316)
            long r0 = r0 * r2
            long r0 = r18 - r0
            r4 = 0
            int r0 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r0 <= 0) goto L69
            long r0 = (long) r9
            long r4 = r0 * r2
            long r0 = (long) r9
            long r6 = r0 * r2
            long r0 = (long) r9
            long r0 = r0 * r2
            long r13 = r18 - r0
            r0 = r15
            r1 = r4
            r3 = r11
            r4 = r6
            r6 = r13
            copy(r0, r1, r3, r4, r6)
        L69:
            return r11
        */
        throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.BigArrays.forceCapacity(long[][], long, long):long[][]");
    }

    public static long[][] ensureCapacity(long[][] array, long length, long preserve) {
        return length > length(array) ? forceCapacity(array, length, preserve) : array;
    }

    public static long[][] grow(long[][] array, long length) {
        long oldLength = length(array);
        return length > oldLength ? grow(array, length, oldLength) : array;
    }

    public static long[][] grow(long[][] array, long length, long preserve) {
        long oldLength = length(array);
        return length > oldLength ? ensureCapacity(array, Math.max((oldLength >> 1) + oldLength, length), preserve) : array;
    }

    public static long[][] trim(long[][] array, long length) {
        ensureLength(length);
        long oldLength = length(array);
        if (length >= oldLength) {
            return array;
        }
        int baseLength = (int) ((length + 134217727) >>> 27);
        long[][] base = (long[][]) java.util.Arrays.copyOf(array, baseLength);
        int residual = (int) (134217727 & length);
        if (residual != 0) {
            base[baseLength - 1] = LongArrays.trim(base[baseLength - 1], residual);
        }
        return base;
    }

    public static long[][] setLength(long[][] array, long length) {
        long oldLength = length(array);
        return length == oldLength ? array : length < oldLength ? trim(array, length) : ensureCapacity(array, length);
    }

    public static long[][] copy(long[][] array, long offset, long length) {
        ensureOffsetLength(array, offset, length);
        long[][] a = LongBigArrays.newBigArray(length);
        copy(array, offset, a, 0L, length);
        return a;
    }

    public static long[][] copy(long[][] array) {
        long[][] base = (long[][]) array.clone();
        int i = base.length;
        while (true) {
            int i2 = i - 1;
            if (i == 0) {
                return base;
            }
            base[i2] = (long[]) array[i2].clone();
            i = i2;
        }
    }

    public static void fill(long[][] array, long value) {
        int i = array.length;
        while (true) {
            int i2 = i - 1;
            if (i == 0) {
                return;
            }
            java.util.Arrays.fill(array[i2], value);
            i = i2;
        }
    }

    public static void fill(long[][] array, long from, long to, long value) {
        long length = length(array);
        ensureFromTo(length, from, to);
        if (length == 0) {
            return;
        }
        int fromSegment = segment(from);
        int toSegment = segment(to);
        int fromDispl = displacement(from);
        int toDispl = displacement(to);
        if (fromSegment == toSegment) {
            java.util.Arrays.fill(array[fromSegment], fromDispl, toDispl, value);
            return;
        }
        if (toDispl != 0) {
            java.util.Arrays.fill(array[toSegment], 0, toDispl, value);
        }
        while (true) {
            toSegment--;
            if (toSegment <= fromSegment) {
                java.util.Arrays.fill(array[fromSegment], fromDispl, SEGMENT_SIZE, value);
                return;
            }
            java.util.Arrays.fill(array[toSegment], value);
        }
    }

    public static boolean equals(long[][] a1, long[][] a2) {
        if (length(a1) != length(a2)) {
            return false;
        }
        int i = a1.length;
        while (true) {
            int i2 = i - 1;
            if (i != 0) {
                long[] t = a1[i2];
                long[] u = a2[i2];
                int j = t.length;
                while (true) {
                    int j2 = j - 1;
                    if (j != 0) {
                        if (t[j2] != u[j2]) {
                            return false;
                        }
                        j = j2;
                    }
                }
            } else {
                return true;
            }
            i = i2;
        }
    }

    public static String toString(long[][] a) {
        if (a == null) {
            return "null";
        }
        long last = length(a) - 1;
        if (last == -1) {
            return HttpUrl.PATH_SEGMENT_ENCODE_SET_URI;
        }
        StringBuilder b = new StringBuilder();
        b.append('[');
        long i = 0;
        while (true) {
            b.append(String.valueOf(get(a, i)));
            if (i == last) {
                return b.append(']').toString();
            }
            b.append(", ");
            i++;
        }
    }

    public static void ensureFromTo(long[][] a, long from, long to) {
        ensureFromTo(length(a), from, to);
    }

    public static void ensureOffsetLength(long[][] a, long offset, long length) {
        ensureOffsetLength(length(a), offset, length);
    }

    public static void ensureSameLength(long[][] a, long[][] b) {
        if (length(a) != length(b)) {
            throw new IllegalArgumentException("Array size mismatch: " + length(a) + " != " + length(b));
        }
    }

    public static long[][] shuffle(long[][] a, long from, long to, Random random) {
        long p = to - from;
        while (true) {
            long i = p - 1;
            if (p != 0) {
                long p2 = (random.nextLong() & Long.MAX_VALUE) % (1 + i);
                long t = get(a, from + i);
                set(a, from + i, get(a, from + p2));
                set(a, from + p2, t);
                p = i;
            } else {
                return a;
            }
        }
    }

    public static long[][] shuffle(long[][] a, Random random) {
        long p = length(a);
        while (true) {
            long i = p - 1;
            if (p != 0) {
                long p2 = (random.nextLong() & Long.MAX_VALUE) % (1 + i);
                long t = get(a, i);
                set(a, i, get(a, p2));
                set(a, p2, t);
                p = i;
            } else {
                return a;
            }
        }
    }

    public static double get(double[][] array, long index) {
        return array[segment(index)][displacement(index)];
    }

    public static void set(double[][] array, long index, double value) {
        array[segment(index)][displacement(index)] = value;
    }

    public static void swap(double[][] array, long first, long second) {
        double t = array[segment(first)][displacement(first)];
        array[segment(first)][displacement(first)] = array[segment(second)][displacement(second)];
        array[segment(second)][displacement(second)] = t;
    }

    public static double[][] reverse(double[][] a) {
        long length = length(a);
        long i = length / 2;
        while (true) {
            long i2 = i - 1;
            if (i == 0) {
                return a;
            }
            swap(a, i2, (length - i2) - 1);
            i = i2;
        }
    }

    public static void add(double[][] array, long index, double incr) {
        double[] dArr = array[segment(index)];
        int displacement = displacement(index);
        dArr[displacement] = dArr[displacement] + incr;
    }

    public static void mul(double[][] array, long index, double factor) {
        double[] dArr = array[segment(index)];
        int displacement = displacement(index);
        dArr[displacement] = dArr[displacement] * factor;
    }

    public static void incr(double[][] array, long index) {
        double[] dArr = array[segment(index)];
        int displacement = displacement(index);
        dArr[displacement] = dArr[displacement] + 1.0d;
    }

    public static void decr(double[][] array, long index) {
        double[] dArr = array[segment(index)];
        int displacement = displacement(index);
        dArr[displacement] = dArr[displacement] - 1.0d;
    }

    public static void assertBigArray(double[][] array) {
        int l = array.length;
        if (l == 0) {
            return;
        }
        for (int i = 0; i < l - 1; i++) {
            if (array[i].length != 134217728) {
                throw new IllegalStateException("All segments except for the last one must be of length 2^" + Integer.toString(27));
            }
        }
        int i2 = l - 1;
        if (array[i2].length > 134217728) {
            throw new IllegalStateException("The last segment must be of length at most 2^" + Integer.toString(27));
        }
        if (array[l - 1].length == 0 && l == 1) {
            throw new IllegalStateException("The last segment must be of nonzero length");
        }
    }

    public static long length(double[][] array) {
        int length = array.length;
        if (length == 0) {
            return 0L;
        }
        return start(length - 1) + array[length - 1].length;
    }

    public static void copy(double[][] srcArray, long srcPos, double[][] destArray, long destPos, long length) {
        if (destPos <= srcPos) {
            int srcSegment = segment(srcPos);
            int destSegment = segment(destPos);
            int srcDispl = displacement(srcPos);
            int srcDispl2 = srcDispl;
            int destDispl = displacement(destPos);
            int destSegment2 = destSegment;
            long length2 = length;
            while (length2 > 0) {
                int l = (int) Math.min(length2, Math.min(srcArray[srcSegment].length - srcDispl2, destArray[destSegment2].length - destDispl));
                if (l == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl2, destArray[destSegment2], destDispl, l);
                int i = srcDispl2 + l;
                srcDispl2 = i;
                if (i == 134217728) {
                    srcDispl2 = 0;
                    srcSegment++;
                }
                int i2 = destDispl + l;
                destDispl = i2;
                if (i2 == 134217728) {
                    destDispl = 0;
                    destSegment2++;
                }
                length2 -= l;
            }
            return;
        }
        int srcSegment2 = segment(srcPos + length);
        int destSegment3 = segment(destPos + length);
        int srcDispl3 = displacement(srcPos + length);
        int srcDispl4 = srcDispl3;
        int destDispl2 = displacement(destPos + length);
        int destSegment4 = destSegment3;
        long length3 = length;
        while (length3 > 0) {
            if (srcDispl4 == 0) {
                srcDispl4 = SEGMENT_SIZE;
                srcSegment2--;
            }
            if (destDispl2 == 0) {
                destDispl2 = SEGMENT_SIZE;
                destSegment4--;
            }
            int l2 = (int) Math.min(length3, Math.min(srcDispl4, destDispl2));
            if (l2 == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray[srcSegment2], srcDispl4 - l2, destArray[destSegment4], destDispl2 - l2, l2);
            srcDispl4 -= l2;
            destDispl2 -= l2;
            length3 -= l2;
        }
    }

    public static void copyFromBig(double[][] srcArray, long srcPos, double[] destArray, int destPos, int length) {
        int srcSegment = segment(srcPos);
        int srcDispl = displacement(srcPos);
        while (length > 0) {
            int l = Math.min(srcArray[srcSegment].length - srcDispl, length);
            if (l == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray[srcSegment], srcDispl, destArray, destPos, l);
            int i = srcDispl + l;
            srcDispl = i;
            if (i == 134217728) {
                srcDispl = 0;
                srcSegment++;
            }
            destPos += l;
            length -= l;
        }
    }

    public static void copyToBig(double[] srcArray, int srcPos, double[][] destArray, long destPos, long length) {
        int destSegment = segment(destPos);
        int destDispl = displacement(destPos);
        while (length > 0) {
            int l = (int) Math.min(destArray[destSegment].length - destDispl, length);
            if (l == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray, srcPos, destArray[destSegment], destDispl, l);
            int i = destDispl + l;
            destDispl = i;
            if (i == 134217728) {
                destDispl = 0;
                destSegment++;
            }
            srcPos += l;
            length -= l;
        }
    }

    public static double[][] wrap(double[] array) {
        if (array.length == 0) {
            return DoubleBigArrays.EMPTY_BIG_ARRAY;
        }
        if (array.length <= 134217728) {
            return new double[][]{array};
        }
        double[][] bigArray = DoubleBigArrays.newBigArray(array.length);
        for (int i = 0; i < bigArray.length; i++) {
            System.arraycopy(array, (int) start(i), bigArray[i], 0, bigArray[i].length);
        }
        return bigArray;
    }

    public static double[][] ensureCapacity(double[][] array, long length) {
        return ensureCapacity(array, length, length(array));
    }

    /* JADX WARN: Code restructure failed: missing block: B:6:0x0013, code lost:            if (r15[r15.length - 1].length == 134217728) goto L8;     */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0057  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0040  */
    /* JADX WARN: Removed duplicated region for block: B:9:0x002d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static double[][] forceCapacity(double[][] r15, long r16, long r18) {
        /*
            r8 = r15
            ensureLength(r16)
            int r0 = r8.length
            int r1 = r8.length
            r2 = 134217728(0x8000000, float:3.85186E-34)
            if (r1 == 0) goto L15
            int r1 = r8.length
            r3 = 1
            if (r1 <= 0) goto L16
            int r1 = r8.length
            int r1 = r1 - r3
            r1 = r8[r1]
            int r1 = r1.length
            if (r1 != r2) goto L16
        L15:
            r3 = 0
        L16:
            int r9 = r0 - r3
            r0 = 134217727(0x7ffffff, double:6.6312368E-316)
            long r3 = r16 + r0
            r5 = 27
            long r3 = r3 >>> r5
            int r10 = (int) r3
            java.lang.Object[] r3 = java.util.Arrays.copyOf(r15, r10)
            r11 = r3
            double[][] r11 = (double[][]) r11
            long r0 = r16 & r0
            int r12 = (int) r0
            if (r12 == 0) goto L40
            r0 = r9
        L2e:
            int r1 = r10 + (-1)
            if (r0 >= r1) goto L39
            double[] r1 = new double[r2]
            r11[r0] = r1
            int r0 = r0 + 1
            goto L2e
        L39:
            int r0 = r10 + (-1)
            double[] r1 = new double[r12]
            r11[r0] = r1
            goto L4a
        L40:
            r0 = r9
        L41:
            if (r0 >= r10) goto L4a
            double[] r1 = new double[r2]
            r11[r0] = r1
            int r0 = r0 + 1
            goto L41
        L4a:
            long r0 = (long) r9
            r2 = 134217728(0x8000000, double:6.63123685E-316)
            long r0 = r0 * r2
            long r0 = r18 - r0
            r4 = 0
            int r0 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r0 <= 0) goto L69
            long r0 = (long) r9
            long r4 = r0 * r2
            long r0 = (long) r9
            long r6 = r0 * r2
            long r0 = (long) r9
            long r0 = r0 * r2
            long r13 = r18 - r0
            r0 = r15
            r1 = r4
            r3 = r11
            r4 = r6
            r6 = r13
            copy(r0, r1, r3, r4, r6)
        L69:
            return r11
        */
        throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.BigArrays.forceCapacity(double[][], long, long):double[][]");
    }

    public static double[][] ensureCapacity(double[][] array, long length, long preserve) {
        return length > length(array) ? forceCapacity(array, length, preserve) : array;
    }

    public static double[][] grow(double[][] array, long length) {
        long oldLength = length(array);
        return length > oldLength ? grow(array, length, oldLength) : array;
    }

    public static double[][] grow(double[][] array, long length, long preserve) {
        long oldLength = length(array);
        return length > oldLength ? ensureCapacity(array, Math.max((oldLength >> 1) + oldLength, length), preserve) : array;
    }

    public static double[][] trim(double[][] array, long length) {
        ensureLength(length);
        long oldLength = length(array);
        if (length >= oldLength) {
            return array;
        }
        int baseLength = (int) ((length + 134217727) >>> 27);
        double[][] base = (double[][]) java.util.Arrays.copyOf(array, baseLength);
        int residual = (int) (134217727 & length);
        if (residual != 0) {
            base[baseLength - 1] = DoubleArrays.trim(base[baseLength - 1], residual);
        }
        return base;
    }

    public static double[][] setLength(double[][] array, long length) {
        long oldLength = length(array);
        return length == oldLength ? array : length < oldLength ? trim(array, length) : ensureCapacity(array, length);
    }

    public static double[][] copy(double[][] array, long offset, long length) {
        ensureOffsetLength(array, offset, length);
        double[][] a = DoubleBigArrays.newBigArray(length);
        copy(array, offset, a, 0L, length);
        return a;
    }

    public static double[][] copy(double[][] array) {
        double[][] base = (double[][]) array.clone();
        int i = base.length;
        while (true) {
            int i2 = i - 1;
            if (i == 0) {
                return base;
            }
            base[i2] = (double[]) array[i2].clone();
            i = i2;
        }
    }

    public static void fill(double[][] array, double value) {
        int i = array.length;
        while (true) {
            int i2 = i - 1;
            if (i == 0) {
                return;
            }
            java.util.Arrays.fill(array[i2], value);
            i = i2;
        }
    }

    public static void fill(double[][] array, long from, long to, double value) {
        long length = length(array);
        ensureFromTo(length, from, to);
        if (length == 0) {
            return;
        }
        int fromSegment = segment(from);
        int toSegment = segment(to);
        int fromDispl = displacement(from);
        int toDispl = displacement(to);
        if (fromSegment == toSegment) {
            java.util.Arrays.fill(array[fromSegment], fromDispl, toDispl, value);
            return;
        }
        if (toDispl != 0) {
            java.util.Arrays.fill(array[toSegment], 0, toDispl, value);
        }
        while (true) {
            toSegment--;
            if (toSegment <= fromSegment) {
                java.util.Arrays.fill(array[fromSegment], fromDispl, SEGMENT_SIZE, value);
                return;
            }
            java.util.Arrays.fill(array[toSegment], value);
        }
    }

    public static boolean equals(double[][] a1, double[][] a2) {
        if (length(a1) != length(a2)) {
            return false;
        }
        int i = a1.length;
        while (true) {
            int i2 = i - 1;
            if (i != 0) {
                double[] t = a1[i2];
                double[] u = a2[i2];
                int j = t.length;
                while (true) {
                    int j2 = j - 1;
                    if (j != 0) {
                        if (Double.doubleToLongBits(t[j2]) != Double.doubleToLongBits(u[j2])) {
                            return false;
                        }
                        j = j2;
                    }
                }
            } else {
                return true;
            }
            i = i2;
        }
    }

    public static String toString(double[][] a) {
        if (a == null) {
            return "null";
        }
        long last = length(a) - 1;
        if (last == -1) {
            return HttpUrl.PATH_SEGMENT_ENCODE_SET_URI;
        }
        StringBuilder b = new StringBuilder();
        b.append('[');
        long i = 0;
        while (true) {
            b.append(String.valueOf(get(a, i)));
            if (i == last) {
                return b.append(']').toString();
            }
            b.append(", ");
            i++;
        }
    }

    public static void ensureFromTo(double[][] a, long from, long to) {
        ensureFromTo(length(a), from, to);
    }

    public static void ensureOffsetLength(double[][] a, long offset, long length) {
        ensureOffsetLength(length(a), offset, length);
    }

    public static void ensureSameLength(double[][] a, double[][] b) {
        if (length(a) != length(b)) {
            throw new IllegalArgumentException("Array size mismatch: " + length(a) + " != " + length(b));
        }
    }

    public static double[][] shuffle(double[][] a, long from, long to, Random random) {
        long p = to - from;
        while (true) {
            long i = p - 1;
            if (p != 0) {
                long p2 = (random.nextLong() & Long.MAX_VALUE) % (1 + i);
                double t = get(a, from + i);
                set(a, from + i, get(a, from + p2));
                set(a, from + p2, t);
                p = i;
            } else {
                return a;
            }
        }
    }

    public static double[][] shuffle(double[][] a, Random random) {
        long p = length(a);
        while (true) {
            long i = p - 1;
            if (p != 0) {
                long p2 = (random.nextLong() & Long.MAX_VALUE) % (1 + i);
                double t = get(a, i);
                set(a, i, get(a, p2));
                set(a, p2, t);
                p = i;
            } else {
                return a;
            }
        }
    }

    public static boolean get(boolean[][] array, long index) {
        return array[segment(index)][displacement(index)];
    }

    public static void set(boolean[][] array, long index, boolean value) {
        array[segment(index)][displacement(index)] = value;
    }

    public static void swap(boolean[][] array, long first, long second) {
        boolean t = array[segment(first)][displacement(first)];
        array[segment(first)][displacement(first)] = array[segment(second)][displacement(second)];
        array[segment(second)][displacement(second)] = t;
    }

    public static boolean[][] reverse(boolean[][] a) {
        long length = length(a);
        long i = length / 2;
        while (true) {
            long i2 = i - 1;
            if (i == 0) {
                return a;
            }
            swap(a, i2, (length - i2) - 1);
            i = i2;
        }
    }

    public static void assertBigArray(boolean[][] array) {
        int l = array.length;
        if (l == 0) {
            return;
        }
        for (int i = 0; i < l - 1; i++) {
            if (array[i].length != 134217728) {
                throw new IllegalStateException("All segments except for the last one must be of length 2^" + Integer.toString(27));
            }
        }
        int i2 = l - 1;
        if (array[i2].length > 134217728) {
            throw new IllegalStateException("The last segment must be of length at most 2^" + Integer.toString(27));
        }
        if (array[l - 1].length == 0 && l == 1) {
            throw new IllegalStateException("The last segment must be of nonzero length");
        }
    }

    public static long length(boolean[][] array) {
        int length = array.length;
        if (length == 0) {
            return 0L;
        }
        return start(length - 1) + array[length - 1].length;
    }

    public static void copy(boolean[][] srcArray, long srcPos, boolean[][] destArray, long destPos, long length) {
        if (destPos <= srcPos) {
            int srcSegment = segment(srcPos);
            int destSegment = segment(destPos);
            int srcDispl = displacement(srcPos);
            int srcDispl2 = srcDispl;
            int destDispl = displacement(destPos);
            int destSegment2 = destSegment;
            long length2 = length;
            while (length2 > 0) {
                int l = (int) Math.min(length2, Math.min(srcArray[srcSegment].length - srcDispl2, destArray[destSegment2].length - destDispl));
                if (l == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl2, destArray[destSegment2], destDispl, l);
                int i = srcDispl2 + l;
                srcDispl2 = i;
                if (i == 134217728) {
                    srcDispl2 = 0;
                    srcSegment++;
                }
                int i2 = destDispl + l;
                destDispl = i2;
                if (i2 == 134217728) {
                    destDispl = 0;
                    destSegment2++;
                }
                length2 -= l;
            }
            return;
        }
        int srcSegment2 = segment(srcPos + length);
        int destSegment3 = segment(destPos + length);
        int srcDispl3 = displacement(srcPos + length);
        int srcDispl4 = srcDispl3;
        int destDispl2 = displacement(destPos + length);
        int destSegment4 = destSegment3;
        long length3 = length;
        while (length3 > 0) {
            if (srcDispl4 == 0) {
                srcDispl4 = SEGMENT_SIZE;
                srcSegment2--;
            }
            if (destDispl2 == 0) {
                destDispl2 = SEGMENT_SIZE;
                destSegment4--;
            }
            int l2 = (int) Math.min(length3, Math.min(srcDispl4, destDispl2));
            if (l2 == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray[srcSegment2], srcDispl4 - l2, destArray[destSegment4], destDispl2 - l2, l2);
            srcDispl4 -= l2;
            destDispl2 -= l2;
            length3 -= l2;
        }
    }

    public static void copyFromBig(boolean[][] srcArray, long srcPos, boolean[] destArray, int destPos, int length) {
        int srcSegment = segment(srcPos);
        int srcDispl = displacement(srcPos);
        while (length > 0) {
            int l = Math.min(srcArray[srcSegment].length - srcDispl, length);
            if (l == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray[srcSegment], srcDispl, destArray, destPos, l);
            int i = srcDispl + l;
            srcDispl = i;
            if (i == 134217728) {
                srcDispl = 0;
                srcSegment++;
            }
            destPos += l;
            length -= l;
        }
    }

    public static void copyToBig(boolean[] srcArray, int srcPos, boolean[][] destArray, long destPos, long length) {
        int destSegment = segment(destPos);
        int destDispl = displacement(destPos);
        while (length > 0) {
            int l = (int) Math.min(destArray[destSegment].length - destDispl, length);
            if (l == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray, srcPos, destArray[destSegment], destDispl, l);
            int i = destDispl + l;
            destDispl = i;
            if (i == 134217728) {
                destDispl = 0;
                destSegment++;
            }
            srcPos += l;
            length -= l;
        }
    }

    public static boolean[][] wrap(boolean[] array) {
        if (array.length == 0) {
            return BooleanBigArrays.EMPTY_BIG_ARRAY;
        }
        if (array.length <= 134217728) {
            return new boolean[][]{array};
        }
        boolean[][] bigArray = BooleanBigArrays.newBigArray(array.length);
        for (int i = 0; i < bigArray.length; i++) {
            System.arraycopy(array, (int) start(i), bigArray[i], 0, bigArray[i].length);
        }
        return bigArray;
    }

    public static boolean[][] ensureCapacity(boolean[][] array, long length) {
        return ensureCapacity(array, length, length(array));
    }

    /* JADX WARN: Code restructure failed: missing block: B:6:0x0013, code lost:            if (r15[r15.length - 1].length == 134217728) goto L8;     */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0057  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0040  */
    /* JADX WARN: Removed duplicated region for block: B:9:0x002d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean[][] forceCapacity(boolean[][] r15, long r16, long r18) {
        /*
            r8 = r15
            ensureLength(r16)
            int r0 = r8.length
            int r1 = r8.length
            r2 = 134217728(0x8000000, float:3.85186E-34)
            if (r1 == 0) goto L15
            int r1 = r8.length
            r3 = 1
            if (r1 <= 0) goto L16
            int r1 = r8.length
            int r1 = r1 - r3
            r1 = r8[r1]
            int r1 = r1.length
            if (r1 != r2) goto L16
        L15:
            r3 = 0
        L16:
            int r9 = r0 - r3
            r0 = 134217727(0x7ffffff, double:6.6312368E-316)
            long r3 = r16 + r0
            r5 = 27
            long r3 = r3 >>> r5
            int r10 = (int) r3
            java.lang.Object[] r3 = java.util.Arrays.copyOf(r15, r10)
            r11 = r3
            boolean[][] r11 = (boolean[][]) r11
            long r0 = r16 & r0
            int r12 = (int) r0
            if (r12 == 0) goto L40
            r0 = r9
        L2e:
            int r1 = r10 + (-1)
            if (r0 >= r1) goto L39
            boolean[] r1 = new boolean[r2]
            r11[r0] = r1
            int r0 = r0 + 1
            goto L2e
        L39:
            int r0 = r10 + (-1)
            boolean[] r1 = new boolean[r12]
            r11[r0] = r1
            goto L4a
        L40:
            r0 = r9
        L41:
            if (r0 >= r10) goto L4a
            boolean[] r1 = new boolean[r2]
            r11[r0] = r1
            int r0 = r0 + 1
            goto L41
        L4a:
            long r0 = (long) r9
            r2 = 134217728(0x8000000, double:6.63123685E-316)
            long r0 = r0 * r2
            long r0 = r18 - r0
            r4 = 0
            int r0 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r0 <= 0) goto L69
            long r0 = (long) r9
            long r4 = r0 * r2
            long r0 = (long) r9
            long r6 = r0 * r2
            long r0 = (long) r9
            long r0 = r0 * r2
            long r13 = r18 - r0
            r0 = r15
            r1 = r4
            r3 = r11
            r4 = r6
            r6 = r13
            copy(r0, r1, r3, r4, r6)
        L69:
            return r11
        */
        throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.BigArrays.forceCapacity(boolean[][], long, long):boolean[][]");
    }

    public static boolean[][] ensureCapacity(boolean[][] array, long length, long preserve) {
        return length > length(array) ? forceCapacity(array, length, preserve) : array;
    }

    public static boolean[][] grow(boolean[][] array, long length) {
        long oldLength = length(array);
        return length > oldLength ? grow(array, length, oldLength) : array;
    }

    public static boolean[][] grow(boolean[][] array, long length, long preserve) {
        long oldLength = length(array);
        return length > oldLength ? ensureCapacity(array, Math.max((oldLength >> 1) + oldLength, length), preserve) : array;
    }

    public static boolean[][] trim(boolean[][] array, long length) {
        ensureLength(length);
        long oldLength = length(array);
        if (length >= oldLength) {
            return array;
        }
        int baseLength = (int) ((length + 134217727) >>> 27);
        boolean[][] base = (boolean[][]) java.util.Arrays.copyOf(array, baseLength);
        int residual = (int) (134217727 & length);
        if (residual != 0) {
            base[baseLength - 1] = BooleanArrays.trim(base[baseLength - 1], residual);
        }
        return base;
    }

    public static boolean[][] setLength(boolean[][] array, long length) {
        long oldLength = length(array);
        return length == oldLength ? array : length < oldLength ? trim(array, length) : ensureCapacity(array, length);
    }

    public static boolean[][] copy(boolean[][] array, long offset, long length) {
        ensureOffsetLength(array, offset, length);
        boolean[][] a = BooleanBigArrays.newBigArray(length);
        copy(array, offset, a, 0L, length);
        return a;
    }

    public static boolean[][] copy(boolean[][] array) {
        boolean[][] base = (boolean[][]) array.clone();
        int i = base.length;
        while (true) {
            int i2 = i - 1;
            if (i == 0) {
                return base;
            }
            base[i2] = (boolean[]) array[i2].clone();
            i = i2;
        }
    }

    public static void fill(boolean[][] array, boolean value) {
        int i = array.length;
        while (true) {
            int i2 = i - 1;
            if (i == 0) {
                return;
            }
            java.util.Arrays.fill(array[i2], value);
            i = i2;
        }
    }

    public static void fill(boolean[][] array, long from, long to, boolean value) {
        long length = length(array);
        ensureFromTo(length, from, to);
        if (length == 0) {
            return;
        }
        int fromSegment = segment(from);
        int toSegment = segment(to);
        int fromDispl = displacement(from);
        int toDispl = displacement(to);
        if (fromSegment == toSegment) {
            java.util.Arrays.fill(array[fromSegment], fromDispl, toDispl, value);
            return;
        }
        if (toDispl != 0) {
            java.util.Arrays.fill(array[toSegment], 0, toDispl, value);
        }
        while (true) {
            toSegment--;
            if (toSegment <= fromSegment) {
                java.util.Arrays.fill(array[fromSegment], fromDispl, SEGMENT_SIZE, value);
                return;
            }
            java.util.Arrays.fill(array[toSegment], value);
        }
    }

    public static boolean equals(boolean[][] a1, boolean[][] a2) {
        if (length(a1) != length(a2)) {
            return false;
        }
        int i = a1.length;
        while (true) {
            int i2 = i - 1;
            if (i != 0) {
                boolean[] t = a1[i2];
                boolean[] u = a2[i2];
                int j = t.length;
                while (true) {
                    int j2 = j - 1;
                    if (j != 0) {
                        if (t[j2] != u[j2]) {
                            return false;
                        }
                        j = j2;
                    }
                }
            } else {
                return true;
            }
            i = i2;
        }
    }

    public static String toString(boolean[][] a) {
        if (a == null) {
            return "null";
        }
        long last = length(a) - 1;
        if (last == -1) {
            return HttpUrl.PATH_SEGMENT_ENCODE_SET_URI;
        }
        StringBuilder b = new StringBuilder();
        b.append('[');
        long i = 0;
        while (true) {
            b.append(String.valueOf(get(a, i)));
            if (i == last) {
                return b.append(']').toString();
            }
            b.append(", ");
            i++;
        }
    }

    public static void ensureFromTo(boolean[][] a, long from, long to) {
        ensureFromTo(length(a), from, to);
    }

    public static void ensureOffsetLength(boolean[][] a, long offset, long length) {
        ensureOffsetLength(length(a), offset, length);
    }

    public static void ensureSameLength(boolean[][] a, boolean[][] b) {
        if (length(a) != length(b)) {
            throw new IllegalArgumentException("Array size mismatch: " + length(a) + " != " + length(b));
        }
    }

    public static boolean[][] shuffle(boolean[][] a, long from, long to, Random random) {
        long p = to - from;
        while (true) {
            long i = p - 1;
            if (p != 0) {
                long p2 = (random.nextLong() & Long.MAX_VALUE) % (1 + i);
                boolean t = get(a, from + i);
                set(a, from + i, get(a, from + p2));
                set(a, from + p2, t);
                p = i;
            } else {
                return a;
            }
        }
    }

    public static boolean[][] shuffle(boolean[][] a, Random random) {
        long p = length(a);
        while (true) {
            long i = p - 1;
            if (p != 0) {
                long p2 = (random.nextLong() & Long.MAX_VALUE) % (1 + i);
                boolean t = get(a, i);
                set(a, i, get(a, p2));
                set(a, p2, t);
                p = i;
            } else {
                return a;
            }
        }
    }

    public static short get(short[][] array, long index) {
        return array[segment(index)][displacement(index)];
    }

    public static void set(short[][] array, long index, short value) {
        array[segment(index)][displacement(index)] = value;
    }

    public static void swap(short[][] array, long first, long second) {
        short t = array[segment(first)][displacement(first)];
        array[segment(first)][displacement(first)] = array[segment(second)][displacement(second)];
        array[segment(second)][displacement(second)] = t;
    }

    public static short[][] reverse(short[][] a) {
        long length = length(a);
        long i = length / 2;
        while (true) {
            long i2 = i - 1;
            if (i == 0) {
                return a;
            }
            swap(a, i2, (length - i2) - 1);
            i = i2;
        }
    }

    public static void add(short[][] array, long index, short incr) {
        short[] sArr = array[segment(index)];
        int displacement = displacement(index);
        sArr[displacement] = (short) (sArr[displacement] + incr);
    }

    public static void mul(short[][] array, long index, short factor) {
        short[] sArr = array[segment(index)];
        int displacement = displacement(index);
        sArr[displacement] = (short) (sArr[displacement] * factor);
    }

    public static void incr(short[][] array, long index) {
        short[] sArr = array[segment(index)];
        int displacement = displacement(index);
        sArr[displacement] = (short) (sArr[displacement] + 1);
    }

    public static void decr(short[][] array, long index) {
        array[segment(index)][displacement(index)] = (short) (r0[r1] - 1);
    }

    public static void assertBigArray(short[][] array) {
        int l = array.length;
        if (l == 0) {
            return;
        }
        for (int i = 0; i < l - 1; i++) {
            if (array[i].length != 134217728) {
                throw new IllegalStateException("All segments except for the last one must be of length 2^" + Integer.toString(27));
            }
        }
        int i2 = l - 1;
        if (array[i2].length > 134217728) {
            throw new IllegalStateException("The last segment must be of length at most 2^" + Integer.toString(27));
        }
        if (array[l - 1].length == 0 && l == 1) {
            throw new IllegalStateException("The last segment must be of nonzero length");
        }
    }

    public static long length(short[][] array) {
        int length = array.length;
        if (length == 0) {
            return 0L;
        }
        return start(length - 1) + array[length - 1].length;
    }

    public static void copy(short[][] srcArray, long srcPos, short[][] destArray, long destPos, long length) {
        if (destPos <= srcPos) {
            int srcSegment = segment(srcPos);
            int destSegment = segment(destPos);
            int srcDispl = displacement(srcPos);
            int srcDispl2 = srcDispl;
            int destDispl = displacement(destPos);
            int destSegment2 = destSegment;
            long length2 = length;
            while (length2 > 0) {
                int l = (int) Math.min(length2, Math.min(srcArray[srcSegment].length - srcDispl2, destArray[destSegment2].length - destDispl));
                if (l == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl2, destArray[destSegment2], destDispl, l);
                int i = srcDispl2 + l;
                srcDispl2 = i;
                if (i == 134217728) {
                    srcDispl2 = 0;
                    srcSegment++;
                }
                int i2 = destDispl + l;
                destDispl = i2;
                if (i2 == 134217728) {
                    destDispl = 0;
                    destSegment2++;
                }
                length2 -= l;
            }
            return;
        }
        int srcSegment2 = segment(srcPos + length);
        int destSegment3 = segment(destPos + length);
        int srcDispl3 = displacement(srcPos + length);
        int srcDispl4 = srcDispl3;
        int destDispl2 = displacement(destPos + length);
        int destSegment4 = destSegment3;
        long length3 = length;
        while (length3 > 0) {
            if (srcDispl4 == 0) {
                srcDispl4 = SEGMENT_SIZE;
                srcSegment2--;
            }
            if (destDispl2 == 0) {
                destDispl2 = SEGMENT_SIZE;
                destSegment4--;
            }
            int l2 = (int) Math.min(length3, Math.min(srcDispl4, destDispl2));
            if (l2 == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray[srcSegment2], srcDispl4 - l2, destArray[destSegment4], destDispl2 - l2, l2);
            srcDispl4 -= l2;
            destDispl2 -= l2;
            length3 -= l2;
        }
    }

    public static void copyFromBig(short[][] srcArray, long srcPos, short[] destArray, int destPos, int length) {
        int srcSegment = segment(srcPos);
        int srcDispl = displacement(srcPos);
        while (length > 0) {
            int l = Math.min(srcArray[srcSegment].length - srcDispl, length);
            if (l == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray[srcSegment], srcDispl, destArray, destPos, l);
            int i = srcDispl + l;
            srcDispl = i;
            if (i == 134217728) {
                srcDispl = 0;
                srcSegment++;
            }
            destPos += l;
            length -= l;
        }
    }

    public static void copyToBig(short[] srcArray, int srcPos, short[][] destArray, long destPos, long length) {
        int destSegment = segment(destPos);
        int destDispl = displacement(destPos);
        while (length > 0) {
            int l = (int) Math.min(destArray[destSegment].length - destDispl, length);
            if (l == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray, srcPos, destArray[destSegment], destDispl, l);
            int i = destDispl + l;
            destDispl = i;
            if (i == 134217728) {
                destDispl = 0;
                destSegment++;
            }
            srcPos += l;
            length -= l;
        }
    }

    public static short[][] wrap(short[] array) {
        if (array.length == 0) {
            return ShortBigArrays.EMPTY_BIG_ARRAY;
        }
        if (array.length <= 134217728) {
            return new short[][]{array};
        }
        short[][] bigArray = ShortBigArrays.newBigArray(array.length);
        for (int i = 0; i < bigArray.length; i++) {
            System.arraycopy(array, (int) start(i), bigArray[i], 0, bigArray[i].length);
        }
        return bigArray;
    }

    public static short[][] ensureCapacity(short[][] array, long length) {
        return ensureCapacity(array, length, length(array));
    }

    /* JADX WARN: Code restructure failed: missing block: B:6:0x0013, code lost:            if (r15[r15.length - 1].length == 134217728) goto L8;     */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0057  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0040  */
    /* JADX WARN: Removed duplicated region for block: B:9:0x002d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static short[][] forceCapacity(short[][] r15, long r16, long r18) {
        /*
            r8 = r15
            ensureLength(r16)
            int r0 = r8.length
            int r1 = r8.length
            r2 = 134217728(0x8000000, float:3.85186E-34)
            if (r1 == 0) goto L15
            int r1 = r8.length
            r3 = 1
            if (r1 <= 0) goto L16
            int r1 = r8.length
            int r1 = r1 - r3
            r1 = r8[r1]
            int r1 = r1.length
            if (r1 != r2) goto L16
        L15:
            r3 = 0
        L16:
            int r9 = r0 - r3
            r0 = 134217727(0x7ffffff, double:6.6312368E-316)
            long r3 = r16 + r0
            r5 = 27
            long r3 = r3 >>> r5
            int r10 = (int) r3
            java.lang.Object[] r3 = java.util.Arrays.copyOf(r15, r10)
            r11 = r3
            short[][] r11 = (short[][]) r11
            long r0 = r16 & r0
            int r12 = (int) r0
            if (r12 == 0) goto L40
            r0 = r9
        L2e:
            int r1 = r10 + (-1)
            if (r0 >= r1) goto L39
            short[] r1 = new short[r2]
            r11[r0] = r1
            int r0 = r0 + 1
            goto L2e
        L39:
            int r0 = r10 + (-1)
            short[] r1 = new short[r12]
            r11[r0] = r1
            goto L4a
        L40:
            r0 = r9
        L41:
            if (r0 >= r10) goto L4a
            short[] r1 = new short[r2]
            r11[r0] = r1
            int r0 = r0 + 1
            goto L41
        L4a:
            long r0 = (long) r9
            r2 = 134217728(0x8000000, double:6.63123685E-316)
            long r0 = r0 * r2
            long r0 = r18 - r0
            r4 = 0
            int r0 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r0 <= 0) goto L69
            long r0 = (long) r9
            long r4 = r0 * r2
            long r0 = (long) r9
            long r6 = r0 * r2
            long r0 = (long) r9
            long r0 = r0 * r2
            long r13 = r18 - r0
            r0 = r15
            r1 = r4
            r3 = r11
            r4 = r6
            r6 = r13
            copy(r0, r1, r3, r4, r6)
        L69:
            return r11
        */
        throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.BigArrays.forceCapacity(short[][], long, long):short[][]");
    }

    public static short[][] ensureCapacity(short[][] array, long length, long preserve) {
        return length > length(array) ? forceCapacity(array, length, preserve) : array;
    }

    public static short[][] grow(short[][] array, long length) {
        long oldLength = length(array);
        return length > oldLength ? grow(array, length, oldLength) : array;
    }

    public static short[][] grow(short[][] array, long length, long preserve) {
        long oldLength = length(array);
        return length > oldLength ? ensureCapacity(array, Math.max((oldLength >> 1) + oldLength, length), preserve) : array;
    }

    public static short[][] trim(short[][] array, long length) {
        ensureLength(length);
        long oldLength = length(array);
        if (length >= oldLength) {
            return array;
        }
        int baseLength = (int) ((length + 134217727) >>> 27);
        short[][] base = (short[][]) java.util.Arrays.copyOf(array, baseLength);
        int residual = (int) (134217727 & length);
        if (residual != 0) {
            base[baseLength - 1] = ShortArrays.trim(base[baseLength - 1], residual);
        }
        return base;
    }

    public static short[][] setLength(short[][] array, long length) {
        long oldLength = length(array);
        return length == oldLength ? array : length < oldLength ? trim(array, length) : ensureCapacity(array, length);
    }

    public static short[][] copy(short[][] array, long offset, long length) {
        ensureOffsetLength(array, offset, length);
        short[][] a = ShortBigArrays.newBigArray(length);
        copy(array, offset, a, 0L, length);
        return a;
    }

    public static short[][] copy(short[][] array) {
        short[][] base = (short[][]) array.clone();
        int i = base.length;
        while (true) {
            int i2 = i - 1;
            if (i == 0) {
                return base;
            }
            base[i2] = (short[]) array[i2].clone();
            i = i2;
        }
    }

    public static void fill(short[][] array, short value) {
        int i = array.length;
        while (true) {
            int i2 = i - 1;
            if (i == 0) {
                return;
            }
            java.util.Arrays.fill(array[i2], value);
            i = i2;
        }
    }

    public static void fill(short[][] array, long from, long to, short value) {
        long length = length(array);
        ensureFromTo(length, from, to);
        if (length == 0) {
            return;
        }
        int fromSegment = segment(from);
        int toSegment = segment(to);
        int fromDispl = displacement(from);
        int toDispl = displacement(to);
        if (fromSegment == toSegment) {
            java.util.Arrays.fill(array[fromSegment], fromDispl, toDispl, value);
            return;
        }
        if (toDispl != 0) {
            java.util.Arrays.fill(array[toSegment], 0, toDispl, value);
        }
        while (true) {
            toSegment--;
            if (toSegment <= fromSegment) {
                java.util.Arrays.fill(array[fromSegment], fromDispl, SEGMENT_SIZE, value);
                return;
            }
            java.util.Arrays.fill(array[toSegment], value);
        }
    }

    public static boolean equals(short[][] a1, short[][] a2) {
        if (length(a1) != length(a2)) {
            return false;
        }
        int i = a1.length;
        while (true) {
            int i2 = i - 1;
            if (i != 0) {
                short[] t = a1[i2];
                short[] u = a2[i2];
                int j = t.length;
                while (true) {
                    int j2 = j - 1;
                    if (j != 0) {
                        if (t[j2] != u[j2]) {
                            return false;
                        }
                        j = j2;
                    }
                }
            } else {
                return true;
            }
            i = i2;
        }
    }

    public static String toString(short[][] a) {
        if (a == null) {
            return "null";
        }
        long last = length(a) - 1;
        if (last == -1) {
            return HttpUrl.PATH_SEGMENT_ENCODE_SET_URI;
        }
        StringBuilder b = new StringBuilder();
        b.append('[');
        long i = 0;
        while (true) {
            b.append(String.valueOf((int) get(a, i)));
            if (i == last) {
                return b.append(']').toString();
            }
            b.append(", ");
            i++;
        }
    }

    public static void ensureFromTo(short[][] a, long from, long to) {
        ensureFromTo(length(a), from, to);
    }

    public static void ensureOffsetLength(short[][] a, long offset, long length) {
        ensureOffsetLength(length(a), offset, length);
    }

    public static void ensureSameLength(short[][] a, short[][] b) {
        if (length(a) != length(b)) {
            throw new IllegalArgumentException("Array size mismatch: " + length(a) + " != " + length(b));
        }
    }

    public static short[][] shuffle(short[][] a, long from, long to, Random random) {
        long p = to - from;
        while (true) {
            long i = p - 1;
            if (p != 0) {
                long p2 = (random.nextLong() & Long.MAX_VALUE) % (1 + i);
                short t = get(a, from + i);
                set(a, from + i, get(a, from + p2));
                set(a, from + p2, t);
                p = i;
            } else {
                return a;
            }
        }
    }

    public static short[][] shuffle(short[][] a, Random random) {
        long p = length(a);
        while (true) {
            long i = p - 1;
            if (p != 0) {
                long p2 = (random.nextLong() & Long.MAX_VALUE) % (1 + i);
                short t = get(a, i);
                set(a, i, get(a, p2));
                set(a, p2, t);
                p = i;
            } else {
                return a;
            }
        }
    }

    public static char get(char[][] array, long index) {
        return array[segment(index)][displacement(index)];
    }

    public static void set(char[][] array, long index, char value) {
        array[segment(index)][displacement(index)] = value;
    }

    public static void swap(char[][] array, long first, long second) {
        char t = array[segment(first)][displacement(first)];
        array[segment(first)][displacement(first)] = array[segment(second)][displacement(second)];
        array[segment(second)][displacement(second)] = t;
    }

    public static char[][] reverse(char[][] a) {
        long length = length(a);
        long i = length / 2;
        while (true) {
            long i2 = i - 1;
            if (i == 0) {
                return a;
            }
            swap(a, i2, (length - i2) - 1);
            i = i2;
        }
    }

    public static void add(char[][] array, long index, char incr) {
        char[] cArr = array[segment(index)];
        int displacement = displacement(index);
        cArr[displacement] = (char) (cArr[displacement] + incr);
    }

    public static void mul(char[][] array, long index, char factor) {
        char[] cArr = array[segment(index)];
        int displacement = displacement(index);
        cArr[displacement] = (char) (cArr[displacement] * factor);
    }

    public static void incr(char[][] array, long index) {
        char[] cArr = array[segment(index)];
        int displacement = displacement(index);
        cArr[displacement] = (char) (cArr[displacement] + 1);
    }

    public static void decr(char[][] array, long index) {
        array[segment(index)][displacement(index)] = (char) (r0[r1] - 1);
    }

    public static void assertBigArray(char[][] array) {
        int l = array.length;
        if (l == 0) {
            return;
        }
        for (int i = 0; i < l - 1; i++) {
            if (array[i].length != 134217728) {
                throw new IllegalStateException("All segments except for the last one must be of length 2^" + Integer.toString(27));
            }
        }
        int i2 = l - 1;
        if (array[i2].length > 134217728) {
            throw new IllegalStateException("The last segment must be of length at most 2^" + Integer.toString(27));
        }
        if (array[l - 1].length == 0 && l == 1) {
            throw new IllegalStateException("The last segment must be of nonzero length");
        }
    }

    public static long length(char[][] array) {
        int length = array.length;
        if (length == 0) {
            return 0L;
        }
        return start(length - 1) + array[length - 1].length;
    }

    public static void copy(char[][] srcArray, long srcPos, char[][] destArray, long destPos, long length) {
        if (destPos <= srcPos) {
            int srcSegment = segment(srcPos);
            int destSegment = segment(destPos);
            int srcDispl = displacement(srcPos);
            int srcDispl2 = srcDispl;
            int destDispl = displacement(destPos);
            int destSegment2 = destSegment;
            long length2 = length;
            while (length2 > 0) {
                int l = (int) Math.min(length2, Math.min(srcArray[srcSegment].length - srcDispl2, destArray[destSegment2].length - destDispl));
                if (l == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl2, destArray[destSegment2], destDispl, l);
                int i = srcDispl2 + l;
                srcDispl2 = i;
                if (i == 134217728) {
                    srcDispl2 = 0;
                    srcSegment++;
                }
                int i2 = destDispl + l;
                destDispl = i2;
                if (i2 == 134217728) {
                    destDispl = 0;
                    destSegment2++;
                }
                length2 -= l;
            }
            return;
        }
        int srcSegment2 = segment(srcPos + length);
        int destSegment3 = segment(destPos + length);
        int srcDispl3 = displacement(srcPos + length);
        int srcDispl4 = srcDispl3;
        int destDispl2 = displacement(destPos + length);
        int destSegment4 = destSegment3;
        long length3 = length;
        while (length3 > 0) {
            if (srcDispl4 == 0) {
                srcDispl4 = SEGMENT_SIZE;
                srcSegment2--;
            }
            if (destDispl2 == 0) {
                destDispl2 = SEGMENT_SIZE;
                destSegment4--;
            }
            int l2 = (int) Math.min(length3, Math.min(srcDispl4, destDispl2));
            if (l2 == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray[srcSegment2], srcDispl4 - l2, destArray[destSegment4], destDispl2 - l2, l2);
            srcDispl4 -= l2;
            destDispl2 -= l2;
            length3 -= l2;
        }
    }

    public static void copyFromBig(char[][] srcArray, long srcPos, char[] destArray, int destPos, int length) {
        int srcSegment = segment(srcPos);
        int srcDispl = displacement(srcPos);
        while (length > 0) {
            int l = Math.min(srcArray[srcSegment].length - srcDispl, length);
            if (l == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray[srcSegment], srcDispl, destArray, destPos, l);
            int i = srcDispl + l;
            srcDispl = i;
            if (i == 134217728) {
                srcDispl = 0;
                srcSegment++;
            }
            destPos += l;
            length -= l;
        }
    }

    public static void copyToBig(char[] srcArray, int srcPos, char[][] destArray, long destPos, long length) {
        int destSegment = segment(destPos);
        int destDispl = displacement(destPos);
        while (length > 0) {
            int l = (int) Math.min(destArray[destSegment].length - destDispl, length);
            if (l == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray, srcPos, destArray[destSegment], destDispl, l);
            int i = destDispl + l;
            destDispl = i;
            if (i == 134217728) {
                destDispl = 0;
                destSegment++;
            }
            srcPos += l;
            length -= l;
        }
    }

    public static char[][] wrap(char[] array) {
        if (array.length == 0) {
            return CharBigArrays.EMPTY_BIG_ARRAY;
        }
        if (array.length <= 134217728) {
            return new char[][]{array};
        }
        char[][] bigArray = CharBigArrays.newBigArray(array.length);
        for (int i = 0; i < bigArray.length; i++) {
            System.arraycopy(array, (int) start(i), bigArray[i], 0, bigArray[i].length);
        }
        return bigArray;
    }

    public static char[][] ensureCapacity(char[][] array, long length) {
        return ensureCapacity(array, length, length(array));
    }

    /* JADX WARN: Code restructure failed: missing block: B:6:0x0013, code lost:            if (r15[r15.length - 1].length == 134217728) goto L8;     */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0057  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0040  */
    /* JADX WARN: Removed duplicated region for block: B:9:0x002d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static char[][] forceCapacity(char[][] r15, long r16, long r18) {
        /*
            r8 = r15
            ensureLength(r16)
            int r0 = r8.length
            int r1 = r8.length
            r2 = 134217728(0x8000000, float:3.85186E-34)
            if (r1 == 0) goto L15
            int r1 = r8.length
            r3 = 1
            if (r1 <= 0) goto L16
            int r1 = r8.length
            int r1 = r1 - r3
            r1 = r8[r1]
            int r1 = r1.length
            if (r1 != r2) goto L16
        L15:
            r3 = 0
        L16:
            int r9 = r0 - r3
            r0 = 134217727(0x7ffffff, double:6.6312368E-316)
            long r3 = r16 + r0
            r5 = 27
            long r3 = r3 >>> r5
            int r10 = (int) r3
            java.lang.Object[] r3 = java.util.Arrays.copyOf(r15, r10)
            r11 = r3
            char[][] r11 = (char[][]) r11
            long r0 = r16 & r0
            int r12 = (int) r0
            if (r12 == 0) goto L40
            r0 = r9
        L2e:
            int r1 = r10 + (-1)
            if (r0 >= r1) goto L39
            char[] r1 = new char[r2]
            r11[r0] = r1
            int r0 = r0 + 1
            goto L2e
        L39:
            int r0 = r10 + (-1)
            char[] r1 = new char[r12]
            r11[r0] = r1
            goto L4a
        L40:
            r0 = r9
        L41:
            if (r0 >= r10) goto L4a
            char[] r1 = new char[r2]
            r11[r0] = r1
            int r0 = r0 + 1
            goto L41
        L4a:
            long r0 = (long) r9
            r2 = 134217728(0x8000000, double:6.63123685E-316)
            long r0 = r0 * r2
            long r0 = r18 - r0
            r4 = 0
            int r0 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r0 <= 0) goto L69
            long r0 = (long) r9
            long r4 = r0 * r2
            long r0 = (long) r9
            long r6 = r0 * r2
            long r0 = (long) r9
            long r0 = r0 * r2
            long r13 = r18 - r0
            r0 = r15
            r1 = r4
            r3 = r11
            r4 = r6
            r6 = r13
            copy(r0, r1, r3, r4, r6)
        L69:
            return r11
        */
        throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.BigArrays.forceCapacity(char[][], long, long):char[][]");
    }

    public static char[][] ensureCapacity(char[][] array, long length, long preserve) {
        return length > length(array) ? forceCapacity(array, length, preserve) : array;
    }

    public static char[][] grow(char[][] array, long length) {
        long oldLength = length(array);
        return length > oldLength ? grow(array, length, oldLength) : array;
    }

    public static char[][] grow(char[][] array, long length, long preserve) {
        long oldLength = length(array);
        return length > oldLength ? ensureCapacity(array, Math.max((oldLength >> 1) + oldLength, length), preserve) : array;
    }

    public static char[][] trim(char[][] array, long length) {
        ensureLength(length);
        long oldLength = length(array);
        if (length >= oldLength) {
            return array;
        }
        int baseLength = (int) ((length + 134217727) >>> 27);
        char[][] base = (char[][]) java.util.Arrays.copyOf(array, baseLength);
        int residual = (int) (134217727 & length);
        if (residual != 0) {
            base[baseLength - 1] = CharArrays.trim(base[baseLength - 1], residual);
        }
        return base;
    }

    public static char[][] setLength(char[][] array, long length) {
        long oldLength = length(array);
        return length == oldLength ? array : length < oldLength ? trim(array, length) : ensureCapacity(array, length);
    }

    public static char[][] copy(char[][] array, long offset, long length) {
        ensureOffsetLength(array, offset, length);
        char[][] a = CharBigArrays.newBigArray(length);
        copy(array, offset, a, 0L, length);
        return a;
    }

    public static char[][] copy(char[][] array) {
        char[][] base = (char[][]) array.clone();
        int i = base.length;
        while (true) {
            int i2 = i - 1;
            if (i == 0) {
                return base;
            }
            base[i2] = (char[]) array[i2].clone();
            i = i2;
        }
    }

    public static void fill(char[][] array, char value) {
        int i = array.length;
        while (true) {
            int i2 = i - 1;
            if (i == 0) {
                return;
            }
            java.util.Arrays.fill(array[i2], value);
            i = i2;
        }
    }

    public static void fill(char[][] array, long from, long to, char value) {
        long length = length(array);
        ensureFromTo(length, from, to);
        if (length == 0) {
            return;
        }
        int fromSegment = segment(from);
        int toSegment = segment(to);
        int fromDispl = displacement(from);
        int toDispl = displacement(to);
        if (fromSegment == toSegment) {
            java.util.Arrays.fill(array[fromSegment], fromDispl, toDispl, value);
            return;
        }
        if (toDispl != 0) {
            java.util.Arrays.fill(array[toSegment], 0, toDispl, value);
        }
        while (true) {
            toSegment--;
            if (toSegment <= fromSegment) {
                java.util.Arrays.fill(array[fromSegment], fromDispl, SEGMENT_SIZE, value);
                return;
            }
            java.util.Arrays.fill(array[toSegment], value);
        }
    }

    public static boolean equals(char[][] a1, char[][] a2) {
        if (length(a1) != length(a2)) {
            return false;
        }
        int i = a1.length;
        while (true) {
            int i2 = i - 1;
            if (i != 0) {
                char[] t = a1[i2];
                char[] u = a2[i2];
                int j = t.length;
                while (true) {
                    int j2 = j - 1;
                    if (j != 0) {
                        if (t[j2] != u[j2]) {
                            return false;
                        }
                        j = j2;
                    }
                }
            } else {
                return true;
            }
            i = i2;
        }
    }

    public static String toString(char[][] a) {
        if (a == null) {
            return "null";
        }
        long last = length(a) - 1;
        if (last == -1) {
            return HttpUrl.PATH_SEGMENT_ENCODE_SET_URI;
        }
        StringBuilder b = new StringBuilder();
        b.append('[');
        long i = 0;
        while (true) {
            b.append(String.valueOf(get(a, i)));
            if (i == last) {
                return b.append(']').toString();
            }
            b.append(", ");
            i++;
        }
    }

    public static void ensureFromTo(char[][] a, long from, long to) {
        ensureFromTo(length(a), from, to);
    }

    public static void ensureOffsetLength(char[][] a, long offset, long length) {
        ensureOffsetLength(length(a), offset, length);
    }

    public static void ensureSameLength(char[][] a, char[][] b) {
        if (length(a) != length(b)) {
            throw new IllegalArgumentException("Array size mismatch: " + length(a) + " != " + length(b));
        }
    }

    public static char[][] shuffle(char[][] a, long from, long to, Random random) {
        long p = to - from;
        while (true) {
            long i = p - 1;
            if (p != 0) {
                long p2 = (random.nextLong() & Long.MAX_VALUE) % (1 + i);
                char t = get(a, from + i);
                set(a, from + i, get(a, from + p2));
                set(a, from + p2, t);
                p = i;
            } else {
                return a;
            }
        }
    }

    public static char[][] shuffle(char[][] a, Random random) {
        long p = length(a);
        while (true) {
            long i = p - 1;
            if (p != 0) {
                long p2 = (random.nextLong() & Long.MAX_VALUE) % (1 + i);
                char t = get(a, i);
                set(a, i, get(a, p2));
                set(a, p2, t);
                p = i;
            } else {
                return a;
            }
        }
    }

    public static float get(float[][] array, long index) {
        return array[segment(index)][displacement(index)];
    }

    public static void set(float[][] array, long index, float value) {
        array[segment(index)][displacement(index)] = value;
    }

    public static void swap(float[][] array, long first, long second) {
        float t = array[segment(first)][displacement(first)];
        array[segment(first)][displacement(first)] = array[segment(second)][displacement(second)];
        array[segment(second)][displacement(second)] = t;
    }

    public static float[][] reverse(float[][] a) {
        long length = length(a);
        long i = length / 2;
        while (true) {
            long i2 = i - 1;
            if (i == 0) {
                return a;
            }
            swap(a, i2, (length - i2) - 1);
            i = i2;
        }
    }

    public static void add(float[][] array, long index, float incr) {
        float[] fArr = array[segment(index)];
        int displacement = displacement(index);
        fArr[displacement] = fArr[displacement] + incr;
    }

    public static void mul(float[][] array, long index, float factor) {
        float[] fArr = array[segment(index)];
        int displacement = displacement(index);
        fArr[displacement] = fArr[displacement] * factor;
    }

    public static void incr(float[][] array, long index) {
        float[] fArr = array[segment(index)];
        int displacement = displacement(index);
        fArr[displacement] = fArr[displacement] + 1.0f;
    }

    public static void decr(float[][] array, long index) {
        float[] fArr = array[segment(index)];
        int displacement = displacement(index);
        fArr[displacement] = fArr[displacement] - 1.0f;
    }

    public static void assertBigArray(float[][] array) {
        int l = array.length;
        if (l == 0) {
            return;
        }
        for (int i = 0; i < l - 1; i++) {
            if (array[i].length != 134217728) {
                throw new IllegalStateException("All segments except for the last one must be of length 2^" + Integer.toString(27));
            }
        }
        int i2 = l - 1;
        if (array[i2].length > 134217728) {
            throw new IllegalStateException("The last segment must be of length at most 2^" + Integer.toString(27));
        }
        if (array[l - 1].length == 0 && l == 1) {
            throw new IllegalStateException("The last segment must be of nonzero length");
        }
    }

    public static long length(float[][] array) {
        int length = array.length;
        if (length == 0) {
            return 0L;
        }
        return start(length - 1) + array[length - 1].length;
    }

    public static void copy(float[][] srcArray, long srcPos, float[][] destArray, long destPos, long length) {
        if (destPos <= srcPos) {
            int srcSegment = segment(srcPos);
            int destSegment = segment(destPos);
            int srcDispl = displacement(srcPos);
            int srcDispl2 = srcDispl;
            int destDispl = displacement(destPos);
            int destSegment2 = destSegment;
            long length2 = length;
            while (length2 > 0) {
                int l = (int) Math.min(length2, Math.min(srcArray[srcSegment].length - srcDispl2, destArray[destSegment2].length - destDispl));
                if (l == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl2, destArray[destSegment2], destDispl, l);
                int i = srcDispl2 + l;
                srcDispl2 = i;
                if (i == 134217728) {
                    srcDispl2 = 0;
                    srcSegment++;
                }
                int i2 = destDispl + l;
                destDispl = i2;
                if (i2 == 134217728) {
                    destDispl = 0;
                    destSegment2++;
                }
                length2 -= l;
            }
            return;
        }
        int srcSegment2 = segment(srcPos + length);
        int destSegment3 = segment(destPos + length);
        int srcDispl3 = displacement(srcPos + length);
        int srcDispl4 = srcDispl3;
        int destDispl2 = displacement(destPos + length);
        int destSegment4 = destSegment3;
        long length3 = length;
        while (length3 > 0) {
            if (srcDispl4 == 0) {
                srcDispl4 = SEGMENT_SIZE;
                srcSegment2--;
            }
            if (destDispl2 == 0) {
                destDispl2 = SEGMENT_SIZE;
                destSegment4--;
            }
            int l2 = (int) Math.min(length3, Math.min(srcDispl4, destDispl2));
            if (l2 == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray[srcSegment2], srcDispl4 - l2, destArray[destSegment4], destDispl2 - l2, l2);
            srcDispl4 -= l2;
            destDispl2 -= l2;
            length3 -= l2;
        }
    }

    public static void copyFromBig(float[][] srcArray, long srcPos, float[] destArray, int destPos, int length) {
        int srcSegment = segment(srcPos);
        int srcDispl = displacement(srcPos);
        while (length > 0) {
            int l = Math.min(srcArray[srcSegment].length - srcDispl, length);
            if (l == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray[srcSegment], srcDispl, destArray, destPos, l);
            int i = srcDispl + l;
            srcDispl = i;
            if (i == 134217728) {
                srcDispl = 0;
                srcSegment++;
            }
            destPos += l;
            length -= l;
        }
    }

    public static void copyToBig(float[] srcArray, int srcPos, float[][] destArray, long destPos, long length) {
        int destSegment = segment(destPos);
        int destDispl = displacement(destPos);
        while (length > 0) {
            int l = (int) Math.min(destArray[destSegment].length - destDispl, length);
            if (l == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray, srcPos, destArray[destSegment], destDispl, l);
            int i = destDispl + l;
            destDispl = i;
            if (i == 134217728) {
                destDispl = 0;
                destSegment++;
            }
            srcPos += l;
            length -= l;
        }
    }

    public static float[][] wrap(float[] array) {
        if (array.length == 0) {
            return FloatBigArrays.EMPTY_BIG_ARRAY;
        }
        if (array.length <= 134217728) {
            return new float[][]{array};
        }
        float[][] bigArray = FloatBigArrays.newBigArray(array.length);
        for (int i = 0; i < bigArray.length; i++) {
            System.arraycopy(array, (int) start(i), bigArray[i], 0, bigArray[i].length);
        }
        return bigArray;
    }

    public static float[][] ensureCapacity(float[][] array, long length) {
        return ensureCapacity(array, length, length(array));
    }

    /* JADX WARN: Code restructure failed: missing block: B:6:0x0013, code lost:            if (r15[r15.length - 1].length == 134217728) goto L8;     */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0057  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0040  */
    /* JADX WARN: Removed duplicated region for block: B:9:0x002d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static float[][] forceCapacity(float[][] r15, long r16, long r18) {
        /*
            r8 = r15
            ensureLength(r16)
            int r0 = r8.length
            int r1 = r8.length
            r2 = 134217728(0x8000000, float:3.85186E-34)
            if (r1 == 0) goto L15
            int r1 = r8.length
            r3 = 1
            if (r1 <= 0) goto L16
            int r1 = r8.length
            int r1 = r1 - r3
            r1 = r8[r1]
            int r1 = r1.length
            if (r1 != r2) goto L16
        L15:
            r3 = 0
        L16:
            int r9 = r0 - r3
            r0 = 134217727(0x7ffffff, double:6.6312368E-316)
            long r3 = r16 + r0
            r5 = 27
            long r3 = r3 >>> r5
            int r10 = (int) r3
            java.lang.Object[] r3 = java.util.Arrays.copyOf(r15, r10)
            r11 = r3
            float[][] r11 = (float[][]) r11
            long r0 = r16 & r0
            int r12 = (int) r0
            if (r12 == 0) goto L40
            r0 = r9
        L2e:
            int r1 = r10 + (-1)
            if (r0 >= r1) goto L39
            float[] r1 = new float[r2]
            r11[r0] = r1
            int r0 = r0 + 1
            goto L2e
        L39:
            int r0 = r10 + (-1)
            float[] r1 = new float[r12]
            r11[r0] = r1
            goto L4a
        L40:
            r0 = r9
        L41:
            if (r0 >= r10) goto L4a
            float[] r1 = new float[r2]
            r11[r0] = r1
            int r0 = r0 + 1
            goto L41
        L4a:
            long r0 = (long) r9
            r2 = 134217728(0x8000000, double:6.63123685E-316)
            long r0 = r0 * r2
            long r0 = r18 - r0
            r4 = 0
            int r0 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r0 <= 0) goto L69
            long r0 = (long) r9
            long r4 = r0 * r2
            long r0 = (long) r9
            long r6 = r0 * r2
            long r0 = (long) r9
            long r0 = r0 * r2
            long r13 = r18 - r0
            r0 = r15
            r1 = r4
            r3 = r11
            r4 = r6
            r6 = r13
            copy(r0, r1, r3, r4, r6)
        L69:
            return r11
        */
        throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.BigArrays.forceCapacity(float[][], long, long):float[][]");
    }

    public static float[][] ensureCapacity(float[][] array, long length, long preserve) {
        return length > length(array) ? forceCapacity(array, length, preserve) : array;
    }

    public static float[][] grow(float[][] array, long length) {
        long oldLength = length(array);
        return length > oldLength ? grow(array, length, oldLength) : array;
    }

    public static float[][] grow(float[][] array, long length, long preserve) {
        long oldLength = length(array);
        return length > oldLength ? ensureCapacity(array, Math.max((oldLength >> 1) + oldLength, length), preserve) : array;
    }

    public static float[][] trim(float[][] array, long length) {
        ensureLength(length);
        long oldLength = length(array);
        if (length >= oldLength) {
            return array;
        }
        int baseLength = (int) ((length + 134217727) >>> 27);
        float[][] base = (float[][]) java.util.Arrays.copyOf(array, baseLength);
        int residual = (int) (134217727 & length);
        if (residual != 0) {
            base[baseLength - 1] = FloatArrays.trim(base[baseLength - 1], residual);
        }
        return base;
    }

    public static float[][] setLength(float[][] array, long length) {
        long oldLength = length(array);
        return length == oldLength ? array : length < oldLength ? trim(array, length) : ensureCapacity(array, length);
    }

    public static float[][] copy(float[][] array, long offset, long length) {
        ensureOffsetLength(array, offset, length);
        float[][] a = FloatBigArrays.newBigArray(length);
        copy(array, offset, a, 0L, length);
        return a;
    }

    public static float[][] copy(float[][] array) {
        float[][] base = (float[][]) array.clone();
        int i = base.length;
        while (true) {
            int i2 = i - 1;
            if (i == 0) {
                return base;
            }
            base[i2] = (float[]) array[i2].clone();
            i = i2;
        }
    }

    public static void fill(float[][] array, float value) {
        int i = array.length;
        while (true) {
            int i2 = i - 1;
            if (i == 0) {
                return;
            }
            java.util.Arrays.fill(array[i2], value);
            i = i2;
        }
    }

    public static void fill(float[][] array, long from, long to, float value) {
        long length = length(array);
        ensureFromTo(length, from, to);
        if (length == 0) {
            return;
        }
        int fromSegment = segment(from);
        int toSegment = segment(to);
        int fromDispl = displacement(from);
        int toDispl = displacement(to);
        if (fromSegment == toSegment) {
            java.util.Arrays.fill(array[fromSegment], fromDispl, toDispl, value);
            return;
        }
        if (toDispl != 0) {
            java.util.Arrays.fill(array[toSegment], 0, toDispl, value);
        }
        while (true) {
            toSegment--;
            if (toSegment <= fromSegment) {
                java.util.Arrays.fill(array[fromSegment], fromDispl, SEGMENT_SIZE, value);
                return;
            }
            java.util.Arrays.fill(array[toSegment], value);
        }
    }

    public static boolean equals(float[][] a1, float[][] a2) {
        if (length(a1) != length(a2)) {
            return false;
        }
        int i = a1.length;
        while (true) {
            int i2 = i - 1;
            if (i != 0) {
                float[] t = a1[i2];
                float[] u = a2[i2];
                int j = t.length;
                while (true) {
                    int j2 = j - 1;
                    if (j != 0) {
                        if (Float.floatToIntBits(t[j2]) != Float.floatToIntBits(u[j2])) {
                            return false;
                        }
                        j = j2;
                    }
                }
            } else {
                return true;
            }
            i = i2;
        }
    }

    public static String toString(float[][] a) {
        if (a == null) {
            return "null";
        }
        long last = length(a) - 1;
        if (last == -1) {
            return HttpUrl.PATH_SEGMENT_ENCODE_SET_URI;
        }
        StringBuilder b = new StringBuilder();
        b.append('[');
        long i = 0;
        while (true) {
            b.append(String.valueOf(get(a, i)));
            if (i == last) {
                return b.append(']').toString();
            }
            b.append(", ");
            i++;
        }
    }

    public static void ensureFromTo(float[][] a, long from, long to) {
        ensureFromTo(length(a), from, to);
    }

    public static void ensureOffsetLength(float[][] a, long offset, long length) {
        ensureOffsetLength(length(a), offset, length);
    }

    public static void ensureSameLength(float[][] a, float[][] b) {
        if (length(a) != length(b)) {
            throw new IllegalArgumentException("Array size mismatch: " + length(a) + " != " + length(b));
        }
    }

    public static float[][] shuffle(float[][] a, long from, long to, Random random) {
        long p = to - from;
        while (true) {
            long i = p - 1;
            if (p != 0) {
                long p2 = (random.nextLong() & Long.MAX_VALUE) % (1 + i);
                float t = get(a, from + i);
                set(a, from + i, get(a, from + p2));
                set(a, from + p2, t);
                p = i;
            } else {
                return a;
            }
        }
    }

    public static float[][] shuffle(float[][] a, Random random) {
        long p = length(a);
        while (true) {
            long i = p - 1;
            if (p != 0) {
                long p2 = (random.nextLong() & Long.MAX_VALUE) % (1 + i);
                float t = get(a, i);
                set(a, i, get(a, p2));
                set(a, p2, t);
                p = i;
            } else {
                return a;
            }
        }
    }

    public static <K> K get(K[][] array, long index) {
        return array[segment(index)][displacement(index)];
    }

    public static <K> void set(K[][] array, long index, K value) {
        array[segment(index)][displacement(index)] = value;
    }

    public static <K> void swap(K[][] array, long first, long second) {
        K t = array[segment(first)][displacement(first)];
        array[segment(first)][displacement(first)] = array[segment(second)][displacement(second)];
        array[segment(second)][displacement(second)] = t;
    }

    public static <K> K[][] reverse(K[][] a) {
        long length = length(a);
        long i = length / 2;
        while (true) {
            long i2 = i - 1;
            if (i == 0) {
                return a;
            }
            swap(a, i2, (length - i2) - 1);
            i = i2;
        }
    }

    public static <K> void assertBigArray(K[][] array) {
        int l = array.length;
        if (l == 0) {
            return;
        }
        for (int i = 0; i < l - 1; i++) {
            if (array[i].length != 134217728) {
                throw new IllegalStateException("All segments except for the last one must be of length 2^" + Integer.toString(27));
            }
        }
        int i2 = l - 1;
        if (array[i2].length > 134217728) {
            throw new IllegalStateException("The last segment must be of length at most 2^" + Integer.toString(27));
        }
        if (array[l - 1].length == 0 && l == 1) {
            throw new IllegalStateException("The last segment must be of nonzero length");
        }
    }

    public static <K> long length(K[][] array) {
        int length = array.length;
        if (length == 0) {
            return 0L;
        }
        return start(length - 1) + array[length - 1].length;
    }

    public static <K> void copy(K[][] srcArray, long srcPos, K[][] destArray, long destPos, long length) {
        if (destPos <= srcPos) {
            int srcSegment = segment(srcPos);
            int destSegment = segment(destPos);
            int srcDispl = displacement(srcPos);
            int srcDispl2 = srcDispl;
            int destDispl = displacement(destPos);
            int destSegment2 = destSegment;
            long length2 = length;
            while (length2 > 0) {
                int l = (int) Math.min(length2, Math.min(srcArray[srcSegment].length - srcDispl2, destArray[destSegment2].length - destDispl));
                if (l == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl2, destArray[destSegment2], destDispl, l);
                int i = srcDispl2 + l;
                srcDispl2 = i;
                if (i == 134217728) {
                    srcDispl2 = 0;
                    srcSegment++;
                }
                int i2 = destDispl + l;
                destDispl = i2;
                if (i2 == 134217728) {
                    destDispl = 0;
                    destSegment2++;
                }
                length2 -= l;
            }
            return;
        }
        int srcSegment2 = segment(srcPos + length);
        int destSegment3 = segment(destPos + length);
        int srcDispl3 = displacement(srcPos + length);
        int srcDispl4 = srcDispl3;
        int destDispl2 = displacement(destPos + length);
        int destSegment4 = destSegment3;
        long length3 = length;
        while (length3 > 0) {
            if (srcDispl4 == 0) {
                srcDispl4 = SEGMENT_SIZE;
                srcSegment2--;
            }
            if (destDispl2 == 0) {
                destDispl2 = SEGMENT_SIZE;
                destSegment4--;
            }
            int l2 = (int) Math.min(length3, Math.min(srcDispl4, destDispl2));
            if (l2 == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray[srcSegment2], srcDispl4 - l2, destArray[destSegment4], destDispl2 - l2, l2);
            srcDispl4 -= l2;
            destDispl2 -= l2;
            length3 -= l2;
        }
    }

    public static <K> void copyFromBig(K[][] srcArray, long srcPos, K[] destArray, int destPos, int length) {
        int srcSegment = segment(srcPos);
        int srcDispl = displacement(srcPos);
        while (length > 0) {
            int l = Math.min(srcArray[srcSegment].length - srcDispl, length);
            if (l == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray[srcSegment], srcDispl, destArray, destPos, l);
            int i = srcDispl + l;
            srcDispl = i;
            if (i == 134217728) {
                srcDispl = 0;
                srcSegment++;
            }
            destPos += l;
            length -= l;
        }
    }

    public static <K> void copyToBig(K[] srcArray, int srcPos, K[][] destArray, long destPos, long length) {
        int destSegment = segment(destPos);
        int destDispl = displacement(destPos);
        while (length > 0) {
            int l = (int) Math.min(destArray[destSegment].length - destDispl, length);
            if (l == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray, srcPos, destArray[destSegment], destDispl, l);
            int i = destDispl + l;
            destDispl = i;
            if (i == 134217728) {
                destDispl = 0;
                destSegment++;
            }
            srcPos += l;
            length -= l;
        }
    }

    public static <K> K[][] wrap(K[] kArr) {
        if (kArr.length == 0 && kArr.getClass() == Object[].class) {
            return (K[][]) ObjectBigArrays.EMPTY_BIG_ARRAY;
        }
        if (kArr.length <= 134217728) {
            K[][] kArr2 = (K[][]) ((Object[][]) Array.newInstance(kArr.getClass(), 1));
            kArr2[0] = kArr;
            return kArr2;
        }
        K[][] kArr3 = (K[][]) ObjectBigArrays.newBigArray(kArr.getClass(), kArr.length);
        for (int i = 0; i < kArr3.length; i++) {
            System.arraycopy(kArr, (int) start(i), kArr3[i], 0, kArr3[i].length);
        }
        return kArr3;
    }

    public static <K> K[][] ensureCapacity(K[][] kArr, long j) {
        return (K[][]) ensureCapacity(kArr, j, length(kArr));
    }

    /* JADX WARN: Code restructure failed: missing block: B:6:0x0014, code lost:            if (r16[r16.length - 1].length == 134217728) goto L8;     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:17:0x007e  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x005d  */
    /* JADX WARN: Removed duplicated region for block: B:9:0x0036  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static <K> K[][] forceCapacity(K[][] r16, long r17, long r19) {
        /*
            r8 = r16
            ensureLength(r17)
            int r0 = r8.length
            int r1 = r8.length
            r2 = 134217728(0x8000000, float:3.85186E-34)
            if (r1 == 0) goto L16
            int r1 = r8.length
            r3 = 1
            if (r1 <= 0) goto L17
            int r1 = r8.length
            int r1 = r1 - r3
            r1 = r8[r1]
            int r1 = r1.length
            if (r1 != r2) goto L17
        L16:
            r3 = 0
        L17:
            int r9 = r0 - r3
            r0 = 134217727(0x7ffffff, double:6.6312368E-316)
            long r3 = r17 + r0
            r5 = 27
            long r3 = r3 >>> r5
            int r10 = (int) r3
            java.lang.Object[] r3 = java.util.Arrays.copyOf(r8, r10)
            r11 = r3
            java.lang.Object[][] r11 = (java.lang.Object[][]) r11
            java.lang.Class r3 = r16.getClass()
            java.lang.Class r12 = r3.getComponentType()
            long r0 = r17 & r0
            int r13 = (int) r0
            if (r13 == 0) goto L5d
            r0 = r9
        L37:
            int r1 = r10 + (-1)
            if (r0 >= r1) goto L4c
            java.lang.Class r1 = r12.getComponentType()
            java.lang.Object r1 = java.lang.reflect.Array.newInstance(r1, r2)
            java.lang.Object[] r1 = (java.lang.Object[]) r1
            java.lang.Object[] r1 = (java.lang.Object[]) r1
            r11[r0] = r1
            int r0 = r0 + 1
            goto L37
        L4c:
            int r0 = r10 + (-1)
            java.lang.Class r1 = r12.getComponentType()
            java.lang.Object r1 = java.lang.reflect.Array.newInstance(r1, r13)
            java.lang.Object[] r1 = (java.lang.Object[]) r1
            java.lang.Object[] r1 = (java.lang.Object[]) r1
            r11[r0] = r1
            goto L71
        L5d:
            r0 = r9
        L5e:
            if (r0 >= r10) goto L71
            java.lang.Class r1 = r12.getComponentType()
            java.lang.Object r1 = java.lang.reflect.Array.newInstance(r1, r2)
            java.lang.Object[] r1 = (java.lang.Object[]) r1
            java.lang.Object[] r1 = (java.lang.Object[]) r1
            r11[r0] = r1
            int r0 = r0 + 1
            goto L5e
        L71:
            long r0 = (long) r9
            r2 = 134217728(0x8000000, double:6.63123685E-316)
            long r0 = r0 * r2
            long r0 = r19 - r0
            r4 = 0
            int r0 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r0 <= 0) goto L91
            long r0 = (long) r9
            long r4 = r0 * r2
            long r0 = (long) r9
            long r6 = r0 * r2
            long r0 = (long) r9
            long r0 = r0 * r2
            long r14 = r19 - r0
            r0 = r16
            r1 = r4
            r3 = r11
            r4 = r6
            r6 = r14
            copy(r0, r1, r3, r4, r6)
        L91:
            return r11
        */
        throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.BigArrays.forceCapacity(java.lang.Object[][], long, long):java.lang.Object[][]");
    }

    public static <K> K[][] ensureCapacity(K[][] kArr, long j, long j2) {
        return j > length(kArr) ? (K[][]) forceCapacity(kArr, j, j2) : kArr;
    }

    public static <K> K[][] grow(K[][] kArr, long j) {
        long length = length(kArr);
        return j > length ? (K[][]) grow(kArr, j, length) : kArr;
    }

    public static <K> K[][] grow(K[][] kArr, long j, long j2) {
        long length = length(kArr);
        return j > length ? (K[][]) ensureCapacity(kArr, Math.max((length >> 1) + length, j), j2) : kArr;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <K> K[][] trim(K[][] kArr, long j) {
        ensureLength(j);
        if (j >= length(kArr)) {
            return kArr;
        }
        int i = (int) ((j + 134217727) >>> 27);
        K[][] kArr2 = (K[][]) ((Object[][]) java.util.Arrays.copyOf(kArr, i));
        int i2 = (int) (134217727 & j);
        if (i2 != 0) {
            kArr2[i - 1] = ObjectArrays.trim(kArr2[i - 1], i2);
        }
        return kArr2;
    }

    public static <K> K[][] setLength(K[][] kArr, long j) {
        long length = length(kArr);
        return j == length ? kArr : j < length ? (K[][]) trim(kArr, j) : (K[][]) ensureCapacity(kArr, j);
    }

    public static <K> K[][] copy(K[][] kArr, long j, long j2) {
        ensureOffsetLength(kArr, j, j2);
        K[][] kArr2 = (K[][]) ObjectBigArrays.newBigArray(kArr, j2);
        copy(kArr, j, kArr2, 0L, j2);
        return kArr2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <K> K[][] copy(K[][] kArr) {
        K[][] kArr2 = (K[][]) ((Object[][]) kArr.clone());
        int length = kArr2.length;
        while (true) {
            int i = length - 1;
            if (length == 0) {
                return kArr2;
            }
            kArr2[i] = (Object[]) kArr[i].clone();
            length = i;
        }
    }

    public static <K> void fill(K[][] array, K value) {
        int i = array.length;
        while (true) {
            int i2 = i - 1;
            if (i == 0) {
                return;
            }
            java.util.Arrays.fill(array[i2], value);
            i = i2;
        }
    }

    public static <K> void fill(K[][] array, long from, long to, K value) {
        long length = length(array);
        ensureFromTo(length, from, to);
        if (length == 0) {
            return;
        }
        int fromSegment = segment(from);
        int toSegment = segment(to);
        int fromDispl = displacement(from);
        int toDispl = displacement(to);
        if (fromSegment == toSegment) {
            java.util.Arrays.fill(array[fromSegment], fromDispl, toDispl, value);
            return;
        }
        if (toDispl != 0) {
            java.util.Arrays.fill(array[toSegment], 0, toDispl, value);
        }
        while (true) {
            toSegment--;
            if (toSegment <= fromSegment) {
                java.util.Arrays.fill(array[fromSegment], fromDispl, SEGMENT_SIZE, value);
                return;
            }
            java.util.Arrays.fill(array[toSegment], value);
        }
    }

    public static <K> boolean equals(K[][] a1, K[][] a2) {
        if (length(a1) != length(a2)) {
            return false;
        }
        int i = a1.length;
        while (true) {
            int i2 = i - 1;
            if (i != 0) {
                K[] t = a1[i2];
                K[] u = a2[i2];
                int j = t.length;
                while (true) {
                    int j2 = j - 1;
                    if (j != 0) {
                        if (!Objects.equals(t[j2], u[j2])) {
                            return false;
                        }
                        j = j2;
                    }
                }
            } else {
                return true;
            }
            i = i2;
        }
    }

    public static <K> String toString(K[][] a) {
        if (a == null) {
            return "null";
        }
        long last = length(a) - 1;
        if (last == -1) {
            return HttpUrl.PATH_SEGMENT_ENCODE_SET_URI;
        }
        StringBuilder b = new StringBuilder();
        b.append('[');
        long i = 0;
        while (true) {
            b.append(String.valueOf(get(a, i)));
            if (i == last) {
                return b.append(']').toString();
            }
            b.append(", ");
            i++;
        }
    }

    public static <K> void ensureFromTo(K[][] a, long from, long to) {
        ensureFromTo(length(a), from, to);
    }

    public static <K> void ensureOffsetLength(K[][] a, long offset, long length) {
        ensureOffsetLength(length(a), offset, length);
    }

    public static <K> void ensureSameLength(K[][] a, K[][] b) {
        if (length(a) != length(b)) {
            throw new IllegalArgumentException("Array size mismatch: " + length(a) + " != " + length(b));
        }
    }

    public static <K> K[][] shuffle(K[][] a, long from, long to, Random random) {
        long p = to - from;
        while (true) {
            long i = p - 1;
            if (p != 0) {
                long p2 = (random.nextLong() & Long.MAX_VALUE) % (1 + i);
                Object obj = get(a, from + i);
                set(a, from + i, get(a, from + p2));
                set(a, from + p2, obj);
                p = i;
            } else {
                return a;
            }
        }
    }

    public static <K> K[][] shuffle(K[][] a, Random random) {
        long p = length(a);
        while (true) {
            long i = p - 1;
            if (p != 0) {
                long p2 = (random.nextLong() & Long.MAX_VALUE) % (1 + i);
                Object obj = get(a, i);
                set(a, i, get(a, p2));
                set(a, p2, obj);
                p = i;
            } else {
                return a;
            }
        }
    }

    public static void main(String[] arg) {
        long j = 1;
        int[][] a = IntBigArrays.newBigArray(1 << Integer.parseInt(arg[0]));
        int k = 10;
        while (true) {
            int k2 = k - 1;
            if (k != 0) {
                long start = -System.currentTimeMillis();
                long x = 0;
                long i = length(a);
                while (true) {
                    long i2 = i - j;
                    if (i == 0) {
                        break;
                    }
                    x ^= get(a, i2) ^ i2;
                    i = i2;
                }
                if (x == 0) {
                    System.err.println();
                }
                System.out.println("Single loop: " + (System.currentTimeMillis() + start) + "ms");
                long start2 = -System.currentTimeMillis();
                long y = 0;
                int i3 = a.length;
                while (true) {
                    int i4 = i3 - 1;
                    if (i3 == 0) {
                        break;
                    }
                    int[] t = a[i4];
                    int d = t.length;
                    while (true) {
                        int d2 = d - 1;
                        if (d != 0) {
                            y ^= t[d2] ^ index(i4, d2);
                            d = d2;
                        }
                    }
                    i3 = i4;
                }
                if (y == 0) {
                    System.err.println();
                }
                if (x != y) {
                    throw new AssertionError();
                }
                System.out.println("Double loop: " + (System.currentTimeMillis() + start2) + "ms");
                long j2 = length(a);
                int i5 = a.length;
                while (true) {
                    int i6 = i5 - 1;
                    if (i5 == 0) {
                        break;
                    }
                    int[] t2 = a[i6];
                    int d3 = t2.length;
                    while (true) {
                        int d4 = d3 - 1;
                        if (d3 != 0) {
                            long j3 = j2 - 1;
                            j2 = j3;
                            y ^= t2[d4] ^ j3;
                            d3 = d4;
                            k2 = k2;
                            t2 = t2;
                        }
                    }
                    i5 = i6;
                }
                int k3 = k2;
                if (0 == 0) {
                    System.err.println();
                }
                if (x != 0) {
                    throw new AssertionError();
                }
                System.out.println("Double loop (with additional index): " + (start2 + System.currentTimeMillis()) + "ms");
                a = a;
                j = 1;
                k = k3;
            } else {
                return;
            }
        }
    }
}
