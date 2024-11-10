package io.netty.handler.codec.compression;

import com.ning.compress.BufferRecycler;
import com.ning.compress.lzf.ChunkEncoder;
import com.ning.compress.lzf.LZFChunk;
import com.ning.compress.lzf.LZFEncoder;
import com.ning.compress.lzf.util.ChunkEncoderFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/* loaded from: classes4.dex */
public class LzfEncoder extends MessageToByteEncoder<ByteBuf> {
    private static final int MIN_BLOCK_TO_COMPRESS = 16;
    private final int compressThreshold;
    private final ChunkEncoder encoder;
    private final BufferRecycler recycler;

    public LzfEncoder() {
        this(false);
    }

    public LzfEncoder(boolean safeInstance) {
        this(safeInstance, 65535);
    }

    public LzfEncoder(boolean safeInstance, int totalLength) {
        this(safeInstance, totalLength, 16);
    }

    public LzfEncoder(int totalLength) {
        this(false, totalLength);
    }

    public LzfEncoder(boolean safeInstance, int totalLength, int compressThreshold) {
        super(false);
        ChunkEncoder optimalNonAllocatingInstance;
        if (totalLength < 16 || totalLength > 65535) {
            throw new IllegalArgumentException("totalLength: " + totalLength + " (expected: 16-65535)");
        }
        if (compressThreshold < 16) {
            throw new IllegalArgumentException("compressThreshold:" + compressThreshold + " expected >=16");
        }
        this.compressThreshold = compressThreshold;
        if (safeInstance) {
            optimalNonAllocatingInstance = ChunkEncoderFactory.safeNonAllocatingInstance(totalLength);
        } else {
            optimalNonAllocatingInstance = ChunkEncoderFactory.optimalNonAllocatingInstance(totalLength);
        }
        this.encoder = optimalNonAllocatingInstance;
        this.recycler = BufferRecycler.instance();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageToByteEncoder
    public void encode(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) throws Exception {
        byte[] input;
        int inputPtr;
        byte[] output;
        int outputPtr;
        int outputLength;
        int length = in.readableBytes();
        int idx = in.readerIndex();
        if (in.hasArray()) {
            input = in.array();
            inputPtr = in.arrayOffset() + idx;
        } else {
            byte[] input2 = this.recycler.allocInputBuffer(length);
            in.getBytes(idx, input2, 0, length);
            input = input2;
            inputPtr = 0;
        }
        int maxOutputLength = LZFEncoder.estimateMaxWorkspaceSize(length) + 1;
        out.ensureWritable(maxOutputLength);
        if (out.hasArray()) {
            byte[] output2 = out.array();
            output = output2;
            outputPtr = out.arrayOffset() + out.writerIndex();
        } else {
            byte[] output3 = new byte[maxOutputLength];
            output = output3;
            outputPtr = 0;
        }
        if (length >= this.compressThreshold) {
            outputLength = encodeCompress(input, inputPtr, length, output, outputPtr);
        } else {
            outputLength = encodeNonCompress(input, inputPtr, length, output, outputPtr);
        }
        if (out.hasArray()) {
            out.writerIndex(out.writerIndex() + outputLength);
        } else {
            out.writeBytes(output, 0, outputLength);
        }
        in.skipBytes(length);
        if (!in.hasArray()) {
            this.recycler.releaseInputBuffer(input);
        }
    }

    private int encodeCompress(byte[] input, int inputPtr, int length, byte[] output, int outputPtr) {
        return LZFEncoder.appendEncoded(this.encoder, input, inputPtr, length, output, outputPtr) - outputPtr;
    }

    private static int lzfEncodeNonCompress(byte[] input, int inputPtr, int length, byte[] output, int outputPtr) {
        int chunkLen = Math.min(65535, length);
        int outputPtr2 = LZFChunk.appendNonCompressed(input, inputPtr, chunkLen, output, outputPtr);
        int left = length - chunkLen;
        if (left < 1) {
            return outputPtr2;
        }
        int inputPtr2 = inputPtr + chunkLen;
        do {
            int chunkLen2 = Math.min(left, 65535);
            outputPtr2 = LZFChunk.appendNonCompressed(input, inputPtr2, chunkLen2, output, outputPtr2);
            inputPtr2 += chunkLen2;
            left -= chunkLen2;
        } while (left > 0);
        return outputPtr2;
    }

    private static int encodeNonCompress(byte[] input, int inputPtr, int length, byte[] output, int outputPtr) {
        return lzfEncodeNonCompress(input, inputPtr, length, output, outputPtr) - outputPtr;
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        this.encoder.close();
        super.handlerRemoved(ctx);
    }
}
