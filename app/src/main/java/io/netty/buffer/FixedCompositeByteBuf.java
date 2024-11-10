package io.netty.buffer;

import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.RecyclableArrayList;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ReadOnlyBufferException;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.util.Collections;
import kotlin.UShort;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class FixedCompositeByteBuf extends AbstractReferenceCountedByteBuf {
    private static final ByteBuf[] EMPTY = {Unpooled.EMPTY_BUFFER};
    private final ByteBufAllocator allocator;
    private final ByteBuf[] buffers;
    private final int capacity;
    private final boolean direct;
    private final int nioBufferCount;
    private final ByteOrder order;

    /* JADX INFO: Access modifiers changed from: package-private */
    public FixedCompositeByteBuf(ByteBufAllocator allocator, ByteBuf... buffers) {
        super(Integer.MAX_VALUE);
        if (buffers.length == 0) {
            this.buffers = EMPTY;
            this.order = ByteOrder.BIG_ENDIAN;
            this.nioBufferCount = 1;
            this.capacity = 0;
            this.direct = Unpooled.EMPTY_BUFFER.isDirect();
        } else {
            ByteBuf b = buffers[0];
            this.buffers = buffers;
            boolean direct = true;
            int nioBufferCount = b.nioBufferCount();
            int capacity = b.readableBytes();
            this.order = b.order();
            for (int i = 1; i < buffers.length; i++) {
                ByteBuf b2 = buffers[i];
                if (buffers[i].order() != this.order) {
                    throw new IllegalArgumentException("All ByteBufs need to have same ByteOrder");
                }
                nioBufferCount += b2.nioBufferCount();
                capacity += b2.readableBytes();
                if (!b2.isDirect()) {
                    direct = false;
                }
            }
            this.nioBufferCount = nioBufferCount;
            this.capacity = capacity;
            this.direct = direct;
        }
        setIndex(0, capacity());
        this.allocator = allocator;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public boolean isWritable() {
        return false;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public boolean isWritable(int size) {
        return false;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf discardReadBytes() {
        throw new ReadOnlyBufferException();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length) {
        throw new ReadOnlyBufferException();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {
        throw new ReadOnlyBufferException();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setBytes(int index, ByteBuffer src) {
        throw new ReadOnlyBufferException();
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf setByte(int index, int value) {
        throw new ReadOnlyBufferException();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractByteBuf
    public void _setByte(int index, int value) {
        throw new ReadOnlyBufferException();
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf setShort(int index, int value) {
        throw new ReadOnlyBufferException();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractByteBuf
    public void _setShort(int index, int value) {
        throw new ReadOnlyBufferException();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractByteBuf
    public void _setShortLE(int index, int value) {
        throw new ReadOnlyBufferException();
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf setMedium(int index, int value) {
        throw new ReadOnlyBufferException();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractByteBuf
    public void _setMedium(int index, int value) {
        throw new ReadOnlyBufferException();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractByteBuf
    public void _setMediumLE(int index, int value) {
        throw new ReadOnlyBufferException();
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf setInt(int index, int value) {
        throw new ReadOnlyBufferException();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractByteBuf
    public void _setInt(int index, int value) {
        throw new ReadOnlyBufferException();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractByteBuf
    public void _setIntLE(int index, int value) {
        throw new ReadOnlyBufferException();
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf setLong(int index, long value) {
        throw new ReadOnlyBufferException();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractByteBuf
    public void _setLong(int index, long value) {
        throw new ReadOnlyBufferException();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractByteBuf
    public void _setLongLE(int index, long value) {
        throw new ReadOnlyBufferException();
    }

    @Override // io.netty.buffer.ByteBuf
    public int setBytes(int index, InputStream in, int length) {
        throw new ReadOnlyBufferException();
    }

    @Override // io.netty.buffer.ByteBuf
    public int setBytes(int index, ScatteringByteChannel in, int length) {
        throw new ReadOnlyBufferException();
    }

    @Override // io.netty.buffer.ByteBuf
    public int setBytes(int index, FileChannel in, long position, int length) {
        throw new ReadOnlyBufferException();
    }

    @Override // io.netty.buffer.ByteBuf
    public int capacity() {
        return this.capacity;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public int maxCapacity() {
        return this.capacity;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf capacity(int newCapacity) {
        throw new ReadOnlyBufferException();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBufAllocator alloc() {
        return this.allocator;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteOrder order() {
        return this.order;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf unwrap() {
        return null;
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean isDirect() {
        return this.direct;
    }

    private Component findComponent(int index) {
        int readable = 0;
        for (int i = 0; i < this.buffers.length; i++) {
            Component comp = null;
            ByteBuf b = this.buffers[i];
            if (b instanceof Component) {
                comp = (Component) b;
                b = comp.buf;
            }
            readable += b.readableBytes();
            if (index < readable) {
                if (comp == null) {
                    Component comp2 = new Component(i, readable - b.readableBytes(), b);
                    this.buffers[i] = comp2;
                    return comp2;
                }
                return comp;
            }
        }
        throw new IllegalStateException();
    }

    private ByteBuf buffer(int i) {
        ByteBuf b = this.buffers[i];
        return b instanceof Component ? ((Component) b).buf : b;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public byte getByte(int index) {
        return _getByte(index);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractByteBuf
    public byte _getByte(int index) {
        Component c = findComponent(index);
        return c.buf.getByte(index - c.offset);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractByteBuf
    public short _getShort(int index) {
        Component c = findComponent(index);
        if (index + 2 > c.endOffset) {
            if (order() == ByteOrder.BIG_ENDIAN) {
                return (short) (((_getByte(index) & 255) << 8) | (_getByte(index + 1) & 255));
            }
            return (short) ((_getByte(index) & 255) | ((_getByte(index + 1) & 255) << 8));
        }
        return c.buf.getShort(index - c.offset);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractByteBuf
    public short _getShortLE(int index) {
        Component c = findComponent(index);
        if (index + 2 > c.endOffset) {
            if (order() == ByteOrder.BIG_ENDIAN) {
                return (short) ((_getByte(index) & 255) | ((_getByte(index + 1) & 255) << 8));
            }
            return (short) (((_getByte(index) & 255) << 8) | (_getByte(index + 1) & 255));
        }
        return c.buf.getShortLE(index - c.offset);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractByteBuf
    public int _getUnsignedMedium(int index) {
        Component c = findComponent(index);
        if (index + 3 > c.endOffset) {
            if (order() == ByteOrder.BIG_ENDIAN) {
                return ((_getShort(index) & UShort.MAX_VALUE) << 8) | (_getByte(index + 2) & 255);
            }
            return (_getShort(index) & UShort.MAX_VALUE) | ((_getByte(index + 2) & 255) << 16);
        }
        return c.buf.getUnsignedMedium(index - c.offset);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractByteBuf
    public int _getUnsignedMediumLE(int index) {
        Component c = findComponent(index);
        if (index + 3 > c.endOffset) {
            if (order() == ByteOrder.BIG_ENDIAN) {
                return (_getShortLE(index) & UShort.MAX_VALUE) | ((_getByte(index + 2) & 255) << 16);
            }
            return ((_getShortLE(index) & UShort.MAX_VALUE) << 8) | (_getByte(index + 2) & 255);
        }
        return c.buf.getUnsignedMediumLE(index - c.offset);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractByteBuf
    public int _getInt(int index) {
        Component c = findComponent(index);
        if (index + 4 > c.endOffset) {
            if (order() == ByteOrder.BIG_ENDIAN) {
                return ((_getShort(index) & UShort.MAX_VALUE) << 16) | (_getShort(index + 2) & UShort.MAX_VALUE);
            }
            return (_getShort(index) & UShort.MAX_VALUE) | ((_getShort(index + 2) & UShort.MAX_VALUE) << 16);
        }
        return c.buf.getInt(index - c.offset);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractByteBuf
    public int _getIntLE(int index) {
        Component c = findComponent(index);
        if (index + 4 > c.endOffset) {
            if (order() == ByteOrder.BIG_ENDIAN) {
                return (_getShortLE(index) & UShort.MAX_VALUE) | ((_getShortLE(index + 2) & UShort.MAX_VALUE) << 16);
            }
            return ((_getShortLE(index) & UShort.MAX_VALUE) << 16) | (_getShortLE(index + 2) & UShort.MAX_VALUE);
        }
        return c.buf.getIntLE(index - c.offset);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractByteBuf
    public long _getLong(int index) {
        Component c = findComponent(index);
        if (index + 8 > c.endOffset) {
            if (order() == ByteOrder.BIG_ENDIAN) {
                return ((_getInt(index) & 4294967295L) << 32) | (_getInt(index + 4) & 4294967295L);
            }
            return (_getInt(index) & 4294967295L) | ((4294967295L & _getInt(index + 4)) << 32);
        }
        return c.buf.getLong(index - c.offset);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractByteBuf
    public long _getLongLE(int index) {
        Component c = findComponent(index);
        if (index + 8 > c.endOffset) {
            if (order() == ByteOrder.BIG_ENDIAN) {
                return (_getIntLE(index) & 4294967295L) | ((4294967295L & _getIntLE(index + 4)) << 32);
            }
            return ((_getIntLE(index) & 4294967295L) << 32) | (_getIntLE(index + 4) & 4294967295L);
        }
        return c.buf.getLongLE(index - c.offset);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
        checkDstIndex(index, length, dstIndex, dst.length);
        if (length == 0) {
            return this;
        }
        Component c = findComponent(index);
        int i = c.index;
        int adjustment = c.offset;
        ByteBuf s = c.buf;
        while (true) {
            int localLength = Math.min(length, s.readableBytes() - (index - adjustment));
            s.getBytes(index - adjustment, dst, dstIndex, localLength);
            index += localLength;
            dstIndex += localLength;
            length -= localLength;
            adjustment += s.readableBytes();
            if (length > 0) {
                i++;
                s = buffer(i);
            } else {
                return this;
            }
        }
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf getBytes(int index, ByteBuffer dst) {
        int limit = dst.limit();
        int length = dst.remaining();
        checkIndex(index, length);
        if (length == 0) {
            return this;
        }
        try {
            Component c = findComponent(index);
            int i = c.index;
            int adjustment = c.offset;
            ByteBuf s = c.buf;
            while (true) {
                int localLength = Math.min(length, s.readableBytes() - (index - adjustment));
                dst.limit(dst.position() + localLength);
                s.getBytes(index - adjustment, dst);
                index += localLength;
                length -= localLength;
                adjustment += s.readableBytes();
                if (length > 0) {
                    i++;
                    s = buffer(i);
                } else {
                    return this;
                }
            }
        } finally {
            dst.limit(limit);
        }
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {
        checkDstIndex(index, length, dstIndex, dst.capacity());
        if (length == 0) {
            return this;
        }
        Component c = findComponent(index);
        int i = c.index;
        int adjustment = c.offset;
        ByteBuf s = c.buf;
        while (true) {
            int localLength = Math.min(length, s.readableBytes() - (index - adjustment));
            s.getBytes(index - adjustment, dst, dstIndex, localLength);
            index += localLength;
            dstIndex += localLength;
            length -= localLength;
            adjustment += s.readableBytes();
            if (length > 0) {
                i++;
                s = buffer(i);
            } else {
                return this;
            }
        }
    }

    @Override // io.netty.buffer.ByteBuf
    public int getBytes(int index, GatheringByteChannel out, int length) throws IOException {
        int count = nioBufferCount();
        if (count == 1) {
            return out.write(internalNioBuffer(index, length));
        }
        long writtenBytes = out.write(nioBuffers(index, length));
        if (writtenBytes > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int) writtenBytes;
    }

    @Override // io.netty.buffer.ByteBuf
    public int getBytes(int index, FileChannel out, long position, int length) throws IOException {
        int count = nioBufferCount();
        if (count == 1) {
            return out.write(internalNioBuffer(index, length), position);
        }
        long writtenBytes = 0;
        for (ByteBuffer buf : nioBuffers(index, length)) {
            writtenBytes += out.write(buf, position + writtenBytes);
        }
        if (writtenBytes > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int) writtenBytes;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf getBytes(int index, OutputStream out, int length) throws IOException {
        checkIndex(index, length);
        if (length == 0) {
            return this;
        }
        Component c = findComponent(index);
        int i = c.index;
        int adjustment = c.offset;
        ByteBuf s = c.buf;
        while (true) {
            int localLength = Math.min(length, s.readableBytes() - (index - adjustment));
            s.getBytes(index - adjustment, out, localLength);
            index += localLength;
            length -= localLength;
            adjustment += s.readableBytes();
            if (length > 0) {
                i++;
                s = buffer(i);
            } else {
                return this;
            }
        }
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf copy(int index, int length) {
        checkIndex(index, length);
        boolean release = true;
        ByteBuf buf = alloc().buffer(length);
        try {
            buf.writeBytes(this, index, length);
            release = false;
            return buf;
        } finally {
            if (release) {
                buf.release();
            }
        }
    }

    @Override // io.netty.buffer.ByteBuf
    public int nioBufferCount() {
        return this.nioBufferCount;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuffer nioBuffer(int index, int length) {
        checkIndex(index, length);
        if (this.buffers.length == 1) {
            ByteBuf buf = buffer(0);
            if (buf.nioBufferCount() == 1) {
                return buf.nioBuffer(index, length);
            }
        }
        ByteBuffer merged = ByteBuffer.allocate(length).order(order());
        ByteBuffer[] buffers = nioBuffers(index, length);
        for (ByteBuffer byteBuffer : buffers) {
            merged.put(byteBuffer);
        }
        merged.flip();
        return merged;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuffer internalNioBuffer(int index, int length) {
        if (this.buffers.length == 1) {
            return buffer(0).internalNioBuffer(index, length);
        }
        throw new UnsupportedOperationException();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuffer[] nioBuffers(int index, int length) {
        checkIndex(index, length);
        if (length == 0) {
            return EmptyArrays.EMPTY_BYTE_BUFFERS;
        }
        RecyclableArrayList array = RecyclableArrayList.newInstance(this.buffers.length);
        try {
            Component c = findComponent(index);
            int i = c.index;
            int adjustment = c.offset;
            ByteBuf s = c.buf;
            while (true) {
                int localLength = Math.min(length, s.readableBytes() - (index - adjustment));
                switch (s.nioBufferCount()) {
                    case 0:
                        throw new UnsupportedOperationException();
                    case 1:
                        array.add(s.nioBuffer(index - adjustment, localLength));
                        break;
                    default:
                        Collections.addAll(array, s.nioBuffers(index - adjustment, localLength));
                        break;
                }
                index += localLength;
                length -= localLength;
                adjustment += s.readableBytes();
                if (length > 0) {
                    i++;
                    s = buffer(i);
                } else {
                    return (ByteBuffer[]) array.toArray(EmptyArrays.EMPTY_BYTE_BUFFERS);
                }
            }
        } finally {
            array.recycle();
        }
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean hasArray() {
        switch (this.buffers.length) {
            case 0:
                return true;
            case 1:
                return buffer(0).hasArray();
            default:
                return false;
        }
    }

    @Override // io.netty.buffer.ByteBuf
    public byte[] array() {
        switch (this.buffers.length) {
            case 0:
                return EmptyArrays.EMPTY_BYTES;
            case 1:
                return buffer(0).array();
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override // io.netty.buffer.ByteBuf
    public int arrayOffset() {
        switch (this.buffers.length) {
            case 0:
                return 0;
            case 1:
                return buffer(0).arrayOffset();
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean hasMemoryAddress() {
        switch (this.buffers.length) {
            case 0:
                return Unpooled.EMPTY_BUFFER.hasMemoryAddress();
            case 1:
                return buffer(0).hasMemoryAddress();
            default:
                return false;
        }
    }

    @Override // io.netty.buffer.ByteBuf
    public long memoryAddress() {
        switch (this.buffers.length) {
            case 0:
                return Unpooled.EMPTY_BUFFER.memoryAddress();
            case 1:
                return buffer(0).memoryAddress();
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override // io.netty.buffer.AbstractReferenceCountedByteBuf
    protected void deallocate() {
        for (int i = 0; i < this.buffers.length; i++) {
            buffer(i).release();
        }
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public String toString() {
        String result = super.toString();
        return result.substring(0, result.length() - 1) + ", components=" + this.buffers.length + ')';
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class Component extends WrappedByteBuf {
        private final int endOffset;
        private final int index;
        private final int offset;

        Component(int index, int offset, ByteBuf buf) {
            super(buf);
            this.index = index;
            this.offset = offset;
            this.endOffset = buf.readableBytes() + offset;
        }
    }
}
