package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.internal.ObjectUtil;
import java.nio.ByteOrder;
import java.util.Set;

/* loaded from: classes4.dex */
public class SpdyFrameEncoder {
    private final int version;

    public SpdyFrameEncoder(SpdyVersion spdyVersion) {
        this.version = ((SpdyVersion) ObjectUtil.checkNotNull(spdyVersion, "spdyVersion")).getVersion();
    }

    private void writeControlFrameHeader(ByteBuf buffer, int type, byte flags, int length) {
        buffer.writeShort(this.version | 32768);
        buffer.writeShort(type);
        buffer.writeByte(flags);
        buffer.writeMedium(length);
    }

    public ByteBuf encodeDataFrame(ByteBufAllocator byteBufAllocator, int i, boolean z, ByteBuf byteBuf) {
        int readableBytes = byteBuf.readableBytes();
        ByteBuf order = byteBufAllocator.ioBuffer(readableBytes + 8).order(ByteOrder.BIG_ENDIAN);
        order.writeInt(Integer.MAX_VALUE & i);
        order.writeByte(z ? 1 : 0);
        order.writeMedium(readableBytes);
        order.writeBytes(byteBuf, byteBuf.readerIndex(), readableBytes);
        return order;
    }

    public ByteBuf encodeSynStreamFrame(ByteBufAllocator byteBufAllocator, int i, int i2, byte b, boolean z, boolean z2, ByteBuf byteBuf) {
        int readableBytes = byteBuf.readableBytes();
        byte b2 = z ? 1 : 0;
        if (z2) {
            b2 = (byte) (b2 | 2);
        }
        int i3 = readableBytes + 10;
        ByteBuf order = byteBufAllocator.ioBuffer(i3 + 8).order(ByteOrder.BIG_ENDIAN);
        writeControlFrameHeader(order, 1, b2, i3);
        order.writeInt(i);
        order.writeInt(i2);
        order.writeShort((b & 255) << 13);
        order.writeBytes(byteBuf, byteBuf.readerIndex(), readableBytes);
        return order;
    }

    public ByteBuf encodeSynReplyFrame(ByteBufAllocator byteBufAllocator, int i, boolean z, ByteBuf byteBuf) {
        int readableBytes = byteBuf.readableBytes();
        int i2 = readableBytes + 4;
        ByteBuf order = byteBufAllocator.ioBuffer(i2 + 8).order(ByteOrder.BIG_ENDIAN);
        writeControlFrameHeader(order, 2, z ? (byte) 1 : (byte) 0, i2);
        order.writeInt(i);
        order.writeBytes(byteBuf, byteBuf.readerIndex(), readableBytes);
        return order;
    }

    public ByteBuf encodeRstStreamFrame(ByteBufAllocator allocator, int streamId, int statusCode) {
        ByteBuf frame = allocator.ioBuffer(8 + 8).order(ByteOrder.BIG_ENDIAN);
        writeControlFrameHeader(frame, 3, (byte) 0, 8);
        frame.writeInt(streamId);
        frame.writeInt(statusCode);
        return frame;
    }

    public ByteBuf encodeSettingsFrame(ByteBufAllocator byteBufAllocator, SpdySettingsFrame spdySettingsFrame) {
        Set<Integer> ids = spdySettingsFrame.ids();
        int size = ids.size();
        boolean clearPreviouslyPersistedSettings = spdySettingsFrame.clearPreviouslyPersistedSettings();
        int i = (size * 8) + 4;
        ByteBuf order = byteBufAllocator.ioBuffer(i + 8).order(ByteOrder.BIG_ENDIAN);
        writeControlFrameHeader(order, 4, clearPreviouslyPersistedSettings ? (byte) 1 : (byte) 0, i);
        order.writeInt(size);
        for (Integer num : ids) {
            byte b = 0;
            if (spdySettingsFrame.isPersistValue(num.intValue())) {
                b = (byte) (0 | 1);
            }
            if (spdySettingsFrame.isPersisted(num.intValue())) {
                b = (byte) (b | 2);
            }
            order.writeByte(b);
            order.writeMedium(num.intValue());
            order.writeInt(spdySettingsFrame.getValue(num.intValue()));
        }
        return order;
    }

    public ByteBuf encodePingFrame(ByteBufAllocator allocator, int id) {
        ByteBuf frame = allocator.ioBuffer(4 + 8).order(ByteOrder.BIG_ENDIAN);
        writeControlFrameHeader(frame, 6, (byte) 0, 4);
        frame.writeInt(id);
        return frame;
    }

    public ByteBuf encodeGoAwayFrame(ByteBufAllocator allocator, int lastGoodStreamId, int statusCode) {
        ByteBuf frame = allocator.ioBuffer(8 + 8).order(ByteOrder.BIG_ENDIAN);
        writeControlFrameHeader(frame, 7, (byte) 0, 8);
        frame.writeInt(lastGoodStreamId);
        frame.writeInt(statusCode);
        return frame;
    }

    public ByteBuf encodeHeadersFrame(ByteBufAllocator byteBufAllocator, int i, boolean z, ByteBuf byteBuf) {
        int readableBytes = byteBuf.readableBytes();
        int i2 = readableBytes + 4;
        ByteBuf order = byteBufAllocator.ioBuffer(i2 + 8).order(ByteOrder.BIG_ENDIAN);
        writeControlFrameHeader(order, 8, z ? (byte) 1 : (byte) 0, i2);
        order.writeInt(i);
        order.writeBytes(byteBuf, byteBuf.readerIndex(), readableBytes);
        return order;
    }

    public ByteBuf encodeWindowUpdateFrame(ByteBufAllocator allocator, int streamId, int deltaWindowSize) {
        ByteBuf frame = allocator.ioBuffer(8 + 8).order(ByteOrder.BIG_ENDIAN);
        writeControlFrameHeader(frame, 9, (byte) 0, 8);
        frame.writeInt(streamId);
        frame.writeInt(deltaWindowSize);
        return frame;
    }
}
