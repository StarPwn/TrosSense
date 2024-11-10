package com.google.common.io;

import com.google.common.base.Preconditions;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Arrays;

/* loaded from: classes.dex */
public final class ByteStreams {
    private static final OutputStream NULL_OUTPUT_STREAM = new OutputStream() { // from class: com.google.common.io.ByteStreams.1
        @Override // java.io.OutputStream
        public void write(int b) {
        }

        @Override // java.io.OutputStream
        public void write(byte[] b) {
            Preconditions.checkNotNull(b);
        }

        @Override // java.io.OutputStream
        public void write(byte[] b, int off, int len) {
            Preconditions.checkNotNull(b);
        }

        public String toString() {
            return "ByteStreams.nullOutputStream()";
        }
    };
    private static final int ZERO_COPY_CHUNK_SIZE = 524288;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] createBuffer() {
        return new byte[8192];
    }

    private ByteStreams() {
    }

    public static long copy(InputStream from, OutputStream to) throws IOException {
        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(to);
        byte[] buf = createBuffer();
        long total = 0;
        while (true) {
            int r = from.read(buf);
            if (r != -1) {
                to.write(buf, 0, r);
                total += r;
            } else {
                return total;
            }
        }
    }

    public static long copy(ReadableByteChannel from, WritableByteChannel to) throws IOException {
        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(to);
        if (from instanceof FileChannel) {
            FileChannel sourceChannel = (FileChannel) from;
            long oldPosition = sourceChannel.position();
            long position = oldPosition;
            while (true) {
                long copied = sourceChannel.transferTo(position, 524288L, to);
                position += copied;
                sourceChannel.position(position);
                if (copied <= 0 && position >= sourceChannel.size()) {
                    return position - oldPosition;
                }
            }
        } else {
            ByteBuffer buf = ByteBuffer.wrap(createBuffer());
            long total = 0;
            while (from.read(buf) != -1) {
                buf.flip();
                while (buf.hasRemaining()) {
                    total += to.write(buf);
                }
                buf.clear();
            }
            return total;
        }
    }

    public static byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(Math.max(32, in.available()));
        copy(in, out);
        return out.toByteArray();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] toByteArray(InputStream in, int expectedSize) throws IOException {
        byte[] bytes = new byte[expectedSize];
        int remaining = expectedSize;
        while (remaining > 0) {
            int off = expectedSize - remaining;
            int read = in.read(bytes, off, remaining);
            if (read == -1) {
                return Arrays.copyOf(bytes, off);
            }
            remaining -= read;
        }
        int b = in.read();
        if (b == -1) {
            return bytes;
        }
        FastByteArrayOutputStream out = new FastByteArrayOutputStream();
        out.write(b);
        copy(in, out);
        byte[] result = new byte[bytes.length + out.size()];
        System.arraycopy(bytes, 0, result, 0, bytes.length);
        out.writeTo(result, bytes.length);
        return result;
    }

    /* loaded from: classes.dex */
    private static final class FastByteArrayOutputStream extends ByteArrayOutputStream {
        private FastByteArrayOutputStream() {
        }

        void writeTo(byte[] b, int off) {
            System.arraycopy(this.buf, 0, b, off, this.count);
        }
    }

    public static long exhaust(InputStream in) throws IOException {
        long total = 0;
        byte[] buf = createBuffer();
        while (true) {
            long read = in.read(buf);
            if (read != -1) {
                total += read;
            } else {
                return total;
            }
        }
    }

    public static ByteArrayDataInput newDataInput(byte[] bytes) {
        return newDataInput(new ByteArrayInputStream(bytes));
    }

    public static ByteArrayDataInput newDataInput(byte[] bytes, int start) {
        Preconditions.checkPositionIndex(start, bytes.length);
        return newDataInput(new ByteArrayInputStream(bytes, start, bytes.length - start));
    }

    public static ByteArrayDataInput newDataInput(ByteArrayInputStream byteArrayInputStream) {
        return new ByteArrayDataInputStream((ByteArrayInputStream) Preconditions.checkNotNull(byteArrayInputStream));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class ByteArrayDataInputStream implements ByteArrayDataInput {
        final DataInput input;

        ByteArrayDataInputStream(ByteArrayInputStream byteArrayInputStream) {
            this.input = new DataInputStream(byteArrayInputStream);
        }

        @Override // com.google.common.io.ByteArrayDataInput, java.io.DataInput
        public void readFully(byte[] b) {
            try {
                this.input.readFully(b);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override // com.google.common.io.ByteArrayDataInput, java.io.DataInput
        public void readFully(byte[] b, int off, int len) {
            try {
                this.input.readFully(b, off, len);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override // com.google.common.io.ByteArrayDataInput, java.io.DataInput
        public int skipBytes(int n) {
            try {
                return this.input.skipBytes(n);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override // com.google.common.io.ByteArrayDataInput, java.io.DataInput
        public boolean readBoolean() {
            try {
                return this.input.readBoolean();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override // com.google.common.io.ByteArrayDataInput, java.io.DataInput
        public byte readByte() {
            try {
                return this.input.readByte();
            } catch (EOFException e) {
                throw new IllegalStateException(e);
            } catch (IOException impossible) {
                throw new AssertionError(impossible);
            }
        }

        @Override // com.google.common.io.ByteArrayDataInput, java.io.DataInput
        public int readUnsignedByte() {
            try {
                return this.input.readUnsignedByte();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override // com.google.common.io.ByteArrayDataInput, java.io.DataInput
        public short readShort() {
            try {
                return this.input.readShort();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override // com.google.common.io.ByteArrayDataInput, java.io.DataInput
        public int readUnsignedShort() {
            try {
                return this.input.readUnsignedShort();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override // com.google.common.io.ByteArrayDataInput, java.io.DataInput
        public char readChar() {
            try {
                return this.input.readChar();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override // com.google.common.io.ByteArrayDataInput, java.io.DataInput
        public int readInt() {
            try {
                return this.input.readInt();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override // com.google.common.io.ByteArrayDataInput, java.io.DataInput
        public long readLong() {
            try {
                return this.input.readLong();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override // com.google.common.io.ByteArrayDataInput, java.io.DataInput
        public float readFloat() {
            try {
                return this.input.readFloat();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override // com.google.common.io.ByteArrayDataInput, java.io.DataInput
        public double readDouble() {
            try {
                return this.input.readDouble();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override // com.google.common.io.ByteArrayDataInput, java.io.DataInput
        public String readLine() {
            try {
                return this.input.readLine();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override // com.google.common.io.ByteArrayDataInput, java.io.DataInput
        public String readUTF() {
            try {
                return this.input.readUTF();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public static ByteArrayDataOutput newDataOutput() {
        return newDataOutput(new ByteArrayOutputStream());
    }

    public static ByteArrayDataOutput newDataOutput(int size) {
        if (size < 0) {
            throw new IllegalArgumentException(String.format("Invalid size: %s", Integer.valueOf(size)));
        }
        return newDataOutput(new ByteArrayOutputStream(size));
    }

    public static ByteArrayDataOutput newDataOutput(ByteArrayOutputStream byteArrayOutputSteam) {
        return new ByteArrayDataOutputStream((ByteArrayOutputStream) Preconditions.checkNotNull(byteArrayOutputSteam));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class ByteArrayDataOutputStream implements ByteArrayDataOutput {
        final ByteArrayOutputStream byteArrayOutputSteam;
        final DataOutput output;

        ByteArrayDataOutputStream(ByteArrayOutputStream byteArrayOutputSteam) {
            this.byteArrayOutputSteam = byteArrayOutputSteam;
            this.output = new DataOutputStream(byteArrayOutputSteam);
        }

        @Override // com.google.common.io.ByteArrayDataOutput, java.io.DataOutput
        public void write(int b) {
            try {
                this.output.write(b);
            } catch (IOException impossible) {
                throw new AssertionError(impossible);
            }
        }

        @Override // com.google.common.io.ByteArrayDataOutput, java.io.DataOutput
        public void write(byte[] b) {
            try {
                this.output.write(b);
            } catch (IOException impossible) {
                throw new AssertionError(impossible);
            }
        }

        @Override // com.google.common.io.ByteArrayDataOutput, java.io.DataOutput
        public void write(byte[] b, int off, int len) {
            try {
                this.output.write(b, off, len);
            } catch (IOException impossible) {
                throw new AssertionError(impossible);
            }
        }

        @Override // com.google.common.io.ByteArrayDataOutput, java.io.DataOutput
        public void writeBoolean(boolean v) {
            try {
                this.output.writeBoolean(v);
            } catch (IOException impossible) {
                throw new AssertionError(impossible);
            }
        }

        @Override // com.google.common.io.ByteArrayDataOutput, java.io.DataOutput
        public void writeByte(int v) {
            try {
                this.output.writeByte(v);
            } catch (IOException impossible) {
                throw new AssertionError(impossible);
            }
        }

        @Override // com.google.common.io.ByteArrayDataOutput, java.io.DataOutput
        public void writeBytes(String s) {
            try {
                this.output.writeBytes(s);
            } catch (IOException impossible) {
                throw new AssertionError(impossible);
            }
        }

        @Override // com.google.common.io.ByteArrayDataOutput, java.io.DataOutput
        public void writeChar(int v) {
            try {
                this.output.writeChar(v);
            } catch (IOException impossible) {
                throw new AssertionError(impossible);
            }
        }

        @Override // com.google.common.io.ByteArrayDataOutput, java.io.DataOutput
        public void writeChars(String s) {
            try {
                this.output.writeChars(s);
            } catch (IOException impossible) {
                throw new AssertionError(impossible);
            }
        }

        @Override // com.google.common.io.ByteArrayDataOutput, java.io.DataOutput
        public void writeDouble(double v) {
            try {
                this.output.writeDouble(v);
            } catch (IOException impossible) {
                throw new AssertionError(impossible);
            }
        }

        @Override // com.google.common.io.ByteArrayDataOutput, java.io.DataOutput
        public void writeFloat(float v) {
            try {
                this.output.writeFloat(v);
            } catch (IOException impossible) {
                throw new AssertionError(impossible);
            }
        }

        @Override // com.google.common.io.ByteArrayDataOutput, java.io.DataOutput
        public void writeInt(int v) {
            try {
                this.output.writeInt(v);
            } catch (IOException impossible) {
                throw new AssertionError(impossible);
            }
        }

        @Override // com.google.common.io.ByteArrayDataOutput, java.io.DataOutput
        public void writeLong(long v) {
            try {
                this.output.writeLong(v);
            } catch (IOException impossible) {
                throw new AssertionError(impossible);
            }
        }

        @Override // com.google.common.io.ByteArrayDataOutput, java.io.DataOutput
        public void writeShort(int v) {
            try {
                this.output.writeShort(v);
            } catch (IOException impossible) {
                throw new AssertionError(impossible);
            }
        }

        @Override // com.google.common.io.ByteArrayDataOutput, java.io.DataOutput
        public void writeUTF(String s) {
            try {
                this.output.writeUTF(s);
            } catch (IOException impossible) {
                throw new AssertionError(impossible);
            }
        }

        @Override // com.google.common.io.ByteArrayDataOutput
        public byte[] toByteArray() {
            return this.byteArrayOutputSteam.toByteArray();
        }
    }

    public static OutputStream nullOutputStream() {
        return NULL_OUTPUT_STREAM;
    }

    public static InputStream limit(InputStream in, long limit) {
        return new LimitedInputStream(in, limit);
    }

    /* loaded from: classes.dex */
    private static final class LimitedInputStream extends FilterInputStream {
        private long left;
        private long mark;

        LimitedInputStream(InputStream in, long limit) {
            super(in);
            this.mark = -1L;
            Preconditions.checkNotNull(in);
            Preconditions.checkArgument(limit >= 0, "limit must be non-negative");
            this.left = limit;
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public int available() throws IOException {
            return (int) Math.min(this.in.available(), this.left);
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public synchronized void mark(int readLimit) {
            this.in.mark(readLimit);
            this.mark = this.left;
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public int read() throws IOException {
            if (this.left == 0) {
                return -1;
            }
            int result = this.in.read();
            if (result != -1) {
                this.left--;
            }
            return result;
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public int read(byte[] b, int off, int len) throws IOException {
            if (this.left == 0) {
                return -1;
            }
            int result = this.in.read(b, off, (int) Math.min(len, this.left));
            if (result != -1) {
                this.left -= result;
            }
            return result;
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public synchronized void reset() throws IOException {
            if (!this.in.markSupported()) {
                throw new IOException("Mark not supported");
            }
            if (this.mark == -1) {
                throw new IOException("Mark not set");
            }
            this.in.reset();
            this.left = this.mark;
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public long skip(long n) throws IOException {
            long skipped = this.in.skip(Math.min(n, this.left));
            this.left -= skipped;
            return skipped;
        }
    }

    public static void readFully(InputStream in, byte[] b) throws IOException {
        readFully(in, b, 0, b.length);
    }

    public static void readFully(InputStream in, byte[] b, int off, int len) throws IOException {
        int read = read(in, b, off, len);
        if (read != len) {
            throw new EOFException("reached end of stream after reading " + read + " bytes; " + len + " bytes expected");
        }
    }

    public static void skipFully(InputStream in, long n) throws IOException {
        long skipped = skipUpTo(in, n);
        if (skipped < n) {
            throw new EOFException("reached end of stream after skipping " + skipped + " bytes; " + n + " bytes expected");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long skipUpTo(InputStream in, long n) throws IOException {
        long totalSkipped = 0;
        byte[] buf = createBuffer();
        while (totalSkipped < n) {
            long remaining = n - totalSkipped;
            long skipped = skipSafely(in, remaining);
            if (skipped == 0) {
                int skip = (int) Math.min(remaining, buf.length);
                long read = in.read(buf, 0, skip);
                skipped = read;
                if (read == -1) {
                    break;
                }
            }
            totalSkipped += skipped;
        }
        return totalSkipped;
    }

    private static long skipSafely(InputStream in, long n) throws IOException {
        int available = in.available();
        if (available == 0) {
            return 0L;
        }
        return in.skip(Math.min(available, n));
    }

    public static <T> T readBytes(InputStream input, ByteProcessor<T> processor) throws IOException {
        int read;
        Preconditions.checkNotNull(input);
        Preconditions.checkNotNull(processor);
        byte[] buf = createBuffer();
        do {
            read = input.read(buf);
            if (read == -1) {
                break;
            }
        } while (processor.processBytes(buf, 0, read));
        return processor.getResult();
    }

    public static int read(InputStream in, byte[] b, int off, int len) throws IOException {
        Preconditions.checkNotNull(in);
        Preconditions.checkNotNull(b);
        if (len < 0) {
            throw new IndexOutOfBoundsException("len is negative");
        }
        int total = 0;
        while (total < len) {
            int result = in.read(b, off + total, len - total);
            if (result == -1) {
                break;
            }
            total += result;
        }
        return total;
    }
}
