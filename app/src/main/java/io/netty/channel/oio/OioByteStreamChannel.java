package io.netty.channel.oio;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.FileRegion;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.util.internal.ObjectUtil;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.WritableByteChannel;

@Deprecated
/* loaded from: classes4.dex */
public abstract class OioByteStreamChannel extends AbstractOioByteChannel {
    private static final InputStream CLOSED_IN = new InputStream() { // from class: io.netty.channel.oio.OioByteStreamChannel.1
        @Override // java.io.InputStream
        public int read() {
            return -1;
        }
    };
    private static final OutputStream CLOSED_OUT = new OutputStream() { // from class: io.netty.channel.oio.OioByteStreamChannel.2
        @Override // java.io.OutputStream
        public void write(int b) throws IOException {
            throw new ClosedChannelException();
        }
    };
    private InputStream is;
    private OutputStream os;
    private WritableByteChannel outChannel;

    /* JADX INFO: Access modifiers changed from: protected */
    public OioByteStreamChannel(Channel parent) {
        super(parent);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void activate(InputStream is, OutputStream os) {
        if (this.is != null) {
            throw new IllegalStateException("input was set already");
        }
        if (this.os != null) {
            throw new IllegalStateException("output was set already");
        }
        this.is = (InputStream) ObjectUtil.checkNotNull(is, "is");
        this.os = (OutputStream) ObjectUtil.checkNotNull(os, "os");
        if (this.readWhenInactive) {
            eventLoop().execute(this.readTask);
            this.readWhenInactive = false;
        }
    }

    @Override // io.netty.channel.Channel
    public boolean isActive() {
        OutputStream os;
        InputStream is = this.is;
        return (is == null || is == CLOSED_IN || (os = this.os) == null || os == CLOSED_OUT) ? false : true;
    }

    @Override // io.netty.channel.oio.AbstractOioByteChannel
    protected int available() {
        try {
            return this.is.available();
        } catch (IOException e) {
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.oio.AbstractOioByteChannel
    public int doReadBytes(ByteBuf buf) throws Exception {
        RecvByteBufAllocator.Handle allocHandle = unsafe().recvBufAllocHandle();
        allocHandle.attemptedBytesRead(Math.max(1, Math.min(available(), buf.maxWritableBytes())));
        return buf.writeBytes(this.is, allocHandle.attemptedBytesRead());
    }

    @Override // io.netty.channel.oio.AbstractOioByteChannel
    protected void doWriteBytes(ByteBuf buf) throws Exception {
        OutputStream os = this.os;
        if (os == null) {
            throw new NotYetConnectedException();
        }
        buf.readBytes(os, buf.readableBytes());
    }

    @Override // io.netty.channel.oio.AbstractOioByteChannel
    protected void doWriteFileRegion(FileRegion region) throws Exception {
        OutputStream os = this.os;
        if (os == null) {
            throw new NotYetConnectedException();
        }
        if (this.outChannel == null) {
            this.outChannel = Channels.newChannel(os);
        }
        long written = 0;
        do {
            long localWritten = region.transferTo(this.outChannel, written);
            if (localWritten == -1) {
                checkEOF(region);
                return;
            }
            written += localWritten;
        } while (written < region.count());
    }

    private static void checkEOF(FileRegion region) throws IOException {
        if (region.transferred() < region.count()) {
            throw new EOFException("Expected to be able to write " + region.count() + " bytes, but only wrote " + region.transferred());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.AbstractChannel
    public void doClose() throws Exception {
        InputStream is = this.is;
        OutputStream os = this.os;
        this.is = CLOSED_IN;
        this.os = CLOSED_OUT;
        if (is != null) {
            try {
                is.close();
            } finally {
                if (os != null) {
                    os.close();
                }
            }
        }
    }
}
