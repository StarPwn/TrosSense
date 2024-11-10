package io.airlift.compress.deflate;

import io.airlift.compress.hadoop.HadoopInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/* loaded from: classes.dex */
class JdkDeflateHadoopInputStream extends HadoopInputStream {
    private boolean closed;
    private final InputStream input;
    private final byte[] inputBuffer;
    private int inputBufferEnd;
    private final byte[] oneByte = new byte[1];
    private final Inflater inflater = new Inflater();

    public JdkDeflateHadoopInputStream(InputStream input, int bufferSize) {
        this.input = (InputStream) Objects.requireNonNull(input, "input is null");
        this.inputBuffer = new byte[bufferSize];
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        int length = read(this.oneByte, 0, 1);
        if (length < 0) {
            return length;
        }
        return this.oneByte[0] & 255;
    }

    @Override // io.airlift.compress.hadoop.HadoopInputStream, java.io.InputStream
    public int read(byte[] output, int offset, int length) throws IOException {
        if (this.closed) {
            throw new IOException("Closed");
        }
        while (true) {
            try {
                int outputSize = this.inflater.inflate(output, offset, length);
                if (outputSize > 0) {
                    return outputSize;
                }
                if (this.inflater.needsDictionary()) {
                    return -1;
                }
                if (this.inflater.finished()) {
                    int remainingBytes = this.inflater.getRemaining();
                    if (remainingBytes > 0) {
                        this.inflater.reset();
                        this.inflater.setInput(this.inputBuffer, this.inputBufferEnd - remainingBytes, remainingBytes);
                    } else {
                        int bufferedBytes = this.input.read(this.inputBuffer, 0, this.inputBuffer.length);
                        if (bufferedBytes < 0) {
                            return -1;
                        }
                        this.inflater.reset();
                        this.inflater.setInput(this.inputBuffer, 0, bufferedBytes);
                        this.inputBufferEnd = bufferedBytes;
                    }
                } else if (this.inflater.needsInput()) {
                    int bufferedBytes2 = this.input.read(this.inputBuffer, 0, this.inputBuffer.length);
                    if (bufferedBytes2 < 0) {
                        throw new EOFException("Unexpected end of input stream");
                    }
                    this.inflater.setInput(this.inputBuffer, 0, bufferedBytes2);
                    this.inputBufferEnd = bufferedBytes2;
                } else {
                    continue;
                }
            } catch (DataFormatException e) {
                throw new IOException(e);
            }
        }
    }

    @Override // io.airlift.compress.hadoop.HadoopInputStream
    public void resetState() {
        this.inflater.reset();
    }

    @Override // io.airlift.compress.hadoop.HadoopInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (!this.closed) {
            this.closed = true;
            this.inflater.end();
            this.input.close();
        }
    }
}
