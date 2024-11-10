package io.airlift.compress.bzip2;

import androidx.core.app.FrameMetricsAggregator;
import androidx.core.view.InputDeviceCompat;
import com.google.android.material.internal.ViewUtils;
import com.trossense.bl;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import org.cloudburstmc.netty.channel.raknet.RakConstants;

/* loaded from: classes.dex */
class CBZip2InputStream extends InputStream {
    private static final long BLOCK_DELIMITER = 54156738319193L;
    private static final int DELIMITER_BIT_LENGTH = 48;
    public static final int END_OF_BLOCK = -2;
    private static final int END_OF_STREAM = -1;
    private static final int MAX_CODE_LEN = 23;
    private static final int[] R_NUMS = {619, 720, 127, 481, 931, 816, 813, bl.c5, 566, bl.di, 985, 724, bl.cD, 454, 863, 491, 741, bl.dd, 949, bl.cM, 733, 859, 335, 708, 621, 574, 73, 654, 730, 472, 419, 436, bl.dO, 496, 867, bl.cI, 399, 680, 480, 51, 878, 465, 811, bl.b4, 869, 675, 611, 697, 867, 561, 862, 687, 507, bl.dT, 482, bl.bq, 807, 591, 733, 623, bl.bL, bl.c_, 59, 379, 684, 877, 625, bl.b4, 643, 105, bl.b5, 607, 520, 932, 727, 476, 693, 425, bl.b9, 647, 73, 122, 335, 530, 442, 853, 695, bl.dk, 445, 515, 909, 545, 703, 919, 874, 474, 882, 500, 594, 612, 641, 801, bl.cS, bl.bX, 819, 984, 589, InputDeviceCompat.SOURCE_DPAD, 495, 799, bl.bW, 604, 958, 533, bl.cT, 400, 386, 867, 600, 782, 382, 596, 414, bl.b6, 516, 375, 682, 485, 911, bl.dM, 98, 553, bl.bY, 354, 666, 933, 424, 341, 533, 870, bl.cZ, 730, 475, bl.ck, 263, 647, 537, 686, 600, bl.cW, 469, 68, 770, 919, bl.co, 373, bl.d4, 822, 808, bl.cE, bl.ci, 943, 795, 384, 383, 461, 404, 758, 839, 887, 715, 67, 618, bl.dM, bl.cC, 918, 873, 777, 604, 560, 951, bl.bV, 578, 722, 79, 804, 96, 409, 713, 940, 652, 934, 970, 447, 318, 353, 859, 672, 112, 785, 645, 863, 803, 350, bl.bA, 93, 354, 99, 820, 908, 609, 772, bl.bP, bl.dK, 580, bl.ci, 79, 626, 630, 742, 653, bl.dS, 762, 623, 680, 81, 927, 626, 789, bl.bm, 411, 521, 938, 300, 821, 78, 343, bl.b_, 128, 250, bl.b5, 774, 972, bl.dL, 999, 639, 495, 78, 352, 126, 857, 956, 358, 619, 580, 124, 737, 594, 701, 612, 669, 112, bl.bv, 694, 363, 992, 809, 743, bl.b3, 974, 944, 375, 748, 52, 600, 747, 642, bl.cg, 862, 81, 344, 805, 988, 739, FrameMetricsAggregator.EVERY_DURATION, 655, 814, 334, bl.dk, 515, 897, 955, 664, 981, 649, 113, 974, 459, 893, bl.c0, 433, 837, 553, bl.dE, 926, bl.db, 102, 654, 459, 51, 686, 754, 806, 760, 493, 403, 415, 394, 687, 700, 946, 670, 656, 610, 738, 392, 760, 799, 887, 653, 978, 321, RakConstants.MINIMUM_MTU_SIZE, 617, 626, 502, 894, 679, bl.de, 440, 680, 879, bl.cs, 572, 640, 724, 926, 56, bl.cC, 700, 707, bl.bM, 457, 449, 797, bl.ct, 791, 558, 945, 679, bl.d7, 59, 87, 824, 713, 663, 412, 693, 342, 606, bl.bv, 108, 571, 364, 631, bl.cK, bl.b9, 643, bl.ed, 329, 343, 97, 430, 751, 497, bl.en, 983, 374, 822, 928, bl.bB, bl.cE, 73, 263, 980, 736, 876, 478, 430, bl.ee, bl.b5, 514, 364, 692, 829, 82, 855, 953, 676, bl.dh, 369, 970, bl.d4, 750, 807, 827, bl.bL, 790, bl.dY, 923, 804, 378, bl.cN, 828, 592, bl.dR, 565, 555, 710, 82, 896, 831, 547, bl.dx, 524, 462, bl.d3, 465, 502, 56, 661, 821, 976, 991, 658, 869, 905, 758, 745, bl.cr, ViewUtils.EDGE_TO_EDGE_FLAGS, 550, 608, 933, 378, bl.dW, bl.cN, 979, 792, 961, 61, 688, 793, 644, 986, 403, 106, 366, 905, 644, 372, 567, 466, 434, 645, bl.cI, 389, 550, 919, bl.bw, 780, 773, 635, 389, 707, 100, 626, 958, bl.b0, 504, 920, bl.ca, bl.cr, 713, 857, bl.dB, bl.cB, 50, 668, 108, 645, 990, 626, bl.cv, 510, 357, 358, 850, 858, 364, 936, 638};
    private boolean blockRandomised;
    private long bsBuff;
    private long bsLive;
    private long bytesReadFromCompressedStream;
    private int computedCombinedCRC;
    private Data data;
    private BufferedInputStream in;
    private boolean initialized;
    private int last;
    private int nInUse;
    private int origPtr;
    private long reportedBytesReadFromCompressedStream;
    private boolean skipResult;
    private int storedBlockCRC;
    private int storedCombinedCRC;
    private int suCh2;
    private int suChPrev;
    private int suCount;
    private int suI2;
    private int suJ2;
    private int suRNToGo;
    private int suRTPos;
    private int suTPos;
    private char suZ;
    private final byte[] array = new byte[1];
    private final Crc32 crc32 = new Crc32();
    private int currentChar = -1;
    private STATE currentState = STATE.START_BLOCK_STATE;
    private int blockSize100k = 57 - 48;

