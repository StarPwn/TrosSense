package io.netty.handler.pcap;

import io.netty.buffer.ByteBuf;

/* loaded from: classes4.dex */
final class UDPPacket {
    private static final short UDP_HEADER_SIZE = 8;

    private UDPPacket() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void writePacket(ByteBuf byteBuf, ByteBuf payload, int srcPort, int dstPort) {
        byteBuf.writeShort(srcPort);
        byteBuf.writeShort(dstPort);
        byteBuf.writeShort(payload.readableBytes() + 8);
        byteBuf.writeShort(1);
        byteBuf.writeBytes(payload);
    }
}
