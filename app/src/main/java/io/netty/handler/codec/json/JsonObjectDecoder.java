package io.netty.handler.codec.json;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.util.internal.ObjectUtil;
import java.util.List;

/* loaded from: classes4.dex */
public class JsonObjectDecoder extends ByteToMessageDecoder {
    private static final int ST_CORRUPTED = -1;
    private static final int ST_DECODING_ARRAY_STREAM = 2;
    private static final int ST_DECODING_NORMAL = 1;
    private static final int ST_INIT = 0;
    private int idx;
    private boolean insideString;
    private int lastReaderIndex;
    private final int maxObjectLength;
    private int openBraces;
    private int state;
    private final boolean streamArrayElements;

    public JsonObjectDecoder() {
        this(1048576);
    }

    public JsonObjectDecoder(int maxObjectLength) {
        this(maxObjectLength, false);
    }

    public JsonObjectDecoder(boolean streamArrayElements) {
        this(1048576, streamArrayElements);
    }

    public JsonObjectDecoder(int maxObjectLength, boolean streamArrayElements) {
        this.maxObjectLength = ObjectUtil.checkPositive(maxObjectLength, "maxObjectLength");
        this.streamArrayElements = streamArrayElements;
    }

    @Override // io.netty.handler.codec.ByteToMessageDecoder
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (this.state == -1) {
            in.skipBytes(in.readableBytes());
            return;
        }
        if (this.idx > in.readerIndex() && this.lastReaderIndex != in.readerIndex()) {
            this.idx = in.readerIndex() + (this.idx - this.lastReaderIndex);
        }
        int idx = this.idx;
        int wrtIdx = in.writerIndex();
        if (wrtIdx > this.maxObjectLength) {
            in.skipBytes(in.readableBytes());
            reset();
            throw new TooLongFrameException("object length exceeds " + this.maxObjectLength + ": " + wrtIdx + " bytes discarded");
        }
        while (idx < wrtIdx) {
            byte c = in.getByte(idx);
            if (this.state == 1) {
                decodeByte(c, in, idx);
                if (this.openBraces == 0) {
                    ByteBuf json = extractObject(ctx, in, in.readerIndex(), (idx + 1) - in.readerIndex());
                    if (json != null) {
                        out.add(json);
                    }
                    in.readerIndex(idx + 1);
                    reset();
                }
            } else if (this.state == 2) {
                decodeByte(c, in, idx);
                if (!this.insideString && ((this.openBraces == 1 && c == 44) || (this.openBraces == 0 && c == 93))) {
                    for (int i = in.readerIndex(); Character.isWhitespace(in.getByte(i)); i++) {
                        in.skipBytes(1);
                    }
                    int idxNoSpaces = idx - 1;
                    while (idxNoSpaces >= in.readerIndex() && Character.isWhitespace(in.getByte(idxNoSpaces))) {
                        idxNoSpaces--;
                    }
                    ByteBuf json2 = extractObject(ctx, in, in.readerIndex(), (idxNoSpaces + 1) - in.readerIndex());
                    if (json2 != null) {
                        out.add(json2);
                    }
                    in.readerIndex(idx + 1);
                    if (c == 93) {
                        reset();
                    }
                }
            } else if (c == 123 || c == 91) {
                initDecoding(c);
                if (this.state == 2) {
                    in.skipBytes(1);
                }
            } else if (Character.isWhitespace(c)) {
                in.skipBytes(1);
            } else {
                this.state = -1;
                throw new CorruptedFrameException("invalid JSON received at byte position " + idx + ": " + ByteBufUtil.hexDump(in));
            }
            idx++;
        }
        if (in.readableBytes() == 0) {
            this.idx = 0;
        } else {
            this.idx = idx;
        }
        this.lastReaderIndex = in.readerIndex();
    }

    protected ByteBuf extractObject(ChannelHandlerContext ctx, ByteBuf buffer, int index, int length) {
        return buffer.retainedSlice(index, length);
    }

    private void decodeByte(byte c, ByteBuf in, int idx) {
        if ((c == 123 || c == 91) && !this.insideString) {
            this.openBraces++;
            return;
        }
        if ((c == 125 || c == 93) && !this.insideString) {
            this.openBraces--;
            return;
        }
        if (c == 34) {
            if (!this.insideString) {
                this.insideString = true;
                return;
            }
            int backslashCount = 0;
            for (int idx2 = idx - 1; idx2 >= 0 && in.getByte(idx2) == 92; idx2--) {
                backslashCount++;
            }
            if (backslashCount % 2 == 0) {
                this.insideString = false;
            }
        }
    }

    private void initDecoding(byte openingBrace) {
        this.openBraces = 1;
        if (openingBrace == 91 && this.streamArrayElements) {
            this.state = 2;
        } else {
            this.state = 1;
        }
    }

    private void reset() {
        this.insideString = false;
        this.state = 0;
        this.openBraces = 0;
    }
}
