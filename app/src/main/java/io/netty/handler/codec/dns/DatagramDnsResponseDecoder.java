package io.netty.handler.codec.dns;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.net.InetSocketAddress;
import java.util.List;

@ChannelHandler.Sharable
/* loaded from: classes4.dex */
public class DatagramDnsResponseDecoder extends MessageToMessageDecoder<DatagramPacket> {
    private final DnsResponseDecoder<InetSocketAddress> responseDecoder;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageToMessageDecoder
    public /* bridge */ /* synthetic */ void decode(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket, List list) throws Exception {
        decode2(channelHandlerContext, datagramPacket, (List<Object>) list);
    }

    public DatagramDnsResponseDecoder() {
        this(DnsRecordDecoder.DEFAULT);
    }

    public DatagramDnsResponseDecoder(DnsRecordDecoder recordDecoder) {
        this.responseDecoder = new DnsResponseDecoder<InetSocketAddress>(recordDecoder) { // from class: io.netty.handler.codec.dns.DatagramDnsResponseDecoder.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // io.netty.handler.codec.dns.DnsResponseDecoder
            public DnsResponse newResponse(InetSocketAddress sender, InetSocketAddress recipient, int id, DnsOpCode opCode, DnsResponseCode responseCode) {
                return new DatagramDnsResponse(sender, recipient, id, opCode, responseCode);
            }
        };
    }

    /* renamed from: decode, reason: avoid collision after fix types in other method */
    protected void decode2(ChannelHandlerContext ctx, DatagramPacket packet, List<Object> out) throws Exception {
        try {
            out.add(decodeResponse(ctx, packet));
        } catch (IndexOutOfBoundsException e) {
            throw new CorruptedFrameException("Unable to decode response", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public DnsResponse decodeResponse(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        return this.responseDecoder.decode(packet.sender(), packet.recipient(), (ByteBuf) packet.content());
    }
}
