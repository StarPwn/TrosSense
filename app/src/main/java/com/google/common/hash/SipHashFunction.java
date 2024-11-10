package com.google.common.hash;

import com.google.common.base.Preconditions;
import com.google.common.hash.AbstractStreamingHashFunction;
import java.io.Serializable;
import java.nio.ByteBuffer;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
final class SipHashFunction extends AbstractStreamingHashFunction implements Serializable {
    private static final long serialVersionUID = 0;
    private final int c;
    private final int d;
    private final long k0;
    private final long k1;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SipHashFunction(int c, int d, long k0, long k1) {
        Preconditions.checkArgument(c > 0, "The number of SipRound iterations (c=%s) during Compression must be positive.", c);
        Preconditions.checkArgument(d > 0, "The number of SipRound iterations (d=%s) during Finalization must be positive.", d);
        this.c = c;
        this.d = d;
        this.k0 = k0;
        this.k1 = k1;
    }

    @Override // com.google.common.hash.HashFunction
    public int bits() {
        return 64;
    }

    @Override // com.google.common.hash.HashFunction
    public Hasher newHasher() {
        return new SipHasher(this.c, this.d, this.k0, this.k1);
    }

    public String toString() {
        return "Hashing.sipHash" + this.c + "" + this.d + "(" + this.k0 + ", " + this.k1 + ")";
    }

    public boolean equals(@Nullable Object object) {
        if (!(object instanceof SipHashFunction)) {
            return false;
        }
        SipHashFunction other = (SipHashFunction) object;
        return this.c == other.c && this.d == other.d && this.k0 == other.k0 && this.k1 == other.k1;
    }

    public int hashCode() {
        return (int) ((((getClass().hashCode() ^ this.c) ^ this.d) ^ this.k0) ^ this.k1);
    }

    /* loaded from: classes.dex */
    private static final class SipHasher extends AbstractStreamingHashFunction.AbstractStreamingHasher {
        private static final int CHUNK_SIZE = 8;
        private long b;
        private final int c;
        private final int d;
        private long finalM;
        private long v0;
        private long v1;
        private long v2;
        private long v3;

        SipHasher(int c, int d, long k0, long k1) {
            super(8);
            this.v0 = 8317987319222330741L;
            this.v1 = 7237128888997146477L;
            this.v2 = 7816392313619706465L;
            this.v3 = 8387220255154660723L;
            this.b = 0L;
            this.finalM = 0L;
            this.c = c;
            this.d = d;
            this.v0 ^= k0;
            this.v1 ^= k1;
            this.v2 ^= k0;
            this.v3 ^= k1;
        }

        @Override // com.google.common.hash.AbstractStreamingHashFunction.AbstractStreamingHasher
        protected void process(ByteBuffer buffer) {
            this.b += 8;
            processM(buffer.getLong());
        }

        @Override // com.google.common.hash.AbstractStreamingHashFunction.AbstractStreamingHasher
        protected void processRemaining(ByteBuffer buffer) {
            this.b += buffer.remaining();
            int i = 0;
            while (buffer.hasRemaining()) {
                this.finalM ^= (buffer.get() & 255) << i;
                i += 8;
            }
        }

        @Override // com.google.common.hash.AbstractStreamingHashFunction.AbstractStreamingHasher
        public HashCode makeHash() {
            this.finalM ^= this.b << 56;
            processM(this.finalM);
            this.v2 ^= 255;
            sipRound(this.d);
            return HashCode.fromLong(((this.v0 ^ this.v1) ^ this.v2) ^ this.v3);
        }

        private void processM(long m) {
            this.v3 ^= m;
            sipRound(this.c);
            this.v0 ^= m;
        }

        private void sipRound(int iterations) {
            for (int i = 0; i < iterations; i++) {
                this.v0 += this.v1;
                this.v2 += this.v3;
                this.v1 = Long.rotateLeft(this.v1, 13);
                this.v3 = Long.rotateLeft(this.v3, 16);
                this.v1 ^= this.v0;
                this.v3 ^= this.v2;
                this.v0 = Long.rotateLeft(this.v0, 32);
                this.v2 += this.v1;
                this.v0 += this.v3;
                this.v1 = Long.rotateLeft(this.v1, 17);
                this.v3 = Long.rotateLeft(this.v3, 21);
                this.v1 ^= this.v2;
                this.v3 ^= this.v0;
                this.v2 = Long.rotateLeft(this.v2, 32);
            }
        }
    }
}
