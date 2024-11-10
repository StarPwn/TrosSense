package io.netty.handler.pcap;

import io.netty.buffer.ByteBuf;

/* loaded from: classes4.dex */
final class EthernetPacket {
    private static final int V4 = 2048;
    private static final int V6 = 34525;
    private static final byte[] DUMMY_SOURCE_MAC_ADDRESS = {0, 0, 94, 0, 83, 0};
    private static final byte[] DUMMY_DESTINATION_MAC_ADDRESS = {0, 0, 94, 0, 83, -1};

    private EthernetPacket() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void writeIPv4(ByteBuf byteBuf, ByteBuf payload) {
        writePacket(byteBuf, payload, DUMMY_SOURCE_MAC_ADDRESS, DUMMY_DESTINATION_MAC_ADDRESS, 2048);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void writeIPv6(ByteBuf byteBuf, ByteBuf payload) {
        writePacket(byteBuf, payload, DUMMY_SOURCE_MAC_ADDRESS, DUMMY_DESTINATION_MAC_ADDRESS, V6);
    }

    private static void writePacket(ByteBuf byteBuf, ByteBuf payload, byte[] srcAddress, byte[] dstAddress, int type) {
        byteBuf.writeBytes(dstAddress);
        byteBuf.writeBytes(srcAddress);
        byteBuf.writeShort(type);
        byteBuf.writeBytes(payload);
    }
}
