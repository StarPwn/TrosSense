package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http2.Http2FrameReader;
import io.netty.handler.codec.http2.Http2HeadersDecoder;
import io.netty.util.internal.PlatformDependent;

/* loaded from: classes4.dex */
public class DefaultHttp2FrameReader implements Http2FrameReader, Http2FrameSizePolicy, Http2FrameReader.Configuration {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private Http2Flags flags;
    private byte frameType;
    private HeadersContinuation headersContinuation;
    private final Http2HeadersDecoder headersDecoder;
    private int maxFrameSize;
    private int payloadLength;
    private boolean readError;
    private boolean readingHeaders;
    private int streamId;

    public DefaultHttp2FrameReader() {
        this(true);
    }

    public DefaultHttp2FrameReader(boolean validateHeaders) {
        this(new DefaultHttp2HeadersDecoder(validateHeaders));
    }

    public DefaultHttp2FrameReader(Http2HeadersDecoder headersDecoder) {
        this.readingHeaders = true;
        this.headersDecoder = headersDecoder;
        this.maxFrameSize = 16384;
    }

    @Override // io.netty.handler.codec.http2.Http2FrameReader.Configuration
    public Http2HeadersDecoder.Configuration headersConfiguration() {
        return this.headersDecoder.configuration();
    }

    @Override // io.netty.handler.codec.http2.Http2FrameReader
    public Http2FrameReader.Configuration configuration() {
        return this;
    }

    @Override // io.netty.handler.codec.http2.Http2FrameReader.Configuration
    public Http2FrameSizePolicy frameSizePolicy() {
        return this;
    }

    @Override // io.netty.handler.codec.http2.Http2FrameSizePolicy
    public void maxFrameSize(int max) throws Http2Exception {
        if (!Http2CodecUtil.isMaxFrameSizeValid(max)) {
            throw Http2Exception.connectionError(Http2Error.FRAME_SIZE_ERROR, "Invalid MAX_FRAME_SIZE specified in sent settings: %d", Integer.valueOf(max));
        }
        this.maxFrameSize = max;
    }

    @Override // io.netty.handler.codec.http2.Http2FrameSizePolicy
    public int maxFrameSize() {
        return this.maxFrameSize;
    }

