package io.netty.handler.codec.dns;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.dns.DnsMessageUtil;
import io.netty.util.internal.ObjectUtil;
import java.util.List;

@ChannelHandler.Sharable
/* loaded from: classes4.dex */
public class DatagramDnsQueryDecoder extends MessageToMessageDecoder<DatagramPacket> {
    private final DnsRecordDecoder recordDecoder;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageToMessageDecoder
    public /* bridge */ /* synthetic */ void decode(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket, List list) throws Exception {
        decode2(channelHandlerContext, datagramPacket, (List<Object>) list);
    }

    public DatagramDnsQueryDecoder() {
        this(DnsRecordDecoder.DEFAULT);
    }

    public DatagramDnsQueryDecoder(DnsRecordDecoder recordDecoder) {
        this.recordDecoder = (DnsRecordDecoder) ObjectUtil.checkNotNull(recordDecoder, "recordDecoder");
    }

    /* renamed from: decode, reason: avoid collision after fix types in other method */
    protected void decode2(ChannelHandlerContext ctx, final DatagramPacket packet, List<Object> out) throws Exception {
        DnsQuery query = DnsMessageUtil.decodeDnsQuery(this.recordDecoder, (ByteBuf) packet.content(), new DnsMessageUtil.DnsQueryFactory() { // from class: io.netty.handler.codec.dns.DatagramDnsQueryDecoder.1
            @Override // io.netty.handler.codec.dns.DnsMessageUtil.DnsQueryFactory
            public DnsQuery newQuery(int id, DnsOpCode dnsOpCode) {
                return new DatagramDnsQuery(packet.sender(), packet.recipient(), id, dnsOpCode);
            }
        });
        out.add(query);
    }
}
