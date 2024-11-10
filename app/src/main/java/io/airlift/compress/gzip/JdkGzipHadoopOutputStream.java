package io.airlift.compress.gzip;

import io.airlift.compress.hadoop.HadoopOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import java.util.zip.GZIPOutputStream;

/* loaded from: classes.dex */
class JdkGzipHadoopOutputStream extends HadoopOutputStream {
    private final byte[] oneByte = new byte[1];
    private final GZIPOutputStreamWrapper output;

    public JdkGzipHadoopOutputStream(OutputStream output, int bufferSize) throws IOException {
        this.output = new GZIPOutputStreamWrapper((OutputStream) Objects.requireNonNull(output, "output is null"), bufferSize);
    }

    @Override // java.io.OutputStream
    public void write(int b) throws IOException {
        this.oneByte[0] = (byte) b;
        write(this.oneByte, 0, 1);
    }

    @Override // io.airlift.compress.hadoop.HadoopOutputStream, java.io.OutputStream
    public void write(byte[] buffer, int offset, int length) throws IOException {
        this.output.write(buffer, offset, length);
    }

    @Override // io.airlift.compress.hadoop.HadoopOutputStream
    public void finish() throws IOException {
        try {
            this.output.finish();
        } finally {
            this.output.end();
        }
    }

    @Override // io.airlift.compress.hadoop.HadoopOutputStream, java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        this.output.flush();
    }

    @Override // io.airlift.compress.hadoop.HadoopOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        try {
            finish();
        } finally {
            this.output.close();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class GZIPOutputStreamWrapper extends GZIPOutputStream {
        GZIPOutputStreamWrapper(OutputStream output, int bufferSize) throws IOException {
            super(output, bufferSize);
        }

        public void end() throws IOException {
            this.def.end();
        }
    }
}
