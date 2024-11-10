package io.netty.handler.codec.compression;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

/* loaded from: classes4.dex */
public class SnappyFrameDecoder extends ByteToMessageDecoder {
    private static final int MAX_COMPRESSED_CHUNK_SIZE = 16777215;
    private static final int MAX_DECOMPRESSED_DATA_SIZE = 65536;
    private static final int MAX_UNCOMPRESSED_DATA_SIZE = 65540;
    private static final int SNAPPY_IDENTIFIER_LEN = 6;
    private boolean corrupted;
    private int numBytesToSkip;
    private final Snappy snappy;
    private boolean started;
    private final boolean validateChecksums;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public enum ChunkType {
        STREAM_IDENTIFIER,
        COMPRESSED_DATA,
        UNCOMPRESSED_DATA,
        RESERVED_UNSKIPPABLE,
        RESERVED_SKIPPABLE
    }

    public SnappyFrameDecoder() {
        this(false);
    }

    public SnappyFrameDecoder(boolean validateChecksums) {
        this.snappy = new Snappy();
        this.validateChecksums = validateChecksums;
    }

    @Override // io.netty.handler.codec.ByteToMessageDecoder
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int idx;
        int inSize;
        if (this.corrupted) {
            in.skipBytes(in.readableBytes());
            return;
        }
        if (this.numBytesToSkip != 0) {
            int skipBytes = Math.min(this.numBytesToSkip, in.readableBytes());
            in.skipBytes(skipBytes);
            this.numBytesToSkip -= skipBytes;
            return;
        }
        try {
            idx = in.readerIndex();
            inSize = in.readableBytes();
        } catch (Exception e) {
            this.corrupted = true;
            throw e;
        }
        if (inSize < 4) {
            return;
        }
        int chunkTypeVal = in.getUnsignedByte(idx);
        ChunkType chunkType = mapChunkType((byte) chunkTypeVal);
        int chunkLength = in.getUnsignedMediumLE(idx + 1);
        switch (chunkType) {
            case STREAM_IDENTIFIER:
                if (chunkLength != 6) {
                    throw new DecompressionException("Unexpected length of stream identifier: " + chunkLength);
                }
                if (inSize >= 10) {
                    in.skipBytes(4);
                    int offset = in.readerIndex();
                    in.skipBytes(6);
                    int offset2 = offset + 1;
                    checkByte(in.getByte(offset), (byte) 115);
                    int offset3 = offset2 + 1;
                    checkByte(in.getByte(offset2), (byte) 78);
                    int offset4 = offset3 + 1;
                    checkByte(in.getByte(offset3), (byte) 97);
                    int offset5 = offset4 + 1;
                    checkByte(in.getByte(offset4), (byte) 80);
                    checkByte(in.getByte(offset5), (byte) 112);
                    checkByte(in.getByte(offset5 + 1), (byte) 89);
                    this.started = true;
                    return;
                }
                return;
            case RESERVED_SKIPPABLE:
                if (!this.started) {
                    throw new DecompressionException("Received RESERVED_SKIPPABLE tag before STREAM_IDENTIFIER");
                }
                in.skipBytes(4);
                int skipBytes2 = Math.min(chunkLength, in.readableBytes());
                in.skipBytes(skipBytes2);
                if (skipBytes2 != chunkLength) {
                    this.numBytesToSkip = chunkLength - skipBytes2;
                    return;
                }
                return;
            case RESERVED_UNSKIPPABLE:
                throw new DecompressionException("Found reserved unskippable chunk type: 0x" + Integer.toHexString(chunkTypeVal));
            case UNCOMPRESSED_DATA:
                if (!this.started) {
                    throw new DecompressionException("Received UNCOMPRESSED_DATA tag before STREAM_IDENTIFIER");
                }
                if (chunkLength > 65540) {
                    throw new DecompressionException("Received UNCOMPRESSED_DATA larger than 65540 bytes");
                }
                if (inSize < chunkLength + 4) {
                    return;
                }
                in.skipBytes(4);
                if (this.validateChecksums) {
                    int checksum = in.readIntLE();
                    Snappy.validateChecksum(checksum, in, in.readerIndex(), chunkLength - 4);
                } else {
                    in.skipBytes(4);
                }
                out.add(in.readRetainedSlice(chunkLength - 4));
                return;
            case COMPRESSED_DATA:
                if (!this.started) {
                    throw new DecompressionException("Received COMPRESSED_DATA tag before STREAM_IDENTIFIER");
                }
                if (chunkLength > 16777215) {
                    throw new DecompressionException("Received COMPRESSED_DATA that contains chunk that exceeds 16777215 bytes");
                }
                if (inSize < chunkLength + 4) {
                    return;
                }
                in.skipBytes(4);
                int checksum2 = in.readIntLE();
                int uncompressedSize = this.snappy.getPreamble(in);
                if (uncompressedSize <= 65536) {
                    ByteBuf uncompressed = ctx.alloc().buffer(uncompressedSize, 65536);
                    try {
                        if (this.validateChecksums) {
                            int oldWriterIndex = in.writerIndex();
                            try {
                                in.writerIndex((in.readerIndex() + chunkLength) - 4);
                                this.snappy.decode(in, uncompressed);
                                in.writerIndex(oldWriterIndex);
                                Snappy.validateChecksum(checksum2, uncompressed, 0, uncompressed.writerIndex());
                            } catch (Throwable th) {
                                in.writerIndex(oldWriterIndex);
                                throw th;
                            }
                        } else {
                            this.snappy.decode(in.readSlice(chunkLength - 4), uncompressed);
                        }
                        out.add(uncompressed);
                        uncompressed = null;
                        this.snappy.reset();
                        return;
                    } finally {
                        if (uncompressed != null) {
                            uncompressed.release();
                        }
                    }
                }
                throw new DecompressionException("Received COMPRESSED_DATA that contains uncompressed data that exceeds 65536 bytes");
            default:
                return;
        }
        this.corrupted = true;
        throw e;
    }

    private static void checkByte(byte actual, byte expect) {
        if (actual != expect) {
            throw new DecompressionException("Unexpected stream identifier contents. Mismatched snappy protocol version?");
        }
    }

    private static ChunkType mapChunkType(byte type) {
        if (type == 0) {
            return ChunkType.COMPRESSED_DATA;
        }
        if (type == 1) {
            return ChunkType.UNCOMPRESSED_DATA;
        }
        if (type == -1) {
            return ChunkType.STREAM_IDENTIFIER;
        }
        if ((type & 128) == 128) {
            return ChunkType.RESERVED_SKIPPABLE;
        }
        return ChunkType.RESERVED_UNSKIPPABLE;
    }
}
