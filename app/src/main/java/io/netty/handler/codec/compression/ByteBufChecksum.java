package io.netty.handler.codec.compression;

import io.netty.buffer.ByteBuf;
import io.netty.util.ByteProcessor;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public abstract class ByteBufChecksum implements Checksum {
    private final ByteProcessor updateProcessor = new ByteProcessor() { // from class: io.netty.handler.codec.compression.ByteBufChecksum.1
        @Override // io.netty.util.ByteProcessor
        public boolean process(byte value) throws Exception {
            ByteBufChecksum.this.update(value);
            return true;
        }
    };

    /* loaded from: classes4.dex */
    private static class ZlibChecksumMethods {
        private static final Method ADLER32_UPDATE_METHOD = ByteBufChecksum.updateByteBuffer(new Adler32());
        private static final Method CRC32_UPDATE_METHOD = ByteBufChecksum.updateByteBuffer(new CRC32());

        private ZlibChecksumMethods() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Method updateByteBuffer(Checksum checksum) {
        if (PlatformDependent.javaVersion() < 8) {
            return null;
        }
        try {
            Method method = checksum.getClass().getDeclaredMethod("update", ByteBuffer.class);
            method.invoke(checksum, ByteBuffer.allocate(1));
            return method;
        } catch (Throwable th) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ByteBufChecksum wrapChecksum(Checksum checksum) {
        ObjectUtil.checkNotNull(checksum, "checksum");
        if (checksum instanceof ByteBufChecksum) {
            return (ByteBufChecksum) checksum;
        }
        if ((checksum instanceof Adler32) && ZlibChecksumMethods.ADLER32_UPDATE_METHOD != null) {
            return new ReflectiveByteBufChecksum(checksum, ZlibChecksumMethods.ADLER32_UPDATE_METHOD);
        }
        if ((checksum instanceof CRC32) && ZlibChecksumMethods.CRC32_UPDATE_METHOD != null) {
            return new ReflectiveByteBufChecksum(checksum, ZlibChecksumMethods.CRC32_UPDATE_METHOD);
        }
        return new SlowByteBufChecksum(checksum);
    }

    public void update(ByteBuf b, int off, int len) {
        if (b.hasArray()) {
            update(b.array(), b.arrayOffset() + off, len);
        } else {
            b.forEachByte(off, len, this.updateProcessor);
        }
    }

    /* loaded from: classes4.dex */
    private static final class ReflectiveByteBufChecksum extends SlowByteBufChecksum {
        private final Method method;

        ReflectiveByteBufChecksum(Checksum checksum, Method method) {
            super(checksum);
            this.method = method;
        }

        @Override // io.netty.handler.codec.compression.ByteBufChecksum
        public void update(ByteBuf b, int off, int len) {
            if (b.hasArray()) {
                update(b.array(), b.arrayOffset() + off, len);
            } else {
                try {
                    this.method.invoke(this.checksum, CompressionUtil.safeNioBuffer(b, off, len));
                } catch (Throwable th) {
                    throw new Error();
                }
            }
        }
    }

    /* loaded from: classes4.dex */
    private static class SlowByteBufChecksum extends ByteBufChecksum {
        protected final Checksum checksum;

        SlowByteBufChecksum(Checksum checksum) {
            this.checksum = checksum;
        }

        @Override // java.util.zip.Checksum
        public void update(int b) {
            this.checksum.update(b);
        }

        @Override // java.util.zip.Checksum
        public void update(byte[] b, int off, int len) {
            this.checksum.update(b, off, len);
        }

        @Override // java.util.zip.Checksum
        public long getValue() {
            return this.checksum.getValue();
        }

        @Override // java.util.zip.Checksum
        public void reset() {
            this.checksum.reset();
        }
    }
}
