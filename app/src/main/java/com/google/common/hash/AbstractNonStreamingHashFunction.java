package com.google.common.hash;

import com.google.common.base.Preconditions;
import io.netty.handler.codec.http2.Http2CodecUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/* loaded from: classes.dex */
abstract class AbstractNonStreamingHashFunction implements HashFunction {
    @Override // com.google.common.hash.HashFunction
    public Hasher newHasher() {
        return new BufferingHasher(32);
    }

    @Override // com.google.common.hash.HashFunction
    public Hasher newHasher(int expectedInputSize) {
        Preconditions.checkArgument(expectedInputSize >= 0);
        return new BufferingHasher(expectedInputSize);
    }

    @Override // com.google.common.hash.HashFunction
    public <T> HashCode hashObject(T instance, Funnel<? super T> funnel) {
        return newHasher().putObject(instance, funnel).hash();
    }

    @Override // com.google.common.hash.HashFunction
    public HashCode hashUnencodedChars(CharSequence input) {
        int len = input.length();
        Hasher hasher = newHasher(len * 2);
        for (int i = 0; i < len; i++) {
            hasher.putChar(input.charAt(i));
        }
        return hasher.hash();
    }

    @Override // com.google.common.hash.HashFunction
    public HashCode hashString(CharSequence input, Charset charset) {
        return hashBytes(input.toString().getBytes(charset));
    }

    @Override // com.google.common.hash.HashFunction
    public HashCode hashInt(int input) {
        return newHasher(4).putInt(input).hash();
    }

    @Override // com.google.common.hash.HashFunction
    public HashCode hashLong(long input) {
        return newHasher(8).putLong(input).hash();
    }

    @Override // com.google.common.hash.HashFunction
    public HashCode hashBytes(byte[] input) {
        return hashBytes(input, 0, input.length);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class BufferingHasher extends AbstractHasher {
        static final int BOTTOM_BYTE = 255;
        final ExposedByteArrayOutputStream stream;

        BufferingHasher(int expectedInputSize) {
            this.stream = new ExposedByteArrayOutputStream(expectedInputSize);
        }

        @Override // com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
        public Hasher putByte(byte b) {
            this.stream.write(b);
            return this;
        }

        @Override // com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
        public Hasher putBytes(byte[] bytes) {
            try {
                this.stream.write(bytes);
                return this;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override // com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
        public Hasher putBytes(byte[] bytes, int off, int len) {
            this.stream.write(bytes, off, len);
            return this;
        }

        @Override // com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
        public Hasher putShort(short s) {
            this.stream.write(s & Http2CodecUtil.MAX_UNSIGNED_BYTE);
            this.stream.write((s >>> 8) & 255);
            return this;
        }

        @Override // com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
        public Hasher putInt(int i) {
            this.stream.write(i & 255);
            this.stream.write((i >>> 8) & 255);
            this.stream.write((i >>> 16) & 255);
            this.stream.write((i >>> 24) & 255);
            return this;
        }

        @Override // com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
        public Hasher putLong(long l) {
            for (int i = 0; i < 64; i += 8) {
                this.stream.write((byte) ((l >>> i) & 255));
            }
            return this;
        }

        @Override // com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
        public Hasher putChar(char c) {
            this.stream.write(c & 255);
            this.stream.write((c >>> '\b') & 255);
            return this;
        }

        @Override // com.google.common.hash.Hasher
        public <T> Hasher putObject(T instance, Funnel<? super T> funnel) {
            funnel.funnel(instance, this);
            return this;
        }

        @Override // com.google.common.hash.Hasher
        public HashCode hash() {
            return AbstractNonStreamingHashFunction.this.hashBytes(this.stream.byteArray(), 0, this.stream.length());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class ExposedByteArrayOutputStream extends ByteArrayOutputStream {
        ExposedByteArrayOutputStream(int expectedInputSize) {
            super(expectedInputSize);
        }

        byte[] byteArray() {
            return this.buf;
        }

        int length() {
            return this.count;
        }
    }
}
