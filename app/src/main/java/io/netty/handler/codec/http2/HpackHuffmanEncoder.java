package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.util.AsciiString;
import io.netty.util.ByteProcessor;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class HpackHuffmanEncoder {
    private final int[] codes;
    private final EncodeProcessor encodeProcessor;
    private final EncodedLengthProcessor encodedLengthProcessor;
    private final byte[] lengths;

    /* JADX INFO: Access modifiers changed from: package-private */
    public HpackHuffmanEncoder() {
        this(HpackUtil.HUFFMAN_CODES, HpackUtil.HUFFMAN_CODE_LENGTHS);
    }

    private HpackHuffmanEncoder(int[] codes, byte[] lengths) {
        this.encodedLengthProcessor = new EncodedLengthProcessor();
        this.encodeProcessor = new EncodeProcessor();
        this.codes = codes;
        this.lengths = lengths;
    }

    public void encode(ByteBuf out, CharSequence data) {
        ObjectUtil.checkNotNull(out, "out");
        if (data instanceof AsciiString) {
            AsciiString string = (AsciiString) data;
            try {
                try {
                    this.encodeProcessor.out = out;
                    string.forEachByte(this.encodeProcessor);
                } catch (Exception e) {
                    PlatformDependent.throwException(e);
                }
                return;
            } finally {
                this.encodeProcessor.end();
            }
        }
        encodeSlowPath(out, data);
    }

    private void encodeSlowPath(ByteBuf out, CharSequence data) {
        long current = 0;
        int n = 0;
        for (int i = 0; i < data.length(); i++) {
            int b = AsciiString.c2b(data.charAt(i)) & 255;
            int code = this.codes[b];
            int nbits = this.lengths[b];
            current = (current << nbits) | code;
            n += nbits;
            while (n >= 8) {
                n -= 8;
                out.writeByte((int) (current >> n));
            }
        }
        if (n > 0) {
            out.writeByte((int) ((current << (8 - n)) | (255 >>> n)));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getEncodedLength(CharSequence data) {
        if (data instanceof AsciiString) {
            AsciiString string = (AsciiString) data;
            try {
                this.encodedLengthProcessor.reset();
                string.forEachByte(this.encodedLengthProcessor);
                return this.encodedLengthProcessor.length();
            } catch (Exception e) {
                PlatformDependent.throwException(e);
                return -1;
            }
        }
        return getEncodedLengthSlowPath(data);
    }

    private int getEncodedLengthSlowPath(CharSequence data) {
        long len = 0;
        for (int i = 0; i < data.length(); i++) {
            len += this.lengths[AsciiString.c2b(data.charAt(i)) & 255];
        }
        return (int) ((7 + len) >> 3);
    }

    /* loaded from: classes4.dex */
    private final class EncodeProcessor implements ByteProcessor {
        private long current;
        private int n;
        ByteBuf out;

        private EncodeProcessor() {
        }

        @Override // io.netty.util.ByteProcessor
        public boolean process(byte value) {
            int b = value & 255;
            int nbits = HpackHuffmanEncoder.this.lengths[b];
            this.current <<= nbits;
            this.current |= HpackHuffmanEncoder.this.codes[b];
            this.n += nbits;
            while (this.n >= 8) {
                this.n -= 8;
                this.out.writeByte((int) (this.current >> this.n));
            }
            return true;
        }

        void end() {
            try {
                if (this.n > 0) {
                    this.current <<= 8 - this.n;
                    this.current |= 255 >>> this.n;
                    this.out.writeByte((int) this.current);
                }
            } finally {
                this.out = null;
                this.current = 0L;
                this.n = 0;
            }
        }
    }

    /* loaded from: classes4.dex */
    private final class EncodedLengthProcessor implements ByteProcessor {
        private long len;

        private EncodedLengthProcessor() {
        }

        @Override // io.netty.util.ByteProcessor
        public boolean process(byte value) {
            this.len += HpackHuffmanEncoder.this.lengths[value & 255];
            return true;
        }

        void reset() {
            this.len = 0L;
        }

        int length() {
            return (int) ((this.len + 7) >> 3);
        }
    }
}
