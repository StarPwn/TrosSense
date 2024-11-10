package io.netty.handler.codec.compression;

/* loaded from: classes4.dex */
final class Bzip2MTFAndRLE2StageEncoder {
    private int alphabetSize;
    private final int[] bwtBlock;
    private final int bwtLength;
    private final boolean[] bwtValuesPresent;
    private final char[] mtfBlock;
    private int mtfLength;
    private final int[] mtfSymbolFrequencies = new int[258];

    /* JADX INFO: Access modifiers changed from: package-private */
    public Bzip2MTFAndRLE2StageEncoder(int[] bwtBlock, int bwtLength, boolean[] bwtValuesPresent) {
        this.bwtBlock = bwtBlock;
        this.bwtLength = bwtLength;
        this.bwtValuesPresent = bwtValuesPresent;
        this.mtfBlock = new char[bwtLength + 1];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void encode() {
        int i;
        int bwtLength;
        int bwtLength2;
        int bwtLength3 = this.bwtLength;
        boolean[] bwtValuesPresent = this.bwtValuesPresent;
        int[] bwtBlock = this.bwtBlock;
        char[] mtfBlock = this.mtfBlock;
        int[] mtfSymbolFrequencies = this.mtfSymbolFrequencies;
        byte[] huffmanSymbolMap = new byte[256];
        Bzip2MoveToFrontTable symbolMTF = new Bzip2MoveToFrontTable();
        int totalUniqueValues = 0;
        for (int i2 = 0; i2 < huffmanSymbolMap.length; i2++) {
            if (bwtValuesPresent[i2]) {
                huffmanSymbolMap[i2] = (byte) totalUniqueValues;
                totalUniqueValues++;
            }
        }
        int i3 = totalUniqueValues + 1;
        int mtfIndex = 0;
        int repeatCount = 0;
        int totalRunAs = 0;
        int totalRunBs = 0;
        int i4 = 0;
        while (i4 < bwtLength3) {
            int mtfPosition = symbolMTF.valueToFront(huffmanSymbolMap[bwtBlock[i4] & 255]);
            if (mtfPosition == 0) {
                repeatCount++;
                bwtLength = bwtLength3;
            } else {
                if (repeatCount <= 0) {
                    bwtLength = bwtLength3;
                } else {
                    int repeatCount2 = repeatCount - 1;
                    while (true) {
                        if ((repeatCount2 & 1) == 0) {
                            mtfBlock[mtfIndex] = 0;
                            totalRunAs++;
                            bwtLength = bwtLength3;
                            mtfIndex++;
                            bwtLength2 = 1;
                        } else {
                            bwtLength = bwtLength3;
                            bwtLength2 = 1;
                            mtfBlock[mtfIndex] = 1;
                            totalRunBs++;
                            mtfIndex++;
                        }
                        if (repeatCount2 <= bwtLength2) {
                            break;
                        }
                        repeatCount2 = (repeatCount2 - 2) >>> 1;
                        bwtLength3 = bwtLength;
                    }
                    repeatCount = 0;
                }
                int bwtLength4 = mtfIndex + 1;
                int mtfIndex2 = mtfPosition + 1;
                mtfBlock[mtfIndex] = (char) mtfIndex2;
                int i5 = mtfPosition + 1;
                mtfSymbolFrequencies[i5] = mtfSymbolFrequencies[i5] + 1;
                mtfIndex = bwtLength4;
            }
            i4++;
            bwtLength3 = bwtLength;
        }
        if (repeatCount <= 0) {
            i = 1;
        } else {
            int repeatCount3 = repeatCount - 1;
            while (true) {
                if ((repeatCount3 & 1) == 0) {
                    mtfBlock[mtfIndex] = 0;
                    totalRunAs++;
                    mtfIndex++;
                    i = 1;
                } else {
                    i = 1;
                    mtfBlock[mtfIndex] = 1;
                    totalRunBs++;
                    mtfIndex++;
                }
                if (repeatCount3 <= i) {
                    break;
                } else {
                    repeatCount3 = (repeatCount3 - 2) >>> 1;
                }
            }
        }
        mtfBlock[mtfIndex] = (char) i3;
        mtfSymbolFrequencies[i3] = mtfSymbolFrequencies[i3] + i;
        mtfSymbolFrequencies[0] = mtfSymbolFrequencies[0] + totalRunAs;
        mtfSymbolFrequencies[i] = mtfSymbolFrequencies[i] + totalRunBs;
        this.mtfLength = mtfIndex + 1;
        this.alphabetSize = i3 + 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public char[] mtfBlock() {
        return this.mtfBlock;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int mtfLength() {
        return this.mtfLength;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int mtfAlphabetSize() {
        return this.alphabetSize;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int[] mtfSymbolFrequencies() {
        return this.mtfSymbolFrequencies;
    }
}
