package io.netty.handler.codec.compression;

import com.ning.compress.BufferRecycler;
import com.ning.compress.lzf.ChunkDecoder;
import com.ning.compress.lzf.util.ChunkDecoderFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

/* loaded from: classes4.dex */
public class LzfDecoder extends ByteToMessageDecoder {
    private static final short MAGIC_NUMBER = 23126;
    private int chunkLength;
    private State currentState;
    private ChunkDecoder decoder;
    private boolean isCompressed;
    private int originalLength;
    private BufferRecycler recycler;

    /* loaded from: classes4.dex */
    private enum State {
        INIT_BLOCK,
        INIT_ORIGINAL_LENGTH,
        DECOMPRESS_DATA,
        CORRUPTED
    }

    public LzfDecoder() {
        this(false);
    }

    public LzfDecoder(boolean safeInstance) {
        ChunkDecoder optimalInstance;
        this.currentState = State.INIT_BLOCK;
        if (safeInstance) {
            optimalInstance = ChunkDecoderFactory.safeInstance();
        } else {
            optimalInstance = ChunkDecoderFactory.optimalInstance();
        }
        this.decoder = optimalInstance;
        this.recycler = BufferRecycler.instance();
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0016. Please report as an issue. */
    @Override // io.netty.handler.codec.ByteToMessageDecoder
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int inPos;
        byte[] inputArray;
        byte[] outputArray;
        int outPos;
        byte[] outputArray2;
        try {
            switch (this.currentState) {
                case INIT_BLOCK:
                    if (in.readableBytes() >= 5) {
                        int magic = in.readUnsignedShort();
                        if (magic != 23126) {
                            throw new DecompressionException("unexpected block identifier");
                        }
                        int type = in.readByte();
                        switch (type) {
                            case 0:
                                this.isCompressed = false;
                                this.currentState = State.DECOMPRESS_DATA;
                                break;
                            case 1:
                                this.isCompressed = true;
                                this.currentState = State.INIT_ORIGINAL_LENGTH;
                                break;
                            default:
                                throw new DecompressionException(String.format("unknown type of chunk: %d (expected: %d or %d)", Integer.valueOf(type), 0, 1));
                        }
                        this.chunkLength = in.readUnsignedShort();
                        if (this.chunkLength > 65535) {
                            throw new DecompressionException(String.format("chunk length exceeds maximum: %d (expected: =< %d)", Integer.valueOf(this.chunkLength), 65535));
                        }
                        if (type != 1) {
                            return;
                        }
                    } else {
                        return;
                    }
                case INIT_ORIGINAL_LENGTH:
                    int magic2 = in.readableBytes();
                    if (magic2 >= 2) {
                        this.originalLength = in.readUnsignedShort();
                        if (this.originalLength <= 65535) {
                            this.currentState = State.DECOMPRESS_DATA;
                        } else {
                            throw new DecompressionException(String.format("original length exceeds maximum: %d (expected: =< %d)", Integer.valueOf(this.chunkLength), 65535));
                        }
                    } else {
                        return;
                    }
                case DECOMPRESS_DATA:
                    int chunkLength = this.chunkLength;
                    if (in.readableBytes() >= chunkLength) {
                        int originalLength = this.originalLength;
                        if (this.isCompressed) {
                            int idx = in.readerIndex();
                            if (in.hasArray()) {
                                byte[] inputArray2 = in.array();
                                inPos = in.arrayOffset() + idx;
                                inputArray = inputArray2;
                            } else {
                                byte[] inputArray3 = this.recycler.allocInputBuffer(chunkLength);
                                in.getBytes(idx, inputArray3, 0, chunkLength);
                                inPos = 0;
                                inputArray = inputArray3;
                            }
                            ByteBuf uncompressed = ctx.alloc().heapBuffer(originalLength, originalLength);
                            if (uncompressed.hasArray()) {
                                byte[] outputArray3 = uncompressed.array();
                                outputArray = outputArray3;
                                outPos = uncompressed.arrayOffset() + uncompressed.writerIndex();
                            } else {
                                byte[] outputArray4 = new byte[originalLength];
                                outputArray = outputArray4;
                                outPos = 0;
                            }
                            try {
                                outputArray2 = outputArray;
                            } catch (Throwable th) {
                                th = th;
                            }
                            try {
                                this.decoder.decodeChunk(inputArray, inPos, outputArray, outPos, outPos + originalLength);
                                if (uncompressed.hasArray()) {
                                    uncompressed.writerIndex(uncompressed.writerIndex() + originalLength);
                                } else {
                                    uncompressed.writeBytes(outputArray2);
                                }
                                out.add(uncompressed);
                                in.skipBytes(chunkLength);
                                if (1 == 0) {
                                    uncompressed.release();
                                }
                                if (!in.hasArray()) {
                                    this.recycler.releaseInputBuffer(inputArray);
                                }
                            } catch (Throwable th2) {
                                th = th2;
                                if (0 == 0) {
                                    uncompressed.release();
                                }
                                throw th;
                            }
                        } else if (chunkLength > 0) {
                            out.add(in.readRetainedSlice(chunkLength));
                        }
                        this.currentState = State.INIT_BLOCK;
                        return;
                    }
                    return;
                case CORRUPTED:
                    in.skipBytes(in.readableBytes());
                    return;
                default:
                    throw new IllegalStateException();
            }
        } catch (Exception e) {
            this.currentState = State.CORRUPTED;
            this.decoder = null;
            this.recycler = null;
            throw e;
        }
    }
}