    @Override // io.netty.handler.codec.http2.Http2FrameReader, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        closeHeadersContinuation();
    }

    private void closeHeadersContinuation() {
        if (this.headersContinuation != null) {
            this.headersContinuation.close();
            this.headersContinuation = null;
        }
    }

    @Override // io.netty.handler.codec.http2.Http2FrameReader
    public void readFrame(ChannelHandlerContext ctx, ByteBuf input, Http2FrameListener listener) throws Http2Exception {
        if (this.readError) {
            input.skipBytes(input.readableBytes());
            return;
        }
        do {
            try {
                if ((this.readingHeaders && !preProcessFrame(input)) || input.readableBytes() < this.payloadLength) {
                    return;
                }
                ByteBuf framePayload = input.readSlice(this.payloadLength);
                this.readingHeaders = true;
                verifyFrameState();
                processPayloadState(ctx, framePayload, listener);
            } catch (Http2Exception e) {
                this.readError = true ^ Http2Exception.isStreamError(e);
                throw e;
            } catch (RuntimeException e2) {
                this.readError = true;
                throw e2;
            } catch (Throwable cause) {
                this.readError = true;
                PlatformDependent.throwException(cause);
                return;
            }
        } while (input.isReadable());
    }

    private boolean preProcessFrame(ByteBuf in) throws Http2Exception {
        if (in.readableBytes() < 9) {
            return false;
        }
        this.payloadLength = in.readUnsignedMedium();
        if (this.payloadLength > this.maxFrameSize) {
            throw Http2Exception.connectionError(Http2Error.FRAME_SIZE_ERROR, "Frame length: %d exceeds maximum: %d", Integer.valueOf(this.payloadLength), Integer.valueOf(this.maxFrameSize));
        }
        this.frameType = in.readByte();
        this.flags = new Http2Flags(in.readUnsignedByte());
        this.streamId = Http2CodecUtil.readUnsignedInt(in);
        this.readingHeaders = false;
        return true;
    }

    private void verifyFrameState() throws Http2Exception {
        switch (this.frameType) {
            case 0:
                verifyDataFrame();
                return;
            case 1:
                verifyHeadersFrame();
                return;
            case 2:
                verifyPriorityFrame();
                return;
            case 3:
                verifyRstStreamFrame();
                return;
            case 4:
                verifySettingsFrame();
                return;
            case 5:
                verifyPushPromiseFrame();
                return;
            case 6:
                verifyPingFrame();
                return;
            case 7:
                verifyGoAwayFrame();
                return;
            case 8:
                verifyWindowUpdateFrame();
                return;
            case 9:
                verifyContinuationFrame();
                return;
            default:
                verifyUnknownFrame();
                return;
        }
    }

    private void processPayloadState(ChannelHandlerContext ctx, ByteBuf in, Http2FrameListener listener) throws Http2Exception {
        if (in.readableBytes() != this.payloadLength) {
            throw new AssertionError();
        }
        switch (this.frameType) {
            case 0:
                readDataFrame(ctx, in, listener);
                return;
            case 1:
                readHeadersFrame(ctx, in, listener);
                return;
            case 2:
                readPriorityFrame(ctx, in, listener);
                return;
            case 3:
                readRstStreamFrame(ctx, in, listener);
                return;
            case 4:
                readSettingsFrame(ctx, in, listener);
                return;
            case 5:
                readPushPromiseFrame(ctx, in, listener);
                return;
            case 6:
                readPingFrame(ctx, in.readLong(), listener);
                return;
            case 7:
                readGoAwayFrame(ctx, in, listener);
                return;
            case 8:
                readWindowUpdateFrame(ctx, in, listener);
                return;
            case 9:
                readContinuationFrame(in, listener);
                return;
            default:
                readUnknownFrame(ctx, in, listener);
                return;
        }
    }

    private void verifyDataFrame() throws Http2Exception {
        verifyAssociatedWithAStream();
        verifyNotProcessingHeaders();
        if (this.payloadLength < this.flags.getPaddingPresenceFieldLength()) {
            throw Http2Exception.streamError(this.streamId, Http2Error.FRAME_SIZE_ERROR, "Frame length %d too small.", Integer.valueOf(this.payloadLength));
        }
    }

    private void verifyHeadersFrame() throws Http2Exception {
        verifyAssociatedWithAStream();
        verifyNotProcessingHeaders();
        int requiredLength = this.flags.getPaddingPresenceFieldLength() + this.flags.getNumPriorityBytes();
        if (this.payloadLength < requiredLength) {
            throw Http2Exception.connectionError(Http2Error.FRAME_SIZE_ERROR, "Frame length %d too small for HEADERS frame with stream %d.", Integer.valueOf(this.payloadLength), Integer.valueOf(this.streamId));
        }
    }

    private void verifyPriorityFrame() throws Http2Exception {
        verifyAssociatedWithAStream();
        verifyNotProcessingHeaders();
        if (this.payloadLength != 5) {
            throw Http2Exception.streamError(this.streamId, Http2Error.FRAME_SIZE_ERROR, "Invalid frame length %d.", Integer.valueOf(this.payloadLength));
        }
    }

    private void verifyRstStreamFrame() throws Http2Exception {
        verifyAssociatedWithAStream();
        verifyNotProcessingHeaders();
        if (this.payloadLength != 4) {
            throw Http2Exception.connectionError(Http2Error.FRAME_SIZE_ERROR, "Invalid frame length %d.", Integer.valueOf(this.payloadLength));
        }
    }

    private void verifySettingsFrame() throws Http2Exception {
        verifyNotProcessingHeaders();
        if (this.streamId != 0) {
            throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "A stream ID must be zero.", new Object[0]);
        }
        if (this.flags.ack() && this.payloadLength > 0) {
            throw Http2Exception.connectionError(Http2Error.FRAME_SIZE_ERROR, "Ack settings frame must have an empty payload.", new Object[0]);
        }
        if (this.payloadLength % 6 > 0) {
            throw Http2Exception.connectionError(Http2Error.FRAME_SIZE_ERROR, "Frame length %d invalid.", Integer.valueOf(this.payloadLength));
        }
    }

    private void verifyPushPromiseFrame() throws Http2Exception {
        verifyNotProcessingHeaders();
        int minLength = this.flags.getPaddingPresenceFieldLength() + 4;
        if (this.payloadLength < minLength) {
            throw Http2Exception.connectionError(Http2Error.FRAME_SIZE_ERROR, "Frame length %d too small for PUSH_PROMISE frame with stream id %d.", Integer.valueOf(this.payloadLength), Integer.valueOf(this.streamId));
        }
    }

    private void verifyPingFrame() throws Http2Exception {
        verifyNotProcessingHeaders();
        if (this.streamId != 0) {
            throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "A stream ID must be zero.", new Object[0]);
        }
        if (this.payloadLength != 8) {
            throw Http2Exception.connectionError(Http2Error.FRAME_SIZE_ERROR, "Frame length %d incorrect size for ping.", Integer.valueOf(this.payloadLength));
        }
    }

    private void verifyGoAwayFrame() throws Http2Exception {
        verifyNotProcessingHeaders();
        if (this.streamId != 0) {
            throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "A stream ID must be zero.", new Object[0]);
        }
        if (this.payloadLength < 8) {
            throw Http2Exception.connectionError(Http2Error.FRAME_SIZE_ERROR, "Frame length %d too small.", Integer.valueOf(this.payloadLength));
        }
    }

    private void verifyWindowUpdateFrame() throws Http2Exception {
        verifyNotProcessingHeaders();
        verifyStreamOrConnectionId(this.streamId, "Stream ID");
        if (this.payloadLength != 4) {
            throw Http2Exception.connectionError(Http2Error.FRAME_SIZE_ERROR, "Invalid frame length %d.", Integer.valueOf(this.payloadLength));
        }
    }

    private void verifyContinuationFrame() throws Http2Exception {
        verifyAssociatedWithAStream();
        if (this.headersContinuation == null) {
            throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Received %s frame but not currently processing headers.", Byte.valueOf(this.frameType));
        }
        if (this.streamId != this.headersContinuation.getStreamId()) {
            throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Continuation stream ID does not match pending headers. Expected %d, but received %d.", Integer.valueOf(this.headersContinuation.getStreamId()), Integer.valueOf(this.streamId));
        }
    }

    private void verifyUnknownFrame() throws Http2Exception {
        verifyNotProcessingHeaders();
    }

    private void readDataFrame(ChannelHandlerContext ctx, ByteBuf payload, Http2FrameListener listener) throws Http2Exception {
        int padding = readPadding(payload);
        verifyPadding(padding);
        int dataLength = lengthWithoutTrailingPadding(payload.readableBytes(), padding);
        payload.writerIndex(payload.readerIndex() + dataLength);
        listener.onDataRead(ctx, this.streamId, payload, padding, this.flags.endOfStream());
    }

    private void readHeadersFrame(final ChannelHandlerContext ctx, ByteBuf payload, Http2FrameListener listener) throws Http2Exception {
        final int headersStreamId = this.streamId;
        final Http2Flags headersFlags = this.flags;
        final int padding = readPadding(payload);
        verifyPadding(padding);
        if (!this.flags.priorityPresent()) {
            this.headersContinuation = new HeadersContinuation() { // from class: io.netty.handler.codec.http2.DefaultHttp2FrameReader.2
                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                {
                    super();
                }

                @Override // io.netty.handler.codec.http2.DefaultHttp2FrameReader.HeadersContinuation
                public int getStreamId() {
                    return headersStreamId;
                }

                @Override // io.netty.handler.codec.http2.DefaultHttp2FrameReader.HeadersContinuation
                public void processFragment(boolean endOfHeaders, ByteBuf fragment, int len, Http2FrameListener listener2) throws Http2Exception {
                    HeadersBlockBuilder hdrBlockBuilder = headersBlockBuilder();
                    hdrBlockBuilder.addFragment(fragment, len, ctx.alloc(), endOfHeaders);
                    if (endOfHeaders) {
                        listener2.onHeadersRead(ctx, headersStreamId, hdrBlockBuilder.headers(), padding, headersFlags.endOfStream());
                    }
                }
            };
            int len = lengthWithoutTrailingPadding(payload.readableBytes(), padding);
            this.headersContinuation.processFragment(this.flags.endOfHeaders(), payload, len, listener);
            resetHeadersContinuationIfEnd(this.flags.endOfHeaders());
            return;
        }
        long word1 = payload.readUnsignedInt();
        final boolean exclusive = (2147483648L & word1) != 0;
        final int streamDependency = (int) (2147483647L & word1);
        if (streamDependency == this.streamId) {
            throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "HEADERS frame for stream %d cannot depend on itself.", Integer.valueOf(this.streamId));
        }
        final short weight = (short) (payload.readUnsignedByte() + 1);
        int lenToRead = lengthWithoutTrailingPadding(payload.readableBytes(), padding);
        this.headersContinuation = new HeadersContinuation() { // from class: io.netty.handler.codec.http2.DefaultHttp2FrameReader.1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // io.netty.handler.codec.http2.DefaultHttp2FrameReader.HeadersContinuation
            public int getStreamId() {
                return headersStreamId;
            }

            @Override // io.netty.handler.codec.http2.DefaultHttp2FrameReader.HeadersContinuation
            public void processFragment(boolean endOfHeaders, ByteBuf fragment, int len2, Http2FrameListener listener2) throws Http2Exception {
                HeadersBlockBuilder hdrBlockBuilder = headersBlockBuilder();
                hdrBlockBuilder.addFragment(fragment, len2, ctx.alloc(), endOfHeaders);
                if (endOfHeaders) {
                    listener2.onHeadersRead(ctx, headersStreamId, hdrBlockBuilder.headers(), streamDependency, weight, exclusive, padding, headersFlags.endOfStream());
                }
            }
        };
        this.headersContinuation.processFragment(this.flags.endOfHeaders(), payload, lenToRead, listener);
        resetHeadersContinuationIfEnd(this.flags.endOfHeaders());
    }

    private void resetHeadersContinuationIfEnd(boolean endOfHeaders) {
        if (endOfHeaders) {
            closeHeadersContinuation();
        }
    }

    private void readPriorityFrame(ChannelHandlerContext ctx, ByteBuf payload, Http2FrameListener listener) throws Http2Exception {
        long word1 = payload.readUnsignedInt();
        boolean exclusive = (2147483648L & word1) != 0;
        int streamDependency = (int) (2147483647L & word1);
        if (streamDependency == this.streamId) {
            throw Http2Exception.streamError(this.streamId, Http2Error.PROTOCOL_ERROR, "A stream cannot depend on itself.", new Object[0]);
        }
        short weight = (short) (payload.readUnsignedByte() + 1);
        listener.onPriorityRead(ctx, this.streamId, streamDependency, weight, exclusive);
    }

    private void readRstStreamFrame(ChannelHandlerContext ctx, ByteBuf payload, Http2FrameListener listener) throws Http2Exception {
        long errorCode = payload.readUnsignedInt();
        listener.onRstStreamRead(ctx, this.streamId, errorCode);
    }

    private void readSettingsFrame(ChannelHandlerContext ctx, ByteBuf payload, Http2FrameListener listener) throws Http2Exception {
        if (this.flags.ack()) {
            listener.onSettingsAckRead(ctx);
            return;
        }
        int numSettings = this.payloadLength / 6;
        Http2Settings settings = new Http2Settings();
        for (int index = 0; index < numSettings; index++) {
            char id = (char) payload.readUnsignedShort();
            long value = payload.readUnsignedInt();
            try {
                settings.put(id, Long.valueOf(value));
            } catch (IllegalArgumentException e) {
                if (id == 4) {
                    throw Http2Exception.connectionError(Http2Error.FLOW_CONTROL_ERROR, e, "Failed setting initial window size: %s", e.getMessage());
                }
                throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, e, "Protocol error: %s", e.getMessage());
            }
        }
        listener.onSettingsRead(ctx, settings);
    }

    private void readPushPromiseFrame(final ChannelHandlerContext ctx, ByteBuf payload, Http2FrameListener listener) throws Http2Exception {
        final int pushPromiseStreamId = this.streamId;
        final int padding = readPadding(payload);
        verifyPadding(padding);
        final int promisedStreamId = Http2CodecUtil.readUnsignedInt(payload);
        this.headersContinuation = new HeadersContinuation() { // from class: io.netty.handler.codec.http2.DefaultHttp2FrameReader.3
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // io.netty.handler.codec.http2.DefaultHttp2FrameReader.HeadersContinuation
            public int getStreamId() {
                return pushPromiseStreamId;
            }

            @Override // io.netty.handler.codec.http2.DefaultHttp2FrameReader.HeadersContinuation
            public void processFragment(boolean endOfHeaders, ByteBuf fragment, int len, Http2FrameListener listener2) throws Http2Exception {
                headersBlockBuilder().addFragment(fragment, len, ctx.alloc(), endOfHeaders);
                if (endOfHeaders) {
                    listener2.onPushPromiseRead(ctx, pushPromiseStreamId, promisedStreamId, headersBlockBuilder().headers(), padding);
                }
            }
        };
        int len = lengthWithoutTrailingPadding(payload.readableBytes(), padding);
        this.headersContinuation.processFragment(this.flags.endOfHeaders(), payload, len, listener);
        resetHeadersContinuationIfEnd(this.flags.endOfHeaders());
    }

    private void readPingFrame(ChannelHandlerContext ctx, long data, Http2FrameListener listener) throws Http2Exception {
        if (this.flags.ack()) {
            listener.onPingAckRead(ctx, data);
        } else {
            listener.onPingRead(ctx, data);
        }
    }

    private void readGoAwayFrame(ChannelHandlerContext ctx, ByteBuf payload, Http2FrameListener listener) throws Http2Exception {
        int lastStreamId = Http2CodecUtil.readUnsignedInt(payload);
        long errorCode = payload.readUnsignedInt();
        listener.onGoAwayRead(ctx, lastStreamId, errorCode, payload);
    }

    private void readWindowUpdateFrame(ChannelHandlerContext ctx, ByteBuf payload, Http2FrameListener listener) throws Http2Exception {
        int windowSizeIncrement = Http2CodecUtil.readUnsignedInt(payload);
        if (windowSizeIncrement == 0) {
            if (this.streamId == 0) {
                throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Received WINDOW_UPDATE with delta 0 for connection stream", new Object[0]);
            }
            throw Http2Exception.streamError(this.streamId, Http2Error.PROTOCOL_ERROR, "Received WINDOW_UPDATE with delta 0 for stream: %d", Integer.valueOf(this.streamId));
        }
        listener.onWindowUpdateRead(ctx, this.streamId, windowSizeIncrement);
    }

    private void readContinuationFrame(ByteBuf payload, Http2FrameListener listener) throws Http2Exception {
        this.headersContinuation.processFragment(this.flags.endOfHeaders(), payload, this.payloadLength, listener);
        resetHeadersContinuationIfEnd(this.flags.endOfHeaders());
    }

    private void readUnknownFrame(ChannelHandlerContext ctx, ByteBuf payload, Http2FrameListener listener) throws Http2Exception {
        listener.onUnknownFrame(ctx, this.frameType, this.streamId, this.flags, payload);
    }

    private int readPadding(ByteBuf payload) {
        if (!this.flags.paddingPresent()) {
            return 0;
        }
        return payload.readUnsignedByte() + 1;
    }

    private void verifyPadding(int padding) throws Http2Exception {
        int len = lengthWithoutTrailingPadding(this.payloadLength, padding);
        if (len < 0) {
            throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Frame payload too small for padding.", new Object[0]);
        }
    }

    private static int lengthWithoutTrailingPadding(int readableBytes, int padding) {
        return padding == 0 ? readableBytes : readableBytes - (padding - 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public abstract class HeadersContinuation {
        private final HeadersBlockBuilder builder;

        abstract int getStreamId();

        abstract void processFragment(boolean z, ByteBuf byteBuf, int i, Http2FrameListener http2FrameListener) throws Http2Exception;

        private HeadersContinuation() {
            this.builder = new HeadersBlockBuilder();
        }

        final HeadersBlockBuilder headersBlockBuilder() {
            return this.builder;
        }

        final void close() {
            this.builder.close();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public class HeadersBlockBuilder {
        private ByteBuf headerBlock;

        protected HeadersBlockBuilder() {
        }

        private void headerSizeExceeded() throws Http2Exception {
            close();
            Http2CodecUtil.headerListSizeExceeded(DefaultHttp2FrameReader.this.headersDecoder.configuration().maxHeaderListSizeGoAway());
        }

        final void addFragment(ByteBuf fragment, int len, ByteBufAllocator alloc, boolean endOfHeaders) throws Http2Exception {
            if (this.headerBlock == null) {
                if (len > DefaultHttp2FrameReader.this.headersDecoder.configuration().maxHeaderListSizeGoAway()) {
                    headerSizeExceeded();
                }
                if (endOfHeaders) {
                    this.headerBlock = fragment.readRetainedSlice(len);
                    return;
                } else {
                    this.headerBlock = alloc.buffer(len).writeBytes(fragment, len);
                    return;
                }
            }
            if (DefaultHttp2FrameReader.this.headersDecoder.configuration().maxHeaderListSizeGoAway() - len < this.headerBlock.readableBytes()) {
                headerSizeExceeded();
            }
            if (this.headerBlock.isWritable(len)) {
                this.headerBlock.writeBytes(fragment, len);
                return;
            }
            ByteBuf buf = alloc.buffer(this.headerBlock.readableBytes() + len);
            buf.writeBytes(this.headerBlock).writeBytes(fragment, len);
            this.headerBlock.release();
            this.headerBlock = buf;
        }

        Http2Headers headers() throws Http2Exception {
            try {
                return DefaultHttp2FrameReader.this.headersDecoder.decodeHeaders(DefaultHttp2FrameReader.this.streamId, this.headerBlock);
            } finally {
                close();
            }
        }

        void close() {
            if (this.headerBlock != null) {
                this.headerBlock.release();
                this.headerBlock = null;
            }
            DefaultHttp2FrameReader.this.headersContinuation = null;
        }
    }

    private void verifyNotProcessingHeaders() throws Http2Exception {
        if (this.headersContinuation != null) {
            throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Received frame of type %s while processing headers on stream %d.", Byte.valueOf(this.frameType), Integer.valueOf(this.headersContinuation.getStreamId()));
        }
    }

    private void verifyAssociatedWithAStream() throws Http2Exception {
        if (this.streamId == 0) {
            throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Frame of type %s must be associated with a stream.", Byte.valueOf(this.frameType));
        }
    }

    private static void verifyStreamOrConnectionId(int streamId, String argumentName) throws Http2Exception {
        if (streamId < 0) {
            throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "%s must be >= 0", argumentName);
        }
    }
}
