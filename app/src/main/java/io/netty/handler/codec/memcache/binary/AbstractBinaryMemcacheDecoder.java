package io.netty.handler.codec.memcache.binary;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.memcache.AbstractMemcacheObjectDecoder;
import io.netty.handler.codec.memcache.DefaultLastMemcacheContent;
import io.netty.handler.codec.memcache.DefaultMemcacheContent;
import io.netty.handler.codec.memcache.LastMemcacheContent;
import io.netty.handler.codec.memcache.MemcacheContent;
import io.netty.handler.codec.memcache.binary.BinaryMemcacheMessage;
import io.netty.util.internal.ObjectUtil;
import java.util.List;

/* loaded from: classes4.dex */
public abstract class AbstractBinaryMemcacheDecoder<M extends BinaryMemcacheMessage> extends AbstractMemcacheObjectDecoder {
    public static final int DEFAULT_MAX_CHUNK_SIZE = 8192;
    private int alreadyReadChunkSize;
    private final int chunkSize;
    private M currentMessage;
    private State state;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public enum State {
        READ_HEADER,
        READ_EXTRAS,
        READ_KEY,
        READ_CONTENT,
        BAD_MESSAGE
    }

    protected abstract M buildInvalidMessage();

    protected abstract M decodeHeader(ByteBuf byteBuf);

    protected AbstractBinaryMemcacheDecoder() {
        this(8192);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractBinaryMemcacheDecoder(int chunkSize) {
        this.state = State.READ_HEADER;
        ObjectUtil.checkPositiveOrZero(chunkSize, "chunkSize");
        this.chunkSize = chunkSize;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x000a. Please report as an issue. */
    @Override // io.netty.handler.codec.ByteToMessageDecoder
    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        MemcacheContent chunk;
        switch (this.state) {
            case READ_HEADER:
                try {
                    if (in.readableBytes() < 24) {
                        return;
                    }
                    resetDecoder();
                    this.currentMessage = decodeHeader(in);
                    this.state = State.READ_EXTRAS;
                } catch (Exception e) {
                    resetDecoder();
                    out.add(invalidMessage(e));
                    return;
                }
            case READ_EXTRAS:
                try {
                    byte extrasLength = this.currentMessage.extrasLength();
                    if (extrasLength > 0) {
                        if (in.readableBytes() < extrasLength) {
                            return;
                        } else {
                            this.currentMessage.setExtras(in.readRetainedSlice(extrasLength));
                        }
                    }
                    this.state = State.READ_KEY;
                } catch (Exception e2) {
                    resetDecoder();
                    out.add(invalidMessage(e2));
                    return;
                }
            case READ_KEY:
                try {
                    short keyLength = this.currentMessage.keyLength();
                    if (keyLength > 0) {
                        if (in.readableBytes() < keyLength) {
                            return;
                        } else {
                            this.currentMessage.setKey(in.readRetainedSlice(keyLength));
                        }
                    }
                    out.add(this.currentMessage.retain());
                    this.state = State.READ_CONTENT;
                } catch (Exception e3) {
                    resetDecoder();
                    out.add(invalidMessage(e3));
                    return;
                }
            case READ_CONTENT:
                try {
                    int valueLength = (this.currentMessage.totalBodyLength() - this.currentMessage.keyLength()) - this.currentMessage.extrasLength();
                    int toRead = in.readableBytes();
                    if (valueLength > 0) {
                        if (toRead == 0) {
                            return;
                        }
                        if (toRead > this.chunkSize) {
                            toRead = this.chunkSize;
                        }
                        int remainingLength = valueLength - this.alreadyReadChunkSize;
                        if (toRead > remainingLength) {
                            toRead = remainingLength;
                        }
                        ByteBuf chunkBuffer = in.readRetainedSlice(toRead);
                        int i = this.alreadyReadChunkSize + toRead;
                        this.alreadyReadChunkSize = i;
                        if (i >= valueLength) {
                            chunk = new DefaultLastMemcacheContent(chunkBuffer);
                        } else {
                            chunk = new DefaultMemcacheContent(chunkBuffer);
                        }
                        out.add(chunk);
                        if (this.alreadyReadChunkSize < valueLength) {
                            return;
                        }
                    } else {
                        out.add(LastMemcacheContent.EMPTY_LAST_CONTENT);
                    }
                    resetDecoder();
                    this.state = State.READ_HEADER;
                    return;
                } catch (Exception e4) {
                    resetDecoder();
                    out.add(invalidChunk(e4));
                    return;
                }
            case BAD_MESSAGE:
                in.skipBytes(actualReadableBytes());
                return;
            default:
                throw new Error("Unknown state reached: " + this.state);
        }
    }

    private M invalidMessage(Exception cause) {
        this.state = State.BAD_MESSAGE;
        M message = buildInvalidMessage();
        message.setDecoderResult(DecoderResult.failure(cause));
        return message;
    }

    private MemcacheContent invalidChunk(Exception cause) {
        this.state = State.BAD_MESSAGE;
        MemcacheContent chunk = new DefaultLastMemcacheContent(Unpooled.EMPTY_BUFFER);
        chunk.setDecoderResult(DecoderResult.failure(cause));
        return chunk;
    }

    @Override // io.netty.handler.codec.ByteToMessageDecoder, io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        resetDecoder();
    }

    protected void resetDecoder() {
        if (this.currentMessage != null) {
            this.currentMessage.release();
            this.currentMessage = null;
        }
        this.alreadyReadChunkSize = 0;
    }
}