    /* loaded from: classes.dex */
    public enum STATE {
        EOF,
        START_BLOCK_STATE,
        RAND_PART_A_STATE,
        RAND_PART_B_STATE,
        RAND_PART_C_STATE,
        NO_RAND_PART_A_STATE,
        NO_RAND_PART_B_STATE,
        NO_RAND_PART_C_STATE,
        NO_PROCESS_STATE
    }

    public CBZip2InputStream(final InputStream in) {
        this.in = new BufferedInputStream(in, 9216);
    }

    public long getProcessedByteCount() {
        return this.reportedBytesReadFromCompressedStream;
    }

    private void updateProcessedByteCount(int count) {
        this.bytesReadFromCompressedStream += count;
    }

    private int readAByte(InputStream inStream) throws IOException {
        int read = inStream.read();
        if (read >= 0) {
            updateProcessedByteCount(1);
        }
        return read;
    }

    private boolean skipToNextMarker(long marker, int markerBitLength) throws IllegalArgumentException {
        try {
            if (markerBitLength > 63) {
                throw new IllegalArgumentException("skipToNextMarker can not find patterns greater than 63 bits");
            }
            long bytes = bsR(markerBitLength);
            if (bytes == -1) {
                this.reportedBytesReadFromCompressedStream = this.bytesReadFromCompressedStream;
                return false;
            }
            while (bytes != marker) {
                long bytes2 = (bytes << 1) & ((1 << markerBitLength) - 1);
                int oneBit = (int) bsR(1L);
                if (oneBit != -1) {
                    bytes = bytes2 | oneBit;
                } else {
                    this.reportedBytesReadFromCompressedStream = this.bytesReadFromCompressedStream;
                    return false;
                }
            }
            long markerBytesRead = ((markerBitLength + this.bsLive) + 7) / 8;
            this.reportedBytesReadFromCompressedStream = this.bytesReadFromCompressedStream - markerBytesRead;
            return true;
        } catch (IOException e) {
            this.reportedBytesReadFromCompressedStream = this.bytesReadFromCompressedStream;
            return false;
        }
    }

    private void makeMaps() {
        boolean[] inUse = this.data.inUse;
        byte[] seqToUnseq = this.data.seqToUnseq;
        int nInUseShadow = 0;
        for (int i = 0; i < 256; i++) {
            if (inUse[i]) {
                seqToUnseq[nInUseShadow] = (byte) i;
                nInUseShadow++;
            }
        }
        this.nInUse = nInUseShadow;
    }

    private void changeStateToProcessABlock() throws IOException {
        if (this.skipResult) {
            initBlock();
            setupBlock();
        } else {
            this.currentState = STATE.EOF;
        }
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (this.in != null) {
            int result = read(this.array, 0, 1);
            int value = this.array[0] & 255;
            return result > 0 ? value : result;
        }
        throw new IOException("stream closed");
    }

