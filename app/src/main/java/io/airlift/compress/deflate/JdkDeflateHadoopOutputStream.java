package io.airlift.compress.deflate;

import io.airlift.compress.hadoop.HadoopOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import java.util.zip.Deflater;

/* loaded from: classes.dex */
class JdkDeflateHadoopOutputStream extends HadoopOutputStream {
    protected boolean closed;
    private final OutputStream output;
    private final byte[] outputBuffer;
    private final byte[] oneByte = new byte[1];
    private final Deflater deflater = new Deflater();

    public JdkDeflateHadoopOutputStream(OutputStream output, int bufferSize) {
        this.output = (OutputStream) Objects.requireNonNull(output, "output is null");
        this.outputBuffer = new byte[bufferSize];
    }

    @Override // java.io.OutputStream
    public void write(int b) throws IOException {
        this.oneByte[0] = (byte) b;
        write(this.oneByte, 0, 1);
    }

    @Override // io.airlift.compress.hadoop.HadoopOutputStream, java.io.OutputStream
    public void write(byte[] buffer, int offset, int length) throws IOException {
        this.deflater.setInput(buffer, offset, length);
        while (!this.deflater.needsInput()) {
            compress();
        }
    }

    @Override // io.airlift.compress.hadoop.HadoopOutputStream
    public void finish() throws IOException {
        if (!this.deflater.finished()) {
            this.deflater.finish();
            while (!this.deflater.finished()) {
                compress();
            }
        }
        this.deflater.reset();
    }

    private void compress() throws IOException {
        int compressedSize = this.deflater.deflate(this.outputBuffer, 0, this.outputBuffer.length);
        this.output.write(this.outputBuffer, 0, compressedSize);
    }

    @Override // io.airlift.compress.hadoop.HadoopOutputStream, java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        this.output.flush();
    }

    @Override // io.airlift.compress.hadoop.HadoopOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (!this.closed) {
            this.closed = true;
            try {
                finish();
            } finally {
                this.output.close();
            }
        }
    }
}
