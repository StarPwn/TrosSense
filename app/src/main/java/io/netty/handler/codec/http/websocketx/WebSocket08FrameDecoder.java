package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.nio.ByteOrder;
import java.util.List;

/* loaded from: classes4.dex */
public class WebSocket08FrameDecoder extends ByteToMessageDecoder implements WebSocketFrameDecoder {
    private static final byte OPCODE_BINARY = 2;
    private static final byte OPCODE_CLOSE = 8;
    private static final byte OPCODE_CONT = 0;
    private static final byte OPCODE_PING = 9;
    private static final byte OPCODE_PONG = 10;
    private static final byte OPCODE_TEXT = 1;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) WebSocket08FrameDecoder.class);
    private final WebSocketDecoderConfig config;
    private int fragmentedFramesCount;
    private boolean frameFinalFlag;
    private boolean frameMasked;
    private int frameOpcode;
    private int framePayloadLen1;
    private long framePayloadLength;
    private int frameRsv;
    private int mask;
    private boolean receivedClosingHandshake;
    private State state;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public enum State {
        READING_FIRST,
        READING_SECOND,
        READING_SIZE,
        MASKING_KEY,
        PAYLOAD,
        CORRUPT
    }

    public WebSocket08FrameDecoder(boolean expectMaskedFrames, boolean allowExtensions, int maxFramePayloadLength) {
        this(expectMaskedFrames, allowExtensions, maxFramePayloadLength, false);
    }

    public WebSocket08FrameDecoder(boolean expectMaskedFrames, boolean allowExtensions, int maxFramePayloadLength, boolean allowMaskMismatch) {
        this(WebSocketDecoderConfig.newBuilder().expectMaskedFrames(expectMaskedFrames).allowExtensions(allowExtensions).maxFramePayloadLength(maxFramePayloadLength).allowMaskMismatch(allowMaskMismatch).build());
    }

    public WebSocket08FrameDecoder(WebSocketDecoderConfig decoderConfig) {
        this.state = State.READING_FIRST;
        this.config = (WebSocketDecoderConfig) ObjectUtil.checkNotNull(decoderConfig, "decoderConfig");
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:7:0x0029. Please report as an issue. */
    @Override // io.netty.handler.codec.ByteToMessageDecoder
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int i;
        if (this.receivedClosingHandshake) {
            in.skipBytes(actualReadableBytes());
            return;
        }
        switch (this.state) {
            case READING_FIRST:
                if (!in.isReadable()) {
                    return;
                }
                this.framePayloadLength = 0L;
                byte b = in.readByte();
                this.frameFinalFlag = (b & 128) != 0;
                this.frameRsv = (b & 112) >> 4;
                this.frameOpcode = b & 15;
                if (logger.isTraceEnabled()) {
                    logger.trace("Decoding WebSocket Frame opCode={}", Integer.valueOf(this.frameOpcode));
                }
                this.state = State.READING_SECOND;
            case READING_SECOND:
                if (!in.isReadable()) {
                    return;
                }
                byte b2 = in.readByte();
                this.frameMasked = (b2 & 128) != 0;
                this.framePayloadLen1 = b2 & Byte.MAX_VALUE;
                if (this.frameRsv != 0 && !this.config.allowExtensions()) {
                    protocolViolation(ctx, in, "RSV != 0 and no extension negotiated, RSV:" + this.frameRsv);
                    return;
                }
                if (!this.config.allowMaskMismatch() && this.config.expectMaskedFrames() != this.frameMasked) {
                    protocolViolation(ctx, in, "received a frame that is not masked as expected");
                    return;
                }
                if (this.frameOpcode > 7) {
                    if (!this.frameFinalFlag) {
                        protocolViolation(ctx, in, "fragmented control frame");
                        return;
                    }
                    if (this.framePayloadLen1 > 125) {
                        protocolViolation(ctx, in, "control frame with payload length > 125 octets");
                        return;
                    }
                    if (this.frameOpcode != 8 && this.frameOpcode != 9 && this.frameOpcode != 10) {
                        protocolViolation(ctx, in, "control frame using reserved opcode " + this.frameOpcode);
                        return;
                    } else if (this.frameOpcode == 8 && this.framePayloadLen1 == 1) {
                        protocolViolation(ctx, in, "received close control frame with payload len 1");
                        return;
                    }
                } else {
                    if (this.frameOpcode != 0 && this.frameOpcode != 1 && this.frameOpcode != 2) {
                        protocolViolation(ctx, in, "data frame using reserved opcode " + this.frameOpcode);
                        return;
                    }
                    if (this.fragmentedFramesCount == 0 && this.frameOpcode == 0) {
                        protocolViolation(ctx, in, "received continuation data frame outside fragmented message");
                        return;
                    } else if (this.fragmentedFramesCount != 0 && this.frameOpcode != 0) {
                        protocolViolation(ctx, in, "received non-continuation data frame while inside fragmented message");
                        return;
                    }
                }
                this.state = State.READING_SIZE;
                break;
            case READING_SIZE:
                if (this.framePayloadLen1 == 126) {
                    if (in.readableBytes() < 2) {
                        return;
                    }
                    this.framePayloadLength = in.readUnsignedShort();
                    if (this.framePayloadLength < 126) {
                        protocolViolation(ctx, in, "invalid data frame length (not using minimal length encoding)");
                        return;
                    }
                } else if (this.framePayloadLen1 != 127) {
                    this.framePayloadLength = this.framePayloadLen1;
                } else {
                    if (in.readableBytes() < 8) {
                        return;
                    }
                    this.framePayloadLength = in.readLong();
                    if (this.framePayloadLength < 0) {
                        protocolViolation(ctx, in, "invalid data frame length (negative length)");
                        return;
                    } else if (this.framePayloadLength < 65536) {
                        protocolViolation(ctx, in, "invalid data frame length (not using minimal length encoding)");
                        return;
                    }
                }
                if (this.framePayloadLength > this.config.maxFramePayloadLength()) {
                    protocolViolation(ctx, in, WebSocketCloseStatus.MESSAGE_TOO_BIG, "Max frame length of " + this.config.maxFramePayloadLength() + " has been exceeded.");
                    return;
                } else {
                    if (logger.isTraceEnabled()) {
                        logger.trace("Decoding WebSocket Frame length={}", Long.valueOf(this.framePayloadLength));
                    }
                    this.state = State.MASKING_KEY;
                }
            case MASKING_KEY:
                if (this.frameMasked) {
                    if (in.readableBytes() < 4) {
                        return;
                    } else {
                        this.mask = in.readInt();
                    }
                }
                this.state = State.PAYLOAD;
            case PAYLOAD:
                if (in.readableBytes() < this.framePayloadLength) {
                    return;
                }
                ByteBuf payloadBuffer = Unpooled.EMPTY_BUFFER;
                try {
                    if (this.framePayloadLength > 0) {
                        payloadBuffer = ByteBufUtil.readBytes(ctx.alloc(), in, toFrameLength(this.framePayloadLength));
                    }
                    this.state = State.READING_FIRST;
                    if (this.frameMasked & (this.framePayloadLength > 0)) {
                        unmask(payloadBuffer);
                    }
                    if (this.frameOpcode == 9) {
                        out.add(new PingWebSocketFrame(this.frameFinalFlag, this.frameRsv, payloadBuffer));
                        payloadBuffer = null;
                        if (payloadBuffer != null) {
                            return;
                        } else {
                            return;
                        }
                    }
                    if (this.frameOpcode == 10) {
                        out.add(new PongWebSocketFrame(this.frameFinalFlag, this.frameRsv, payloadBuffer));
                        ByteBuf payloadBuffer2 = null;
                        if (0 != 0) {
                            payloadBuffer2.release();
                            return;
                        }
                        return;
                    }
                    if (this.frameOpcode == 8) {
                        this.receivedClosingHandshake = true;
                        checkCloseFrameBody(ctx, payloadBuffer);
                        out.add(new CloseWebSocketFrame(this.frameFinalFlag, this.frameRsv, payloadBuffer));
                        ByteBuf payloadBuffer3 = null;
                        if (0 != 0) {
                            payloadBuffer3.release();
                            return;
                        }
                        return;
                    }
                    if (this.frameFinalFlag) {
                        this.fragmentedFramesCount = 0;
                        i = 1;
                    } else {
                        i = 1;
                        this.fragmentedFramesCount++;
                    }
                    if (this.frameOpcode == i) {
                        out.add(new TextWebSocketFrame(this.frameFinalFlag, this.frameRsv, payloadBuffer));
                        ByteBuf payloadBuffer4 = null;
                        if (0 != 0) {
                            payloadBuffer4.release();
                            return;
                        }
                        return;
                    }
                    if (this.frameOpcode == 2) {
                        out.add(new BinaryWebSocketFrame(this.frameFinalFlag, this.frameRsv, payloadBuffer));
                        ByteBuf payloadBuffer5 = null;
                        if (0 != 0) {
                            payloadBuffer5.release();
                            return;
                        }
                        return;
                    }
                    if (this.frameOpcode != 0) {
                        throw new UnsupportedOperationException("Cannot decode web socket frame with opcode: " + this.frameOpcode);
                    }
                    out.add(new ContinuationWebSocketFrame(this.frameFinalFlag, this.frameRsv, payloadBuffer));
                    ByteBuf payloadBuffer6 = null;
                    if (0 != 0) {
                        payloadBuffer6.release();
                        return;
                    }
                    return;
                } finally {
                    if (payloadBuffer != null) {
                        payloadBuffer.release();
                    }
                }
            case CORRUPT:
                if (in.isReadable()) {
                    in.readByte();
                    return;
                }
                return;
            default:
                throw new Error("Shouldn't reach here.");
        }
    }

    private void unmask(ByteBuf frame) {
        int i = frame.readerIndex();
        int end = frame.writerIndex();
        ByteOrder order = frame.order();
        int intMask = this.mask;
        long longMask = intMask & 4294967295L;
        long longMask2 = longMask | (longMask << 32);
        int lim = end - 7;
        while (i < lim) {
            frame.setLong(i, frame.getLong(i) ^ longMask2);
            i += 8;
        }
        int lim2 = end - 3;
        if (i < lim2) {
            frame.setInt(i, frame.getInt(i) ^ ((int) longMask2));
            i += 4;
        }
        if (order == ByteOrder.LITTLE_ENDIAN) {
            intMask = Integer.reverseBytes(intMask);
        }
        int maskOffset = 0;
        while (i < end) {
            frame.setByte(i, WebSocketUtil.byteAtIndex(intMask, maskOffset & 3) ^ frame.getByte(i));
            i++;
            maskOffset++;
        }
    }

    private void protocolViolation(ChannelHandlerContext ctx, ByteBuf in, String reason) {
        protocolViolation(ctx, in, WebSocketCloseStatus.PROTOCOL_ERROR, reason);
    }

    private void protocolViolation(ChannelHandlerContext ctx, ByteBuf in, WebSocketCloseStatus status, String reason) {
        protocolViolation(ctx, in, new CorruptedWebSocketFrameException(status, reason));
    }

    private void protocolViolation(ChannelHandlerContext ctx, ByteBuf in, CorruptedWebSocketFrameException ex) {
        Object closeMessage;
        this.state = State.CORRUPT;
        int readableBytes = in.readableBytes();
        if (readableBytes > 0) {
            in.skipBytes(readableBytes);
        }
        if (!ctx.channel().isActive()) {
            throw ex;
        }
        if (this.config.closeOnProtocolViolation()) {
            if (this.receivedClosingHandshake) {
                closeMessage = Unpooled.EMPTY_BUFFER;
            } else {
                WebSocketCloseStatus closeStatus = ex.closeStatus();
                String reasonText = ex.getMessage();
                if (reasonText == null) {
                    reasonText = closeStatus.reasonText();
                }
                closeMessage = new CloseWebSocketFrame(closeStatus, reasonText);
            }
            ctx.writeAndFlush(closeMessage).addListener((GenericFutureListener<? extends Future<? super Void>>) ChannelFutureListener.CLOSE);
            throw ex;
        }
        throw ex;
    }

    private static int toFrameLength(long l) {
        if (l > 2147483647L) {
            throw new TooLongFrameException("Length:" + l);
        }
        return (int) l;
    }

    protected void checkCloseFrameBody(ChannelHandlerContext ctx, ByteBuf buffer) {
        if (buffer == null || !buffer.isReadable()) {
            return;
        }
        if (buffer.readableBytes() < 2) {
            protocolViolation(ctx, buffer, WebSocketCloseStatus.INVALID_PAYLOAD_DATA, "Invalid close frame body");
        }
        int statusCode = buffer.getShort(buffer.readerIndex());
        if (!WebSocketCloseStatus.isValidStatusCode(statusCode)) {
            protocolViolation(ctx, buffer, "Invalid close frame getStatus code: " + statusCode);
        }
        if (buffer.readableBytes() > 2) {
            try {
                new Utf8Validator().check(buffer, buffer.readerIndex() + 2, buffer.readableBytes() - 2);
            } catch (CorruptedWebSocketFrameException ex) {
                protocolViolation(ctx, buffer, ex);
            }
        }
    }
}
