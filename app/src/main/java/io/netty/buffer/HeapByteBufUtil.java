package io.netty.buffer;

/* loaded from: classes4.dex */
final class HeapByteBufUtil {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte getByte(byte[] memory, int index) {
        return memory[index];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static short getShort(byte[] memory, int index) {
        return (short) ((memory[index] << 8) | (memory[index + 1] & 255));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static short getShortLE(byte[] memory, int index) {
        return (short) ((memory[index] & 255) | (memory[index + 1] << 8));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getUnsignedMedium(byte[] memory, int index) {
        return ((memory[index] & 255) << 16) | ((memory[index + 1] & 255) << 8) | (memory[index + 2] & 255);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getUnsignedMediumLE(byte[] memory, int index) {
        return (memory[index] & 255) | ((memory[index + 1] & 255) << 8) | ((memory[index + 2] & 255) << 16);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getInt(byte[] memory, int index) {
        return ((memory[index] & 255) << 24) | ((memory[index + 1] & 255) << 16) | ((memory[index + 2] & 255) << 8) | (memory[index + 3] & 255);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getIntLE(byte[] memory, int index) {
        return (memory[index] & 255) | ((memory[index + 1] & 255) << 8) | ((memory[index + 2] & 255) << 16) | ((memory[index + 3] & 255) << 24);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long getLong(byte[] memory, int index) {
        return ((memory[index] & 255) << 56) | ((memory[index + 1] & 255) << 48) | ((memory[index + 2] & 255) << 40) | ((memory[index + 3] & 255) << 32) | ((memory[index + 4] & 255) << 24) | ((memory[index + 5] & 255) << 16) | ((memory[index + 6] & 255) << 8) | (255 & memory[index + 7]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long getLongLE(byte[] memory, int index) {
        return (memory[index] & 255) | ((memory[index + 1] & 255) << 8) | ((memory[index + 2] & 255) << 16) | ((memory[index + 3] & 255) << 24) | ((memory[index + 4] & 255) << 32) | ((memory[index + 5] & 255) << 40) | ((memory[index + 6] & 255) << 48) | ((255 & memory[index + 7]) << 56);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setByte(byte[] memory, int index, int value) {
        memory[index] = (byte) value;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setShort(byte[] memory, int index, int value) {
        memory[index] = (byte) (value >>> 8);
        memory[index + 1] = (byte) value;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setShortLE(byte[] memory, int index, int value) {
        memory[index] = (byte) value;
        memory[index + 1] = (byte) (value >>> 8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setMedium(byte[] memory, int index, int value) {
        memory[index] = (byte) (value >>> 16);
        memory[index + 1] = (byte) (value >>> 8);
        memory[index + 2] = (byte) value;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setMediumLE(byte[] memory, int index, int value) {
        memory[index] = (byte) value;
        memory[index + 1] = (byte) (value >>> 8);
        memory[index + 2] = (byte) (value >>> 16);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setInt(byte[] memory, int index, int value) {
        memory[index] = (byte) (value >>> 24);
        memory[index + 1] = (byte) (value >>> 16);
        memory[index + 2] = (byte) (value >>> 8);
        memory[index + 3] = (byte) value;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setIntLE(byte[] memory, int index, int value) {
        memory[index] = (byte) value;
        memory[index + 1] = (byte) (value >>> 8);
        memory[index + 2] = (byte) (value >>> 16);
        memory[index + 3] = (byte) (value >>> 24);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setLong(byte[] memory, int index, long value) {
        memory[index] = (byte) (value >>> 56);
        memory[index + 1] = (byte) (value >>> 48);
        memory[index + 2] = (byte) (value >>> 40);
        memory[index + 3] = (byte) (value >>> 32);
        memory[index + 4] = (byte) (value >>> 24);
        memory[index + 5] = (byte) (value >>> 16);
        memory[index + 6] = (byte) (value >>> 8);
        memory[index + 7] = (byte) value;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setLongLE(byte[] memory, int index, long value) {
        memory[index] = (byte) value;
        memory[index + 1] = (byte) (value >>> 8);
        memory[index + 2] = (byte) (value >>> 16);
        memory[index + 3] = (byte) (value >>> 24);
        memory[index + 4] = (byte) (value >>> 32);
        memory[index + 5] = (byte) (value >>> 40);
        memory[index + 6] = (byte) (value >>> 48);
        memory[index + 7] = (byte) (value >>> 56);
    }

    private HeapByteBufUtil() {
    }
}
