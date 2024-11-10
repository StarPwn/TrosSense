package org.cloudburstmc.netty.handler.codec.raknet.common;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import java.util.List;
import org.cloudburstmc.netty.channel.raknet.packet.EncapsulatedPacket;
import org.cloudburstmc.netty.channel.raknet.packet.RakDatagramPacket;

/* loaded from: classes5.dex */
public class RakDatagramCodec extends MessageToMessageCodec<ByteBuf, RakDatagramPacket> {
    public static final String NAME = "rak-datagram-codec";

    @Override // io.netty.handler.codec.MessageToMessageCodec
    protected /* bridge */ /* synthetic */ void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List list) throws Exception {
        decode2(channelHandlerContext, byteBuf, (List<Object>) list);
    }

    @Override // io.netty.handler.codec.MessageToMessageCodec
    protected /* bridge */ /* synthetic */ void encode(ChannelHandlerContext channelHandlerContext, RakDatagramPacket rakDatagramPacket, List list) throws Exception {
        encode2(channelHandlerContext, rakDatagramPacket, (List<Object>) list);
    }

    /* renamed from: encode, reason: avoid collision after fix types in other method */
    protected void encode2(ChannelHandlerContext ctx, RakDatagramPacket packet, List<Object> out) throws Exception {
        ByteBuf header = ctx.alloc().ioBuffer(4);
        header.writeByte(packet.getFlags());
        header.writeMediumLE(packet.getSequenceIndex());
        CompositeByteBuf buf = ctx.alloc().compositeBuffer((packet.getPackets().size() * 2) + 1);
        buf.addComponent(true, header);
        for (EncapsulatedPacket encapsulated : packet.getPackets()) {
            encapsulated.encode(buf);
        }
        out.add(buf);
    }

    /* renamed from: decode, reason: avoid collision after fix types in other method */
    protected void decode2(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> list) throws Exception {
        byte potentialFlags = buffer.getByte(buffer.readerIndex());
        if ((potentialFlags & Byte.MIN_VALUE) == 0) {
            list.add(buffer.retain());
            return;
        }
        if ((potentialFlags & 64) != 0 || (potentialFlags & 32) != 0) {
            list.add(buffer.retain());
            return;
        }
        RakDatagramPacket packet = RakDatagramPacket.newInstance();
        try {
            packet.setFlags(buffer.readByte());
            packet.setSequenceIndex(buffer.readUnsignedMediumLE());
            while (buffer.isReadable()) {
                EncapsulatedPacket encapsulated = EncapsulatedPacket.newInstance();
                try {
                    encapsulated.decode(buffer);
                    packet.getPackets().add(encapsulated.retain());
                    encapsulated.release();
                } catch (Throwable th) {
                    encapsulated.release();
                    throw th;
                }
            }
            list.add(packet.retain());
        } finally {
            packet.release();
        }
    }
}
