package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public class SpdyHeaderBlockRawDecoder extends SpdyHeaderBlockDecoder {
    private static final int LENGTH_FIELD_SIZE = 4;
    private ByteBuf cumulation;
    private int headerSize;
    private int length;
    private final int maxHeaderSize;
    private String name;
    private int numHeaders;
    private State state;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public enum State {
        READ_NUM_HEADERS,
        READ_NAME_LENGTH,
        READ_NAME,
        SKIP_NAME,
        READ_VALUE_LENGTH,
        READ_VALUE,
        SKIP_VALUE,
        END_HEADER_BLOCK,
        ERROR
    }

    public SpdyHeaderBlockRawDecoder(SpdyVersion spdyVersion, int maxHeaderSize) {
        ObjectUtil.checkNotNull(spdyVersion, "spdyVersion");
        this.maxHeaderSize = maxHeaderSize;
        this.state = State.READ_NUM_HEADERS;
    }

    private static int readLengthField(ByteBuf buffer) {
        int length = SpdyCodecUtil.getSignedInt(buffer, buffer.readerIndex());
        buffer.skipBytes(4);
        return length;
    }

    @Override // io.netty.handler.codec.spdy.SpdyHeaderBlockDecoder
    void decode(ByteBufAllocator alloc, ByteBuf headerBlock, SpdyHeadersFrame frame) throws Exception {
        ObjectUtil.checkNotNull(headerBlock, "headerBlock");
        ObjectUtil.checkNotNull(frame, "frame");
        if (this.cumulation == null) {
            decodeHeaderBlock(headerBlock, frame);
            if (headerBlock.isReadable()) {
                this.cumulation = alloc.buffer(headerBlock.readableBytes());
                this.cumulation.writeBytes(headerBlock);
                return;
            }
            return;
        }
        this.cumulation.writeBytes(headerBlock);
        decodeHeaderBlock(this.cumulation, frame);
        if (this.cumulation.isReadable()) {
            this.cumulation.discardReadBytes();
        } else {
            releaseBuffer();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00c3 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0001 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void decodeHeaderBlock(io.netty.buffer.ByteBuf r9, io.netty.handler.codec.spdy.SpdyHeadersFrame r10) throws java.lang.Exception {
        /*
            Method dump skipped, instructions count: 544
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.codec.spdy.SpdyHeaderBlockRawDecoder.decodeHeaderBlock(io.netty.buffer.ByteBuf, io.netty.handler.codec.spdy.SpdyHeadersFrame):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // io.netty.handler.codec.spdy.SpdyHeaderBlockDecoder
    public void endHeaderBlock(SpdyHeadersFrame frame) throws Exception {
        if (this.state != State.END_HEADER_BLOCK) {
            frame.setInvalid();
        }
        releaseBuffer();
        this.headerSize = 0;
        this.name = null;
        this.state = State.READ_NUM_HEADERS;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // io.netty.handler.codec.spdy.SpdyHeaderBlockDecoder
    public void end() {
        releaseBuffer();
    }

    private void releaseBuffer() {
        if (this.cumulation != null) {
            this.cumulation.release();
            this.cumulation = null;
        }
    }
}
