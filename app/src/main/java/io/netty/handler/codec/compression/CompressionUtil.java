package io.netty.handler.codec.compression;

import io.netty.buffer.ByteBuf;
import java.nio.ByteBuffer;

/* loaded from: classes4.dex */
final class CompressionUtil {
    private CompressionUtil() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void checkChecksum(ByteBufChecksum checksum, ByteBuf uncompressed, int currentChecksum) {
        checksum.reset();
        checksum.update(uncompressed, uncompressed.readerIndex(), uncompressed.readableBytes());
        int checksumResult = (int) checksum.getValue();
        if (checksumResult != currentChecksum) {
            throw new DecompressionException(String.format("stream corrupted: mismatching checksum: %d (expected: %d)", Integer.valueOf(checksumResult), Integer.valueOf(currentChecksum)));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ByteBuffer safeReadableNioBuffer(ByteBuf buffer) {
        return safeNioBuffer(buffer, buffer.readerIndex(), buffer.readableBytes());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ByteBuffer safeNioBuffer(ByteBuf buffer, int index, int length) {
        return buffer.nioBufferCount() == 1 ? buffer.internalNioBuffer(index, length) : buffer.nioBuffer(index, length);
    }
}