    @Override // java.io.InputStream
    public int read(final byte[] dest, final int offs, final int len) throws IOException {
        if (offs < 0) {
            throw new IndexOutOfBoundsException("offs(" + offs + ") < 0.");
        }
        if (len < 0) {
            throw new IndexOutOfBoundsException("len(" + len + ") < 0.");
        }
        if (offs + len > dest.length) {
            throw new IndexOutOfBoundsException("offs(" + offs + ") + len(" + len + ") > dest.length(" + dest.length + ").");
        }
        if (this.in == null) {
            throw new IOException("stream closed");
        }
        if (!this.initialized) {
            init();
            this.initialized = true;
        }
        int hi = offs + len;
        int destOffs = offs;
        int b = 0;
        while (destOffs < hi) {
            int read0 = read0();
            b = read0;
            if (read0 < 0) {
                break;
            }
            dest[destOffs] = (byte) b;
            destOffs++;
        }
        int destOffs2 = destOffs - offs;
        if (destOffs2 == 0) {
            int result = b;
            this.skipResult = skipToNextMarker(BLOCK_DELIMITER, 48);
            changeStateToProcessABlock();
            return result;
        }
        return destOffs2;
    }

    private int read0() throws IOException {
        int retChar = this.currentChar;
        switch (this.currentState) {
            case EOF:
                return -1;
            case NO_PROCESS_STATE:
                return -2;
            case START_BLOCK_STATE:
                throw new IllegalStateException();
            case RAND_PART_A_STATE:
                throw new IllegalStateException();
            case RAND_PART_B_STATE:
                setupRandPartB();
                return retChar;
            case RAND_PART_C_STATE:
                setupRandPartC();
                return retChar;
            case NO_RAND_PART_A_STATE:
                throw new IllegalStateException();
            case NO_RAND_PART_B_STATE:
                setupNoRandPartB();
                return retChar;
            case NO_RAND_PART_C_STATE:
                setupNoRandPartC();
                return retChar;
            default:
                throw new IllegalStateException();
        }
    }

    private void init() throws IOException {
        int magic2 = readAByte(this.in);
        if (magic2 != 104) {
            throw new IOException("Stream is not BZip2 formatted: expected 'h' as first byte but got '" + ((char) magic2) + "'");
        }
        int blockSize = readAByte(this.in);
        if (blockSize < 49 || blockSize > 57) {
            throw new IOException("Stream is not BZip2 formatted: illegal blocksize " + ((char) blockSize));
        }
        this.blockSize100k = blockSize - 48;
        initBlock();
        setupBlock();
    }

    private void initBlock() throws IOException {
        char magic0 = bsGetUByte();
        char magic1 = bsGetUByte();
        char magic2 = bsGetUByte();
        char magic3 = bsGetUByte();
        char magic4 = bsGetUByte();
        char magic5 = bsGetUByte();
        if (magic0 == 23 && magic1 == 'r' && magic2 == 'E' && magic3 == '8' && magic4 == 'P' && magic5 == 144) {
            complete();
            return;
        }
        if (magic0 != '1' || magic1 != 'A' || magic2 != 'Y' || magic3 != '&' || magic4 != 'S' || magic5 != 'Y') {
            this.currentState = STATE.EOF;
            throw new IOException("bad block header");
        }
        this.storedBlockCRC = bsGetInt();
        this.blockRandomised = bsR(1L) == 1;
        if (this.data == null) {
            this.data = new Data(this.blockSize100k);
        }
        getAndMoveToFrontDecode();
        this.crc32.initialiseCRC();
        this.currentState = STATE.START_BLOCK_STATE;
    }

    private void endBlock() throws IOException {
        int computedBlockCRC = this.crc32.getFinalCRC();
        if (this.storedBlockCRC != computedBlockCRC) {
            this.computedCombinedCRC = (this.storedCombinedCRC << 1) | (this.storedCombinedCRC >>> 31);
            this.computedCombinedCRC ^= this.storedBlockCRC;
            throw new IOException("crc error");
        }
        this.computedCombinedCRC = (this.computedCombinedCRC << 1) | (this.computedCombinedCRC >>> 31);
        this.computedCombinedCRC ^= computedBlockCRC;
    }

    private void complete() throws IOException {
        this.storedCombinedCRC = bsGetInt();
        this.currentState = STATE.EOF;
        this.data = null;
        if (this.storedCombinedCRC != this.computedCombinedCRC) {
            throw new IOException("crc error");
        }
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        BufferedInputStream bufferedInputStream = this.in;
        if (bufferedInputStream != null) {
            try {
                if (bufferedInputStream != System.in) {
                    bufferedInputStream.close();
                }
            } finally {
                this.data = null;
                this.in = null;
            }
        }
    }

    private long bsR(final long n) throws IOException {
        long bsLiveShadow = this.bsLive;
        long bsBuffShadow = this.bsBuff;
        if (bsLiveShadow < n) {
            InputStream inShadow = this.in;
            do {
                int thech = readAByte(inShadow);
                if (thech < 0) {
                    throw new IOException("unexpected end of stream");
                }
                bsBuffShadow = (bsBuffShadow << 8) | thech;
                bsLiveShadow += 8;
            } while (bsLiveShadow < n);
            this.bsBuff = bsBuffShadow;
        }
        this.bsLive = bsLiveShadow - n;
        return (bsBuffShadow >> ((int) (bsLiveShadow - n))) & ((1 << ((int) n)) - 1);
    }

