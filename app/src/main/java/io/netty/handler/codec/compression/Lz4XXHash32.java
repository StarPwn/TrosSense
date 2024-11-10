package io.netty.handler.codec.compression;

import io.netty.buffer.ByteBuf;
import net.jpountz.xxhash.XXHash32;
import net.jpountz.xxhash.XXHashFactory;

/* loaded from: classes4.dex */
public final class Lz4XXHash32 extends ByteBufChecksum {
    private static final XXHash32 XXHASH32 = XXHashFactory.fastestInstance().hash32();
    private final int seed;
    private boolean used;
    private int value;

    public Lz4XXHash32(int seed) {
        this.seed = seed;
    }

    @Override // java.util.zip.Checksum
    public void update(int b) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.zip.Checksum
    public void update(byte[] b, int off, int len) {
        if (this.used) {
            throw new IllegalStateException();
        }
        this.value = XXHASH32.hash(b, off, len, this.seed);
        this.used = true;
    }

    @Override // io.netty.handler.codec.compression.ByteBufChecksum
    public void update(ByteBuf b, int off, int len) {
        if (this.used) {
            throw new IllegalStateException();
        }
        if (b.hasArray()) {
            this.value = XXHASH32.hash(b.array(), b.arrayOffset() + off, len, this.seed);
        } else {
            this.value = XXHASH32.hash(CompressionUtil.safeNioBuffer(b, off, len), this.seed);
        }
        this.used = true;
    }

    @Override // java.util.zip.Checksum
    public long getValue() {
        if (!this.used) {
            throw new IllegalStateException();
        }
        return this.value & 268435455;
    }

    @Override // java.util.zip.Checksum
    public void reset() {
        this.used = false;
    }
}
