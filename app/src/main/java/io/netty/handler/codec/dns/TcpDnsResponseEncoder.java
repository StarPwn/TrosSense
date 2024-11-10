package io.netty.handler.codec.dns;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.internal.ObjectUtil;
import java.util.List;

@ChannelHandler.Sharable
/* loaded from: classes4.dex */
public final class TcpDnsResponseEncoder extends MessageToMessageEncoder<DnsResponse> {
    private final DnsRecordEncoder encoder;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageToMessageEncoder
    public /* bridge */ /* synthetic */ void encode(ChannelHandlerContext channelHandlerContext, DnsResponse dnsResponse, List list) throws Exception {
        encode2(channelHandlerContext, dnsResponse, (List<Object>) list);
    }

    public TcpDnsResponseEncoder() {
        this(DnsRecordEncoder.DEFAULT);
    }

    public TcpDnsResponseEncoder(DnsRecordEncoder encoder) {
        this.encoder = (DnsRecordEncoder) ObjectUtil.checkNotNull(encoder, "encoder");
    }

    /* renamed from: encode, reason: avoid collision after fix types in other method */
    protected void encode2(ChannelHandlerContext ctx, DnsResponse response, List<Object> out) throws Exception {
        ByteBuf buf = ctx.alloc().ioBuffer(1024);
        buf.writerIndex(buf.writerIndex() + 2);
        DnsMessageUtil.encodeDnsResponse(this.encoder, response, buf);
        buf.setShort(0, buf.readableBytes() - 2);
        out.add(buf);
    }
}
