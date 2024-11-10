package io.airlift.compress.bzip2;

import io.airlift.compress.hadoop.HadoopInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
class BZip2HadoopInputStream extends HadoopInputStream {
    private final BufferedInputStream bufferedIn;
    private CBZip2InputStream input;

    public BZip2HadoopInputStream(InputStream in) {
        this.bufferedIn = new BufferedInputStream(in);
    }

    @Override // io.airlift.compress.hadoop.HadoopInputStream, java.io.InputStream
    public int read(byte[] buffer, int offset, int length) throws IOException {
        if (length == 0) {
            return 0;
        }
        if (this.input == null) {
            this.bufferedIn.mark(2);
            if (this.bufferedIn.read() != 66 || this.bufferedIn.read() != 90) {
                this.bufferedIn.reset();
            }
            this.input = new CBZip2InputStream(this.bufferedIn);
        }
        int result = this.input.read(buffer, offset, length);
        if (result == -2) {
            return this.input.read(buffer, offset, 1);
        }
        return result;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        byte[] buffer = new byte[1];
        int result = read(buffer, 0, 1);
        if (result < 0) {
            return result;
        }
        return buffer[0] & 255;
    }

    @Override // io.airlift.compress.hadoop.HadoopInputStream
    public void resetState() {
        this.input = null;
    }

    @Override // io.airlift.compress.hadoop.HadoopInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.input = null;
        this.bufferedIn.close();
    }
}
