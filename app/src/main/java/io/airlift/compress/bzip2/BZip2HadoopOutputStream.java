package io.airlift.compress.bzip2;

import io.airlift.compress.hadoop.HadoopOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

/* loaded from: classes.dex */
class BZip2HadoopOutputStream extends HadoopOutputStream {
    private boolean initialized;
    private CBZip2OutputStream output;
    private final OutputStream rawOutput;

    public BZip2HadoopOutputStream(OutputStream out) {
        this.rawOutput = (OutputStream) Objects.requireNonNull(out, "out is null");
    }

    @Override // java.io.OutputStream
    public void write(int b) throws IOException {
        openStreamIfNecessary();
        this.output.write(b);
    }

    @Override // io.airlift.compress.hadoop.HadoopOutputStream, java.io.OutputStream
    public void write(byte[] b, int off, int len) throws IOException {
        openStreamIfNecessary();
        this.output.write(b, off, len);
    }

    @Override // io.airlift.compress.hadoop.HadoopOutputStream
    public void finish() throws IOException {
        if (this.output != null) {
            this.output.finish();
            this.output = null;
        }
    }

    @Override // io.airlift.compress.hadoop.HadoopOutputStream, java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        this.rawOutput.flush();
    }

    @Override // io.airlift.compress.hadoop.HadoopOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        try {
            if (!this.initialized) {
                openStreamIfNecessary();
            }
            finish();
        } finally {
            this.rawOutput.close();
        }
    }

    private void openStreamIfNecessary() throws IOException {
        if (this.output == null) {
            this.initialized = true;
            this.rawOutput.write(new byte[]{66, 90});
            this.output = new CBZip2OutputStream(this.rawOutput);
        }
    }
}
