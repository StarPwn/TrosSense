package io.airlift.compress.gzip;

import io.airlift.compress.hadoop.HadoopInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.zip.GZIPInputStream;

/* loaded from: classes.dex */
class JdkGzipHadoopInputStream extends HadoopInputStream {
    private final GZIPInputStream input;
    private final byte[] oneByte = new byte[1];

    public JdkGzipHadoopInputStream(InputStream input, int bufferSize) throws IOException {
        this.input = new GZIPInputStream(new GzipBufferedInputStream(input, bufferSize), bufferSize);
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        int length = this.input.read(this.oneByte, 0, 1);
        if (length < 0) {
            return length;
        }
        return this.oneByte[0] & 255;
    }

    @Override // io.airlift.compress.hadoop.HadoopInputStream, java.io.InputStream
    public int read(byte[] output, int offset, int length) throws IOException {
        return this.input.read(output, offset, length);
    }

    @Override // io.airlift.compress.hadoop.HadoopInputStream
    public void resetState() {
        throw new UnsupportedOperationException("resetState not supported for gzip");
    }

    @Override // io.airlift.compress.hadoop.HadoopInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.input.close();
    }

    /* loaded from: classes.dex */
    private static class GzipBufferedInputStream extends BufferedInputStream {
        public GzipBufferedInputStream(InputStream input, int bufferSize) {
            super((InputStream) Objects.requireNonNull(input, "input is null"), bufferSize);
        }

        @Override // java.io.BufferedInputStream, java.io.FilterInputStream, java.io.InputStream
        public int available() throws IOException {
            return Math.max(1, super.available());
        }
    }
}
