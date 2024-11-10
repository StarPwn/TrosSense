package org.cloudburstmc.protocol.bedrock.netty.codec.compression;

import androidx.emoji2.text.flatbuffer.Utf8Old$$ExternalSyntheticThreadLocal1;
import io.airlift.compress.snappy.SnappyRawCompressor;
import io.airlift.compress.snappy.SnappyRawDecompressor;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.util.function.Supplier;
import org.cloudburstmc.protocol.bedrock.data.CompressionAlgorithm;
import org.cloudburstmc.protocol.bedrock.data.PacketCompressionAlgorithm;
import sun.misc.Unsafe;

/* loaded from: classes5.dex */
public class SnappyCompression implements BatchCompression {
    private static final ThreadLocal<short[]> TABLE = new Utf8Old$$ExternalSyntheticThreadLocal1(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.netty.codec.compression.SnappyCompression$$ExternalSyntheticLambda0
        @Override // java.util.function.Supplier
        public final Object get() {
            return SnappyCompression.lambda$static$0();
        }
    });

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ short[] lambda$static$0() {
        return new short[16384];
    }

    @Override // org.cloudburstmc.protocol.bedrock.netty.codec.compression.BatchCompression
    public ByteBuf encode(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        ByteBuf direct;
        long outputAddress;
        long outputEndAddress;
        if (!msg.isDirect() || (msg instanceof CompositeByteBuf)) {
            ByteBuf direct2 = ctx.alloc().ioBuffer(msg.readableBytes());
            direct2.writeBytes(msg);
            direct = direct2;
        } else {
            direct = msg;
        }
        ByteBuf output = ctx.alloc().directBuffer();
        try {
            long inputAddress = direct.memoryAddress() + direct.readerIndex();
            long inputEndAddress = direct.readableBytes() + inputAddress;
            output.ensureWritable(SnappyRawCompressor.maxCompressedLength(direct.readableBytes()));
            byte[] outputArray = null;
            if (output.isDirect() && output.hasMemoryAddress()) {
                long outputAddress2 = output.memoryAddress() + output.writerIndex();
                outputAddress = outputAddress2;
                outputEndAddress = output.writableBytes() + outputAddress2;
            } else if (output.hasArray()) {
                outputArray = output.array();
                long outputAddress3 = Unsafe.ARRAY_BYTE_BASE_OFFSET + output.arrayOffset() + output.writerIndex();
                outputAddress = outputAddress3;
                outputEndAddress = Unsafe.ARRAY_BYTE_BASE_OFFSET + output.arrayOffset() + output.writableBytes();
            } else {
                throw new IllegalStateException("Unsupported ByteBuf " + output.getClass().getSimpleName());
            }
            int compressed = SnappyRawCompressor.compress(null, inputAddress, inputEndAddress, outputArray, outputAddress, outputEndAddress, TABLE.get());
            output.writerIndex(output.writerIndex() + compressed);
            return output.retain();
        } finally {
            output.release();
            if (direct != msg) {
                direct.release();
            }
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.netty.codec.compression.BatchCompression
    public ByteBuf decode(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        ByteBuf direct;
        long outputAddress;
        long outputEndAddress;
        if (!msg.isDirect() || (msg instanceof CompositeByteBuf)) {
            ByteBuf direct2 = ctx.alloc().ioBuffer(msg.readableBytes());
            direct2.writeBytes(msg);
            direct = direct2;
        } else {
            direct = msg;
        }
        ByteBuf output = ctx.alloc().directBuffer();
        try {
            long inputAddress = direct.memoryAddress() + direct.readerIndex();
            long inputEndAddress = direct.readableBytes() + inputAddress;
            output.ensureWritable(SnappyRawDecompressor.getUncompressedLength(null, inputAddress, inputEndAddress));
            byte[] outputArray = null;
            if (output.isDirect() && output.hasMemoryAddress()) {
                long outputAddress2 = output.memoryAddress() + output.writerIndex();
                outputAddress = outputAddress2;
                outputEndAddress = output.writableBytes() + outputAddress2;
            } else if (output.hasArray()) {
                outputArray = output.array();
                long outputAddress3 = Unsafe.ARRAY_BYTE_BASE_OFFSET + output.arrayOffset() + output.writerIndex();
                outputAddress = outputAddress3;
                outputEndAddress = Unsafe.ARRAY_BYTE_BASE_OFFSET + output.arrayOffset() + output.writableBytes();
            } else {
                throw new IllegalStateException("Unsupported ByteBuf " + output.getClass().getSimpleName());
            }
            int decompressed = SnappyRawDecompressor.decompress(null, inputAddress, inputEndAddress, outputArray, outputAddress, outputEndAddress);
            output.writerIndex(output.writerIndex() + decompressed);
            return output.retain();
        } finally {
            output.release();
            if (direct != msg) {
                direct.release();
            }
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.netty.codec.compression.BatchCompression
    public CompressionAlgorithm getAlgorithm() {
        return PacketCompressionAlgorithm.SNAPPY;
    }

    @Override // org.cloudburstmc.protocol.bedrock.netty.codec.compression.BatchCompression
    public void setLevel(int level) {
    }

    @Override // org.cloudburstmc.protocol.bedrock.netty.codec.compression.BatchCompression
    public int getLevel() {
        return -1;
    }
}
