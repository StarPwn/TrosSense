package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.nio.ByteOrder;
import java.util.List;

/* loaded from: classes4.dex */
public class WebSocket08FrameEncoder extends MessageToMessageEncoder<WebSocketFrame> implements WebSocketFrameEncoder {
    private static final int GATHERING_WRITE_THRESHOLD = 1024;
    private static final byte OPCODE_BINARY = 2;
    private static final byte OPCODE_CLOSE = 8;
    private static final byte OPCODE_CONT = 0;
    private static final byte OPCODE_PING = 9;
    private static final byte OPCODE_PONG = 10;
    private static final byte OPCODE_TEXT = 1;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) WebSocket08FrameEncoder.class);
    private final boolean maskPayload;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageToMessageEncoder
    public /* bridge */ /* synthetic */ void encode(ChannelHandlerContext channelHandlerContext, WebSocketFrame webSocketFrame, List list) throws Exception {
        encode2(channelHandlerContext, webSocketFrame, (List<Object>) list);
    }

    public WebSocket08FrameEncoder(boolean maskPayload) {
        this.maskPayload = maskPayload;
    }

    /* renamed from: encode, reason: avoid collision after fix types in other method */
    protected void encode2(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out) throws Exception {
        byte opcode;
        ByteBuf data = msg.content();
        if (msg instanceof TextWebSocketFrame) {
            opcode = 1;
        } else if (msg instanceof PingWebSocketFrame) {
            opcode = 9;
        } else if (msg instanceof PongWebSocketFrame) {
            opcode = 10;
        } else if (msg instanceof CloseWebSocketFrame) {
            opcode = 8;
        } else if (msg instanceof BinaryWebSocketFrame) {
            opcode = 2;
        } else if (msg instanceof ContinuationWebSocketFrame) {
            opcode = 0;
        } else {
            throw new UnsupportedOperationException("Cannot encode frame of type: " + msg.getClass().getName());
        }
        int length = data.readableBytes();
        if (logger.isTraceEnabled()) {
            logger.trace("Encoding WebSocket Frame opCode={} length={}", Byte.valueOf(opcode), Integer.valueOf(length));
        }
        int b0 = 0;
        if (msg.isFinalFragment()) {
            b0 = 0 | 128;
        }
        int b02 = (opcode % 128) | b0 | ((msg.rsv() % 8) << 4);
        if (opcode == 9 && length > 125) {
            throw new TooLongFrameException("invalid payload for PING (payload length must be <= 125, was " + length);
        }
        ByteBuf buf = null;
        try {
            int maskLength = this.maskPayload ? 4 : 0;
            try {
                if (length <= 125) {
                    buf = ctx.alloc().buffer(maskLength + 2 + length);
                    buf.writeByte(b02);
                    byte b = (byte) (this.maskPayload ? ((byte) length) | 128 : (byte) length);
                    buf.writeByte(b);
                } else {
                    int i = 255;
                    if (length <= 65535) {
                        int size = maskLength + 4;
                        if (this.maskPayload || length <= 1024) {
                            size += length;
                        }
                        buf = ctx.alloc().buffer(size);
                        buf.writeByte(b02);
                        buf.writeByte(this.maskPayload ? 254 : 126);
                        buf.writeByte(255 & (length >>> 8));
                        buf.writeByte(length & 255);
                    } else {
                        int size2 = maskLength + 10;
                        if (this.maskPayload) {
                            size2 += length;
                        }
                        buf = ctx.alloc().buffer(size2);
                        buf.writeByte(b02);
                        if (!this.maskPayload) {
                            i = 127;
                        }
                        buf.writeByte(i);
                        buf.writeLong(length);
                    }
                }
                try {
                    if (this.maskPayload) {
                        int mask = PlatformDependent.threadLocalRandom().nextInt(Integer.MAX_VALUE);
                        buf.writeInt(mask);
                        if (data.isReadable()) {
                            ByteOrder srcOrder = data.order();
                            ByteOrder dstOrder = buf.order();
                            int i2 = data.readerIndex();
                            int end = data.writerIndex();
                            if (srcOrder == dstOrder) {
                                long longMask = mask & 4294967295L;
                                long longMask2 = longMask | (longMask << 32);
                                if (srcOrder == ByteOrder.LITTLE_ENDIAN) {
                                    longMask2 = Long.reverseBytes(longMask2);
                                }
                                int lim = end - 7;
                                while (i2 < lim) {
                                    byte opcode2 = opcode;
                                    int length2 = length;
                                    buf.writeLong(data.getLong(i2) ^ longMask2);
                                    i2 += 8;
                                    opcode = opcode2;
                                    length = length2;
                                }
                                int lim2 = end - 3;
                                if (i2 < lim2) {
                                    buf.writeInt(data.getInt(i2) ^ ((int) longMask2));
                                    i2 += 4;
                                }
                            }
                            int maskOffset = 0;
                            while (i2 < end) {
                                byte byteData = data.getByte(i2);
                                buf.writeByte(WebSocketUtil.byteAtIndex(mask, maskOffset & 3) ^ byteData);
                                i2++;
                                maskOffset++;
                            }
                        }
                        out.add(buf);
                    } else if (buf.writableBytes() >= data.readableBytes()) {
                        buf.writeBytes(data);
                        out.add(buf);
                    } else {
                        out.add(buf);
                        out.add(data.retain());
                    }
                    if (0 != 0 && buf != null) {
                        buf.release();
                    }
                } catch (Throwable th) {
                    th = th;
                    if (1 != 0 && buf != null) {
                        buf.release();
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Throwable th3) {
            th = th3;
        }
    }
}
