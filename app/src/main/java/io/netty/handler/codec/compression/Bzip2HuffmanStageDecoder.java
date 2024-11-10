package io.netty.handler.codec.compression;

import java.lang.reflect.Array;

/* loaded from: classes4.dex */
final class Bzip2HuffmanStageDecoder {
    final int alphabetSize;
    private final int[][] codeBases;
    private final int[][] codeLimits;
    private final int[][] codeSymbols;
    int currentAlpha;
    int currentGroup;
    int currentSelector;
    private int currentTable;
    private final int[] minimumLengths;
    boolean modifyLength;
    private final Bzip2BitReader reader;
    byte[] selectors;
    final byte[][] tableCodeLengths;
    final int totalTables;
    private int groupIndex = -1;
    private int groupPosition = -1;
    final Bzip2MoveToFrontTable tableMTF = new Bzip2MoveToFrontTable();
    int currentLength = -1;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Bzip2HuffmanStageDecoder(Bzip2BitReader reader, int totalTables, int alphabetSize) {
        this.reader = reader;
        this.totalTables = totalTables;
        this.alphabetSize = alphabetSize;
        this.minimumLengths = new int[totalTables];
        this.codeBases = (int[][]) Array.newInstance((Class<?>) Integer.TYPE, totalTables, 25);
        this.codeLimits = (int[][]) Array.newInstance((Class<?>) Integer.TYPE, totalTables, 24);
        this.codeSymbols = (int[][]) Array.newInstance((Class<?>) Integer.TYPE, totalTables, 258);
        this.tableCodeLengths = (byte[][]) Array.newInstance((Class<?>) Byte.TYPE, totalTables, 258);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void createHuffmanDecodingTables() {
        int alphabetSize = this.alphabetSize;
        for (int table = 0; table < this.tableCodeLengths.length; table++) {
            int[] tableBases = this.codeBases[table];
            int[] tableLimits = this.codeLimits[table];
            int[] tableSymbols = this.codeSymbols[table];
            byte[] codeLengths = this.tableCodeLengths[table];
            int minimumLength = 23;
            int maximumLength = 0;
            for (int i = 0; i < alphabetSize; i++) {
                byte currLength = codeLengths[i];
                maximumLength = Math.max((int) currLength, maximumLength);
                minimumLength = Math.min((int) currLength, minimumLength);
            }
            this.minimumLengths[table] = minimumLength;
            for (int i2 = 0; i2 < alphabetSize; i2++) {
                int i3 = codeLengths[i2] + 1;
                tableBases[i3] = tableBases[i3] + 1;
            }
            int b = tableBases[0];
            for (int i4 = 1; i4 < 25; i4++) {
                b += tableBases[i4];
                tableBases[i4] = b;
            }
            int code = 0;
            for (int i5 = minimumLength; i5 <= maximumLength; i5++) {
                int base = code;
                int code2 = code + (tableBases[i5 + 1] - tableBases[i5]);
                tableBases[i5] = base - tableBases[i5];
                tableLimits[i5] = code2 - 1;
                code = code2 << 1;
            }
            int codeIndex = 0;
            for (int bitLength = minimumLength; bitLength <= maximumLength; bitLength++) {
                for (int symbol = 0; symbol < alphabetSize; symbol++) {
                    if (codeLengths[symbol] == bitLength) {
                        tableSymbols[codeIndex] = symbol;
                        codeIndex++;
                    }
                }
            }
        }
        this.currentTable = this.selectors[0];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int nextSymbol() {
        int i = this.groupPosition + 1;
        this.groupPosition = i;
        if (i % 50 == 0) {
            this.groupIndex++;
            if (this.groupIndex == this.selectors.length) {
                throw new DecompressionException("error decoding block");
            }
            this.currentTable = this.selectors[this.groupIndex] & 255;
        }
        Bzip2BitReader reader = this.reader;
        int currentTable = this.currentTable;
        int[] tableLimits = this.codeLimits[currentTable];
        int[] tableBases = this.codeBases[currentTable];
        int[] tableSymbols = this.codeSymbols[currentTable];
        int codeLength = this.minimumLengths[currentTable];
        int codeBits = reader.readBits(codeLength);
        while (codeLength <= 23) {
            if (codeBits <= tableLimits[codeLength]) {
                return tableSymbols[codeBits - tableBases[codeLength]];
            }
            codeBits = (codeBits << 1) | reader.readBits(1);
            codeLength++;
        }
        throw new DecompressionException("a valid code was not recognised");
    }
}