    private boolean bsGetBit() throws IOException {
        long bsLiveShadow = this.bsLive;
        long bsBuffShadow = this.bsBuff;
        if (bsLiveShadow < 1) {
            int thech = readAByte(this.in);
            if (thech < 0) {
                throw new IOException("unexpected end of stream");
            }
            bsBuffShadow = (bsBuffShadow << 8) | thech;
            bsLiveShadow += 8;
            this.bsBuff = bsBuffShadow;
        }
        this.bsLive = bsLiveShadow - 1;
        return (1 & (bsBuffShadow >> ((int) (bsLiveShadow - 1)))) != 0;
    }

    private char bsGetUByte() throws IOException {
        return (char) bsR(8L);
    }

    private int bsGetInt() throws IOException {
        return (int) (bsR(8L) | (((((bsR(8L) << 8) | bsR(8L)) << 8) | bsR(8L)) << 8));
    }

    private static void hbCreateDecodeTables(final int[] limit, final int[] base, final int[] perm, final char[] length, final int minLen, final int maxLen, final int alphaSize) {
        int pp = 0;
        for (int i = minLen; i <= maxLen; i++) {
            for (int j = 0; j < alphaSize; j++) {
                if (length[j] == i) {
                    perm[pp] = j;
                    pp++;
                }
            }
        }
        int i2 = 23;
        while (true) {
            i2--;
            if (i2 <= 0) {
                break;
            }
            base[i2] = 0;
            limit[i2] = 0;
        }
        for (int i3 = 0; i3 < alphaSize; i3++) {
            int i4 = length[i3] + 1;
            base[i4] = base[i4] + 1;
        }
        int b = base[0];
        for (int i5 = 1; i5 < 23; i5++) {
            b += base[i5];
            base[i5] = b;
        }
        int i6 = minLen;
        int vec = 0;
        int b2 = base[i6];
        while (i6 <= maxLen) {
            int nb = base[i6 + 1];
            int vec2 = vec + (nb - b2);
            b2 = nb;
            limit[i6] = vec2 - 1;
            vec = vec2 << 1;
            i6++;
        }
        for (int i7 = minLen + 1; i7 <= maxLen; i7++) {
            base[i7] = ((limit[i7 - 1] + 1) << 1) - base[i7];
        }
    }

    private void recvDecodingTables() throws IOException {
        int i;
        Data dataShadow = this.data;
        boolean[] inUse = dataShadow.inUse;
        byte[] pos = dataShadow.recvDecodingTablesPos;
        byte[] selector = dataShadow.selector;
        byte[] selectorMtf = dataShadow.selectorMtf;
        int inUse16 = 0;
        for (int i2 = 0; i2 < 16; i2++) {
            if (bsGetBit()) {
                inUse16 |= 1 << i2;
            }
        }
        int i3 = 256;
        while (true) {
            i = -1;
            i3--;
            if (i3 < 0) {
                break;
            } else {
                inUse[i3] = false;
            }
        }
        for (int i4 = 0; i4 < 16; i4++) {
            if (((1 << i4) & inUse16) != 0) {
                int i16 = i4 << 4;
                for (int j = 0; j < 16; j++) {
                    if (bsGetBit()) {
                        inUse[i16 + j] = true;
                    }
                }
            }
        }
        makeMaps();
        int alphaSize = this.nInUse + 2;
        int nGroups = (int) bsR(3L);
        int nSelectors = (int) bsR(15L);
        for (int i5 = 0; i5 < nSelectors; i5++) {
            int j2 = 0;
            while (bsGetBit()) {
                j2++;
            }
            selectorMtf[i5] = (byte) j2;
        }
        int v = nGroups;
        while (true) {
            v--;
            if (v < 0) {
                break;
            } else {
                pos[v] = (byte) v;
            }
        }
        for (int i6 = 0; i6 < nSelectors; i6++) {
            int v2 = selectorMtf[i6] & 255;
            byte tmp = pos[v2];
            while (v2 > 0) {
                pos[v2] = pos[v2 - 1];
                v2--;
            }
            pos[0] = tmp;
            selector[i6] = tmp;
        }
        char[][] len = dataShadow.tempCharArray2D;
        int t = 0;
        while (t < nGroups) {
            int curr = (int) bsR(5L);
            char[] lenT = len[t];
            int i7 = 0;
            while (i7 < alphaSize) {
                while (bsGetBit()) {
                    curr += bsGetBit() ? i : 1;
                }
                lenT[i7] = (char) curr;
                i7++;
                i = -1;
            }
            t++;
            i = -1;
        }
        createHuffmanDecodingTables(alphaSize, nGroups);
    }

