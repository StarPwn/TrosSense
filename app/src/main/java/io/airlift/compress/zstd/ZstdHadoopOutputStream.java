package io.airlift.compress.zstd;

import io.airlift.compress.hadoop.HadoopOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

/* loaded from: classes.dex */
class ZstdHadoopOutputStream extends HadoopOutputStream {
    private boolean initialized;
    private final OutputStream out;
    private ZstdOutputStream zstdOutputStream;

    public ZstdHadoopOutputStream(OutputStream out) {
        this.out = (OutputStream) Objects.requireNonNull(out, "out is null");
    }

    @Override // java.io.OutputStream
    public void write(int b) throws IOException {
        openStreamIfNecessary();
        this.zstdOutputStream.write(b);
    }

    @Override // io.airlift.compress.hadoop.HadoopOutputStream, java.io.OutputStream
    public void write(byte[] buffer, int offset, int length) throws IOException {
        openStreamIfNecessary();
        this.zstdOutputStream.write(buffer, offset, length);
    }

    @Override // io.airlift.compress.hadoop.HadoopOutputStream
    public void finish() throws IOException {
        if (this.zstdOutputStream != null) {
            this.zstdOutputStream.finishWithoutClosingSource();
            this.zstdOutputStream = null;
        }
    }

    @Override // io.airlift.compress.hadoop.HadoopOutputStream, java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        this.out.flush();
    }

    @Override // io.airlift.compress.hadoop.HadoopOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        try {
            if (!this.initialized) {
                openStreamIfNecessary();
            }
            finish();
        } finally {
            this.out.close();
        }
    }

    private void openStreamIfNecessary() throws IOException {
        if (this.zstdOutputStream == null) {
            this.initialized = true;
            this.zstdOutputStream = new ZstdOutputStream(this.out);
        }
    }
}
