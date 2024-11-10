package io.netty.handler.pcap;

import io.netty.buffer.ByteBuf;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

/* loaded from: classes4.dex */
final class PcapWriter implements Closeable {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) PcapWriter.class);
    private final OutputStream outputStream;
    private final PcapWriteHandler pcapWriteHandler;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PcapWriter(PcapWriteHandler pcapWriteHandler) throws IOException {
        this.pcapWriteHandler = pcapWriteHandler;
        this.outputStream = pcapWriteHandler.outputStream();
        if (!pcapWriteHandler.sharedOutputStream()) {
            PcapHeaders.writeGlobalHeader(pcapWriteHandler.outputStream());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void writePacket(ByteBuf packetHeaderBuf, ByteBuf packet) throws IOException {
        if (this.pcapWriteHandler.state() == State.CLOSED) {
            logger.debug("Pcap Write attempted on closed PcapWriter");
        }
        long timestamp = System.currentTimeMillis();
        PcapHeaders.writePacketHeader(packetHeaderBuf, (int) (timestamp / 1000), (int) ((timestamp % 1000) * 1000), packet.readableBytes(), packet.readableBytes());
        if (this.pcapWriteHandler.sharedOutputStream()) {
            synchronized (this.outputStream) {
                packetHeaderBuf.readBytes(this.outputStream, packetHeaderBuf.readableBytes());
                packet.readBytes(this.outputStream, packet.readableBytes());
            }
            return;
        }
        packetHeaderBuf.readBytes(this.outputStream, packetHeaderBuf.readableBytes());
        packet.readBytes(this.outputStream, packet.readableBytes());
    }

    public String toString() {
        return "PcapWriter{outputStream=" + this.outputStream + '}';
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.pcapWriteHandler.state() == State.CLOSED) {
            logger.debug("PcapWriter is already closed");
            return;
        }
        if (this.pcapWriteHandler.sharedOutputStream()) {
            synchronized (this.outputStream) {
                this.outputStream.flush();
            }
        } else {
            this.outputStream.flush();
            this.outputStream.close();
        }
        this.pcapWriteHandler.markClosed();
        logger.debug("PcapWriter is now closed");
    }
}