    private void createHuffmanDecodingTables(final int alphaSize, final int nGroups) {
        Data dataShadow = this.data;
        char[][] len = dataShadow.tempCharArray2D;
        int[] minLens = dataShadow.minLens;
        int[][] limit = dataShadow.limit;
        int[][] base = dataShadow.base;
        int[][] perm = dataShadow.perm;
        for (int t = 0; t < nGroups; t++) {
            int minLen = 32;
            int maxLen = 0;
            char[] lenT = len[t];
            int i = alphaSize;
            while (true) {
                i--;
                if (i >= 0) {
                    char lent = lenT[i];
                    if (lent > maxLen) {
                        maxLen = lent;
                    }
                    if (lent < minLen) {
                        minLen = lent;
                    }
                }
            }
            hbCreateDecodeTables(limit[t], base[t], perm[t], len[t], minLen, maxLen, alphaSize);
            minLens[t] = minLen;
        }
    }

    private void getAndMoveToFrontDecode() throws IOException {
        int bsLiveShadow;
        int nextSym;
        InputStream inShadow;
        int lastShadow;
        int[][] base;
        int[][] perm;
        int lastShadow2;
        int nextSym2;
        int bsLiveShadow2;
        int zn;
        this.origPtr = (int) bsR(24L);
        recvDecodingTables();
        InputStream inShadow2 = this.in;
        Data dataShadow = this.data;
        byte[] ll8 = dataShadow.ll8;
        int[] unzftab = dataShadow.unzftab;
        byte[] selector = dataShadow.selector;
        byte[] seqToUnseq = dataShadow.seqToUnseq;
        char[] yy = dataShadow.getAndMoveToFrontDecodeYy;
        int[] minLens = dataShadow.minLens;
        int[][] limit = dataShadow.limit;
        int[][] base2 = dataShadow.base;
        int[][] perm2 = dataShadow.perm;
        int limitLast = this.blockSize100k * 100000;
        int i = 256;
        while (true) {
            i--;
            if (i < 0) {
                break;
            }
            yy[i] = (char) i;
            unzftab[i] = 0;
        }
        int eob = this.nInUse + 1;
        int nextSym3 = getAndMoveToFrontDecode0(0);
        InputStream inShadow3 = inShadow2;
        int bsBuffShadow = (int) this.bsBuff;
        int bsLiveShadow3 = (int) this.bsLive;
        int bsLiveShadow4 = selector[0];
        int zt = bsLiveShadow4 & 255;
        int[] baseZt = base2[zt];
        int[] limitZt = limit[zt];
        int[] permZt = perm2[zt];
        int minLensZt = minLens[zt];
        int bsLiveShadow5 = zt;
        int zvec = bsLiveShadow3;
        int groupPos = -1;
        int lastShadow3 = bsBuffShadow;
        int bsBuffShadow2 = 49;
        int groupNo = 0;
        int zn2 = nextSym3;
        while (zn2 != eob) {
            int eob2 = eob;
            int bsBuffShadow3 = lastShadow3;
            if (zn2 != 0) {
                bsLiveShadow = zvec;
                if (zn2 == 1) {
                    nextSym = zn2;
                    inShadow = inShadow3;
                } else {
                    int lastShadow4 = groupPos + 1;
                    if (lastShadow4 >= limitLast) {
                        throw new IOException("block overrun");
                    }
                    char tmp = yy[zn2 - 1];
                    int i2 = seqToUnseq[tmp] & 255;
                    unzftab[i2] = unzftab[i2] + 1;
                    ll8[lastShadow4] = seqToUnseq[tmp];
                    if (zn2 <= 16) {
                        int j = zn2 - 1;
                        while (j > 0) {
                            int j2 = j - 1;
                            yy[j] = yy[j2];
                            j = j2;
                        }
                        lastShadow2 = lastShadow4;
                        nextSym2 = 0;
                    } else {
                        int i3 = zn2 - 1;
                        lastShadow2 = lastShadow4;
                        nextSym2 = 0;
                        System.arraycopy(yy, 0, yy, 1, i3);
                    }
                    yy[nextSym2] = tmp;
                    if (bsBuffShadow2 == 0) {
                        groupNo++;
                        int zt2 = selector[groupNo] & 255;
                        int[] baseZt2 = base2[zt2];
                        int[] limitZt2 = limit[zt2];
                        int[] permZt2 = perm2[zt2];
                        limitZt = limitZt2;
                        permZt = permZt2;
                        minLensZt = minLens[zt2];
                        bsBuffShadow2 = 49;
                        bsLiveShadow5 = zt2;
                        baseZt = baseZt2;
                    } else {
                        bsBuffShadow2--;
                    }
                    int zn3 = minLensZt;
                    int bsLiveShadow6 = bsLiveShadow;
                    while (bsLiveShadow6 < zn3) {
                        InputStream inShadow4 = inShadow3;
                        int thech = readAByte(inShadow4);
                        if (thech < 0) {
                            throw new IOException("unexpected end of stream");
                        }
                        bsBuffShadow3 = (bsBuffShadow3 << 8) | thech;
                        bsLiveShadow6 += 8;
                        inShadow3 = inShadow4;
                    }
                    InputStream inShadow5 = inShadow3;
                    int zvec2 = (bsBuffShadow3 >> (bsLiveShadow6 - zn3)) & ((1 << zn3) - 1);
                    int bsLiveShadow7 = bsLiveShadow6 - zn3;
                    while (true) {
                        int bsLiveShadow8 = bsLiveShadow7;
                        int bsLiveShadow9 = limitZt[zn3];
                        if (zvec2 > bsLiveShadow9) {
                            int thech2 = zn3 + 1;
                            bsLiveShadow2 = bsLiveShadow8;
                            while (true) {
                                zn = thech2;
                                if (bsLiveShadow2 < 1) {
                                    int thech3 = readAByte(inShadow5);
                                    if (thech3 < 0) {
                                        throw new IOException("unexpected end of stream");
                                    }
                                    bsBuffShadow3 = (bsBuffShadow3 << 8) | thech3;
                                    bsLiveShadow2 += 8;
                                    thech2 = zn;
                                }
                            }
                        } else {
                            zn2 = permZt[zvec2 - baseZt[zn3]];
                            zvec = bsLiveShadow8;
                            eob = eob2;
                            lastShadow3 = bsBuffShadow3;
                            inShadow3 = inShadow5;
                            groupPos = lastShadow2;
                            break;
                        }
                        bsLiveShadow7 = bsLiveShadow2 - 1;
                        zvec2 = (zvec2 << 1) | ((bsBuffShadow3 >> bsLiveShadow7) & 1);
                        zn3 = zn;
                    }
                }
            } else {
                bsLiveShadow = zvec;
                nextSym = zn2;
                inShadow = inShadow3;
            }
            int i4 = groupNo;
            int s = -1;
            int zn4 = nextSym;
            int minLensZt2 = minLensZt;
            int[] permZt3 = permZt;
            int[] limitZt3 = limitZt;
            int[] baseZt3 = baseZt;
            int zt3 = bsLiveShadow5;
            int groupPos2 = bsBuffShadow2;
            int groupPos3 = 1;
            int groupNo2 = i4;
            while (true) {
                if (zn4 == 0) {
                    s += groupPos3;
                    lastShadow = groupPos;
                } else {
                    lastShadow = groupPos;
                    if (zn4 != 1) {
                        InputStream inShadow6 = inShadow;
                        byte[] selector2 = selector;
                        byte[] seqToUnseq2 = seqToUnseq;
                        int[] minLens2 = minLens;
                        int[][] limit2 = limit;
                        int[][] base3 = base2;
                        int[][] perm3 = perm2;
                        int nextSym4 = zn4;
                        byte ch = seqToUnseq2[yy[0]];
                        int i5 = ch & 255;
                        unzftab[i5] = unzftab[i5] + s + 1;
                        int lastShadow5 = lastShadow;
                        while (true) {
                            int s2 = s - 1;
                            if (s < 0) {
                                break;
                            }
                            lastShadow5++;
                            ll8[lastShadow5] = ch;
                            s = s2;
                        }
                        if (lastShadow5 >= limitLast) {
                            throw new IOException("block overrun");
                        }
                        base2 = base3;
                        seqToUnseq = seqToUnseq2;
                        groupNo = groupNo2;
                        bsBuffShadow2 = groupPos2;
                        bsLiveShadow5 = zt3;
                        baseZt = baseZt3;
                        limitZt = limitZt3;
                        permZt = permZt3;
                        eob = eob2;
                        lastShadow3 = bsBuffShadow3;
                        zvec = bsLiveShadow;
                        minLensZt = minLensZt2;
                        zn2 = nextSym4;
                        minLens = minLens2;
                        limit = limit2;
                        perm2 = perm3;
                        inShadow3 = inShadow6;
                        groupPos = lastShadow5;
                        selector = selector2;
                    } else {
                        s += groupPos3 << 1;
                    }
                }
                if (groupPos2 == 0) {
                    groupNo2++;
                    int nextSym5 = selector[groupNo2];
                    int zt4 = nextSym5 & 255;
                    baseZt3 = base2[zt4];
                    limitZt3 = limit[zt4];
                    permZt3 = perm2[zt4];
                    minLensZt2 = minLens[zt4];
                    zt3 = zt4;
                    groupPos2 = 49;
                } else {
                    groupPos2--;
                }
                int zn5 = minLensZt2;
                int bsLiveShadow10 = bsLiveShadow;
                while (bsLiveShadow10 < zn5) {
                    int thech4 = readAByte(inShadow);
                    if (thech4 >= 0) {
                        bsBuffShadow3 = (bsBuffShadow3 << 8) | thech4;
                        bsLiveShadow10 += 8;
                    } else {
                        throw new IOException("unexpected end of stream");
                    }
                }
                byte[] selector3 = selector;
                int[] minLens3 = minLens;
                int[][] limit3 = limit;
                long zvec3 = (bsBuffShadow3 >> (bsLiveShadow10 - zn5)) & ((1 << zn5) - 1);
                bsLiveShadow = bsLiveShadow10 - zn5;
                while (true) {
                    base = base2;
                    perm = perm2;
                    if (zvec3 > limitZt3[zn5]) {
                        zn5++;
                        int bsLiveShadow11 = bsLiveShadow;
                        while (bsLiveShadow11 < 1) {
                            int thech5 = readAByte(inShadow);
                            if (thech5 >= 0) {
                                bsBuffShadow3 = (bsBuffShadow3 << 8) | thech5;
                                bsLiveShadow11 += 8;
                            } else {
                                throw new IOException("unexpected end of stream");
                            }
                        }
                        bsLiveShadow = bsLiveShadow11 - 1;
                        zvec3 = (zvec3 << 1) | ((bsBuffShadow3 >> bsLiveShadow) & 1);
                        base2 = base;
                        seqToUnseq = seqToUnseq;
                        perm2 = perm;
                        inShadow = inShadow;
                    }
                }
                zn4 = permZt3[(int) (zvec3 - baseZt3[zn5])];
                groupPos3 <<= 1;
                base2 = base;
                seqToUnseq = seqToUnseq;
                groupPos = lastShadow;
                selector = selector3;
                minLens = minLens3;
                limit = limit3;
                perm2 = perm;
                inShadow = inShadow;
            }
        }
        int bsBuffShadow4 = lastShadow3;
        int lastShadow6 = groupPos;
        this.last = lastShadow6;
        this.bsLive = zvec;
        this.bsBuff = bsBuffShadow4;
    }

