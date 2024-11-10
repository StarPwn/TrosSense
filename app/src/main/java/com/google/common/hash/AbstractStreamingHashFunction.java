package com.google.common.hash;

import com.google.common.base.Preconditions;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

/* loaded from: classes.dex */
abstract class AbstractStreamingHashFunction implements HashFunction {
    @Override // com.google.common.hash.HashFunction
    public <T> HashCode hashObject(T instance, Funnel<? super T> funnel) {
        return newHasher().putObject(instance, funnel).hash();
    }

    @Override // com.google.common.hash.HashFunction
    public HashCode hashUnencodedChars(CharSequence input) {
        return newHasher().putUnencodedChars(input).hash();
    }

    @Override // com.google.common.hash.HashFunction
    public HashCode hashString(CharSequence input, Charset charset) {
        return newHasher().putString(input, charset).hash();
    }

    @Override // com.google.common.hash.HashFunction
    public HashCode hashInt(int input) {
        return newHasher().putInt(input).hash();
    }

    @Override // com.google.common.hash.HashFunction
    public HashCode hashLong(long input) {
        return newHasher().putLong(input).hash();
    }

    @Override // com.google.common.hash.HashFunction
    public HashCode hashBytes(byte[] input) {
        return newHasher().putBytes(input).hash();
    }

    @Override // com.google.common.hash.HashFunction
    public HashCode hashBytes(byte[] input, int off, int len) {
        return newHasher().putBytes(input, off, len).hash();
    }

    @Override // com.google.common.hash.HashFunction
    public Hasher newHasher(int expectedInputSize) {
        Preconditions.checkArgument(expectedInputSize >= 0);
        return newHasher();
    }

    /* loaded from: classes.dex */
    protected static abstract class AbstractStreamingHasher extends AbstractHasher {
        private final ByteBuffer buffer;
        private final int bufferSize;
        private final int chunkSize;

        abstract HashCode makeHash();

        protected abstract void process(ByteBuffer byteBuffer);

        /* JADX INFO: Access modifiers changed from: protected */
        public AbstractStreamingHasher(int chunkSize) {
            this(chunkSize, chunkSize);
        }

        protected AbstractStreamingHasher(int chunkSize, int bufferSize) {
            Preconditions.checkArgument(bufferSize % chunkSize == 0);
            this.buffer = ByteBuffer.allocate(bufferSize + 7).order(ByteOrder.LITTLE_ENDIAN);
            this.bufferSize = bufferSize;
            this.chunkSize = chunkSize;
        }

        protected void processRemaining(ByteBuffer bb) {
            bb.position(bb.limit());
            bb.limit(this.chunkSize + 7);
            while (bb.position() < this.chunkSize) {
                bb.putLong(0L);
            }
            bb.limit(this.chunkSize);
            bb.flip();
            process(bb);
        }

        @Override // com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
        public final Hasher putBytes(byte[] bytes) {
            return putBytes(bytes, 0, bytes.length);
        }

        @Override // com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
        public final Hasher putBytes(byte[] bytes, int off, int len) {
            return putBytes(ByteBuffer.wrap(bytes, off, len).order(ByteOrder.LITTLE_ENDIAN));
        }

        private Hasher putBytes(ByteBuffer readBuffer) {
            if (readBuffer.remaining() <= this.buffer.remaining()) {
                this.buffer.put(readBuffer);
                munchIfFull();
                return this;
            }
            int bytesToCopy = this.bufferSize - this.buffer.position();
            for (int i = 0; i < bytesToCopy; i++) {
                this.buffer.put(readBuffer.get());
            }
            munch();
            while (readBuffer.remaining() >= this.chunkSize) {
                process(readBuffer);
            }
            this.buffer.put(readBuffer);
            return this;
        }

        @Override // com.google.common.hash.AbstractHasher, com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
        public final Hasher putUnencodedChars(CharSequence charSequence) {
            for (int i = 0; i < charSequence.length(); i++) {
                putChar(charSequence.charAt(i));
            }
            return this;
        }

        @Override // com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
        public final Hasher putByte(byte b) {
            this.buffer.put(b);
            munchIfFull();
            return this;
        }

        @Override // com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
        public final Hasher putShort(short s) {
            this.buffer.putShort(s);
            munchIfFull();
            return this;
        }

        @Override // com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
        public final Hasher putChar(char c) {
            this.buffer.putChar(c);
            munchIfFull();
            return this;
        }

        @Override // com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
        public final Hasher putInt(int i) {
            this.buffer.putInt(i);
            munchIfFull();
            return this;
        }

        @Override // com.google.common.hash.Hasher, com.google.common.hash.PrimitiveSink
        public final Hasher putLong(long l) {
            this.buffer.putLong(l);
            munchIfFull();
            return this;
        }

        @Override // com.google.common.hash.Hasher
        public final <T> Hasher putObject(T instance, Funnel<? super T> funnel) {
            funnel.funnel(instance, this);
            return this;
        }

        @Override // com.google.common.hash.Hasher
        public final HashCode hash() {
            munch();
            this.buffer.flip();
            if (this.buffer.remaining() > 0) {
                processRemaining(this.buffer);
            }
            return makeHash();
        }

        private void munchIfFull() {
            if (this.buffer.remaining() < 8) {
                munch();
            }
        }

        private void munch() {
            this.buffer.flip();
            while (this.buffer.remaining() >= this.chunkSize) {
                process(this.buffer);
            }
            this.buffer.compact();
        }
    }
}
