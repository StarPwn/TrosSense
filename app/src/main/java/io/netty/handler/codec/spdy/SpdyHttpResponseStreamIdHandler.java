package io.netty.handler.codec.spdy;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.spdy.SpdyHttpHeaders;
import io.netty.util.ReferenceCountUtil;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

/* loaded from: classes4.dex */
public class SpdyHttpResponseStreamIdHandler extends MessageToMessageCodec<Object, HttpMessage> {
    private static final Integer NO_ID = -1;
    private final Queue<Integer> ids = new ArrayDeque();

    @Override // io.netty.handler.codec.MessageToMessageCodec
    protected /* bridge */ /* synthetic */ void encode(ChannelHandlerContext channelHandlerContext, HttpMessage httpMessage, List list) throws Exception {
        encode2(channelHandlerContext, httpMessage, (List<Object>) list);
    }

    @Override // io.netty.handler.codec.MessageToMessageCodec
    public boolean acceptInboundMessage(Object msg) throws Exception {
        return (msg instanceof HttpMessage) || (msg instanceof SpdyRstStreamFrame);
    }

    /* renamed from: encode, reason: avoid collision after fix types in other method */
    protected void encode2(ChannelHandlerContext ctx, HttpMessage msg, List<Object> out) throws Exception {
        Integer id = this.ids.poll();
        if (id != null && id.intValue() != NO_ID.intValue() && !msg.headers().contains(SpdyHttpHeaders.Names.STREAM_ID)) {
            msg.headers().setInt(SpdyHttpHeaders.Names.STREAM_ID, id.intValue());
        }
        out.add(ReferenceCountUtil.retain(msg));
    }

    @Override // io.netty.handler.codec.MessageToMessageCodec
    protected void decode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
        if (msg instanceof HttpMessage) {
            boolean contains = ((HttpMessage) msg).headers().contains(SpdyHttpHeaders.Names.STREAM_ID);
            if (!contains) {
                this.ids.add(NO_ID);
            } else {
                this.ids.add(((HttpMessage) msg).headers().getInt(SpdyHttpHeaders.Names.STREAM_ID));
            }
        } else {
            boolean contains2 = msg instanceof SpdyRstStreamFrame;
            if (contains2) {
                this.ids.remove(Integer.valueOf(((SpdyRstStreamFrame) msg).streamId()));
            }
        }
        out.add(ReferenceCountUtil.retain(msg));
    }
}