    private int getAndMoveToFrontDecode0(final int groupNo) throws IOException {
        InputStream inShadow = this.in;
        Data dataShadow = this.data;
        int zt = dataShadow.selector[groupNo] & 255;
        int[] limitZt = dataShadow.limit[zt];
        int zn = dataShadow.minLens[zt];
        int zvec = (int) bsR(zn);
        int bsLiveShadow = (int) this.bsLive;
        int bsBuffShadow = (int) this.bsBuff;
        while (zvec > limitZt[zn]) {
            zn++;
            while (bsLiveShadow < 1) {
                int thech = readAByte(inShadow);
                if (thech >= 0) {
                    bsBuffShadow = (bsBuffShadow << 8) | thech;
                    bsLiveShadow += 8;
                } else {
                    throw new IOException("unexpected end of stream");
                }
            }
            bsLiveShadow--;
            zvec = (zvec << 1) | (1 & (bsBuffShadow >> bsLiveShadow));
        }
        this.bsLive = bsLiveShadow;
        this.bsBuff = bsBuffShadow;
        return dataShadow.perm[zt][zvec - dataShadow.base[zt][zn]];
    }

    private void setupBlock() throws IOException {
        if (this.data == null) {
            return;
        }
        int[] cftab = this.data.cftab;
        int[] tt = this.data.initTT(this.last + 1);
        byte[] ll8 = this.data.ll8;
        cftab[0] = 0;
        System.arraycopy(this.data.unzftab, 0, cftab, 1, 256);
        int c = cftab[0];
        for (int i = 1; i <= 256; i++) {
            c += cftab[i];
            cftab[i] = c;
        }
        int lastShadow = this.last;
        for (int i2 = 0; i2 <= lastShadow; i2++) {
            int i3 = ll8[i2] & 255;
            int i4 = cftab[i3];
            cftab[i3] = i4 + 1;
            tt[i4] = i2;
        }
        int i5 = this.origPtr;
        if (i5 < 0 || this.origPtr >= tt.length) {
            throw new IOException("stream corrupted");
        }
        this.suTPos = tt[this.origPtr];
        this.suCount = 0;
        this.suI2 = 0;
        this.suCh2 = 256;
        if (this.blockRandomised) {
            this.suRNToGo = 0;
            this.suRTPos = 0;
            setupRandPartA();
            return;
        }
        setupNoRandPartA();
    }

