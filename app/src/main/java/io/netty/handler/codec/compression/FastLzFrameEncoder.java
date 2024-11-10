package io.netty.handler.codec.compression;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import java.util.zip.Adler32;
import java.util.zip.Checksum;

/* loaded from: classes4.dex */
public class FastLzFrameEncoder extends MessageToByteEncoder<ByteBuf> {
    private final ByteBufChecksum checksum;
    private final int level;

    public FastLzFrameEncoder() {
        this(0, null);
    }

    public FastLzFrameEncoder(int level) {
        this(level, null);
    }

    public FastLzFrameEncoder(boolean validateChecksums) {
        this(0, validateChecksums ? new Adler32() : null);
    }

    public FastLzFrameEncoder(int level, Checksum checksum) {
        if (level != 0 && level != 1 && level != 2) {
            throw new IllegalArgumentException(String.format("level: %d (expected: %d or %d or %d)", Integer.valueOf(level), 0, 1, 2));
        }
        this.level = level;
        this.checksum = checksum == null ? null : ByteBufChecksum.wrapChecksum(checksum);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageToByteEncoder
    public void encode(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) throws Exception {
        int compressedLength;
        int outputPtr;
        ByteBufChecksum checksum = this.checksum;
        while (in.isReadable()) {
            int idx = in.readerIndex();
            int length = Math.min(in.readableBytes(), 65535);
            int outputIdx = out.writerIndex();
            out.setMedium(outputIdx, 4607066);
            int i = 0;
            int outputOffset = outputIdx + 4 + (checksum != null ? 4 : 0);
            if (length < 32) {
                compressedLength = 0;
                out.ensureWritable(outputOffset + 2 + length);
                int outputPtr2 = outputOffset + 2;
                if (checksum != null) {
                    checksum.reset();
                    checksum.update(in, idx, length);
                    out.setInt(outputIdx + 4, (int) checksum.getValue());
                }
                out.setBytes(outputPtr2, in, idx, length);
                outputPtr = length;
            } else {
                if (checksum != null) {
                    checksum.reset();
                    checksum.update(in, idx, length);
                    out.setInt(outputIdx + 4, (int) checksum.getValue());
                }
                int maxOutputLength = FastLz.calculateOutputBufferLength(length);
                out.ensureWritable(outputOffset + 4 + maxOutputLength);
                int outputPtr3 = outputOffset + 4;
                int compressedLength2 = FastLz.compress(in, in.readerIndex(), length, out, outputPtr3, this.level);
                if (compressedLength2 < length) {
                    out.setShort(outputOffset, compressedLength2);
                    outputOffset += 2;
                    compressedLength = 1;
                    outputPtr = compressedLength2;
                } else {
                    out.setBytes(outputOffset + 2, in, idx, length);
                    compressedLength = 0;
                    outputPtr = length;
                }
            }
            out.setShort(outputOffset, length);
            int i2 = outputIdx + 3;
            if (checksum != null) {
                i = 16;
            }
            out.setByte(i2, compressedLength | i);
            out.writerIndex(outputOffset + 2 + outputPtr);
            in.skipBytes(length);
        }
    }
}
