package io.netty.channel.unix;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.util.internal.ObjectUtil;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

/* loaded from: classes4.dex */
public abstract class SocketWritableByteChannel implements WritableByteChannel {
    protected final FileDescriptor fd;

    protected abstract ByteBufAllocator alloc();

    /* JADX INFO: Access modifiers changed from: protected */
    public SocketWritableByteChannel(FileDescriptor fd) {
        this.fd = (FileDescriptor) ObjectUtil.checkNotNull(fd, "fd");
    }

    protected int write(ByteBuffer buf, int pos, int limit) throws IOException {
        return this.fd.write(buf, pos, limit);
    }

    @Override // java.nio.channels.WritableByteChannel
    public final int write(ByteBuffer src) throws IOException {
        int written;
        int position = src.position();
        int limit = src.limit();
        if (src.isDirect()) {
            written = write(src, position, src.limit());
        } else {
            int written2 = limit - position;
            ByteBuf buffer = null;
            try {
                if (written2 == 0) {
                    buffer = Unpooled.EMPTY_BUFFER;
                } else {
                    ByteBufAllocator alloc = alloc();
                    if (alloc.isDirectBufferPooled()) {
                        buffer = alloc.directBuffer(written2);
                    } else {
                        buffer = ByteBufUtil.threadLocalDirectBuffer();
                        if (buffer == null) {
                            buffer = Unpooled.directBuffer(written2);
                        }
                    }
                }
                buffer.writeBytes(src.duplicate());
                ByteBuffer nioBuffer = buffer.internalNioBuffer(buffer.readerIndex(), written2);
                written = write(nioBuffer, nioBuffer.position(), nioBuffer.limit());
            } finally {
                if (buffer != null) {
                    buffer.release();
                }
            }
        }
        if (written > 0) {
            src.position(position + written);
        }
        return written;
    }

    @Override // java.nio.channels.Channel
    public final boolean isOpen() {
        return this.fd.isOpen();
    }

    @Override // java.nio.channels.Channel, java.io.Closeable, java.lang.AutoCloseable
    public final void close() throws IOException {
        this.fd.close();
    }
}