    private void setupRandPartA() throws IOException {
        if (this.suI2 <= this.last) {
            this.suChPrev = this.suCh2;
            int suCh2Shadow = this.data.ll8[this.suTPos] & 255;
            this.suTPos = this.data.tt[this.suTPos];
            if (this.suRNToGo == 0) {
                this.suRNToGo = R_NUMS[this.suRTPos] - 1;
                int i = this.suRTPos + 1;
                this.suRTPos = i;
                if (i == 512) {
                    this.suRTPos = 0;
                }
            } else {
                this.suRNToGo--;
            }
            int suCh2Shadow2 = suCh2Shadow ^ (this.suRNToGo == 1 ? 1 : 0);
            this.suCh2 = suCh2Shadow2;
            this.suI2++;
            this.currentChar = suCh2Shadow2;
            this.currentState = STATE.RAND_PART_B_STATE;
            this.crc32.updateCRC(suCh2Shadow2);
            return;
        }
        endBlock();
        initBlock();
        setupBlock();
    }

    private void setupNoRandPartA() throws IOException {
        if (this.suI2 <= this.last) {
            this.suChPrev = this.suCh2;
            int suCh2Shadow = this.data.ll8[this.suTPos] & 255;
            this.suCh2 = suCh2Shadow;
            this.suTPos = this.data.tt[this.suTPos];
            this.suI2++;
            this.currentChar = suCh2Shadow;
            this.currentState = STATE.NO_RAND_PART_B_STATE;
            this.crc32.updateCRC(suCh2Shadow);
            return;
        }
        this.currentState = STATE.NO_RAND_PART_A_STATE;
        endBlock();
        initBlock();
        setupBlock();
    }

