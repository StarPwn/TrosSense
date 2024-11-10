package io.netty.handler.codec.compression;

import com.trossense.bl;

/* loaded from: classes4.dex */
final class Bzip2DivSufSort {
    private static final int BUCKET_A_SIZE = 256;
    private static final int BUCKET_B_SIZE = 65536;
    private static final int INSERTIONSORT_THRESHOLD = 8;
    private static final int[] LOG_2_TABLE = {-1, 0, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7};
    private static final int SS_BLOCKSIZE = 1024;
    private static final int STACK_SIZE = 64;
    private final int[] SA;
    private final byte[] T;
    private final int n;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Bzip2DivSufSort(byte[] block, int[] bwtBlock, int blockLength) {
        this.T = block;
        this.SA = bwtBlock;
        this.n = blockLength;
    }

    private static void swapElements(int[] array1, int idx1, int[] array2, int idx2) {
        int temp = array1[idx1];
        array1[idx1] = array2[idx2];
        array2[idx2] = temp;
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:?, code lost:            return (r1[r4] & 255) - (r1[r5] & 255);     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0033, code lost:            return 1;     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0027, code lost:            if (r5 >= r3) goto L11;     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private int ssCompare(int r9, int r10, int r11) {
        /*
            r8 = this;
            int[] r0 = r8.SA
            byte[] r1 = r8.T
            int r2 = r9 + 1
            r2 = r0[r2]
            int r2 = r2 + 2
            int r3 = r10 + 1
            r3 = r0[r3]
            int r3 = r3 + 2
            r4 = r0[r9]
            int r4 = r4 + r11
            r5 = r0[r10]
            int r5 = r5 + r11
        L16:
            if (r4 >= r2) goto L25
            if (r5 >= r3) goto L25
            r6 = r1[r4]
            r7 = r1[r5]
            if (r6 != r7) goto L25
            int r4 = r4 + 1
            int r5 = r5 + 1
            goto L16
        L25:
            if (r4 >= r2) goto L35
            if (r5 >= r3) goto L33
            r6 = r1[r4]
            r6 = r6 & 255(0xff, float:3.57E-43)
            r7 = r1[r5]
            r7 = r7 & 255(0xff, float:3.57E-43)
            int r6 = r6 - r7
            goto L3a
        L33:
            r6 = 1
            goto L3a
        L35:
            if (r5 >= r3) goto L39
            r6 = -1
            goto L3a
        L39:
            r6 = 0
        L3a:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.codec.compression.Bzip2DivSufSort.ssCompare(int, int, int):int");
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x0048, code lost:            if (r3 >= r5) goto L35;     */
    /* JADX WARN: Code restructure failed: missing block: B:27:?, code lost:            return (r1[r2] & 255) - (r1[r3] & 255);     */
    /* JADX WARN: Code restructure failed: missing block: B:28:?, code lost:            return 1;     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private int ssCompareLast(int r10, int r11, int r12, int r13, int r14) {
        /*
            r9 = this;
            int[] r0 = r9.SA
            byte[] r1 = r9.T
            r2 = r0[r11]
            int r2 = r2 + r13
            r3 = r0[r12]
            int r3 = r3 + r13
            r4 = r14
            int r5 = r12 + 1
            r5 = r0[r5]
            int r5 = r5 + 2
        L11:
            if (r2 >= r4) goto L20
            if (r3 >= r5) goto L20
            r6 = r1[r2]
            r7 = r1[r3]
            if (r6 != r7) goto L20
            int r2 = r2 + 1
            int r3 = r3 + 1
            goto L11
        L20:
            r6 = 1
            if (r2 >= r4) goto L2f
            if (r3 >= r5) goto L2e
            r6 = r1[r2]
            r6 = r6 & 255(0xff, float:3.57E-43)
            r7 = r1[r3]
            r7 = r7 & 255(0xff, float:3.57E-43)
            int r6 = r6 - r7
        L2e:
            return r6
        L2f:
            if (r3 != r5) goto L32
            return r6
        L32:
            int r2 = r2 % r14
            r7 = r0[r10]
            int r7 = r7 + 2
        L37:
            if (r2 >= r7) goto L46
            if (r3 >= r5) goto L46
            r4 = r1[r2]
            r8 = r1[r3]
            if (r4 != r8) goto L46
            int r2 = r2 + 1
            int r3 = r3 + 1
            goto L37
        L46:
            if (r2 >= r7) goto L55
            if (r3 >= r5) goto L5a
            r4 = r1[r2]
            r4 = r4 & 255(0xff, float:3.57E-43)
            r6 = r1[r3]
            r6 = r6 & 255(0xff, float:3.57E-43)
            int r6 = r4 - r6
            goto L5a
        L55:
            if (r3 >= r5) goto L59
            r6 = -1
            goto L5a
        L59:
            r6 = 0
        L5a:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.codec.compression.Bzip2DivSufSort.ssCompareLast(int, int, int, int, int):int");
    }

    private void ssInsertionSort(int pa, int first, int last, int depth) {
        int r;
        int[] SA = this.SA;
        for (int i = last - 2; first <= i; i--) {
            int t = SA[i];
            int j = i + 1;
            do {
                r = ssCompare(pa + t, SA[j] + pa, depth);
                if (r <= 0) {
                    break;
                }
                do {
                    SA[j - 1] = SA[j];
                    j++;
                    if (j >= last) {
                        break;
                    }
                } while (SA[j] < 0);
            } while (last > j);
            if (r == 0) {
                SA[j] = ~SA[j];
            }
            SA[j - 1] = t;
        }
    }

    private void ssFixdown(int td, int pa, int sa, int i, int size) {
        int[] SA = this.SA;
        byte[] T = this.T;
        int v = SA[sa + i];
        int c = T[SA[pa + v] + td] & 255;
        while (true) {
            int j = (i * 2) + 1;
            if (j >= size) {
                break;
            }
            int j2 = j + 1;
            int k = j;
            int d = T[SA[SA[j + sa] + pa] + td] & 255;
            int e = T[SA[SA[sa + j2] + pa] + td] & 255;
            if (d < e) {
                k = j2;
                d = e;
            }
            if (d <= c) {
                break;
            }
            SA[sa + i] = SA[sa + k];
            i = k;
        }
        SA[sa + i] = v;
    }

    private void ssHeapSort(int td, int pa, int sa, int size) {
        int[] SA = this.SA;
        byte[] T = this.T;
        int m = size;
        if (size % 2 == 0) {
            m--;
            if ((T[SA[SA[(m / 2) + sa] + pa] + td] & 255) < (T[SA[SA[sa + m] + pa] + td] & 255)) {
                swapElements(SA, sa + m, SA, (m / 2) + sa);
            }
        }
        for (int i = (m / 2) - 1; i >= 0; i--) {
            ssFixdown(td, pa, sa, i, m);
        }
        if (size % 2 == 0) {
            swapElements(SA, sa, SA, sa + m);
            ssFixdown(td, pa, sa, 0, m);
        }
        for (int i2 = m - 1; i2 > 0; i2--) {
            int t = SA[sa];
            SA[sa] = SA[sa + i2];
            ssFixdown(td, pa, sa, 0, i2);
            SA[sa + i2] = t;
        }
    }

    private int ssMedian3(int td, int pa, int v1, int v2, int v3) {
        int[] SA = this.SA;
        byte[] T = this.T;
        int T_v1 = T[SA[SA[v1] + pa] + td] & 255;
        int T_v2 = T[SA[SA[v2] + pa] + td] & 255;
        int T_v3 = T[SA[SA[v3] + pa] + td] & 255;
        if (T_v1 > T_v2) {
            v1 = v2;
            v2 = v1;
            T_v1 = T_v2;
            T_v2 = T_v1;
        }
        if (T_v2 > T_v3) {
            if (T_v1 > T_v3) {
                return v1;
            }
            return v3;
        }
        return v2;
    }

    private int ssMedian5(int td, int pa, int v1, int v2, int v3, int v4, int v5) {
        int v22;
        int v32;
        int v42;
        int v52;
        int v12;
        int[] SA = this.SA;
        byte[] T = this.T;
        int T_v1 = T[td + SA[pa + SA[v1]]] & 255;
        int T_v2 = T[td + SA[pa + SA[v2]]] & 255;
        int T_v3 = T[td + SA[pa + SA[v3]]] & 255;
        int T_v4 = T[td + SA[pa + SA[v4]]] & 255;
        int T_v5 = T[td + SA[pa + SA[v5]]] & 255;
        if (T_v2 <= T_v3) {
            v22 = v2;
            v32 = v3;
        } else {
            v22 = v3;
            v32 = v2;
            T_v2 = T_v3;
            T_v3 = T_v2;
        }
        if (T_v4 <= T_v5) {
            v42 = v4;
            v52 = v5;
        } else {
            v42 = v5;
            v52 = v4;
            T_v4 = T_v5;
            T_v5 = T_v4;
        }
        if (T_v2 > T_v4) {
            int temp = v22;
            v42 = temp;
            int T_vtemp = T_v2;
            T_v4 = T_vtemp;
            int temp2 = v32;
            v32 = v52;
            v52 = temp2;
            int T_vtemp2 = T_v3;
            T_v3 = T_v5;
            T_v5 = T_vtemp2;
        }
        if (T_v1 <= T_v3) {
            v12 = v1;
        } else {
            v12 = v32;
            v32 = v1;
            T_v1 = T_v3;
            T_v3 = T_v1;
        }
        if (T_v1 > T_v4) {
            int temp3 = v12;
            v42 = temp3;
            int T_vtemp3 = T_v1;
            T_v4 = T_vtemp3;
            v32 = v52;
            T_v3 = T_v5;
        }
        if (T_v3 > T_v4) {
            return v42;
        }
        return v32;
    }

    private int ssPivot(int td, int pa, int first, int last) {
        int t = last - first;
        int middle = first + (t / 2);
        if (t <= 512) {
            if (t <= 32) {
                return ssMedian3(td, pa, first, middle, last - 1);
            }
            int t2 = t >> 2;
            return ssMedian5(td, pa, first, first + t2, middle, (last - 1) - t2, last - 1);
        }
        int t3 = t >> 3;
        return ssMedian3(td, pa, ssMedian3(td, pa, first, first + t3, first + (t3 << 1)), ssMedian3(td, pa, middle - t3, middle, middle + t3), ssMedian3(td, pa, (last - 1) - (t3 << 1), (last - 1) - t3, last - 1));
    }

    private static int ssLog(int n) {
        return (65280 & n) != 0 ? LOG_2_TABLE[(n >> 8) & 255] + 8 : LOG_2_TABLE[n & 255];
    }

    private int ssSubstringPartition(int pa, int first, int last, int depth) {
        int[] SA = this.SA;
        int a = first - 1;
        int b = last;
        while (true) {
            a++;
            if (a < b && SA[SA[a] + pa] + depth >= SA[SA[a] + pa + 1] + 1) {
                SA[a] = ~SA[a];
            } else {
                b--;
                while (a < b && SA[SA[b] + pa] + depth < SA[SA[b] + pa + 1] + 1) {
                    b--;
                }
                if (b <= a) {
                    break;
                }
                int t = ~SA[b];
                SA[b] = SA[a];
                SA[a] = t;
            }
        }
        if (first < a) {
            SA[first] = ~SA[first];
        }
        return a;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class StackEntry {
        final int a;
        final int b;
        final int c;
        final int d;

        StackEntry(int a, int b, int c, int d) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
        }
    }

    private void ssMultiKeyIntroSort(int pa, int first, int last, int depth) {
        int x;
        int x2;
        int b;
        int c;
        int x3;
        int a;
        int a2;
        int b2;
        Bzip2DivSufSort bzip2DivSufSort = this;
        int[] SA = bzip2DivSufSort.SA;
        byte[] T = bzip2DivSufSort.T;
        StackEntry[] stack = new StackEntry[64];
        int x4 = 0;
        int ssize = 0;
        int a3 = ssLog(last - first);
        int first2 = first;
        int last2 = last;
        int depth2 = depth;
        while (true) {
            if (last2 - first2 <= 8) {
                if (1 < last2 - first2) {
                    bzip2DivSufSort.ssInsertionSort(pa, first2, last2, depth2);
                }
                if (ssize == 0) {
                    return;
                }
                ssize--;
                StackEntry entry = stack[ssize];
                first2 = entry.a;
                last2 = entry.b;
                depth2 = entry.c;
                a3 = entry.d;
            } else {
                int Td = depth2;
                int limit = a3 - 1;
                if (a3 == 0) {
                    bzip2DivSufSort.ssHeapSort(Td, pa, first2, last2 - first2);
                }
                if (limit < 0) {
                    int a4 = first2 + 1;
                    int v = T[SA[SA[first2] + pa] + Td] & 255;
                    while (a4 < last2) {
                        int i = T[Td + SA[pa + SA[a4]]] & 255;
                        x4 = i;
                        if (i != v) {
                            if (1 < a4 - first2) {
                                break;
                            }
                            first2 = a4;
                            v = x4;
                        }
                        a4++;
                    }
                    if ((T[(SA[SA[first2] + pa] + Td) - 1] & 255) < v) {
                        first2 = bzip2DivSufSort.ssSubstringPartition(pa, first2, a4, depth2);
                    }
                    if (a4 - first2 <= last2 - a4) {
                        if (1 < a4 - first2) {
                            stack[ssize] = new StackEntry(a4, last2, depth2, -1);
                            last2 = a4;
                            depth2++;
                            ssize++;
                            a3 = ssLog(a4 - first2);
                            x4 = x4;
                        } else {
                            first2 = a4;
                            a3 = -1;
                        }
                    } else {
                        int x5 = x4;
                        int x6 = last2 - a4;
                        if (1 < x6) {
                            int ssize2 = ssLog(a4 - first2);
                            stack[ssize] = new StackEntry(first2, a4, depth2 + 1, ssize2);
                            first2 = a4;
                            a3 = -1;
                            ssize++;
                            x4 = x5;
                        } else {
                            last2 = a4;
                            depth2++;
                            a3 = ssLog(a4 - first2);
                            x4 = x5;
                        }
                    }
                } else {
                    int a5 = bzip2DivSufSort.ssPivot(Td, pa, first2, last2);
                    int v2 = T[SA[SA[a5] + pa] + Td] & 255;
                    swapElements(SA, first2, SA, a5);
                    int b3 = first2 + 1;
                    while (b3 < last2) {
                        int i2 = T[SA[SA[b3] + pa] + Td] & 255;
                        x4 = i2;
                        if (i2 != v2) {
                            break;
                        } else {
                            b3++;
                        }
                    }
                    int a6 = b3;
                    if (b3 < last2 && x4 < v2) {
                        while (true) {
                            b3++;
                            if (b3 >= last2) {
                                break;
                            }
                            int i3 = T[SA[SA[b3] + pa] + Td] & 255;
                            x4 = i3;
                            if (i3 > v2) {
                                break;
                            } else if (x4 == v2) {
                                swapElements(SA, b3, SA, a6);
                                a6++;
                            }
                        }
                    }
                    int c2 = last2 - 1;
                    while (true) {
                        if (b3 >= c2) {
                            break;
                        }
                        int x7 = T[Td + SA[pa + SA[c2]]];
                        int x8 = x7 & 255;
                        if (x8 != v2) {
                            x4 = x8;
                            break;
                        } else {
                            c2--;
                            x4 = x8;
                        }
                    }
                    int d = c2;
                    if (b3 < c2 && x4 > v2) {
                        x = x4;
                        x2 = d;
                        while (true) {
                            c2--;
                            if (b3 >= c2) {
                                break;
                            }
                            int a7 = a6;
                            int a8 = T[Td + SA[pa + SA[c2]]];
                            int i4 = a8 & 255;
                            x = i4;
                            if (i4 < v2) {
                                a6 = a7;
                                break;
                            } else if (x != v2) {
                                x = x;
                                a6 = a7;
                            } else {
                                swapElements(SA, c2, SA, x2);
                                x2--;
                                x = x;
                                a6 = a7;
                            }
                        }
                    } else {
                        x = x4;
                        x2 = d;
                        a6 = a6;
                    }
                    while (b3 < c2) {
                        swapElements(SA, b3, SA, c2);
                        int b4 = b3;
                        int b5 = a6;
                        int b6 = x;
                        while (true) {
                            int x9 = b6;
                            b = b4 + 1;
                            if (b >= c2) {
                                c = c2;
                                x3 = x9;
                                break;
                            }
                            c = c2;
                            int c3 = T[Td + SA[pa + SA[b]]];
                            int x10 = c3 & 255;
                            if (x10 > v2) {
                                x3 = x10;
                                break;
                            }
                            if (x10 != v2) {
                                b4 = b;
                                b6 = x10;
                                c2 = c;
                            } else {
                                swapElements(SA, b, SA, b5);
                                b5++;
                                b4 = b;
                                b6 = x10;
                                c2 = c;
                            }
                        }
                        while (true) {
                            a = b5;
                            a2 = c - 1;
                            if (b >= a2) {
                                b2 = b;
                                break;
                            }
                            b2 = b;
                            int b7 = T[Td + SA[pa + SA[a2]]];
                            int i5 = b7 & 255;
                            x3 = i5;
                            if (i5 >= v2) {
                                if (x3 != v2) {
                                    b = b2;
                                    c = a2;
                                    b5 = a;
                                } else {
                                    swapElements(SA, a2, SA, x2);
                                    x2--;
                                    b = b2;
                                    c = a2;
                                    b5 = a;
                                }
                            }
                        }
                        a6 = a;
                        b3 = b2;
                        x = x3;
                        c2 = a2;
                    }
                    if (a6 <= x2) {
                        int c4 = b3 - 1;
                        int limit2 = a6 - first2;
                        int s = limit2;
                        StackEntry[] stack2 = stack;
                        int t = b3 - a6;
                        if (limit2 > t) {
                            s = t;
                        }
                        int e = first2;
                        int f = b3 - s;
                        while (s > 0) {
                            swapElements(SA, e, SA, f);
                            s--;
                            e++;
                            f++;
                        }
                        int e2 = x2 - c4;
                        int s2 = e2;
                        int f2 = (last2 - x2) - 1;
                        if (e2 > f2) {
                            s2 = f2;
                        }
                        int e3 = b3;
                        int f3 = last2 - s2;
                        while (s2 > 0) {
                            swapElements(SA, e3, SA, f3);
                            s2--;
                            e3++;
                            f3++;
                        }
                        int a9 = first2 + (b3 - a6);
                        int c5 = last2 - (x2 - c4);
                        int e4 = T[(Td + SA[pa + SA[a9]]) - 1];
                        int b8 = v2 <= (e4 & 255) ? a9 : bzip2DivSufSort.ssSubstringPartition(pa, a9, c5, depth2);
                        int b9 = a9 - first2;
                        if (b9 <= last2 - c5) {
                            if (last2 - c5 <= c5 - b8) {
                                int ssize3 = ssize + 1;
                                int f4 = depth2 + 1;
                                stack2[ssize] = new StackEntry(b8, c5, f4, ssLog(c5 - b8));
                                ssize = ssize3 + 1;
                                stack2[ssize3] = new StackEntry(c5, last2, depth2, limit);
                                last2 = a9;
                                bzip2DivSufSort = this;
                                x4 = x;
                                a3 = limit;
                                stack = stack2;
                            } else if (a9 - first2 <= c5 - b8) {
                                int ssize4 = ssize + 1;
                                stack2[ssize] = new StackEntry(c5, last2, depth2, limit);
                                int ssize5 = ssize4 + 1;
                                int ssize6 = ssLog(c5 - b8);
                                stack2[ssize4] = new StackEntry(b8, c5, depth2 + 1, ssize6);
                                last2 = a9;
                                bzip2DivSufSort = this;
                                ssize = ssize5;
                                x4 = x;
                                a3 = limit;
                                stack = stack2;
                            } else {
                                int ssize7 = ssize + 1;
                                stack2[ssize] = new StackEntry(c5, last2, depth2, limit);
                                ssize = ssize7 + 1;
                                stack2[ssize7] = new StackEntry(first2, a9, depth2, limit);
                                first2 = b8;
                                last2 = c5;
                                depth2++;
                                x4 = x;
                                a3 = ssLog(c5 - b8);
                                stack = stack2;
                                bzip2DivSufSort = this;
                            }
                        } else if (a9 - first2 <= c5 - b8) {
                            int ssize8 = ssize + 1;
                            int v3 = ssLog(c5 - b8);
                            stack2[ssize] = new StackEntry(b8, c5, depth2 + 1, v3);
                            ssize = ssize8 + 1;
                            stack2[ssize8] = new StackEntry(first2, a9, depth2, limit);
                            first2 = c5;
                            bzip2DivSufSort = this;
                            x4 = x;
                            a3 = limit;
                            stack = stack2;
                        } else if (last2 - c5 <= c5 - b8) {
                            int ssize9 = ssize + 1;
                            stack2[ssize] = new StackEntry(first2, a9, depth2, limit);
                            ssize = ssize9 + 1;
                            stack2[ssize9] = new StackEntry(b8, c5, depth2 + 1, ssLog(c5 - b8));
                            first2 = c5;
                            bzip2DivSufSort = this;
                            x4 = x;
                            a3 = limit;
                            stack = stack2;
                        } else {
                            int ssize10 = ssize + 1;
                            stack2[ssize] = new StackEntry(first2, a9, depth2, limit);
                            ssize = ssize10 + 1;
                            stack2[ssize10] = new StackEntry(c5, last2, depth2, limit);
                            first2 = b8;
                            last2 = c5;
                            depth2++;
                            x4 = x;
                            a3 = ssLog(c5 - b8);
                            stack = stack2;
                            bzip2DivSufSort = this;
                        }
                    } else {
                        StackEntry[] stack3 = stack;
                        int limit3 = limit + 1;
                        if ((T[(SA[SA[first2] + pa] + Td) - 1] & 255) >= v2) {
                            bzip2DivSufSort = this;
                        } else {
                            bzip2DivSufSort = this;
                            first2 = bzip2DivSufSort.ssSubstringPartition(pa, first2, last2, depth2);
                            limit3 = ssLog(last2 - first2);
                        }
                        depth2++;
                        x4 = x;
                        a3 = limit3;
                        stack = stack3;
                    }
                }
            }
        }
    }

    private static void ssBlockSwap(int[] array1, int first1, int[] array2, int first2, int size) {
        int i = size;
        int a = first1;
        int b = first2;
        while (i > 0) {
            swapElements(array1, a, array2, b);
            i--;
            a++;
            b++;
        }
    }

    private void ssMergeForward(int pa, int[] buf, int bufoffset, int first, int middle, int last, int depth) {
        int[] SA = this.SA;
        int bufend = ((middle - first) + bufoffset) - 1;
        ssBlockSwap(buf, bufoffset, SA, first, middle - first);
        int t = SA[first];
        int j = first;
        int i = bufoffset;
        int k = middle;
        while (true) {
            int r = ssCompare(pa + buf[i], pa + SA[k], depth);
            if (r < 0) {
                while (true) {
                    int i2 = j + 1;
                    SA[j] = buf[i];
                    if (bufend <= i) {
                        buf[i] = t;
                        return;
                    }
                    int j2 = i + 1;
                    buf[i] = SA[i2];
                    if (buf[j2] >= 0) {
                        i = j2;
                        j = i2;
                        break;
                    } else {
                        i = j2;
                        j = i2;
                    }
                }
            } else if (r > 0) {
                while (true) {
                    int j3 = j + 1;
                    SA[j] = SA[k];
                    int k2 = k + 1;
                    SA[k] = SA[j3];
                    if (last <= k2) {
                        while (i < bufend) {
                            int i3 = j3 + 1;
                            SA[j3] = buf[i];
                            int j4 = i + 1;
                            buf[i] = SA[i3];
                            i = j4;
                            j3 = i3;
                        }
                        SA[j3] = buf[i];
                        buf[i] = t;
                        return;
                    }
                    if (SA[k2] >= 0) {
                        k = k2;
                        j = j3;
                        break;
                    } else {
                        k = k2;
                        j = j3;
                    }
                }
            } else {
                int i4 = SA[k];
                SA[k] = ~i4;
                while (true) {
                    int k3 = j + 1;
                    SA[j] = buf[i];
                    if (bufend <= i) {
                        buf[i] = t;
                        return;
                    }
                    int j5 = i + 1;
                    buf[i] = SA[k3];
                    if (buf[j5] >= 0) {
                        while (true) {
                            int j6 = k3 + 1;
                            SA[k3] = SA[k];
                            int k4 = k + 1;
                            SA[k] = SA[j6];
                            if (last <= k4) {
                                while (j5 < bufend) {
                                    int i5 = j6 + 1;
                                    SA[j6] = buf[j5];
                                    buf[j5] = SA[i5];
                                    j5++;
                                    j6 = i5;
                                }
                                int i6 = buf[j5];
                                SA[j6] = i6;
                                buf[j5] = t;
                                return;
                            }
                            if (SA[k4] >= 0) {
                                k = k4;
                                i = j5;
                                j = j6;
                                break;
                            }
                            k = k4;
                            k3 = j6;
                        }
                    } else {
                        i = j5;
                        j = k3;
                    }
                }
            }
        }
    }

    private void ssMergeBackward(int pa, int[] buf, int bufoffset, int first, int middle, int last, int depth) {
        int p1;
        int p2;
        int i;
        int j;
        int i2;
        int k;
        int i3;
        int k2;
        int i4;
        int j2;
        Bzip2DivSufSort bzip2DivSufSort = this;
        int[] SA = bzip2DivSufSort.SA;
        int bufend = (last - middle) + bufoffset;
        ssBlockSwap(buf, bufoffset, SA, middle, last - middle);
        int x = 0;
        if (buf[bufend - 1] < 0) {
            x = 0 | 1;
            p1 = pa + (~buf[bufend - 1]);
        } else {
            int p12 = bufend - 1;
            p1 = pa + buf[p12];
        }
        if (SA[middle - 1] < 0) {
            x |= 2;
            p2 = pa + (~SA[middle - 1]);
        } else {
            int p22 = middle - 1;
            p2 = pa + SA[p22];
        }
        int t = SA[last - 1];
        int j3 = last - 1;
        int j4 = bufend - 1;
        int k3 = middle - 1;
        while (true) {
            int r = bzip2DivSufSort.ssCompare(p1, p2, depth);
            if (r > 0) {
                if ((x & 1) != 0) {
                    while (true) {
                        i = j3 - 1;
                        SA[j3] = buf[j4];
                        j = j4 - 1;
                        buf[j4] = SA[i];
                        if (buf[j] >= 0) {
                            break;
                        }
                        j4 = j;
                        j3 = i;
                    }
                    x ^= 1;
                    j4 = j;
                    j3 = i;
                }
                int i5 = j3 - 1;
                SA[j3] = buf[j4];
                if (j4 <= bufoffset) {
                    buf[j4] = t;
                    return;
                }
                int j5 = j4 - 1;
                buf[j4] = SA[i5];
                if (buf[j5] < 0) {
                    x |= 1;
                    p1 = pa + (~buf[j5]);
                    j4 = j5;
                    j3 = i5;
                } else {
                    p1 = pa + buf[j5];
                    j4 = j5;
                    j3 = i5;
                }
            } else if (r < 0) {
                if ((x & 2) != 0) {
                    while (true) {
                        i2 = j3 - 1;
                        SA[j3] = SA[k3];
                        k = k3 - 1;
                        SA[k3] = SA[i2];
                        if (SA[k] >= 0) {
                            break;
                        }
                        k3 = k;
                        j3 = i2;
                    }
                    x ^= 2;
                    k3 = k;
                    j3 = i2;
                }
                int j6 = j3 - 1;
                SA[j3] = SA[k3];
                int k4 = k3 - 1;
                SA[k3] = SA[j6];
                if (k4 < first) {
                    while (bufoffset < j4) {
                        int i6 = j6 - 1;
                        SA[j6] = buf[j4];
                        buf[j4] = SA[i6];
                        j4--;
                        j6 = i6;
                    }
                    SA[j6] = buf[j4];
                    buf[j4] = t;
                    return;
                }
                if (SA[k4] < 0) {
                    x |= 2;
                    p2 = pa + (~SA[k4]);
                    k3 = k4;
                    j3 = j6;
                } else {
                    p2 = pa + SA[k4];
                    k3 = k4;
                    j3 = j6;
                }
            } else {
                if ((x & 1) != 0) {
                    while (true) {
                        i4 = j3 - 1;
                        SA[j3] = buf[j4];
                        j2 = j4 - 1;
                        buf[j4] = SA[i4];
                        if (buf[j2] >= 0) {
                            break;
                        }
                        j4 = j2;
                        j3 = i4;
                    }
                    x ^= 1;
                    j4 = j2;
                    j3 = i4;
                }
                int i7 = j3 - 1;
                SA[j3] = ~buf[j4];
                if (j4 <= bufoffset) {
                    buf[j4] = t;
                    return;
                }
                int j7 = j4 - 1;
                buf[j4] = SA[i7];
                if ((x & 2) != 0) {
                    while (true) {
                        i3 = i7 - 1;
                        SA[i7] = SA[k3];
                        k2 = k3 - 1;
                        SA[k3] = SA[i3];
                        if (SA[k2] >= 0) {
                            break;
                        }
                        i7 = i3;
                        k3 = k2;
                    }
                    x ^= 2;
                    i7 = i3;
                    k3 = k2;
                }
                j3 = i7 - 1;
                SA[i7] = SA[k3];
                int k5 = k3 - 1;
                SA[k3] = SA[j3];
                if (k5 < first) {
                    while (bufoffset < j7) {
                        int i8 = j3 - 1;
                        SA[j3] = buf[j7];
                        buf[j7] = SA[i8];
                        j7--;
                        j3 = i8;
                    }
                    SA[j3] = buf[j7];
                    buf[j7] = t;
                    return;
                }
                if (buf[j7] < 0) {
                    x |= 1;
                    p1 = pa + (~buf[j7]);
                } else {
                    p1 = pa + buf[j7];
                }
                if (SA[k5] < 0) {
                    x |= 2;
                    p2 = pa + (~SA[k5]);
                    k3 = k5;
                    j4 = j7;
                    bzip2DivSufSort = this;
                } else {
                    p2 = pa + SA[k5];
                    k3 = k5;
                    j4 = j7;
                    bzip2DivSufSort = this;
                }
            }
        }
    }

    private static int getIDX(int a) {
        return a >= 0 ? a : ~a;
    }

    private void ssMergeCheckEqual(int pa, int depth, int a) {
        int[] SA = this.SA;
        if (SA[a] >= 0 && ssCompare(getIDX(SA[a - 1]) + pa, SA[a] + pa, depth) == 0) {
            SA[a] = ~SA[a];
        }
    }

    private void ssMerge(int pa, int first, int middle, int last, int[] buf, int bufoffset, int bufsize, int depth) {
        int[] SA;
        int last2;
        int[] SA2 = this.SA;
        StackEntry[] stack = new StackEntry[64];
        int first2 = first;
        int middle2 = middle;
        int last3 = last;
        int check = 0;
        int ssize = 0;
        while (true) {
            int check2 = last3 - middle2;
            if (check2 <= bufsize) {
                if (first2 >= middle2 || middle2 >= last3) {
                    SA = SA2;
                    last2 = last3;
                } else {
                    SA = SA2;
                    last2 = last3;
                    ssMergeBackward(pa, buf, bufoffset, first2, middle2, last3, depth);
                }
                if ((check & 1) != 0) {
                    ssMergeCheckEqual(pa, depth, first2);
                }
                if ((check & 2) != 0) {
                    ssMergeCheckEqual(pa, depth, last2);
                }
                if (ssize == 0) {
                    return;
                }
                ssize--;
                StackEntry entry = stack[ssize];
                first2 = entry.a;
                middle2 = entry.b;
                last3 = entry.c;
                int check3 = entry.d;
                check = check3;
                SA2 = SA;
            } else {
                int[] SA3 = SA2;
                int last4 = last3;
                if (middle2 - first2 <= bufsize) {
                    if (first2 < middle2) {
                        ssMergeForward(pa, buf, bufoffset, first2, middle2, last4, depth);
                    }
                    if ((check & 1) != 0) {
                        ssMergeCheckEqual(pa, depth, first2);
                    }
                    if ((check & 2) != 0) {
                        ssMergeCheckEqual(pa, depth, last4);
                    }
                    if (ssize == 0) {
                        return;
                    }
                    ssize--;
                    StackEntry entry2 = stack[ssize];
                    first2 = entry2.a;
                    middle2 = entry2.b;
                    last3 = entry2.c;
                    int check4 = entry2.d;
                    check = check4;
                    SA2 = SA3;
                } else {
                    int m = 0;
                    int len = Math.min(middle2 - first2, last4 - middle2);
                    int half = len >> 1;
                    while (len > 0) {
                        if (ssCompare(getIDX(SA3[middle2 + m + half]) + pa, getIDX(SA3[((middle2 - m) - half) - 1]) + pa, depth) < 0) {
                            m += half + 1;
                            half -= (len & 1) ^ 1;
                        }
                        len = half;
                        half >>= 1;
                    }
                    if (m > 0) {
                        ssBlockSwap(SA3, middle2 - m, SA3, middle2, m);
                        int j = middle2;
                        int i = middle2;
                        int next = 0;
                        if (middle2 + m < last4) {
                            if (SA3[middle2 + m] < 0) {
                                while (SA3[i - 1] < 0) {
                                    i--;
                                }
                                SA3[middle2 + m] = ~SA3[middle2 + m];
                            }
                            j = middle2;
                            while (SA3[j] < 0) {
                                j++;
                            }
                            next = 1;
                        }
                        if (i - first2 <= last4 - j) {
                            int ssize2 = middle2 + m;
                            int half2 = (check & 2) | (next & 1);
                            stack[ssize] = new StackEntry(j, ssize2, last4, half2);
                            middle2 -= m;
                            last3 = i;
                            check &= 1;
                            ssize++;
                            SA2 = SA3;
                        } else {
                            if (i == middle2 && middle2 == j) {
                                next <<= 1;
                            }
                            int ssize3 = (check & 1) | (next & 2);
                            stack[ssize] = new StackEntry(first2, middle2 - m, i, ssize3);
                            first2 = j;
                            middle2 += m;
                            check = (check & 2) | (next & 1);
                            ssize++;
                            last3 = last4;
                            SA2 = SA3;
                        }
                    } else {
                        if ((check & 1) != 0) {
                            ssMergeCheckEqual(pa, depth, first2);
                        }
                        ssMergeCheckEqual(pa, depth, middle2);
                        if ((check & 2) != 0) {
                            ssMergeCheckEqual(pa, depth, last4);
                        }
                        if (ssize == 0) {
                            return;
                        }
                        ssize--;
                        StackEntry entry3 = stack[ssize];
                        first2 = entry3.a;
                        middle2 = entry3.b;
                        last3 = entry3.c;
                        int check5 = entry3.d;
                        check = check5;
                        SA2 = SA3;
                    }
                }
            }
        }
    }

    /* JADX WARN: Incorrect condition in loop: B:12:0x004b */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void subStringSort(int r24, int r25, int r26, int[] r27, int r28, int r29, int r30, boolean r31, int r32) {
        /*
            Method dump skipped, instructions count: 216
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.codec.compression.Bzip2DivSufSort.subStringSort(int, int, int, int[], int, int, int, boolean, int):void");
    }

    private int trGetC(int isa, int isaD, int isaN, int p) {
        return isaD + p < isaN ? this.SA[isaD + p] : this.SA[(((isaD - isa) + p) % (isaN - isa)) + isa];
    }

    private void trFixdown(int isa, int isaD, int isaN, int sa, int i, int size) {
        int[] SA = this.SA;
        int v = SA[sa + i];
        int c = trGetC(isa, isaD, isaN, v);
        while (true) {
            int i2 = (i * 2) + 1;
            int k = i2;
            if (i2 >= size) {
                break;
            }
            int j = k + 1;
            int d = trGetC(isa, isaD, isaN, SA[sa + k]);
            int e = trGetC(isa, isaD, isaN, SA[sa + j]);
            if (d < e) {
                k = j;
                d = e;
            }
            if (d <= c) {
                break;
            }
            SA[sa + i] = SA[sa + k];
            i = k;
        }
        SA[sa + i] = v;
    }

    private void trHeapSort(int isa, int isaD, int isaN, int sa, int size) {
        int m;
        int[] SA = this.SA;
        if (size % 2 == 0) {
            int m2 = size - 1;
            if (trGetC(isa, isaD, isaN, SA[(m2 / 2) + sa]) < trGetC(isa, isaD, isaN, SA[sa + m2])) {
                swapElements(SA, sa + m2, SA, (m2 / 2) + sa);
            }
            m = m2;
        } else {
            m = size;
        }
        for (int i = (m / 2) - 1; i >= 0; i--) {
            trFixdown(isa, isaD, isaN, sa, i, m);
        }
        if (size % 2 == 0) {
            swapElements(SA, sa, SA, sa + m);
            trFixdown(isa, isaD, isaN, sa, 0, m);
        }
        for (int i2 = m - 1; i2 > 0; i2--) {
            int t = SA[sa];
            SA[sa] = SA[sa + i2];
            trFixdown(isa, isaD, isaN, sa, 0, i2);
            SA[sa + i2] = t;
        }
    }

    private void trInsertionSort(int isa, int isaD, int isaN, int first, int last) {
        int r;
        int[] SA = this.SA;
        for (int a = first + 1; a < last; a++) {
            int t = SA[a];
            int b = a - 1;
            do {
                r = trGetC(isa, isaD, isaN, t) - trGetC(isa, isaD, isaN, SA[b]);
                if (r >= 0) {
                    break;
                }
                do {
                    SA[b + 1] = SA[b];
                    b--;
                    if (first > b) {
                        break;
                    }
                } while (SA[b] < 0);
            } while (b >= first);
            if (r == 0) {
                SA[b] = ~SA[b];
            }
            SA[b + 1] = t;
        }
    }

    private static int trLog(int n) {
        return ((-65536) & n) != 0 ? ((-16777216) & n) != 0 ? LOG_2_TABLE[(n >> 24) & 255] + 24 : LOG_2_TABLE[(n >> 16) & bl.dH] : (65280 & n) != 0 ? LOG_2_TABLE[(n >> 8) & 255] + 8 : LOG_2_TABLE[n & 255];
    }

    private int trMedian3(int isa, int isaD, int isaN, int v1, int v2, int v3) {
        int[] SA = this.SA;
        int SA_v1 = trGetC(isa, isaD, isaN, SA[v1]);
        int SA_v2 = trGetC(isa, isaD, isaN, SA[v2]);
        int SA_v3 = trGetC(isa, isaD, isaN, SA[v3]);
        if (SA_v1 > SA_v2) {
            v1 = v2;
            v2 = v1;
            SA_v1 = SA_v2;
            SA_v2 = SA_v1;
        }
        if (SA_v2 > SA_v3) {
            if (SA_v1 > SA_v3) {
                return v1;
            }
            return v3;
        }
        return v2;
    }

    private int trMedian5(int isa, int isaD, int isaN, int v1, int v2, int v3, int v4, int v5) {
        int v22;
        int v32;
        int v42;
        int v52;
        int v12;
        int[] SA = this.SA;
        int SA_v1 = trGetC(isa, isaD, isaN, SA[v1]);
        int SA_v2 = trGetC(isa, isaD, isaN, SA[v2]);
        int SA_v3 = trGetC(isa, isaD, isaN, SA[v3]);
        int SA_v4 = trGetC(isa, isaD, isaN, SA[v4]);
        int SA_v5 = trGetC(isa, isaD, isaN, SA[v5]);
        if (SA_v2 <= SA_v3) {
            v22 = v2;
            v32 = v3;
        } else {
            v22 = v3;
            v32 = v2;
            SA_v2 = SA_v3;
            SA_v3 = SA_v2;
        }
        if (SA_v4 <= SA_v5) {
            v42 = v4;
            v52 = v5;
        } else {
            v42 = v5;
            v52 = v4;
            SA_v4 = SA_v5;
            SA_v5 = SA_v4;
        }
        if (SA_v2 > SA_v4) {
            int temp = v22;
            v42 = temp;
            int SA_vtemp = SA_v2;
            SA_v4 = SA_vtemp;
            int temp2 = v32;
            v32 = v52;
            v52 = temp2;
            int SA_vtemp2 = SA_v3;
            SA_v3 = SA_v5;
            SA_v5 = SA_vtemp2;
        }
        if (SA_v1 <= SA_v3) {
            v12 = v1;
        } else {
            v12 = v32;
            v32 = v1;
            SA_v1 = SA_v3;
            SA_v3 = SA_v1;
        }
        if (SA_v1 > SA_v4) {
            int temp3 = v12;
            v42 = temp3;
            int SA_vtemp3 = SA_v1;
            SA_v4 = SA_vtemp3;
            v32 = v52;
            SA_v3 = SA_v5;
        }
        if (SA_v3 > SA_v4) {
            return v42;
        }
        return v32;
    }

    private int trPivot(int isa, int isaD, int isaN, int first, int last) {
        int t = last - first;
        int middle = first + (t / 2);
        if (t <= 512) {
            if (t <= 32) {
                return trMedian3(isa, isaD, isaN, first, middle, last - 1);
            }
            int t2 = t >> 2;
            return trMedian5(isa, isaD, isaN, first, first + t2, middle, (last - 1) - t2, last - 1);
        }
        int t3 = t >> 3;
        return trMedian3(isa, isaD, isaN, trMedian3(isa, isaD, isaN, first, first + t3, first + (t3 << 1)), trMedian3(isa, isaD, isaN, middle - t3, middle, middle + t3), trMedian3(isa, isaD, isaN, (last - 1) - (t3 << 1), (last - 1) - t3, last - 1));
    }

    private void lsUpdateGroup(int isa, int first, int last) {
        int[] SA = this.SA;
        int a = first;
        while (a < last) {
            if (SA[a] >= 0) {
                int b = a;
                do {
                    SA[SA[a] + isa] = a;
                    a++;
                    if (a >= last) {
                        break;
                    }
                } while (SA[a] >= 0);
                SA[b] = b - a;
                if (last <= a) {
                    return;
                }
            }
            int b2 = a;
            do {
                SA[a] = ~SA[a];
                a++;
            } while (SA[a] < 0);
            do {
                SA[SA[b2] + isa] = a;
                b2++;
            } while (b2 <= a);
            a++;
        }
    }

    private void lsIntroSort(int isa, int isaD, int isaN, int first, int last) {
        int[] SA = this.SA;
        StackEntry[] stack = new StackEntry[64];
        int first2 = first;
        int last2 = last;
        int x = 0;
        int ssize = 0;
        int limit = trLog(last - first);
        while (true) {
            int x2 = last2 - first2;
            if (x2 <= 8) {
                if (1 < last2 - first2) {
                    trInsertionSort(isa, isaD, isaN, first2, last2);
                    lsUpdateGroup(isa, first2, last2);
                } else if (last2 - first2 == 1) {
                    SA[first2] = -1;
                }
                if (ssize == 0) {
                    return;
                }
                ssize--;
                StackEntry entry = stack[ssize];
                first2 = entry.a;
                last2 = entry.b;
                int limit2 = entry.c;
                limit = limit2;
            } else {
                int limit3 = limit - 1;
                if (limit == 0) {
                    int limit4 = first2;
                    trHeapSort(isa, isaD, isaN, limit4, last2 - first2);
                    int a = last2 - 1;
                    int x3 = x;
                    while (first2 < a) {
                        x3 = trGetC(isa, isaD, isaN, SA[a]);
                        int b = a - 1;
                        while (first2 <= b && trGetC(isa, isaD, isaN, SA[b]) == x3) {
                            SA[b] = ~SA[b];
                            b--;
                        }
                        a = b;
                    }
                    lsUpdateGroup(isa, first2, last2);
                    if (ssize == 0) {
                        return;
                    }
                    ssize--;
                    StackEntry entry2 = stack[ssize];
                    first2 = entry2.a;
                    last2 = entry2.b;
                    int limit5 = entry2.c;
                    x = x3;
                    limit = limit5;
                } else {
                    StackEntry[] stack2 = stack;
                    int a2 = trPivot(isa, isaD, isaN, first2, last2);
                    swapElements(SA, first2, SA, a2);
                    int v = trGetC(isa, isaD, isaN, SA[first2]);
                    int b2 = first2 + 1;
                    int x4 = x;
                    while (b2 < last2) {
                        int trGetC = trGetC(isa, isaD, isaN, SA[b2]);
                        x4 = trGetC;
                        if (trGetC != v) {
                            break;
                        } else {
                            b2++;
                        }
                    }
                    int a3 = b2;
                    if (b2 < last2 && x4 < v) {
                        while (true) {
                            b2++;
                            if (b2 >= last2) {
                                break;
                            }
                            int trGetC2 = trGetC(isa, isaD, isaN, SA[b2]);
                            x4 = trGetC2;
                            if (trGetC2 > v) {
                                break;
                            } else if (x4 == v) {
                                swapElements(SA, b2, SA, a3);
                                a3++;
                            }
                        }
                    }
                    int c = last2 - 1;
                    while (b2 < c) {
                        int trGetC3 = trGetC(isa, isaD, isaN, SA[c]);
                        x4 = trGetC3;
                        if (trGetC3 != v) {
                            break;
                        } else {
                            c--;
                        }
                    }
                    int d = c;
                    if (b2 < c && x4 > v) {
                        while (true) {
                            c--;
                            if (b2 >= c) {
                                break;
                            }
                            int trGetC4 = trGetC(isa, isaD, isaN, SA[c]);
                            x4 = trGetC4;
                            if (trGetC4 < v) {
                                break;
                            } else if (x4 == v) {
                                swapElements(SA, c, SA, d);
                                d--;
                            }
                        }
                    }
                    while (b2 < c) {
                        swapElements(SA, b2, SA, c);
                        while (true) {
                            b2++;
                            if (b2 >= c) {
                                break;
                            }
                            int trGetC5 = trGetC(isa, isaD, isaN, SA[b2]);
                            x4 = trGetC5;
                            if (trGetC5 > v) {
                                break;
                            } else if (x4 == v) {
                                swapElements(SA, b2, SA, a3);
                                a3++;
                            }
                        }
                        while (true) {
                            c--;
                            if (b2 < c) {
                                int trGetC6 = trGetC(isa, isaD, isaN, SA[c]);
                                x4 = trGetC6;
                                if (trGetC6 >= v) {
                                    if (x4 == v) {
                                        swapElements(SA, c, SA, d);
                                        d--;
                                    }
                                }
                            }
                        }
                    }
                    if (a3 <= d) {
                        int c2 = b2 - 1;
                        int i = a3 - first2;
                        int s = i;
                        int v2 = b2 - a3;
                        if (i > v2) {
                            s = v2;
                        }
                        int e = first2;
                        int f = b2 - s;
                        while (s > 0) {
                            swapElements(SA, e, SA, f);
                            s--;
                            e++;
                            f++;
                        }
                        int e2 = d - c2;
                        int s2 = e2;
                        int x5 = x4;
                        int x6 = (last2 - d) - 1;
                        if (e2 > x6) {
                            s2 = x6;
                        }
                        int e3 = b2;
                        int f2 = last2 - s2;
                        while (s2 > 0) {
                            swapElements(SA, e3, SA, f2);
                            s2--;
                            e3++;
                            f2++;
                        }
                        int a4 = (b2 - a3) + first2;
                        int a5 = d - c2;
                        int b3 = last2 - a5;
                        int c3 = first2;
                        int c4 = a4 - 1;
                        while (c3 < a4) {
                            SA[isa + SA[c3]] = c4;
                            c3++;
                        }
                        if (b3 < last2) {
                            c3 = a4;
                            int v3 = b3 - 1;
                            while (c3 < b3) {
                                SA[isa + SA[c3]] = v3;
                                c3++;
                            }
                        }
                        int e4 = b3 - a4;
                        if (e4 == 1) {
                            SA[a4] = -1;
                        }
                        if (a4 - first2 <= last2 - b3) {
                            if (first2 < a4) {
                                stack2[ssize] = new StackEntry(b3, last2, limit3, 0);
                                last2 = a4;
                                ssize++;
                                stack = stack2;
                                limit = limit3;
                                x = x5;
                            } else {
                                first2 = b3;
                                stack = stack2;
                                limit = limit3;
                                x = x5;
                            }
                        } else if (b3 < last2) {
                            stack2[ssize] = new StackEntry(first2, a4, limit3, 0);
                            first2 = b3;
                            ssize++;
                            stack = stack2;
                            limit = limit3;
                            x = x5;
                        } else {
                            last2 = a4;
                            stack = stack2;
                            limit = limit3;
                            x = x5;
                        }
                    } else {
                        int x7 = x4;
                        if (ssize == 0) {
                            return;
                        }
                        ssize--;
                        StackEntry entry3 = stack2[ssize];
                        first2 = entry3.a;
                        last2 = entry3.b;
                        int limit6 = entry3.c;
                        stack = stack2;
                        x = x7;
                        limit = limit6;
                    }
                }
            }
        }
    }

    private void lsSort(int isa, int n, int depth) {
        int skip;
        int[] SA = this.SA;
        int isaD = isa + depth;
        while ((-n) < SA[0]) {
            int skip2 = 0;
            int first = 0;
            do {
                int first2 = SA[first];
                if (first2 < 0) {
                    first -= first2;
                    skip2 += first2;
                } else {
                    if (skip2 == 0) {
                        skip = skip2;
                    } else {
                        SA[first + skip2] = skip2;
                        skip = 0;
                    }
                    int last = SA[isa + first2] + 1;
                    lsIntroSort(isa, isaD, isa + n, first, last);
                    first = last;
                    skip2 = skip;
                }
            } while (first < n);
            if (skip2 != 0) {
                SA[first + skip2] = skip2;
            }
            if (n >= isaD - isa) {
                int first3 = isaD - isa;
                isaD += first3;
            } else {
                int first4 = 0;
                do {
                    int t = SA[first4];
                    if (t < 0) {
                        first4 -= t;
                    } else {
                        int last2 = SA[isa + t] + 1;
                        for (int i = first4; i < last2; i++) {
                            SA[SA[i] + isa] = i;
                        }
                        first4 = last2;
                    }
                } while (first4 < n);
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class PartitionResult {
        final int first;
        final int last;

        PartitionResult(int first, int last) {
            this.first = first;
            this.last = last;
        }
    }

    private PartitionResult trPartition(int isa, int isaD, int isaN, int first, int last, int v) {
        int first2;
        int x;
        int x2;
        int x3;
        int last2 = last;
        int[] SA = this.SA;
        int x4 = 0;
        int b = first;
        while (b < last2) {
            int trGetC = trGetC(isa, isaD, isaN, SA[b]);
            x4 = trGetC;
            if (trGetC != v) {
                break;
            }
            b++;
        }
        int a = b;
        if (b < last2 && x4 < v) {
            while (true) {
                b++;
                if (b >= last2) {
                    break;
                }
                int trGetC2 = trGetC(isa, isaD, isaN, SA[b]);
                x4 = trGetC2;
                if (trGetC2 > v) {
                    break;
                }
                if (x4 == v) {
                    swapElements(SA, b, SA, a);
                    a++;
                }
            }
        }
        int c = last2 - 1;
        while (b < c) {
            int trGetC3 = trGetC(isa, isaD, isaN, SA[c]);
            x4 = trGetC3;
            if (trGetC3 != v) {
                break;
            }
            c--;
        }
        int d = c;
        if (b < c && x4 > v) {
            while (true) {
                c--;
                if (b >= c || (x3 = trGetC(isa, isaD, isaN, SA[c])) < v) {
                    break;
                }
                if (x3 == v) {
                    swapElements(SA, c, SA, d);
                    d--;
                }
            }
        }
        while (b < c) {
            swapElements(SA, b, SA, c);
            while (true) {
                b++;
                if (b >= c || (x2 = trGetC(isa, isaD, isaN, SA[b])) > v) {
                    break;
                }
                if (x2 == v) {
                    swapElements(SA, b, SA, a);
                    a++;
                }
            }
            while (true) {
                c--;
                if (b < c && (x = trGetC(isa, isaD, isaN, SA[c])) >= v) {
                    if (x == v) {
                        swapElements(SA, c, SA, d);
                        d--;
                    }
                }
            }
        }
        if (a > d) {
            first2 = first;
        } else {
            int c2 = b - 1;
            int i = a - first;
            int s = i;
            int t = b - a;
            if (i > t) {
                s = t;
            }
            int e = first;
            int f = b - s;
            while (s > 0) {
                swapElements(SA, e, SA, f);
                s--;
                e++;
                f++;
            }
            int i2 = d - c2;
            int s2 = i2;
            int t2 = (last2 - d) - 1;
            if (i2 > t2) {
                s2 = t2;
            }
            int e2 = b;
            int f2 = last2 - s2;
            while (s2 > 0) {
                swapElements(SA, e2, SA, f2);
                s2--;
                e2++;
                f2++;
            }
            first2 = first + (b - a);
            last2 -= d - c2;
        }
        return new PartitionResult(first2, last2);
    }

    private void trCopy(int isa, int isaN, int first, int a, int b, int last, int depth) {
        int[] SA = this.SA;
        int v = b - 1;
        int d = a - 1;
        for (int c = first; c <= d; c++) {
            int i = SA[c] - depth;
            int s = i;
            if (i < 0) {
                s += isaN - isa;
            }
            if (SA[isa + s] == v) {
                d++;
                SA[d] = s;
                SA[isa + s] = d;
            }
        }
        int c2 = last - 1;
        int e = d + 1;
        int d2 = b;
        while (e < d2) {
            int i2 = SA[c2] - depth;
            int s2 = i2;
            if (i2 < 0) {
                s2 += isaN - isa;
            }
            if (SA[isa + s2] == v) {
                d2--;
                SA[d2] = s2;
                SA[isa + s2] = d2;
            }
            c2--;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:107:0x068a, code lost:            r0 = 0;     */
    /* JADX WARN: Code restructure failed: missing block: B:108:0x068b, code lost:            if (r0 >= r8) goto L381;     */
    /* JADX WARN: Code restructure failed: missing block: B:110:0x0692, code lost:            if (r14[r0].d != (-3)) goto L249;     */
    /* JADX WARN: Code restructure failed: missing block: B:111:0x0694, code lost:            lsUpdateGroup(r26, r14[r0].b, r14[r0].c);     */
    /* JADX WARN: Code restructure failed: missing block: B:113:0x06a4, code lost:            r0 = r0 + 1;     */
    /* JADX WARN: Code restructure failed: missing block: B:118:0x06a9, code lost:            return;     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void trIntroSort(int r26, int r27, int r28, int r29, int r30, io.netty.handler.codec.compression.Bzip2DivSufSort.TRBudget r31, int r32) {
        /*
            Method dump skipped, instructions count: 1725
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.codec.compression.Bzip2DivSufSort.trIntroSort(int, int, int, int, int, io.netty.handler.codec.compression.Bzip2DivSufSort$TRBudget, int):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class TRBudget {
        int budget;
        int chance;

        TRBudget(int budget, int chance) {
            this.budget = budget;
            this.chance = chance;
        }

        boolean update(int size, int n) {
            this.budget -= n;
            if (this.budget <= 0) {
                int i = this.chance - 1;
                this.chance = i;
                if (i == 0) {
                    return false;
                }
                this.budget += size;
            }
            return true;
        }
    }

    private void trSort(int isa, int n, int depth) {
        int[] SA = this.SA;
        if ((-n) < SA[0]) {
            TRBudget budget = new TRBudget(n, ((trLog(n) * 2) / 3) + 1);
            int first = 0;
            do {
                int first2 = SA[first];
                if (first2 < 0) {
                    first -= first2;
                } else {
                    int last = SA[isa + first2] + 1;
                    if (1 < last - first) {
                        trIntroSort(isa, isa + depth, isa + n, first, last, budget, n);
                        if (budget.chance == 0) {
                            if (first > 0) {
                                SA[0] = -first;
                            }
                            lsSort(isa, n, depth);
                            return;
                        }
                    }
                    first = last;
                }
            } while (first < n);
        }
    }

    private static int BUCKET_B(int c0, int c1) {
        return (c1 << 8) | c0;
    }

    private static int BUCKET_BSTAR(int c0, int c1) {
        return (c0 << 8) | c1;
    }

    private int sortTypeBstar(int[] bucketA, int[] bucketB) {
        int flag;
        int bufoffset;
        int bufoffset2;
        int[] buf;
        int c1;
        int c0;
        int t0;
        int[] tempbuf;
        int[] buf2;
        int ISAb;
        int m;
        byte[] T = this.T;
        int[] SA = this.SA;
        int n = this.n;
        int[] tempbuf2 = new int[256];
        int i = 1;
        while (true) {
            if (i >= n) {
                break;
            }
            if (T[i - 1] == T[i]) {
                i++;
            } else if ((T[i - 1] & 255) > (T[i] & 255)) {
                flag = 0;
            }
        }
        flag = 1;
        int i2 = n - 1;
        int m2 = n;
        int i3 = T[i2] & 255;
        int ti = i3;
        int i4 = T[0] & 255;
        int t02 = i4;
        int i5 = 1;
        if (i3 < i4 || (T[i2] == T[0] && flag != 0)) {
            if (flag == 0) {
                int BUCKET_BSTAR = BUCKET_BSTAR(ti, t02);
                bucketB[BUCKET_BSTAR] = bucketB[BUCKET_BSTAR] + 1;
                m2--;
                SA[m2] = i2;
            } else {
                int BUCKET_B = BUCKET_B(ti, t02);
                bucketB[BUCKET_B] = bucketB[BUCKET_B] + 1;
            }
            i2--;
            while (i2 >= 0) {
                int i6 = T[i2] & 255;
                ti = i6;
                int ti1 = T[i2 + 1] & 255;
                if (i6 > ti1) {
                    break;
                }
                int BUCKET_B2 = BUCKET_B(ti, ti1);
                bucketB[BUCKET_B2] = bucketB[BUCKET_B2] + 1;
                i2--;
            }
        }
        while (i2 >= 0) {
            do {
                int i7 = T[i2] & 255;
                bucketA[i7] = bucketA[i7] + 1;
                i2--;
                if (i2 < 0) {
                    break;
                }
            } while ((T[i2] & 255) >= (T[i2 + 1] & 255));
            if (i2 >= 0) {
                int BUCKET_BSTAR2 = BUCKET_BSTAR(T[i2] & 255, T[i2 + 1] & 255);
                bucketB[BUCKET_BSTAR2] = bucketB[BUCKET_BSTAR2] + 1;
                m2--;
                SA[m2] = i2;
                i2--;
                while (i2 >= 0) {
                    int ti2 = T[i2] & 255;
                    int ti12 = T[i2 + 1] & 255;
                    if (ti2 <= ti12) {
                        int BUCKET_B3 = BUCKET_B(ti2, ti12);
                        bucketB[BUCKET_B3] = bucketB[BUCKET_B3] + 1;
                        i2--;
                    }
                }
            }
        }
        int m3 = n - m2;
        if (m3 == 0) {
            for (int i8 = 0; i8 < n; i8++) {
                SA[i8] = i8;
            }
            return 0;
        }
        int c02 = 0;
        int c12 = -1;
        int j = 0;
        while (c02 < 256) {
            int t = bucketA[c02] + c12;
            bucketA[c02] = c12 + j;
            int i9 = bucketB[BUCKET_B(c02, c02)] + t;
            for (int c13 = c02 + 1; c13 < 256; c13++) {
                j += bucketB[BUCKET_BSTAR(c02, c13)];
                bucketB[(c02 << 8) | c13] = j;
                i9 += bucketB[BUCKET_B(c02, c13)];
            }
            c02++;
            c12 = i9;
        }
        int PAb = n - m3;
        int ISAb2 = m3;
        for (int i10 = m3 - 2; i10 >= 0; i10--) {
            int t2 = SA[PAb + i10];
            int c03 = T[t2] & 255;
            int c14 = T[t2 + 1] & 255;
            int BUCKET_BSTAR3 = BUCKET_BSTAR(c03, c14);
            int i11 = bucketB[BUCKET_BSTAR3] - 1;
            bucketB[BUCKET_BSTAR3] = i11;
            SA[i11] = i10;
        }
        int t3 = SA[(PAb + m3) - 1];
        int c04 = T[t3] & 255;
        int c15 = T[t3 + 1] & 255;
        int BUCKET_BSTAR4 = BUCKET_BSTAR(c04, c15);
        int i12 = bucketB[BUCKET_BSTAR4] - 1;
        bucketB[BUCKET_BSTAR4] = i12;
        SA[i12] = m3 - 1;
        int bufsize = n - (m3 * 2);
        if (bufsize > 256) {
            bufoffset = m3;
            bufoffset2 = bufsize;
            buf = SA;
        } else {
            bufoffset = 0;
            bufoffset2 = 256;
            buf = tempbuf2;
        }
        int j2 = m3;
        int c05 = 255;
        while (j2 > 0) {
            int c16 = 255;
            int j3 = j2;
            while (c05 < c16) {
                int i13 = bucketB[BUCKET_BSTAR(c05, c16)];
                if (i5 >= j3 - i13) {
                    c1 = c16;
                    c0 = c05;
                    t0 = t02;
                    tempbuf = tempbuf2;
                    buf2 = buf;
                    ISAb = ISAb2;
                    m = m3;
                } else {
                    c1 = c16;
                    int[] iArr = buf;
                    c0 = c05;
                    int c06 = bufoffset;
                    tempbuf = tempbuf2;
                    ISAb = ISAb2;
                    int ISAb3 = bufoffset2;
                    buf2 = buf;
                    m = m3;
                    t0 = t02;
                    subStringSort(PAb, i13, j3, iArr, c06, ISAb3, 2, SA[i13] == m3 + (-1) ? i5 : 0, n);
                }
                j3 = i13;
                c16 = c1 - 1;
                ISAb2 = ISAb;
                m3 = m;
                t02 = t0;
                c05 = c0;
                tempbuf2 = tempbuf;
                buf = buf2;
                i5 = 1;
            }
            c05--;
            j2 = j3;
            tempbuf2 = tempbuf2;
            buf = buf;
            i5 = 1;
        }
        int ISAb4 = ISAb2;
        int m4 = m3;
        int i14 = m4 - 1;
        while (i14 >= 0) {
            if (SA[i14] >= 0) {
                int j4 = i14;
                do {
                    SA[ISAb4 + SA[i14]] = i14;
                    i14--;
                    if (i14 < 0) {
                        break;
                    }
                } while (SA[i14] >= 0);
                SA[i14 + 1] = i14 - j4;
                if (i14 <= 0) {
                    break;
                }
            }
            int j5 = i14;
            do {
                int i15 = ~SA[i14];
                SA[i14] = i15;
                SA[ISAb4 + i15] = j5;
                i14--;
            } while (SA[i14] < 0);
            SA[ISAb4 + SA[i14]] = j5;
            i14--;
        }
        trSort(ISAb4, m4, 1);
        int i16 = n - 1;
        int j6 = m4;
        int j7 = T[i16];
        if ((j7 & 255) < (T[0] & 255) || (T[i16] == T[0] && flag != 0)) {
            if (flag == 0) {
                j6--;
                SA[SA[ISAb4 + j6]] = i16;
            }
            i16--;
            while (i16 >= 0 && (T[i16] & 255) <= (T[i16 + 1] & 255)) {
                i16--;
            }
        }
        while (i16 >= 0) {
            i16--;
            while (i16 >= 0 && (T[i16] & 255) >= (T[i16 + 1] & 255)) {
                i16--;
            }
            if (i16 >= 0) {
                j6--;
                SA[SA[ISAb4 + j6]] = i16;
                i16--;
                while (i16 >= 0 && (T[i16] & 255) <= (T[i16 + 1] & 255)) {
                    i16--;
                }
            }
        }
        int i17 = n - 1;
        int k = m4 - 1;
        for (int c07 = 255; c07 >= 0; c07--) {
            for (int c17 = 255; c07 < c17; c17--) {
                int t4 = i17 - bucketB[BUCKET_B(c07, c17)];
                bucketB[BUCKET_B(c07, c17)] = i17 + 1;
                i17 = t4;
                int j8 = bucketB[BUCKET_BSTAR(c07, c17)];
                while (j8 <= k) {
                    SA[i17] = SA[k];
                    i17--;
                    k--;
                }
            }
            int t5 = i17 - bucketB[BUCKET_B(c07, c07)];
            bucketB[BUCKET_B(c07, c07)] = i17 + 1;
            if (c07 < 255) {
                bucketB[BUCKET_BSTAR(c07, c07 + 1)] = t5 + 1;
            }
            i17 = bucketA[c07];
        }
        return m4;
    }

    private int constructBWT(int[] bucketA, int[] bucketB) {
        byte[] T = this.T;
        int[] SA = this.SA;
        int n = this.n;
        int t = 0;
        int c2 = 0;
        int orig = -1;
        for (int c1 = 254; c1 >= 0; c1--) {
            int i = bucketB[BUCKET_BSTAR(c1, c1 + 1)];
            t = 0;
            c2 = -1;
            for (int j = bucketA[c1 + 1]; i <= j; j--) {
                int s = SA[j];
                if (s >= 0) {
                    int s2 = s - 1;
                    if (s2 < 0) {
                        s2 = n - 1;
                    }
                    int c0 = T[s2] & 255;
                    if (c0 <= c1) {
                        SA[j] = ~s;
                        if (s2 > 0 && (T[s2 - 1] & 255) > c0) {
                            s2 = ~s2;
                        }
                        if (c2 == c0) {
                            t--;
                            SA[t] = s2;
                        } else {
                            if (c2 >= 0) {
                                bucketB[BUCKET_B(c2, c1)] = t;
                            }
                            c2 = c0;
                            int i2 = bucketB[BUCKET_B(c0, c1)] - 1;
                            t = i2;
                            SA[i2] = s2;
                        }
                    }
                } else {
                    SA[j] = ~s;
                }
            }
        }
        for (int i3 = 0; i3 < n; i3++) {
            int s3 = SA[i3];
            int s1 = s3;
            if (s3 >= 0) {
                int s4 = s3 - 1;
                if (s4 < 0) {
                    s4 = n - 1;
                }
                int c02 = T[s4] & 255;
                if (c02 >= (T[s4 + 1] & 255)) {
                    if (s4 > 0 && (T[s4 - 1] & 255) < c02) {
                        s4 = ~s4;
                    }
                    if (c02 == c2) {
                        t++;
                        SA[t] = s4;
                    } else {
                        if (c2 != -1) {
                            bucketA[c2] = t;
                        }
                        c2 = c02;
                        int i4 = bucketA[c02] + 1;
                        t = i4;
                        SA[i4] = s4;
                    }
                }
            } else {
                s1 = ~s1;
            }
            if (s1 == 0) {
                SA[i3] = T[n - 1];
                orig = i3;
            } else {
                SA[i3] = T[s1 - 1];
            }
        }
        return orig;
    }

    public int bwt() {
        int[] SA = this.SA;
        byte[] T = this.T;
        int n = this.n;
        int[] bucketA = new int[256];
        int[] bucketB = new int[65536];
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            SA[0] = T[0];
            return 0;
        }
        int m = sortTypeBstar(bucketA, bucketB);
        if (m <= 0) {
            return 0;
        }
        return constructBWT(bucketA, bucketB);
    }
}
