package io.netty.handler.codec.dns;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

@ChannelHandler.Sharable
/* loaded from: classes4.dex */
public final class TcpDnsQueryEncoder extends MessageToByteEncoder<DnsQuery> {
    private final DnsQueryEncoder encoder;

    public TcpDnsQueryEncoder() {
        this(DnsRecordEncoder.DEFAULT);
    }

    public TcpDnsQueryEncoder(DnsRecordEncoder recordEncoder) {
        this.encoder = new DnsQueryEncoder(recordEncoder);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageToByteEncoder
    public void encode(ChannelHandlerContext ctx, DnsQuery msg, ByteBuf out) throws Exception {
        out.writerIndex(out.writerIndex() + 2);
        this.encoder.encode(msg, out);
        out.setShort(0, out.readableBytes() - 2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageToByteEncoder
    public ByteBuf allocateBuffer(ChannelHandlerContext ctx, DnsQuery msg, boolean preferDirect) {
        return preferDirect ? ctx.alloc().ioBuffer(1024) : ctx.alloc().heapBuffer(1024);
    }
}
