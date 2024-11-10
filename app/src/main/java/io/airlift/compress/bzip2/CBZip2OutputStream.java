package io.airlift.compress.bzip2;

import androidx.core.app.FrameMetricsAggregator;
import androidx.core.view.InputDeviceCompat;
import com.google.android.material.internal.ViewUtils;
import com.trossense.bl;
import io.netty.handler.codec.http2.Http2CodecUtil;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import org.cloudburstmc.netty.channel.raknet.RakConstants;

/* loaded from: classes.dex */
class CBZip2OutputStream extends OutputStream {
    private static final int CLEAR_MASK = -2097153;
    private static final int DEPTH_THRESH = 10;
    private static final int GREATER_ICOST = 15;
    private static final int LESSER_ICOST = 0;
    private static final int MAX_BLOCK_SIZE = 9;
    private static final int NUM_OVERSHOOT_BYTES = 20;
    private static final int N_ITERS = 4;
    private static final int QSORT_STACK_SIZE = 1000;
    private static final int SET_MASK = 2097152;
    private static final int SMALL_THRESH = 20;
    private static final int WORK_FACTOR = 30;
    private int allowableBlockSize;
    private boolean blockRandomised;
    private final int blockSize100k;
    private int bsBuff;
    private int bsLive;
    private int combinedCRC;
    private final Crc32 crc32;
    private int currentChar;
    private Data data;
    private boolean firstAttempt;
    private int last;
    private int nInUse;
    private int nMTF;
    private int origPtr;
    private OutputStream out;
    private int runLength;
    private int workDone;
    private int workLimit;
    private static final int[] R_NUMS = {619, 720, 127, 481, 931, 816, 813, bl.c5, 566, bl.di, 985, 724, bl.cD, 454, 863, 491, 741, bl.dd, 949, bl.cM, 733, 859, 335, 708, 621, 574, 73, 654, 730, 472, 419, 436, bl.dO, 496, 867, bl.cI, 399, 680, 480, 51, 878, 465, 811, bl.b4, 869, 675, 611, 697, 867, 561, 862, 687, 507, bl.dT, 482, bl.bq, 807, 591, 733, 623, bl.bL, bl.c_, 59, 379, 684, 877, 625, bl.b4, 643, 105, bl.b5, 607, 520, 932, 727, 476, 693, 425, bl.b9, 647, 73, 122, 335, 530, 442, 853, 695, bl.dk, 445, 515, 909, 545, 703, 919, 874, 474, 882, 500, 594, 612, 641, 801, bl.cS, bl.bX, 819, 984, 589, InputDeviceCompat.SOURCE_DPAD, 495, 799, bl.bW, 604, 958, 533, bl.cT, 400, 386, 867, 600, 782, 382, 596, 414, bl.b6, 516, 375, 682, 485, 911, bl.dM, 98, 553, bl.bY, 354, 666, 933, 424, 341, 533, 870, bl.cZ, 730, 475, bl.ck, 263, 647, 537, 686, 600, bl.cW, 469, 68, 770, 919, bl.co, 373, bl.d4, 822, 808, bl.cE, bl.ci, 943, 795, 384, 383, 461, 404, 758, 839, 887, 715, 67, 618, bl.dM, bl.cC, 918, 873, 777, 604, 560, 951, bl.bV, 578, 722, 79, 804, 96, 409, 713, 940, 652, 934, 970, 447, 318, 353, 859, 672, 112, 785, 645, 863, 803, 350, bl.bA, 93, 354, 99, 820, 908, 609, 772, bl.bP, bl.dK, 580, bl.ci, 79, 626, 630, 742, 653, bl.dS, 762, 623, 680, 81, 927, 626, 789, bl.bm, 411, 521, 938, 300, 821, 78, 343, bl.b_, 128, 250, bl.b5, 774, 972, bl.dL, 999, 639, 495, 78, 352, 126, 857, 956, 358, 619, 580, 124, 737, 594, 701, 612, 669, 112, bl.bv, 694, 363, 992, 809, 743, bl.b3, 974, 944, 375, 748, 52, 600, 747, 642, bl.cg, 862, 81, 344, 805, 988, 739, FrameMetricsAggregator.EVERY_DURATION, 655, 814, 334, bl.dk, 515, 897, 955, 664, 981, 649, 113, 974, 459, 893, bl.c0, 433, 837, 553, bl.dE, 926, bl.db, 102, 654, 459, 51, 686, 754, 806, 760, 493, 403, 415, 394, 687, 700, 946, 670, 656, 610, 738, 392, 760, 799, 887, 653, 978, 321, RakConstants.MINIMUM_MTU_SIZE, 617, 626, 502, 894, 679, bl.de, 440, 680, 879, bl.cs, 572, 640, 724, 926, 56, bl.cC, 700, 707, bl.bM, 457, 449, 797, bl.ct, 791, 558, 945, 679, bl.d7, 59, 87, 824, 713, 663, 412, 693, 342, 606, bl.bv, 108, 571, 364, 631, bl.cK, bl.b9, 643, bl.ed, 329, 343, 97, 430, 751, 497, bl.en, 983, 374, 822, 928, bl.bB, bl.cE, 73, 263, 980, 736, 876, 478, 430, bl.ee, bl.b5, 514, 364, 692, 829, 82, 855, 953, 676, bl.dh, 369, 970, bl.d4, 750, 807, 827, bl.bL, 790, bl.dY, 923, 804, 378, bl.cN, 828, 592, bl.dR, 565, 555, 710, 82, 896, 831, 547, bl.dx, 524, 462, bl.d3, 465, 502, 56, 661, 821, 976, 991, 658, 869, 905, 758, 745, bl.cr, ViewUtils.EDGE_TO_EDGE_FLAGS, 550, 608, 933, 378, bl.dW, bl.cN, 979, 792, 961, 61, 688, 793, 644, 986, 403, 106, 366, 905, 644, 372, 567, 466, 434, 645, bl.cI, 389, 550, 919, bl.bw, 780, 773, 635, 389, 707, 100, 626, 958, bl.b0, 504, 920, bl.ca, bl.cr, 713, 857, bl.dB, bl.cB, 50, 668, 108, 645, 990, 626, bl.cv, 510, 357, 358, 850, 858, 364, 936, 638};
    private static final int[] INCS = {1, 4, 13, 40, 121, 364, 1093, 3280, 9841, 29524, 88573, 265720, 797161, 2391484};

