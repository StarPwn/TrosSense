package io.airlift.compress.zstd;

/* loaded from: classes.dex */
class BitInputStream {
    private BitInputStream() {
    }

    public static boolean isEndOfStream(long startAddress, long currentAddress, int bitsConsumed) {
        return startAddress == currentAddress && bitsConsumed == 64;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x000b. Please report as an issue. */
    static long readTail(Object inputBase, long inputAddress, int inputSize) {
        long bits = UnsafeUtil.UNSAFE.getByte(inputBase, inputAddress) & 255;
        switch (inputSize) {
            case 2:
                return bits | ((255 & UnsafeUtil.UNSAFE.getByte(inputBase, 1 + inputAddress)) << 8);
            case 3:
                bits |= (UnsafeUtil.UNSAFE.getByte(inputBase, 2 + inputAddress) & 255) << 16;
                return bits | ((255 & UnsafeUtil.UNSAFE.getByte(inputBase, 1 + inputAddress)) << 8);
            case 4:
                bits |= (UnsafeUtil.UNSAFE.getByte(inputBase, 3 + inputAddress) & 255) << 24;
                bits |= (UnsafeUtil.UNSAFE.getByte(inputBase, 2 + inputAddress) & 255) << 16;
                return bits | ((255 & UnsafeUtil.UNSAFE.getByte(inputBase, 1 + inputAddress)) << 8);
            case 5:
                bits |= (UnsafeUtil.UNSAFE.getByte(inputBase, 4 + inputAddress) & 255) << 32;
                bits |= (UnsafeUtil.UNSAFE.getByte(inputBase, 3 + inputAddress) & 255) << 24;
                bits |= (UnsafeUtil.UNSAFE.getByte(inputBase, 2 + inputAddress) & 255) << 16;
                return bits | ((255 & UnsafeUtil.UNSAFE.getByte(inputBase, 1 + inputAddress)) << 8);
            case 6:
                bits |= (UnsafeUtil.UNSAFE.getByte(inputBase, 5 + inputAddress) & 255) << 40;
                bits |= (UnsafeUtil.UNSAFE.getByte(inputBase, 4 + inputAddress) & 255) << 32;
                bits |= (UnsafeUtil.UNSAFE.getByte(inputBase, 3 + inputAddress) & 255) << 24;
                bits |= (UnsafeUtil.UNSAFE.getByte(inputBase, 2 + inputAddress) & 255) << 16;
                return bits | ((255 & UnsafeUtil.UNSAFE.getByte(inputBase, 1 + inputAddress)) << 8);
            case 7:
                bits |= (UnsafeUtil.UNSAFE.getByte(inputBase, 6 + inputAddress) & 255) << 48;
                bits |= (UnsafeUtil.UNSAFE.getByte(inputBase, 5 + inputAddress) & 255) << 40;
                bits |= (UnsafeUtil.UNSAFE.getByte(inputBase, 4 + inputAddress) & 255) << 32;
                bits |= (UnsafeUtil.UNSAFE.getByte(inputBase, 3 + inputAddress) & 255) << 24;
                bits |= (UnsafeUtil.UNSAFE.getByte(inputBase, 2 + inputAddress) & 255) << 16;
                return bits | ((255 & UnsafeUtil.UNSAFE.getByte(inputBase, 1 + inputAddress)) << 8);
            default:
                return bits;
        }
    }

    public static long peekBits(int bitsConsumed, long bitContainer, int numberOfBits) {
        return ((bitContainer << bitsConsumed) >>> 1) >>> (63 - numberOfBits);
    }

    public static long peekBitsFast(int bitsConsumed, long bitContainer, int numberOfBits) {
        return (bitContainer << bitsConsumed) >>> (64 - numberOfBits);
    }

    /* loaded from: classes.dex */
    static class Initializer {
        private long bits;
        private int bitsConsumed;
        private long currentAddress;
        private final long endAddress;
        private final Object inputBase;
        private final long startAddress;

        public Initializer(Object inputBase, long startAddress, long endAddress) {
            this.inputBase = inputBase;
            this.startAddress = startAddress;
            this.endAddress = endAddress;
        }

        public long getBits() {
            return this.bits;
        }

        public long getCurrentAddress() {
            return this.currentAddress;
        }

        public int getBitsConsumed() {
            return this.bitsConsumed;
        }

        public void initialize() {
            Util.verify(this.endAddress - this.startAddress >= 1, this.startAddress, "Bitstream is empty");
            int lastByte = UnsafeUtil.UNSAFE.getByte(this.inputBase, this.endAddress - 1) & 255;
            Util.verify(lastByte != 0, this.endAddress, "Bitstream end mark not present");
            this.bitsConsumed = 8 - Util.highestBit(lastByte);
            int inputSize = (int) (this.endAddress - this.startAddress);
            if (inputSize >= 8) {
                this.currentAddress = this.endAddress - 8;
                this.bits = UnsafeUtil.UNSAFE.getLong(this.inputBase, this.currentAddress);
            } else {
                this.currentAddress = this.startAddress;
                this.bits = BitInputStream.readTail(this.inputBase, this.startAddress, inputSize);
                this.bitsConsumed += (8 - inputSize) * 8;
            }
        }
    }

    /* loaded from: classes.dex */
    static final class Loader {
        private long bits;
        private int bitsConsumed;
        private long currentAddress;
        private final Object inputBase;
        private boolean overflow;
        private final long startAddress;

        public Loader(Object inputBase, long startAddress, long currentAddress, long bits, int bitsConsumed) {
            this.inputBase = inputBase;
            this.startAddress = startAddress;
            this.bits = bits;
            this.currentAddress = currentAddress;
            this.bitsConsumed = bitsConsumed;
        }

        public long getBits() {
            return this.bits;
        }

        public long getCurrentAddress() {
            return this.currentAddress;
        }

        public int getBitsConsumed() {
            return this.bitsConsumed;
        }

        public boolean isOverflow() {
            return this.overflow;
        }

        public boolean load() {
            if (this.bitsConsumed > 64) {
                this.overflow = true;
                return true;
            }
            if (this.currentAddress == this.startAddress) {
                return true;
            }
            int bytes = this.bitsConsumed >>> 3;
            if (this.currentAddress >= this.startAddress + 8) {
                if (bytes > 0) {
                    this.currentAddress -= bytes;
                    this.bits = UnsafeUtil.UNSAFE.getLong(this.inputBase, this.currentAddress);
                }
                this.bitsConsumed &= 7;
                return false;
            }
            if (this.currentAddress - bytes < this.startAddress) {
                int bytes2 = (int) (this.currentAddress - this.startAddress);
                this.currentAddress = this.startAddress;
                this.bitsConsumed -= bytes2 * 8;
                this.bits = UnsafeUtil.UNSAFE.getLong(this.inputBase, this.startAddress);
                return true;
            }
            this.currentAddress -= bytes;
            this.bitsConsumed -= bytes * 8;
            this.bits = UnsafeUtil.UNSAFE.getLong(this.inputBase, this.currentAddress);
            return false;
        }
    }
}
