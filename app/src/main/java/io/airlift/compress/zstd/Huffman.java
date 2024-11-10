package io.airlift.compress.zstd;

import io.airlift.compress.zstd.BitInputStream;
import io.airlift.compress.zstd.FiniteStateEntropy;
import java.util.Arrays;
import kotlin.UShort;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class Huffman {
    public static final int MAX_FSE_TABLE_LOG = 6;
    public static final int MAX_SYMBOL = 255;
    public static final int MAX_SYMBOL_COUNT = 256;
    public static final int MAX_TABLE_LOG = 12;
    public static final int MIN_TABLE_LOG = 5;
    private final byte[] weights = new byte[256];
    private final int[] ranks = new int[13];
    private int tableLog = -1;
    private final byte[] symbols = new byte[4096];
    private final byte[] numbersOfBits = new byte[4096];
    private final FseTableReader reader = new FseTableReader();
    private final FiniteStateEntropy.Table fseTable = new FiniteStateEntropy.Table(6);

    public boolean isLoaded() {
        return this.tableLog != -1;
    }

    public int readTable(final Object inputBase, final long inputAddress, final int size) {
        String str;
        int outputSize;
        int inputSize;
        int rest;
        Arrays.fill(this.ranks, 0);
        int i = 1;
        Util.verify(size > 0, inputAddress, "Not enough input bytes");
        long input = inputAddress + 1;
        int inputSize2 = UnsafeUtil.UNSAFE.getByte(inputBase, inputAddress) & 255;
        if (inputSize2 >= 128) {
            outputSize = inputSize2 - 127;
            inputSize = (outputSize + 1) / 2;
            Util.verify(inputSize + 1 <= size, input, "Not enough input bytes");
            Util.verify(outputSize <= 256, input, "Input is corrupted");
            for (int i2 = 0; i2 < outputSize; i2 += 2) {
                int value = UnsafeUtil.UNSAFE.getByte(inputBase, (i2 / 2) + input) & 255;
                this.weights[i2] = (byte) (value >>> 4);
                this.weights[i2 + 1] = (byte) (value & 15);
            }
            str = "Input is corrupted";
        } else {
            int outputSize2 = inputSize2 + 1;
            Util.verify(outputSize2 <= size, input, "Not enough input bytes");
            long inputLimit = input + inputSize2;
            str = "Input is corrupted";
            input += this.reader.readFseTable(this.fseTable, inputBase, input, inputLimit, 255, 6);
            outputSize = FiniteStateEntropy.decompress(this.fseTable, inputBase, input, inputLimit, this.weights);
            inputSize = inputSize2;
        }
        int totalWeight = 0;
        for (int i3 = 0; i3 < outputSize; i3++) {
            int[] iArr = this.ranks;
            byte b = this.weights[i3];
            iArr[b] = iArr[b] + 1;
            totalWeight += (1 << this.weights[i3]) >> 1;
        }
        Util.verify(totalWeight != 0, input, str);
        this.tableLog = Util.highestBit(totalWeight) + 1;
        Util.verify(this.tableLog <= 12, input, str);
        int total = 1 << this.tableLog;
        int rest2 = total - totalWeight;
        Util.verify(Util.isPowerOf2(rest2), input, str);
        int lastWeight = Util.highestBit(rest2) + 1;
        this.weights[outputSize] = (byte) lastWeight;
        int[] iArr2 = this.ranks;
        iArr2[lastWeight] = iArr2[lastWeight] + 1;
        int numberOfSymbols = outputSize + 1;
        int nextRankStart = 0;
        int i4 = 1;
        while (true) {
            int outputSize3 = outputSize;
            int outputSize4 = this.tableLog;
            if (i4 >= outputSize4 + i) {
                break;
            }
            int current = nextRankStart;
            nextRankStart += this.ranks[i4] << (i4 - 1);
            this.ranks[i4] = current;
            i4++;
            outputSize = outputSize3;
            i = 1;
        }
        int n = 0;
        while (n < numberOfSymbols) {
            int weight = this.weights[n];
            int length = (1 << weight) >> 1;
            byte symbol = (byte) n;
            int totalWeight2 = totalWeight;
            int totalWeight3 = this.tableLog;
            byte numberOfBits = (byte) ((totalWeight3 + 1) - weight);
            int total2 = total;
            int i5 = this.ranks[weight];
            while (true) {
                rest = rest2;
                if (i5 < this.ranks[weight] + length) {
                    this.symbols[i5] = symbol;
                    this.numbersOfBits[i5] = numberOfBits;
                    i5++;
                    rest2 = rest;
                }
            }
            int[] iArr3 = this.ranks;
            iArr3[weight] = iArr3[weight] + length;
            n++;
            totalWeight = totalWeight2;
            total = total2;
            rest2 = rest;
        }
        Util.verify(this.ranks[1] >= 2 && (this.ranks[1] & 1) == 0, input, str);
        return inputSize + 1;
    }

    public void decodeSingleStream(final Object inputBase, final long inputAddress, final long inputLimit, final Object outputBase, final long outputAddress, final long outputLimit) {
        long bits;
        int bitsConsumed;
        long currentAddress;
        BitInputStream.Initializer initializer = new BitInputStream.Initializer(inputBase, inputAddress, inputLimit);
        initializer.initialize();
        long bits2 = initializer.getBits();
        int bitsConsumed2 = initializer.getBitsConsumed();
        long currentAddress2 = initializer.getCurrentAddress();
        int tableLog = this.tableLog;
        byte[] bArr = this.numbersOfBits;
        byte[] symbols = this.symbols;
        long fastOutputLimit = outputLimit - 4;
        long output = outputAddress;
        while (true) {
            if (output >= fastOutputLimit) {
                bits = bits2;
                bitsConsumed = bitsConsumed2;
                currentAddress = currentAddress2;
                break;
            }
            BitInputStream.Loader loader = new BitInputStream.Loader(inputBase, inputAddress, currentAddress2, bits2, bitsConsumed2);
            boolean done = loader.load();
            bits2 = loader.getBits();
            int bitsConsumed3 = loader.getBitsConsumed();
            currentAddress2 = loader.getCurrentAddress();
            if (done) {
                bits = bits2;
                bitsConsumed = bitsConsumed3;
                currentAddress = currentAddress2;
                break;
            }
            byte[] symbols2 = symbols;
            int tableLog2 = tableLog;
            bitsConsumed2 = decodeSymbol(outputBase, output + 3, bits2, decodeSymbol(outputBase, output + 2, bits2, decodeSymbol(outputBase, output + 1, bits2, decodeSymbol(outputBase, output, bits2, bitsConsumed3, tableLog, bArr, symbols2), tableLog2, bArr, symbols2), tableLog2, bArr, symbols2), tableLog2, bArr, symbols2);
            output += 4;
            symbols = symbols2;
            tableLog = tableLog2;
        }
        decodeTail(inputBase, inputAddress, currentAddress, bitsConsumed, bits, outputBase, output, outputLimit);
    }

    public void decode4Streams(final Object inputBase, final long inputAddress, final long inputLimit, final Object outputBase, final long outputAddress, final long outputLimit) {
        int stream4bitsConsumed;
        long output1;
        int stream3bitsConsumed;
        int stream1bitsConsumed;
        int stream2bitsConsumed;
        Util.verify(inputLimit - inputAddress >= 10, inputAddress, "Input is corrupted");
        long start1 = inputAddress + 6;
        long start2 = start1 + (UnsafeUtil.UNSAFE.getShort(inputBase, inputAddress) & UShort.MAX_VALUE);
        long start3 = start2 + (UnsafeUtil.UNSAFE.getShort(inputBase, inputAddress + 2) & UShort.MAX_VALUE);
        long start4 = start3 + (UnsafeUtil.UNSAFE.getShort(inputBase, inputAddress + 4) & UShort.MAX_VALUE);
        BitInputStream.Initializer initializer = new BitInputStream.Initializer(inputBase, start1, start2);
        initializer.initialize();
        int stream1bitsConsumed2 = initializer.getBitsConsumed();
        long stream1currentAddress = initializer.getCurrentAddress();
        long stream1bits = initializer.getBits();
        BitInputStream.Initializer initializer2 = new BitInputStream.Initializer(inputBase, start2, start3);
        initializer2.initialize();
        int stream2bitsConsumed2 = initializer2.getBitsConsumed();
        long stream2currentAddress = initializer2.getCurrentAddress();
        long stream2bits = initializer2.getBits();
        BitInputStream.Initializer initializer3 = new BitInputStream.Initializer(inputBase, start3, start4);
        initializer3.initialize();
        int stream3bitsConsumed2 = initializer3.getBitsConsumed();
        long stream3currentAddress = initializer3.getCurrentAddress();
        long stream3bits = initializer3.getBits();
        BitInputStream.Initializer initializer4 = new BitInputStream.Initializer(inputBase, start4, inputLimit);
        initializer4.initialize();
        int stream4bitsConsumed2 = initializer4.getBitsConsumed();
        long stream4currentAddress = initializer4.getCurrentAddress();
        long stream4bits = initializer4.getBits();
        int segmentSize = (int) (((outputLimit - outputAddress) + 3) / 4);
        long outputStart2 = outputAddress + segmentSize;
        long outputStart3 = outputStart2 + segmentSize;
        long outputStart4 = outputStart3 + segmentSize;
        long fastOutputLimit = outputLimit - 7;
        int tableLog = this.tableLog;
        byte[] numbersOfBits = this.numbersOfBits;
        byte[] symbols = this.symbols;
        long output3 = outputStart3;
        long output4 = outputStart4;
        long stream3bits2 = stream3bits;
        long output2 = outputStart2;
        long stream3currentAddress2 = stream3currentAddress;
        long stream2bits2 = stream2bits;
        long stream1bits2 = stream1bits;
        long stream4bits2 = stream4bits;
        int stream3bitsConsumed3 = stream3bitsConsumed2;
        long stream2currentAddress2 = stream2currentAddress;
        long stream1currentAddress2 = stream1currentAddress;
        long stream4currentAddress2 = stream4currentAddress;
        long stream4currentAddress3 = outputAddress;
        while (true) {
            if (output4 >= fastOutputLimit) {
                stream4bitsConsumed = stream4bitsConsumed2;
                output1 = stream4currentAddress3;
                stream3bitsConsumed = stream3bitsConsumed3;
                stream1bitsConsumed = stream1bitsConsumed2;
                stream2bitsConsumed = stream2bitsConsumed2;
                break;
            }
            int i = tableLog;
            int stream1bitsConsumed3 = decodeSymbol(outputBase, stream4currentAddress3, stream1bits2, stream1bitsConsumed2, i, numbersOfBits, symbols);
            int stream2bitsConsumed3 = decodeSymbol(outputBase, output2, stream2bits2, stream2bitsConsumed2, i, numbersOfBits, symbols);
            int stream3bitsConsumed4 = decodeSymbol(outputBase, output3, stream3bits2, stream3bitsConsumed3, i, numbersOfBits, symbols);
            int stream4bitsConsumed3 = decodeSymbol(outputBase, output4, stream4bits2, stream4bitsConsumed2, i, numbersOfBits, symbols);
            int stream1bitsConsumed4 = decodeSymbol(outputBase, stream4currentAddress3 + 1, stream1bits2, stream1bitsConsumed3, i, numbersOfBits, symbols);
            int stream2bitsConsumed4 = decodeSymbol(outputBase, output2 + 1, stream2bits2, stream2bitsConsumed3, i, numbersOfBits, symbols);
            int stream3bitsConsumed5 = decodeSymbol(outputBase, output3 + 1, stream3bits2, stream3bitsConsumed4, i, numbersOfBits, symbols);
            int stream4bitsConsumed4 = decodeSymbol(outputBase, output4 + 1, stream4bits2, stream4bitsConsumed3, i, numbersOfBits, symbols);
            int stream1bitsConsumed5 = decodeSymbol(outputBase, stream4currentAddress3 + 2, stream1bits2, stream1bitsConsumed4, i, numbersOfBits, symbols);
            int stream2bitsConsumed5 = decodeSymbol(outputBase, output2 + 2, stream2bits2, stream2bitsConsumed4, i, numbersOfBits, symbols);
            int stream3bitsConsumed6 = decodeSymbol(outputBase, output3 + 2, stream3bits2, stream3bitsConsumed5, i, numbersOfBits, symbols);
            int stream4bitsConsumed5 = decodeSymbol(outputBase, output4 + 2, stream4bits2, stream4bitsConsumed4, i, numbersOfBits, symbols);
            int stream1bitsConsumed6 = decodeSymbol(outputBase, stream4currentAddress3 + 3, stream1bits2, stream1bitsConsumed5, i, numbersOfBits, symbols);
            stream2bitsConsumed = decodeSymbol(outputBase, output2 + 3, stream2bits2, stream2bitsConsumed5, i, numbersOfBits, symbols);
            stream3bitsConsumed = decodeSymbol(outputBase, output3 + 3, stream3bits2, stream3bitsConsumed6, i, numbersOfBits, symbols);
            stream4bitsConsumed = decodeSymbol(outputBase, output4 + 3, stream4bits2, stream4bitsConsumed5, i, numbersOfBits, symbols);
            output1 = stream4currentAddress3 + 4;
            output2 += 4;
            output3 += 4;
            output4 += 4;
            int tableLog2 = tableLog;
            int segmentSize2 = segmentSize;
            BitInputStream.Loader loader = new BitInputStream.Loader(inputBase, start1, stream1currentAddress2, stream1bits2, stream1bitsConsumed6);
            boolean done = loader.load();
            int stream1bitsConsumed7 = loader.getBitsConsumed();
            stream1bits2 = loader.getBits();
            stream1currentAddress2 = loader.getCurrentAddress();
            if (done) {
                stream1bitsConsumed = stream1bitsConsumed7;
                break;
            }
            BitInputStream.Loader loader2 = new BitInputStream.Loader(inputBase, start2, stream2currentAddress2, stream2bits2, stream2bitsConsumed);
            boolean done2 = loader2.load();
            stream2bitsConsumed = loader2.getBitsConsumed();
            stream2bits2 = loader2.getBits();
            stream2currentAddress2 = loader2.getCurrentAddress();
            if (done2) {
                stream1bitsConsumed = stream1bitsConsumed7;
                break;
            }
            BitInputStream.Loader loader3 = new BitInputStream.Loader(inputBase, start3, stream3currentAddress2, stream3bits2, stream3bitsConsumed);
            boolean done3 = loader3.load();
            int stream3bitsConsumed7 = loader3.getBitsConsumed();
            stream3bits2 = loader3.getBits();
            stream3currentAddress2 = loader3.getCurrentAddress();
            if (done3) {
                stream1bitsConsumed = stream1bitsConsumed7;
                stream3bitsConsumed = stream3bitsConsumed7;
                break;
            }
            BitInputStream.Loader loader4 = new BitInputStream.Loader(inputBase, start4, stream4currentAddress2, stream4bits2, stream4bitsConsumed);
            boolean done4 = loader4.load();
            int stream4bitsConsumed6 = loader4.getBitsConsumed();
            stream4bits2 = loader4.getBits();
            stream4currentAddress2 = loader4.getCurrentAddress();
            if (done4) {
                stream4bitsConsumed = stream4bitsConsumed6;
                stream1bitsConsumed = stream1bitsConsumed7;
                stream3bitsConsumed = stream3bitsConsumed7;
                break;
            } else {
                stream4bitsConsumed2 = stream4bitsConsumed6;
                stream1bitsConsumed2 = stream1bitsConsumed7;
                stream2bitsConsumed2 = stream2bitsConsumed;
                stream4currentAddress3 = output1;
                tableLog = tableLog2;
                segmentSize = segmentSize2;
                stream3bitsConsumed3 = stream3bitsConsumed7;
            }
        }
        Util.verify(output1 <= outputStart2 && output2 <= outputStart3 && output3 <= outputStart4, inputAddress, "Input is corrupted");
        decodeTail(inputBase, start1, stream1currentAddress2, stream1bitsConsumed, stream1bits2, outputBase, output1, outputStart2);
        decodeTail(inputBase, start2, stream2currentAddress2, stream2bitsConsumed, stream2bits2, outputBase, output2, outputStart3);
        decodeTail(inputBase, start3, stream3currentAddress2, stream3bitsConsumed, stream3bits2, outputBase, output3, outputStart4);
        decodeTail(inputBase, start4, stream4currentAddress2, stream4bitsConsumed, stream4bits2, outputBase, output4, outputLimit);
    }

    private void decodeTail(final Object inputBase, final long startAddress, long currentAddress, int bitsConsumed, long bits, final Object outputBase, long outputAddress, final long outputLimit) {
        long currentAddress2;
        int bitsConsumed2;
        long outputAddress2;
        int tableLog = this.tableLog;
        byte[] numbersOfBits = this.numbersOfBits;
        byte[] symbols = this.symbols;
        long currentAddress3 = currentAddress;
        int bitsConsumed3 = bitsConsumed;
        long bits2 = bits;
        long outputAddress3 = outputAddress;
        while (true) {
            if (outputAddress3 >= outputLimit) {
                currentAddress2 = currentAddress3;
                bitsConsumed2 = bitsConsumed3;
                outputAddress2 = outputAddress3;
                break;
            }
            BitInputStream.Loader loader = new BitInputStream.Loader(inputBase, startAddress, currentAddress3, bits2, bitsConsumed3);
            boolean done = loader.load();
            int bitsConsumed4 = loader.getBitsConsumed();
            bits2 = loader.getBits();
            currentAddress3 = loader.getCurrentAddress();
            if (done) {
                currentAddress2 = currentAddress3;
                bitsConsumed2 = bitsConsumed4;
                outputAddress2 = outputAddress3;
                break;
            }
            bitsConsumed3 = decodeSymbol(outputBase, outputAddress3, bits2, bitsConsumed4, tableLog, numbersOfBits, symbols);
            outputAddress3++;
        }
        while (outputAddress2 < outputLimit) {
            bitsConsumed2 = decodeSymbol(outputBase, outputAddress2, bits2, bitsConsumed2, tableLog, numbersOfBits, symbols);
            outputAddress2++;
            currentAddress2 = currentAddress2;
        }
        Util.verify(BitInputStream.isEndOfStream(startAddress, currentAddress2, bitsConsumed2), startAddress, "Bit stream is not fully consumed");
    }

    private static int decodeSymbol(Object outputBase, long outputAddress, long bitContainer, int bitsConsumed, int tableLog, byte[] numbersOfBits, byte[] symbols) {
        int value = (int) BitInputStream.peekBitsFast(bitsConsumed, bitContainer, tableLog);
        UnsafeUtil.UNSAFE.putByte(outputBase, outputAddress, symbols[value]);
        return numbersOfBits[value] + bitsConsumed;
    }
}
