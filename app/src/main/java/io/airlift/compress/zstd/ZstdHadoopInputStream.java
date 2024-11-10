package io.airlift.compress.zstd;

import io.airlift.compress.hadoop.HadoopInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/* loaded from: classes.dex */
class ZstdHadoopInputStream extends HadoopInputStream {
    private final InputStream in;
    private ZstdInputStream zstdInputStream;

    public ZstdHadoopInputStream(InputStream in) {
        this.in = (InputStream) Objects.requireNonNull(in, "in is null");
        this.zstdInputStream = new ZstdInputStream(in);
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        return this.zstdInputStream.read();
    }

    @Override // java.io.InputStream
    public int read(byte[] b) throws IOException {
        return this.zstdInputStream.read(b);
    }

    @Override // io.airlift.compress.hadoop.HadoopInputStream, java.io.InputStream
    public int read(byte[] outputBuffer, int outputOffset, int outputLength) throws IOException {
        return this.zstdInputStream.read(outputBuffer, outputOffset, outputLength);
    }

    @Override // io.airlift.compress.hadoop.HadoopInputStream
    public void resetState() {
        this.zstdInputStream = new ZstdInputStream(this.in);
    }

    @Override // io.airlift.compress.hadoop.HadoopInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.zstdInputStream.close();
    }
}
