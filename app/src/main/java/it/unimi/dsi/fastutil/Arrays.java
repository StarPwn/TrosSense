package it.unimi.dsi.fastutil;

import it.unimi.dsi.fastutil.ints.IntComparator;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

/* loaded from: classes4.dex */
public class Arrays {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final int MAX_ARRAY_SIZE = 2147483639;
    private static final int MERGESORT_NO_REC = 16;
    private static final int PARALLEL_QUICKSORT_NO_FORK = 8192;
    private static final int QUICKSORT_MEDIAN_OF_9 = 128;
    private static final int QUICKSORT_NO_REC = 16;

    private Arrays() {
    }

    public static void ensureFromTo(int arrayLength, int from, int to) {
        if (arrayLength < 0) {
            throw new AssertionError();
        }
        if (from < 0) {
            throw new ArrayIndexOutOfBoundsException("Start index (" + from + ") is negative");
        }
        if (from > to) {
            throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        if (to > arrayLength) {
            throw new ArrayIndexOutOfBoundsException("End index (" + to + ") is greater than array length (" + arrayLength + ")");
        }
    }

    public static void ensureOffsetLength(int arrayLength, int offset, int length) {
        if (arrayLength < 0) {
            throw new AssertionError();
        }
        if (offset < 0) {
            throw new ArrayIndexOutOfBoundsException("Offset (" + offset + ") is negative");
        }
        if (length < 0) {
            throw new IllegalArgumentException("Length (" + length + ") is negative");
        }
        if (length > arrayLength - offset) {
            throw new ArrayIndexOutOfBoundsException("Last index (" + (offset + length) + ") is greater than array length (" + arrayLength + ")");
        }
    }

    private static void inPlaceMerge(int from, int mid, int to, IntComparator comp, Swapper swapper) {
        int secondCut;
        int firstCut;
        if (from >= mid || mid >= to) {
            return;
        }
        if (to - from == 2) {
            if (comp.compare(mid, from) < 0) {
                swapper.swap(from, mid);
                return;
            }
            return;
        }
        if (mid - from > to - mid) {
            firstCut = ((mid - from) / 2) + from;
            secondCut = lowerBound(mid, to, firstCut, comp);
        } else {
            int firstCut2 = to - mid;
            secondCut = mid + (firstCut2 / 2);
            firstCut = upperBound(from, mid, secondCut, comp);
        }
        int first2 = firstCut;
        int last2 = secondCut;
        if (mid != first2 && mid != last2) {
            int first1 = first2;
            int last1 = mid;
            while (true) {
                last1--;
                if (first1 >= last1) {
                    break;
                }
                swapper.swap(first1, last1);
                first1++;
            }
            int first12 = mid;
            int last12 = last2;
            while (true) {
                last12--;
                if (first12 >= last12) {
                    break;
                }
                swapper.swap(first12, last12);
                first12++;
            }
            int first13 = first2;
            int last13 = last2;
            while (true) {
                last13--;
                if (first13 >= last13) {
                    break;
                }
                swapper.swap(first13, last13);
                first13++;
            }
        }
        int mid2 = (secondCut - mid) + firstCut;
        inPlaceMerge(from, firstCut, mid2, comp, swapper);
        inPlaceMerge(mid2, secondCut, to, comp, swapper);
    }

    private static int lowerBound(int from, int to, int pos, IntComparator comp) {
        int len = to - from;
        while (len > 0) {
            int half = len / 2;
            int middle = from + half;
            if (comp.compare(middle, pos) < 0) {
                from = middle + 1;
                len -= half + 1;
            } else {
                len = half;
            }
        }
        return from;
    }

    private static int upperBound(int from, int mid, int pos, IntComparator comp) {
        int len = mid - from;
        while (len > 0) {
            int half = len / 2;
            int middle = from + half;
            if (comp.compare(pos, middle) < 0) {
                len = half;
            } else {
                from = middle + 1;
                len -= half + 1;
            }
        }
        return from;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0018, code lost:            if (r1 > 0) goto L11;     */
    /* JADX WARN: Code restructure failed: missing block: B:4:0x0011, code lost:            if (r1 < 0) goto L11;     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x001d, code lost:            return r4;     */
    /* JADX WARN: Code restructure failed: missing block: B:9:?, code lost:            return r6;     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int med3(int r4, int r5, int r6, it.unimi.dsi.fastutil.ints.IntComparator r7) {
        /*
            int r0 = r7.compare(r4, r5)
            int r1 = r7.compare(r4, r6)
            int r2 = r7.compare(r5, r6)
            if (r0 >= 0) goto L14
            if (r2 >= 0) goto L11
            goto L16
        L11:
            if (r1 >= 0) goto L1c
            goto L1a
        L14:
            if (r2 <= 0) goto L18
        L16:
            r3 = r5
            goto L1d
        L18:
            if (r1 <= 0) goto L1c
        L1a:
            r3 = r6
            goto L1d
        L1c:
            r3 = r4
        L1d:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: it.unimi.dsi.fastutil.Arrays.med3(int, int, int, it.unimi.dsi.fastutil.ints.IntComparator):int");
    }

    private static ForkJoinPool getPool() {
        ForkJoinPool current = ForkJoinTask.getPool();
        return current == null ? ForkJoinPool.commonPool() : current;
    }

    public static void mergeSort(int from, int to, IntComparator c, Swapper swapper) {
        int length = to - from;
        if (length < 16) {
            for (int i = from; i < to; i++) {
                for (int j = i; j > from && c.compare(j - 1, j) > 0; j--) {
                    swapper.swap(j, j - 1);
                }
            }
            return;
        }
        int mid = (from + to) >>> 1;
        mergeSort(from, mid, c, swapper);
        mergeSort(mid, to, c, swapper);
        if (c.compare(mid - 1, mid) <= 0) {
            return;
        }
        inPlaceMerge(from, mid, to, c, swapper);
    }

    protected static void swap(Swapper swapper, int a, int b, int n) {
        int i = 0;
        while (i < n) {
            swapper.swap(a, b);
            i++;
            a++;
            b++;
        }
    }

    /* loaded from: classes4.dex */
    protected static class ForkJoinGenericQuickSort extends RecursiveAction {
        private static final long serialVersionUID = 1;
        private final IntComparator comp;
        private final int from;
        private final Swapper swapper;
        private final int to;

        public ForkJoinGenericQuickSort(int from, int to, IntComparator comp, Swapper swapper) {
            this.from = from;
            this.to = to;
            this.comp = comp;
            this.swapper = swapper;
        }

        @Override // java.util.concurrent.RecursiveAction
        protected void compute() {
            int len = this.to - this.from;
            if (len < 8192) {
                Arrays.quickSort(this.from, this.to, this.comp, this.swapper);
                return;
            }
            int m = this.from + (len / 2);
            int l = this.from;
            int n = this.to - 1;
            int s = len / 8;
            int b = Arrays.med3(l, l + s, (s * 2) + l, this.comp);
            int m2 = Arrays.med3(b, Arrays.med3(m - s, m, m + s, this.comp), Arrays.med3(n - (s * 2), n - s, n, this.comp), this.comp);
            int a = this.from;
            int b2 = a;
            int c = this.to - 1;
            int d = c;
            while (true) {
                if (b2 <= c) {
                    int comparison = this.comp.compare(b2, m2);
                    if (comparison <= 0) {
                        if (comparison == 0) {
                            if (a == m2) {
                                m2 = b2;
                            } else if (b2 == m2) {
                                m2 = a;
                            }
                            this.swapper.swap(a, b2);
                            a++;
                        }
                        b2++;
                    }
                }
                while (c >= b2) {
                    int comparison2 = this.comp.compare(c, m2);
                    if (comparison2 < 0) {
                        break;
                    }
                    if (comparison2 == 0) {
                        if (c == m2) {
                            m2 = d;
                        } else if (d == m2) {
                            m2 = c;
                        }
                        this.swapper.swap(c, d);
                        d--;
                    }
                    c--;
                }
                if (b2 > c) {
                    break;
                }
                int len2 = len;
                int l2 = b;
                if (b2 == m2) {
                    m2 = d;
                } else if (c == m2) {
                    m2 = c;
                }
                this.swapper.swap(b2, c);
                b2++;
                c--;
                len = len2;
                b = l2;
            }
            int s2 = Math.min(a - this.from, b2 - a);
            Arrays.swap(this.swapper, this.from, b2 - s2, s2);
            int s3 = Math.min(d - c, (this.to - d) - 1);
            Arrays.swap(this.swapper, b2, this.to - s3, s3);
            int s4 = b2 - a;
            int t = d - c;
            if (s4 > 1 && t > 1) {
                invokeAll(new ForkJoinGenericQuickSort(this.from, this.from + s4, this.comp, this.swapper), new ForkJoinGenericQuickSort(this.to - t, this.to, this.comp, this.swapper));
            } else if (s4 > 1) {
                invokeAll(new ForkJoinTask[]{new ForkJoinGenericQuickSort(this.from, this.from + s4, this.comp, this.swapper)});
            } else {
                invokeAll(new ForkJoinTask[]{new ForkJoinGenericQuickSort(this.to - t, this.to, this.comp, this.swapper)});
            }
        }
    }

    public static void parallelQuickSort(int from, int to, IntComparator comp, Swapper swapper) {
        ForkJoinPool pool = getPool();
        if (to - from < 8192 || pool.getParallelism() == 1) {
            quickSort(from, to, comp, swapper);
        } else {
            pool.invoke(new ForkJoinGenericQuickSort(from, to, comp, swapper));
        }
    }

    public static void quickSort(int from, int to, IntComparator comp, Swapper swapper) {
        int len = to - from;
        if (len < 16) {
            for (int i = from; i < to; i++) {
                for (int j = i; j > from && comp.compare(j - 1, j) > 0; j--) {
                    swapper.swap(j, j - 1);
                }
            }
            return;
        }
        int m = (len / 2) + from;
        int l = from;
        int n = to - 1;
        if (len > 128) {
            int s = len / 8;
            l = med3(l, l + s, (s * 2) + l, comp);
            m = med3(m - s, m, m + s, comp);
            n = med3(n - (s * 2), n - s, n, comp);
        }
        int m2 = med3(l, m, n, comp);
        int a = from;
        int b = a;
        int c = to - 1;
        int d = c;
        while (true) {
            if (b <= c) {
                int comparison = comp.compare(b, m2);
                if (comparison <= 0) {
                    if (comparison == 0) {
                        if (a == m2) {
                            m2 = b;
                        } else if (b == m2) {
                            m2 = a;
                        }
                        swapper.swap(a, b);
                        a++;
                    }
                    b++;
                }
            }
            while (c >= b) {
                int comparison2 = comp.compare(c, m2);
                if (comparison2 < 0) {
                    break;
                }
                if (comparison2 == 0) {
                    if (c == m2) {
                        m2 = d;
                    } else if (d == m2) {
                        m2 = c;
                    }
                    swapper.swap(c, d);
                    d--;
                }
                c--;
            }
            if (b > c) {
                break;
            }
            if (b == m2) {
                m2 = d;
            } else if (c == m2) {
                m2 = c;
            }
            swapper.swap(b, c);
            b++;
            c--;
        }
        int s2 = Math.min(a - from, b - a);
        swap(swapper, from, b - s2, s2);
        int s3 = Math.min(d - c, (to - d) - 1);
        swap(swapper, b, to - s3, s3);
        int s4 = b - a;
        if (s4 > 1) {
            quickSort(from, from + s4, comp, swapper);
        }
        int s5 = d - c;
        if (s5 > 1) {
            quickSort(to - s5, to, comp, swapper);
        }
    }
}