    private static void hbMakeCodeLengths(final byte[] len, final int[] freq, final Data dat, final int alphaSize, final int maxLen) {
        int i;
        int i2;
        int yy;
        int[] heap = dat.heap;
        int[] weight = dat.weight;
        int[] parent = dat.parent;
        int i3 = alphaSize;
        while (true) {
            i = -1;
            i3--;
            i2 = 1;
            if (i3 < 0) {
                break;
            }
            int i4 = i3 + 1;
            if (freq[i3] != 0) {
                i2 = freq[i3];
            }
            weight[i4] = i2 << 8;
        }
        int i5 = 1;
        while (i5 != 0) {
            i5 = 0;
            int nNodes = alphaSize;
            int nHeap = 0;
            heap[0] = 0;
            weight[0] = 0;
            parent[0] = -2;
            for (int i6 = 1; i6 <= alphaSize; i6++) {
                parent[i6] = i;
                nHeap++;
                heap[nHeap] = i6;
                int zz = nHeap;
                int tmp = heap[zz];
                while (weight[tmp] < weight[heap[zz >> 1]]) {
                    heap[zz] = heap[zz >> 1];
                    zz >>= 1;
                }
                heap[zz] = tmp;
            }
            while (nHeap > i2) {
                int n1 = heap[i2];
                heap[i2] = heap[nHeap];
                int nHeap2 = nHeap - 1;
                int zz2 = 1;
                int tmp2 = heap[i2];
                while (true) {
                    int yy2 = zz2 << 1;
                    if (yy2 > nHeap2) {
                        break;
                    }
                    if (yy2 < nHeap2 && weight[heap[yy2 + 1]] < weight[heap[yy2]]) {
                        yy2++;
                    }
                    if (weight[tmp2] < weight[heap[yy2]]) {
                        break;
                    }
                    heap[zz2] = heap[yy2];
                    zz2 = yy2;
                    i2 = 1;
                }
                heap[zz2] = tmp2;
                int n2 = heap[i2];
                heap[i2] = heap[nHeap2];
                int nHeap3 = nHeap2 - 1;
                int zz3 = 1;
                int tmp3 = heap[i2];
                while (true) {
                    int yy3 = zz3 << 1;
                    if (yy3 > nHeap3) {
                        break;
                    }
                    if (yy3 < nHeap3 && weight[heap[yy3 + 1]] < weight[heap[yy3]]) {
                        yy = yy3 + 1;
                    } else {
                        yy = yy3;
                    }
                    if (weight[tmp3] < weight[heap[yy]]) {
                        break;
                    }
                    heap[zz3] = heap[yy];
                    zz3 = yy;
                }
                heap[zz3] = tmp3;
                nNodes++;
                parent[n1] = nNodes;
                parent[n2] = nNodes;
                int weightN1 = weight[n1];
                int weightN2 = weight[n2];
                weight[nNodes] = (Math.max(weightN1 & 255, weightN2 & 255) + 1) | ((weightN1 & InputDeviceCompat.SOURCE_ANY) + (weightN2 & InputDeviceCompat.SOURCE_ANY));
                parent[nNodes] = -1;
                int nHeap4 = nHeap3 + 1;
                heap[nHeap4] = nNodes;
                int zz4 = nHeap4;
                int zz5 = heap[zz4];
                int weightTmp = weight[zz5];
                while (weightTmp < weight[heap[zz4 >> 1]]) {
                    heap[zz4] = heap[zz4 >> 1];
                    zz4 >>= 1;
                }
                heap[zz4] = zz5;
                nHeap = nHeap4;
                i2 = 1;
            }
            for (int i7 = 1; i7 <= alphaSize; i7++) {
                int j = 0;
                int k = i7;
                while (true) {
                    int parentK = parent[k];
                    if (parentK < 0) {
                        break;
                    }
                    k = parentK;
                    j++;
                }
                len[i7 - 1] = (byte) j;
                if (j > maxLen) {
                    i5 = 1;
                }
            }
            if (i5 != 0) {
                for (int i8 = 1; i8 < alphaSize; i8++) {
                    int j2 = weight[i8] >> 8;
                    weight[i8] = ((j2 >> 1) + 1) << 8;
                }
            }
            i2 = 1;
            i = -1;
        }
    }

    public CBZip2OutputStream(final OutputStream out) throws IOException {
        this(out, 9);
    }

    private CBZip2OutputStream(final OutputStream out, final int blockSize) throws IOException {
        this.crc32 = new Crc32();
        this.currentChar = -1;
        if (blockSize < 1) {
            throw new IllegalArgumentException("blockSize(" + blockSize + ") < 1");
        }
        if (blockSize > 9) {
            throw new IllegalArgumentException("blockSize(" + blockSize + ") > 9");
        }
        this.blockSize100k = blockSize;
        this.out = out;
        init();
    }

    @Override // java.io.OutputStream
    public void write(final int b) throws IOException {
        if (this.out != null) {
            write0(b);
            return;
        }
        throw new IOException("closed");
    }

    private void writeRun() throws IOException {
        int lastShadow = this.last;
        if (lastShadow < this.allowableBlockSize) {
            int currentCharShadow = this.currentChar;
            Data dataShadow = this.data;
            dataShadow.inUse[currentCharShadow] = true;
            byte ch = (byte) currentCharShadow;
            int runLengthShadow = this.runLength;
            this.crc32.updateCRC(currentCharShadow, runLengthShadow);
            switch (runLengthShadow) {
                case 1:
                    dataShadow.block[lastShadow + 2] = ch;
                    this.last = lastShadow + 1;
                    return;
                case 2:
                    dataShadow.block[lastShadow + 2] = ch;
                    dataShadow.block[lastShadow + 3] = ch;
                    this.last = lastShadow + 2;
                    return;
                case 3:
                    byte[] block = dataShadow.block;
                    block[lastShadow + 2] = ch;
                    block[lastShadow + 3] = ch;
                    block[lastShadow + 4] = ch;
                    this.last = lastShadow + 3;
                    return;
                default:
                    int runLengthShadow2 = runLengthShadow - 4;
                    dataShadow.inUse[runLengthShadow2] = true;
                    byte[] block2 = dataShadow.block;
                    block2[lastShadow + 2] = ch;
                    block2[lastShadow + 3] = ch;
                    block2[lastShadow + 4] = ch;
                    block2[lastShadow + 5] = ch;
                    block2[lastShadow + 6] = (byte) runLengthShadow2;
                    this.last = lastShadow + 5;
                    return;
            }
        }
        endBlock();
        initBlock();
        writeRun();
    }

    protected void finalize() throws Throwable {
        finish();
        super.finalize();
    }

