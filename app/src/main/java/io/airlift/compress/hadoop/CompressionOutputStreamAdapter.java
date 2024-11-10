package io.airlift.compress.hadoop;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.hadoop.io.compress.CompressionOutputStream;

/* loaded from: classes.dex */
final class CompressionOutputStreamAdapter extends CompressionOutputStream {
    private static final OutputStream FAKE_OUTPUT_STREAM = new OutputStream() { // from class: io.airlift.compress.hadoop.CompressionOutputStreamAdapter.1
        @Override // java.io.OutputStream
        public void write(int b) {
            throw new UnsupportedOperationException();
        }
    };
    private final HadoopOutputStream output;

    public CompressionOutputStreamAdapter(HadoopOutputStream output) {
        super(FAKE_OUTPUT_STREAM);
        this.output = output;
    }

    public void write(byte[] b, int off, int len) throws IOException {
        this.output.write(b, off, len);
    }

    public void write(int b) throws IOException {
        this.output.write(b);
    }

    public void finish() throws IOException {
        this.output.finish();
    }

    public void resetState() {
    }

    public void close() throws IOException {
        try {
            super.close();
        } finally {
            this.output.close();
        }
    }
}
