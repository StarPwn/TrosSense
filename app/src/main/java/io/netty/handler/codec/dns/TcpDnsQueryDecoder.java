package io.netty.handler.codec.dns;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.dns.DnsMessageUtil;
import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public final class TcpDnsQueryDecoder extends LengthFieldBasedFrameDecoder {
    private final DnsRecordDecoder decoder;

    public TcpDnsQueryDecoder() {
        this(DnsRecordDecoder.DEFAULT, 65535);
    }

    public TcpDnsQueryDecoder(DnsRecordDecoder decoder, int maxFrameLength) {
        super(maxFrameLength, 0, 2, 0, 2);
        this.decoder = (DnsRecordDecoder) ObjectUtil.checkNotNull(decoder, "decoder");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.LengthFieldBasedFrameDecoder
    public Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        if (frame == null) {
            return null;
        }
        return DnsMessageUtil.decodeDnsQuery(this.decoder, frame.slice(), new DnsMessageUtil.DnsQueryFactory() { // from class: io.netty.handler.codec.dns.TcpDnsQueryDecoder.1
            @Override // io.netty.handler.codec.dns.DnsMessageUtil.DnsQueryFactory
            public DnsQuery newQuery(int id, DnsOpCode dnsOpCode) {
                return new DefaultDnsQuery(id, dnsOpCode);
            }
        });
    }
}