    public void finish() throws IOException {
        if (this.out != null) {
            try {
                if (this.runLength > 0) {
                    writeRun();
                }
                this.currentChar = -1;
                endBlock();
                endCompression();
            } finally {
                this.out = null;
                this.data = null;
            }
        }
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.out != null) {
            OutputStream outShadow = this.out;
            try {
                finish();
                outShadow.close();
                outShadow = null;
            } finally {
                outShadow.close();
            }
        }
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        OutputStream outShadow = this.out;
        if (outShadow != null) {
            outShadow.flush();
        }
    }

    private void init() throws IOException {
        this.data = new Data(this.blockSize100k);
        bsPutUByte(104);
        bsPutUByte(this.blockSize100k + 48);
        this.combinedCRC = 0;
        initBlock();
    }

    private void initBlock() {
        this.crc32.initialiseCRC();
        this.last = -1;
        boolean[] inUse = this.data.inUse;
        int i = 256;
        while (true) {
            i--;
            if (i >= 0) {
                inUse[i] = false;
            } else {
                this.allowableBlockSize = (this.blockSize100k * 100000) - 20;
                return;
            }
        }
    }

    private void endBlock() throws IOException {
        int blockCRC = this.crc32.getFinalCRC();
        this.combinedCRC = (this.combinedCRC << 1) | (this.combinedCRC >>> 31);
        this.combinedCRC ^= blockCRC;
        if (this.last == -1) {
            return;
        }
        blockSort();
        bsPutUByte(49);
        bsPutUByte(65);
        bsPutUByte(89);
        bsPutUByte(38);
        bsPutUByte(83);
        bsPutUByte(89);
        bsPutInt(blockCRC);
        if (this.blockRandomised) {
            bsW(1, 1);
        } else {
            bsW(1, 0);
        }
        moveToFrontCodeAndSend();
    }

    private void endCompression() throws IOException {
        bsPutUByte(23);
        bsPutUByte(114);
        bsPutUByte(69);
        bsPutUByte(56);
        bsPutUByte(80);
        bsPutUByte(bl.bF);
        bsPutInt(this.combinedCRC);
        bsFinishedWithStream();
    }

    @Override // java.io.OutputStream
    public void write(final byte[] buf, int offs, final int len) throws IOException {
        if (offs < 0) {
            throw new IndexOutOfBoundsException("offs(" + offs + ") < 0.");
        }
        if (len < 0) {
            throw new IndexOutOfBoundsException("len(" + len + ") < 0.");
        }
        if (offs + len > buf.length) {
            throw new IndexOutOfBoundsException("offs(" + offs + ") + len(" + len + ") > buf.length(" + buf.length + ").");
        }
        if (this.out == null) {
            throw new IOException("stream closed");
        }
        int hi = offs + len;
        while (offs < hi) {
            write0(buf[offs]);
            offs++;
        }
    }

    private void write0(int b) throws IOException {
        if (this.currentChar != -1) {
            int b2 = b & 255;
            if (this.currentChar == b2) {
                int i = this.runLength + 1;
                this.runLength = i;
                if (i > 254) {
                    writeRun();
                    this.currentChar = -1;
                    this.runLength = 0;
                    return;
                }
                return;
            }
            writeRun();
            this.runLength = 1;
            this.currentChar = b2;
            return;
        }
        this.currentChar = b & 255;
        this.runLength++;
    }

    private static void hbAssignCodes(final int[] code, final byte[] length, final int minLen, final int maxLen, final int alphaSize) {
        int vec = 0;
        for (int n = minLen; n <= maxLen; n++) {
            for (int i = 0; i < alphaSize; i++) {
                if ((length[i] & 255) == n) {
                    code[i] = vec;
                    vec++;
                }
            }
            vec <<= 1;
        }
    }

    private void bsFinishedWithStream() throws IOException {
        while (this.bsLive > 0) {
            int ch = this.bsBuff >> 24;
            this.out.write(ch);
            this.bsBuff <<= 8;
            this.bsLive -= 8;
        }
    }

    private void bsW(final int n, final int v) throws IOException {
        OutputStream outShadow = this.out;
        int bsLiveShadow = this.bsLive;
        int bsBuffShadow = this.bsBuff;
        while (bsLiveShadow >= 8) {
            outShadow.write(bsBuffShadow >> 24);
            bsBuffShadow <<= 8;
            bsLiveShadow -= 8;
        }
        this.bsBuff = (v << ((32 - bsLiveShadow) - n)) | bsBuffShadow;
        this.bsLive = bsLiveShadow + n;
    }

    private void bsPutUByte(final int c) throws IOException {
        bsW(8, c);
    }

    private void bsPutInt(final int u) throws IOException {
        bsW(8, (u >> 24) & 255);
        bsW(8, (u >> 16) & 255);
        bsW(8, (u >> 8) & 255);
        bsW(8, u & 255);
    }

    private void sendMTFValues() throws IOException {
        byte[][] len = this.data.sendMTFValuesLen;
        int alphaSize = this.nInUse + 2;
        int t = 6;
        while (true) {
            t--;
            if (t < 0) {
                break;
            }
            byte[] lenT = len[t];
            int v = alphaSize;
            while (true) {
                v--;
                if (v >= 0) {
                    lenT[v] = 15;
                }
            }
        }
        int t2 = this.nMTF;
        int nGroups = t2 >= 200 ? this.nMTF < 600 ? 3 : this.nMTF < 1200 ? 4 : this.nMTF < 2400 ? 5 : 6 : 2;
        sendMTFValues0(nGroups, alphaSize);
        int nSelectors = sendMTFValues1(nGroups, alphaSize);
        sendMTFValues2(nGroups, nSelectors);
        sendMTFValues3(nGroups, alphaSize);
        sendMTFValues4();
        sendMTFValues5(nGroups, nSelectors);
        sendMTFValues6(nGroups, alphaSize);
        sendMTFValues7();
    }

    private void sendMTFValues0(final int nGroups, final int alphaSize) {
        byte[][] len = this.data.sendMTFValuesLen;
        int[] mtfFreq = this.data.mtfFreq;
        int remF = this.nMTF;
        int gs = 0;
        for (int nPart = nGroups; nPart > 0; nPart--) {
            int tFreq = remF / nPart;
            int ge = gs - 1;
            int aFreq = 0;
            int a = alphaSize - 1;
            while (aFreq < tFreq && ge < a) {
                ge++;
                aFreq += mtfFreq[ge];
            }
            if (ge > gs && nPart != nGroups && nPart != 1 && (1 & (nGroups - nPart)) != 0) {
                aFreq -= mtfFreq[ge];
                ge--;
            }
            int ge2 = nPart - 1;
            byte[] lenNp = len[ge2];
            int v = alphaSize;
            while (true) {
                v--;
                if (v >= 0) {
                    if (v >= gs && v <= ge) {
                        lenNp[v] = 0;
                    } else {
                        lenNp[v] = 15;
                    }
                }
            }
            gs = ge + 1;
            remF -= aFreq;
        }
    }

    private int sendMTFValues1(final int nGroups, final int alphaSize) {
        int iter;
        int nMTFShadow;
        int i;
        Data dataShadow = this.data;
        int[][] rfreq = dataShadow.sendMTFValuesRfreq;
        int[] fave = dataShadow.sendMTFValuesFave;
        short[] cost = dataShadow.sendMTFValuesCost;
        char[] sfmap = dataShadow.sfmap;
        byte[] selector = dataShadow.selector;
        byte[][] len = dataShadow.sendMTFValuesLen;
        int i2 = 0;
        byte[] len0 = len[0];
        byte[] len1 = len[1];
        byte[] len2 = len[2];
        byte[] len3 = len[3];
        int i3 = 4;
        byte[] len4 = len[4];
        byte[] len5 = len[5];
        int nMTFShadow2 = this.nMTF;
        int nSelectors = 0;
        int iter2 = 0;
        while (iter2 < i3) {
            int t = nGroups;
            while (true) {
                t--;
                if (t < 0) {
                    break;
                }
                fave[t] = i2;
                int[] rfreqt = rfreq[t];
                int i4 = alphaSize;
                while (true) {
                    i4--;
                    if (i4 >= 0) {
                        rfreqt[i4] = i2;
                    }
                }
            }
            nSelectors = 0;
            int gs = 0;
            while (gs < this.nMTF) {
                Data dataShadow2 = dataShadow;
                int ge = Math.min((gs + 50) - 1, nMTFShadow2 - 1);
                if (nGroups == 6) {
                    short cost5 = 0;
                    short cost2 = 0;
                    short cost3 = 0;
                    short cost4 = 0;
                    short cost42 = 0;
                    short cost52 = 0;
                    nMTFShadow = nMTFShadow2;
                    int nMTFShadow3 = gs;
                    while (nMTFShadow3 <= ge) {
                        char c = sfmap[nMTFShadow3];
                        int iter3 = iter2;
                        int iter4 = len0[c];
                        short cost0 = (short) ((iter4 & 255) + cost5);
                        short cost1 = (short) (cost2 + (len1[c] & 255));
                        short cost02 = len2[c];
                        short cost22 = (short) (cost3 + (cost02 & Http2CodecUtil.MAX_UNSIGNED_BYTE));
                        short cost23 = len3[c];
                        short cost32 = (short) (cost4 + (cost23 & Http2CodecUtil.MAX_UNSIGNED_BYTE));
                        short cost33 = len4[c];
                        short cost43 = (short) (cost42 + (cost33 & Http2CodecUtil.MAX_UNSIGNED_BYTE));
                        short cost44 = len5[c];
                        nMTFShadow3++;
                        cost52 = (short) (cost52 + (cost44 & Http2CodecUtil.MAX_UNSIGNED_BYTE));
                        cost42 = cost43;
                        cost5 = cost0;
                        cost4 = cost32;
                        cost3 = cost22;
                        cost2 = cost1;
                        iter2 = iter3;
                    }
                    iter = iter2;
                    cost[0] = cost5;
                    cost[1] = cost2;
                    cost[2] = cost3;
                    cost[3] = cost4;
                    cost[4] = cost42;
                    cost[5] = cost52;
                    i = 0;
                } else {
                    iter = iter2;
                    nMTFShadow = nMTFShadow2;
                    int t2 = nGroups;
                    while (true) {
                        t2--;
                        if (t2 < 0) {
                            break;
                        }
                        cost[t2] = 0;
                    }
                    i = 0;
                    for (int i5 = gs; i5 <= ge; i5++) {
                        char c2 = sfmap[i5];
                        int t3 = nGroups;
                        while (true) {
                            t3--;
                            if (t3 >= 0) {
                                cost[t3] = (short) (cost[t3] + (len[t3][c2] & 255));
                            }
                        }
                    }
                }
                int bt = -1;
                int t4 = nGroups;
                int bc = 999999999;
                while (true) {
                    t4--;
                    if (t4 < 0) {
                        break;
                    }
                    byte[] len02 = len0;
                    short s = cost[t4];
                    if (s < bc) {
                        bc = s;
                        bt = t4;
                    }
                    len0 = len02;
                }
                byte[] len03 = len0;
                fave[bt] = fave[bt] + 1;
                selector[nSelectors] = (byte) bt;
                nSelectors++;
                int[] rfreqBt = rfreq[bt];
                for (int i6 = gs; i6 <= ge; i6++) {
                    char c3 = sfmap[i6];
                    rfreqBt[c3] = rfreqBt[c3] + 1;
                }
                gs = ge + 1;
                len0 = len03;
                dataShadow = dataShadow2;
                nMTFShadow2 = nMTFShadow;
                iter2 = iter;
            }
            Data dataShadow3 = dataShadow;
            byte[] len04 = len0;
            int iter5 = iter2;
            int nMTFShadow4 = nMTFShadow2;
            for (int t5 = 0; t5 < nGroups; t5++) {
                hbMakeCodeLengths(len[t5], rfreq[t5], this.data, alphaSize, 20);
            }
            iter2 = iter5 + 1;
            i3 = 4;
            i2 = 0;
            len0 = len04;
            dataShadow = dataShadow3;
            nMTFShadow2 = nMTFShadow4;
        }
        return nSelectors;
    }

    private void sendMTFValues2(final int nGroups, final int nSelectors) {
        Data dataShadow = this.data;
        byte[] pos = dataShadow.sendMTFValues2Pos;
        int i = nGroups;
        while (true) {
            i--;
            if (i < 0) {
                break;
            } else {
                pos[i] = (byte) i;
            }
        }
        for (int i2 = 0; i2 < nSelectors; i2++) {
            byte llI = dataShadow.selector[i2];
            byte tmp = pos[0];
            int j = 0;
            while (llI != tmp) {
                j++;
                byte tmp2 = tmp;
                tmp = pos[j];
                pos[j] = tmp2;
            }
            pos[0] = tmp;
            dataShadow.selectorMtf[i2] = (byte) j;
        }
    }

    private void sendMTFValues3(final int nGroups, final int alphaSize) {
        int[][] code = this.data.sendMTFValuesCode;
        byte[][] len = this.data.sendMTFValuesLen;
        for (int t = 0; t < nGroups; t++) {
            int minLen = 32;
            int maxLen = 0;
            byte[] lenT = len[t];
            int i = alphaSize;
            while (true) {
                i--;
                if (i >= 0) {
                    int l = lenT[i] & 255;
                    if (l > maxLen) {
                        maxLen = l;
                    }
                    if (l < minLen) {
                        minLen = l;
                    }
                }
            }
            hbAssignCodes(code[t], len[t], minLen, maxLen, alphaSize);
        }
    }

    private void sendMTFValues4() throws IOException {
        boolean[] zArr = this.data.inUse;
        boolean[] zArr2 = this.data.sentMTFValues4InUse16;
        int i = 16;
        while (true) {
            i--;
            if (i < 0) {
                break;
            }
            zArr2[i] = false;
            int i2 = i * 16;
            int i3 = 16;
            while (true) {
                i3--;
                if (i3 >= 0) {
                    if (zArr[i2 + i3]) {
                        zArr2[i] = true;
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        for (int i4 = 0; i4 < 16; i4++) {
            bsW(1, zArr2[i4] ? 1 : 0);
        }
        OutputStream outputStream = this.out;
        int i5 = this.bsLive;
        int i6 = this.bsBuff;
        for (int i7 = 0; i7 < 16; i7++) {
            if (zArr2[i7]) {
                int i8 = i7 * 16;
                for (int i9 = 0; i9 < 16; i9++) {
                    while (i5 >= 8) {
                        outputStream.write(i6 >> 24);
                        i6 <<= 8;
                        i5 -= 8;
                    }
                    if (zArr[i8 + i9]) {
                        i6 |= 1 << ((32 - i5) - 1);
                    }
                    i5++;
                }
            }
        }
        this.bsBuff = i6;
        this.bsLive = i5;
    }

    private void sendMTFValues5(final int nGroups, final int nSelectors) throws IOException {
        bsW(3, nGroups);
        bsW(15, nSelectors);
        OutputStream outShadow = this.out;
        byte[] selectorMtf = this.data.selectorMtf;
        int bsLiveShadow = this.bsLive;
        int bsBuffShadow = this.bsBuff;
        for (int i = 0; i < nSelectors; i++) {
            int hj = selectorMtf[i] & 255;
            for (int j = 0; j < hj; j++) {
                while (bsLiveShadow >= 8) {
                    outShadow.write(bsBuffShadow >> 24);
                    bsBuffShadow <<= 8;
                    bsLiveShadow -= 8;
                }
                bsBuffShadow |= 1 << ((32 - bsLiveShadow) - 1);
                bsLiveShadow++;
            }
            while (bsLiveShadow >= 8) {
                outShadow.write(bsBuffShadow >> 24);
                bsBuffShadow <<= 8;
                bsLiveShadow -= 8;
            }
            bsLiveShadow++;
        }
        this.bsBuff = bsBuffShadow;
        this.bsLive = bsLiveShadow;
    }

    private void sendMTFValues6(final int nGroups, final int alphaSize) throws IOException {
        byte[][] len = this.data.sendMTFValuesLen;
        OutputStream outShadow = this.out;
        int bsLiveShadow = this.bsLive;
        int bsBuffShadow = this.bsBuff;
        for (int t = 0; t < nGroups; t++) {
            byte[] lenT = len[t];
            int curr = lenT[0] & 255;
            while (bsLiveShadow >= 8) {
                outShadow.write(bsBuffShadow >> 24);
                bsBuffShadow <<= 8;
                bsLiveShadow -= 8;
            }
            bsBuffShadow |= curr << ((32 - bsLiveShadow) - 5);
            bsLiveShadow += 5;
            for (int i = 0; i < alphaSize; i++) {
                int lti = lenT[i] & 255;
                while (curr < lti) {
                    while (bsLiveShadow >= 8) {
                        outShadow.write(bsBuffShadow >> 24);
                        bsBuffShadow <<= 8;
                        bsLiveShadow -= 8;
                    }
                    bsBuffShadow |= 2 << ((32 - bsLiveShadow) - 2);
                    bsLiveShadow += 2;
                    curr++;
                }
                while (curr > lti) {
                    while (bsLiveShadow >= 8) {
                        outShadow.write(bsBuffShadow >> 24);
                        bsBuffShadow <<= 8;
                        bsLiveShadow -= 8;
                    }
                    bsBuffShadow |= 3 << ((32 - bsLiveShadow) - 2);
                    bsLiveShadow += 2;
                    curr--;
                }
                while (bsLiveShadow >= 8) {
                    outShadow.write(bsBuffShadow >> 24);
                    bsBuffShadow <<= 8;
                    bsLiveShadow -= 8;
                }
                bsLiveShadow++;
            }
        }
        this.bsBuff = bsBuffShadow;
        this.bsLive = bsLiveShadow;
    }

    private void sendMTFValues7() throws IOException {
        Data dataShadow;
        Data dataShadow2 = this.data;
        byte[][] len = dataShadow2.sendMTFValuesLen;
        int[][] code = dataShadow2.sendMTFValuesCode;
        OutputStream outShadow = this.out;
        byte[] selector = dataShadow2.selector;
        char[] sfmap = dataShadow2.sfmap;
        int nMTFShadow = this.nMTF;
        int selCtr = 0;
        int bsLiveShadow = this.bsLive;
        int bsBuffShadow = this.bsBuff;
        int gs = 0;
        while (gs < nMTFShadow) {
            int ge = Math.min((gs + 50) - 1, nMTFShadow - 1);
            int selectorSelCtr = selector[selCtr] & 255;
            int[] codeSelCtr = code[selectorSelCtr];
            byte[] lenSelCtr = len[selectorSelCtr];
            while (gs <= ge) {
                char c = sfmap[gs];
                while (true) {
                    dataShadow = dataShadow2;
                    if (bsLiveShadow >= 8) {
                        outShadow.write(bsBuffShadow >> 24);
                        bsBuffShadow <<= 8;
                        bsLiveShadow -= 8;
                        dataShadow2 = dataShadow;
                    }
                }
                int n = lenSelCtr[c] & 255;
                bsBuffShadow |= codeSelCtr[c] << ((32 - bsLiveShadow) - n);
                bsLiveShadow += n;
                gs++;
                dataShadow2 = dataShadow;
            }
            gs = ge + 1;
            selCtr++;
        }
        this.bsBuff = bsBuffShadow;
        this.bsLive = bsLiveShadow;
    }

    private void moveToFrontCodeAndSend() throws IOException {
        bsW(24, this.origPtr);
        generateMTFValues();
        sendMTFValues();
    }

    private boolean mainSimpleSort(final Data dataShadow, final int lo, final int hi, final int d) {
        int hp;
        int bigN;
        int bigN2 = (hi - lo) + 1;
        if (bigN2 < 2) {
            return this.firstAttempt && this.workDone > this.workLimit;
        }
        int hp2 = 0;
        while (INCS[hp2] < bigN2) {
            hp2++;
        }
        int[] fmap = dataShadow.fmap;
        char[] quadrant = dataShadow.quadrant;
        byte[] block = dataShadow.block;
        int lastShadow = this.last;
        int lastPlus1 = lastShadow + 1;
        boolean firstAttemptShadow = this.firstAttempt;
        int workLimitShadow = this.workLimit;
        int workDoneShadow = this.workDone;
        loop1: while (true) {
            hp2--;
            if (hp2 < 0) {
                break;
            }
            int h = INCS[hp2];
            int mj = (lo + h) - 1;
            int i = lo + h;
            while (i <= hi) {
                int k = 3;
                while (i <= hi) {
                    k--;
                    if (k < 0) {
                        break;
                    }
                    int v = fmap[i];
                    int vd = v + d;
                    int j = i;
                    boolean onceRunned = false;
                    int a = 0;
                    while (true) {
                        if (onceRunned) {
                            fmap[j] = a;
                            int i2 = j - h;
                            j = i2;
                            if (i2 <= mj) {
                                bigN = bigN2;
                                hp = hp2;
                                break;
                            }
                        } else {
                            onceRunned = true;
                        }
                        a = fmap[j - h];
                        int i1 = a + d;
                        bigN = bigN2;
                        int bigN3 = block[i1 + 1];
                        hp = hp2;
                        int hp3 = block[vd + 1];
                        if (bigN3 == hp3) {
                            if (block[i1 + 2] == block[vd + 2]) {
                                if (block[i1 + 3] == block[vd + 3]) {
                                    if (block[i1 + 4] == block[vd + 4]) {
                                        if (block[i1 + 5] != block[vd + 5]) {
                                            if ((block[i1 + 5] & 255) <= (block[vd + 5] & 255)) {
                                                break;
                                            }
                                            bigN2 = bigN;
                                            hp2 = hp;
                                        } else {
                                            int i12 = i1 + 6;
                                            int i22 = vd + 6;
                                            if (block[i12] == block[i22]) {
                                                int x = lastShadow;
                                                while (true) {
                                                    if (x > 0) {
                                                        int x2 = x - 4;
                                                        int i3 = block[i12 + 1];
                                                        int x3 = block[i22 + 1];
                                                        if (i3 == x3) {
                                                            if (quadrant[i12] == quadrant[i22]) {
                                                                if (block[i12 + 2] == block[i22 + 2]) {
                                                                    if (quadrant[i12 + 1] == quadrant[i22 + 1]) {
                                                                        if (block[i12 + 3] == block[i22 + 3]) {
                                                                            if (quadrant[i12 + 2] == quadrant[i22 + 2]) {
                                                                                if (block[i12 + 4] == block[i22 + 4]) {
                                                                                    if (quadrant[i12 + 3] == quadrant[i22 + 3]) {
                                                                                        i12 += 4;
                                                                                        if (i12 >= lastPlus1) {
                                                                                            i12 -= lastPlus1;
                                                                                        }
                                                                                        int i23 = i22 + 4;
                                                                                        if (i23 >= lastPlus1) {
                                                                                            i23 -= lastPlus1;
                                                                                        }
                                                                                        i22 = i23;
                                                                                        workDoneShadow++;
                                                                                        x = x2;
                                                                                    } else if (quadrant[i12 + 3] <= quadrant[i22 + 3]) {
                                                                                        break;
                                                                                    }
                                                                                } else if ((block[i12 + 4] & 255) <= (block[i22 + 4] & 255)) {
                                                                                    break;
                                                                                }
                                                                            } else if (quadrant[i12 + 2] <= quadrant[i22 + 2]) {
                                                                                break;
                                                                            }
                                                                        } else if ((block[i12 + 3] & 255) <= (block[i22 + 3] & 255)) {
                                                                            break;
                                                                        }
                                                                    } else if (quadrant[i12 + 1] <= quadrant[i22 + 1]) {
                                                                        break;
                                                                    }
                                                                } else if ((block[i12 + 2] & 255) <= (block[i22 + 2] & 255)) {
                                                                    break;
                                                                }
                                                            } else if (quadrant[i12] <= quadrant[i22]) {
                                                                break;
                                                            }
                                                        } else if ((block[i12 + 1] & 255) <= (block[i22 + 1] & 255)) {
                                                            break;
                                                        }
                                                    }
                                                }
                                                bigN2 = bigN;
                                                hp2 = hp;
                                            } else {
                                                if ((block[i12] & 255) <= (block[i22] & 255)) {
                                                    break;
                                                }
                                                bigN2 = bigN;
                                                hp2 = hp;
                                            }
                                        }
                                    } else {
                                        if ((block[i1 + 4] & 255) <= (block[vd + 4] & 255)) {
                                            break;
                                        }
                                        bigN2 = bigN;
                                        hp2 = hp;
                                    }
                                } else {
                                    if ((block[i1 + 3] & 255) <= (block[vd + 3] & 255)) {
                                        break;
                                    }
                                    bigN2 = bigN;
                                    hp2 = hp;
                                }
                            } else {
                                if ((block[i1 + 2] & 255) <= (block[vd + 2] & 255)) {
                                    break;
                                }
                                bigN2 = bigN;
                                hp2 = hp;
                            }
                        } else {
                            if ((block[i1 + 1] & 255) <= (block[vd + 1] & 255)) {
                                break;
                            }
                            bigN2 = bigN;
                            hp2 = hp;
                        }
                    }
                    fmap[j] = v;
                    i++;
                    bigN2 = bigN;
                    hp2 = hp;
                }
                int bigN4 = bigN2;
                int hp4 = hp2;
                if (firstAttemptShadow && i <= hi && workDoneShadow > workLimitShadow) {
                    break loop1;
                }
                bigN2 = bigN4;
                hp2 = hp4;
            }
        }
        this.workDone = workDoneShadow;
        return firstAttemptShadow && workDoneShadow > workLimitShadow;
    }

    private static void vswap(int[] fmap, int p1, int p2, int n) {
        int n2 = n + p1;
        while (p1 < n2) {
            int t = fmap[p1];
            fmap[p1] = fmap[p2];
            fmap[p2] = t;
            p2++;
            p1++;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x000c, code lost:            if (r1 > r3) goto L10;     */
    /* JADX WARN: Code restructure failed: missing block: B:3:0x0005, code lost:            if (r1 < r3) goto L10;     */
    /* JADX WARN: Code restructure failed: missing block: B:5:0x0011, code lost:            return r1;     */
    /* JADX WARN: Code restructure failed: missing block: B:8:?, code lost:            return r3;     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static byte med3(byte r1, byte r2, byte r3) {
        /*
            if (r1 >= r2) goto L8
            if (r2 >= r3) goto L5
            goto La
        L5:
            if (r1 >= r3) goto L10
            goto Le
        L8:
            if (r2 <= r3) goto Lc
        La:
            r0 = r2
            goto L11
        Lc:
            if (r1 <= r3) goto L10
        Le:
            r0 = r3
            goto L11
        L10:
            r0 = r1
        L11:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.airlift.compress.bzip2.CBZip2OutputStream.med3(byte, byte, byte):byte");
    }

    private void blockSort() {
        this.workLimit = this.last * 30;
        this.workDone = 0;
        this.blockRandomised = false;
        this.firstAttempt = true;
        mainSort();
        if (this.firstAttempt && this.workDone > this.workLimit) {
            randomiseBlock();
            this.workLimit = 0;
            this.workDone = 0;
            this.firstAttempt = false;
            mainSort();
        }
        int[] fmap = this.data.fmap;
        this.origPtr = -1;
        int lastShadow = this.last;
        for (int i = 0; i <= lastShadow; i++) {
            if (fmap[i] == 0) {
                this.origPtr = i;
                return;
            }
        }
    }

    private void mainQSort3(final Data dataShadow, final int loSt, final int hiSt, final int dSt) {
        byte[] block;
        Data data;
        int unHi;
        int gtHi;
        int[] stackLl = dataShadow.stackLl;
        int[] stackHh = dataShadow.stackHh;
        int[] stackDd = dataShadow.stackDd;
        int[] fmap = dataShadow.fmap;
        byte[] block2 = dataShadow.block;
        stackLl[0] = loSt;
        stackHh[0] = hiSt;
        stackDd[0] = dSt;
        int sp = 1;
        while (true) {
            sp--;
            if (sp >= 0) {
                int lo = stackLl[sp];
                int hi = stackHh[sp];
                int d = stackDd[sp];
                if (hi - lo < 20) {
                    block = block2;
                } else if (d > 10) {
                    block = block2;
                } else {
                    int d1 = d + 1;
                    int med = med3(block2[fmap[lo] + d1], block2[fmap[hi] + d1], block2[fmap[(lo + hi) >>> 1] + d1]) & 255;
                    int unLo = lo;
                    int n = hi;
                    int ltLo = lo;
                    int gtHi2 = hi;
                    while (true) {
                        if (unLo > n) {
                            unHi = n;
                        } else {
                            unHi = n;
                            int unHi2 = block2[fmap[unLo] + d1];
                            int n2 = (unHi2 & 255) - med;
                            if (n2 == 0) {
                                int temp = fmap[unLo];
                                fmap[unLo] = fmap[ltLo];
                                fmap[ltLo] = temp;
                                ltLo++;
                                unLo++;
                            } else if (n2 < 0) {
                                unLo++;
                            }
                            n = unHi;
                        }
                        gtHi = unHi;
                        while (unLo <= gtHi) {
                            int n3 = (block2[fmap[gtHi] + d1] & 255) - med;
                            if (n3 == 0) {
                                int temp2 = fmap[gtHi];
                                int unHi3 = gtHi - 1;
                                fmap[gtHi] = fmap[gtHi2];
                                fmap[gtHi2] = temp2;
                                gtHi2--;
                                gtHi = unHi3;
                            } else if (n3 <= 0) {
                                break;
                            } else {
                                gtHi--;
                            }
                        }
                        if (unLo > gtHi) {
                            break;
                        }
                        int temp3 = fmap[unLo];
                        int unLo2 = unLo + 1;
                        fmap[unLo] = fmap[gtHi];
                        int unHi4 = gtHi - 1;
                        fmap[gtHi] = temp3;
                        n = unHi4;
                        unLo = unLo2;
                    }
                    if (gtHi2 < ltLo) {
                        stackLl[sp] = lo;
                        stackHh[sp] = hi;
                        stackDd[sp] = d1;
                        sp++;
                        block = block2;
                        data = dataShadow;
                    } else {
                        block = block2;
                        int n4 = Math.min(ltLo - lo, unLo - ltLo);
                        vswap(fmap, lo, unLo - n4, n4);
                        int n5 = gtHi2 - gtHi;
                        int m = Math.min(hi - gtHi2, n5);
                        vswap(fmap, unLo, (hi - m) + 1, m);
                        int n6 = ((lo + unLo) - ltLo) - 1;
                        int n7 = gtHi2 - gtHi;
                        int m2 = (hi - n7) + 1;
                        stackLl[sp] = lo;
                        stackHh[sp] = n6;
                        stackDd[sp] = d;
                        int sp2 = sp + 1;
                        stackLl[sp2] = n6 + 1;
                        stackHh[sp2] = m2 - 1;
                        stackDd[sp2] = d1;
                        int sp3 = sp2 + 1;
                        stackLl[sp3] = m2;
                        stackHh[sp3] = hi;
                        stackDd[sp3] = d;
                        sp = sp3 + 1;
                        data = dataShadow;
                    }
                    block2 = block;
                }
                data = dataShadow;
                if (mainSimpleSort(data, lo, hi, d)) {
                    return;
                } else {
                    block2 = block;
                }
            } else {
                return;
            }
        }
    }

    private void mainSort() {
        Data dataShadow;
        int c1;
        char[] quadrant;
        CBZip2OutputStream cBZip2OutputStream = this;
        Data dataShadow2 = cBZip2OutputStream.data;
        int[] runningOrder = dataShadow2.mainSortRunningOrder;
        int[] copy = dataShadow2.mainSortCopy;
        boolean[] bigDone = dataShadow2.mainSortBigDone;
        int[] ftab = dataShadow2.ftab;
        byte[] block = dataShadow2.block;
        int[] fmap = dataShadow2.fmap;
        char[] quadrant2 = dataShadow2.quadrant;
        int lastShadow = cBZip2OutputStream.last;
        int workLimitShadow = cBZip2OutputStream.workLimit;
        boolean firstAttemptShadow = cBZip2OutputStream.firstAttempt;
        int i = 65537;
        while (true) {
            i--;
            if (i < 0) {
                break;
            } else {
                ftab[i] = 0;
            }
        }
        for (int i2 = 0; i2 < 20; i2++) {
            block[lastShadow + i2 + 2] = block[(i2 % (lastShadow + 1)) + 1];
        }
        int i3 = lastShadow + 20;
        int i4 = i3 + 1;
        while (true) {
            i4--;
            if (i4 < 0) {
                break;
            } else {
                quadrant2[i4] = 0;
            }
        }
        int i5 = lastShadow + 1;
        block[0] = block[i5];
        int i6 = 255;
        int c12 = block[0] & 255;
        for (int i7 = 0; i7 <= lastShadow; i7++) {
            int c2 = block[i7 + 1] & 255;
            int i8 = (c12 << 8) + c2;
            ftab[i8] = ftab[i8] + 1;
            c12 = c2;
        }
        for (int i9 = 1; i9 <= 65536; i9++) {
            ftab[i9] = ftab[i9] + ftab[i9 - 1];
        }
        int c13 = block[1] & 255;
        for (int i10 = 0; i10 < lastShadow; i10++) {
            int c22 = block[i10 + 2] & 255;
            int i11 = (c13 << 8) + c22;
            int i12 = ftab[i11] - 1;
            ftab[i11] = i12;
            fmap[i12] = i10;
            c13 = c22;
        }
        int i13 = lastShadow + 1;
        int i14 = (block[i13] & 255) << 8;
        int c14 = c13;
        int c15 = block[1];
        int i15 = i14 + (c15 & 255);
        int i16 = ftab[i15] - 1;
        ftab[i15] = i16;
        fmap[i16] = lastShadow;
        int i17 = 256;
        while (true) {
            i17--;
            if (i17 < 0) {
                break;
            }
            bigDone[i17] = false;
            runningOrder[i17] = i17;
        }
        int h = 364;
        while (h != 1) {
            h /= 3;
            int i18 = h;
            while (i18 <= i6) {
                int vv = runningOrder[i18];
                int a = ftab[(vv + 1) << 8] - ftab[vv << 8];
                int lastShadow2 = lastShadow;
                int lastShadow3 = h - 1;
                int j = i18;
                int ro = runningOrder[j - h];
                while (true) {
                    quadrant = quadrant2;
                    if (ftab[(ro + 1) << 8] - ftab[ro << 8] > a) {
                        runningOrder[j] = ro;
                        int j2 = j - h;
                        if (j2 > lastShadow3) {
                            ro = runningOrder[j2 - h];
                            j = j2;
                            quadrant2 = quadrant;
                        } else {
                            j = j2;
                            break;
                        }
                    }
                }
                runningOrder[j] = vv;
                i18++;
                lastShadow = lastShadow2;
                quadrant2 = quadrant;
                i6 = 255;
            }
            i6 = 255;
        }
        char[] quadrant3 = quadrant2;
        int lastShadow4 = lastShadow;
        int i19 = 0;
        int c16 = c14;
        while (true) {
            if (i19 <= 255) {
                int ss = runningOrder[i19];
                int j3 = 0;
                for (int i20 = 255; j3 <= i20; i20 = 255) {
                    int sb = (ss << 8) + j3;
                    int ftabSb = ftab[sb];
                    int[] runningOrder2 = runningOrder;
                    if ((ftabSb & 2097152) == 2097152) {
                        c1 = c16;
                    } else {
                        int lo = ftabSb & CLEAR_MASK;
                        int hi = (ftab[sb + 1] & CLEAR_MASK) - 1;
                        if (hi <= lo) {
                            c1 = c16;
                        } else {
                            c1 = c16;
                            cBZip2OutputStream.mainQSort3(dataShadow2, lo, hi, 2);
                            if (firstAttemptShadow && cBZip2OutputStream.workDone > workLimitShadow) {
                                return;
                            }
                        }
                        ftab[sb] = ftabSb | 2097152;
                    }
                    j3++;
                    runningOrder = runningOrder2;
                    c16 = c1;
                }
                int[] runningOrder3 = runningOrder;
                int c17 = c16;
                for (int j4 = 0; j4 <= 255; j4++) {
                    copy[j4] = ftab[(j4 << 8) + ss] & CLEAR_MASK;
                }
                int j5 = ss << 8;
                int hj = ftab[(ss + 1) << 8] & CLEAR_MASK;
                c16 = c17;
                for (int j6 = ftab[j5] & CLEAR_MASK; j6 < hj; j6++) {
                    int fmapJ = fmap[j6];
                    c16 = block[fmapJ] & 255;
                    if (!bigDone[c16]) {
                        fmap[copy[c16]] = fmapJ == 0 ? lastShadow4 : fmapJ - 1;
                        copy[c16] = copy[c16] + 1;
                    }
                }
                int j7 = 256;
                while (true) {
                    j7--;
                    if (j7 < 0) {
                        break;
                    }
                    int i21 = (j7 << 8) + ss;
                    ftab[i21] = ftab[i21] | 2097152;
                }
                bigDone[ss] = true;
                if (i19 >= 255) {
                    dataShadow = dataShadow2;
                } else {
                    int bbStart = ftab[ss << 8] & CLEAR_MASK;
                    int bbSize = (ftab[(ss + 1) << 8] & CLEAR_MASK) - bbStart;
                    int shifts = 0;
                    while ((bbSize >> shifts) > 65534) {
                        shifts++;
                    }
                    int j8 = 0;
                    while (j8 < bbSize) {
                        int a2update = fmap[bbStart + j8];
                        Data dataShadow3 = dataShadow2;
                        char qVal = (char) (j8 >> shifts);
                        quadrant3[a2update] = qVal;
                        int bbStart2 = bbStart;
                        if (a2update < 20) {
                            quadrant3[a2update + lastShadow4 + 1] = qVal;
                        }
                        j8++;
                        dataShadow2 = dataShadow3;
                        bbStart = bbStart2;
                    }
                    dataShadow = dataShadow2;
                }
                i19++;
                cBZip2OutputStream = this;
                dataShadow2 = dataShadow;
                runningOrder = runningOrder3;
            } else {
                return;
            }
        }
    }

    private void randomiseBlock() {
        boolean[] inUse = this.data.inUse;
        byte[] block = this.data.block;
        int lastShadow = this.last;
        int i = 256;
        while (true) {
            i--;
            if (i < 0) {
                break;
            } else {
                inUse[i] = false;
            }
        }
        int rNToGo = 0;
        int rTPos = 0;
        int i2 = 0;
        int j = 1;
        while (i2 <= lastShadow) {
            if (rNToGo == 0) {
                rNToGo = (char) R_NUMS[rTPos];
                rTPos++;
                if (rTPos == 512) {
                    rTPos = 0;
                }
            }
            rNToGo--;
            block[j] = (byte) (block[j] ^ (rNToGo == 1 ? (byte) 1 : (byte) 0));
            inUse[block[j] & 255] = true;
            i2 = j;
            j++;
        }
        this.blockRandomised = true;
    }

    private void generateMTFValues() {
        int lastShadow = this.last;
        Data dataShadow = this.data;
        boolean[] inUse = dataShadow.inUse;
        byte[] block = dataShadow.block;
        int[] fmap = dataShadow.fmap;
        char[] sfmap = dataShadow.sfmap;
        int[] mtfFreq = dataShadow.mtfFreq;
        byte[] unseqToSeq = dataShadow.unseqToSeq;
        byte[] yy = dataShadow.generateMTFValuesYy;
        int nInUseShadow = 0;
        for (int i = 0; i < 256; i++) {
            if (inUse[i]) {
                unseqToSeq[i] = (byte) nInUseShadow;
                nInUseShadow++;
            }
        }
        this.nInUse = nInUseShadow;
        int eob = nInUseShadow + 1;
        for (int i2 = eob; i2 >= 0; i2--) {
            mtfFreq[i2] = 0;
        }
        int i3 = nInUseShadow;
        while (true) {
            i3--;
            if (i3 < 0) {
                break;
            } else {
                yy[i3] = (byte) i3;
            }
        }
        int wr = 0;
        int zPend = 0;
        int i4 = 0;
        while (i4 <= lastShadow) {
            byte llI = unseqToSeq[block[fmap[i4]] & 255];
            byte tmp = yy[0];
            int j = 0;
            int lastShadow2 = lastShadow;
            byte tmp2 = tmp;
            while (llI != tmp2) {
                j++;
                byte tmp22 = tmp2;
                tmp2 = yy[j];
                yy[j] = tmp22;
            }
            yy[0] = tmp2;
            if (j == 0) {
                zPend++;
            } else {
                if (zPend > 0) {
                    int zPend2 = zPend - 1;
                    while (true) {
                        if ((zPend2 & 1) == 0) {
                            sfmap[wr] = 0;
                            wr++;
                            mtfFreq[0] = mtfFreq[0] + 1;
                        } else {
                            sfmap[wr] = 1;
                            wr++;
                            mtfFreq[1] = mtfFreq[1] + 1;
                        }
                        byte tmp3 = tmp2;
                        if (zPend2 < 2) {
                            break;
                        }
                        zPend2 = (zPend2 - 2) >> 1;
                        tmp2 = tmp3;
                    }
                    zPend = 0;
                }
                sfmap[wr] = (char) (j + 1);
                wr++;
                int i5 = j + 1;
                mtfFreq[i5] = mtfFreq[i5] + 1;
            }
            i4++;
            lastShadow = lastShadow2;
        }
        if (zPend > 0) {
            int zPend3 = zPend - 1;
            while (true) {
                if ((zPend3 & 1) == 0) {
                    sfmap[wr] = 0;
                    wr++;
                    mtfFreq[0] = mtfFreq[0] + 1;
                } else {
                    sfmap[wr] = 1;
                    wr++;
                    mtfFreq[1] = mtfFreq[1] + 1;
                }
                if (zPend3 < 2) {
                    break;
                } else {
                    zPend3 = (zPend3 - 2) >> 1;
                }
            }
        }
        sfmap[wr] = (char) eob;
        mtfFreq[eob] = mtfFreq[eob] + 1;
        this.nMTF = wr + 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class Data {
        final byte[] block;
        final int[] fmap;
        final char[] quadrant;
        final char[] sfmap;
        final boolean[] inUse = new boolean[256];
        final byte[] unseqToSeq = new byte[256];
        final int[] mtfFreq = new int[258];
        final byte[] selector = new byte[BZip2Constants.MAX_SELECTORS];
        final byte[] selectorMtf = new byte[BZip2Constants.MAX_SELECTORS];
        final byte[] generateMTFValuesYy = new byte[256];
        final byte[][] sendMTFValuesLen = (byte[][]) Array.newInstance((Class<?>) Byte.TYPE, 6, 258);
        final int[][] sendMTFValuesRfreq = (int[][]) Array.newInstance((Class<?>) Integer.TYPE, 6, 258);
        final int[] sendMTFValuesFave = new int[6];
        final short[] sendMTFValuesCost = new short[6];
        final int[][] sendMTFValuesCode = (int[][]) Array.newInstance((Class<?>) Integer.TYPE, 6, 258);
        final byte[] sendMTFValues2Pos = new byte[6];
        final boolean[] sentMTFValues4InUse16 = new boolean[16];
        final int[] stackLl = new int[1000];
        final int[] stackHh = new int[1000];
        final int[] stackDd = new int[1000];
        final int[] mainSortRunningOrder = new int[256];
        final int[] mainSortCopy = new int[256];
        final boolean[] mainSortBigDone = new boolean[256];
        final int[] heap = new int[bl.dw];
        final int[] weight = new int[516];
        final int[] parent = new int[516];
        final int[] ftab = new int[65537];

        Data(int blockSize100k) {
            int n = 100000 * blockSize100k;
            this.block = new byte[n + 1 + 20];
            this.fmap = new int[n];
            this.sfmap = new char[n * 2];
            this.quadrant = this.sfmap;
        }
    }
}
