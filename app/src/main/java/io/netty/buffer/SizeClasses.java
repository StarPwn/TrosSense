package io.netty.buffer;

import java.lang.reflect.Array;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class SizeClasses implements SizeClassesMetric {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int LOG2DELTA_IDX = 2;
    private static final int LOG2GROUP_IDX = 1;
    private static final int LOG2_DELTA_LOOKUP_IDX = 6;
    private static final int LOG2_MAX_LOOKUP_SIZE = 12;
    static final int LOG2_QUANTUM = 4;
    private static final int LOG2_SIZE_CLASS_GROUP = 2;
    private static final int NDELTA_IDX = 3;
    private static final int PAGESIZE_IDX = 4;
    private static final int SUBPAGE_IDX = 5;
    private static final byte no = 0;
    private static final byte yes = 1;
    final int chunkSize;
    final int directMemoryCacheAlignment;
    final int lookupMaxSize;
    final int nPSizes;
    final int nSizes;
    final int nSubpages;
    private final int[] pageIdx2sizeTab;
    final int pageShifts;
    final int pageSize;
    private final int[] size2idxTab;
    private final int[] sizeIdx2sizeTab;
    final int smallMaxSizeIdx;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SizeClasses(int pageSize, int pageShifts, int chunkSize, int directMemoryCacheAlignment) {
        char c = 4;
        int group = ((PoolThreadCache.log2(chunkSize) - 4) - 2) + 1;
        short[][] sizeClasses = (short[][]) Array.newInstance((Class<?>) Short.TYPE, group << 2, 7);
        int normalMaxSize = -1;
        int nSizes = 0;
        int size = 0;
        int log2Delta = 4;
        int nDelta = 0;
        while (nDelta < 4) {
            short[] sizeClass = newSizeClass(nSizes, 4, 4, nDelta, pageShifts);
            sizeClasses[nSizes] = sizeClass;
            size = sizeOf(sizeClass, directMemoryCacheAlignment);
            nDelta++;
            nSizes++;
        }
        int log2Group = 4 + 2;
        while (size < chunkSize) {
            int nDelta2 = 1;
            while (nDelta2 <= 4 && size < chunkSize) {
                short[] sizeClass2 = newSizeClass(nSizes, log2Group, log2Delta, nDelta2, pageShifts);
                sizeClasses[nSizes] = sizeClass2;
                int sizeOf = sizeOf(sizeClass2, directMemoryCacheAlignment);
                normalMaxSize = sizeOf;
                size = sizeOf;
                nDelta2++;
                nSizes++;
            }
            log2Group++;
            log2Delta++;
        }
        if (chunkSize != normalMaxSize) {
            throw new AssertionError();
        }
        int smallMaxSizeIdx = 0;
        int lookupMaxSize = 0;
        int nPSizes = 0;
        int nSubpages = 0;
        int idx = 0;
        while (idx < nSizes) {
            int group2 = group;
            short[] sz = sizeClasses[idx];
            int normalMaxSize2 = normalMaxSize;
            if (sz[c] == 1) {
                nPSizes++;
            }
            if (sz[5] == 1) {
                nSubpages++;
                int nSubpages2 = idx;
                smallMaxSizeIdx = nSubpages2;
            }
            if (sz[6] != 0) {
                lookupMaxSize = sizeOf(sz, directMemoryCacheAlignment);
            }
            idx++;
            group = group2;
            normalMaxSize = normalMaxSize2;
            c = 4;
        }
        int normalMaxSize3 = nPSizes;
        this.smallMaxSizeIdx = smallMaxSizeIdx;
        this.lookupMaxSize = lookupMaxSize;
        this.nPSizes = normalMaxSize3;
        this.nSubpages = nSubpages;
        this.nSizes = nSizes;
        this.pageSize = pageSize;
        this.pageShifts = pageShifts;
        this.chunkSize = chunkSize;
        this.directMemoryCacheAlignment = directMemoryCacheAlignment;
        this.sizeIdx2sizeTab = newIdx2SizeTab(sizeClasses, nSizes, directMemoryCacheAlignment);
        this.pageIdx2sizeTab = newPageIdx2sizeTab(sizeClasses, nSizes, normalMaxSize3, directMemoryCacheAlignment);
        this.size2idxTab = newSize2idxTab(lookupMaxSize, sizeClasses);
    }

    private static short[] newSizeClass(int index, int log2Group, int log2Delta, int nDelta, int pageShifts) {
        short isMultiPageSize;
        if (log2Delta >= pageShifts) {
            isMultiPageSize = 1;
        } else {
            int pageSize = 1 << pageShifts;
            int size = calculateSize(log2Group, nDelta, log2Delta);
            isMultiPageSize = size == (size / pageSize) * pageSize ? (short) 1 : (short) 0;
        }
        int log2Ndelta = nDelta == 0 ? 0 : PoolThreadCache.log2(nDelta);
        byte remove = (1 << log2Ndelta) < nDelta ? (byte) 1 : (byte) 0;
        int log2Size = log2Delta + log2Ndelta == log2Group ? log2Group + 1 : log2Group;
        if (log2Size == log2Group) {
            remove = 1;
        }
        short isSubpage = log2Size < pageShifts + 2 ? (short) 1 : (short) 0;
        int log2DeltaLookup = (log2Size < 12 || (log2Size == 12 && remove == 0)) ? log2Delta : 0;
        return new short[]{(short) index, (short) log2Group, (short) log2Delta, (short) nDelta, isMultiPageSize, isSubpage, (short) log2DeltaLookup};
    }

    private static int[] newIdx2SizeTab(short[][] sizeClasses, int nSizes, int directMemoryCacheAlignment) {
        int[] sizeIdx2sizeTab = new int[nSizes];
        for (int i = 0; i < nSizes; i++) {
            short[] sizeClass = sizeClasses[i];
            sizeIdx2sizeTab[i] = sizeOf(sizeClass, directMemoryCacheAlignment);
        }
        return sizeIdx2sizeTab;
    }

    private static int calculateSize(int log2Group, int nDelta, int log2Delta) {
        return (1 << log2Group) + (nDelta << log2Delta);
    }

    private static int sizeOf(short[] sizeClass, int directMemoryCacheAlignment) {
        int size = calculateSize(sizeClass[1], sizeClass[3], sizeClass[2]);
        return alignSizeIfNeeded(size, directMemoryCacheAlignment);
    }

    private static int[] newPageIdx2sizeTab(short[][] sizeClasses, int nSizes, int nPSizes, int directMemoryCacheAlignment) {
        int[] pageIdx2sizeTab = new int[nPSizes];
        int pageIdx = 0;
        for (int i = 0; i < nSizes; i++) {
            short[] sizeClass = sizeClasses[i];
            if (sizeClass[4] == 1) {
                pageIdx2sizeTab[pageIdx] = sizeOf(sizeClass, directMemoryCacheAlignment);
                pageIdx++;
            }
        }
        return pageIdx2sizeTab;
    }

    private static int[] newSize2idxTab(int lookupMaxSize, short[][] sizeClasses) {
        int[] size2idxTab = new int[lookupMaxSize >> 4];
        int idx = 0;
        int size = 0;
        int i = 0;
        while (size <= lookupMaxSize) {
            int idx2 = 1 << (sizeClasses[i][2] - 4);
            while (size <= lookupMaxSize) {
                int times = idx2 - 1;
                if (idx2 > 0) {
                    int idx3 = idx + 1;
                    size2idxTab[idx] = i;
                    size = (idx3 + 1) << 4;
                    idx = idx3;
                    idx2 = times;
                }
            }
            i++;
        }
        return size2idxTab;
    }

    @Override // io.netty.buffer.SizeClassesMetric
    public int sizeIdx2size(int sizeIdx) {
        return this.sizeIdx2sizeTab[sizeIdx];
    }

    @Override // io.netty.buffer.SizeClassesMetric
    public int sizeIdx2sizeCompute(int sizeIdx) {
        int group = sizeIdx >> 2;
        int mod = sizeIdx & 3;
        int groupSize = group == 0 ? 0 : 32 << group;
        int shift = group == 0 ? 1 : group;
        int lgDelta = (shift + 4) - 1;
        int modSize = (mod + 1) << lgDelta;
        return groupSize + modSize;
    }

    @Override // io.netty.buffer.SizeClassesMetric
    public long pageIdx2size(int pageIdx) {
        return this.pageIdx2sizeTab[pageIdx];
    }

    @Override // io.netty.buffer.SizeClassesMetric
    public long pageIdx2sizeCompute(int pageIdx) {
        int group = pageIdx >> 2;
        int mod = pageIdx & 3;
        long groupSize = group == 0 ? 0L : (1 << ((this.pageShifts + 2) - 1)) << group;
        int shift = group == 0 ? 1 : group;
        int log2Delta = (this.pageShifts + shift) - 1;
        int modSize = (mod + 1) << log2Delta;
        return modSize + groupSize;
    }

    @Override // io.netty.buffer.SizeClassesMetric
    public int size2SizeIdx(int size) {
        if (size == 0) {
            return 0;
        }
        if (size > this.chunkSize) {
            return this.nSizes;
        }
        int size2 = alignSizeIfNeeded(size, this.directMemoryCacheAlignment);
        if (size2 <= this.lookupMaxSize) {
            return this.size2idxTab[(size2 - 1) >> 4];
        }
        int x = PoolThreadCache.log2((size2 << 1) - 1);
        int shift = x >= 7 ? x - 6 : 0;
        int group = shift << 2;
        int log2Delta = x >= 7 ? (x - 2) - 1 : 4;
        int mod = ((size2 - 1) >> log2Delta) & 3;
        return group + mod;
    }

    @Override // io.netty.buffer.SizeClassesMetric
    public int pages2pageIdx(int pages) {
        return pages2pageIdxCompute(pages, false);
    }

    @Override // io.netty.buffer.SizeClassesMetric
    public int pages2pageIdxFloor(int pages) {
        return pages2pageIdxCompute(pages, true);
    }

    private int pages2pageIdxCompute(int pages, boolean floor) {
        int pageSize = pages << this.pageShifts;
        if (pageSize > this.chunkSize) {
            return this.nPSizes;
        }
        int x = PoolThreadCache.log2((pageSize << 1) - 1);
        int shift = x < this.pageShifts + 2 ? 0 : x - (this.pageShifts + 2);
        int group = shift << 2;
        int log2Delta = x < (this.pageShifts + 2) + 1 ? this.pageShifts : (x - 2) - 1;
        int mod = ((pageSize - 1) >> log2Delta) & 3;
        int pageIdx = group + mod;
        if (floor && this.pageIdx2sizeTab[pageIdx] > (pages << this.pageShifts)) {
            return pageIdx - 1;
        }
        return pageIdx;
    }

    private static int alignSizeIfNeeded(int size, int directMemoryCacheAlignment) {
        if (directMemoryCacheAlignment <= 0) {
            return size;
        }
        int delta = (directMemoryCacheAlignment - 1) & size;
        return delta == 0 ? size : (size + directMemoryCacheAlignment) - delta;
    }

    @Override // io.netty.buffer.SizeClassesMetric
    public int normalizeSize(int size) {
        if (size == 0) {
            return this.sizeIdx2sizeTab[0];
        }
        int size2 = alignSizeIfNeeded(size, this.directMemoryCacheAlignment);
        if (size2 <= this.lookupMaxSize) {
            int ret = this.sizeIdx2sizeTab[this.size2idxTab[(size2 - 1) >> 4]];
            if (ret != normalizeSizeCompute(size2)) {
                throw new AssertionError();
            }
            return ret;
        }
        return normalizeSizeCompute(size2);
    }

    private static int normalizeSizeCompute(int size) {
        int x = PoolThreadCache.log2((size << 1) - 1);
        int log2Delta = x < 7 ? 4 : (x - 2) - 1;
        int delta = 1 << log2Delta;
        int delta_mask = delta - 1;
        return (size + delta_mask) & (~delta_mask);
    }
}