    private void setupRandPartB() throws IOException {
        if (this.suCh2 != this.suChPrev) {
            this.currentState = STATE.RAND_PART_A_STATE;
            this.suCount = 1;
            setupRandPartA();
            return;
        }
        int i = this.suCount + 1;
        this.suCount = i;
        if (i >= 4) {
            this.suZ = (char) (this.data.ll8[this.suTPos] & 255);
            this.suTPos = this.data.tt[this.suTPos];
            if (this.suRNToGo == 0) {
                this.suRNToGo = R_NUMS[this.suRTPos] - 1;
                int i2 = this.suRTPos + 1;
                this.suRTPos = i2;
                if (i2 == 512) {
                    this.suRTPos = 0;
                }
            } else {
                this.suRNToGo--;
            }
            this.suJ2 = 0;
            this.currentState = STATE.RAND_PART_C_STATE;
            if (this.suRNToGo == 1) {
                this.suZ = (char) (this.suZ ^ 1);
            }
            setupRandPartC();
            return;
        }
        this.currentState = STATE.RAND_PART_A_STATE;
        setupRandPartA();
    }

    private void setupRandPartC() throws IOException {
        if (this.suJ2 < this.suZ) {
            this.currentChar = this.suCh2;
            this.crc32.updateCRC(this.suCh2);
            this.suJ2++;
        } else {
            this.currentState = STATE.RAND_PART_A_STATE;
            this.suI2++;
            this.suCount = 0;
            setupRandPartA();
        }
    }

    private void setupNoRandPartB() throws IOException {
        if (this.suCh2 != this.suChPrev) {
            this.suCount = 1;
            setupNoRandPartA();
            return;
        }
        int i = this.suCount + 1;
        this.suCount = i;
        if (i >= 4) {
            this.suZ = (char) (this.data.ll8[this.suTPos] & 255);
            this.suTPos = this.data.tt[this.suTPos];
            this.suJ2 = 0;
            setupNoRandPartC();
            return;
        }
        setupNoRandPartA();
    }

    private void setupNoRandPartC() throws IOException {
        if (this.suJ2 < this.suZ) {
            int suCh2Shadow = this.suCh2;
            this.currentChar = suCh2Shadow;
            this.crc32.updateCRC(suCh2Shadow);
            this.suJ2++;
            this.currentState = STATE.NO_RAND_PART_C_STATE;
            return;
        }
        this.suI2++;
        this.suCount = 0;
        setupNoRandPartA();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class Data {
        byte[] ll8;
        int[] tt;
        final boolean[] inUse = new boolean[256];
        final byte[] seqToUnseq = new byte[256];
        final byte[] selector = new byte[BZip2Constants.MAX_SELECTORS];
        final byte[] selectorMtf = new byte[BZip2Constants.MAX_SELECTORS];
        final int[] unzftab = new int[256];
        final int[][] limit = (int[][]) Array.newInstance((Class<?>) Integer.TYPE, 6, 258);
        final int[][] base = (int[][]) Array.newInstance((Class<?>) Integer.TYPE, 6, 258);
        final int[][] perm = (int[][]) Array.newInstance((Class<?>) Integer.TYPE, 6, 258);
        final int[] minLens = new int[6];
        final int[] cftab = new int[257];
        final char[] getAndMoveToFrontDecodeYy = new char[256];
        final char[][] tempCharArray2D = (char[][]) Array.newInstance((Class<?>) Character.TYPE, 6, 258);
        final byte[] recvDecodingTablesPos = new byte[6];

        Data(int blockSize100k) {
            this.ll8 = new byte[100000 * blockSize100k];
        }

        int[] initTT(int length) {
            int[] ttShadow = this.tt;
            if (ttShadow == null || ttShadow.length < length) {
                int[] ttShadow2 = new int[length];
                this.tt = ttShadow2;
                return ttShadow2;
            }
            return ttShadow;
        }
    }
}
